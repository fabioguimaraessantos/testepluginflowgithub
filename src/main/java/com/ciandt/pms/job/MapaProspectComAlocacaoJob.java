package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.MapaProspectComAlocacaoResultsetVO;
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
 * Created by vnogueira on 13/04/18.
 */
public class MapaProspectComAlocacaoJob {

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

    public void sendMailMapaProspectComAlocacao() throws Exception {

        // Valida se o Job esta ativo nos config.properties
        if (!jobUtil.isJobActive(Constants.JOB_MAPA_PROSPECT_COM_ALOCACAO_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_CONTRATO_PRATICA_DEAL_FISCAL_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

        if (this.canSendEmail()) {
            logger.info("INFO", "Job sendMailMapaProspectComAlocacao() iniciado em " + new Date());

            List<MapaProspectComAlocacaoResultsetVO> resultMaps = contratoPraticaService.findProspectMapWithAllocation();

            Map<String, List<String>> mapsByManager = this.getProspectMapsByManager(resultMaps);
            
            for (Map.Entry<String, List<String>> mapItem : mapsByManager.entrySet()) {
                this.sendMailToManagers(mapItem.getKey(), mapItem.getValue());
            }

            this.sendMailToControllership(resultMaps);
        }
    }

    private Map<String, List<String>> getProspectMapsByManager(List<MapaProspectComAlocacaoResultsetVO> resultMaps) {

        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();

        for (MapaProspectComAlocacaoResultsetVO map : resultMaps) {
            List<String> mapsByManagerApprover = resultMap.get(map.getLoginManagerApprover());

            if (mapsByManagerApprover != null) {
                if (!mapsByManagerApprover.contains(map.getNomeMapaAlocacao())) {
                    mapsByManagerApprover.add(map.getNomeMapaAlocacao());
                }
            } else {
                List<String> allocationMaps = new ArrayList<String>();
                allocationMaps.add(map.getNomeMapaAlocacao());
                resultMap.put(map.getLoginManagerApprover(), allocationMaps);
            }

            List<String> mapsByBusinessManager = resultMap.get(map.getLoginBusinessManager());

            if (mapsByBusinessManager != null) {
                if (!mapsByBusinessManager.contains(map.getNomeMapaAlocacao())) {
                    mapsByBusinessManager.add(map.getNomeMapaAlocacao());
                }
            } else {
                List<String> allocationMaps = new ArrayList<String>();
                allocationMaps.add(map.getNomeMapaAlocacao());
                resultMap.put(map.getLoginBusinessManager(), allocationMaps);
            }
        }

        return resultMap;
    }

    private void sendMailToManagers(String loginManager, List<String> mapList) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM/yyyy");
        String subject = this.getmailSubject(sdf.format(new Date()));

        StringBuilder bodyMaps = new StringBuilder();
        for (String map : mapList) {
            bodyMaps.append(this.getBodyItemText(map));
        }

        String mailBody = this.getManagersBodyPrincipalText(loginManager, bodyMaps.toString());

        Pessoa pessoa = pessoaService.findPessoaByLogin(loginManager);

        if (null == pessoa.getTextoEmail() || "".equalsIgnoreCase(pessoa.getTextoEmail())) {
            logger.warn("WARN", "E-Mail address not found for login " + loginManager + ".");
        } else {
            mailSender.sendHtmlMail(pessoa.getTextoEmail(), subject, mailBody.toString());
        }
    }

    private void sendMailToControllership(List<MapaProspectComAlocacaoResultsetVO> resultMaps) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM/yyyy");
        String subject = this.getmailSubject(sdf.format(new Date()));

        StringBuilder bodyMaps = new StringBuilder();
        for (MapaProspectComAlocacaoResultsetVO map : resultMaps) {
            bodyMaps.append(this.getBodyItemText(map.getNomeMapaAlocacao()));
        }

        String mailBody = this.getControllershipBodyPrincipalText(bodyMaps.toString());

        String mailTo = this.getControllershipMailAddress();

        mailSender.sendHtmlMail(mailTo, subject, mailBody.toString());
    }

    private String getControllershipMailAddress() {
        return systemProperties.getProperty(Constants.EMAIL_CONTROLLERSHIP_ALIAS_TO);
    }

    private String getBodyItemText(String map) {
        return BundleUtil.getBundle(Constants.EMAIL_MSG_ITENS_MAPA_PROSPECT_COM_ALOCACAO, map);
    }

    private String getManagersBodyPrincipalText(String loginManager, String allocationMaps) {
        return BundleUtil.getBundle(Constants.EMAIL_MGR_MSG_PRINCIPAL_MAPA_PROSPECT_COM_ALOCACAO, loginManager, allocationMaps);
    }

    private String getControllershipBodyPrincipalText(String allocationMaps) {
        return BundleUtil.getBundle(Constants.EMAIL_CTRL_MSG_PRINCIPAL_MAPA_PROSPECT_COM_ALOCACAO, allocationMaps);
    }

    private String getmailSubject(String format) {
        return BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_MAPA_PROSPECT_COM_ALOCACAO, format);
    }

    private boolean canSendEmail() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        Date ultimoDiaMes = df.parse(df.format(DateUtil.getDateLastDayOfMonth(new Date())));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(ultimoDiaMes);

        Date date = new Date();
        Calendar today = Calendar.getInstance();
        today.setTime(date);
        Calendar hojeCalendar = new GregorianCalendar(Locale.ENGLISH);

        // Simula a data de hoje para fins de teste
         hojeCalendar.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);

        if (hojeCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || hojeCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return false;
        } else {
            int qtdDiasUteis = 0;
            while (qtdDiasUteis < 5) {

                if ((hojeCalendar.get(Calendar.YEAR) <= calendar.get(Calendar.YEAR)
                        && hojeCalendar.get(Calendar.MONTH) <= calendar.get(Calendar.MONTH)
                        && hojeCalendar.get(Calendar.DAY_OF_MONTH) <= calendar.get(Calendar.DAY_OF_MONTH))
                        && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                        && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    qtdDiasUteis += 1;
                } else if ((hojeCalendar.get(Calendar.YEAR) >= calendar.get(Calendar.YEAR)
                        && hojeCalendar.get(Calendar.MONTH) >= calendar.get(Calendar.MONTH)
                        && hojeCalendar.get(Calendar.DAY_OF_MONTH) >= calendar.get(Calendar.DAY_OF_MONTH))) {
                    return false;
                }

                if ((hojeCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                        && hojeCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                        && hojeCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))) {
                    logger.warn("INFO","Ultimo dia do MES:" + ultimoDiaMes.toString() + "Caiu em: " + Calendar.SATURDAY);
                    logger.warn("INFO", "Enviando email na data: " + hojeCalendar.toString());
                    return true;
                }
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            return false;
        }
    }
}
