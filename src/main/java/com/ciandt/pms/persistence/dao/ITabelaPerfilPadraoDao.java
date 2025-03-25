package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TabelaPerfilPadrao;


/**
 * 
 * A classe ITabelaPerfilPadraoDao proporciona a interfece de acesso para o
 * banco de dados referente a entidade TabelaPerfilPadrao.
 * 
 * @since 19/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public interface ITabelaPerfilPadraoDao extends
        IAbstractDao<Long, TabelaPerfilPadrao> {
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TabelaPerfilPadrao> findAll();
    
    /**
     * Retorna Tabela perfil padrao com maior data de incio.
     * @return tabela com maior data.
     */
    TabelaPerfilPadrao findMaxStartDate();
    
    /**
     * Busca maior data de inicio por moeda.
     * 
     * @param moeda
     *            moeda.
     * @return TabelaPerfilPadrao
     */
    TabelaPerfilPadrao findMaxStartDataByCurrency(final Moeda moeda);


}
