package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PessoaGrupoCustoDelete;
import org.springframework.stereotype.Repository;

@Repository
public interface IPessoaGrupoCustoDeleteDao extends IAbstractDao<Long, PessoaGrupoCustoDelete> {

    void createTicketAssociation(Long removedId, String ticketId);
}