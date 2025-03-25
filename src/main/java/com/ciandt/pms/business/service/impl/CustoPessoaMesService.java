package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAbstractDreProcessoExecutavelService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.ICustoPessoaMesService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IPessoaBancoHorasService;
import com.ciandt.pms.business.service.IPessoaGrupoCustoService;
import com.ciandt.pms.business.service.IPessoaSalarioService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IPessoaTipoContratoService;
import com.ciandt.pms.business.service.IRegistroAtividadeService;
import com.ciandt.pms.business.service.IValidacaoPessoaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.CustoPessoaCp;
import com.ciandt.pms.model.CustoPessoaGc;
import com.ciandt.pms.model.CustoPessoaMes;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaBancoHoras;
import com.ciandt.pms.model.PessoaGrupoCusto;
import com.ciandt.pms.model.PessoaSalario;
import com.ciandt.pms.model.PessoaTipoContrato;
import com.ciandt.pms.model.RegistroAtividade;
import com.ciandt.pms.persistence.dao.ICustoPessoaMesDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 30/08/2010
 */
@Service
public class CustoPessoaMesService implements ICustoPessoaMesService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private ICustoPessoaMesDao custoPessoaMesDao;

    /** Instancia do servico - RegistroAtividade. */
    @Autowired
    private IRegistroAtividadeService registroAtividadeService;

    /** Instancia do servico - Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do servico - PessoaSalario. */
    @Autowired
    private IPessoaSalarioService pessoaSalarioService;

    /** Instancia do servico - PessoaTipoContrato. */
    @Autowired
    private IPessoaTipoContratoService pessoaTipoContratoService;

    /** Instancia do servico - PessoaBancoHoras. */
    @Autowired
    private IPessoaBancoHorasService pessoaBancoHorasService;

    /** Instancia do servico - PessoaGrupoCusto. */
    @Autowired
    private IPessoaGrupoCustoService pessoaGrupoCustoService;

    /** Instancia do servico - ValidacaoPessoa. */
    @Autowired
    private IValidacaoPessoaService validacaoPessoaService;

    /** Instancia do servico - GrupoCusto. */
    @Autowired
    private IGrupoCustoService grupoCustoService;

    /** Instancia do Servico da entidade ContratoPratica. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /**
     * FIXME: **** ATENÇÃO: NÃO PRECISA FAZER MERGE PARA O NOVO CLOSING DRE **** 
     * Instancia do Servico da entidade DreProcessoRegistraPlantaoHorasExtrasService. 
     * */
    @Autowired
    @Qualifier("dreProcessoRegistraPlantaoHorasExtrasService")
    private IAbstractDreProcessoExecutavelService registraPlantaoHorasExtrasService;
	
	
	
	

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createCustoPessoaMes(final CustoPessoaMes entity) {
        custoPessoaMesDao.create(entity);
    }

    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    public void updateCustoPessoaMes(final CustoPessoaMes entity) {
        custoPessoaMesDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    public void removeCustoPessoaMes(final CustoPessoaMes entity) {
        custoPessoaMesDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public CustoPessoaMes findCustoPessoaMesById(final Long id) {
        return custoPessoaMesDao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<CustoPessoaMes> findCustoPessoaMesAll() {
        return custoPessoaMesDao.findAll();
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
    public List<CustoPessoaMes> findCustPessMesByPessoaAndDataMes(
            final Pessoa pessoa, final Date dataMes) {
        return custoPessoaMesDao.findByPessoaAndDataMes(pessoa, dataMes);
    }

    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    public List<CustoPessoaMes> findCustPessMesByDataMes(final Date dataMes) {
        return custoPessoaMesDao.findByDataMes(dataMes);
    }

    /**
     * FIXME: **** ATENÇÃO: NÃO PRECISA FAZER MERGE PARA O NOVO CLOSING DRE **** 
     * 
	 * Faz a apropriacao do Plantao e das Horas Extras por Valor. Método criado
	 * somente para fechamento do antigo Closing DRE com a nova apropriacao de
	 * PL e HE por valor.
	 * 
	 * @param dataMes
	 *            - mes da apropriacao
	 * @return true se apropriou corretamente, caso contrario false
	 */
    public Boolean registerPLAndHEByValue(final Date dataMes) {
    	return registraPlantaoHorasExtrasService.processOldClosindDreByValue(dataMes);    	
    }

    /**
     * Faz a apropriacao do Plantao e das Horas Extras.
     * 
     * @param dataMes
     *            - mes da apropriacao
     * @return true se apropriou corretamente, caso contrario false
     */
    @Deprecated
    public Boolean registerPLAndHECosts(final Date dataMes) {
        synchronized (CustoPessoaMesService.class) {

            // remove todos os registros do mes corrente para inserir novamente
            List<CustoPessoaMes> custoPessoaMesList =
                    this.findCustPessMesByDataMes(dataMes);
            for (CustoPessoaMes custoPessoaMes : custoPessoaMesList) {
                this.removeCustoPessoaMes(custoPessoaMes);
            }

            Boolean success = Boolean.valueOf(true);

            // inicio PLANTAO
            List<RegistroAtividade> raList =
                    registroAtividadeService
                            .findRegistroAtividadeByDataMesChp(dataMes);

            CustoPessoaMes cpm = null;

            for (RegistroAtividade ra : raList) {
                // CustoPessoaMes
                if ((cpm == null)
                        || (ra.getPessoa().getCodigoPessoa() != cpm.getPessoa()
                                .getCodigoPessoa())) {
                    Pessoa pessoa =
                            pessoaService.findPessoaById(ra.getPessoa()
                                    .getCodigoPessoa());

                    PessoaSalario pessSal =
                            pessoaSalarioService.findPessSalByPessoaAndDate(
                                    pessoa, dataMes);
                    if (pessSal == null) {
                        Messages
                                .showError(
                                        "registerPLAndHECosts",
                                        Constants.REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_SALARY,
                                        pessoa.getCodigoLogin());
                        success = Boolean.valueOf(false);
                    }

                    PessoaTipoContrato pessTC =
                            pessoaTipoContratoService
                                    .findPessTCByPessoaAndDate(pessoa, dataMes);
                    if (pessTC == null) {
                        Messages
                                .showError(
                                        "registerPLAndHECosts",
                                        Constants.REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_CONTRACT_TYPE,
                                        pessoa.getCodigoLogin());
                        success = Boolean.valueOf(false);
                    }

                    // se algum Recurso está incompleto, vai para proximo
                    if (!success) {
                        continue;
                    }

                    cpm = new CustoPessoaMes();
                    cpm.setPessoa(pessoa);
                    cpm.setDataMes(dataMes);
                    cpm.setValorSalario(pessSal.getValorSalario());
                    cpm.setValorJornada(pessTC.getValorJornada());
                    cpm
                            .setIndicadorOrigem(Constants.CUSTO_PESSOA_MES_ORIGEM_PLANTAO);
                    cpm.setMoeda(pessSal.getMoeda());

                    // cria o CustoPessoaMes
                    this.createCustoPessoaMes(cpm);
                }

                List<CustoPessoaCp> cpcpList = new ArrayList<CustoPessoaCp>();

                List<CustoPessoaGc> cpgcList = new ArrayList<CustoPessoaGc>();

                if (ra.getContratoPratica() != null) {

                    // CustoPessoaCp
                    CustoPessoaCp cpcp = new CustoPessoaCp();
                    cpcp.setCustoPessoaMes(cpm);
                    cpcp.setContratoPratica(contratoPraticaService
                            .findContratoPraticaById(ra.getContratoPratica()
                                    .getCodigoContratoPratica()));
                    cpcp.setPercentualApropriado(BigDecimal.valueOf(1));
                    cpcp.setNumeroHoras(ra.getNumeroHoras());
                    cpcp.setValorFator(ra.getAtividade().getValorFator());

                    cpcpList.add(cpcp);

                } else {
                    // CustoPessoaCp
                    CustoPessoaGc cpgc = new CustoPessoaGc();
                    cpgc.setCustoPessoaMes(cpm);
                    cpgc.setGrupoCusto(grupoCustoService.findGrupoCustoById(ra
                            .getGrupoCusto().getCodigoGrupoCusto()));
                    cpgc.setPercentualApropriado(BigDecimal.valueOf(1));
                    cpgc.setNumeroHoras(ra.getNumeroHoras());
                    cpgc.setValorFator(ra.getAtividade().getValorFator());

                    cpgcList.add(cpgc);
                }

                cpm.setCustoPessoaCps(cpcpList);
                cpm.setCustoPessoaGcs(cpgcList);

                // atualiza o CustoPessoaMes e a lista de CustoPessoaCp
                this.updateCustoPessoaMes(cpm);
            }
            // fim PLANTAO

            // se tiver recursos incompletos, nao faz a verificacao das horas
            // extras
            if (!success) {
                return success;
            }

            // inicio HORAS EXTRAS
            List<PessoaBancoHoras> pbhList =
                    pessoaBancoHorasService.findPessBcoHrsByDataMes(dataMes);

            cpm = null;
            PessoaGrupoCusto pgc = null;
            List<AlocacaoPeriodo> alocacaoPeriodoList = null;
            double totalAlocacao = Double.valueOf(0);

            for (PessoaBancoHoras pbh : pbhList) {

                // CustoPessoaMes
                if ((cpm == null)
                        || (pbh.getPessoa().getCodigoPessoa() != cpm
                                .getPessoa().getCodigoPessoa())) {

                    totalAlocacao = Double.valueOf(0);

                    Pessoa pessoa =
                            pessoaService.findPessoaById(pbh.getPessoa()
                                    .getCodigoPessoa());

                    pgc =
                            pessoaGrupoCustoService.findPessGCByPessoaAndDate(
                                    pessoa, dataMes);
                    if (pgc == null) {
                        Messages
                                .showError(
                                        "registerPLAndHECosts",
                                        Constants.REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_COST_GROUP,
                                        pessoa.getCodigoLogin());
                        success = Boolean.valueOf(false);
                    }

                    PessoaSalario pessSal =
                            pessoaSalarioService.findPessSalByPessoaAndDate(
                                    pessoa, dataMes);
                    if (pessSal == null) {
                        Messages
                                .showError(
                                        "registerPLAndHECosts",
                                        Constants.REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_SALARY,
                                        pessoa.getCodigoLogin());
                        success = Boolean.valueOf(false);
                    }

                    PessoaTipoContrato pessTC =
                            pessoaTipoContratoService
                                    .findPessTCByPessoaAndDate(pessoa, dataMes);
                    if (pessTC == null) {
                        Messages
                                .showError(
                                        "registerPLAndHECosts",
                                        Constants.REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_CONTRACT_TYPE,
                                        pessoa.getCodigoLogin());
                        success = Boolean.valueOf(false);
                    }

                    // se algum Recurso está incompleto, vai para proximo
                    if (!success) {
                        continue;
                    }

                    cpm = new CustoPessoaMes();
                    cpm.setPessoa(pessoa);
                    cpm.setDataMes(dataMes);
                    cpm.setValorSalario(pessSal.getValorSalario());
                    cpm.setValorJornada(pessTC.getValorJornada());
                    cpm
                            .setIndicadorOrigem(Constants.CUSTO_PESSOA_MES_ORIGEM_HORAS_EXTRAS);
                    cpm.setMoeda(pessSal.getMoeda());

                    // cria o CustoPessoaMes
                    this.createCustoPessoaMes(cpm);

                    // recupera a lista de AlocacaoPeriodo (alocacoes dos mapas)
                    alocacaoPeriodoList =
                            validacaoPessoaService.getAlocacaoPeriodoList(
                                    pessoa, dataMes);
                    // soma o percentual de alocacao
                    for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoList) {
                        totalAlocacao +=
                                alocacaoPeriodo.getPercentualAlocacaoPeriodo()
                                        .doubleValue();
                    }
                }

                List<CustoPessoaCp> cpcpList = new ArrayList<CustoPessoaCp>();
                List<CustoPessoaGc> cpgcList = new ArrayList<CustoPessoaGc>();

                // se tem Alocacao, apropria no ContratoPratica, caso contrário
                // apropria no GrupoCusto.
                if (alocacaoPeriodoList.size() > 0) {
                    for (AlocacaoPeriodo ap : alocacaoPeriodoList) {
                        // CustoPessoaCp
                        CustoPessoaCp cpcp = new CustoPessoaCp();
                        cpcp.setCustoPessoaMes(cpm);
                        cpcp.setContratoPratica(contratoPraticaService
                                .findContratoPraticaById(ap.getAlocacao()
                                        .getMapaAlocacao().getContratoPratica()
                                        .getCodigoContratoPratica()));
                        cpcp.setPercentualApropriado(BigDecimal.valueOf(ap
                                .getPercentualAlocacaoPeriodo().doubleValue()
                                / totalAlocacao));
                        cpcp.setNumeroHoras(pbh.getNumeroHoras());
                        cpcp.setValorFator(pbh.getValorFator());

                        cpcpList.add(cpcp);
                    }

                    cpm.setCustoPessoaCps(cpcpList);
                } else {
                    // CustoPessoaGc
                    CustoPessoaGc cpgc = new CustoPessoaGc();
                    cpgc.setCustoPessoaMes(cpm);
                    cpgc.setGrupoCusto(grupoCustoService.findGrupoCustoById(pgc
                            .getGrupoCusto().getCodigoGrupoCusto()));
                    cpgc.setPercentualApropriado(BigDecimal.valueOf(1));
                    cpgc.setNumeroHoras(pbh.getNumeroHoras());
                    cpgc.setValorFator(pbh.getValorFator());

                    cpgcList.add(cpgc);

                    cpm.setCustoPessoaGcs(cpgcList);
                }

                // atualiza o CustoPessoaMes e as listas de CustoPessoaCp e
                // CustoPessoaGc
                this.updateCustoPessoaMes(cpm);
            }
            // fim HORAS EXTRAS

            return success;
        }
    }

}