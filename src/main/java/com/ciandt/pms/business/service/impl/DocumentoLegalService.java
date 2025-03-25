package com.ciandt.pms.business.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IDocumentoLegalResponsavelService;
import com.ciandt.pms.business.service.IDocumentoLegalService;
import com.ciandt.pms.business.service.IFichaReajusteService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IDocumentoLegalDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.PMSUtil;

/**
 * 
 * @author peter
 * 
 */
@Service
public class DocumentoLegalService extends AbstractGestaoReajusteService
		implements IDocumentoLegalService {

	/**
	 * Dao da entidade DocumentoLegal.
	 */
	@Autowired
	private IDocumentoLegalDao documentoLegalDao;
	
	/**
	 * Dao da entidade DocumentoLegal.
	 */
	@Autowired
	private IFichaReajusteService fichaReajusteService;

	@Autowired
	private IDocumentoLegalResponsavelService documentoLegalResponsavelService;


	/**
	 * Cria um DocumentoLegal.
	 * 
	 * @param documentoLegal
	 */
	public void createDocumentoLegal(DocumentoLegal documentoLegal) {

		// Busca o proximo numero da sequencia para gerar o codigo do Documento
		// Legal
		Integer sequence = this.getNextSequencieByMsa(documentoLegal.getMsa());
		documentoLegal.setCodigoSequencial(sequence);

		documentoLegal.setCodigoCodigoGerado(geraCodigoDocumentoLegal(
				documentoLegal, sequence));

//		for (DocumentoLegalResponsavel dlr : documentoLegal.getDocumentoLegalResponsaveis()) {
//			documentoLegalResponsavelService.create(dlr);
//		}

		documentoLegalDao.create(documentoLegal);
	}

	/**
	 * Gera o codigo do Documento Legal (código gravado na coluna
	 * DOLE_CD_CODIGO_GERADO) no formato
	 * NomeMSA.SiglaTipoDocLegal-CodigoSequencialIncremental3digitos
	 * 
	 * <li>Ex: NomeMSA.AG-001</li>
	 * 
	 * @param documentoLegal
	 * @param sequence
	 * @return
	 */
	private String geraCodigoDocumentoLegal(DocumentoLegal documentoLegal,
			Integer sequence) {

		StringBuilder sb = new StringBuilder();
		sb.append(documentoLegal.getMsa().getNomeMsa());
		sb.append(".");
		sb.append(documentoLegal.getDocumentoLegalTipo()
				.getSiglaTipoDocumentoLegal());
		sb.append("-");
		sb.append(StringUtils.leftPad(sequence.toString(), 3, '0'));

		return sb.toString();
	}

	/**
	 * Atualiza um documentoLegal.
	 * 
	 * @param documentoLegal
	 */
	public void updateDocumentoLegal(DocumentoLegal documentoLegal) {
		documentoLegalDao.update(documentoLegal);
	}

	/**
	 * Remove um documentoLegal.
	 * 
	 * @param documentoLegal
	 */
	public void deleteDocumentoLegal(DocumentoLegal documentoLegal) {
		
		// Se tiver ficha vinculada ao documento legal, verifica se não há mais
		// nenhum outro documento legal vinculado à ficha. Se não exitir, exclui
		// a ficha e os controles reajuste.
		if (documentoLegal.getFichaReajuste() != null) {
			// Consulta todos os documentos legais vinculados a ficha
			List<DocumentoLegal> listaDocLegal = this
					.findByFichaReajuste(documentoLegal.getFichaReajuste());

			// Se a ficha estiver vinculado somente ao documento legal sendo
			// excluído, então remove a ficha e os controles de reajuste
			if (listaDocLegal.size() == 1
					&& listaDocLegal.get(0).getCodigoDocumentoLegal()
							.equals(documentoLegal.getCodigoDocumentoLegal())) {

				FichaReajuste ficha = fichaReajusteService
						.findFichaReajusteById(documentoLegal
								.getFichaReajuste().getCodigoFichaReajuste());
				
				// Exclui os controles reajuste em cascata
				fichaReajusteService.deleteFichaReajuste(ficha);
			}
		}
		documentoLegalDao.remove(documentoLegal);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IDocumentoLegalService#
	 * findDocumentoLegalByMsa(com.ciandt.pms.model.Msa)
	 */
	public List<DocumentoLegal> findDocumentoLegalByMsa(final Msa msa) {
		return documentoLegalDao.findByMsa(msa);
	}

	/**
	 * Busca a maior sequencie por msa, para geracao do codigo do legal doc.
	 * 
	 * @param msa
	 * @return
	 */
	public Integer getNextSequencieByMsa(final Msa msa) {
		return documentoLegalDao.findMaxSequencieByMsa(msa) + 1;
	}

	/**
	 * Busca um DocumentoLegal por id
	 * 
	 * @param id
	 *            Código do DocumentoLegal (chave primária)
	 * @return {@link DocumentoLegal}
	 */
	public DocumentoLegal findDocumentoLegalById(Long id) {
		return documentoLegalDao.findById(id);
	}

	/**
	 * busca por ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	public List<DocumentoLegal> findByFichaReajuste(
			final FichaReajuste fichaReajuste) {
		return documentoLegalDao.findByFichaReajuste(fichaReajuste);
	}

	/**
	 * Retorna todos {@link DocumentoLegal} ativos.
	 * 
	 * @return List<DocuemtnoLegal>
	 */
	public List<DocumentoLegal> findAllDocumentoLegalActive() {
		return documentoLegalDao.findAllActive();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.business.service.IDocumentoLegalService#findToSendEmail
	 * (java.util.Date, java.util.Date)
	 */
	public List<DocumentoLegal> findToSendEmail(final Date dataPrevista,
			final Date dataUltimoEnvioEmail) {
		return documentoLegalDao.findToSendEmail(dataPrevista,
				dataUltimoEnvioEmail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IDocumentoLegalService#
	 * montaMailDocumentoLegal(java.lang.Integer, java.lang.Integer)
	 */
	public void montaMailDocumentoLegal(final Integer qtMesesInicioLembrete,
			final Integer qtDiasEntreLembretes) {

		// Consulta a lista de documento legal que não seja renovação
		// automática,
		// que possui data fim vigência e este seja anterior a data atual menos
		// 3 meses e que o documento legal e o MSA esteja ativo
		List<DocumentoLegal> legalDocList = this.findToSendEmail(
				getDataInicioEnvio(qtMesesInicioLembrete),
				getDataUltimoEnvio(qtDiasEntreLembretes));

		for (DocumentoLegal docLegal : legalDocList) {

			// Lista com os emails que deverão receber o aviso
			// Inclui o email do CS, configurado no arquivo config.properties
			StringBuilder mailTo = new StringBuilder(
					systemProperties.getProperty(Constants.CONTROLE_REAJUSTE_EMAIL_CS));

			mailTo.append(super.SEPARADOR_EMAIL);
			List<String> gestores = docLegal.getNomeDocumentoLegalResponsaveis();
			mailTo.append(docLegal.getDocumentoLegalResponsaveisCommaSeparated());

			// Pega o nome do cliente
			String nomeCliente = docLegal.getMsa().getCliente()
					.getNomeCliente();

			// Se diferente de ambiente de produção seta o email de teste.
			// Verificação de segurança, para que não seja enviado
			// email para o usuário no ambiente de desenvolvimento/teste.
			if (!PMSUtil.isProduction()) {
				mailTo = new StringBuilder(systemProperties.getProperty(Constants.EMAIL_ADDRESS_TEST_KEY));
			}

			// Se a data atual for anterior ou igual a data fim vigência, então
			// envia o email de lembrete que o documento legal deverá ser
			// renovado até data x
			if (DateUtil.before(new Date(), docLegal.getDataFimVigencia(),
					Calendar.HOUR_OF_DAY)) {
				// Define o assunto do e-mail
				String subject = BundleUtil.getBundle(
						Constants.DOCUMENTO_LEGAL_EMAIL_ASSUNTO_A_VENCER,
						nomeCliente);

				this.sendMailDocumentoLegal(docLegal, nomeCliente, gestores,
						docLegal.getMsa().getNomeMsa(), mailTo.toString(),
						"documentoLegalReminder.vm", subject);
			} else {

				// Define o assunto do e-mail
				String subject = BundleUtil.getBundle(
						Constants.DOCUMENTO_LEGAL_EMAIL_ASSUNTO_VENCIDO,
						nomeCliente);

				// Se a data atual for posterior a data fim vigência, então
				// envia o email que já Venceu
				this.sendMailDocumentoLegal(docLegal, nomeCliente, gestores,
						docLegal.getMsa().getNomeMsa(), mailTo.toString(),
						"documentoLegalRenewalNeeded.vm", subject);
			}
			
			// Atualiza a data envio e-mail
			DocumentoLegal documentoLegal = this.findDocumentoLegalById(docLegal.getCodigoDocumentoLegal());
			documentoLegal.setDataEnvioEmail(new Date());
			this.updateDocumentoLegal(documentoLegal);
		}

	}

	/**
	 * Monta e envia o email do documento legal
	 * 
	 * @param documentolegal
	 *            Objeto {@link DocumentoLegal}
	 * @param nomeCliente
	 *            Nome do Cliente
	 * @param gestores
	 *            Lista com os nomes dos Gestores dos Contratos Praticas do MSA
	 * @param nomeMSA
	 *            Nome do MSA
	 * @param mailTo
	 *            e-mails dos destinatários, separado por virgula
	 * @param emailTemplate
	 *            Nome do template de e-mail
	 * @param subject
	 *            Subject do e-mail
	 * 
	 */
	private void sendMailDocumentoLegal(final DocumentoLegal documentolegal,
			final String nomeCliente, final List<String> gestores,
			final String nomeMSA, final String mailTo,
			final String emailTemplate, final String subject) {

		// Seta os parametros necessários para montar o body do e-mail
		Map<String, Object> dataSource = new HashMap<String, Object>();

		dataSource.put("nomesGestores", montaNomesGestores(gestores));
		dataSource.put("startDate",
				sdf.format(documentolegal.getDataInicioVigencia()));
		dataSource.put("endDate",
				sdf.format(documentolegal.getDataFimVigencia()));
		dataSource.put("codeDocLegal", documentolegal.getCodigoCodigoGerado());
		dataSource.put("nomeMSA", nomeMSA);
		dataSource
				.put("codigoDocLegal", documentolegal.getCodigoCodigoGerado());
		dataSource.put("nomeDocLegal", documentolegal.getNomeDocumentoLegal());
		dataSource.put("link",
				systemProperties.getProperty(Constants.HOST_ENVIRONMENT));

		// Define o body do email atraves do template e parametros
		String messageBody = mailSender.getTemplateMailMessage(emailTemplate,
				dataSource);

		// Envia o email
		mailSender.sendHtmlMail(mailTo.toString().trim(), subject, messageBody);
	}
}