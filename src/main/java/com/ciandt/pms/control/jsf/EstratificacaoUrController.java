package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IEstratificacaoUrService;
import com.ciandt.pms.business.service.IExplicacaoDesvioService;
import com.ciandt.pms.business.service.IItemReceitaService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.control.jsf.bean.EstratificacaoUrBean;
import com.ciandt.pms.control.jsf.bean.MapaAlocacaoBean;
import com.ciandt.pms.control.jsf.bean.ReceitaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.EstratificacaoUr;
import com.ciandt.pms.model.ExplicacaoDesvio;
import com.ciandt.pms.model.ItemEstratificacaoUr;
import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;


/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 08/06/2010
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.CLOSING_DRE:VW" })
public class EstratificacaoUrController {

    /** outcome tela validacao da entidade. */
    private static final String OUTCOME_UR_REVIEW_FECHAMENTO = "urReviewFechamento";

    /** Instancia do servico do MapaAlocacai. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    /** Instancia do servico da Receita. */
    @Autowired
    private IReceitaService receitaService;

    /** Instancia do servico do EstratificacaoUr. */
    @Autowired
    private IEstratificacaoUrService estratificacaoUrService;

    /** Instancia do servico do ExplicacaoDesvio. */
    @Autowired
    private IExplicacaoDesvioService explicacaoDesvioService;

    /** Instancia do servico da Pessoa. */
    @Autowired
    private IPessoaService pessoaService;
    
    /** Instancia do serviço de ItemReceita. */
    @Autowired
    private IItemReceitaService itemReceitaService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private EstratificacaoUrBean bean = null;

    /**
     * Instancia do bean.
     */
    @Autowired
    private MapaAlocacaoBean mapaAlocacaoBean = null;

    /**
     * Instancia do bean.
     */
    @Autowired
    private ReceitaBean receitaBean = null;


    private boolean isITSupport = false;


    /**
     * @return the bean
     */
    public EstratificacaoUrBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final EstratificacaoUrBean bean) {
        this.bean = bean;
    }

    /**
     * @return the mapaAlocacaoBean
     */
    public MapaAlocacaoBean getMapaAlocacaoBean() {
        return mapaAlocacaoBean;
    }

    /**
     * @param mapaAlocacaoBean
     *            the mapaAlocacaoBean to set
     */
    public void setMapaAlocacaoBean(final MapaAlocacaoBean mapaAlocacaoBean) {
        this.mapaAlocacaoBean = mapaAlocacaoBean;
    }

    /**
     * @return the receitaBean
     */
    public ReceitaBean getReceitaBean() {
        return receitaBean;
    }

    /**
     * @param receitaBean
     *            the receitaBean to set
     */
    public void setReceitaBean(final ReceitaBean receitaBean) {
        this.receitaBean = receitaBean;
    }

    /**
     * Prepara a tela para fechamento da UR Review.
     * 
     * @return pagina de destino
     */
    public String prepareProcess() {
        bean.reset();
        setIsITSupport(validateITSupport());
        return OUTCOME_UR_REVIEW_FECHAMENTO;
    }

    /**
     * Processa o fechamento da UR Review.
     * 
     */
    public void process() {

        bean.setCurrentValue(0);
        bean.setTotalValue(0);
        setIsITSupport(validateITSupport());
        mapaAlocacaoBean.setExplicacaoDesvioDefault(explicacaoDesvioService
                .findExplicacaoDesvioSelecionado());

        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        // realiza a busca pelo MapaAlocacao
        mapaAlocacaoBean.setResultList(mapaAlocacaoService
                .findMapaAlocacaoByDate(dataMes));

        // se nenhum resultado encontrado
        if (mapaAlocacaoBean.getResultList().size() == 0) {
            Messages.showWarning("findMapaByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        } else {
            bean.setTotalValue(mapaAlocacaoBean.getResultList().size());
            float cont = 0;
            for (MapaAlocacao mapaAlocacao : mapaAlocacaoBean.getResultList()) {
                if (!mapaAlocacao.getEstratificacaoUrs().isEmpty()) {
                    for (EstratificacaoUr estratificacaoUr : mapaAlocacao
                            .getEstratificacaoUrs()) {
                        if (estratificacaoUr.getDataMes().equals(dataMes)) {
                            mapaAlocacao.setStratified(Boolean.TRUE);
                        }
                    }
                } else {
                    mapaAlocacao.setStratified(Boolean.FALSE);
                }
                if (!mapaAlocacao.isStratified()) {
                    mapaAlocacaoBean.setTo(mapaAlocacao);
                    this.prepareReviewUr();
                    this.saveReviewUr();
                }
                cont++;
                bean.setCurrentValue((cont / bean.getTotalValue()) * 100);
            }
        }
    }

    /**
     * Prepara a Estratificacao da UR.
     * 
     */
    public void prepareReviewUr() {
        setIsITSupport(validateITSupport());
        mapaAlocacaoBean
                .setItensExtratificacaoUr(new ArrayList<ItemEstratificacaoUr>());

        // Pega a data (mês/ano)
        Date monthDate = DateUtil
                .getDate(bean.getMonthBeg(), bean.getYearBeg());

        List<Alocacao> alocacoes = mapaAlocacaoBean.getTo().getAlocacaos();

        for (Iterator<Alocacao> iteratorAlocacao = alocacoes.iterator(); iteratorAlocacao
                .hasNext();) {

            Alocacao alocacao = iteratorAlocacao.next();

            List<AlocacaoPeriodo> alocacoesPeriodos = new ArrayList<AlocacaoPeriodo>(
                    alocacao.getAlocacaoPeriodos());

            for (Iterator<AlocacaoPeriodo> iterator = alocacoesPeriodos
                    .iterator(); iterator.hasNext();) {
                AlocacaoPeriodo alocacaoPeriodo = iterator.next();

                ItemEstratificacaoUr itemEstratificacaoUr = new ItemEstratificacaoUr();

                if (alocacaoPeriodo.getDataAlocacaoPeriodo().compareTo(
                        monthDate) == 0) {

                    Pessoa pessoa = pessoaService.findPessoaByRecursoTipo(
                            alocacaoPeriodo.getAlocacao().getRecurso(), "PE");

                    if (pessoa != null) {
                        itemEstratificacaoUr.setPessoa(pessoa);

                        itemEstratificacaoUr
                                .setNumeroFteAlocado(BigDecimal.ZERO
                                        .setScale(2));
                        itemEstratificacaoUr
                                .setNumeroFteReceitado(BigDecimal.ZERO
                                        .setScale(2));

                        itemEstratificacaoUr
                                .setNumeroFteAlocado(alocacaoPeriodo
                                        .getPercentualAlocacaoPeriodo()
                                        .setScale(2, RoundingMode.HALF_UP));

                        itemEstratificacaoUr
                                .setExplicacaoDesvio(mapaAlocacaoBean
                                        .getExplicacaoDesvioDefault());

                        // verifica se a pessoa ja esta na lista e unifica os
                        // FTE
                        boolean existe = false;
                        for (Iterator<ItemEstratificacaoUr> iteratorEstratificacao = mapaAlocacaoBean
                                .getItensExtratificacaoUr().iterator(); iteratorEstratificacao
                                .hasNext();) {
                            ItemEstratificacaoUr itemEstratificacaoUr2 = iteratorEstratificacao
                                    .next();

                            if (itemEstratificacaoUr2.getPessoa()
                                    .getCodigoPessoa() == itemEstratificacaoUr
                                    .getPessoa().getCodigoPessoa()) {
                                existe = true;
                                itemEstratificacaoUr2
                                        .setNumeroFteAlocado(itemEstratificacaoUr2
                                                .getNumeroFteAlocado()
                                                .add(
                                                        itemEstratificacaoUr
                                                                .getNumeroFteAlocado()
                                                                .setScale(
                                                                        2,
                                                                        RoundingMode.HALF_UP))
                                                .setScale(2,
                                                        RoundingMode.HALF_UP));
                                itemEstratificacaoUr2
                                        .setNumeroFteReceitado(itemEstratificacaoUr2
                                                .getNumeroFteReceitado()
                                                .add(
                                                        itemEstratificacaoUr
                                                                .getNumeroFteReceitado()
                                                                .setScale(
                                                                        2,
                                                                        RoundingMode.HALF_UP))
                                                .setScale(2,
                                                        RoundingMode.HALF_UP));
                            }
                        }
                        if (!existe) {
                            mapaAlocacaoBean.getItensExtratificacaoUr().add(
                                    itemEstratificacaoUr);
                        }
                    } else {
                        continue;
                    }
                }
            }
        }

        // Busca as receitas do mesmo Contract-LOB e data
        Receita receita = (findReceitaByMapaData(mapaAlocacaoBean.getTo(),
                monthDate));
        receitaBean.setTo(receita);

        if (receita != null) {
        	// pesquisa os itens de receita desta receita
            List<ItemReceita> itensReceita = itemReceitaService
					.findItemReceitaByReceita(receita);

            // Verifica se a pessoa ja esta na lista
            boolean existe = false;
            // Seta os valores de FTE Receitado para as pessoas da lista
            for (Iterator<ItemReceita> iteratorReceita = itensReceita
                    .iterator(); iteratorReceita.hasNext();) {
                ItemReceita itemReceita = iteratorReceita.next();

                for (Iterator<ItemEstratificacaoUr> iteratorEstratificacao = mapaAlocacaoBean
                        .getItensExtratificacaoUr().iterator(); iteratorEstratificacao
                        .hasNext();) {
                    ItemEstratificacaoUr itemEstratificacaoUr = iteratorEstratificacao
                            .next();

                    if (itemReceita.getPessoa().getCodigoPessoa() == itemEstratificacaoUr
                            .getPessoa().getCodigoPessoa()) {
                        existe = true;

                        itemEstratificacaoUr
                                .setNumeroFteReceitado(itemEstratificacaoUr
                                        .getNumeroFteReceitado()
                                        .add(
                                                itemReceita
                                                        .getNumeroFte()
                                                        .setScale(
                                                                2,
                                                                RoundingMode.HALF_UP)));
                    }
                }

                // Se a pessoa nao esta na lista, adiciona com FTE Alocado igual
                // a 0
                if (!existe) {

                    ItemEstratificacaoUr itemEstratificacaoUr = new ItemEstratificacaoUr();
                    itemEstratificacaoUr.setNumeroFteAlocado(BigDecimal.ZERO
                            .setScale(2, RoundingMode.HALF_UP));
                    itemEstratificacaoUr.setNumeroFteReceitado(itemReceita
                            .getNumeroFte().setScale(2, RoundingMode.HALF_UP));
                    itemEstratificacaoUr.setPessoa(itemReceita.getPessoa());
                    itemEstratificacaoUr
                            .setExplicacaoDesvio(new ExplicacaoDesvio());

                    mapaAlocacaoBean.getItensExtratificacaoUr().add(
                            itemEstratificacaoUr);
                }
            }
        }

        // calcula o total da diferenca
        float totalDifference = 0;
        float totalAlocado = 0;
        float totalReceitado = 0;
        for (Iterator<ItemEstratificacaoUr> iterator = mapaAlocacaoBean
                .getItensExtratificacaoUr().iterator(); iterator.hasNext();) {
            ItemEstratificacaoUr itemEstratificacaoUr = iterator.next();

            float fteReceitado = itemEstratificacaoUr.getNumeroFteReceitado()
                    .setScale(2, RoundingMode.HALF_UP).floatValue();
            float fteAlocado = itemEstratificacaoUr.getNumeroFteAlocado()
                    .setScale(2, RoundingMode.HALF_UP).floatValue();

            totalReceitado += fteReceitado;
            totalAlocado += fteAlocado;
            float difference = fteReceitado - fteAlocado;

            totalDifference += difference;

        }
        mapaAlocacaoBean.setTotalAlocado(new BigDecimal(totalAlocado).setScale(
                2, RoundingMode.HALF_UP));
        mapaAlocacaoBean.setTotalReceitado(new BigDecimal(totalReceitado)
                .setScale(2, RoundingMode.HALF_UP));
        mapaAlocacaoBean.setTotalDifference(new BigDecimal(totalDifference)
                .setScale(2, RoundingMode.HALF_UP));

    }

    /**
     * Salva as modificacoes da tela de Estratificacao da UR.
     *
     */
    public void saveReviewUr() {
        setIsITSupport(validateITSupport());
        EstratificacaoUr estratificacaoUr = new EstratificacaoUr();

        estratificacaoUr.setDataAtualizacao(new Date());
        // Pega a data (mês/ano)
        Date monthDate = DateUtil
                .getDate(bean.getMonthBeg(), bean.getYearBeg());
        estratificacaoUr.setDataMes(monthDate);
        estratificacaoUr.setNumeroTotalDesvio(mapaAlocacaoBean
                .getTotalDifference());
        estratificacaoUr.setMapaAlocacao(mapaAlocacaoBean.getTo());
        estratificacaoUr.setReceita(receitaBean.getTo());

        estratificacaoUr.setCodigoLoginCriador(LoginUtil.getLoggedUsername());

        List<ItemEstratificacaoUr> itensExtratificacaoUr = mapaAlocacaoBean
                .getItensExtratificacaoUr();
        for (ItemEstratificacaoUr item : itensExtratificacaoUr) {
            item.setEstratificacaoUr(estratificacaoUr);

            float fteReceitado = item.getNumeroFteReceitado().floatValue();
            float fteAlocado = item.getNumeroFteAlocado().floatValue();

            if (fteReceitado - fteAlocado >= 0) {
                item.setExplicacaoDesvio(null);
            }

        }

        estratificacaoUr.setItemEstratificacaoUrs(itensExtratificacaoUr);

        estratificacaoUrService.createEstratificacaoUr(estratificacaoUr);

    }

    /**
     * Busca uma Receita a partir de uma MapaAlocacao.
     * 
     * @param mapa
     *            - MapaAlocacao
     * @param monthDate
     *            - data mês
     * 
     * @return uma Receita a partir de uma MapaAlocacao.
     */
    public Receita findReceitaByMapaData(final MapaAlocacao mapa,
            final Date monthDate) {

        Receita filter = new Receita();
        filter.setContratoPratica(mapa.getContratoPratica());
        filter.setDataMes(monthDate);

        List<Receita> receitas = receitaService.findReceitaByFilter(filter);

        if (receitas == null || receitas.isEmpty()) {
            return null;
        }
        return receitas.get(0);
    }


    private Boolean validateITSupport() {
        GrantedAuthority[] loggedUserAuthorities = LoginUtil
                .getLoggedUserAuthorities();
        boolean isItSupport = false;
        for (GrantedAuthority authority : loggedUserAuthorities) {

            if(authority.getAuthority().equals("BOF.CLOSING_DRE:ED")
                    ||authority.getAuthority().equals("BOF.CLOSING_DRE:CR")){
                isItSupport = false;
                continue;
            }
            if (authority.getAuthority().equals("BOF.CLOSING_DRE:VW")) {
                isItSupport = true;
            }
        }
        return isItSupport;
    }
    public boolean getIsITSupport() {
        return isITSupport;
    }

    public void setIsITSupport(boolean isITSupport) {
        this.isITSupport = isITSupport;
    }

}