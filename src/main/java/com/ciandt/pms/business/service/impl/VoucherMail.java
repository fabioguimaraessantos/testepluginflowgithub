package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ciandt.pms.business.service.IContratoPraticaOrcDespClService;
import com.ciandt.pms.business.service.IFollowClienteService;
import com.ciandt.pms.business.service.IFollowGrupoCustoService;
import com.ciandt.pms.business.service.IFollowOrcamentoDespService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IVoucherMail;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPraticaOrcDespCl;
import com.ciandt.pms.model.FollowCliente;
import com.ciandt.pms.model.FollowGrupoCusto;
import com.ciandt.pms.model.FollowOrcamentoDesp;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Voucher;
import com.ciandt.pms.util.MailSenderUtil;
import com.ciandt.pms.util.VoucherMailTextUtil;

@Component
public class VoucherMail implements IVoucherMail {

	/** Intancia de mailSender. */
	private MailSenderUtil mailSender;

	/** Servico da entidade FollowCliente. */
	@Autowired
	private IFollowClienteService followClienteService;

	/** Servico da entidade FollowCliente. */
	@Autowired
	private IFollowGrupoCustoService followGrupoCustoService;

	/** Servico da entidade FollowCliente. */
	@Autowired
	private IPessoaService pessoaService;

	/** Servico de ContratoPraticaOrcDespCl. */
	@Autowired
	private IContratoPraticaOrcDespClService contratoPraticaOrcDespClService;

	/** Servico de Follow de Orcamento Despesa. */
	@Autowired
	private IFollowOrcamentoDespService followOrcamentoDespService;

	/** getMailSender. */
	public MailSenderUtil getMailSender() {
		return mailSender;
	}

	/** setMailSender. */
	public void setMailSender(final MailSenderUtil mailSender) {
		this.mailSender = mailSender;
	}

	/** Envia email de criacao de Travel Budget de Cliente. */
	public void sendMailCreateClientTravelBudget(final OrcDespesaCl orcDespCl) {
		String subject = VoucherMailTextUtil
				.getSubjectClientTravelBudgetCreated(orcDespCl);
		String message = VoucherMailTextUtil
				.getContentClientTravelBudgetCreated(orcDespCl);
		// Add clobs no email
		List<ContratoPraticaOrcDespCl> cls = contratoPraticaOrcDespClService
				.findByOrcDespesaCl(orcDespCl);
		for (ContratoPraticaOrcDespCl cpodcl : cls) {
			message += cpodcl.getContratoPratica().getNomeContratoPratica()
					+ "<br />";

		}
		this.sendMailToClientFollowers(orcDespCl.getCliente(), subject, message);
	}

	/** Envia email de criacao de Travel Budget de Grupo de Custo. */
	public void sendMailCreateCostGroupTravelBudget(final OrcDespesaGc orcDespGc) {
		String subject = VoucherMailTextUtil
				.getSubjectCostGroupTravelBudgetCreated(orcDespGc);
		String message = VoucherMailTextUtil
				.getContentCostGroupTravelBudgetCreated(orcDespGc);
		this.sendMailToCostGroupFollowers(orcDespGc.getGrupoCusto(), subject,
				message);
	}

	/** Envia email de desabilitacao de Travel Budget de Cliente. */
	public void sendMailDisableClientTravelBudget(final OrcDespesaCl orcDespCl) {
		String subject = VoucherMailTextUtil
				.getSubjectClientTravelBudgetDisabled(orcDespCl);
		String message = VoucherMailTextUtil
				.getContentClientTravelBudgetDisabled(orcDespCl);
		this.sendMailToClientFollowers(orcDespCl.getCliente(), subject, message);
	}

	/** Envia email de desabilitacao de Travel Budget de Grupo de Custo. */
	public void sendMailDisableCostGroupTravelBudget(
			final OrcDespesaGc orcDespGc) {
		String subject = VoucherMailTextUtil
				.getSubjectCostGroupTravelBudgetDisabled(orcDespGc);
		String message = VoucherMailTextUtil
				.getContentCostGroupTravelBudgetDisabled(orcDespGc);
		this.sendMailToCostGroupFollowers(orcDespGc.getGrupoCusto(), subject,
				message);
	}

	/** Envia email de remocao de Travel Budget. */
	public void sendMailDeleteClientTravelBudget(final OrcDespesaCl orcDespCl) {
		String subject = VoucherMailTextUtil
				.getSubjectClientTravelBudgetDeleted(orcDespCl);
		String message = VoucherMailTextUtil
				.getContentClientTravelBudgetDeleted(orcDespCl);
		this.sendMailToClientFollowers(orcDespCl.getCliente(), subject, message);
	}

	/** Envia email de remocao de Travel Budget. */
	public void sendMailDeleteCostGroupTravelBudget(final OrcDespesaGc orcDespGc) {
		String subject = VoucherMailTextUtil
				.getSubjectCostGroupTravelBudgetDeleted(orcDespGc);
		String message = VoucherMailTextUtil
				.getContentCostGroupTravelBudgetDeleted(orcDespGc);
		this.sendMailToCostGroupFollowers(orcDespGc.getGrupoCusto(), subject,
				message);
	}

	/** Envia email de voucher cliente emitido. */
	public void sendMailIssuedClientVoucher(final OrcDespesaCl orcDespCl,
			final Voucher voucher) {
		String subject = VoucherMailTextUtil
				.getSubjectVoucherEmission(orcDespCl.getOrcamentoDespesa());
		String message = VoucherMailTextUtil.getContentClientVoucherEmission(
				orcDespCl, voucher);
		this.sendMailToTravelBudgetFollowers(orcDespCl.getOrcamentoDespesa(),
				subject, message);
	}

	/** Envia email de voucher grupo de custo emitido. */
	public void sendMailIssuedCostGroupVoucher(final OrcDespesaGc orcDespGc,
			final Voucher voucher) {
		String subject = VoucherMailTextUtil
				.getSubjectVoucherEmission(orcDespGc.getOrcamentoDespesa());
		String message = VoucherMailTextUtil
				.getContentCostGroupVoucherEmission(orcDespGc, voucher);
		this.sendMailToTravelBudgetFollowers(orcDespGc.getOrcamentoDespesa(),
				subject, message);
	}

	/** Envia email de voucher open por vários dias. */
	public void sendMailOlderThanDate(final OrcamentoDespesa orcDespesa, final Voucher voucher) {
		String subject = VoucherMailTextUtil.getSubjectOldVoucherOpen(voucher);
		String message = VoucherMailTextUtil
				.getContentMailOldVoucherOpen(voucher);

		this.sendMailToTravelBudgetFollowers(orcDespesa, subject, message);
		this.sendMailToVoucherOwner(voucher, subject, message);

	}

	/** Envia email de voucher alterado. */
	public void sendMailChangedVoucher(final OrcamentoDespesa travelBudget,
			final Voucher voucher) {
		String subject = VoucherMailTextUtil.getSubjectVoucherChanged(
				travelBudget, voucher);
		String message = VoucherMailTextUtil.getContentVoucherChanged(
				travelBudget, voucher);
		this.sendMailToTravelBudgetFollowers(travelBudget, subject, message);
	}

	/** Envia email de voucher removido. */
	public void sendMailDeletedVoucher(final OrcamentoDespesa travelBudget,
			final Voucher voucher) {
		String subject = VoucherMailTextUtil
				.getSubjectVoucherDeleted(travelBudget);
		String message = VoucherMailTextUtil.getContentVoucherDeleted(
				travelBudget, voucher);
		this.sendMailToTravelBudgetFollowers(travelBudget, subject, message);
	}

	/** Envia email para o criador do travel budget. */
	public void sendMailRedFlag(final OrcamentoDespesa travelBudget,
			final Voucher voucher) {

		String subject = VoucherMailTextUtil
				.getSubjectMailRedFlag(travelBudget);
		String message = VoucherMailTextUtil.getContentMailRedFlag(voucher);

		this.sendMailToOwnerTravelBudget(travelBudget, subject, message);

	}

	/**
	 * Dispara email para seguidores de cliente.
	 * 
	 * @param cliente
	 * @param subject
	 * @param message
	 */
	private void sendMailToClientFollowers(final Cliente cliente,
			final String subject, final String message) {
		Pessoa pessoa = null;
		for (FollowCliente followCliente : this.getClientFollowers(cliente)) {
			pessoa = pessoaService.findPessoaByLogin(followCliente
					.getCodigoLogin());
			mailSender.sendHtmlMail(pessoa.getTextoEmail(), subject, message);
		}
	}

	/**
	 * Dispara email para seguidores de Grupo de Custo.
	 * 
	 * @param costGroup
	 * @param subject
	 * @param message
	 */
	private void sendMailToCostGroupFollowers(final GrupoCusto costGroup,
			final String subject, final String message) {
		Pessoa pessoa = null;
		for (FollowGrupoCusto followGrupoCusto : this
				.getCostGroupFollowers(costGroup)) {
			pessoa = pessoaService.findPessoaByLogin(followGrupoCusto
					.getCodigoLogin());
			mailSender.sendHtmlMail(pessoa.getTextoEmail(), subject, message);
		}
	}

	/**
	 * Envia email para seguidores de Travel Budget.
	 * 
	 * @param travelBudget
	 * @param subject
	 * @param message
	 */
	private void sendMailToTravelBudgetFollowers(OrcamentoDespesa travelBudget,
			final String subject, final String message) {

		// Entidade Pessoa
		Pessoa pessoa = null;

		// Percorre lista, busca pessoa e dispara email
		for (FollowOrcamentoDesp followOrcDesp : this
				.getTravelBudgetFollowers(travelBudget)) {

			pessoa = pessoaService.findPessoaByLogin(followOrcDesp
					.getCodigoLogin());

			mailSender.sendHtmlMail(pessoa.getTextoEmail(), subject, message);
		}
	}

	/**
	 * Envia email para o criador do {@link Voucher}.
	 * 
	 * @param voucher
	 * @param subject
	 * @param message
	 */
	private void sendMailToVoucherOwner(Voucher voucher, final String subject, final String message) {
		Pessoa pessoa = pessoaService.findPessoaById(voucher.getPessoa().getCodigoPessoa());
		
		mailSender.sendHtmlMail(pessoa.getTextoEmail(), subject, message);
	}

	/**
	 * Envia email para o dono do travel budget.
	 * 
	 * @param travelBudget
	 * @param subject
	 * @param message
	 */
	private void sendMailToOwnerTravelBudget(
			final OrcamentoDespesa travelBudget, final String subject,
			final String message) {
		Pessoa owner = pessoaService.findPessoaByLogin(travelBudget
				.getLoginCriador());

		mailSender.sendHtmlMail(owner.getTextoEmail(), subject, message);
	}

	/**
	 * Lista de seguidores de cliente.
	 * 
	 * @param cliente
	 * @return
	 */
	private List<FollowCliente> getClientFollowers(final Cliente cliente) {
		return followClienteService.findByCliente(cliente);
	}

	/**
	 * Lista de seguidores de grupo de custo.
	 * 
	 * @param grupoCusto
	 * @return
	 */
	private List<FollowGrupoCusto> getCostGroupFollowers(
			final GrupoCusto grupoCusto) {
		return followGrupoCustoService.findByGrupoCusto(grupoCusto);
	}

	/**
	 * Lista de seguidores de Travel Budget.
	 * 
	 * @param travel
	 *            buget
	 * @return
	 */
	private List<FollowOrcamentoDesp> getTravelBudgetFollowers(
			final OrcamentoDespesa travelBudget) {
		return followOrcamentoDespService.findByOrcDespesa(travelBudget);
	}

}
