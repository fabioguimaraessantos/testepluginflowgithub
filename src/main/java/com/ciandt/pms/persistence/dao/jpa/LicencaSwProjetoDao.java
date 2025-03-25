package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.persistence.dao.ILicencaSwProjetoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class LicencaSwProjetoDao extends AbstractDao<Long, LicencaSwProjeto> implements ILicencaSwProjetoDao {

    @Autowired
    public LicencaSwProjetoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, LicencaSwProjeto.class);
    }

    public List<LicencaSwProjeto> findByCodigoTiRecurso(Long codigoTiRecurso) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoTiRecurso", codigoTiRecurso);

        List<LicencaSwProjeto> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams("LicencaSwProjeto.findLicencaByTiRecurso", params);

        return results;
    }

    public List<LicencaSwProjeto> findAll() {
        List<LicencaSwProjeto> listResult = getJpaTemplate().findByNamedQuery(
                LicencaSwProjeto.FIND_ALL);

        return listResult;
    }

    @Override
    public List<LicencaSwProjeto> findByCodigoProcurify(String codigoProcurify) {
        List<LicencaSwProjeto> licencaSwProjetos = getJpaTemplate().findByNamedQuery(
                LicencaSwProjeto.FIND_BY_CODIGO_PROCURIFY, new Object[]{codigoProcurify}
        );

        return licencaSwProjetos;
    }

    public List<LicencaSwProjeto> findByFilter(Collection<Long> codigosTiRecurso, String codigoProcurify, Long notaFiscal, Date searchStartDate, Date searchEndDate,Long invoiceNumber, Long project, Long licenseID, String resourceName) {
        Map<String, Object> params = new HashMap<String, Object>();

        if (codigosTiRecurso == null || codigosTiRecurso.isEmpty()) {
            params.put("codigosTiRecursoNull", "Y");
        } else {
            params.put("codigosTiRecursoNull", "N");
        }

        params.put("codigosTiRecurso", codigosTiRecurso);
        params.put("codigoProcurify", codigoProcurify);
        params.put("notaFiscal", notaFiscal);
        params.put("searchStartDate", searchStartDate);
        params.put("searchEndDate", searchEndDate);
        params.put("invoiceNumber", invoiceNumber);
        params.put("project", project);
        params.put("licenseID", licenseID);
        params.put("resourceName", resourceName);

        List<LicencaSwProjeto> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjetoParcela.FIND_LICENCA_SW_PROJETO_BY_DATE, params);

        return results;
    }

    @Override
    public List<LicencaSwProjeto> findProjetosUtilizados(Long numeroNota, Long codigoErpFornecedor) {
        Map<String, Object> params = new HashMap<>();
        params.put("numeroNota", numeroNota);
        params.put("codigoErpFornecedor", codigoErpFornecedor);
        List<LicencaSwProjeto> results = getJpaTemplate().findByNamedQueryAndNamedParams(LicencaSwProjeto.FIND_PROJETOS_UTILIZADOS, params);

        return results;
    }

    public List<InvoiceProjectMegaDTO> findProjectByFilter(Date searchStartDate, Date searchEndDate, Long invoiceNumber) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchStartDate", searchStartDate);
        params.put("searchEndDate", searchEndDate);
        params.put("invoiceNumber", invoiceNumber);

        List<Object[]> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjeto.FIND_PROJECT_BY_FILTER, params);

        List<InvoiceProjectMegaDTO> resultObject = new ArrayList<InvoiceProjectMegaDTO>();

        for (Object[] obj : results) {
            InvoiceProjectMegaDTO invoiceProjectMegaDTO = new InvoiceProjectMegaDTO();
            invoiceProjectMegaDTO.setProjectCode(((BigDecimal)((Object[])obj)[0]).longValue());
            invoiceProjectMegaDTO.setProjectDescription((String)((Object[])obj)[1]);

            resultObject.add(invoiceProjectMegaDTO);

        }

        return resultObject;
    }

    public List<InvoiceProjectMegaDTO> findProjectByDataDaParcelaAndFilter(Date monthDate, Long invoiceNumber) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("monthDate", monthDate);
        params.put("invoiceNumber", invoiceNumber);

        List<Object[]> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjeto.FIND_PROJECT_BY_DATA_DA_PARCELA_AND_FILTER, params);

        List<InvoiceProjectMegaDTO> resultObject = new ArrayList<InvoiceProjectMegaDTO>();

        for (Object[] obj : results) {
            InvoiceProjectMegaDTO invoiceProjectMegaDTO = new InvoiceProjectMegaDTO();
            invoiceProjectMegaDTO.setProjectCode(((BigDecimal)((Object[])obj)[0]).longValue());
            invoiceProjectMegaDTO.setProjectDescription((String)((Object[])obj)[1]);

            resultObject.add(invoiceProjectMegaDTO);

        }

        return resultObject;
    }

    public List<Long> findInvoiceByDate(Date searchStartDate, Date searchEndDate){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("searchStartDate", searchStartDate);
        params.put("searchEndDate", searchEndDate);

        List<Long> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjeto.FIND_INVOICE_BY_DATE, params);

        return results;
    }

    public List<Long> findInvoiceByDataDaParcela(Date monthDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("monthDate", monthDate);

        List<Long> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(LicencaSwProjeto.FIND_INVOICE_BY_DATA_DA_PARCELA, params);

        return results;
    }

}
