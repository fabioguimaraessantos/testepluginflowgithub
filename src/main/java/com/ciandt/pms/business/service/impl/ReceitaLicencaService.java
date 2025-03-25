package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IReceitaLicencaService;
import com.ciandt.pms.business.service.ITaxaImpostoService;
import com.ciandt.pms.business.service.ITipoServicoService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.persistence.dao.IReceitaLicencaDao;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 14/11/2013
 */
@Service
public class ReceitaLicencaService implements IReceitaLicencaService {

	/** Log padrao da classe. */
	private final Logger log = LogManager.getLogger(ReceitaLicencaService.class
			.getName());

	/** Instancia do DAO da entidade. */
	@Autowired
	private IReceitaLicencaDao dao;

	/** Instancia do servico TaxaImposto. */
	@Autowired
	private ITaxaImpostoService taxaImpostoService;

	/** Instancia do servico TipoServico. */
	@Autowired
	private ITipoServicoService tipoServicoService;

	/** Instancia do servico Empresa. */
	@Autowired
	private IEmpresaService empresaService;

	@Autowired
	private MailSenderUtil mailSenderUtil;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return true se criado com sucesso, caso contrario retorna false
	 */
	@Transactional
	public Boolean create(final ReceitaLicenca entity) {
		dao.create(entity);

		return Boolean.valueOf(true);
	}

	/**
	 * Executa um update na entidade passada por parametro.
	 * 
	 * @param entity
	 *            que ser� atualizada.
	 * 
	 * @return true se update com sucesso, caso contrario retorna false
	 */
	@Transactional
	public void update(final ReceitaLicenca entity) {
		dao.update(entity);
	}

	@Transactional
	public void updateStatusReceitaLicenca(ReceitaLicenca receitaLicenca, StatusReceitaLicenca statusReceitaLicenca) {
		receitaLicenca.setIndicadorStatus(statusReceitaLicenca.getRevenueStatus());
		receitaLicenca.setIndicadorVersao(statusReceitaLicenca.getRevenueVersion());
		receitaLicenca.setTextoError(statusReceitaLicenca.getErrorMessage());
		receitaLicenca.setCodigoErpPedido(statusReceitaLicenca.getMegaOrderID().longValue());

		dao.update(receitaLicenca);
	}

	/**
	 * Remove a entidade passada por parametro.
	 * 
	 * @param entity
	 */
	@Transactional
	@Override
	public void remove(final ReceitaLicenca entity) {
		dao.remove(entity);
	}

	/**
	 * Remove uma {@link ReceitaLicenca} l�gicamente.
	 * @param entity
	 */
	@Transactional
	@Override
	public void removeLogically(final ReceitaLicenca entity) {
		ReceitaLicenca receitaLicenca = entity;
		receitaLicenca.setIndicadorDeleteLogico(Constants.SIM);

		this.update(receitaLicenca);
	}

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	public ReceitaLicenca findById(final Long entityId) {
		return dao.findById(entityId);
	}

	/**
	 * Busca uma {@link ReceitaLicenca} por todos os seus atributos.
	 * 
	 * @return Lista de {@link ReceitaLicenca}
	 */
	@Override
	public List<ReceitaLicenca> findByDataMesAndContratoPratica(
			final Date dataMes, final ContratoPratica contratoPratica) {
		return dao.findByDataMesAndContratoPratica(dataMes, contratoPratica);
	}

	/**
	 * Busca todas {@link ReceitaLicenca} pelo {@code codigoPaiReceitaLicenca}.
	 * 
	 * @return lista de {@link ReceitaLicenca}
	 */
	@Override
	public List<ReceitaLicenca> findByCodigoPaiReceitaLicenca(
			final Long codigoPaiReceitaLicenca) {
		return dao.findByCodigoPaiReceitaLicenca(codigoPaiReceitaLicenca);
	}

	@Override
	@Transactional
	public List<ReceitaLicenca> createReceitaLicenca(
			final ReceitaLicenca receitaLicenca, final Integer installments,
			final Double totalValue) {

		ReceitaLicenca firstInstallment = this
				.createFirstReceitaLicencaInstallment(receitaLicenca,
						installments, totalValue);

		this.createAllReceitaLicencaIntallments(firstInstallment, installments);

		return this.findByCodigoPaiReceitaLicenca(firstInstallment
				.getCodigoReceitaLicenca());
	}

	@Transactional
	private Boolean createAllReceitaLicencaIntallments(
			final ReceitaLicenca firstReceitaLicencaInstallment,
			final Integer installments) {
		ReceitaLicenca fi = firstReceitaLicencaInstallment;

		for (int i = 0; i < installments; i++) {

			ReceitaLicenca receitaLicenca = new ReceitaLicenca();
			receitaLicenca.setCodigoLoginCriador(fi.getCodigoLoginCriador());
			receitaLicenca.setCodigoPaiReceitaLicenca(fi
					.getCodigoReceitaLicenca());
			receitaLicenca.setContratoPratica(fi.getContratoPratica());
			receitaLicenca.setDataCriacao(fi.getDataCriacao());
			receitaLicenca.setDealFiscal(fi.getDealFiscal());
			receitaLicenca.setIndicadorVersao(fi.getIndicadorVersao());
			receitaLicenca.setIndicadorStatus(fi.getIndicadorStatus());
			receitaLicenca.setMoeda(fi.getMoeda());
			receitaLicenca.setTextoObservacao(fi.getTextoObservacao());
			receitaLicenca.setValorReceita(fi.getValorReceita());
			receitaLicenca.setIndicadorDeleteLogico(fi.getIndicadorDeleteLogico());

			if (i == 0) {
				firstReceitaLicencaInstallment
						.setCodigoPaiReceitaLicenca(firstReceitaLicencaInstallment
								.getCodigoReceitaLicenca());
				this.update(firstReceitaLicencaInstallment);
			} else {

				Calendar dataMes = Calendar.getInstance();
				dataMes.clear();
				dataMes.setTime(fi.getDataMes());
				dataMes.add(Calendar.MONTH, i);
				receitaLicenca.setDataMes(dataMes.getTime());

				this.create(receitaLicenca);
			}

		}

		return true;
	}

	@Transactional
	private ReceitaLicenca createFirstReceitaLicencaInstallment(
			final ReceitaLicenca receitaLicenca, final Integer installments,
			final Double totalValue) {
		ReceitaLicenca receitaInstallment = new ReceitaLicenca();
		Integer umaParcela = 1;
		BigDecimal installmentValue = BigDecimal.ZERO;
		if(installments == 0){
			installmentValue = new BigDecimal(totalValue / umaParcela);
		} else {installmentValue = new BigDecimal(totalValue / installments); }

		receitaInstallment.setDataCriacao(new Date());
		receitaInstallment.setCodigoLoginCriador(LoginUtil.getLoggedUsername());
		receitaInstallment.setContratoPratica(receitaLicenca
				.getContratoPratica());
		receitaInstallment.setDealFiscal(receitaLicenca.getDealFiscal());
		receitaInstallment.setMoeda(receitaLicenca.getDealFiscal().getMoeda());
		receitaInstallment.setValorReceita(installmentValue);
		receitaInstallment.setIndicadorVersao(receitaLicenca
				.getIndicadorVersao());
		receitaInstallment.setIndicadorStatus(receitaLicenca
				.getIndicadorStatus());
		receitaInstallment.setTextoObservacao(receitaLicenca
				.getTextoObservacao());
		receitaInstallment.setDataMes(receitaLicenca.getDataMes());
		receitaInstallment.setIndicadorDeleteLogico(receitaLicenca
				.getIndicadorDeleteLogico());

		this.create(receitaInstallment);

		return receitaInstallment;
	}

	public List<ReceitaLicenca> findByFormFilter(
			final ReceitaLicenca receitaLicenca, final Cliente cliente,
			final CentroLucro centroLucro, final NaturezaCentroLucro natureza) {

		return dao.findByFormFilter(receitaLicenca, cliente, centroLucro,
				natureza);
	}

	/**
	 * Retorna o percentual de imposto para a {@code receitaLicenca}
	 * 
	 * @param receitaLicenca
	 * @return
	 */
	public BigDecimal getTaxaImpostoForReceitaLicenca(
			final ReceitaLicenca receitaLicenca) {
		ReceitaLicenca rc = this.findById(receitaLicenca
				.getCodigoReceitaLicenca());

		TipoServico tipoServico = tipoServicoService.findTipoServicoById(3L);
		Empresa empresa = empresaService.findEmpresaById(rc.getDealFiscal()
				.getEmpresa().getCodigoEmpresa());
		TaxaImposto taxaImposto = taxaImpostoService
				.findTaxaByEmpresaTipoServicoDate(empresa, tipoServico,
						new Date());

		return taxaImposto.getValorTaxa();
	}

	/**
	 * Obtem as receitas integraveis de acordo com o filtro informado.
	 * 
	 * @param filter
	 *            {@link FormFilter}
	 * @return lista de {@link ReceitaLicenca}
	 */
	@Override
	public List<ReceitaLicenca> findIntegrableRevenueByFilter(
			final FormFilter filter) {
		return dao.findIntegrableRevenueByFormFilter(filter);
	}

	/**
	 * Executa a integracao entre a {@link ReceitaLicenca} e o ERP (Mega).
	 * 
	 * @param receitaLicenca
	 *            - {@link ReceitaLicenca}l a ser integrada ao ERP.
	 * 
	 * @return retorna o status da execucao da integracao.
	 */
	@Override
	@Transactional
	public Boolean integrate(final ReceitaLicenca receitaLicenca) {
		Integer status = dao.integrate(receitaLicenca);
		return !status.equals(0);
	}

	/**
	 * Realiza a reintegracao da {@link ReceitaLicenca}.
	 * 
	 * @param receitaLicenca
	 *            entidade {@link ReceitaLicenca}.
	 * @throws SQLException
	 * @throws HibernateException
	 */
	@Transactional
	public void reintegrate(final ReceitaLicenca receitaLicenca)
			throws HibernateException, SQLException {
			
		Boolean isOrderCanceled = Boolean.FALSE;
		isOrderCanceled = dao.isErpOrderCanceled(receitaLicenca);

		if (!isOrderCanceled) {
			Messages.showError("reIntegrarReceitaDeal",
					Constants.INTEGRACAO_RECEITA_MSG_ERROR_NOT_CANCELED);
		} else {
			receitaLicenca.resetIntegration();
			dao.update(receitaLicenca);
			Messages.showSucess("reintegrate",
					Constants.INTEGRACAO_RECEITA_REINTEGRATE_MSG_SUCCESS);
		}
	}

	@Transactional
	public void updateLicenseRevenueFromMega(Long revenueCode, String revenueStatus, BigDecimal megaOrderID, String errorMessage){
		ReceitaLicenca receitaLicenca = this.findById(revenueCode);

		if (revenueStatus.equals("A") && !receitaLicenca.getIndicadorVersao().equals(Constants.VERSION_RECEITA_INTEGRATED)) {
			StatusReceitaLicenca statusReceitaLicenca = new StatusReceitaLicenca(receitaLicenca.getCodigoReceitaLicenca(), Constants.INTEGRACAO_STATUS_INTEGRADO, Constants.VERSION_RECEITA_INTEGRATED, errorMessage, megaOrderID);
			this.updateStatusReceitaLicenca(receitaLicenca,statusReceitaLicenca);
		}
		else if (revenueStatus.equals("E") && !receitaLicenca.getIndicadorVersao().equals(Constants.VERSION_RECEITA_INTEGRATED)){
			StatusReceitaLicenca statusReceitaLicenca = new StatusReceitaLicenca(receitaLicenca.getCodigoReceitaLicenca(), Constants.INTEGRACAO_STATUS_ERROR, receitaLicenca.getIndicadorVersao(), errorMessage, megaOrderID);
			if (null != errorMessage || !"".equalsIgnoreCase(errorMessage)) {
				mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY,
						BundleUtil.getBundle("_nls.receita_integracao.mail.error.subject", receitaLicenca.getCodigoReceitaLicenca()),
						BundleUtil.getBundle("_nls.receita_integracao.mail.error.message", errorMessage));
			}
			this.updateStatusReceitaLicenca(receitaLicenca,statusReceitaLicenca);
		}
	}

}