package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IControleReajusteService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    ControleReajuste findControleReajusteById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<ControleReajuste> findControleReajusteAll();

	/**
	 * Popula e persiste um {@link ControleReajuste} a partir de uma
	 * {@link FichaReajuste}.
	 * 
	 * @param fichaReajuste
	 * @return 
	 */
	ControleReajuste populateControleReajusteFromFichaReajuste(
			final FichaReajuste fichaReajuste);

	/**
	 * Busca {@link ControleReajuste} onde sua Data Prevista está entre
	 * {@code startDate} e {@code endDate}.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Lista de {@link ControleReajuste}
	 */
	List<ControleReajuste> findControleReajusteByDateIntervalAndFichaReajuste(
			Date startDate, Date endDate, FichaReajuste fichaReajuste);

	/**
	 * Busca {@link ControleReajuste} onde sua Data prevista é maior que
	 * {@code date}.
	 * 
	 * @param date
	 * @return Lista de {@link ControleReajuste}
	 */
	List<ControleReajuste> findControleReajusteGreaterThanDateAndFichaReajuste(Date date,
			FichaReajuste fichaReajuste);

    /**
     * Busca {@link ControleReajuste} onde sua Data Prevista está entre a vigência do {@link DocumentoLegal}.
     *
     * @param documentoLegal
     * @return Lista de {@link DocumentoLegal}
     */
    List<ControleReajuste> findControleReajusteByDocumentoLegal(DocumentoLegal documentoLegal);
    
	/**
	 * Busca o histório de alteracoes do controle de reajuste
	 * 
	 * @param codigoControleReajuste
	 *            Código do controle de reajuste
	 * @return Lista de {@link ControleReajusteAud}
	 */
	List<ControleReajusteAud> findHistoryByCodigoControleReajuste(
			final Long codigoControleReajuste);
	
	/**
	 * Busca o histório de alteracoes do controle de reajuste por codigo por revtype.
	 * 
	 * @param codigoControleReajuste
	 *            Código do controle de reajuste
	 * @param revtype
	 *            codigo que indica se foi: <li>0 - Insert</li><li>1 - Update</li>
	 *            <li>2 - Delete</li>
	 * @return Lista de {@link ControleReajusteAud}
	 */
	List<ControleReajusteAud> findHistoryByCdControleReajusteAndRevtype(
			final Long codigoControleReajuste, final Long revtype);
    
	/**
	 * Monta e envia o email de controle de reajuste.
	 * 
	 * @param qtMesesInicioLembrete
	 *            Quantidade de meses configurado no config.properties
	 *            (controle.reajuste.mail.qt.meses.inicio.lembrete)
	 * @param qtDiasEntreLembretes
	 *            Quantidade de dias configurador no config.properties
	 *            (controle.reajuste.mail.qt.dias.entre.lembretes)
	 */
	@Transactional
	void montaMailControleReajuste(final Integer qtMesesInicioLembrete,
			final Integer qtDiasEntreLembretes);

	/**
	 * Insere uma entidade.
	 *
	 * @param entity
	 */
    @Transactional
	void createControleReajuste(final ControleReajuste entity);
	
	/**
	 * Atualiza uma entidade.
	 *
	 * @param entity
	 */
	@Transactional
	void updateControleReajuste(final ControleReajuste entity);

	/**
	 * Deleta uma entidade.
	 *
	 * @param entity
	 */
	@Transactional
	void deleteControleReajuste(final ControleReajuste entity);

	/**
	 * Busca por ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	List<ControleReajuste> findByFichaReajuste(final FichaReajuste fichaReajuste);

	/**
	 * Retorna o ultimo {@link ControleReajuste} com {@code fichasResjustes}.
	 *
	 * @param fichasReajustes
	 * @return List<ControleReajuste>
	 */
	List<ControleReajuste> findLastControleReajustebyFichasReajuste(
			final List<FichaReajuste> fichasReajustes);

	/**
	 * Retorna o ultimo {@link ControleReajuste} com {@code fichaResjuste}.
	 *
	 * @param fichaReajuste
	 * @return List<ControleReajuste>
	 */
	ControleReajuste findLastControleReajustebyFichaReajuste(
			final FichaReajuste fichaReajuste);

	/**
	 * Busca {@link ControleReajuste} onde sua {@code dataPrevista} está entre
	 * {@code startDate} e {@code endDate} e está relacionado a {@link Cliente}
	 * e {@link Msa}.
	 *
	 * @param startDate
	 * @param endDate
	 * @param msa
	 * @param cliente
	 * @return List<ControleReajuste>
	 */
	List<ControleReajuste> findControleReajusteByDateIntervalAndMsaAndCliente(
			final Date startDate, final Date endDate, final Msa msa,
			final Cliente cliente, final Long controleReajusteStatusId);

	/**
	 * Cria um ou mais {@link ControleReajuste} necessários para
	 * {@link FichaReajuste}.
	 *
	 * @param fichaReajuste
	 */
	void createAllControleReajusteForFichaReajuste(FichaReajuste fichaReajuste);

	/**
	 * Retorna a {@code dataPrevista} do próximo {@link ControleReajuste} para
	 * {@code fichaReajuste}.
	 * 
	 * @param fichaReajuste
	 * @param controleReajuste
	 * @return {@link Date}
	 */
	Date getDataPrevistaOfNextControleReajuste(FichaReajuste fichaReajuste);

	/**
	 * Cria um {@link ControleReajuste} para
	 * {@link FichaReajuste}.
	 *
	 * @param fichaReajuste
	 */
	void createControleReajusteForFichaReajuste(FichaReajuste fichaReajuste);

	/**
	 * Busca lista de {@link ControleReajuste} para envio de email. Busca os
	 * controles de reajuste que atendam as seguintes regras: <li>Estão com
	 * status 1(Open), 2(Rescheduled) e 3(Negotiating)</li> <li>Data prevista
	 * seja anterior a dataInicioLembrete</li> <li>Data de envio do ultimo email
	 * seja maior que dataUltimoEnvioEmail</li> <li>Ficha de reajuste esteja 1
	 * (Ativo)</li>
	 * 
	 * @param dataInicioLembrete
	 *            Data atual + quantidade de meses configurado no
	 *            config.properties
	 *            (controle.reajuste.mail.qt.meses.inicio.lembrete)
	 * @param dataUltimoEnvioEmail
	 *            Data atual - quantidade de dias configurador no
	 *            config.properties
	 *            (controle.reajuste.mail.qt.dias.entre.lembretes)
	 * @return Lista de {@link ControleReajuste} que deverá ser enviado email
	 */
	List<ControleReajuste> findToSendEmail(final Date dataPrevista,
			final Date dataUltimoEnvioEmail);

}