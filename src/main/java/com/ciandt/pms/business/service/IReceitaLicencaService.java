package com.ciandt.pms.business.service;

import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.FormFilter;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/08/2014
 */
@Service
public interface IReceitaLicencaService {

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	ReceitaLicenca findById(final Long entityId);

	/**
	 * Remove a entidade passada por parametro.
	 * 
	 * @param entity
	 */
	void remove(final ReceitaLicenca entity);

	/**
	 * Atualiza a entidade passada por paramentro.
	 * 
	 * @param entity
	 */
	void update(final ReceitaLicenca entity);

	void updateStatusReceitaLicenca(ReceitaLicenca receitaLicenca, StatusReceitaLicenca statusReceitaLicenca);

	/**
	 * Busca uma {@link ReceitaLicenca} por todos os seus atributos.
	 * 
	 * @return Lista de {@link ReceitaLicenca}
	 */
	List<ReceitaLicenca> findByDataMesAndContratoPratica(final Date dataMes,
			final ContratoPratica contratoPratica);

	/**
	 * Busca todas {@link ReceitaLicenca} pelo {@code codigoPaiReceitaLicenca}.
	 * 
	 * @return lista de {@link ReceitaLicenca}
	 */
	List<ReceitaLicenca> findByCodigoPaiReceitaLicenca(
			final Long codigoPaiReceitaLicenca);

	List<ReceitaLicenca> createReceitaLicenca(ReceitaLicenca receitaLicenca,
			Integer installments, Double totalValue);

	List<ReceitaLicenca> findByFormFilter(final ReceitaLicenca receitaLicenca,
			final Cliente cliente, final CentroLucro centroLucro,
			final NaturezaCentroLucro natureza);

	/**
	 * Retorna o percentual de imposto para a {@code receitaLicenca}
	 * 
	 * @param receitaLicenca
	 * @return
	 */
	BigDecimal getTaxaImpostoForReceitaLicenca(
			final ReceitaLicenca receitaLicenca);

	/**
	 * Obtem as receitas integraveis de acordo com o filtro informado.
	 * 
	 * @param filter
	 *            {@link FormFilter}
	 * @return lista de {@link ReceitaLicenca}
	 */
	List<ReceitaLicenca> findIntegrableRevenueByFilter(final FormFilter filter);

	/**
	 * Executa a integracao entre a {@link ReceitaLicenca} e o ERP (Mega).
	 * 
	 * @param receitaLicenca
	 *            - {@link ReceitaLicenca}l a ser integrada ao ERP.
	 * 
	 * @return retorna o status da execucao da integracao.
	 */
	Boolean integrate(final ReceitaLicenca receitaLicenca);

	/**
	 * Realiza a reintegracao da {@link ReceitaLicenca}.
	 * 
	 * @param receitaLicenca
	 *            entidade {@link ReceitaLicenca}.
	 * @throws SQLException
	 * @throws HibernateException
	 */
	void reintegrate(final ReceitaLicenca receitaLicenca)
			throws HibernateException, SQLException;

	/**
	 * Remove uma {@link ReceitaLicenca} lógicamente.
	 * @param entity
	 */
	void removeLogically(final ReceitaLicenca entity);

	void updateLicenseRevenueFromMega(Long revenueCode, String revenueStatus, BigDecimal megaOrderID, String errorMessage);

}