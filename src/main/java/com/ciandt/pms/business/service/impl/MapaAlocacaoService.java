/**
 * Classe MapaAlocacaoService - Implementação do serviço do MapaAlocacao.
 */
package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoPeriodoService;
import com.ciandt.pms.business.service.IAlocacaoService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IEtiquetaAlocacaoService;
import com.ciandt.pms.business.service.IEtiquetaService;
import com.ciandt.pms.business.service.IFatorUrMesService;
import com.ciandt.pms.business.service.IMapaAlocacaoPessoaService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.business.service.IVwPmsFinancialsService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.EstratificacaoUr;
import com.ciandt.pms.model.EtiquetaAlocacao;
import com.ciandt.pms.model.EtiquetaAlocacaoId;
import com.ciandt.pms.model.FatorUrMes;
import com.ciandt.pms.model.ItemEstratificacaoUr;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaAlocacaoPessoa;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.model.VwAlocContratoRecursoMes;
import com.ciandt.pms.model.VwPmsFinancials;
import com.ciandt.pms.model.VwPmsFinancialsId;
import com.ciandt.pms.model.vo.AlocacaoCell;
import com.ciandt.pms.model.vo.AlocacaoContratoPraticaRow;
import com.ciandt.pms.model.vo.AlocacaoRow;
import com.ciandt.pms.model.vo.MapDashboardMoeda;
import com.ciandt.pms.model.vo.MapDashboardRow;
import com.ciandt.pms.persistence.dao.IMapaAlocacaoDao;
import com.ciandt.pms.persistence.dao.IVwAlocContratoRecursoMesDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A classe MapaAlocacaoService proporciona as funcionalidades de serviço para a
 * entidade MapaAlocacao.
 * 
 * @since 12/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class MapaAlocacaoService implements IMapaAlocacaoService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IMapaAlocacaoDao mapaAlocacaoDao;

	/** Instancia do DAO da FatorUrMes. */
	@Autowired
	private IFatorUrMesService fatorUrMesService;

	/** Instancia do servico Alocacao. */
	@Autowired
	private IAlocacaoService alocacaoService;

	/** Instancia do servico PerfilVendido. */
	@Autowired
	private IPerfilVendidoService perfilVendidoService;

	/** Instancia do servico AlocacaoPeriodo. */
	@Autowired
	private IAlocacaoPeriodoService alocacaoPeriodoService;

	/** Instancia do servico Recurso. */
	@Autowired
	private IRecursoService recursoService;

	/** Instancia do servico Etiqueta. */
	@Autowired
	private IEtiquetaService etiquetaService;

	/** Instancia do servico EtiquetaAlocacao. */
	@Autowired
	private IEtiquetaAlocacaoService etiquetaAlocacaoService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do servico Cidade Base. */
	@Autowired
	private ICidadeBaseService cidadeBaseService;

	/** Instancia do servico MapaAlocacaoPessoa. */
	@Autowired
	private IMapaAlocacaoPessoaService mapaAlocPessService;

	/** Instancia do DAO VwAlocContratoRecursoMesDao. */
	@Autowired
	private IVwAlocContratoRecursoMesDao alocContRecDao;

	/** Instancia do DAO VwPmsFinancialsService. */
	@Autowired
	private IVwPmsFinancialsService vwPmsFinancialsService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Instancia do servico da Receita. */
	@Autowired
	private IReceitaService receitaService;

	/** Instancia do servico de {@link ContratoPratica}. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createMapaAlocacao(final MapaAlocacao entity) {
		this.setNewTxtTitulo(entity);
		// seta a data inicio da janela de visualização
		entity.setDataInicioJanela(entity.getDataInicio());
		entity.setCodigoMapaAlocacao(null);
		mapaAlocacaoDao.create(entity);
	}

	/**
	 * Metodo que a ajusta a data inicio da janela do mapa de aloação.
	 * 
	 * @param mapa
	 *            - Mapaalocacao que se deseja ajustar a janela.
	 */
	public void ajustaDataInicioJanela(final MapaAlocacao mapa) {
		Date dataInicio = mapa.getDataInicio();
		// pega a data final do mapa menos tamanho da janela
		Date data = DateUtils
				.addMonths(
						mapa.getDataFim(),
						-Integer.valueOf(systemProperties
								.getProperty(Constants.MAPA_ALOCACAO_DEFAULT_WINDOW_SIZE)));

		// verifica se a data da janela é menor que a data inicial do
		// mapa ou se a janela for menor que a vigencia do mapa.
		if (dataInicio.after(mapa.getDataInicioJanela())
				|| data.before(dataInicio)) {
			mapa.setDataInicioJanela(dataInicio);
		} else {
			// pega a data da janela mais tamanho da janela
			Date dataAux = DateUtils
					.addMonths(
							mapa.getDataInicioJanela(),
							+Integer.valueOf(systemProperties
									.getProperty(Constants.MAPA_ALOCACAO_DEFAULT_WINDOW_SIZE)));
			// verifica se a data inicial da janela é depois a
			// data final menos tamanho da janela
			if (dataAux.after(mapa.getDataFim())) {
				mapa.setDataInicioJanela(data);
			}
		}
	}

	/**
	 * Atualiza a entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 */
	public void updateMapaAlocacao(final MapaAlocacao entity) {
		this.setNewTxtTitulo(entity);
		mapaAlocacaoDao.update(entity);
	}

	@Transactional
	public void atualizaSufixosMapaAlocacao(ContratoPratica contratoPratica) {
		if (contratoPratica == null ) {
			return;
		}
		MapaAlocacao filter = new MapaAlocacao();
		filter.setContratoPratica(contratoPratica);
		List<MapaAlocacao> mapaAlocacaoList = findMapaAlocacaoByFilter(filter);

		for (MapaAlocacao mapaAlocacao: mapaAlocacaoList) {
			updateMapaAlocacao(mapaAlocacao);
		}
	}

	/**
	 * Realiza o update da viagencia do mapa de alocação.
	 * 
	 * @param mapa
	 *            - entidade do tipo MapaAlocacao
	 * @param startDate
	 *            - Data inicia da nova vigencia
	 * @param endDate
	 *            - Data final da nova vigencia
	 */
	public void updateMapaAlocacaoPeriod(final MapaAlocacao mapa,
			final Date startDate, final Date endDate) {

		mapa.setDataInicio(startDate);
		mapa.setDataFim(endDate);
		ajustaDataInicioJanela(mapa);

		// deleta todas as AlocacoesPeriodo do periodo do mapa.
		alocacaoPeriodoService.deleteAlocacaoPeriodoByMapaAndNotInRange(mapa);

		updateUrMonthList(mapa);

		updateMapaAlocacao(mapa);
	}

	/**
	 * Realiza o merge da entidade.
	 * 
	 * @param entity
	 *            que sera sofrerá o merge
	 * 
	 * @return a entidade atualizada
	 */
	public MapaAlocacao mergeMapaAlocacao(final MapaAlocacao entity) {
		return mapaAlocacaoDao.merge(entity);
	}

	/**
	 * Busca o Mapa pelo contratoPratica que possuem a versão igual a
	 * 'Published'.
	 * 
	 * @param cp
	 *            - Entidade ContratoPratica.
	 * 
	 * @return retorna o MapaAlocacao.
	 */
	public MapaAlocacao findMapaAlocacaoByContratoPratica(
			final ContratoPratica cp) {
		return mapaAlocacaoDao.findByContratoPratica(cp);
	}

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 * 
	 * @return retorna true se removido com sucesso, caso contrario false
	 */
	public Boolean removeMapaAlocacao(final MapaAlocacao entity) {
		// pega a data de fechamento do modulo do mapa de alocacao
		Date closingDate = this.getClosingDateMapaAlocacao();

		Date minDate = alocacaoPeriodoService.findMinDateByMapa(entity);
		// se min date é igual a null, não existe alocação periodo.
		// então mapa pode ser removido
		if (minDate != null) {

			Date validityDateBeg = DateUtils.truncate(minDate, Calendar.MONTH);
			Date dateCurrent = DateUtils.truncate(closingDate, Calendar.MONTH);

			// verifica se data incial da vigência do mapa é menor do que a data
			// corrente, se sim, lança exceção e não deixa excluir
			if (!Constants.VERSION_DRAFT.equals(entity.getIndicadorVersao())
					&& validityDateBeg.compareTo(dateCurrent) <= 0) {
				Messages.showError("remove",
						Constants.MSG_ERROR_REMOVE_ALLOCATION_MAP);

				return false;
			}
		}
		this.remove(entity);

		return true;
	}

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 */
	private void remove(final MapaAlocacao entity) {

		MapaAlocacao mapa = this.findMapaAlocacaoById(entity
				.getCodigoMapaAlocacao());

		// remove a lista de MapaAlocacaoPessoa do mapa.
		List<MapaAlocacaoPessoa> maPessList = mapaAlocPessService
				.findMapaAlocPessoaByMapa(mapa.getCodigoMapaAlocacao());

		mapaAlocPessService.unfollowListaMapaAlocacao(mapa, maPessList);

		// itera a lista de Alocacao e por sua vez a lista de AlocacaoPeriodo de
		// cada Alocacao e exclui cada entidade, uma a uma
		List<Alocacao> alocacaoList = mapa.getAlocacaos();
		for (Alocacao alocacao : alocacaoList) {
			Set<AlocacaoPeriodo> alocacaoPeriodoList = alocacao
					.getAlocacaoPeriodos();
			for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoList) {
				// remove AlocacaoPeriodo
				alocacaoPeriodoService.removeAlocacaoPeriodo(alocacaoPeriodo);
			}

			// remove Alocacao
			alocacaoService.removeAlocacao(alocacaoService
					.findAlocacaoById(alocacao.getCodigoAlocacao()));
		}

		// remove a lista de FatorUrMes do mapa.
		List<FatorUrMes> fatorUrMeseList = mapa.getFatorUrMeses();
		for (FatorUrMes fatorUrMes : fatorUrMeseList) {
			fatorUrMesService.removeFatorUrMes(fatorUrMes);
		}

		// remove MapaAlocacao
		mapaAlocacaoDao.remove(mapa);
	}

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	public MapaAlocacao findMapaAlocacaoById(final Long entityId) {
		return mapaAlocacaoDao.findById(entityId);
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<MapaAlocacao> findMapaAlocacaoByFilter(final MapaAlocacao filter) {
		return mapaAlocacaoDao.findByFilter(filter);
	}

	/**
	 * Busca uma lista de entidades pelo filtro. Também carrega asentidades
	 * relacionadas.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro e carrega.
	 * @param cli
	 *            entidade Cliente
	 * @param msa
	 *            entidade Msa
	 * @param natureza
	 *            entidade NaturezaCentroLucro
	 * @param centroLucro
	 *            entidade CentroLucro
	 * @param isFollowingOnly
	 *            - flag somente MapaAlocacao que a Pessoa corrente está
	 *            seguindo.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<MapaAlocacao> findMapaAlocacaoByFilterFetch(
			final MapaAlocacao filter, final Cliente cli, final Msa msa,
			final NaturezaCentroLucro natureza, final CentroLucro centroLucro,
			final Boolean isFollowingOnly) {

		// se Following Only, retorna a lista de MapaAlocacao que o follower
		// está
		// seguindo
		if (isFollowingOnly) {
			return mapaAlocacaoDao.findByFilterFetch(filter, cli, msa,
					natureza, centroLucro, LoginUtil.getLoggedUser());
		} else {
			return mapaAlocacaoDao.findByFilterFetch(filter, cli, msa,
					natureza, centroLucro, null);
		}
	}

	/**
	 * Busca uma lista de entidades pelo filtro. Também carrega as entidades
	 * relacionadas.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro e carrega.
	 * @param cli
	 *            entidade Cliente
	 * @param monthDate
	 *            data para filtro
	 * @param natureza
	 *            NaturezaCentroLucro
	 * @param centroLucro
	 *            CentroLucro
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<MapaAlocacao> findMapaAlocacaoByFilter(
			final MapaAlocacao filter, final Cliente cli, final Date monthDate,
			final NaturezaCentroLucro natureza, final CentroLucro centroLucro) {

		return mapaAlocacaoDao.findByFilter(filter, cli, monthDate, natureza,
				centroLucro);
	}

	/**
	 * Busca uma lista de entidades pelo filtro. Também carrega as entidades
	 * relacionadas.
	 * 
	 * @param monthDate
	 *            data para filtro
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<MapaAlocacao> findMapaAlocacaoByDate(final Date monthDate) {
		return mapaAlocacaoDao.findByDate(monthDate);
	}

	/**
	 * Prepara a matriz de Alocacao X AlocacaoPeriodo para criação do
	 * MapaAlocaao.
	 * 
	 * @param perfilVendidoSet
	 *            - lista de PerfilVendido do ContratoPratica selecionado
	 * @param validityDateList
	 *            - lista de datas do periodo de vigencia ordenado
	 * @return lista de AlocacaoRow - matriz de Alocacao X AlocacaoPeriodo
	 */
	public List<AlocacaoRow> prepareCreateMatrixAlocacao(
			final Set<PerfilVendido> perfilVendidoSet,
			final List<Date> validityDateList) {
		// lista final de AlocacaoRow que será retornada
		List<AlocacaoRow> alocacaoRowList = new ArrayList<AlocacaoRow>();
		int rowCont = 0;

		// dado uma lista de PerfilVendido, cria uma Alocacao para cada
		for (PerfilVendido perfilVendido : perfilVendidoSet) {
			Alocacao alocacao = new Alocacao();
			alocacao.setPerfilVendido(perfilVendido);
			// atribui o valor do Utilization Rate
			alocacao.setValorUr(BigDecimal.valueOf(0));
			// cria uma AlocacaoRow (Alocacao) para ser inserida na matriz
			AlocacaoRow row = new AlocacaoRow(alocacao, rowCont++);

			// cria uma lista de AlocacaoCell (AlocacaoPeriodo) em branco
			List<AlocacaoCell> alocacaoCellList = createMatrixLineAlocacao(
					alocacao, validityDateList, BigDecimal.valueOf(0));

			// atribui essa lista de AlocacaoCell à AlocacaoRow corrente
			row.setAlocacaoCellList(alocacaoCellList);
			// adiciona a AlocacaoRow corrente à lista final
			alocacaoRowList.add(row);
		}

		return alocacaoRowList;
	}

	/**
	 * Cria a lista de FatorUrMes.
	 * 
	 * @param mapaAlocacao
	 *            - entidade do tipo MapaAlocacao
	 * 
	 * @return retorna a lista de FatorUrMes criada
	 */
	public List<FatorUrMes> createUrMonthList(final MapaAlocacao mapaAlocacao) {
		// cria a lista de datas dado data inicial e final da vigência
		List<Date> dateList = DateUtil.getValidityDateList(
				mapaAlocacao.getDataInicio(), mapaAlocacao.getDataFim());

		List<FatorUrMes> fatorUrList = new ArrayList<FatorUrMes>();
		FatorUrMes fator = null;
		for (Date date : dateList) {
			fator = new FatorUrMes();

			fator.setDataMes(date);
			fator.setMapaAlocacao(mapaAlocacao);
			fator.setPercentualFatorUr(BigDecimal.valueOf(1));

			fatorUrList.add(fator);
		}

		return fatorUrList;
	}

	/**
	 * Gera a lista de UR por Mes do mapa de alocação, e persiste no banco.
	 * 
	 * @param mapa
	 *            - MapaAlocacao em questão.
	 * 
	 */
	public void updateUrMonthList(final MapaAlocacao mapa) {

		// cria a lista de datas dado data inicial e final da vigência
		List<Date> validityDateList = DateUtil.getValidityDateList(
				mapa.getDataInicio(), mapa.getDataFim());

		// deleta todos os FatorUrMes fora do periodo.
		fatorUrMesService.deleteFatorUrByMapaAndNotInRange(mapa);

		FatorUrMes f = null;
		for (Date date : validityDateList) {
			if (fatorUrMesService.findFatorUrMesByMapaAndDate(mapa, date) == null) {

				f = new FatorUrMes();
				f.setDataMes(date);
				f.setMapaAlocacao(mapa);
				f.setPercentualFatorUr(BigDecimal.valueOf(1));

				fatorUrMesService.createFatorUrMes(f);
			}
		}
	}

	/**
	 * Busca todas as alocações referente a um recurso existente na alocacao
	 * passado por parametro. Returna uma lista com todas as alocações e
	 * periodos das alocações.
	 * 
	 * @param alocacao
	 *            - alocacao que se deseja buscar o recurso.
	 * @param dateList
	 *            - lista com todos as datas que se deseja saber o percentual de
	 *            alocacao
	 * 
	 * @return - retorna uma lista de AlocacaoContratoPraticaRow com todas as
	 *         alocações.
	 */
	public List<AlocacaoContratoPraticaRow> findMapaAlocacaoAllByRecurso(
			final Alocacao alocacao, final List<Date> dateList) {
		// declaração de variaveis
		Alocacao alocacaoAux;
		// falg utilizada para verificar se toda a lista de alocacao
		// esta com o percentual igual a zero
		boolean allAlocationIsZero = true;
		MapaAlocacao mapa;
		ContratoPratica cp;
		AlocacaoContratoPraticaRow row;
		List<AlocacaoCell> alocacaoCellList;
		// lista de retorno
		List<AlocacaoContratoPraticaRow> ret = new ArrayList<AlocacaoContratoPraticaRow>();
		// map com as row (linhas), utilizada como auxiliar para recuperar a
		// referencia
		Map<Long, AlocacaoContratoPraticaRow> rowAuxMap = new HashMap<Long, AlocacaoContratoPraticaRow>();
		Map<Long, ContratoPratica> mapAux = new HashMap<Long, ContratoPratica>();
		// pega o recurso atraves da alocação
		Recurso r = recursoService.findRecursoById(alocacao.getRecurso()
				.getCodigoRecurso());

		// pega o mapa de alocacao
		MapaAlocacao mapaCorrente = alocacao.getMapaAlocacao();

		// pega todas as alocações de um recurso
		Iterator<Alocacao> alocacaoIt = r.getAlocacaos().iterator();
		while (alocacaoIt.hasNext()) {
			// pega uma alocação
			alocacaoAux = alocacaoIt.next();
			// pega o mapa dessa alocação
			mapa = alocacaoAux.getMapaAlocacao();
			// verifica se o mapa uma versão publicada ("PB") e se
			// diferente do mapa (da alocação) passado por parametro
			if (Constants.VERSION_PUBLISHED.equals(mapa.getIndicadorVersao())
					&& mapaCorrente.getCodigoMapaAlocacao().compareTo(
							mapa.getCodigoMapaAlocacao()) != 0) {

				cp = mapa.getContratoPratica();
				// verifica se o contrato-pratica já existe
				boolean containsKey = rowAuxMap.containsKey(cp
						.getCodigoContratoPratica());
				// se não existe no map
				if (!containsKey) {
					// cria uma lista de AlocacaoCell (AlocacaoPeriodo) em
					// branco
					alocacaoCellList = createMatrixLineAlocacao(alocacao,
							dateList, BigDecimal.valueOf(0));
					// cria uma nova instancia da linha
					row = new AlocacaoContratoPraticaRow();
					// seta o recurso
					row.setRecurso(alocacaoAux.getRecurso());
					// seta o contrato-pratica
					row.setContratoPratica(cp);
					// seta a lista celulas (lista com os periodos das alocações
					// em cada mes)
					row.setAlocacaoCellList(alocacaoCellList);
					// adiciona o contrato-pratica no map
					rowAuxMap.put(cp.getCodigoContratoPratica(), row);
					// adiciona a nova linha na lista de retorno
					// ret.add(row);

					// se o contrato-pratica já existe no map
				} else {
					// pega a linha (lista com os periodos das alocações em cada
					// mes)
					row = rowAuxMap.get(cp.getCodigoContratoPratica());
					// pega a lista de celulas
					alocacaoCellList = row.getAlocacaoCellList();
				}

				// pega a lista de AlocacaoPeriodo da alocacao
				Set<AlocacaoPeriodo> alocacaoPeriodoList = alocacaoAux
						.getAlocacaoPeriodos();

				// percorre todas as celulas
				for (AlocacaoCell cell : alocacaoCellList) {
					// percorre todas os periodos de alocacao para setar
					// os percentual de alocacao de cada mes
					for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoList) {
						AlocacaoPeriodo ap = cell.getAlocacaoPeriodo();
						// compara as datas para saber exatamente qual "celula"
						// da tabela/grade será preenchida
						if (alocacaoPeriodo.getDataAlocacaoPeriodo().compareTo(
								ap.getDataAlocacaoPeriodo()) == 0) {
							// allAlocationIsZero = false;
							mapAux.put(cp.getCodigoContratoPratica(), cp);
							// se codigo da AlocacaoPeriodo for diferente de
							// null então esta alocacao já existe no mesmo mapa,
							// então soma o percentual
							if (ap.getCodigoAlocacaoPeriodo() != null) {
								ap.setPercentualAlocacaoPeriodo(alocacaoPeriodo
										.getPercentualAlocacaoPeriodo()
										.add(ap.getPercentualAlocacaoPeriodo()));

								// senao seta o novo percentual
							} else {
								cell.setAlocacaoPeriodo(alocacaoPeriodo);
							}
							break;
						}
					}
				}
			}
		}

		// itera o map auxiliar para verificar quais ContratoPratica são válidos
		// para serem exibidos na lista final, somente os que tem pelo menos uma
		// alocacao, nao tem todas as alocacoes zeradas
		Iterator<Long> iterator = mapAux.keySet().iterator();
		while (iterator.hasNext()) {
			Long codigoContratoPratica = iterator.next();
			ret.add(rowAuxMap.get(codigoContratoPratica));
		}

		Pessoa p = pessoaService.findPessoaByRecurso(r);
		List<AlocacaoOverhead> alocacaoOverheadList = p.getAlocacoesOverhead();

		for (AlocacaoOverhead alocacaoOverhead : alocacaoOverheadList) {

			row = new AlocacaoContratoPraticaRow();
			allAlocationIsZero = true;

			alocacaoCellList = createMatrixLineAlocacao(alocacao, dateList,
					BigDecimal.valueOf(0));

			Iterator<AlocacaoPeriodoOh> itAlocacaoPeriodo = alocacaoOverhead
					.getAlocacaoPeriodoOhs().iterator();

			while (itAlocacaoPeriodo.hasNext()) {
				AlocacaoPeriodoOh alocacaoPeriodoOh = (AlocacaoPeriodoOh) itAlocacaoPeriodo
						.next();

				AlocacaoPeriodo ap = new AlocacaoPeriodo();

				for (AlocacaoCell alocacaoCell : alocacaoCellList) {

					if (alocacaoPeriodoOh.getDataAlocPeriodoOh() != null
							&& alocacaoCell.getAlocacaoPeriodo()
									.getDataAlocacaoPeriodo() != null
							&& alocacaoPeriodoOh.getDataAlocPeriodoOh()
									.compareTo(
											alocacaoCell.getAlocacaoPeriodo()
													.getDataAlocacaoPeriodo()) == 0) {
						ap.setPercentualAlocacaoPeriodo(alocacaoPeriodoOh
								.getPercentualAlocPeriodoOh());

						alocacaoCell.setAlocacaoPeriodo(ap);
						allAlocationIsZero = false;
					}
				}
			}

			if (!allAlocationIsZero) {
				cp = new ContratoPratica();
				cp.setNomeContratoPratica(alocacaoOverhead.getTipoOverhead()
						.getNomeTipoOverhead());

				row.setContratoPratica(cp);
				row.setRecurso(p.getRecurso());
				row.setAlocacaoCellList(alocacaoCellList);
				ret.add(row);
			}
		}

		return ret;
	}

	/**
	 * Gera os valores do dashboard do MapaAlocacao.
	 * 
	 * @param contratoPratica
	 *            - {@link ContratoPratica} base
	 * @param dateList
	 *            - lista de meses do MapaAlocacao
	 * 
	 * @return - retorna uma lista de {@link VwPmsFinancials}
	 */
	@Override
	public SortedMap<Date, MapDashboardRow> genereteMapaAlocacaoDashboard(
			final ContratoPratica contratoPratica, final List<Date> dateList) {

		// map que será o Dashboard
		// neste map a chave é a data e o objeto tem o numero de FTEs, a média
		// de UR
		// e um map que é são todas as moedas utilizadas (a chave deste map é o
		// nome da moeda
		// e o seu objeto contem o valor da receita-moeda e o objeto moeda)
		SortedMap<Date, MapDashboardRow> mapDashboardRowMap = new TreeMap<Date, MapDashboardRow>();

		for (Date date : dateList) {
			List<VwPmsFinancials> financialsList = this.vwPmsFinancialsService
					.findVwPmsFinancialsByClobAndDataMes(contratoPratica, date);

			if (!financialsList.isEmpty() && financialsList.get(0) != null) {
				mapDashboardRowMap
						.put(date,
								this.buildMapDashboardRowFromFinancials(financialsList));
			}
		}

		return mapDashboardRowMap;
	}

	/**
	 * Obtem um {@link MapDashboardRow} a partir de um {@link VwPmsFinancialsId}
	 * .
	 * 
	 * @param financials
	 *            - the financials
	 * 
	 * @return MapDashboardRow
	 */
	private MapDashboardRow buildMapDashboardRowFromFinancials(
			final List<VwPmsFinancials> financials) {

		if (financials.isEmpty() && financials.get(0) != null) {
			return null;
		}

		VwPmsFinancialsId firstItem = financials.get(0).getId();

		MapDashboardRow dashboardRow = new MapDashboardRow();
		dashboardRow.setDataMes(firstItem.getMes());

		// compoe os valores de FTE e UR
		ContratoPratica contratoPratica = contratoPraticaService
				.findContratoPraticaById(firstItem.getCodigoContratoPratica());

		// codigo referente ao tipo de calculo a ser adotado para obter o valor
		// da Revenue.
		int revenueCalculationRule = receitaService.getRevenueCalculationRule(
				firstItem.getMes(), contratoPratica, firstItem.getMoeda());

		if (revenueCalculationRule == Constants.REVENUE_CALCULATION_RULE_RECEITED) {
			dashboardRow.setAverageUtilizationRate(firstItem
					.getUrMedioReceitado().doubleValue());
			dashboardRow.setFteTotal(firstItem.getTotalFteReceitado()
					.doubleValue());
		} else {
			dashboardRow.setAverageUtilizationRate(firstItem
					.getAvgUrProjetado().doubleValue());
			dashboardRow.setFteTotal(firstItem.getTotalFteProjetado()
					.doubleValue());
		}

		// monta as colunas dinamicas referentes a receita moeda
		dashboardRow.setMapDashboardMoedaMap(this.buildMapDashboardMoedaMap(
				financials, revenueCalculationRule));

		return dashboardRow;
	}

	/**
	 * Monta um {@link SortedMap} que é composto por todas as moedas projetadas
	 * num mes (a chave deste map é o nome da moeda e o seu objeto contem o
	 * valor da receita-moeda e o objeto moeda).
	 * 
	 * @param financials
	 *            - lista de {@link VwPmsFinancials}
	 * 
	 * @param revenueCalculationRule
	 *            - indicador de regra de calculo da revenue para saber qual
	 *            valor usar (Projetado do Mapa, Receitado, ou Nenhum/Zero)
	 * 
	 * @return um {@link SortedMap}
	 */
	private SortedMap<String, MapDashboardMoeda> buildMapDashboardMoedaMap(
			final List<VwPmsFinancials> financials,
			final int revenueCalculationRule) {

		SortedMap<String, MapDashboardMoeda> result = new TreeMap<String, MapDashboardMoeda>();

		MapDashboardMoeda mapDashboardMoeda = null;
		for (VwPmsFinancials financial : financials) {
			VwPmsFinancialsId f = financial.getId();

			mapDashboardMoeda = new MapDashboardMoeda();
			mapDashboardMoeda.setMoeda(f.getMoeda());

			if (revenueCalculationRule == Constants.REVENUE_CALCULATION_RULE_RECEITED) {
				mapDashboardMoeda.setRevenueTotal(f.getTotalReceitaReceitado()
						.doubleValue());
			} else if (revenueCalculationRule == Constants.REVENUE_CALCULATION_RULE_PROJECTED) {
				mapDashboardMoeda.setRevenueTotal(f.getTotalReceitaProjetado()
						.doubleValue());
			} else {
				mapDashboardMoeda.setRevenueTotal(0D);
			}

			result.put(f.getMoeda().getNomeMoeda(), mapDashboardMoeda);
		}

		return result;
	}

	/**
	 * Gera o codigo hash de uma alocacao periodo.
	 * 
	 * @param ap
	 *            - entidade do tipo AlocacaoPeriodo
	 * 
	 * @return retorna um inteiro que representa o código hash
	 */
	private int generateAlocationHash(final AlocacaoPeriodo ap) {
		int key = 17;

		key = 37
				* key
				+ (ap.getAlocacao().getRecurso() == null ? 0 : ap.getAlocacao()
						.getRecurso().getCodigoRecurso().hashCode());
		key = 37 * key
				+ (ap == null ? 0 : ap.getCodigoAlocacaoPeriodo().hashCode());
		key = 37 * key
				+ (ap == null ? 0 : ap.getDataAlocacaoPeriodo().hashCode());

		return key;
	}

	/**
	 * Prepara a matriz de Alocacao X AlocacaoPeriodo para edição do
	 * MapaAlocaao.
	 * 
	 * @param alocacaoList
	 *            - lista de Alocacao do MapaAlocacao corrente
	 * @param validityDateList
	 *            - lista de datas do periodo de vigencia ordenado
	 * @param mapaAlocacao
	 *            - entidade do tipo Mapaalocacao
	 * 
	 * @return lista de AlocacaoRow - matriz de Alocacao X AlocacaoPeriodo
	 */
	public List<AlocacaoRow> prepareUpdateMatrixAlocacao(
			final List<Alocacao> alocacaoList,
			final List<Date> validityDateList, final MapaAlocacao mapaAlocacao) {
		// lista final de AlocacaoRow que será retornada
		List<AlocacaoRow> alocacaoRowList = new ArrayList<AlocacaoRow>();

		int rowCont = 0;

		Date startDate = validityDateList.get(0);
		Date endDate = validityDateList.get(validityDateList.size() - 1);

		List<VwAlocContratoRecursoMes> alocContRecList = alocContRecDao
				.findByMapaAndPeriod(mapaAlocacao, startDate, endDate);

		Map<Integer, VwAlocContratoRecursoMes> alocAux = new Hashtable<Integer, VwAlocContratoRecursoMes>(
				0);
		for (VwAlocContratoRecursoMes aloc : alocContRecList) {
			alocAux.put(aloc.getId().hashCode(), aloc);
		}

		// dado uma lista de Alocacao, vinda do banco, cria a matriz de
		// AlocacaoRow x AlocacaoCell
		for (Alocacao alocacao : alocacaoList) {

			// cria uma AlocacaoRow (Alocacao) para ser inserida na matriz
			AlocacaoRow row = new AlocacaoRow(alocacao, rowCont++);

			etiquetaService.generateEtiquetaNamesForAlocacaoRow(row);

			checkResourceType(row);

			// cria uma lista de AlocacaoCell (AlocacaoPeriodo) em branco
			List<AlocacaoCell> alocacaoCellList = createMatrixLineAlocacao(
					alocacao, validityDateList, BigDecimal.valueOf(0));

			// pega a lista de AlocacaoPeriodo da Alocacao corrente
			List<AlocacaoPeriodo> alocacaoPeriodoList = alocacaoPeriodoService
					.findAlocPerByAlocacaoAndPeriod(alocacao, startDate,
							endDate);

			orderAlocacaoPeriodoList(alocacaoPeriodoList);

			// se for uma pessoa
			Recurso recurso = alocacao.getRecurso();
			Pessoa pessoa = pessoaService.findPessoaByRecurso(recurso);

			Map<Long, AlocacaoPeriodo> apAuxMap = new Hashtable<Long, AlocacaoPeriodo>();
			for (AlocacaoPeriodo ap : alocacaoPeriodoList) {
				apAuxMap.put(ap.getDataAlocacaoPeriodo().getTime(), ap);
			}

			// itera a lista de AlocacaoPeriodo para popular a lista de
			// AlocacaoCell criada em branco
			for (AlocacaoCell cell : alocacaoCellList) {

				if (pessoa != null) {
					// Busca data de recisao da pessoa
					Date dataRecisao = pessoa.getDataRescisao();

					// Seta flag de indicador ativo para pintar celula de
					// vermelho
					if (dataRecisao != null) {
						String indicadorAtivo = cell.getAlocacaoPeriodo()
								.getAlocacao().getRecurso().getIndicadorAtivo();
						Date dataAlocacao = cell.getAlocacaoPeriodo()
								.getDataAlocacaoPeriodo();
						// Pinta celula de vermelho
						if (indicadorAtivo.equals("I")
								&& dataAlocacao.after(dataRecisao)) {
							cell.setFlagIndicadorAtivo(Boolean.TRUE);
						}
					}
				}

				long cellKey = cell.getAlocacaoPeriodo()
						.getDataAlocacaoPeriodo().getTime();
				if (apAuxMap.containsKey(cellKey)) {
					AlocacaoPeriodo alocacaoPeriodo = apAuxMap.get(cellKey);
					cell.setAlocacaoPeriodo(alocacaoPeriodo);
					cell.setWasChanged(Boolean.FALSE);

					if (pessoa != null) {
						int key = generateAlocationHash(alocacaoPeriodo);

						if (alocAux.containsKey(key)) {
							cell.setFlagIndicadorAlocacao(alocAux.get(key)
									.getIndicadorSuperAlocacao());
						}
					}
				}

			}

			// pega o ultimo mes fechado da alocação
			Date closingDate = this.getClosingDateMapaAlocacao();
			if (pessoa != null && pessoa.getDataMesValidado() != null) {
				// se o mes validado é posterior ao mes fechado
				// seta o closing date com o mes validado
				if (DateUtil.after(pessoa.getDataMesValidado(), closingDate)) {
					closingDate = pessoa.getDataMesValidado();
				}
			}
			// verifica se para essa Alocacao corrente, existe percentual de
			// alocação no passado, se sim, essa Alocacao (dados como recurso,
			// base, perfil, estagio) não pode ser alterada/removida
			if (alocacaoPeriodoList != null && alocacaoPeriodoList.size() > 0) {
				// pega o primeiro AlocacaoPeriodo da Alocacao corrente
				AlocacaoPeriodo alocacaoPeriodoFirst = alocacaoPeriodoList
						.get(0);

				if (alocacaoPeriodoFirst != null
						&& alocacaoPeriodoFirst.getDataAlocacaoPeriodo() != null) {

					Date dateCurrent = DateUtils.truncate(closingDate,
							Calendar.MONTH);

					// se a data for menor do que a data fechamento, ou seja, se
					// existir AlocacaoPeriodo no passado, desabilita os botões
					// edit/delete da tela
					if ((alocacaoPeriodoFirst.getDataAlocacaoPeriodo()
							.compareTo(dateCurrent) <= 0)
							&& (alocacaoPeriodoFirst
									.getPercentualAlocacaoPeriodo()
									.doubleValue() > BigDecimal.valueOf(0)
									.doubleValue())) {
						row.setIsEnabled(Boolean.FALSE);
					}
				}
			}

			// atribui essa lista de AlocacaoCell à AlocacaoRow corrente
			row.setAlocacaoCellList(alocacaoCellList);
			// adiciona a AlocacaoRow corrente à lista final
			alocacaoRowList.add(row);
		}

		return alocacaoRowList;
	}

	/**
	 * Realiza o update da lista de entidades Alocacao X AlocacaoPeriodo com os
	 * dados da matriz para persistir no banco.
	 * 
	 * @param alocacaoRowList
	 *            - matriz de Alocacao X AlocacaoPeriodo
	 * @return lista de Alocacao X AlocacaoPeriodo
	 */
	public Boolean updateMatrixAlocacao(final List<AlocacaoRow> alocacaoRowList) {
		// marcador se a matriz está ok, sem erro ou incoerência alguma
		Boolean matrixOk = Boolean.TRUE;

		// verifica se o MapaAlocacao está sendo criado/alterado corretamente
		// com pelo menos uma Alocacao. Se não retorna null
		if (!isValidAllocationList(alocacaoRowList)) {
			return Boolean.FALSE;
		}

		// itera a lista de AlocacaoRow
		for (AlocacaoRow row : alocacaoRowList) {
			Alocacao alocacao = row.getAlocacao();

			// verifica se a linha está marcada para ser removida, se não
			// estiver marcada, faz as validações necessárias
			if (!row.getIsRemove()) {
				// verifica se a Alocacao corrente é válida, se todos os dados
				// (dados como recurso, base, perfil, estagio, utilization rate)
				// foram preenchidos corretamente, se não marca que a matriz não
				// está ok e vai para a próxima Alocacao e não precisa executar
				// o código abaixo
				if (!isValidAllocation(alocacao)) {
					matrixOk = Boolean.FALSE;
					continue;
				}

				// verifica se o percentual é válido para o tipo do Recurso
				// se não marca que a matriz não está ok e continua
				if (!isValidResourceTypePercentage(alocacao.getRecurso()
						.getCodigoMnemonico(), alocacao.getRecurso()
						.getIndicadorTipoRecurso(), row.getAlocacaoCellList())) {
					matrixOk = Boolean.FALSE;
					continue;
				}
			}
		}

		// se a matriz estiver ok retorna a lista final, se não retorna null
		if (matrixOk) {
			// itera a lista de AlocacaoRow
			for (AlocacaoRow row : alocacaoRowList) {
				Alocacao alocacao = row.getAlocacao();
				List<AlocacaoPeriodo> alocacaoPeriodoList = new ArrayList<AlocacaoPeriodo>();

				if (row.getIsRemove()) {

					// se sim e se Alocacao tiver código (nesse caso somente na
					// alteração do MapaAlocacao)
					if (alocacao.getCodigoAlocacao() != null) {
						// itera a lista de AlocacaoCell (AlocacaoPeriodo) da
						// Alocacao corrente e exclui cada entidade
						// (AlocacaoPeriodo), uma a uma
						for (AlocacaoCell cell : row.getAlocacaoCellList()) {
							AlocacaoPeriodo alocacaoPeriodo = cell
									.getAlocacaoPeriodo();
							// se AlocacaoPeriodo tiver código (nesse caso
							// somente
							// na alteração do MapaAlocacao)
							if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() != null) {
								// remove AlocacaoPeriodo
								AlocacaoPeriodo ap = alocacaoPeriodoService
										.findAlocacaoPeriodoById(alocacaoPeriodo
												.getCodigoAlocacaoPeriodo());

								if (ap != null) {
									alocacaoPeriodoService
											.removeAlocacaoPeriodo(ap);
								}
							}
						}
					}

				} else {
					// isso é necessário para iniciar uma nova sessão
					alocacao.setMapaAlocacao(mapaAlocacaoDao.findById(alocacao
							.getMapaAlocacao().getCodigoMapaAlocacao()));

					alocacao.setPerfilVendido(perfilVendidoService
							.findPerfilVendidoById(alocacao.getPerfilVendido()
									.getCodigoPerfilVendido()));

					alocacao.setRecurso(recursoService.findRecursoById(alocacao
							.getRecurso().getCodigoRecurso()));

					alocacao.setCidadeBase(cidadeBaseService
							.findCidadeBaseById(alocacao.getCidadeBase()
									.getCodigoCidadeBase()));

					if (alocacao.getCodigoAlocacao() != null) {
						alocacaoService.updateAlocacao(alocacao);
					} else {
						alocacaoService.createAlocacao(alocacao);
					}

					// se tudo está correto até este momento,
					// itera a lista de AlocacaoPeriodo
					for (AlocacaoCell cell : row.getAlocacaoCellList()) {

						if (cell.getWasChanged()) {

							AlocacaoPeriodo alocacaoPeriodo = cell
									.getAlocacaoPeriodo();
							// verifica se o percentual foi preenchido
							// corretamente e se
							// é maior do que 0 (zero), pois se for 0, não será
							// persistido
							BigDecimal percentualAlocacaoPeriodo = alocacaoPeriodo
									.getPercentualAlocacaoPeriodo();

							if (percentualAlocacaoPeriodo != null
									&& percentualAlocacaoPeriodo.doubleValue() > 0) {
								// adiciona a AlocacaoPeriodo na lista
								alocacaoPeriodoList.add(alocacaoPeriodo);
								if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() == null) {
									alocacaoPeriodoService
											.createAlocacaoPeriodo(alocacaoPeriodo);
								} else {
									alocacaoPeriodoService
											.updateAlocacaoPeriodo(alocacaoPeriodo);
								}
							} else {
								// se o percentual for igual a 0 (zero),
								// verifica se tem
								// código da AlocacaoPeriodo. Se sim, remove do
								// banco,
								// se não, não tem a necessidade pois ele não
								// existe
								if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() != null) {
									AlocacaoPeriodo ap = alocacaoPeriodoService
											.findAlocacaoPeriodoById(alocacaoPeriodo
													.getCodigoAlocacaoPeriodo());

									if (ap != null) {
										alocacaoPeriodoService
												.removeAlocacaoPeriodo(ap);
									}
								}
							}

							cell.setWasChanged(false);
						}
					}
				}

				if (alocacao.getCodigoAlocacao() != null) {
					Alocacao aloc = alocacaoService.findAlocacaoById(alocacao
							.getCodigoAlocacao());
					// se não existir mais nenhuma alocação periodo remove a
					// alocação
					if (alocacaoPeriodoService.findAlocacaoPeriodoByAlocacao(
							aloc).isEmpty()) {
						// remove Alocacao
						alocacaoService.removeAlocacao(aloc);
					}
				}
			}

			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Adiciona uma Alocacao na matriz para ser criada posteriormente no banco.
	 * 
	 * @param alocacao
	 *            - Alocacao a ser adicionada na matriz
	 * @param alocacaoRowList
	 *            - lista de Alocacao X AlocacaoPeriodo
	 * @param validityDateList
	 *            - lista de datas do periodo de vigencia ordenado
	 * @param percentualAlocacao
	 *            - percentual de alocacao do recurso para a vigencia
	 * @return true se Alocacao for válida e se foi adicionada corretamente na
	 *         matriz, caso contrário retorna false
	 */
	public Boolean addAlocacaoMatrix(final Alocacao alocacao,
			final List<AlocacaoRow> alocacaoRowList,
			final List<Date> validityDateList,
			final BigDecimal percentualAlocacao) {

		Recurso recurso = alocacao.getRecurso();

		// se o percentual for válido para o tipo do Recurso
		if (isValidResourceTypePercentage(recurso.getCodigoMnemonico(),
				recurso.getIndicadorTipoRecurso(), percentualAlocacao)) {

			// cria uma AlocacaoRow (Alocacao)
			AlocacaoRow row = new AlocacaoRow(alocacao,
					alocacaoRowList.size() + 1);

			checkResourceType(row);

			// cria uma lista de AlocacaoCell (AlocacaoPeriodo) com o percentual
			// passado por parametro e atribui para a nova AlocacaoRow
			List<AlocacaoCell> cellList = createMatrixLineAlocacao(alocacao,
					validityDateList, percentualAlocacao);

			// persiste a alocação no banco de dados
			alocacaoService.createAlocacao(alocacao);
			// persiste todas as alocações periodos
			for (AlocacaoCell alocacaoCell : cellList) {
				// persiste a alocação periodo no banco de dados
				alocacaoPeriodoService.createAlocacaoPeriodo(alocacaoCell
						.getAlocacaoPeriodo());
			}

			row.setAlocacaoCellList(cellList);

			// adiciona na lista de AlocacaoRow da matriz
			alocacaoRowList.add(row);

			// retorna true pois a Alocacao foi adicionada na matriz
			// corretamente
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Edita uma Alocacao na matriz para ser atualizada posteriormente no banco.
	 * 
	 * @param alocacaoNew
	 *            - Alocacao a ser editada na matriz
	 * @param alocacaoRowList
	 *            - lista de Alocacao X AlocacaoPeriodo
	 */
	public void editAlocacaoMatrix(final Alocacao alocacaoNew,
			final List<AlocacaoRow> alocacaoRowList) {
		// itera a lista de AlocacaoRow (matriz)
		for (AlocacaoRow row : alocacaoRowList) {
			Alocacao alocacao = row.getAlocacao();

			checkResourceType(row);

			// verifica qual Alocacao correta para ser substituída pela Alocacao
			// nova que vem por parâmetro
			if (alocacaoNew.getCodigoAlocacao() != null) {
				if (alocacao.getCodigoAlocacao().compareTo(
						alocacaoNew.getCodigoAlocacao()) == 0) {
					row.setAlocacao(alocacaoNew);
					break;
				}
			} else if (alocacao.equals(alocacaoNew)) {
				row.setAlocacao(alocacaoNew);
				break;
			}
		}
	}

	/**
	 * Marca a Alocacao deletada da matriz para ser removida posteriormente do
	 * banco.
	 * 
	 * @param alocacaoOld
	 *            - Alocacao a ser excluida da matriz
	 * @param alocacaoRowList
	 *            - lista de Alocacao X AlocacaoPeriodo
	 */
	public void deleteAlocacaoMatrix(final Alocacao alocacaoOld,
			final List<AlocacaoRow> alocacaoRowList) {
		// itera a lista de AlocacaoRow (matriz)
		for (AlocacaoRow row : alocacaoRowList) {
			Alocacao alocacao = row.getAlocacao();

			// verifica qual Alocacao correta para ser comparada à que vem por
			// parâmetro para ser marcada par ser removida
			if (alocacaoOld.getCodigoAlocacao() != null) {
				if (alocacao.getCodigoAlocacao().compareTo(
						alocacaoOld.getCodigoAlocacao()) == 0) {
					row.setIsRemove(Boolean.TRUE);
					break;
				}
			} else if (alocacao.equals(alocacaoOld)) {
				row.setIsRemove(Boolean.TRUE);
				break;
			}
		}
	}

	/**
	 * Recupera os dados de um mapa (Alocação, AlocacçãoPeriodo, MapaAlocação) e
	 * retorna uma lista que possui a estrutura para exibir na tela.
	 * 
	 * @param mapaAlocacao
	 *            que se deseja carregar os dados
	 * @return retotna uma lista com a estrutura que sera exibida na tela.
	 */
	public List<AlocacaoRow> loadMapaAlocacaoManageView(
			final MapaAlocacao mapaAlocacao) {
		MapaAlocacao mapa = this.findMapaAlocacaoById(mapaAlocacao
				.getCodigoMapaAlocacao());

		// recupera a data inicial da vigência do MapaAlocacao
		Date minDate = fatorUrMesService.findFatorUrMesMinDateByMapa(mapa);
		// recupera a data final da vigência do MapaAlocacao
		Date maxDate = fatorUrMesService.findFatorUrMesMaxDateByMapa(mapa);

		// cria a lista de datas dado início e fim da vigência

		List<Date> dateList =

		DateUtil.getValidityDateList("" + DateUtil.getMonthNumber(minDate), ""
				+ DateUtil.getYear(minDate),
				"" + DateUtil.getMonthNumber(maxDate),
				"" + DateUtil.getYear(maxDate));

		List<Alocacao> alocacaoList = alocacaoService
				.findAlocacaoByMapaAlocacaoAndPeriod(mapa, minDate, maxDate);
		// cria a matriz para ser exibida na tela
		List<AlocacaoRow> alocacaoRowList = this.prepareUpdateMatrixAlocacao(
				alocacaoList, dateList, mapa);

		return alocacaoRowList;

	}

	/**
	 * Bloqueia o MapaAlocacao.
	 * 
	 * @param mapa
	 *            - MapaAlocacao para realizar o desbloqueio
	 * @return - true se possui permissao para realizar a operação, caso
	 *         contrario false
	 */
	public Boolean lockMapaAlocacao(final MapaAlocacao mapa) {
		MapaAlocacao mapaAlocacao = this.findMapaAlocacaoById(mapa
				.getCodigoMapaAlocacao());

		if (verifyMapaAlocacaoLockPermission(mapaAlocacao)) {
			mapaAlocacao.setLoginTrava(LoginUtil.getLoggedUsername());
			mapaAlocacao.setDataTrava(new Date());
			this.updateMapaAlocacao(mapaAlocacao);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Desbloqueia o MapaAlocacao.
	 * 
	 * @param mapa
	 *            - MapaAlocacao para realizar o desbloqueio
	 * @return - true se possui permissao para realizar a operação, caso
	 *         contrario false
	 */
	public Boolean unlockMapaAlocacao(final MapaAlocacao mapa) {
		MapaAlocacao mapaAlocacao = this.findMapaAlocacaoById(mapa
				.getCodigoMapaAlocacao());

		if (verifyMapaAlocacaoLockPermission(mapaAlocacao)) {
			mapaAlocacao.setLoginTrava(null);
			mapaAlocacao.setDataTrava(new Date());
			this.updateMapaAlocacao(mapaAlocacao);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * verifica se o usuario logado no sistema possui permissão para lock/unlock
	 * no mapa.
	 * 
	 * @param mapa
	 *            - MapaAlocacao a ser verificado.
	 * @return - true se possui permissao, caso contrario false.
	 */
	private Boolean verifyMapaAlocacaoLockPermission(final MapaAlocacao mapa) {

		if (mapa.getLoginTrava() == null) {
			return true;
		}

		String username = LoginUtil.getLoggedUsername();

		return username.equals(mapa.getLoginTrava());
	}

	/**
	 * Cria a matriz vazia (valores zerados) para ser utilizada pelos metodos de
	 * criacao e atualizacao do MapaAlocacao.
	 * 
	 * @param alocacao
	 *            - Alocacao corrente
	 * @param validityDateList
	 *            - lista de datas do periodo de vigencia ordenado
	 * @param percentualAlocacao
	 *            - percentual de alocacao do recurso que está sendo adicionado
	 *            no MapaAlocacao
	 * @return lista de celulas da matriz (AlocacaoPeriodo)
	 */
	private List<AlocacaoCell> createMatrixLineAlocacao(
			final Alocacao alocacao, final List<Date> validityDateList,
			final BigDecimal percentualAlocacao) {
		int cellCont = 0;
		Pessoa pessoa = null;

		// pega a data de fechamento do modulo do mapa de alocacao
		Date closingDate = this.getClosingDateMapaAlocacao();

		Date dateCurrent = DateUtils.truncate(closingDate, Calendar.MONTH);
		List<AlocacaoCell> alocacaoCellList = new ArrayList<AlocacaoCell>();

		// dada a lista de datas do período da vigência
		for (Date period : validityDateList) {
			AlocacaoPeriodo alocacaoPeriodo = new AlocacaoPeriodo();
			alocacaoPeriodo.setDataAlocacaoPeriodo(period);
			alocacaoPeriodo.setPercentualAlocacaoPeriodo(percentualAlocacao);
			alocacaoPeriodo.setAlocacao(alocacao);
			// para cada data, cria uma AlocacaoCell (AlocacaoPeriodo)
			AlocacaoCell cell = new AlocacaoCell(alocacaoPeriodo, cellCont++);
			// se a data for passado, ou seja, se for menor do que a data
			// de fechamento
			if (alocacao.getRecurso() != null) {
				pessoa = pessoaService.findPessoaByRecurso(alocacao
						.getRecurso());
			}

			if (!period.after(dateCurrent)
					|| (pessoa != null && pessoa.getDataMesValidado() != null && !DateUtil
							.after(period, pessoa.getDataMesValidado(),
									Calendar.MONTH))) {

				alocacaoPeriodo.setPercentualAlocacaoPeriodo(BigDecimal
						.valueOf(0));

				// marca como não editável
				cell.setRendered(Boolean.valueOf(false));
			}

			// Verifica se a data de adimissao da pessoa é maior ou igual ao
			// periodo.
			if (pessoa != null
					&& (DateUtil.after(pessoa.getDataAdmissao(), period,
							Calendar.MONTH))) {
				alocacaoPeriodo.setPercentualAlocacaoPeriodo(BigDecimal
						.valueOf(0));
			}

			// adiciona a AlocacaoCell na lista final de retorno
			alocacaoCellList.add(cell);

		}

		return alocacaoCellList;
	}

	/**
	 * Verifica se a lista de AlocacaoRow do MapaAlocacao é válida, se pelo
	 * menos uma Alocacao foi preenchida.
	 * 
	 * @param alocacaoRowList
	 *            - lista de AlocacaoRow do MapaAlocacao corrente
	 * @return true se a lista do MapaAlocacao é valida, caso contrario retorna
	 *         false
	 */
	private Boolean isValidAllocationList(
			final List<AlocacaoRow> alocacaoRowList) {
		// contador de AlocacaoRow válidas
		int validAlocacaoRowCont = 0;
		for (AlocacaoRow alocacaoRow : alocacaoRowList) {
			// se pelo menos uma AlocacaoRow não estiver marcada pra ser
			// removida, conta 1 AlocacaoRow válida
			if (!alocacaoRow.getIsRemove()) {
				validAlocacaoRowCont++;
				break;
			}
		}

		// se não existir AlocacaoRow válida, dá mensagem de erro, pois o
		// MapaAlocacao deve ter pelo menos uma Alocacao
		if (validAlocacaoRowCont <= 0) {
			Messages.showError("isValidAllocationList",
					Constants.MSG_ERROR_INVAL_ALLOCATION_LIST);
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * Verifica se a Alocacao é válida, se todos os dados foram preenchidos.
	 * 
	 * @param alocacao
	 *            - alocacao corrente
	 * @return true se Alocacao é valida, caso contrario retorna false
	 */
	private Boolean isValidAllocation(final Alocacao alocacao) {
		Recurso recurso = alocacao.getRecurso();
		CidadeBase cidadeBase = alocacao.getCidadeBase();
		PerfilVendido perfilVendido = alocacao.getPerfilVendido();
		String indicadorEstagio = alocacao.getIndicadorEstagio();
		BigDecimal valorUr = alocacao.getValorUr();

		// se todos os dados foram preenchidos a Alocacao é válida, se não, dá
		// mensagem de erro
		if (recurso == null || cidadeBase == null || perfilVendido == null
				|| (indicadorEstagio == null || ("").equals(indicadorEstagio))
				|| valorUr == null) {
			Messages.showError("isValidAllocation",
					Constants.MSG_ERROR_INVAL_ALLOCATION, alocacao
							.getPerfilVendido().getNomePerfilVendido());
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * Verifica o tipo do recurso, se Pessoa, o percentual de alocacao deve ser
	 * de 0 a 1 com intervalos de 0,01.
	 * 
	 * ----- NAO SERA VALIDADO ----- Se PapelAlocacao, o percentual de alocacao
	 * deve ser de 0 a n com intervalos de 1. ----- NAO SERA VALIDADO -----
	 * 
	 * @param codigoMnemonico
	 *            - codigo mnemonico da alocacao corrente
	 * @param indicadorTipoRecurso
	 *            - PE para Pessoa e PA para PapelAlocacao
	 * @param alocacaoCellList
	 *            - lista de AlocacaoCell (AlocacaoPeriodo)
	 * @return true se percentual alocacao é valido, caso contrario retorna
	 *         false
	 */
	private Boolean isValidResourceTypePercentage(final String codigoMnemonico,
			final String indicadorTipoRecurso,
			final List<AlocacaoCell> alocacaoCellList) {
		for (AlocacaoCell cell : alocacaoCellList) {
			// se nao for editavel, nao valida o valor da celula
			if (cell.getRendered()) {
				AlocacaoPeriodo alocacaoPeriodo = cell.getAlocacaoPeriodo();
				// verifica se o percentual foi preenchido corretamente e se é
				// maior do que 0 (zero), pois se for 0, não será persistido
				BigDecimal percentualAlocacao = alocacaoPeriodo
						.getPercentualAlocacaoPeriodo();
				if (percentualAlocacao != null
						&& percentualAlocacao.doubleValue() > 0) {
					if (!isValidResourceTypePercentage(codigoMnemonico,
							indicadorTipoRecurso, percentualAlocacao)) {
						return Boolean.FALSE;
					}
				}
			}
		}

		return Boolean.TRUE;
	}

	/**
	 * Verifica o tipo do recurso, se Pessoa, o percentual de alocacao deve ser
	 * de 0 a 1 com intervalos de 0,01.
	 * 
	 * ----- NAO SERA VALIDADO ----- Se PapelAlocacao, o percentual de alocacao
	 * deve ser de 0 a n com intervalos de 1. ----- NAO SERA VALIDADO -----
	 * 
	 * @param codigoMnemonico
	 *            - codigo mnemonico da alocacao corrente
	 * @param indicadorTipoRecurso
	 *            - PE para Pessoa e PA para PapelAlocacao
	 * @param percentualAlocacao
	 *            - valor da alocacao
	 * @return true se percentual alocacao é valido, caso contrario retorna
	 *         false
	 */
	private Boolean isValidResourceTypePercentage(final String codigoMnemonico,
			final String indicadorTipoRecurso,
			final BigDecimal percentualAlocacao) {
		double percAlocDoubleValue = percentualAlocacao.doubleValue();

		// se tipo de Recurso for Pessoa e o percentual for maior do que 1, dá
		// mensagem de erro
		if (indicadorTipoRecurso.equals(Constants.RESOURCE_TYPE_PE)) {
			if (percAlocDoubleValue > 1) {
				Messages.showError("isValidAllocationPercentage",
						Constants.MSG_ERROR_INVAL_PERCENT_TYPE_PE,
						codigoMnemonico);
				return Boolean.FALSE;
			}
		}

		// pega o step (quebra) do percentual do arquivo de configurações
		double step = Double
				.parseDouble(systemProperties
						.getProperty(Constants.DEFAULT_PROPERTY_ALLOCATION_PERIOD_STEP));
		BigDecimal stepBD = new BigDecimal(step, new MathContext(2));
		BigDecimal percAlocDoubleValueBD = new BigDecimal(percAlocDoubleValue,
				new MathContext(2));
		// se o percentual não for válido, ou seja, não for múltiplo de step, dá
		// mensagem de erro.
		if (percAlocDoubleValue > 0) {
			try {
				percAlocDoubleValueBD.divide(stepBD, 0,
						BigDecimal.ROUND_UNNECESSARY);
			} catch (ArithmeticException e) {
				Messages.showError("isValidAllocationPercentage",
						Constants.MSG_ERROR_INVAL_PERCENT_TYPE, codigoMnemonico);
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	/**
	 * Verifica se o MapaAlocacao que está sendo criado é válido. Se não existir
	 * um MapaAlocacao com o ContratoPratica e Versão passado por parâmetro, o
	 * MapaAlocacao é válido. Caso contrário é inválido.
	 * 
	 * @param codigoContratoPratica
	 *            - código do ContratoPratica
	 * @param indicadorVersao
	 *            - versão do MapaAlocacao
	 * @return true se o MapaAlocacao for válido, false se já existir um
	 *         MapaAlocacao cadastrado com o ContratoPratica e Versão passados
	 *         por parâmetro
	 */
	public Boolean isValidAllocationMapVersion(
			final Long codigoContratoPratica, final String indicadorVersao) {
		ContratoPratica contratoPratica = new ContratoPratica();
		contratoPratica.setCodigoContratoPratica(codigoContratoPratica);

		MapaAlocacao filter = new MapaAlocacao();
		filter.setContratoPratica(contratoPratica);
		filter.setIndicadorVersao(indicadorVersao);

		List<MapaAlocacao> mapaAlocacaoList = findMapaAlocacaoByFilter(filter);

		if (mapaAlocacaoList.size() <= 0) {
			return Boolean.TRUE;
		} else {
			Messages.showError("isValidAllocationMapVersion",
					Constants.MSG_ERROR_INVAL_ALLOCATION_MAP);
			return Boolean.FALSE;
		}
	}

	/**
	 * Realiaza uma cópia (salva uma cópia) do MapaAlocacao para a versão
	 * passada por parametro.
	 * 
	 * @param mapaAlocacao
	 *            mapaAlocacao para ser copiado e salvo.
	 * 
	 * @param typeVersion
	 *            para qual o mapa será copiado
	 * 
	 * @return retorna a cópia do MapaAlocacao salvo
	 */
	public MapaAlocacao saveAs(final MapaAlocacao mapaAlocacao,
			final String typeVersion) {
		// Busca o MapaAlocacao
		MapaAlocacao ma = mapaAlocacaoDao.findById(mapaAlocacao
				.getCodigoMapaAlocacao());

		// Realiza a busca do MapaAlocacao pelo contrato-pratica e versão
		MapaAlocacao mapaClone = new MapaAlocacao();
		mapaClone.setContratoPratica(ma.getContratoPratica());
		mapaClone.setIndicadorVersao(typeVersion);
		List<MapaAlocacao> mapaAlocacaoList = findMapaAlocacaoByFilter(mapaClone);

		// para casos de re-publish, guarda a lista de EstratificacaoUr do
		// antigo Mapa PB para ser replicada para no novo Mapa PB
		List<EstratificacaoUr> estratUrAuxList = new ArrayList<EstratificacaoUr>();

		// Se existir um MapaAlocacao realiza a remoção
		if (!mapaAlocacaoList.isEmpty()) {
			mapaClone = mapaAlocacaoList.get(0);
			estratUrAuxList = mapaClone.getEstratificacaoUrs();
			this.remove(mapaClone);
		}
		// realiza uma copia do MapaAlocacao
		mapaClone = ma.getClone();
		mapaClone.setCodigoMapaAlocacao(null);

		// realiza uma iteração nas alocações, para realizar cópias
		Iterator<Alocacao> itAlocacao = ma.getAlocacaos().iterator();
		List<Alocacao> alocacaoList = new ArrayList<Alocacao>();
		Alocacao alocacao, alocacaoClone;
		Map<Alocacao, List<EtiquetaAlocacao>> mapEtiquetaAlocacao = new HashMap<Alocacao, List<EtiquetaAlocacao>>();

		while (itAlocacao.hasNext()) {
			alocacao = itAlocacao.next();
			// realiza a cópia de uma alocação
			alocacaoClone = alocacao.getClone();
			alocacaoClone.setCodigoAlocacao(null);

			// realiza uma iteração nos AlocacaoPeriodo, para realizar cópias
			Iterator<AlocacaoPeriodo> itAlocacaoPeriodo = alocacao
					.getAlocacaoPeriodos().iterator();
			AlocacaoPeriodo alocacaoPeriodo, alocacaoPeriodoClone;
			Set<AlocacaoPeriodo> alocacaoPeriodoList = new HashSet<AlocacaoPeriodo>();
			while (itAlocacaoPeriodo.hasNext()) {
				alocacaoPeriodo = itAlocacaoPeriodo.next();
				// realiza uma cópia de um AlocacaoPeriodo
				alocacaoPeriodoClone = alocacaoPeriodo.getClone();
				// seta os novos atributos
				alocacaoPeriodoClone.setCodigoAlocacaoPeriodo(null);
				alocacaoPeriodoClone.setCustoRealizados(null);
				alocacaoPeriodoClone.setAlocacao(alocacaoClone);
				alocacaoPeriodoList.add(alocacaoPeriodoClone);
			}

			// seta os novos atributos da Alocacao
			alocacaoClone.setMapaAlocacao(mapaClone);
			alocacaoClone.setAlocacaoPeriodos(alocacaoPeriodoList);
			// atribui uma nova lista pq nesse momento as Alocacao não tem
			// código
			alocacaoClone
					.setEtiquetaAlocacaos(new ArrayList<EtiquetaAlocacao>());
			alocacaoList.add(alocacaoClone);

			// guarda a lista de EtiquetaAlocacao para cada Alocacao clone
			// criada
			List<EtiquetaAlocacao> etiquetaAlocacaoList = alocacao
					.getEtiquetaAlocacaos();
			mapEtiquetaAlocacao.put(alocacaoClone, etiquetaAlocacaoList);
		}

		// Seta os novos atributos do mapa
		mapaClone.setAlocacaos(alocacaoList);
		mapaClone.setIndicadorVersao(typeVersion);

		// Realiza o clone da lista de FatorUrMes.
		Iterator<FatorUrMes> itFatorUr = ma.getFatorUrMeses().iterator();
		List<FatorUrMes> fatorUrMeses = new ArrayList<FatorUrMes>();
		FatorUrMes fatorClone;

		while (itFatorUr.hasNext()) {
			fatorClone = itFatorUr.next().getClone();
			fatorClone.setCodigoFatorUrMes(null);
			fatorClone.setMapaAlocacao(mapaClone);
			fatorUrMeses.add(fatorClone);
		}
		// seta o clone da lista de FatorUrMes.
		mapaClone.setFatorUrMeses(fatorUrMeses);

		// clone do Follow - MapaAlocacaoPessoa
		List<MapaAlocacaoPessoa> mapaAlocPessCloneList = new ArrayList<MapaAlocacaoPessoa>();

		List<MapaAlocacaoPessoa> maPessList = ma.getMapaAlocacaoPessoas();
		for (MapaAlocacaoPessoa maPess : maPessList) {
			MapaAlocacaoPessoa maPessClone = maPess.getClone();
			maPessClone.setCodigoMapaAlocPessoa(null);
			maPessClone.setMapaAlocacao(mapaClone);
			maPessClone.setPessoa(maPess.getPessoa().getClone());
			mapaAlocPessCloneList.add(maPessClone);
		}
		mapaClone.setMapaAlocacaoPessoas(mapaAlocPessCloneList);

		// clone da EstratificacaoUr
		List<EstratificacaoUr> estratUrCloneList = new ArrayList<EstratificacaoUr>();
		for (EstratificacaoUr estratUr : estratUrAuxList) {
			EstratificacaoUr estratUrClone = estratUr.getClone();
			estratUrClone.setCodigoEstratificacaoUr(null);
			estratUrClone.setMapaAlocacao(mapaClone);

			List<ItemEstratificacaoUr> itemEstratUr = estratUr
					.getItemEstratificacaoUrs();
			List<ItemEstratificacaoUr> itemEstratUrCloneList = new ArrayList<ItemEstratificacaoUr>();
			for (ItemEstratificacaoUr ieUr : itemEstratUr) {
				ItemEstratificacaoUr ieUrclone = ieUr.getClone();
				ieUrclone.setCodigoItemEstratUr(null);
				ieUrclone.setEstratificacaoUr(estratUrClone);

				itemEstratUrCloneList.add(ieUrclone);
			}

			estratUrClone.setItemEstratificacaoUrs(itemEstratUrCloneList);
			estratUrCloneList.add(estratUrClone);
		}
		mapaClone.setEstratificacaoUrs(estratUrCloneList);

		// ========= Salva Tudo ==============================

		// salvo a nova cópia
		this.createMapaAlocacao(mapaClone);
		for (FatorUrMes fator : fatorUrMeses) {
			fatorUrMesService.createFatorUrMes(fator);
		}
		for (Alocacao a : alocacaoList) {
			alocacaoService.createAlocacao(a);
			for (AlocacaoPeriodo ap : a.getAlocacaoPeriodos()) {
				alocacaoPeriodoService.createAlocacaoPeriodo(ap);
			}
		}
		// ===================================================

		// faz novamente a iteração para gravar as EtiquetaAlocacao, pois na
		// primeira vez, as Alocacao ainda não tem código
		Iterator<Alocacao> itAlocacaoClone = mapaClone.getAlocacaos()
				.iterator();
		while (itAlocacaoClone.hasNext()) {
			alocacaoClone = itAlocacaoClone.next();

			// realiza uma iteração nas EtiquetaAlocacao, para realizar cópias
			Iterator<EtiquetaAlocacao> itEtiquetaAlocacao = mapEtiquetaAlocacao
					.get(alocacaoClone).iterator();
			EtiquetaAlocacao etiquetaAlocacao, etiquetaAlocacaoClone;
			List<EtiquetaAlocacao> etiquetaAlocacaoCloneList = new ArrayList<EtiquetaAlocacao>();
			while (itEtiquetaAlocacao.hasNext()) {
				etiquetaAlocacao = itEtiquetaAlocacao.next();
				// realiza uma cópia de uma EtiquetaAlocacao
				etiquetaAlocacaoClone = etiquetaAlocacao.getClone();
				// seta os novos atributos
				etiquetaAlocacaoClone.setAlocacao(alocacaoClone);
				etiquetaAlocacaoClone.setEtiqueta(etiquetaAlocacao
						.getEtiqueta().getClone());
				etiquetaAlocacaoClone
						.setId(new EtiquetaAlocacaoId(alocacaoClone
								.getCodigoAlocacao(), etiquetaAlocacaoClone
								.getEtiqueta().getCodigoEtiqueta()));
				etiquetaAlocacaoService
						.createEtiquetaAlocacao(etiquetaAlocacaoClone);
				etiquetaAlocacaoCloneList.add(etiquetaAlocacaoClone);
			}

			// atribui a nova lista de EtiquetaAlocacao à Alocacao clone
			// corrente
			alocacaoClone.setEtiquetaAlocacaos(etiquetaAlocacaoCloneList);
		}

		return mapaClone;
	}

	/**
	 * Realiza o update de um alocação periodo.
	 * 
	 * @param ap
	 *            - entidade do tipo AlocacaoPerido
	 */
	public synchronized void updateAlocacaoPeriodo(final AlocacaoPeriodo ap) {

		if (ap.getCodigoAlocacaoPeriodo() != null) {
			if (BigDecimal.ZERO.compareTo(ap.getPercentualAlocacaoPeriodo()) != 0) {
				alocacaoPeriodoService.updateAlocacaoPeriodo(ap);
			} else {
				alocacaoPeriodoService.removeAlocacaoPeriodo(ap);
			}
		} else {
			if (BigDecimal.ZERO.compareTo(ap.getPercentualAlocacaoPeriodo()) != 0) {
				alocacaoPeriodoService.createAlocacaoPeriodo(ap);
			}
		}
	}

	/**
	 * Realiza o update do fator UR mes.
	 * 
	 * @param fator
	 *            - entidade a ser atualizada.
	 */
	public void updateFatorUrMes(final FatorUrMes fator) {
		BigDecimal percentualFatorUr = fator.getPercentualFatorUr();

		FatorUrMes fatorUr = fatorUrMesService.findFatorUrMesById(fator
				.getCodigoFatorUrMes());
		fatorUr.setPercentualFatorUr(percentualFatorUr);

		fatorUrMesService.updateFatorUrMes(fatorUr);
	}

	/**
	 * Atualiza os valores do U.R. das Alocacao selecionadas.
	 * 
	 * @param alocacaoRowList
	 *            - matriz de de Alocacao do MapaAlocacao
	 * @param updateUr
	 *            - valor a ser atualizado do U.R. na lista de Alocacao
	 */
	public void updateIncomeUr(final List<AlocacaoRow> alocacaoRowList,
			final BigDecimal updateUr) {
		Alocacao alocacao;

		for (AlocacaoRow alocacaoRow : alocacaoRowList) {
			if (alocacaoRow.getIsSelected() && alocacaoRow.getIsView()) {
				alocacao = alocacaoRow.getAlocacao();
				alocacao.setValorUr(updateUr);

				if (alocacao.getCidadeBase() != null) {
					alocacaoService.updateAlocacao(alocacao);
				}
			}
		}
	}

	/**
	 * Atualiza os valores do FTE das AlocacaoPeriodo das Alocacao selecionadas.
	 * 
	 * @param alocacaoRowList
	 *            - matriz de Alocacao do MapaAlocacao
	 * @param updateFte
	 *            - valor a ser atualizado do FTE na lista de Alocacao
	 */
	public void updateMatrixIncomeFte(final List<AlocacaoRow> alocacaoRowList,
			final BigDecimal updateFte) {
		for (AlocacaoRow alocacaoRow : alocacaoRowList) {
			if (alocacaoRow.getIsSelected() && alocacaoRow.getIsView()) {
				List<AlocacaoCell> alocacaoCellList = alocacaoRow
						.getAlocacaoCellList();
				updateLineIncomeFte(alocacaoRow.getAlocacao(),
						alocacaoCellList, updateFte);
			}
		}
	}

	/**
	 * Atualiza os valores do FTE das AlocacaoPeriodo das Alocacao selecionadas.
	 * 
	 * @param alocacao
	 *            - Alocacao corrente
	 * @param alocacaoCellList
	 *            - lista de AlocacaoPeriodo da Alocacao corrente
	 * @param updateFte
	 *            - valor a ser atualizado do FTE na lista de Alocacao
	 */
	public void updateLineIncomeFte(final Alocacao alocacao,
			final List<AlocacaoCell> alocacaoCellList,
			final BigDecimal updateFte) {
		Date closingDateMapaAlocacao = DateUtils.truncate(
				getClosingDateMapaAlocacao(), Calendar.MONTH);

		Recurso recurso = alocacao.getRecurso();
		Pessoa pessoa = null;

		if (recurso != null) {
			pessoa = pessoaService.findPessoaByRecurso(recurso);
		}

		if (pessoa != null && pessoa.getDataMesValidado() != null) {
			Date dataMesValidado = pessoa.getDataMesValidado();

			for (AlocacaoCell alocacaoCell : alocacaoCellList) {

				AlocacaoPeriodo alocacaoPeriodo = alocacaoCell
						.getAlocacaoPeriodo();

				Date dataAlocacaoPeriodo = alocacaoPeriodo
						.getDataAlocacaoPeriodo();

				if (dataAlocacaoPeriodo.compareTo(closingDateMapaAlocacao) > 0) {

					if (dataAlocacaoPeriodo.compareTo(dataMesValidado) > 0) {
						alocacaoPeriodo.setPercentualAlocacaoPeriodo(updateFte);

						if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() != null) {
							alocacaoPeriodoService
									.updateAlocacaoPeriodo(alocacaoPeriodo);
						} else {
							if (alocacaoPeriodo.getPercentualAlocacaoPeriodo()
									.compareTo(BigDecimal.ZERO) != 0) {
								alocacaoPeriodoService
										.createAlocacaoPeriodo(alocacaoPeriodo);
							}
						}

						alocacaoCell.setWasChanged(true);
					}
				}
			}

		} else {
			for (AlocacaoCell alocacaoCell : alocacaoCellList) {

				AlocacaoPeriodo alocacaoPeriodo = alocacaoCell
						.getAlocacaoPeriodo();
				Date dataAlocacaoPeriodo = alocacaoPeriodo
						.getDataAlocacaoPeriodo();

				if (dataAlocacaoPeriodo.compareTo(closingDateMapaAlocacao) > 0) {
					alocacaoPeriodo.setPercentualAlocacaoPeriodo(updateFte);

					if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() != null) {
						alocacaoPeriodoService
								.updateAlocacaoPeriodo(alocacaoPeriodo);
					} else {
						if (alocacaoPeriodo.getPercentualAlocacaoPeriodo()
								.compareTo(BigDecimal.ZERO) != 0) {
							alocacaoPeriodoService
									.createAlocacaoPeriodo(alocacaoPeriodo);
						}
					}

					alocacaoCell.setWasChanged(true);
				}
			}
		}

	}

	/**
	 * Remove todas as alocações selecionadas.
	 * 
	 * @param rowList
	 *            - lista de alocações.
	 */
	public void removeAlocacao(final List<AlocacaoRow> rowList) {

		Iterator<AlocacaoRow> rowIterator = rowList.iterator();
		while (rowIterator.hasNext()) {
			AlocacaoRow row = rowIterator.next();

			if (row.getIsSelected() && row.getIsView()) {
				List<AlocacaoCell> cellList = row.getAlocacaoCellList();
				int removeCount = 0;

				// se for habilitada pra editar, irá excluir a linha
				// normalmente, se nao for habilitada, o excluir irá
				// funcionar somente para zerar os meses futuros
				Alocacao alocacao = row.getAlocacao();
				if (row.getIsEnabled()) {

					for (AlocacaoCell cell : cellList) {
						AlocacaoPeriodo alocacaoPeriodo = cell
								.getAlocacaoPeriodo();
						if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() != null) {
							alocacaoPeriodoService
									.removeAlocacaoPeriodo(alocacaoPeriodo);
						}
						removeCount++;
					}
				} else {
					Date closingDateMapaAlocacao = DateUtils.truncate(
							getClosingDateMapaAlocacao(), Calendar.MONTH);

					Recurso recurso = alocacao.getRecurso();
					Pessoa pessoa = null;

					if (recurso != null) {
						pessoa = pessoaService.findPessoaByRecurso(recurso);
					}

					if (pessoa != null && pessoa.getDataMesValidado() != null) {
						Date dataMesValidado = pessoa.getDataMesValidado();

						for (AlocacaoCell alocacaoCell : cellList) {

							AlocacaoPeriodo alocacaoPeriodo = alocacaoCell
									.getAlocacaoPeriodo();

							Date dataAlocacaoPeriodo = alocacaoPeriodo
									.getDataAlocacaoPeriodo();

							if (dataAlocacaoPeriodo
									.compareTo(closingDateMapaAlocacao) > 0) {

								if (dataAlocacaoPeriodo
										.compareTo(dataMesValidado) > 0) {
									if (alocacaoPeriodo
											.getCodigoAlocacaoPeriodo() != null) {
										alocacaoPeriodoService
												.removeAlocacaoPeriodo(alocacaoPeriodo);

										alocacaoPeriodo
												.setCodigoAlocacaoPeriodo(null);

									}
									removeCount++;

									alocacaoPeriodo
											.setPercentualAlocacaoPeriodo(BigDecimal.ZERO);
									alocacaoCell.setWasChanged(true);
								}
							}
						}

					} else {

						for (AlocacaoCell alocacaoCell : cellList) {
							AlocacaoPeriodo alocacaoPeriodo = alocacaoCell
									.getAlocacaoPeriodo();
							Date dataAlocacaoPeriodo = alocacaoPeriodo
									.getDataAlocacaoPeriodo();

							if (dataAlocacaoPeriodo
									.compareTo(closingDateMapaAlocacao) > 0) {
								if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() != null) {
									alocacaoPeriodoService
											.removeAlocacaoPeriodo(alocacaoPeriodo);

									alocacaoPeriodo
											.setCodigoAlocacaoPeriodo(null);

								}
								removeCount++;

								alocacaoPeriodo
										.setPercentualAlocacaoPeriodo(BigDecimal.ZERO);
								alocacaoCell.setWasChanged(true);
							}
						}
					}
				}

				if (alocacao.getCodigoAlocacao() != null) {
					Alocacao aloc = alocacaoService.findAlocacaoById(alocacao
							.getCodigoAlocacao());
					// se não existir mais nenhuma alocação periodo remove a
					// alocação
					if (alocacaoPeriodoService.findAlocacaoPeriodoByAlocacao(
							aloc).isEmpty()) {
						// remove Alocacao do banco
						alocacaoService.removeAlocacao(aloc);
					}
				}
				// se todas as celulas foram excluidas então
				// remove a alocacao da lista
				if (removeCount == cellList.size()) {
					rowIterator.remove();
				}
			}
		}

	}

	/**
	 * Move a janela do mapa de alocação.
	 * 
	 * @param mapaAlocacao
	 *            - entidade MapaAlocacao
	 * @param numberMonthsToMove
	 *            - Número de meses que a janela vai mover
	 */
	public void moveMapaAlocacaoWindow(final MapaAlocacao mapaAlocacao,
			final Integer numberMonthsToMove) {

		// busca novamente o MapaAlocacao para abrir sessão
		// de conexão com o banco (hibernate)
		MapaAlocacao mapa = findMapaAlocacaoById(mapaAlocacao
				.getCodigoMapaAlocacao());

		Date dataInicioJanela = mapa.getDataInicioJanela();

		mapa.setDataInicioJanela(DateUtils.addMonths(dataInicioJanela,
				numberMonthsToMove));

		// ajusta a janela
		this.ajustaDataInicioJanela(mapa);
		// salva o mapa de alocação
		this.updateMapaAlocacao(mapa);

	}

	/**
	 * Ordena a lista de AlocacaoPeriodo de cada Alocacao da matriz.
	 * 
	 * @param alocacaoPeriodoList
	 *            - linha de AlocacaoPeriodo da Alocacao corrente
	 */
	private void orderAlocacaoPeriodoList(
			final List<AlocacaoPeriodo> alocacaoPeriodoList) {
		Collections.sort(alocacaoPeriodoList,
				new Comparator<AlocacaoPeriodo>() {
					public int compare(final AlocacaoPeriodo ap1,
							final AlocacaoPeriodo ap2) {
						return ap1.getDataAlocacaoPeriodo().compareTo(
								ap2.getDataAlocacaoPeriodo());
					}
				});
	}

	/**
	 * Se Recurso for do tipo PapelAlocacao marca para ser exibido em itálico.
	 * 
	 * @param alocacaoRow
	 *            - linha Alocacao corrente
	 */
	private void checkResourceType(final AlocacaoRow alocacaoRow) {
		Alocacao alocacao = alocacaoRow.getAlocacao();
		Recurso recurso = alocacao.getRecurso();
		if (recurso != null) {
			if (recurso.getIndicadorTipoRecurso().equals(
					Constants.RESOURCE_TYPE_PA)) {
				alocacaoRow.setStyleClass(BundleUtil
						.getBundle(Constants.BUNDLE_KEY_LABEL_STYLE_ITALIC));
			}
		}
	}

	/**
	 * Exibir toda a matriz (atribuir true para o atributo isView da
	 * AlocacaoRow).
	 * 
	 * @param alocacaoRowList
	 *            - matriz de Alocacao X AlocacaoPeriodo
	 */
	public void showFullMatrixAlocacao(final List<AlocacaoRow> alocacaoRowList) {
		for (AlocacaoRow alocacaoRow : alocacaoRowList) {
			alocacaoRow.setIsView(Boolean.valueOf(true));
		}
	}

	/**
	 * Busca uma lista de entidades pelo login(username) que possui o 'lock' do
	 * mapa de alocação.
	 * 
	 * @param username
	 *            login do usuário desejado.
	 * 
	 * @return lista de MapaAlocacao.
	 */
	public List<MapaAlocacao> findByLoginTrava(final String username) {
		return mapaAlocacaoDao.findByLoginTrava(username);
	}

	/**
	 * Busca uma lista de entidades pelo por todos os mapas que estão com lock.
	 * 
	 * @return lista de MapaAlocacao.
	 */
	public List<MapaAlocacao> findMapaAlocacaoAllLock() {
		return mapaAlocacaoDao.findAllLock();
	}

	/**
	 * Pega a data de fechamento do MapaAlocacao.
	 * 
	 * @return retorna a Data de Fechamento.
	 */
	public Date getClosingDateMapaAlocacao() {
		// pega a data de fechamento do modulo do MapaAlocacao
		Modulo moduloMapaAlocacao = moduloService.findModuloById(new Long(
				systemProperties
						.getProperty(Constants.MODULO_ALOCATION_MAP_CODE)));

		return DateUtils.truncate(moduloMapaAlocacao.getDataFechamento(),
				Calendar.MONTH);
	}

	/**
	 * Busca uma lista de entidades pelo por todos os mapas que possuem a versão
	 * igual a 'Published'.
	 * 
	 * @return lista de MapaAlocacao.
	 */
	public List<MapaAlocacao> findMapaAlocaoAllPublished() {
		return mapaAlocacaoDao.findAllPublished();
	}

	/**
	 * Realiza a validação da vigencia.
	 * 
	 * @param validityMonthBeg
	 *            - mes inicial da vigencia
	 * @param validityYearBeg
	 *            - ano inicial da vigencia
	 * @param validityMonthEnd
	 *            - mes final da vigencia
	 * @param validityYearEnd
	 *            - ano final da vigencia
	 * @param mapa
	 *            - Mapa de alocação em questão.
	 * 
	 * @return retorna true se o período for válido, caso contrario false.
	 */
	public Boolean validateAlocationMapPeriod(final MapaAlocacao mapa,
			final String validityMonthBeg, final String validityYearBeg,
			final String validityMonthEnd, final String validityYearEnd) {
		// nova data inicio
		Date newDateBeg = DateUtil.getDate(validityMonthBeg, validityYearBeg);
		// nova data fim
		Date newDateEnd = DateUtil.getDate(validityMonthEnd, validityYearEnd);
		// data inicio atual
		Date beginDate = fatorUrMesService.findFatorUrMesMinDateByMapa(mapa);

		if (beginDate == null) {
			beginDate = newDateBeg;
		}
		// data de fechamento do mapa
		Date closingDate = this.getClosingDateMapaAlocacao();

		// verifica se data inicial é maior que data final,
		// se sim dá mensagem de erro.
		if (newDateBeg.after(newDateEnd)) {
			Messages.showError("validateAlocationMapPeriod",
					Constants.DEFAULT_MSG_ERROR_DATE_BEG_GT_DATE_END,
					Constants.DEFAULT_LABEL_VALIDITY_DATE_MAPA_ALOCACAO);
			return Boolean.FALSE;

			// Verificação da 'DATA INICIAL'
			// verifica se a data inicial pode ser alterada
			// se data inicial for é anterior a data de fechamento,
			// então não pode ser alterada.
		} else if (!DateUtil.after(beginDate, closingDate, Calendar.MONTH)
				&& !DateUtil.isSameDate(beginDate, newDateBeg, Calendar.MONTH)) {
			Messages.showError("validateAlocationMapPeriod",
					Constants.MAPA_ALOCACAO_MSG_ERROR_CHANGE_DATE_BEG,
					Constants.DEFAULT_LABEL_VALIDITY_DATE_MAPA_ALOCACAO);

			return Boolean.FALSE;

			// Verificação da 'DATA FINAL'
			// verifica se a nova data final é menor que data de fechamento
			// se verdadeiro seta mensagem de erro.
		} else if (DateUtil.before(newDateEnd, closingDate, Calendar.MONTH)) {
			Messages.showError("validateAlocationMapPeriod",
					Constants.DEFAULT_MSG_ERROR_NEW_DATE_END_LT_OLD_DATE_END,
					Constants.DEFAULT_LABEL_VALIDITY_DATE_MAPA_ALOCACAO);

			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * Busca uma lista de entidades pelo filtro. Também carrega asentidades
	 * relacionadas.
	 * 
	 * @param cli
	 *            entidade Cliente
	 * @param natureza
	 *            entidade NaturezaCentroLucro
	 * @param centroLucro
	 *            entidade CentroLucro
	 * @param dataMes
	 *            entidade do tipo Date, referente a data-mes
	 * @param pratica
	 *            do tipo Pratica.
	 * 
	 * @param statusReceita
	 *            status da receita
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<MapaAlocacao> findMapaAlocacaoByFilterWithoutRevenue(
			final Cliente cli, final NaturezaCentroLucro natureza,
			final CentroLucro centroLucro, final Pratica pratica,
			final Date dataMes, final String statusReceita) {

		return mapaAlocacaoDao.findByFilterWithoutRevenue(cli, natureza,
				centroLucro, pratica, dataMes, statusReceita);
	}

	/**
	 * Private retorna a data final da janela de exibição.
	 * 
	 * @param mapa
	 *            - Mapaalocacao
	 * 
	 * @return retorna a data final da janale de exibição.
	 */
	public Date getMapaAlocacaoEndDateWindow(final MapaAlocacao mapa) {
		// pega a data final que deve ser exibido no mapa
		Date dataFinal = DateUtils
				.addMonths(
						mapa.getDataInicioJanela(),
						Integer.valueOf(systemProperties
								.getProperty(Constants.MAPA_ALOCACAO_DEFAULT_WINDOW_SIZE)));

		if (mapa.getDataFim().before(dataFinal)) {
			dataFinal = mapa.getDataFim();
		}

		return dataFinal;
	}

	/**
	 * Seta o nome do mapa. Padrão: Map-[ContratoPratica].
	 * 
	 * @param mapa
	 *            que se deseja atualizar o titulo.
	 */
	private void setNewTxtTitulo(final MapaAlocacao mapa) {

		String cp = mapa.getContratoPratica().getNomeContratoPratica();
		String acronym = (String) systemProperties
				.get(Constants.MAPA_ALOCACAO_DEFAULT_ACRONYM_NAME);

		if (cp != null) {
			mapa.setTextoTitulo(acronym + cp);
		}
	}

	/**
	 * Busca uma lista de mapas que possuem a versão 'Published' no range de
	 * meses: previousDate até infinito.
	 * 
	 * @param startDate
	 *            - data de início do range do follow
	 * 
	 * @return lista de MapaAlocacao.
	 */
	public List<MapaAlocacao> findMapaAlocAllPBByRangeMonths(
			final Date startDate) {
		return mapaAlocacaoDao.findAllPBByRangeMonths(startDate);
	}

	/**
	 * Validação do ClosingDateMapaAlocacao. Caso startDate (data de início da
	 * vigência) seja maior do que a data Revenue, a vigência é válida e pode
	 * ser adicionada ou removida. Caso contrário, a vigência não pode ser
	 * adicionada nem removida.
	 * 
	 * @param startDate
	 *            - data de início da vigência
	 * @param showMsgError
	 *            - mostra mensagem de erro caso startDate não seja válido
	 * 
	 * @return true se startDate for maior do que ClosingDateMapaAlocacao. false
	 *         caso contrário
	 */
	public Boolean verifyClosingDateMapaAlocacao(final Date startDate,
			final Boolean showMsgError) {
		Date closingMapDate = moduloService.getClosingDateMapaAlocacao();
		if (startDate.compareTo(closingMapDate) > 0) {
			return Boolean.valueOf(true);
		} else {
			if (showMsgError) {
				Messages.showError("verifyClosingDateRevenue",
						Constants.MSG_ERROR_AJUSTE_RECEITA_ADD_CLOS_DATE,
						DateUtil.formatDate(closingMapDate,
								Constants.DEFAULT_DATE_PATTERN_SIMPLE,
								Constants.DEFAULT_CALENDAR_LOCALE));
			}

			return Boolean.valueOf(false);
		}
	}

	/**
	 * Obtem uma data (01/MM/YYYY) logó após o closinDate.
	 * 
	 * @return date
	 */
	public Date getDateAfterClosingDate() {
		Date closingDate = this.getClosingDateMapaAlocacao();
		return DateUtil.getNextMonth(closingDate);
	}

	@Override
	public Boolean isPessoaAllocatedOnMapaAlocacaoAtDate(Pessoa pessoa,
			MapaAlocacao mapaAlocacao, Date date) {
		List<AlocacaoPeriodo> alocacaoPeriodos = alocacaoPeriodoService
				.findByPessoaAndMapaAlocacao(pessoa, mapaAlocacao);

		date = DateUtils.truncate(date, Calendar.MONTH);

		for (AlocacaoPeriodo ap : alocacaoPeriodos) {
			if (!ap.getPercentualAlocacaoPeriodo().equals(BigDecimal.ZERO)
					&& ap.getDataAlocacaoPeriodo().equals(date)) {
				return true;
			}
		}

		return false;
	}
}