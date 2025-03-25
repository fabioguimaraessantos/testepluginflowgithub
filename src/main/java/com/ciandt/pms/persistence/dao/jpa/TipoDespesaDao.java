package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.TipoDespesa;
import com.ciandt.pms.persistence.dao.ITipoDespesaDao;


/**
 * 
 * A classe TipoDespesaDao proporciona as funcionalidades da camada de
 * persistencia referwente a entidade TipoDespesa.
 * 
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class TipoDespesaDao extends AbstractDao<Long, TipoDespesa> implements
        ITipoDespesaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public TipoDespesaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TipoDespesa.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    @SuppressWarnings("unchecked")
    public List<TipoDespesa> findAllActive() {
        List<TipoDespesa> listResult = getJpaTemplate().findByNamedQuery(
                TipoDespesa.FIND_ALL_ACTIVE);

        return listResult;
    }

    /**
     * Busca por Nome.
     * 
     * @param nomeTipoDespesa
     *            nome do TipoDespesa
     * 
     * @return retorna um tipoDespesa.
     */
    @SuppressWarnings("unchecked")
    public TipoDespesa findByName(final String nomeTipoDespesa) {
        List<TipoDespesa> listResult = getJpaTemplate().findByNamedQuery(
                TipoDespesa.FIND_BY_NAME, new Object[] {nomeTipoDespesa });

        if (!listResult.isEmpty()) {
            return listResult.get(0);
        }
        return null;
    }
}
