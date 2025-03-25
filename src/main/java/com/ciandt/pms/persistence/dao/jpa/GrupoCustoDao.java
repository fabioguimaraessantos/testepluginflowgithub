package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoAud;
import com.ciandt.pms.persistence.dao.IGrupoCustoDao;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * A classe GrupoCustoDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade GrupoCusto.
 * 
 * @since 12/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class GrupoCustoDao extends AbstractDao<Long, GrupoCusto> implements
        IGrupoCustoDao {

    /**
     * Construtor padr�o.
     * 
     * @param factory
     *            - fabrica do entity manager
     */
    @Autowired
    public GrupoCustoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, GrupoCusto.class);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<GrupoCusto> findByFilter(final GrupoCusto filter) {
        List<GrupoCusto> listResult = getJpaTemplate()
                .findByNamedQuery(
                        GrupoCusto.FIND_BY_FILTER,
                        new Object[] {filter.getNomeGrupoCusto(),
                                filter.getNomeGrupoCusto(),
                                filter.getSiglaGrupoCusto(),
                                filter.getSiglaGrupoCusto(),
								filter.getGrupoCustoStatus().getSiglaStatusGrupoCusto(),
								filter.getGrupoCustoStatus().getSiglaStatusGrupoCusto()});
        return listResult;
    }

	public List<GrupoCusto> findByAproverRescinded() {
		return (List<GrupoCusto>) getJpaTemplate().findByNamedQuery(
				GrupoCusto.FIND_BY_APPROVER_RESCINDED);
	}

    /**
     * Busca e entidade pelo acronym.
     * 
     * @param siglaGrupoCusto
     *            sigla do GrupoCusto
     * 
     * @return retorna a entidade
     */
    @SuppressWarnings("unchecked")
    public GrupoCusto findByAcronym(final String siglaGrupoCusto) {
        List<GrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
                GrupoCusto.FIND_BY_ACRONYM, new Object[] {siglaGrupoCusto });
        if (!listResult.isEmpty()) {
            return listResult.get(0);
        }
        return null;
    }

    /**
     * Busca por todos os GrupoCusto ativos.
     * 
     * @return uma lista com todos os GrupoCusto ativos.
     */
    @SuppressWarnings("unchecked")
    public List<GrupoCusto> findAllActive() {
        List<GrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
                GrupoCusto.FIND_ALL_ACTIVE);

        return listResult;
    }


	/**
	 * Busca por todos os GrupoCusto ativos, do tipo Prod_ Com_.
	 *
	 * @return uma lista com os GrupoCusto ativos do tipo Prod e Com.
	 */
	public List<GrupoCusto> findActiveTypeProdCom() {
		return getJpaTemplate().findByNamedQuery(GrupoCusto.FIND_ACTIVE_PROD_COM);
	}

	/**
	 * Busca por todos os GrupoCusto.
	 *
	 * @return uma lista com todos os GrupoCusto.
	 */
	@SuppressWarnings("unchecked")
	public List<GrupoCusto> findAll() {
		List<GrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
				GrupoCusto.FIND_ALL);

		return listResult;
	}

    /**
	 * Busca por todos os GrupoCusto ativos.
	 * 
	 * @return uma lista com todos os GrupoCusto ativos mas s� o codigo e nome
	 *         est�o preenchidos.
	 */
    @Override
    @SuppressWarnings("unchecked")
    public List<GrupoCusto> findAllActiveReturnCodigoAndNomeGrupoCusto() {
    	List<GrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
    			GrupoCusto.FIND_ALL_ACTIVE_RETURN_CODIGO_AND_NOME_GRUPO_CUSTO);
    	
    	return listResult;
    }

	/* 
	 * @see com.ciandt.pms.persistence.dao.IGrupoCustoDao#findGrupoCustoByIdWithPeriodos(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public GrupoCusto findGrupoCustoByIdWithPeriodos(Long codigoGrupoCusto) {
		List<GrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
                GrupoCusto.FIND_GRUPOCUSTO_BY_ID_WITH_PERIODOS, new Object[] { codigoGrupoCusto });
		if(listResult.isEmpty()){
			return new GrupoCusto();
		} else {
			return listResult.get(0);
		}
	}

	/* 
	 * @see com.ciandt.pms.persistence.dao.IGrupoCustoDao#findGrupoCustoAllActiveAndEstrOrgan(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<GrupoCusto> findGrupoCustoAllActiveAndEstrOrgan(
			String sgEstruturaOrganizacional) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siglaArea", sgEstruturaOrganizacional);
		List<GrupoCusto> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						GrupoCusto.FIND_ALL_ACTIVE_BY_ESTR_ORG, map);
		return listResult;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GrupoCustoAud> findHistoryByCodigo(
			Long codigoGrupoCusto) {
		return getJpaTemplate().findByNamedQuery(GrupoCustoAud.FIND_BY_CODIGO, codigoGrupoCusto);
	}

	@Override
	public List<GrupoCusto> findByCostCenterHierarchy(final Long costCenterHierarchyCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("costCenterHierarchyCode", costCenterHierarchyCode);

		return getJpaTemplate().findByNamedQueryAndNamedParams(GrupoCusto.FIND_ALL_BY_COST_CENTER_HIERARCHY, params);
	}

}
