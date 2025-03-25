package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.OrcDespesaCloneBean;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.*;

@Controller
@RolesAllowed({ "BUS.CLIENT.TRAVEL_BUDGET.EXTRA:ED", "BUS.COST_CENTER.TRAVEL_BUDGET:ED",
        "BOF.COST_CENTER.TRAVEL_BUDGET:ED", "BUS.CLIENT.TRAVEL_BUDGET.EXTRA:VW" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespesaCloneController {

    private static final String OUTCOME_CL_PAGE = "orc_despesa_cl_manage";

    private static final String OUTCOME_GC_PAGE = "orcamento_despesa_grupo_custo";

    private static final String STR_VALIDATE_CLOB = "validateCLobs";

    private static final String STR_CREATE_ORC_DESP_CL = "createOrcDespesaCl";

    @Autowired
    public OrcDespesaCloneBean bean;

    @Autowired
    private IOrcDespesaClService orcDespesaClService;

    @Autowired
    private IOrcDespesaGcService orcDespesaGcService;

    @Autowired
    private IMoedaService moedaService;

    @Autowired
    private IContratoPraticaService contratoPraticaService;

    public OrcDespesaCloneBean getBean() {
        return bean;
    }

    public void setBean(OrcDespesaCloneBean bean) {
        this.bean = bean;
    }

    @PostConstruct
    public void init() {
        String code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigoOrcaDespesaClonable");
        if (code != null) {
            bean.resetTo();
            OrcamentoDespesa clonable = new OrcamentoDespesa();
            clonable.setCodigoOrcaDespesa(Long.parseLong(code));

            OrcDespesaCl cl = orcDespesaClService.findByOrcamentoDespesa(clonable);
            if (cl != null) {
                bean.setToCl(cl);
                bean.setOrcDespesaToCloneName(cl.getOrcamentoDespesa().getNomeOrcDespesa());
                prepareCreateOrcDespesaCl();
            } else {
                OrcDespesaGc gc = orcDespesaGcService.findByOrcDespesa(clonable);
                bean.setToGc(gc);
                bean.setOrcDespesaToCloneName(gc.getOrcamentoDespesa().getNomeOrcDespesa());
                prepareCreateOrcDespesaGc();
            }
        }
    }

    private void loadMoedaList() {
        List<Moeda> moedas = moedaService.findMoedaAll();
        Map<String, Long> mapMoeda = new HashMap<String, Long>();
        List<String> listResult = new ArrayList<String>();

        for (Moeda moeda : moedas) {
            mapMoeda.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            listResult.add(moeda.getNomeMoeda());
        }

        bean.setMoedaStringList(listResult);
        bean.setMoedaMap(mapMoeda);
    }

    private void loadTiposFaturaList() {
        Map<String, String> mapTipoFatura = new HashMap<String, String>();
        List<String> listResult = new ArrayList<String>();

        mapTipoFatura.put(BundleUtil
                .getBundle(Constants.ORCAMENTO_DESPESA_NOTA_FISCAL), BundleUtil
                .getBundle(Constants.ORCAMENTO_DESPESA_NOTA_FISCAL_VALOR));
        mapTipoFatura.put(BundleUtil
                .getBundle(Constants.ORCAMENTO_DESPESA_NOTA_DEBITO), BundleUtil
                .getBundle(Constants.ORCAMENTO_DESPESA_NOTA_DEBITO_VALOR));

        listResult.add(BundleUtil
                .getBundle(Constants.ORCAMENTO_DESPESA_NOTA_FISCAL));
        listResult.add(BundleUtil
                .getBundle(Constants.ORCAMENTO_DESPESA_NOTA_DEBITO));

        bean.setListaTipoFaturaCombobox(listResult);
        bean.setTipoFaturaMap(mapTipoFatura);
    }

    private void loadClobPickListItems( ) {
        List<ContratoPratica> contratoPraticaList =  contratoPraticaService.findContratoPraticaByCliente(bean.getToCl().getCliente());
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();

        for (ContratoPratica cp : contratoPraticaList) {
            String reimbursableAcronym = cp.getIndicadorReembolsavel() != null && cp.getIndicadorReembolsavel().equalsIgnoreCase("Y") ? Constants.ORCAMENTO_DESPESA_REEMBOLSAVEL : Constants.ORCAMENTO_DESPESA_NAO_REEMBOLSAVEL;
            String reimbursableText = cp.getIndicadorReembolsavel() != null ? " (" + reimbursableAcronym + ")" : "";
            selectItemList.add(new SelectItem(cp.getCodigoContratoPratica(), cp.getNomeContratoPratica().concat(reimbursableText)));
        }
        bean.setContratoPraticaPickList(selectItemList);

        ArrayList<Long> grantedClobSelected = new ArrayList<Long>();
        for(ContratoPraticaOrcDespCl cpOrcDespCl : bean.getToCl().getContratoPraticaOrcDespCls()) {
            grantedClobSelected.add(cpOrcDespCl.getContratoPratica().getCodigoContratoPratica());
        }
        bean.setGrantedCLobPickList(grantedClobSelected.toArray(new Long[grantedClobSelected.size()]));
    }

    private void prepareCreateOrcDespesaCl() {
        this.loadMoedaList();
        this.loadTiposFaturaList();
        this.loadClobPickListItems();
        bean.setMoedaSelecionda(bean.getToCl().getOrcamentoDespesa().getMoeda().getNomeMoeda());
        bean.setIsOrcDespesaClReembolsavel(bean.getToCl().getIsReembolsavel());
        bean.getTo().setNomeOrcDespesa("");
        bean.getTo().setValorOrcado(bean.getToCl().getOrcamentoDespesa().getValorOrcado());
    }

    private void prepareCreateOrcDespesaGc() {
        this.loadMoedaList();
        bean.setMoedaSelecionda(bean.getToGc().getOrcamentoDespesa().getMoeda().getNomeMoeda());
        bean.getTo().setValorOrcado(bean.getToGc().getOrcamentoDespesa().getValorOrcado());
        bean.getTo().setNomeOrcDespesa("");
    }

    public void createOrcDespesaCloned() {
        OrcDespesaCl newOrcClClone;
        OrcDespesaGc newOrcGcClone;
        try {
            if (Boolean.TRUE.equals(validateFields())) {

                if (Boolean.TRUE.equals(bean.getIsOrcDespesaCl()) && Boolean.TRUE.equals(this.validateCLobs())) {
                    newOrcClClone = this.getOrcDespesaClToPersist();

                    if (Boolean.TRUE.equals(validateLimitAndExtraBudgetAndPeriod(newOrcClClone))) {
                        orcDespesaClService.clone(bean.getToCl().getOrcamentoDespesa(), newOrcClClone, bean.getGrantedCLobPickList(), bean.getIsCopyFollow(), bean.getIsCopyWhiteList(), bean.getIsCopyWhiteListDelegation());
                        prepareCreateOrcDespesaCl();
                    }
                } else {
                    newOrcGcClone = this.getOrcDespesaGcToPersist();
                    orcDespesaGcService.clone(bean.getToGc().getOrcamentoDespesa(), newOrcGcClone, bean.getIsCopyFollow(), bean.getIsCopyWhiteList(), bean.getIsCopyWhiteListDelegation());
                    prepareCreateOrcDespesaGc();
                }
                Messages.showSucess(STR_CREATE_ORC_DESP_CL, Constants.GENERIC_MSG_SUCESS_CLONE);
            }
        } catch (IntegrityConstraintException e) {
            Messages.showWarning(STR_CREATE_ORC_DESP_CL, Constants.TRAVEL_BUDGET_ALREADY_EXISTS);
        } catch (Exception ex) {
            Messages.showError(STR_CREATE_ORC_DESP_CL, Constants.GENERIC_ERROR);
        }
    }

    private OrcDespesaCl getOrcDespesaClToPersist() {
        OrcDespesaCl orcDespesaCl = new OrcDespesaCl();
        orcDespesaCl.setCliente(bean.getToCl().getCliente());
        orcDespesaCl.setIndicadorReembolsavel(bean.getIsOrcDespesaClReembolsavel() ? Constants.REEMBOLSAVEL : Constants.NOT_REEMBOLSAVEL);
        orcDespesaCl.setOrcamentoDespesa(this.getOrcamentoDespesaToPersist());
        orcDespesaCl.setIndicadorDeleteLogico(Constants.NO);
        orcDespesaCl.setIndicadorTipoFatura(bean.getToCl().getIndicadorTipoFatura());

        return orcDespesaCl;
    }

    private OrcDespesaGc getOrcDespesaGcToPersist() {
        OrcDespesaGc orcDespesaGc = new OrcDespesaGc();
        orcDespesaGc.setGrupoCusto(bean.getToGc().getGrupoCusto());
        orcDespesaGc.setOrcamentoDespesa(getOrcamentoDespesaToPersist());
        orcDespesaGc.setIndicadorDeleteLogico(Constants.NO);
        orcDespesaGc.setIndicadorWlOnlyAsBoolean(bean.getToGc().getIndicadorWlOnlyAsBoolean());
        orcDespesaGc.setIndicadorWlOnly(bean.getToGc().getIndicadorWlOnly());

        return orcDespesaGc;
    }

    private OrcamentoDespesa getOrcamentoDespesaToPersist() {
        Moeda moeda = this.getMoedaSelecionada();

        OrcamentoDespesa orcamentoDespesa = bean.getTo();
        orcamentoDespesa.setCodigoOrcaDespesa(null);
        orcamentoDespesa.setMoeda(moeda);
        orcamentoDespesa.setLoginCriador(LoginUtil.getLoggedUsername());
        orcamentoDespesa.setIndicadorAtivo(Constants.SIM);
        orcamentoDespesa.setIndicadorSync(Constants.NO);
        orcamentoDespesa.setIndicadorDeleteLogico(Constants.NO);

        if (Boolean.TRUE.equals(bean.getIsOrcDespesaCl())) {
            orcamentoDespesa.setTpOrcDesp(Constants.TRAVEL_BUDGET_CLIENT_TYPE);
            orcamentoDespesa.setTbPurpose(Constants.TB_PURPOSE_GENERAL);
        } else {
            orcamentoDespesa.setTpOrcDesp(Constants.TRAVEL_BUDGET_COST_GROUP_TYPE);
            orcamentoDespesa.setTbPurpose(bean.getToGc().getOrcamentoDespesa().getTbPurpose());
        }

        return orcamentoDespesa;
    }

    private Moeda getMoedaSelecionada() {
        Long codigoMoeda = bean.getMoedaMap().get(bean.getMoedaSelecionda());
        return moedaService.findMoedaById(codigoMoeda);
    }

    public String backToPreviousPage() {
        Boolean isOrcDespesaCl = bean.getIsOrcDespesaCl();
        bean.reset();
        if (Boolean.TRUE.equals(isOrcDespesaCl)) {
            return OUTCOME_CL_PAGE;
        } else {
            return OUTCOME_GC_PAGE;
        }
    }

    private Boolean validateFields() {
        if (Boolean.FALSE.equals(this.isInputDateValid()) || Boolean.FALSE.equals(this.isValidOrcamentoDespesaName(bean.getTo().getNomeOrcDespesa()))) {
            return false;
        }
        if (bean.getTo().getValorOrcado().doubleValue() <= 0) {
            Messages.showError(STR_CREATE_ORC_DESP_CL, Constants.BUNDLE_VALUE_IS_LESS_THAN_ALLOWABLE_MINIMUM);
            return false;
        }
        return true;
    }

    private Boolean validateLimitAndExtraBudgetAndPeriod(OrcDespesaCl orcDespesaCl) {

        orcDespesaCl.setIndicadorOrcamentoExtra(Constants.NO);
        boolean hasBudget = orcDespesaClService.hasBudgetToCreateTravelBudget(orcDespesaCl.getOrcamentoDespesa(), bean.getGrantedCLobPickList());
        if ((!bean.getIsOrcDespesaClReembolsavel()) && (!hasBudget)) {

            if (Boolean.TRUE.equals(orcDespesaClService.isVp(LoginUtil.getLoggedUsername())) && (bean.getIsOrcDespesaExtra())) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(orcDespesaCl.getOrcamentoDespesa().getDataInicio());
                cal.add(Calendar.MONTH, 3);

                if (orcDespesaCl.getOrcamentoDespesa().getDataFim().compareTo(cal.getTime()) > 0) {
                    Messages.showError(STR_CREATE_ORC_DESP_CL, Constants.TRAVEL_BUDGET_THREE_MONTHS);
                    return false;
                }
                orcDespesaCl.setIndicadorOrcamentoExtra(Constants.SIM);
            } else {
                Messages.showError(STR_CREATE_ORC_DESP_CL, Constants.ORCAMENTO_DESPESA_ORCAMENTO_VIAGEM_LIMITE);
                return false;
            }
        }
        return true;
    }

    public Boolean isValidOrcamentoDespesaName(String name) {

        if (name.indexOf('\'') >= 0) {
            Messages.showError("orcDespName", Constants.ORCAMENTO_DESPESA_CHARACTER_NOT_ALLOWED, " \" ' \" ");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Boolean validateCLobs() {

        String lastReimbusableIndicator = null;

        if (bean.getGrantedCLobPickList().length > 0) {
            for (Long clobCode : bean.getGrantedCLobPickList()) {
                ContratoPratica cp = contratoPraticaService.findContratoPraticaById(clobCode);

                //Validar se existe algum indicador reembolsável não preenchido
                if (cp.getIndicadorReembolsavel() == null) {
                    Messages.showError(STR_VALIDATE_CLOB, Constants.BUNDLE_IS_CLOB_REIMBURSABLE_MISSING, cp.getNomeContratoPratica());
                    return Boolean.FALSE;
                }

                if (lastReimbusableIndicator != null && !lastReimbusableIndicator.equals(cp.getIndicadorReembolsavel())) {
                    Messages.showError(STR_VALIDATE_CLOB, Constants.BUNDLE_IS_CLOB_NOT_REIMBURSABLE);
                    return Boolean.FALSE;
                }
                lastReimbusableIndicator = cp.getIndicadorReembolsavel();
            }
        } else {
            Messages.showError(STR_VALIDATE_CLOB,
                    Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    Constants.ORCAMENT_DESPESA_CLOBS);
        }

        String invoiceType = bean.getToCl().getIndicadorTipoFatura() != null ? bean.getToCl().getIndicadorTipoFatura() : "";

        //Validar se o Invoice Type está preenchido caso o tipo do reembolso for Y
        if (lastReimbusableIndicator != null && lastReimbusableIndicator.equalsIgnoreCase("Y") && invoiceType.equalsIgnoreCase("")) {
            Messages.showError(STR_VALIDATE_CLOB,
                    Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    Constants.ORCAMENTO_DESPESA_TIPO_FATURA);
            return Boolean.FALSE;
        }
        //Se o tipo do reembolso for N, zerar o valor do Invoice Type
        else if (lastReimbusableIndicator != null && lastReimbusableIndicator.equalsIgnoreCase("N")) {
            bean.getToCl().setIndicadorTipoFatura(null);
        }

        return Boolean.TRUE;
    }

    private boolean isInputDateValid() {
        Date dataBeg = bean.getTo().getDataInicio();
        Date dataEnd = bean.getTo().getDataFim();
        if (dataBeg == null && dataEnd == null) {
            Messages.showError(STR_VALIDATE_CLOB,
                    Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    Constants.ORCAMENTO_DESPESA_DATE);
            Messages.showError(STR_VALIDATE_CLOB,
                    Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    Constants.ORCAMENTO_DESPESA_UNTIL);

        } else if ( Boolean.TRUE.equals(DateUtil.validateValidityDate(dataBeg, dataEnd, false))) {
           return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean validateITSupport() {
        GrantedAuthority[] loggedUserAuthorities = LoginUtil
                .getLoggedUserAuthorities();
        boolean isItSupport = false;
        for (GrantedAuthority authority : loggedUserAuthorities) {

            if(authority.getAuthority().equals("BOF.COST_CENTER.TRAVEL_BUDGET:ED")
                    || authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET.EXTRA:ED")
                    || authority.getAuthority().equals("BUS.COST_CENTER.TRAVEL_BUDGET:ED")){
                isItSupport = false;
                continue;
            }
            if (authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET.EXTRA:VW")) {
                isItSupport = true;
            }
        }
        return isItSupport;
    }
}
