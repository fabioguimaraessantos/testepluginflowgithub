package com.ciandt.pms.control.jsf;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICustoPessoaMesService;
import com.ciandt.pms.business.service.IDreLogFechamentoService;
import com.ciandt.pms.business.service.IValidacaoPessoaService;
import com.ciandt.pms.control.jsf.bean.DreLogFechamentoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.DreLogFechamento;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.ValidacaoPessoaRow;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.TempoConstrucao;


/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 08/06/2010
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_FINANCIAL" })
public class DreLogFechamentoController {

    private static Logger logger = LogManager.getLogger(DreLogFechamentoController.class);


    /** outcome tela validacao da entidade. */
    private static final String OUTCOME_DRE_LOG_FECHAMENTO_VALIDATE = "dreLogFechamento_validate";

    /** Instancia do servico. */
    @Autowired
    private IDreLogFechamentoService dreLogFechamentoService;

    /** Instancia do servico - ValidacaoPessoa. */
    @Autowired
    private IValidacaoPessoaService validacaoPessoaService;

    /** Instancia do servico - CustoPessoaMes. */
    @Autowired
    private ICustoPessoaMesService custoPessoaMesService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private DreLogFechamentoBean bean = null;

    /**
     * @return the bean
     */
    public DreLogFechamentoBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final DreLogFechamentoBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela para fechamento da DRE.
     * 
     * @return pagina de destino
     */
    public String prepareProcess() {
        bean.reset();

        return OUTCOME_DRE_LOG_FECHAMENTO_VALIDATE;
    }

    /**
     * Processa a validacao do fechamento da DRE - geracao de logs.
     * 
     */
    @Deprecated
    public void process() {

        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        // processa as validacoes da DRE
        DreLogFechamento dreLogFechamento = dreLogFechamentoService
                .validateDreLogFechamento(dataMes);

        // se a validacao retornar null, é pq é sucesso e vai para o proximo
        // passo validacao de Recurso, caso contrario retorna
        // o objeto com o log para ser exibido na tela
        if (dreLogFechamento != null) {
            bean.setTo(dreLogFechamento);
            bean.setScreenNumber(Integer.valueOf(2));
            Messages.showError("validate",
                    Constants.MSG_ERROR_VALIDATE_DRE_LOG_FECHAMENTO);
        } else {
            prepareValidate();
        }

    }

    /**
     * Reseta os valores das progressBars.
     */
    public void resetBar() {
        bean.setCurrentValue(0);
        bean.setTc(new TempoConstrucao());
        bean.getTc().setPercentualConclusao(0);
    }

    /**
     * Prepara a tela de validacao dos Recursos.
     * 
     */
    @Deprecated
    public void prepareValidate() {

        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        Date before = new Date();

        bean.setPessoaRowList(validacaoPessoaService.findByManagerAndDataMes(
                null, dataMes));

        Date after = new Date();

        if (bean.getConsultingTime() == 0) {
            bean
                    .setConsultingTime((int) (after.getTime() - before
                            .getTime()) / 1000);
        }
        if (bean.getPessoaRowList().size() > 0) {
            bean.setScreenNumber(Integer.valueOf(3));
        } else {
            bean.setScreenNumber(Integer.valueOf(4));

            Messages.showSucess("prepareValidate",
                    Constants.VALIDATE_PERSON_SUCCESS_ALL_RESOURCES_VALIDATED);
        }
    }

    /**
     * Executa a validacao das Pessoas.
     */
    public void validate() {
        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());
        List<ValidacaoPessoaRow> pessoaRowList = bean.getPessoaRowList();

        bean.setLogValidate(new String());

        Date data = new Date();

        float cont = 0;
        float totalSelected = 0;

        TempoConstrucao tc = bean.getTc();

        for (ValidacaoPessoaRow row : pessoaRowList) {
            if (row.getIsSelected()) {
                totalSelected++;
            }
        }

        for (ValidacaoPessoaRow row : pessoaRowList) {

            if (row.getIsSelected()) {
                Pessoa pessoa = row.getPessoa();

                List<AlocacaoPeriodo> alocacaoPeriodoList = validacaoPessoaService
                        .getAlocacaoPeriodoList(pessoa, dataMes);

                data = new Date();

                logger
                        .info(data + " - Validating - "
                                + pessoa.getCodigoLogin());

                bean.setLogValidate(pessoa.getCodigoLogin());

                validacaoPessoaService.validatePessoa(pessoa,
                        alocacaoPeriodoList, dataMes);

                cont++;
                bean.setCurrentValue((cont / totalSelected) * 100);
            }
        }

        tc.setTempo(bean.getConsultingTime());
        tc.setPercentualConclusao(0);
        tc.start();
        prepareValidate();
        tc.setDone(Boolean.TRUE);
    }

    /**
     * Executa a validacao das Pessoas.
     */
    @RolesAllowed({ "ROLE_PMS_ADMIN" })
    public void validateAll() {
        prepareValidate();

        List<ValidacaoPessoaRow> pessoaRowList = bean.getPessoaRowList();
        for (ValidacaoPessoaRow row : pessoaRowList) {
            row.setIsSelected(true);
        }

        validate();

        Messages.showSucess("prepareValidate",
                Constants.VALIDATE_PERSON_SUCCESS_ALL_RESOURCES_VALIDATED);
    }

    /**
     * Executa a apropriacao do Plantao e Horas Extras.
     */
    @Deprecated
    public void registerPLAndHECosts() {
        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        if (custoPessoaMesService.registerPLAndHEByValue(dataMes)) {
            this.next();
            Messages.showSucess("registerPLAndHECosts",
                    Constants.MSG_SUCCESS_REGISTER_PL_HE_COSTS_DRE);
        }
    }

    /**
     * Executa a consolidacao da DRE.
     */
    @Deprecated
    public void consolidate() {

        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        // se a procedure processar normalmente, mensagem de sucesso, caso
        // contrario mensagem de erro
        if (dreLogFechamentoService.consolidateDre(dataMes)) {
            bean.reset();
            Messages.showSucess("consolidate",
                    Constants.MSG_SUCCESS_CONSOLIDATE_DRE_LOG_FECHAMENTO);
        } else {
            Messages.showError("consolidate",
                    Constants.MSG_ERROR_CONSOLIDATE_DRE);
        }
    }

    /**
     * Cancela a atualização de um DreLogFechamento.
     */
    public void cancel() {
        bean.reset();
    }

    /**
     * Vai para a próxima etapa da validacao / tela.
     */
    public void next() {
        Integer screenNumber = bean.getScreenNumber();
        bean.setScreenNumber(++screenNumber);
    }

}