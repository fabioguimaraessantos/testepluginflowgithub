package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.VwDifMapaSnapshots;
import com.ciandt.pms.model.VwDifMapaSnapshotsId;


/**
 * 
 * A classe IVwDifMapaSnapshotsDao proporciona a interface de acesso para a
 * camada de persistencia referente a entidade VwDifMapaSnapshots.
 * 
 * @since 08/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IVwDifMapaSnapshotsDao extends
        IAbstractDao<VwDifMapaSnapshotsId, VwDifMapaSnapshots> {

    /**
     * Retorna todas as entidades dentro do range de meses.
     * 
     * @param closingDateMapaAloc
     *            - closingDate do Modulo do MapaAlocacao
     * @param rangeMonths
     *            - range de meses
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     * 
     * @return retorna uma lista com todas as entidades dentro do range de
     *         meses.
     */
    List<VwDifMapaSnapshots> findAllByRangeMonths(
            final Date closingDateMapaAloc, final Integer rangeMonths,
            final Boolean isClobFollow);

}