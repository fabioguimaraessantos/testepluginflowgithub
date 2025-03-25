package com.ciandt.pms.persistence.dao;

import java.util.Date;

import com.ciandt.pms.model.DreMes;

/**
 * A classe IDreMes proporciona a interface de acesso para a camada DAO da
 * entidade DreMes.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
public interface IDreMesDao extends IAbstractDao<Long, DreMes> {

	/**
	 * Realiza a busca de um {@link DreMes} por uma data base.
	 * 
	 * @param dataMes
	 *            data base para busca da entidade.
	 * @return {@link DreMes}
	 */
	DreMes findByDataMes(final Date dataMes);

}
