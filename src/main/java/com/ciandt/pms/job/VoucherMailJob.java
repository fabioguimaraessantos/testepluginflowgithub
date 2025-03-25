package com.ciandt.pms.job;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.model.FollowOrcamentoDesp;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Voucher;
import com.ciandt.pms.model.vo.OrcDespesaVoucherRow;
import com.ciandt.pms.util.JobUtil;
import com.ciandt.pms.util.MailSenderUtil;
import com.ciandt.pms.util.PMSUtil;
import com.ciandt.pms.util.VoucherMailTextUtil;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 
 * A classe VoucherMailJob proporciona as funcionalidades de enviar email para
 * seguidores de Travel Budget.
 * 
 * @since 28/08/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public class VoucherMailJob {

	private Logger logger = LoggerFactory.getLogger(VoucherMailJob.class);

	@Autowired
	private JobUtil jobUtil;

	/** Instancia de VoucherService. */
	@Autowired
	private IVoucherService voucherService;

	/** Instancia de OrcamentoDespesaService. */
	@Autowired
	private IOrcamentoDespesaService orcDespService;

	/** Instancia de OrcamentoDesepsaGcService. */
	@Autowired
	private IOrcDespesaGcService orcDespGcService;

	/** Instancia de OrcamentoDesepsaGcService. */
	@Autowired
	private IOrcDespesaClService orcDespClService;

	/** Utilitario para envio de email. */
	@Autowired
	private MailSenderUtil mailSender;

	/** Instancia de voucherMail. */
	@Autowired
	private IVoucherMail voucherMail;

	/** Propriedades do sistema. */
	@Autowired
	private Properties systemProperties;

	/** Propriedades do PMS */
	@Autowired
	private IParametroService parametroService;

	/** Servico de Follow de Orcamento Despesa. */
	@Autowired
	private IFollowOrcamentoDespService followOrcamentoDespService;

	/** Servico de Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/**
	 * @return the mailSender
	 */
	public MailSenderUtil getMailSender() {
		return mailSender;
	}

	/**
	 * @param mailSender
	 *            the mailSender to set
	 */
	public void setMailSender(final MailSenderUtil mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * M�todo disparado de x em x minutos para mandar email para seguidores de
	 * TravelBudget.
	 */
	public void sendVoucherMail() {

	    if (!jobUtil.isJobActive(Constants.JOB_SEND_VOUCHER_MAIL)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_SEND_VOUCHER_MAIL)
                    .concat(" property to 'true' for active it."));
            return;
        }

		// Busca voucher com emails ainda nao emitidos
		List<Voucher> listVouchers = voucherService.findAllNotEmailSent();

		OrcamentoDespesa orcDesp = new OrcamentoDespesa();
		logger.info("JOB", "iniciando envio de emails do voucher");
		for (Voucher voucher : listVouchers) {
			logger.info("JOB", voucher.getNumeroVoucher());

			try {
				orcDesp = orcDespService.findOrcamentoDespesaById(voucher
						.getCodigoOrcDespesa());
				if (Constants.STATUS_VOUCHER_OPEN.equals(voucher
						.getIndicadorStatus())) {

					// send mail voucher emitido
					this.sendMailEmissonVoucher(orcDesp, voucher);

				} else if (Constants.STATUS_VOUCHER_CANCEL.equals(voucher
						.getIndicadorStatus())) {

					// send mail voucher cancelado
					this.sendMailDeletionVoucher(orcDesp, voucher);

				} else {

					// send mail voucher alterado
					this.sendMailChangeVoucher(orcDesp, voucher);

					// Se red flag esta setada, envia email para dono do Travel
					// Budget
					if (Constants.YES.equals(voucher.getIndicadorRedFlag())) {
						this.sendMailRedFlag(orcDesp, voucher);
					}

				}

				// Atualiza voucher para email ja enviado
				voucher.setIndicadorEmailSent(Constants.FLAG_YES);
				voucherService.update(voucher);

			} catch (Exception e) {
                logger.error("JOB", Throwables.getStackTraceAsString(e));
//				mailSender.sendHtmlMail(Constants.EMAIL_ADDRESS_ERROR_KEY, "erro email voucher", e.getMessage());
			}
		}

	}

	/**
	 * Dispara email para o criador do voucher e seguidores do<br />
	 * travelbudget.
	 */
	public void sendOldOpenVoucherMail() {

        if (!jobUtil.isJobActive(Constants.JOB_SEND_OLD_OPEN_VOUCHER_MAIL)) {
            logger.warn("JOB", "Job is not active on config.properties. Set the "
                    .concat(Constants.JOB_SEND_OLD_OPEN_VOUCHER_MAIL)
                    .concat(" property to 'true' for active it."));
            return;
        }

		// Pega quantidade de dias que define o filtro de vouchers.
		Integer days = parametroService
				.findParametroByNomeParametro("EMAIL_VOUCHER_ABERTO")
				.getValorParametro().intValueExact();

		this.sendMailOldVoucherToOwners(days);
		this.sendMailOldVoucherToOrcDespFollowers(days);
	}

	private void sendMailOldVoucherToOwners(Integer days) {

		Calendar oldVoucherDefinition = Calendar.getInstance();
		oldVoucherDefinition.add(Calendar.DATE, -days);

		List<Pessoa> pessoas = pessoaService
				.findWithOpenVoucherBeforeDate(oldVoucherDefinition.getTime());

		for (Pessoa pessoa : pessoas) {

			List<Voucher> vouchesrWithOrcDesp = new ArrayList<Voucher>();
			for (Voucher voucher : pessoa.getVouchers()) {
				voucher.setOrcamentoDespesa(orcDespService
						.findOrcamentoDespesaById(voucher.getCodigoOrcDespesa()));
				vouchesrWithOrcDesp.add(voucher);
			}

			Map<String, Object> dataSource = new HashMap<String, Object>();
			dataSource.put("pessoa", pessoa);
			dataSource.put("vouchers", vouchesrWithOrcDesp);
			dataSource.put("days", days);

			String message = mailSender.getTemplateMailMessage(
					"voucherOpenVariousDays.vm", dataSource);

			String to = pessoa.getTextoEmail();

			logger.info("JOB", "*** sendMailOldVoucherToOwners TO: " + to);

			// Se diferente de ambiente de producao, seta o email de teste.
			// Verificacao de seguranca, para que nao seja enviado email
			//para o usuario no ambiente de desenvolvimento/teste.
			if (!PMSUtil.isProduction()) {
				to = systemProperties.getProperty(Constants.EMAIL_ADDRESS_TEST_KEY);
			}

			String subject = VoucherMailTextUtil.getSubjectOpenVoucherVariousDays(days);
			mailSender.sendHtmlMail(to, subject, message);
		}
	}

	private void sendMailOldVoucherToOrcDespFollowers(Integer days) {

		Calendar oldVoucherDefinition = Calendar.getInstance();
		oldVoucherDefinition.add(Calendar.DATE, -days);

		List<Pessoa> pessoas = pessoaService
				.findFollowingOrcDespWithOpenVoucherBeforeDate(oldVoucherDefinition
						.getTime());
		List<OrcDespesaVoucherRow> orcDespesaVoucherRows = null;
		for (Pessoa pessoa : pessoas) {
			orcDespesaVoucherRows = new ArrayList<OrcDespesaVoucherRow>();
			OrcDespesaVoucherRow orcDespesaVoucherRow = null;

			List<FollowOrcamentoDesp> followOrcamentoDesps = followOrcamentoDespService
					.findByPessoaAndDateOfOpenVoucher(pessoa,
							oldVoucherDefinition.getTime());

			for (FollowOrcamentoDesp followOrcamentoDesp : followOrcamentoDesps) {
				orcDespesaVoucherRow = new OrcDespesaVoucherRow();

				orcDespesaVoucherRow.setOrcDespesa(followOrcamentoDesp
						.getOrcamentoDespesa());
				orcDespesaVoucherRow.setVouchers(voucherService
						.findOpenVoucherByOrcDespAndDate(
								followOrcamentoDesp.getOrcamentoDespesa(),
								oldVoucherDefinition.getTime()));

				orcDespesaVoucherRows.add(orcDespesaVoucherRow);
			}

			Map<String, Object> dataSource = new HashMap<String, Object>();
			dataSource.put("pessoa", pessoa);
			dataSource.put("orcDespesaVouchers", orcDespesaVoucherRows);
			dataSource.put("days", days);

			String message = mailSender.getTemplateMailMessage(
					"voucherOfOrcDespOpenVariousDays.vm", dataSource);

			String to = pessoa.getTextoEmail();

			logger.info("JOB", "*** sendMailOldVoucherToOrcDespFollowers TO: "
					+ to);

			// Se diferente de ambiente de produ��o seta o email de teste.
			// Verifica��o de seguran�a, para que n�o seja enviado
			// email para o usu�rio no ambiente de desenvolvimento/teste.
			if (!PMSUtil.isProduction()) {
				to = systemProperties.getProperty(Constants.EMAIL_ADDRESS_TEST_KEY);
			}
			String subject = VoucherMailTextUtil.getSubjectVoucherOpenOrcDesp(days);
			mailSender.sendHtmlMail(to, subject, message);
		}
	}

	/**
	 * Dispara email para seguidores ao emitir um Vouhcer.
	 * 
	 * @param orcDesp
	 *            orcamentoDepesa.
	 * @param voucher
	 *            voucher.
	 */
	private void sendMailEmissonVoucher(final OrcamentoDespesa orcDesp,
			final Voucher voucher) {

		if ("CL".equals(orcDesp.getTpOrcDesp())) {
			voucherMail.sendMailIssuedClientVoucher(
					orcDespClService.findByOrcamentoDespesa(orcDesp), voucher);
		}

		if ("GC".equals(orcDesp.getTpOrcDesp())) {
			voucherMail.sendMailIssuedCostGroupVoucher(
					orcDespGcService.findByOrcDespesa(orcDesp), voucher);
		}

	}

	/**
	 * Dispara email para seguidores ao alterar o status de um vouhcer.
	 * 
	 * @param orcDesp
	 *            orcamentoDepesa.
	 * @param voucher
	 *            voucher.
	 */
	private void sendMailChangeVoucher(final OrcamentoDespesa orcDesp, final Voucher voucher) {
		voucherMail.sendMailChangedVoucher(orcDesp, voucher);
	}

	/**
	 * Dispara email para seguidores ao deletar um vouhcer.
	 * 
	 * @param orcDesp
	 *            orcamentoDepesa.
	 * 
	 * @param voucher
	 *            voucher.
	 */
	private void sendMailDeletionVoucher(final OrcamentoDespesa orcDesp, final Voucher voucher) {
		voucherMail.sendMailDeletedVoucher(orcDesp, voucher);
	}

	/**
	 * Envia email para dono do Travel Budget quando red flag esta setada.
	 * 
	 * @param orcDesp
	 *            orcamento despesa.
	 * @param voucher
	 *            voucher.
	 */
	private void sendMailRedFlag(final OrcamentoDespesa orcDesp, final Voucher voucher) {
		voucherMail.sendMailRedFlag(orcDesp, voucher);
	}
}
