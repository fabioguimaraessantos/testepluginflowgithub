package com.ciandt.pms.util;

import com.ciandt.pms.Constants;
import com.ciandt.pms.integration.queue.PendingMailProducer;
import com.ciandt.pms.resource.Attachments;
import com.ciandt.pms.resource.SQSMessageResource;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Classe utilitaria que possui as funcionalidades de envio de email.
 * 
 * @since 11/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class MailSenderUtil {

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	@Autowired
	private PendingMailProducer pendingMailProducer;

	/** Arquivo de configuracoes. */
	private VelocityEngine velocityEngine;

	private static final Logger logger = LogManager.getLogger(MailSenderUtil.class.getName());

	/**
	 * Envia um email somente texto.
	 * 
	 * @param to
	 *            - Endere�o de email do destinat�rio
	 * @param subject
	 *            - Assunto do email
	 * @param message
	 *            - corpo do email
	 */
	public void sendTextMail(final String to, final String subject,
			final String message) {
		this.sendMail(to, subject, message, false, "");
	}

	/**
	 * Envia um email no formato HTML.
	 * 
	 * @param to
	 *            - Endere�o de email do destinat�rio
	 * @param subject
	 *            - Assunto do email
	 * @param message
	 *            - corpo do email
	 */
	public void sendHtmlMail(final String to, final String subject,
			final String message) {
		this.sendMail(to, subject, message, true, "");
	}

	public void sendHtmlMail(final String to, final String subject,
							 final String message, String cc) {
		this.sendMail(to, subject, message, true, cc);
	}

	/**
	 * M�todo que envia um email.
	 * 
	 * @param to
	 *            - Endere�o de email do destinat�rio
	 * @param subject
	 *            - Assunto do email
	 * @param message
	 *            - corpo do email
	 * @param isHtmlMail
	 *            - flag que indica se o email � somente texto ou html
	 */
	private void sendMail(final String to, final String subject,
			final String message, final boolean isHtmlMail, String cc) {

		String emailAddress = to;
		// Verifica��o de seguran�a, para que n�o seja enviado
		// email para o usu�rio no ambiente de desenvolvimento/teste
		// Se diferente de ambiente de produ��o seta o email de teste

		if (!PMSUtil.isProduction()) {
			emailAddress = systemProperties
					.getProperty(Constants.EMAIL_ADDRESS_TEST_KEY);
		}

		try {
			SQSMessageResource messageResource = new SQSMessageResource();
			messageResource.setTo(emailAddress);
			messageResource.setSubject(subject);
			messageResource.setCc(cc);
			messageResource.setFrom(systemProperties.getProperty("mail.sender.from.address"));

			if (isHtmlMail) {
				messageResource.setHtml(message);
			} else {
				messageResource.setText(message);
			}

			pendingMailProducer.produce(messageResource, to, null, null);

			logger.info(String.format("E-mail send successfully to: %s subject: %s", emailAddress, subject));

		} catch (Exception e) {
			logger.error((String.format("Error sending mail: to: %s subject: %s error message: %s", emailAddress, subject, e.getMessage())));
		}
	}

	/**
	 * Envia um email com anexo.
	 *
	 * @param to
	 *            - Endereço de email do destinatário
	 * @param subject
	 *            - Assunto do email
	 * @param replyTo
	 *             - Endereço do email de resposta
	 * @param cc
	 *             - Endereço do email em copia
	 * @param message
	 *            - corpo do email
	 * @param attachment
	 *            - arquivo a ser anexado
	 * @param attachmentName
	 *            - nome do arquivo a ser anexado
	 * @param isHtmlMail
	 *            - flag que indica se o email é somente texto ou html
	 */
	public void sendMailAttachment(String to, final String subject, final String replyTo, String cc,
									final String message, final boolean isHtmlMail,
									final byte[] attachment, final String attachmentName) {

		if (!PMSUtil.isProduction()) {
			to = systemProperties.getProperty(Constants.EMAIL_ADDRESS_TEST_KEY);
			cc = systemProperties.getProperty(Constants.EMAIL_ADDRESS_TEST_KEY);
		}

		try {

			SQSMessageResource messageResource = new SQSMessageResource();
			messageResource.setTo(to);
			messageResource.setSubject(subject);
			messageResource.setCc(cc);
			messageResource.setFrom(systemProperties.getProperty("mail.sender.from.address"));
			messageResource.setReplyTo(replyTo);

			if (isHtmlMail) {
				messageResource.setHtml(message);
			} else {
				messageResource.setText(message);
			}

			if (attachment != null && attachmentName != null) {
				Attachments attachments = new Attachments();
				attachments.setContent(new String(Base64.encodeBase64(attachment)));
				attachments.setEncoding("BASE64");
				attachments.setFilename(attachmentName);
				attachments.setFromS3(false);
				messageResource.setAttachments(Arrays.asList(attachments));
			}

			pendingMailProducer.produce(messageResource, to, null, null);

			logger.info(String.format("E-mail send successfully to: %s subject: %s", to, subject));
		} catch (Exception e) {
			logger.error(String.format("Error sending mail: to: %s subject: %s error message: %s", to, subject, e.getMessage()));
		}
	}

	/**
	 * Pega o template do email passado por parametro e gera uma messagem de
	 * texto com os dados passado pelo segundo parametro.
	 * 
	 * @param templateLocation
	 *            - nome e localiza��o do template de email.
	 * @param dataSource
	 *            - fonte de dados para o template
	 * 
	 * @return retorna o tamplete preenchido em formato de String.
	 */
	public String getTemplateMailMessage(final String templateLocation,
			final Map<String, Object> dataSource) {

		try {
			return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
					templateLocation, dataSource);
		} catch (VelocityException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * @param velocityEngine
	 *            the velocityEngine to set
	 */
	public void setVelocityEngine(final VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

}