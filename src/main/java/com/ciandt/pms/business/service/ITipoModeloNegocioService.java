package com.ciandt.pms.business.service;

import com.ciandt.pms.model.TipoModeloNegocio;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by josef on 02/02/2017.
 */
@Service
public interface ITipoModeloNegocioService {

    /**
     * Find All TipoPratica
     *
     * @return
     */
    List<TipoModeloNegocio> findAllActive();

    /**
     * Busca uma entidade pelo id.
     *
     * @param id
     *            da entidade.
     *
     * @return entidade com o id passado por parametro.
     */
    TipoModeloNegocio findTipoModeloNegocioById(final Long id);

}
