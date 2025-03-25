package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.GrupoCustoBean;
import com.ciandt.pms.model.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.impl.OrcDespDelegacaoService;
import com.ciandt.pms.control.jsf.bean.OrcDespesaDelegacaoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.vo.OrcDespesaDelegacaoRow;
import com.ciandt.pms.util.LoginUtil;

/**
 * Define o Controller da entidade.
 *
 * @since 31/07/2009
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BUS.WHITE_LIST:VW" })
public class OrcDespesaDelegacaoController {

	/** Outcome da tela de delegados. */
	private static final String OUTCOME_ORC_DESPESA_DELEGACAO = "orc_despesa_delegacao";

	/** Outcome da tela de white list delegados. */
	private static final String OUTCOME_ORC_DESPESA_DELEGACAO_ALLOW_LIST = "orc_despesa_delegacao_allow_list";

	/** BackBean da tela */
	@Autowired
	private OrcDespesaDelegacaoBean bean;

	/** Instancia do servico de {@link OrcDespDelegacaoService}. */
	@Autowired
	private IOrcDespDelegacaoService orcDespesaDelegacaoService;

	/** Instancia do servico de {@link OrcamentoDespesa}. */
	@Autowired
	private IOrcamentoDespesaService orcDespesaService;

	/** Instancia do servico de {@link OrcDespesaCl}. */
	@Autowired
	private IOrcDespesaClService orcDespesaClService;

	/** Instancia do servico de {@link OrcDespesaGc}. */
	@Autowired
	private IOrcDespesaGcService orcDespesaGcService;

	/** Instancia do servico de {@link OrcDespesaGc}. */
	@Autowired
	private IOrcDespWhiteListService whiteListService;

	/** Instancia do servico de {@link OrcDespesaGc}. */
	@Autowired
	private IRecursoService recursoService;

	/** Instancia do servico. */
	@Autowired
	private IGrupoCustoService grupoCustoService;

	@Autowired
	private IFollowGrupoCustoService followGrupoCustoService;

	@Autowired
	private IFollowOrcamentoDespService followOrcamentoDespService;
	/**
	 * Cria um seguidor de grupo de custo.
	 */
	public void followGrupoCusto() {
		OrcamentoDespesa orcDespesa = bean.getTo();
		orcDespesa.setCodigoOrcaDespesa(bean.getCurrentRowId());
		OrcDespesaGc orcDespGc = orcDespesaGcService.findByOrcDespesa(orcDespesa);

		if(orcDespGc == null){

			FollowOrcamentoDesp followOrcamentoDesp = new FollowOrcamentoDesp();
			followOrcamentoDesp.setCodigoLogin(LoginUtil.getLoggedUsername());
			followOrcamentoDesp.setOrcamentoDespesa(orcDespesa);

			followOrcamentoDespService.createFollowOrcamentoDesp(followOrcamentoDesp);

		}else {
			// busca grupo de custo
			GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(orcDespGc.getGrupoCusto().getCodigoGrupoCusto());

			// cria o seguidor
			FollowGrupoCusto followGC = new FollowGrupoCusto();
			followGC.setGrupoCusto(grupoCusto);
			followGC.setCodigoLogin(LoginUtil.getLoggedUsername());

			followGrupoCustoService.createFollowGrupoCusto(followGC);
		}

		// Carrega o map de seguidor.
		bean.setMapFollowGrupoCusto(this.mapFollowGrupoCusto(LoginUtil.getLoggedUser()));

		this.prepareOrcDespesaDelegacao();
	}

	/**
	 * Remove um seguidor de Grupo de custo.
	 */
	public void unfollowGrupoCusto() {
		OrcamentoDespesa orcDespesa = bean.getTo();
		orcDespesa.setCodigoOrcaDespesa(bean.getCurrentRowId());

		OrcDespesaGc orcDespGc = orcDespesaGcService.findByOrcDespesa(orcDespesa);

		if(orcDespGc == null){
			FollowOrcamentoDesp followOcDesp = followOrcamentoDespService.findByOrcDespesaAndLogin(orcDespesa,LoginUtil.getLoggedUsername());
			followOrcamentoDespService.removeFollowOrcamentoDesp(followOcDesp);
		}else{
			// busca grupo de custo
			GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(orcDespGc.getGrupoCusto().getCodigoGrupoCusto());
			// busca o followgrupocusto pelo grupo de custo e login
			FollowGrupoCusto followGrupoCusto = followGrupoCustoService.findByGrupoCustoAndLogin(grupoCusto,LoginUtil.getLoggedUsername());

			followGrupoCustoService.removeFollowGrupoCusto(followGrupoCusto);
		}



		// Carrega o map de seguidor.
		bean.setMapFollowGrupoCusto(this.mapFollowGrupoCusto(LoginUtil.getLoggedUser()));

		this.prepareOrcDespesaDelegacao();

	}

	/**
	 * Carrega o map de seguidores pelo login.
	 *
	 * @param pessoa
	 *            login.
	 * @return map.
	 */
	private Map<Long, String> mapFollowGrupoCusto(final Pessoa pessoa) {
		Map<Long, String> mapFollow = new HashMap<Long, String>();
		List<FollowGrupoCusto> listaFollow = followGrupoCustoService.findbyLogin(pessoa.getCodigoLogin());

		List<FollowOrcamentoDesp> listaFollowOrcDesp = followOrcamentoDespService.findByLogin(pessoa.getCodigoLogin());

		for (FollowGrupoCusto followGrupoCusto : listaFollow) {
			mapFollow.put(followGrupoCusto.getGrupoCusto().getCodigoGrupoCusto(), followGrupoCusto.getCodigoLogin());
		}

		for(FollowOrcamentoDesp followOrc : listaFollowOrcDesp){
			mapFollow.put(followOrc.getOrcamentoDespesa().getCodigoOrcaDespesa(),followOrc.getCodigoLogin());
		}

		return mapFollow;
	}

	/**
	 * @return the bean
	 */
	public OrcDespesaDelegacaoBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(OrcDespesaDelegacaoBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepare da tela de delegacao.
	 *
	 * @return
	 */
	public String prepareOrcDespesaDelegacao() {

		List<OrcDespDelegacao> listOrcDespDelegacao = orcDespesaDelegacaoService
				.findByLogin(LoginUtil.getLoggedUsername());

		List<OrcDespesaDelegacaoRow> listResult = new ArrayList<OrcDespesaDelegacaoRow>();
		OrcamentoDespesa orcDesp = null;
		OrcDespesaDelegacaoRow orcDespRow = null;
		for (OrcDespDelegacao orcDespDelegacao : listOrcDespDelegacao) {
			orcDesp = orcDespesaService
					.findOrcamentoDespesaById(orcDespDelegacao.getOrcDespesa()
							.getCodigoOrcaDespesa());

			orcDespRow = new OrcDespesaDelegacaoRow();

			if ("CL".equals(orcDesp.getTpOrcDesp())) {
				OrcDespesaCl orcDespCl = orcDespesaClService
						.findByOrcamentoDespesa(orcDesp);
				orcDespRow.setNomeClientOrCostgroup(orcDespCl.getCliente()
						.getNomeCliente());
			} else {
				OrcDespesaGc orcDespGc = orcDespesaGcService
						.findByOrcDespesa(orcDesp);
				orcDespRow.setNomeClientOrCostgroup(orcDespGc.getGrupoCusto()
						.getNomeGrupoCusto());
			}

			orcDespRow.setOrcDespesa(orcDesp);
			listResult.add(orcDespRow);
		}

		bean.setListOrcamentoDespesaRow(listResult);

		bean.setMapFollowGrupoCusto(this.mapFollowGrupoCusto(LoginUtil.getLoggedUser()));

		return OUTCOME_ORC_DESPESA_DELEGACAO;
	}

	/**
	 * Prepare da tela de whiteList
	 *
	 * @return
	 */
	public String prepareWhiteList() {
		bean.setOrcDespWhiteList(new OrcDespWhiteList());
		OrcamentoDespesa orcDespesa = bean.getTo();

		if ("CL".equals(orcDespesa.getTpOrcDesp())) {
			OrcDespesaCl orcDespCl = orcDespesaClService
					.findByOrcamentoDespesa(orcDespesa);
			bean.setNomeClientOrCostCroup(orcDespCl.getCliente()
					.getNomeCliente());
		} else {
			OrcDespesaGc orcDespGc = orcDespesaGcService
					.findByOrcDespesa(orcDespesa);
			bean.setNomeClientOrCostCroup(orcDespGc.getGrupoCusto()
					.getNomeGrupoCusto());
		}

		bean.setListWhiteList(whiteListService
				.findByOrcamentoDespesa(orcDespesa));

		return OUTCOME_ORC_DESPESA_DELEGACAO_ALLOW_LIST;

	}

	/**
	 * Realiza a valida?ao do Recurso e da Pessoa.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            , valor do componente.
	 */
	public void validateRecursoPessoa(final FacesContext context,
									  final UIComponent component, final Object value) {

		String login = (String) value;
		if ((login != null) && (!"".equals(login))) {
			Recurso r = recursoService.findRecursoByMnemonico(login);
			if (r == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * A??o utilizada no autocomplete login. Retorna uma lista de recursos.
	 *
	 * @param value
	 *            - valor (mnemonico) utilizado na busca dos recursos
	 *
	 * @return retorna uma lista de recurso
	 */
	public List<Recurso> autoCompleteRecurso(final Object value) {
		String tipoRecurso = Constants.RESOURCE_TYPE_PE;
		return recursoService.quickSearch((String) value, tipoRecurso);
	}

	/**
	 * Adiciona login na WhiteList.
	 *
	 */
	public void addPersonWhiteList() {
		OrcDespWhiteList whiteList = bean.getOrcDespWhiteList();

		OrcamentoDespesa orcamento = orcDespesaService
				.findOrcamentoDespesaById(bean.getTo().getCodigoOrcaDespesa());

		Hibernate.initialize(orcamento);
		whiteList.setOrcamentoDespesa(orcamento);

		if (whiteListService.addPersonOrcDespWhiteList(whiteList)) {
			Messages.showSucess("addPersonWhiteList",
					Constants.GENEREC_MSG_SUCCESS_ADD);
			bean.setOrcDespWhiteList(new OrcDespWhiteList());
		}
		this.prepareWhiteList();
	}

	/**
	 * Delete pessoa da whiteLIst.
	 *
	 */
	public void removePersonWhiteList() {
		OrcDespWhiteList entity = whiteListService.findById(bean
				.getOrcDespWhiteList().getCodigoOrcDespWl());
		if (whiteListService.removePersonOrcDespWhiteLIst(entity)) {
			Messages.showSucess("remove", Constants.GENEREC_MSG_SUCCESS_REMOVE);
		}
		this.prepareWhiteList();
	}
}