package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.JobUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by josef on 18/09/2017.
 */
public class ContratoPraticaJob {

    private Logger logger = LoggerFactory.getLogger(ContratoPraticaJob.class);

    /** Utilitario para envio de email. */
    @Autowired
    private MailSenderUtil mailSender;

    @Autowired
    private JobUtil jobUtil;

    @Autowired
    private IContratoPraticaService contratoPraticaService;

    @Autowired
    private IPessoaService pessoaService;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;


    /**
     * @return the mailSender
     */
    public MailSenderUtil getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender
     *            the mailSender to set
     */
    public void setMailSender(final MailSenderUtil mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMailContratoPraticaSemDealFiscal() throws Exception{
        try {
            //verificar se job esta ativo
            if (!jobUtil.isJobActive(Constants.JOB_CONTRATO_PRATICA_DEAL_FISCAL_ACTIVE)) {
                logger.warn("JOB", "Job is not active on config.properties. Set the "
                        .concat(Constants.JOB_CONTRATO_PRATICA_DEAL_FISCAL_ACTIVE)
                        .concat(" property to 'true' for active it."));
                return;
            }

            //Definicao: enviar a partir 3 dias antes do fechamento de receita(ultimo dia util do mes)
            if (this.canSendEmail()) {
                //consultar gerentes e relacao de Clobs que serao enviado email.
                List<String> managers = contratoPraticaService.findManagerOfContratoPratricaWithouDealFiscal();

                for(String manager : managers){
                    List<Map<String,String>> listClob = contratoPraticaService.findContratoPraticaWithoutDealFiscal(manager);

                    this.sendMail(listClob, manager);
                }
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private void sendMail(List<Map<String,String>> listContratoPratica, String loginManager){
        SimpleDateFormat fullMonthFormat = new SimpleDateFormat("MMMM/yyyy");
        String subject = this.getSubjectText(fullMonthFormat.format(new Date()));

        String messagePrincipal = this.getMessagePrincipalText();
        String messageItens = "";
        for (Map<String, String> item : listContratoPratica){
            for(Map.Entry<String, String> entry : item.entrySet()){
                messageItens += this.getMessageItensText(entry.getKey(),entry.getValue());
            }
        }
        messagePrincipal = messagePrincipal.concat(messageItens);

        Pessoa pessoa = pessoaService.findPessoaByLogin(loginManager);

        String emailTo = pessoa.getTextoEmail() + "," + systemProperties.getProperty("mail.address.controladoria.to");

        mailSender.sendHtmlMail(emailTo, subject, messagePrincipal);
    }

    private String getSubjectText(String monthYearDesc){
        return BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_CONTRATO_PRATICA_DEAL_FISCAL,
                monthYearDesc);
    }

    private String getMessagePrincipalText(){
        return BundleUtil.getBundle(Constants.EMAIL_MSG_PRINCIPAL_CONTRATO_PRATICA_DEAL_FISCAL);
    }

    private String getMessageItensText(String clob, String fte){
        return BundleUtil.getBundle(Constants.EMAIL_MSG_ITENS_CONTRATO_PRATICA_DEAL_FISCAL, clob, fte);
    }

    private Boolean canSendEmail() throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        Date ultimoDiaMes = df.parse(new SimpleDateFormat("yyyyMMdd").format(DateUtil.getDateLastDayOfMonth(new Date())));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(ultimoDiaMes);

        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd");
        formatter.setLenient(false);
        //Date hoje = formatter.parse("20171127"); //TESTE
        Date hoje = formatter.parse(new SimpleDateFormat("yyyyMMdd").format(new Date()));


        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            //Se hoje for maior ou igual 3 dias antes pode enviar email
            if((hoje.after(DateUtil.sumDays(-3L, ultimoDiaMes))|| hoje.equals(DateUtil.sumDays(-3L, ultimoDiaMes))) && hoje.before(ultimoDiaMes)){ //OK
                //Sendmail
                logger.warn("INFO","Ultimo dia do MES:" + ultimoDiaMes.toString() + "Caiu em: " + Calendar.SATURDAY);
                logger.warn("INFO", "Enviando email na data: " + hoje.toString());

                return true;
            }
            //Enviar email quarta,quinta e sexta
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            if(hoje.equals(DateUtil.sumDays(-4L, ultimoDiaMes)) || hoje.equals(DateUtil.sumDays(-3L, ultimoDiaMes)) || hoje.equals(DateUtil.sumDays(-2L, ultimoDiaMes))) {
                logger.warn("INFO", "Ultimo dia do MES:" + ultimoDiaMes.toString() + "Caiu em: " + Calendar.SUNDAY);
                logger.warn("INFO", "Enviando email na data: " + hoje.toString());
                return true;
            }

        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            if(hoje.equals(DateUtil.sumDays(-4L, ultimoDiaMes)) || hoje.equals(DateUtil.sumDays(-3L, ultimoDiaMes)) || ultimoDiaMes.equals(hoje)) {
                logger.warn("INFO", "Ultimo dia do MES:" + ultimoDiaMes.toString() + "Caiu em: " + Calendar.MONDAY);
                logger.warn("INFO", "Enviando email na data: " + hoje.toString());
                return true;
            }

        }else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {

            if(hoje.equals(DateUtil.sumDays(-4L,ultimoDiaMes)) || hoje.equals(DateUtil.sumDays(-1L,ultimoDiaMes)) || hoje.equals(ultimoDiaMes)) {
                logger.warn("INFO", "Ultimo dia do MES:" + ultimoDiaMes.toString() + "Caiu em: " + Calendar.TUESDAY);
                logger.warn("INFO", "Enviando email na data: " + hoje.toString());
                return true;
            }
        }if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            if(hoje.equals(DateUtil.sumDays(-2L,ultimoDiaMes)) || hoje.equals(DateUtil.sumDays(-1L,ultimoDiaMes)) || hoje.equals(ultimoDiaMes)) {
                logger.warn("INFO", "Ultimo dia do MES:" + ultimoDiaMes.toString() + "Caiu em: QUARTA,QUINTA ou SEXTA");
                logger.warn("INFO", "Enviando email na data: " + hoje.toString());
                return true;
            }

        }
        return false;
    }
}
