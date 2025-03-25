package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwEstratificacaoUrResultado;
import com.ciandt.pms.model.VwEstratificacaoUrResultadoId;


/**
 * 
 * A classe IVwEstratificacaoUrResultadoDao proporciona a interface de acesso
 * para a camada de persistencia referente a entidade
 * VwEstratificacaoUrResultado.
 * 
 * @since 03/03/2010
 * @author cmantovani
 * 
 */
public interface IVwEstratificacaoUrResultadoDao
        extends
        IAbstractDao<VwEstratificacaoUrResultadoId, VwEstratificacaoUrResultado> {

    /**
     * Busca as entidades por contratoPratica e data.
     * 
     * @param contratoPratica
     *            - contratoPratica para busca
     * @param dataMes
     *            - data para busca
     * @return lista de entidades.
     */
    List<VwEstratificacaoUrResultado> findByContratoPraticaAndDataMes(
            final ContratoPratica contratoPratica, final Date dataMes);
}
