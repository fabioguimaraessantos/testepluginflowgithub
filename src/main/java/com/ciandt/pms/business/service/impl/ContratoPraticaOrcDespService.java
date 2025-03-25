package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IContratoPraticaOrcDespService;
import com.ciandt.pms.model.ContratoPraticaOrcDesp;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IContratoPraticaOrcDespDao;


/**
 * 
 * A classe ContratoPraticaOrcDespService proporciona as funcionalidades de
 * servico para entidade ContratoPraticaOrcDesp.
 * 
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class ContratoPraticaOrcDespService implements
        IContratoPraticaOrcDespService {

    /** Instancia de dao. */
    @Autowired
    private IContratoPraticaOrcDespDao dao;

    /**
     * Cria entidade no banco.
     * 
     * @param entity
     *            entidade.
     * @return true.
     */
    @Transactional
    public Boolean createContratoPraticaOrcDesp(
            final ContratoPraticaOrcDesp entity) {
        dao.create(entity);
        return Boolean.TRUE;
    }
    
    /**
     * Remove entidade do banco.
     * @param entity ContratoPraticaOrcDesp.
     */
    @Transactional
    public void removeContratoPraticaOrcDesp(final ContratoPraticaOrcDesp entity) {
        dao.remove(entity);
    }
    
    /**
     * Busca por orcamentoDespesa.
     * @param entity orcaemntoDespesa.
     * @return lista.
     */
    @Transactional
    public List<ContratoPraticaOrcDesp> findByOrcamentoDespesa(final OrcamentoDespesa entity) {
        return dao.findByOrcDesp(entity);
    }


}
