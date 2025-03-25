package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.event.ValueChangeEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IDespesaMesService;
import com.ciandt.pms.business.service.IMapaDespesaService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.control.jsf.bean.MapaDespesaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.vo.MapaDespesaRow;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe DespesaController proporciona as funcionalidades da camada de
 * apresentação referente a entidade Despesa.
 * 
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"ROLE_PMS_ADMIN", "ROLE_PMS_SR_MANAGER"})
public class MapaDespesaController {

    /** outcome tela gerenciamento da entidade. */
    private static final String OUTCOME_MANAGE = "mapaDespesa_manage";

    /** outcome tela add da entidade. */
    private static final String OUTCOME_ADD = "mapaDespesa_add";

    /** outcome tela search da entidade. */
    private static final String OUTCOME_SEARCH = "mapaDespesa_research";

    /** outcome tela de visualização. */
    private static final String OUTCOME_VIEW = "mapaDespesa_view";

    /** Instancia da entidade DespesaService. */
    @Autowired
    private MapaDespesaBean despBean;

    /** Instancia da entidade DespesaService. */
    @Autowired
    private IMapaDespesaService mapDespService;

    /** Instancia da entidade DespesaMesService. */
    @Autowired
    private IDespesaMesService despMesService;

    /** Instancia da entidade ContratoPraticaService. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /** Instancia do servico ClienteService. */
    @Autowired
    private IClienteService clienteService;

    /** Instancia do servico NaturezaCentroLucro. */
    @Autowired
    private INaturezaCentroLucroService naturezaService;

    /** Instancia do servico Contrato. */
    @Autowired
    private IMsaService contratoService;

    /** Instancia do servico Contrato. */
    @Autowired
    private ICentroLucroService centroLucroService;

    /**
     * @param despBean
     *            the despBean to set
     */
    public void setDespBean(final MapaDespesaBean despBean) {
        this.despBean = despBean;
    }

    /**
     * @return the despBean
     */
    public MapaDespesaBean getDespBean() {
        return despBean;
    }

    /**
     * Prepara a tela de view.
     * 
     * @return retorna a tela de view
     */
    public String prepareView() {
        this.manage();
        despBean.setIsRemove(Boolean.FALSE);

        return OUTCOME_VIEW;
    }

    /**
     * Prapara a tela de gerenciamento.
     * 
     * @return retorna para a página de gerencimento
     */
    public String prepareManage() {
        ContratoPratica cp = despBean.getContratoPratica();
        MapaDespesa mapaDesp = findMapaDespesaByContratoPratica(cp);
        List<MapaDespesaRow> rowList;
        Date startDate, endDate;

        if (mapaDesp != null) {
            rowList = mapDespService.prepareManageMapaDespesa(mapaDesp);

            startDate = despMesService.findDespesaMesMinDateByMapa(mapaDesp);
            endDate = despMesService.findDespesaMesMaxDateByMapa(mapaDesp);

            despBean.setDateList(DateUtil.getValidityDateList(startDate,
                    endDate));
        } else {
            startDate =
                    DateUtil.getDate(despBean.getMonthBeg(),
                            despBean.getYearBeg());
            endDate =
                    DateUtil.getDate(despBean.getMonthEnd(),
                            despBean.getYearEnd());
            mapaDesp = new MapaDespesa();
            mapaDesp.setContratoPratica(cp);

            rowList = mapDespService.prepareCreate(cp, startDate, endDate);
        }

        despBean.setTo(mapaDesp);
        despBean.setDateList(DateUtil.getValidityDateList(startDate, endDate));
        despBean.setMoeda(mapDespService.getMoedaMapaDespesa(cp));
        despBean.setDespesaRowList(rowList);

        calculateTotalPerMonth();

        return OUTCOME_MANAGE;
    }

    /**
     * Prepara para a tela de gerenciamento.
     * 
     * @return a tela de gerencimento
     */
    public String manage() {
        MapaDespesa mapaDesp =
                mapDespService.findMapaDespesaById(despBean.getTo()
                        .getCodigoMapaDespesa());

        List<MapaDespesaRow> rowList =
                mapDespService.prepareManageMapaDespesa(mapaDesp);

        Date startDate = despMesService.findDespesaMesMinDateByMapa(mapaDesp);
        Date endDate = despMesService.findDespesaMesMaxDateByMapa(mapaDesp);
        despBean.setMonthBeg(DateUtil.getMonthString(startDate));
        despBean.setYearBeg(DateUtil.getYearString(startDate));
        despBean.setMonthEnd(DateUtil.getMonthString(endDate));
        despBean.setYearEnd(DateUtil.getYearString(endDate));

        despBean.setTo(mapaDesp);
        despBean.setDateList(DateUtil.getValidityDateList(startDate, endDate));
        despBean.setMoeda(mapDespService.getMoedaMapaDespesa(mapaDesp
                .getContratoPratica()));
        despBean.setDespesaRowList(rowList);

        calculateTotalPerMonth();

        return OUTCOME_MANAGE;
    }

    /**
     * Prepara para a criação de um Mapa de despesas.
     * 
     * @return retorna a pagina de criação(add).
     */
    public String prepareCreate() {
        despBean.reset();

        loadContratoPraticaList(contratoPraticaService
                .findContratoPraticaAllComplete());

        return OUTCOME_ADD;
    }

    /**
     * Prepara a tela de manage do Mapa de Despesa.
     * 
     * @return retorna a pagina de manage
     */
    public String prepareCreateManage() {
        ContratoPratica cp =
                contratoPraticaService.findContratoPraticaById(despBean
                        .getContratoPraticaMap().get(
                                despBean.getContratoPratica()
                                        .getNomeContratoPratica()));

        if (mapDespService.existsMapaDespesa(cp)) {
            Messages.showError("prepareCreateManage",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_MAPA_DESPESA);

            return null;
        } else {
            despBean.setContratoPratica(cp);

            return prepareManage();
        }
    }

    /**
     * Salva/Atuliza o mapa de despesas.
     */
    public void saveMapaDespesa() {
        MapaDespesa mapaDesp = despBean.getTo();

        boolean result =
                mapDespService.saveMapaDespesa(mapaDesp,
                        despBean.getDespesaRowList());

        if (result) {
            // mensagem de sucesso
            Messages.showSucess("saveMapaDespesa",
                    Constants.DEFAULT_MSG_SUCCESS_SAVE,
                    Constants.ENTITY_NAME_MAPA_DESPESA);

            despBean.setDespesaRowList(mapDespService
                    .prepareManageMapaDespesa(mapaDesp));

            calculateTotalPerMonth();
        }

    }

    /**
     * Prepara a teal de remoção do mapa de despesa.
     * 
     * @return retorna a pagina de view
     */
    public String prepareRemoveMapaDespesa() {
        this.prepareView();

        despBean.setIsRemove(Boolean.TRUE);

        return OUTCOME_VIEW;
    }

    /**
     * Deleta o mapa de despesa.
     * 
     * @return retorna a pagina de search
     */
    public String deleteMapaDespesa() {

        try {
            if (mapDespService.removeMapaDespesa(despBean.getTo())) {
                // mensagem de sucesso
                Messages.showSucess("saveMapaDespesa",
                        Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                        Constants.ENTITY_NAME_MAPA_DESPESA);
            }
        } catch (DataIntegrityViolationException e) {
            // caso não passe na validação,
            // capitura a exceção e dá mensagem de erro
            Messages.showError("remove",
                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_MAPA_DESPESA);
        }

        this.findByFilter();

        return OUTCOME_SEARCH;
    }

    /**
     * Realiza o filtro e retorna para a página de filtro.
     * 
     * @return retorna a páginade filtro.
     */
    public String findByFilterBack() {
        this.findByFilter();

        return OUTCOME_SEARCH;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        despBean.reset();

        this.loadContratoPraticaList(contratoPraticaService
                .findContratoPraticaAllComplete());

        this.loadClienteList(clienteService.findClienteAllClientePai());
        this.loadMsaList(contratoService.findMsaAll());
        this.loadNaturezaList(naturezaService.findNaturezaCentroLucroAll());

        return OUTCOME_SEARCH;
    }

    /**
     * Altera o periodo do mapa de despesa.
     */
    public void changePeriod() {

        Date startDate =
                DateUtil.getDate(despBean.getMonthBeg(), despBean.getYearBeg());
        Date endDate =
                DateUtil.getDate(despBean.getMonthEnd(), despBean.getYearEnd());

        List<MapaDespesaRow> rowList =
                mapDespService.mapaDespesachangePeriod(despBean.getTo(),
                        startDate, endDate);

        despBean.setDespesaRowList(rowList);
        despBean.setDateList(DateUtil.getValidityDateList(startDate, endDate));
        calculateTotalPerMonth();
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        MapaDespesa filter = despBean.getFilter();
        Long contratoPraticaId =
                despBean.getContratoPraticaMap().get(
                        filter.getContratoPratica().getNomeContratoPratica());

        if (contratoPraticaId != null) {
            filter.getContratoPratica().setCodigoContratoPratica(
                    contratoPraticaId);

        } else if (!"".equals(filter.getContratoPratica()
                .getNomeContratoPratica())) {
            filter.getContratoPratica().setCodigoContratoPratica(-1L);

        } else {
            filter.getContratoPratica().setCodigoContratoPratica(0L);
        }

        Cliente cli = despBean.getCliente();
        cli.setCodigoCliente(despBean.getClienteMap().get(cli.getNomeCliente()));

        Msa msa = despBean.getMsa();
        msa.setCodigoMsa(despBean.getMsaMap().get(msa.getNomeMsa()));

        NaturezaCentroLucro natureza = despBean.getNatureza();
        natureza.setCodigoNatureza(despBean.getNaturezaMap().get(
                natureza.getNomeNatureza()));

        CentroLucro centroLucro = despBean.getCentroLucro();
        centroLucro.setCodigoCentroLucro(despBean.getCentroLucroMap().get(
                centroLucro.getNomeCentroLucro()));

        // realiza a busca pelo MapaAlocacao
        despBean.setResultList(mapDespService.findMapaDespesaByFilter(filter,
                cli, msa, natureza, centroLucro));
        // se nenhum resultado encontrado
        if (despBean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina
        despBean.setCurrentPageId(0);
    }

    /**
     * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
     * 
     * @param e
     *            - evento de mudança
     */
    public void prepareCentroLucroCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null && !"".equals(value)) {
            Long codNatureza = despBean.getNaturezaMap().get(value);
            NaturezaCentroLucro natureza = null;

            // se o centro de lucro existir
            if (codNatureza != null) {
                natureza =
                        naturezaService
                                .findNaturezaCentroLucroById(codNatureza);
                this.loadCentroLucroList(centroLucroService
                        .findCentroLucroByNatureza(natureza));
                // senao existir cria uma lista vazia de centro-lucro
            } else {
                this.loadCentroLucroList(new ArrayList<CentroLucro>());
            }
        } else {
            this.loadCentroLucroList(centroLucroService.findCentroLucroAll());
        }
    }

    /**
     * Popula o combobox do contrato-pratica de acordo com o contrato
     * selecionado.
     * 
     * @param e
     *            - evento de mudança
     */
    public void prepareContratoPraticaCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null && !"".equals(value)) {
            Long codContrato = despBean.getMsaMap().get(value);
            Msa msa = null;

            // se o centro de lucro existir
            if (codContrato != null) {
                msa = contratoService.findMsaById(codContrato);
                this.loadContratoPraticaList(contratoPraticaService
                        .findContratoPraticaByContrato(msa));
                // senao existir cria uma lista vazia de centro-lucro
            } else {
                this.loadContratoPraticaList(new ArrayList<ContratoPratica>());
            }
        } else {
            this.loadContratoPraticaList(contratoPraticaService
                    .findContratoPraticaAllComplete());
        }
    }

    /**
     * Popula o combobox do contrato de acordo com o cliente selecionado.
     * 
     * @param e
     *            - evento de mudança
     */
    public void prepareContratoCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null && !"".equals(value)) {
            Long codCli = despBean.getClienteMap().get(value);
            Cliente cliente = null;

            // se o centro de lucro existir
            if (codCli != null) {
                cliente = clienteService.findClienteById(codCli);
                this.loadMsaList(contratoService.findMsaByCliente(cliente));
                // senao existir cria uma lista vazia de centro-lucro
            } else {
                this.loadMsaList(new ArrayList<Msa>());
            }
        } else {
            this.loadMsaList(contratoService.findMsaAll());
        }
    }

    /**
     * Busca o mada de despesa pelo contrato pratica.
     * 
     * @param cp
     *            - entidade do tipo ContratoPratica.
     * 
     * @return retorna o mapa de despesa, caso não exista retorna null.
     */
    private MapaDespesa findMapaDespesaByContratoPratica(
            final ContratoPratica cp) {
        ContratoPratica contratoPratica =
                contratoPraticaService.findContratoPraticaById(cp
                        .getCodigoContratoPratica());

        MapaDespesa mapaDesp = null;

        if (cp != null) {
            mapaDesp =
                    mapDespService
                            .findMapaDespesaByContratoPratica(contratoPratica);
        }

        return mapaDesp;
    }

    /**
     * Calcula os totais por mes.
     */
    private void calculateTotalPerMonth() {
        List<MapaDespesaRow> despesaRowList = despBean.getDespesaRowList();

        int listSize = despBean.getDateList().size();

        List<BigDecimal> totalPerMonthList =
                getInitialTotalPerMonthList(listSize);

        BigDecimal valorReembolso, valorDespesa;

        for (int i = 0; i < listSize; i++) {
            for (MapaDespesaRow row : despesaRowList) {
                BigDecimal total = totalPerMonthList.get(i);

                valorDespesa =
                        row.getDespesaDebitoList().get(i).getValorDespesa();

                valorReembolso =
                        row.getDespesaCreditoList().get(i).getValorDespesa();
                if (Constants.DESPESA_INDICADOR_UNIDADE_PERCENTUAL.equals(row
                        .getUnidadeReembolso())) {
                    valorReembolso =
                            BigDecimal.valueOf(
                                    valorDespesa.doubleValue()
                                            * valorReembolso.doubleValue())
                                    .setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                totalPerMonthList.set(i,
                        total.add(valorDespesa.subtract(valorReembolso)));
            }
        }

        despBean.setTotalPerMonthList(totalPerMonthList);
    }

    /**
     * Retorna a lista de totais inicializada com zeros.
     * 
     * @param size
     *            numero de elementos da lista.
     * 
     * @return retorna uma lista de BigDecimal
     */
    private List<BigDecimal> getInitialTotalPerMonthList(final int size) {
        List<BigDecimal> totalPerMonthList = new ArrayList<BigDecimal>(size);
        for (int i = 0; i < size; i++) {
            totalPerMonthList.add(BigDecimal.valueOf(0.0));
        }

        return totalPerMonthList;
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

        despBean.setContratoPraticaMap(contratoPraticaMap);
        despBean.setContratoPraticaList(contratoPraticaList);
    }

    /**
     * Popula a lista de Msa para combobox.
     * 
     * @param msas
     *            lista de Msa.
     * 
     */
    private void loadMsaList(final List<Msa> msas) {

        Map<String, Long> msaMap = new HashMap<String, Long>();
        List<String> msaList = new ArrayList<String>();

        for (Msa msa : msas) {
            msaMap.put(msa.getNomeMsa(), msa.getCodigoMsa());
            msaList.add(msa.getNomeMsa());
        }

        despBean.setMsaMap(msaMap);
        despBean.setMsaList(msaList);
    }

    /**
     * Popula a lista de centroLucros para combobox.
     * 
     * @param centroLucros
     *            lista de contratos.
     * 
     */
    private void loadCentroLucroList(final List<CentroLucro> centroLucros) {

        Map<String, Long> centroLucroMap = new HashMap<String, Long>();
        List<String> centroLucroList = new ArrayList<String>();

        for (CentroLucro centroLucro : centroLucros) {
            centroLucroMap.put(centroLucro.getNomeCentroLucro(),
                    centroLucro.getCodigoCentroLucro());
            centroLucroList.add(centroLucro.getNomeCentroLucro());
        }

        despBean.setCentroLucroMap(centroLucroMap);
        despBean.setCentroLucroList(centroLucroList);
    }

    /**
     * Popula a lista de Natureza para combobox.
     * 
     * @param naturezas
     *            lista de NaturezaCentroLucro.
     * 
     */
    private void loadNaturezaList(final List<NaturezaCentroLucro> naturezas) {

        Map<String, Long> naturezaMap = new HashMap<String, Long>();
        List<String> naturezaList = new ArrayList<String>();

        for (NaturezaCentroLucro natureza : naturezas) {
            naturezaMap.put(natureza.getNomeNatureza(),
                    natureza.getCodigoNatureza());
            naturezaList.add(natureza.getNomeNatureza());
        }

        despBean.setNaturezaMap(naturezaMap);
        despBean.setNaturezaList(naturezaList);
    }

    /**
     * Popula a lista de clientes para combobox.
     * 
     * @param clientes
     *            lista de clientes.
     * 
     */
    private void loadClienteList(final List<Cliente> clientes) {

        Map<String, Long> clienteMap = new HashMap<String, Long>();
        List<String> clienteList = new ArrayList<String>();

        for (Cliente cliente : clientes) {
            clienteMap
                    .put(cliente.getNomeCliente(), cliente.getCodigoCliente());
            clienteList.add(cliente.getNomeCliente());
        }
        despBean.setClienteMap(clienteMap);
        despBean.setClienteList(clienteList);
    }

}
