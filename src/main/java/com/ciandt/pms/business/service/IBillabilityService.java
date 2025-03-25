package com.ciandt.pms.business.service;

import java.util.List;

import com.ciandt.pms.model.Billability;
import org.springframework.stereotype.Service;

/**
 *
 * A classe IBillabilityService proporciona a interface de acesso a camada de serviço
 * referente a entidade Billability.
 *
 */
@Service
public interface IBillabilityService {

    /**
     * Retorna todas as Billabilities ativas.
     *
     * @return retorna uma lista com todas as Billabilities ativas.
     */
    List<Billability> findAllActive();

    Billability findByName(String name);

}