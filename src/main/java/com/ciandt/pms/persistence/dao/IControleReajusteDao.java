package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Repository
public interface IControleReajusteDao extends IAbstractDao<Long, ControleReajuste> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<ControleReajuste> findAll();

	/**
	 * Busca {@link ControleReajuste} onde sua Data Prevista está entre
	 * {@code startDate} e {@code endDate}.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Lista de {@link ControleReajuste}
	 */
	List<ControleReajuste> findByDateIntervalAndFichaReajuste(Date startDate,
			Date endDate, FichaReajuste fichaReajuste);

	/**
	 * Busca {@link ControleReajuste} onde sua Data prevista é maior que
	 * {@code date}.
	 * 
	 * @param date
	 * @return Lista de {@link ControleReajuste}
	 */
	List<ControleReajuste> findGreaterThanDateAndFichaReajuste(Date date,
			FichaReajuste fichaReajuste);

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
	 * @param dtUltimoEnvio
	 *            Data atual - quantidade de dias configurador no
	 *            config.properties
	 *            (controle.reajuste.mail.qt.dias.entre.lembretes)
	 * @return Lista de {@link ControleReajuste} que deverá ser enviado email
	 */
	List<ControleReajuste> findToSendEmail(final Date dataInicioLembrete,
			final Date dataUltimoEnvioEmail);

	/**
	 * Retorna o ultimo {@link ControleReajuste} com {@code fichasResjustes}.
	 * 
	 * @param fichasReajustes
	 * @return List<ControleReajuste>
	 */
	ControleReajuste findLastbyFichaReajuste(final FichaReajuste fichasReajustes);

	/**
	 * Busca por ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	List<ControleReajuste> findByFichaReajuste(final FichaReajuste fichaReajuste);


	/**
	 * Busca {@link ControleReajuste} onde sua Data Prevista está entre
	 * {@code startDate} e {@code endDate}.
	 * 
	 * @param startDate
	 * @param endDate
	 *
	 * @return Lista de {@link ControleReajuste}
	 */
	List<ControleReajuste> findByDateInterval(Date startDate, Date endDate);

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
	List<ControleReajuste> findByDateIntervalAndMsaAndCliente(
			final Date startDate, final Date endDate, final Msa msa,
			final Cliente cliente, final Long controleReajusteStatusId);
}