package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.ciandt.pms.model.GrupoCustoAreaOrcamentariaAud;
import com.ciandt.pms.model.GrupoCustoPeriodoAud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoPeriodo;
import com.ciandt.pms.persistence.dao.IGrupoCustoPeriodoDao;


/**
 * 
 * A classe GrupoCustoPeriodoDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade GrupoCustoPeriodo.
 * 
 * @since 18/05/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class GrupoCustoPeriodoDao extends AbstractDao<Long, GrupoCustoPeriodo>
        implements IGrupoCustoPeriodoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica do entity manager
     */
    @Autowired
    public GrupoCustoPeriodoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, GrupoCustoPeriodo.class);
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param grupoCusto
     *            entidade populada com os valores do GrupoCusto.
     * 
     * @return lista de entidades que atendem ao criterio do GrupoCusto.
     */
    @SuppressWarnings("unchecked")
    public GrupoCustoPeriodo findMaxStartDateByGrupoCusto(
            final GrupoCusto grupoCusto) {

        List<GrupoCustoPeriodo> listResult = getJpaTemplate().findByNamedQuery(
                GrupoCustoPeriodo.FIND_MAX_START_DATE_BY_GRUPO_CUSTO,
                new Object[] {grupoCusto.getCodigoGrupoCusto(),
                        grupoCusto.getCodigoGrupoCusto() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GrupoCustoPeriodoAud> findHistoryByCodigo(Long codigoGrupoCusto){
        return getJpaTemplate().findByNamedQuery(GrupoCustoPeriodoAud.FIND_BY_CODIGO, codigoGrupoCusto);
    }
}
