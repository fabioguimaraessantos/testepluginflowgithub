package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.ILicencaSwProjetoParcelaService;
import com.ciandt.pms.business.service.ILicencaSwProjetoPessoaService;
import com.ciandt.pms.business.service.ILicencaSwProjetoService;
import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.persistence.dao.ILicencaSwProjetoDao;
import org.jsefa.common.config.InitialConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LicencaSwProjetoService implements ILicencaSwProjetoService {

    @Autowired
    private ILicencaSwProjetoDao dao;

    @Autowired
    private ILicencaSwProjetoParcelaService licencaSwProjetoParcelaService;

    @Autowired
    private ILicencaSwProjetoPessoaService licencaSwProjetoPessoaService;

    public LicencaSwProjeto findById(final Long entityId) {
        return dao.findById(entityId);
    }

    public List<LicencaSwProjeto> findAll() {
        return dao.findAll();
    }

    @Transactional
    public Boolean create(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal, List<String> logins) {
        dao.create(licencaSwProjeto);
        licencaSwProjetoPessoaService.addPeopleLicencaSwProjeto(logins, licencaSwProjeto);
        return licencaSwProjetoParcelaService.create(licencaSwProjeto, contratoPraticaCodigos, isInternal);
    }

    public List<LicencaSwProjeto> findByCodigoTiRecurso(Long codigoTiRecurso) {
        return dao.findByCodigoTiRecurso(codigoTiRecurso);
    }

    public List<LicencaSwProjeto> findByCodigosTiRecurso(Collection<Long> codigosTiRecurso) {
        List<LicencaSwProjeto> licencaSwProjetos = new ArrayList<LicencaSwProjeto>();
        for (Long codigoTiRecurso : codigosTiRecurso) {
            licencaSwProjetos.addAll(this.findByCodigoTiRecurso(codigoTiRecurso));
        }

        return licencaSwProjetos;
    }

    @Override
    public List<LicencaSwProjeto> findProjetosUtilizados(Long numeroNota, Long codigoErpFornecedor) {
        return dao.findProjetosUtilizados(numeroNota, codigoErpFornecedor);
    }

    @Override
    public List<LicencaSwProjeto> findByFilter(Collection<Long> codigosTiRecurso, String codigoProcurify, Long notaFiscal, Date searchStartDate, Date searchEndDate, Long invoiceNumber, Long project, Long licenseID, String resourceName) {
        return dao.findByFilter(codigosTiRecurso, codigoProcurify, notaFiscal,searchStartDate, searchEndDate, invoiceNumber, project, licenseID, resourceName);
    }

    @Override
    public List<InvoiceProjectMegaDTO> findProjectByFilter(Date searchStartDate, Date searchEndDate, Long invoiceNumber) {
        return dao.findProjectByFilter(searchStartDate,searchEndDate, invoiceNumber);
    }

    @Override
    public List<InvoiceProjectMegaDTO> findProjectByDataDaParcelaAndFilter(Date monthDate, Long invoiceNumber) {
        return dao.findProjectByDataDaParcelaAndFilter(monthDate, invoiceNumber);
    }

    @Override
    public List<Long> findInvoiceByDate(Date searchStartDate, Date searchEndDate) {
        return dao.findInvoiceByDate(searchStartDate,searchEndDate);
    }

    public List<Long> findInvoiceByDataDaParcela(Date monthDate) {
        return dao.findInvoiceByDataDaParcela(monthDate);
    }

    @Transactional
    public Boolean update(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal, List<String> logins) {
        dao.update(licencaSwProjeto);
        licencaSwProjetoPessoaService.updatePeopleLicencaSwProjeto(logins, licencaSwProjeto);
        return licencaSwProjetoParcelaService.updateParcelasFromLicencaSwProjeto(licencaSwProjeto, contratoPraticaCodigos, isInternal);
    }

    @Transactional
    public void update(LicencaSwProjeto licencaSwProjeto) {
        dao.update(licencaSwProjeto);
    }

    @Transactional
    public void createInitialParcelas() {
        List<LicencaSwProjeto> all = dao.findAll();
        for (LicencaSwProjeto licencaSwProjeto : all) {
            if (licencaSwProjeto.getCdCC() != null) {
                licencaSwProjetoParcelaService.create(licencaSwProjeto, Arrays.asList(licencaSwProjeto.getCdCC()), true);
            } else if (licencaSwProjeto.getCdClob() != null) {
                licencaSwProjetoParcelaService.create(licencaSwProjeto, Arrays.asList(licencaSwProjeto.getCdClob()), false);
            } else {
                System.out.print("error!!!!");
                throw new InitialConfigurationException("CC e Clob estão nulos!!!!!");
            }
        }
    }

    @Transactional
    public void updateIntegratedParcelasTST() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date integrateParcelasUntil = cal.getTime();
        List<LicencaSwProjeto> licencaSwProjetos = dao.findAll();

        for (LicencaSwProjeto licencaSwProjeto : licencaSwProjetos) {
            List<LicencaSwProjetoParcela> parcelas = licencaSwProjetoParcelaService.findByLicencaSwProjeto(licencaSwProjeto.getCodigoLicencaSwProjeto());
            for (LicencaSwProjetoParcela parcela : parcelas) {
                if (parcela.getDataParcela().before(integrateParcelasUntil)) {
                    parcela.setStatus(LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName());
                    licencaSwProjetoParcelaService.update(parcela);
                }
            }
        }
    }

    @Override
    @Transactional
    public void removeSoftwareProjectLicenseAndInstallments(final LicencaSwProjeto entity) {
        List<LicencaSwProjetoParcela> installments =
            licencaSwProjetoParcelaService.findByLicencaSwProjeto(entity.getCodigoLicencaSwProjeto());
        if (!installments.isEmpty()) {
            licencaSwProjetoParcelaService.removeInstallments(installments);
        }
        dao.remove(entity);
    }

}
