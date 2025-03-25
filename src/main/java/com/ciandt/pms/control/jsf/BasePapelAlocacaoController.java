package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.BasePapelAlocacaoBean;
import com.ciandt.pms.control.jsf.bean.CustoBasePapelAlocacaoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.*;
import com.ciandt.pms.util.DateUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.math.BigDecimal;
import java.util.*;


/**
 * Define o Controller da entidade.
 * 
 * @author cmantovani
 * @since 13/07/2011
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.RESOURCE_POSITION:ED", "BOF.RESOURCE_POSITION:VW"})
public class BasePapelAlocacaoController {

    /*********** OUTCOMES **************************/

    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_BASE_PAPEL_ALOCACAO_ADD = "basePapelAlocacao_add";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_BASE_PAPEL_ALOCACAO_RESEARCH = "basePapelAlocacao_research";

    /** outcome tela manage da entidade. */
    private static final String OUTCOME_BASE_PAPEL_ALOCACAO_EDIT = "basePapelAlocacao_edit";

    /** outcome tela view da entidade. */
    private static final String OUTCOME_BASE_PAPEL_ALOCACAO_VIEW = "basePapelAlocacao_view";

    /*********** SERVICES **************************/

    /** Instancia do servico - BasePapelAlocacao. */
    @Autowired
    private IBasePapelAlocacaoService basePapelAlocacaoService;

    /** Instancia do servico - BasePapelAlocacao. */
    @Autowired
    private ICustoBasePapelAlocacaoService custoBasePapelAlocacaoService;

    /** Instancia do servico - PapelAlocacao. */
    @Autowired
    private IPapelAlocacaoService papelAlocacaoService;

    /** Instancia do serviço da entidade CidadeBase. */
    @Autowired
    private ICidadeBaseService cidadeBaseService;

    /** Instancia do serviço da entidade Modulo. */
    @Autowired
    private IModuloService moduloService;

    @Autowired
    private IEmpresaService empresaService;

    /*********** BEANS **************************/

    /** Instancia do bean. */
    @Autowired
    private BasePapelAlocacaoBean bean = null;

    /** Instancia do bean. */
    @Autowired
    private CustoBasePapelAlocacaoBean custoBasePapelAlocacaoBean = null;

    /********************************************/

    /**
     * @return the bean
     */
    public BasePapelAlocacaoBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final BasePapelAlocacaoBean bean) {
        this.bean = bean;
    }

    /**
     * @return the custoBasePapelAlocacaoBean
     */
    public CustoBasePapelAlocacaoBean getCustoBasePapelAlocacaoBean() {
        return custoBasePapelAlocacaoBean;
    }

    /**
     * @param custoBasePapelAlocacaoBean
     *            the custoBasePapelAlocacaoBean to set
     */
    public void setCustoBasePapelAlocacaoBean(
            final CustoBasePapelAlocacaoBean custoBasePapelAlocacaoBean) {
        this.custoBasePapelAlocacaoBean = custoBasePapelAlocacaoBean;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();

        this.loadPapelAlocacaoList(papelAlocacaoService
                .findPapelAlocacaoAllActive());

        this.loadEmpresaList(empresaService.findWithAssociatedPosition());

        this.loadCidadeBaseList(new ArrayList<CidadeBase>());

        return OUTCOME_BASE_PAPEL_ALOCACAO_RESEARCH;
    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareCreate() {
        // limpa os dados do formulário
        bean.reset();
        // marca como modo de inserção
        bean.setIsUpdate(Boolean.valueOf(false));
        bean.setIsEdit(Boolean.valueOf(false));

        // guarda a data do HistoryLock
        custoBasePapelAlocacaoBean.setHistoryLockDate(moduloService
                .getClosingDateHistoryLock());

        this.loadPapelAlocacaoList(papelAlocacaoService
                .findPapelAlocacaoAllActive());

        this.loadEmpresaList(empresaService.findWithAssociatedPosition());

        return OUTCOME_BASE_PAPEL_ALOCACAO_ADD;
    }

    /**
     * Insere uma entidade.
     * 
     * @return pagina de destino
     */
    public String create() {
        // popula a BasePapelAlocacao
        BasePapelAlocacao basePapelAlocacao = bean.getTo();

        basePapelAlocacao = populaBasePapelAlocacao(basePapelAlocacao);

        CustoBasePapelAlocacao custoBasePapelAlocacao =
                populaCustoBasePapelAlocacao();

        if (custoBasePapelAlocacao == null) {
            return null;
        }

        if (!this.exists(basePapelAlocacao)) {

            // cria a BasePapelAlocacao
            basePapelAlocacaoService.createBasePapelAlocacao(basePapelAlocacao);

            bean.setTo(basePapelAlocacao);

            custoBasePapelAlocacao.setBasePapelAlocacao(basePapelAlocacao);

            custoBasePapelAlocacaoService
                    .createCustoBasePapelAlocacao(custoBasePapelAlocacao);

            // limpa o custoBasePapelAlocacao no bean
            custoBasePapelAlocacaoBean.reset();
            // atualiza a lista de custoBasePapelAlocacao na BasePapelAlocacao
            // corrente
            findCustoByIdBasePapelAlocacao(bean.getTo()
                    .getCodigoBasePapelAlocacao());

            // mensagem de sucesso
            Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_BASE_PAPEL_ALOCACAO);

            bean
                    .setCurrentRowId(basePapelAlocacao
                            .getCodigoBasePapelAlocacao());

            // carrega a tela de gerenciamento (alteração da BasePapelAlocacao)
            return prepareEdit();

        } else {
            Messages.showWarning("create",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_BASE_PAPEL_ALOCACAO);

            return null;
        }
    }

    /**
     * Verifica se a entidade ja existe na base de dados.
     * 
     * @param basePapelAlocacao
     *            - entidade a ser consultada
     * @return true se ja existe
     */
    private Boolean exists(final BasePapelAlocacao basePapelAlocacao) {

        BasePapelAlocacao bpa =
                basePapelAlocacaoService
                        .findBasePapelAlocacaoByBasePapelAlocacao(basePapelAlocacao);

        if (bpa == null) {
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareEdit() {

        custoBasePapelAlocacaoBean.reset();

        // marca como modo de alterção
        bean.setIsUpdate(Boolean.valueOf(true));
        
        // marca como mode de edicao
        bean.setIsEdit(Boolean.valueOf(true));

        // busca a entidade a ser alterada pelo id
        findCustoByIdBasePapelAlocacao(bean.getCurrentRowId());

        // pega a entidade BasePapelAlocacao
        final BasePapelAlocacao basePapelAlocacao = custoBasePapelAlocacaoBean.getTo().getBasePapelAlocacao();

        // se existir o BasePapelAlocacao redireciona para a
        // tela de gerenciamento
        if (basePapelAlocacao != null) {

            this.loadPapelAlocacaoList(papelAlocacaoService
                    .findPapelAlocacaoAllActive());

            this.loadEmpresaList(empresaService.findWithAssociatedPosition());

            if(basePapelAlocacao.getCidadeBase() != null) {
                CidadeBase cidadeBase = cidadeBaseService.findCidadeBaseById(basePapelAlocacao.getCidadeBase().getCodigoCidadeBase());
                if (cidadeBase != null) {
                    bean.getTo().setCidadeBase(cidadeBase);
                }
            }

            custoBasePapelAlocacaoBean.setNomeEmpresaMatriz(getEmpresaMatrizByCodigoEmpresaERP(basePapelAlocacao.getCodigoEmpresaERP()));

            prepareConfigure();

            return OUTCOME_BASE_PAPEL_ALOCACAO_EDIT;
            // se nao existir na base de dados
        } else {
            Messages.showError("prepareEdit",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_BASE_PAPEL_ALOCACAO);
        }

        // retorna null caso não seja validado
        return null;
    }

    public void prepareEditOneCost() {
        custoBasePapelAlocacaoBean.setTo(custoBasePapelAlocacaoBean.getToEdit().clone());
        custoBasePapelAlocacaoBean.setIsUpdate(Boolean.TRUE);
    }

    public void cancelEditOneCost() {
        BasePapelAlocacao basePapelAlocacao = custoBasePapelAlocacaoBean.getTo().getBasePapelAlocacao();
        custoBasePapelAlocacaoBean.resetTo();
        custoBasePapelAlocacaoBean.resetToEdit();
        custoBasePapelAlocacaoBean.getTo().setBasePapelAlocacao(basePapelAlocacao);
    }

    public void editOneCost() {
        CustoBasePapelAlocacao toUpdate = custoBasePapelAlocacaoBean.getTo();
        BasePapelAlocacao basePapelAlocacao = basePapelAlocacaoService.findBasePapelAlocacaoById(
                toUpdate.getBasePapelAlocacao().getCodigoBasePapelAlocacao());

        if (verifyAddCustoBasePapelAlocacaoFields(toUpdate.getDataInicio())) {
            List<CustoBasePapelAlocacao> toUpdateList = new ArrayList<CustoBasePapelAlocacao>();

            toUpdate.setBasePapelAlocacao(basePapelAlocacao);
            toUpdate.setValorBasePapelAlocacao(this.getTotalTceValue(toUpdate));
            toUpdateList.add(toUpdate);

            if (custoBasePapelAlocacaoBean.getUpdateAllMonthsAhead()) {
                List<CustoBasePapelAlocacao> toUpdateAllAhead =
                        custoBasePapelAlocacaoService.findCustoBasePapelAlocacaoByStartDateGreaterThan(
                                toUpdate.getBasePapelAlocacao().getCodigoBasePapelAlocacao(), toUpdate.getDataInicio());

                if (toUpdateAllAhead != null && !toUpdateAllAhead.isEmpty()) {
                    for (CustoBasePapelAlocacao custo : toUpdateAllAhead) {
                        CustoBasePapelAlocacao cloned = custo.clone();
                        cloned.setBasePapelAlocacao(basePapelAlocacao);
                        cloned.setValorBasePapelAlocacao(toUpdate.getValorBasePapelAlocacao());
                        cloned.setValorSalario(toUpdate.getValorSalario());
                        cloned.setValorBeneficios(toUpdate.getValorBeneficios());
                        cloned.setValorImpostos(toUpdate.getValorImpostos());
                        cloned.setValorProvisionamento(toUpdate.getValorProvisionamento());

                        toUpdateList.add(cloned);
                    }
                }
            }

            try {
                for (CustoBasePapelAlocacao custoBasePapelAlocacao : toUpdateList) {
                    custoBasePapelAlocacaoService.updateCustoBasePapelAlocacao(custoBasePapelAlocacao);
                }
            } catch (Exception ex) {
                Messages.showError("createCustoBasePapelAlocacao",
                        Constants.MSG_ERROR_CUSTO_BASE_PAPEL_ALOCACAO_UPDATE);
                this.prepareEdit();
                return;
            }

            Messages.showSucess("createCustoBasePapelAlocacao",
                    Constants.DEFAULT_MSG_UPDATE_SUCCESS,
                    Constants.ENTITY_NAME_CUSTO_BASE_PAPEL_ALOCACAO);

            this.prepareEdit();
        }
    }

    public String getEmpresaMatrizByCodigoEmpresaERP(Long codigoEmpresaERP){

        for(Map.Entry<String, Long> entry : bean.getEmpresaMap().entrySet()) {

           if(codigoEmpresaERP.equals(entry.getValue())){
               return entry.getKey();
           }
        }
        return null;
    }

    /**
     * Prepara a tela de edicao da entidade.
     */
    private void prepareConfigure() {

        // guarda a data do HistoryLock
        custoBasePapelAlocacaoBean.setHistoryLockDate(moduloService
                .getClosingDateHistoryLock());

        BasePapelAlocacao basePapelAlocacao =
                basePapelAlocacaoService.findBasePapelAlocacaoById(custoBasePapelAlocacaoBean.getTo()
                        .getBasePapelAlocacao().getCodigoBasePapelAlocacao());

        this.findCustoByIdBasePapelAlocacao(basePapelAlocacao
                .getCodigoBasePapelAlocacao());

    }

    /**
     * Cria uma entidade do tipo CustoBasePapelAlocacao.
     */
    public void createCustoBasePapelAlocacao() {
        String validityMonth = custoBasePapelAlocacaoBean.getValidityMonthBeg();
        String validityYear = custoBasePapelAlocacaoBean.getValidityYearBeg();
        Date startDate = DateUtil.getDate(validityMonth, validityYear);

        if (!verifyAddCustoBasePapelAlocacaoFields(startDate)) {
            return;
        }

        CustoBasePapelAlocacao custoBasePapelAlocacao =
                custoBasePapelAlocacaoBean.getTo();

        BasePapelAlocacao basePapelAlocacao =
                basePapelAlocacaoService.findBasePapelAlocacaoById(custoBasePapelAlocacaoBean.getTo().getBasePapelAlocacao()
                        .getCodigoBasePapelAlocacao());

        custoBasePapelAlocacao.setDataInicio(startDate);
        custoBasePapelAlocacao.setBasePapelAlocacao(basePapelAlocacao);
        custoBasePapelAlocacao.setValorBasePapelAlocacao(this.getTotalTceValue(custoBasePapelAlocacao));

        // cria o custoBasePapelAlocacao
        if (custoBasePapelAlocacaoService
                .createCustoBasePapelAlocacao(custoBasePapelAlocacao)) {
            // mensagem de sucesso
            Messages.showSucess("createCustoBasePapelAlocacao",
                    Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_CUSTO_BASE_PAPEL_ALOCACAO);

            // atualiza a lista de custoBasePapelAlocacao na BasePapelAlocacao
            // corrente
            this.prepareEdit();
        }
    }

    private BigDecimal getTotalTceValue(CustoBasePapelAlocacao custoBasePapelAlocacao) {
        Double salary = custoBasePapelAlocacao.getValorSalario().doubleValue();
        Double benefits = custoBasePapelAlocacao.getValorBeneficios().doubleValue();
        Double taxes = custoBasePapelAlocacao.getValorImpostos().doubleValue();
        Double provisioning = custoBasePapelAlocacao.getValorProvisionamento().doubleValue();

        Double totalTceValue = salary + benefits + taxes + provisioning;

        return BigDecimal.valueOf(totalTceValue);
    }

    public Boolean verifyAddCustoBasePapelAlocacaoFields(Date startDate) {
        if (!moduloService.verifyHistoryLock(startDate, Boolean.TRUE, Boolean.FALSE)) {
            return Boolean.FALSE;
        }
        if (custoBasePapelAlocacaoBean.getTo().getValorSalario().doubleValue() <= 0){
            Messages.showError("verifySalary",
                    Constants.MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_SALARY_GREATER_THAN_ZERO);
            return Boolean.FALSE;
        }
        if (custoBasePapelAlocacaoBean.getTo().getValorBeneficios() == null) {
            custoBasePapelAlocacaoBean.getTo().setValorBeneficios(BigDecimal.ZERO);
        } else if (custoBasePapelAlocacaoBean.getTo().getValorBeneficios().doubleValue() < 0) {
            Messages.showError("verifyBenefit",
                    Constants.MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_BENEFIT_NEGATIVE);
            return Boolean.FALSE;
        }
        if (custoBasePapelAlocacaoBean.getTo().getValorImpostos() == null) {
            custoBasePapelAlocacaoBean.getTo().setValorImpostos(BigDecimal.ZERO);
        } else if (custoBasePapelAlocacaoBean.getTo().getValorImpostos().doubleValue() < 0) {
            Messages.showError("verifyTax",
                    Constants.MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_TAX_NEGATIVE);
            return Boolean.FALSE;
        }
        if (custoBasePapelAlocacaoBean.getTo().getValorProvisionamento() == null) {
            custoBasePapelAlocacaoBean.getTo().setValorProvisionamento(BigDecimal.ZERO);
        } else if (custoBasePapelAlocacaoBean.getTo().getValorProvisionamento().doubleValue() < 0) {
            Messages.showError("verifyProvisioning",
                    Constants.MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_PROVISIONING_NEGATIVE);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Remove uma entidade do tipo CustoBasePapelAlocacao.
     */
    public void removeCustoBasePapelAlocacao() {
        CustoBasePapelAlocacao custoBasePapelAlocacao =
                custoBasePapelAlocacaoBean.getTo();

        // remove o TcePapelAlocacao
        if (custoBasePapelAlocacaoService
                .removeCustoBasePapelAlocacao(custoBasePapelAlocacaoService
                        .findCustoBasePapelAlocacaoById(custoBasePapelAlocacao
                                .getCodigoCustoBasePapelAloc()))) {
            // mensagem de sucesso
            Messages.showSucess("removeCustoBasePapelAlocacao",
                    Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                    Constants.ENTITY_NAME_CUSTO_BASE_PAPEL_ALOCACAO);

            // limpa o bean do CustoBasePapelAlocacao
            custoBasePapelAlocacaoBean.reset();
            // atualiza a lista de CustoBasePapelAlocacao no BasePapelAlocacao
            // corrente
            findCustoByIdBasePapelAlocacao(bean.getTo()
                    .getCodigoBasePapelAlocacao());
        }
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        BasePapelAlocacao basePapelAlocacao = bean.getTo();

        basePapelAlocacao = populaBasePapelAlocacao(basePapelAlocacao);

        if (!this.exists(basePapelAlocacao)) {

            basePapelAlocacaoService.updateBasePapelAlocacao(basePapelAlocacao);

            Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_BASE_PAPEL_ALOCACAO);

            bean
                    .setCurrentRowId(basePapelAlocacao
                            .getCodigoBasePapelAlocacao());

            return prepareEdit();

        } else {
            Messages.showWarning("create",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_BASE_PAPEL_ALOCACAO);

            return null;
        }
    }

    /**
     * Prepara a tela de remocao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareRemove() {
        // marca o modo de remoção como false (view) para que a tela não mostre
        // o botão de excluir
        bean.setIsRemove(Boolean.valueOf(true));

        // carrega a BasePapelAlocacao em modo de visualização
        if (loadBasePapelAlocacaoView()) {
            return OUTCOME_BASE_PAPEL_ALOCACAO_VIEW;
        }

        return null;
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {
        try {
            basePapelAlocacaoService
                    .removeBasePapelAlocacao(basePapelAlocacaoService
                            .findBasePapelAlocacaoById(bean.getTo()
                                    .getCodigoBasePapelAlocacao()));
        } catch (DataIntegrityViolationException e) {
            Messages.showError("remove",
                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_BASE_PAPEL_ALOCACAO);
            return null;
        }

        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_BASE_PAPEL_ALOCACAO);
        bean.resetTo();
        findByFilter();
        return OUTCOME_BASE_PAPEL_ALOCACAO_RESEARCH;
    }

    /**
     * Prepara a tela de visualização da entidade.
     * 
     * @return retorna a pagina de visualização da BasePapelAlocacao
     */
    public String prepareView() {
        // marca o modo de remoção como false (view) para que a tela não mostre
        // o botão de excluir
        bean.setIsRemove(Boolean.valueOf(false));

        // carrega o PapelAlocacao em modo de visualização
        if (loadBasePapelAlocacaoView()) {

            return OUTCOME_BASE_PAPEL_ALOCACAO_VIEW;
        }

        return null;
    }

    /**
     * Carrega os conteudo de uma BasePapelAlocacao para exibir na tela.
     * 
     * @return retorna true se encontrou a BasePapelAlocacao corretamente
     */
    private Boolean loadBasePapelAlocacaoView() {

        // busca o PapelAlocacao corrente pelo código
        findCustoByIdBasePapelAlocacao(bean.getCurrentRowId());

        // pega a entidade PapelAlocacao
        BasePapelAlocacao basePapelAlocacao = custoBasePapelAlocacaoBean.getTo().getBasePapelAlocacao();

        // se existir o PapelAlocacao atribui a Moeda
        if (basePapelAlocacao != null ) {
            bean.getTo().setPapelAlocacao(basePapelAlocacao.getPapelAlocacao());
            bean.getTo().setCidadeBase(basePapelAlocacao.getCidadeBase());
            return Boolean.valueOf(true);
            // se nao existir na base de dados, mostra msg
        } else {
            Messages.showError("loadBasePapelAlocacaoView",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_BASE_PAPEL_ALOCACAO);
            return Boolean.valueOf(false);
        }
    }

    /**
     * Cancela a atualização de uma BasePapelAlocacao.
     * 
     * @return pagina de destino
     */
    public String cancelBasePapelAlocacao() {
        findByFilter();

        return OUTCOME_BASE_PAPEL_ALOCACAO_RESEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findCustoByIdBasePapelAlocacao(final Long id) {
        custoBasePapelAlocacaoBean.getTo().setBasePapelAlocacao(basePapelAlocacaoService.findBasePapelAlocacaoById(id));

        if (custoBasePapelAlocacaoBean.getTo().getBasePapelAlocacao() == null
                || custoBasePapelAlocacaoBean.getTo().getBasePapelAlocacao().getCodigoBasePapelAlocacao() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        List<CustoBasePapelAlocacao> custoBasePapelAlocacaoList =
            custoBasePapelAlocacaoService.findCustoBasePapelAlocacaoByBasePapelAlocacao(custoBasePapelAlocacaoBean.getTo().getBasePapelAlocacao());
        for (CustoBasePapelAlocacao cbpa : custoBasePapelAlocacaoList) {
            if (cbpa.getValorBasePapelAlocacao() == null) cbpa.setValorBasePapelAlocacao(BigDecimal.ZERO);
            if (cbpa.getValorSalario() == null) cbpa.setValorSalario(BigDecimal.ZERO);
            if (cbpa.getValorBeneficios() == null) cbpa.setValorBeneficios(BigDecimal.ZERO);
            if (cbpa.getValorImpostos() == null) cbpa.setValorImpostos(BigDecimal.ZERO);
            if (cbpa.getValorProvisionamento() == null) cbpa.setValorProvisionamento(BigDecimal.ZERO);
        }

        custoBasePapelAlocacaoBean.setResultList(custoBasePapelAlocacaoList);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        BasePapelAlocacao filter = bean.getFilter();

        Long codigoEmpresaERP = bean.getEmpresaMap().get(bean.getNomeEmpresaMatriz());

        filter.setCodigoEmpresaERP(codigoEmpresaERP);

        if (bean.getTo().getCidadeBase().getNomeCidadeBase().equals("")){
            filter.setCidadeBase(new CidadeBase());
        }
        else {
            filter.setCidadeBase(bean.getTo().getCidadeBase());
        }

        List<BasePapelAlocacao> resultList =
                basePapelAlocacaoService.findBasePapelAlocacaoByFilterFetch(filter);

        for (BasePapelAlocacao basePapelAlocacao : resultList) {
            basePapelAlocacao.setNomeEmpresa(
                    getEmpresaMatrizByCodigoEmpresaERP(basePapelAlocacao.getCodigoEmpresaERP()));
            CustoBasePapelAlocacao custoBasePapelAlocacao =
                    custoBasePapelAlocacaoService.findCustoBasePapelAlocacaoByBasePapelAndCurrentDate(basePapelAlocacao);
            Hibernate.initialize(basePapelAlocacao.getCidadeBase().getMoeda());

            if (custoBasePapelAlocacao != null) {
                basePapelAlocacao.setValorTCEVigente(custoBasePapelAlocacao.getValorBasePapelAlocacao());
            } else {
                basePapelAlocacao.setValorTCEVigente(BigDecimal.ZERO);
            }
        }

        bean.setResultList(resultList);

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    /**
     * Popula a lista de papelAlocacao para combobox.
     * 
     * @param papeisAlocacao
     *            lista de papelAlocacao.
     * 
     */
    private void loadPapelAlocacaoList(final List<PapelAlocacao> papeisAlocacao) {

        Map<String, Long> papelAlocacaoMap = new HashMap<String, Long>();
        List<String> papelAlocacaoList = new ArrayList<String>();

        for (PapelAlocacao papelAlocacao : papeisAlocacao) {
            papelAlocacaoMap.put(papelAlocacao.getNomePapelAlocacao(),
                    papelAlocacao.getCodigoPapelAlocacao());
            papelAlocacaoList.add(papelAlocacao.getNomePapelAlocacao());
        }

        bean.setPapelAlocacaoMap(papelAlocacaoMap);
        bean.setPapelAlocacaoList(papelAlocacaoList);
    }

    private void loadEmpresaList(final List<Empresa> empList) {

        Map<String, Long> empresaMap = new HashMap<String, Long>();
        List<String> empresaList = new ArrayList<String>();

        for (Empresa empresa : empList) {
            if(empresa.getEmpresa() == null && empresa.getCodigoEmpresaERP() != null) {
                empresaMap.put(empresa.getNomeEmpresa(), empresa.getCodigoEmpresaERP());
                empresaList.add(empresa.getNomeEmpresa());
            }
        }

        bean.setEmpresaMap(empresaMap);
        bean.setEmpresaList(empresaList);
    }

    /**
     * Popula a lista de cidadeBase para combobox.
     * 
     * @param cidadesBase
     *            lista de cidadeBase.
     * 
     */
    private void loadCidadeBaseList(final List<CidadeBase> cidadesBase) {

        Map<String, Long> cidadeBaseMap = new HashMap<String, Long>();
        List<String> cidadeBaseList = new ArrayList<String>();

        for (CidadeBase cidadeBase : cidadesBase) {
            cidadeBaseMap.put(cidadeBase.getNomeCidadeBase(), cidadeBase
                    .getCodigoCidadeBase());
            cidadeBaseList.add(cidadeBase.getNomeCidadeBase());
        }

        bean.setCidadeBaseMap(cidadeBaseMap);
        bean.setCidadeBaseList(cidadeBaseList);
    }

    /**
     * Popula a entidade com a cidadeBase e papelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade a ser populada
     * @return entidade populada
     */
    private BasePapelAlocacao populaBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao) {
        CidadeBase cidadeBase = new CidadeBase();
        PapelAlocacao papelAlocacao = new PapelAlocacao();

        Long codigoCidadeBase =
                bean.getCidadeBaseMap().get(
                        basePapelAlocacao.getCidadeBase().getNomeCidadeBase());
        if (codigoCidadeBase != null) {
            cidadeBase = cidadeBaseService.findCidadeBaseById(codigoCidadeBase);
        }

        Long codigoPapelAlocacao =
                bean.getPapelAlocacaoMap().get(
                        basePapelAlocacao.getPapelAlocacao()
                                .getNomePapelAlocacao());
        if (codigoPapelAlocacao != null) {
            papelAlocacao =
                    papelAlocacaoService
                            .findPapelAlocacaoById(codigoPapelAlocacao);
        }

        Long codigoEmpresaERP = bean.getEmpresaMap().get(bean.getNomeEmpresaMatriz());

        if(codigoEmpresaERP != null){
            basePapelAlocacao.setCodigoEmpresaERP(codigoEmpresaERP);
        }

        basePapelAlocacao.setCidadeBase(cidadeBase);
        basePapelAlocacao.setPapelAlocacao(papelAlocacao);

        return basePapelAlocacao;
    }

    /**
     * Popula a entidade.
     * 
     * @return entidade populada
     */
    private CustoBasePapelAlocacao populaCustoBasePapelAlocacao() {
        CustoBasePapelAlocacao custoBasePapelAlocacao =
                new CustoBasePapelAlocacao();

        String validityMonth = custoBasePapelAlocacaoBean.getValidityMonthBeg();
        String validityYear = custoBasePapelAlocacaoBean.getValidityYearBeg();
        Date startDate = DateUtil.getDate(validityMonth, validityYear);

        // verifica o History Lock. Se startDate não for válido, dA mensagem de
        // erro
        if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
            return null;
        }

        custoBasePapelAlocacao
                .setValorBasePapelAlocacao(custoBasePapelAlocacaoBean.getTo()
                        .getValorBasePapelAlocacao());

        custoBasePapelAlocacao.setDataInicio(startDate);

        return custoBasePapelAlocacao;
    }

    /**
     * Valida a cidadeBase selecionada.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     * 
     */
    public void validateCidadeBase(final FacesContext context,
            final UIComponent component, final Object value) {

        String nomeCidadeBase = (String) value;

        CidadeBase cidadeBase = new CidadeBase();
        Long codigoCidadeBase = bean.getCidadeBaseMap().get(nomeCidadeBase);
        if (codigoCidadeBase != null) {
            cidadeBase = cidadeBaseService.findCidadeBaseById(codigoCidadeBase);
        }
        bean.getTo().setCidadeBase(cidadeBase);
    }

    public void prepareCidadeBaseCombo(final ValueChangeEvent e) {

        String nomeEmpresaMatriz = (String) e.getNewValue();

        if (!nomeEmpresaMatriz.isEmpty()) {
            Long codigoEmpresaERP = bean.getEmpresaMap().get(nomeEmpresaMatriz);
            if (codigoEmpresaERP != null) {
                loadCidadeBaseList(cidadeBaseService.findActiveByEmpresa(codigoEmpresaERP));
                bean.getFilter().getCidadeBase().setNomeCidadeBase("");
                bean.getTo().setCidadeBase(new CidadeBase());
            }
        }
    }
}