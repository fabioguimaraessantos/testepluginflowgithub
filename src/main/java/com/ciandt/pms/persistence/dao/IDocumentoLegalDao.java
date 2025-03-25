package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;

public interface IDocumentoLegalDao extends IAbstractDao<Long, DocumentoLegal> {

	/**
	 * Busca DocumentoLegal por msa.
	 * 
	 * @param msa
	 * @return
	 */
	List<DocumentoLegal> findByMsa(final Msa msa);

	/**
	 * Busca a maior sequencie por msa, para geracao do codigo do legal doc.
	 * 
	 * @param msa
	 * @return
	 */
	Integer findMaxSequencieByMsa(final Msa msa);

	/**
	 * Retorna todos {@link DocumentoLegal} ativos.
	 * 
	 * @return List<DocuemtnoLegal>
	 */
	List<DocumentoLegal> findAllActive();

	/**
	 * busca por ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	List<DocumentoLegal> findByFichaReajuste(final FichaReajuste fichaReajuste);

	/**
	 * Busca lista de {@link DocumentoLegal} para envio de email. Busca os
	 * documentos legais que atendam as seguintes regras: <li>Estão com status
	 * (Active). O campo dole_in_status = "A"</li><li>O MSA esteja com status
	 * diferente de I (Inativo></li> <li>Que não seja renovado automaticamente.
	 * O campo dole_sg_renovacao_automatica = "N"</li> <li>Data fim da vigêngia
	 * esteja prenchida</li>
	 * 
	 * @param dataInicioLembrete
	 *            Data atual + quantidade de meses configurado no
	 *            config.properties
	 *            (documento.legal.mail.qt.meses.inicio.lembrete)
	 * @param dataUltimoEnvioEmail
	 *            Data atual - quantidade de dias configurador no
	 *            config.properties
	 *            (documento.legal.mail.qt.dias.entre.lembretes)
	 * @return Lista de {@link DocumentoLegal} que deverá ser enviado email
	 */
	List<DocumentoLegal> findToSendEmail(final Date dataInicioLembrete,
			final Date dataUltimoEnvioEmail);
}
