package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ComposicaoTce;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IComposicaoTceDao;


/**
 * 
 * A classe ComposicaoTceDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade ComposicaoTce.
 * 
 * @since 07/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class ComposicaoTceDao extends AbstractDao<Long, ComposicaoTce>
        implements IComposicaoTceDao {
    
    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    public ComposicaoTceDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ComposicaoTce.class);

        entityManager = factory.createEntityManager();
    }
    
    /**
     * Remove as entidades pela data e tipo.
     * 
     * @param dataMes
     *            - data da vigencia.
     * @param indicadorTipo
     *            - tipo MN ou SY.
     *            
     * @return true se a remoção ocorrou corretamente. False caso contrário.
     */
    public Boolean removeByDateAndType(final Date dataMes,
            final String indicadorTipo) {
        Boolean isRemoveOk = Boolean.valueOf(false);

        EntityManager entityManager = null;
        try {
            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            
            Query query = entityManager
                    .createNamedQuery(ComposicaoTce.REMOVE_BY_DATE);


            query.setParameter("param1", dataMes);
            query.setParameter("param2", indicadorTipo);

            query.executeUpdate();
            
            transaction.commit();
            
            isRemoveOk = Boolean.valueOf(true);
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
        }
        
        return isRemoveOk;
    }

    /**
     * Busca pela Pessoa e pela Data.
     * 
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     * 
     * @return a entidade que atende aos criterios do filtro
     */
    @SuppressWarnings("unchecked")
    public ComposicaoTce findByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes) {

        List<ComposicaoTce> listResult = getJpaTemplate().findByNamedQuery(
                ComposicaoTce.FIND_BY_PESSOA_AND_DATE,
                new Object[] {pessoa.getCodigoPessoa(), dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
    
    /**
     * Busca o último registro ComposicaoTce da Pessoa (maior data).
     * 
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     * 
     * @return a entidade que atende aos criterios do filtro
     */
    @SuppressWarnings("unchecked")
    public ComposicaoTce findByPessoaAndMaxDate(final Pessoa pessoa,
            final Date dataMes) {

        List<ComposicaoTce> listResult = getJpaTemplate().findByNamedQuery(
                ComposicaoTce.FIND_BY_PESSOA_AND_MAX_DATE,
                new Object[] {pessoa.getCodigoPessoa(), dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<ComposicaoTce> findByFilter(final ComposicaoTce filter) {

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        Date dataMes = filter.getDataMes();
        String indicadorTipo = filter.getIndicadorTipo();
        if (Constants.ALL.equals(indicadorTipo)) {
            indicadorTipo = "";
        }

        List<ComposicaoTce> listResult = getJpaTemplate().findByNamedQuery(
                ComposicaoTce.FIND_BY_FILTER,
                new Object[] {codigoLogin, dataMes, indicadorTipo });

        return listResult;
    }
    
    /**
     * Busca uma lista de entidades pelo filtro e com algum valor nulo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<ComposicaoTce> findByFilterMissBlank(final ComposicaoTce filter) {

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        Date dataMes = filter.getDataMes();
        String indicadorTipo = filter.getIndicadorTipo();
        if (Constants.ALL.equals(indicadorTipo)) {
            indicadorTipo = "";
        }

        List<ComposicaoTce> listResult = getJpaTemplate().findByNamedQuery(
                ComposicaoTce.FIND_BY_FILTER_MISS_BLANK,
                new Object[] {codigoLogin, dataMes, indicadorTipo });

        return listResult;
    }
    
}