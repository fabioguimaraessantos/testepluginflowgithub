package com.ciandt.pms.business.service;

import com.ciandt.pms.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILicencaSwProjetoPessoaService {

    List<LicencaSwProjetoPessoa> findAll();

    LicencaSwProjetoPessoa findById(final Long id);

    List<LicencaSwProjetoPessoa> findByLicencaSwProjeto(
            final LicencaSwProjeto licencaSwProjeto);

    List<LicencaSwProjetoPessoa> findByLicencaSwProjetoAndName(final LicencaSwProjeto licencaSwProjeto, final String name);

    Boolean removePersonLicencaSwProjeto(final LicencaSwProjetoPessoa entity);

    void removePeopleLicencaSwProjeto(final List<LicencaSwProjetoPessoa> entities);

    void addPeopleLicencaSwProjeto(List<String> logins, LicencaSwProjeto licencaSwProjeto);

    void updatePeopleLicencaSwProjeto(List<String> logins, LicencaSwProjeto licencaSwProjeto);
}
