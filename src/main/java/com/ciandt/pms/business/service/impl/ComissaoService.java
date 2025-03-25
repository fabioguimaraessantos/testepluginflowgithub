package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IComissaoFaturaService;
import com.ciandt.pms.business.service.IComissaoService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.ICotacaoMoedaService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.ITaxaImpostoService;
import com.ciandt.pms.control.jsf.converters.StatusComissaoConverter;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Acelerador;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Comissao;
import com.ciandt.pms.model.ComissaoAcelerador;
import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.HistoricoComissao;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TaxaImposto;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.model.vo.ComissaoRow;
import com.ciandt.pms.persistence.dao.IComissaoAceleradorDao;
import com.ciandt.pms.persistence.dao.IComissaoDao;
import com.ciandt.pms.persistence.dao.IComissaoFaturaDao;
import com.ciandt.pms.persistence.dao.IHistoricoComissaoDao;
import com.ciandt.pms.persistence.dao.IItemFaturaDao;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;

/**
 * 
 * A classe ComissaoService proporciona as funcionalidades da camada de servico
 * referente as comissões.
 * 
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ComissaoService implements IComissaoService {

	/** Instancia do dao ComissaoDao. */
	@Autowired
	private IComissaoDao comissaoDao;

	/** Instancia do dao ComissaoFaturaDao. */
	@Autowired
	private IComissaoFaturaDao comissaoFaturaDao;

	/** Instancia do dao ComissaoAceleradorDao. */
	@Autowired
	private IComissaoAceleradorDao comissaoAceleradorDao;

	/** Instancia do dao HistoricoComissaoDao. */
	@Autowired
	private IHistoricoComissaoDao historicoComissaoDao;

	/** Instancia do serviço PessoaService. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do serviço ContratoPraticaService. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do serviço CotacaoMoedaService. */
	@Autowired
	private ICotacaoMoedaService cotacaoMoedaService;

	/** Instancia do serviço EmpresaService. */
	@Autowired
	private ITaxaImpostoService taxaService;

	/** Instancia do serviço ComissaoFatura. */
	@Autowired
	private IComissaoFaturaService comissaoFaturaService;

	/** Instancia do serviço ItemFaturaDao. */
	@Autowired
	private IItemFaturaDao itemFaturaDao;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties appConfig;

	/** Intancia de mailSender. */
	private MailSenderUtil mailSender;

	@Autowired
	private IDealFiscalService dealFiscalService;

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

	/**
	 * Cria o uma comissao de acelerador.
	 * 
	 * @param cAcelerador
	 *            - entidade ComissaoAcelerador.
	 * 
	 * @return retorna true se criado com sucesso, caso contrario retorna false.
	 */
	public Boolean createComissaoAcelerador(final ComissaoAcelerador cAcelerador) {

		Comissao c = cAcelerador.getComissao();

		if (!c.getContratoPratica().getCpraticaDfiscals().isEmpty()) {

			// seta o tipo como camissao acelerador
			c.setIndicadorTipo(Constants.COMISSAO_TYPE_ACCELERATOR);

			// seta o percentual de imposto
			cAcelerador.setPercentualImposto(calculatePercentualTaxaComAcel(c));

			// calcula e seta a comissao na ComissaoAcelerador
			BigDecimal vlComissao = calculateValorComissao(c,
					calculateNetValue(cAcelerador));
			c.setValorComissao(vlComissao);
			// cria a comissão
			if (!createComissao(c)) {
				return Boolean.FALSE;
			}

			// cria a ComissaoAcelerador
			comissaoAceleradorDao.create(cAcelerador);

			return Boolean.TRUE;

		} else {
			Messages.showError("createComissaoAcelerador",
					Constants.COMISSAO_MSG_ERROR_DEAL_FISCAL_NOT_FOUND);

			return Boolean.FALSE;
		}
	}

	/**
	 * Cria uma comissao.
	 * 
	 * @param comissao
	 *            - comissão a ser criada
	 * 
	 * @return retorna true se realizado com sucesso, caso contrario false.
	 */
	private Boolean createComissao(final Comissao comissao) {
		return createComissao(comissao, null, null);
	}

	/**
	 * Cria uma comissao.
	 * 
	 * @param comissao
	 *            - comissão a ser criada
	 * @param comment
	 *            - comentário ao criar uma comssissão
	 * 
	 * @return retorna true se realizado com sucesso, caso contrario false.
	 */
	private Boolean createComissao(final Comissao comissao,
			final String comment, final ComissaoFatura comissaoFatura) {
		// seta o status como open
		comissao.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_OPEN);

		if (comissao.getPessoaAe() == null) {
			// seta o AE da comissao
			comissao.setPessoaAe(pessoaService.findPessoaByLogin(LoginUtil
					.getLoggedUsername()));
		}

		ContratoPratica cp = null;
		Pessoa pessoa = null;

		// seta o contrato-pratica
		if (comissao.getContratoPratica() != null) {
			cp = contratoPraticaService.findContratoPraticaById(comissao
					.getContratoPratica().getCodigoContratoPratica());
			pessoa = cp.getMsa().getPessoa();
		} else {
			pessoa = comissaoFatura.getItemFatura().getFatura().getDealFiscal()
					.getMsa().getPessoa();
		}

		comissao.setContratoPratica(cp);

		if (pessoa == null) {
			Messages.showError("createComissao",
					Constants.COMISSAO_MSG_ERROR_DN_NOT_FOUND);

			return false;
		}

		// seta o DN aprovador
		comissao.setPessoaAprovador(pessoa);
		// cria a comissao
		comissaoDao.create(comissao);

		createHistoricoComissao(comissao, comment);

		return true;
	}

	/**
	 * Realiza o update de uma comissão.
	 * 
	 * @param comissao
	 *            - entidade do tipo Comissão
	 * 
	 * @param comments
	 *            - comentário do update.
	 */
	private void updateComissao(final Comissao comissao, final String comments) {

		comissaoDao.update(comissao);

		createHistoricoComissao(comissao, comments);
	}

	/**
	 * Realiza o update do percentual de comissao.
	 * 
	 * @param comissao
	 *            - comissao a ser atualizada.
	 * @param valorTotal
	 *            - valor total da comissão.
	 */
	public void updatePercentualComissao(final Comissao comissao,
			final BigDecimal valorTotal) {

		BigDecimal valorComissao = calculateValorComissao(comissao, valorTotal);

		comissao.setValorComissao(valorComissao);

		Long codigoComissao = comissao.getCodigoComissao();
		if (codigoComissao != null) {
			Comissao c = comissaoDao.findById(codigoComissao);

			c.setValorComissao(valorComissao);
			c.setPercentualComissao(comissao.getPercentualComissao());

			comissaoDao.update(c);
		}
	}

	/**
	 * Cria um historico de comissão.
	 * 
	 * @param comissao
	 *            - comissao que se deseja criar o histórico
	 * @param coments
	 *            - comentário do histórico.
	 */
	private void createHistoricoComissao(final Comissao comissao,
			final String coments) {
		HistoricoComissao historico = new HistoricoComissao();

		historico.setCodigoAutorLogin(LoginUtil.getLoggedUsername());
		historico.setComissao(comissao);
		historico.setDataHistorico(new Date());
		historico.setIndicadorEstado(comissao.getIndicadorEstadoAtual());
		historico.setTextoComentario(coments);

		historicoComissaoDao.create(historico);
	}

	/**
	 * Busca pelo filtro uma comissao de acelerador.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param c
	 *            - cliente
	 * @param cp
	 *            - contrato pratica
	 * @param a
	 *            - acelerador
	 * @param ae
	 *            - Pessoa AE
	 * @param dn
	 *            - Pessoa DN
	 * @param status
	 *            - status da comissao.
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	public List<ComissaoRow> findComissaoAceleradorByFilterPerAe(
			final Date startDate, final Date endDate, final Cliente c,
			final ContratoPratica cp, final Acelerador a, final Pessoa ae,
			final Pessoa dn, final String status) {

		return generateComissaoRowList(comissaoAceleradorDao.findByFilter(
				startDate, endDate, c, cp, a, ae, dn, status));

	}

	/**
	 * Gera a lista com as linhas de comissões de acelerador.
	 * 
	 * @param comissaoAceleradorList
	 *            - lista de comissao acelerador
	 * 
	 * @return retorna uma lista de ComissaoRow
	 */
	private List<ComissaoRow> generateComissaoRowList(
			final List<ComissaoAcelerador> comissaoAceleradorList) {

		List<ComissaoRow> rowList = new ArrayList<ComissaoRow>();
		ComissaoRow row;

		for (ComissaoAcelerador comissaoAcelerador : comissaoAceleradorList) {
			row = new ComissaoRow();

			row.setComissaoAcelerador(comissaoAcelerador);
			row.setComissao(comissaoAcelerador.getComissao());
			row.setTotalAcumulado(calculateTotalAcumulado(comissaoAcelerador));
			row.setNetValue(calculateNetValue(comissaoAcelerador));
			rowList.add(row);
		}

		return rowList;
	}

	/**
	 * Calcula o valor liquido a ser comissionado (valor sem impostos).
	 * 
	 * @param ca
	 *            - ComissaoAcelerador
	 * 
	 * @return retorna o valor a ser comissionado
	 */
	private BigDecimal calculateNetValue(final ComissaoAcelerador ca) {
		return ca
				.getValorContrato()
				.multiply(
						BigDecimal.ONE.subtract(ca.getPercentualImposto()
								.movePointLeft(2)))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Busca pelo filtro uma comissao de acelerador.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param c
	 *            - cliente
	 * @param cp
	 *            - contrato pratica
	 * @param a
	 *            - acelerador
	 * @param ae
	 *            - pessoa AE
	 * @param status
	 *            - status da comissao.
	 * @param dn
	 *            - pessoa DN
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	public List<ComissaoRow> findComissaoAceleradorByFilterPerDn(
			final Date startDate, final Date endDate, final Cliente c,
			final ContratoPratica cp, final Acelerador a, final Pessoa ae,
			final String status, final Pessoa dn) {

		return generateComissaoRowList(comissaoAceleradorDao.findByFilter(
				startDate, endDate, c, cp, a, ae, dn, status));
	}

	/**
	 * Busca por ComissaoFatura pelo filtro.
	 * 
	 * @param itemFaturaList
	 *            - lista de item de fatura
	 * @param comissaoFaturaList
	 *            - lista de comissao fatura
	 * 
	 * @return retorna uma lista de ComissaoRow
	 */
	private List<ComissaoRow> findComissaoFaturaByFilter(
			final List<ItemFatura> itemFaturaList,
			final List<ComissaoFatura> comissaoFaturaList) {

		List<ComissaoRow> comissaoRowList = new ArrayList<ComissaoRow>();
		ComissaoRow row;
		ComissaoFatura cf;
		Comissao c;
		for (ItemFatura itemFatura : itemFaturaList) {
			row = new ComissaoRow();
			cf = new ComissaoFatura();
			c = new Comissao();
			cf.setComissao(c);
			row.setComissaoFatura(cf);

			// cria a comissao fatua
			cf.setItemFatura(itemFatura);
			cf.setValorItemFatura(calculateValorComissionavelFatura(itemFatura));

			// cria a comissao referente a fatura
			c.setDataComissao(itemFatura.getDataPagamento());
			c.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_OPEN);
			c.setIndicadorTipo(Constants.COMISSAO_TYPE_INVOICE);
			c.setPessoaAe(LoginUtil.getLoggedUser());
			c.setMoeda(itemFatura.getFatura().getMoeda());
			c.setPercentualComissao(itemFatura.getFatura().getDealFiscal()
					.getMsa().getPercentualComissao());
			c.setValorComissao(calculateValorComissao(c,
					cf.getValorItemFatura()));
			c.setPessoaAprovador(itemFatura.getFatura().getDealFiscal()
					.getMsa().getPessoa());

			row.setConvertedComissionValue(convertComissaoValueToDefaulCurrency(c));
			comissaoRowList.add(row);
		}

		for (ComissaoFatura comissaoFatura : comissaoFaturaList) {
			DealFiscal dealFiscal = dealFiscalService
					.findDealFiscalById(comissaoFatura.getItemFatura()
							.getFatura().getDealFiscal().getCodigoDealFiscal());
			comissaoFatura.getItemFatura().getFatura()
					.setDealFiscal(dealFiscal);
			row = new ComissaoRow();
			row.setComissaoFatura(comissaoFatura);
			row.setConvertedComissionValue(convertComissaoValueToDefaulCurrency(comissaoFatura
					.getComissao()));
			comissaoRowList.add(row);
		}

		return comissaoRowList;
	}

	/**
	 * Calcula o valor comissional de uma comisão fatura. valor comissionavel =
	 * (Valor do item da fatura - impostos)
	 * 
	 * @param itemFatura
	 *            - entidade ItemFatura
	 * 
	 * @return retorna o valor comissionável
	 */
	private BigDecimal calculateValorComissionavelFatura(
			final ItemFatura itemFatura) {

		ItemFatura item = itemFaturaDao.findById(itemFatura
				.getCodigoItemFatura());

		Empresa empresa = item.getFatura().getDealFiscal().getEmpresa();
		Cliente cliente = item.getFatura().getDealFiscal().getCliente();

		BigDecimal taxaTotal = BigDecimal.ZERO, valorComissionavel;

		// se cliente nacional
		if (Constants.CLIENTE_TYPE_NATIONAL.equals(cliente.getIndicadorTipo())) {

			// pega o percentual da taxa de CSLRF
			if (empresa.getValorTaxaCslrf() != null) {
				taxaTotal = taxaTotal.add(empresa.getValorTaxaCslrf());
			} else {
				return BigDecimal.ZERO;
			}

			// Pega o percentual da taxa imposto e adiciona a taxa do CSLRF
			TaxaImposto taxa = taxaService.findTaxaByEmpresaTipoServicoDate(
					empresa, item.getTipoServico(), item.getFatura()
							.getDataPrevisao());
			if (taxa != null) {

				BigDecimal taxaImp = new BigDecimal(0);
				if (taxa.getValorTaxaMunicipal() != null) {
					taxaImp = taxaImp.add(taxa.getValorTaxaMunicipal());
				}

				if (taxa.getValorTaxaFederal() != null) {
					taxaImp = taxaImp.add(taxa.getValorTaxaFederal());
				}

				// taxaTotal = taxaTotal.add(taxa.getValorTaxa());
				taxaTotal = taxaTotal.add(taxaImp);

				// Se não existir a taxa seta o percentrual da taxa igual a
				// zero.
			} else {
				return BigDecimal.ZERO;
			}

			// valorComissionavel = valorItem * (1 - taxaTotal/100)
			valorComissionavel = item
					.getValorItem()
					.multiply(
							BigDecimal.ONE.subtract(taxaTotal.movePointLeft(2)))
					.setScale(2, BigDecimal.ROUND_HALF_UP);

			// se cliente internacional
		} else {
			valorComissionavel = item.getValorItem();
		}

		return valorComissionavel;
	}

	/**
	 * Calcula o valor o percentual da Taxa: media de impostos dos Fiscals Deals
	 * associados ao Contract-LOB, sendo que o imposto do Fiscal Deal é a media
	 * dos impostos de seus Service Types associados + o Cslrf associado a
	 * empresa.
	 * 
	 * @param comissao
	 *            - Comissao que será usado para calcular Percentual Taxa de
	 *            imposto
	 * 
	 * @return o percentual da taxa calculada
	 */
	private BigDecimal calculatePercentualTaxaComAcel(final Comissao comissao) {
		// variaveis
		BigDecimal totalContratoPratica = BigDecimal.ZERO;
		int contDealFiscal = 0;

		// recupera a lista de DealFiscal do ContratoPratica corrente
		List<CpraticaDfiscal> cpraticaDealFiscalList = comissao
				.getContratoPratica().getCpraticaDfiscals();

		// itera sobre a lista de DealFiscal, para cada um, verifica as
		// associacoes dos TipoServico e verifica se tem TaxaImposto cadastrada
		// para a Empresa (do DealFiscal corrente) e TipoServico
		Iterator<CpraticaDfiscal> itCpraticaDealFiscal = cpraticaDealFiscalList
				.iterator();
		while (itCpraticaDealFiscal.hasNext()) {
			CpraticaDfiscal cpraticaDealFiscal = itCpraticaDealFiscal.next();
			DealFiscal dealFiscal = cpraticaDealFiscal.getDealFiscal();

			Cliente cliente = dealFiscal.getCliente();

			// se cliente nacional
			if (Constants.CLIENTE_TYPE_NATIONAL.equals(cliente
					.getIndicadorTipo())) {

				// testa se o DealFiscal é ativo, se nao desconsidera o
				// DealFiscal
				// corrente e vai para o próximo
				if (!Constants.ACTIVE.equals(dealFiscal.getIndicadorStatus())) {
					continue;
				}

				// variaveis
				BigDecimal totalDealFiscal = BigDecimal.ZERO;
				int contTipoServico = 0;

				// recupera a Empresa (do DealFiscal corrente)
				Empresa empresa = dealFiscal.getEmpresa();

				// recupera a lista de TipoServico (do DealFiscal corrente)
				List<TipoServico> tipoServicoList = dealFiscal
						.getTipoServicos();

				// itera sobre a lista de TipoServico e verifica a TaxaImposto
				// (para
				// o TipoServico e Empresa correntes)
				for (TipoServico tipoServico : tipoServicoList) {

					// busca a TaxaImposto conforme o filtro (filter)
					TaxaImposto taxaImposto = taxaService
							.findTaxaByEmpresaTipoServicoDate(empresa,
									tipoServico, comissao.getDataComissao());

					// se existir TaxaImposto cadastrada para o filtro,
					// soma no total do DealFiscal
					if (taxaImposto != null) {
						/*
						 * totalDealFiscal = totalDealFiscal.add(taxaImposto
						 * .getValorTaxa());
						 */
						BigDecimal taxa = new BigDecimal(0);
						if (taxaImposto.getValorTaxaMunicipal() != null) {
							taxa = taxa
									.add(taxaImposto.getValorTaxaMunicipal());
						}

						if (taxaImposto.getValorTaxaFederal() != null) {
							taxa = taxa.add(taxaImposto.getValorTaxaFederal());
						}
						totalDealFiscal = totalDealFiscal.add(taxa);

						contTipoServico++;
					}
				}

				// caso quantidade de TipoServico calculados seja maior que
				// zero, ou seja, caso
				// o total do DealFiscal seja maior do que zero, calcula a
				// media dos totais dos DealFiscal e soma ao total do
				// ContratoPratica - total DealFiscal div qtde TipoServico
				if (contTipoServico > 0) {
					BigDecimal mediaDealFiscal = totalDealFiscal.divide(
							BigDecimal.valueOf(contTipoServico), 2,
							BigDecimal.ROUND_HALF_UP);
					totalContratoPratica = totalContratoPratica
							.add(mediaDealFiscal);
					contDealFiscal++;
				}

				// pega a taxa do CSLRF
				BigDecimal valorTaxaCslrf = empresa.getValorTaxaCslrf();
				if (valorTaxaCslrf != null) {
					totalContratoPratica = totalContratoPratica
							.add(valorTaxaCslrf);
				}
			}
		}

		// caso a quantidade de DealFiscal calculados seja maior do que zero,
		// pega o somatorio de todas as medias dos DealFiscal e calcula a media
		// do ContratoPratica - total ContratoPratica div qtde DealFiscal
		if (contDealFiscal > 0) {
			BigDecimal mediaContratoPratica = totalContratoPratica.divide(
					BigDecimal.valueOf(contDealFiscal), 2,
					BigDecimal.ROUND_HALF_UP);

			return mediaContratoPratica;
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * Busca pelo filtro a comissão fatura por DN.
	 * 
	 * @param startDate
	 *            - data inicio
	 * @param endDate
	 *            - data fim
	 * @param c
	 *            - entidade do tipo Cliente
	 * @param cp
	 *            - entidade do tipo ContratoPratica
	 * @param ae
	 *            - pessoa AE
	 * @param status
	 *            - status
	 * @param dn
	 *            - pessoa DN
	 * 
	 * @return retorna uma lista de ComissaoRow, referente ao resultado da
	 *         busca.
	 */
	public List<ComissaoRow> findComissaoFaturaByFilterDn(final Date startDate,
			final Date endDate, final Cliente c, final ContratoPratica cp,
			final Pessoa ae, final String status, final Pessoa dn) {

		List<ItemFatura> itemFaturaList = new ArrayList<ItemFatura>();
		if (status == null || Constants.COMISSAO_STATUS_OPEN.equals(status)) {
			itemFaturaList = itemFaturaDao.findByFilterComissao(startDate,
					endDate, c, cp, ae);
		}

		List<ComissaoFatura> comissaoFaturaList = comissaoFaturaDao
				.findByFilter(startDate, endDate, c, cp, ae, dn, status);

		return findComissaoFaturaByFilter(itemFaturaList, comissaoFaturaList);
	}

	/**
	 * Busca pelo filtro a comissão fatura por AE.
	 * 
	 * @param startDate
	 *            - data inicio
	 * @param endDate
	 *            - data fim
	 * @param status
	 *            - status
	 * @param loginAe
	 *            - {@link Pessoa} AE
	 * @param loginDn
	 *            - {@link Pessoa} DN
	 * 
	 * @return retorna uma lista de ComissaoRow, referente ao resultado da
	 *         busca.
	 */
	public List<ComissaoRow> findComissaoFaturaByFilterAe(final Date startDate,
			final Date endDate, final String status, final String loginAe,
			final String loginDn, final Msa msa, final Long invoiceCode) {

		List<ItemFatura> itemFaturaList = new ArrayList<ItemFatura>();
		if (status == null || Constants.COMISSAO_STATUS_OPEN.equals(status)) {
			itemFaturaList = itemFaturaDao.findByFilterComissao(startDate,
					endDate, loginAe, msa, invoiceCode);
		}

		List<ComissaoFatura> comissaoFaturaList = comissaoFaturaDao
				.findByFilter(startDate, endDate, loginDn, loginAe, status,
						msa, invoiceCode);

		return findComissaoFaturaByFilter(itemFaturaList, comissaoFaturaList);
	}

	/**
	 * Calcula o total acumulado.
	 * 
	 * @param ca
	 *            - entidade do tipo ComissaoAcelerador
	 * 
	 * @return retorna o total acumulado.
	 */
	public BigDecimal calculateTotalAcumulado(final ComissaoAcelerador ca) {
		return comissaoAceleradorDao.getTotalAcumulado(comissaoAceleradorDao
				.findById(ca.getCodigoComissaoAcel()));
	}

	/**
	 * Busca pelo filtro uma comissao de acelerador.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param ae
	 *            - AE
	 * @param status
	 *            - status da comissao.
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	public List<Comissao> findComissaoByFilterDp(final Date startDate,
			final Date endDate, final Pessoa ae, final String status) {

		return comissaoDao.findByFilterDp(startDate, endDate, ae, null, status);
	}

	/**
	 * Cria o uma comissao de fatura.
	 * 
	 * @param cFatura
	 *            - entidade ComissaoFatura.
	 * @param comments
	 *            - Comentário
	 * 
	 * @return retorna true se criado com sucesso, caso contrario retorna false.
	 */
	public Boolean createComissaoFatura(final ComissaoFatura cFatura,
			final String comments) {

		ItemFatura item = itemFaturaDao.findById(cFatura.getItemFatura()
				.getCodigoItemFatura());

		List<ComissaoFatura> comissoes = comissaoFaturaService
				.findByItemFatura(item);

		if (comissoes.isEmpty()) {
			Comissao comissao = cFatura.getComissao();
			comissao.setValorComissao(calculateValorComissao(comissao,
					cFatura.getValorItemFatura()));
			// seta o tipo como camissao acelerador
			comissao.setIndicadorTipo(Constants.COMISSAO_TYPE_INVOICE);

			createComissao(comissao, null, cFatura);

			cFatura.setItemFatura(item);
			cFatura.setValorItemFatura(calculateValorComissionavelFatura(item));

			comissaoFaturaDao.create(cFatura);

			comissao.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_REQUEST);

			updateComissao(comissao, comments);
		} else {
			Messages.showWarning("createComissaoFatura",
					Constants.COMISSAO_ALREADY_EXISTS);

			return Boolean.valueOf(false);
		}

		return Boolean.valueOf(true);
	}

	/**
	 * Calcula o valor da comissão.
	 * 
	 * @param comissao
	 *            - entidade do tipo Comissao
	 * @param valorTotal
	 *            - valor total referente a comissão
	 * 
	 * @return retorna o valor calculado da comissão
	 */
	private BigDecimal calculateValorComissao(final Comissao comissao,
			final BigDecimal valorTotal) {

		BigDecimal percentualComissao = comissao.getPercentualComissao();
		if (percentualComissao == null) {
			percentualComissao = BigDecimal.ZERO;
		}

		return valorTotal.multiply(percentualComissao.movePointLeft(2))
				.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Realiza a converção do valor da comissao para a moeda padrão.
	 * 
	 * @param comissao
	 *            - Entidade do tipo Comissao
	 * 
	 * @return retorna o valor da comissao convertido
	 */
	public BigDecimal convertComissaoValueToDefaulCurrency(
			final Comissao comissao) {

		Long defaultCurrencyId = Long.parseLong((String) appConfig
				.get(Constants.DEFAULT_PROPERTY_CURRENCY_CODE));

		BigDecimal valorComissao = BigDecimal.ZERO;

		if (comissao.getMoeda().getCodigoMoeda().equals(defaultCurrencyId)) {
			valorComissao = comissao.getValorComissao();
		} else {

			CotacaoMoeda cotacao = cotacaoMoedaService
					.findCotacaoMoedaByMoedaMes(comissao.getMoeda(),
							comissao.getDataComissao());

			if (cotacao != null) {
				valorComissao = cotacao.getValorCotacao()
						.multiply(comissao.getValorComissao())
						.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}

		return valorComissao;

	}

	/**
	 * Altera o status das comissões para status aprovado.
	 * 
	 * @param rowList
	 *            - Lista de comissões
	 * 
	 * @return retorna true se alguma comissao foi selecionada, caso contrario
	 *         false.
	 */
	public Boolean setAllComissaoAceleradorToApprove(
			final List<ComissaoRow> rowList) {
		boolean success = false;

		for (ComissaoRow row : rowList) {
			// verifica se a linha foi selecionada
			if (row.getIsSelected()) {
				ComissaoAcelerador comissaoAcelerador = row
						.getComissaoAcelerador();

				Comissao c = comissaoAceleradorDao.findById(
						comissaoAcelerador.getCodigoComissaoAcel())
						.getComissao();
				// verifica se a comissão esta no status open
				if (Constants.COMISSAO_STATUS_OPEN.equals(c
						.getIndicadorEstadoAtual())) {
					c.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_REQUEST);

					updateComissao(c, null);

					success = true;
				}
			}
		}

		return success;
	}

	/**
	 * Altera o status.
	 * 
	 * @param rowList
	 *            - Lista com as comissões
	 * @param status
	 *            - valor do status para qual se deseja alterar
	 * @param comment
	 *            - comentário sobre a alteração do status
	 * 
	 * @return retorna true se alterado com sucesso, casocontrario false
	 */
	public Boolean changeStatus(final List<ComissaoRow> rowList,
			final String status, final String comment) {

		boolean success = false;
		String mailTo = "ronaldon@ciandt.com";

		for (ComissaoRow row : rowList) {
			// verifica se a linha foi selecionada
			if (row.getIsSelected()) {
				Comissao c = row.getComissao();

				if (c.getCodigoComissao() == null
						&& Constants.COMISSAO_TYPE_INVOICE.equals(c
								.getIndicadorTipo())
						&& Constants.COMISSAO_STATUS_REQUEST.equals(status)) {
					ComissaoFatura comissaoFatura = row.getComissaoFatura();

					if (!createComissaoFatura(comissaoFatura, comment)) {
						return Boolean.valueOf(false);
					}

					mailTo = c.getPessoaAprovador().getTextoEmail();

					success = true;
				} else {
					c = comissaoDao.findById(c.getCodigoComissao());

					// verifica se a comissão esta no status open
					if (Constants.COMISSAO_STATUS_OPEN.equals(status)) {
						if (Constants.COMISSAO_STATUS_REQUEST.equals(c
								.getIndicadorEstadoAtual())) {

							if (Constants.COMISSAO_TYPE_INVOICE.equals(c
									.getIndicadorTipo())) {
								comissaoFaturaDao.remove(comissaoFaturaDao
										.findById(row.getComissaoFatura()
												.getCodigoComissaoFatura()));
							} else {
								c.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_OPEN);

								updateComissao(c, comment);
							}

							mailTo = c.getPessoaAe().getCodigoLogin();

							success = true;
						}
					} else if (Constants.COMISSAO_STATUS_REQUEST.equals(status)) {
						if (Constants.COMISSAO_STATUS_OPEN.equals(c
								.getIndicadorEstadoAtual())
								|| Constants.COMISSAO_STATUS_REVIEW.equals(c
										.getIndicadorEstadoAtual())) {

							c.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_REQUEST);

							updateComissao(c, comment);

							mailTo = c.getPessoaAprovador().getTextoEmail();

							success = true;
						}
					} else if (Constants.COMISSAO_STATUS_REVIEW.equals(status)) {
						if (Constants.COMISSAO_STATUS_REQUEST.equals(c
								.getIndicadorEstadoAtual())) {

							c.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_REVIEW);

							updateComissao(c, comment);

							mailTo = c.getPessoaAe().getCodigoLogin();

							success = true;
						}
					} else if (Constants.COMISSAO_STATUS_APPROVED
							.equals(status)) {

						if (Constants.COMISSAO_STATUS_REQUEST.equals(c
								.getIndicadorEstadoAtual())) {

							c.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_APPROVED);

							updateComissao(c, comment);

							mailTo = appConfig
									.getProperty("mail.address.dp.to");

							success = true;
						}

					} else if (Constants.COMISSAO_STATUS_COMISSIONED
							.equals(status)) {
						if (Constants.COMISSAO_STATUS_APPROVED.equals(c
								.getIndicadorEstadoAtual())
								|| Constants.COMISSAO_STATUS_FORWARDED.equals(c
										.getIndicadorEstadoAtual())) {

							c.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_COMISSIONED);

							updateComissao(c, comment);

							mailTo = c.getPessoaAe().getCodigoLogin();

							success = true;
						}

					} else if (Constants.COMISSAO_STATUS_FORWARDED
							.equals(status)) {
						if (Constants.COMISSAO_STATUS_APPROVED.equals(c
								.getIndicadorEstadoAtual())
								|| Constants.COMISSAO_STATUS_COMISSIONED
										.equals(c.getIndicadorEstadoAtual())) {

							c.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_FORWARDED);

							updateComissao(c, comment);

							mailTo = c.getPessoaAe().getCodigoLogin();

							success = true;
						}
					}

					row.setComissao(c);
				}

			}
		}

		if (success) {
			sendComissionMail(status, mailTo);
		}

		return success;
	}

	/**
	 * Envia um email com os dados de uma comissão, para o email passado no
	 * parametro 'to'.
	 * 
	 * @param status
	 *            - Status da comissão
	 * @param to
	 *            - email que será enviado
	 */
	private void sendComissionMail(final String status, final String to) {

		StatusComissaoConverter statusConverter = new StatusComissaoConverter();
		String statusStr = statusConverter.getAsString(status);
		String username = LoginUtil.getLoggedUsername();

		Map<String, Object> mailDataSource = new HashMap<String, Object>();
		mailDataSource.put("username", username);
		mailDataSource.put("status", statusStr);

		// pega o tamplete de email e insere os dados
		String message = mailSender.getTemplateMailMessage("comissao.vm",
				mailDataSource);

		String subject = BundleUtil.getBundle("_nls.comissao.mail.subject",
				statusStr, username);

		// envia o email
		mailSender.sendHtmlMail(to, subject, message);
	}

	/**
	 * Realiza o update de uma ComissaoAcelerador.
	 * 
	 * @param comissaoAcelerador
	 *            - entidade do tipo ComissaoAcelerador.
	 * 
	 * @return retorna true se update realizado com sucesso, caso contrario
	 *         false.
	 */
	public Boolean updateComissaoAcelerador(
			final ComissaoAcelerador comissaoAcelerador) {

		Comissao comissao = comissaoAcelerador.getComissao();
		BigDecimal valorComissao = calculateValorComissao(comissao,
				calculateNetValue(comissaoAcelerador));

		comissao.setValorComissao(valorComissao);

		comissaoAceleradorDao.update(comissaoAcelerador);

		updateComissao(comissao, null);

		return Boolean.TRUE;
	}

	/**
	 * Realiza o update de uma ComissaoFatura.
	 * 
	 * @param comissaoFatura
	 *            - entidade do tipo ComissaoFatura.
	 * 
	 * @return retorna true se update realizado com sucesso, caso contrario
	 *         false.
	 */
	public Boolean updateComissaoFatura(final ComissaoFatura comissaoFatura) {
		Comissao comissao = comissaoFatura.getComissao();

		BigDecimal valorComissao = calculateValorComissao(comissao,
				comissaoFatura.getValorItemFatura());

		comissao.setValorComissao(valorComissao);

		// cria uma comissao fatura
		if (comissaoFatura.getCodigoComissaoFatura() == null) {
			if (!Constants.COMISSAO_STATUS_OPEN.equals(comissao
					.getIndicadorEstadoAtual())) {
				createComissaoFatura(comissaoFatura, null);
			}
		} else {
			// remove uma comissao fatura
			if (Constants.COMISSAO_STATUS_OPEN.equals(comissao
					.getIndicadorEstadoAtual())) {

				removeComissaoFatura(comissaoFatura);
				// realiza o update da comissao fatura
			} else {
				Comissao c = findComissaoById(comissao.getCodigoComissao());
				c.setValorComissao(valorComissao);
				c.setIndicadorEstadoAtual(comissao.getIndicadorEstadoAtual());
				updateComissao(c, null);
			}

		}

		return Boolean.TRUE;
	}

	/**
	 * Deleta uma comissao acelerador.
	 * 
	 * @param comissaoAcelerador
	 *            - entidade a ser removida
	 * 
	 * @return retorna true se removida com sucesso, caso contrario false.
	 */
	public Boolean removeComissaoAcelerador(
			final ComissaoAcelerador comissaoAcelerador) {

		ComissaoAcelerador ca = comissaoAceleradorDao
				.findById(comissaoAcelerador.getCodigoComissaoAcel());

		if (canRemoveComissao(ca.getComissao())) {
			comissaoAceleradorDao.remove(ca);

			return true;
		}

		return false;
	}

	/**
	 * Remove uma ComissaoFatura.
	 * 
	 * @param comissaoFatura
	 *            - Entidade a ser removida
	 * 
	 * @return retorna true se removido com sucesso, caso contrario false
	 */
	private Boolean removeComissaoFatura(final ComissaoFatura comissaoFatura) {
		ComissaoFatura cf = comissaoFaturaDao.findById(comissaoFatura
				.getCodigoComissaoFatura());

		if (canRemoveComissao(cf.getComissao())) {
			comissaoFaturaDao.remove(cf);

			return true;
		}

		return false;
	}

	/**
	 * Verifica se a comissao pode ser removida.
	 * 
	 * @param c
	 *            - comissao a ser verificada.
	 * 
	 * @return retorna true caso possa ser removida, caso contrario false.
	 */
	private Boolean canRemoveComissao(final Comissao c) {

		String status = c.getIndicadorEstadoAtual();

		if (status.equals(Constants.COMISSAO_BUNDLE_STATUS_COMISSIONED)
				|| status.equals(Constants.COMISSAO_BUNDLE_STATUS_FORWARDED)) {
			return false;
		}

		return true;
	}

	/**
	 * Retorna a entidade referente ao id passado por parametro.
	 * 
	 * @param id
	 *            - Id da entidade
	 * 
	 * @return retorna uma entidade do tipo ComissaoAcelerador.
	 */
	public ComissaoAcelerador findComissaoAceleradorById(final Long id) {
		return comissaoAceleradorDao.findById(id);
	}

	/**
	 * Retorna a entidade referente ao id passado por parametro.
	 * 
	 * @param id
	 *            - Id da entidade
	 * 
	 * @return retorna uma entidade do tipo ComissaoFatura.
	 */
	public ComissaoFatura findComissaoFaturaById(final Long id) {
		return comissaoFaturaDao.findById(id);
	}

	/**
	 * Retorna a entidade referente ao id passado por parametro.
	 * 
	 * @param id
	 *            - Id da entidade
	 * 
	 * @return retorna uma entidade do tipo Comissao.
	 */
	public Comissao findComissaoById(final Long id) {
		return comissaoDao.findById(id);
	}

}
