package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IReceitaTipoService;
import com.ciandt.pms.model.ReceitaTipo;
import com.ciandt.pms.persistence.dao.IReceitaTipoDao;

/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 14/11/2013
 */
@Service
public class ReceitaTipoService implements IReceitaTipoService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IReceitaTipoDao dao;

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<ReceitaTipo> findAll() {
    	return dao.findAll();
    }

    public ReceitaTipo findById(ReceitaTipo receitaTipo) {
    	return dao.findById(receitaTipo.getCodigoReceitaTipo());
    }
    /**
     * Busca um {@link ReceitaTipo} pelo seu nome.
     *
     * @return {@link ReceitaTipo}
     */
    public ReceitaTipo findByNomeReceitaTipo(final String nomeReceitaTipo) {
    	return dao.findByNomeReceitaTipo(nomeReceitaTipo);
    }

    /**
     * Busca um {@link ReceitaTipo} pelo sua sigla.
     *
     * @return {@link ReceitaTipo}
     */
    public ReceitaTipo findBySiglaReceitaTipo(final String siglaReceitaTipo) {
    	return dao.findBySiglaReceitaTipo(siglaReceitaTipo);
    }
}