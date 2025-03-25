package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.persistence.dao.IDreProcessoDao;

/**
 * A classe DreProcessoDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade DreProcesso.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Repository
public class DreProcessoDao extends AbstractDao<Long, DreProcesso> implements
		IDreProcessoDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - fabrica do entity manager.
	 */
	@Autowired
	public DreProcessoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, DreProcesso.class);
	}

	/**
	 * Retorna o último processo executado.
	 * 
	 * @param codigoProcesso
	 *            Código do processo
	 * @param monthYear
	 *            MM/yyyy
	 * @param indicadorPorLogin
	 *            - <li>S - Processo executado para um mes/ano, especificando
	 *            login</li><li>N - Processo executado para um mes/ano, sem
	 *            especificar um login</li>
	 * @return {@link DreProcesso}
	 */
	@SuppressWarnings("unchecked")
	public DreProcesso findLastByProcessoDataAndIndPorLogin(
			final Long codigoProcesso, final String monthYear,
			final String indicadorPorLogin) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoProcesso", codigoProcesso);
		params.put("monthYear", monthYear);
		params.put("indicadorPorLogin", indicadorPorLogin);

		List<DreProcesso> result = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						DreProcesso.FIND_lAST_BY_PROCESSO_DATA_AND_IND_POR_LOGIN,
						params);

		if (!result.isEmpty()) {
			return (DreProcesso) result.get(0);
		}
		return null;
	}

	/**
	 * Retorna todos DreProcesso com um Dremes
	 * 
	 * @param dreMes
	 *            dreMes
	 * @return Lista de {@link DreProcesso}
	 */
	@SuppressWarnings("unchecked")
	public List<DreProcesso> findAllByDreMes(final DreMes dreMes) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoDreMes", dreMes.getCodigoDreMes());

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				DreProcesso.FIND_ALL_BY_DRE_MES, params);
	}
}
