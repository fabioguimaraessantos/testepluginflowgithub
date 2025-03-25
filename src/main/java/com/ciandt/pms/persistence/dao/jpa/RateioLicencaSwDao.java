package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.RateioLicencaSw;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.model.vo.LicencaSwUserCell;
import com.ciandt.pms.model.vo.LicencaSwUserVO;
import com.ciandt.pms.persistence.dao.IRateioLicencaSwDao;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class RateioLicencaSwDao extends AbstractDao<Long, RateioLicencaSw> implements IRateioLicencaSwDao {

    @Autowired
    public RateioLicencaSwDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, RateioLicencaSw.class);
    }


    public List<LicencaSwUserVO> findByFilter(Date dtCompetencia, Long codTiRecurso, String status) {
        try {

            if (codTiRecurso == null) {
                codTiRecurso = 0L;
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("dtCompetencia", dtCompetencia);
            params.put("codigoLicenca", codTiRecurso);
            params.put("status", status);

            List<Object[]> resultset = getJpaTemplate().findByNamedQueryAndNamedParams(RateioLicencaSw.FIND_BY_FILTER, params);

            List<LicencaSwUserVO> resultObject = new ArrayList<LicencaSwUserVO>();

            for (Object[] map : resultset) {
                LicencaSwUserVO objVo = new LicencaSwUserVO();
                objVo.setCodigoTiRecurso(((BigDecimal) map[0]).longValue());
                objVo.setNomeLicenca((String) map[1]);
                objVo.setValorLicencaTotal((BigDecimal) map[2]);
                objVo.setStatus((String) map[3]);
                objVo.setTextoError((String) map[4]);
                objVo.setLastUpdate((Date) map[5]);
                resultObject.add(objVo);
            }

            return resultObject;

        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<LicencaSwDetail> findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(final Date dtCompetencia, Long codTiRecurso) {
        try {

            if (codTiRecurso == null){
                codTiRecurso = 0L;
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("dtCompetencia", dtCompetencia);
            params.put("codigoLicenca", codTiRecurso);
            List<Object[]> resultset = getJpaTemplate().findByNamedQueryAndNamedParams(RateioLicencaSw.FIND_BY_RECURSO_TI_MONTH_GROUPBY_CENTROCUSTO_PROJETO, params);

            List<LicencaSwDetail> resultObject = new ArrayList<LicencaSwDetail>();

            for (Object[] map : resultset) {
                LicencaSwDetail obj = new LicencaSwDetail();
                obj.setCodigoGrupoCusto(((BigDecimal)map[0]).longValue());
                obj.setNomeGrupoCusto((String) map[1]);
                obj.setCodigoCentroCustoErp(((BigDecimal)map[2]).longValue());
                obj.setCodigoProjetoErp(map[3] != null ? ((BigDecimal)map[3]).longValue() : null);
                obj.setNomeProjetoErp((String) map[4]);
                obj.setCodigoContratoPratica(map[5] != null ? ((BigDecimal)map[5]).longValue() : null);
                obj.setNomeContratoPratica(map[6] != null ? (String)map[6] : null);
                obj.setValor((BigDecimal) map[7]);
                obj.setNotaFiscal((String) map[9]);
                obj.setIsUserLicense(true);
                resultObject.add(obj);
            }

            return resultObject;

        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public List<RateioLicencaSw> findByRecursoTIAndMonth(final Date dtCompetencia, Long codTiRecurso) {
        if (codTiRecurso == null){
            codTiRecurso = 0L;
        }

        List<RateioLicencaSw> listResult =
                getJpaTemplate().findByNamedQuery(
                        RateioLicencaSw.FIND_BY_RECURSO_TI_AND_MONTH,
                        new Object[]{dtCompetencia, codTiRecurso, codTiRecurso});
        return listResult;
    }

    @Override
    public List<LicencaSwUserCell> findByRecursoTiAndMonthAndStatus(Long itResourceCode, Date monthDate, String status) {
        try {
            if (itResourceCode == null){
                itResourceCode = 0L;
            }

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("dataCompetencia", monthDate);
            params.put("codTiRecurso", itResourceCode);
            params.put("status", status);
            List<Object[]>  listResult = getJpaTemplate().findByNamedQueryAndNamedParams(RateioLicencaSw.FIND_BY_CODIGO_RECURSO_DATA_INICIO_AND_STATUS, params);

            List<LicencaSwUserCell> licencaSwUserCellList = new ArrayList<LicencaSwUserCell>();
            for(Object[] obj : listResult){
                if (obj != null) {
                    LicencaSwUserCell licencaSwUserCell = new LicencaSwUserCell();
                    licencaSwUserCell.setMonth(obj[0] != null ? (String) obj[0] : null);
                    licencaSwUserCell.setItResourceCode(obj[1] != null ? ((BigDecimal) obj[1]).longValue() : null);
                    licencaSwUserCell.setItResourceName(obj[2] != null ? (String) obj[2] : null);
                    licencaSwUserCell.setLicenseTotalValue(obj[3] != null ? ((BigDecimal) obj[3]).doubleValue() : null);
                    licencaSwUserCell.setStatus(obj[4] != null ? (String) obj[4] : null);
                    licencaSwUserCell.setCostCenterCode(obj[5] != null ? ((BigDecimal) obj[5]).longValue() : null);
                    licencaSwUserCell.setCostCenterName(obj[6] != null ? (String) obj[6] : null);
                    licencaSwUserCell.setMegaProjectCode(obj[7] != null ? ((BigDecimal) obj[7]).longValue() : null);
                    licencaSwUserCell.setMegaProjectName(obj[8] != null ? (String) obj[8] : null);
                    licencaSwUserCell.setLicenseValue(obj[9] != null ? ((BigDecimal) obj[9]).doubleValue() : null);
                    licencaSwUserCell.setLicenseLogins(obj[10] != null ? ((String) obj[10]).replace("\u0000", "") : null);

                    licencaSwUserCellList.add(licencaSwUserCell);
                }
              }

            return licencaSwUserCellList;

        } catch (HibernateException e) {
            e.printStackTrace();
            return new ArrayList<LicencaSwUserCell>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<LicencaSwUserCell>();
        }
    }

}
