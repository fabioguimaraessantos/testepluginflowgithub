/**
 * Interface da camada de DAO da entidade Etiqueta
 */
package com.ciandt.pms.persistence.dao;

import java.util.List;
import java.util.Map;

import com.ciandt.pms.model.EtiquetaItemReceita;
import com.ciandt.pms.model.EtiquetaItemReceitaId;
import com.ciandt.pms.model.ItemReceita;


/**
 * 
 * A classe IEtiquetaItemReceitaDao proporciona a interface de acesso para a
 * camada DAO da entidade EtiquetaItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IEtiquetaItemReceitaDao extends
        IAbstractDao<EtiquetaItemReceitaId, EtiquetaItemReceita> {

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
    Map<Long, EtiquetaItemReceita> findByEtiquetaAndReceita(
            final Long codigoEtiqueta, final Long codigoReceita);
    
    
    /**
     * Busca um mapa de entidades pelo filtro.
     * 
     * @param itemReceita
     *            ItemReceita da busca.
     * 
     * @return mapa de entidades que atendem ao criterio de filtro.
     */
    List<EtiquetaItemReceita> findByItemReceita(final ItemReceita itemReceita);

}