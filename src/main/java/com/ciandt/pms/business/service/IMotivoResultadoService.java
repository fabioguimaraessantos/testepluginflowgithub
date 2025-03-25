package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.MotivoResultado;


/**
 * 
 * A classe IMotivoResultadoService proporciona a interface de acesso para a
 * camada de serviço referente a entidade {@link MotivoResultado}.
 * 
 * @since 12/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public interface IMotivoResultadoService {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<MotivoResultado> findMotivoResultadoAll();

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    List<MotivoResultado> findMotivoResultadoAllAtivos();

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    MotivoResultado findMotivoResultadoById(final Long id);

}