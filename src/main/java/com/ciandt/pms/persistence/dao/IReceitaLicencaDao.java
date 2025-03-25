package com.ciandt.pms.persistence.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.*;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.vo.FormFilter;

/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Osvaldo</a>
 * @since 18/08/2014
 */
@Repository
public interface IReceitaLicencaDao extends IAbstractDao<Long, ReceitaLicenca> {

	/**
	 * Busca uma {@link ReceitaLicenca} por todos os seus atributos.
	 * 
	 * @return Lista de {@link ReceitaLicenca}
	 */
	List<ReceitaLicenca> findByDataMesAndContratoPratica(final Date dataMes,
			final ContratoPratica contratoPratica);

	List<ReceitaLicenca> findByCodigoPaiReceitaLicenca(
			final Long codigoPaiReceitaLicenca);

	List<ReceitaLicenca> findByFormFilter(final ReceitaLicenca receitaLicenca,
			final Cliente cliente, final CentroLucro centroLucro,
			final NaturezaCentroLucro natureza);

	/**
	 * Obtem as receitas integraveis de acordo com o filtro informado.
	 * 
	 * @param filter
	 *            {@link FormFilter}
	 * @return lista de {@link ReceitaLicenca}
	 */
	List<ReceitaLicenca> findIntegrableRevenueByFormFilter(
			final FormFilter filter);

	/**
	 * Executa a integracao entre a {@link ReceitaLicenca} e o ERP (Mega).
	 * 
	 * @param receitaLicenca
	 *            - {@link ReceitaLicenca}l a ser integrada ao ERP.
	 * 
	 * @return retorna o status da execucao da integracao. Se retorno menor que
	 *         zero erro, caso contrario sucesso.
	 */
	Integer integrate(final ReceitaLicenca receitaLicenca);

	Boolean updateStatusReceitaLicenca(StatusReceitaLicenca statusReceitaLicenca);

	/**
	 * Busca se o pedido foi cancelado no ERP.
	 * 
	 * @param receitaLicenca
	 *            entidade ReceitaDealFiscal.
	 * 
	 * 
	 * @return retorna true se cancelado ou nao existe, caso contrario false.
	 *         Retorna null caso ocorra uma excetion ao executar a consulta.
	 * @throws SQLException
	 * @throws HibernateException
	 */
	Boolean isErpOrderCanceled(final ReceitaLicenca receitaLicenca)
			throws HibernateException, SQLException;

}