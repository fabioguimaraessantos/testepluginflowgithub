package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.ciandt.pms.model.*;

/**
 * 
 * A classe IAlocacaoPeriodo proporciona a interface para a 
 * camada de persistencia para a entidade AlocacaoPeriodo.
 *
 * @since 18/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IAlocacaoPeriodoDao extends IAbstractDao<Long, AlocacaoPeriodo> {

    /**
     * Busca a maior data referente ao mapa de alocacao.
     * 
     * @param mapa que se deseja obter a maior data de alocação
     * 
     * @return retorna a marior data
     */
    Date findMaxDateByMapa(MapaAlocacao mapa);

    /**
     * Busca por alocacões maior que zero para um determinado Clob, Recurso e Mes
     *
     * @param codigoContratoPratica  - Long  codigoContratoPratica.
     * @param codigoRecurso  - Long  codigoRecurso.
     * @param dataMes  - Date  dataMes.
     *
     * @return True ou False caso encontre algum registro.
     */
    List<AlocacaoPeriodo> findAlocacaoPeriodoGreaterZeroByContratoPraticaAndDateAndRecurso(final Long codigoContratoPratica, final Long codigoRecurso, final Date dataMes);


    /**
     * Busca a menor data referente ao mapa de alocacao.
     *  
     * @param mapa que se deseja obter a menor data de alocação 
     *  
     * @return retorna a menor data
     */
    Date findMinDateByMapa(MapaAlocacao mapa);
    
    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param alocacao
     *          Alocacao utilizado na busca.
     * @param dataMes
     *          Data referente ao mes da alocação.         
     * 
     * @return retorna a AlocacaoPeriodo.
     */
    AlocacaoPeriodo findByAlocacaoAndDate(final Alocacao alocacao, final Date dataMes);
    
    /**
     * Busca pelo Recurso e Data Mês.
     * 
     * @param recurso
     *          Recurso utilizado na busca.
     * @param dataMes
     *          Data referente ao mes da alocação.         
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    List<AlocacaoPeriodo> findByRecursoAndDate(final Recurso recurso, final Date dataMes);
    
    /**
     * Busca as alocações periodos, que não estão dentro 
     * do periodo passado por aprametro.
     * 
     * @param mapa
     *          MapaAlocacao que se deseja deletar as AlocacaoPeriodo.
     * @param startDate
     *          Data referente ao inicio do periodo.
     * @param endDate
     *          Data referente ao fim do periodo.
     *                
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    List<AlocacaoPeriodo> findByMapaAndNotInRange(
            final MapaAlocacao mapa, final Date startDate, final Date endDate);
    
    /**
     * Busca as alocações periodos de uma alocacao.
     * 
     * @param alocacao - entidade do tipo Alocacao.
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    List<AlocacaoPeriodo> findByAlocacao(final Alocacao alocacao);
    
    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param recurso
     *            Recurso utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    List<AlocacaoPeriodo> findByMaxDateAndRecurso(
            final Recurso recurso, final Date dataMes);
    
    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param alocacao
     *            Alucacao utilizado na busca.
     * @param startDate
     *            Data inicio do periodo.
     * @param endDate
     *            Data fim do periodo.
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    List<AlocacaoPeriodo> findByAlocacaoAndPeriod(
            final Alocacao alocacao, final Date startDate, final Date endDate);

	List<AlocacaoPeriodo> findByMapaAlocacaoAndRecurso(
			MapaAlocacao mapaAlocacao, Recurso recurso);

    /**
     * Busca pelo Perfil Vendido apos a Closing Date do Mapa de Alocacoes.
     *
     * @param perfilVendido
     *            PerfilVendido utilizado na busca.
     * @param closingMapDate
     *            Data de fechamendo para o Modulo de Alocacoes.
     *
     * @return retorna uma lista AlocacaoPeriodo.
     */
    List<AlocacaoPeriodo> findByPerfilVendidoAndClosingDate (
        final PerfilVendido perfilVendido, final Date closingMapDate);

    List<AlocacaoPeriodo> findByResourceCodeInAndMonth(final Set<Long> resourceCodes, final Date month);
}
