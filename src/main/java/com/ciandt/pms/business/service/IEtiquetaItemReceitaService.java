package com.ciandt.pms.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.EtiquetaItemReceita;
import com.ciandt.pms.model.EtiquetaItemReceitaId;
import com.ciandt.pms.model.ItemReceita;


/**
 * 
 * A classe IEtiquetaItemReceitaService proporciona a interface de acesso para a
 * camada de serviço referente a entidade EtiquetaItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IEtiquetaItemReceitaService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createEtiquetaItemReceita(final EtiquetaItemReceita entity);

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    @Transactional
    void removeEtiquetaItemReceita(final EtiquetaItemReceita entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    EtiquetaItemReceita findEtiquetaItemReceitaById(
            final EtiquetaItemReceitaId entityId);

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
    Map<Long, EtiquetaItemReceita> findEtiqItemReceitaByEtiquetaAndReceita(
            final Long codigoEtiqueta, final Long codigoReceita);
    
    /**
     * Busca um mapa de entidades pelo filtro.
     * 
     * @param itemReceita
     *            ItemReceita da busca.
     * 
     * @return mapa de entidades que atendem ao criterio de filtro.
     */
    List<EtiquetaItemReceita> findEtiqItemReceitaByItemReceita(
            final ItemReceita itemReceita);

}