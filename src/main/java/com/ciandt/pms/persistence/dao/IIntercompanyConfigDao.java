package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.IntercompanyConfig;

import java.util.List;


public interface IIntercompanyConfigDao extends IAbstractDao<Long, IntercompanyConfig> {

    /**
     * Retorna uma list de configs intercompany entre 2 empresas
     *
     * @param mainCompany  Id da empresa principal.
     * @param interCompany Id da empresa intercompany.
     * @return Configs de intercompany, se existir
     */
    List<IntercompanyConfig> findByCompanies(Long mainCompany, Long interCompany);
}