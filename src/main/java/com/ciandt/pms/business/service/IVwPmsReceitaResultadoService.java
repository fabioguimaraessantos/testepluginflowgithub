package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.VwPmsReceitaResultado;


/**
 * 
 * A interface IVwPmsReceitaResultadoService proporciona as funcionalidades de
 * serviço para a entidade VwPmsReceitaResultado.
 * 
 * @since 19/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public interface IVwPmsReceitaResultadoService {

    /**
     * Busca as entidades filtradas de acordo com os parametros.
     * 
     * @param dataMes
     *            - data base para busca
     * 
     * @return retorna uma lista de VwPmsReceitaResultado
     */
    List<VwPmsReceitaResultado> findVwPmsReceitaResultadoByDataMes(
            final Date dataMes);

}