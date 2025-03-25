package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.message.dto.CostCenterDTO;
import com.ciandt.pms.model.Convergencia;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoAud;
import com.ciandt.pms.model.GrupoCustoCentroLucro;
import com.ciandt.pms.model.GrupoCustoPeriodo;
import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.vo.NaturezaRow;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 
 * A classe IGrupoCustoService proporciona a interface de acesso da camada de
 * servico referente a entidade.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IGrupoCustoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * @param naturezaRowList
	 *            lista com as naturezas
	 * @param startDate
	 *            data inicio da vigencia
	 */
	@Transactional
	void createGrupoCusto(final GrupoCusto entity,
			final List<NaturezaRow> naturezaRowList, final Date startDate)
			throws IntegrityConstraintException;

	/**
	 * Cria um {@link GrupoCusto}.
	 * 
	 * @param grupoCusto
	 * @param clucros 
	 * @param startDate 
	 */
	@Transactional
	void create(final GrupoCusto grupoCusto)
			throws IntegrityConstraintException;

	/**
	 * Cria um CostCenter ({@link GrupoCusto}) juntamente com um registro de
	 * {@link Convergencia}.
	 * 
	 * @param centroCustoMega
	 *            {@link VwMegaCCusto}
	 * @return
	 * @throws Exception 
	 */
	GrupoCusto createCostCenter(final VwMegaCCusto centroCustoMega)
			throws ConstraintViolationException, IntegrityConstraintException, Exception;

	/**
	 * Prepara os dados para a criacao de um grupo de custo.
	 * 
	 * @return retorna uma lista com as naturezas.
	 */
	List<NaturezaRow> prepareCreateGrupoCusto();

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que ser� atualizada.
	 * @param mapCentroLucro
	 *            onde a chave representa a sigla da natureza e o valor a sua
	 *            entidade
	 * @throws IntegrityConstraintException
	 *             lan�a exce��o caso o GrupoCusto possua GrupoCustoPeriodo
	 *             associados e tente inativ�-lo
	 * 
	 */
	@Transactional
	void updateGrupoCusto(final GrupoCusto entity)
			throws IntegrityConstraintException;

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 * @throws IntegrityConstraintException
	 *             - tratamento erro de refer�ncia de integridade
	 * 
	 */
	@Transactional
	void removeGrupoCusto(final GrupoCusto entity)
			throws IntegrityConstraintException;

	/**
	 * Marca a entidade como deletada logicamente.
	 * 
	 * @param gcusto
	 *            que sera marcada como deletada logicamente.
	 * @throws IntegrityConstraintException
	 *             - tratamento erro de referencia de integridade
	 * 
	 */
	@Transactional(readOnly=false)
	void deleteLogico(final GrupoCusto gcusto)
			throws IntegrityConstraintException;

	/**
	 * Busca por todos os GrupoCusto.
	 * 
	 * @return uma lista com todos os ContratoPraticas.
	 */
	List<GrupoCusto> findGrupoCustoAllActive();

	/**
	 * Busca GrupoCusto Ativo Prod e Com.
	 *
	 * @return uma lista dos GrupoCusto.
	 */
	public List<GrupoCusto> findGrupoCustoActiveProdCom();

	/**
	 * Busca por todos os GrupoCusto.
	 *
	 * @return uma lista com todos os GrupoCusto.
	 */
	List<GrupoCusto> findGrupoCustoAll();


	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<GrupoCusto> findGrupoCustoByFilter(final GrupoCusto filter);


	List<GrupoCusto> findByAproverRescinded();

	/**
	 * Busca e entidade pelo id.
	 * 
	 * @param id
	 *            - id da entidade.
	 * 
	 * @return retorna a entidade com o id passado por parametro. Caso nao
	 *         exista retorna null.
	 */
	GrupoCusto findGrupoCustoById(final Long id);

	/**
	 * Busca e entidade pelo acronym.
	 * 
	 * @param siglaGrupoCusto
	 *            sigla do GrupoCusto
	 * 
	 * @return retorna a entidade
	 */
	GrupoCusto findGrupoCustoByAcronym(final String siglaGrupoCusto);

	/**
	 * Ordena uma lista de GrupoCustoPeriodo.
	 * 
	 * @param grupoCustoPeriodoList
	 *            - lista de GrupoCustoPeriodo
	 */
	void orderGrupoCustoPeriodoList(
			final List<GrupoCustoPeriodo> grupoCustoPeriodoList);

	/**
	 * Ordena uma lista de GrupoCustoCentroLucro.
	 * 
	 * @param grupoCustoCentroLucroList
	 *            - lista de GrupoCustoCentroLucro
	 */
	void orderGrupoCustoCentroLucroList(
			final List<GrupoCustoCentroLucro> grupoCustoCentroLucroList);

	/**
	 * Ordena a hierarquia de entidades (listas GrupoCustoPeriodo e suas
	 * respectivas listas GrupoCustoCentroLucro) de um GrupoCusto.
	 * 
	 * @param grupoCusto
	 *            - GrupoCusto com as listas a serem ordenadas
	 */
	void orderGrupoCustoHierarchyList(final GrupoCusto grupoCusto);

	/**
	 * Prepara os dados para a edicao de um GrupoCusto.
	 * 
	 * @param grupoCusto
	 *            - GrupoCusto que sera atualizado
	 */
	void prepareUpdateGrupoCusto(final GrupoCusto grupoCusto);

	void sendEmailToControladoria(String subject, String message);

	/**
	 * Encontra grupo de custo por id e trazendo consigo seus respectivos
	 * periodos
	 *
	 * @param codigoGrupoCusto
	 * @return grupo de custo
	 */
	GrupoCusto findGrupoCustoByIdWithPeriodos(Long codigoGrupoCusto);

	/**
	 * Busca por todos os GrupoCusto ativos pela sigla da estrutura
	 * organizacional.
	 * 
	 * @param name
	 * @return lista de grupo de custos
	 */
	List<GrupoCusto> findGrupoCustoAllActiveAndEstrOrgan(
			String sgEstruturaOrganizacional);

    /**
	 * Busca por todos os GrupoCusto ativos.
	 * 
	 * @return uma lista com todos os GrupoCusto ativos mas s� o codigo e nome
	 *         est�o preenchidos.
	 */
	List<GrupoCusto> findAllActiveReturnCodigoAndNomeGrupoCusto();

	/**
	 * Busca por todos os GrupoCusto na api do Mega.
	 *
	 * @return uma lista com todos os GrupoCusto
	 */
	List<CostCenterDTO> findAllWithExternalRestRequest();


	List<GrupoCustoAud> findHistoryByCodigo(Long codigoGrupoCusto);


	void checkGrupoCustoAreaOrcamentaria(GrupoCusto entity);

   Long findTipoAreaByCentroCusto(Long grupoCustoId);

    List<GrupoCusto> findCostCentersByCostCenterHierarchy(final Long code) throws BusinessException;
}
