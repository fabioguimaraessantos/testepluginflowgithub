/**
 * Classe EtiquetaAlocacaoService - Implementação do serviço do EtiquetaAlocacao
 */
package com.ciandt.pms.business.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IEtiquetaAlocacaoService;
import com.ciandt.pms.model.EtiquetaAlocacao;
import com.ciandt.pms.model.EtiquetaAlocacaoId;
import com.ciandt.pms.persistence.dao.IEtiquetaAlocacaoDao;


/**
 * A classe EtiquetaAlocacaoService proporciona as funcionalidades de serviço
 * para a entidade EtiquetaAlocacao.
 * 
 * @since 19/10/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class EtiquetaAlocacaoService implements IEtiquetaAlocacaoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IEtiquetaAlocacaoDao etiquetaAlocacaoDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createEtiquetaAlocacao(final EtiquetaAlocacao entity) {
        etiquetaAlocacaoDao.create(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    public void removeEtiquetaAlocacao(final EtiquetaAlocacao entity) {
        etiquetaAlocacaoDao.remove(entity);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public EtiquetaAlocacao findEtiquetaAlocacaoById(
            final EtiquetaAlocacaoId entityId) {
        return etiquetaAlocacaoDao.findById(entityId);
    }

    /**
     * Busca um mapa de entidades pelo filtro.
     * 
     * @param codigoEtiqueta
     *            código da Etiqueta da busca.
     * @param codigoMapaAlocacao
     *            código do MapaAlocacao corrente.
     * 
     * @return mapa de entidades que atendem ao criterio de filtro.
     */
    public Map<Long, EtiquetaAlocacao> findEtiqAlocByEtiquetaAndMapaAlocacao(
            final Long codigoEtiqueta, final Long codigoMapaAlocacao) {
        return etiquetaAlocacaoDao.findByEtiquetaAndMapaAlocacao(
                codigoEtiqueta, codigoMapaAlocacao);
    }

}