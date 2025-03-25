package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwEstratificacaoUrResultado;


/**
 * 
 * A classe IVwEstratificacaoUrResultadoService proporciona a interface de
 * acesso a camada de serviço referente a entidade VwEstratificacaoUrResultado.
 * 
 * @since 19/08/2011
 * @author cmantovani
 * 
 */
@Service
public interface IVwEstratificacaoUrResultadoService {

    /**
     * Busca as entidades por contratoPratica e data.
     * 
     * @param contratoPratica
     *            - contratoPratica para busca
     * @param dataMes
     *            - data para busca
     * @return lista de entidades.
     */
    List<VwEstratificacaoUrResultado> findEstratificacaoUrResultadoByContratoPraticaAndDataMes(
            final ContratoPratica contratoPratica, final Date dataMes);
}
