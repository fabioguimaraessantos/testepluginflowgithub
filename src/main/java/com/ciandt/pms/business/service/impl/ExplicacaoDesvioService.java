package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IExplicacaoDesvioService;
import com.ciandt.pms.model.ExplicacaoDesvio;
import com.ciandt.pms.persistence.dao.IExplicacaoDesvioDao;


/**
 * Define o Service da entidade.
 * 
 * @since 19/04/2011
 * @author cmantovani
 * 
 */
@Service
public class ExplicacaoDesvioService implements IExplicacaoDesvioService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IExplicacaoDesvioDao explicacaoDesvioDao;

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public ExplicacaoDesvio findExplicacaoDesvioById(final Long entityId) {
        return explicacaoDesvioDao.findById(entityId);
    }

    /**
     * Busca uma lista de entidades.
     * 
     * @return lista de entidades.
     */
    public List<ExplicacaoDesvio> findExplicacaoDesvioAtivos() {

        return explicacaoDesvioDao.findAtivos();
    }

    /**
     * Busca a entidade selecionada.
     * 
     * @return uma ExplicacaoDesvio.
     */
    public ExplicacaoDesvio findExplicacaoDesvioSelecionado() {
        return explicacaoDesvioDao.findSelecionado();
    }

}