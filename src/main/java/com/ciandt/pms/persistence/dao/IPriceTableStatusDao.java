package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PriceTableStatus;

import java.util.List;

/**
 *
 * Define a interface do DAO da entidade.
 *
 * @since 15/08/2022
 * @author <a href="mailto:pricaldeira@ciandt.com">Priscilla Caldeira</a>
 *
 */
public interface IPriceTableStatusDao extends IAbstractDao <Long, PriceTableStatus> {

    /**
     * Retorna todas os PriceTableStatus.
     *
     * @return retorna uma lista com todos os PriceTableStatus.
     */
    List<PriceTableStatus> findPriceTableStatusAll();

    PriceTableStatus findByAcronym(String acronym);

    List<PriceTableStatus> findPriceTableStatus(final List<String> acronyms);

}
