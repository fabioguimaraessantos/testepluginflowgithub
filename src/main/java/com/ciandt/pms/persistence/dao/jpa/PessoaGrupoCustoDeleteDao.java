package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.PessoaGrupoCustoDelete;
import com.ciandt.pms.persistence.dao.IPessoaGrupoCustoDeleteDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;

@Repository
public class PessoaGrupoCustoDeleteDao extends AbstractDao<Long, PessoaGrupoCustoDelete>
        implements IPessoaGrupoCustoDeleteDao {

    public PessoaGrupoCustoDeleteDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, PessoaGrupoCustoDelete.class);
    }

    @Override
    public void createTicketAssociation(Long deletedCode, String ticketId) {
        PessoaGrupoCustoDelete deleted = new PessoaGrupoCustoDelete();
        deleted.setDeletedCode(deletedCode);
        deleted.setTicketId(ticketId);
        create(deleted);
    }
}