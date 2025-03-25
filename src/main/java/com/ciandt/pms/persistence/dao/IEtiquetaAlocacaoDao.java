/**
 * Interface da camada de DAO da entidade Etiqueta
 */
package com.ciandt.pms.persistence.dao;

import java.util.Map;

import com.ciandt.pms.model.EtiquetaAlocacao;
import com.ciandt.pms.model.EtiquetaAlocacaoId;


/**
 * 
 * A classe IEtiquetaAlocacaoDao proporciona a interface de acesso para a camada
 * DAO da entidade EtiquetaAlocacao.
 * 
 * @since 19/10/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IEtiquetaAlocacaoDao extends
        IAbstractDao<EtiquetaAlocacaoId, EtiquetaAlocacao> {

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
    Map<Long, EtiquetaAlocacao> findByEtiquetaAndMapaAlocacao(
            final Long codigoEtiqueta, final Long codigoMapaAlocacao);

}