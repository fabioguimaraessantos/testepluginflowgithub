package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.Acelerador;


/**
 * 
 * A classe IAceleradorService proporciona a interface de
 * acesso a camada de serviço referente a entidade Acelerador.
 *
 * @since 06/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IAceleradorService {

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista de Acelerador.
     */
    List<Acelerador> findAceleradorAllActive();
    
    /**
     * Busca a entidade pelo id.
     * 
     * @param id - id da entidade
     * 
     * @return retorna uma instancia do tipo Acelerador
     */
    Acelerador findAceleradorById(final Long id);
}
