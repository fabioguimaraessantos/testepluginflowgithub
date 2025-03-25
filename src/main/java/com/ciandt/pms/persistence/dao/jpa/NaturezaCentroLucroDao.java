package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.enums.NaturezaCentroLucroSigla;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.persistence.dao.INaturezaCentroLucroDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 01/08/2009
 */
@Repository
public class NaturezaCentroLucroDao extends
        AbstractDao<Long, NaturezaCentroLucro> implements
        INaturezaCentroLucroDao {

    /**
     * Construtor padr�o.
     * 
     * @param factory
     *            Entidade do tipo NaturezaCentroLucro
     */
    @Autowired
    public NaturezaCentroLucroDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, NaturezaCentroLucro.class);
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
    public List<NaturezaCentroLucro> findByFilter(
            final NaturezaCentroLucro filter) {
        List<NaturezaCentroLucro> listResult = getJpaTemplate()
                .findByNamedQuery(
                        NaturezaCentroLucro.FIND_BY_FILTER,
                        new Object[] {filter.getNomeNatureza(),
                                filter.getNomeNatureza(),
                                filter.getIndicadorAtivo(),
                                filter.getIndicadorAtivo(),
                                filter.getIndicadorTipo(),
                                filter.getIndicadorTipo() });
        return listResult;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<NaturezaCentroLucro> findAll() {
        List<NaturezaCentroLucro> listResult = getJpaTemplate()
                .findByNamedQuery(NaturezaCentroLucro.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param codigoGcPeriodo
     *            - codigo do GrupoCustoPeriodo
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<NaturezaCentroLucro> findAllNotInGrupoCusto(
            final Long codigoGcPeriodo) {
        List<NaturezaCentroLucro> listResult = getJpaTemplate()
                .findByNamedQuery(
                        NaturezaCentroLucro.FIND_ALL_NOT_IN_GRUPO_CUSTO,
                        new Object[] {codigoGcPeriodo });
        return listResult;
    }

	/**
	 * Busca uma {@link NaturezaCentroLucro} por uma determinada
	 * {@link NaturezaCentroLucroSigla}.
	 * 
	 * @param sigla
	 *            {@link NaturezaCentroLucroSigla}
	 * @return lista de {@link NaturezaCentroLucro}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public NaturezaCentroLucro findBySigla(final NaturezaCentroLucroSigla sigla) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sigla", sigla.getSigla());

		List<NaturezaCentroLucro> naturezaCentroLucros = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						NaturezaCentroLucro.FIND_BY_SIGLA, params);

		if (naturezaCentroLucros.isEmpty())
			throw new EntityNotFoundException("No entity found with sigla "
					+ sigla.getSigla());

		return naturezaCentroLucros.get(0);
	}

}