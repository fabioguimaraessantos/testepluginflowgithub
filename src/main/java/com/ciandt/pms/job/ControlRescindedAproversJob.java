package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.util.JobUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by pricaldeira on 25/10/2021.
 */
public class ControlRescindedAproversJob {

    private Logger logger = LoggerFactory.getLogger(ControlRescindedAproversJob.class);

    @Autowired
    private JobUtil jobUtil;

    /** Utilitario para envio de email. */
    @Autowired
    private MailSenderUtil mailSender;

    @Autowired
    private Properties systemProperties;

    @Autowired
    private IContratoPraticaService contratoPraticaService;

    @Autowired
    private IGrupoCustoService grupoCustoService;

    @Autowired
    private IParametroService parametroService;

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

    public void sendEmailControlRescindedAprovers() throws Exception {

        Parametro controllershipEmails = parametroService.findParametroByNomeParametro(Constants.CONTROLLERSHIP_EMAILS_PARAMETER);

        if (!jobIsActive(controllershipEmails)) {
            return;
        }

        logger.info("Job sendEmailControlRescindedAprovers() iniciado em " + new Date());

        Map<String, List<String>> userRescindedMap = getCLobAndCostCenterWithRescinded();

        if (userRescindedMap.isEmpty()) {
            return;
        }

        String emailTo = controllershipEmails.getTextoParametro();
        String subject = getmailSubject();

        StringBuilder bodyCostCenterMaps = new StringBuilder();

        List<String> costCenterCLobList;
        for (Map.Entry<String, List<String>> userRescindedItem : userRescindedMap.entrySet()) {
            costCenterCLobList = userRescindedItem.getValue();
            bodyCostCenterMaps.append(getmailTitleTextCostCenter());
            for(String costCenterCLobItem : costCenterCLobList) {
                bodyCostCenterMaps.append(getmailBodyTextCostCenter(costCenterCLobItem));
            }
            bodyCostCenterMaps.append(getmailBodyPrincipalTextAprover(userRescindedItem.getKey()));
        }
        emailSenderGeneral(subject,bodyCostCenterMaps,emailTo);
    }

    private Map<String, List<String>> getCLobAndCostCenterWithRescinded() {
        Map<String, List<String>> userRescindedMap = new HashMap<>();
        ArrayList<String> costCenterCLobSet;

        List<ContratoPratica> contratoPraticaList = contratoPraticaService.findByAproverRescinded();
        for(ContratoPratica contratoPratica : contratoPraticaList) {
            if (!userRescindedMap.containsKey(contratoPratica.getAprovador().getCodigoLogin()) || !userRescindedMap.containsKey(contratoPratica.getGerenteAprovador().getCodigoLogin())) {
                costCenterCLobSet = new ArrayList<>();
                costCenterCLobSet.add(contratoPratica.getNomeContratoPratica());

                if (!userRescindedMap.containsKey(contratoPratica.getAprovador().getCodigoLogin())) {
                    userRescindedMap.put(contratoPratica.getAprovador().getCodigoLogin(), costCenterCLobSet);
                } else {
                    userRescindedMap.put(contratoPratica.getGerenteAprovador().getCodigoLogin(), costCenterCLobSet);
                }
            } else {
                if (contratoPratica.getAprovador().getDataRescisao() != null) {
                    userRescindedMap.get(contratoPratica.getAprovador().getCodigoLogin()).add(contratoPratica.getNomeContratoPratica());
                }else if (contratoPratica.getGerenteAprovador().getDataRescisao() != null) {
                    userRescindedMap.get(contratoPratica.getGerenteAprovador().getCodigoLogin()).add(contratoPratica.getNomeContratoPratica());
                }
            }
        }

        List<GrupoCusto> grupoCustoList = grupoCustoService.findByAproverRescinded();
        for(GrupoCusto grupoCusto : grupoCustoList) {
            if (!userRescindedMap.containsKey(grupoCusto.getAprovador().getCodigoLogin()) || !userRescindedMap.containsKey(grupoCusto.getGerenteAprovador().getCodigoLogin())) {
                costCenterCLobSet = new ArrayList<>();
                costCenterCLobSet.add(grupoCusto.getNomeGrupoCusto());

                if (!userRescindedMap.containsKey(grupoCusto.getAprovador().getCodigoLogin())) {
                    userRescindedMap.put(grupoCusto.getAprovador().getCodigoLogin(), costCenterCLobSet);
                } else {
                    userRescindedMap.put(grupoCusto.getGerenteAprovador().getCodigoLogin(), costCenterCLobSet);
                }
            } else {
                if(grupoCusto.getAprovador().getDataRescisao() != null) {
                    userRescindedMap.get(grupoCusto.getAprovador().getCodigoLogin()).add(grupoCusto.getNomeGrupoCusto());
                }else if(grupoCusto.getGerenteAprovador().getDataRescisao() != null){
                    userRescindedMap.get(grupoCusto.getGerenteAprovador().getCodigoLogin()).add(grupoCusto.getNomeGrupoCusto());
                }
            }
        }

        return userRescindedMap;
    }

    private void emailSenderGeneral(String subject,StringBuilder bodyCostCenterMaps, String emailTo) {
       mailSender.sendHtmlMail(emailTo, subject, bodyCostCenterMaps.toString());
    }

    private boolean jobIsActive(Parametro controllershipEmails) {

        if (!jobUtil.isJobActive(Constants.JOB_CONTROL_RESCINDED_APROVERS_ACTIVE)) {
            logger.warn("JOB is not active on config.properties. Set the "
                    .concat(Constants.JOB_CONTROL_RESCINDED_APROVERS_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return false;
        }

        if(controllershipEmails == null || controllershipEmails.getTextoParametro() == null){
            logger.warn("JOB - Controllership email not configured. Parameter: CONTROLLERSHIP_EMAILS");
            return false;
        }

        return true;
    }


    private String getmailSubject() {
        return BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_CONTROL_RESCINDED_APROVERS);
    }

    private String getmailTitleTextCostCenter() {
        return BundleUtil.getBundle(Constants.EMAIL_MSG_CONTROL_RESCINDED_APROVERS_COST_CENTER);
    }

    private String getmailBodyTextCostCenter(String nameCostCenter) {
        return BundleUtil.getBundle(Constants.EMAIL_MSG_ITENS_CONTROL_RESCINDED_COST_CENTER,nameCostCenter);
    }


    private String getmailBodyPrincipalTextAprover(String aproverLogin) {
        return BundleUtil.getBundle(Constants.EMAIL_MSG_CONTROL_RESCINDED_APROVERS_APROVER,aproverLogin);
    }

}
