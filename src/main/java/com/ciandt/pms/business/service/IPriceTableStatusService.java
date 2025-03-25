package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.PriceTableStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Define a interface do Service da entidade.
 *
 * @since 10/08/2022
 * @author <a href="mailto:pricaldeira@ciandt.com">Priscilla Caldeira</a>
 *
 */
@Service
public interface IPriceTableStatusService {
    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    List<PriceTableStatus> findPriceTableStatusAll()  throws BusinessException;

    PriceTableStatus findPriceTableStatusById(Long priceTableStatusId)  throws BusinessException;

    PriceTableStatus findByAcronym(String acronym) throws BusinessException;

    List<PriceTableStatus> findPriceTableStatus(final List<String> acronyms) throws BusinessException;

}
