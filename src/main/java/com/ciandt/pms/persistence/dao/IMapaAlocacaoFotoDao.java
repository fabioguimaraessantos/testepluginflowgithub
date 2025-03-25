/**
 * Interface da camada de DAO da entidade MapaAlocacaoFoto
 */
package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MapaAlocacaoFoto;

/**
 * 
 * A classe IMapaAlocacaoFotoDao proporciona a interface de acesso para a camada
 * DAO da entidade MapaAlocacaoFoto.
 * 
 * @since 17/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IMapaAlocacaoFotoDao extends
        IAbstractDao<Long, MapaAlocacaoFoto> {

    /**
     * Atualiza o status dos registros as tabelas Foto.
     * 
     * @return código do resultado da execução da atualização
     */
    Integer updateStatusFotos();

}