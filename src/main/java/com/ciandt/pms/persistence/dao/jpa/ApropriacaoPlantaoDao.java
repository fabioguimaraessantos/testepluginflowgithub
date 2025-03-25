package com.ciandt.pms.persistence.dao.jpa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.ApropriacaoPlantao;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IApropriacaoPlantaoDao;

/**
 * 
 * A classe ApropriacaoPlantaoDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade {@link ApropriacaoPlantao}.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 22/10/2013
 */
@Repository
public class ApropriacaoPlantaoDao extends
		AbstractDao<Long, ApropriacaoPlantao> implements IApropriacaoPlantaoDao {

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			BundleUtil.getBundle(Constants.DEFAULT_DATE_PATTERN_MONTH_YEAR));

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public ApropriacaoPlantaoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ApropriacaoPlantao.class);
	}

	/**
	 * Busca todas as entidades ativas.
	 * 
	 * @return retorna uma lista de Area.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ApropriacaoPlantao> findAll() {
		return getJpaTemplate().findByNamedQuery(ApropriacaoPlantao.FIND_ALL);
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean exists(final ApropriacaoPlantao apropriacaoPlantao) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoLogin", apropriacaoPlantao.getPessoa()
				.getCodigoLogin());
		params.put("dataMes", apropriacaoPlantao.getDataMes());

		List<ApropriacaoPlantao> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ApropriacaoPlantao.EXISTS_APROPRIACAO_PLANTAO, params);

		if (results.isEmpty()) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IApropriacaoPlantaoService#
	 * findApropriacaoPlantaoByData(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<ApropriacaoPlantao> findApropriacaoPlantaoByData(
			final Date dataFechamento) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataFechamento", sdf.format(dataFechamento));

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				ApropriacaoPlantao.FIND_BY_DATA, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IApropriacaoPlantaoService#
	 * findApropriacaoPlantaoByDataAndCdPessoa(java.util.Date,
	 * com.ciandt.pms.model.Pessoa)
	 */
	@SuppressWarnings("unchecked")
	public List<ApropriacaoPlantao> findApropriacaoPlantaoByDataAndCdPessoa(
			final Date dataFechamento, final Pessoa pessoa) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataFechamento", sdf.format(dataFechamento));
		params.put("codigoPessoa", pessoa.getCodigoPessoa());

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				ApropriacaoPlantao.FIND_BY_DATA_E_PESSOA, params);
	}
}