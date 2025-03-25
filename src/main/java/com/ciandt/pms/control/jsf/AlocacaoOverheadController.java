package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoOverheadService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.ITipoOverheadService;
import com.ciandt.pms.control.jsf.bean.AlocacaoOverheadBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TipoOverhead;
import com.ciandt.pms.model.vo.AlocacaoOverheadRow;
import com.ciandt.pms.util.DateUtil;


/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 19/07/2010
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "PER.LEAVE_OF_ABSENCE:VW" })
public class AlocacaoOverheadController {

    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_ALOCACAO_OVERHEAD_ADD = "alocacaoOverhead_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_ALOCACAO_OVERHEAD_EDIT = "alocacaoOverhead_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_ALOCACAO_OVERHEAD_DELETE = "alocacaoOverhead_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_ALOCACAO_OVERHEAD_RESEARCH = "alocacaoOverhead_research";

    /** Instancia do servico. */
    @Autowired
    private IAlocacaoOverheadService alOvService;

    /** Instancia do servico Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do servico TipoOverhead. */
    @Autowired
    private ITipoOverheadService tipoOverheadService;
    
    /** Instancia do servico ModuloService. */
    @Autowired
    private IModuloService moduloService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private AlocacaoOverheadBean bean = null;

    /**
     * @return the bean
     */
    public AlocacaoOverheadBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final AlocacaoOverheadBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();
        this.loadTipoOverheadList(tipoOverheadService.findTipoOverheadAll());
        return OUTCOME_ALOCACAO_OVERHEAD_RESEARCH;
    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareCreate() {
        bean.reset();
        this.loadTipoOverheadList(tipoOverheadService
                .findTipoOverheadAllActive());
        bean.setIsUpdate(Boolean.valueOf(false));
        return OUTCOME_ALOCACAO_OVERHEAD_ADD;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void create() {
        AlocacaoOverhead to = bean.getTo();

        Date closingDateHistoryLock = moduloService.getClosingDateHistoryLock();

		if (moduloService.dateAfterHistoryLock(to.getDataInicio())
				&& moduloService.dateAfterHistoryLock(to.getDataFim())) {

	        to.setIndicadorStatus(Constants.ALOCACAO_OH_STATUS_PERFORMED);
	        to.setPessoa(pessoaService.findPessoaByLogin(to.getPessoa()
	                .getCodigoLogin()));
	        String nomeTipoOverhead = to.getTipoOverhead().getNomeTipoOverhead();
	        to.setTipoOverhead(tipoOverheadService.findTipoOverheadById(bean
	                .getTipoOverheadMap().get(nomeTipoOverhead)));
	
	        alOvService.createAlocacaoOverhead(to);
	        
	        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
	        		Constants.ENTITY_NAME_ALOCACAO_OVERHEAD);
	        bean.resetTo();
        } else {
			Messages.showError("create",
					Constants.MSG_ERROR_MODULO_VERIFY_HISTORY_LOCK, DateUtil
							.formatDate(closingDateHistoryLock,
									Constants.DEFAULT_DATE_PATTERN_SIMPLE,
									Constants.LOCALE_DEFAULT_BUNDLE));
		}
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareUpdate() {
        findById(bean.getCurrentRowId());
        this.loadTipoOverheadList(tipoOverheadService
                .findTipoOverheadAllActive());
        bean.setIsUpdate(Boolean.valueOf(true));
        return OUTCOME_ALOCACAO_OVERHEAD_EDIT;
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        AlocacaoOverhead to = bean.getTo();
        to.setPessoa(pessoaService.findPessoaByLogin(to.getPessoa()
                .getCodigoLogin()));
        String nomeTipoOverhead = to.getTipoOverhead().getNomeTipoOverhead();
        to.setTipoOverhead(tipoOverheadService.findTipoOverheadById(bean
                .getTipoOverheadMap().get(nomeTipoOverhead)));

        alOvService.updateAlocacaoOverhead(to);

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_ALOCACAO_OVERHEAD);
        bean.resetTo();
        findByFilter();
        return OUTCOME_ALOCACAO_OVERHEAD_RESEARCH;
    }

    /**
     * Prepara a tela de remocao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareRemove() {
        findById(bean.getCurrentRowId());
        return OUTCOME_ALOCACAO_OVERHEAD_DELETE;
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {
        alOvService.removeAlocacaoOverhead(alOvService
                .findAlocacaoOverheadById(bean.getTo()
                        .getCodigoAlocacaoOverhead()));
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_ALOCACAO_OVERHEAD);
        bean.resetTo();
        findByFilter();
        return OUTCOME_ALOCACAO_OVERHEAD_RESEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        bean.setTo(alOvService.findAlocacaoOverheadById(id));
        if (bean.getTo() == null
                || bean.getTo().getCodigoAlocacaoOverhead() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        AlocacaoOverhead filter = bean.getFilter();

        Long codigoTipoOverhead = Long.valueOf(0);
        TipoOverhead tipoOverhead = filter.getTipoOverhead();
        String nomeTipoOverhead = tipoOverhead.getNomeTipoOverhead();
        if (!StringUtils.isEmpty(nomeTipoOverhead)) {
            codigoTipoOverhead = bean.getTipoOverheadMap()
                    .get(nomeTipoOverhead);
        }
        tipoOverhead.setCodigoTipoOverhead(codigoTipoOverhead);

        List<AlocacaoOverhead> alocacoesOverhead = alOvService.findAlocacaoOverheadByFilter(filter);
        List<AlocacaoOverheadRow> alocacaoOverheadRows = new ArrayList<AlocacaoOverheadRow>();
        AlocacaoOverheadRow alocacaoOverheadRow = null;
        for (AlocacaoOverhead alocacaoOverhead : alocacoesOverhead) {

        	alocacaoOverheadRow = new AlocacaoOverheadRow();
			alocacaoOverheadRow.setShowEditButton(this.alocacaoOverheadCanBeEdited(alocacaoOverhead));
			alocacaoOverheadRow.setAlocacaoOverhead(alocacaoOverhead);
			alocacaoOverheadRows.add(alocacaoOverheadRow);
		}

        bean.setResultList(alocacaoOverheadRows);

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    private Boolean alocacaoOverheadCanBeEdited(AlocacaoOverhead alocacaoOverhead) {
		if (alocacaoOverhead.getTipoOverhead().getCodigoTipoOverhead() == 1) {
			return false;
		}

		if (!moduloService.dateAfterHistoryLock(alocacaoOverhead.getDataInicio())) {
			return false;
		}

		return true;
	}

    /**
     * Realiza a validaçao do campo Login.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validatePessoa(final FacesContext context,
            final UIComponent component, final Object value) {

        String login = (String) value;

        if ((login != null) && (!"".equals(login))) {
            Pessoa pessoa = pessoaService.findPessoaByLogin(login);

            if (pessoa == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }

    }

    /**
     * Popula a lista de TipoOverhead para combobox.
     * 
     * @param tipoOverheads
     *            lista de TipoOverhead.
     * 
     */
    private void loadTipoOverheadList(final List<TipoOverhead> tipoOverheads) {
        Map<String, Long> tipoOverheadMap = new HashMap<String, Long>();
        List<String> tipoOverheadList = new ArrayList<String>();

        for (TipoOverhead tipoOverhead : tipoOverheads) {
            tipoOverheadMap.put(tipoOverhead.getNomeTipoOverhead(),
                    tipoOverhead.getCodigoTipoOverhead());
            tipoOverheadList.add(tipoOverhead.getNomeTipoOverhead());
        }

        bean.setTipoOverheadMap(tipoOverheadMap);
        bean.setTipoOverheadList(tipoOverheadList);
    }

    /**
     * Ação utilizada no autocomplete da Pessoa. Retorna uma lista de Pessoas.
     * 
     * @param value
     *            - valor (login) utilizado na busca das Pessoas
     * 
     * @return retorna uma lista de recurso
     */
    public List<Pessoa> autoCompletePessoa(final Object value) {
        return pessoaService.findPessoaByLikeLoginAll((String) value);
    }

}