package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.model.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IPerfilSistemaService;
import com.ciandt.pms.model.PerfilSistema;
import com.ciandt.pms.persistence.dao.IPerfilSistemaDao;


/**
 * 
 * A classe PerfilSistemaService proporciona as funcionalidades da camada 
 * de serviço referente a entidade PefilSistema.
 *
 * @since 04/01/2011
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class PerfilSistemaService implements IPerfilSistemaService {

    /** Instancia do DAO PerfilSistemaDao. */
    @Autowired
    private IPerfilSistemaDao dao;

    @Autowired
    private IPessoaService pessoaService;
    
    /**
     * Retorna todas os perfis do sistema.
     * 
     * @return retorna uma lista de PerfilSistema.
     */
    public List<PerfilSistema> findPerfilSistemaAll() {
        return dao.findAll();
    }
    
    /**
     * Busca um perfil pelo id.
     * 
     * @param id identificador da entidade
     * 
     * @return retorna um PerfilSistema referente ao id passado por parametro,
     * caso não exista retorna null.
     */
    public PerfilSistema findPefilSistemaById(final Long id) {
        return dao.findById(id);
    }

    public Boolean verifySystemAcess(String login, List<Long> systemPermissionToVerify) {
        Pessoa pessoa = pessoaService.findPessoaByLogin(login);
        List<PerfilSistema> listPerfilSistema = new ArrayList<PerfilSistema>(pessoa.getPerfilSistemas());
        boolean hasPermission = false;
        for (PerfilSistema perfilSistema : listPerfilSistema) {
            if (systemPermissionToVerify.contains(perfilSistema.getCodigoPerfilSistema())) {
                hasPermission = true;
                break;
            }
        }
        return Boolean.valueOf(hasPermission);
    }
}
