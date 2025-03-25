package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.TipoContrato;
import com.ciandt.pms.persistence.dao.ITipoContratoDao;


/**
 * 
 * A classe TipoContratoDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade TipoContrato.
 * 
 * @since 26/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class TipoContratoDao extends AbstractDao<Long, TipoContrato> implements
        ITipoContratoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public TipoContratoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TipoContrato.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TipoContrato> findAll() {
        List<TipoContrato> listResult = getJpaTemplate().findByNamedQuery(
                TipoContrato.FIND_ALL);

        return listResult;
    }

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    @SuppressWarnings("unchecked")
    public List<TipoContrato> findAllActive() {
        List<TipoContrato> listResult = getJpaTemplate().findByNamedQuery(
                TipoContrato.FIND_ALL_ACTIVE);

        return listResult;
    }

}