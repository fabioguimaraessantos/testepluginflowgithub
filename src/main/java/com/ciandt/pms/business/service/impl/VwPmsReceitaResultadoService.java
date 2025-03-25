package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwPmsReceitaResultadoService;
import com.ciandt.pms.model.VwPmsReceitaResultado;
import com.ciandt.pms.persistence.dao.IVwPmsReceitaResultadoDao;


/**
 * 
 * A classe IVwPmsReceitaResultadoService proporciona as funcionalidades de
 * serviço para a entidade VwPmsReceitaResultado.
 * 
 * @since 19/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public class VwPmsReceitaResultadoService implements
        IVwPmsReceitaResultadoService {

    /** Instancia do dao . */
    @Autowired
    private IVwPmsReceitaResultadoDao dao;

    /**
     * Busca as entidades filtradas de acordo com os parametros.
     * 
     * @param dataMes
     *            - data base para busca
     * 
     * @return retorna uma lista de VwPmsReceitaResultado
     */
    @Override
    public List<VwPmsReceitaResultado> findVwPmsReceitaResultadoByDataMes(
            final Date dataMes) {
        return dao.findByDataMes(dataMes);
    }

}