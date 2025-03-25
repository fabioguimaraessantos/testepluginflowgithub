package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.persistence.dao.IRecursoDao;


/**
 * 
 * A classe RecursoService proporciona as funcionalidades de serviços referentes
 * a entidade Recruso.
 * 
 * @since 21/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class RecursoService implements IRecursoService {

    /** Instancia do DAO da entidade RecursoService. */
    @Autowired
    private IRecursoDao recursoDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createRecurso(final Recurso entity) {
        recursoDao.create(entity);
    }
    
    /**
     * Realiza o update de uma entidade.
     * 
     * @param entity
     *            entidade a relizar o update.
     */
    public void updateRecurso(final Recurso entity) {
        recursoDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeRecurso(final Recurso entity) {
        recursoDao.remove(entity);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public Recurso findRecursoById(final Long entityId) {
        return recursoDao.findById(entityId);
    }

    /**
     * Realiza uma busca rápida.
     * 
     * @param value
     *            utilizado na busca
     * @param tipoRecurso 
     *          tipo de recurso
     * 
     * @return com os resultados
     */
    public List<Recurso> quickSearch(
            final String value, final String tipoRecurso) {
        return recursoDao.quickSearch(value, tipoRecurso);
    }

    /**
     * Retorna todas os recursos.
     * 
     * @return retorna uma lista com todos os recursos.
     */
    public List<Recurso> findRecursoAll() {
        return recursoDao.findAll();
    }

    /**
     * Verifica se o recurso é do tipo pessoa (PE).
     * 
     * @param mnemonico
     *            que se deseja saber o tipo
     * 
     * @return true se recurso é do tipo Pessoa (PE), caso contrário false.
     */
    public Boolean isPessoa(final String mnemonico) {
        
        if (mnemonico != null) {
            Recurso r = recursoDao.findByMnemonico(mnemonico);
    
            if (Constants.RESOURCE_TYPE_PE.equals(r.getIndicadorTipoRecurso())) {
                return Boolean.TRUE;
            }
        }
           
        return Boolean.FALSE;
    }

    /**
     * Realiza uma busca pelo tipo.
     * 
     * @param value
     *            utilizado na busca
     * 
     * @return com os resultados
     */
    public List<Recurso> findRecursoByTipo(final String value) {
        return recursoDao.findByTipo(value);
    }
    
    /**
     * Realiza uma busca pelo mnemonico do recurso.
     * 
     * @param mnemonico
     *            utilizado na busca
     * 
     * @return com os resultados
     */
    public Recurso findRecursoByMnemonico(final String mnemonico) {
        return recursoDao.findByMnemonico(mnemonico);
    }

}