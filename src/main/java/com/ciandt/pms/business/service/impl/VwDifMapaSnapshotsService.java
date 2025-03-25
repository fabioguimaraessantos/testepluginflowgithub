package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IVwDifMapaSnapshotsService;
import com.ciandt.pms.model.VwDifMapaSnapshots;
import com.ciandt.pms.persistence.dao.IVwDifMapaSnapshotsDao;


/**
 * 
 * A classe VwDifMapaSnapshotsService proporciona as funcionalidades da camada
 * de serviço referente a entidade VwDifMapaSnapshots.
 * 
 * @since 08/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class VwDifMapaSnapshotsService implements IVwDifMapaSnapshotsService {

    /** Instancia DAO da entidade VwDifMapaSnapshots. */
    @Autowired
    private IVwDifMapaSnapshotsDao difMapaSnapshotsDao;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Retorna todas as entidades dentro do range de meses.
     * 
     * @param startDate
     *            - data de início do range do follow
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     * 
     * @return retorna uma lista com todas as entidades dentro do range de
     *         meses.
     */
    public List<VwDifMapaSnapshots> findAllByRangeMonths(final Date startDate,
            final Boolean isClobFollow) {
        return difMapaSnapshotsDao.findAllByRangeMonths(startDate, new Integer(
                (String) systemProperties
                        .get(Constants.MAPA_ALOCACAO_RANGE_MONTHS_SNAPSHOTS)),
                isClobFollow);
    }

}