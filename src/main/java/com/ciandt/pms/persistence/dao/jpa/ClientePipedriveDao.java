package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.ClientePipedrive;
import com.ciandt.pms.persistence.dao.IClientePipedriveDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author peter
 * 
 */
@Repository
public class ClientePipedriveDao extends AbstractDao<Long, ClientePipedrive>
		implements IClientePipedriveDao {

	/**
	 * Contrutor padrão.
	 *
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public ClientePipedriveDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ClientePipedrive.class);
	}

	public List<ClientePipedrive> findByName(String nomeClientePipedrive) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nomeClientePipedrive", nomeClientePipedrive);

		List<ClientePipedrive> results = getJpaTemplate().findByNamedQueryAndNamedParams(
				ClientePipedrive.FIND_BY_NAME, params);

		if (results.isEmpty()) {
			return null;
		}

		return results;
	}

}
