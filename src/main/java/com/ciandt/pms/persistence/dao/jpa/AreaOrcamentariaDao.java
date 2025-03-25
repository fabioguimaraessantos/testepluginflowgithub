package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.persistence.dao.IAreaOrcamentariaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.*;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @since 11/08/2015
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Repository
public class AreaOrcamentariaDao extends AbstractDao<Long, AreaOrcamentaria>
        implements IAreaOrcamentariaDao {

    private EntityManager entityManager;

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public AreaOrcamentariaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, AreaOrcamentaria.class);
        entityManager = factory.createEntityManager();
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<AreaOrcamentaria> findAll() {
        List<AreaOrcamentaria> listResult = getJpaTemplate().findByNamedQuery(
        		AreaOrcamentaria.FIND_ALL);

        return listResult;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AreaOrcamentaria> findAllActiveAreaOrcamentariaPai() {
        List<AreaOrcamentaria> listResult = getJpaTemplate().findByNamedQuery(
        		AreaOrcamentaria.FIND_ALL_ACTIVE_AREA_ORCAMENTARIA_PAI);

        return listResult;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AreaOrcamentaria> findByAreaOrcamentariaPai(final Long codigoAreaOrcamentariaPai) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoAreaOrcamentariaPai", codigoAreaOrcamentariaPai);

		List<AreaOrcamentaria> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						AreaOrcamentaria.FIND_BY_AREA_ORCAMENTARIA_PAI,
						params);

		if (results.isEmpty()) {
			return null;
		}

		return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AreaOrcamentaria> findAllAreaOrcamentariaFilho() {
        List<AreaOrcamentaria> listResult = getJpaTemplate().findByNamedQuery(
                AreaOrcamentaria.FIND_ALL_AREA_ORCAMENTARIA_FILHO);

        return listResult;
    }

    @Override
    public List<AreaOrcamentaria> findByFilter(final AreaOrcamentaria filter) {
        List<AreaOrcamentaria> listResult = getJpaTemplate()
                .findByNamedQuery(
                        AreaOrcamentaria.FIND_BY_FILTER,
                        new Object[] {filter.getNomeAreaOrcamentaria(),
                                filter.getNomeAreaOrcamentaria(),
                                filter.getAreaOrcamentariaPai() != null ? filter.getAreaOrcamentariaPai().getNomeAreaOrcamentaria() : null,
                                filter.getAreaOrcamentariaPai() != null ? filter.getAreaOrcamentariaPai().getNomeAreaOrcamentaria() : null,
                                filter.getIndicadorStatus(),
                                filter.getIndicadorStatus() });
        return listResult;
    }
}