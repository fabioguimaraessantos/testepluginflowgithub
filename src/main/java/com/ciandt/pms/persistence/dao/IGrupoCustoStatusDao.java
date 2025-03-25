package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.GrupoCustoStatus;

import java.util.List;

public interface IGrupoCustoStatusDao extends IAbstractDao<Long, GrupoCustoStatus> {

    GrupoCustoStatus findBySiglaStatusGrupoCusto(final String siglaStatusGrupoCusto);

    List<GrupoCustoStatus> findAllActive();

    List<GrupoCustoStatus> findByStatusActiveAndRequestInac(final String siglaActive, final String siglaRequestInact);

    List<GrupoCustoStatus> findByStatusACTIAndCLOSAndREINAndININ(final String siglaActive, final String siglaClosed,
                                                                 final String siglaRequestInact,
                                                                 final String siglaIntegInactivation);

    List<GrupoCustoStatus> findByStatusRECRAndINCR(final String siglaRequestCreation, final String siglaIntegCreation);

    List<GrupoCustoStatus> findByStatusINAC(final String siglaInactive);

    List<GrupoCustoStatus> findBySiglaInList(final List<String> listSigla);

}
