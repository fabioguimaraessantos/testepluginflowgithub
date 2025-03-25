package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import com.ciandt.pms.model.LicencaSwProjeto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ILicencaSwProjetoDao extends IAbstractDao<Long, LicencaSwProjeto> {

    List<LicencaSwProjeto> findByCodigoTiRecurso(Long codigoTiRecurso);

    List<LicencaSwProjeto> findAll();

    List<LicencaSwProjeto> findByCodigoProcurify(String codigoProcurify);

    List<LicencaSwProjeto> findProjetosUtilizados(Long numeroNota, Long codigoErpFornecedor);

    List<LicencaSwProjeto> findByFilter(Collection<Long> codigosTiRecurso, String codigoProcurify, Long notaFiscal, Date searchStartDate, Date searchEndDate,Long invoiceNumber, Long project, Long licenseID, String resourceName);

    List<InvoiceProjectMegaDTO> findProjectByFilter(Date searchStartDate, Date searchEndDate, Long invoiceNumber);

    List<InvoiceProjectMegaDTO> findProjectByDataDaParcelaAndFilter(Date monthDate, Long invoiceNumber);

    List<Long> findInvoiceByDate(Date searchStartDate, Date searchEndDate);

    List<Long> findInvoiceByDataDaParcela(Date monthDate);

}
