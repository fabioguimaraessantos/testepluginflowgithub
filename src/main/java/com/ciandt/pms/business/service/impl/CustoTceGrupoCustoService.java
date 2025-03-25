package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICustoTceGrupoCustoService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.model.CustoTceGrupoCusto;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.ICustoTceGrupoCustoDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 24/02/2010
 */
@Service
public class CustoTceGrupoCustoService implements ICustoTceGrupoCustoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private ICustoTceGrupoCustoDao custoTceGrupoCustoDao;

    /** Intancia do serviço PessoaService. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /** Instancia do servico GrupoCusto. */
    @Autowired
    private IGrupoCustoService grupoCustoService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createCustoTceGrupoCusto(final CustoTceGrupoCusto entity) {
        entity.setDataAtualizacao(new Date());
        custoTceGrupoCustoDao.create(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    public void removeCustoTceGrupoCusto(final CustoTceGrupoCusto entity) {
        custoTceGrupoCustoDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public CustoTceGrupoCusto findCustoTceGrupoCustoById(final Long id) {
        return custoTceGrupoCustoDao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<CustoTceGrupoCusto> findCustoTceGrupoCustoAll() {
        return custoTceGrupoCustoDao.findAll();
    }

    /**
     * Cria um objeto CustoTceGrupoCusto para validacao.
     * 
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMesValidar
     *            - dataMes que será validado
     * @param difTotalAlocacao
     *            - diferenca / restante do total das alocacoes que será
     *            validado
     * @param pGrupoCusto
     *            - GrupoCusto que a Pessoa está associada
     * @param percentualAlocavelMes
     *            - percentual alocável da pessoa no mes corrente.
     */
    public void createCustoTceGrupoCusto(final Pessoa pPessoa,
            final Date dataMesValidar, final double difTotalAlocacao,
            final GrupoCusto pGrupoCusto, final Double percentualAlocavelMes) {
        Pessoa pessoa = pessoaService.findPessoaById(pPessoa.getCodigoPessoa());
        GrupoCusto grupoCusto = grupoCustoService
                .findGrupoCustoById(pGrupoCusto.getCodigoGrupoCusto());

        Moeda moeda = moedaService.
        		findMoedaByAcronym(Constants.SIGLA_MOEDA_REAL);

        // cria o CustoTceGrupoCusto OH
        CustoTceGrupoCusto entity = new CustoTceGrupoCusto();
        entity.setDataMes(dataMesValidar);
        entity.setPessoa(pessoa);
        entity.setGrupoCusto(grupoCusto);
        entity.setIndicadorTipo(Constants.CUSTO_REALIZADO_TYPE_OH);
        entity.setValorTceMes(BigDecimal.ZERO);
        entity.setValorTaxaCotacao(BigDecimal.ONE);
        entity.setMoeda(moeda);
        entity.setPercentualAlocacaoMes(BigDecimal.valueOf(difTotalAlocacao));
        entity.setDataAtualizacao(new Date());
        entity.setPercentualAlocavel(BigDecimal.valueOf(percentualAlocavelMes));
        custoTceGrupoCustoDao.create(entity);
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
    public List<CustoTceGrupoCusto> findCusTceGCByPessoaAndDataMes(
            final Pessoa pessoa, final Date dataMes) {
        return custoTceGrupoCustoDao.findByPessoaAndDataMes(pessoa, dataMes);
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * 
     * @return lista de entidades que atendem ao criterio da Pessoa.
     */
    public CustoTceGrupoCusto findCusTceGCMaxStartDateByPessoa(
            final Pessoa pessoa) {
        return custoTceGrupoCustoDao.findMaxStartDateByPessoa(pessoa);
    }

}