package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.*;


/**
 * 
 * A classe IAlocacaoPeriodoService proporciona a interface para acesso ao
 * servicos relacionados a entidade AlocacaoPeriodoService.
 * 
 * @since 19/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IAlocacaoPeriodoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createAlocacaoPeriodo(final AlocacaoPeriodo entity);

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateAlocacaoPeriodo(final AlocacaoPeriodo entity);

    /**
     * Remove a entidade passado por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    @Transactional
    void removeAlocacaoPeriodo(final AlocacaoPeriodo entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    AlocacaoPeriodo findAlocacaoPeriodoById(final Long id);

    /**
     * Busca a maior data referente ao mapa de alocacao.
     * 
     * @param mapa
     *            que se deseja obter a maior data de alocação
     * 
     * @return retorna a marior data
     */
    Date findMaxDateByMapa(final MapaAlocacao mapa);

    /**
     * Busca a menor data referente ao mapa de alocacao.
     * 
     * @param mapa
     *            que se deseja obter a menor data de alocação
     * 
     * @return retorna a menor data
     */
    Date findMinDateByMapa(final MapaAlocacao mapa);

    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param alocacao
     *            Alocacao utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna a AlocacaoPeriodo.
     */
    AlocacaoPeriodo findAlocacaoPeriodoByAlocacaoAndDate(
            final Alocacao alocacao, final Date dataMes);

    /**
     * Busca pela Recurso e Data Mês.
     * 
     * @param recurso
     *            Recurso utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    List<AlocacaoPeriodo> findAlocacaoPeriodoByRecursoAndDate(
            final Recurso recurso, final Date dataMes);

    /**
     * Deleta as alocações periodos, que não estão dentro do periodo passado por
     * aprametro.
     * 
     * @param mapa
     *            MapaAlocacao que se deseja deletar as AlocacaoPeriodo.
     * 
     */
    @Transactional
    void deleteAlocacaoPeriodoByMapaAndNotInRange(final MapaAlocacao mapa);

    /**
     * Busca as alocações periodos de uma alocacao.
     * 
     * @param alocacao
     *            - entidade do tipo Alocacao.
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    @Transactional
    List<AlocacaoPeriodo> findAlocacaoPeriodoByAlocacao(final Alocacao alocacao);

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
    List<AlocacaoPeriodo> findAlocPerByMaxDateAndRecurso(final Recurso recurso,
            final Date dataMes);

    /**
     * Busca pela Alocacao e periodo.
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
    List<AlocacaoPeriodo> findAlocPerByAlocacaoAndPeriod(
            final Alocacao alocacao, final Date startDate, final Date endDate);

    /**
     * Busca pela Pessoa e MapaAlocacao
     * 
     * @param pessoa
     *            Pessoa utilizada na busca
     * @param mapaAlocacao
     *            Escopo da busca
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    List<AlocacaoPeriodo> findByPessoaAndMapaAlocacao(Pessoa pessoa, MapaAlocacao mapaAlocacao);

    /**
     * Busca pela Pessoa e MapaAlocacao
     * 
     * @param recurso
     *            Recurso utilizada na busca
     * @param mapaAlocacao
     *            Escopo da busca
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
	List<AlocacaoPeriodo> findByRecursoAndMapaAlocacao(Recurso recurso,
			MapaAlocacao mapaAlocacao);

    /**
     * Busca por alocacões maior que zero para um determinado Clob, Recurso e Mes
     *
     * @param codigoContratoPratica  - Long  codigoContratoPratica.
     * @param codigoRecurso  - Long  codigoRecurso.
     * @param dataMes  - Date  dataMes.
     *
     * @return True ou False caso encontre algum registro.
     */
    Boolean findAlocacaoPeriodoGreaterZeroByContratoPraticaAndDateAndRecurso(final Long codigoContratoPratica, final Long codigoRecurso, final Date dataMes);

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