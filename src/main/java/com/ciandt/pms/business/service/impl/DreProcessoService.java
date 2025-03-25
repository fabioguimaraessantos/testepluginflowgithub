package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IDreProcessoService;
import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.persistence.dao.IDreProcessoDao;
import com.ciandt.pms.util.DateUtil;

/**
 * A classe DreProcessoService proporciona as funcionalidades da camada de
 * serviço referente a entidade DreProcesso.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Service
public class DreProcessoService implements IDreProcessoService {

	/**
	 * Instancia do DAO da entidade.
	 */
	@Autowired
	private IDreProcessoDao dao;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	public void createDreProcesso(DreProcesso entity) {
		dao.create(entity);
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 */
	@Transactional
	public void updateDreProcesso(DreProcesso entity) {
		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 */
	@Transactional
	public void removeDreProcesso(DreProcesso entity) {
		dao.remove(entity);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public DreProcesso findDreProcessoById(Long id) {
		return dao.findById(id);
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
	public DreProcesso findLastByProcessoDataAndIndPorLogin(
			final Long codigoProcesso, final Date monthYear,
			final String indicadorPorLogin) {

		return dao.findLastByProcessoDataAndIndPorLogin(codigoProcesso,
				DateUtil.formatDate(monthYear,
						Constants.DEFAULT_DATE_PATTERN_MONTH_YEAR,
						Constants.DEFAULT_CALENDAR_LOCALE), indicadorPorLogin);
	}

	/**
	 * Retorna todos DreProcesso com um Dremes
	 * 
	 * @param dreMes
	 *            dreMes
	 * @return Lista de {@link DreProcesso}
	 */
	public List<DreProcesso> findAllDreProcessByDreMes(final DreMes dreMes) {
		return dao.findAllByDreMes(dreMes);
	}
}
