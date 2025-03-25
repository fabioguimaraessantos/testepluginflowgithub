package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwChbackSoftware;
import com.ciandt.pms.model.VwChbackSoftwareId;
import com.ciandt.pms.persistence.dao.IVwChbackSoftwareDao;


/**
 * 
 * A classe VwChbackSoftwareDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade VwChbackSoftware.
 * 
 * @since 13/07/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class VwChbackSoftwareDao extends
        AbstractDao<VwChbackSoftwareId, VwChbackSoftware> implements
        IVwChbackSoftwareDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public VwChbackSoftwareDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, VwChbackSoftware.class);

    }

    /**
     * Retorna todas as entidades pela data mês.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return retorna uma lista com todas as entidades do mês.
     */
    @SuppressWarnings("unchecked")
    public List<VwChbackSoftware> findAllByDate(final Date dataMes) {
        List<VwChbackSoftware> listResult = getJpaTemplate().findByNamedQuery(
                VwChbackSoftware.FIND_ALL_BY_DATE, new Object[] {dataMes });

        return listResult;
    }

}