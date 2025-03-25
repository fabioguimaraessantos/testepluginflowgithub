package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.VwPmsIntegReceitaNacional;
import com.ciandt.pms.persistence.dao.IVwPmsIntegReceitaNacionalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class VwPmsIntegReceitaNacionalDao extends AbstractDao<Long, VwPmsIntegReceitaNacional> implements IVwPmsIntegReceitaNacionalDao {

    /**
     * Construtor.
     *
     * @param factory
     *            facotry
     */
    @Autowired
    public VwPmsIntegReceitaNacionalDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, VwPmsIntegReceitaNacional.class);
    }

    public List<VwPmsIntegReceitaNacional> findAll(){
        List<VwPmsIntegReceitaNacional> listResult = getJpaTemplate().findByNamedQuery(
                VwPmsIntegReceitaNacional.FIND_ALL);

        return listResult;
    }
}
