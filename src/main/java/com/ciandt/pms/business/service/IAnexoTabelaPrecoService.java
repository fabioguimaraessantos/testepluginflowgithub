package com.ciandt.pms.business.service;

import java.io.IOException;
import java.util.List;

import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.AnexoTabelaPreco;
import com.ciandt.pms.model.TabelaPreco;


/**
 * 
 * A classe IAnexoTabelaPrecoService proporciona as funcionalidades de interface
 * para AnexoTabelaPrecoService.
 * 
 * @since 27/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface IAnexoTabelaPrecoService {

    /**
     * Cria entidade no banco.
     * @param entity entidade
     * @return true
     */
    @Transactional
    Boolean createAnexoTabelaPreco(final AnexoTabelaPreco entity);
    
    /**
     * Busca pro entidades de anexoTabelaPreco por tabelaPreco.
     * 
     * @param tabelaPreco
     *            tabela.
     * @return Lista com entidades de anexo tabela preco.
     */
    List<AnexoTabelaPreco> findAnexoTabelaPrecoByTabelaPreco(final TabelaPreco tabelaPreco);
    
    /**
    * Remove entidade do banco.
    * @param anexo anexo
    * @return true
    */
   Boolean removeAnexoTabelaPreco(final AnexoTabelaPreco anexo);
   
   /**
    * Busca por id.
    * @param id id
    * @return entidade
    */
   @Transactional
   AnexoTabelaPreco findAnexoTabelaPrecoById(final Long id);
   
   @Transactional
   AnexoTabelaPreco uploadTabelaPreco(final UploadItem uploadItem,
           final TabelaPreco tabelaPreco, final String coment) throws IOException;
   
   void saveUploadAnexo(final AnexoTabelaPreco anexoTabelaPreco, final byte[] fileBytes) throws IOException;
    
}
