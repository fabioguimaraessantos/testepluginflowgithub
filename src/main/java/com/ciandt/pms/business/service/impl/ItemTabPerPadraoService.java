package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IItemTabPerPadraoService;
import com.ciandt.pms.model.ItemTabPerPadrao;
import com.ciandt.pms.model.TabelaPerfilPadrao;
import com.ciandt.pms.persistence.dao.IItemTabPerPadraoDao;


/**
 * 
 * A classe ItemTabPerPadraoService proporciona as funcionalidades de ... para ...
 *
 * @since 21/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public class ItemTabPerPadraoService implements IItemTabPerPadraoService {
    
    /** Inastancia do dao. */
    @Autowired
    private IItemTabPerPadraoDao dao;
    
    /**
     * Persiste um objeto no banco.
     * @param entity entidade a ser persistida
     * @return true
     */
    @Transactional 
    public Boolean createItemTabPerPadrao(final ItemTabPerPadrao entity) {
        dao.create(entity);
        return true;
    }
    
    /**
     * Executa um update na lista de entidade passado por parametro.
     * 
     * @param entityList
     *            - lista de entidades que serao atualizadas.
     * 
     */
    @Transactional
    public void updateItemTabPerPadraoList(final List<ItemTabPerPadrao> entityList) {
        for (ItemTabPerPadrao entity : entityList) {
            ItemTabPerPadrao item = dao.findById(entity.getCodigoItemTabPerPadrao());
            item.setValorPrecoPadrao(entity.getValorPrecoPadrao());
            dao.update(item);
            
        }
    }
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<ItemTabPerPadrao> findItemTabPerPadraoAll() {

        return dao.findAll();
    }
    
    /**
     * Retorna lista de entidades por tabelaPerfilpadrao.
     * @param tabela tabela
     * @return lista
     */
    public List<ItemTabPerPadrao> findItemTabPerPadraoByTabelaPerfilPadrao(final TabelaPerfilPadrao tabela) {
        return dao.findByTabelaPerfilPadrao(tabela);
    }
    
    /**
     * Remove ItemTabPerPadrao.
     * @param entity item.
     * @return true
     */
    @Transactional
    public Boolean removeItemTabPerPadrao(final ItemTabPerPadrao entity) {
        dao.remove(entity);
        return true;
    }
    
    /**
     * Atualiza entidade.
     * @param entity entidade
     * @return true
     */
    @Transactional
    public Boolean updateItemTabPerPadrao(final ItemTabPerPadrao entity) {
        dao.update(entity);
        return Boolean.valueOf(true);
    }
    
    /**
     * Busca por id.
     * @param id id.
     * @return ItemTabPerPadrao.
     */
    @Transactional
    public ItemTabPerPadrao findItemTabPerPadraoById(final Long id) {
        return dao.findById(id);
    }

}
