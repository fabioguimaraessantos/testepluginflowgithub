package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;


/**
 * Define o Bean da entidade.
 * 
 * @since 12/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class FichaReajusteBean implements Serializable {
    
    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private FichaReajuste to = new FichaReajuste();

    /** filter do backingBean. */
    private Msa msa = new Msa();

    /** Lista para o combobox com FichaReajusteIndice. */
    private List<String> indices = new ArrayList<String>();

    /** Mapa para o combobox com FichaReajusteIndice. */
    private Map<String, Long> indicesMap = new HashMap<String, Long>();

    /** Valor selecionado no combo de FichaReajusteIndice. */
    private String indiceSelected = null;

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    private String frequency = null;

    /** Lista para combobox */
    private List<String> status = new ArrayList<String>();

    /** Mapa para o combobox com FichaReajusteStatus. */
    private Map<String, Long> statusMap = new HashMap<String, Long>();

    /** Valor selecionado no combo de FichaReajusteStatus. */
    private String statusSelected = null;
    
    /** Indica se a FichaReajuste precisa ser atribuida a mais de um DocumentoLegal. */
    private Boolean fichaReajuseHasMultipleDocumentosLegais = false;

    /** Id da Ficha de Reajuste que está sendo inativada. */
    private Long inactiveFichaReajusteId = null;
    
    /**
     * @return the to
     */
    public FichaReajuste getTo() {
    	if (to == null) {
    		to = new FichaReajuste();
    	}
    	
    	return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(final FichaReajuste to) {
        this.to = to;
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
    	frequency = null;
    	indiceSelected = null;
    	isUpdate = Boolean.valueOf(false);
        this.to = new FichaReajuste();
    }

	/**
	 * @return the msa
	 */
	public Msa getMsa() {
		return msa;
	}

	/**
	 * @param msa the msa to set
	 */
	public void setMsa(Msa msa) {
		this.msa = msa;
	}

	/**
	 * @return the indices
	 */
	public List<String> getIndices() {
		return indices;
	}

	/**
	 * @param indices the indices to set
	 */
	public void setIndices(List<String> indices) {
		this.indices = indices;
	}

	/**
	 * @return the indicesMap
	 */
	public Map<String, Long> getIndicesMap() {
		return indicesMap;
	}

	/**
	 * @param indicesMap the indicesMap to set
	 */
	public void setIndicesMap(Map<String, Long> indicesMap) {
		this.indicesMap = indicesMap;
	}

	/**
	 * @return the indiceSelected
	 */
	public String getIndiceSelected() {
		return indiceSelected;
	}

	/**
	 * @param indiceSelected the indiceSelected to set
	 */
	public void setIndiceSelected(String indiceSelected) {
		this.indiceSelected = indiceSelected;
	}

	/**
	 * @return the isUpdate
	 */
	public Boolean getIsUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate the isUpdate to set
	 */
	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the status
	 */
	public List<String> getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(List<String> status) {
		this.status = status;
	}

	/**
	 * @return the statusMap
	 */
	public Map<String, Long> getStatusMap() {
		return statusMap;
	}

	/**
	 * @param statusMap the statusMap to set
	 */
	public void setStatusMap(Map<String, Long> statusMap) {
		this.statusMap = statusMap;
	}

	/**
	 * @return the statusSelected
	 */
	public String getStatusSelected() {
		return statusSelected;
	}

	/**
	 * @param statusSelected the statusSelected to set
	 */
	public void setStatusSelected(String statusSelected) {
		this.statusSelected = statusSelected;
	}

	/**
	 * @return the fichaReajuseHasMultipleDocumentosLegais
	 */
	public Boolean getFichaReajuseHasMultipleDocumentosLegais() {
		return fichaReajuseHasMultipleDocumentosLegais;
	}

	/**
	 * @param fichaReajuseHasMultipleDocumentosLegais the fichaReajuseHasMultipleDocumentosLegais to set
	 */
	public void setFichaReajuseHasMultipleDocumentosLegais(
			Boolean fichaReajuseHasMultipleDocumentosLegais) {
		this.fichaReajuseHasMultipleDocumentosLegais = fichaReajuseHasMultipleDocumentosLegais;
	}

	/**
	 * @return the inactiveFichaReajusteId
	 */
	public Long getInactiveFichaReajusteId() {
		return inactiveFichaReajusteId;
	}

	/**
	 * @param inactiveFichaReajusteId the inactiveFichaReajusteId to set
	 */
	public void setInactiveFichaReajusteId(Long inactiveFichaReajusteId) {
		this.inactiveFichaReajusteId = inactiveFichaReajusteId;
	}
}
