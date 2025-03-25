package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.VwChbackSoftware;
import com.ciandt.pms.model.VwChbackSoftwareId;


/**
 * 
 * A classe IVwChbackSoftwareDao proporciona a interface de acesso para a camada
 * de persistencia referente a entidade VwChbackSoftware.
 * 
 * @since 13/07/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IVwChbackSoftwareDao extends
        IAbstractDao<VwChbackSoftwareId, VwChbackSoftware> {

    /**
     * Retorna todas as entidades pela data mês.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return retorna uma lista com todas as entidades do mês.
     */
    List<VwChbackSoftware> findAllByDate(final Date dataMes);

}