package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.PessoaBancoHoras;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 12/08/2010
 */
@Repository
public interface IPessoaBancoHorasDao extends
        IAbstractDao<Long, PessoaBancoHoras> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<PessoaBancoHoras> findByFilter(final PessoaBancoHoras filter);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    PessoaBancoHoras findUnique(final PessoaBancoHoras filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PessoaBancoHoras> findAll();
    
    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<PessoaBancoHoras> findByDataMes(final Date dataMes);

}