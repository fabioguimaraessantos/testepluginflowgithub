package com.ciandt.pms.business.service;

import org.springframework.stereotype.Component;

import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Voucher;

@Component
public interface IVoucherMail {

	/** Envia email de criacao de Travel Budget de Cliente. */
	void sendMailCreateClientTravelBudget(final OrcDespesaCl orcDespCl);

	/** Envia email de criacao de Travel Budget de Grupo de Custo. */
	void sendMailCreateCostGroupTravelBudget(final OrcDespesaGc orcDespGc);

	/** Envia email de desabilitacao de Travel Budget de Cliente. */
	void sendMailDisableClientTravelBudget(final OrcDespesaCl orcDespCl);

	/** Envia email de desabilitacao de Travel Budget de Grupo de Custo. */
	void sendMailDisableCostGroupTravelBudget(final OrcDespesaGc orcDespGc);

	/** Envia email de remocao de Travel Budget. */
	void sendMailDeleteClientTravelBudget(final OrcDespesaCl orcDespCl);

	/** Envia email de remocao de Travel Budget. */
	void sendMailDeleteCostGroupTravelBudget(final OrcDespesaGc orcDespGc);

	/** Envia email de voucher de cliente emitido. */
	void sendMailIssuedClientVoucher(final OrcDespesaCl orcDespCl,
			final Voucher voucher);

	/** Envia email de voucher de grupo de custo emitido. */
	void sendMailIssuedCostGroupVoucher(final OrcDespesaGc orcDespGc,
			final Voucher voucher);

	/** Envia email de voucher alterado. */
	void sendMailChangedVoucher(final OrcamentoDespesa travelBudget,
			final Voucher voucher);

	/** Envia email de voucher removido. */
	void sendMailDeletedVoucher(final OrcamentoDespesa travelBudget,
			final Voucher voucher);

	/** Envia email para o criador do travel budget. */
	void sendMailRedFlag(final OrcamentoDespesa travelBudget,
			final Voucher voucher);

	/** Envia email de voucher open por vários dias. */
	void sendMailOlderThanDate(final OrcamentoDespesa orcDespesa,
			final Voucher voucher);
}
