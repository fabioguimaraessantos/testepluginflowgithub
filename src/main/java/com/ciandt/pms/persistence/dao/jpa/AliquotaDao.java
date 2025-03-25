package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Aliquota;
import com.ciandt.pms.model.Imposto;
import com.ciandt.pms.persistence.dao.IAliquotaDao;


/**
 * 
 * A classe AliquotaDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade Imposto.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Repository
public class AliquotaDao extends AbstractDao<Long, Aliquota> implements
        IAliquotaDao {

    /**
     * Contrutor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public AliquotaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Aliquota.class);
    }

    /**
     * Busca por todos os ContratoPraticas.
     * 
     * @return uma lista com todos os ContratoPraticas.
     */
    @SuppressWarnings("unchecked")
    public List<Aliquota> findAll() {

        List<Aliquota> listResult = getJpaTemplate().findByNamedQuery(
                Aliquota.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo imposto.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    @SuppressWarnings("unchecked")
    public List<Aliquota> findByImposto(final Imposto imposto) {

        List<Aliquota> listResult = getJpaTemplate().findByNamedQuery(
                Aliquota.FIND_BY_IMPOSTO,
                new Object[] {imposto.getCodigoImposto() });
        return listResult;

    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    @SuppressWarnings("unchecked")
    public Aliquota findMaxStartDateByImposto(final Imposto imposto) {

        List<Aliquota> listResult = getJpaTemplate().findByNamedQuery(
                Aliquota.FIND_BY_MAX_START_DATE_BY_IMPOSTO,
                new Object[] {imposto.getCodigoImposto(),
                        imposto.getCodigoImposto() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);

    }

    /**
     * Busca a entidade na data atual.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de esntidades que atendem ao criterio do imposto.
     */
    @SuppressWarnings("unchecked")
    public Aliquota findByImpostoByCurrentDate(final Imposto imposto) {

        List<Aliquota> listResult = getJpaTemplate().findByNamedQuery(
                Aliquota.FIND_BY_IMPOSTO_BY_CURRENT_DATE,
                new Object[] {imposto.getCodigoImposto() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);

    }
}
