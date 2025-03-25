package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwChbackSoftwareService;
import com.ciandt.pms.model.VwChbackSoftware;
import com.ciandt.pms.persistence.dao.IVwChbackSoftwareDao;


/**
 * 
 * A classe VwChbackSoftwareService proporciona as funcionalidades da camada de
 * serviço referente a entidade VwChbackSoftware.
 * 
 * @since 13/07/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class VwChbackSoftwareService implements IVwChbackSoftwareService {

    /** Instancia DAO da entidade VwChbackSoftware. */
    @Autowired
    private IVwChbackSoftwareDao chbackSoftwareDao;

    /**
     * Retorna todas as entidades pela data mês.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return retorna uma lista com todas as entidades do mês.
     */
    public List<VwChbackSoftware> findAllByDate(final Date dataMes) {
        return chbackSoftwareDao.findAllByDate(dataMes);
    }

}