package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.vo.ProcessoRow;
import com.ciandt.pms.model.vo.ValidacaoPessoaRow;

/**
 * Define o BackingBean da entidade.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class DreProcessosFechamentoBean extends Thread implements Observer,
		Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** to do backingBean. */
	private DreProcesso to = new DreProcesso();

	/** representa o mes selecionado. */
	private String monthBeg;

	/** representa o ano selecionado. */
	private String yearBeg;

	/** StringBuffer do log da validação. */
	private String logValidate = new Date()
			+ " - Starting the Validation Process... \n";

	/** Lista de resultados da entidade Pessoa. */
	private List<ValidacaoPessoaRow> pessoaRowList = new ArrayList<ValidacaoPessoaRow>();

	/** Lista de resultados da entidade Processo. */
	private List<ProcessoRow> processoRowList = new ArrayList<ProcessoRow>();

	/** Indicador se o botao ficara desabilidato. */
	private Boolean btnRegisterDisable = Boolean.valueOf(false);

	/** Indicador do valor da barra de progresso. */
	private float currentValue;

	private Long countPessoaNotValidatedByDate;

	private String lastStatusDREProcess;

	private boolean enabled = false;

	private String msgError;
	
	private String msgSuccess;

	@Override
	public void update(Observable o, Object arg) {
		try {
			this.setCurrentValue((Float) arg);
		} catch (Exception e) {
			this.setCurrentValue(100);
		}
	}

	/**
	 * @return the currentValue
	 */
	public float getCurrentValue() {
		return currentValue;
	}


	public Long getCountPessoaNotValidatedByDate() { return countPessoaNotValidatedByDate;}

	public void setCountPessoaNotValidatedByDate(final Long countPessoaNotValidatedByDate) {
		this.countPessoaNotValidatedByDate = countPessoaNotValidatedByDate;
	}

	public String getLastStatusDREProcess() { return lastStatusDREProcess;}

	public void setLastStatusDREProcess( String lastStatusDREProcess) {
		this.lastStatusDREProcess = lastStatusDREProcess;
	}

	/**
	 * @return the currentValueRounded
	 */
	public float getValueRounded() {
		return Math.round(currentValue);
	}

	/**
	 * @param currentValue
	 *            the currentValue to set
	 */
	public void setCurrentValue(final float currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * @return the pessoaRowList
	 */
	public List<ValidacaoPessoaRow> getPessoaRowList() {
		return pessoaRowList;
	}

	/**
	 * @param pessoaRowList
	 *            the pessoaRowList to set
	 */
	public void setPessoaRowList(final List<ValidacaoPessoaRow> pessoaRowList) {
		this.pessoaRowList = pessoaRowList;
	}

	/**
	 * @return the processoRowList
	 */
	public List<ProcessoRow> getProcessoRowList() {
		return processoRowList;
	}

	/**
	 * @param processoRowList
	 *            the processoRowList to set
	 */
	public void setProcessoRowList(List<ProcessoRow> processoRowList) {
		this.processoRowList = processoRowList;
	}

	/**
	 * @return the to
	 */
	public DreProcesso getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(DreProcesso to) {
		this.to = to;
	}

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		resetTo();
		resetValidityDate();
		this.enabled = Boolean.valueOf(false);
		this.currentValue = new Float(0d);
		this.processoRowList = null;
		this.msgError = "";
		this.msgSuccess = "";
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new DreProcesso();
	}

	/**
	 * Reseta a data do periodo da vigencia.
	 */
	public void resetValidityDate() {
		this.monthBeg = null;
		this.yearBeg = null;
	}

	/**
	 * @param monthBeg
	 *            the monthBeg to set
	 */
	public void setMonthBeg(final String monthBeg) {
		this.monthBeg = monthBeg;
	}

	/**
	 * @return the monthBeg
	 */
	public String getMonthBeg() {
		return monthBeg;
	}

	/**
	 * @param yearBeg
	 *            the yearBeg to set
	 */
	public void setYearBeg(final String yearBeg) {
		this.yearBeg = yearBeg;
	}

	/**
	 * @return the yearBeg
	 */
	public String getYearBeg() {
		return yearBeg;
	}

	/**
	 * @return the logValidate
	 */
	public String getLogValidate() {
		return logValidate;
	}

	/**
	 * @param logValidate
	 *            the logValidate to set
	 */
	public void setLogValidate(final String logValidate) {
		this.logValidate += (new Date() + " - Validating - " + logValidate + "\n");
	}

	/**
	 * @param btnRegisterDisable
	 *            the btnRegisterDisable to set
	 */
	public void setBtnRegisterDisable(final Boolean btnRegisterDisable) {
		this.btnRegisterDisable = btnRegisterDisable;
	}

	/**
	 * @return the btnRegisterDisable
	 */
	public Boolean getBtnRegisterDisable() {
		return btnRegisterDisable;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the msgError
	 */
	public String getMsgError() {
		return msgError;
	}

	/**
	 * @param msgError the msgError to set
	 */
	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	/**
	 * @return the msgSuccess
	 */
	public String getMsgSuccess() {
		return msgSuccess;
	}

	/**
	 * @param msgSuccess the msgSuccess to set
	 */
	public void setMsgSuccess(String msgSuccess) {
		this.msgSuccess = msgSuccess;
	}
}