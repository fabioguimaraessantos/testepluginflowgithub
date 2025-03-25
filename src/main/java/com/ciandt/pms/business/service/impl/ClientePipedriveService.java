package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IClientePipedriveService;
import com.ciandt.pms.model.ClientePipedrive;
import com.ciandt.pms.persistence.dao.IClientePipedriveDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author peter
 * 
 */
@Service
public class ClientePipedriveService implements IClientePipedriveService {

	private Logger logger = LoggerFactory.getLogger(ClientePipedriveService.class);

	/**
	 * Dao da entidade DocumentoLegal.
	 */
	@Autowired
	private IClientePipedriveDao dao;


	@Override
	@Transactional
	public void create(ClientePipedrive clientePipedrive) {
		dao.create(clientePipedrive);
	}

	@Override
	@Transactional
	public void create(List<ClientePipedrive> clientePipedrives) {
		for (ClientePipedrive clientePipedrive : clientePipedrives) {
			logger.debug(clientePipedrive.getCodigoClientePipedrive().toString());
			System.out.println(clientePipedrive.getCodigoClientePipedrive().toString());
			dao.create(clientePipedrive);
		}
	}

	@Override
	public void update(ClientePipedrive clientePipedrive) {
		dao.update(clientePipedrive);
	}

	@Override
	public void remove(ClientePipedrive clientePipedrive) {
		dao.remove(clientePipedrive);
	}

	public ClientePipedrive findById(Long id) {
		return dao.findById(id);
	}

	public List<ClientePipedrive> findByName(String clientePipedriveName) {
		return dao.findByName(clientePipedriveName);
	}

	public List<ClientePipedrive> findByIdOrName(String searchTerm) {
		ClientePipedrive byId = dao.findById(Long.getLong(searchTerm));
		List<ClientePipedrive> byName = dao.findByName(searchTerm);

		byName.add(byId);
		return byName;
	}
}