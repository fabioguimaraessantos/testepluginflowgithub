package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ReceitaTipo;
import com.ciandt.pms.persistence.dao.IReceitaTipoDao;

/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/08/2014
 */
@Repository
public class ReceitaTipoDao extends AbstractDao<Long, ReceitaTipo>
        implements IReceitaTipoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public ReceitaTipoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ReceitaTipo.class);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<ReceitaTipo> findAll() {
		List<ReceitaTipo> receitaTipos = getJpaTemplate().findByNamedQuery(ReceitaTipo.FIND_ALL);

		return receitaTipos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReceitaTipo findByNomeReceitaTipo(String nomeReceitaTipo) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nomeReceitaTipo", nomeReceitaTipo);

		List<ReceitaTipo> receitaTipos = getJpaTemplate()
				.findByNamedQueryAndNamedParams(ReceitaTipo.FIND_BY_NOME_RECEITA_TIPO,
						params);

		if (receitaTipos == null || receitaTipos.isEmpty()) {
			return null;
		}

		return receitaTipos.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReceitaTipo findBySiglaReceitaTipo(String siglaReceitaTipo) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siglaReceitaTipo", siglaReceitaTipo);

		List<ReceitaTipo> receitaTipos = getJpaTemplate()
				.findByNamedQueryAndNamedParams(ReceitaTipo.FIND_BY_SIGLA_RECEITA_TIPO,
						params);

		if (receitaTipos == null) {
			return null;
		}

		return receitaTipos.get(0);
	}
}