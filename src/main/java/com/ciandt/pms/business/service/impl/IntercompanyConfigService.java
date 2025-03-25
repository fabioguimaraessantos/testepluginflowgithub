package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IInterCompanyConfigService;
import com.ciandt.pms.model.IntercompanyConfig;
import com.ciandt.pms.persistence.dao.IIntercompanyConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntercompanyConfigService implements IInterCompanyConfigService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IIntercompanyConfigDao intercompanyConfigDao;

    /**
     * Retorna uma list de configs intercompany entre 2 empresas
     *
     * @param mainCompany  Id da empresa principal.
     * @param interCompany Id da empresa intercompany.
     * @return Configs de intercompany, se existir
     */
    public List<IntercompanyConfig> findByCompanies(Long mainCompany, Long interCompany) {
        return this.intercompanyConfigDao.findByCompanies(mainCompany, interCompany);
    }
}