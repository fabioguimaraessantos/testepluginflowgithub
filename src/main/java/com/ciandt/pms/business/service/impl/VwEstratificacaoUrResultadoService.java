package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwEstratificacaoUrResultadoService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwEstratificacaoUrResultado;
import com.ciandt.pms.persistence.dao.IVwEstratificacaoUrResultadoDao;


/**
 * 
 * A classe VwEstratificacaoUrResultadoService proporciona as funcionalidades da
 * camada de serviço referente a entidade VwEstratificacaoUrResultado.
 * 
 * @since 19/08/2011
 * @author cmantovani
 * 
 */
@Service
public class VwEstratificacaoUrResultadoService implements
        IVwEstratificacaoUrResultadoService {

    /** Instancia DAO da entidade VwEstratificacaoUrResultado. */
    @Autowired
    private IVwEstratificacaoUrResultadoDao dao;

    /**
     * Busca as entidades por contratoPratica e data.
     * 
     * @param contratoPratica
     *            - contratoPratica para busca
     * @param dataMes
     *            - data para busca
     * @return lista de entidades.
     */
    public List<VwEstratificacaoUrResultado> findEstratificacaoUrResultadoByContratoPraticaAndDataMes(
            final ContratoPratica contratoPratica, final Date dataMes) {
        return dao.findByContratoPraticaAndDataMes(contratoPratica, dataMes);
    }

}
