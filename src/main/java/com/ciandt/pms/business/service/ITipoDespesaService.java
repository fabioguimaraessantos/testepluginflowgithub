package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.TipoDespesa;


/**
 * 
 * A classe ITipoDespesaService proporciona a interface de acesso a camada de
 * serviço referente a entidade TipoDespesa.
 * 
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface ITipoDespesaService {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    List<TipoDespesa> findTipoDespesaAllActive();

    /**
     * Busca por Nome.
     * 
     * @param nomeTipoDespesa
     *            nome do TipoDespesa
     * 
     * @return retorna um tipoDespesa.
     */
    TipoDespesa findTipoDespesaByName(String nomeTipoDespesa);

    /**
     * Busca por Id.
     * 
     * @param id
     *            id do TipoDespesa
     * 
     * @return retorna um tipoDespesa.
     */
    TipoDespesa findTipoDespesaById(final Long id);
}
