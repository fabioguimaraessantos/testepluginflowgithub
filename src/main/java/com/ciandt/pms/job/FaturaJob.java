package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.util.JobUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Properties;


/**
 * 
 * A classe FaturaJob proporciona as funcionalidades 
 * de envio de email periodicamente.
 *
 * @since 12/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class FaturaJob {

    private Logger logger = LoggerFactory.getLogger(FaturaJob.class);

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** Instancia do serviço FaturaService. */
    @Autowired
    private IFaturaService faturaService;

    @Autowired
    private JobUtil jobUtil;
    
    /**
     * Metodo que envia email diariamente.
     */
    public void sendInvoiceMailDelayed() {

        if (!jobUtil.isJobActive(Constants.JOB_SEND_INVOICE_MAIL_DELAYED_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_SEND_INVOICE_MAIL_DELAYED_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

        logger.info("JOB", "Executando Job sendInvoiceMailDelayed - " + new Date());
        faturaService.sendFaturaMailDelayed();
        logger.info("JOB", "Fim Job sendInvoiceMailDelayed - " + new Date());
    }
    
    /**
     * Metodo que envia email semanalmente.
     */
    public void sendInvoiceMailToBeSubmitted() {

        if (!jobUtil.isJobActive(Constants.JOB_SEND_INVOICE_MAIL_TO_BE_SUBMITTED_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_SEND_INVOICE_MAIL_TO_BE_SUBMITTED_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

        logger.info("JOB", "Executando Job sendInvoiceMailToBeSubmitted - " + new Date());
        faturaService.sendFaturaMailToBeSubmitted();
        logger.info("JOB", "Fim Job sendInvoiceMailToBeSubmitted - " + new Date());
    }

}
