package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IValidacaoPessoaService;
import com.ciandt.pms.control.jsf.bean.ValidacaoPessoaBean;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.ValidacaoPessoaRow;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe ValidacaoPessoaController proporciona as funcionalidades da camada
 * de controle.
 * 
 * @since 04/12/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "PRO.RESOURCE_VALIDATION:VW" })
public class ValidacaoPessoaController {

    /** Instancia do servico ValidacaoPessoa. */
    @Autowired
    private IValidacaoPessoaService validacaoService;

    /** Instancia do servico Modulo. */
    @Autowired
    private IModuloService moduloService;

    /** Instancia do servico MapaAloacacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    /** Instancia do servico PerfilVendido. */
    @Autowired
    private IPerfilVendidoService perfilVendidoService;

    /** Instancia do servico Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** Bean ValidacaoPessoaBean. */
    @Autowired
    private ValidacaoPessoaBean bean;

    /*********** OUTCOMES **************************/

    /** outcome tela busca da entidade. */
    private static final String OUTCOME_VALIDACAO_PESSOA_RESEARCH = "validacaoPessoa_research";

    /** outcome tela busca do gerente senior da entidade. */
    private static final String OUTCOME_VALIDACAO_PESSOA_RESEARCH_SENIOR = "validacaoPessoa_research_sr";

    /** outcome tela edição da entidade. */
    private static final String OUTCOME_VALIDACAO_PESSOA_MANAGE = "validacaoPessoa_manage";

    /***********************************************/

    /** Padrao de formatacao das datas. */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
            "MMM/yyyy", new Locale("en/US"));

    /**
     * Ação que realiza a busca de todos os gerenciados do login corrente.
     * 
     * @return retorna a pagina de destino.
     */
    public String findAllMyManaged() {
        bean.reset();

        // carrega o combo com as datas possiveis para validacao
        bean.setDataMesList(DateUtil.getDateMonthListString(moduloService
                .getClosingDateMapaAlocacao(), SIMPLE_DATE_FORMAT));

        // atribui a primeira data para busca automatica das pessoas gerenciadas
        // pelo login corrente
        bean.setSelectedMonthDate(SIMPLE_DATE_FORMAT.format(DateUtil
                .getNextMonth(moduloService.getClosingDateMapaAlocacao())));

        return findAllMyManagedByDataMes();
    }

    /**
     * Ação que realiza a busca de todos os gerenciados do login corrente pela
     * data.
     * 
     * @return retorna a pagina de destino.
     */
    public String findAllMyManagedByDataMes() {

        Date data = DateUtil.getDateParse(bean.getSelectedMonthDate(),
                SIMPLE_DATE_FORMAT);

        bean.setRowResultList(validacaoService.findAllMyManaged(data));

        bean.setDelegatedResultList(validacaoService.findAllMyDelegated(data));

        // bean.setCurrentMonthDate(this.getCurrentMonthDate());
        bean.setRedirectPage(OUTCOME_VALIDACAO_PESSOA_RESEARCH);

        if (bean.getRowResultList().size() == 0) {
            Messages.showWarning("findAllMyManagedByDataMes",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        return OUTCOME_VALIDACAO_PESSOA_RESEARCH;
    }

    /**
     * Ação que prapara a tela de research.
     * 
     * @return tela de filtro.
     */
    public String prepareResearch() {
        bean.reset();

        this.loadPessoaList(pessoaService.findAllManager());

        // carrega o combo com as datas possiveis para validacao
        bean.setDataMesList(DateUtil.getDateMonthListString(moduloService
                .getClosingDateMapaAlocacao(), SIMPLE_DATE_FORMAT));

        // bean.setCurrentMonthDate(this.getCurrentMonthDate());
        bean.setRedirectPage(OUTCOME_VALIDACAO_PESSOA_RESEARCH_SENIOR);

        // caso seja a primeira vez que entra na tela de validação
        if (bean.getTo().getCodigoLogin() != null) {
            filterByManagerAndDataMes();
        } else {
            bean.setRowResultList(new ArrayList<ValidacaoPessoaRow>());
        }

        return OUTCOME_VALIDACAO_PESSOA_RESEARCH_SENIOR;
    }

    /**
     * Filtra todos as pessoa, referente ao gerente selecionado.
     * 
     * @return tela de filtro.
     */
    public String filterByManagerAndDataMes() {
        bean.setRowResultList(validacaoService.findByManagerAndDataMes(bean
                .getTo().getCodigoLogin(), DateUtil.getDateParse(bean
                .getSelectedMonthDate(), SIMPLE_DATE_FORMAT)));

        if (bean.getRowResultList().size() == 0) {
            Messages.showWarning("findAllMyManagedByDataMes",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        return OUTCOME_VALIDACAO_PESSOA_RESEARCH_SENIOR;
    }

    /**
     * Prepara a tela de gerenciamento.
     * 
     * @return a pagina de gerenciamento.
     */
    public String prepareManage() {
        ValidacaoPessoaRow row = bean.getRow();
        Date selectedMonthDate = DateUtil.getDateParse(bean
                .getSelectedMonthDate(), SIMPLE_DATE_FORMAT);

        List<AlocacaoPeriodo> alocacaoPeriodoList = validacaoService
                .getAlocacaoPeriodoList(row.getPessoa(), selectedMonthDate);

        bean.setAlocacaoPeriodoList(alocacaoPeriodoList);

        List<AlocacaoPeriodoOh> alocacaoPeriodoOhList = validacaoService
                .getAlocacaoPeriodoOhList(pessoaService.findPessoaById(row
                        .getPessoa().getCodigoPessoa()), selectedMonthDate);

        bean.setAlocacaoPeriodoOhList(alocacaoPeriodoOhList);

        this.calculateTotalFTE();

        return OUTCOME_VALIDACAO_PESSOA_MANAGE;
    }

    /**
     * Prepara a Tela para adicionar um periodo alocacao.
     */
    public void prepareAddAlocacao() {
        bean.resetAdd();
        this.loadMapaAlocacaoList(mapaAlocacaoService
                .findMapaAlocaoAllPublished());
    }

    /**
     * Pega a lista de perfil vendido do mapa alocacao (contrato-pratica).
     * 
     * @param e
     *            - evento de mudança
     */
    public void getPerfilVendidoByMapaAlocacao(final ValueChangeEvent e) {
        String nameMapaAlocacao = (String) e.getNewValue();
        Long codMapaAlocacao;

        if (nameMapaAlocacao != null) {
            codMapaAlocacao = bean.getMapaAlocacaoMap().get(nameMapaAlocacao);
            MapaAlocacao mapa = mapaAlocacaoService
                    .findMapaAlocacaoById(codMapaAlocacao);
            ContratoPratica cp = mapa.getContratoPratica();

            this.loadPerfilVendidoList(perfilVendidoService
                    .findByContratoPraticaAndActive(cp));
        }
    }

    /**
     * Adiciona uma alocação na lista de alocações periodos.
     */
    public void addAlocacao() {
        Long idMapaAlocacao = bean.getMapaAlocacaoMap().get(
                bean.getNomeMapaAlocacao());
        Long idPerfilVendido = bean.getPerfilVendidoMap().get(
                bean.getNomePerfilVendido());

        MapaAlocacao mapa = mapaAlocacaoService
                .findMapaAlocacaoById(idMapaAlocacao);
        PerfilVendido pv = perfilVendidoService
                .findPerfilVendidoById(idPerfilVendido);

        AlocacaoPeriodo alocacaoPeriodo = validacaoService.addAlocacao(bean
                .getRow().getPessoa(), mapa, pv, bean.getPercentualAlocacao(),
                DateUtil.getDateParse(bean.getSelectedMonthDate(),
                        SIMPLE_DATE_FORMAT));

        // bean.getAlocacaoPeriodoList().add(alocacao.getAlocacaoPeriodos().get(0));
        bean.getAlocacaoPeriodoList().add(alocacaoPeriodo);

        this.calculateTotalFTE();
    }

    /**
     * Remove a validação das alocações selecionadas.
     */
    public void removeValidateAll() {
        List<ValidacaoPessoaRow> rowResultList = bean.getRowResultList();

        List<ValidacaoPessoaRow> delegatedResultList = bean
                .getDelegatedResultList();

        for (ValidacaoPessoaRow row : rowResultList) {
            if (row.getIsSelected()) {

                // verifica se a dataMes (data corrente) for igual ao mes
                // validado, ou seja, se o mes a ser removido for o ultimo mes
                // (mes
                // validado) remove normalmente. Caso contrario, msg de erro e
                // nao
                // remove
                Pessoa pessoa = row.getPessoa();
                Date dataMesValidado = pessoa.getDataMesValidado();
                Date selectMonthDate = DateUtil.getDateParse(bean
                        .getSelectedMonthDate(), SIMPLE_DATE_FORMAT);
                if ((dataMesValidado != null)
                        && (selectMonthDate.compareTo(dataMesValidado) == 0)) {
                    validacaoService.removeValidate(pessoa, selectMonthDate);
                } else {
                    if (dataMesValidado != null) {
                        Messages
                                .showError(
                                        "removeValidate",
                                        Constants.VALIDATE_PERSON_ERROR_REMOVE_NO_LAST_VALID_MONTH,
                                        new Object[] {
                                                pessoa.getCodigoLogin(),
                                                ": "
                                                        + SIMPLE_DATE_FORMAT
                                                                .format(dataMesValidado) });
                    } else {
                        Messages
                                .showError(
                                        "removeValidate",
                                        Constants.VALIDATE_PERSON_ERROR_REMOVE_NO_LAST_VALID_MONTH,
                                        new Object[] {pessoa.getCodigoLogin(),
                                                "" });
                    }
                }

            }
        }

        for (ValidacaoPessoaRow delegated : delegatedResultList) {
            if (delegated.getIsSelected()) {

                // verifica se a dataMes (data corrente) for igual ao mes
                // validado, ou seja, se o mes a ser removido for o ultimo mes
                // (mes
                // validado) remove normalmente. Caso contrario, msg de erro e
                // nao
                // remove
                Pessoa pessoa = delegated.getPessoa();
                Date dataMesValidado = pessoa.getDataMesValidado();
                Date selectMonthDate = DateUtil.getDateParse(bean
                        .getSelectedMonthDate(), SIMPLE_DATE_FORMAT);
                if ((dataMesValidado != null)
                        && (selectMonthDate.compareTo(dataMesValidado) == 0)) {
                    validacaoService.removeValidate(pessoa, selectMonthDate);
                } else {
                    if (dataMesValidado != null) {
                        Messages
                                .showError(
                                        "removeValidate",
                                        Constants.VALIDATE_PERSON_ERROR_REMOVE_NO_LAST_VALID_MONTH,
                                        new Object[] {
                                                pessoa.getCodigoLogin(),
                                                ": "
                                                        + SIMPLE_DATE_FORMAT
                                                                .format(dataMesValidado) });
                    } else {
                        Messages
                                .showError(
                                        "removeValidate",
                                        Constants.VALIDATE_PERSON_ERROR_REMOVE_NO_LAST_VALID_MONTH,
                                        new Object[] {pessoa.getCodigoLogin(),
                                                "" });
                    }
                }

            }
        }

        if (OUTCOME_VALIDACAO_PESSOA_RESEARCH_SENIOR.equals(bean
                .getRedirectPage())) {
            this.filterByManagerAndDataMes();
        } else {
            this.findAllMyManagedByDataMes();
        }
    }

    /**
     * Auto completa os percentuais alocados para o limite de percentual
     * alocavel da Pessoa.
     * 
     */
    public void autoComplete() {
        // total alocado
        double totalAlocacao = bean.getTotalAlocation().doubleValue();

        Pessoa pessoa = bean.getRow().getPessoa();
        // percentual máximo alocável da Pessoa
        double percentualAlocavel = bean.getRow().getPercentualDisponivelMes()
                .doubleValue();

        // login da Pessoa
        String codigoLogin = pessoa.getCodigoLogin();

        // se o total alocado >= percentual máximo alocável não faz nada, apenas
        // msg
        if (totalAlocacao >= percentualAlocavel) {
            Messages.showWarning("autoComplete",
                    Constants.MSG_WARNG_AUTO_COMPLETE_PESSOA_TOTAL_OK,
                    codigoLogin);
            // senao, se o total alocado for entre 0% e máximo percentual
            // alocavel, completa proporcionalmente
        } else {
            if (totalAlocacao > 0) {
                List<AlocacaoPeriodo> alocacaoPeriodoList = bean
                        .getAlocacaoPeriodoList();
                // se a qtde de registros for 1, acrescenta o restante
                // normalmente
                if (alocacaoPeriodoList.size() == 1) {
                    AlocacaoPeriodo alocacaoPeriodo = alocacaoPeriodoList
                            .get(0);
                    double percentualAlocacaoPeriodo = alocacaoPeriodo
                            .getPercentualAlocacaoPeriodo().doubleValue();
                    double rest = percentualAlocavel - totalAlocacao;
                    alocacaoPeriodo.setPercentualAlocacaoPeriodo(BigDecimal
                            .valueOf(percentualAlocacaoPeriodo + rest));
                    // se a qtde de reg for maior do que 1, faz a redistribuição
                } else if (alocacaoPeriodoList.size() > 1) {
                    for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoList) {
                        double percentualAlocacaoPeriodo = alocacaoPeriodo
                                .getPercentualAlocacaoPeriodo().doubleValue();
                        double calcRedistribution = percentualAlocacaoPeriodo
                                / totalAlocacao
                                * (percentualAlocavel - totalAlocacao);
                        alocacaoPeriodo.setPercentualAlocacaoPeriodo(BigDecimal
                                .valueOf(percentualAlocacaoPeriodo
                                        + calcRedistribution));
                    }
                }
                // se o total alocado for igual a 0 tras o mes anterior.
                // Caso nao encontre nenhuma alocacao no mes anterior, mostra
                // msg
            } else if (totalAlocacao == 0) {
                // Recurso recurso = pessoa.getRecurso();
                // Date closingDate = getClosingDate();
                // List<AlocacaoPeriodo> alocPerListPrevMonth =
                // alocacaoPeriodoService.findAlocacaoPeriodoByRecursoAndDate(recurso,
                // closingDate);
                List<AlocacaoPeriodo> alocPerListPrevMonth = validacaoService
                        .getAlocacaoPeriodoList(pessoaService
                                .findPessoaById(pessoa.getCodigoPessoa()),
                                DateUtil.getDateParse(bean
                                        .getSelectedMonthDate(),
                                        SIMPLE_DATE_FORMAT));

                if (alocPerListPrevMonth.size() > 0) {
                    bean.setAlocacaoPeriodoList(alocPerListPrevMonth);
                } else {
                    Messages
                            .showWarning(
                                    "autoComplete",
                                    Constants.MSG_WARNG_AUTO_COMPLETE_PESSOA_NO_RESULT_PREV_MONTH,
                                    codigoLogin);
                }
            }

            // recalcula os totais
            calculateTotalFTE();
            bean.getRow().setPercentualAlocado(bean.getTotalAlocation());
        }
    }

    /**
     * Realiza a validação de uma pessoa.
     * 
     * @return a pagina de destino.
     */
    public String validate() {
        // if (this.validatePessoa(bean.getRow().getPessoa(),
        // bean.getAlocacaoPeriodoList())) {

        Pessoa pessoa = pessoaService.findPessoaById(bean.getRow().getPessoa()
                .getCodigoPessoa());
        Date selectedMonthDate = DateUtil.getDateParse(bean
                .getSelectedMonthDate(), SIMPLE_DATE_FORMAT);

        if (this.preValidatePessoa(pessoa, selectedMonthDate)) {

            // independente se a linha corrente já estiver no estado validado,
            // remove a
            // validacao, remove os CustoRealizado e CustoTceGrupocusto (caso
            // houver) e valida novamente
            // if (bean.getRow().getIsMesValidado()) {
            validacaoService.removeValidate(pessoa, DateUtil.getDateParse(bean
                    .getSelectedMonthDate(), SIMPLE_DATE_FORMAT));
            // }

            if (validacaoService.validatePessoa(pessoa, bean
                    .getAlocacaoPeriodoList(), DateUtil.getDateParse(bean
                    .getSelectedMonthDate(), SIMPLE_DATE_FORMAT))) {
                Messages.showSucess("validateAll",
                        Constants.VALIDATE_PERSON_SUCCESS_VALIDATE);

                if (OUTCOME_VALIDACAO_PESSOA_RESEARCH_SENIOR.equals(bean
                        .getRedirectPage())) {

                    this.filterByManagerAndDataMes();
                    return OUTCOME_VALIDACAO_PESSOA_RESEARCH_SENIOR;

                } else {
                    return this.findAllMyManagedByDataMes();
                }
            } else {
                return null;
            }

        } else {
            return null;
        }

        /*
         * } else { Messages.showError("validateAll",
         * Constants.VALIDATE_PERSON_ERROR_VALIDATE, bean.getRow()
         * .getPessoa().getCodigoLogin()); return null; }
         */
    }

    /**
     * Valida todas as alocações selecionada.
     */
    public void validateAll() {
        List<ValidacaoPessoaRow> rowResultList = bean.getRowResultList();
        List<ValidacaoPessoaRow> delegatedResultList = bean
                .getDelegatedResultList();
        Date selectedMonthDate = DateUtil.getDateParse(bean
                .getSelectedMonthDate(), SIMPLE_DATE_FORMAT);

        List<AlocacaoPeriodo> alocacaoPeriodoList;
        boolean success = true;

        for (ValidacaoPessoaRow row : rowResultList) {
            if (row.getIsSelected()) {
                Pessoa pessoa = pessoaService.findPessoaById(row.getPessoa()
                        .getCodigoPessoa());

                if (this.preValidatePessoa(pessoa, selectedMonthDate)) {
                    // independente se a linha corrente já estiver no estado
                    // validado, remove a
                    // validacao, remove os CustoRealizado e CustoTceGrupocusto
                    // (caso houver) e valida novamente
                    // if (row.getIsMesValidado()) {
                    validacaoService.removeValidate(pessoa, selectedMonthDate);
                    // }

                    alocacaoPeriodoList = validacaoService
                            .getAlocacaoPeriodoList(pessoa, selectedMonthDate);
                    /*
                     * if (!validatePessoa(row.getPessoa(),
                     * alocacaoPeriodoList)) { Messages.showError("validateAll",
                     * Constants.VALIDATE_PERSON_ERROR_VALIDATE, row
                     * .getPessoa().getCodigoLogin()); success = false; }
                     */
                    if (!validacaoService.validatePessoa(pessoa,
                            alocacaoPeriodoList, selectedMonthDate)) {
                        success = Boolean.valueOf(false);
                    }
                } else {
                    success = Boolean.valueOf(false);
                }
            }
        }

        for (ValidacaoPessoaRow delegated : delegatedResultList) {
            if (delegated.getIsSelected()) {
                Pessoa pessoa = pessoaService.findPessoaById(delegated
                        .getPessoa().getCodigoPessoa());

                if (this.preValidatePessoa(pessoa, selectedMonthDate)) {
                    // independente se a linha corrente já estiver no estado
                    // validado, remove a
                    // validacao, remove os CustoRealizado e CustoTceGrupocusto
                    // (caso houver) e valida novamente
                    // if (row.getIsMesValidado()) {
                    validacaoService.removeValidate(pessoa, selectedMonthDate);
                    // }

                    alocacaoPeriodoList = validacaoService
                            .getAlocacaoPeriodoList(pessoa, selectedMonthDate);
                    /*
                     * if (!validatePessoa(row.getPessoa(),
                     * alocacaoPeriodoList)) { Messages.showError("validateAll",
                     * Constants.VALIDATE_PERSON_ERROR_VALIDATE, row
                     * .getPessoa().getCodigoLogin()); success = false; }
                     */
                    if (!validacaoService.validatePessoa(pessoa,
                            alocacaoPeriodoList, selectedMonthDate)) {
                        success = Boolean.valueOf(false);
                    }
                } else {
                    success = Boolean.valueOf(false);
                }
            }
        }

        if (success) {
            Messages.showSucess("validateAll",
                    Constants.VALIDATE_PERSON_SUCCESS_VALIDATE);

            bean.setTotalAlocationStyle(BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_LABEL_STYLE_COLOR_GREEN));
        }

        if (OUTCOME_VALIDACAO_PESSOA_RESEARCH_SENIOR.equals(bean
                .getRedirectPage())) {
            this.filterByManagerAndDataMes();
        } else {
            this.findAllMyManagedByDataMes();
        }
    }

    /**
     * Calcula o FTE total de uma pessoa.
     */
    public void calculateTotalFTE() {
        Double total = Double.valueOf(0);

        List<AlocacaoPeriodo> alocacaoPeriodoList = bean
                .getAlocacaoPeriodoList();
        for (AlocacaoPeriodo alocacaoPerido : alocacaoPeriodoList) {
            total += alocacaoPerido.getPercentualAlocacaoPeriodo().setScale(2,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        List<AlocacaoPeriodoOh> alocacaoPeriodoOhList = bean
                .getAlocacaoPeriodoOhList();
        for (AlocacaoPeriodoOh alocacaoPeriodoOh : alocacaoPeriodoOhList) {
            total += alocacaoPeriodoOh.getPercentualAlocPeriodoOh().setScale(2,
                    BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        bean.setTotalAlocation(BigDecimal.valueOf(total));

        Pessoa pessoa = bean.getRow().getPessoa();
        // pega o percentual alocavel da pessoa no mês.
        double percentualAlocavel = bean.getRow().getPercentualDisponivelMes()
                .doubleValue();

        // Date dataMesValidado = pessoa.getDataMesValidado();
        Date selectedMonthDate = DateUtil.getDateParse(bean
                .getSelectedMonthDate(), SIMPLE_DATE_FORMAT);
        // se total < percentualAlocavel, busca no CustoRealizado
        // if (dataMesValidado != null && total < percentualAlocavel) {
        if (total < percentualAlocavel) {
            // verifica se existe validacao para a Pessoa no mes passado por
            // parametro. Se já validado mostra verde senao mostra vermelho
            // if (validacaoService.hasValidationForTheMonth(pessoa,
            // dataMesValidado)) {
            if (validacaoService.hasValidationForTheMonth(pessoa,
                    selectedMonthDate)) {
                bean
                        .setTotalAlocationStyle(BundleUtil
                                .getBundle(Constants.BUNDLE_KEY_LABEL_STYLE_COLOR_GREEN));
            } else {
                bean.setTotalAlocationStyle(BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_LABEL_STYLE_COLOR_RED));
            }
        } else if (total >= percentualAlocavel) {
            bean.setTotalAlocationStyle(BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_LABEL_STYLE_COLOR_GREEN));
        }
    }

    /**
     * Ação que aciona a validação de uma pessoa.
     * 
     * @param pessoa
     *            - Pessoa a ser validada
     * @param alocacaoPeriodoList
     *            - lista de alocações periodos.
     * 
     * @return retorna true se validado com sucesso, caso contrario false.
     */
    /*
     * private Boolean validatePessoa(final Pessoa pessoa, final
     * List<AlocacaoPeriodo> alocacaoPeriodoList) { return
     * validacaoService.validatePessoa(pessoa, alocacaoPeriodoList); }
     */

    /**
     * @return the bean
     */
    public ValidacaoPessoaBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final ValidacaoPessoaBean bean) {
        this.bean = bean;
    }

    /**
     * Verifica se o valor do atributo MapaAlocacao é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateMapaAlocacao(final FacesContext context,
            final UIComponent component, final Object value) {

        Long idMapaalocacao = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            idMapaalocacao = bean.getMapaAlocacaoMap().get(strValue);
            if (idMapaalocacao == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * Verifica se o valor do atributo MapaAlocacao é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validatePerfilVendido(final FacesContext context,
            final UIComponent component, final Object value) {

        Long idPerfilVendido = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            idPerfilVendido = bean.getPerfilVendidoMap().get(strValue);
            if (idPerfilVendido == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * Popula a lista de Perfil Vendido para combobox de Perfil vendido.
     * 
     * @param pVendidoList
     *            lista de PerfilVendido.
     * 
     */
    private void loadPerfilVendidoList(final List<PerfilVendido> pVendidoList) {
        Map<String, Long> perfilVendidoMap = new HashMap<String, Long>();
        List<String> perfilVendidoList = new ArrayList<String>();

        for (PerfilVendido pv : pVendidoList) {
            perfilVendidoMap.put(pv.getNomePerfilVendido(), pv
                    .getCodigoPerfilVendido());
            perfilVendidoList.add(pv.getNomePerfilVendido());
        }

        bean.setPerfilVendidoMap(perfilVendidoMap);
        bean.setPerfilVendidoList(perfilVendidoList);
    }

    /**
     * Popula a lista de Mapa Alocacao para combobox de Perfil MapaAlocacao.
     * 
     * @param mapaAlocacaoList
     *            lista de MapaAlocacao.
     * 
     */
    private void loadMapaAlocacaoList(final List<MapaAlocacao> mapaAlocacaoList) {
        Map<String, Long> mapaAlocacaoMap = new HashMap<String, Long>();
        List<String> mapaList = new ArrayList<String>();

        for (MapaAlocacao mapa : mapaAlocacaoList) {
            mapaAlocacaoMap.put(mapa.getTextoTitulo(), mapa
                    .getCodigoMapaAlocacao());
            mapaList.add(mapa.getTextoTitulo());
        }

        bean.setMapaAlocacaoMap(mapaAlocacaoMap);
        bean.setMapaAlocacaoList(mapaList);
    }

    /**
     * Popula a lista de Pessoa para combobox de base.
     * 
     * @param pessoas
     *            lista de Pessoa.
     * 
     */
    private void loadPessoaList(final List<Pessoa> pessoas) {
        Map<String, Long> pessoaMap = new HashMap<String, Long>();
        List<String> pessoaList = new ArrayList<String>();

        for (Pessoa pessoa : pessoas) {
            pessoaMap.put(pessoa.getCodigoLogin(), pessoa.getCodigoPessoa());
            pessoaList.add(pessoa.getCodigoLogin());
        }

        bean.setPessoaMap(pessoaMap);
        bean.setPessoaList(pessoaList);
    }

    /**
     * Faz a pré-validação da Pessoa para verificar se pode ser validada ou não,
     * verifica se existe registros na CustoRealizado e/ou CustoTceGrupoCusto. O
     * Recurso somente será validado se o mes anterior ao mes selecionado,
     * estiver validado.
     * 
     * @param pessoa
     *            - Pessoa a ser validada
     * @param dataMes
     *            - mes que sera validado
     * 
     * @return true se a pré-validação está ok caso contrário retorna false
     */
    private Boolean preValidatePessoa(final Pessoa pessoa, final Date dataMes) {
        Date dataMesValidado = pessoa.getDataMesValidado();
        if (dataMesValidado == null) {
            return Boolean.valueOf(true);
        } else {
            if (dataMes.compareTo(dataMesValidado) < 0) {
                Messages.showError("validateAll",
                        Constants.VALIDATE_PERSON_ERROR_NO_LAST_VALID_MONTH,
                        pessoa.getCodigoLogin());
                return Boolean.valueOf(false);
            }
        }

        Date prevDataMes = DateUtils.addMonths(dataMes, -1);

        // verifica CustoRealizado / CustoTceGrupoCusto no mes anterior
        if (validacaoService.hasValidationForTheMonth(pessoa, prevDataMes)) {
            return Boolean.valueOf(true);
        }

        // verifica CustoRealizado / CustoTceGrupoCusto no mes corrente
        if (validacaoService.hasValidationForTheMonth(pessoa, dataMes)) {
            return Boolean.valueOf(true);
        }

        Messages.showError("validateAll",
                Constants.VALIDATE_PERSON_ERROR_NO_PREV_VALIDATED_MONTH,
                new Object[] {pessoa.getCodigoLogin(),
                        SIMPLE_DATE_FORMAT.format(prevDataMes) });
        return Boolean.valueOf(false);
    }

}