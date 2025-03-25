package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwCompTceFuncionario;
import com.ciandt.pms.model.VwCompTceFuncionarioId;
import com.ciandt.pms.persistence.dao.IVwCompTceFuncionarioDao;


/**
 * 
 * A classe VwCompTceFuncionarioDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade VwCompTceFuncionario.
 * 
 * @since 10/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class VwCompTceFuncionarioDao extends
        AbstractDao<VwCompTceFuncionarioId, VwCompTceFuncionario> implements
        IVwCompTceFuncionarioDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public VwCompTceFuncionarioDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, VwCompTceFuncionario.class);

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
    public List<VwCompTceFuncionario> findAllByDate(final Date dataMes) {
        List<VwCompTceFuncionario> listResult = getJpaTemplate()
                .findByNamedQuery(VwCompTceFuncionario.FIND_ALL_BY_DATE,
                        new Object[] {dataMes });

        return listResult;
    }

}