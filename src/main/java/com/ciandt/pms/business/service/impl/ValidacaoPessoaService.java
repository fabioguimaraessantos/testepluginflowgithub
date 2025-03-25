package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.ciandt.pms.business.service.*;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ClosingDREPessoaVO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.audit.listener.AuditoriaJpaListener;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.vo.ValidacaoPessoaRow;
import com.ciandt.pms.persistence.dao.IVwAlocacaoRecursoMesDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;


/**
 * 
 * A classe ValidacaoPessoaService proporciona as funcionalidades valida��o
 * referente a pessoa.
 * 
 * @since 08/12/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ValidacaoPessoaService extends Observable implements
		IValidacaoPessoaService {
	
	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(ValidacaoPessoaService.class);


    /** Intancia do servi�o PessoaService. */
    @Autowired
    private IPessoaService pessoaService;

    /** Intancia do servi�o AlocacaoPeriodoService. */
    @Autowired
    private IAlocacaoPeriodoService alocacaoPeriodoService;

    /** Intancia do servi�o AlocacaoService. */
    @Autowired
    private IAlocacaoService alocacaoService;

    /** Intancia do servi�o MapaAlocacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    /** Intancia do servi�o PerfilVendido. */
    @Autowired
    private IPerfilVendidoService perfilVendidoService;

    /** Intancia do dao VwAlocacaoRecursoMesDao. */
    @Autowired
    private IVwAlocacaoRecursoMesDao vwAlocacaoRecursoMesDao;

    /** Instancia do servico AlocacaoPeriodoOh. */
    @Autowired
    private IAlocacaoPeriodoOhService alocaoPeridoOhService;

    /** Instancia do servico CustoRealizado. */
    @Autowired
    private ICustoRealizadoService custoRealizadoService;

    /** Instancia do servico PessoaGrupoCusto. */
    @Autowired
    private IPessoaGrupoCustoService pessoaGrupoCustoService;

    /** Instancia do servico CustoTceGrupoCusto. */
    @Autowired
    private ICustoTceGrupoCustoService custoTceGrupoCustoService;

    /** Instancia do servico CidadeBase. */
    @Autowired
    private ICidadeBaseService cidadeBaseService;

    /** Instancia do servico PessoaTipoContratoService. */
    @Autowired
    private IPessoaTipoContratoService pessTCService;
    
    /** Instancia do servico PessoaCidadeBaseService. */
    @Autowired
    private IPessoaCidadeBaseService pessoaCidadeBaseService;

    /** Instancia do servico PessoaBillability. */
    @Autowired
    private IPessoaBillabilityService pessoaBillabilityService;

    /** Intancia de mailSender. */
    private MailSenderUtil mailSender;

    /** Log de auditoria da aplica��o. */
    private AuditoriaJpaListener logJpa = new AuditoriaJpaListener();

    private ArrayList<Object> observers;

    public ValidacaoPessoaService() {
		observers = new ArrayList<Object>();
	}

    /**
     * Retorna uma lista com todos os gerenciados, referente a pessoa logada no
     * sistema.
     * 
     * @param dataMes
     *            - mes da busca.
     * 
     * @return retorna uma lista com todos os recursos.
     */
    public List<ValidacaoPessoaRow> findAllMyManaged(final Date dataMes) {

        return findByManagerAndDataMes(LoginUtil.getLoggedUsername(), dataMes);

    }

    /**
     * Retorna uma lista com todos os delegados, referente a pessoa logada no
     * sistema.
     * 
     * @param dataMes
     *            - mes da busca.
     * 
     * @return retorna uma lista com todos os recursos delegados.
     */
    public List<ValidacaoPessoaRow> findAllMyDelegated(final Date dataMes) {

        List<ValidacaoPessoaRow> rowList = new ArrayList<ValidacaoPessoaRow>();

        Pessoa user =
                pessoaService.findPessoaByLogin(LoginUtil.getLoggedUsername());

        Date dataAtual = new Date();

        for (PessoaDelegacao pessoaDelegacao : user
                .getPessoaDelegacaosDelegadas()) {

            if ((dataAtual.after(pessoaDelegacao.getDataInicio()) || dataAtual
                    .equals(pessoaDelegacao.getDataInicio()))
                    && (pessoaDelegacao.getDataFim() == null
                            || dataAtual.before(pessoaDelegacao.getDataFim()) || dataAtual
                            .equals(pessoaDelegacao.getDataFim()))) {

                Pessoa pessoa =
                        pessoaService.findPessoaById(pessoaDelegacao
                                .getPessoa().getCodigoPessoa());

                rowList.addAll(findByManagerAndDataMes(pessoa.getCodigoLogin(),
                        dataMes));
            }
        }

        return rowList;

    }

    /**
     * Retorna uma lista com todos os gerenciados, referente ao login passado
     * por parametro.
     * 
     * @param loginMgr
     *            - login do gerente.
     * @param dataMes
     *            - mes da busca.
     * 
     * @return retorna uma lista com todos os recursos.
     */


    public List<ValidacaoPessoaRow> findByManagerAndDataMes(
            final String loginMgr, final Date dataMes) {
        List<ValidacaoPessoaRow> rowList = new ArrayList<ValidacaoPessoaRow>();
        List<Pessoa> pessoaList;
        double count = 1d;
        double totalPessoaList = 2d;

        if (!StringUtils.isEmpty(loginMgr)) {
            pessoaList = pessoaService.findPessoaByLoginMgrAndDate(loginMgr, dataMes);
        } else {
            pessoaList = pessoaService.findPessoaNotValidatedByDate(dataMes);
        }

        if (pessoaList != null) {
        	totalPessoaList = pessoaList.size() + 1;
        }

        if (pessoaList != null && !pessoaList.isEmpty()) {
            List<String> resourceMnemonics = this.getMnemonicsFromPeople(pessoaList);
            Set<Long> peopleCodes = this.getPeopleCodes(pessoaList);
            List<VwAlocacaoRecursoMes> allocations = vwAlocacaoRecursoMesDao.findByMnemonicoInAndDataAlocacao(resourceMnemonics, dataMes);
            List<PessoaBillability> billabilities = pessoaBillabilityService.findByPeopleCodeInAndMonth(peopleCodes, dataMes);

            for (Pessoa pessoa : pessoaList) {
                ValidacaoPessoaRow row = new ValidacaoPessoaRow();
                row.setPessoa(pessoa);
                row.setPercentualAlocado(this.getPercentFromAllocation(pessoa, allocations));
                row.setIsMesValidado(this.isMesValidadoFromPessoa(pessoa, dataMes));
                row.setPercentualDisponivelMes(this.calculatePercentAvaliable(pessoa, dataMes, null));
                row.setIndStatusPessGrupoCusto(this.getPersonBillability(pessoa, billabilities));

                rowList.add(row);

                notifyObservers(new Float((count / totalPessoaList) * 100));
                count++;
            }
        }

        return rowList;
    }



    public List<Pessoa> findPessoaByManagerAndDataMes( final String loginMgr, final Date dataMes) {

        List<Pessoa> pessoaList;
        if (!StringUtils.isEmpty(loginMgr)) {
            pessoaList = pessoaService.findPessoaByLoginMgrAndDate(loginMgr, dataMes);
        } else {
            pessoaList = pessoaService.findPessoaNotValidatedByDate(dataMes);
        }
        return pessoaList;
    }

    public Long countPessoaByDataMes(final Date dataMes) {

        return pessoaService.countPessoaNotValidatedByDate(dataMes);

    }

    private Boolean isMesValidadoFromPessoa(Pessoa pessoa, Date dataMes) {
        if (pessoa.getDataMesValidado() != null) {
            Date mesValidadoTrunc =
                    DateUtils.truncate(pessoa.getDataMesValidado(),
                            Calendar.MONTH);
            return Boolean.TRUE.equals(mesValidadoTrunc.compareTo(dataMes) >= 0);
        }
        return Boolean.FALSE;
    }

    private String getPersonBillability(Pessoa pessoa, List<PessoaBillability> billabilities) {
        for (PessoaBillability billability : billabilities) {
            if (pessoa.equals(billability.getPessoa().getCodigoPessoa())) {
                return billability.getBillability().getSiglaBillability();
            }
        }
        return null;
    }

    private BigDecimal getPercentFromAllocation(Pessoa pessoa, List<VwAlocacaoRecursoMes> allocations) {
        for (VwAlocacaoRecursoMes allocation : allocations) {
            if (allocation.getId().getCodigoMnemonico().equalsIgnoreCase(pessoa.getCodigoLogin())) {
                return allocation.getId().getAlocacaoPeriodoMes();
            }
        }
        return BigDecimal.ZERO;
    }

    private List<String> getMnemonicsFromPeople(List<Pessoa> people) {
        List<String> mnemonics = new ArrayList<String>();

        for (Pessoa person : people) {
            mnemonics.add(person.getCodigoLogin());
        }

        return mnemonics;
    }

    /**
     * Retorna uma lista com as AlocacaoPeriodo de uma pessoa referente ao
     * primeiro mes n�o fechado.
     * 
     * @param p
     *            - do tipo Pessoa.
     * @param dataMes
     *            - mes da busca.
     * 
     * @return Lista de AlocacaoPeriodo.
     * 
     */
    public List<AlocacaoPeriodo> getAlocacaoPeriodoList(final Pessoa p,
            final Date dataMes) {
        List<AlocacaoPeriodo> alocacaoPeriodoList =
                new ArrayList<AlocacaoPeriodo>();
        Alocacao alocacao = null;
        AlocacaoPeriodo ap = null;

        Pessoa pessoa = pessoaService.findPessoaById(p.getCodigoPessoa());

        Iterator<Alocacao> itAlocacao =
                pessoa.getRecurso().getAlocacaos().iterator();
        // Date nextMonth = DateUtils.addMonths(getClosingDate(), 1);

        while (itAlocacao.hasNext()) {
            alocacao = itAlocacao.next();
            // ap =
            // alocacaoPeriodoService.findAlocacaoPeriodoByAlocacaoAndDate(alocacao,
            // nextMonth);
            ap =
                    alocacaoPeriodoService
                            .findAlocacaoPeriodoByAlocacaoAndDate(alocacao,
                                    dataMes);
            if (ap != null) {
                alocacaoPeriodoList.add(ap);
            }
        }

        // se a lista de AlocacaoPerido for vazia, é pq nao tem alocacao para o
        // mes da validacao. Portanto, pega o ultimo mes alocado
        // if (alocacaoPeriodoList.isEmpty() && isUseAlocLastMonth) {
        // alocacaoPeriodoList =
        // alocacaoPeriodoService.findAlocPerByMaxDateAndRecurso(pessoa.getRecurso(),nextMonth);
        // alocacaoPeriodoList =
        // alocacaoPeriodoService.findAlocPerByMaxDateAndRecurso(pessoa.getRecurso(),
        // dataMes);
        // }

        return alocacaoPeriodoList;
    }

    /**
     * Retorna uma lista com as AlocacaoPeriodo de uma pessoa referente ao
     * primeiro mes n�o fechado.
     * 
     * @param p
     *            - do tipo Pessoa.
     * @param dataMes
     *            - mes da busca.
     * 
     * @return Lista de AlocacaoPeriodo.
     * 
     */
    public List<AlocacaoPeriodoOh> getAlocacaoPeriodoOhList(final Pessoa p,
            final Date dataMes) {
        // Date closingDate = getClosingDate();
        // Date nextMonthDate = DateUtils.addMonths(closingDate, 1);

        // List<AlocacaoPeriodoOh> apohList =
        // alocaoPeridoOhService.findAlocacaoPeriodoOhByPessoaAndMonthDate(p,
        // nextMonthDate);
        List<AlocacaoPeriodoOh> apohList =
                alocaoPeridoOhService
                        .findAlocacaoPeriodoOhByPessoaAndMonthDate(p, dataMes);

        return apohList;
    }

    /**
     * Realiza a validacao de uma pessoa.
     * 
     * @param alocacaoPeriodoList
     *            - lista de alocca��es periodo de uma pessoa.
     * @param pessoa
     *            - entidade Pessoa que ser� validada.
     * @param dataMes
     *            - mes da validacao.
     * @return true se validacao ok, caso contrario retorna false
     */
    public Boolean validatePessoa(final Pessoa pessoa,
            final List<AlocacaoPeriodo> alocacaoPeriodoList, final Date dataMes) {
        
        double totalAlocacao = Double.valueOf(0);

        Pessoa p = pessoaService.findPessoaById(pessoa.getCodigoPessoa());
        
        LOGGER.info("Starting resouce validate: " + p.getCodigoLogin());

        // busca a PessoaGrupoCusto para verificar o Billability. Se
        // Billable, grava CustoRealizado, se Fixed (not Billable)
        // ou TBA (ToBeAssigned) grava CustoTceGrupoCusto. Se n�o
        // encontrar � pq nao tem associacao e essa Pessoa nao deve ser validada

        PessoaBillability pessoaBillability =
                pessoaBillabilityService.findByPessoaAndDate(p, dataMes);
        PessoaGrupoCusto pessoaGrupoCusto  =
                pessoaGrupoCustoService.findPessGCByPessoaAndDate(p, dataMes);
        if (pessoaBillability == null || pessoaBillability.getBillability() == null) {
            Messages.showError("validatePessoa",
                    Constants.VALIDATE_PERSON_ERROR_VALIDATE_COST_GROUP, pessoa
                            .getCodigoLogin());
            LOGGER.info("PessoaGrupoCusto is null to: " + p.getCodigoLogin());
            return Boolean.valueOf(false);
        }


        // calcula o percentual de alocavel disponivel no mes da pessoa.
        double percentAvaliable =
                calculatePercentAvaliable(pessoa, dataMes, null).doubleValue();

        // calcula o percentual em OH da pessoa no mes/ano
        double totalOhFerias = calculaPercentualAlocacaoOH(dataMes, pessoa, new ArrayList<AlocacaoPeriodoOh>());

        // soma o percentual de alocacao em contrato-pratica
        for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoList) {
            totalAlocacao +=
                    alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                            .doubleValue();
        }


        // if (total == 1) {
        for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoList) {
            // se aloca��o j� existente
            if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() != null) {
                if (alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                        .doubleValue() != 0) {
                    alocacaoPeriodo.setPercentualUr(alocacaoPeriodo
                            .getAlocacao().getValorUr());

                    alocacaoPeriodoService
                            .updateAlocacaoPeriodo(alocacaoPeriodo);

                    // log da auditoria
                    logJpa.preUpdate(alocacaoPeriodoService
                            .findAlocacaoPeriodoById(alocacaoPeriodo
                                    .getCodigoAlocacaoPeriodo()));


                    if (pessoaBillability.getBillability().getSiglaBillability().equals(
                            Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL)) {
                        // cria o CustoRealizado
                        // custoRealizadoService.createCustoRealizado(alocacaoPeriodo,
                        // pessoa, nextMonthDate, totalAlocacao, totalOhFerias,
                        // percentAvaliable);
                        custoRealizadoService.createCustoRealizado(
                                alocacaoPeriodo, pessoa, dataMes,
                                totalAlocacao, totalOhFerias, percentAvaliable);
                    } else {
                        // cria o CustoRealizado Fixed
                        // custoRealizadoService.createCustoRealizadoFixedResource(alocacaoPeriodo,
                        // pessoa, nextMonthDate, totalAlocacao, totalOhFerias,
                        // percentAvaliable);
                        custoRealizadoService
                                .createCustoRealizadoFixedResource(
                                        alocacaoPeriodo, pessoa, dataMes,
                                        totalAlocacao, totalOhFerias,
                                        percentAvaliable);
                    }

                }

			// se adicionou uma nova aloca��o
            } else {
                if (alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                        .doubleValue() != 0) {

                    alocacaoPeriodo.setPercentualUr(BigDecimal.valueOf(0));

                    Alocacao alocacaoNew = alocacaoPeriodo.getAlocacao();
                    alocacaoNew.setCidadeBase(cidadeBaseService
                            .findCidadeBaseById(alocacaoNew.getCidadeBase()
                                    .getCodigoCidadeBase()));
                    alocacaoNew.setPerfilVendido(perfilVendidoService
                            .findPerfilVendidoById(alocacaoNew
                                    .getPerfilVendido()
                                    .getCodigoPerfilVendido()));
                    alocacaoNew.setIndicadorEstagio(Constants.STAGE_COMMITED);
                    alocacaoService.createAlocacao(alocacaoNew);

                    alocacaoPeriodoService
                            .createAlocacaoPeriodo(alocacaoPeriodo);

                    // log da auditoria
                    logJpa.postPersist(alocacaoPeriodoService
                            .findAlocacaoPeriodoById(alocacaoPeriodo
                                    .getCodigoAlocacaoPeriodo()));

                    if (pessoaBillability.getBillability().getSiglaBillability().equals(
                            Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL)) {
                        // cria o CustoRealizado
                        custoRealizadoService.createCustoRealizado(
                                alocacaoPeriodo, pessoa, dataMes,
                                totalAlocacao, totalOhFerias, percentAvaliable);
                    } else {
                        // cria o CustoRealizado Fixed
                        custoRealizadoService
                                .createCustoRealizadoFixedResource(
                                        alocacaoPeriodo, pessoa, dataMes,
                                        totalAlocacao, totalOhFerias,
                                        percentAvaliable);
                    }
                }
            }
        }

        if (!pessoaBillability.getBillability().getSiglaBillability().equals(
                Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL)) {
            // diferenca / restante OH = percentual alocavel - (soma do total
            // alocacoes e total OH/ferias)
            double difTotalAlocacao =
                    percentAvaliable - (totalAlocacao + totalOhFerias);

            // se existir diferenca / restante, grava na tabela
            // CustoTceGrupoCusto
            if (difTotalAlocacao > 0) {
                // cria o CustoTceGrupoCusto
                // custoTceGrupoCustoService.createCustoTceGrupoCusto(pessoa,
                // nextMonthDate, difTotalAlocacao,
                // pessoaGrupoCusto.getGrupoCusto());
                custoTceGrupoCustoService.createCustoTceGrupoCusto(pessoa,
                        dataMes, difTotalAlocacao, pessoaGrupoCusto
                                .getGrupoCusto(), percentAvaliable);
            }
        }

        // se for desalocacao total e for Billable
        if (alocacaoPeriodoList.isEmpty()
                && pessoaBillability.getBillability().getSiglaBillability().equals(
                        Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL)) {
            // cria o CustoRealizado desalocacao total
            custoRealizadoService.createCustoRealizadoDesalocTotal(pessoa,
                    dataMes, totalOhFerias, percentAvaliable);
        }

        // cria o CustoRealizado das ferias (OH)
        if (totalOhFerias > 0) {
            // se for desalocacao total
            // custoRealizadoService.createCustoRealizadoFerias(pessoa,
            // nextMonthDate, totalOhFerias);
            custoRealizadoService.createCustoRealizadoFerias(pessoa, dataMes,
                    totalOhFerias, percentAvaliable);
        }

        // p.setDataMesValidado(nextMonthDate);
        
		// Se valida��o estiver sendo executada pela tela do Closing DRE By
		// Login, n�o atualiza a dataMesValidado
        if (!pessoa.isValidacaoCloseDreByLogin()) {
        	p.setDataMesValidado(dataMes);
       		pessoaService.updatePessoa(p);
        }
        LOGGER.info("End resource validate: " + p.getCodigoLogin());
        return Boolean.valueOf(true);
    }



	/**
	 * Calcula o percentual de alocacao em OH de uma pessoa dentro de um
	 * MM/yyyy.
	 * 
	 * @param dataMes
	 *            - mes/ano da validacao.
	 * @param pessoa
	 *            {@link Pessoa}
	 * @return
	 */
	private double calculaPercentualAlocacaoOH(final Date dataMes,
			final Pessoa pessoa, List<AlocacaoPeriodoOh> alocacaoPeriodoOverheads) {

		double totalOhFerias = Double.valueOf(0);
		Date dataInicio = null;
		Date auxDataInicio = null;
		Date dataFim = null;
		Date auxDataFim = null;
		int qtdeDiasOH = Integer.valueOf(0);
		int firstDay = Integer.valueOf(0);
		int lastDay = Integer.valueOf(0);
		int auxLastDay = Integer.valueOf(0);

		if (alocacaoPeriodoOverheads.isEmpty()) {
            // Lista de AlocacaoPeriodoOh ordenada pela dataInicio e dataFim
            alocacaoPeriodoOverheads = alocaoPeridoOhService
                    .findAlocacaoPeriodoOhByPessoaAndMonthDate(pessoa, dataMes);
        }

		for (AlocacaoPeriodoOh alocacaoPeriodoOh : alocacaoPeriodoOverheads) {

			// Guarda o valor de Data inicial
			dataInicio = alocacaoPeriodoOh.getAlocacaoOverhead()
					.getDataInicio();
			
			// Se o mes da data inicial for anterior ao mes da data de
			// fechamento(dataMes), seta o primeiro dia para 1, se não pega o
			// dia da dataInicio
			if (DateUtil.before(dataInicio, dataMes, Calendar.DAY_OF_MONTH)) {
				firstDay = 1;
			} else {
				firstDay = DateUtil.getDayOfMonth(dataInicio);
			}
			

			// Guarda o valor de Data fim
			dataFim = alocacaoPeriodoOh.getAlocacaoOverhead().getDataFim();

			// Guarda o dia de dataFim ou o ultimo dia do mes da dataMes caso o
			// dataFim for em mes diferente da dataInicio
			lastDay = Integer.valueOf(0);

			// Auxilia guardando o valor do lastDay da iteracao anterior
			auxLastDay = Integer.valueOf(0);

			// Se o mes de dataMes for igual ao mes de dataFim, pega o dia,
			// senao, se o mes de dataFim for posterior ao dataMes/dataInicio,
			// entao pega o ultimo dia do mes (de dataMes)
			if (DateUtil.getMonthNumber(dataMes) == DateUtil
					.getMonthNumber(dataFim)) {
				lastDay = DateUtil.getDayOfMonth(dataFim);
			} else {
				lastDay = DateUtil.getLastDayOfMonth(dataMes);
			}

			// Mesma logica do lastDay, porem comparando o mes de auxDataFim
			// para setar na variavel auxiliar auxLastDay
			if (auxDataFim != null) {
				if (DateUtil.getMonthNumber(dataMes) == DateUtil
						.getMonthNumber(auxDataFim)) {
					auxLastDay = DateUtil.getDayOfMonth(auxDataFim);
				} else {
					auxLastDay = DateUtil.getLastDayOfMonth(dataMes);
				}
			}

			if (auxDataInicio == null) {
				// Na primeira iteracao, pega a quantidade de dias
				qtdeDiasOH += (lastDay - firstDay) + 1;

				// Guarda o valor de dataInicio na variavel auxiliar
				// auxDataInicio (para ser usada na proxima iteracao)
				auxDataInicio = dataInicio;
				// Guarda o valor de dataFim na variavel auxiliar auxDataFim
				// (para ser usada na proxima iteracao)
				auxDataFim = dataFim;

			} else {
				if (DateUtil.before(dataInicio, auxDataFim, Calendar.DATE)
						&& DateUtil.after(dataFim, auxDataFim, Calendar.DATE)) {
					// Se dataInicio for anterior a auxDataFim (valor de dataFim
					// da
					// iteracao anterior), e se a dataFim for posteior a
					// auxDataFim
					// (valor de dataFim da iteracao anterior), ent�o faz a
					// diferenca entre o dia com o dia da iteracao anterior

					qtdeDiasOH += (lastDay - auxLastDay);

					// Atribui o valor de dataFim a auxDataFim e mant�m o
					// auxDataInicio com o valor inicial
					auxDataFim = dataFim;

				} else if (DateUtil
						.after(dataInicio, auxDataFim, Calendar.DATE)) {
					// Se dataInicio for posterior a auxDataFim (valor de
					// dataFim da iteracao anterior), ent�o faz a diferen�a
					// entre a dataInicio e dataFim e soma � qtdeDiasOH

					qtdeDiasOH += (lastDay - firstDay) + 1;

					// Atribui o valor de dataFim a auxDataFim e mant�m o
					// auxDataInicio com o valor inicial
					auxDataFim = dataFim;
				}
			}
		}

		// Calcula o percentual totalOhFerias
		totalOhFerias = Double.valueOf(qtdeDiasOH)
				/ Double.valueOf(DateUtil.getLastDayOfMonth(dataMes));
		return totalOhFerias;
    }

    /**
     * Remove a validacao da pessoa do mes corrente do sistema.
     * 
     * @param pessoa
     *            - entidade do tipo Pessoa.
     * @param dataMes
     *            - mes da remocao.
     */
    public void removeValidate(final Pessoa pessoa, final Date dataMes) {
        // verifica se a dataMes (data corrente) for igual ao mes
        // validado, ou seja, se o mes a ser removido for o ultimo mes (mes
        // validado) remove normalmente. Caso contrario, msg de erro e nao
        // remove
        // Date dataMesValidado = pessoa.getDataMesValidado();
        // if ((dataMesValidado != null) && (dataMes.compareTo(dataMesValidado)
        // == 0)) {
        // Date closingDate = getClosingDate();
        // Date nextMonthDate = DateUtils.addMonths(closingDate, 1);

        Pessoa p = pessoaService.findPessoaById(pessoa.getCodigoPessoa());

        // remove os CustoRealizado
        // List<CustoRealizado> custoRealizadoList =
        // custoRealizadoService.findCustRealizByPessoaAndDataMes(p,
        // nextMonthDate);
        List<CustoRealizado> custoRealizadoList =
                custoRealizadoService.findCustRealizByPessoaAndDataMes(p,
                        dataMes);
        for (CustoRealizado custoRealizado : custoRealizadoList) {
            custoRealizadoService.removeCustoRealizado(custoRealizado);
        }

        // remove os CustoTceGrupoCusto
        // List<CustoTceGrupoCusto> custoTceGCList =
        // custoTceGrupoCustoService.findCusTceGCByPessoaAndDataMes(p,
        // nextMonthDate);
        List<CustoTceGrupoCusto> custoTceGCList =
                custoTceGrupoCustoService.findCusTceGCByPessoaAndDataMes(p,
                        dataMes);
        for (CustoTceGrupoCusto custoTceGrupoCusto : custoTceGCList) {
            custoTceGrupoCustoService
                    .removeCustoTceGrupoCusto(custoTceGrupoCusto);
        }

        // p.setDataMesValidado(closingDate);

        // verifica o ultimo mes validado para a Pessoa passado por parametro
        p.setDataMesValidado(this.getLastValidatedMonthByPessoa(pessoa));

        pessoaService.updatePessoa(p);
        /*
         * } else { if (dataMesValidado != null) { Messages .showError(
         * "removeValidate",
         * Constants.VALIDATE_PERSON_ERROR_REMOVE_NO_LAST_VALID_MONTH, new
         * Object[] { pessoa.getCodigoLogin(), ": " + SIMPLE_DATE_FORMAT
         * .format(dataMesValidado) }); } else { Messages .showError(
         * "removeValidate",
         * Constants.VALIDATE_PERSON_ERROR_REMOVE_NO_LAST_VALID_MONTH, new
         * Object[] {pessoa.getCodigoLogin(), "" }); } }
         */
    }

    /**
     * Cria um objeto AlocacaoPeriodo associado a uma Alocacao.
     * 
     * @param pessoa
     *            - entidade do tipo Pessoa.
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * @param perfilVendido
     *            - entidade do tipo PerfilVendido.
     * @param percentualAlocacao
     *            - percentual alocado.
     * @param dataMes
     *            - mes corrente.
     * 
     * @return retorna a AlocacaoPeriodo com a Alocacao associada.
     * 
     */
    public AlocacaoPeriodo addAlocacao(final Pessoa pessoa,
            final MapaAlocacao mapa, final PerfilVendido perfilVendido,
            final BigDecimal percentualAlocacao, final Date dataMes) {
        Alocacao alocacao = new Alocacao();

        MapaAlocacao m =
                mapaAlocacaoService.findMapaAlocacaoById(mapa
                        .getCodigoMapaAlocacao());

        PerfilVendido pv =
                perfilVendidoService.findPerfilVendidoById(perfilVendido
                        .getCodigoPerfilVendido());

        Pessoa p = pessoaService.findPessoaById(pessoa.getCodigoPessoa());
        CidadeBase cidadeBase = cidadeBaseService.findCurrentCidadeBaseByPessoa(pessoa);

        alocacao.setMapaAlocacao(m);
        alocacao.setPerfilVendido(pv);
        alocacao.setRecurso(p.getRecurso());
        alocacao.setCidadeBase(cidadeBase);
        alocacao.setIndicadorEstagio(Constants.STAGE_RESERVED);
        alocacao.setValorUr(new BigDecimal(0));

        // alocacaoService.createAlocacao(alocacao);

        AlocacaoPeriodo alocacaoPeriodo = new AlocacaoPeriodo();
        // Date nextMonthDate = DateUtils.addMonths(getClosingDate(), 1);

        alocacaoPeriodo.setAlocacao(alocacao);
        // alocacaoPeriodo.setDataAlocacaoPeriodo(nextMonthDate);
        alocacaoPeriodo.setDataAlocacaoPeriodo(dataMes);
        alocacaoPeriodo.setPercentualAlocacaoPeriodo(percentualAlocacao);

        // alocacaoPeriodoService.createAlocacaoPeriodo(alocacaoPeriodo);

        // List<AlocacaoPeriodo> alocacaoPeriodoList = new
        // ArrayList<AlocacaoPeriodo>();
        // alocacaoPeriodoList.add(alocacaoPeriodo);

        // alocacao.setAlocacaoPeriodos(alocacaoPeriodoList);

        return alocacaoPeriodo;
    }

    /**
     * Calcula o percentual de disponibilidade da pessoa no mes corrente.
     * 
     * @param p
     *            - entidade do tipo Pessoa em quest�o.
     * @param dataMes
     *            - mes corrente.
     * 
     * @return retorna o percentual de disponibilidade da pessoa.
     */
    private BigDecimal calculatePercentAvaliable(final Pessoa p,
            final Date dataMes, PessoaTipoContrato pessoaTipoContrato) {

        Date admissionDate = p.getDataAdmissao();
        Date firedDate = p.getDataRescisao();
        Date auxDate = firedDate;

        if (firedDate == null) {
            auxDate = new Date();
        }

        if (pessoaTipoContrato == null) {
            pessoaTipoContrato = pessTCService.findPessTCByPessoaAndDate(p, dataMes);
        }

        BigDecimal percentAvaliable = null;

        if (pessoaTipoContrato != null) {
            percentAvaliable = pessoaTipoContrato.getPercentualAlocavel();
        } else {
            percentAvaliable = p.getPercentualAlocavel();
        }

        // se a data corrente n�o estiver dentro do intervalo de
        if (!DateUtil.isBetween(dataMes, admissionDate, auxDate,
                Calendar.MONTH)) {

            percentAvaliable = BigDecimal.valueOf(0.0);

            // se a data de recisao for igual a data corrente
        } else if (firedDate != null
        		&& DateUtil.isSameDate(dataMes, firedDate, Calendar.MONTH)) {
        	
        	Double lastDay =
        			Double.valueOf(DateUtil.getLastDayOfMonth(firedDate));
        	
        	Double firedDay = Double.valueOf(DateUtil.getDayOfMonth(firedDate));
        	
        	percentAvaliable =
        			BigDecimal.valueOf((firedDay / lastDay)
        					* percentAvaliable.doubleValue());
        	
            // se a dta de admissao for igual a data corrente
        } else if (DateUtil.isSameDate(dataMes, admissionDate,
                Calendar.MONTH)) {

            Double lastDay =
                    Double.valueOf(DateUtil.getLastDayOfMonth(admissionDate));

            Double admissionDay =
                    Double.valueOf(DateUtil.getDayOfMonth(admissionDate));

            percentAvaliable =
                    BigDecimal.valueOf(((lastDay - admissionDay + 1) / lastDay)
                            * percentAvaliable.doubleValue());
        }


        return percentAvaliable;
    }

    /**
     * @return the mailSender
     */
    public MailSenderUtil getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender
     *            the mailSender to set
     */
    public void setMailSender(final MailSenderUtil mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Verifica CustoRealizado e/ou CustoTceGrupoCusto para a Pessoa e o mes
     * passado por parametro.
     * 
     * @param pPessoa
     *            - pessoa que deseja verificar se existe validacao
     * @param dataMes
     *            - mes que deseja verificar se existe validacao
     * 
     * @return true existe validacao para o mes, caso contrario retorna false
     */
    public Boolean hasValidationForTheMonth(final Pessoa pPessoa,
            final Date dataMes) {
        Pessoa pessoa = pessoaService.findPessoaById(pPessoa.getCodigoPessoa());

        List<CustoRealizado> custoRealizadoList =
                custoRealizadoService.findCustRealizByPessoaAndDataMes(pessoa,
                        dataMes);
        List<CustoTceGrupoCusto> custoTceGCList =
                custoTceGrupoCustoService.findCusTceGCByPessoaAndDataMes(
                        pessoa, dataMes);

        if (custoRealizadoList.size() > 0 || custoTceGCList.size() > 0) {
            return Boolean.valueOf(true);
        } else {
            return Boolean.valueOf(false);
        }
    }

    /**
     * Verifica qual o ultimo mes validado no CustoRealizado e/ou
     * CustoTceGrupoCusto para a Pessoa passado por parametro.
     * 
     * @param pPessoa
     *            - pessoa que deseja verificar se existe validacao
     * 
     * @return ultimo mes validado ou null caso nao tiver
     */
    public Date getLastValidatedMonthByPessoa(final Pessoa pPessoa) {
        Pessoa pessoa = pessoaService.findPessoaById(pPessoa.getCodigoPessoa());

        CustoRealizado custoRealizado =
                custoRealizadoService
                        .findCustoRealizMaxStartDateByPessoa(pessoa);
        CustoTceGrupoCusto custoTceGC =
                custoTceGrupoCustoService
                        .findCusTceGCMaxStartDateByPessoa(pessoa);

        Date dataMesCustoRealizado = null;
        if (custoRealizado != null) {
            dataMesCustoRealizado = custoRealizado.getDataMes();
        }

        Date dataMesCustoTceGC = null;
        if (custoTceGC != null) {
            dataMesCustoTceGC = custoTceGC.getDataMes();
        }

        if (dataMesCustoRealizado != null && dataMesCustoTceGC != null) {
            if (dataMesCustoRealizado.compareTo(dataMesCustoTceGC) >= 0) {
                return dataMesCustoRealizado;
            } else {
                return dataMesCustoTceGC;
            }
        } else if (dataMesCustoRealizado != null) {
            return dataMesCustoRealizado;
        } else if (dataMesCustoTceGC != null) {
            return dataMesCustoTceGC;
        } else {
            return null;
        }
    }
    
	/**
	 * Registra o objeto observer (que fica "escutando" qualquer atualiza��o no
	 * percentual de conclus�o do processo)
	 * 
	 * @param obs
	 *            Objeto que ficara observando
	 */
	public void registerObserver(Observer o) {

		boolean isRegistered = false;
		for (int i = 0; i < observers.size(); i++) {
			if (observers.get(i).equals(o)) {
				isRegistered = true;
				break;
			}
		}
		if (!isRegistered) {
			observers.add(o);
		}
	}

    /**
	 * Notifica o percentual do processo concluido atualizado aos Observers
	 * 
	 * @param percentualConcluido
	 *            percentual do processo j� executado
	 */
	public void notifyObservers(final Float progress) {
		for (int i = 0; i < observers.size(); i++) {
			Observer observer = (Observer) observers.get(i);
			observer.update(this, progress);
		}
	}

    @Override
    public List<ClosingDREPessoaVO> loadPeopleInformation(List<Pessoa> people, Date month) {

        LOGGER.info("Init: Allocation validation");
        Set<Long> resourceCodes = this.getResourceCodesFromPeople(people);
        Set<Long> peopleCodes = this.getPeopleCodes(people);

        Map<String, List<AlocacaoPeriodo>> allocationPeriods = this.getAllocationPeriodFromResourceCodesAndMonth(resourceCodes, month);
        Map<String, PessoaBillability> peopleBillability = this.getBillabilityFromPeopleCodesAndMonth(peopleCodes, month);
        Map<String, PessoaTipoContrato> peopleContractType = this.getContractTypeFromPeopleCodesAndMonth(peopleCodes, month);
        Map<String, List<AlocacaoPeriodoOh>> allocationPeriodOverheads = this.getAllocationPeriodOverheadFromPeopleCodesAndMonth(peopleCodes, month);
        Map<String, PessoaGrupoCusto> peopleCostCenter = this.getCostCenterFromPeopleCodesAndMonth(peopleCodes, month);
        LOGGER.info("End: Allocation validation");
        return this.mergeInformationIntoPeopleVO(people, allocationPeriods, peopleBillability,
                peopleContractType, allocationPeriodOverheads, peopleCostCenter);
    }

    @Override
    public Boolean validatePeople(ClosingDREPessoaVO pessoa, Date month) {
        double totalAlocacao = Double.valueOf(0);

        Pessoa p = pessoa.getPerson();

        LOGGER.info("Starting resouce validate: " + p.getCodigoLogin());
        System.out.println("Starting resouce validate: " + p.getCodigoLogin());

        PessoaBillability pessoaBillability = pessoa.getBillability();
        if (pessoaBillability == null || pessoaBillability.getBillability() == null) {
            Messages.showError("validatePessoa",
                    Constants.VALIDATE_PERSON_ERROR_VALIDATE_COST_GROUP,
                    pessoa.getPerson().getCodigoLogin());
            LOGGER.info("PessoaGrupoCusto is null to: " + p.getCodigoLogin());
            return Boolean.valueOf(false);
        }


        // calcula o percentual de alocavel disponivel no mes da pessoa.
        double percentAvaliable =
                calculatePercentAvaliable(pessoa.getPerson(), month, pessoa.getContractType()).doubleValue();

        double totalOhFerias = 0.0;
        if (pessoa.getOverheads() != null && !pessoa.getOverheads().isEmpty()) {
            // calcula o percentual em OH da pessoa no mes/ano
            totalOhFerias = calculaPercentualAlocacaoOH(month, pessoa.getPerson(), pessoa.getOverheads());
        }

        List<AlocacaoPeriodo> periodos = new ArrayList<AlocacaoPeriodo>();
        if (pessoa.getAllocationPeriods() != null && !pessoa.getAllocationPeriods().isEmpty()) {
            periodos = pessoa.getAllocationPeriods();
        }

        // soma o percentual de alocacao em contrato-pratica
        for (AlocacaoPeriodo alocacaoPeriodo : periodos) {
            totalAlocacao +=
                    alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                            .doubleValue();
        }

        for (AlocacaoPeriodo alocacaoPeriodo : periodos) {
            // se aloca��o j� existente
            if (alocacaoPeriodo.getCodigoAlocacaoPeriodo() != null) {
                if (alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                        .doubleValue() != 0) {
                    alocacaoPeriodo.setPercentualUr(alocacaoPeriodo
                            .getAlocacao().getValorUr());

                    alocacaoPeriodoService
                            .updateAlocacaoPeriodo(alocacaoPeriodo);

                    // log da auditoria
                    logJpa.preUpdate(alocacaoPeriodoService
                            .findAlocacaoPeriodoById(alocacaoPeriodo
                                    .getCodigoAlocacaoPeriodo()));


                    if (pessoaBillability.getBillability().getSiglaBillability().equals(
                            Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL)) {
                        // cria o CustoRealizado
                        // custoRealizadoService.createCustoRealizado(alocacaoPeriodo,
                        // pessoa, nextMonthDate, totalAlocacao, totalOhFerias,
                        // percentAvaliable);
                        custoRealizadoService.createCustoRealizado(
                                alocacaoPeriodo, pessoa.getPerson(), month,
                                totalAlocacao, totalOhFerias, percentAvaliable);
                    } else {
                        // cria o CustoRealizado Fixed
                        // custoRealizadoService.createCustoRealizadoFixedResource(alocacaoPeriodo,
                        // pessoa, nextMonthDate, totalAlocacao, totalOhFerias,
                        // percentAvaliable);
                        custoRealizadoService
                                .createCustoRealizadoFixedResource(
                                        alocacaoPeriodo, pessoa.getPerson(), month,
                                        totalAlocacao, totalOhFerias,
                                        percentAvaliable);
                    }

                }

                // se adicionou uma nova alocação
            } else {
                if (alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                        .doubleValue() != 0) {

                    alocacaoPeriodo.setPercentualUr(BigDecimal.valueOf(0));

                    Alocacao alocacaoNew = alocacaoPeriodo.getAlocacao();
                    alocacaoNew.setCidadeBase(cidadeBaseService
                            .findCidadeBaseById(alocacaoNew.getCidadeBase()
                                    .getCodigoCidadeBase()));
                    alocacaoNew.setPerfilVendido(perfilVendidoService
                            .findPerfilVendidoById(alocacaoNew
                                    .getPerfilVendido()
                                    .getCodigoPerfilVendido()));
                    alocacaoNew.setIndicadorEstagio(Constants.STAGE_COMMITED);
                    alocacaoService.createAlocacao(alocacaoNew);

                    alocacaoPeriodoService
                            .createAlocacaoPeriodo(alocacaoPeriodo);

                    // log da auditoria
                    logJpa.postPersist(alocacaoPeriodoService
                            .findAlocacaoPeriodoById(alocacaoPeriodo
                                    .getCodigoAlocacaoPeriodo()));

                    if (pessoaBillability.getBillability().getSiglaBillability().equals(
                            Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL)) {
                        // cria o CustoRealizado
                        custoRealizadoService.createCustoRealizado(
                                alocacaoPeriodo, pessoa.getPerson(), month,
                                totalAlocacao, totalOhFerias, percentAvaliable);
                    } else {
                        // cria o CustoRealizado Fixed
                        custoRealizadoService
                                .createCustoRealizadoFixedResource(
                                        alocacaoPeriodo, pessoa.getPerson(), month,
                                        totalAlocacao, totalOhFerias,
                                        percentAvaliable);
                    }
                }
            }
        }

        if (!pessoaBillability.getBillability().getSiglaBillability().equals(
                Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL)) {
            // diferenca / restante OH = percentual alocavel - (soma do total
            // alocacoes e total OH/ferias)
            double difTotalAlocacao =
                    percentAvaliable - (totalAlocacao + totalOhFerias);

            // se existir diferenca / restante, grava na tabela
            // CustoTceGrupoCusto
            if (difTotalAlocacao > 0) {
                // cria o CustoTceGrupoCusto
                // custoTceGrupoCustoService.createCustoTceGrupoCusto(pessoa,
                // nextMonthDate, difTotalAlocacao,
                // pessoaGrupoCusto.getGrupoCusto());
                custoTceGrupoCustoService.createCustoTceGrupoCusto(pessoa.getPerson(),
                        month, difTotalAlocacao, pessoa.getCostCenter()
                                .getGrupoCusto(), percentAvaliable);
            }
        }

        // se for desalocacao total e for Billable
        if (periodos.isEmpty()
                && pessoaBillability.getBillability().getSiglaBillability().equals(
                Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL)) {
            // cria o CustoRealizado desalocacao total
            custoRealizadoService.createCustoRealizadoDesalocTotal(pessoa.getPerson(),
                    month, totalOhFerias, percentAvaliable);
        }

        // cria o CustoRealizado das ferias (OH)
        if (totalOhFerias > 0) {
            // se for desalocacao total
            // custoRealizadoService.createCustoRealizadoFerias(pessoa,
            // nextMonthDate, totalOhFerias);
            custoRealizadoService.createCustoRealizadoFerias(pessoa.getPerson(), month,
                    totalOhFerias, percentAvaliable);
        }

        // Se valida��o estiver sendo executada pela tela do Closing DRE By
        // Login, n�o atualiza a dataMesValidado
        if (!pessoa.getPerson().isValidacaoCloseDreByLogin()) {
            p.setDataMesValidado(month);
            pessoaService.updatePessoa(p);
        }
        LOGGER.info("End resource validate: " + p.getCodigoLogin());
        return Boolean.valueOf(true);
    }

    private List<ClosingDREPessoaVO> mergeInformationIntoPeopleVO(List<Pessoa> people,
                                                                  Map<String, List<AlocacaoPeriodo>> allocationPeriods,
                                                                  Map<String, PessoaBillability> peopleBillability,
                                                                  Map<String, PessoaTipoContrato> peopleContractType,
                                                                  Map<String, List<AlocacaoPeriodoOh>> allocationPeriodOverheads,
                                                                  Map<String, PessoaGrupoCusto> peopleCostCenter) {

	    List<ClosingDREPessoaVO> peopleVO = new ArrayList<ClosingDREPessoaVO>();

	    for (Pessoa person : people) {
	        ClosingDREPessoaVO vo = new ClosingDREPessoaVO();
	        vo.setCode(person.getCodigoPessoa());
	        vo.setLogin(person.getCodigoLogin());
	        vo.setPerson(person);
	        vo.setAllocationPeriods(allocationPeriods.get(person.getCodigoLogin()));
	        vo.setBillability(peopleBillability.get(person.getCodigoLogin()));
	        vo.setContractType(peopleContractType.get(person.getCodigoLogin()));
	        vo.setOverheads(allocationPeriodOverheads.get(person.getCodigoLogin()));
	        vo.setCostCenter(peopleCostCenter.get(person.getCodigoLogin()));

	        peopleVO.add(vo);
        }

        return peopleVO;
    }

    private Map<String, List<AlocacaoPeriodoOh>> getAllocationPeriodOverheadFromPeopleCodesAndMonth(Set<Long> peopleCodes, Date month) {
	    List<AlocacaoPeriodoOh> allocationPeriodOverheads = alocaoPeridoOhService.findByPeopleCodeInAndMonth(peopleCodes, month);
	    Map<String, List<AlocacaoPeriodoOh>> allocationPeriodOverheadMap = new HashMap<String, List<AlocacaoPeriodoOh>>();

	    for (AlocacaoPeriodoOh overhead : allocationPeriodOverheads) {
	        if (allocationPeriodOverheadMap.get(overhead.getAlocacaoOverhead().getPessoa().getCodigoLogin()) != null) {
                allocationPeriodOverheadMap.get(overhead.getAlocacaoOverhead().getPessoa().getCodigoLogin()).add(overhead);
            } else {
	            List<AlocacaoPeriodoOh> overheads = new ArrayList<AlocacaoPeriodoOh>();
	            overheads.add(overhead);
	            allocationPeriodOverheadMap.put(overhead.getAlocacaoOverhead().getPessoa().getCodigoLogin(), overheads);
            }
        }
        return allocationPeriodOverheadMap;
    }

    private Map<String, PessoaTipoContrato> getContractTypeFromPeopleCodesAndMonth(Set<Long> peopleCodes, Date month) {
	    List<PessoaTipoContrato> contractTypes = pessTCService.findByPeopleCodesInAndMonth(peopleCodes, month);
	    Map<String, PessoaTipoContrato> contractTypeMap = new HashMap<String, PessoaTipoContrato>();

	    for (PessoaTipoContrato contractType : contractTypes) {
	        contractTypeMap.put(contractType.getPessoa().getCodigoLogin(), contractType);
        }

        return contractTypeMap;
    }

    private Map<String, PessoaGrupoCusto> getCostCenterFromPeopleCodesAndMonth(Set<Long> peopleCodes, Date month) {
	    List<PessoaGrupoCusto> costCenters = pessoaGrupoCustoService.findByPeopleCodeInAndDate(peopleCodes, month);
	    Map<String, PessoaGrupoCusto> costCenterMap = new HashMap<String, PessoaGrupoCusto>();

	    for (PessoaGrupoCusto costCenter : costCenters) {
	        costCenterMap.put(costCenter.getPessoa().getCodigoLogin(), costCenter);
        }

        return costCenterMap;
    }

    private Map<String, PessoaBillability> getBillabilityFromPeopleCodesAndMonth(final Set<Long> peopleCodes, final Date month) {
        List<PessoaBillability> billabilities = pessoaBillabilityService.findByPeopleCodeInAndMonth(peopleCodes, month);
        Map<String, PessoaBillability> billabilityMap = new HashMap<String, PessoaBillability>();

        for (PessoaBillability billability : billabilities) {
            billabilityMap.put(billability.getPessoa().getCodigoLogin(), billability);
        }

        return billabilityMap;
    }

    private Map<String, List<AlocacaoPeriodo>> getAllocationPeriodFromResourceCodesAndMonth(final Set<Long> resourceCodes, final Date month) {
        List<AlocacaoPeriodo> periods = alocacaoPeriodoService.findByResourceCodeInAndMonth(resourceCodes, month);
        Map<String, List<AlocacaoPeriodo>> mapPeriods = new HashMap<String, List<AlocacaoPeriodo>>();

        for (AlocacaoPeriodo periodo : periods) {
            if (mapPeriods.get(periodo.getAlocacao().getRecurso().getCodigoMnemonico()) != null) {
                mapPeriods.get(periodo.getAlocacao().getRecurso().getCodigoMnemonico()).add(periodo);
            } else {
                List<AlocacaoPeriodo> allocationPeriods = new ArrayList<AlocacaoPeriodo>();
                allocationPeriods.add(periodo);
                mapPeriods.put(periodo.getAlocacao().getRecurso().getCodigoMnemonico(), allocationPeriods);
            }
        }

        return mapPeriods;
    }

    private Set<Long> getResourceCodesFromPeople(List<Pessoa> pessoas) {
	    Set<Long> resourceCodes = new HashSet<Long>();
	    for (Pessoa person : pessoas) {
	        resourceCodes.add(person.getRecurso().getCodigoRecurso());
        }
        return resourceCodes;
    }

    private Set<Long> getPeopleCodes(List<Pessoa> pessoas) {
        Set<Long> peopleCodes = new HashSet<Long>();
        for (Pessoa person : pessoas) {
            peopleCodes.add(person.getCodigoPessoa());
        }
        return peopleCodes;
    }

}