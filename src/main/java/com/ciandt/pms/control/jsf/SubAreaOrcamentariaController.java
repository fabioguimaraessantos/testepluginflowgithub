package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAreaOrcamentariaService;
import com.ciandt.pms.business.service.IGrupoCustoAreaOrcamentariaService;
import com.ciandt.pms.business.service.IModeloAreaOrcamentariaService;
import com.ciandt.pms.business.service.IModeloService;
import com.ciandt.pms.control.jsf.bean.SubAreaOrcamentariaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Modelo;
import com.ciandt.pms.model.ModeloAreaOrcamentaria;
import com.ciandt.pms.persistence.dao.jpa.GrupoCustoAreaOrcamentaria;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import java.util.*;

/**
 * Define o Controller da entidade.
 *
 * @author <a href="mailto:vnogueira@ciandt.com">Vinicius Figueiredo Nogueira</a>
 * @since 29/08/2019
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.SUB_BUDGET_AREA:VW", "BOF.SUB_BUDGET_AREA:CR", "BOF.SUB_BUDGET_AREA:ED" })
public class SubAreaOrcamentariaController {

    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_SUB_AREA_ORCAMENTARIA_ADD = "subAreaOrcamentaria_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_SUB_AREA_ORCAMENTARIA_EDIT = "subAreaOrcamentaria_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_SUB_AREA_ORCAMENTARIA_DELETE = "subAreaOrcamentaria_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_SUB_AREA_ORCAMENTARIA_RESEARCH = "subAreaOrcamentaria_research";

    @Autowired
    private SubAreaOrcamentariaBean bean;
    @Autowired
    private IAreaOrcamentariaService areaOrcamentariaService;
    @Autowired
    private IGrupoCustoAreaOrcamentariaService grupoCustoAreaOrcamentariaService;
    @Autowired
    private IModeloAreaOrcamentariaService modeloAreaOrcamentariaService;
    @Autowired
    private IModeloService modeloService;

    public SubAreaOrcamentariaController() {
    }

    public SubAreaOrcamentariaBean getBean() {
        return bean;
    }

    public void setBean(final SubAreaOrcamentariaBean bean) {
        this.bean = bean;
    }

    public String prepareCreate() {
        bean.reset();
        bean.setIsUpdate(Boolean.FALSE);
        this.getModelos();
        this.getAreasOrcamentariasPais();
        return OUTCOME_SUB_AREA_ORCAMENTARIA_ADD;
    }

    public String prepareResearch() {
        bean.reset();
        this.getAreasOrcamentariasPais();
        this.getModelos();
        bean.setSuggestionsListInAtivo(Constants.SUB_AREA_ORCAMENTARIA_ACTIVE_INACTIVE_VALUES);
        return OUTCOME_SUB_AREA_ORCAMENTARIA_RESEARCH;
    }

    public void getAreasOrcamentariasPais() {
        List<AreaOrcamentaria> areasOrcamentarias = areaOrcamentariaService.findAllActiveAreaOrcamentariaPai();
        List<String> areasOrcamentariasPaisString = new ArrayList<>();

        for (AreaOrcamentaria area : areasOrcamentarias) {
            areasOrcamentariasPaisString.add(area.getNomeAreaOrcamentaria());
            Hibernate.initialize(area.getAreaOrcamentariaPai());
        }

        bean.setAreaOrcamentariaPais(areasOrcamentarias);
        bean.setAreasOrcamentariasPaisString(areasOrcamentariasPaisString);
    }

    public void getModelos() {

        List<Modelo> modelos = modeloService.findAll();
        bean.setModelos(modelos);
        if (Boolean.TRUE.equals(bean.getIsUpdate())) {
            ModeloAreaOrcamentaria modeloAreaOrcamentaria = modeloAreaOrcamentariaService.findByCurrentAreaOrcamentaria(bean.getTo().getCodigoAreaOrcamentaria());
            if (modeloAreaOrcamentaria != null) {
                bean.setSelectedModelo(modeloAreaOrcamentaria.getModelo());
            }
        }

   }

    public void create() throws Exception {
        this.getAreasOrcamentariasPais();
        if (bean.getTo().getNomeAreaOrcamentaria().isEmpty()) {
            Messages.showError("findByFilter",
                    Constants.SUB_BUDGET_AREA_REQUIRED_NAME);
            return;
        }
        if (bean.getSelectedAreaOrcamentariaPai().isEmpty()) {
            Messages.showError("findByFilter",
                    Constants.SUB_BUDGET_AREA_REQUIRED_BUDGET_AREA);
            return;
        }

        AreaOrcamentaria selectedPai = null;
        for (AreaOrcamentaria pai : bean.getAreaOrcamentariaPais()) {
            if (pai.getNomeAreaOrcamentaria().equalsIgnoreCase(bean.getSelectedAreaOrcamentariaPai())) {
                selectedPai = pai;
                break;
            }
        }

        if (null == selectedPai) {
            Messages.showError("findByFilter",
                    Constants.SUB_BUDGET_AREA_INVALID_BUDGET_AREA);
            return;
        }

        AreaOrcamentaria areaOrcamentaria = this.bean.getTo();
        areaOrcamentaria.setAreaOrcamentariaPai(selectedPai);
        areaOrcamentaria.setIndicadorStatus(Constants.ACTIVE);

        Modelo selectedModelo = bean.getSelectedModelo();

        if (areaOrcamentariaService.create(areaOrcamentaria, selectedModelo)) {
            Messages.showSucess("findByFilter",
                    Constants.DEFAULT_MSG_SUCCESS_CREATE, Constants.ENTITY_NAME_SUB_BUDGET_AREA);
        } else {
            Messages.showError("findByFilter",
                    Constants.DEFAULT_MSG_ERROR_CURRENT_DATE_GT_END_DATE, Constants.ENTITY_NAME_SUB_BUDGET_AREA);
        }

        bean.reset();
        this.getAreasOrcamentariasPais();
        this.getModelos();
    }

    public void findByFilter() {
        bean.getFilter().setAreaOrcamentariaPai(null);

        if (!bean.getSelectedAreaOrcamentariaPai().isEmpty()) {
            for (AreaOrcamentaria pai : bean.getAreaOrcamentariaPais()) {
                if (pai.getNomeAreaOrcamentaria().equalsIgnoreCase(bean.getSelectedAreaOrcamentariaPai())) {
                    bean.getFilter().setAreaOrcamentariaPai(pai);
                    break;
                }
            }

            if (null == bean.getFilter().getAreaOrcamentariaPai()) {
                Messages.showError("findByFilter",
                        Constants.SUB_BUDGET_AREA_INVALID_BUDGET_AREA);
                return;
            }
        }

        bean.setResultList(areaOrcamentariaService
                .findByFilter(bean.getFilter()));

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    public String prepareUpdate() throws Exception {

        bean.setTo(areaOrcamentariaService.findById(bean.getCurrentRowId()));
        bean.setIsUpdate(Boolean.TRUE);
        this.getModelos();
        this.getAreasOrcamentariasPais();
        bean.setSelectedAreaOrcamentariaPai(bean.getTo().getAreaOrcamentariaPai().getNomeAreaOrcamentaria());

        return OUTCOME_SUB_AREA_ORCAMENTARIA_EDIT;
    }

    public String update() {
        for (AreaOrcamentaria ao : bean.getAreaOrcamentariaPais()) {
            if (bean.getSelectedAreaOrcamentariaPai().equalsIgnoreCase(ao.getNomeAreaOrcamentaria())) {
                bean.getTo().setAreaOrcamentariaPai(ao);
                break;
            }
        }

        if (bean.getTo().getIndicadorStatus().equalsIgnoreCase(Constants.INACTIVE)) {
            List<GrupoCustoAreaOrcamentaria> associatedCostCenters = grupoCustoAreaOrcamentariaService.
                    findBySubAreaOrcamentariaAndVigencia(bean.getTo().getCodigoAreaOrcamentaria(), new Date());
            if (null != associatedCostCenters && !associatedCostCenters.isEmpty()) {
                for (GrupoCustoAreaOrcamentaria gcao : associatedCostCenters) {
                    if (gcao.getGrupoCusto().getIndicadorAtivo().equalsIgnoreCase(Constants.ACTIVE)) {
                        Messages.showError("findByFilter",
                                Constants.SUB_BUDGET_AREA_ERROR_ASSOCIATED_COST_CENTERS,
                                this.getCostCenterNames(associatedCostCenters));
                        return null;
                    }
                }
            }
        }

        ModeloAreaOrcamentaria modeloAreaOrcamentariaAtual = modeloAreaOrcamentariaService.findByCurrentAreaOrcamentaria(bean.getTo().getCodigoAreaOrcamentaria());

        if (modeloAreaOrcamentariaAtual != null) {
             modeloAreaOrcamentariaAtual.setModelo(bean.getSelectedModelo());
             modeloAreaOrcamentariaService.update(modeloAreaOrcamentariaAtual);
        }
            areaOrcamentariaService.update(bean.getTo());

        Messages.showSucess("findByFilter",
                Constants.DEFAULT_MSG_UPDATE_SUCCESS, Constants.ENTITY_NAME_SUB_BUDGET_AREA);

        if (null != bean.getFilter().getAreaOrcamentariaPai()) {
            bean.setSelectedAreaOrcamentariaPai(bean.getFilter().getAreaOrcamentariaPai().getNomeAreaOrcamentaria());
        } else {
            bean.setSelectedAreaOrcamentariaPai("");
        }

        this.findByFilter();

        return OUTCOME_SUB_AREA_ORCAMENTARIA_RESEARCH;
    }

    private String getCostCenterNames(List<GrupoCustoAreaOrcamentaria> costCentersBudgetAreas) {
        List<GrupoCusto> costCenters = new ArrayList<GrupoCusto>();
        StringBuilder costCenterNames = new StringBuilder();
        String prefix = "";

        for (GrupoCustoAreaOrcamentaria gcao : costCentersBudgetAreas) {
            if (gcao.getGrupoCusto().getIndicadorAtivo().equalsIgnoreCase(Constants.ACTIVE)) {
                costCenters.add(gcao.getGrupoCusto());
            }
        }

        this.sortCostCentersByName(costCenters);

        for (GrupoCusto gc : costCenters) {
            costCenterNames.append(prefix);
            prefix = ", ";
            costCenterNames.append(gc.getNomeGrupoCusto());
        }

        return costCenterNames.toString();
    }

    private void sortCostCentersByName(List<GrupoCusto> costCenters) {
        Collections.sort(costCenters, new Comparator<GrupoCusto>() {
            @Override
            public int compare(final GrupoCusto gc1, final GrupoCusto gc2) {
                return gc1.getNomeGrupoCusto().compareToIgnoreCase(gc2.getNomeGrupoCusto());
            }
        });
    }
}
