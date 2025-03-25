package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IDocumentoLegalService;
import com.ciandt.pms.util.JobUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Properties;

/**
 * 
 * @author peter
 * 
 */
public class DocumentoLegalJob {

    private Logger logger = LoggerFactory.getLogger(DocumentoLegalJob.class);

	@Autowired
	private JobUtil jobUtil;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Instancia do servico de documentoLegal. */
	@Autowired
	private IDocumentoLegalService documentoLegalService;

	/**
	 * Metodo que envia email quinzenalmente para o Gestor (GN) e CS lembrando
	 * que o contrato precisa ser renovado na data x ou que venceu na data y.
	 */
	public void sendMail() {

        if (!jobUtil.isJobActive(Constants.JOB_MAIL_DOCUMENTO_LEGAL_SEND_MAIL_ACTIVE)){
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_MAIL_DOCUMENTO_LEGAL_SEND_MAIL_ACTIVE)
                    .concat(" property to 'true' for active it."));
            return;
        }

        logger.info("JOB", "Executando Job DocumentoLegalMailJob.sendMail - " + new Date());

        // Quantidade de meses é configurado no config.properties
        Integer qtMesesInicioLembrete = Integer.valueOf(
                String.valueOf(systemProperties.get(Constants.DOCUMENTO_LEGAL_QT_MESES_INICIO_LEMBRETE)).trim());

        // Quantidade de dias é configurado no config.properties
        Integer qtDiasEntreLembretes = Integer.valueOf(
                String.valueOf(systemProperties.get(Constants.DOCUMENTO_LEGAL_QT_DIAS_ENTRE_LEMBRETES)).trim());

        // Chama servico que monta e envia email
        this.documentoLegalService.montaMailDocumentoLegal(qtMesesInicioLembrete, qtDiasEntreLembretes);

        logger.info("JOB", "Fim Job DocumentoLegalMailJob.sendMail - " + new Date());
	}

}
