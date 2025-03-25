package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.audit.listener.AuditoriaJpaListener;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.TaxaImpostoNotFoundException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IReceitaDao;
import com.ciandt.pms.persistence.dao.IReceitaDealFiscalDao;
import com.ciandt.pms.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 
 * A classe ReceitaDealService proporciona as funcionalidades da camada de
 * servi�o referente a entidade ReceitaDealFiscal.
 * 
 * @since 04/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ReceitaDealFiscalService implements IReceitaDealFiscalService {

	/** Instancia do DAO da entidade ReceitaDealFiscal. */
	@Autowired
	private IReceitaDealFiscalDao dao;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Intancia que realiza a auditoria (Log). */
	private AuditoriaJpaListener auditoria = new AuditoriaJpaListener();

	/** Instancia do Servi�o da entidade Receita. */
	@Autowired
	private IReceitaDao receitaDao;

	/** Instancia do Servi�o da entidade DealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/** Instancia do Servi�o da entidade Cliente. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do Servi�o da entidade TipoServico. */
	@Autowired
	private ITipoServicoService tipoServicoService;

	/** Instancia do Servi�o da entidade TaxaImposto. */
	@Autowired
	private ITaxaImpostoService taxaImpostoService;

	/**
	 * Cria uma entidade.
	 * 
	 * @param entity
	 *            - entidade a ser criada.
	 */
	public void createReceitaDealFiscal(final ReceitaDealFiscal entity) {
		dao.create(entity);

		auditoria.postPersist(entity);
	}

	/**
	 * Realiza a busca por id de uma entidade.
	 * 
	 * @param entityId
	 *            - id da entidade.
	 * 
	 * @return retorna a entidade, ou null caso n�o exista
	 */
	public ReceitaDealFiscal findReceitaDealById(final Long entityId) {
		return dao.findById(entityId);
	}

	/**
	 * Remove uma entidade.
	 * 
	 * @param entity
	 *            - entidade a ser removida.
	 * 
	 */
	public void removeReceitaDealFiscal(final ReceitaDealFiscal entity) {
		auditoria.postRemove(entity);

		dao.remove(entity);
	}

	/**
	 * Realiza o update de uma entidade.
	 * 
	 * @param entity
	 *            - entidade a ser atualizada.
	 * 
	 */
	@Transactional
	public void updateReceitaDealFiscal(final ReceitaDealFiscal entity) {
		entity.setDataAtualizacao(new Date());
		dao.update(entity);

		auditoria.preUpdate(entity);
	}

	@Transactional
	public void updateStatusReceitaDealFiscal(StatusReceitaDealFiscal statusReceitaDealFiscal) {

		ReceitaDealFiscal receitaDealFiscal = dao.findById(statusReceitaDealFiscal.getRevenueDealFiscalCode());

		receitaDealFiscal.setIndicadorStatus(statusReceitaDealFiscal.getRevenueStatus());
		receitaDealFiscal.setCodigoErpPedido(statusReceitaDealFiscal.getMegaOrderID());
		receitaDealFiscal.setTextoError(statusReceitaDealFiscal.getErrorMessage());
		receitaDealFiscal.setDataAtualizacao(new Date());

		dao.update(receitaDealFiscal);
	}

	/**
	 * Busca uma lista de entidades pela receita.
	 * 
	 * @param entity
	 *            entidade do tipo Receita.
	 * 
	 * @return lista de entidades relacionadas com a receita passada por
	 *         parametro.
	 */
	public List<ReceitaDealFiscal> findReceitaDealByReceita(final Receita entity) {
		return dao.findByReceita(entity.getCodigoReceita());
	}

	public Long findNotIntegratedRevenues(final Long revenueCurrencyCode) {
		return dao.findNotIntegratedRevenues(revenueCurrencyCode);
	}

	/**
	 * Busca uma lista de entidades pelo contrato-pratica and data-mes.
	 * 
	 * @param idContratoPratica
	 *            id da entidade do tipo ContratoPratica.
	 * 
	 * @param dataMes
	 *            objeto do tipo Date.
	 * 
	 * @param status
	 *            status da receita.
	 * 
	 * @return lista de entidades relacionadas com a ContratoPratica passada por
	 *         parametro.
	 */
	public List<ReceitaDealFiscal> findReceitaDealByFilter(
			final Long idContratoPratica, final Long idEmpresa,  final Date dataMes,
			final String status) {

		return dao.findByFilter(idContratoPratica, idEmpresa, dataMes, status);
	}

	/**
	 * Busca uma lista de entidades pelo contrato-pratica and data-mes.
	 * 
	 * @param idContratoPratica
	 *            id da entidade do tipo ContratoPratica.
	 * 
	 * @param dataMes
	 *            objeto do tipo Date.
	 * 
	 * @return lista de entidades relacionadas com a ContratoPratica passada por
	 *         parametro.
	 */
	public List<ReceitaDealFiscal> findByReceitaDealFiscalContratoPraticaAndDataMesToAjuste(
			final Long idContratoPratica, final Date dataMes) {

		return dao.findByContratoPraticaAndDataMesToAjuste(idContratoPratica,
				dataMes);
	}

	/**
	 * Executa a integra��o da receita-deal com o ERP.
	 * 
	 * @param rdf
	 *            - ReceitaDealFiscal a ser integrada ao ERP.
	 * 
	 * @return retorna o status da execu��o da integra��o. Se retorno menor que
	 *         zero erro, caso contrario sucesso.
	 */
	public Boolean integrarReceitaDeal(final ReceitaDealFiscal rdf) {

		Integer status = dao.integrateReceita(rdf.getCodigoReceitaDfiscal());

		// caso erro na integra��o
		if (status == 0) {
			return Boolean.FALSE;
		}

		// sucesso na integra��o
		return Boolean.TRUE;

	}

	/**
	 * Realiza a reintegra��o da receita-deal.
	 * 
	 * @param rdf
	 *            entidade ReceitaDealFiscal.
	 */
	public void reIntegrarReceitaDeal(final ReceitaDealFiscal rdf) {

		// Implementa��o antiga
		ReceitaDealFiscal receitaDeal = dao.findById(rdf
				.getCodigoReceitaDfiscal());

		Boolean isCanceled = dao.isErpOrderCanceled(receitaDeal);
		// Fim da implementa��o antiga

		if (isCanceled == null) {
			Messages.showError("reIntegrarReceitaDeal",
					Constants.INTEGRACAO_RECEITA_DATABASE_ACCESS_ERP_ERROR);
		} else if (isCanceled) {

			receitaDeal.setCodigoErpPedido(null);
			receitaDeal
					.setIndicadorStatus(Constants.INTEGRACAO_STATUS_NAO_INTEGRADO);
			receitaDeal.setTextoError(null);
			receitaDeal.setDataAtualizacao(new Date());

			dao.update(receitaDeal);

			this.updateStatusReceita(receitaDeal.getReceitaMoeda().getReceita());

			Messages.showSucess("reIntegrarReceitaDeal",
					Constants.INTEGRACAO_RECEITA_REINTEGRATE_MSG_SUCCESS);
		} else {
			Messages.showError("reIntegrarReceitaDeal",
					Constants.INTEGRACAO_RECEITA_MSG_ERROR_NOT_CANCELED);
		}

	}

	/**
	 * Realiza o update do status da receita.
	 * 
	 * @param receita
	 *            - receita em quest�o.
	 */
	private void updateStatusReceita(final Receita receita) {
		Receita r = receitaDao.findById(receita.getCodigoReceita());

		List<ReceitaDealFiscal> rdfList = new ArrayList<ReceitaDealFiscal>();

		// popula a lista de ReceitaDealFiscal
		for (ReceitaMoeda receitaMoeda : r.getReceitaMoedas()) {
			rdfList.addAll(receitaMoeda.getReceitaDealFiscals());
		}

		int countIntegreted = 0;
		int countOthers = 0;
		// conta quantos receita-dealFiscal est�o no estado Integrado
		for (ReceitaDealFiscal receitaDF : rdfList) {
			if (Constants.INTEGRACAO_BUNDLE_STATUS_INTEGRADO.equals(receitaDF
					.getIndicadorStatus())) {
				countIntegreted++;
			} else {
				countOthers++;
			}
		}
		// se todas est�o no estado integrado
		if (countOthers == 0) {
			r.setIndicadorVersao(Constants.VERSION_RECEITA_INTEGRATED);
			// se nenhuma esta integrada
		} else if (countIntegreted == 0) {
			r.setIndicadorVersao(Constants.VERSION_RECEITA_PUBLISHED);
			// se somente algumas estao integradas
		} else {
			r.setIndicadorVersao(Constants.VERSION_RECEITA_PENDING);
		}
		receitaDao.update(r);
	}

	/**
	 * Realiza a busca da entidade, atravez dos criterios do filtro.
	 * 
	 * @param cp
	 *            - ContratoPratica
	 * @param cli
	 *            - Cliente
	 * @param cl
	 *            - CentroLucro
	 * @param natureza
	 *            - NaturezaCentroLucro
	 * @param dataMesInicio
	 *            - data mes inicio
	 * @param dataMesFim
	 *            - data mes fim
	 * 
	 * @return retorna uma lista de ReceitaDealFiscal
	 */
	public List<ReceitaDealFiscal> findReceitaDealByFilter(
			final ContratoPratica cp, final Cliente cli, final CentroLucro cl,
			final NaturezaCentroLucro natureza, final Date dataMesInicio,
			final Date dataMesFim) {

		return dao.findByFilter(cp, cli, cl, natureza, dataMesInicio,
				dataMesFim);
	}

	/**
	 * Realiza a busca da entidade, atravez dos criterios do filtro.
	 * 
	 * @param cp
	 *            - ContratoPratica
	 * @param cli
	 *            - Cliente
	 * @param dataMesInicio
	 *            - data mes inicio
	 * @param dataMesFim
	 *            - data mes fim
	 * @param receita
	 *            - Receita
	 * @return retorna uma lista de ReceitaDealFiscal
	 */
	@Deprecated
	public List<ReceitaDealFiscal> findReceitaDealByFilterSub(
			final ContratoPratica cp, final Cliente cli,
			final Date dataMesInicio, final Date dataMesFim,
			final Receita receita, final DealFiscal dealFiscal) {

		return dao.findByFilterSub(cp, cli, dataMesInicio, dataMesFim, receita,
				dealFiscal);
	}

	/**
	 * Pega o valor total da receita relacionado com o deal e a receita,
	 * passados por parametro.
	 * 
	 * @param receita
	 *            - Receita em quest�o
	 * @param deal
	 *            - DealFiscal em quest�o
	 * 
	 * @return retorna o valor total da receita
	 */
	public BigDecimal getTotalReceitaByDealAndReceita(final Receita receita,
			final DealFiscal deal) {
		return dao.getTotalReceitaByDealAndReceita(receita, deal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IReceitaDealFiscalService#
	 * getTotalPublishedByDealFiscalAndDate(com.ciandt.pms.model.DealFiscal,
	 * java.util.Date)
	 */
	public BigDecimal getTotalPublishedByDealFiscalAndDate(
			final DealFiscal dealFiscal, final Date dataFim) {
		return dao
				.getTotalPublishedByDealFiscalAndDate(dealFiscal, dataFim);
	}

	/**
	 * Busca a Receita Deal Fiscal pela receita e deal fiscal.
	 * 
	 * @param receita
	 *            - Receita em quest�o
	 * @param deal
	 *            - DealFiscal em quest�o
	 * 
	 * @return retorna um {@link ReceitaDealFiscal}
	 */
	public ReceitaDealFiscal findReceitaDealByReceitaAndDeal(
			final Receita receita, final DealFiscal deal) {
		return dao.findByReceitaAndDeal(receita, deal);
	}

	/**
	 * Busca todos os {@link ReceitaDealFiscal} ativos dado um
	 * {@link ContratoPratica} e uma {@link Moeda}.
	 * 
	 * @param contratoPratica
	 *            - contratoPratica
	 * @param moeda
	 *            - moeda
	 * @return uma lista de {@link ReceitaDealFiscal}
	 */
	public List<ReceitaDealFiscal> findReceitaDFiscalByActiveAndCLobAndMoeda(
			final ContratoPratica contratoPratica, final Moeda moeda) {
		return dao.findByActiveAndCLobAndMoeda(contratoPratica, moeda);
	}

	/**
	 * Calcula a media dos impostos dos TiposServico do DealFiscal e da Empresa.
	 * Caso n�o encontre a TaxaImposto associada � Empresa e TipoServico, n�o
	 * termina o c�uculo e retorna uma exception para que a Receita n�o seja
	 * publicada.
	 * 
	 * @param receitaDealFiscal
	 *            a {@link ReceitaDealFiscal} a ser calculada
	 * 
	 * @param dataMes
	 *            data da receita - usado para buscar a vigencia da taxa de
	 *            imposto
	 * 
	 * @exception TaxaImpostoNotFoundException
	 *                - excecao lancada quando n�o encontrado a TaxaImposto
	 *                associada � Empresa e TipoServico
	 * 
	 * @return retorna o valor calculado da taxa de imposto
	 */
	@Override
	public BigDecimal calculaMediaPercentualImposto(
			final ReceitaDealFiscal receitaDealFiscal, final Date dataMes)
			throws TaxaImpostoNotFoundException {

		BigDecimal valorImposto = BigDecimal.valueOf(0);

		// obtem o deal fiscal
		DealFiscal df = dealFiscalService.findDealFiscalById(receitaDealFiscal
				.getDealFiscal().getCodigoDealFiscal());

		// obtem o cliente
		Cliente cliente = clienteService.findClienteById(df.getCliente()
				.getCodigoCliente());

		// se o cliente for internacional n�o possui taxa
		// setta o percentual imposto como zero e sai do metodo
		if (cliente.getIndicadorTipo().equals(
				Constants.CLIENTE_TYPE_INTERNATIONAL)) {
			return BigDecimal.valueOf(0);
		}

		// obtem uma lista de TipoServico a partir do DealFiscal
		List<TipoServico> tipoServicoList = tipoServicoService
				.findTipoServicoByDeal(df);

		// checa se o deal fiscal possui tipo de servico associado
		if (tipoServicoList != null && !tipoServicoList.isEmpty()) {
			// percorre todos os tipos de servico de um deal fiscal
			for (TipoServico ts : tipoServicoList) {
				// Desconsidera as taxas para TipoServico de Reembolso de
				// despesa
				if (!ts.getCodigoTipoServico()
						.equals(Long.valueOf(systemProperties
								.getProperty(Constants.EXPENSE_REIMBURSEMENT_CODE)))) {

					// obtem a taxa de imposto
					TaxaImposto taxaImposto = taxaImpostoService
							.findTaxaByEmpresaTipoServicoDate(df.getEmpresa(),
									ts, dataMes);

					// compoe o valor do imposto
					if (taxaImposto != null) {
						/*
						 * valorImposto = valorImposto.add(taxaImposto
						 * .getValorTaxa());
						 */
						if (taxaImposto.getValorTaxaMunicipal() != null) {
							valorImposto = valorImposto.add(taxaImposto
									.getValorTaxaMunicipal());
						}
						if (taxaImposto.getValorTaxaFederal() != null) {
							valorImposto = valorImposto.add(taxaImposto
									.getValorTaxaFederal());
						}

					} else {
						// caso n�o tenha TaxaImposto cadastrada, lanca a
						// exception
						String nomeTipoServico = ts.getNomeTipoServico();
						String nomeEmpresa = df.getEmpresa().getNomeEmpresa();
						String nomeDealFiscal = df.getNomeDealFiscal();
						String mesBase = DateUtil
								.getStringDate(
										dataMes,
										new SimpleDateFormat(
												BundleUtil
														.getBundle(Constants.DEFAULT_DATE_PATTERN_SIMPLE)));

						throw new TaxaImpostoNotFoundException(
								BundleUtil
										.getBundle(
												Constants.RECEITA_DEAL_FISCAL_MSG_ERROR_NO_TAX_FOUND,
												new Object[] { nomeTipoServico,
														nomeEmpresa,
														nomeDealFiscal, mesBase }));
					}
				}
			}

			// divide o valor obtido pelo numero de servicos (media)
			if (tipoServicoList.size() != 0) {
				valorImposto = valorImposto.divide(new BigDecimal(
						tipoServicoList.size()), BigDecimal.ROUND_UP);
			}
		}

		// retorna o percentual do imposto
		return valorImposto.divide(BigDecimal.valueOf(100));
	}

	/**
	 * Busca receitaDealFiscal por ReceitaMOeda.
	 * 
	 * @param receitaMoeda
	 *            receitaMoeda.
	 * @return listReuslt.
	 */
	public List<ReceitaDealFiscal> findReceitaDealFiscalByReceitaMoeda(
			final ReceitaMoeda receitaMoeda) {
		return dao.findByReceitaMoeda(receitaMoeda.getCodigoReceitaMoeda());
	}

	/**
	 * Busca ReceitaDealFiscal por DealFiscal.
	 * 
	 * @param DealFiscal
	 *            DealFiscal.
	 * @return listResult.
	 */
	public List<ReceitaDealFiscal> findPublishedAndIntegrateByDealFiscal(
			final DealFiscal dealFiscal) {
		return dao.findPublishedAndIntegrateByDealFiscal(dealFiscal);
	}

	/**
	 * Busca ReceitaDealFiscal por DealFiscal e periodo.
	 * 
	 * @param DealFiscal
	 *            DealFiscal.
	 * @return listResult.
	 */
	public List<ReceitaDealFiscal> findByDealFiscalAndPeriod(
			final DealFiscal dealFiscal, final Date startDate,
			final Date endDate) {
		return dao.findByDealFiscalAndPeriod(dealFiscal, startDate, endDate);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.business.service.IReceitaDealFiscalService#findByDealFiscal
	 * (java.lang.Long)
	 */
	public List<ReceitaDealFiscal> findByDealFiscal(final Long codigoDealFiscal) {
		return dao.findByDealFiscal(codigoDealFiscal);
	}

}
