package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IDiasUteisCidadeBaseService;
import com.ciandt.pms.persistence.dao.IDiasUteisCidadeBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by josef on 01/11/2017.
 */
@Service
public class DiasUteisCidadeBaseService implements IDiasUteisCidadeBaseService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IDiasUteisCidadeBaseDao diasUteisCidadeBaseDao;

    public Long findByCidadeBaseAndMonth(final Long codigoCidadeBase, final Date dataMes ){
        return diasUteisCidadeBaseDao.findByCidadeBaseAndMonth(codigoCidadeBase, dataMes);
    }
}
