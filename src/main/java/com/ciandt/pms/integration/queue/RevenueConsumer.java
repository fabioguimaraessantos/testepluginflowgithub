package com.ciandt.pms.integration.queue;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaLicenca;
import com.ciandt.pms.model.ReceitaPlantao;
import com.ciandt.pms.util.MailSenderUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Service
public class RevenueConsumer {

    private final Logger logger = LoggerFactory.getLogger(RevenueConsumer.class);

    /**
     * Arquivo de configuracoes.
     */
    @Autowired
    private Properties systemProperties;

    @Autowired
    private IReceitaService receitaService;

    @Autowired
    private IReceitaDealFiscalService receitaDealFiscalService;

    @Autowired
    private IReceitaMoedaService receitaMoedaService;

    @Autowired
    private IReceitaLicencaService receitaLicencaService;

    @Autowired
    private IReceitaPlantaoService receitaPlantaoService;

    @Autowired
    private IItemFaturaService itemFaturaService;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    ConsumerExceptionProducer exceptionProducer;

    @PostConstruct
    public void get() throws TimeoutException, IOException {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(systemProperties.getProperty(Constants.RABBITMQ_HOST));
            factory.setUsername(systemProperties.getProperty(Constants.RABBITMQ_USERNAME));
            factory.setPassword(systemProperties.getProperty(Constants.RABBITMQ_PASSWORD));
            factory.setPort(Integer.valueOf(systemProperties.getProperty(Constants.RABBITMQ_PORT)));
            factory.setAutomaticRecoveryEnabled(Boolean.TRUE);

            Connection connection = null;

            try {
                connection = factory.newConnection();
            } catch(Exception e) {
                logger.error(Constants.RABBITMQ_LOG_CONNECTION_FAIL_LAYOUT);
                logger.error(e.getMessage());
            }

            Channel channel = connection.createChannel();
            channel.queueDeclare(systemProperties.getProperty(Constants.RABBITMQ_REVENUE_EXCHANGE_OUTGOING_QUEUE_NAME), true, false, false, null);

            connection.addShutdownListener(new ShutdownListener() {
                public void shutdownCompleted(ShutdownSignalException cause) {
                    logger.info("======================================================");
                    logger.info(cause.getMessage());
                    logger.info("======================================================");
                }
            });

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);
                    logger.info("Revenue message received {}", message);
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject obj = parser.parse(message).getAsJsonObject();

                        if (obj.isJsonObject()) {

                            Long revenueCode = 0L;
                            String revenueStatus = "";
                            String errorMessage = "";
                            BigDecimal megaOrderID = new BigDecimal(0);
                            if (obj.getAsJsonObject().has("megaCode")) {
                                megaOrderID = obj.getAsJsonObject().get("megaCode").getAsBigDecimal();
                            }

                            if (obj.getAsJsonObject().has("revenueCode")) {
                                revenueCode = obj.getAsJsonObject().get("revenueCode").getAsLong();
                            }

                            if (obj.getAsJsonObject().has("revenueStatus")) {
                                revenueStatus = obj.getAsJsonObject().get("revenueStatus").getAsString();
                            }

                            if (obj.getAsJsonObject().has("errorMessage")) {
                                errorMessage = obj.getAsJsonObject().get("errorMessage").getAsString();
                            }

                            ReceitaDealFiscal receitaDealFiscal = receitaDealFiscalService.findReceitaDealById(revenueCode);
                            ReceitaLicenca receitaLicenca = receitaLicencaService.findById(revenueCode);
                            ReceitaPlantao receitaPlantao = receitaPlantaoService.findById(revenueCode);

                            if (receitaLicenca != null) {
                                receitaLicencaService.updateLicenseRevenueFromMega(revenueCode, revenueStatus, megaOrderID, errorMessage);
                                logger.info("License Revenue {} updated with success with status {} and Mega order ID {} ({}).",
                                        revenueCode, revenueStatus, megaOrderID, errorMessage);
                            } else if (receitaPlantao != null) {
                                receitaPlantaoService.updateDutyHourRevenueFromMega(receitaPlantao, revenueStatus, megaOrderID, errorMessage);
                                logger.info("Duty Hours Revenue {} updated with success with status {} and Mega order ID {} ({}).",
                                        revenueCode, revenueStatus, megaOrderID, errorMessage);
                            } else if (receitaDealFiscal != null) {
                                receitaService.updateServiceRevenueFromMega(receitaDealFiscal, revenueStatus, megaOrderID, errorMessage);
                                receitaService.updateRevenueStatus(receitaDealFiscal.getCodigoReceitaDfiscal());
                                logger.info("Revenue {} updated with success with status {} and Mega order ID {} ({}).",
                                        revenueCode, revenueStatus, megaOrderID, errorMessage);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("======================================================");
                        logger.error("An error occurred when processing revenue message");
                        logger.error(e.getMessage(), e);
                        logger.error("======================================================");
                        exceptionProducer.send(message, e,
                                Constants.RABBITMQ_REVENUE_EXCHANGE_OUTGOING_NAME_EXCEPTION);
                    }
                }
            };
            String createdChannelTag = channel.basicConsume(
                    systemProperties.getProperty(Constants.RABBITMQ_REVENUE_EXCHANGE_OUTGOING_QUEUE_NAME),
                    true,
                    ConsumerTagGenerator.generate(),
                    consumer);
            logger.info("RevenueConsumer channel created with success {}", createdChannelTag);
        } catch (RuntimeException e) {
            mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle("_nls.receita_integracao.mail.exception.subject"), e.getMessage());
            System.out.println("Error on Revenue Consumer" + e);
            logger.error(Constants.RABBITMQ_LOG_CONNECTION_FAIL_LAYOUT);
            logger.error(e.getMessage());
        }
    }
}
