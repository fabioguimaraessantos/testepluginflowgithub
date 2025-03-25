package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IAreaService;
import com.ciandt.pms.model.Area;
import com.ciandt.pms.persistence.dao.IAreaDao;


/**
 * 
 * A classe AreaService proporciona as funcionalidades da
 * camada de negócio referente a entidade Area.
 *
 * @since 13/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class AreaService implements IAreaService {

    /** Instancia do Dao da entidade Area. */
    @Autowired
    private IAreaDao areaDao;
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */ 
    public void createArea(final Area entity) {
        areaDao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que sera atualizada.
     * 
     */
    public void updateArea(final Area entity) {
        areaDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity que sera apagada.
     */
    public void removeArea(final Area entity) {
        areaDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public Area findAreaById(final Long id) {
        return areaDao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<Area> findAreaAllActive() {
        return areaDao.findAllActive();
    }

}
