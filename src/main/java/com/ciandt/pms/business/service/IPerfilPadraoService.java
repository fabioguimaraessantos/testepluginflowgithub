package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.PerfilPadrao;
import com.ciandt.pms.model.Pmg;


/**
 * 
 * A classe IPerfilPadraoService proporciona as funcionalidades de ... para ...
 *
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public interface IPerfilPadraoService {

    /**
     * Cria uma entidade no banco.
     * @param entity entidade
     * @return true
     */
    @Transactional
    Boolean createPerfilPadrao(final PerfilPadrao entity);
    
    /**
     * 
     * @param cargo cargo
     * @param pmg pmg
     * @param base base
     * @return PerfilPadrao
     */
    @Transactional
    List<PerfilPadrao> findByCargoPmgAndCidadeBase(final CargoPms cargo,
            final Pmg pmg, final CidadeBase base);
    
    /**
     * Busca por id.
     * @param id id
     * @return entidade
     */
    @Transactional
    PerfilPadrao findPerfilPadraoById(final Long id);
    
    /**
     * 
     * @param cargoPms cargoPms.
     * @return lista.
     */
    @Transactional
    List<PerfilPadrao> findByCargoPms(final CargoPms cargoPms);
    
}
