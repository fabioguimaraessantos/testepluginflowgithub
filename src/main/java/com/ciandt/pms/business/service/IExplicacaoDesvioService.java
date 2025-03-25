package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ExplicacaoDesvio;


/**
 * Define a interface do Service da entidade.
 * 
 * @since 19/04/2011
 * @author cmantovani
 * 
 */
@Service
public interface IExplicacaoDesvioService {

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    ExplicacaoDesvio findExplicacaoDesvioById(final Long entityId);

    /**
     * Busca uma lista de entidades.
     * 
     * @return lista de entidades.
     */
    List<ExplicacaoDesvio> findExplicacaoDesvioAtivos();

    /**
     * Busca a entidade selecionada.
     * 
     * @return uma ExplicacaoDesvio.
     */
    ExplicacaoDesvio findExplicacaoDesvioSelecionado();

}