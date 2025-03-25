package com.ciandt.pms.persistence.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.model.CustoBasePapelAlocacao;
import com.ciandt.pms.persistence.dao.ICustoBasePapelAlocacaoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 27/01/2010
 */
@Repository
public class CustoBasePapelAlocacaoDao extends
        AbstractDao<Long, CustoBasePapelAlocacao> implements
        ICustoBasePapelAlocacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo CustoBasePapelAlocacao
     */
    @Autowired
    public CustoBasePapelAlocacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CustoBasePapelAlocacao.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<CustoBasePapelAlocacao> findAll() {
        List<CustoBasePapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(CustoBasePapelAlocacao.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo BasePapelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    @SuppressWarnings("unchecked")
    public List<CustoBasePapelAlocacao> findByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao) {

        List<CustoBasePapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(
                        CustoBasePapelAlocacao.FIND_BY_BASE_PAPEL_ALOCACAO,
                        new Object[] {basePapelAlocacao
                                .getCodigoBasePapelAlocacao() });

        return listResult;
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do BasePapelAlocacao.
     */
    @SuppressWarnings("unchecked")
    public CustoBasePapelAlocacao findMaxStartDateByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao) {

        List<CustoBasePapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(
                        CustoBasePapelAlocacao.FIND_MAX_START_DATE_BY_BASE_PAPEL_ALOCACAO,
                        new Object[] {
                                basePapelAlocacao.getCodigoBasePapelAlocacao(),
                                basePapelAlocacao.getCodigoBasePapelAlocacao() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca a entidade no qual intervalo possua a pada passado por parametro,
     * referente ao BasePapelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * @param date
     *            Data que se deseja saber o valor do CustoBase do
     *            PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do BasePapelAlocacao.
     */
    @SuppressWarnings("unchecked")
    public CustoBasePapelAlocacao findByBasePapelAndDate(
            final BasePapelAlocacao basePapelAlocacao, final Date date) {

        List<CustoBasePapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(
                        CustoBasePapelAlocacao.FIND_BY_BASE_PAPEL_AND_DATE,
                        new Object[] {
                                basePapelAlocacao.getCodigoBasePapelAlocacao(),
                                date, date });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);

    }

    /**
     * Busca pelo fator de reajuste referente a data de inicio.
     * 
     * @param custoBasePapelAlocacao
     *            - entidade do tipo CustoBasePapelAlocacao.
     * 
     * @return retorna o CustoBasePapelAlocacao.
     */
    @SuppressWarnings("unchecked")
    public CustoBasePapelAlocacao findByStartDate(
            final CustoBasePapelAlocacao custoBasePapelAlocacao) {

        List<CustoBasePapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(
                        CustoBasePapelAlocacao.FIND_BY_START_DATE,
                        new Object[] {
                                custoBasePapelAlocacao.getDataInicio(),
                                custoBasePapelAlocacao.getBasePapelAlocacao()
                                        .getCodigoBasePapelAlocacao() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param custoBasePapelAlocacao
     *            - entidade do tipo CustoBasePapelAlocacao.
     * 
     * @return retorna o proximo CustoBasePapelAlocacao.
     */
    @SuppressWarnings("unchecked")
    public CustoBasePapelAlocacao findNext(
            final CustoBasePapelAlocacao custoBasePapelAlocacao) {

        Long codigo = custoBasePapelAlocacao.getBasePapelAlocacao()
                .getCodigoBasePapelAlocacao();

        List<CustoBasePapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(
                        CustoBasePapelAlocacao.FIND_NEXT,
                        new Object[] {codigo, codigo,
                                custoBasePapelAlocacao.getDataInicio() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param custoBasePapelAlocacao
     *            - entidade do tipo CustoBasePapelAlocacao.
     * 
     * @return retorna o CustoBasePapelAlocacao anterior.
     */
    @SuppressWarnings("unchecked")
    public CustoBasePapelAlocacao findPrevious(
            final CustoBasePapelAlocacao custoBasePapelAlocacao) {

        Long codigo = custoBasePapelAlocacao.getBasePapelAlocacao()
                .getCodigoBasePapelAlocacao();

        List<CustoBasePapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(
                        CustoBasePapelAlocacao.FIND_PREVIOUS,
                        new Object[] {codigo, codigo,
                                custoBasePapelAlocacao.getDataInicio() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @SuppressWarnings("unchecked")
    public CustoBasePapelAlocacao findByBasePapelAndCurrentDate(
            final BasePapelAlocacao basePapelAlocacao) {

        List<CustoBasePapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(
                        CustoBasePapelAlocacao.FIND_BY_BASE_PAPEL_AND_CURRENT_DATE,
                        new Object[] {
                                basePapelAlocacao.getCodigoBasePapelAlocacao()});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @Override
    public List<CustoBasePapelAlocacao> findByStartDateGreaterThan(final Long basePapelAlocacaoCode, final Date startDate) {
        List<CustoBasePapelAlocacao> resultList = getJpaTemplate().findByNamedQuery(
                CustoBasePapelAlocacao.FIND_BY_START_DATE_GREATER_THAN,
                new Object[] {
                        basePapelAlocacaoCode,
                        startDate
                });
        if (resultList.isEmpty()) {
            return new ArrayList<CustoBasePapelAlocacao>();
        }

        return resultList;
    }

}