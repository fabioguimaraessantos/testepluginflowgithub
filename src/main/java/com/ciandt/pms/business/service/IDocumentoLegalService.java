package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;

/**
 * 
 * @author peter
 * 
 */
@Service
public interface IDocumentoLegalService {

	/**
	 * Cria um DocumentoLegal.
	 * 
	 * @param documentoLegal
	 */
	@Transactional
	void createDocumentoLegal(DocumentoLegal documentoLegal);

	/**
	 * Atualiza um documentoLegal.
	 * 
	 * @param documentoLegal
	 */
	@Transactional
	void updateDocumentoLegal(DocumentoLegal documentoLegal);

	/**
	 * Remove um documentoLegal.
	 * 
	 * @param documentoLegal
	 */
	@Transactional
	void deleteDocumentoLegal(DocumentoLegal documentoLegal);

	/**
	 * Busca por msa.
	 * 
	 * @param msa
	 * @return
	 */
	List<DocumentoLegal> findDocumentoLegalByMsa(final Msa msa);

	/**
	 * Busca a maior sequencie por msa, para geracao do codigo do legal doc.
	 * 
	 * @param msa
	 * @return
	 */
	Integer getNextSequencieByMsa(final Msa msa);

	/**
	 * Busca um DocumentoLegal por id
	 * 
	 * @param id
	 *            Código do DocumentoLegal (chave primária)
	 * @return {@link DocumentoLegal}
	 */
	DocumentoLegal findDocumentoLegalById(final Long id);

	/**
	 * busca por ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	List<DocumentoLegal> findByFichaReajuste(final FichaReajuste fichaReajuste);

	/**
	 * Retorna todos {@link DocumentoLegal} ativos.
	 * 
	 * @return List<DocuemtnoLegal>
	 */
	List<DocumentoLegal> findAllDocumentoLegalActive();

	/**
	 * Monta e envia o email de documento legal.
	 * 
	 * @param qtMesesInicioLembrete
	 *            Quantidade de meses configurado no config.properties
	 *            (documento.legal.mail.qt.meses.inicio.lembrete)
	 * @param qtDiasEntreLembretes
	 *            Qquantidade de dias configurado no config.properties
	 *            (documento.legal.mail.qt.dias.entre.lembretes)
	 */
	@Transactional
	void montaMailDocumentoLegal(final Integer qtMesesInicioLembrete,
			final Integer qtDiasEntreLembretes);

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
	public List<DocumentoLegal> findToSendEmail(final Date dataPrevista,
			final Date dataUltimoEnvioEmail);

}
