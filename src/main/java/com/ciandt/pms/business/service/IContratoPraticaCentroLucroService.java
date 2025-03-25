package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.vo.NaturezaContratoPraticaCLRow;

/**
 * 
 * A classe IContratoPraticaCentroLucroService proporciona a interface de acesso
 * a camada de servi�o referente a entidade ContratoPraticaCentroLucro.
 * 
 * @since 18/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IContratoPraticaCentroLucroService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return true se salvo com sucesso, caso contrario false.
	 */
	@Transactional
	Boolean createCPCL(final ContratoPraticaCentroLucro entity) throws MoreThanOneActiveEntityException;

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que ser� atualizada.
	 * 
	 */
	@Transactional
	void updateCPCL(final ContratoPraticaCentroLucro entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que ser� apagada.
	 * 
	 * @return true se removido com sucesso, caso contrario false.
	 */
	@Transactional
	Boolean removeCPCL(final ContratoPraticaCentroLucro entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ContratoPraticaCentroLucro findCPCLById(final Long id);

	/**
	 * Busca uma lista de entidades pelo ContratoPratica.
	 * 
	 * @param cp
	 *            entidade populada com os valores do ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do ContratoPratica.
	 */
	List<ContratoPraticaCentroLucro> findCPCLByContratoPratica(
			final ContratoPratica cp);

	/**
	 * Busca uma lista de entidades pelo ContratoPratica e NaturezaCentroLucro
	 * opcionais (diferente de obrigatorias).
	 * 
	 * @param cp
	 *            entidade populada com os valores do ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do ContratoPratica.
	 */
	List<ContratoPraticaCentroLucro> findCPCLByContratoPraticaAndNaturezaOptional(
			final ContratoPratica cp);

	/**
	 * Busca pelo ContratoPratica e pela Natureza.
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado na busca.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro utilizado na busca.
	 * 
	 * @return lista com todas as entidades ContratoPraticaCentroLucro
	 *         relacionadas com o ContratoPratica e Natureza
	 */
	List<ContratoPraticaCentroLucro> findCPCLByContratoPraticaAndNatureza(
			final ContratoPratica cp, final NaturezaCentroLucro natureza);

	/**
	 * Cria uma Lista de NaturezaContratoPraticaCLRow utilizada no view do
	 * ContratoPratica, referente a listagem dos ContratoPraticaCentroLucro.
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado para montar a lista
	 * 
	 * @return lista de NaturezaContratoPraticaCLRow referente ao
	 *         contrato-pratica passado por parametro
	 */
	List<NaturezaContratoPraticaCLRow> prepareNaturezaContratoPraticaCLList(
			final ContratoPratica cp);

	/**
	 * Cria uma Lista de NaturezaContratoPraticaCLRow utilizada na aba Profit
	 * Center do ContratoPratica, referente a listagem dos
	 * ContratoPraticaCentroLucro. <br>
	 * Gera 3 listas: <br>
	 * 1 - lista de NaturezaContratoPraticaCLRow com as NaturezaCentroLucro
	 * Obrigatorias <br>
	 * 2 - lista de NaturezaCentroLucro nao obrigatorias (opcionais) <br>
	 * 3 - lista de ContratoPraticaCentroLucro das NaturezaCentroLucro nao
	 * obrigatorias
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado para montar a lista
	 * 
	 * @return mapa com as 3 listas referente ao contrato-pratica passado por
	 *         parametro
	 */
	Map<Integer, List> prepareNaturezaCPCLListMandatory(final ContratoPratica cp);

	/**
	 * Busca pelo ContratoPratica e pela Natureza e pela Data de vigencia.
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado na busca.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro utilizado na busca.
	 * 
	 * @param dataMes
	 *            - data da vigencia.
	 * 
	 * @return a entidade que atende aos criterios do filtro
	 */
	ContratoPraticaCentroLucro findCPCLByContPratAndNatAndDate(
			final ContratoPratica cp, final NaturezaCentroLucro natureza,
			final Date dataMes);

	/**
	 * Busca o {@link ContratoPraticaCentroLucro} vigente pelo ContratoPratica e
	 * pela Natureza.
	 * 
	 * @param cp
	 *            - ContratoPratica utilizado na busca.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro utilizado na busca.
	 * 
	 * @return ContratoPraticaCentroLucro vigente relacionadas com o
	 *         ContratoPratica e Natureza
	 * @throws MoreThanOneActiveEntityException
	 */
	ContratoPraticaCentroLucro findPresentByContratoPraticaAndNatureza(
			final ContratoPratica contratoPratica,
			final NaturezaCentroLucro natureza);

	/**
	 * Checa se um {@link ContratoPratica} possui mais do que um CentroLucro
	 * vigente.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica}
	 * @param natureza
	 *            {@link NaturezaCentroLucro}
	 * @throws MoreThanOneActiveEntityException
	 */
	void checkMoreThanOneCentroLucroPresent(
			final ContratoPratica contratoPratica,
			final NaturezaCentroLucro natureza)
			throws MoreThanOneActiveEntityException;

}