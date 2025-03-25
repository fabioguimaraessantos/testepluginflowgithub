package com.ciandt.pms.business.service;

import com.ciandt.pms.model.GrupoCustoStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface IGrupoCustoStatusService {

    GrupoCustoStatus findBySiglaStatusGrupoCusto(final String siglaStatusGrupoCusto);

    List<GrupoCustoStatus> findAllActive();

    List<GrupoCustoStatus> findByStatusActiveAndRequestInac(final String siglaActive, final String siglaRequestInact);

    List<GrupoCustoStatus> findByStatusACTIAndCLOSAndREINAndININ(final String siglaActive, final String siglaClosed,
                                                                 final String siglaRequestInact,
                                                                 final String siglaIntegInactivation);

    List<GrupoCustoStatus> findByStatusRECRAndINCR(final String siglaRequestCreation, final String siglaIntegCreation);

    List<GrupoCustoStatus> findByStatusINAC(final String siglaInactive);

    List<GrupoCustoStatus> findBySiglaInList(final List<String> siglaInactive);
}
