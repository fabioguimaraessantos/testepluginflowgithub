package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwDifMapaSnapshots;
import com.ciandt.pms.model.VwDifMapaSnapshotsId;
import com.ciandt.pms.persistence.dao.IVwDifMapaSnapshotsDao;


/**
 * 
 * A classe VwDifMapaSnapshotsDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade VwDifMapaSnapshots.
 * 
 * @since 08/03/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class VwDifMapaSnapshotsDao extends
        AbstractDao<VwDifMapaSnapshotsId, VwDifMapaSnapshots> implements
        IVwDifMapaSnapshotsDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public VwDifMapaSnapshotsDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, VwDifMapaSnapshots.class);

    }

    /**
     * Retorna todas as entidades dentro do range de meses.
     * 
     * @param startDate
     *            - data de início do range do follow
     * @param rangeMonths
     *            - range de meses
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     * 
     * @return retorna uma lista com todas as entidades dentro do range de
     *         meses.
     */
    @SuppressWarnings("unchecked")
    public List<VwDifMapaSnapshots> findAllByRangeMonths(final Date startDate,
            final Integer rangeMonths, final Boolean isClobFollow) {
        List<VwDifMapaSnapshots> listResult = getJpaTemplate()
                .findByNamedQuery(
                        isClobFollow ? VwDifMapaSnapshots.FIND_ALL_BY_RANGE_MONTHS_CLOB
                                : VwDifMapaSnapshots.FIND_ALL_BY_RANGE_MONTHS_PEOPLE,
                        new Object[] {startDate, startDate, rangeMonths });

        return listResult;
    }

}