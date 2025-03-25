package com.ciandt.pms.business.service;

import com.ciandt.pms.integration.vo.licenses.IntegLicense;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public interface IIntegrateLicenseService {


    @Transactional
    IntegLicense payload(Long tiRecursoCode, Date period,  Long companyCode);

    void integrate(IntegLicense payloads);

    List<LicencaSwDetail> findIntegrateDetailsByTiRecurso(Long tiRecursoCode, Date period, List<LicencaSwDetail> details);
}