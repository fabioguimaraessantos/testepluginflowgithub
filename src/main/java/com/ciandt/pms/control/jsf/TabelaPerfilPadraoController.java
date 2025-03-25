package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICargoService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IItemTabPerPadraoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPerfilPadraoService;
import com.ciandt.pms.business.service.IPmgService;
import com.ciandt.pms.business.service.ITabelaPerfilPadraoService;
import com.ciandt.pms.control.jsf.bean.CargoBean;
import com.ciandt.pms.control.jsf.bean.ItemTabPerPadraoBean;
import com.ciandt.pms.control.jsf.bean.TabelaPerfilPadraoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.ItemTabPerPadrao;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PerfilPadrao;
import com.ciandt.pms.model.Pmg;
import com.ciandt.pms.model.TabelaPerfilPadrao;


/**
 * 
 * A classe TabelaPerfilPadraoController proporciona as funcionalidades de
 * controle para as entidades de TabelaPerfilPadrao.
 * 
 * @since 19/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Controller
@RolesAllowed({ "BUS.DEFAULT_PRICE_TABLE:VW" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TabelaPerfilPadraoController {

    /*********** OUTCOMES **************************/

    /** outcome tela inculsao de entidade. */
    private static final String OUTCOME_TABELA_PERFIL_PADRAO_ADD =
            "tabela_perfil_padrao_add";

    /** outcome tela item tabela perfil padrao. */
    private static final String OUTCOME_ITEM_TABELA_PERFIL_PADRAO =
            "item_tabela_perfil_padrao";

    /*********** SERVICES **************************/

    /** Instancia do serviço. */
    @Autowired
    private ITabelaPerfilPadraoService tabelaPerfilPadraoService;

    /** Instancia do servico Modulo. */
    @Autowired
    private IModuloService moduloService;

    /** Instancia do servico CidadeBase. */
    @Autowired
    private ICidadeBaseService cidadeBaseService;

    /** Instancia do servico Pmg. */
    @Autowired
    private IPmgService pmgService;

    /** Instancia do servico ItemTabPerPadrao. */
    @Autowired
    private IItemTabPerPadraoService itemTabPerPadraoService;

    /** Instancia do servico PerfilPadrao. */
    @Autowired
    private IPerfilPadraoService perfilPadraoService;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /** Insancia do servico CargoPms. */
    @Autowired
    private ICargoService cargoPmsService;

    /*********** BEANS **************************/

    /** Bean. */
    @Autowired
    private TabelaPerfilPadraoBean bean = null;

    /** Bean. */
    @Autowired
    private ItemTabPerPadraoBean beanItemTabPerPadrao = null;

    /** Bean. */
    @Autowired
    private CargoBean beanCargo = null;

    /********************************************/

    /**
     * @return the bean
     */
    public TabelaPerfilPadraoBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final TabelaPerfilPadraoBean bean) {
        this.bean = bean;
    }

    /**
     * @return the beanItemTabPerPadrao
     */
    public ItemTabPerPadraoBean getBeanItemTabPerPadrao() {
        return beanItemTabPerPadrao;
    }

    /**
     * @param beanItemTabPerPadrao
     *            the beanItemTabPerPadrao to set
     */
    public void setBeanItemTabPerPadrao(
            final ItemTabPerPadraoBean beanItemTabPerPadrao) {
        this.beanItemTabPerPadrao = beanItemTabPerPadrao;
    }

    /**
     * @return the beanCargo
     */
    public CargoBean getBeanCargo() {
        return beanCargo;
    }

    /**
     * @param beanCargo
     *            the beanCargo to set
     */
    public void setBeanCargo(final CargoBean beanCargo) {
        this.beanCargo = beanCargo;
    }

    /**
     * @return Outcome para adicionar entidade.
     */
    public String prepareAdd() {

        bean.resetBean();

        this.loadMoedaList(moedaService.findMoedaAll());

        // guarda a data do HistoryLock
        bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());

        // Seta a lista de entidades
        bean.setListaTabelaPerfilPadraoAll(this.listaTabelaPerfilPadrao());

        return OUTCOME_TABELA_PERFIL_PADRAO_ADD;
    }

    /**
     * 
     * @return Outcome da tela de item do tabela perfil padrao.
     */
    public String prepareItemTabelaPerfilPadrao() {

        TabelaPerfilPadrao tabelaPerfilPadrao =
                tabelaPerfilPadraoService.findById(bean.getTo()
                        .getCodigoTabPerfilPadrao());

        // Carrega combos.
        this.loadPmgList(pmgService.findPmgAll());
        this.loadBaseList(cidadeBaseService.findCidadeBaseAllActive());
        // this.loadCargoList(vwHrsCargoService.findAllActive());
        this.loadCargoPmsList(cargoPmsService.findAllCargoPms());

        bean.setMoeda(tabelaPerfilPadrao.getMoeda().getSiglaMoeda());

        beanItemTabPerPadrao.setListaItemTabPerPadrao(this
                .listaItemTabelaPerfilPadrao(tabelaPerfilPadrao));

        /*
         * beanItemTabPerPadrao.setListaItemTabPerPadraoRow(this
         * .listaItemTabelaPerfilPadraoRow(tabelaPerfilPadrao));
         */

        return OUTCOME_ITEM_TABELA_PERFIL_PADRAO;
    }

    /**
     * Prepare para editar tabelaPerfilPadrao.
     */
    public void prepareEditTabelaPerfilPadrao() {

        // Flag para botao update e cancel
        bean.setFlagEdicaoTabelaPerPadrao(Boolean.valueOf(true));

        // Flag para edicao da data
        bean.setFlagEdicaoData(Boolean.TRUE);

        // Recupera tabela selecionada
        TabelaPerfilPadrao tabelaPerfilPadrao =
                tabelaPerfilPadraoService.findById(bean.getTo()
                        .getCodigoTabPerfilPadrao());

        // Recupera data de inicio e converte para string
        Date mesInicioVigencia = tabelaPerfilPadrao.getDataInicio();

        Calendar dataInicio = new GregorianCalendar();
        dataInicio.setTime(mesInicioVigencia);

        String mes = Integer.toString(dataInicio.get(Calendar.MONTH) + 1);
        String ano = Integer.toString(dataInicio.get(Calendar.YEAR));

        // Seta o formulario
        bean.setMesInicioVigencia(mes);
        bean.setAnoInicioVigencia(ano);
        bean.setMoeda(tabelaPerfilPadrao.getMoeda().getNomeMoeda());
        bean.getTo().setNomeTabPerfilPadrao(
                tabelaPerfilPadrao.getNomeTabPerfilPadrao());

    }

    /**
     * Cria uma entidade TabelaPerfilPadrao no banco.
     */
    public void createTabelaPerfilPadrao() {

        // Captura o to.
        TabelaPerfilPadrao tabela = bean.getTo();

        // Captura a moeda
        Long codigoMoeda = bean.getMapMoeda().get(bean.getMoeda());
        Moeda moeda = moedaService.findMoedaById(codigoMoeda);

        Date startDate = null;

        try {
            String startDateStr =
                    bean.getMesInicioVigencia() + "/"
                            + bean.getAnoInicioVigencia();

            String[] dateFormat = {"MM/yyyy"};

            startDate = DateUtils.parseDate(startDateStr, dateFormat);

            if (!moduloService.verifyHistoryLock(startDate, Boolean
                    .valueOf(true), Boolean.FALSE)) {
                return;
            }

            tabela.setDataInicio(startDate);
            tabela.setMoeda(moeda);

            // Cria a entidade no banco
            if (tabelaPerfilPadraoService.createTabelaPerfilPadrao(tabela)) {
                bean.resetBean();
                Messages.showSucess("createTabelaPerfilPadrao",
                        Constants.GENEREC_MSG_SUCCESS_ADD);
            }

            // Atualiza a lista
            bean.setListaTabelaPerfilPadraoAll(this.listaTabelaPerfilPadrao());

        } catch (ParseException e) {

            e.printStackTrace();
            Messages.showError("createTabelaPreco",
                    Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
        }

    }

    /**
     * Update TabelaPerPadrao.
     */
    public void updateTabPerPadrao() {
        TabelaPerfilPadrao tabela = bean.getTo();

        Long cogidoMoeda = bean.getMapMoeda().get(bean.getMoeda());
        Moeda moeda = moedaService.findMoedaById(cogidoMoeda);

        Date startDate = null;

        try {
            String startDateStr =
                    bean.getMesInicioVigencia() + "/"
                            + bean.getAnoInicioVigencia();

            String[] dateFormat = {"MM/yyyy"};

            startDate = DateUtils.parseDate(startDateStr, dateFormat);

            if (!moduloService.verifyHistoryLock(startDate, Boolean
                    .valueOf(true), Boolean.FALSE)) {
                return;
            }

            tabela.setDataInicio(startDate);
            tabela.setMoeda(moeda);

            if (tabelaPerfilPadraoService.updateTabPerPadrao(tabela)) {
                Messages.showSucess("TabelaPerfilPadrao",
                        Constants.GENEREC_MSG_SUCCESS_UPDATE);
            }

        } catch (ParseException e) {

            e.printStackTrace();
            Messages.showError("createTabelaPreco",
                    Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
        }

        bean.resetBean();
        bean.setFlagEdicaoTabelaPerPadrao(Boolean.valueOf(false));
    }

    /**
     * Executa um update das entidades.
     * 
     */
    public void updateList() {
        itemTabPerPadraoService.updateItemTabPerPadraoList(beanItemTabPerPadrao
                .getListaItemTabPerPadrao());

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.GENEREC_MSG_SUCCESS_UPDATE);

        this.listaItemTabPerfilPadraoByTabela(bean.getTo());

        beanItemTabPerPadrao.reset();
    }

    /**
     * Persiste um obejto ItemTabPerPadrao no banco.
     */
    public void createItemTabPerPadrao() {

        // Busca Pmg
        Long codigoPmg =
                beanItemTabPerPadrao.getMapPmg().get(
                        beanItemTabPerPadrao.getPmg());
        Pmg pmg = pmgService.findPmgById(codigoPmg);

        // Busca cidadeBase
        Long codigoBase =
                beanItemTabPerPadrao.getMapBase().get(
                        beanItemTabPerPadrao.getBase());
        CidadeBase base = cidadeBaseService.findCidadeBaseById(codigoBase);

        // Busca Cargo
        Long codigoCargoPms =
                beanItemTabPerPadrao.getMapCargo().get(
                        beanItemTabPerPadrao.getCargo());
        CargoPms cargo = cargoPmsService.findCargoPmsById(codigoCargoPms);

        // Busca valor
        BigDecimal valor = beanItemTabPerPadrao.getValor();

        // Perfil Padrao
        PerfilPadrao perfilPadrao = new PerfilPadrao();

        List<PerfilPadrao> perfilPadraoList =
                perfilPadraoService.findByCargoPmgAndCidadeBase(cargo, pmg,
                        base);

        // Se perfilPadrao nao existir, cria um
        if (perfilPadraoList.size() == 0) {
            perfilPadrao.setPmg(pmg);
            perfilPadrao.setCargoPms(cargo);
            perfilPadrao.setCidadeBase(base);
            perfilPadraoService.createPerfilPadrao(perfilPadrao);
        } else {
            perfilPadrao = perfilPadraoList.get(0);
        }

        ItemTabPerPadrao item = new ItemTabPerPadrao();
        item.setPerfilPadrao(perfilPadrao);
        item.setTabelaPerfilPadrao(bean.getTo());
        item.setValorPrecoPadrao(valor);
        // item.setIndicadorAtivo("A");

        // Persiste item
        if (itemTabPerPadraoService.createItemTabPerPadrao(item)) {
            Messages.showSucess("addItem", Constants.GENEREC_MSG_SUCCESS_ADD);
        }

        beanItemTabPerPadrao.setListaItemTabPerPadrao(this
                .listaItemTabelaPerfilPadrao(bean.getTo()));

        beanItemTabPerPadrao.reset();

    }

    /**
     * Remove entidade do banco.
     */
    public void removeTabelaPerfilPadrao() {
        TabelaPerfilPadrao tabelaPerfilPadrao =
                tabelaPerfilPadraoService.findById(bean.getTo()
                        .getCodigoTabPerfilPadrao());

        if (tabelaPerfilPadraoService
                .removeTabelaPerfilPadrao(tabelaPerfilPadrao)) {
            Messages.showSucess("tabelaPerfilPadrao",
                    Constants.GENEREC_MSG_SUCCESS_REMOVE);
        }

        bean.setListaTabelaPerfilPadraoAll(this.listaTabelaPerfilPadrao());
        bean.resetBean();
    }

    /**
     * Lista todos as entidades.
     * 
     * @return lista
     */
    public List<TabelaPerfilPadrao> listaTabelaPerfilPadrao() {
        List<TabelaPerfilPadrao> tabelaPerfilPadraoList =
                tabelaPerfilPadraoService.findTabelaPerfilPadraoAll();
        return tabelaPerfilPadraoList;
    }

    /**
     * Lista com entidades de ItemTabPerfPdarao por tabela.
     * 
     * @param tabela
     *            tabela
     * @return lista
     */
    public List<ItemTabPerPadrao> listaItemTabPerfilPadraoByTabela(
            final TabelaPerfilPadrao tabela) {
        List<ItemTabPerPadrao> itemTabPerfilPadraoList =
                itemTabPerPadraoService
                        .findItemTabPerPadraoByTabelaPerfilPadrao(tabela);
        return itemTabPerfilPadraoList;
    }

    /**
     * Carrega a lista de Pmg para combobox.
     * 
     * @param listaPmg
     *            lista
     */
    private void loadPmgList(final List<Pmg> listaPmg) {
        Map<String, Long> pmgMap = new HashMap<String, Long>();
        List<String> listaPmgCombo = new ArrayList<String>();

        for (Pmg pmg : listaPmg) {
            pmgMap.put(pmg.getNomePmg(), pmg.getCodigoPmg());
            listaPmgCombo.add(pmg.getNomePmg());
        }

        bean.setListaPmgCombobox(listaPmgCombo);
        beanItemTabPerPadrao.setMapPmg(pmgMap);
    }

    /**
     * Carrega a lista de cidadeBase para combobox.
     * 
     * @param listaCidadeBase
     *            lista
     */
    private void loadBaseList(final List<CidadeBase> listaCidadeBase) {
        Map<String, Long> baseMap = new HashMap<String, Long>();
        List<String> listabase = new ArrayList<String>();

        for (CidadeBase base : listaCidadeBase) {
            baseMap.put(base.getSiglaCidadeBase(), base.getCodigoCidadeBase());
            listabase.add(base.getSiglaCidadeBase());
        }

        bean.setListaCidadeBaseCombobox(listabase);
        beanItemTabPerPadrao.setMapBase(baseMap);
    }

    /**
     * Carrega lista de moeda para combo e seta map.
     * 
     * @param listaMoeda
     *            lista de moeda
     */
    private void loadMoedaList(final List<Moeda> listaMoeda) {
        Map<String, Long> moedaMap = new HashMap<String, Long>();
        List<String> listaMoedaCombo = new ArrayList<String>();

        for (Moeda moeda : listaMoeda) {
            moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            listaMoedaCombo.add(moeda.getNomeMoeda());
        }

        bean.setMapMoeda(moedaMap);
        bean.setListaMoedaCombobox(listaMoedaCombo);
    }

    /**
     * Cancela edicao da tabelaPerPadrao.
     */
    public void cancelEdit() {
        bean.resetBean();
        bean.setListaTabelaPerfilPadraoAll(tabelaPerfilPadraoService
                .findTabelaPerfilPadraoAll());
        bean.setFlagEdicaoTabelaPerPadrao(Boolean.valueOf(false));
    }

    /**
     * Lista todas as entidades para a view.
     * 
     * @return lista
     * @param tabela
     *            tabela
     */
    public List<ItemTabPerPadrao> listaItemTabelaPerfilPadrao(
            final TabelaPerfilPadrao tabela) {
        List<ItemTabPerPadrao> listAll =
                this.listaItemTabPerfilPadraoByTabela(tabela);
        List<ItemTabPerPadrao> listResult = new ArrayList<ItemTabPerPadrao>();

        // Monta Vo para tabela da view.

        for (ItemTabPerPadrao item : listAll) {

            /*
             * if (item.getIndicadorAtivo().equals("A")) {
             * item.setIndicadorAtivo("Active"); } else {
             * item.setIndicadorAtivo("Inactive"); }
             */

            listResult.add(item);

        }

        return listResult;

    }

    /**
     * Troca o status do item da tabela padrao.
     */
    /*
     * public void trocaStatus() {
     * 
     * ItemTabPerPadrao item =
     * itemTabPerPadraoService.findItemTabPerPadraoById(bean
     * .getItemTabPerPadrao().getCodigoItemTabPerPadrao());
     * 
     * if (item.getIndicadorAtivo().equals("A")) { item.setIndicadorAtivo("I");
     * } else { item.setIndicadorAtivo("A"); } if
     * (itemTabPerPadraoService.updateItemTabPerPadrao(item)) {
     * Messages.showSucess("TabelaPerfilPadrao",
     * Constants.GENEREC_MSG_SUCCESS_UPDATE); }
     * 
     * itemTabPerPadraoService.findItemTabPerPadraoById(item
     * .getCodigoItemTabPerPadrao());
     * 
     * beanItemTabPerPadrao.setListaItemTabPerPadrao(this
     * .listaItemTabelaPerfilPadrao(item.getTabelaPerfilPadrao())); }
     */

    /**
     * Carrega lista para combo e seta map.
     * 
     * @param lista
     *            lista
     */
    private void loadCargoPmsList(final List<CargoPms> lista) {
        Map<String, Long> mapCargoPms = new HashMap<String, Long>();
        List<String> listaCombo = new ArrayList<String>();

        for (CargoPms cargoPms : lista) {
            mapCargoPms.put(cargoPms.getNomeCargoPms(), cargoPms
                    .getCodigoCargoPms());
            listaCombo.add(cargoPms.getNomeCargoPms());
        }

        beanItemTabPerPadrao.setMapCargo(mapCargoPms);
        bean.setListaCargoCombobox(listaCombo);
    }

    public void deleteItemTabPerPadrao() {
        ItemTabPerPadrao item =
                itemTabPerPadraoService
                        .findItemTabPerPadraoById(beanItemTabPerPadrao.getTo()
                                .getCodigoItemTabPerPadrao());

        if (itemTabPerPadraoService.removeItemTabPerPadrao(item)) {
            Messages.showSucess("removeItem",
                    Constants.GENEREC_MSG_SUCCESS_REMOVE);
        }

        beanItemTabPerPadrao.setListaItemTabPerPadrao(this
                .listaItemTabelaPerfilPadrao(item.getTabelaPerfilPadrao()));

    }

}
