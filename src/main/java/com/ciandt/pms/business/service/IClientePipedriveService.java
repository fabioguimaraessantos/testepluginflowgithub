package com.ciandt.pms.business.service;

import com.ciandt.pms.model.ClientePipedrive;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author peter
 * 
 */
@Service
public interface IClientePipedriveService {


	@Transactional
	void create(ClientePipedrive clientePipedrive);

	@Transactional
	void create(List<ClientePipedrive> clientePipedrives);

	@Transactional
	void update(ClientePipedrive clientePipedrive);

	@Transactional
	void remove(ClientePipedrive clientePipedrive);

	List<ClientePipedrive> findByName(String nomeClientePipedrive);

	ClientePipedrive findById(Long id);

	List<ClientePipedrive> findByIdOrName(String searchTerm);
}
