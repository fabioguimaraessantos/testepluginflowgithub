package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.enums.StatusDocumentoLegal;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.DocumentoLegalResponsavel;
import com.ciandt.pms.model.MsaContrato;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define o BackingBean da entidade DocumentoLegal.
 * 
 * @since 14/11/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class DocumentoLegalBean implements Serializable {

	/** Defaul serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Flag que verifica se é um update. */
	private Boolean isUpdate = Boolean.FALSE;

	/** to do backingBean. */
	private DocumentoLegal to = new DocumentoLegal();

	/** to do backingBean para exclusão. */
	private DocumentoLegal docLegalToDelete = new DocumentoLegal();

	/** Lista de resultados. */
	private List<DocumentoLegal> resultList = new ArrayList<DocumentoLegal>();

	/** Map para combobox de tipo documento legal. */
	private Map<String, Long> mapDocumentoLegalTipo = new HashMap<String, Long>();

	/** Lista de tipo documento legal para combobox. */
	private List<String> documentoLegalTipoComboList = new ArrayList<String>();

	/** String para tipo documento legal que vem da view. */
	private String documentoLegalTipo;
	
	/** Map para combobox de status documento legal. */
	private Map<String, String> mapDocumentoLegalStatus = new HashMap<String, String>();

	/** Lista de status documento legal para combobox. */
	private List<String> documentoLegalStatusComboList = new ArrayList<String>();

	/** String para status documento legal que vem da view. */
	private String documentoLegalStatus;

	/** Flag que indica se pode remover o documento legal */
	private boolean canRemove = Boolean.valueOf(true);

	/** String para a inativação de um legal doc */
	private String inactivateComment;
	
	/** Map para combobox de pessoa. */
	private Map<Long, String> pessoaComboMap = new HashMap<Long, String>();
	
	/** Lista de pessoa para combobox. */
	private List<String> pessoaComboList = new ArrayList<String>();
	
	/** valor de pessoa para combobox. */
	private String pessoaComboValue = "";
	
	/** responsavel para remover. */
	private DocumentoLegalResponsavel documentoLegalResponsavelToRemove = new DocumentoLegalResponsavel();

	/**
	 * Reseta o to do bean.
	 */
	public void resetTo() {
		this.to = new DocumentoLegal();
		this.docLegalToDelete = new DocumentoLegal();
		//this.getTo().setSiglaRenovacaoAutomatica(Constants.SIM);
		this.documentoLegalStatus = StatusDocumentoLegal.ACTIVE.getName();
		this.documentoLegalTipo = "";
	}

	/**
	 * Reseta o to do bean.
	 */
	public void reset() {
		this.resetTo();
		this.isUpdate = Boolean.valueOf(false);
		this.canRemove = Boolean.valueOf(true);
		this.inactivateComment = "";
		this.pessoaComboValue = "";
	}

	/**
	 * @return the isUpdate
	 */
	public Boolean getIsUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate
	 *            the isUpdate to set
	 */
	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @return the to
	 */
	public DocumentoLegal getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(DocumentoLegal to) {
		this.to = to;
	}

	/**
	 * @return the docLegalToDelete
	 */
	public DocumentoLegal getDocLegalToDelete() {
		return docLegalToDelete;
	}

	/**
	 * @param docLegalToDelete
	 *            the docLegalToDelete to set
	 */
	public void setDocLegalToDelete(DocumentoLegal docLegalToDelete) {
		this.docLegalToDelete = docLegalToDelete;
	}

	/**
	 * @return the resultList
	 */
	public List<DocumentoLegal> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(List<DocumentoLegal> resultList) {
		this.resultList = resultList;
	}

	/**
	 * @return the mapDocumentoLegalTipo
	 */
	public Map<String, Long> getMapDocumentoLegalTipo() {
		return mapDocumentoLegalTipo;
	}

	/**
	 * @param mapDocumentoLegalTipo
	 *            the mapDocumentoLegalTipo to set
	 */
	public void setMapDocumentoLegalTipo(Map<String, Long> mapDocumentoLegalTipo) {
		this.mapDocumentoLegalTipo = mapDocumentoLegalTipo;
	}

	/**
	 * @return the documentoLegalTipoComboList
	 */
	public List<String> getDocumentoLegalTipoComboList() {
		return documentoLegalTipoComboList;
	}

	/**
	 * @param documentoLegalTipoComboList
	 *            the documentoLegalTipoComboList to set
	 */
	public void setDocumentoLegalTipoComboList(
			List<String> documentoLegalTipoComboList) {
		this.documentoLegalTipoComboList = documentoLegalTipoComboList;
	}

	/**
	 * @return the documentoLegalTipo
	 */
	public String getDocumentoLegalTipo() {
		return documentoLegalTipo;
	}

	/**
	 * @param documentoLegalTipo
	 *            the documentoLegalTipo to set
	 */
	public void setDocumentoLegalTipo(String documentoLegalTipo) {
		this.documentoLegalTipo = documentoLegalTipo;
	}

	/**
	 * @return the mapDocumentoLegalStatus
	 */
	public Map<String, String> getMapDocumentoLegalStatus() {
		return mapDocumentoLegalStatus;
	}

	/**
	 * @param mapDocumentoLegalStatus
	 *            the mapDocumentoLegalStatus to set
	 */
	public void setMapDocumentoLegalStatus(
			Map<String, String> mapDocumentoLegalStatus) {
		this.mapDocumentoLegalStatus = mapDocumentoLegalStatus;
	}

	/**
	 * @return the documentoLegalStatusComboList
	 */
	public List<String> getDocumentoLegalStatusComboList() {
		return documentoLegalStatusComboList;
	}

	/**
	 * @param documentoLegalStatusComboList
	 *            the documentoLegalStatusComboList to set
	 */
	public void setDocumentoLegalStatusComboList(
			List<String> documentoLegalStatusComboList) {
		this.documentoLegalStatusComboList = documentoLegalStatusComboList;
	}

	/**
	 * @return the documentoLegalStatus
	 */
	public String getDocumentoLegalStatus() {
		return documentoLegalStatus;
	}

	/**
	 * @param documentoLegalStatus
	 *            the documentoLegalStatus to set
	 */
	public void setDocumentoLegalStatus(String documentoLegalStatus) {
		this.documentoLegalStatus = documentoLegalStatus;
	}

	/**
	 * @return the canRemove
	 */
	public boolean isCanRemove() {
		return canRemove;
	}

	/**
	 * @param canRemove
	 *            the canRemove to set
	 */
	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	/**
	 * @return the inactivateComment
	 */
	public String getInactivateComment() {
		return inactivateComment;
	}

	/**
	 * @param inactivateComment
	 *            the inactivateComment to set
	 */
	public void setInactivateComment(String inactivateComment) {
		this.inactivateComment = inactivateComment;
	}

	/**
	 * @return the pessoaComboMap
	 */
	public Map<Long, String> getPessoaComboMap() {
		return pessoaComboMap;
	}

	/**
	 * @param pessoaComboMap the pessoaComboMap to set
	 */
	public void setPessoaComboMap(Map<Long, String> pessoaComboMap) {
		this.pessoaComboMap = pessoaComboMap;
	}

	/**
	 * @return the pessoaComboList
	 */
	public List<String> getPessoaComboList() {
		return pessoaComboList;
	}

	/**
	 * @param pessoaComboList the pessoaComboList to set
	 */
	public void setPessoaComboList(List<String> pessoaComboList) {
		this.pessoaComboList = pessoaComboList;
	}

	/**
	 * @return the pessoaComboValue
	 */
	public String getPessoaComboValue() {
		return pessoaComboValue;
	}

	/**
	 * @param pessoaComboValue the pessoaComboValue to set
	 */
	public void setPessoaComboValue(String pessoaComboValue) {
		this.pessoaComboValue = pessoaComboValue;
	}

	/**
	 * @return the documentoLegalResponsavelToRemove
	 */
	public DocumentoLegalResponsavel getDocumentoLegalResponsavelToRemove() {
		return documentoLegalResponsavelToRemove;
	}

	/**
	 * @param documentoLegalResponsavelToRemove the documentoLegalResponsavelToRemove to set
	 */
	public void setDocumentoLegalResponsavelToRemove(
			DocumentoLegalResponsavel documentoLegalResponsavelToRemove) {
		this.documentoLegalResponsavelToRemove = documentoLegalResponsavelToRemove;
	}
}