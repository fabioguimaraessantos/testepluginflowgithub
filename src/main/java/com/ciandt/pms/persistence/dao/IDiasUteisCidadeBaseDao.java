package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.DiasUteisCidadeBase;

import java.util.Date;

/**
 * Created by josef on 01/11/2017.
 */
public interface IDiasUteisCidadeBaseDao extends IAbstractDao<Long, DiasUteisCidadeBase> {

    Long findByCidadeBaseAndMonth(final Long codigoCidadeBase, final Date dataMes );
}
