package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.DreProcesso;

/**
 * A classe IDreProcessoDao proporciona a interface de acesso para a camada DAO
 * da entidade Processo.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
public interface IDreProcessoDao extends IAbstractDao<Long, DreProcesso> {

	/**
	 * Retorna o último processo executado.
	 * 
	 * @param codigoProcesso
	 *            Código do processo
	 * @param monthYear
	 *            MM/yyyy
	 * @param indicadorPorLogin
	 *            - <li>S - Processo executado para um mes/ano, especificando
	 *            login</li><li>N - Processo executado para um mes/ano, sem
	 *            especificar um login</li>
	 * @return {@link DreProcesso}
	 */
	DreProcesso findLastByProcessoDataAndIndPorLogin(final Long codigoProcesso,
			final String monthYear, final String indicadorPorLogin);

	/**
	 * Retorna todos DreProcesso com um Dremes
	 * 
	 * @param dreMes
	 *            dreMes
	 * @return Lista de {@link DreProcesso}
	 */
	List<DreProcesso> findAllByDreMes(final DreMes dreMes);

}
