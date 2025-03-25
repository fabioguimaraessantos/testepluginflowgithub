package com.ciandt.pms.persistence.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IRateioLicencaSwDao;
import com.ciandt.pms.util.DateUtil;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.Constants;
import com.ciandt.pms.persistence.dao.IChargebackPessDao;


/**
 * 
 * A classe ChargebackPessDao proporciona as funcionalidades de persistencia
 * referente a entidade ChargebackPessoa.
 * 
 * @since 01/07/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class ChargebackPessDao extends AbstractDao<Long, ChargebackPessoa>
        implements IChargebackPessDao {

    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    @Autowired
    private IRateioLicencaSwDao rateioLicencaSwDao;

    /**
     * Construtor Padrão.
     * 
     * @param factory
     *            - Instancia da fabrica da entidade.
     */
    @Autowired
    public ChargebackPessDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ChargebackPessoa.class);

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

            Query query =
                    entityManager
                            .createNamedQuery(ChargebackPessoa.REMOVE_BY_DATE);

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
     * Realiza uma busca por ChargebackPessoa referentes a um TiRecurso em um
     * determinado periodo, passados por parametro.
     * 
     * @param tiRecurso
     *            do tipo TiRecurso.
     * @param startDate
     *            data inicio do periodo.
     * @param endDate
     *            data fim do periodo.
     * 
     * @return lista de ChargebackPessoa que atendem ao criterio de filtro
     */
    @SuppressWarnings("unchecked")
    public List<ChargebackPessoa> findByTiRecursoAndPeriod(
            final TiRecurso tiRecurso, final Date startDate, final Date endDate) {

        List<ChargebackPessoa> listResult =
                getJpaTemplate().findByNamedQuery(
                        ChargebackPessoa.FIND_BY_TIRECURSO_AND_PERIOD,
                        new Object[]{tiRecurso.getCodigoTiRecurso(), startDate,
                                endDate});

        return listResult;
    }

    /**
     * Realiza uma busca por ChargebackPessoa referentes a uma Pessoa em um
     * determinado periodo, passados por parametro.
     * 
     * @param pessoa
     *            do tipo Pessoa.
     * @param startDate
     *            data inicio do periodo.
     * @param endDate
     *            data fim do periodo.
     * 
     * @return lista de ChargebackPessoa que atendem ao criterio de filtro
     */
    @SuppressWarnings("unchecked")
    public List<ChargebackPessoa> findByPessoaAndPeriod(final Pessoa pessoa,
            final Date startDate, final Date endDate) {

        List<ChargebackPessoa> listResult =
                getJpaTemplate().findByNamedQuery(
                        ChargebackPessoa.FIND_BY_PESSOA_AND_PERIOD,
                        new Object[]{pessoa.getCodigoPessoa(), startDate,
                                endDate});

        return listResult;
    }

    /**
     * Realiza uma busca por ChargebackPessoa por Pessoa, TiRecurso e Data.
     * Estes tres compoem uma cahva unica.
     * 
     * @param tiRecurso
     *            do tipo TiRecurso.
     * 
     * @param pessoa
     *            do tipo Pessoa.
     * 
     * @param monthDate
     *            data mes.
     * 
     * @return lista de ChargebackPessoa que atendem ao criterio de filtro
     */
    @SuppressWarnings("unchecked")
    public ChargebackPessoa findByUniqueKey(final TiRecurso tiRecurso,
            final Pessoa pessoa, final Date monthDate) {

        if (tiRecurso == null || tiRecurso.getCodigoTiRecurso() == null) {
            return null;
        }

        List<ChargebackPessoa> listResult =
                getJpaTemplate().findByNamedQuery(
                        ChargebackPessoa.FIND_BY_UNIQUE_KEY,
                        new Object[]{pessoa.getCodigoPessoa(),
                                tiRecurso.getCodigoTiRecurso(), monthDate});

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
     * @param multCodTiRecMap
     *            multiplos codigo de TiRecurso.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<ChargebackPessoa> findByFilter(final ChargebackPessoa filter,
            final Map<Long, String> multCodTiRecMap) {

        String codigoLogin = "";
        Pessoa pessoa = filter.getPessoa();
        if (pessoa != null) {
            codigoLogin = pessoa.getCodigoLogin();
        }

        Long codigoTiRecurso = null;
        TiRecurso tiRecurso = filter.getTiRecurso();
        if (tiRecurso != null && tiRecurso.getCodigoTiRecurso() != null) {
            codigoTiRecurso = tiRecurso.getCodigoTiRecurso();
            multCodTiRecMap.put(codigoTiRecurso, null);
        }

        Date dataMes = filter.getDataMes();
        String indicadorTipo = filter.getIndicadorTipo();
        if (Constants.ALL.equals(indicadorTipo)) {
            indicadorTipo = "";
        }

        List<ChargebackPessoa> listResultAux =
                getJpaTemplate().findByNamedQuery(
                        ChargebackPessoa.FIND_BY_FILTER,
                        new Object[]{codigoLogin, dataMes, indicadorTipo});

        List<ChargebackPessoa> listResult = new ArrayList<ChargebackPessoa>();

        if (!listResultAux.isEmpty() && !multCodTiRecMap.isEmpty()) {
            for (ChargebackPessoa chbackPess : listResultAux) {
                TiRecurso tiRec = chbackPess.getTiRecurso();
                if (tiRec != null) {
                    if (multCodTiRecMap.get(tiRec.getCodigoTiRecurso()) != null) {

                        List<RateioLicencaSw> rateioLicencaSwList = rateioLicencaSwDao.findByRecursoTIAndMonth(DateUtil.getDateFirstDayOfMonth(new Date()), tiRec.getCodigoTiRecurso());

                        for (RateioLicencaSw rateioLicencaSw : rateioLicencaSwList){
                            if(chbackPess.getPessoa().getCodigoLogin().equals(rateioLicencaSw.getLogin()) && rateioLicencaSw.getStatus().equals(Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL)){
                                chbackPess.setIsExcludable(Boolean.TRUE);
                                break;
                            }else {
                                chbackPess.setIsExcludable(Boolean.FALSE);
                            }
                        }

                        listResult.add(chbackPess);
                    }
                }
            }
        } else {
            return listResultAux;
        }

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
    public List<ChargebackPessoa> findByFilterMissBlank(
            final ChargebackPessoa filter) {

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        Date dataMes = filter.getDataMes();
        String indicadorTipo = filter.getIndicadorTipo();
        if (Constants.ALL.equals(indicadorTipo)) {
            indicadorTipo = "";
        }

        List<ChargebackPessoa> listResult =
                getJpaTemplate().findByNamedQuery(
                        ChargebackPessoa.FIND_BY_FILTER_MISS_BLANK,
                        new Object[]{codigoLogin, dataMes, indicadorTipo});

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro de mes.
     * 
     * @param dataMes
     *            - mes de filtro
     * @return lista de entidades
     */
    @SuppressWarnings("unchecked")
    public List<Long> findByMonth(final Date dataMes) {

        List<Long> codesChargebackPessoaList =
                getJpaTemplate().findByNamedQuery(
                        ChargebackPessoa.FIND_BY_MONTH, new Object[]{dataMes});

        return codesChargebackPessoaList;
    }

    @Override
    public ChargebackPessoa findByPessoaAndTiRecursoAndEndDateBefore(final Pessoa pessoa, final TiRecurso tiRecurso, final Date dataMes) {
        if (tiRecurso == null || tiRecurso.getCodigoTiRecurso() == null) {
            return null;
        }

        List<ChargebackPessoa> listResult =
                getJpaTemplate().findByNamedQuery(
                        ChargebackPessoa.FIND_BY_PESSOA_TIRECURSO_ENDDATE,
                        new Object[]{pessoa.getCodigoPessoa(),
                                tiRecurso.getCodigoTiRecurso(), dataMes});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
}