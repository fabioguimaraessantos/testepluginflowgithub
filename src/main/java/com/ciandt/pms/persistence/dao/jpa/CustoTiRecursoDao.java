package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CustoTiRecurso;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.persistence.dao.ICustoTiRecursoDao;


/**
 * 
 * A classe CustoTiRecursoDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade CustoTiRecurso.
 * 
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class CustoTiRecursoDao extends AbstractDao<Long, CustoTiRecurso>
        implements ICustoTiRecursoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - Fabrica da entidade
     */
    @Autowired
    public CustoTiRecursoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, CustoTiRecurso.class);

    }

    /**
     * Busca pelo proximo custo, referente a data de inicio.
     * 
     * @param custo
     *            - entidade do tipo CustoTiRecurso.
     * 
     * @return retorna o proximo CustoTiRecurso.
     */
    @SuppressWarnings("unchecked")
    public CustoTiRecurso findNext(final CustoTiRecurso custo) {

        Long codigoTiRecurso = custo.getTiRecurso().getCodigoTiRecurso();

        List<CustoTiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(
                        CustoTiRecurso.FIND_NEXT,
                        new Object[]{codigoTiRecurso, codigoTiRecurso,
                                custo.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca pelo custo anterior, referente a data de inicio.
     * 
     * @param custo
     *            - entidade do tipo CustoTiRecurso.
     * 
     * @return retorna o CustoTiRecurso anterior.
     */
    @SuppressWarnings("unchecked")
    public CustoTiRecurso findPrevious(final CustoTiRecurso custo) {

        Long codigoTiRecurso = custo.getTiRecurso().getCodigoTiRecurso();

        List<CustoTiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(
                        CustoTiRecurso.FIND_PREVIOUS,
                        new Object[]{codigoTiRecurso, codigoTiRecurso,
                                custo.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca os custoTiRecurso por tiRecurso.
     * 
     * @param tiRecurso
     *            - tiRecurso com a busca
     * @return lista com o resultado da consulta
     */
    @SuppressWarnings("unchecked")
    public List<CustoTiRecurso> findByTiRecurso(final TiRecurso tiRecurso) {
        Long codigoTiRecurso = tiRecurso.getCodigoTiRecurso();

        List<CustoTiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(
                        CustoTiRecurso.FIND_BY_TI_RECURSO,
                        new Object[]{codigoTiRecurso});

        return listResult;
    }

}
