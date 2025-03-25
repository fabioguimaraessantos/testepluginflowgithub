package com.ciandt.pms.business.service.impl;

import java.util.List;

import com.ciandt.pms.business.service.IBillabilityService;
import com.ciandt.pms.model.Billability;
import com.ciandt.pms.persistence.dao.IBillabilityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * A classe BillabilityService proporciona as funcionalidades da
 * camada de negócio referente a entidade Billability.
 *
 */
@Service
public class BillabilityService implements IBillabilityService {

    /** Instancia do Dao da entidade Area. */
    @Autowired
    private IBillabilityDao billabilityDao;

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    public List<Billability> findAllActive() {
        return billabilityDao.findAllActive();
    }

    public Billability findByName(String name) { return billabilityDao.findByName(name); }

}