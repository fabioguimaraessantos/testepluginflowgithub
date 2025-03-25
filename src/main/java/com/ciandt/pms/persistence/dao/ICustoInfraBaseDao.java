package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CustoInfraBase;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Repository
public interface ICustoInfraBaseDao extends IAbstractDao<Long, CustoInfraBase> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<CustoInfraBase> findByFilter(final CustoInfraBase filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoInfraBase> findAll();

    /**
     * Busca a entidade filtrando pelos parametros.
     * 
     * @param dataMes
     *            do CustoInfraBase.
     * @param cidadeBase
     *            entidade populada com os valores da CidadeBase.
     * 
     * @return lista de entidades que atendem ao criterio da CidadeBase e
     *         dataMes.
     */
    CustoInfraBase findByDateAndCidadeBase(final Date dataMes,
            final CidadeBase cidadeBase);

}