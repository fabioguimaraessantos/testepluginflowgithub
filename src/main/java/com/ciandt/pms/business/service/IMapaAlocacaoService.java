/**
 * 
 */
package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.FatorUrMes;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.VwPmsFinancials;
import com.ciandt.pms.model.vo.AlocacaoCell;
import com.ciandt.pms.model.vo.AlocacaoContratoPraticaRow;
import com.ciandt.pms.model.vo.AlocacaoRow;
import com.ciandt.pms.model.vo.MapDashboardRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

/**
 * 
 * A classe IMapaAlocacaoService proporciona a interface de acesso para a camada
 * de servi�o referente a entidade MapaAlocacao.
 * 
 * @since 12/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IMapaAlocacaoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createMapaAlocacao(final MapaAlocacao entity);

	/**
	 * Atualiza a entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 */
	@Transactional
	void updateMapaAlocacao(final MapaAlocacao entity);

	/**
	 * Realiza o merge da entidade.
	 * 
	 * @param entity
	 *            que sera sofrer� o merge
	 * 
	 * @return a entidade atualizada
	 */
	MapaAlocacao mergeMapaAlocacao(final MapaAlocacao entity);

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 * 
	 * @return retorna true se removido com sucesso, caso contrario false
	 */
	@Transactional
	Boolean removeMapaAlocacao(final MapaAlocacao entity);

	/**
	 * Busca o Mapa pelo contratoPratica que possuem a vers�o igual a
	 * 'Published'.
	 * 
	 * @param cp
	 *            - Entidade ContratoPratica.
	 * 
	 * @return retorna o MapaAlocacao.
	 */
	MapaAlocacao findMapaAlocacaoByContratoPratica(final ContratoPratica cp);

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	MapaAlocacao findMapaAlocacaoById(final Long entityId);

	/**
	 * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente
	 * as entidades relacionadas a esta.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<MapaAlocacao> findMapaAlocacaoByFilter(final MapaAlocacao filter);

	/**
	 * Busca uma lista de entidades pelo filtro. Tamb�m carrega asentidades
	 * relacionadas.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro e carrega.
	 * @param cli
	 *            entidade Cliente
	 * @param msa
	 *            entidade Msa
	 * @param natureza
	 *            entidade NaturezaCentroLucro
	 * @param centroLucro
	 *            entidade CentroLucro
	 * @param isFollowingOnly
	 *            - flag somente MapaAlocacao que a Pessoa corrente est�
	 *            seguindo.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<MapaAlocacao> findMapaAlocacaoByFilterFetch(final MapaAlocacao filter,
			final Cliente cli, final Msa msa,
			final NaturezaCentroLucro natureza, final CentroLucro centroLucro,
			final Boolean isFollowingOnly);

	/**
	 * Busca uma lista de entidades pelo filtro. Tamb�m carrega as entidades
	 * relacionadas.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro e carrega.
	 * @param cli
	 *            entidade Cliente
	 * @param monthDate
	 *            data para filtro
	 * @param natureza
	 *            NaturezaCentroLucro
	 * @param centroLucro
	 *            CentroLucro
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<MapaAlocacao> findMapaAlocacaoByFilter(final MapaAlocacao filter,
			final Cliente cli, final Date monthDate,
			final NaturezaCentroLucro natureza, final CentroLucro centroLucro);

	/**
	 * Busca uma lista de entidades pelo filtro. Tamb�m carrega as entidades
	 * relacionadas.
	 * 
	 * @param monthDate
	 *            data para filtro
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<MapaAlocacao> findMapaAlocacaoByDate(final Date monthDate);

	/**
	 * Busca uma lista de entidades pelo login(username) que possui o 'lock' do
	 * mapa de aloca��o.
	 * 
	 * @param username
	 *            login do usu�rio desejado.
	 * 
	 * @return lista de MapaAlocacao.
	 */
	List<MapaAlocacao> findByLoginTrava(final String username);

	/**
	 * Prepara a matriz de Alocacao X AlocacaoPeriodo para cria��o do
	 * MapaAlocaao.
	 * 
	 * @param perfilVendidoSet
	 *            - lista de PerfilVendido do ContratoPratica selecionado
	 * @param validityDateList
	 *            - lista de datas do periodo de vigencia ordenado
	 * @return lista de AlocacaoRow - matriz de Alocacao X AlocacaoPeriodo
	 */
	List<AlocacaoRow> prepareCreateMatrixAlocacao(
			final Set<PerfilVendido> perfilVendidoSet,
			final List<Date> validityDateList);

	/**
	 * Prepara a matriz de Alocacao X AlocacaoPeriodo para edi��o do
	 * MapaAlocaao.
	 * 
	 * @param alocacaoList
	 *            - lista de Alocacao do MapaAlocacao corrente
	 * @param validityDateList
	 *            - lista de datas do periodo de vigencia ordenado
	 * @param mapaAlocacao
	 *            - entidade do tipo MapaAlocacao
	 * 
	 * @return lista de AlocacaoRow - matriz de Alocacao X AlocacaoPeriodo
	 */
	List<AlocacaoRow> prepareUpdateMatrixAlocacao(
			final List<Alocacao> alocacaoList,
			final List<Date> validityDateList, final MapaAlocacao mapaAlocacao);

	/**
	 * Persiste as altera��es realizada nas aloca��es vindas da tela.
	 * 
	 * @param alocacaoRowList
	 *            - matriz de Alocacao X AlocacaoPeriodo
	 * @return retorna true se salvo com sucesso, caso contrario false
	 */
	Boolean updateMatrixAlocacao(final List<AlocacaoRow> alocacaoRowList);

	/**
	 * Desbloqueia o MapaAlocacao.
	 * 
	 * @param mapa
	 *            - MapaAlocacao para realizar o desbloqueio
	 * @return - true se possui permissao para realizar a opera��o, caso
	 *         contrario false
	 */
	@Transactional
	Boolean unlockMapaAlocacao(final MapaAlocacao mapa);

	/**
	 * Bloqueia o MapaAlocacao.
	 * 
	 * @param mapa
	 *            - MapaAlocacao para realizar o desbloqueio
	 * @return - true se possui permissao para realizar a opera��o, caso
	 *         contrario false
	 */
	@Transactional
	Boolean lockMapaAlocacao(final MapaAlocacao mapa);

	/**
	 * Adiciona uma Alocacao na matriz para ser criada posteriormente no banco.
	 * 
	 * @param alocacao
	 *            - Alocacao a ser adicionada na matriz
	 * @param alocacaoRowList
	 *            - lista de Alocacao X AlocacaoPeriodo
	 * @param validityDateList
	 *            - lista de datas do periodo de vigencia ordenado
	 * @param percentualAlocacao
	 *            - percentual de alocacao do recurso para a vigencia
	 * @return true se Alocacao for v�lida e se foi adicionada corretamente na
	 *         matriz, caso contr�rio retorna false
	 */
	@Transactional
	Boolean addAlocacaoMatrix(final Alocacao alocacao,
			final List<AlocacaoRow> alocacaoRowList,
			final List<Date> validityDateList,
			final BigDecimal percentualAlocacao);

	/**
	 * Edita uma Alocacao na matriz para ser atualizada posteriormente no banco.
	 * 
	 * @param alocacaoNew
	 *            - Alocacao a ser editada na matriz
	 * @param alocacaoRowList
	 *            - lista de Alocacao X AlocacaoPeriodo
	 */
	void editAlocacaoMatrix(final Alocacao alocacaoNew,
			final List<AlocacaoRow> alocacaoRowList);

	/**
	 * Marca a Alocacao deletada da matriz para ser removida posteriormente do
	 * banco.
	 * 
	 * @param alocacaoOld
	 *            - Alocacao a ser excluida da matriz
	 * @param alocacaoRowList
	 *            - lista de Alocacao X AlocacaoPeriodo
	 */
	void deleteAlocacaoMatrix(final Alocacao alocacaoOld,
			final List<AlocacaoRow> alocacaoRowList);

	/**
	 * Recupera os dados de um mapa (Aloca��o, PeriodoAlocac��o, MapaAloca��o) e
	 * retorna uma lista que possui a estrutura para exibir na tela.
	 * 
	 * @param mapa
	 *            que se deseja carregar os dados
	 * @return retotna uma lista com a estrutura que sera exibida na tela.
	 */
	List<AlocacaoRow> loadMapaAlocacaoManageView(final MapaAlocacao mapa);

	/**
	 * Verifica se o MapaAlocacao que est� sendo criado � v�lido. Se n�o existir
	 * um MapaAlocacao com o ContratoPratica e Vers�o passado por par�metro, o
	 * MapaAlocacao � v�lido. Caso contr�rio � inv�lido.
	 * 
	 * @param codigoContratoPratica
	 *            - c�digo do ContratoPratica
	 * @param indicadorVersao
	 *            - vers�o do MapaAlocacao
	 * @return true se o MapaAlocacao for v�lido, false se j� existir um
	 *         MapaAlocacao cadastrado com o ContratoPratica e Vers�o passados
	 *         por par�metro
	 */
	Boolean isValidAllocationMapVersion(final Long codigoContratoPratica,
			final String indicadorVersao);

	/**
	 * Realiza a c�pia de um MapaAlocacao de uma vers�o para outra passada por
	 * parametro.
	 * 
	 * @param mapaAlocacao
	 *            referencia do MapaAlocacao que ser� copiado.
	 * @param typeVersion
	 *            vers�o para realizar a c�pia
	 * 
	 * @return c�pia do MapaAlocacao
	 */
	@Transactional
	MapaAlocacao saveAs(final MapaAlocacao mapaAlocacao,
			final String typeVersion);

	/**
	 * Atualiza os valores do U.R. das Alocacao selecionadas.
	 * 
	 * @param alocacaoRowList
	 *            - matriz de de Alocacao do MapaAlocacao
	 * @param updateUr
	 *            - valor a ser atualizado do U.R. na lista de Alocacao
	 */
	void updateIncomeUr(final List<AlocacaoRow> alocacaoRowList,
			final BigDecimal updateUr);

	/**
	 * Atualiza os valores do FTE das AlocacaoPeriodo das Alocacao selecionadas.
	 * 
	 * @param alocacaoRowList
	 *            - matriz de de Alocacao do MapaAlocacao
	 * @param updateFte
	 *            - valor a ser atualizado do FTE na lista de Alocacao
	 */
	void updateMatrixIncomeFte(final List<AlocacaoRow> alocacaoRowList,
			final BigDecimal updateFte);

	/**
	 * Atualiza os valores do FTE das AlocacaoPeriodo das Alocacao selecionadas.
	 * 
	 * @param alocacao
	 *            - Alocacao corrente
	 * @param alocacaoCellList
	 *            - lista de AlocacaoPeriodo da Alocacao corrente
	 * @param updateFte
	 *            - valor a ser atualizado do FTE na lista de Alocacao
	 */
	void updateLineIncomeFte(final Alocacao alocacao,
			final List<AlocacaoCell> alocacaoCellList,
			final BigDecimal updateFte);

	/**
	 * Busca todas as aloca��es referente a um recurso existente na alocacao
	 * passado por parametro. Returna uma lista com todas as aloca��es e
	 * periodos das aloca��es.
	 * 
	 * @param alocacao
	 *            - alocacao que se deseja buscar o recurso.
	 * @param dateList
	 *            - lista com todos as datas que se deseja saber o percentual de
	 *            alocacao
	 * @return - retorna uma lista de AlocacaoContratoPraticaRow com todas as
	 *         aloca��es.
	 */
	List<AlocacaoContratoPraticaRow> findMapaAlocacaoAllByRecurso(
			final Alocacao alocacao, final List<Date> dateList);

	/**
	 * Exibir toda a matriz (atribuir true para o atributo isView da
	 * AlocacaoRow).
	 * 
	 * @param alocacaoRowList
	 *            - matriz de Alocacao X AlocacaoPeriodo
	 */
	void showFullMatrixAlocacao(final List<AlocacaoRow> alocacaoRowList);

	/**
	 * Gera os valores do dashboard do MapaAlocacao.
	 * 
	 * @param contratoPratica
	 *            - {@link ContratoPratica} base
	 * @param dateList
	 *            - lista de meses do MapaAlocacao
	 * 
	 * @return - retorna uma lista de {@link VwPmsFinancials}
	 */
	SortedMap<Date, MapDashboardRow> genereteMapaAlocacaoDashboard(
			final ContratoPratica contratoPratica, final List<Date> dateList);

	/**
	 * Busca uma lista de entidades pelo por todos os mapas que est�o com lock.
	 * 
	 * @return lista de MapaAlocacao.
	 */
	List<MapaAlocacao> findMapaAlocacaoAllLock();

	/**
	 * Pega a data de fechamento do MapaAlocacao.
	 * 
	 * @return retorna a Data de Fechamento.
	 */
	Date getClosingDateMapaAlocacao();

	/**
	 * Busca uma lista de entidades pelo por todos os mapas que possuem a vers�o
	 * igual a 'Published'.
	 * 
	 * @return lista de MapaAlocacao.
	 */
	List<MapaAlocacao> findMapaAlocaoAllPublished();

	/**
	 * Gera a lista de UR por Mes do mapa de aloca��o.
	 * 
	 * @param mapa
	 *            - MapaAlocacao em quest�o.
	 * 
	 */
	@Transactional
	void updateUrMonthList(final MapaAlocacao mapa);

	/**
	 * Realiza a valida��o da vigencia.
	 * 
	 * @param validityMonthBeg
	 *            - mes inicial da vigencia
	 * @param validityYearBeg
	 *            - ano inicial da vigencia
	 * @param validityMonthEnd
	 *            - mes final da vigencia
	 * @param validityYearEnd
	 *            - ano final da vigencia
	 * @param mapa
	 *            - Mapa de aloca��o em quest�o.
	 * 
	 * @return retorna true se o per�odo for v�lido, caso contrario false.
	 */
	Boolean validateAlocationMapPeriod(final MapaAlocacao mapa,
			final String validityMonthBeg, final String validityYearBeg,
			final String validityMonthEnd, final String validityYearEnd);

	/**
	 * Busca uma lista de entidades pelo filtro. Tamb�m carrega asentidades
	 * relacionadas.
	 * 
	 * @param cli
	 *            entidade Cliente
	 * @param natureza
	 *            entidade NaturezaCentroLucro
	 * @param centroLucro
	 *            entidade CentroLucro
	 * @param dataMes
	 *            entidade do tipo Date, referente a data-mes
	 * @param pratica
	 *            do tipo Pratica.
	 * 
	 * @param statusReceita
	 *            status da receita
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<MapaAlocacao> findMapaAlocacaoByFilterWithoutRevenue(
			final Cliente cli, final NaturezaCentroLucro natureza,
			final CentroLucro centroLucro, final Pratica pratica,
			final Date dataMes, final String statusReceita);

	/**
	 * Private retorna a data final da janela de exibi��o.
	 * 
	 * @param mapa
	 *            - Mapaalocacao
	 * 
	 * @return retorna a data final da janale de exibi��o.
	 */
	Date getMapaAlocacaoEndDateWindow(final MapaAlocacao mapa);

	/**
	 * Realiza o update da viagencia do mapa de aloca��o.
	 * 
	 * @param mapa
	 *            - entidade do tipo MapaAlocacao
	 * @param startDate
	 *            - Data inicia da nova vigencia
	 * @param endDate
	 *            - Data final da nova vigencia
	 */
	@Transactional
	void updateMapaAlocacaoPeriod(final MapaAlocacao mapa,
			final Date startDate, final Date endDate);

	/**
	 * Cria a lista de FatorUrMes.
	 * 
	 * @param mapaAlocacao
	 *            - entidade do tipo MapaAlocacao
	 * 
	 * @return retorna a lista de FatorUrMes criada
	 */
	List<FatorUrMes> createUrMonthList(final MapaAlocacao mapaAlocacao);

	/**
	 * Metodo que a ajusta a data inicio da janela do mapa de aloa��o.
	 * 
	 * @param mapa
	 *            - Mapaalocacao que se deseja ajustar a janela.
	 */
	void ajustaDataInicioJanela(final MapaAlocacao mapa);

	/**
	 * Remove todas as aloca��es selecionadas.
	 * 
	 * @param rowList
	 *            - lista de aloca��es.
	 */
	@Transactional
	void removeAlocacao(final List<AlocacaoRow> rowList);

	/**
	 * Realiza o update de um aloca��o periodo.
	 * 
	 * @param ap
	 *            - entidade do tipo AlocacaoPerido
	 */
	@Transactional
	void updateAlocacaoPeriodo(final AlocacaoPeriodo ap);

	/**
	 * Realiza o update do fator UR mes.
	 * 
	 * @param fator
	 *            - entidade a ser atualizada.
	 */
	@Transactional
	void updateFatorUrMes(final FatorUrMes fator);

	/**
	 * Move a janela do mapa de aloca��o.
	 * 
	 * @param mapaAlocacao
	 *            - entidade MapaAlocacao
	 * @param numberMonthsToMove
	 *            - N�mero de meses que a janela vai mover
	 */
	@Transactional
	void moveMapaAlocacaoWindow(final MapaAlocacao mapaAlocacao,
			final Integer numberMonthsToMove);

	/**
	 * Busca uma lista de mapas que possuem a vers�o 'Published' no range de
	 * meses: previousDate at� infinito.
	 * 
	 * @param startDate
	 *            - data de in�cio do range do follow
	 * 
	 * @return lista de MapaAlocacao.
	 */
	List<MapaAlocacao> findMapaAlocAllPBByRangeMonths(final Date startDate);

	/**
	 * Valida��o do ClosingDateMapaAlocacao. Caso startDate (data de in�cio da
	 * vig�ncia) seja maior do que a data Revenue, a vig�ncia � v�lida e pode
	 * ser adicionada ou removida. Caso contr�rio, a vig�ncia n�o pode ser
	 * adicionada nem removida.
	 * 
	 * @param startDate
	 *            - data de in�cio da vig�ncia
	 * @param showMsgError
	 *            - mostra mensagem de erro caso startDate n�o seja v�lido
	 * 
	 * @return true se startDate for maior do que ClosingDateMapaAlocacao. false
	 *         caso contr�rio
	 */
	Boolean verifyClosingDateMapaAlocacao(final Date startDate,
			final Boolean showMsgError);

	/**
	 * Obtem uma data (01/MM/YYYY) log� ap�s o closinDate.
	 * 
	 * @return date
	 */
	Date getDateAfterClosingDate();

	Boolean isPessoaAllocatedOnMapaAlocacaoAtDate(Pessoa pessoa,
			MapaAlocacao mapaAlocacao, Date date);

	void atualizaSufixosMapaAlocacao(ContratoPratica contratoPratica);
}