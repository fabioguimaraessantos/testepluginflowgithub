package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwPmsReceitaResultado;


/**
 * 
 * A interface IVwPmsReceitaResultadoDao proporciona as funcionalidades de
 * acesso ao banco para a entidade VwPmsReceitaResultado.
 * 
 * @since 19/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Repository
public interface IVwPmsReceitaResultadoDao extends
        IAbstractDao<Long, VwPmsReceitaResultado> {

    /**
     * Busca as entidades filtradas de acordo com os parametros.
     * 
     * @param dataMes
     *            - data base para busca
     * 
     * @return retorna uma lista de VwPmsReceitaResultado
     */
    List<VwPmsReceitaResultado> findByDataMes(final Date dataMes);

}