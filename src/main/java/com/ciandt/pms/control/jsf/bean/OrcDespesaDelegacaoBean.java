package com.ciandt.pms.control.jsf.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ciandt.pms.business.service.IFollowGrupoCustoService;
import com.ciandt.pms.model.FollowGrupoCusto;
import com.ciandt.pms.model.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.vo.OrcDespesaDelegacaoRow;

/**
 * Define o backBean da tela de delegation.
 *
 * @author peter
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespesaDelegacaoBean {

	/** Default serial version UID. */
	private static final long serialVersionUID = 1L;

	/** To. */
	private OrcamentoDespesa to = new OrcamentoDespesa();

	/** OrcDespWhiteList. */
	private OrcDespWhiteList orcDespWhiteList = new OrcDespWhiteList();

	/** Nome do cliente ou do grupo de custo do orcamentoDespesa. */
	private String nomeClientOrCostCroup;

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);

	/** Lista de orcamento despesa delegados. */
	private List<OrcDespesaDelegacaoRow> listOrcamentoDespesaRow = new ArrayList<OrcDespesaDelegacaoRow>();

	/** Lista de WhiteList. */
	private List<OrcDespWhiteList> listWhiteList = new ArrayList<OrcDespWhiteList>();

	/** Mapa de seguidores do grupo de custo. */
	public Map<Long, String> mapFollowGrupoCusto = new HashMap<Long, String>();

	@Autowired
	private IFollowGrupoCustoService followGrupoCustoService;

	/**
	 * Carrega o map de seguidores pelo login.
	 *
	 * @param pessoa
	 *            login.
	 * @return map.
	 */
	public Map<Long, String> mapFollowGrupoCusto(final Pessoa pessoa) {
		Map<Long, String> mapFollow = new HashMap<Long, String>();
		List<FollowGrupoCusto> listaFollow = followGrupoCustoService.findbyLogin(pessoa.getCodigoLogin());

		for (FollowGrupoCusto followGrupoCusto : listaFollow) {
			mapFollow.put(followGrupoCusto.getGrupoCusto().getCodigoGrupoCusto(), followGrupoCusto.getCodigoLogin());
		}

		return mapFollow;
	}

	/**
	 * @return the mapFollowGrupoCusto
	 */
	public Map<Long, String> getMapFollowGrupoCusto() {
		return mapFollowGrupoCusto;
	}

	/**
	 * @param mapFollowGrupoCusto
	 *            the mapFollowGrupoCusto to set
	 */
	public void setMapFollowGrupoCusto(final Map<Long, String> mapFollowGrupoCusto) {
		this.mapFollowGrupoCusto = mapFollowGrupoCusto;
	}

	/**
	 * @return the to
	 */
	public OrcamentoDespesa getTo() {
		return to;
	}

	/**
	 * @return the currentRowId
	 */
	public Long getCurrentRowId() {
		return currentRowId;
	}

	/**
	 * @param currentRowId
	 *            the currentRowId to set
	 */
	public void setCurrentRowId(final Long currentRowId) {
		this.currentRowId = currentRowId;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(OrcamentoDespesa to) {
		this.to = to;
	}

	/**
	 * @return the orcDespWhiteList
	 */
	public OrcDespWhiteList getOrcDespWhiteList() {
		return orcDespWhiteList;
	}

	/**
	 * @param orcDespWhiteList
	 *            the orcDespWhiteList to set
	 */
	public void setOrcDespWhiteList(OrcDespWhiteList orcDespWhiteList) {
		this.orcDespWhiteList = orcDespWhiteList;
	}

	/**
	 * @return the nomePai
	 */
	public String getNomeClientOrCostCroup() {
		return nomeClientOrCostCroup;
	}

	/**
	 * @param nomePai
	 *            the nomePai to set
	 */
	public void setNomeClientOrCostCroup(String nomeClientOrCostCroup) {
		this.nomeClientOrCostCroup = nomeClientOrCostCroup;
	}

	/**
	 * @return the listOrcamentoDespesa
	 */
	public List<OrcDespesaDelegacaoRow> getListOrcamentoDespesaRow() {
		return listOrcamentoDespesaRow;
	}

	/**
	 * @param listOrcamentoDespesa
	 *            the listOrcamentoDespesa to set
	 */
	public void setListOrcamentoDespesaRow(
			List<OrcDespesaDelegacaoRow> listOrcamentoDespesaRow) {
		this.listOrcamentoDespesaRow = listOrcamentoDespesaRow;
	}

	/**
	 * @return the listWhiteList
	 */
	public List<OrcDespWhiteList> getListWhiteList() {
		return listWhiteList;
	}

	/**
	 * @param listWhiteList
	 *            the listWhiteList to set
	 */
	public void setListWhiteList(List<OrcDespWhiteList> listWhiteList) {
		this.listWhiteList = listWhiteList;
	}

}