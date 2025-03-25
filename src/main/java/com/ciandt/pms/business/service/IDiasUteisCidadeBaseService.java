package com.ciandt.pms.business.service;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by josef on 01/11/2017.
 */
@Service
public interface IDiasUteisCidadeBaseService {

    Long findByCidadeBaseAndMonth(final Long codigoCidadeBase, final Date dataMes );
}
