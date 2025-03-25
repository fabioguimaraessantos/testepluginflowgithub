/**
 * Classe CotacaoMoedaController - Classe da camada de apresentação da CotacaoMoeda
 */
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

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICotacaoMoedaService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.control.jsf.bean.CotacaoMoedaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe CotacaoMoedaController proporciona as funcionalidades da camada de
 * apresentação para a entidade CotacaoMoeda.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_FINANCIAL" })
public class CotacaoMoedaController {

    /*********** OUTCOMES **************************/

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_COTACAO_MOEDA_RESEARCH = "cotacaoMoeda_research";
    
    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_COTACAO_MOEDA_ADD = "cotacaoMoeda_add";
    
    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_COTACAO_MOEDA_EDIT = "cotacaoMoeda_edit";
    
    /*********** SERVICES **************************/

    /** Instancia do servico. */
    @Autowired
    private ICotacaoMoedaService cotacaoMoedaService;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /*********** BEANS **************************/

    /** Instancia do bean. */
    @Autowired
    private CotacaoMoedaBean bean = null;

    /********************************************/
    
    /**
     * @return the bean
     */
    public CotacaoMoedaBean getBean() {
        return bean;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(final CotacaoMoedaBean bean) {
        this.bean = bean;
    }
    
    /**
     * Preapra a tela para criar uma contação.
     * 
     * @return retorna a pagina de criação.
     */
//    public String prepareCreate() {
//        bean.resetTo();
//        
//        // carrega as listas de entidades Moeda
//        loadMoedaList(moedaService.findMoedaAll());
//        
//        return OUTCOME_COTACAO_MOEDA_ADD;
//    }
    
    /**
     * Cria uma cotação planejada.
     */
//    public void createForcast() {
//        //pega a cotação
//        CotacaoMoeda cotacao = bean.getTo();
//        // seta a data
//        cotacao.setDataDia(DateUtil.getDate(
//                bean.getMonth(), bean.getYear()));
//        // pega a moeda selecionada
//        Moeda moeda = moedaService.findMoedaById(
//                bean.getMoedaMap().get(cotacao.getMoeda().getNomeMoeda()));
//        //seta a moeda da cotacao
//        cotacao.setMoeda(moeda);
//        
//        // cria a cotação
//        if (cotacaoMoedaService.createCotacaoMoedaForecast(cotacao)) {
//            Messages.showSucess("createForcast",
//                    Constants.DEFAULT_MSG_SUCCESS_CREATE,
//                    Constants.ENTITY_NAME_COTACAO_MOEDA);
//
//            bean.resetTo();
//        }   
//    }
    
    /**
     * Prepara a tela edição de uma cotação.
     * 
     * @return retorna a página de edição.
     */
//    public String prepareUpdate() {
//        Date date = bean.getTo().getDataDia();
//        
//        bean.setMonth(DateUtil.getMonthString(date));
//        bean.setYear("" + DateUtil.getYear(date));
//        
//        return OUTCOME_COTACAO_MOEDA_EDIT;
//    }
    
    /**
     * Remove uma cotação.
     */
//    public void remove() {
//        try {
//            cotacaoMoedaService.removeCotacaoMoedaForecast(bean.getTo());
//        } catch (DataIntegrityViolationException e) { 
//            //capitura a exceção e dá mensagem de erro
//            Messages.showError("remove",
//                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
//                    Constants.ENTITY_NAME_COTACAO_MOEDA);
//        }
//        
//        this.findByFilter();
//    }
    
    /**
     * Realiza o update de uma cotação planejada.
     * 
     * @return retorna a de search se update realizado
     * com sucasso, caso contrario fica na mesma página.
     */
//    public String updateForcast() {
//        
//        CotacaoMoeda cotacao = bean.getTo();
//        // seta a data
//        cotacao.setDataDia(DateUtil.getDate(
//                bean.getMonth(), bean.getYear()));
//        // pega a moeda selecionada
//        Moeda moeda = moedaService.findMoedaById(
//                bean.getMoedaMap().get(cotacao.getMoeda().getNomeMoeda()));
//        //seta a moeda da cotacao
//        cotacao.setMoeda(moeda);
//        //realiza o update da cotação
//        if (cotacaoMoedaService.updateCotacaoMoedaForecast(cotacao)) {
//            return OUTCOME_COTACAO_MOEDA_RESEARCH;
//        } 
//        
//        return null;
//    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
//    public String prepareResearch() {
//        bean.reset();
//
//        // carrega as listas de entidades
//        loadMoedaList(moedaService.findMoedaAll());
//
//        this.setPeriodFilter();
//        bean.getFilter().setIndicadorTipo(Constants.COTACAO_MOEDA_TIPO_REAL);
//        
//        return OUTCOME_COTACAO_MOEDA_RESEARCH;
//    }
    
    /**
     * Retorna para a página de busca.
     * 
     * @return retorna a página de busca
     */
//    public String backToSearch() {
//        findByFilter();
//        
//        return OUTCOME_COTACAO_MOEDA_RESEARCH;
//    }

    /**
     * Seta o range da datas do filtro. - Data inicial = (Data de hoje) - (1
     * mês) - Data final = (Data corrente) + (3 meses)
     */
    private void setPeriodFilter() {
        Date date = new Date();

        if (bean.getDataDiaBeg() == null) {
            bean.setDataDiaBeg(DateUtils.addMonths(date, -1));
        }
        if (bean.getDataDiaEnd() == null) {
            bean.setDataDiaEnd(DateUtils.addMonths(date, 2));
        }

    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
//    public void findByFilter() {
//        boolean isValidDate = false;
//
//        Date dataDiaBeg = bean.getDataDiaBeg();
//        Date dataDiaEnd = bean.getDataDiaEnd();
//        if (dataDiaBeg != null && dataDiaEnd != null) {
//            if (DateUtil.validateValidityDate(dataDiaBeg, dataDiaEnd, Boolean
//                    .valueOf(false))) {
//                isValidDate = true;
//            }
//        }
//
//        if (isValidDate) {
//            CotacaoMoeda filter = bean.getFilter();
//
//            Long codigoMoeda = bean.getMoedaMap().get(
//                    filter.getMoeda().getNomeMoeda());
//            if (codigoMoeda != null) {
//                filter.getMoeda().setCodigoMoeda(codigoMoeda);
//            } else {
//                filter.setMoeda(null);
//            }
//
//            // realiza a busca pela CotacaoMoeda
//            bean.setResultList(cotacaoMoedaService.findCotacaoMoedaByFilter(
//                    filter, dataDiaBeg, dataDiaEnd));
//
//            // se nenhum resultado encontrado
//            if (bean.getResultList().isEmpty()) {
//                Messages.showWarning("findByFilter",
//                        Constants.DEFAULT_MSG_WARNG_NO_RESULT);
//            }
//        }
//    }

    /**
     * Popula a lista de Moeda para combobox.
     * 
     * @param pMoedaList
     *            lista de Moeda.
     * 
     */
//    private void loadMoedaList(final List<Moeda> pMoedaList) {
//
//        Map<String, Long> moedaMap = new HashMap<String, Long>();
//        List<String> moedaList = new ArrayList<String>();
//
//        for (Moeda moeda : pMoedaList) {
//            moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
//            moedaList.add(moeda.getNomeMoeda());
//        }
//        bean.setMoedaMap(moedaMap);
//        bean.setMoedaList(moedaList);
//    }

    /**
     * Verifica se o valor do atributo Moeda é valido. Se o valor não é nulo e
     * existe no moedaMap, então o valor é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
//    public void validateMoeda(final FacesContext context,
//            final UIComponent component, final Object value) {
//
//        Long codigoMoeda = null;
//        String strValue = (String) value;
//
//        if ((strValue != null) && (!"".equals(strValue))) {
//            codigoMoeda = bean.getMoedaMap().get(strValue);
//            if (codigoMoeda == null) {
//                String label = (String) component.getAttributes().get("label");
//                throw new ValidatorException(Messages.getMessageError(
//                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
//            }
//        }
//    }

}