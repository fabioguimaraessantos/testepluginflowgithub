/**
 * IContratoPraticaService - Classe de interface
 */
package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.message.dto.ContractLobDTO;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaAud;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.UploadManagers;
import com.ciandt.pms.model.vo.MapaProspectComAlocacaoResultsetVO;
import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * A classe IContratoPraticaService proporciona interface para os servi�os
 * referentes a entidade ContratoPratica.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IContratoPraticaService {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findContratoPraticaAll();

	List<ContratoPratica> findContratoPraticaAllForComboBox();

	List<ContratoPratica> findAllCompleteForCombobox();

	/**
	 * Retorna todas as entidades com estado igual a 'Complete'.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findContratoPraticaAllComplete();


	/**
	 * Retorna todas as entidades com estado igual a 'Active'.
	 *
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findAllContratoPraticaActive();


	List<ContratoPratica> findByAproverRescinded();

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ContratoPratica findContratoPraticaById(final Long id);

	/**
	 * Busca o ContratoPratica por Contrato e Pratica.
	 * 
	 * @param idPratica
	 *            id da entidade pratica
	 * @param idContrato
	 *            id do entidade contrato
	 * 
	 * @return o ContratoPratica caso exista
	 * 
	 */
	ContratoPratica findContratoPraticaByPraticaAndContrato(
			final Long idPratica, final Long idContrato);

	/**
	 * Busca o ContratoPratica por Pratica.
	 *
	 * @param idPratica
	 *            id da entidade pratica
	 *
	 * @return o ContratoPratica caso exista
	 *
	 */
	List<ContratoPratica> findContratoPraticaByPratica(
			final Long idPratica);

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os ContratoPratica
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findContratoPraticaByCliente(final Cliente cliente);

	/**
	 * Insere uma entidade.
	 * 
	 * @param cp
	 *            entidade a ser inserida.
	 * 
	 * @throws IntegrityConstraintException
	 *             - Excecao indicando violacao de Constraint
	 */
	@Transactional
	void createContratoPratica(ContratoPratica cp)
			throws IntegrityConstraintException;

	/**
	 * Cria contrato pratico com sua respectiva convergencia a partir de um
	 * grupo de custo e de um contrato pratico
	 * 
	 * @param entity
	 * @param grupoCusto
	 * @throws IntegrityConstraintException
	 *             excecao lancada quando contrato pratico possui o nome ja
	 *             existente na base
	 */
	@Transactional(readOnly = false)
	void createContratoPraticaWithConvergencia(ContratoPratica entity,
											   GrupoCusto grupoCusto, List<ContratoPraticaCentroLucro> contratoPraticaCentroLucro) throws IntegrityConstraintException;

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que ser� atualizada.
	 * @throws MoreThanOneActiveEntityException
	 * 
	 */
	@Transactional
	void updateContratoPratica(final ContratoPratica entity)
			throws MoreThanOneActiveEntityException;

	/**
	 * Atualiza contrato pratico com sua respectiva convergencia a partir de um
	 * grupo de custo e de um contrato pratico
	 * 
	 * @param cp
	 * @param grupoCusto
	 * @throws MoreThanOneActiveEntityException
	 */
	@Transactional(readOnly = false)
	void updateContratoPraticaWithConvergencia(ContratoPratica cp,
			GrupoCusto grupoCusto) throws MoreThanOneActiveEntityException;

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que ser� apagada.
	 * 
	 * @return true se removido com sucesso, caso contrario retorna false.
	 * 
	 */
	@Transactional
	Boolean removeContratoPratica(final ContratoPratica entity);

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * @param cli
	 *            - entidade Cliente
	 * 
	 * @param natureza
	 *            - entidade NaturezaCentroLucro
	 * 
	 * @param cp
	 *            - entidade CentroLucro
	 * @param grupoCusto
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<ContratoPratica> findContratoPraticaByFilterFetch(
			final ContratoPratica filter,
			final Cliente cli,
			final NaturezaCentroLucro natureza,
			final CentroLucro cp,
			final GrupoCusto grupoCusto,
			final List<String> indicadorWorkAtRiskList);

	/**
	 * Verifica se o contratoPratica esta completamente preenchido.
	 * 
	 * @param contratoPratica
	 *            para realizar a verifica��o
	 * 
	 * @return se completo retorna true sen�o retorna false
	 * @throws MoreThanOneActiveEntityException
	 */
	@Transactional
	ContratoPratica verifyContratoPraticaComplete(ContratoPratica contratoPratica);

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cl
	 *            - CentroLucro que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findContratoPraticaByCentroLucro(final CentroLucro cl);

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findContratoPraticaByNatureza(
			final NaturezaCentroLucro natureza);

	/**
	 * Retorna todas as entidades relacionadas com o Contrato passado por
	 * parametro.
	 * 
	 * @param msa
	 *            - {@link Msa} que se deseja buscar
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findContratoPraticaByContrato(final Msa msa);

	/**
	 * Retorna todas as entidades sem Fiscal Deal associado.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findContratoPraticaSemFiscalDeal();

	/**
	 * Busca uma lista de entidades que possuem MapaAlocacao.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<ContratoPratica> findContPratAllWithMapaAlocacao();

	/**
	 * Retorna todos os ContratoPratica relacionados com o chargeback e dentro
	 * de um periodo.
	 * 
	 * @param tiRecurso
	 *            - TiRecurso que se deseja buscar os ContratoPraticas
	 * 
	 * @param startDate
	 *            - Data inicio do periodo.
	 * 
	 * @param endDate
	 *            - Data fim do periodo
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findContratoPraticaByTiRecursoAndPeriod(
			final TiRecurso tiRecurso, final Date startDate, final Date endDate);

	/**
	 * Busca um contrato pratica pelo nome.
	 * 
	 * @param name
	 *            the nome
	 * @return {@link ContratoPratica}
	 */
	ContratoPratica findContratoPraticaByName(final String name);

	/**
	 * Checa se um {@link ContratoPratica} j� existe no banco utilizando as
	 * regras de constraints do banco.
	 * 
	 * @param contratoPratica
	 *            the contratoPratica
	 * @return retorna true se o contratoPratica existe
	 */
	Boolean existContratoPratica(final ContratoPratica contratoPratica);

	/**
	 * Add todas as {@link DealFiscal}s associando ao {@link ContratoPratica}.
	 * 
	 * @param contratoPratica
	 *            the contratoPratica
	 * @param cpraticaDfiscals
	 *            the {@link CpraticaDfiscal}
	 */
	@Transactional
	void addAllCpraticaDfiscal(final ContratoPratica contratoPratica,
			final List<CpraticaDfiscal> cpraticaDfiscals);

	/**
	 * Remove todas as {@link DealFiscal}s associadas a um
	 * {@link ContratoPratica} removendo as associacoes
	 * 
	 * @param contratoPratica
	 *            the {@link ContratoPratica}
	 */
	@Transactional
	void removeAllCpraticaDfiscal(final ContratoPratica contratoPratica);

	/**
	 * Atualiza a lista de {@link CpraticaDfiscal}s do {@link ContratoPratica}.
	 * 
	 * @param contratoPratica
	 *            the {@link ContratoPratica}
	 * @param cpraticaDfiscals
	 *            a list of {@link CpraticaDfiscal}
	 */
	@Transactional
	void updateCpraticaDfiscal(final ContratoPratica contratoPratica,
			List<CpraticaDfiscal> cpraticaDfiscals);

	/**
	 * Busca todos os clobs comepletos e ativos.
	 * 
	 * @return listResult
	 */
	List<ContratoPratica> findContratoPraticaAllCompleteAndActive();

	Boolean isContratoPraticaActive(Long codigoContratoPratica);

	Boolean isContratoPraticaActive(ContratoPratica contratoPratica);

	List<ContratoPraticaAud> findHistoryByCodigo(Long codigoContratoPratica);

	List<String> findManagerOfContratoPratricaWithouDealFiscal();

	List<Map<String,String>> findContratoPraticaWithoutDealFiscal(String loginManager);

	/**
	 * Busca todos os Mapas de Alocação com o status "Prospect" que possuem alocacao
	 * 	de algum recurso no mês corrente.
	 *
	 * @return {@link List} of {@link MapaProspectComAlocacaoResultsetVO}
	 */
	List<MapaProspectComAlocacaoResultsetVO> findProspectMapWithAllocation();

	List<ContractLobDTO> findAllWithExternalRestRequest();


	/**
	 * Salva o upload do arquivo de contrato pratica.
	 *
	 * @param uploadItem      - contem o valor do upload
	 * @param centroLucroList
	 * @return retorna true se salvo com sucesso, caso contrario false.
	 */
	@Transactional
	Map<String, List<String>> uploadContratoPratica(final UploadItem uploadItem, List<String> centroLucroList) throws Exception;



	/**
	 * Monta o map de erros do arquivo que foi feito upload.
	 *
	 * @param uploadManagersList
	 * @param centroLucroList
	 */
	Map<String, List<String>> mapErrosUploadFile(final List<UploadManagers> uploadManagersList, List<String> centroLucroList) throws Exception;

	@Transactional
	void saveContratoPraticaData(List<UploadManagers> uploadManagersList) throws Exception;

	boolean hasKeyWithValue(Map<String, List<String>> upload);

	void atualizaNomesContratoPratica(List<ContratoPratica> contratoPratica);

	void atualizaContratoPratica(ContratoPratica contratoPratica);
}
