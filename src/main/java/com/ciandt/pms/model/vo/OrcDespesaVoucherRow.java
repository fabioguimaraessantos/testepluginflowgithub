package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Voucher;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma linha do email de Voucher abertos a mais de x dias.
 * 
 * @author luizsj
 */
public class OrcDespesaVoucherRow {

	/** OrcamentoDespesa */
	private OrcamentoDespesa orcDespesa = new OrcamentoDespesa();

	/** Voucher */
	private List<Voucher> vouchers = new ArrayList<Voucher>();

	/**
	 * @return the orcDespesa
	 */
	public OrcamentoDespesa getOrcDespesa() {
		return orcDespesa;
	}

	/**
	 * @param orcDespesa
	 *            the orcDespesa to set
	 */
	public void setOrcDespesa(OrcamentoDespesa orcDespesa) {
		this.orcDespesa = orcDespesa;
	}

	/**
	 * @return the vouchers
	 */
	public List<Voucher> getVouchers() {
		return vouchers;
	}

	/**
	 * @param vouchers the vouchers to set
	 */
	public void setVouchers(List<Voucher> vouchers) {
		this.vouchers = vouchers;
	}
}
