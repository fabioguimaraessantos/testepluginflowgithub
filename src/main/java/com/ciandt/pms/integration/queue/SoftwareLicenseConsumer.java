package com.ciandt.pms.integration.queue;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ILicencaSwProjetoParcelaService;
import com.ciandt.pms.business.service.IRateioLicencaSwService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
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
public class SoftwareLicenseConsumer {
    private final Logger logger = LoggerFactory.getLogger(SoftwareLicenseConsumer.class);

    /**
     * Arquivo de configuracoes.
     */
    @Autowired
    private Properties systemProperties;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    private IRateioLicencaSwService rateioLicencaSwService;

    @Autowired
    private ILicencaSwProjetoParcelaService licencaSwProjetoParcelaService;

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

            Connection connection = null;

            try {
                connection = factory.newConnection();
            } catch(Exception e) {
                logger.error(Constants.RABBITMQ_LOG_CONNECTION_FAIL_LAYOUT);
                logger.error(e.getMessage());
            }

            Channel channel = connection.createChannel();
            channel.queueDeclare(systemProperties.getProperty(Constants.RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_OUTGOING_QUEUE_NAME), true, false, false, null);

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
                    logger.info("Software License message received {}", message);
                    try {
                        JsonParser parser = new JsonParser();
                        JsonObject obj = parser.parse(message).getAsJsonObject();

                        if (obj.isJsonObject()) {
                            String code = "";
                            String type = "";
                            String status = "";
                            String errorMessage = "";
                            BigDecimal megaOrderID = new BigDecimal(0);

                            if (obj.getAsJsonObject().has("megaCode")) {
                                megaOrderID = obj.getAsJsonObject().get("megaCode").getAsBigDecimal();
                            }

                            if (obj.getAsJsonObject().has("softwareLicenseCode")) {
                                code = obj.getAsJsonObject().get("softwareLicenseCode").getAsString();
                            }

                            if (obj.getAsJsonObject().has("type")) {
                                type = obj.getAsJsonObject().get("type").getAsString();
                            }

                            if (obj.getAsJsonObject().has("status")) {
                                status = obj.getAsJsonObject().get("status").getAsString();
                            }

                            if (obj.getAsJsonObject().has("errorMessage")) {
                                errorMessage = obj.getAsJsonObject().get("errorMessage").getAsString();
                            }

                            if (code != "") {
                                if (type.equals(Constants.LICENSE_SW_TYPE_USER)) {
                                    rateioLicencaSwService.updateSoftwareLicenseUserFromERP(code, status, megaOrderID, errorMessage);
                                    logger.info("User License {} updated with success with status {} and Mega order ID {} ({}).",
                                            code, status, megaOrderID, errorMessage);
                                } else if (type.equals(Constants.LICENSE_SW_TYPE_PROJECT)) {
                                    licencaSwProjetoParcelaService.updateSoftwareLicenseProjectFromERP(code, status, megaOrderID, errorMessage);
                                    logger.info("Project License {} updated with success with status {} and Mega order ID {} ({}).",
                                            code, status, megaOrderID, errorMessage);
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("======================================================");
                        logger.error("An error occurred when processing software license message");
                        logger.error(e.getMessage(), e);
                        logger.error("======================================================");
                        exceptionProducer.send(message, e,
                                Constants.RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_OUTGOING_NAME_EXCEPTION);
                    }

                }
            };
            String createdChannelTag = channel.basicConsume(
                    systemProperties.getProperty(Constants.RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_OUTGOING_QUEUE_NAME),
                    true,
                    ConsumerTagGenerator.generate(),
                    consumer);
            logger.info("SoftwareLicenseConsumer channel created with success {}", createdChannelTag);
        } catch (RuntimeException e) {
            mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle("_nls.licenca_software_integracao.mail.exception.subject"), e.getMessage());
            System.out.println("Error on Software License Consumer" + e);
            logger.error(Constants.RABBITMQ_LOG_CONNECTION_FAIL_LAYOUT);
            logger.error(e.getMessage());
        }
    }
}
