package com.ciandt.pms.util;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Voucher;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VoucherMailTextUtil {

	/**
	 * 
	 */
	private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Retorna o assunto do email de criacao de travel budget para cliente.
	 * 
	 * @param orcDespCl
	 * @return
	 */
	public static String getSubjectClientTravelBudgetCreated(
			OrcDespesaCl orcDespCl) {
		return BundleUtil.getBundle(
				Constants.EMAIL_ASSUNTO_CREATE_TRAVEL_BUDGET_CL, orcDespCl
						.getCliente().getNomeCliente());
	}

	/**
	 * Retorna o conteudo do email de criacao de TravelBudget para cliente.
	 * 
	 * @param orcDespCl
	 * @return
	 */
	public static String getContentClientTravelBudgetCreated(
			final OrcDespesaCl orcDespCl) {

		// formata o tipo de orçamento(Reembolsavel/Nao Reembolsavel)
		String tipoOrcamento = BundleUtil
				.getBundle("_nls.travel.budget.label.reembolsavel");

		if (!orcDespCl.getIsReembolsavel()) {
			tipoOrcamento = BundleUtil
					.getBundle("_nls.travel.budget.label.not_reembolsavel");
		}

		OrcamentoDespesa orcamentoDespesa = orcDespCl.getOrcamentoDespesa();
		
		Date dataIni = orcamentoDespesa.getDataInicio();
		String dataInicio = DateUtil.getStringDate(dataIni, SDF);

		Date dataFim = orcamentoDespesa.getDataFim();
		String dataFinal = DateUtil.getStringDate(dataFim, SDF);
		
		String indicadorExtra = orcDespCl.getIndicadorOrcamentoExtra();
		String extra = Constants.SIM.equals(indicadorExtra) ? Constants.BUNDLE_EXTRA_LABEL
				: "";

		String nomeOrcDespesa = orcamentoDespesa.getNomeOrcDespesa();
		String nomeCliente = orcDespCl.getCliente().getNomeCliente();
		String siglaMoeda = orcamentoDespesa.getMoeda().getSiglaMoeda();
		String username = LoginUtil.getLoggedUsername();
		BigDecimal valorOrcado = orcamentoDespesa.getValorOrcado();

		String content = BundleUtil.getBundle(
				Constants.EMAIL_MSG_CREATE_TRAVEL_BUDGET_CL, extra,
				nomeOrcDespesa, tipoOrcamento, siglaMoeda, valorOrcado,
				nomeCliente, dataInicio, dataFinal, username);

		return content;
	}

	/**
	 * Retorna o assunto do email de criacao de travel budget para grupo de
	 * custo.
	 * 
	 * @param orcDespGc
	 * @return
	 */
	public static String getSubjectCostGroupTravelBudgetCreated(
			OrcDespesaGc orcDespGc) {
		return BundleUtil.getBundle(
				Constants.EMAIL_ASSUNTO_CREATE_TRAVEL_BUDGET_GC, orcDespGc
						.getGrupoCusto().getNomeGrupoCusto());
	}

	/**
	 * Retorna o conteudo do email de criacao de TravelBudget para Grupo de
	 * custo.
	 * 
	 * @param orcDespGc
	 * @return
	 */
	public static String getContentCostGroupTravelBudgetCreated(
			final OrcDespesaGc orcDespGc) {
		String dataInicio = DateUtil.getStringDate(orcDespGc
				.getOrcamentoDespesa().getDataInicio(), SDF);
		String dataFim = DateUtil.getStringDate(orcDespGc.getOrcamentoDespesa()
				.getDataFim(), SDF);

		return BundleUtil.getBundle(
				Constants.EMAIL_MSG_CREATE_TRAVEL_BUDGET_GC, orcDespGc
						.getOrcamentoDespesa().getNomeOrcDespesa(), orcDespGc
						.getOrcamentoDespesa().getMoeda().getSiglaMoeda(),
				orcDespGc.getOrcamentoDespesa().getValorOrcado(), orcDespGc
						.getGrupoCusto().getNomeGrupoCusto(), dataInicio,
				dataFim, LoginUtil.getLoggedUsername());
	}

	/**
	 * 
	 * @param orcDespCl
	 * @return
	 */
	public static String getSubjectClientTravelBudgetDisabled(
			OrcDespesaCl orcDespCl) {
		return BundleUtil.getBundle(
				Constants.EMAIL_ASSUNTO_DISABLE_TRAVEL_BUDGET_CL, orcDespCl
						.getCliente().getNomeCliente());
	}

	/**
	 * 
	 * @param orcDespCl
	 * @return
	 */
	public static String getContentClientTravelBudgetDisabled(
			OrcDespesaCl orcDespCl) {
		return BundleUtil.getBundle(
				Constants.EMAIL_MSG_DISABLE_TRAVEL_BUDGET_CL, orcDespCl
						.getOrcamentoDespesa().getNomeOrcDespesa(), orcDespCl
						.getCliente().getNomeCliente(), LoginUtil
						.getLoggedUsername());
	}

	/**
	 * Retorna o assunto do email de criacao de travel budget para grupo de
	 * custo.
	 * 
	 * @param orcDespGc
	 * @return
	 */
	public static String getSubjectCostGroupTravelBudgetDisabled(
			OrcDespesaGc orcDespGc) {
		return BundleUtil.getBundle(
				Constants.EMAIL_ASSUNTO_DISABLE_TRAVEL_BUDGET_GC, orcDespGc
						.getGrupoCusto().getNomeGrupoCusto());
	}

	/**
	 * 
	 * @param orcDespGc
	 * @return
	 */
	public static String getContentCostGroupTravelBudgetDisabled(
			OrcDespesaGc orcDespGc) {
		return BundleUtil.getBundle(
				Constants.EMAIL_MSG_DISABLE_TRAVEL_BUDGET_GC, orcDespGc
						.getOrcamentoDespesa().getNomeOrcDespesa(), orcDespGc
						.getGrupoCusto().getNomeGrupoCusto(), LoginUtil
						.getLoggedUsername());
	}

	/**
	 * 
	 * @param orcDespCl
	 * @return
	 */
	public static String getSubjectClientTravelBudgetDeleted(
			OrcDespesaCl orcDespCl) {
		return BundleUtil.getBundle(
				Constants.EMAIL_ASSUNTO_DELETE_TRAVEL_BUDGET_CL, orcDespCl
						.getCliente().getNomeCliente());
	}

	/**
	 * 
	 * @param orcDespCl
	 * @return
	 */
	public static String getContentClientTravelBudgetDeleted(
			OrcDespesaCl orcDespCl) {
		return BundleUtil.getBundle(
				Constants.EMAIL_MSG_DELETE_TRAVEL_BUDGET_CL, orcDespCl
						.getOrcamentoDespesa().getNomeOrcDespesa(), orcDespCl
						.getCliente().getNomeCliente(), LoginUtil
						.getLoggedUsername());
	}

	/**
	 * 
	 * @param orcDespGc
	 * @return
	 */
	public static String getSubjectCostGroupTravelBudgetDeleted(
			OrcDespesaGc orcDespGc) {
		return BundleUtil.getBundle(
				Constants.EMAIL_ASSUNTO_DELETE_TRAVEL_BUDGET_GC, orcDespGc
						.getGrupoCusto().getNomeGrupoCusto());
	}

	/**
	 * 
	 * @param orcDespGc
	 * @return
	 */
	public static String getContentCostGroupTravelBudgetDeleted(
			OrcDespesaGc orcDespGc) {
		return BundleUtil.getBundle(
				Constants.EMAIL_MSG_DELETE_TRAVEL_BUDGET_GC, orcDespGc
						.getOrcamentoDespesa().getNomeOrcDespesa(), orcDespGc
						.getGrupoCusto().getNomeGrupoCusto(), LoginUtil
						.getLoggedUsername());
	}

	/**
	 * Assunto email de emissao de voucher.
	 * 
	 * @param orcDesp
	 * @return
	 */
	public static String getSubjectVoucherEmission(
			final OrcamentoDespesa orcDesp) {
		return BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_EMISSAO_VOUCHER,
				orcDesp.getNomeOrcDespesa());
	}

	/**
	 * Conteudo email emissao de voucher para cliente.
	 * 
	 * @param orcDespCl
	 * @param voucher
	 * @return
	 */
	public static String getContentClientVoucherEmission(
			OrcDespesaCl orcDespCl, Voucher voucher) {
		return BundleUtil.getBundle(Constants.EMAIL_MSG_EMISSAO_VOUCHER,
				voucher.getNumeroVoucher(), voucher.getSiglaMoeda(), voucher.getValorIssued(),
				orcDespCl.getOrcamentoDespesa().getNomeOrcDespesa(), "client",
				orcDespCl.getCliente().getNomeCliente(), voucher.getPessoa()
						.getCodigoLogin());
	}

	/**
	 * Conteudo email emissao de voucher para grupo de custo.
	 * 
	 * @param orcDespGc
	 * @param voucher
	 * @return
	 */
	public static String getContentCostGroupVoucherEmission(
			OrcDespesaGc orcDespGc, Voucher voucher) {
		return BundleUtil.getBundle(Constants.EMAIL_MSG_EMISSAO_VOUCHER,
				voucher.getNumeroVoucher(), voucher.getSiglaMoeda(), voucher.getValorIssued(),
				orcDespGc.getOrcamentoDespesa().getNomeOrcDespesa(),
				"cost group", orcDespGc.getGrupoCusto().getNomeGrupoCusto(),
				voucher.getPessoa().getCodigoLogin());
	}

	/**
	 * Assunto email delecao de voucher.
	 * 
	 * @param orcDesp
	 * @return
	 */
	public static String getSubjectVoucherDeleted(OrcamentoDespesa orcDesp) {
		return BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_DELECAO_VOUCHER,
				orcDesp.getNomeOrcDespesa());
	}

	/**
	 * Conteudo email delecao de voucher.
	 * 
	 * @param orcDesp
	 * @param voucher
	 * @return
	 */
	public static String getContentVoucherDeleted(OrcamentoDespesa orcDesp,
			Voucher voucher) {
		return BundleUtil.getBundle(Constants.EMAIL_MSG_DELECAO_VOUCHER,
				voucher.getNumeroVoucher(), orcDesp.getNomeOrcDespesa());
	}

	/**
	 * Assunto email de voucher alterado.
	 * 
	 * @param orcDesp
	 * @param voucher
	 * @return
	 */
	public static String getSubjectVoucherChanged(OrcamentoDespesa orcDesp,
			Voucher voucher) {
		return BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_ALTERACAO_VOUCHER,
				orcDesp.getNomeOrcDespesa(), voucher.getCurrentStatusName());
	}

	/**
	 * Conteudo email voucher alterado.
	 * 
	 * @param orcDesp
	 * @param voucher
	 * @return
	 */
	public static String getContentVoucherChanged(OrcamentoDespesa orcDesp,
			Voucher voucher) {
		return BundleUtil.getBundle(Constants.EMAIL_MSG_ALTERACAO_VOUCHER,
				voucher.getNumeroVoucher(), voucher.getSiglaMoeda(),
				voucher.getValorIssued(), orcDesp.getNomeOrcDespesa(), voucher
						.getCurrentStatusName(), voucher.getValueFromCurrentStatus());
	}

	/**
	 * Email de RedFlag
	 * 
	 * @param orcDesp
	 * @return
	 */
	public static String getSubjectMailRedFlag(OrcamentoDespesa orcDesp) {
		return BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_REDFLAG_VOUCHER,
				orcDesp.getNomeOrcDespesa());
	}

	/**
	 * Email de RedFlag
	 * 
	 * @param voucher
	 * @return
	 */
	public static String getContentMailRedFlag(Voucher voucher) {
		return BundleUtil.getBundle(Constants.EMAIL_MSG_REDFLAG_VOUCHER,
				voucher.getNumeroVoucher(), voucher.getDataCreation(), voucher
						.getPessoa().getCodigoLogin());
	}

	public static String getSubjectOldVoucherOpen(Voucher voucher) {
		return BundleUtil.getBundle(
				Constants.EMAIL_ASSUNTO_VOUCHER_OPEN_VARIOUS_DAYS,
				voucher.getNumeroVoucher());
	}

	public static String getContentMailOldVoucherOpen(Voucher voucher) {
		return BundleUtil.getBundle(
				Constants.EMAIL_MSG_VOUCHER_OPEN_VARIOUS_DAYS,
				voucher.getNumeroVoucher(), voucher.getDataCreation(),
				voucher.getValueFromCurrentStatus());
	}

	public static String getSubjectOpenVoucherVariousDays(final Integer days) {
		return BundleUtil.getBundle(
				Constants.EMAIL_MSG_VOUCHER_OPEN_VARIOUS_DAYS, days);
	}

	public static String getSubjectVoucherOpenOrcDesp(final Integer days) {
		return BundleUtil.getBundle(Constants.EMAIL_MSG_VOUCHER_OPEN_ORC_DESP,
				days);
	}
}
