package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcamentoDespesa;


/**
 * 
 * A classe IOrcDespWhiteListService proporciona as funcionalidades de interface
 * para OrcDespWhiteListService.
 * 
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface IOrcDespWhiteListService {

    /**
     * Adiciona Pessoa na lista branca (OrcDespWhiteList).
     * 
     * @param entity
     *            OrcDespWhiteList
     * @return true
     */
    Boolean addPersonOrcDespWhiteList(final OrcDespWhiteList entity);

    /**
     * Deleta pessoa da lista WhitList.
     * 
     * @param entity
     *            pessoa.
     * @return true.
     */
    Boolean removePersonOrcDespWhiteLIst(final OrcDespWhiteList entity);

    /**
     * Busca por id.
     * 
     * @param id
     *            id
     * @return OrcDespWhiteList.
     */
    OrcDespWhiteList findById(final Long id);

    /**
     * Busca por OrcamentoDespesa.
     * 
     * @param orcamentoDespesa
     *            orcamentoDespesa
     * @return lista.
     */
    List<OrcDespWhiteList> findByOrcamentoDespesa(
            final OrcamentoDespesa orcamentoDespesa);
    
    /**
     * Busca por OrcamentoDespesa e nome.
     * @param orcDesp orcamentoDespesa
     * @param name nome
     * @return lista.
     */
    List<OrcDespWhiteList> findByOrcDespAndName(final OrcamentoDespesa orcDesp, final String name);

}
