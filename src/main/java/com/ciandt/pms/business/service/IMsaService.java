package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.MsaSaldoMoedaRow;
import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * A classe IMsaService proporciona a interface de acesso para a camada de
 * serviço referente a entidade Msa.
 * 
 * @since 01/10/2012
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IMsaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            a ser inserida
	 */
	@Transactional
	boolean createMsa(final Msa entity);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Msa> findMsaAll();

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	Msa findMsaById(final Long id);

	/**
	 * Busca uma lista de entidades por filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro
	 */
	List<Msa> findMsaByFilter(final Msa filter);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * @exception IntegrityConstraintException
	 *                lança exceção caso a Pratica possua ContratoPratica
	 *                associados e tente inativá-la.
	 */
	@Transactional
	void updateMsa(final Msa entity) throws IntegrityConstraintException;

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @throws IntegrityConstraintException
	 *             lança exceção caso o Msa possua ContratoPratica associados e
	 *             tente inativá-la.
	 */
	@Transactional
	void removeMsa(final Msa entity) throws IntegrityConstraintException;

	/**
	 * Executa um update da Tab Budget.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * @param listMsaSalMoeRow
	 *            listMsaSalMoeRow
	 * 
	 * @throws IntegrityConstraintException
	 *             lança exceção caso o Msa possua ContratoPratica associados e
	 *             tente inativá-lo
	 */
	@Transactional
	void updateMsaTabBudget(final Msa entity,
			final List<MsaSaldoMoedaRow> listMsaSalMoeRow)
			throws IntegrityConstraintException;

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os Msa
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Msa> findMsaByCliente(final Cliente cliente);

	/**
	 * Retorna todas as entidades relacionadas com o BmDn passado por
	 * parametro.
	 *
	 * @param bmdn - BmDn que se deseja buscar os Msa
	 *
	 * @return retorna uma lista com todas as entidades.
	 * @throws BusinessException Exceção de negócio
	 */
	List<Msa> findMsaByBmDn(final Long bmdn) throws BusinessException;

	/**
	 * Retorna todas as entidades relacionadas com o Industry TYpe passado por
	 * parametro.
	 *
	 * @param industryType - Industry Type que se deseja buscar os Msa
	 *
	 * @return retorna uma lista com todas as entidades.
	 * @throws BusinessException Exceção de negócio
	 */
	List<Msa> findMsaByIndustryType(final Long industryType) throws BusinessException;

	/**
	 * Busca todas as Moedas ativas e marcadas como selecionadas na aba Budget
	 * do MSA.
	 * 
	 * @param msa
	 *            msa
	 * @return retorna uma lista com as entidades.
	 */
	List<MsaSaldoMoedaRow> getListMsaSaldoMoedaRow(final Msa msa);

	/**
	 * Retorna todas as entidades no estado completo.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Msa> findMsaAllComplete();

	/**
	 * Checa se o MSA esta com status completo. Caso esteja retorna TRUE.
	 * 
	 * @param codigoMsa
	 *            - codigo do MSA a ser pesquisado
	 * 
	 * @return retorna true caso o MSA em questao esteja completo
	 */
	boolean isMsaCompleted(final Long codigoMsa);

	/**
	 * Calcula o valor do Forecast Sales Tax: media de impostos dos Fiscals
	 * Deals associados ao Contract-LOB, sendo que o imposto do Fiscal Deal eh a
	 * media dos impostos de seus Service Types associados.
	 * 
	 * @param msa
	 *            - ContratoPratica que será usado para calcular o Forecast
	 *            Sales Tax
	 * 
	 * @return valor do Forecast Sales Tax
	 */
	BigDecimal calculateForecastSalesTax(final Msa msa);

	/**
	 * Retorna uma busca rapida por parte do nome do msa.(AutoComplete)
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	List<Msa> findMsabyNameQuick(final String name);

	/**
	 * Retorna uma busca nome do msa.
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	Msa findMsaByName(String name);
	
	boolean existsMsa(final Msa msa);

	/**
	 * Retorna um Msa relacionado a {@link CentroLucro}.
	 *
	 * @param centroLucro
	 * @return {@link Msa}
	 */
	List<Msa> findMsaByCentroLucro(CentroLucro centroLucro);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades mas apenas o codigo e
	 *         nome estao preenchidos.
	 */
	List<Msa> findAllReturnCodigoAndNomeMsa();

	/**
	 * Faz o upload do arquivo de legal doc.
	 *
	 * @param uploadItem      - contem o valor do upload
	 * @return retorna true se salvo com sucesso, caso contrario false.
	 */
	@Transactional
	Map<String, List<String>> uploadLegalDoc(final UploadItem uploadItem) throws Exception;


	/**
	 * Monta o map de erros do arquivo que foi feito upload.
	 *
	 * @param uploadMsaLegalDocs
	 */
	Map<String, List<String>> mapErrosUploadFile(final List<UploadMsaLegalDoc> uploadMsaLegalDocs) throws Exception;


	@Transactional
	void saveMsaLegalDocData(List<UploadMsaLegalDoc> uploadMsaLegalDocs) throws Exception;

	boolean hasKeyWithValue(Map<String, List<String>> upload);


}