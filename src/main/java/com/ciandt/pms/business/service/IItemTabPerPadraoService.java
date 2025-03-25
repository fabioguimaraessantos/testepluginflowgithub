package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ItemTabPerPadrao;
import com.ciandt.pms.model.TabelaPerfilPadrao;


/**
 * 
 * A classe IItemTabPerPadraoService proporciona as funcionalidades de ... para ...
 *
 * @since 21/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public interface IItemTabPerPadraoService {

    /**
    * Persiste um objeto no banco.
    * @param entity entidade a ser persistida
    * @return true
    */
   Boolean createItemTabPerPadrao(final ItemTabPerPadrao entity);
   
   /**
    * Retorna todas as entidades.
    * 
    * @return retorna uma lista com todas as entidades.
    */
   List<ItemTabPerPadrao> findItemTabPerPadraoAll();
   
   /**
    * Retorna lista de entidades por tabelaperiflpadrao.
    * @param tabela tabela
    * @return lista
    */
   List<ItemTabPerPadrao> findItemTabPerPadraoByTabelaPerfilPadrao(final TabelaPerfilPadrao tabela);
   
   /**
    * Executa um update na lista de entidade passado por parametro.
    * 
    * @param entityList
    *            - lista de entidades que serao atualizadas.
    * 
    */
   void updateItemTabPerPadraoList(final List<ItemTabPerPadrao> entityList);
   
   /**
    * Remove ItemTabPerPadrao.
    * @param entity item.
    * @return true.
    */
   Boolean removeItemTabPerPadrao(final ItemTabPerPadrao entity);
   
   /**
    * Atualiza entidade.
    * @param entity entidade
    * @return true
    */
   Boolean updateItemTabPerPadrao(final ItemTabPerPadrao entity);
   
   /**
    * Busca por id.
    * @param id id
    * @return entidade.
    */
   ItemTabPerPadrao findItemTabPerPadraoById(Long id);
    
}
