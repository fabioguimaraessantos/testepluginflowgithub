package com.ciandt.pms.business.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.EtiquetaAlocacao;
import com.ciandt.pms.model.EtiquetaAlocacaoId;


/**
 * 
 * A classe IEtiquetaAlocacaoService proporciona a interface de acesso para a
 * camada de serviço referente a entidade EtiquetaAlocacao.
 * 
 * @since 19/10/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IEtiquetaAlocacaoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createEtiquetaAlocacao(final EtiquetaAlocacao entity);

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    @Transactional
    void removeEtiquetaAlocacao(final EtiquetaAlocacao entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    EtiquetaAlocacao findEtiquetaAlocacaoById(final EtiquetaAlocacaoId entityId);

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
    Map<Long, EtiquetaAlocacao> findEtiqAlocByEtiquetaAndMapaAlocacao(
            final Long codigoEtiqueta, final Long codigoMapaAlocacao);

}