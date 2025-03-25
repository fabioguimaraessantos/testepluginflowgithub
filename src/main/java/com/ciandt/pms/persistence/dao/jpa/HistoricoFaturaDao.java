package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.HistoricoFatura;
import com.ciandt.pms.persistence.dao.IHistoricoFaturaDao;


/**
 * 
 * A classe HistoricoFaturaDao proporciona as funcionalidades de acesso ao banco
 * de dados referente a entidade HistoricoFatura.
 * 
 * @since 10/05/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class HistoricoFaturaDao extends AbstractDao<Long, HistoricoFatura>
        implements IHistoricoFaturaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public HistoricoFaturaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, HistoricoFatura.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<HistoricoFatura> findByCodigoFatura(Long codigoFatura) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoFatura", codigoFatura);

		List<HistoricoFatura> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(HistoricoFatura.FIND_BY_CODIGO_FATURA,
						params);

		return results;
    }
}