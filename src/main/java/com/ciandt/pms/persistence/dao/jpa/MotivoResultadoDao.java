package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.MotivoResultado;
import com.ciandt.pms.persistence.dao.IMotivoResultadoDao;


/**
 * 
 * A classe MotivoResultadoDao proporciona as funcionalidades de acesso ao banco
 * de dados para a entidade {@link MotivoResultado}.
 * 
 * @since 12/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Repository
public class MotivoResultadoDao extends AbstractDao<Long, MotivoResultado>
        implements IMotivoResultadoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public MotivoResultadoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MotivoResultado.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<MotivoResultado> findAll() {
        return this.getJpaTemplate().findByNamedQuery(MotivoResultado.FIND_ALL);
    }

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<MotivoResultado> findAllAtivos() {
        return this.getJpaTemplate().findByNamedQuery(
                MotivoResultado.FIND_ALL_ATIVOS);
    }

}