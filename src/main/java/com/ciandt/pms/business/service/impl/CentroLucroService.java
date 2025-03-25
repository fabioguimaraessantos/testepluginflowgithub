/**
 * CentroLucroService - Classe da camada de Servico do Centro de Lucro 
 */
package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.persistence.dao.ICentroLucroDao;

/**
 * A classe CentroLucroService proporciona as funcionalidades de servico
 * referentes ao Centro de Lucro.
 * 
 * @since 04/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class CentroLucroService implements ICentroLucroService {

	/**
	 * Intancia do DAO de CentroLucro.
	 */
	@Autowired
	private ICentroLucroDao dao;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	public void createCentroLucro(final CentroLucro entity) {
		dao.create(entity);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<CentroLucro> findCentroLucroAll() {
		return dao.findAll();
	}

	/**
	 * Busca uma lista de entidades pelo filtro. Carrega previamente a entidade
	 * naturezaCentroLucro
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<CentroLucro> findCentroLucroByFilterFetch(
			final CentroLucro filter) {
		return dao.findByFilterFetch(filter);
	}

	/**
	 * Busca uma lista de entidades pelo filtro. Não carrega previamente a
	 * entidade naturezaCentroLucro, somente no momento do acesso
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<CentroLucro> findCentroLucroByFilter(final CentroLucro filter) {
		return dao.findByFilter(filter);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public CentroLucro findCentroLucroById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @throws IntegrityConstraintException
	 *             lança exceção caso o CentroLucro possua ContratoPratica
	 *             associados e tente inativá-la
	 */
	@Transactional
	public void removeCentroLucro(final CentroLucro entity)
			throws IntegrityConstraintException {
		// verifica se existem CentroLucro relacionados, se sim lança exceção
		if (entity.getContratoPraticas().size() > 0) {
			throw new IntegrityConstraintException(
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE);
		}
		dao.remove(entity);
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	@Transactional
	public void updateCentroLucro(final CentroLucro entity) {
		dao.update(entity);
	}

	/**
	 * Retorna todas as entidades relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 * 
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<CentroLucro> findCentroLucroByNatureza(
			final NaturezaCentroLucro natureza) {

		return dao.findByNatureza(natureza);
	}
	
	/**
	 * Retorna todas as entidades ativas relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 * 
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<CentroLucro> findActiveByNatureza(final NaturezaCentroLucro natureza) {
		
		return dao.findActiveByNatureza(natureza);
	}

	/**
	 * Busca por dealfiscal e natureza.
	 * 
	 * @param df
	 *            the {@link DealFiscal}
	 * 
	 * @param codigoNatureza
	 *            codigo da natureza
	 * @param dataVigencia
	 *            data de vigencia
	 * @return listResult
	 */
	public List<CentroLucro> findByDealFiscalAndNatureza(final DealFiscal df,
			final Long codigoNatureza, final Date dataVigencia) {
		return dao
				.findByDealFiscalAndNatureza(df, codigoNatureza, dataVigencia);
	}


	/**
	 * Retorna o Name das entidades ativas relacionadas
	 *
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 *
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<String> findNameActiveByNatureza(final NaturezaCentroLucro natureza) {

		return dao.findNameActiveByNatureza(natureza);
	}


	/**
	 * Retorna o Name das entidades ativas relacionadas
	 *
	 * @param name
	 *
	 *
	 * @return entidade.
	 */
	@Override
	public List<CentroLucro> findCentroLucroBusinessManagerByName(final NaturezaCentroLucro natureza, String name) {

		return dao.findCentroLucroBusinessManagerByName(natureza,name);
	}


}
