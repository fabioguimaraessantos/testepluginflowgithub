package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.ITipoModeloNegocioService;
import com.ciandt.pms.model.TipoModeloNegocio;
import com.ciandt.pms.persistence.dao.ITipoModeloNegocioDao;
import com.ciandt.pms.persistence.dao.ITipoPraticaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by josef on 02/02/2017.
 */
@Service
public class TipoModeloNegocioService implements ITipoModeloNegocioService{

    /**
     * TipoPratica Dao.
     */
    @Autowired
    private ITipoModeloNegocioDao tipoModeloNegocioDao;

    /**
     * Find All TipoPratica
     * @return
     */
    public List<TipoModeloNegocio> findAllActive() {
        return tipoModeloNegocioDao.findAllActive();
    }

    /**
     * Busca uma entidade pelo id.
     *
     * @param id
     *            da entidade.
     *
     * @return entidade com o id passado por parametro.
     */
    public TipoModeloNegocio findTipoModeloNegocioById(final Long id) {
        return tipoModeloNegocioDao.findById(id);
    }
}
