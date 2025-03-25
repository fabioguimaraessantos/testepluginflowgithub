package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IControleReajusteService;
import com.ciandt.pms.business.service.IControleReajusteStatusService;
import com.ciandt.pms.business.service.IDocumentoLegalService;
import com.ciandt.pms.business.service.IFichaReajusteService;
import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.comparator.ControleReajusteDataPrevistaComparator;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.ControleReajusteStatus;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.persistence.dao.IControleReajusteDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.PMSUtil;
import com.google.common.base.Preconditions;

/**
 * A classe ControleReajusteService proporciona as funcionalidades de serviço
 * referente a entidade ControleReajuste.
 * 
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class ControleReajusteService extends AbstractGestaoReajusteService
		implements IControleReajusteService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IControleReajusteDao dao;

	/** Instancia do DAO da entidade ControleReajusteAud. */
	@Autowired
	private IControleReajusteDao controleReajusteDao;

	/** Instancia do Servico da entidade {@link ControleReajusteStatus}. */
	@Autowired
	private IControleReajusteStatusService controleReajusteStatusService;

	/** Instancia do Servico da entidade {@link FichaReajusteService}. */
	@Autowired
	private IFichaReajusteService fichaReajusteService;

	/** Instancia do Servico da entidade {@link DocumentoLegalService}. */
	@Autowired
	private IDocumentoLegalService documentoLegalService;

	/** Instancia do Servico da entidade {@link ContratoPraticaService}. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do Servico da entidade {@link ParametroService}. */
	@Autowired
	private IParametroService parametroService;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public ControleReajuste findControleReajusteById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<ControleReajuste> findControleReajusteAll() {
		return dao.findAll();
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void createControleReajuste(final ControleReajuste entity) {
		dao.create(entity);
	}

	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 */
	public void updateControleReajuste(final ControleReajuste entity) {
		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 */
	public void deleteControleReajuste(final ControleReajuste entity) {
		dao.remove(entity);
	}

	/**
	 * Popula e um {@link ControleReajuste} a partir de uma
	 * {@link FichaReajuste}.
	 * 
	 * @param fichaReajuste
	 * @return {@link ControleReajuste}.
	 */
	@Override
	public ControleReajuste populateControleReajusteFromFichaReajuste(
			final FichaReajuste fichaReajuste) {

		Hibernate.initialize(fichaReajuste);

		ControleReajusteStatus controleReajusteStatus = controleReajusteStatusService
				.findControleReajusteStatusBySiglaControleReajusteStatus(Constants.CONTROLE_REAJUSTE_STATUS_SG_OPEN);

		ControleReajuste lastControleReajuste = this
				.findLastControleReajustebyFichaReajuste(fichaReajuste);

		if (lastControleReajuste.getCodigoControleReajuste() == null) {
			ControleReajuste controleReajuste = new ControleReajuste(
					controleReajusteStatus, fichaReajuste);

			return controleReajuste;
		}

		ControleReajuste controleReajuste = new ControleReajuste();
		controleReajuste.setControleReajusteStatus(controleReajusteStatus);
		controleReajuste.setFichaReajuste(fichaReajuste);

		// Calculando a data prevista.
		Calendar dataPrevista = Calendar.getInstance();
		dataPrevista.setTime(lastControleReajuste.getDataPrevista());
		dataPrevista
				.add(Calendar.MONTH, fichaReajuste.getNumeroPeriodicidade());
		controleReajuste.setDataPrevista(dataPrevista.getTime());

		return controleReajuste;
	}

	/**
	 * Busca {@link ControleReajuste} onde sua Data Prevista está entre
	 * {@code startDate} e {@code endDate}.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Lista de {@link ControleReajuste}
	 */
	@Override
	public List<ControleReajuste> findControleReajusteByDateIntervalAndFichaReajuste(
			Date startDate, Date endDate, FichaReajuste fichaReajuste) {

		return dao.findByDateIntervalAndFichaReajuste(startDate, endDate,
				fichaReajuste);
	}

	/**
	 * Busca {@link ControleReajuste} onde sua Data prevista é maior que
	 * {@code date}.
	 * 
	 * @param date
	 * @return Lista de {@link ControleReajuste}
	 */
	@Override
	public List<ControleReajuste> findControleReajusteGreaterThanDateAndFichaReajuste(
			Date date, FichaReajuste fichaReajuste) {

		return dao.findGreaterThanDateAndFichaReajuste(date, fichaReajuste);
	}

	/**
	 * Busca {@link ControleReajuste} onde sua Data Prevista está entre a
	 * vigência e {@link FichaReajuste} do {@link DocumentoLegal}.
	 * 
	 * @param documentoLegal
	 * @return Lista de {@link DocumentoLegal}
	 */
	@Override
	public List<ControleReajuste> findControleReajusteByDocumentoLegal(
			DocumentoLegal documentoLegal) {

		if (documentoLegal.getSiglaRenovacaoAutomatica().equals(
				Constants.DOCUMENTO_LEGAL_AUTO_RENEWAL_NO)) {

			Integer addition = Integer.valueOf(systemProperties.getProperty(Constants.DOCUMENTO_LEGAL_ADICAO_PERIODO));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(documentoLegal.getDataFimVigencia());
			calendar.add(Calendar.DATE, addition);
			Date fimVigenciaDocumentoLegal = calendar.getTime();

			return this.findControleReajusteByDateIntervalAndFichaReajuste(
					documentoLegal.getDataInicioVigencia(),
					fimVigenciaDocumentoLegal,
					documentoLegal.getFichaReajuste());
		}

		return this.findControleReajusteGreaterThanDateAndFichaReajuste(
				documentoLegal.getDataInicioVigencia(),
				documentoLegal.getFichaReajuste());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IControleReajusteService#
	 * findHistoryByCodigoControleReajuste(java.lang.Long)
	 */
	public List<ControleReajusteAud> findHistoryByCodigoControleReajuste(
			final Long codigoControleReajuste) {
		return controleReajusteDao
				.findHistoryByCodigoControleReajuste(codigoControleReajuste);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IControleReajusteService#
	 * findHistoryByCdControleReajusteAndRevtype(java.lang.Long, java.lang.Long)
	 */
	public List<ControleReajusteAud> findHistoryByCdControleReajusteAndRevtype(
			final Long codigoControleReajuste, final Long revtype) {
		return controleReajusteDao.findHistoryByCdControleReajusteAndRevtype(
				codigoControleReajuste, revtype);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.business.service.IControleReajusteService#findToSendEmail
	 * (java.util.Date, java.util.Date)
	 */
	public List<ControleReajuste> findToSendEmail(final Date dataPrevista,
			final Date dataUltimoEnvioEmail) {
		return dao.findToSendEmail(dataPrevista, dataUltimoEnvioEmail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IControleReajusteService#
	 * montaMailControleReajuste(java.lang.Integer, java.lang.Integer)
	 */
	public void montaMailControleReajuste(final Integer qtMesesInicioLembrete,
			final Integer qtDiasEntreLembretes) {

		// Busca lista de controles de reajustes que precisa ser enviado
		// e-mail, nesta lista retorna os reajustes a vencer e os reajustes
		// já vencidos
		List<ControleReajuste> lista = this.findToSendEmail(
				getDataInicioEnvio(qtMesesInicioLembrete),
				getDataUltimoEnvio(qtDiasEntreLembretes));

		for (ControleReajuste controleReajuste : lista) {

			// Lista de documentos legais ativos
			List<DocumentoLegal> documentoLegalAtivos = new ArrayList<DocumentoLegal>();
			// Lista com os emails que deverão receber o aviso
			StringBuilder mailTo = null;
			// Lista com os gestores, Business Manager do Contratos Praticas do
			// MSA
			List<String> gestores = new ArrayList<String>();
			// Nome do MSA
			String nomeMSA = "";
			// Nome do Cliente
			String nomeCliente = "";

			final Iterator<DocumentoLegal> it = controleReajuste
					.getFichaReajuste().getDocumentoLegals().iterator();
			while (it.hasNext()) {

				DocumentoLegal documentoLegal = it.next();

				// Se o Documento Legal estiver ATIVO e se o MSA não estiver
				// INATIVO
				if (Constants.ACTIVE
						.equals(documentoLegal.getIndicadorStatus())
						&& !Constants.INACTIVE.equals(documentoLegal.getMsa()
								.getIndicadorStatus())) {

					// seta o mailTo e o nome do gestor, somente na primeira
					// iteração
					if (mailTo == null) {
						// Inclui o email do CS, configurado no arquivo
						// config.properties
						mailTo = new StringBuilder(systemProperties.getProperty(Constants.CONTROLE_REAJUSTE_EMAIL_CS));

						//Removendo o envio de email para os gestores a pedido da area de invoicing
						//mailTo.append(super.SEPARADOR_EMAIL);

						// Pega os gestores dos Contratos Praticas vinculado ao
						// MSA
						gestores = documentoLegal.getNomeDocumentoLegalResponsaveis();

						// Pega o nome do MSA para montar corpo do email
						nomeMSA = documentoLegal.getMsa().getNomeMsa();

						// Pega o nome do cliente
						nomeCliente = documentoLegal.getMsa().getCliente()
								.getNomeCliente();
					}

					// Adiciona o documento legal na lista para incluí-lo no
					// email
					documentoLegalAtivos.add(documentoLegal);

					// Atualiza a data de envio de email do controle de reajuste
					ControleReajuste reajusteToUpdate = this
							.findControleReajusteById(controleReajuste.getCodigoControleReajuste());
					reajusteToUpdate.setDataEnvioEmail(new Date());
					this.updateControleReajuste(reajusteToUpdate);
				}
			}

			// Se existir algum Documento Legal ativo vinculado ao controle de
			// reajuste, então envia email
			if (!documentoLegalAtivos.isEmpty()) {

				// Se diferente de ambiente de produção seta o email de teste.
				// Verificação de segurança, para que não seja enviado
				// email para o usuário no ambiente de desenvolvimento/teste.
				if (!PMSUtil.isProduction()) {
					mailTo = new StringBuilder(
							systemProperties
									.getProperty(Constants.EMAIL_ADDRESS_TEST_KEY));
				}

				// Se a data atual for anterior ou igual a data prevista, então
				// envia o email de lembrete que deverá ser reajustado até data
				// x
				if (DateUtil.before(new Date(),
						controleReajuste.getDataPrevista(),
						Calendar.HOUR_OF_DAY)) {
					// Define o assunto do e-mail
					String subject = BundleUtil.getBundle(
							Constants.CONTROLE_REAJUSTE_EMAIL_ASSUNTO_A_VENCER,
							nomeCliente);

					this.sendMailControleReajuste(controleReajuste,
							nomeCliente, gestores, nomeMSA, mailTo.toString(),
							"controleReajusteReminder.vm", subject);
				} else {

					// Define o assunto do e-mail
					String subject = BundleUtil.getBundle(
							Constants.CONTROLE_REAJUSTE_EMAIL_ASSUNTO_VENCIDO,
							nomeCliente);

					// Se a data atual for posterior a data prevista, então
					// envia o email que já Venceu
					this.sendMailControleReajuste(controleReajuste,
							nomeCliente, gestores, nomeMSA, mailTo.toString(),
							"controleReajusteAdjustmentNeeded.vm", subject);
				}
			}
		}
	}

	/**
	 * Monta e envia o email do reajuste
	 * 
	 * @param controleReajuste
	 *            Objeto {@link ControleReajuste}
	 * @param nomeCliente
	 *            Nome do Cliente
	 * @param gestores
	 *            Lista com os nomes dos Gestores dos Contratos Praticas do MSA
	 * @param nomeMSA
	 *            Nome do MSA
	 * @param mailTo
	 *            e-mails dos destinatários, separado por virgula
	 * @param emailTemplate
	 *            Nome do template de e-mail
	 * @param subject
	 *            Subject do e-mail
	 * 
	 */
	private void sendMailControleReajuste(
			final ControleReajuste controleReajuste, final String nomeCliente,
			final List<String> gestores, final String nomeMSA,
			final String mailTo, final String emailTemplate,
			final String subject) {

		// Seta os parametros necessários para montar o body do e-mail
		Map<String, Object> dataSource = new HashMap<String, Object>();

		dataSource.put("nomesGestores", montaNomesGestores(gestores));
		dataSource.put("expectedDate",
				sdf.format(controleReajuste.getDataPrevista()));
		dataSource.put("codigoFichaReajuste", controleReajuste.getFichaReajuste()
				.getCodigoFichaReajuste());
		dataSource.put("nomeFichaReajuste", controleReajuste.getFichaReajuste()
				.getNomeFichaReajuste());
		dataSource.put("indiceReajuste", controleReajuste.getFichaReajuste()
				.getFichaReajusteIndice().getNomeFichaReajusteIndice());
		dataSource.put("statusControleReajuste", controleReajuste
				.getControleReajusteStatus().getNomeControleReajStatus());
		dataSource.put("link",
				systemProperties.getProperty(Constants.HOST_ENVIRONMENT));

		// Define o body do email atraves do template e parametros
		String messageBody = mailSender.getTemplateMailMessage(emailTemplate,
				dataSource);

		// Envia o email
		mailSender.sendHtmlMail(mailTo.toString(), subject, messageBody);
	}

	/**
	 * Busca por ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	public List<ControleReajuste> findByFichaReajuste(
			final FichaReajuste fichaReajuste) {
		return dao.findByFichaReajuste(fichaReajuste);
	}

	/**
	 * Retorna o ultimo {@link ControleReajuste} com {@code fichaResjuste}.
	 * 
	 * @param fichaReajuste
	 * @return List<ControleReajuste>
	 */
	@Override
	public ControleReajuste findLastControleReajustebyFichaReajuste(
			final FichaReajuste fichaReajuste) {

		return dao.findLastbyFichaReajuste(fichaReajuste);
	}

	/**
	 * Retorna o ultimo {@link ControleReajuste} de cada {@link FichaReajuste}
	 * em {@code fichasResjustes}.
	 * 
	 * @param fichasReajustes
	 * @return List<ControleReajuste>
	 */
	@Override
	public List<ControleReajuste> findLastControleReajustebyFichasReajuste(
			final List<FichaReajuste> fichasReajustes) {

		List<ControleReajuste> controlesReajuste = new ArrayList<ControleReajuste>();
		for (FichaReajuste fichaReajuste : fichasReajustes) {
			controlesReajuste.add(dao.findLastbyFichaReajuste(fichaReajuste));
		}

		return controlesReajuste;
	}

	@Override
	public List<ControleReajuste> findControleReajusteByDateIntervalAndMsaAndCliente(
			final Date startDate, final Date endDate, final Msa msa,
			final Cliente cliente, final Long controleReajusteStatusId) {

		return dao.findByDateIntervalAndMsaAndCliente(startDate, endDate, msa,
				cliente, controleReajusteStatusId);
	}

	/**
	 * Cria um ou mais {@link ControleReajuste} necessários para
	 * {@link FichaReajuste}.
	 * 
	 * @param fichaReajuste
	 */
	@Override
	@Transactional
	public void createControleReajusteForFichaReajuste(FichaReajuste fichaReajuste) {
		ControleReajuste controleReajuste = new ControleReajuste();

		controleReajuste.setFichaReajuste(fichaReajuste);
		controleReajuste.setDataPrevista(this
				.getDataPrevistaOfNextControleReajuste(fichaReajuste));
		controleReajuste
				.setControleReajusteStatus(controleReajusteStatusService
						.findControleReajusteStatusBySiglaControleReajusteStatus(Constants.CONTROLE_REAJUSTE_STATUS_SG_OPEN));

		this.createControleReajuste(controleReajuste);
	}

	/**
	 * Retorna a {@code dataPrevista} do próximo {@link ControleReajuste} para
	 * {@code fichaReajuste}.
	 * 
	 * @param fichaReajuste
	 * @param controleReajuste
	 * @return {@link Date}
	 */
	@Override
	public Date getDataPrevistaOfNextControleReajuste(
			FichaReajuste fichaReajuste) {
		Preconditions.checkNotNull(fichaReajuste.getControleReajustes());

		Parametro parametroDataCorte = parametroService
				.findParametroByNomeParametro(Constants.CONTROLE_REAJUSTE_DATA_CORTE);
		Calendar nextDataPrevista = Calendar.getInstance();

		List<ControleReajuste> controlesReajuste = this.findByFichaReajuste(fichaReajuste);

		if (controlesReajuste.isEmpty()) {
			nextDataPrevista.setTime(fichaReajuste.getDataBase());
			nextDataPrevista.add(Calendar.MONTH, fichaReajuste.getNumeroPeriodicidade());
		} else {
			// Ordena a lista de ControleReajuste e pega o ControleReajuste mais
			// recente
			Collections.sort(controlesReajuste,
					new ControleReajusteDataPrevistaComparator());
			ControleReajuste mostRecentControleReajuste = controlesReajuste
					.get(controlesReajuste.size() - 1);

			nextDataPrevista.setTime(mostRecentControleReajuste.getDataPrevista());
			nextDataPrevista.add(Calendar.MONTH, fichaReajuste.getNumeroPeriodicidade());
		}

		while (nextDataPrevista.getTime().before(parametroDataCorte.getDataParametro())) {
			nextDataPrevista.add(Calendar.MONTH, fichaReajuste.getNumeroPeriodicidade());
		}

		return nextDataPrevista.getTime();
	}

	@Transactional
	public void createAllControleReajusteForFichaReajuste(FichaReajuste fichaReajuste) {
		while (fichaReajusteService.fichaReajusteNeedsNewControleReajuste(fichaReajuste)) {

			this.createControleReajusteForFichaReajuste(fichaReajuste);

		      List<ControleReajuste> controlesReajuste = this.findByFichaReajuste(fichaReajuste);
		      Set<ControleReajuste> controlesSet = new HashSet<ControleReajuste>();
		      controlesSet.addAll(controlesReajuste);
		      fichaReajuste.setControleReajustes(controlesSet);
		}
	}
}