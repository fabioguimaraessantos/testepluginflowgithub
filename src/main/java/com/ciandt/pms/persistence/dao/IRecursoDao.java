package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Recurso;


/**
 * 
 * A classe IRecursoDao proporciona a interface de acesso
 * ao banco de dados para a entidade Recurso.
 *
 * @since 21/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IRecursoDao extends IAbstractDao<Long, Recurso> {

    
    /**
     * Realiza uma busca rápida.
     * 
     * @param value
     *            utilizado na busca
     * @param tipoRecurso
     *          tipo do recurso (Pessoa ou Papel)
     * 
     * @return com os resultados
     */
    List<Recurso> quickSearch(
            final String value, final String tipoRecurso);
    
    /**
     * Retorna todas os recursos.
     * 
     * @return retorna uma lista com todos os recursos.
     */
    List<Recurso> findAll();
    
    /**
     * Realiza uma busca pelo mnemonico do recurso.
     * 
     * @param value
     *            utilizado na busca
     * 
     * @return com os resultados
     */
    Recurso findByMnemonico(final String value);
    
    /**
     * Realiza uma busca pelo tipo.
     * 
     * @param value
     *            utilizado na busca
     * 
     * @return com os resultados
     */
    List<Recurso> findByTipo(final String value);
}
