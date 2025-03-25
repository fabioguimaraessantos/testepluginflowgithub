/**
 * Interface da camada de DAO da entidade Receita
 */
package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Receita;


/**
 * 
 * A classe IReceitaDao proporciona a interface de acesso para a camada DAO da
 * entidade Receita.
 * 
 * @since 28/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IReceitaDao extends IAbstractDao<Long, Receita> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Receita> findByFilter(final Receita filter);
    Long findByRevenueDealFiscal(final Long revenueDealFiscalCode);

    /**
     * Busca uma lista de entidades pelo filtro. Também carrega asentidades
     * relacionadas.
     * 
     * @param filter
     *            entidade populada com os valores do filtro e carrega.
     * @param cli
     *            entidade do tipo Cliente.
     * @param cl
     *            entidade do tipo CentroLucro
     * @param natureza
     *            entidade do tipo NaturezaCentroLucro
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Receita> findByFilter(final Receita filter, final Cliente cli,
            final CentroLucro cl, final NaturezaCentroLucro natureza);

    /**
     * Busca uma lista de entidades pelo filtro, porém com status Published e
     * Integrated.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Receita> findAllNoWorkingByFilter(final Receita filter);

    /**
     * Busca uma lista de entidades nao fechadas, status diferente de Published
     * e Integrated.
     * 
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Receita> findAllNotClosed(final Date dataMes);
    
    /**
     * Busca uma todas as receitas de um determinado mês.
     * 
     * @param monthDate - data mês de referencia.
     * 
     * @return lista de receitas do mês passado por parametro.
     */
    List<Receita> findByMonthDate(final Date monthDate);
    
    /**
     * 
     * @param cpcl ContratoPraticaCentroLucro
     * @param cl Centro Lucro
     * @return Lista com filtro
     */
    List<Receita> findByCpclAndCentroLucro(final CentroLucro cl);
    
	/**
	 * Consulta receita dos ultimos x meses por contrato pratica
	 * 
	 * @param meses
	 *            Numero de meses que será usado na busca para retorar as
	 *            receitas que possuem data maior que (data atual - meses)
	 * @param codigoContratoPratica
	 *            Código do contrato prática
	 * @return Lista de {@link Receita}
	 */
	List<Receita> findUltimasReceitasByCP(Long meses, Long codigoContratoPratica);

    List<Receita> findAllReceitaForecastByCodigoContratoPratica(Long codigoContratoPratica);

    Boolean updateStatusReceita(Long revenueCode, String revenueStatus);

    List<Receita> findNotIntegratedRevenueAfterClosingRevenueDate (Long codigoDealFiscal, Date closingRevenueDate);

    /**
     * @param msa Msa to find Revenues through c-lob
     * @param startDate  Price Table Start Date
     * @param endDate  Price Table End Date
     * @return List - Receita - Revenue List filter by params
     */
    List<Receita> findRevenuesByMsaAndDates(Long msa, Date startDate, Date endDate);
}