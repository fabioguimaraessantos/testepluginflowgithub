package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.AlocacaoFoto;
import com.ciandt.pms.persistence.dao.IAlocacaoFotoDao;


/**
 * 
 * A classe AlocacaoFotoDao proporciona as funcionalidades de acesso ao banco
 * para referentes a entidade AlocacaoFoto.
 * 
 * @since 17/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class AlocacaoFotoDao extends AbstractDao<Long, AlocacaoFoto> implements
        IAlocacaoFotoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public AlocacaoFotoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, AlocacaoFoto.class);
    }

}