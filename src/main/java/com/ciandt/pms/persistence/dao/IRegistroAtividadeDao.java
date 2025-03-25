package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.RegistroAtividade;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 17/08/2010
 */
@Repository
public interface IRegistroAtividadeDao extends
        IAbstractDao<Long, RegistroAtividade> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<RegistroAtividade> findByFilter(final RegistroAtividade filter);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    RegistroAtividade findUnique(final RegistroAtividade filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<RegistroAtividade> findAll();
    
    /**
     * Busca uma lista de entidades pela dataMesChp.
     * 
     * @param dataMesChp
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<RegistroAtividade> findByDataMesChp(final Date dataMesChp);

}