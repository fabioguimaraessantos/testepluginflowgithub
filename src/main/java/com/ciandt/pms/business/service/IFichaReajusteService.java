package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteStatus;
import com.ciandt.pms.model.Msa;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IFichaReajusteService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
	FichaReajuste findFichaReajusteById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<FichaReajuste> findFichaReajusteAll();

	/**
	 * Insere uma entidade.
	 *
	 * @param entity
	 * @throws  
	 */
    @Transactional
	void createFichaReajuste(final FichaReajuste entity);
	
	/**
	 * Atualiza uma entidade.
	 *
	 * @param entity
	 */
	@Transactional
	boolean updateFichaReajuste(final FichaReajuste entity);

	/**
	 * Deleta uma entidade.
	 *
	 * @param entity
	 */
	@Transactional
	void deleteFichaReajuste(final FichaReajuste entity);

    /**
     * Retorna uma {@link FichaReajuste} com seu nome igual a {@code nomeFichaReajuste}.
     *
     * @param nomeFichaReajuste
     * @return {@link FichaReajuste}
     */
    FichaReajuste findFichaReajusteByNomeFichaReajuste(String nomeFichaReajuste);

    /**
     * Retorna Fichas de Reajuste que estão em {@code documentosLegais}.
     *
     * @param documentosLegais
     * @return List<FichaReajuste>.
     */
	List<FichaReajuste> findFichaReajusteByDocumentosLegais(
			List<DocumentoLegal> documentosLegais);

    /**
     * Retorna {@link FichaReajuste} que está em {@code documentoLegal}.
     *
     * @param documentoLegal
     * @return Entidade {@link FichaReajuste}.
     */
	FichaReajuste findFichaReajusteByDocumentoLegal(
			DocumentoLegal documentoLegal);

    /**
     * Retorna Fichas de Reajuste com status igual a {@code status}.
     * 
     * @return list<FichaReajusteStatus>
     */
	List<FichaReajuste> findFichaReajusteByFichaReajusteStatus(
			FichaReajusteStatus status);

	/**
	 * Retorna todas as Fichas de Reajuste com status ativo.
	 *
	 * @return list<FichaReajusteStatus>
	 */
	List<FichaReajuste> findAllFichaReajusteActive();

	/**
	 * Retorna Fichas de Reajuste onde seus DocumentosLegais estão relacionados a {@code msa}.
	 *
	 * @param msa
	 * @return List<FichaReajuste>
	 */
	List<FichaReajuste> findAllFichaReajusteActiveByMsa(final Msa msa);

	/**
	 * Indica se {@code fichaReajuste} precisa de um ou mais {@link ControleReajuste}.
	 *
	 * @param fichaReajuste
	 * @return Boolean
	 */
	Boolean fichaReajusteNeedsNewControleReajuste(final FichaReajuste fichaReajuste);
}