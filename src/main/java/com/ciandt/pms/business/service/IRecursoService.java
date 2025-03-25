package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Recurso;


/**
 * 
 * A classe IRecrusoService proporciona a interface de acesso para o serviços
 * referentes a entidade Recurso.
 * 
 * @since 21/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IRecursoService {
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createRecurso(final Recurso entity);

    /**
     * Realiza o update de uma entidade.
     * 
     * @param entity
     *            entidade a relizar o update.
     */
    @Transactional
    void updateRecurso(final Recurso entity);
    
    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeRecurso(final Recurso entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    Recurso findRecursoById(final Long entityId);

    /**
     * Realiza uma busca rápida.
     * 
     * @param value
     *            utilizado na busca
     * @param tipoRecurso 
     *          tipo de serviço
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
    List<Recurso> findRecursoAll();
    
    /**
     * Verifica se o recurso é do tipo pessoa (PE).
     * 
     * @param mnemonico que se deseja saber o tipo
     * 
     * @return true se recurso é do tipo Pessoa (PE), caso contrário false.
     */
    Boolean isPessoa(final String mnemonico);
    
    /**
     * Realiza uma busca pelo tipo.
     * 
     * @param value
     *            utilizado na busca
     * 
     * @return com os resultados
     */
    List<Recurso> findRecursoByTipo(final String value);
    
    /**
     * Realiza uma busca pelo mnemonico do recurso.
     * 
     * @param mnemonico
     *            utilizado na busca
     * 
     * @return com os resultados
     */
    Recurso findRecursoByMnemonico(final String mnemonico);
}
