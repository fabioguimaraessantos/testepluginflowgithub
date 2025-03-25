package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.DiasUteisCidadeBase;
import com.ciandt.pms.persistence.dao.IDiasUteisCidadeBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.List;

/**
 * Created by josef on 01/11/2017.
 */
@Repository
public class DiasUteisCidadeBaseDao extends AbstractDao<Long, DiasUteisCidadeBase> implements IDiasUteisCidadeBaseDao {

    /**
            * Construtor padrão.
            *
            * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public DiasUteisCidadeBaseDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, DiasUteisCidadeBase.class);
    }


    @SuppressWarnings("unchecked")
    public Long findByCidadeBaseAndMonth(final Long codigoCidadeBase, final Date dataMes ) {
        List<DiasUteisCidadeBase> result = getJpaTemplate().findByNamedQuery(DiasUteisCidadeBase.FIND_BY_CIDADE_BASE_MONTH, new Object[] {codigoCidadeBase, dataMes});

        if(result.isEmpty()){
            return 0L;
        }

        return result.get(0).getQuantidadeDiasUteis();
    }
}
