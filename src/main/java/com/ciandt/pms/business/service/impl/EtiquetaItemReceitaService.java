/**
 * Classe EtiquetaItemReceitaService - Implementação do serviço do EtiquetaItemReceita
 */
package com.ciandt.pms.business.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IEtiquetaItemReceitaService;
import com.ciandt.pms.model.EtiquetaItemReceita;
import com.ciandt.pms.model.EtiquetaItemReceitaId;
import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.persistence.dao.IEtiquetaItemReceitaDao;


/**
 * A classe EtiquetaItemReceitaService proporciona as funcionalidades de serviço
 * para a entidade EtiquetaItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class EtiquetaItemReceitaService implements IEtiquetaItemReceitaService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IEtiquetaItemReceitaDao etiquetaItemReceitaDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createEtiquetaItemReceita(final EtiquetaItemReceita entity) {
        etiquetaItemReceitaDao.create(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    public void removeEtiquetaItemReceita(final EtiquetaItemReceita entity) {
        etiquetaItemReceitaDao.remove(entity);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public EtiquetaItemReceita findEtiquetaItemReceitaById(
            final EtiquetaItemReceitaId entityId) {
        return etiquetaItemReceitaDao.findById(entityId);
    }

    /**
     * Busca um mapa de entidades pelo filtro.
     * 
     * @param codigoEtiqueta
     *            código da Etiqueta da busca.
     * @param codigoReceita
     *            código do Receita corrente.
     * 
     * @return mapa de entidades que atendem ao criterio de filtro.
     */
    public Map<Long, EtiquetaItemReceita> findEtiqItemReceitaByEtiquetaAndReceita(
            final Long codigoEtiqueta, final Long codigoReceita) {
        return etiquetaItemReceitaDao.findByEtiquetaAndReceita(codigoEtiqueta,
                codigoReceita);
    }
    
    /**
     * Busca um mapa de entidades pelo filtro.
     * 
     * @param itemReceita
     *            ItemReceita da busca.
     * 
     * @return mapa de entidades que atendem ao criterio de filtro.
     */
    public List<EtiquetaItemReceita> findEtiqItemReceitaByItemReceita(
            final ItemReceita itemReceita) {
        return etiquetaItemReceitaDao.findByItemReceita(itemReceita);
    }

}