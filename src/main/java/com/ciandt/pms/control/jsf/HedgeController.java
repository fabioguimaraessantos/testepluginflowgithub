package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IHedgeService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.control.jsf.bean.HedgeBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.HedgeMoedaMes;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe HedgeController proporciona o controle das ações
 * da camada de presentação.
 *
 * @since 24/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_FINANCIAL" })
public class HedgeController {

    /** Instancia do serviço HedgeService. */
    @Autowired
    private IHedgeService hedgeService;
    
    /** Instancia do serviço MoedaService. */
    @Autowired
    private IMoedaService moedaService;
    
    /** Instancia do bean. */
    @Autowired
    private HedgeBean bean;
    
    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_ADD = "hedge_add";
    
    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_SEARCH = "hedge_research";
    
    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_EDIT = "hedge_edit";
    
    /**
     * Prepara a tela de criação de Hedge.
     * 
     * @return retorna a página de criação.
     */
    public String prepareCreateHedge() {
        bean.reset();
        
        loadMoedaList(moedaService.findMoedaAll());
        
        return OUTCOME_ADD;
    }
    
    /**
     * Cria uma entidade Hedge.
     */
    public void createHedge() {
        HedgeMoedaMes hedge = bean.getTo();
        
        setSelectedAttributes(hedge);
        
       
        if (hedgeService.createHedge(hedge)) {
            Messages.showSucess("createHedge", 
                Constants.DEFAULT_MSG_SUCCESS_CREATE,
                Constants.ENTITY_NAME_HEDGE);
            
            bean.reset();
        } else {
            Messages.showError("createHedge",
                Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                Constants.ENTITY_NAME_HEDGE);
        }
        
    }
    
    /**
     * Prepara para edição.
     * 
     * @return retorna a página de edição.
     */
    public String prepareUpdateHedge() {
        HedgeMoedaMes hedge = hedgeService.findHedgeById(
                bean.getTo().getCodigoHedgeMoedaMes());
        
        bean.setTo(hedge);
        
        bean.setMonth(DateUtil.getMonthString(hedge.getDataMes()));
        bean.setYear(DateUtil.getYearString(hedge.getDataMes()));
        
        return OUTCOME_EDIT;
    }
    
    /**
     * Realiza o update da entidade.
     * 
     * @return retorna a página de search
     */
    public String updateHedge() {
        HedgeMoedaMes hedge = bean.getTo();
        
        setSelectedAttributes(hedge);
        
        if (hedgeService.updateHedge(hedge)) {
            Messages.showSucess("updateHedge", 
                Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_HEDGE);
            
            return backToSearch();
        } else {
            Messages.showError("createHedge",
                Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                Constants.ENTITY_NAME_HEDGE);
            
            return null;
        }
        
    }
    
    /**
     * retorna para a tela de busca.
     * 
     * @return retorna a página de busca.
     */
    public String backToSearch() {
        findByFilter();
        
        return OUTCOME_SEARCH;
    }
    
    /**
     * Prepara para o search do Hedge.
     * 
     * @return retorna a pagina de search
     */
    public String prepareResearchHedge() {
        bean.reset();
        
        loadMoedaList(moedaService.findMoedaAll());
        
        return OUTCOME_SEARCH;
    }
    
    /**
     * Remove a entidade Hedge.
     */
    public void removeHedge() {
        
        try {
            hedgeService.removeHedge(bean.getTo());
            
            Messages.showSucess("updateHedge", 
                Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_HEDGE);
        
        } catch (DataIntegrityViolationException e) {
            Messages.showError("removeHedge",
                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_HEDGE);
        }
        
        findByFilter();
    }
    
    /**
     * Realiza a busca utlizando os criterios do filtro.
     */
    public void findByFilter() {
        HedgeMoedaMes filter = bean.getFilter();
        
        setSelectedAttributes(filter);
        
        List<HedgeMoedaMes> resultList = hedgeService.findHedgeByFilter(filter);
        bean.setResultList(resultList);
        
        if (resultList.isEmpty()) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }
    
    /**
     * Seta os atributos selecionados do TO.
     * 
     * @param hedge - TO a ser setado.
     */
    private void setSelectedAttributes(
            final HedgeMoedaMes hedge) {
       
        hedge.setDataMes(DateUtil.getDate(
                bean.getMonth(), bean.getYear()));
        
        Long idMoeda = bean.getMoedaMap().get(
                hedge.getMoeda().getNomeMoeda());
        Moeda moeda = null;
        if (idMoeda != null) {
            moeda = moedaService.findMoedaById(idMoeda);
        }
       
        hedge.setMoeda(moeda);
        
    }
    
    /**
     * Popula a lista de Moeda para combobox.
     * 
     * @param pMoedaList
     *            lista de Moeda.
     * 
     */
    private void loadMoedaList(final List<Moeda> pMoedaList) {

        Map<String, Long> moedaMap = new HashMap<String, Long>();
        List<String> moedaList = new ArrayList<String>();

        for (Moeda moeda : pMoedaList) {
            moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            moedaList.add(moeda.getNomeMoeda());
        }
        bean.setMoedaMap(moedaMap);
        bean.setMoedaList(moedaList);
    }

    /**
     * @return the bean
     */
    public HedgeBean getBean() {
        return bean;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(final HedgeBean bean) {
        this.bean = bean;
    }
}
