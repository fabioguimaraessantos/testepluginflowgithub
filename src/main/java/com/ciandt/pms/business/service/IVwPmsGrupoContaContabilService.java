package com.ciandt.pms.business.service;

import com.ciandt.pms.model.VwPmsGrupoContaContabil;
import org.springframework.stereotype.Service;

@Service
public interface IVwPmsGrupoContaContabilService {

    VwPmsGrupoContaContabil findByCodigoContratoPratica(final Long codigoContratoPratica);

    VwPmsGrupoContaContabil findByCodigoCentroCusto(final Long codigoCentroCusto);

    VwPmsGrupoContaContabil findByCodigoGrupoCusto(final Long codigoCentroCusto);
}
