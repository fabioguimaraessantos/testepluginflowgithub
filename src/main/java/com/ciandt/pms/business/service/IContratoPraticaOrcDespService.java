package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPraticaOrcDesp;
import com.ciandt.pms.model.OrcamentoDespesa;


/**
 * 
 * A classe IContratoPraticaOrcDespService proporciona as funcionalidades de
 * interface para ContratoPraticaOrcDespService.
 * 
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface IContratoPraticaOrcDespService {

    /**
     * Cria entidade no banco.
     * 
     * @param entity
     *            entidade.
     * @return true.
     */
    Boolean createContratoPraticaOrcDesp(final ContratoPraticaOrcDesp entity);

    /**
     * Busca por orcamentoDespesa.
     * 
     * @param entity
     *            orcaemntoDespesa.
     * @return lista.
     */
    List<ContratoPraticaOrcDesp> findByOrcamentoDespesa(
            final OrcamentoDespesa entity);
    
    /**
     * Remove entidade do banco.
     * @param entity ContratoPraticaOrcDesp.
     */
    void removeContratoPraticaOrcDesp(final ContratoPraticaOrcDesp entity);

}
