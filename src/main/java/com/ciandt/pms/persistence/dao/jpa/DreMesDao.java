package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.persistence.dao.IDreMesDao;

/**
 * A classe DreMesDao proporciona as funcionalidades da camada de persistencia
 * referente a entidade DreMes.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Repository
public class DreMesDao extends AbstractDao<Long, DreMes> implements IDreMesDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - fabrica do entity manager.
	 */
	@Autowired
	public DreMesDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, DreMes.class);

	}

	/**
	 * Realiza a busca de um {@link DreMes} por uma data base.
	 * 
	 * @param dataMes
	 *            data base para busca da entidade.
	 * @return {@link DreMes}
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public DreMes findByDataMes(final Date dataMes) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataMes", dataMes);

		List<DreMes> dreMeses = getJpaTemplate()
				.findByNamedQueryAndNamedParams(DreMes.FIND_BY_DATA_MES, params);

		if (dreMeses.size() > 0) {
			return dreMeses.get(0);
		}
		
		return null;
	}

}
