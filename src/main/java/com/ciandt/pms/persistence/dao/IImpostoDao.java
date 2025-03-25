package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Imposto;


/**
 * 
 * A classe IContratoPraticaDao proporciona a interface de acesso para a camada
 * DAO da entidade ContratoPratica.
 * 
 * @since 08/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
public interface IImpostoDao extends IAbstractDao<Long, Imposto> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Imposto> findAll();
}
