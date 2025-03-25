/**
 * Classe ReceitaService - Implementacao do servico da Receita
 */
package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.audit.listener.AuditoriaJpaListener;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.integration.queue.RevenueProducer;
import com.ciandt.pms.integration.vo.IncomingRevenue;
import com.ciandt.pms.integration.vo.Installment;
import com.ciandt.pms.integration.vo.RevenueItem;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ItemReceitaRow;
import com.ciandt.pms.model.vo.ReceitaDealFiscalRow;
import com.ciandt.pms.model.vo.ReceitaFilter;
import com.ciandt.pms.model.vo.ReceitaMoedaRow;
import com.ciandt.pms.persistence.dao.IReceitaDao;
import com.ciandt.pms.persistence.dao.IVwItemReceitaDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * A classe ReceitaService proporciona as funcionalidades de servico para a
 * entidade Receita.
 *
 * @since 28/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 *
 */
@Service
public class ReceitaService implements IReceitaService {

	private static final String SYSTEM_LOGIN = "system";

	/** Instancia do DAO da entidade. */
	@Autowired
	private IReceitaDao receitaDao;

	/** Instancia do DAO da entidade VwItemReceita. */
	@Autowired
	private IVwItemReceitaDao vwItemReceitaDao;

	/** Instancia do servico ItemReceita. */
	@Autowired
	private IItemReceitaService itemReceitaService;

	/** Instancia do servico ReceitaDealFiscal. */
	@Autowired
	private IReceitaDealFiscalService receitaDealFiscalService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	@Autowired
	private ICidadeBaseService cidadeBaseService;

	/** Instancia do servico PerfilVendido. */
	@Autowired
	private IPerfilVendidoService perfilVendidoService;

	/** Instancia do servico ItemTabelaPreco. */
	@Autowired
	private IItemTabelaPrecoService itemTabelaPrecoService;

	/** Instancia do servico ContratoPratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do servico Alocacao. */
	@Autowired
	private IAlocacaoService alocacaoService;

	/** Instancia do servico AlocacaoPeriodo. */
	@Autowired
	private IAlocacaoPeriodoService alocacaoPeriodoService;

	/** Instancia do servico Etiqueta. */
	@Autowired
	private IEtiquetaService etiquetaService;

	/** Instancia do servico ReceitaDealFiscal. */
	@Autowired
	private IReceitaDealFiscalService rdfService;

	/** Instancia do servico FatorUrMes. */
	@Autowired
	private IFatorUrMesService fatorUrMesService;

	/** Instancia do servico MapaAlocacao. */
	@Autowired
	private IMapaAlocacaoService mapaAlocacaoService;

	/** Instancia do servico Moeda. */
	@Autowired
	private IMoedaService moedaService;

	/** Instancia do servico ReceitaMoeda. */
	@Autowired
	private IReceitaMoedaService receitaMoedaService;

	/** Instancia do servico HistoricoReceita. */
	@Autowired
	private IHistoricoReceitaService hiReService;

	@Autowired
	private IHistoricoPercentualIntercompService historicoPercentualIntercompService;

	/** Instancia do servico MsaService. */
	@Autowired
	private IMsaService msaService;

	@Autowired
	private ICotacaoMoedaMediaService cotacaoMoedaMediaService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Intancia que realiza a auditoria (Log). */
	private AuditoriaJpaListener auditoria = new AuditoriaJpaListener();

	/** Instancia do servico do ReceitaResultado. */
	@Autowired
	private IReceitaResultadoService receitaResultadoService;

	/** Instancia do servico de AjusteReceita. */
	@Autowired
	private IAjusteReceitaService ajusteReceitaSerice;

	/** Instancia do servico de ReceitaLicenca. */
	@Autowired
	private IReceitaLicencaService receitaLicencaService;

	/** Instancia do servico de Cliente. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do servico de DealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/** Instancia do servico de CpraticaDfiscalService. */
	@Autowired
	private ICpraticaDfiscalService cpraticaDfiscalService;

	/** Instancia do servico de ReceitaPlantao. */
	@Autowired
	private IReceitaPlantaoService receitaPlantaoService;

	@Autowired
	private IPessoaCidadeBaseService pessoaCidadeBaseService;

	@Autowired
	private IDiasUteisCidadeBaseService diasUteisCidadeBaseService;

	@Autowired
	private IPessoaTipoContratoService pessoaTipoContratoService;

	@Autowired
	private IEmpresaService empresaService;

	@Autowired
	private RevenueProducer revenueProducer;

	@Autowired
	private MailSenderUtil mailSenderUtil;

	/**
	 * Insere uma entidade.
	 *
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createReceita(final Receita entity) {
		// seta o criador da receita
		if (entity.getCodigoLoginCriador() == null
				|| entity.getCodigoLoginCriador() == "") {
			entity.setCodigoLoginCriador(LoginUtil.getLoggedUsername());
		}

		// seta a data de criacao e atualizacao como a data atual
		Date data = new Date();
		entity.setDataCriacao(data);
		entity.setDataAtualizacao(data);

		// cria a receita
		receitaDao.create(entity);

		// realiza o log de auditoria da receita
		auditoria.postPersist(entity);

		// cria o HistoricoReceita caso tenha alterado o status
		this.createHistoricoReceita(entity, Constants.VERSION_UNAVAILABLE);
	}

	/**
	 * Atualiza a entidade.
	 *
	 * @param entity
	 *            entidade a ser atualizada.
	 * @param indStatusOld
	 *            indicador de Status antes da atualizacao
	 */
	public void updateReceita(final Receita entity, final String indStatusOld) {
		// log de auditoria
		auditoria.preUpdate(entity);

		// seta a data de atualizacao da receita
		entity.setDataAtualizacao(new Date());

		// realiza o update
		receitaDao.update(entity);

		// cria o HistoricoReceita
		if (!indStatusOld.equals(entity.getIndicadorVersao())) {
			this.createHistoricoReceita(entity, indStatusOld);
		}
	}

	@Transactional
	public void updateStatusReceita(Long revenueCode, String revenueStatus) {
		Receita receita = receitaDao.findById(revenueCode);
		String statusOld = receita.getIndicadorVersao();
		receita.setIndicadorVersao(revenueStatus);

		this.updateReceita(receita, statusOld);
	}



	/**
	 * Cria o HistoricoReceita da alteracao de status da Receita.
	 *
	 * @param entity
	 *            - Receita corrente
	 * @param indStatusOld
	 *            - Status anterior a alteracao
	 */
	public void createHistoricoReceita(final Receita entity,
									   final String indStatusOld) {
		String loggedUsername = LoginUtil.getLoggedUsername();

		if (loggedUsername == null || loggedUsername.isEmpty()) {
			loggedUsername = SYSTEM_LOGIN;
		}
		// pega o novo status da Receita
		String indStatusCurrent = entity.getIndicadorVersao();
		HistoricoReceita hiRe = new HistoricoReceita();
		hiRe.setReceita(entity);
		if (entity.getCodigoLoginCriador() == null
				|| entity.getCodigoLoginCriador() == "") {
			entity.setCodigoLoginCriador(LoginUtil.getLoggedUsername());
		}

		hiRe.setCodigoAutor(loggedUsername);
		hiRe.setDataAlteracao(new Date());
		hiRe.setIndicadorVersaoAnterior(indStatusOld);
		hiRe.setIndicadorVersaoAtual(indStatusCurrent);
		hiReService.createHistoricoReceita(hiRe);
	}

	/**
	 * Remove a entidade.
	 *
	 * @param entity
	 *            entidade a ser removida.
	 */
	public void removeReceita(final Receita entity) {
		// pega as horas faturadas
		Receita receita = this.findReceitaById(entity.getCodigoReceita());

		// itera a lista de entidades filhas e exclui uma a uma
		for (ReceitaMoeda receitaMoeda : receita.getReceitaMoedas()) {
			for (ItemReceita itemReceita : receitaMoeda.getItemReceitas()) {
				itemReceitaService.removeItemReceita(itemReceita);
			}

			for (ReceitaDealFiscal receitaDealFiscal : receitaMoeda
					.getReceitaDealFiscals()) {
				receitaDealFiscalService
						.removeReceitaDealFiscal(receitaDealFiscal);
			}

			receitaMoedaService.removeReceitaMoeda(receitaMoeda);
		}

		auditoria.postRemove(receita);

		// itera a lista de HistoricoReceita e exclui cada entidade, uma a uma
		List<HistoricoReceita> historicoReceitaList = entity
				.getHistoricoReceitas();
		for (HistoricoReceita hiRe : historicoReceitaList) {
			hiReService
					.removeHistoricoReceita(hiReService
							.findHistoricoReceitaById(hiRe
									.getCodigoHistoricoReceita()));
		}

		// remove Receita
		receitaDao.remove(receita);
	}

	/**
	 * Busca pelo id da entidade.
	 *
	 * @param entityId
	 *            id da entidade
	 *
	 * @return retorna a entidade
	 */
	public Receita findReceitaById(final Long entityId) {
		return receitaDao.findById(entityId);
	}

	public Long findByRevenueDealFiscal(final Long revenueDealFiscalCode) {
		return receitaDao.findByRevenueDealFiscal(revenueDealFiscalCode);
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 *
	 * @param natureza
	 *            entidade NaturezaCentroLucro.
	 * @param cl
	 *            - entidade CentroLucro.
	 * @param p
	 *            - entidade Pratica.
	 * @param cli
	 *            - entidade Cliente.
	 * @param status
	 *            - valor do Status.
	 * @param dataMes
	 *            - valor da data (mes/ano)
	 *
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<ReceitaFilter> findReceitaByFilter(
			final NaturezaCentroLucro natureza, final CentroLucro cl,
			final Pratica p, final Cliente cli, final String status,
			final Date dataMes) {

		List<ReceitaFilter> resultList = new ArrayList<ReceitaFilter>();
		List<Receita> receitaList = new ArrayList<Receita>();
		List<MapaAlocacao> mapaList = new ArrayList<MapaAlocacao>();
		List<ReceitaLicenca> receitaLicencas = new ArrayList<ReceitaLicenca>();

		// pega as Receita que existem de acordo com o filtro
		if (!Constants.VERSION_UNAVAILABLE.equals(status)) {
			Receita filter = new Receita();
			ContratoPratica cp = new ContratoPratica();
			cp.setPratica(p);
			filter.setContratoPratica(cp);
			filter.setIndicadorVersao(status);
			filter.setDataMes(dataMes);

			receitaList = this.findReceitaByFilter(filter, cli, cl, natureza);
		}
		// pega todos os mapas que nao possuem receita
		if (status == null || Constants.VERSION_UNAVAILABLE.equals(status)) {
			mapaList = mapaAlocacaoService
					.findMapaAlocacaoByFilterWithoutRevenue(cli, natureza, cl,
							p, dataMes, status);
		}

		if (!Constants.VERSION_UNAVAILABLE.equals(status)) {
			ReceitaLicenca filter = new ReceitaLicenca();
			ContratoPratica cp = new ContratoPratica();
			cp.setPratica(p);
			filter.setContratoPratica(cp);
			filter.setIndicadorVersao(status);
			filter.setDataMes(dataMes);

			receitaLicencas = receitaLicencaService.findByFormFilter(filter,
					cli, cl, natureza);
		}

		ReceitaFilter filter;
		// adiciona na lista de retorno
		for (Receita receita : receitaList) {

			// indica se esta receita moeda e a primeira
			Boolean isFirstReceitaMoeda = Boolean.TRUE;

			for (ReceitaMoeda receitaMoeda : receita.getReceitaMoedas()) {
				ContratoPratica cp = receita.getContratoPratica();

				filter = new ReceitaFilter();
				// seta o ContratoPratica
				filter.setCodigoContratoPratica(cp.getCodigoContratoPratica());
				filter.setNomeContratoPratica(cp.getNomeContratoPratica());
				// seta o cliente
				Cliente c = cp.getMsa().getCliente();
				filter.setCodigoCliente(c.getCodigoCliente());
				filter.setNomeCliente(c.getNomeCliente());

				filter.setIndicadorVersao(receita.getIndicadorVersao());
				filter.setCodigoReceita(receita.getCodigoReceita());

				filter.setReceitaMoedas(receita.getReceitaMoedas());
				filter.setReceitaMoeda(receitaMoeda);

				String nacionality = Constants.CLIENTE_TYPE_NATIONAL;
				List<CpraticaDfiscal> cpraticaDfiscals = cpraticaDfiscalService
						.findByContratoPratica(cp);
				List<DealFiscal> dealFiscals = null;
				for (CpraticaDfiscal cpraticaDfiscal : cpraticaDfiscals) {
					DealFiscal dealFiscal = dealFiscalService
							.findDealFiscalById(cpraticaDfiscal.getDealFiscal()
									.getCodigoDealFiscal());

					if (dealFiscal.getIndicadorIntercompany()) {
						nacionality = Constants.CLIENTE_TYPE_INTERNATIONAL;
					}
				}
				// verifica se a data da receita esta antes do closing date.
				filter.setAfterClosingDate(moduloService
						.isDateAfterRevenueClosingDate(dataMes, nacionality));

				filter.setNomeMoeda(receitaMoeda.getMoeda().getNomeMoeda());
				filter.setTipoReceita(Constants.RECEITA_TYPE_SERVICE);
				if (receitaMoeda.getTotalAjuste() != null) {
					filter.setValorReceita(receitaMoeda.getTotalAjuste()
							+ receitaMoeda.getValorTotalMoeda().doubleValue()
							+ receitaMoeda.getTotalReceitaPlantao());
				} else {
					filter.setValorReceita(receitaMoeda.getValorTotalMoeda()
							.doubleValue());
				}

				if (receitaMoeda.getTotalReceitaPlantao() != null) {
					filter.setValorReceita(filter.getValorReceita()
							+ receitaMoeda.getTotalReceitaPlantao());
				}

				// verifica se e a primeira ReceitaMoeda
				if (isFirstReceitaMoeda) {
					// se for, mostra a linha
					filter.setShowRow(Boolean.TRUE);
					filter.setRowspan(receita.getReceitaMoedas().size());
					isFirstReceitaMoeda = Boolean.FALSE;
				} else {
					filter.setShowRow(Boolean.FALSE);
				}

				resultList.add(filter);
			}
		}

		// setas receitas futuras, nao foram criadas ainda
		for (MapaAlocacao mapaAlocacao : mapaList) {
			ContratoPratica cp = mapaAlocacao.getContratoPratica();

			filter = new ReceitaFilter();
			// seta o ContratoPratica
			filter.setCodigoContratoPratica(cp.getCodigoContratoPratica());
			filter.setNomeContratoPratica(cp.getNomeContratoPratica());
			// seta o cliente
			Cliente cliente = cp.getMsa().getCliente();
			filter.setCodigoCliente(cliente.getCodigoCliente());
			filter.setNomeCliente(cliente.getNomeCliente());

			String nacionality = Constants.CLIENTE_TYPE_NATIONAL;
			List<CpraticaDfiscal> cpraticaDfiscals = cpraticaDfiscalService
					.findByContratoPratica(cp);
			List<DealFiscal> dealFiscals = null;
			for (CpraticaDfiscal cpraticaDfiscal : cpraticaDfiscals) {
				DealFiscal dealFiscal = dealFiscalService
						.findDealFiscalById(cpraticaDfiscal.getDealFiscal()
								.getCodigoDealFiscal());

				if (dealFiscal.getIndicadorIntercompany()) {
					nacionality = Constants.CLIENTE_TYPE_INTERNATIONAL;
				}
			}

			// verifica se a data da receita esta antes do closing date.
			filter.setAfterClosingDate(moduloService
					.isDateAfterRevenueClosingDate(dataMes, nacionality));

			// seta a versao como 'Missing'
			filter.setIndicadorVersao(Constants.VERSION_UNAVAILABLE);
			// seta o codigo com -1 pois nao existe
			// Receita para o mes escolhido na busca
			filter.setCodigoReceita(-1L);
			filter.setShowRow(Boolean.TRUE);
			filter.setTipoReceita(Constants.RECEITA_TYPE_SERVICE);

			resultList.add(filter);
		}

		for (ReceitaLicenca receitaLicenca : receitaLicencas) {

			Cliente cliente = clienteService.findClienteById(receitaLicenca
					.getContratoPratica().getMsa().getCliente()
					.getCodigoCliente());

			filter = new ReceitaFilter();
			filter.setCodigoContratoPratica(receitaLicenca.getContratoPratica()
					.getCodigoContratoPratica());
			filter.setNomeContratoPratica(receitaLicenca.getContratoPratica()
					.getNomeContratoPratica());
			filter.setCodigoCliente(cliente.getCodigoCliente());
			filter.setNomeCliente(receitaLicenca.getContratoPratica().getMsa()
					.getCliente().getNomeCliente());
			filter.setIndicadorVersao(receitaLicenca.getIndicadorVersao());
			filter.setCodigoReceita(receitaLicenca.getCodigoReceitaLicenca());
			filter.setNomeMoeda(receitaLicenca.getMoeda().getNomeMoeda());
			filter.setShowRow(Boolean.TRUE);
			filter.setTipoReceita(Constants.RECEITA_TYPE_LICENCE);
			filter.setValorReceita(receitaLicenca.getValorReceita().doubleValue());

			String nacionality = Constants.CLIENTE_TYPE_NATIONAL;
			if (receitaLicenca.getDealFiscal().getIndicadorIntercompany()) {
				nacionality = Constants.CLIENTE_TYPE_INTERNATIONAL;
			}
			filter.setAfterClosingDate(moduloService
					.isDateAfterRevenueClosingDate(dataMes, nacionality));

			resultList.add(filter);
		}

		// ordena a lista
		Collections.sort(resultList);

		return resultList;

	}

	/**
	 * Busca uma lista de entidades pelo filtro. Tambem carrega asentidades
	 * relacionadas.
	 *
	 * @param filter
	 *            entidade populada com os valores do filtro e carrega.
	 * @param cli
	 *            entidade do tipo Cliente.
	 * @param cl
	 *            entidade do tipo CentroLucro
	 * @param natureza
	 *            entidade do tipo NaturezaCentroLucro
	 *
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Receita> findReceitaByFilter(final Receita filter,
											 final Cliente cli, final CentroLucro cl,
											 final NaturezaCentroLucro natureza) {

		List<Receita> resultList = receitaDao.findByFilter(filter, cli, cl,
				natureza);

		for (Receita receita : resultList) {
			Hibernate.initialize(receita.getReceitaMoedas());
			for (ReceitaMoeda receitaMoeda : receita.getReceitaMoedas()) {
				Hibernate.initialize(receitaMoeda);
				Hibernate.initialize(receitaMoeda.getMoeda());
			}
		}

		return resultList;
	}

	public List<Receita> findReceitaByCpclAndCentroLucro(final CentroLucro cl) {
		return receitaDao.findByCpclAndCentroLucro(cl);
	}

	/**
	 * Busca uma lista de entidades pelo filtro, porem com status Published e
	 * Integrated.
	 *
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 *
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Receita> findReceitaAllNoWorkingByFilter(final Receita filter) {
		return receitaDao.findAllNoWorkingByFilter(filter);
	}

	/**
	 * Prepara a lista de ItemReceita para criacao da Receita.
	 *
	 * @param codigoContratoPratica
	 *            - codigo do ContratoPratica
	 * @param validityMonth
	 *            - mes de vigencia
	 * @param validityYear
	 *            - ano de vigencia
	 * @param codigoMoeda
	 *            - o codigoMoeda
	 * @return lista de ItemReceitaRow - lista de Receita
	 */
	public List<ItemReceitaRow> prepareCreateListItemReceita(
			final Long codigoContratoPratica, final String validityMonth,
			final String validityYear, final Long codigoMoeda) {
		Date dataAlocacaoPeriodo = DateUtil
				.getDate(validityMonth, validityYear);

		// dado ContratoPratica e um mes/ano, cria uma lista ItemReceitaRow
		return findItemReceitaByFilter(dataAlocacaoPeriodo,
				codigoContratoPratica, codigoMoeda);
	}

	/**
	 * Adiciona o ItemReceita na lista de {@link ReceitaMoedaRow}.
	 *
	 * @param itemReceita
	 *            - item para adicionar na lista
	 * @param receitaMoedaRow
	 *            - o {@link ReceitaMoedaRow} no qual o itme deve ser adicionado
	 * @param contratoPratica
	 *            - ContratoPratica da Receita
	 * @param percentualFaturamento
	 *            - percentual de faturamento (FTE) da Pessoa
	 *
	 * @return ItemReceitaRow (ItemReceita) que acabou de ser adicionado na
	 *         lista
	 */
	public ItemReceitaRow addItemReceita(final ItemReceita itemReceita,
										 final ReceitaMoedaRow receitaMoedaRow,
										 final ContratoPratica contratoPratica,
										 final BigDecimal percentualFaturamento) {

		// cria um ItemReceitaRow (ItemReceita)
		ItemReceitaRow row = new ItemReceitaRow(itemReceita, receitaMoedaRow
				.getItemReceitaRowList().size() + 1);

		ContratoPratica cp = contratoPraticaService
				.findContratoPraticaById(contratoPratica
						.getCodigoContratoPratica());

		// busca o valor do PerfilVendido para a TabelaPreco vigente do Msa
		// corrente
		ItemTabelaPreco itemTbPreco = itemTabelaPrecoService
				.findItemTabPrecoByMsaAndPerfil(cp.getMsa(),
						perfilVendidoService.findPerfilVendidoById(itemReceita
								.getPerfilVendido().getCodigoPerfilVendido()),
						itemReceita.getReceitaMoeda().getReceita().getDataMes());

		// atribui os valores
		// se existir o valor horas
		if (itemTbPreco != null) {
			row.setHrsPrice(itemTbPreco.getValorItemTbPreco());
			itemReceita
					.setPercentualDespesa(itemTbPreco.getPercentualDespesa());
			row.getItemReceita().setPercentualDespesa(
					itemTbPreco.getPercentualDespesa());
		} else { // senao existir seta com zero
			row.setHrsPrice(new BigDecimal(0));
			itemReceita.setPercentualDespesa(BigDecimal.valueOf(0));
		}

		itemReceita.setNumeroFte(percentualFaturamento);

		// adiciona na lista de ItemReceitaRow
		receitaMoedaRow.getItemReceitaRowList().add(row);

		// retorna a row que foi adicionada na lista
		return row;
	}

	/**
	 * Edita um ItemReceita na lista para ser atualizada posteriormente no
	 * banco.
	 *
	 * @param contratoPratica
	 *            - ContratoPratica da Receita
	 * @param itemReceitaRow
	 *            - row do ItemReceita corrente
	 */
	public void editItemReceita(final ContratoPratica contratoPratica,
								final ItemReceitaRow itemReceitaRow) {
		ItemReceita itemReceita = itemReceitaRow.getItemReceita();

		ContratoPratica cp = contratoPraticaService
				.findContratoPraticaById(contratoPratica
						.getCodigoContratoPratica());

		// busca o valor do PerfilVendido para a TabelaPreco vigente do
		// ContratoPratica corrente
		ItemTabelaPreco itemTbPreco = itemTabelaPrecoService
				.findItemTabPrecoByMsaAndPerfil(cp.getMsa(),
						perfilVendidoService.findPerfilVendidoById(itemReceita
								.getPerfilVendido().getCodigoPerfilVendido()),
						itemReceita.getReceitaMoeda().getReceita().getDataMes());

		// atribui os valores
		if (itemTbPreco != null) {
			itemReceitaRow.setHrsPrice(itemTbPreco.getValorItemTbPreco());
		} else {
			itemReceitaRow.setHrsPrice(BigDecimal.valueOf(0));
		}
	}

	/**
	 * Converte um {@link ItemReceita} para um {@link ItemReceitaRow}.
	 *
	 * @param item
	 *            o {@link ItemReceita} base a ser convertido
	 * @return {@link ItemReceitaRow}
	 */
	private ItemReceitaRow convertToItemReceitaRow(final ItemReceita item, final Receita receita) {
		ItemReceitaRow row = new ItemReceitaRow();

		row.setItemReceita(item);

		double numeroFte = item.getNumeroFte().doubleValue();
		double numberHours;

		if(receita.getContratoPratica().getMsa().getIndicadorArredondamentoDiarias().equals(Constants.NO)){
			numberHours = numeroFte * this.getNumberHoursMonth(receita.getContratoPratica().getCodigoContratoPratica(), item.getPessoa().getCidadeBase().getCodigoCidadeBase(), receita.getDataMes(), numeroFte);
		}else {
			numberHours =  this.getNumberHoursMonth(receita.getContratoPratica().getCodigoContratoPratica(), item.getPessoa().getCidadeBase().getCodigoCidadeBase(), receita.getDataMes(), numeroFte);
		}

		// atribui os valores aos campos ocultos para serem utilizados nos
		// calculos
		row.setNumberHoursHidden(BigDecimal.valueOf(numberHours));
		row.setNumeroFteHidden(BigDecimal.valueOf(numeroFte));

		// atribui o numero de horas, multiplicacao do percentual da
		// Alocacao (FTE alocado) x numero de horas mes
		row.setNumberHours(BigDecimal.valueOf(numberHours));

		row.setHrsPrice(item.getValorPerfilVendidoHora());

		// atribui o valor do PerfilVendido
		if (item.getNumeroFte() == BigDecimal.ZERO) {
			row.setFtePrice(BigDecimal.ZERO);
		} else {
			row.setFtePrice(item.getValorTotalItem().divide(
					item.getNumeroFte(), 2, RoundingMode.HALF_UP));
		}

		// atribui o somatorio do subtotal da linha corrente
		row.setAmountValue(item.getValorTotalItem());

		//Atribui o nome da cidade base do login da pessoa
		row.setNomeCidadeBase(item.getPessoa().getCidadeBase().getSiglaCidadeBase());

		return row;
	}

	/**
	 * Prepara a lista de {@link ItemReceita} para a visualizacao.
	 *
	 * @param receitaMoeda
	 *            a {@link ReceitaMoeda} base para a busca dos itens
	 * @return lista de {@link ItemReceitaRow}
	 */
	public List<ItemReceitaRow> prepareViewListItemReceita(
			final ReceitaMoeda receitaMoeda) {

		List<ItemReceita> itemReceitas = itemReceitaService
				.findItemReceitaByReceitaMoeda(receitaMoeda);

		List<ItemReceitaRow> itemReceitaRows = new ArrayList<ItemReceitaRow>();
		int rowId = 0;
		for (ItemReceita item : itemReceitas) {
			rowId++;

			ItemReceitaRow itemRow = this.convertToItemReceitaRow(item, receitaMoeda.getReceita());
			itemRow.setRowId(rowId);

			etiquetaService.generateEtiquetaNamesForItemReceitaRow(itemRow);

			itemReceitaRows.add(itemRow);
		}

		return itemReceitaRows;
	}

	/**
	 * Prepara a lista de ItemReceita para edicao da Receita.
	 *
	 * @param receitaMoeda
	 *            - ReceitaMoeda corrente
	 * @return lista de ItemReceitaRow - lista de ItemReceita
	 */
	public List<ItemReceitaRow> prepareUpdateListItemReceita(
			final ReceitaMoeda receitaMoeda) {

		// lista de ItemReceita da ReceitaMoeda corrente
		List<ItemReceita> itemReceitaList = itemReceitaService.findItemReceitaByReceitaMoeda(receitaMoeda);
		// lista final de ItemReceitaRow que sera retornada
		List<ItemReceitaRow> itemReceitaRowList = new ArrayList<ItemReceitaRow>();
		int rowCont = 0;

		// dado uma lista de ItemReceita, vinda do banco, cria a lista de
		// ItemReceitaRow
		for (ItemReceita itemReceita : itemReceitaList) {
			// cria uma ItemReceitaRow (ItemReceita) para ser inserida na
			// lista
			ItemReceitaRow row = new ItemReceitaRow(itemReceita, rowCont++);

			etiquetaService.generateEtiquetaNamesForItemReceitaRow(row);
			double numeroFte = itemReceita.getNumeroFte().doubleValue();
			// recupera o numero de horas do mes do arquivo de configuracoes
			double numberHoursMonth = getNumberHoursMonth(receitaMoeda.getReceita().getContratoPratica().getCodigoContratoPratica(), itemReceita.getPessoa().getCidadeBase().getCodigoCidadeBase(), receitaMoeda.getReceita().getDataMes(), numeroFte);

			double numberHours;
			if(receitaMoeda.getReceita().getContratoPratica().getMsa().getIndicadorArredondamentoDiarias().equals(Constants.NO)){
				numberHours = numeroFte * numberHoursMonth;
			}else {
				numberHours = numberHoursMonth;
			}

			// atribui o numero de horas, multiplicacao do percentual da
			// Alocacao (FTE alocado) x numero de horas mes
			row.setNumberHours(BigDecimal.valueOf(numberHours));

			// atribui os valores aos campos ocultos para serem utilizados nos
			// calculos
			row.setNumberHoursHidden(BigDecimal.valueOf(numberHours));
			row.setNumeroFteHidden(BigDecimal.valueOf(numeroFte));

			// Busca msa para evitar Lazy
			ContratoPratica contratoPratica = contratoPraticaService
					.findContratoPraticaById(receitaMoeda.getReceita()
							.getContratoPratica().getCodigoContratoPratica());
			Msa msa = msaService.findMsaById(contratoPratica.getMsa()
					.getCodigoMsa());

			// Busca tabela de preco para ver valor do item receita
			ItemTabelaPreco itemTabelaPreco = itemTabelaPrecoService
					.findItemTabPrecoByMsaAndPerfil(msa, itemReceita
							.getPerfilVendido(), receitaMoeda.getReceita()
							.getDataMes());

			if (itemTabelaPreco != null) {
				double valorItemTbPreco = itemTabelaPreco.getValorItemTbPreco()
						.doubleValue();

				// atribui o valor do PerfilVendido (configurado no
				// ItemTabelaPreco)
				// do ContratoPratica
				row.setHrsPrice(BigDecimal.valueOf(valorItemTbPreco));

				// atribui o valor do PerfilVendido (configurado no
				// ItemTabelaPreco)
				// do ContratoPratica (em FTE)
				row.setFtePrice(BigDecimal.valueOf(valorItemTbPreco
						* numberHoursMonth));

				// atribui o somatorio do subtotal da linha corrente
				row.setAmountValue(BigDecimal.valueOf(valorItemTbPreco
						* numberHours));
			}

			//Atribui o nome da cidade base do login da pessoa
			row.setNomeCidadeBase(itemReceita.getPessoa().getCidadeBase().getSiglaCidadeBase());

			// adiciona o ItemReceitaRow corrente a lista final
			itemReceitaRowList.add(row);
		}

		return itemReceitaRowList;
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 *
	 * @param dataAlocacaoPeriodo
	 *            data mes/ano da Alocacao.
	 * @param codigoContratoPratica
	 *            codigo do ContratoPratica.
	 * @param codigoMoeda
	 *            codigo da moeda.
	 *
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	private List<ItemReceitaRow> findItemReceitaByFilter(
			final Date dataAlocacaoPeriodo, final Long codigoContratoPratica,
			final Long codigoMoeda) {

		Moeda moeda = moedaService.findMoedaById(codigoMoeda);

		List<ItemReceitaRow> itemReceitaRowList = new ArrayList<ItemReceitaRow>();
		int rowCont = 0;

		// busca a lista de itens atraves da view, lista das Alocacao dos
		// MapaAlocacao do ContratoPratica corrente, do mes/ano especifico
		List<VwItemReceita> vwItemReceitaList = vwItemReceitaDao
				.findByFilterAndCurrency(dataAlocacaoPeriodo,
						codigoContratoPratica, moeda.getCodigoMoeda());

		MapaAlocacao mapa = null;
		Double fatorUr = null;

		if (!vwItemReceitaList.isEmpty()) {
			mapa = mapaAlocacaoService
					.findMapaAlocacaoByContratoPratica(contratoPraticaService
							.findContratoPraticaById(codigoContratoPratica));

			// Busca a entidade 'Fator UR' do mes
			FatorUrMes fatorUrMes = null;
			if (mapa != null) {
				fatorUrMes = fatorUrMesService.findFatorUrMesByMapaAndDate(
						mapa, dataAlocacaoPeriodo);
			}
			// Pega o valor do 'Fator UR' do mes
			fatorUr = Double.valueOf(1);
			if (fatorUrMes != null) {
				fatorUr = fatorUrMes.getPercentualFatorUr().doubleValue();
			}
		}

		// itera a lista de itens retornados pela view
		for (VwItemReceita vwItemReceita : vwItemReceitaList) {
			ItemReceita itemReceita = new ItemReceita();
			ItemReceitaRow itemReceitaRow = new ItemReceitaRow(itemReceita,
					rowCont++);

			// atribui a Pessoa
			itemReceita.setPessoa(pessoaService.findPessoaById(vwItemReceita
					.getId().getCodigoPessoa()));

			Alocacao alocacao = alocacaoService.findAlocacaoById(vwItemReceita
					.getId().getCodigoAlocacao());

			etiquetaService.generateEtiquetaNamesForItemReceitaRow(
					itemReceitaRow, alocacao.getEtiquetaAlocacaos());

			double valorUr = alocacao.getValorUr().doubleValue() * fatorUr;
			double percentualAlocacaoPeriodo = vwItemReceita.getId()
					.getPercentualAlocacaoPeriodo().doubleValue();
			double valorPerfilVendido = vwItemReceita.getId()
					.getValorPerfilVendido().doubleValue();

			double numeroFte = percentualAlocacaoPeriodo * valorUr;
			// recupera o numero de horas do mes do arquivo de configuracoes
			double numberHoursMonth = getNumberHoursMonth(codigoContratoPratica, itemReceita.getPessoa().getCidadeBase().getCodigoCidadeBase(), dataAlocacaoPeriodo, numeroFte);
			double numberHours;
			if(alocacao.getMapaAlocacao().getContratoPratica().getMsa().getIndicadorArredondamentoDiarias().equals(Constants.NO)) {
				numberHours = numeroFte * numberHoursMonth;
			}else{
				numberHours = numberHoursMonth;
			}
			BigDecimal percentualDespesa = vwItemReceita.getId()
					.getPercentualDespesa();

			// atribui o numero do percentual da Alocacao (FTE alocado)
			itemReceita.setNumeroFte(BigDecimal.valueOf(numeroFte));
			itemReceitaRow.setNumeroFteHidden(BigDecimal.valueOf(numeroFte));
			itemReceita.setPercentualDespesa(percentualDespesa);

			// atribui o numero de horas, multiplicacao do percentual da
			// Alocacao (FTE alocado) x numero de horas mes
			itemReceitaRow.setNumberHours(BigDecimal.valueOf(numberHours));
			itemReceitaRow
					.setNumberHoursHidden(BigDecimal.valueOf(numberHours));

			// atribui o PerfilVendido
			itemReceita.setPerfilVendido(perfilVendidoService
					.findPerfilVendidoById(vwItemReceita.getId()
							.getCodigoPerfilVendido()));

			// atribui o valor do PerfilVendido (configurado no ItemTabelaPreco)
			// do ContratoPratica
			itemReceitaRow.setHrsPrice(BigDecimal.valueOf(valorPerfilVendido));

			// atribui o valor do PerfilVendido (configurado no ItemTabelaPreco)
			// do ContratoPratica (em FTE)
			itemReceitaRow.setFtePrice(BigDecimal.valueOf(valorPerfilVendido
					* numberHoursMonth));

			// atribui o somatorio do subtotal da linha corrente
			itemReceitaRow.setAmountValue(BigDecimal.valueOf(valorPerfilVendido
					* numberHours));

			//Atribui o nome da cidade base do login da pessoa
			itemReceitaRow.setNomeCidadeBase(itemReceita.getPessoa().getCidadeBase().getSiglaCidadeBase());

			itemReceitaRowList.add(itemReceitaRow);
		}

		// se nenhum registro foi encontrado, da mensagem de warning
		if (itemReceitaRowList.size() <= 0) {
			Messages.showWarning("findItemReceitaByFilter",
					Constants.MSG_WARNG_ITEM_RECEITA_NO_RESULT,
					moeda.getNomeMoeda());
			return null;
		}

		String nomesPapelAlocacao = "";
		if (mapa != null) {
			// pega todas as alocacoes
			List<Alocacao> alocacaoList = mapa.getAlocacaos();
			for (Alocacao alocacao : alocacaoList) {
				// verifica se o recurso e um papel alocacao
				Recurso recurso = alocacao.getRecurso();
				if (recurso.getIndicadorTipoRecurso().equals(
						Constants.RESOURCE_TYPE_PA)) {
					// concatena todos no nomes dos papeis alocados
					// para adiconar a mensagem de aviso
					if (!nomesPapelAlocacao.equals("")) {
						nomesPapelAlocacao += ", ";
					}

					AlocacaoPeriodo ap = alocacaoPeriodoService.findAlocacaoPeriodoByAlocacaoAndDate(alocacao, dataAlocacaoPeriodo);

					// so adiciona a mensagem se o percentual eh maior que zero
					if (ap != null && ap.getPercentualAlocacaoPeriodo().compareTo(BigDecimal.ZERO) > 0) {
						nomesPapelAlocacao += recurso.getCodigoMnemonico();
					}
				}
			}
			// adiciona a mensagem caso necessario
			if (!nomesPapelAlocacao.equals("")) {
				Messages.showError("findItemReceitaByFilter",
						Constants.MSG_ERROR_RECEITA_EXISTE_PAPEL, new Object[] {
								mapa.getTextoTitulo(), nomesPapelAlocacao });
			}
		}

		return itemReceitaRowList;
	}

	/**
	 * Carrega uma lista de entidades ItemReceita com os dados da lista para
	 * persistir no banco.
	 *
	 * @param receitaMoedaRow
	 *            - ReceitaMoedaRow corrente
	 * @param isDraft
	 *            - indicador do Status da Receita, se e Draft ou nao
	 *
	 * @return lista de ItemReceita
	 */
	public List<ItemReceita> loadItemReceitaList(
			final ReceitaMoedaRow receitaMoedaRow, final Boolean isDraft, final Long codigoContratoPratica) {
		ReceitaMoeda receitaMoeda = receitaMoedaRow.getReceitaMoeda();
		List<ItemReceitaRow> itemReceitaRowList = receitaMoedaRow
				.getItemReceitaRowList();

		// lista final de ItemReceita que sera retornada
		List<ItemReceita> itemReceitaList = new ArrayList<ItemReceita>();
		// marcador se a lista esta ok, sem erro ou incoerencia alguma
		Boolean listOk = Boolean.valueOf(true);

		// verifica se o Receita esta sendo criado/alterado
		// corretamente com pelo menos um ItemReceita. Se nao retorna null
		if (!isValidItemReceitaList(itemReceitaRowList)) {
			return null;
		}

		// itera a lista de ItemReceitaRow
		for (ItemReceitaRow row : itemReceitaRowList) {
			ItemReceita itemReceita = row.getItemReceita();

			// verifica se a linha esta marcada para ser removida, se nao
			// estiver marcada, faz as validacoes necessarias
			if (!row.getIsRemove()) {
				// Verifica se o preco na TabelaPreco (ItemTabelaPreco) do
				// ContratoPratica foi preenchido corretamente para o
				// PerfilVendido corrente, o valor deve ser maior do que zero,
				// se nao marca que a lista nao esta ok e vai para o proximo
				// ItemReceita
				// e nao precisa executar o codigo abaixo
				if (!isValidItemReceitaPrice(row, itemReceita.getPessoa()
						.getCodigoLogin())) {
					listOk = Boolean.valueOf(false);
					continue;
				}

				// Verifica se o numero de horas foi preenchido com valor maior
				// do que 0 (zero) se nao marca que a lista nao
				// esta ok e vai para o proximo ItemReceita e nao precisa
				// executar o codigo abaixo
				if (!isValidItemReceitaValues(row, receitaMoeda,  itemReceita.getPessoa()
						.getCodigoLogin())) {
					listOk = Boolean.valueOf(false);
					continue;
				}
			}
		}

		// se a lista estiver ok retorna a lista final, se nao retorna null
		if (listOk) {
			// itera a lista de ItemReceitaRow
			for (ItemReceitaRow row : itemReceitaRowList) {
				ItemReceita itemReceita = row.getItemReceita();

				if (row.getIsRemove()) {
					// se sim e se ItemReceita tiver codigo (nesse caso
					// somente na alteracao do Receita)
					if (itemReceita.getCodigoItemReceita() != null) {
						// remove ItemReceita
						itemReceitaService.removeItemReceita(itemReceitaService
								.findItemReceitaById(itemReceita
										.getCodigoItemReceita()));
					}
					// se este ItemReceita corrente estiver marcado para ser
					// removido vai para o proximo e nao precisa executar o
					// codigo abaixo
					continue;
				}

				// atribui o ReceitaMoeda corrente ao ItemReceita
				itemReceita.setReceitaMoeda(receitaMoeda);

				// atribui o valor oculto do campo numeroFte para armazenar o
				// valor correto no banco
				itemReceita.setNumeroFte(row.getNumeroFteHidden());

				// atualiza os valores dos ItemReceita para ser persistido
				// somente se for diferente de Draft
				if (!isDraft) {
					double hrsPrice = row.getHrsPrice().doubleValue();
					double numeroFte = row.getItemReceita().getNumeroFte()
							.doubleValue();
					double numberHoursMonth = getNumberHoursMonth(codigoContratoPratica, itemReceita.getPessoa().getCidadeBase().getCodigoCidadeBase(), receitaMoeda.getReceita().getDataMes(), numeroFte);
					double numberHours;
					if(receitaMoeda.getReceita().getContratoPratica().getMsa().getIndicadorArredondamentoDiarias().equals(Constants.NO)){
						numberHours = numeroFte * numberHoursMonth;
					}else{
						numberHours = numberHoursMonth;
					}
					BigDecimal amountValue = BigDecimal.valueOf(numberHours
							* hrsPrice);
					itemReceita.setValorTotalItem(amountValue);
					itemReceita.setValorPerfilVendidoHora(row.getHrsPrice());
				}

				// adiciona o ItemReceita corrente a lista final
				itemReceitaList.add(itemReceita);
			}

			return itemReceitaList;
		} else {
			return null;
		}
	}

	/**
	 * Carrega uma lista de entidades ReceitaDealFiscal com os dados da lista
	 * para persistir no banco.
	 *
	 * @param receitaMoedaRow
	 *            - ReceitaMoedaRow corrente
	 *
	 * @return lista de ReceitaDealFiscal
	 */
	public List<ReceitaDealFiscal> loadReceitaDealFiscalList(
			final ReceitaMoedaRow receitaMoedaRow) {
		ReceitaMoeda receitaMoeda = receitaMoedaRow.getReceitaMoeda();
		List<ReceitaDealFiscalRow> recDealFiscalRowList = receitaMoedaRow
				.getRecDealFiscalRowList();

		// lista final de ReceitaDealFiscal que sera retornada
		List<ReceitaDealFiscal> recDealFiscalList = new ArrayList<ReceitaDealFiscal>();

		// itera a lista de ReceitaDealFiscalRow
		for (ReceitaDealFiscalRow row : recDealFiscalRowList) {
			ReceitaDealFiscal recDealFiscal = row.getTo();

			// Exclusao de deal fiscal com valor 0
			if (recDealFiscal.getValorReceita()
					.compareTo(BigDecimal.valueOf(0)) != 0) {

				// atribui o ReceitaMoeda corrente ao ReceitaDealFiscal
				recDealFiscal.setReceitaMoeda(receitaMoeda);

				// adiciona o ReceitaDealFiscal corrente a lista final
				recDealFiscalList.add(recDealFiscal);
			}
		}

		return recDealFiscalList;
	}

	/**
	 * Verifica se o Receita que esta sendo criado e valido. Se nao existir uma
	 * Receita com o ContratoPratica, mes/ano e Versao passado por parametro, a
	 * Receita e valida. Caso contrario e invalida.
	 *
	 * @param codigoContratoPratica
	 *            - codigo do codigoContratoPratica
	 * @param validityMonth
	 *            - mes da vigencia
	 * @param validityYear
	 *            - ano da vigencia
	 * @return true se a Receita for valida, false se ja existir uma Receita
	 *         cadastrado com o ContratoPratica, mes/ano e Versao passados por
	 *         parametro
	 */
	public Boolean isValidReceitaVersion(final Long codigoContratoPratica,
										 final String validityMonth, final String validityYear) {
		ContratoPratica contratoPratica = new ContratoPratica();
		contratoPratica.setCodigoContratoPratica(codigoContratoPratica);

		Receita filter = new Receita();
		filter.setContratoPratica(contratoPratica);
		filter.setDataMes(DateUtil.getDate(validityMonth, validityYear));

		List<Receita> receitaList = findReceitaByFilter(filter);

		for (Receita receita : receitaList) {
			if (!Constants.VERSION_RECEITA_FORECAST.equalsIgnoreCase(receita.getIndicadorVersao())) {
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	/**
	 * Verifica se o Receita que esta sendo criado e valido. Verifica se o
	 * mes/ano passado por parametro e menor do que a data atual. Se sim, o
	 * Receita e valido. Caso contrario e invalido.
	 *
	 * @param validityMonth
	 *            - mes da vigencia
	 * @param validityYear
	 *            - ano da vigencia
	 * @return true se a Receita for valido se o mes/ano passados por parametro
	 *         for menor do que a data atual. false caso contrario
	 */
	public Boolean isValidReceitaMonthYear(final String validityMonth,
										   final String validityYear) {
		Date currentDate = DateUtils.truncate(this.getClosingDateReceita(),
				Calendar.MONTH);
		Date dataMes = DateUtil.getDate(validityMonth, validityYear);

		// somente sera permitido se o mes/ano (que vem da tela)
		// for maior que a data de fechamento
		if (dataMes.compareTo(currentDate) > 0) {
			return Boolean.TRUE;
		} else {
			Messages.showError("isValidReceitaMonthYear",
					Constants.MSG_ERROR_INVAL_RECEITA_MONTH);
			return Boolean.FALSE;
		}
	}

	/**
	 * Realiza a redistribuicao da receita para os itens das Receita
	 * selecionados.
	 *
	 * @param itemReceitaRowList
	 *            - lista com todos os itens da Receita
	 * @param redistributionValue
	 *            - valor a ser redistribuidos.
	 */
	public void calculateIncomeRedistribution(
			final List<ItemReceitaRow> itemReceitaRowList,
			final BigDecimal redistributionValue,
			final Receita receita) {

		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal factor = new BigDecimal(0);
		Integer scale = Integer.parseInt((String) systemProperties
				.get(Constants.DEFAULT_NUMBER_SCALE));

		// realiza a somatoria da receita, sobre todos os itens selecionados
		for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
			if (itemReceitaRow.getIsSelected() && !itemReceitaRow.getIsRemove()) {
				totalAmount = totalAmount.add(BigDecimal.valueOf(itemReceitaRow
						.getNumberHoursHidden().doubleValue()
						* itemReceitaRow.getHrsPrice().doubleValue()));
			}
		}

		if (totalAmount.compareTo(BigDecimal.ZERO) != 0) {
			// calcula o fator multiplicador, para gerar o novo FTE
			factor = redistributionValue.divide(totalAmount, scale,
					BigDecimal.ROUND_HALF_UP);
		}

		// realiza os calculos da redistriucao para todos os itens selecionados
		Iterator<ItemReceitaRow> itItemReceitaRow = itemReceitaRowList.iterator();
		while (itItemReceitaRow.hasNext()) {
			ItemReceitaRow itemReceitaRow = itItemReceitaRow.next();

			if (itemReceitaRow.getIsSelected() && !itemReceitaRow.getIsRemove()) {
				// pega o item
				ItemReceita itemReceita = itemReceitaRow.getItemReceita();
				// calcula o novo FTE
				// BigDecimal newFTE =
				// factor.multiply(itemReceita.getNumeroFte());
				BigDecimal newFTE = factor.multiply(itemReceitaRow
						.getNumeroFteHidden());

				// seta o novo FTE para o item
				itemReceita.setNumeroFte(newFTE);
				itemReceitaRow.setNumeroFteHidden(newFTE);
				// pega o total de horas por mes
				BigDecimal numberHoursMonth = new BigDecimal(getNumberHoursMonth(
						receita.getContratoPratica().getCodigoContratoPratica(),
						itemReceita.getPessoa().getCidadeBase().getCodigoCidadeBase(),
						receita.getDataMes(), newFTE.doubleValue()));
				// calcula o numero de horas utilizando o novo FTE
				BigDecimal newNumberHours = new BigDecimal(0);

				if(receita.getContratoPratica().getMsa().getIndicadorArredondamentoDiarias().equals(Constants.NO)) {
					newNumberHours = newFTE.multiply(numberHoursMonth);
				}else{
					newNumberHours = numberHoursMonth;
				}


				// seta o novo numero horas
				itemReceitaRow.setNumberHours(newNumberHours);
				itemReceitaRow.setNumberHoursHidden(newNumberHours);

				// seta o Fte Price
				itemReceitaRow.setFtePrice(numberHoursMonth
						.multiply(itemReceitaRow.getHrsPrice()));
				// seta o valor total (receita)
				itemReceitaRow.setAmountValue(newNumberHours
						.multiply(itemReceitaRow.getHrsPrice()));
			}
		}

	}

	public Boolean calculateDealFiscalRedistribution(final List<ItemReceitaRow> itemReceitaRowList, final List<ReceitaDealFiscalRow> receitaDealFiscalRowList){

		List<Long> cidadeBaseFromItem = getListEmpresaFromItem(itemReceitaRowList);
		List<Long> cidadeBaseFromDealFiscal = getListEmpresaFromDealFiscal(receitaDealFiscalRowList);

		if(isValidCidadeBaseDealFiscalItemReceita(cidadeBaseFromDealFiscal, cidadeBaseFromItem)){
			for(ReceitaDealFiscalRow receitaDealFiscalRow : receitaDealFiscalRowList){
				receitaDealFiscalRow.getTo().setValorReceita(BigDecimal.ZERO);
				Long empresaDFiscalCode = dealFiscalService.findDealFiscalById(receitaDealFiscalRow.getTo().getDealFiscal().getCodigoDealFiscal()).getEmpresa().getCodigoEmpresa();
				for(ItemReceitaRow item : itemReceitaRowList){

					PessoaCidadeBase pcb = pessoaCidadeBaseService.findPessCBByPessoaAndDate(item.getItemReceita().getPessoa(),getDateFirstDay());
					Long empresaPessoa = cidadeBaseService.findEmpresaByCidadeBase(pcb.getCidadeBase().getCodigoCidadeBase());
					if(empresaDFiscalCode.equals(empresaPessoa)){
						receitaDealFiscalRow.getTo().setValorReceita(receitaDealFiscalRow.getTo().getValorReceita().add(item.getAmountValue()));
					}
				}
			}
			return true;
		} else {
			Messages.showError("isValidReceitaDealFiscalCityBase",
					Constants.MSG_ERROR_INVAL_CITY_BASE_DEAL_FISCAL);
			return false;
		}
	}

	public Boolean calculateDealFiscalRedistribution(final BigDecimal factor, final List<ItemReceitaRow> itemReceitaRowList, final List<ReceitaDealFiscalRow> receitaDealFiscalRowList){

		List<Long> cidadeBaseFromItem = getListEmpresaFromItem(itemReceitaRowList);
		List<Long> cidadeBaseFromDealFiscal = getListEmpresaFromDealFiscal(receitaDealFiscalRowList);

		if(isValidCidadeBaseDealFiscalItemReceita(cidadeBaseFromDealFiscal, cidadeBaseFromItem)){
			for(ReceitaDealFiscalRow receita : receitaDealFiscalRowList){
				receita.getTo().setValorReceita(BigDecimal.ZERO);
				Long empresaDFiscalCode = dealFiscalService.findDealFiscalById(receita.getTo().getDealFiscal().getCodigoDealFiscal()).getEmpresa().getCodigoEmpresa();
				for(ItemReceitaRow item : itemReceitaRowList){

					PessoaCidadeBase pcb = pessoaCidadeBaseService.findPessCBByPessoaAndDate(item.getItemReceita().getPessoa(),getDateFirstDay());
					Long empresaPessoa = cidadeBaseService.findEmpresaByCidadeBase(pcb.getCidadeBase().getCodigoCidadeBase());
					if(empresaDFiscalCode.equals(empresaPessoa)){
						receita.getTo().setValorReceita(receita.getTo().getValorReceita().add(item.getAmountValue().multiply(factor)));
					}
				}
			}
			return true;
		}else{
			Messages.showError("isValidReceitaDealFiscalCityBase",
					Constants.MSG_ERROR_INVAL_CITY_BASE_DEAL_FISCAL);
			return false;
		}
	}

	private Boolean isValidCidadeBaseDealFiscalItemReceita(List<Long> cidadeBaseDealFiscal, List<Long>cidadeBaseItemReceita){

		for(Long itemReceita : cidadeBaseItemReceita){
			//Se uma cidade de col
			if(!cidadeBaseDealFiscal.contains(itemReceita)){
				return false;
			}
		}
		return true;
	}

	private List<Long> getListEmpresaFromDealFiscal(List<ReceitaDealFiscalRow> receitaDealFiscalRowList){
		List<Long> listEmpresa = new ArrayList<Long>();
		for (ReceitaDealFiscalRow receitaDealFiscal : receitaDealFiscalRowList) {
			Long EmpresaCode = dealFiscalService.findDealFiscalById(receitaDealFiscal.getTo().getDealFiscal().getCodigoDealFiscal()).getEmpresa().getCodigoEmpresa();
			if (listEmpresa.size() == 0){
				listEmpresa.add(EmpresaCode);
			}else{
				if(!listEmpresa.contains(EmpresaCode)){
					listEmpresa.add(EmpresaCode);
				}
			}
		}
		return listEmpresa;
	}

	private List<Long> getListEmpresaFromItem(List<ItemReceitaRow> itemReceitaRowList){
		List<Long> listEmpresa = new ArrayList<Long>();
		for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
			PessoaCidadeBase pcb = pessoaCidadeBaseService.findPessCBByPessoaAndDate(itemReceitaRow.getItemReceita().getPessoa(), getDateFirstDay());
			Long empresaPessoa = cidadeBaseService.findEmpresaByCidadeBase(pcb.getCidadeBase().getCodigoCidadeBase());

			if (listEmpresa.size() == 0){
				listEmpresa.add(empresaPessoa);
			}else{
				if(!listEmpresa.contains(empresaPessoa)){
					listEmpresa.add(empresaPessoa);
				}
			}
		}
		return listEmpresa;
	}

	private PessoaCidadeBase getActualPessoaCidadeBse(Set<PessoaCidadeBase> listPessoaCidadeBase){
		for(PessoaCidadeBase pcb : listPessoaCidadeBase){
			if(pcb.getDataFim() == null){return pcb;}
		}
		return null;
	}

	private Date getDateFirstDay(){

		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * Atualiza os valores do FTE dos itens selecionados.
	 *
	 * @param itemReceitaRowList
	 *            - Lista com as linhas do ReceitaRow.
	 * @param updateHours
	 *            - valor a ser atualizado do FTE na lista de ItemReceita.
	 */
	public void updateIncomeFte(final List<ItemReceitaRow> itemReceitaRowList, final BigDecimal updateHours, final Receita receita) {

		for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
			if (itemReceitaRow.getIsSelected() && !itemReceitaRow.getIsRemove()) {
				ItemReceita itemReceita = itemReceitaRow.getItemReceita();
				BigDecimal numberHoursMonth = new BigDecimal(this.getNumberHoursMonth(receita.getContratoPratica().getCodigoContratoPratica(), itemReceita.getPessoa().getCidadeBase().getCodigoCidadeBase(), receita.getDataMes(), itemReceita.getNumeroFte().doubleValue()));
				// o resultado e tranfomado em string para criar um novo big decimal,
				// pois assim nao existe perda de precisao
				BigDecimal updateFte = new BigDecimal("" + updateHours.doubleValue() / numberHoursMonth.doubleValue());

				// pega o item
				itemReceita.setNumeroFte(updateFte);
				itemReceitaRow.setNumeroFteHidden(updateFte);

				// calcula o numero de horas utilizando o novo FTE
				BigDecimal newNumberHours;
				if(receita.getContratoPratica().getMsa().getIndicadorArredondamentoDiarias().equals(Constants.NO)){
					newNumberHours = updateFte.multiply(numberHoursMonth);
				}else{
					newNumberHours = numberHoursMonth;
				}

				// seta o novo numero horas
				itemReceitaRow.setNumberHours(newNumberHours);
				itemReceitaRow.setNumberHoursHidden(newNumberHours);

				// seta o Fte Price
				itemReceitaRow.setFtePrice(numberHoursMonth
						.multiply(itemReceitaRow.getHrsPrice()));
				// seta o valor total (receita)
				itemReceitaRow.setAmountValue(newNumberHours
						.multiply(itemReceitaRow.getHrsPrice()));
			}
		}

	}

	/**
	 * Atualiza os valores do FTE dos itens selecionados.
	 *
	 * @param itemReceitaRowList
	 *            - Lista com as linhas do ReceitaRow.
	 * @param updateHours
	 *            - valor a ser atualizado da Horas na lista de ItemReceita.
	 */
	public void updateIncomeHours(
			final List<ItemReceitaRow> itemReceitaRowList,
			final BigDecimal updateHours,
			final Receita receita) {

		this.updateIncomeFte(itemReceitaRowList, updateHours, receita);
	}

	/**
	 * Verifica se a lista de ItemReceitaRow do Receita e valida, se pelo menos
	 * um ItemReceita foi preenchido.
	 *
	 * @param itemReceitaRowList
	 *            - lista de ItemReceitaRow do Receita corrente
	 * @return true se a lista do Receita e valida, caso contrario retorna false
	 */
	private Boolean isValidItemReceitaList(
			final List<ItemReceitaRow> itemReceitaRowList) {
		// contador de ItemReceitaRow validas
		int validItemReceitaRowCont = 0;
		for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
			// se pelo menos uma ItemReceitaRow nao estiver marcada pra ser
			// removida, conta 1 ItemReceitaRow valida
			if (!itemReceitaRow.getIsRemove()) {
				validItemReceitaRowCont++;
				break;
			}
		}

		// se nao existir ItemReceitaRow valida, da mensagem de erro, pois o
		// Receita deve ter pelo menos um ItemReceita
		if (validItemReceitaRowCont <= 0) {
			Messages.showError("isValidItemReceitaList",
					Constants.MSG_ERROR_INVAL_ITEM_RECEITA_LIST);
			return Boolean.valueOf(false);
		}

		return Boolean.valueOf(true);
	}

	/**
	 * Verifica se pelo menos 1 mes foi preenchido com valor maior do que 0
	 * (zero).
	 *
	 * @param itemReceitaRow
	 *            - linha do ItemReceita
	 * @param codigoLoginPessoa
	 *            - codigo do login da Pessoa do ItemReceita corrente
	 * @return true se a linha for valida, se os campos de valores foram
	 *         preenchidos com valor maior do que 0 (zero). Caso contrario
	 *         retorna false
	 */
	private Boolean isValidItemReceitaValues(
			final ItemReceitaRow itemReceitaRow, final ReceitaMoeda receitaMoeda, final String codigoLoginPessoa) {
		ItemReceita itemReceita = itemReceitaRow.getItemReceita();
		// verifica se o numero de horas foi preenchido corretamente e se e
		// maior do que 0 (zero)
		if (itemReceita.getNumeroFte() != null && itemReceitaRow.getNumberHours() != null) {

			if(isFteGreaterThanZero(itemReceitaRow.getNumeroFteHidden())) {
				Receita receita =  receitaMoeda.getReceita();

				//ICAS-20943 Logins sem alocacao gerando receita.
				if(alocacaoPeriodoService.findAlocacaoPeriodoGreaterZeroByContratoPraticaAndDateAndRecurso(receita.getContratoPratica().getCodigoContratoPratica(),
						itemReceitaRow.getItemReceita().getPessoa().getRecurso().getCodigoRecurso(),
						receita.getDataMes())) {
					return Boolean.valueOf(true);
				}else{
					Messages.showError("isAlocationZero",
							Constants.MSG_ERROR_INVAL_ALLOCATION_ZERO_VALUES,
							codigoLoginPessoa);
					return Boolean.FALSE;
				}
			}else{
				return Boolean.valueOf(true);
			}
		}

		// se os campos de valores nao forem preenchidos corretamente e/ou com
		// valor maior do que 0 (zero), da mensagem de erro
		Messages.showError("isValidItemReceitaValues",
				Constants.MSG_ERROR_INVAL_ITEM_RECEITA_VALUES,
				codigoLoginPessoa);
		return Boolean.valueOf(false);
	}

	private boolean isFteGreaterThanZero(BigDecimal fte){
		BigDecimal bg1, bg2;
		bg1 = new BigDecimal("0");
		bg2 = new BigDecimal(fte.toString());

		int res;
		res = bg1.compareTo(bg2); // compare bg1 with bg2

		return res == -1 ? true : false;
	}


	/**
	 * Busca uma lista de entidades pelo filtro.
	 *
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 *
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Receita> findReceitaByFilter(final Receita filter) {
		return receitaDao.findByFilter(filter);
	}

	/**
	 * Verifica se pelo menos 1 mes foi preenchido com valor maior do que 0
	 * (zero).
	 *
	 * @param itemReceitaRow
	 *            - linha do ItemReceita
	 * @param codigoLoginPessoa
	 *            - codigo do login da Pessoa do ItemReceita corrente
	 * @return true se a linha for valida, se os campos de valores foram
	 *         preenchidos com valor maior do que 0 (zero). Caso contrario
	 *         retorna false
	 */
	private Boolean isValidItemReceitaPrice(
			final ItemReceitaRow itemReceitaRow, final String codigoLoginPessoa) {
		BigDecimal hrsPrice = itemReceitaRow.getHrsPrice();
		// verifica se o preco do item foi preenchido corretamente e se e
		// maior do que 0 (zero) na TabelaPreco (ItemTabelaPreco)
		if (hrsPrice != null && hrsPrice.doubleValue() >= 0) {
			return Boolean.valueOf(true);
		}

		// se os campos de valores nao forem preenchidos corretamente e/ou com
		// valor maior do que 0 (zero), da mensagem de erro
		Messages.showError("isValidItemReceitaPrice",
				Constants.MSG_ERROR_INVAL_ITEM_RECEITA_PRICE, codigoLoginPessoa);
		return Boolean.valueOf(false);
	}

	/**
	 * Recupera o numero de horas do mes do arquivo de configuracoes.
	 *
	 * @return numero de horas do mes do arquivo de configuracoes.
	 */
	public double getNumberHoursMonth(final Long codigoContratoPratica, final Long codigoPessoaCidadeBase, final Date dataReceita, final Double fte) {
		Double numberHoursMonth = 0.0;

		ContratoPratica cp = contratoPraticaService.findContratoPraticaById(codigoContratoPratica);

		if(cp.getMsa().getIndicadorCalculoDiasUteis().equals(Constants.YES)){
			if (cp.getMsa().getIndicadorArredondamentoDiarias().equals(Constants.YES)) {
				numberHoursMonth = Math.ceil(diasUteisCidadeBaseService.findByCidadeBaseAndMonth(codigoPessoaCidadeBase, dataReceita) * fte) * 8;
			} else {
				numberHoursMonth = (double) diasUteisCidadeBaseService.findByCidadeBaseAndMonth(codigoPessoaCidadeBase, dataReceita) * 8;
			}
		} else {
			numberHoursMonth = Double.parseDouble(systemProperties.getProperty(Constants.DEFAULT_PROPERTY_NUMBER_HOURS_MONTH));
		}
		return numberHoursMonth;
	}

	/**
	 * Recuprea a moeda corrente.
	 *
	 * @param moeda
	 *            - Moeda corrente
	 * @param isSiglaMoeda
	 *            - booleano se retorna sigla da Moeda ou o nome
	 * @return o pattern da Moeda.
	 */
	public String getCurrency(final Moeda moeda, final Boolean isSiglaMoeda) {
		if (moeda != null) {
			// atribui o pattern dos valores conforme sigla da moeda
			return systemProperties
					.getProperty(Constants.DEFAULT_PROPERTY_PATTERN_CURRENCY
							+ moeda.getSiglaMoeda().toLowerCase());
			// se a Moeda e null, forca Moeda padrao por default
		} else {
			String defaultCurrencyCode = systemProperties
					.getProperty(Constants.DEFAULT_PROPERTY_CURRENCY_CODE);

			if (!StringUtils.isEmpty(defaultCurrencyCode)) {
				Moeda defaultMoeda = moedaService.findMoedaById(Long
						.valueOf(defaultCurrencyCode));

				if (defaultMoeda != null) {
					if (isSiglaMoeda) {
						return defaultMoeda.getSiglaMoeda();
					} else {
						return defaultMoeda.getNomeMoeda();
					}

				}
			}
		}

		return "";
	}

	/**
	 * Pega a data de fechamento do horas faturadas.
	 *
	 * @return retorna a Data de Fechamento.
	 */
	public Date getClosingDateReceita() {
		// pega a data de fechamento do modulo do mapa de alocacao
		Modulo moduloRevenue = moduloService.findModuloById(new Long(
				systemProperties.getProperty(Constants.MODULO_RECEITA_CODE)));

		return DateUtils.truncate(moduloRevenue.getDataFechamento(),
				Calendar.MONTH);
	}

	/**
	 * Exibir toda a lista (atribuir true para o atributo isView da
	 * ItemHrsFatDealRow).
	 *
	 * @param itemReceitaRowList
	 *            - lista de ItemReceita
	 */
	public void showFullListItemReceita(
			final List<ItemReceitaRow> itemReceitaRowList) {
		for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
			itemReceitaRow.setIsView(Boolean.valueOf(true));
		}
	}

	/**
	 * Calcula o valor total da lista de Receita convertido na Moeda Real.
	 *
	 * @param receitaFilterList
	 *            - lista das ReceitaFilter que estao sendo exibidas no
	 *            relatorio
	 * @return o valor total da lista de Receita convertido
	 */
	public BigDecimal calculateTotalReceitaConvert(
			final List<ReceitaFilter> receitaFilterList) {
		double totalReceitaConvertDouble = 0;

		// recupera o codigo da Moeda Real
		String currencyBrlCode = systemProperties
				.getProperty(Constants.PROPERTY_CURRENCY_BRL_CODE);
		Long currencyBrlCodeLong = null;
		if (!StringUtils.isEmpty(currencyBrlCode)) {
			currencyBrlCodeLong = Long.valueOf(currencyBrlCode);
		}

		// itera a lista de ReceitaFilter para calcular o total convertido em
		// Real
		for (ReceitaFilter receitaFilter : receitaFilterList) {

			if (receitaFilter.getReceitaMoeda().getValorTotalMoeda() == null
					|| receitaFilter.getReceitaMoeda().getValorTotalMoeda() == BigDecimal.ZERO) {
				continue;
			}

			double valorReceitaDouble = receitaFilter.getReceitaMoeda()
					.getValorTotalMoeda().doubleValue();

			// recupera a CotacaoMoeda da Receita corrente
			CotacaoMoeda cotacaoMoeda = receitaFilter.getReceitaMoeda()
					.getCotacaoMoeda();
			// se tiver CotacaoMoeda relacionada, faz a conversao e soma
			if (cotacaoMoeda != null) {
				double valorCotacaoDouble = cotacaoMoeda.getValorCotacao()
						.doubleValue();
				totalReceitaConvertDouble += valorCotacaoDouble
						* valorReceitaDouble;
				// senao, apenas soma normalmente
			} else {
				totalReceitaConvertDouble += valorReceitaDouble;
				// caso for versao Unavailable e caso nao tiver CotacaoMoeda
				// relacionada e a Moeda nao for
				// Real, esta inconsistente. Mostra mensagem de warning.
				if (!(Constants.VERSION_UNAVAILABLE).equals(receitaFilter
						.getIndicadorVersao())
						&& currencyBrlCodeLong.compareTo(receitaFilter
						.getReceitaMoeda().getMoeda().getCodigoMoeda()) != 0) {
					Messages.showError("findByFilterReport",
							Constants.MSG_WARNG_RECEITA_NO_EXCHANGE_RATE,
							receitaFilter.getNomeContratoPratica());
				}
			}
		}

		return BigDecimal.valueOf(totalReceitaConvertDouble);
	}

	/**
	 * Calcula o total das receitas de cada moeda. IMPORTANTE: o valor nao e
	 * convertido.
	 *
	 * @param receitaFilterList
	 *            - lista das {@link ReceitaFilter}
	 * @return lista de {@link ReceitaFilter} cada moeda com seu respectivo
	 *         total
	 */
	public List<ReceitaFilter> calculateTotalReceitaByMoeda(
			final List<ReceitaFilter> receitaFilterList) {

		List<ReceitaFilter> totalReceitaPorMoeda = new ArrayList<ReceitaFilter>();
		Map<Long, ReceitaFilter> mapTotalsAux = new HashMap<Long, ReceitaFilter>();

		ReceitaFilter receitaFilter = null;
		for (ReceitaFilter rf : receitaFilterList) {

			if (rf.getReceitaMoeda().getValorTotalMoeda() == null
					|| rf.getReceitaMoeda().getValorTotalMoeda() == BigDecimal.ZERO) {
				continue;
			}

			Moeda moeda = rf.getReceitaMoeda().getMoeda();
			// verifica se no mapa auxiliar de totais tem a Moeda corrente
			receitaFilter = mapTotalsAux.get(moeda.getCodigoMoeda());
			// se tem atualiza o total das Receitas da Moeda corrente
			if (receitaFilter != null) {

				receitaFilter.getReceitaMoeda()
						.setValorTotalMoeda(
								receitaFilter
										.getReceitaMoeda()
										.getValorTotalMoeda()
										.add(rf.getReceitaMoeda()
												.getValorTotalMoeda()));

				// se nao tem, cria um novo objeto ReceitaFilter para armazenar
				// o valor total da Receita da Moeda corrente
			} else {
				receitaFilter = new ReceitaFilter();
				ReceitaMoeda rm = new ReceitaMoeda();
				rm.setMoeda(moeda);
				rm.setValorTotalMoeda(rf.getReceitaMoeda().getValorTotalMoeda());
				receitaFilter.setReceitaMoeda(rm);
				mapTotalsAux.put(moeda.getCodigoMoeda(), receitaFilter);
			}
		}

		totalReceitaPorMoeda.addAll(mapTotalsAux.values());
		return totalReceitaPorMoeda;
	}

	/**
	 * Busca uma lista de entidades nao fechadas, status diferente de Published
	 * e Integrated.
	 *
	 * @param dataMes
	 *            - data do fechamento.
	 *
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Receita> findReceitaAllNotClosed(final Date dataMes) {
		return receitaDao.findAllNotClosed(dataMes);
	}

	/**
	 * Busca uma todas as receitas de um determinado mes.
	 *
	 * @param monthDate
	 *            - data mes de referencia.
	 *
	 * @return lista de receitas do mes passado por parametro.
	 */
	public List<Receita> findReceitaByMonthDate(final Date monthDate) {
		return receitaDao.findByMonthDate(monthDate);
	}

	/**
	 * Validacao do ClosingDateRevenue. Caso startDate (data de inicio da
	 * vigencia) seja maior do que a data Revenue, a vigencia e valida e pode
	 * ser adicionada ou removida. Caso contrario, a vigencia nao pode ser
	 * adicionada nem removida.
	 *
	 * @param startDate
	 *            - data de inicio da vigencia
	 * @param showMsgError
	 *            - mostra mensagem de erro caso startDate nao seja valido
	 *
	 * @return true se startDate for maior do que ClosingDateRevenue. false caso
	 *         contrario
	 */
	public Boolean verifyClosingDateRevenue(final Date startDate,
											final Boolean showMsgError) {
		Date closingRevenueDate = moduloService.getClosingDateRevenue();
		if (startDate.compareTo(closingRevenueDate) > 0) {
			return Boolean.valueOf(true);
		} else {
			if (showMsgError) {
				Messages.showError("verifyClosingDateRevenue",
						Constants.MSG_ERROR_AJUSTE_RECEITA_ADD_CLOS_DATE,
						DateUtil.formatDate(closingRevenueDate,
								Constants.DEFAULT_DATE_PATTERN_SIMPLE,
								Constants.DEFAULT_CALENDAR_LOCALE));
			}

			return Boolean.valueOf(false);
		}
	}

	/**
	 * Valida a data de inicio de Revenue. E necessario que o inicio da vigencia
	 * da Revenue seja maior que a data de closing Internacional.
	 *
	 * @param startDateRevenue
	 *            - data do inicio da vigencia da Revenue
	 * @param showMsgError
	 *            - mostra mensagem de erro caso startDateRevenue nao seja
	 *            valido
	 *
	 * @return true caso {@code startDateRevenue} seja maior que a data de
	 *         fechamento de Revenue Internacional. E false caso contrario
	 */
	public Boolean verifyClosingDateInternationalRevenue(
			final Date startDateRevenue, final Boolean showMsgError) {
		Date closingInternationalRevenue = moduloService
				.getClosingDateInternationalRevenue();
		if (startDateRevenue.compareTo(closingInternationalRevenue) > 0) {
			return Boolean.TRUE;
		} else {
			if (showMsgError) {
				Messages.showError(
						"verifyClosingDateInternationalRevenue",
						Constants.MSG_ERROR_RECEITA_INTERNACIONAL_ADD_CLOS_DATE,
						DateUtil.formatDate(closingInternationalRevenue,
								Constants.DEFAULT_DATE_PATTERN_SIMPLE,
								Constants.DEFAULT_CALENDAR_LOCALE));
			}

			return Boolean.FALSE;
		}
	}

	/**
	 * Validacao do ClosingDateRevenue. Caso startDate (data de inicio da
	 * vigencia) seja maior do que a data Revenue, a vigencia e valida e pode
	 * ser adicionada ou removida. Caso contrario, a vigencia nao pode ser
	 * adicionada nem removida.
	 *
	 * the value 0 if the argument Date is equal to this Date; a value less than
	 * 0 if this Date is before the Date argument; and a value greater than 0 if
	 * this Date is after the Date argument.
	 *
	 * @param startDate
	 *            - data de inicio da vigencia
	 *
	 * @return true se startDate for maior do que ClosingDateRevenue. false caso
	 *         contrario
	 */
	public int verifyClosingDateRevenueCompareTo(final Date startDate) {
		Date closingRevenueDate = moduloService.getClosingDateRevenue();
		return startDate.compareTo(closingRevenueDate);
	}

	/**
	 * Compara {@code startDateRevenue} com a data de fechamento de
	 * International Revenue.
	 *
	 * @param {@code startDateRevenue} - Data de inicio da vigencia da Receita
	 *
	 * @return o valor 0 se o {@code startDateRevenue} e igual a data de
	 *         fechamento de Internatinal Revenue; um valor menor que 0 se a
	 *         data de fechamento for antes/menor que {@code startDateRevenue};
	 *         e um valor maior que 0 se a data de fechamento for depois/maior
	 *         que {@code startDateRevenue}.
	 */
	public int verifyClosingDateInternationalRevenueCompareTo(
			final Date startDateRevenue) {
		Date closingInternationalRevenueDate = moduloService
				.getClosingDateInternationalRevenue();

		return startDateRevenue.compareTo(closingInternationalRevenueDate);
	}

    /**
     * Checa se eh uma receita internacional sem recursos intercompany..
     *
     * @param receita
     * @param dealFiscal
     * @return boolean
     */
    @Transactional
	public boolean isInternationalRevenueWithoutIntercompanyResource(final Map<Long, String> companyErp,
																	 final Receita receita,
																	 final DealFiscal dealFiscal) {
		return (this.isInternationalRevenue(companyErp, receita) && !this.hasIntercompanyResource(receita, dealFiscal));
	}

	@Transactional
	public boolean isInternationalRevenueWithIntercompany(final Map<Long, String> companyErp,
														  final Receita receita,
														  final DealFiscal dealFiscal) {
		return (this.isInternationalRevenue(companyErp, receita) && this.hasIntercompanyResource(receita, dealFiscal));
	}

	/**
	 * Verifica se a empresa de {@code receita} e internacional.
	 *
	 * @param receita
	 * @return {@link Boolean}
	 */
	@Transactional
	public Boolean isInternationalRevenue(final Map<Long, String> companyErp, final Receita receita) {
		List<ReceitaDealFiscal> receitasDealFiscal = receitaDealFiscalService
				.findReceitaDealByReceita(receita);

		Cliente cliente = null;
		DealFiscal dealFiscal = null;
		Empresa empresa = null;
		for (ReceitaDealFiscal receitaDealFiscal : receitasDealFiscal) {
			dealFiscal = dealFiscalService.findDealFiscalById(receitaDealFiscal
					.getDealFiscal().getCodigoDealFiscal());
			cliente = clienteService.findClienteById(dealFiscal.getCliente()
					.getCodigoCliente());
			empresa = empresaService.findEmpresaById(dealFiscal.getEmpresa()
					.getCodigoEmpresa());
			if (cliente.getIndicadorTipo().equals(Constants.CLIENTE_TYPE_INTERNATIONAL)
					&& !Constants.LEGACY_ERP_BRAZIL.equals(companyErp.get(empresa.getCodigoEmpresa()))) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	/**
	 * Retorna uma lista de Deal Fiscal referente a receita informada.
	 *
	 * @param receita
	 *            - recebe uma receita
	 *
	 * @return retorna uma lista de deal fiscal
	 */
	public List<DealFiscal> getAllFiscalDealIntercompany(final Receita receita) {

		List<DealFiscal> result = new ArrayList<DealFiscal>();

		List<ReceitaDealFiscal> receitaDealFiscalList = rdfService
				.findReceitaDealByReceita(receita);

		if (receitaDealFiscalList != null && !receitaDealFiscalList.isEmpty()) {
			for (ReceitaDealFiscal rdf : receitaDealFiscalList) {
				// se for intercompany adiciona no resultado
				if (rdf.getDealFiscal().getIndicadorIntercompany()) {
					result.add(rdf.getDealFiscal());
				}
			}
		}

		return result;
	}

	/**
	 * Obtem a diferenca (em percentual) entre dois valores.
	 *
	 * @param value1
	 *            valor um
	 * @param value2
	 *            valor dois
	 * @return o percentual correspondente a diferenca entre os valores.
	 */
	public BigDecimal getPercentualDiferenca(final BigDecimal value1,
											 final BigDecimal value2) {

		// retorna 100% caso algum dos valores seja zero ou null
		if ((value1 == null || value2 == null)
				|| (value1.doubleValue() == 0 || value2.doubleValue() == 0)) {
			return BigDecimal.valueOf(100);
		}

		// retorna 0% caso os valores sejam iguais
		if (value1.equals(value2)) {
			return BigDecimal.valueOf(0);
		}

		BigDecimal v1 = value1;
		if (v1.doubleValue() < 0) {
			v1 = value1.multiply(BigDecimal.valueOf(-1));
		}

		BigDecimal v2 = value2;
		if (v2.doubleValue() < 0) {
			v2 = value2.multiply(BigDecimal.valueOf(-1));
		}

		BigDecimal diferenca = value1.subtract(value2);
		if (diferenca.doubleValue() < 0) {
			diferenca = diferenca.multiply(BigDecimal.valueOf(-1));
		}

		// para garantir que independente da ordem dos parametros o resultado
		// sera o mesmo
		BigDecimal divisor = v1.doubleValue() > v2.doubleValue() ? v1 : v2;

		return diferenca.multiply(BigDecimal.valueOf(100)).divide(divisor,
				BigDecimal.ROUND_UP);
	}

	/**
	 * Checa se a diferenca em percentual entre o valor da Receita e o valor da
	 * {@link ReceitaResultado} (Foto) e superior ao percentual toleravel.
	 *
	 * @param valorTotalReceita
	 *            recebe o valor total de uma receita
	 *
	 * @param valorFotoReceita
	 *            recebe o valor total da receita de um {@link ReceitaResultado}
	 *
	 * @return retorna um booleano - true caso o valor excede o percentual
	 *         tolerado.
	 */
	public boolean excedePercentualToleravel(
			final BigDecimal valorTotalReceita,
			final BigDecimal valorFotoReceita) {

		BigDecimal percentualDiferenca = BigDecimal.valueOf(0);

		// obtem o percentual aceitavel
		BigDecimal percentualTolerancia = new BigDecimal(
				systemProperties
						.getProperty("receita.publish.percentual_tolerancia"));

		// Obtem a diferenca dos valores em percentual
		percentualDiferenca = this.getPercentualDiferenca(valorTotalReceita,
				valorFotoReceita);

		// checa se excedeu o percentual tolerado
		return percentualDiferenca.doubleValue() > percentualTolerancia
				.doubleValue();
	}

	/**
	 * Remove a receita e limpa todas as justificativas ShortTermRevenue por
	 * receita moeda caso a receita a ser excluida possua alguma justificativa.
	 * Remove a lista de Ajustes de Receita referente a receita.
	 *
	 * @param receita
	 *            receita a ser removida (juntamente com as justificativas)
	 */
	@Transactional
	public void removeReceitaAndShortTermRevenueAndAdjusts(final Receita receita) {

		// filtro para buscar o receita resultado
		ReceitaResultado filter = new ReceitaResultado();
		filter.setContratoPratica(receita.getContratoPratica());
		filter.setDataMes(receita.getDataMes());

		ReceitaResultado rr = null;
		// itera por todas as moedas da receita para apagar todas as
		// justificativas
		for (ReceitaMoeda rm : receita.getReceitaMoedas()) {
			filter.setMoeda(rm.getMoeda());
			rr = this.receitaResultadoService
					.findReceitaResultadoByContratoAndMoedaAndDate(filter);
			if (rr != null) {
				// apaga os campos do usuario
				receitaResultadoService.removeShortTermRevenue(rr);
			}
		}

		// Deleta os Ajustes de Receita referente a Receita.
		ajusteReceitaSerice.removeAjusteReceitaByReceita(receita);

		// remove a receita
		this.removeReceita(receita);
	}

	/**
	 * Metodo utilizado para saber qual regra de claculo deve ser adotada para
	 * uma determinada receita na tela de Financials. Retorna um codigo
	 * indicando qual das regras listadas abaixo a receita se encaixa. 0 =
	 * Projetada, 1 = Receitada e 2 = None.
	 *
	 * <table border="2">
	 * <tr>
	 * <td><strong>Mes Fechado (closing date)?</strong></td>
	 * <td><strong>Tem Receita Publicada?</strong></td>
	 * <td><strong>Fonte</strong></td>
	 * <td><strong>Retorno</strong></td>
	 * </tr>
	 * <tr>
	 * <td>NAO</td>
	 * <td>NAO</td>
	 * <td>MapaAlocacao</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>SIM</td>
	 * <td>NAO</td>
	 * <td>Zero com excecao do FTE total que vem do MapaAlocacao</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td>NAO</td>
	 * <td>SIM</td>
	 * <td>Receita</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>SIM</td>
	 * <td>SIM</td>
	 * <td>Receita</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 *
	 * @param dataMes
	 *            - mes referencia
	 *
	 * @param contratoPratica
	 *            - c-lob em questao
	 *
	 * @param moeda
	 *            - moeda em questao
	 *
	 * @return indicador de regra de calculo.
	 */
	@Override
	public int getRevenueCalculationRule(final Date dataMes,
										 final ContratoPratica contratoPratica, final Moeda moeda) {

		ReceitaMoeda receitaMoeda = receitaMoedaService
				.findReceitaMoedaByClobAndMoedaAndDataMes(contratoPratica,
						moeda, dataMes);

		if (receitaMoeda != null
				&& (Constants.VERSION_RECEITA_PUBLISHED.equals(receitaMoeda
				.getReceita().getIndicadorVersao()) || Constants.VERSION_RECEITA_INTEGRATED
				.equals(receitaMoeda.getReceita().getIndicadorVersao()))) {
			return Constants.REVENUE_CALCULATION_RULE_RECEITED;
		} else if (this.getClosingDateReceita().compareTo(dataMes) >= 0) {
			return Constants.REVENUE_CALCULATION_RULE_NONE;
		}

		return Constants.REVENUE_CALCULATION_RULE_PROJECTED;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ciandt.pms.business.service.IReceitaService#findUltimasReceitasByCP
	 * (java.lang.Long, java.lang.Long)
	 */
	public List<Receita> findUltimasReceitasByCP(Long meses,
												 Long codigoContratoPratica) {
		return receitaDao.findUltimasReceitasByCP(meses, codigoContratoPratica);
	}

	@Override
	public List<Receita> findAllReceitaForecastByCodigoContratoPratica(Long codigoContratoPratica) {
		return receitaDao.findAllReceitaForecastByCodigoContratoPratica(codigoContratoPratica);
	}

	@Override
	public void updateAllReceitaForecastDealFiscalByContratoPratica(List<DealFiscal> dealFiscals, Long codigoContratoPratica) {
		List<Receita> receitasForecast = this.findAllReceitaForecastByCodigoContratoPratica(codigoContratoPratica);

		for (DealFiscal dealFiscal : dealFiscals) {
			for (Receita receita : receitasForecast) {
				for (ReceitaMoeda receitaMoeda : receita.getReceitaMoedas()) {
					ReceitaMoeda rm = receitaMoedaService.findReceitaMoedaById(receitaMoeda.getCodigoReceitaMoeda());
					if (dealFiscal.getMoeda().getCodigoMoeda() == rm.getMoeda().getCodigoMoeda()) {
						List<ReceitaDealFiscal> receitaDealFiscals = receitaDealFiscalService.findReceitaDealFiscalByReceitaMoeda(rm);
						for (ReceitaDealFiscal receitaDealFiscal : receitaDealFiscals) {
							receitaDealFiscal.setDealFiscal(dealFiscal);
							receitaDealFiscalService.updateReceitaDealFiscal(receitaDealFiscal);
						}
					}
				}
			}
		}
	}

	@Transactional
	public void updateServiceRevenueFromMega(ReceitaDealFiscal receitaDealFiscal, String revenueStatus, BigDecimal megaOrderID, String errorMessage){

		if (revenueStatus.equals("A") && !receitaDealFiscal.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_INTEGRADO)){
			this.setStatusIntegratedRevenue(receitaDealFiscal, megaOrderID, errorMessage);
		}
		else if (revenueStatus.equals("E") && !receitaDealFiscal.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_INTEGRADO)){
			if (null != errorMessage || !"".equalsIgnoreCase(errorMessage)) {
				mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY,
						BundleUtil.getBundle("_nls.receita_integracao.mail.error.subject", receitaDealFiscal.getCodigoReceitaDfiscal()),
						BundleUtil.getBundle("_nls.receita_integracao.mail.error.message", errorMessage));
			}
			this.setStatusErrorRevenue(receitaDealFiscal, megaOrderID, errorMessage);
		}
	}

	@Transactional
	public void setStatusIntegratedRevenue(ReceitaDealFiscal receitaDealFiscal, BigDecimal megaOrderID, String errorMessage){
		if(receitaDealFiscal.getIndicadorIntegradoQuickbooks() == null || (receitaDealFiscal.getIndicadorIntegradoQuickbooks() != null && receitaDealFiscal.getIndicadorIntegradoQuickbooks().equals(Constants.YES))) {
			StatusReceitaDealFiscal statusReceitaDealFiscal = new StatusReceitaDealFiscal(receitaDealFiscal.getCodigoReceitaDfiscal(), Constants.INTEGRACAO_STATUS_INTEGRADO, errorMessage, megaOrderID);
			receitaDealFiscalService.updateStatusReceitaDealFiscal(statusReceitaDealFiscal);
		}
		//Mantem status Pendente, pois Quickbooks nao integrou ainda e atualizamos com o codigo Pedido no MEGA
		if(receitaDealFiscal.getIndicadorIntegradoQuickbooks() != null && receitaDealFiscal.getIndicadorIntegradoQuickbooks().equals(Constants.NO)) {
			StatusReceitaDealFiscal statusReceitaDealFiscal = new StatusReceitaDealFiscal(receitaDealFiscal.getCodigoReceitaDfiscal(), Constants.INTEGRACAO_STATUS_PENDENTE, errorMessage, megaOrderID);
			receitaDealFiscalService.updateStatusReceitaDealFiscal(statusReceitaDealFiscal);
		}
	}

	@Transactional
	public void updateRevenueStatus(Long revenueDealFiscalCode){
		Long revenueCode = this.findByRevenueDealFiscal(revenueDealFiscalCode);
		Receita receita = this.findReceitaById(revenueCode);
		ReceitaDealFiscal receitaDealFiscal = receitaDealFiscalService.findReceitaDealById(revenueDealFiscalCode);

		Boolean isIntegrated = receitaDealFiscalService.findNotIntegratedRevenues(receitaDealFiscal.getReceitaMoeda().getCodigoReceitaMoeda()) == 0;

		if (isIntegrated) {
			this.updateStatusReceita(receita.getCodigoReceita(), Constants.VERSION_RECEITA_INTEGRATED);
		}
		else {
			this.updateStatusReceita(receita.getCodigoReceita(), Constants.VERSION_RECEITA_PENDING);
		}
	}

	public void setStatusErrorRevenue(ReceitaDealFiscal receitaDealFiscal, BigDecimal megaOrderID, String errorMessage){
		Long revenueCode = this.findByRevenueDealFiscal(receitaDealFiscal.getCodigoReceitaDfiscal());
		Receita receita = this.findReceitaById(revenueCode);

		StatusReceitaDealFiscal statusReceitaDealFiscal = new StatusReceitaDealFiscal(receitaDealFiscal.getCodigoReceitaDfiscal(), Constants.INTEGRACAO_STATUS_ERROR, errorMessage, megaOrderID);
		receitaDealFiscalService.updateStatusReceitaDealFiscal(statusReceitaDealFiscal);
	}

	/**
	 * Ceheca se a receita possui recursos intercompany.
	 *
	 * @param receita
	 *            recebe a receita a ser checada.
	 *
	 * @return retorna true caso exista um ou mais recurso intercompany.
	 */
	private boolean hasIntercompanyResource(final Receita receita,
											final DealFiscal dealFiscal) {
		Receita r = this.findReceitaById(receita.getCodigoReceita());
		List<ItemReceita> itemReceitaList = new ArrayList<ItemReceita>();

		// popula a lista de Item de Receita
		for (ReceitaMoeda receitaMoeda : r.getReceitaMoedas()) {
			itemReceitaList.addAll(receitaMoeda.getItemReceitas());
		}

		List<HistoricoPercentualIntercomp> listIntercomp = historicoPercentualIntercompService.findByDealFiscal(dealFiscal.getCodigoDealFiscal());
		Iterator<HistoricoPercentualIntercomp> itr = listIntercomp.iterator();
		while (itr.hasNext()) {
			HistoricoPercentualIntercomp current = itr.next();
			if (current.getDataFim() != null && current.getDataFim().before(DateUtil.getDateFirstDayOfMonth(new Date()))) {
				itr.remove();
			}
		}

		if (itemReceitaList != null && !itemReceitaList.isEmpty()) {
			for (ItemReceita item : itemReceitaList) {
				// Considerar apenas recusros
				if (item.getValorTotalItem().compareTo(BigDecimal.ZERO) > 0) {
					PessoaTipoContrato pessoaTipoContrato = pessoaTipoContratoService
							.findPessTCByPessoaAndDate(item.getPessoa(), item
									.getReceitaMoeda().getReceita().getDataMes());

					Empresa empresaRecurso = empresaService
							.findEmpresaById(pessoaTipoContrato.getEmpresa()
									.getCodigoEmpresa());

					for (HistoricoPercentualIntercomp hist : listIntercomp) {
						if (hist.getEmpresaIntercomp().getEmpresa().getCodigoEmpresa().equals(empresaRecurso.getCodigoEmpresa())) {
							return true;
						}
					}
					// Se a empresa pai for Intercompany
					/*if (empresaRecurso.getCodigoEmpresa() == empresaDeal.getEmpresa().getCodigoEmpresa()) {
						return Boolean.TRUE;
					}*/
				}
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * M�todo responsavel para for�ar receita para Integrated os casos onde n�o fazem integra�ao com
	 * MEGA (China, Japao e Intercompany sem colaboradores BR).
	 *
	 * @param receita
	 */
	@Override
	public void updateRevenueStatusAsIntegrated(Receita receita) {
		String indStatusOld = receita.getIndicadorVersao();
		receita.setIndicadorVersao(Constants.VERSION_RECEITA_INTEGRATED);
		this.updateReceita(receita, indStatusOld);
	}

	/**
	 * Metodo para converter o objeto retornado direto da View para o JSON que ser� enviado para a fila de integra��o.
	 *
	 * @param model
	 * @return
	 */
	@Override
	public IncomingRevenue convertFromModel(VwPmsIntegReceitaNacional model) throws Exception {
		IncomingRevenue incomingRevenue = new IncomingRevenue();

		incomingRevenue.setBranchCompanyCode(model.getBranchCompanyCode());
		incomingRevenue.setIssueAt(model.getIssueAt());
		incomingRevenue.setSentAt(model.getSentAt());
		incomingRevenue.setPaymentConditionName(model.getPaymentConditionName());

		incomingRevenue.setTotalValue(model.getTotalValue());

		incomingRevenue.setRevenueCode(model.getRevenueCode());
		incomingRevenue.setInvoiceSituation(model.getInvoiceSituation());

		incomingRevenue.setAuxCode(model.getAuxCode());
		incomingRevenue.setCalcAgentCode(model.getCalcAgentCode());
		incomingRevenue.setCalcAgentTypeCode(model.getCalcAgentTypeCode());

		RevenueItem revenueItem = new RevenueItem();
		revenueItem.setItemOperationCode(model.getItemOperationCode());
		revenueItem.setItemSequencial(model.getItemSequencial());
		revenueItem.setItemCode(model.getItemCode());
		revenueItem.setItemQuantity(model.getItemQuantity());

		revenueItem.setUnitValue(model.getUnitValue());

		if(Constants.SERVICES_OTHERS_APL_CODE.contains(model.getServiceCode())
				&& model.getItemApplicationCode() != null
				&& !model.getItemApplicationCode().equals(Constants.INTERNATIONAL_APL_CODE)) {
			revenueItem.setItemApplicationCode(Constants.APL_SERVICE_CODE);
			incomingRevenue.setDocumentTypeCode(Constants.TPD_SERVICE_CODE);
		} else {
			revenueItem.setItemApplicationCode(model.getItemApplicationCode());
			incomingRevenue.setDocumentTypeCode(model.getDocumentTypeCode());
		}
		revenueItem.setClassType(model.getClassType());
		revenueItem.setIdProjectCode(model.getIdProjectCode());
		revenueItem.setProjectReduceCode(model.getProjectReduceCode());
		revenueItem.setIdCostCenterCode(model.getIdCostCenterCode());
		revenueItem.setCostCenterReduceCode(model.getCostCenterReduceCode());
		revenueItem.setServiceCode(model.getServiceCode());

		Installment installment = new Installment();
		installment.setInstallmentOperationCode(model.getInstallmentOperationCode());
		installment.setInstallmentNumber(model.getInstallmentNumber());
		installment.setInstallmentValue(model.getInstallmentValue());

		incomingRevenue.setRevenueItem(revenueItem);
		incomingRevenue.setInstallment(installment);

		return incomingRevenue;
	}

	/**
	 * Integra uma receita inviando para o ERP.
	 * @param receita {@link Receita}
	 * @return retorna a {@link Receita} integrada
	 */
	@Override
	public VwPmsIntegReceitaNacional integrate(VwPmsIntegReceitaNacional receita) throws NullPointerException, IOException, TimeoutException, Exception {
		this.validateRequiredFields(receita);

		IncomingRevenue incomingRevenue = this.convertFromModel(receita);
		this.revenueProducer.send(incomingRevenue.toJson(), Constants.REVENUE_PRODUCER);

		return receita;
	}

	/**
	 * Metodo que valida se todos os campos obrigatorios do XML que vai para o MEGA est� preenchido.
	 *
	 * @param vwPmsIntegReceitaNacional
	 * @return
	 */
	@Override
	public void validateRequiredFields(VwPmsIntegReceitaNacional vwPmsIntegReceitaNacional) {
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getBranchCompanyCode(), "Branch Company Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getIssueAt(), "Issue Date cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getSentAt(), "Sent Date cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getPaymentConditionName(), "Payment Condition Name cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getTotalValue(), "Total Value cannot be null, check currency quotation exists");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getInvoiceSituation(), "Invoice Situation cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getDocumentTypeCode(), "Document Type cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getAuxCode(), "Auxiliary Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getCalcAgentCode(), "Calc Agent Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getCalcAgentTypeCode(), "Calc Agent Type Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getItemSequencial(), "Item Sequencial Number cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getItemCode(), "Item Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getItemQuantity(), "Item Quantity cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getItemApplicationCode(), "Item Applicatoin Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getClassType(), "Item Class Type cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getUnitValue(), "Item Unit Value cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getIdProjectCode(), "Project ID Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getProjectReduceCode(), "Project Reduce Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getIdCostCenterCode(), "ID CostCenter Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getCostCenterReduceCode(), "CostCenter Reduce Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getServiceCode(), "Item Service Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getInstallmentNumber(), "Installment Number cannot be null");
		Preconditions.checkNotNull(vwPmsIntegReceitaNacional.getInstallmentValue(), "Installment Value cannot be null");
	}

	@Override
	public List<Receita> findNotIntegratedRevenueAfterClosingRevenueDate(final Long codigoDealFiscal,
																		 final Date closingMapDate) {
		return receitaDao.findNotIntegratedRevenueAfterClosingRevenueDate(codigoDealFiscal,
				closingMapDate);
	}

	@Override
	public Boolean isInternationalClient(Receita receita) {

		List<CpraticaDfiscal> cpraticaDfiscals =
				receita.getContratoPratica().getCpraticaDfiscals();
		Cliente cliente = null;
		DealFiscal dealFiscal = null;

		for (CpraticaDfiscal cpraticaDfiscal : cpraticaDfiscals) {
			dealFiscal = dealFiscalService.findDealFiscalById(cpraticaDfiscal
					.getDealFiscal().getCodigoDealFiscal());
			cliente = clienteService.findClienteById(dealFiscal.getCliente()
					.getCodigoCliente());
			if (cliente.getIndicadorTipo().equals(
					Constants.CLIENTE_TYPE_INTERNATIONAL)) {

				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	public List<Receita> findRevenuesByMsaAndDates(Long msa, Date startDate, Date endDate){
		return receitaDao.findRevenuesByMsaAndDates(msa, startDate, endDate);
	}
}