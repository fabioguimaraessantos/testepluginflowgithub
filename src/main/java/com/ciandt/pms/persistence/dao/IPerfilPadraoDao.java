package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.PerfilPadrao;
import com.ciandt.pms.model.Pmg;


/**
 * 
 * A classe ITabelaPerfilPadraoDao proporciona a interfece de acesso para o
 * banco de dados referente a entidade PerfilPadrao.
 *
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public interface IPerfilPadraoDao extends IAbstractDao<Long, PerfilPadrao> {
    
    /**
     * 
     * @param cargo cargo
     * @param pmg pmg
     * @param base base
     * @return PerfilPadrao
     */
    List<PerfilPadrao> findByCargoPmgAndCidadeBase(final CargoPms cargo,
            final Pmg pmg, final CidadeBase base);
    
    /**
     * 
     * @param cargoPms cargoPms.
     * @return lista.
     */
    List<PerfilPadrao> findByCargoPms(final CargoPms cargoPms);

}
