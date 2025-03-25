package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.ILicencaSwProjetoPessoaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoPessoa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.ILicencaSwProjetoPessoaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LicencaSwProjetoPessoaService implements ILicencaSwProjetoPessoaService {

    @Autowired
    private ILicencaSwProjetoPessoaDao dao;

    @Autowired
    private IPessoaService pessoaService;

    public List<LicencaSwProjetoPessoa> findAll() {
        return dao.findAll();
    }

    @Transactional
    public LicencaSwProjetoPessoa findById(final Long id) {
        return dao.findById(id);
    }

    @Transactional
    public List<LicencaSwProjetoPessoa> findByLicencaSwProjeto(
            final LicencaSwProjeto licencaSwProjeto) {
        return dao.findByLicencaSwProjeto(licencaSwProjeto);
    }

    @Transactional
    public List<LicencaSwProjetoPessoa> findByLicencaSwProjetoAndName(
            final LicencaSwProjeto licencaSwProjeto, final String name) {
        return dao.findByLicencaSwProjetoAndName(licencaSwProjeto, name);
    }

    @Transactional
    public Boolean removePersonLicencaSwProjeto(final LicencaSwProjetoPessoa entity) {
        dao.remove(entity);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public void removePeopleLicencaSwProjeto(List<LicencaSwProjetoPessoa> entities) {
        for (LicencaSwProjetoPessoa entity : entities) {
            dao.remove(entity);
        }
    }

    @Override
    public void addPeopleLicencaSwProjeto(List<String> logins, LicencaSwProjeto licencaSwProjeto) {
        List<Pessoa> people = pessoaService.findPessoaByLoginIn(logins);

        for (Pessoa pessoa : people) {
            LicencaSwProjetoPessoa licencaSwProjetoPessoa = new LicencaSwProjetoPessoa();
            licencaSwProjetoPessoa.setPessoa(pessoa);
            licencaSwProjetoPessoa.setLicencaSwProjeto(licencaSwProjeto);

            dao.create(licencaSwProjetoPessoa);
        }
    }

    @Override
    @Transactional
    public void updatePeopleLicencaSwProjeto(List<String> logins, LicencaSwProjeto licencaSwProjeto) {
        for (String login : logins) {
            login.replace(" \\(Inactive\\)", "");
        }

        if (licencaSwProjeto != null && licencaSwProjeto.getCodigoLicencaSwProjeto() != null) {
            List<LicencaSwProjetoPessoa> existentLogins = this.findByLicencaSwProjeto(licencaSwProjeto);

            if (!existentLogins.isEmpty()) {
                List<LicencaSwProjetoPessoa> toRemove = new ArrayList<LicencaSwProjetoPessoa>();
                List<String> toAdd = new ArrayList<String>(logins);
                for (LicencaSwProjetoPessoa pessoa : existentLogins) {
                    if (!logins.contains(pessoa.getPessoa().getCodigoLogin())) {
                        toRemove.add(pessoa);
                    } else {
                        toAdd.remove(pessoa.getPessoa().getCodigoLogin());
                    }
                }
                logins = new ArrayList<String>(toAdd);
                this.removePeopleLicencaSwProjeto(toRemove);
            }
        }

        if (!logins.isEmpty()) {
            this.addPeopleLicencaSwProjeto(logins, licencaSwProjeto);
        }
    }
}
