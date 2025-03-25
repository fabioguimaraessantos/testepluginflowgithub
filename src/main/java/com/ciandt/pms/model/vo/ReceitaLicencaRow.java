package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ReceitaLicenca;

import java.util.ArrayList;
import java.util.List;

public class ReceitaLicencaRow {

	private ReceitaLicenca receitaLicenca;
	private Integer installmentNumber;

	/**
	 * @return the receitaLicenca
	 */
	public ReceitaLicenca getReceitaLicenca() {
		return receitaLicenca;
	}
	/**
	 * @param receitaLicenca the receitaLicenca to set
	 */
	public void setReceitaLicenca(ReceitaLicenca receitaLicenca) {
		this.receitaLicenca = receitaLicenca;
	}
	/**
	 * @return the installmentNumber
	 */
	public Integer getInstallmentNumber() {
		return installmentNumber;
	}
	/**
	 * @param installmentNumber the installmentNumber to set
	 */
	public void setInstallmentNumber(Integer installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public static List<ReceitaLicencaRow> toReceitaLicencaRow(final List<ReceitaLicenca> receitaLicencas) {

		List<ReceitaLicencaRow> receitaLicencaRows = new ArrayList<ReceitaLicencaRow>();
		ReceitaLicencaRow receitaLicencaRow = null;
		Integer installmentNumber = 0;
		for (ReceitaLicenca receitaLicenca : receitaLicencas) {

			receitaLicencaRow = new ReceitaLicencaRow();
			installmentNumber++;
			receitaLicencaRow.setInstallmentNumber(installmentNumber);
			receitaLicencaRow.setReceitaLicenca(receitaLicenca);

			receitaLicencaRows.add(receitaLicencaRow);
		}

		return receitaLicencaRows;
	}
}