package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.PerfilSistema;


/**
 * 
 * A classe IPerfilSistemaService proporciona a interface de acesso
 * para a camada de serviço referente a entidade PerfilSistema.
 *
 * @since 04/01/2011
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IPerfilSistemaService {

    /**
     * Retorna todas os perfis do sistema.
     * 
     * @return retorna uma lista de PerfilSistema.
     */
    List<PerfilSistema> findPerfilSistemaAll();
    
    /**
     * Busca um perfil pelo id.
     * 
     * @param id identificador da entidade
     * 
     * @return retorna um PerfilSistema referente ao id passado por parametro,
     * caso não exista retorna null.
     */
    PerfilSistema findPefilSistemaById(final Long id);

    Boolean verifySystemAcess(String login, List<Long> systemPermissionToVerify);
}
