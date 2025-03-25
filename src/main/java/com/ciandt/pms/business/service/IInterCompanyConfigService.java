package com.ciandt.pms.business.service;

import com.ciandt.pms.model.IntercompanyConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IInterCompanyConfigService {

    /**
     * Retorna uma list de configs intercompany entre 2 empresas
     *
     * @param mainCompany  Id da empresa principal.
     * @param interCompany Id da empresa intercompany.
     * @return Configs de intercompany, se existir
     */
    List<IntercompanyConfig> findByCompanies(Long mainCompany, Long interCompany);
}