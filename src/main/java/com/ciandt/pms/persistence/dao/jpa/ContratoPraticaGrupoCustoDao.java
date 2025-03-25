package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPraticaGrupoCusto;
import com.ciandt.pms.persistence.dao.IContratoPraticaGrupoCustoDao;


/**
 * A classe ContratoPraticaGrupoCustoDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ContratoPraticaGrupoCusto.
 *
 * @since 11/03/2015
 * @author <a href="mailto:luizsj@ciandt.com">Alisson Fernando da Silva</a>
 */
@Repository
public class ContratoPraticaGrupoCustoDao extends AbstractDao<Long, ContratoPraticaGrupoCusto>
        implements IContratoPraticaGrupoCustoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public ContratoPraticaGrupoCustoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ContratoPraticaGrupoCusto.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ContratoPraticaGrupoCusto> findAll() {
        List<ContratoPraticaGrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
        		ContratoPraticaGrupoCusto.FIND_ALL);

        return listResult;
    }

    @Override
    @SuppressWarnings("unchecked")
	public List<ContratoPraticaGrupoCusto> findByContratopraticaId(final Long codigoContratoPratica) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoContratoPratica", codigoContratoPratica);

		return getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ContratoPraticaGrupoCusto.FIND_BY_CONTRATO_PRATICA_ID,
						params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ContratoPraticaGrupoCusto findLastByContratoPraticaId(final Long codigoContratoPratica) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoContratoPratica", codigoContratoPratica);

		List<ContratoPraticaGrupoCusto> contratoPraticaGrupoCustos = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ContratoPraticaGrupoCusto.FIND_LAST_MAX_DATA_INICIO_VIGENCIA_CONTRATO_PRATICA_ID,
						params);

		if (contratoPraticaGrupoCustos == null || contratoPraticaGrupoCustos.isEmpty()) {
			return null;
		}

		return contratoPraticaGrupoCustos.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ContratoPraticaGrupoCusto findByContratoPraticaIdAndDataFimVigencia(final Long contratoPraticaId, final Date dataFimVigencia) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contratoPraticaId", contratoPraticaId);
		params.put("dataFimVigencia", dataFimVigencia);

		List<ContratoPraticaGrupoCusto> contratoPraticaGrupoCustos = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ContratoPraticaGrupoCusto.FIND_BY_CONTRATO_PRATICA_AND_DATA_FIM_VIGENCIA,
						params);
		// TODO: exceção quanto tiver mais de um resultado?
		// TODO: handle more than one result -> MoreThanOneActiveEntityException

		return contratoPraticaGrupoCustos.get(0);
    }
}