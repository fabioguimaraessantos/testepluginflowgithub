package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IControleReajusteService;
import com.ciandt.pms.business.service.IFichaReajusteService;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.util.JobUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * A classe ControleReajusteJob envia email semanalmente para o Gestor e para o
 * CS informando que o contrato precisa ser reajustado.
 * 
 * @since 17/12/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
public class ControleReajusteJob {

	private Logger logger = LoggerFactory.getLogger(ControleReajusteJob.class);

	@Autowired
	private JobUtil jobUtil;

	/** Instancia do serviço DocumentoLegalService. */

	/** Instancia do serviço FichaReajusteService. */
	@Autowired
	private IFichaReajusteService fichaReajusteService;

	/** Instancia do serviço ControleReajusteService. */
	@Autowired
	private IControleReajusteService controleReajusteService;

	/** Arquivo de configuracoes (config.properties). */
	@Autowired
	private Properties systemProperties;

	/**
	 * 
	 */
	public void create() {

		if (!jobUtil.isJobActive(Constants.JOB_CONTROLE_REAJUSTE_CREATE_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_CONTROLE_REAJUSTE_CREATE_ACTIVE)
                    .concat(" property to 'true' for active it."));
			return;
		}

		logger.info("JOB", "Executando job ControleReajusteJob.create - " + new Date());

		List<FichaReajuste> fichasReajuste = fichaReajusteService.findAllFichaReajusteActive();

		for (FichaReajuste fichaReajuste : fichasReajuste) {
			controleReajusteService.createAllControleReajusteForFichaReajuste(fichaReajuste);
		}
	}

	/**
	 * Metodo que envia email semanalmente para o Gestor (GN) e CS lembrando que
	 * o contrato precisa ser reajustado na data x ou que venceu na data y.
	 */
	public void sendMail() {

		if (!jobUtil.isJobActive(Constants.JOB_CONTROLE_REAJUSTE_SEND_MAIL_ACTIVE)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_CONTROLE_REAJUSTE_SEND_MAIL_ACTIVE)
                    .concat(" property to 'true' for active it."));
			return;
		}

		logger.info("JOB", "Executando Job ControleReajusteJob.sendMail - " + new Date());

		// Quantidade de meses é configurado no config.properties
		Integer qtMesesInicioLembrete = Integer.valueOf(
				String.valueOf(systemProperties.get(Constants.CONTROLE_REAJUSTE_QT_MESES_INICIO_LEMBRETE)).trim());

		// Quantidade de dias é configurado no config.properties
		Integer qtDiasEntreLembretes = Integer.valueOf(
				String.valueOf(systemProperties.get(Constants.CONTROLE_REAJUSTE_QT_DIAS_ENTRE_LEMBRETES)).trim());

		// Chama servico que monta e envia email
		this.controleReajusteService.montaMailControleReajuste(qtMesesInicioLembrete, qtDiasEntreLembretes);

		logger.info("JOB", "Fim Job ControleReajusteJob.sendMail - " + new Date());

	}
}
