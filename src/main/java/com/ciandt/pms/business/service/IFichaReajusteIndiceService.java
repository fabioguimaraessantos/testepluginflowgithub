package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.FichaReajusteIndice;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IFichaReajusteIndiceService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
	FichaReajusteIndice findFichaReajusteIndiceById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<FichaReajusteIndice> findFichaReajusteIndiceAll();

	/**
	 * Retorna o indice com nome igual a {@code nomeIndice}
	 *
	 * @param nome
	 * @return {@link FichaReajusteIndice}
	 */
	FichaReajusteIndice findFichaReajusteIndiceByNome(String nomeIndice);
}