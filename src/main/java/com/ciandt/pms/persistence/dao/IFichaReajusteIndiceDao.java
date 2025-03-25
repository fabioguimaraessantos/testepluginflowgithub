package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FichaReajusteIndice;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Repository
public interface IFichaReajusteIndiceDao extends IAbstractDao<Long, FichaReajusteIndice> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<FichaReajusteIndice> findAll();

	/**
	 * Retorna o indice com nome igual a {@code nomeIndice}
	 *
	 * @param nome
	 * @return {@link FichaReajusteIndice}
	 */
	FichaReajusteIndice findByNome(String nomeIndice);
}