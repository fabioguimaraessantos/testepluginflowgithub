package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.VwPmsGrupoContaContabil;
import org.springframework.stereotype.Repository;

@Repository
public interface IVwPmsGrupoContaContabilDao  extends IAbstractDao<Long, VwPmsGrupoContaContabil>{

    VwPmsGrupoContaContabil findByCodigoContratoPratica(final Long codigoContratoPratica);

    VwPmsGrupoContaContabil findByCodigoCentroCusto(final Long codigoCentroCusto);

    VwPmsGrupoContaContabil findByCodigoGrupoCusto(final Long codigoCentroCusto);
}
