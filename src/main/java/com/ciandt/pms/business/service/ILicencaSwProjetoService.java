package com.ciandt.pms.business.service;

import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import com.ciandt.pms.model.LicencaSwProjeto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public interface ILicencaSwProjetoService {
    LicencaSwProjeto findById(final Long entityId);

    List<LicencaSwProjeto> findAll();

    Boolean create(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal, List<String> logins);

    List<LicencaSwProjeto> findByCodigoTiRecurso(Long codigoTiRecurso);

    List<LicencaSwProjeto> findByCodigosTiRecurso(Collection<Long> codigosTiRecurso);

    List<LicencaSwProjeto> findProjetosUtilizados(Long numeroNota, Long codigoErpFornecedor);


    List<LicencaSwProjeto> findByFilter(Collection<Long> codigosTiRecurso, String codigoProcurify, Long notaFiscal, Date searchStartDate, Date searchEndDate, Long invoiceNumber, Long project, Long licenseID, String resourceName);

    List<InvoiceProjectMegaDTO> findProjectByFilter(Date searchStartDate, Date searchEndDate, Long invoiceNumber);

    List<InvoiceProjectMegaDTO> findProjectByDataDaParcelaAndFilter(Date monthDate, Long invoiceNumber);

    List<Long> findInvoiceByDate(Date searchStartDate, Date searchEndDate);

    List<Long> findInvoiceByDataDaParcela(Date monthDate);

    Boolean update(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal, List<String> logins);

    void update(LicencaSwProjeto licencaSwProjeto);

    void createInitialParcelas();

    void updateIntegratedParcelasTST();

	void removeSoftwareProjectLicenseAndInstallments(final LicencaSwProjeto entity);
}
