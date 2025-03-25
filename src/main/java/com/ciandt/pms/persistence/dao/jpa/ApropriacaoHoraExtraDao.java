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
import com.ciandt.pms.model.ApropriacaoHoraExtra;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IApropriacaoHoraExtraDao;

/**
 * 
 * A classe ApropriacaoHoraExtraDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade {@link ApropriacaoHoraExtra}.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 22/10/2013
 */
@Repository
public class ApropriacaoHoraExtraDao extends
		AbstractDao<Long, ApropriacaoHoraExtra> implements
		IApropriacaoHoraExtraDao {

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			BundleUtil.getBundle(Constants.DEFAULT_DATE_PATTERN_MONTH_YEAR));

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public ApropriacaoHoraExtraDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ApropriacaoHoraExtra.class);
	}

	/**
	 * Busca todas as entidades ativas.
	 * 
	 * @return retorna uma lista de Area.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ApropriacaoHoraExtra> findAll() {
		return getJpaTemplate().findByNamedQuery(ApropriacaoHoraExtra.FIND_ALL);
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
	public boolean exists(final ApropriacaoHoraExtra apropriacaoHoraExtra) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoLogin", apropriacaoHoraExtra.getPessoa()
				.getCodigoLogin());
		params.put("dataMes", apropriacaoHoraExtra.getDataMes());

		List<ApropriacaoHoraExtra> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ApropriacaoHoraExtra.EXISTS_APROPRIACAO_HORA_EXTRA,
						params);

		if (results.isEmpty()) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.persistence.dao.IApropriacaoHoraExtraDao#
	 * findApropriacaoHoraExtraByData(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<ApropriacaoHoraExtra> findApropriacaoHoraExtraByData(
			final Date dataFechamento) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataFechamento", sdf.format(dataFechamento));

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				ApropriacaoHoraExtra.FIND_BY_DATA, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.persistence.dao.IApropriacaoHoraExtraDao#
	 * findApropriacaoHoraExtraByDataAndCdPessoa(java.util.Date,
	 * com.ciandt.pms.model.Pessoa)
	 */
	@SuppressWarnings("unchecked")
	public List<ApropriacaoHoraExtra> findApropriacaoHoraExtraByDataAndCdPessoa(
			final Date dataFechamento, final Pessoa pessoa) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataFechamento", sdf.format(dataFechamento));
		params.put("codigoPessoa", pessoa.getCodigoPessoa());

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				ApropriacaoHoraExtra.FIND_BY_DATA_E_PESSOA, params);
	}
}