package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.business.service.*;
import com.ciandt.pms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.persistence.dao.IOrcDespesaClDao;
import com.ciandt.pms.util.LoginUtil;

/**
 * 
 * A classe OrcDespesaClService proporciona as funcionalidades de servico para a
 * entidade OrcDespesaCl.
 * 
 * @since 10/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public class OrcDespesaClService implements IOrcDespesaClService {

	/** Instancia de dao. */
	@Autowired
	private IOrcDespesaClDao dao;

	/** Instancia do servico de {@link OrcamentoDespesa}. */
	@Autowired
	private IOrcamentoDespesaService orcamentoDespesaService;

	/** serviço de FollowOrcamentoDespesa. */
	@Autowired
	private IFollowOrcamentoDespService followOrcDespService;

	/** Servico de OrcDespesaMes. */
	@Autowired
	private IOrcDespesaMesService orcDespesaMesService;

	/** Servico de VwPmsCotacaoMoeda. */
	@Autowired
	private IVwPmsCotacaoMoedaService vwPmsCotacaoMoedaService;

	/** Servico de vwPmsCustoEmbutidoPv. */
	@Autowired
	private IVwPmsCustoEmbutidoPvService vwPmsCustoEmbutidoPvService;

	/** Servico de {@link ContratoPratica}. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Servico de {@link Pessoa}. */
	@Autowired
	private IPessoaService pessoaService;

	@Autowired
	private IFollowOrcamentoDespService followOrcamentoDespService;

	@Autowired
	private IOrcDespWhiteListService whiteListService;

	@Autowired
	private IContratoPraticaOrcDespClService contratoPraticaOrcDespClService;

	@Autowired
	private IOrcDespDelegacaoService orcDespDelegacaoService;

	/**
	 * Checa se o nome do OrcDespesaCl já existe no banco de dados.
	 * 
	 * @param entity
	 *            {@link OrcDespesaCl}
	 * 
	 * @return boolean
	 * 
	 */
	private boolean nameAlreadyExiste(final OrcDespesaCl entity) {

		/*
		 * OrcDespesaCl orcamento = this.findOrcDespesaClByNameAndActive(entity
		 * .getOrcamentoDespesa().getNomeOrcDespesa());
		 */
		OrcDespesaCl orcamento = this.findByNameAndClientAndActive(entity);

		if (orcamento == null) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Override
	@Transactional
	public void createOrcDespesaCl(final OrcDespesaCl entity)
			throws IntegrityConstraintException {
		if (this.nameAlreadyExiste(entity)) {
			throw new IntegrityConstraintException(
					BundleUtil
							.getBundle(Constants.TRAVEL_BUDGET_ALREADY_EXISTS));
		}
		orcamentoDespesaService.createOrcamentoDespesa(entity
				.getOrcamentoDespesa());

		// Quebra o TB em meses e cria os OrcDespesaMes
		orcDespesaMesService.createOrcDespesaMesByOrcDespesa(entity
				.getOrcamentoDespesa());

		// Cria Follower por default
		FollowOrcamentoDesp follower = new FollowOrcamentoDesp();
		follower.setOrcamentoDespesa(entity.getOrcamentoDespesa());
		follower.setCodigoLogin(LoginUtil.getLoggedUsername());

		followOrcDespService.createFollowOrcamentoDesp(follower);

		dao.create(entity);

	}

	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 */
	@Override
	@Transactional
	public void updateOrcDespesaCl(final OrcDespesaCl entity) {
		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser deletada.
	 */
	@Override
	@Transactional
	public void removeOrcDespesaCl(final OrcDespesaCl entity) {
		orcamentoDespesaService.removeOrcamentoDespesa(entity
				.getOrcamentoDespesa());
		entity.setIndicadorDeleteLogico(Constants.SIM);
		this.updateOrcDespesaCl(entity);
	}

	/**
	 * Busca todas os registros de OrcDespesaCl.
	 * 
	 * @return listResult.
	 */
	@Override
	public List<OrcDespesaCl> findAll() {
		return dao.findAll();
	}

	/**
	 * Busca por cliente.
	 * 
	 * @param cliente
	 *            cliente
	 * @return listResult.
	 */
	@Override
	public List<OrcDespesaCl> findOrcDespesaClByCliente(final Cliente cliente) {
		return dao.findByCliente(cliente);
	}

	/**
	 * Busca entidade por id.
	 * 
	 * @param id
	 *            id.
	 * @return entidade.
	 */
	@Override
	public OrcDespesaCl findOrcDespesaClById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Busca por cliente.
	 * 
	 * @param cliente
	 *            cliente.
	 * @return lista.
	 */
	@Override
	public List<OrcDespesaCl> findOrcDespesaClByClientAndActive(
			final Cliente cliente) {
		return dao.findByClientAndActive(cliente);
	}

	/**
	 * Busca por cliente e name.
	 * 
	 * @param cliente
	 *            cliente.
	 * @param name
	 *            name.
	 * @return lista.
	 */
	@Override
	public List<OrcDespesaCl> findOrcDespesaClByClientAndName(
			final Cliente cliente, final String name) {
		return dao.findByClientAndName(cliente, name);
	}

	/**
	 * Busca um {@link OrcDespesaCl} por nome.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	@Override
	public OrcDespesaCl findOrcDespesaClByNameAndActive(final String name) {
		return dao.findByNameAndActive(name);
	}

	/**
	 * Busca um {@link OrcDespesaCl} por OrcamentoDespesa.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	@Override
	public OrcDespesaCl findByOrcamentoDespesa(
			final OrcamentoDespesa orcamentoDespesa) {
		return dao.findByOrcamentoDespesa(orcamentoDespesa);
	}

	/**
	 * Busca um OrcDespCl por nome, cliente e ativos para validacao de nome na
	 * criacao do mesmo.
	 * 
	 * @param {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	public OrcDespesaCl findByNameAndClientAndActive(
			final OrcDespesaCl orcDespCl) {
		return dao.findByNameAndClientAndActive(orcDespCl);
	}

	/**
	 * Busca apenas os TB vigentes.
	 * 
	 * @return
	 */
	public List<OrcDespesaCl> findOnlyValidByClient(final Cliente cliente) {
		return dao.findOnlyValidByClient(cliente);
	}

	/**
	 * Método que verifica se o Travel Budget pode ser criado com o valor
	 * informado.
	 * 
	 * @param orcDespesa
	 *            Objeto do tipo {@link OrcamentoDespesa}
	 * @param grantedCLobList
	 *            Lista com os ids de c-Lobs selecionados na tela
	 * 
	 * @return boolean <li>true - o valor do travel budget está dentro do limite
	 *         permitido</li> <li>false - o valor do travel budget está fora do
	 *         limite permitido</li>
	 */
	public boolean canCreateTravelBudget(final OrcamentoDespesa orcDespesa,
			final String[] grantedCLobList) {

		BigDecimal orcamentoMaximo = new BigDecimal(0);
		BigDecimal totalJaOrcado = new BigDecimal(0);

		ContratoPratica clob = new ContratoPratica();

		for (String grantedCLob : grantedCLobList) {

			// Busca o contrato pratica por id
			clob = contratoPraticaService.findContratoPraticaById(Long
					.valueOf(grantedCLob));

			totalJaOrcado = totalJaOrcado
					.add(calculaValorTravelBudgetsExistentes(orcDespesa, clob));

			orcamentoMaximo = orcamentoMaximo.add(calculaValorMaximoOrcamento(
					orcDespesa, clob));

		}

		if (totalJaOrcado.add(orcDespesa.getValorOrcado()).compareTo(
				orcamentoMaximo) == -1) {
			return true;
		}

		return false;
	}

	public boolean hasBudgetToCreateTravelBudget(final OrcamentoDespesa orcDespesa, final Long[] grantedCLobList) {

		BigDecimal orcamentoMaximo = new BigDecimal(0);
		BigDecimal totalJaOrcado = new BigDecimal(0);

		ContratoPratica clob;

		for (Long grantedCLob : grantedCLobList) {

			// Busca o contrato pratica por id
			clob = contratoPraticaService.findContratoPraticaById(grantedCLob);
			totalJaOrcado = totalJaOrcado.add(calculaValorTravelBudgetsExistentes(orcDespesa, clob));
			orcamentoMaximo = orcamentoMaximo.add(calculaValorMaximoOrcamento(orcDespesa, clob));
		}

		if (totalJaOrcado.add(orcDespesa.getValorOrcado()).compareTo(orcamentoMaximo) == -1) {
			return true;
		}

		return false;
	}

	/**
	 * Calcula o valor máximo que um Travel Budget pode ser criado. Este valor é
	 * calculado da seguinte forma:
	 * 
	 * <li>- Quebra o Travel Budget em meses</li> <li>- Chama view que pega o
	 * valor mensal do custo embutivo dos funcionários alocados no mapa
	 * (percentual de viagem vinculado ao perfil x valor salario)</li> <li>-
	 * Converte o valor retornada da view para a moeda do travel budget</li> <li>
	 * - Soma o valor</li>
	 * 
	 * @param orcDespesa
	 *            Objeto {@link OrcamentoDespesa}
	 * @param clob
	 *            Objeto {@link ContratoPratica}
	 * 
	 * @return Valor orçamento máximo disponível para criação de Travel Budget
	 *         {@link BigDecimal}
	 */
	private BigDecimal calculaValorMaximoOrcamento(OrcamentoDespesa orcDespesa,
			ContratoPratica clob) {

		List<VwPmsCustoEmbutidoPv> listCustoEmbutido = new ArrayList<VwPmsCustoEmbutidoPv>();
		BigDecimal orcamentoMaximo = new BigDecimal(0);

		// Quebra o período do Travel Budget em meses
		for (Date dataMes : orcamentoDespesaService
				.quebraOrcamentoDespesaMeses(orcDespesa)) {

			// Chama view que pega o valor mensal do custo embutivo dos
			// funcionários alocados no mapa (percentual x valor salario), por
			// perfil vendido
			listCustoEmbutido = vwPmsCustoEmbutidoPvService.findByClobAndMes(
					clob, dataMes);

			for (VwPmsCustoEmbutidoPv custoEmbutido : listCustoEmbutido) {

				// Chama o serviço que converte o valor de uma
				// moeda(moedaDe) para outra (moedaPara) e soma o valor a
				// variável orcamentoMaximo
				orcamentoMaximo = orcamentoMaximo.add(vwPmsCotacaoMoedaService
						.getValorConvertidoMoedaDePara(custoEmbutido.getId()
								.getTotal(), custoEmbutido.getId()
								.getCodigoMoeda(), orcDespesa.getMoeda()
								.getCodigoMoeda()));
			}

		}
		return orcamentoMaximo;
	}

	/**
	 * Soma os valores já lançados para os Travel Budgets já existentes.
	 * 
	 * @param orcDespesa
	 *            Objeto {@link OrcamentoDespesa}
	 * @param clob
	 *            Objeto {@link ContratoPratica}
	 * 
	 * @return Valor travel budget calculado {@link BigDecimal}
	 */
	private BigDecimal calculaValorTravelBudgetsExistentes(
			OrcamentoDespesa orcDespesa, ContratoPratica clob) {

		BigDecimal totalJaOrcado = new BigDecimal(Long.valueOf(0));

		List<OrcDespesaMes> listOrcDespesaMes = orcDespesaMesService
				.findOrcDespesaMesByClobAndPeriod(clob,
						orcDespesa.getDataInicio(), orcDespesa.getDataFim());

		// Valor total ja orcado pelos travel budget anteriores
		for (OrcDespesaMes orcDespMes : listOrcDespesaMes) {

			OrcamentoDespesa orcDespesaIterado = orcamentoDespesaService
					.findOrcamentoDespesaById(orcDespMes.getOrcDespesa()
							.getCodigoOrcaDespesa());

			// Chama o serviço que converte o valor de uma moeda(moedaDe)
			// para outra moeda (moedaPara) e soma o valor a variável
			// totalJaOrcado
			BigDecimal valorMes = orcDespesaIterado.getValorOrcado().multiply(
					orcDespMes.getPercentualMes().divide(new BigDecimal(100)));

			totalJaOrcado = totalJaOrcado.add(vwPmsCotacaoMoedaService
					.getValorConvertidoMoedaDePara(valorMes, orcDespesaIterado
							.getMoeda().getCodigoMoeda(), orcDespesa.getMoeda()
							.getCodigoMoeda()));
		}

		return totalJaOrcado;
	}

	/**
	 * Verifica se um login é VP ou nao.
	 * 
	 * @param login
	 * @return
	 */
	public Boolean isVp(String login) {
		Pessoa pessoa = pessoaService.findPessoaByLogin(login);
		List<PerfilSistema> listPerfilSistema = new ArrayList<PerfilSistema>(
				pessoa.getPerfilSistemas());
		boolean vpPerfil = false;
		for (PerfilSistema perfilSistema : listPerfilSistema) {
			if (perfilSistema.getCodigoPerfilSistema().equals(13L)
					|| perfilSistema.getCodigoPerfilSistema().equals(1L)) {
				vpPerfil = true;
				break;
			}
		}
		return Boolean.valueOf(vpPerfil);
	}

	@Transactional
	public void clone(OrcamentoDespesa orcamentoDespesa, OrcDespesaCl entity, Long[] clobsCodeToClone, Boolean copyFollow, Boolean copyWhiteList, Boolean copyWhiteListDelegation) throws IntegrityConstraintException {
		this.createOrcDespesaCl(entity);

		for (Long code :clobsCodeToClone) {

			ContratoPratica cp = contratoPraticaService.findContratoPraticaById(code);

			ContratoPraticaOrcDespClId id = new ContratoPraticaOrcDespClId(cp.getCodigoContratoPratica(),entity.getCodigoOrcaDespCl());
			ContratoPraticaOrcDespCl cpodCl = new ContratoPraticaOrcDespCl(id, entity, cp);

			contratoPraticaOrcDespClService.createContratoPraticaOrcDespCl(cpodCl);
		}

		if (Boolean.TRUE.equals(copyWhiteList)) {
			List<OrcDespWhiteList> whiteListOrigin = whiteListService.findByOrcamentoDespesa(orcamentoDespesa);
			for (OrcDespWhiteList whiteList : whiteListOrigin) {
				OrcDespWhiteList whiteListToClone = new OrcDespWhiteList();
				whiteListToClone.setOrcamentoDespesa(entity.getOrcamentoDespesa());
				whiteListToClone.setCodigoLogin(whiteList.getCodigoLogin());
				whiteListService.addPersonOrcDespWhiteList(whiteListToClone);
			}
		}

		if (Boolean.TRUE.equals(copyFollow)) {
			List<FollowOrcamentoDesp> followOrcamentoDespListOrigin = followOrcamentoDespService.findByOrcDespesa(orcamentoDespesa);
			for (FollowOrcamentoDesp follow : followOrcamentoDespListOrigin) {
				FollowOrcamentoDesp followToClone = new FollowOrcamentoDesp();
				followToClone.setOrcamentoDespesa(entity.getOrcamentoDespesa());
				followToClone.setCodigoLogin(follow.getCodigoLogin());
				followOrcamentoDespService.createFollowOrcamentoDesp(followToClone);
			}
		}

		if (Boolean.TRUE.equals(copyWhiteListDelegation)) {
			List<OrcDespDelegacao> delegations = orcDespDelegacaoService.findByOrcDespesa(orcamentoDespesa);
			for(OrcDespDelegacao delegation : delegations) {
				OrcDespDelegacao delegationToClone = new OrcDespDelegacao();
				delegationToClone.setId(new OrcDespDelegacaoId(entity.getOrcamentoDespesa().getCodigoOrcaDespesa(), delegation.getCodigoLogin()));
				delegationToClone.setCodigoLogin(delegation.getCodigoLogin());
				delegationToClone.setOrcDespesa(entity.getOrcamentoDespesa());
				orcDespDelegacaoService.createOrcDespDelegacao(delegationToClone);
			}
		}
	}
}