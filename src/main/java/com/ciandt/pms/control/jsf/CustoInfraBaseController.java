package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.ICustoInfraBaseService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.control.jsf.bean.CustoInfraBaseBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CustoInfraBase;
import com.ciandt.pms.util.DateUtil;


/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_FINANCIAL" })
public class CustoInfraBaseController {

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_CUSTO_INFRA_BASE_MANAGE = "custoInfraBase_manage";

    /**
     * Instancia do servico.
     */
    @Autowired
    private ICustoInfraBaseService custoInfraBaseService;

    /**
     * Instancia do servico.
     */
    @Autowired
    private ICidadeBaseService cidadeBaseService;
    
    /**
     * Instancia do servico.
     */
    @Autowired
    private IModuloService moduloService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private CustoInfraBaseBean bean = null;

    /**
     * @return the bean
     */
    public CustoInfraBaseBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final CustoInfraBaseBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();
        
        // guarda a data do HistoryLock
        bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());

        return prepareManage();
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareManage() {
        bean.resetTo();
        loadCidadeBaseList(cidadeBaseService.findCidadeBaseAllActive());

        findByFilter();

        return OUTCOME_CUSTO_INFRA_BASE_MANAGE;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void create() {
        CustoInfraBase custoInfraBase = bean.getTo();
        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());
        custoInfraBase.setDataMes(dataMes);

        // verifica o History Lock. Se startDate não for válido, dá mensagem de
        // erro
        if (!moduloService.verifyHistoryLock(dataMes, Boolean.valueOf(true), Boolean.FALSE)) {
            return;
        }

        String nomeCidadeBase = custoInfraBase.getCidadeBase()
                .getNomeCidadeBase();
        if (!StringUtils.isEmpty(nomeCidadeBase)) {
            Long codigoCidadeBase = bean.getCidadeBaseMap().get(nomeCidadeBase);
            custoInfraBase.setCidadeBase(cidadeBaseService
                    .findCidadeBaseById(codigoCidadeBase));
        }

        // busca o CustoInfraBase pela dataMes e CidadeBase
        CustoInfraBase cibByDateAndCidade = custoInfraBaseService
                .findCustoInfBaseByDateAndCidadeBase(dataMes, custoInfraBase
                        .getCidadeBase());
        // se nao existir, insere normalmente, caso contrario msg de erro
        if (cibByDateAndCidade == null) {
            custoInfraBaseService.createCustoInfraBase(custoInfraBase);

            Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_CUSTO_INFRA_BASE);
            bean.resetTo();

            findByFilter();
        } else {
            Messages.showError("create",
                    Constants.MSG_ERROR_ADD_CUSTO_INFRA_BASE_PERIOD);
        }
    }

    /**
     * Executa um update da entidade.
     * 
     */
    public void update() {
        custoInfraBaseService.updateCustoInfraBase(bean.getTo());

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_CUSTO_INFRA_BASE);
        bean.resetTo();
        findByFilter();
    }

    /**
     * Executa um update da entidade.
     * 
     */
    public void updateList() {
        custoInfraBaseService.updateCustoInfraBaseList(bean.getResultList());

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_CUSTO_INFRA_BASE);
        bean.resetTo();
        findByFilter();
    }

    /**
     * Deleta uma entidade.
     * 
     */
    public void remove() {
        // verifica o History Lock. Se startDate não for válido, dá mensagem de
        // erro
        if (!moduloService.verifyHistoryLock(bean.getTo().getDataMes(), Boolean
                .valueOf(true), Boolean.FALSE)) {
            return;
        }
        
        custoInfraBaseService
                .removeCustoInfraBase(custoInfraBaseService
                        .findCustoInfraBaseById(bean.getTo()
                                .getCodigoCustoInfraBase()));
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_CUSTO_INFRA_BASE);
        bean.resetTo();
        findByFilter();
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        bean.setTo(custoInfraBaseService.findCustoInfraBaseById(id));
        if (bean.getTo() == null
                || bean.getTo().getCodigoCustoInfraBase() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        CustoInfraBase filter = bean.getFilter();

        Long codigoCidadeBase = Long.valueOf(0);
        CidadeBase cidadeBase = filter.getCidadeBase();
        String nomeCidadeBase = cidadeBase.getNomeCidadeBase();
        if (!StringUtils.isEmpty(nomeCidadeBase)) {
            codigoCidadeBase = bean.getCidadeBaseMap().get(nomeCidadeBase);
        }
        cidadeBase.setCodigoCidadeBase(codigoCidadeBase);

        bean.setResultList(custoInfraBaseService
                .findCustoInfraBaseByFilter(filter));

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    /**
     * Popula a lista de Cidade Base para combobox de base.
     * 
     * @param bases
     *            lista de CidadeBase.
     * 
     */
    private void loadCidadeBaseList(final List<CidadeBase> bases) {
        Map<String, Long> cidadeMap = new HashMap<String, Long>();
        List<String> cidadeList = new ArrayList<String>();

        for (CidadeBase base : bases) {
            cidadeMap.put(base.getNomeCidadeBase(), base.getCodigoCidadeBase());
            cidadeList.add(base.getNomeCidadeBase());
        }
        bean.setCidadeBaseMap(cidadeMap);
        bean.setCidadeBaseList(cidadeList);
    }

    /**
     * Popula o combobox do ContratoPratica de acordo com o Cliente selecionado.
     * 
     * @param e
     *            - evento de mudança
     */
    public void selectPatternCurrency(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null) {
            Long codigoCidadeBase = bean.getCidadeBaseMap().get(value);
            CidadeBase cidadeBase = null;

            // se o CidadeBase existir
            if (codigoCidadeBase != null) {
                cidadeBase = cidadeBaseService
                        .findCidadeBaseById(codigoCidadeBase);
                bean.setPatternCurrency(custoInfraBaseService
                        .getCurrency(cidadeBase.getMoeda()));
            }
        }
    }

    /**
     * Cancela a atualização de um CustoInfraBase.
     */
    public void cancel() {
        bean.resetTo();
    }

}