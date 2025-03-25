package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.ControleReajusteStatus;

public class HistoricoControleReajusteRow {

	private ControleReajusteStatus status;
	private ControleReajusteAud auditoria;

	/**
	 * Construtor.
	 * 
	 * @param status
	 * @param auditoria
	 */
	public HistoricoControleReajusteRow(ControleReajusteStatus status,
			ControleReajusteAud auditoria) {
		this.status = status;
		this.auditoria = auditoria;
	}

	/**
	 * @return the status
	 */
	public ControleReajusteStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(ControleReajusteStatus status) {
		this.status = status;
	}

	/**
	 * @return the auditoria
	 */
	public ControleReajusteAud getAuditoria() {
		return auditoria;
	}

	/**
	 * @param auditoria
	 *            the auditoria to set
	 */
	public void setAuditoria(ControleReajusteAud auditoria) {
		this.auditoria = auditoria;
	}

}
