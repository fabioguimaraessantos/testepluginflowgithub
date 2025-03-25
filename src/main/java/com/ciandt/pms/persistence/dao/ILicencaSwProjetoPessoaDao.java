package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoPessoa;
import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcamentoDespesa;

import java.util.List;

public interface ILicencaSwProjetoPessoaDao extends IAbstractDao<Long, LicencaSwProjetoPessoa> {

    List<LicencaSwProjetoPessoa> findAll();

    List<LicencaSwProjetoPessoa> findByLicencaSwProjeto(final LicencaSwProjeto entity);

    List<LicencaSwProjetoPessoa> findByLicencaSwProjetoAndName(
            final LicencaSwProjeto licencaSwProjeto, final String name);
}
