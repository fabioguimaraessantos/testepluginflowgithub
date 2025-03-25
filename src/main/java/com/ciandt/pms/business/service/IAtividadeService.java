package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.Atividade;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 17/08/2010
 */
@Service
public interface IAtividadeService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    Atividade findAtividadeById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Atividade> findAtividadeAll();

    /**
     * Retorna todas as entidades - ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Atividade> findAtividadeAllActive();
    
    /**
     * Retorna a Atividade referente a sigla.
     * 
     * @param sigla da atividade
     * 
     * @return retorna uma entidade do tipo Atividade 
     */
    Atividade findAtividadeBySigla(final String sigla);

}