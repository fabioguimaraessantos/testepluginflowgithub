package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IAtividadeService;
import com.ciandt.pms.model.Atividade;
import com.ciandt.pms.persistence.dao.IAtividadeDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 17/08/2010
 */
@Service
public class AtividadeService implements IAtividadeService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IAtividadeDao dao;

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public Atividade findAtividadeById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<Atividade> findAtividadeAll() {
        return dao.findAll();
    }

    /**
     * Retorna todas as entidades - ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<Atividade> findAtividadeAllActive() {
        return dao.findAllActive();
    }
    
    /**
     * Retorna a Atividade referente a sigla.
     * 
     * @param sigla da atividade
     * 
     * @return retorna uma entidade do tipo Atividade 
     */
    public Atividade findAtividadeBySigla(final String sigla) {
        return dao.findBySigla(sigla);
    }

}