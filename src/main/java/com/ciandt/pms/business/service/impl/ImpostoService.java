package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IImpostoService;
import com.ciandt.pms.model.Imposto;
import com.ciandt.pms.persistence.dao.IImpostoDao;


/**
 * 
 * A classe ImpostoService proporciona as funcionalidades da camada de
 * serviço referentes a entidade Imposto.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Service
public class ImpostoService implements IImpostoService {

    /**
     * Cria uma entidade.
     * 
     * @param entity
     *            que sera criada.
     * 
     * @return true se for inserida com sucesso.
     */
    public Boolean createImposto(final Imposto entity) {
        impostoDao.create(entity);
        return Boolean.TRUE;
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    public void removeImposto(final Imposto entity) {
        impostoDao.remove(entity);
    }

    /** Instancia do DAO da entidade. */
    @Autowired
    private IImpostoDao impostoDao;

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<Imposto> findImpostoAll() {

        return impostoDao.findAll();
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public Imposto findImpostoById(final Long id) {

        return impostoDao.findById(id);
    }
}
