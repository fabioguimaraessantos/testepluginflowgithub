package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwPmsReceitaResultado;
import com.ciandt.pms.persistence.dao.IVwPmsReceitaResultadoDao;


/**
 * 
 * A classe IVwPmsReceitaResultadoDao proporciona as funcionalidades de acesso
 * ao banco para a entidade VwPmsReceitaResultado.
 * 
 * @since 19/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Repository
public class VwPmsReceitaResultadoDao extends
        AbstractDao<Long, VwPmsReceitaResultado> implements
        IVwPmsReceitaResultadoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public VwPmsReceitaResultadoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, VwPmsReceitaResultado.class);
    }

    /**
     * Busca as entidades filtradas de acordo com os parametros.
     * 
     * @param dataMes
     *            - data base para busca
     * 
     * @return retorna uma lista de VwPmsReceitaResultado
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<VwPmsReceitaResultado> findByDataMes(final Date dataMes) {
        List<VwPmsReceitaResultado> result =
                getJpaTemplate().findByNamedQuery(
                        VwPmsReceitaResultado.FIND_BY_DATA_MES,
                        new Object[]{dataMes});

        if (result != null) {
            return result;
        }
        return null;
    }

}