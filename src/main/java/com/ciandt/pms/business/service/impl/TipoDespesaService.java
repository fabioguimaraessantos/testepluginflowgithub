package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ITipoDespesaService;
import com.ciandt.pms.model.TipoDespesa;
import com.ciandt.pms.persistence.dao.ITipoDespesaDao;


/**
 * 
 * A classe TipoDespesaService proporciona as funcionalidades da camada de
 * serviço referente a entidade TipoDespesa.
 * 
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class TipoDespesaService implements ITipoDespesaService {

    /** Instancia do dao TipoDespesaDao. */
    @Autowired
    private ITipoDespesaDao dao;

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    public List<TipoDespesa> findTipoDespesaAllActive() {
        return dao.findAllActive();
    }

    /**
     * Busca por Nome.
     * 
     * @param nomeTipoDespesa
     *            nome do TipoDespesa
     * 
     * @return retorna um tipoDespesa.
     */
    public TipoDespesa findTipoDespesaByName(final String nomeTipoDespesa) {
        return dao.findByName(nomeTipoDespesa);
    }

    /**
     * Busca por Id.
     * 
     * @param id
     *            id do TipoDespesa
     * 
     * @return retorna um tipoDespesa.
     */
    public TipoDespesa findTipoDespesaById(final Long id) {
        return dao.findById(id);
    }

}
