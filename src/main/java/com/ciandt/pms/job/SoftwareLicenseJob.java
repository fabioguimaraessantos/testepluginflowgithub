package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IChargebackPessService;
import com.ciandt.pms.util.JobUtil;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Properties;


/**
 * 
 * A classe SoftwareLicenseJob envia e-mail para os logins que possuem licença
 * vinculada ao seus softwares.
 * 
 * @since 07/12/2011
 * @author cmantovani
 * 
 */
public class SoftwareLicenseJob {

    private Logger logger = LoggerFactory.getLogger(SoftwareLicenseJob.class);

    @Autowired
    private JobUtil jobUtil;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** Instancia do servico de chargebackPess. */
    @Autowired
    private IChargebackPessService chargebackPessService;

    /**
     * Método que faz comparação das fotos dos Mapas e envia e-mails para os
     * followers.
     */
    public void softwareLicense() {

        if (!jobUtil.isJobActive(Constants.JOB_SOFTWARE_LICENSE_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_SOFTWARE_LICENSE_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

        try {
            logger.info("JOB", "Starting Job SoftwareLicense - " + new Date());
            chargebackPessService.sendSoftwareLicenseMail();
            logger.info("JOB", "End - Job SoftwareLicense - " + new Date());
        } catch (Exception e) {
            // se der algum erro no Job, envia um email informando o erro
            logger.error("JOB", Throwables.getStackTraceAsString(e));
        }
    }
}