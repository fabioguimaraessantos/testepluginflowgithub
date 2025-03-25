package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.VwDifMapaSnapshots;


/**
 * 
 * A classe IVwDifMapaSnapshotsService proporciona a interface de acesso a
 * camada de servi�o referente a entidade VwDifMapaSnapshots.
 * 
 * @since 08/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IVwDifMapaSnapshotsService {

    /**
     * Retorna todas as entidades dentro do range de meses.
     * 
     * @param startDate
     *            - data de in�cio do range do follow
     * @param isClobFollow
     *            - indicador se � follow de C-lob ou n�o (People)
     * 
     * @return retorna uma lista com todas as entidades dentro do range de
     *         meses.
     */
    List<VwDifMapaSnapshots> findAllByRangeMonths(final Date startDate,
            final Boolean isClobFollow);

}