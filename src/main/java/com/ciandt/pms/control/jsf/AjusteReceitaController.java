package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAjusteReceitaService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IReceitaDealFiscalService;
import com.ciandt.pms.business.service.ITipoAjusteService;
import com.ciandt.pms.comparator.AjusteReceitaComparator;
import com.ciandt.pms.control.jsf.bean.AjusteReceitaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.TipoAjuste;
import com.ciandt.pms.util.DateUtil;

/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * @since 14/07/2011
 */
@Controller
@RolesAllowed({"BUS.REVENUE.ADJUSTMENT:VW", "BUS.REVENUE.ADJUSTMENT:ED", "BUS.REVENUE.ADJUSTMENT:DE", "BUS.REVENUE.ADJUSTMENT:CR"})
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AjusteReceitaController {

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_AJUSTE_RECEITA_SEARCH =
            "ajusteReceita_search";

    /** outcome tela view da entidade. */
    private static final String OUTCOME_AJUSTE_RECEITA_VIEW =
            "ajusteReceita_view";

    /** outcome tela view da entidade. */
    private static final String OUTCOME_RECEITA = "receita_manage_view";

    /**
     * Instancia do Bean.
     */
    @Autowired
    private AjusteReceitaBean bean;

    /**
     * Serviço de ContratoPratica.
     */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /**
     * Serviço de AjusteReceita.
     */
    @Autowired
    private IAjusteReceitaService ajusteReceitaService;

    /**
     * Serviço de TipoAjuste.
     */
    @Autowired
    private ITipoAjusteService tipoAjusteService;

    /**
     * Serviço de ReceitaDealFisca.
     */
    @Autowired
    private IReceitaDealFiscalService receitaDealFiscalService;

    /**
     * @return the bean
     */
    public AjusteReceitaBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final AjusteReceitaBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareSearch() {
        // limpa o bean
        bean.reset();

        // pesquisa e carrega todos os Contratos Pratica no combo
        this.loadContratoPraticaList(contratoPraticaService
                .findAllCompleteForCombobox());

        return OUTCOME_AJUSTE_RECEITA_SEARCH;
    }

    /**
     * Prepara a tela de visualização do AjusteReceita.
     * 
     * @return retorna o outcome d atela de edição.
     */
    public String prepareViewAjusteReceita() {
        // bean.resetTo();
        bean.setShowEditAjusteReceitaPanel(Boolean.valueOf(false));

        Long codigoAjusteReceita = bean.getCurrentRowId();

        AjusteReceita ajusteReceita =
                ajusteReceitaService.findAjusteReceitaById(codigoAjusteReceita);

        bean.setTo(ajusteReceita);
        bean.setNomeDealFiscalSelected(ajusteReceita.getReceitaDealFiscal()
                .getDealFiscal().getNomeDealFiscal());

        bean.setNomeTipoAjuste(ajusteReceita.getTipoAjuste()
                .getNomeTipoAjuste());

        Date dataMes = ajusteReceita.getDataMesAjuste();

        bean.setValidityMonthAdjust(DateUtil.getMonthString(dataMes));
        bean.setValidityYearAdjust(DateUtil.getYearString(dataMes));
        bean.setBackFlag(Boolean.valueOf(true));
        return OUTCOME_AJUSTE_RECEITA_VIEW;
    }

    /**
     * Prepara a lista de AjusteReceita.
     * 
     * @return lista preparada
     */
    public List<AjusteReceita> prepareAjusteReceitaList() {

        List<AjusteReceita> ajusteReceitaList = ajusteReceitaService
				.findAjusteReceitaByDateReceitaAndContratoPratica(bean.getTo()
						.getReceitaDealFiscal().getReceitaMoeda().getReceita()
						.getDataMes(), bean.getTo().getReceitaDealFiscal()
						.getReceitaMoeda().getReceita().getContratoPratica());

        bean.setResultListAddEdit(new ArrayList<AjusteReceita>());

        for (AjusteReceita ajusteReceita : ajusteReceitaList) {
            ajusteReceita.setReceitaDealFiscal(receitaDealFiscalService
                    .findReceitaDealById(ajusteReceita.getReceitaDealFiscal()
                            .getCodigoReceitaDfiscal()));

            if (ajusteReceita.getTipoAjuste() != null) {
                ajusteReceita.setTipoAjuste(tipoAjusteService
                        .findTipoAjusteById(ajusteReceita.getTipoAjuste()
                                .getCodigoTipoAjuste()));
            } else {
                ajusteReceita.setTipoAjuste(new TipoAjuste());
            }

            ajusteReceita.setBalanco(ajusteReceita.getReceitaDealFiscal()
                    .getValorReceita().add(ajusteReceita.getValorAjuste()));
        }

        return ajusteReceitaList;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        bean.setTo(ajusteReceitaService.findAjusteReceitaById(id));
        if (bean.getTo() == null
                || bean.getTo().getCodigoAjusteReceita() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Pesquisa Ajustes de Receita de acordo com os filtros.
     */
    public void findByFilter() {

        bean.setIsViewBySearch(Boolean.valueOf(true));

        if (this.validateDates()) {

            bean.setAnoMesAjuste(this.getDateSelected(
                    bean.getValidityMonthAdjust(), bean.getValidityYearAdjust()));

            bean.setAnoMesReceita(this.getDateSelected(
                    bean.getValidityMonthRevenue(),
                    bean.getValidityYearRevenue()));

            Long codigoContratoPratica =
                    bean.getContratoPraticaMap().get(
                            bean.getNomeContratoPraticaConsulta());

            ContratoPratica contratoPratica = new ContratoPratica();
            if (codigoContratoPratica != null) {
                contratoPratica =
                        contratoPraticaService
                                .findContratoPraticaById(codigoContratoPratica);
            }

            if (bean.getAnoMesAjuste() == null
                    && bean.getAnoMesReceita() == null) {
                Messages.showWarning("findByFilter",
                        Constants.MSG_WARN_REVENUE_ADJUSTMENT_DATE_SELECT);
            } else {
                if (bean.getAnoMesAjuste() != null
                        && bean.getAnoMesReceita() == null) {
                    // Busca pela data de Ajuste
                    List<AjusteReceita> lista =
                            ajusteReceitaService
                                    .findAjusteReceitaByDateAjusteAndContratoPratica(
                                            bean.getAnoMesAjuste(),
                                            contratoPratica);
                    Collections.sort(lista, new AjusteReceitaComparator());
                    bean.setResultList(lista);

                } else if (bean.getAnoMesAjuste() == null
                        && bean.getAnoMesReceita() != null) {
                    List<AjusteReceita> lista =
                            ajusteReceitaService
                                    .findAjusteReceitaByDateReceitaAndContratoPratica(
                                            bean.getAnoMesReceita(),
                                            contratoPratica);
                    Collections.sort(lista, new AjusteReceitaComparator());
                    bean.setResultList(lista);

                } else {

                    List<AjusteReceita> lista =
                            ajusteReceitaService
                                    .findAjusteReceitaByDateReceitaAndDateAjusteAndContratoPratica(
                                            bean.getAnoMesReceita(),
                                            bean.getAnoMesAjuste(),
                                            contratoPratica);

                    Collections.sort(lista, new AjusteReceitaComparator());
                    bean.setResultList(lista);
                }

                if (bean.getResultList().isEmpty() && !bean.getIsUpdate()) {
                    Messages.showWarning("findByFilter",
                            Constants.DEFAULT_MSG_WARNG_NO_RESULT);
                } else {
                    for (AjusteReceita ajusteReceita : bean.getResultList()) {
                        ajusteReceita
                                .setReceitaDealFiscal(receitaDealFiscalService
                                        .findReceitaDealById(ajusteReceita
                                                .getReceitaDealFiscal()
                                                .getCodigoReceitaDfiscal()));

                        if (ajusteReceita.getTipoAjuste() != null) {
                            ajusteReceita.setTipoAjuste(tipoAjusteService
                                    .findTipoAjusteById(ajusteReceita
                                            .getTipoAjuste()
                                            .getCodigoTipoAjuste()));
                        } else {
                            ajusteReceita.setTipoAjuste(new TipoAjuste());
                        }
                        ajusteReceita.setBalanco(ajusteReceita
                                .getReceitaDealFiscal().getValorReceita()
                                .add(ajusteReceita.getValorAjuste()));
                    }
                }
                bean.setIsUpdate(Boolean.valueOf(false));
            }
        }
    }

    /**
     * Retorna uma data de acordo com ano e mes.
     * 
     * @param month
     *            - mes
     * @param year
     *            - ano
     * 
     * @return Date
     */
    private Date getDateSelected(final String month, final String year) {

        Date data = DateUtil.getDate(month, year);

        return data;
    }

    /**
     * Valida as datas de Ajuste e Receita para a busca.
     * 
     * @return true se datas estao corretas ou nao selecionadas
     */
    private Boolean validateDates() {

        Boolean retorno = Boolean.valueOf(true);

        if (bean.getValidityMonthAdjust() != ""
                && bean.getValidityYearAdjust() == "") {
            Messages.showWarning("validateDates",
                    Constants.MSG_WARN_REVENUE_ADJUSTMENT_YEAR_ADJUST_NULL);
            retorno = Boolean.valueOf(false);

        } else if (bean.getValidityMonthAdjust() == ""
                && bean.getValidityYearAdjust() != "") {
            Messages.showWarning("validateDates",
                    Constants.MSG_WARN_REVENUE_ADJUSTMENT_MONTH_ADJUST_NULL);
            retorno = Boolean.valueOf(false);
        }

        if (bean.getValidityMonthRevenue() != ""
                && bean.getValidityYearRevenue() == "") {
            Messages.showWarning("validateDates",
                    Constants.MSG_WARN_REVENUE_ADJUSTMENT_YEAR_REVENUE_NULL);
            retorno = Boolean.valueOf(false);

        } else if (bean.getValidityMonthRevenue() == ""
                && bean.getValidityYearRevenue() != "") {
            Messages.showWarning("validateDates",
                    Constants.MSG_WARN_REVENUE_ADJUSTMENT_MONTH_REVENUE_NULL);
            retorno = Boolean.valueOf(false);
        }

        return retorno;
    }

    /**
     * Popula a lista de ContratoPratica para combobox de contratos praticas.
     * 
     * @param contratosPratica
     *            lista de ContratoPratica.
     * 
     */
    private void loadContratoPraticaList(
            final List<ContratoPratica> contratosPratica) {

        Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
        List<String> contratoPraticaList = new ArrayList<String>();

        for (ContratoPratica cp : contratosPratica) {
            contratoPraticaMap.put(cp.getNomeContratoPratica(),
                    cp.getCodigoContratoPratica());
            contratoPraticaList.add(cp.getNomeContratoPratica());
        }

        bean.setContratoPraticaMap(contratoPraticaMap);
        bean.setContratoPraticaList(contratoPraticaList);
    }

    /**
     * Volta para a tela de Manage da Receita.
     * 
     * @return página de destino
     */
    public String backToReceita() {
        return OUTCOME_RECEITA;
    }

    /**
     * Volta para a tela de Search do AjusteReceita.
     * 
     * @return página de destino
     */
    public String backToAjusteReceita() {
        this.findByFilter();
        return OUTCOME_AJUSTE_RECEITA_SEARCH;
    }
    
    /**
     * Prepara a tela de visualização do AjusteReceita.
     * 
     * @return retorna o outcome d atela de edição.
     */
    public String prepareViewReceita() {
        bean.setShowEditAjusteReceitaPanel(Boolean.valueOf(false));

        Long codigoAjusteReceita = bean.getCurrentRowId();

        AjusteReceita ajusteReceita =
                ajusteReceitaService.findAjusteReceitaById(codigoAjusteReceita);

        bean.setTo(ajusteReceita);
        bean.setNomeDealFiscalSelected(ajusteReceita.getReceitaDealFiscal()
                .getDealFiscal().getNomeDealFiscal());

        bean.setNomeTipoAjuste(ajusteReceita.getTipoAjuste()
                .getNomeTipoAjuste());

        Date dataMes = ajusteReceita.getDataMesAjuste();

        bean.setValidityMonthAdjust(DateUtil.getMonthString(dataMes));
        bean.setValidityYearAdjust(DateUtil.getYearString(dataMes));

        bean.setBackFlag(Boolean.valueOf(true));

        return OUTCOME_AJUSTE_RECEITA_VIEW;
    }

}