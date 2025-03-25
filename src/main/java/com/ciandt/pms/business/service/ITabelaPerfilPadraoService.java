package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TabelaPerfilPadrao;


/**
 * 
 * A classe ITabelaPerfilPadraoService proporciona as funcionalidades de ... para ...
 *
 * @since 19/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public interface ITabelaPerfilPadraoService {

    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * @return true se sucesso.
     */
    @Transactional
    Boolean createTabelaPerfilPadrao(final TabelaPerfilPadrao entity);
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TabelaPerfilPadrao> findTabelaPerfilPadraoAll();
    
    /**
     * Retorna Tabela perfil padrao com maior data de incio.
     * @return tabela com maior data.
     */
    TabelaPerfilPadrao findMaxStartDate();
    
    /**
     * @param id id
     * @return entidade referente ao id
     */
    TabelaPerfilPadrao findById(final Long id);
    
    /**
     * Remove item da tabela de perfilPadrao.
     * 
     * @param entity
     *            entidade a ser removida
     * @return boolean
     */
    @Transactional
    Boolean removeTabelaPerfilPadrao(final TabelaPerfilPadrao entity);
    
    /**
     * Atualiza entidade.
     * @param entity entidade
     * @return true
     */
    @Transactional
    Boolean updateTabPerPadrao(final TabelaPerfilPadrao entity); 
    
        /**
     * Busca maior data de inicio por moeda.
     * 
     * @param moeda
     *            moeda.
     * @return TabelaPerfilPadrao
     */
    TabelaPerfilPadrao findMaxStartDataByCurrency(final Moeda moeda);
      
}
