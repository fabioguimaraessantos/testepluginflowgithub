package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.model.vo.LicencaSwProjetoCell;
import com.ciandt.pms.persistence.dao.ILicencaSwProjetoParcelaDao;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class LicencaSwProjetoParcelaDao extends AbstractDao<Long, LicencaSwProjetoParcela> implements ILicencaSwProjetoParcelaDao {
    @Autowired
    public LicencaSwProjetoParcelaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, LicencaSwProjetoParcela.class);
    }

    public List<LicencaSwProjetoParcela> findByFilter(final Date monthDate, final Long codTiRecurso, final String status, Long invoiceNumber, Long project, Long licenseId) {

        Long codTiRecursoNull = 0L;
        if (codTiRecurso != null) {
            codTiRecursoNull = codTiRecurso;
        }
        Long invoiceNumberNull = 0L;
        if (invoiceNumber != null) {
            invoiceNumberNull = invoiceNumber;
        }
        Long projectNull = 0L;
        if (project != null) {
            projectNull = project;
        }
        Long licenseIdNull = 0L;
        if (licenseId != null) {
            licenseIdNull = licenseId;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("monthDate", monthDate);
        params.put("codTiRecurso", codTiRecursoNull);
        params.put("status", status);
        params.put("invoiceNumber", invoiceNumberNull);
        params.put("project", projectNull);
        params.put("licenseId", licenseIdNull);

        List<LicencaSwProjetoParcela> results = getJpaTemplate().findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_BY_FILTER, params);

        return results;
    }

    public List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonth(final Long codigoLicencaSwProjeto, final Date month) {
        List<LicencaSwProjetoParcela> listResult = getJpaTemplate().findByNamedQuery(LicencaSwProjetoParcela.FIND_BY_LICENCA_SW_PROJETO_AND_MONTH,
                                                    new Object[] {month, codigoLicencaSwProjeto});

        return listResult;
    }

    public BigDecimal findBalanceByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoLicencaSwProjeto", codigoLicencaSwProjeto);

        BigDecimal result = (BigDecimal)getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_BALANCE_BY_LICENCA_SW_PROJETO_AND_MONTH, params).get(0);
        return result == null ? new BigDecimal(0) : result;
    }

    public BigDecimal findBalanceToAppropriateByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto, Date month){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoLicencaSwProjeto", codigoLicencaSwProjeto);
        params.put("month", month);

        BigDecimal result = (BigDecimal)getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_BALANCE_TO_APPROPRIATE_BY_LICENCA_SW_PROJETO_AND_MONTH, params).get(0);
        return result == null ? new BigDecimal(0) : result;
    }

    public List<LicencaSwProjetoParcela> findByLicencaSwProjeto(final Long codigoLicencaSwProjeto) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoLicencaSwProjeto", codigoLicencaSwProjeto);

        List<LicencaSwProjetoParcela> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_BY_LICENCA_SW_PROJETO, params);

        return results;
    }

    public BigDecimal findInstallmentValue(final Date dataParcela, final Long codigoLicencaSwProjeto, final Integer numeroParcela) {
        EntityManager entityManager = null;
        try {
            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            Query query = entityManager.createNamedQuery(LicencaSwProjetoParcela.FIND_INSTALLMENT_VALUE);

            query.setParameter("dataParcela", dataParcela);
            query.setParameter("codigoLicencaSwProjeto", codigoLicencaSwProjeto);
            query.setParameter("numeroParcela", numeroParcela);

            List listResult = query.getResultList();
            return listResult == null ? new BigDecimal(0) : (BigDecimal) listResult.get(0);

        } catch (HibernateException e) {
            e.printStackTrace();
            return new BigDecimal(0);
        } catch (Exception e) {
            e.printStackTrace();
            return new BigDecimal(0);
        } finally {
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
        }
    }

    public List<LicencaSwProjetoCell> findByStartDateAndCodigoLicencaProjeto(final Date data, Long codigoTiRecurso, String status){
        EntityManager entityManager = null;
        try {
            if (codigoTiRecurso == null){
                codigoTiRecurso = 0L;
            }

            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            Query query = entityManager.createNamedQuery(LicencaSwProjetoParcela.FIND_BY_START_DATE_AND_CODIGO_LICENCA_SW_PROJETO);

            query.setParameter("dataInicio", data);
            query.setParameter("codigoTiRecurso", codigoTiRecurso);
            query.setParameter("status", status);

            List result = query.getResultList();
            List<LicencaSwProjetoCell> licencaSwProjetoCellList = new ArrayList<LicencaSwProjetoCell>();
            for(Object obj : result){
                LicencaSwProjetoCell licencaSwProjetoCell = new LicencaSwProjetoCell();
                licencaSwProjetoCell.setMonth((String)((Object[])obj)[0]);
                licencaSwProjetoCell.setCodigoLicencaSwProjeto(((BigDecimal)((Object[])obj)[1]).longValue());
                licencaSwProjetoCell.setNomeLicenca((String)((Object[])obj)[2]);
                licencaSwProjetoCell.setNotaFiscal((String)((Object[])obj)[3]);
                licencaSwProjetoCell.setValorTotal((BigDecimal) ((Object[])obj)[4]);
                licencaSwProjetoCell.setStatus((String)((Object[])obj)[5]);
                licencaSwProjetoCell.setQtdeParcelas(((BigDecimal)((Object[])obj)[6]).longValue());
                licencaSwProjetoCell.setValorParcela((BigDecimal) ((Object[])obj)[7]);
                licencaSwProjetoCell.setParcelaApropriada(((BigDecimal)((Object[])obj)[8]).longValue());
                licencaSwProjetoCell.setValorApropriacao((BigDecimal) ((Object[])obj)[9]);
                licencaSwProjetoCell.setSaldoParcelas((BigDecimal) ((Object[])obj)[10]);
                licencaSwProjetoCell.setDataInicio((Date)((Object[])obj)[11]);
                licencaSwProjetoCell.setCodigoCentroCustoErp(((BigDecimal)((Object[])obj)[12]).longValue());
                licencaSwProjetoCell.setNomeCentroCusto((String)((Object[])obj)[13]);
                if(((Object[])obj)[14] != null ){
                    licencaSwProjetoCell.setCodigoProjetoErp(((BigDecimal)((Object[])obj)[14]).longValue());
                }
                licencaSwProjetoCell.setNomeProjetoErp((String)((Object[])obj)[15]);
                licencaSwProjetoCell.setDescricaoLicenca((String)((Object[])obj)[16]);
                licencaSwProjetoCell.setSaldo(((BigDecimal)((Object[])obj)[17]).longValue());
                licencaSwProjetoCell.setBusinessManager((String)((Object[])obj)[18]);
                licencaSwProjetoCell.setProjectManager((String)((Object[])obj)[19]);

                if(((Object[])obj)[20] != null ){
                    licencaSwProjetoCell.setLogins((String)((Object[])obj)[20]);
                }

                licencaSwProjetoCellList.add(licencaSwProjetoCell);
            }

            return licencaSwProjetoCellList;

        } catch (HibernateException e) {
            e.printStackTrace();
            return new ArrayList<LicencaSwProjetoCell>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<LicencaSwProjetoCell>();
        } finally {
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
        }
    }

    public List<LicencaSwProjetoCell> findAllInstallmentsFromCurrentMonth(){
        EntityManager entityManager = null;
        try {
            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            Query query = entityManager.createNamedQuery(LicencaSwProjetoParcela.FIND_ALL_INSTAMENTS_CURRENT_MONTH);

            List result = query.getResultList();
            List<LicencaSwProjetoCell> licencaSwProjetoCellList = new ArrayList<LicencaSwProjetoCell>();
            for(Object obj : result){
                LicencaSwProjetoCell licencaSwProjetoCell = new LicencaSwProjetoCell();
                licencaSwProjetoCell.setCodigoLicencaSwProjeto(((BigDecimal)((Object[])obj)[0]).longValue());
                licencaSwProjetoCell.setNomeLicenca((String)((Object[])obj)[1]);
                licencaSwProjetoCell.setNotaFiscal((String)((Object[])obj)[2]);
                licencaSwProjetoCell.setSaldo(((BigDecimal)((Object[])obj)[3]).longValue());
                licencaSwProjetoCell.setValorParcela((BigDecimal) ((Object[])obj)[4]);
                licencaSwProjetoCell.setDataInicio((Date)((Object[])obj)[5]);
                licencaSwProjetoCell.setCodigoCentroCustoErp(((BigDecimal)((Object[])obj)[6]).longValue());
                licencaSwProjetoCell.setNomeCentroCusto((String)((Object[])obj)[7]);
                if(((Object[])obj)[8] != null ){
                    licencaSwProjetoCell.setCodigoProjetoErp(((BigDecimal)((Object[])obj)[8]).longValue());
                }
                licencaSwProjetoCell.setNomeProjetoErp((String)((Object[])obj)[9]);
                licencaSwProjetoCell.setLogins((String)((Object[])obj)[10]);
                licencaSwProjetoCell.setBusinessManager((String)((Object[])obj)[11]);
                licencaSwProjetoCell.setProjectManager((String)((Object[])obj)[12]);
                licencaSwProjetoCell.setDescricaoLicenca((String)((Object[])obj)[13]);

                licencaSwProjetoCellList.add(licencaSwProjetoCell);
            }

            return licencaSwProjetoCellList;

        } catch (HibernateException e) {
            e.printStackTrace();
            return new ArrayList<LicencaSwProjetoCell>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<LicencaSwProjetoCell>();
        } finally {
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
        }
    }


    @Override
    public List<LicencaSwProjetoParcela> findByCodigoLicencaSwProjetoIn(List<Long> licencaSwProjetoCodigos, Date monthDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("licencaSwProjetoCodigos", licencaSwProjetoCodigos);
        params.put("monthDate", monthDate);

        return getJpaTemplate().findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_BY_CODIGO_IN, params);
    }

    public List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndStatus(final Long codigoLicencaSwProjeto, final String statusLicencaSwProjetoParcela) {
        EntityManager entityManager = null;
        try {
            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            Query query = entityManager.createNamedQuery(LicencaSwProjetoParcela.FIND_BY_LICENCA_SW_PROJETO_AND_STATUS);

            query.setParameter("codigoLicencaSwProjeto", codigoLicencaSwProjeto);
            query.setParameter("statusLicencaSwProjetoParcela", statusLicencaSwProjetoParcela);

            List<LicencaSwProjetoParcela> listResult = query.getResultList();

            return listResult;
        } finally {
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
        }
    }

    @Override
    public List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonthGreaterThan(Long codigoLicencaSwProjeto, Date beginDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoLicencaSwProjeto", codigoLicencaSwProjeto);
        params.put("beginDate", beginDate);


        return getJpaTemplate().findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_BY_LICENCA_SW_PROJETO_AND_MONTH_GREATER_THAN, params);
    }


    public List<Long> findLicenseIdByDateStart(Date searchStartDate, Date searchEndDate, Long invoiceNumber, Long project) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchStartDate", searchStartDate);
        params.put("searchEndDate", searchEndDate);
        params.put("invoiceNumber", invoiceNumber);
        params.put("project", project);

        return (List<Long>) getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_LICENSEID_BY_DATE_START, params);
    }

    public List<Long> findLicenseIdByDate(Date monthDate, Long invoiceNumber, Long project) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("monthDate", monthDate);
        params.put("invoiceNumber", invoiceNumber);
        params.put("project", project);

        return (List<Long>) getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_LICENSEID_BY_DATE, params);
    }

    @Override
    public List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonthLessThan(Long codigoLicencaSwProjeto, Date beginDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoLicencaSwProjeto", codigoLicencaSwProjeto);
        params.put("beginDate", beginDate);


        return getJpaTemplate().findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_BY_LICENCA_SW_PROJETO_AND_MONTH_LESS_THAN, params);
    }
}
