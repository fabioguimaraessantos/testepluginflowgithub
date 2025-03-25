package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IGrupoCustoStatusService;
import com.ciandt.pms.model.GrupoCustoStatus;
import com.ciandt.pms.persistence.dao.IGrupoCustoStatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoCustoStatusService implements IGrupoCustoStatusService {

    @Autowired
    private IGrupoCustoStatusDao grupoCustoStatusDao;

    @Override
    public GrupoCustoStatus findBySiglaStatusGrupoCusto(final String siglaStatusGrupoCusto) {
        return grupoCustoStatusDao.findBySiglaStatusGrupoCusto(siglaStatusGrupoCusto);
    }

    public List<GrupoCustoStatus> findAllActive() {
        return grupoCustoStatusDao.findAllActive();
    }

    @Override
    public List<GrupoCustoStatus> findByStatusActiveAndRequestInac(String siglaActive, String siglaRequestInact) {
        return grupoCustoStatusDao.findByStatusActiveAndRequestInac(siglaActive, siglaRequestInact);
    }

    public List<GrupoCustoStatus> findByStatusACTIAndCLOSAndREINAndININ(final String siglaActive, final String siglaClosed,
                                                                        final String siglaRequestInact,
                                                                        final String siglaIntegInactivation) {
        return grupoCustoStatusDao.findByStatusACTIAndCLOSAndREINAndININ(siglaActive, siglaClosed,
                siglaRequestInact, siglaIntegInactivation);
    }

    @Override
    public List<GrupoCustoStatus> findByStatusRECRAndINCR(String siglaRequestCreation, String siglaIntegCreation) {
        return grupoCustoStatusDao.findByStatusRECRAndINCR(siglaRequestCreation, siglaIntegCreation);
    }

    @Override
    public List<GrupoCustoStatus> findByStatusINAC(String siglaInactive) {
        return grupoCustoStatusDao.findByStatusINAC(siglaInactive);
    }

    @Override
    public List<GrupoCustoStatus> findBySiglaInList(List<String> listSigla) {
        return grupoCustoStatusDao.findBySiglaInList(listSigla);
    }
}
