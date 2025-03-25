package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IPriceTableStatusService;
import com.ciandt.pms.model.PriceTableStatus;
import com.ciandt.pms.persistence.dao.IPriceTableStatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceTableStatusService implements IPriceTableStatusService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IPriceTableStatusDao dao;

    @Override
    public List<PriceTableStatus> findPriceTableStatusAll() {
        return dao.findPriceTableStatusAll();
    }

    public PriceTableStatus findPriceTableStatusById(final Long id) {
        return dao.findById(id);
    }

    public PriceTableStatus findByAcronym(final String acronym) {
        return dao.findByAcronym(acronym);
    }

    public List<PriceTableStatus> findPriceTableStatus(final List<String> acronyms){
        return dao.findPriceTableStatus(acronyms);
    }

}
