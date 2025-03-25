package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IVwPmsGrupoContaContabilService;
import com.ciandt.pms.model.VwPmsGrupoContaContabil;
import com.ciandt.pms.persistence.dao.IVwPmsGrupoContaContabilDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VwPmsGrupoContaContabilService implements IVwPmsGrupoContaContabilService {

    @Autowired
    private IVwPmsGrupoContaContabilDao vwPmsGrupoContaContabilDao;

    public VwPmsGrupoContaContabil findByCodigoContratoPratica(final Long codigoContratoPratica) {
        return vwPmsGrupoContaContabilDao.findByCodigoContratoPratica(codigoContratoPratica);

    }

    public VwPmsGrupoContaContabil findByCodigoCentroCusto(final Long codigoCentroCusto) {
        return vwPmsGrupoContaContabilDao.findByCodigoCentroCusto(codigoCentroCusto);
    }

    public VwPmsGrupoContaContabil findByCodigoGrupoCusto(final Long codigoGrupoCusto) {
        return vwPmsGrupoContaContabilDao.findByCodigoGrupoCusto(codigoGrupoCusto);
    }
}
