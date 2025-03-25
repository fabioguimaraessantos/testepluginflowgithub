package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoPeriodoService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.ICustoRealizadoService;
import com.ciandt.pms.business.service.ICustoTceGrupoCustoService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPessoaGrupoCustoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CustoRealizado;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaGrupoCusto;
import com.ciandt.pms.persistence.dao.ICustoRealizadoDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 24/02/2010
 */
@Service
public class CustoRealizadoService implements ICustoRealizadoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private ICustoRealizadoDao custoRealizadoDao;

    /** Intancia do serviço PessoaService. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do servico ContratoPratica. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /** Instancia do servico AlocacaoPeriodo. */
    @Autowired
    private IAlocacaoPeriodoService alocacaoPeriodoService;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /** Instancia do servico CustoTceGrupoCusto. */
    @Autowired
    private ICustoTceGrupoCustoService custoTceGCService;

    /** Instancia do servico PessoaGrupoCusto. */
    @Autowired
    private IPessoaGrupoCustoService pessoaGCService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createCustoRealizado(final CustoRealizado entity) {
        entity.setDataAtualizacao(new Date());
        custoRealizadoDao.create(entity);
    }

    /**
     * Cria um objeto CustoRealizado para validacao.
     * 
     * @param alocacaoPeriodo
     *            - AlocacaoPeriodo que será validado
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMesValidar
     *            - dataMes que será validado
     * @param totalAlocacao
     *            - total das alocacoes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias para somar com o total das alocacoes
     *            e verificar se cria ou não CustoRealizado. Criara apenas se a
     *            soma dos totais for menor do que o percentual alocavel.
     * @param percentualAlocavelMes
     *            - percentual alocavel que a pessoa possui no mês
     */
    public void createCustoRealizado(final AlocacaoPeriodo alocacaoPeriodo,
            final Pessoa pPessoa, final Date dataMesValidar,
            final double totalAlocacao, final double totalOhFerias,
            final Double percentualAlocavelMes) {

        MathContext mc =
                new MathContext(Integer.valueOf("6"), RoundingMode.HALF_UP);

        BigDecimal percAlocMesRound = new BigDecimal(percentualAlocavelMes, mc);
        BigDecimal totalAlocacaoRound = new BigDecimal(totalAlocacao, mc);

        Pessoa pessoa = pessoaService.findPessoaById(pPessoa.getCodigoPessoa());

        Moeda moeda =
                moedaService.findMoedaByAcronym(Constants.SIGLA_MOEDA_REAL);

        ContratoPratica contratoPratica =
                contratoPraticaService.findContratoPraticaById(alocacaoPeriodo
                        .getAlocacao().getMapaAlocacao().getContratoPratica()
                        .getCodigoContratoPratica());

        // percentual alocacao corrente
        BigDecimal percentualAlocacaoPeriodo =
                alocacaoPeriodo.getPercentualAlocacaoPeriodo().round(mc);
        double percAlocPerDouble = percentualAlocacaoPeriodo.doubleValue();

        // soma do total alocacoes e total OH/ferias
        double sumTotalAlocacao =
                new BigDecimal(
                        totalAlocacaoRound.doubleValue() + totalOhFerias, mc)
                        .doubleValue();

        // cria o CustoRealizado Normal
        CustoRealizado entity = new CustoRealizado();
        entity.setAlocacaoPeriodo(alocacaoPeriodo);
        entity.setDataMes(dataMesValidar);
        entity.setPessoa(pessoa);
        entity.setContratoPratica(contratoPratica);
        entity.setIndicadorTipo(Constants.CUSTO_REALIZADO_TYPE_NM);
        entity.setValorTceMes(BigDecimal.ZERO);
        entity.setValorTaxaCotacao(BigDecimal.ONE);
        entity.setMoeda(moeda);
        Date dataAtualizacao = new Date();
        entity.setDataAtualizacao(dataAtualizacao);
        entity.setPercentualAlocavel(percAlocMesRound);
        // cenario 1 wiki - alocacao 100% e sem ferias ou com ferias
        if (sumTotalAlocacao == percAlocMesRound.doubleValue()) {
            entity.setPercentualAlocacaoMes(percentualAlocacaoPeriodo);

            // cria CustoRealizado Normal
            custoRealizadoDao.create(entity);

            // cenario 3, 4 wiki - desalocacao e sem ferias ou com ferias
        } else if ((totalOhFerias == 0 && totalAlocacaoRound.doubleValue() < percAlocMesRound
                .doubleValue())
                || (totalOhFerias > 0 && sumTotalAlocacao < percAlocMesRound
                        .doubleValue())) {
            entity.setPercentualAlocacaoMes(percentualAlocacaoPeriodo);

            // cria CustoRealizado Normal
            custoRealizadoDao.create(entity);

            double desalocacao =
                    percAlocMesRound.doubleValue() - sumTotalAlocacao;
            double percAlocPerOh =
                    desalocacao
                            * (percAlocPerDouble / totalAlocacaoRound
                                    .doubleValue());

            entity = new CustoRealizado();
            entity.setAlocacaoPeriodo(alocacaoPeriodo);
            entity.setDataMes(dataMesValidar);
            entity.setPercentualAlocacaoMes(new BigDecimal(percAlocPerOh, mc));
            entity.setPessoa(pessoa);
            entity.setContratoPratica(contratoPratica);
            entity.setIndicadorTipo(Constants.CUSTO_REALIZADO_TYPE_OH);
            entity.setValorTceMes(BigDecimal.ZERO);
            entity.setValorTaxaCotacao(BigDecimal.ONE);
            entity.setMoeda(moeda);
            entity.setDataAtualizacao(dataAtualizacao);
            entity.setPercentualAlocavel(percAlocMesRound);

            // cria CustoRealizado Overhead
            custoRealizadoDao.create(entity);

            // cenario 2, 6, 7 wiki - superalocacao e sem ferias ou com ferias
        } else if (sumTotalAlocacao > percAlocMesRound.doubleValue()) {
            double percAlocSemFerias =
                    percAlocMesRound.doubleValue() - totalOhFerias;
            double percAlocPerRest =
                    percAlocPerDouble
                            * (percAlocSemFerias / totalAlocacaoRound
                                    .doubleValue());

            entity
                    .setPercentualAlocacaoMes(new BigDecimal(percAlocPerRest,
                            mc));

            // cria CustoRealizado Normal
            custoRealizadoDao.create(entity);
        }
    }

    /**
     * Cria um objeto CustoRealizado para validacao de um Recurso Fixo (not
     * Billable).
     * 
     * @param alocacaoPeriodo
     *            - AlocacaoPeriodo que será validado
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMesValidar
     *            - dataMes que será validado
     * @param totalAlocacao
     *            - total das alocacoes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias para somar com o total das alocacoes
     *            e verificar se cria ou não CustoRealizado. Criara apenas se a
     *            soma dos totais for menor do que o percentual alocavel.
     * @param percentualAlocavelMes
     *            - percentual alocavel que a pessoa possui no mês
     */
    public void createCustoRealizadoFixedResource(
            final AlocacaoPeriodo alocacaoPeriodo, final Pessoa pPessoa,
            final Date dataMesValidar, final double totalAlocacao,
            final double totalOhFerias, final Double percentualAlocavelMes) {
        Pessoa pessoa = pessoaService.findPessoaById(pPessoa.getCodigoPessoa());

        Moeda moeda =
                moedaService.findMoedaByAcronym(Constants.SIGLA_MOEDA_REAL);

        ContratoPratica contratoPratica =
                contratoPraticaService.findContratoPraticaById(alocacaoPeriodo
                        .getAlocacao().getMapaAlocacao().getContratoPratica()
                        .getCodigoContratoPratica());

        // percentual alocacao corrente
        BigDecimal percentualAlocacaoPeriodo =
                alocacaoPeriodo.getPercentualAlocacaoPeriodo();
        double percAlocPerDouble = percentualAlocacaoPeriodo.doubleValue();

        // soma do total alocacoes e total OH/ferias
        double sumTotalAlocacao = totalAlocacao + totalOhFerias;

        // cria o CustoRealizado Normal
        CustoRealizado entity = new CustoRealizado();
        entity.setAlocacaoPeriodo(alocacaoPeriodo);
        entity.setDataMes(dataMesValidar);
        entity.setPessoa(pessoa);
        entity.setContratoPratica(contratoPratica);
        entity.setIndicadorTipo(Constants.CUSTO_REALIZADO_TYPE_NM);
        entity.setValorTceMes(BigDecimal.ZERO);
        entity.setValorTaxaCotacao(BigDecimal.ONE);
        entity.setMoeda(moeda);
        entity.setDataAtualizacao(new Date());
        entity.setPercentualAlocavel(BigDecimal.valueOf(percentualAlocavelMes));
        // cenario 1 - alocacao <= 100%
        if (sumTotalAlocacao <= percentualAlocavelMes) {
            entity.setPercentualAlocacaoMes(percentualAlocacaoPeriodo);

            // cenario 2 - superalocacao e sem ferias ou com ferias
        } else if (sumTotalAlocacao > percentualAlocavelMes) {
            double percAlocSemFerias = percentualAlocavelMes - totalOhFerias;
            double percAlocPerRest =
                    percAlocPerDouble * (percAlocSemFerias / totalAlocacao);

            entity
                    .setPercentualAlocacaoMes(BigDecimal
                            .valueOf(percAlocPerRest));
        }

        // cria CustoRealizado Normal
        custoRealizadoDao.create(entity);
    }

    /**
     * Cria um objeto CustoRealizado Ferias para validacao.
     * 
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMesValidar
     *            - dataMes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias
     * @param percentualAlocavelMes
     *            - percentual alocável da pessoa no mes corrente.
     */
    public void createCustoRealizadoFerias(final Pessoa pPessoa,
            final Date dataMesValidar, final double totalOhFerias,
            final Double percentualAlocavelMes) {

        Pessoa pessoa = pessoaService.findPessoaById(pPessoa.getCodigoPessoa());

        Moeda moeda =
                moedaService.findMoedaByAcronym(Constants.SIGLA_MOEDA_REAL);

        // cria o CustoRealizado Ferias
        CustoRealizado entity = new CustoRealizado();
        entity.setDataMes(dataMesValidar);
        entity.setPessoa(pessoa);
        entity.setContratoPratica(null);
        entity.setIndicadorTipo(Constants.CUSTO_REALIZADO_TYPE_FR);
        entity.setPercentualAlocacaoMes(BigDecimal.valueOf(totalOhFerias));
        entity.setValorTceMes(BigDecimal.ZERO);
        entity.setValorTaxaCotacao(BigDecimal.ONE);
        entity.setMoeda(moeda);
        entity.setDataAtualizacao(new Date());
        entity.setPercentualAlocavel(BigDecimal.valueOf(percentualAlocavelMes));

        custoRealizadoDao.create(entity);
    }

    /**
     * Cenario 5 wiki - desalocacao total e com ferias.
     * 
     * @param pPessoa
     *            - Pessoa que será validada
     * @param closingDate
     *            - dataMes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias
     * @param percentualAlocavelMes
     *            - percentual alocável da pessoa no mes corrente.
     * 
     */
    public void createCustoRealizadoDesalocTotalClosingDate(
            final Pessoa pPessoa, final Date closingDate,
            final double totalOhFerias, final double percentualAlocavelMes) {

        Pessoa pessoa = pessoaService.findPessoaById(pPessoa.getCodigoPessoa());

        List<AlocacaoPeriodo> alocacaoPeriodoAuxList =
                new ArrayList<AlocacaoPeriodo>();

        // busca todos as AlocacaoPeriodo do mes anterior ao closingDate
        // para somar o total (todas as Alocacao da Pessoa corrente)
        double previousTotalAlocacao = Double.valueOf(0);
        Iterator<Alocacao> itAlocacao =
                pessoa.getRecurso().getAlocacaos().iterator();
        while (itAlocacao.hasNext()) {
            Alocacao alocacaoAux = itAlocacao.next();
            AlocacaoPeriodo alocPerAux =
                    alocacaoPeriodoService
                            .findAlocacaoPeriodoByAlocacaoAndDate(alocacaoAux,
                                    closingDate);
            if (alocPerAux != null) {
                previousTotalAlocacao +=
                        alocPerAux.getPercentualAlocacaoPeriodo().doubleValue();
                alocacaoPeriodoAuxList.add(alocPerAux);
            }
        }

        // percentual desalocacao
        double desalocacao = percentualAlocavelMes - totalOhFerias;

        Date nextMonthDate = DateUtils.addMonths(closingDate, 1);

        Moeda moeda =
                moedaService.findMoedaByAcronym(Constants.SIGLA_MOEDA_REAL);

        // itera a lista de AlocacaoPeriodo do mes anterior e cria os
        // CustoRealizado OH
        for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoAuxList) {
            double percentualAlocacaoPeriodo =
                    alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                            .doubleValue();

            double previousPercAlocPerOh =
                    percentualAlocacaoPeriodo
                            * (desalocacao / previousTotalAlocacao);

            ContratoPratica contratoPratica =
                    contratoPraticaService
                            .findContratoPraticaById(alocacaoPeriodo
                                    .getAlocacao().getMapaAlocacao()
                                    .getContratoPratica()
                                    .getCodigoContratoPratica());

            // cria o CustoRealizado Overhead
            CustoRealizado entity = new CustoRealizado();
            entity.setDataMes(nextMonthDate);
            entity.setPessoa(pessoa);
            entity.setContratoPratica(contratoPratica);
            entity.setPercentualAlocacaoMes(BigDecimal
                    .valueOf(previousPercAlocPerOh));
            entity.setIndicadorTipo(Constants.CUSTO_REALIZADO_TYPE_OH);
            entity.setValorTceMes(BigDecimal.ZERO);
            entity.setValorTaxaCotacao(BigDecimal.ONE);
            entity.setMoeda(moeda);
            entity.setDataAtualizacao(new Date());
            entity.setPercentualAlocavel(BigDecimal
                    .valueOf(percentualAlocavelMes));

            // cria CustoRealizado OH
            custoRealizadoDao.create(entity);
        }
    }

    /**
     * Desalocacao total - Billable.
     * 
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMes
     *            - dataMes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias
     * @param percentualAlocavelMes
     *            - percentual alocável da pessoa no mes corrente.
     * 
     */
    public void createCustoRealizadoDesalocTotal(final Pessoa pPessoa,
            final Date dataMes, final double totalOhFerias,
            final double percentualAlocavelMes) {
        Pessoa pessoa = pessoaService.findPessoaById(pPessoa.getCodigoPessoa());
        double previousTotalAlocacao = Double.valueOf(0);
        double difTotalAlocacao = percentualAlocavelMes - totalOhFerias;

        List<AlocacaoPeriodo> alocPerPrevMonthList =
                alocacaoPeriodoService.findAlocPerByMaxDateAndRecurso(pPessoa
                        .getRecurso(), dataMes);

        for (AlocacaoPeriodo alocacaoPeriodo : alocPerPrevMonthList) {
            previousTotalAlocacao +=
                    alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                            .doubleValue();
        }

        Moeda moeda =
                moedaService.findMoedaByAcronym(Constants.SIGLA_MOEDA_REAL);

        // itera a lista de AlocacaoPeriodo do mes anterior e cria os
        // CustoRealizado OH
        for (AlocacaoPeriodo alocacaoPeriodo : alocPerPrevMonthList) {
            double percentualAlocacaoPeriodo =
                    alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                            .doubleValue();

            double previousPercAlocPerOh = Double.valueOf(0);
            if (previousTotalAlocacao != 0.0) {
                previousPercAlocPerOh = percentualAlocacaoPeriodo
                    * (difTotalAlocacao / previousTotalAlocacao);
            }

            ContratoPratica contratoPratica =
                    contratoPraticaService
                            .findContratoPraticaById(alocacaoPeriodo
                                    .getAlocacao().getMapaAlocacao()
                                    .getContratoPratica()
                                    .getCodigoContratoPratica());

            // cria o CustoRealizado Overhead
            CustoRealizado entity = new CustoRealizado();
            entity.setDataMes(dataMes);
            entity.setPessoa(pessoa);
            entity.setContratoPratica(contratoPratica);
            entity.setPercentualAlocacaoMes(BigDecimal
                    .valueOf(previousPercAlocPerOh));
            entity.setIndicadorTipo(Constants.CUSTO_REALIZADO_TYPE_OH);
            entity.setValorTceMes(BigDecimal.ZERO);
            entity.setValorTaxaCotacao(BigDecimal.ONE);
            entity.setMoeda(moeda);
            entity.setDataAtualizacao(new Date());
            entity.setPercentualAlocavel(BigDecimal
                    .valueOf(percentualAlocavelMes));
            entity.setAlocacaoPeriodo(alocacaoPeriodo);

            // cria CustoRealizado OH
            custoRealizadoDao.create(entity);
        }

        // se nao possuir alocacao em algum mes anterior, se for desalocacao
        // total de billable, grava a diferenca / restante na tabela
        // CustoTceGrupoCusto
        if (alocPerPrevMonthList.isEmpty()) {
            PessoaGrupoCusto pessoaGrupoCusto =
                    pessoaGCService.findPessGCByPessoaAndDate(pessoa, dataMes);
            // cria o CustoTceGrupoCusto
            custoTceGCService.createCustoTceGrupoCusto(pessoa, dataMes,
                    difTotalAlocacao, pessoaGrupoCusto.getGrupoCusto(),
                    percentualAlocavelMes);
        }

    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    public void removeCustoRealizado(final CustoRealizado entity) {
        custoRealizadoDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public CustoRealizado findCustoRealizadoById(final Long id) {
        return custoRealizadoDao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<CustoRealizado> findCustoRealizadoAll() {
        return custoRealizadoDao.findAll();
    }

    /**
     * Busca uma lista de entidades pela Pessoa e dataMes.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    public List<CustoRealizado> findCustRealizByPessoaAndDataMes(
            final Pessoa pessoa, final Date dataMes) {
        return custoRealizadoDao.findByPessoaAndDataMes(pessoa, dataMes);
    }

    /**
     * Busca uma lista de entidades pela Pessoa, dataMes e ContratoPratica.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    public List<CustoRealizado> findCustoRealizadoByPessoaAndContratoPratica(
            final Pessoa pessoa, final Date dataMes, final ContratoPratica cp) {
        return custoRealizadoDao.findByPessoaAndContratoPratica(pessoa,
                dataMes, cp);
    }

    /**
     * Busca uma lista de entidades pela Pessoa, dataMes e ContratoPratica.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    public Double getTotalAlocacaoPessoa(final Pessoa pessoa,
            final Date dataMes, final ContratoPratica cp) {
        return custoRealizadoDao.getTotalAlocacaoPessoa(pessoa, dataMes, cp);
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * 
     * @return lista de entidades que atendem ao criterio da Pessoa.
     */
    public CustoRealizado findCustoRealizMaxStartDateByPessoa(
            final Pessoa pessoa) {
        return custoRealizadoDao.findMaxStartDateByPessoa(pessoa);
    }

    /**
     * Busca uma lista de entidades pelo ContratoPratica e dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    public List<CustoRealizado> findCustoRealizadoByContratoPraticaAndDate(
            final ContratoPratica cp, final Date dataMes) {
        return custoRealizadoDao.findByContratoPraticaAndDate(cp, dataMes);
    }

}