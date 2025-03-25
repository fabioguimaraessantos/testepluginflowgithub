package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.CotacaoMoedaMediaAud;
import com.ciandt.pms.persistence.dao.ICotacaoMoedaMediaAudDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CotacaoMoedaMediaAudDao implements ICotacaoMoedaMediaAudDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(CotacaoMoedaMediaAud CotacaoMoedaMediaAud) {
        entityManager.persist(CotacaoMoedaMediaAud);
    }
}
