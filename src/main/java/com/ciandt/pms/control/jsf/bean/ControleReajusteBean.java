package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.vo.HistoricoControleReajusteRow;


/**
 * Define o Bean da entidade.
 * 
 * @since 18/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ControleReajusteBean implements Serializable {
    
    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private ControleReajuste to = new ControleReajuste();

    /** Informações do MSA. */
    private Msa msa = new Msa();
    
    /** filter do backingBean. */
    private DocumentoLegal documentoLegal = new DocumentoLegal();

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Lista para combobox */
    private List<String> status = new ArrayList<String>();

    /** Mapa para o combobox com FichaReajusteStatus. */
    private Map<String, Long> statusMap = new HashMap<String, Long>();

    /** Valor selecionado no combo de FichaReajusteStatus. */
    private String statusSelected = null;

    /** Valor selecionado na lista de {@link ControleReajuste} na tela DocumentoLegalConfigure. */
    private Long chosenCodigoControleReajuste;
    
	/** Lista com o historico de auditoria. */
	private List<HistoricoControleReajusteRow> historyList = new ArrayList<HistoricoControleReajusteRow>();

    /**
     * @return the to
     */
    public ControleReajuste getTo() {
    	if (to == null) {
    		to = new ControleReajuste();
    	}
    	
    	return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(final ControleReajuste to) {
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
        this.to = new ControleReajuste();
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
	 * @return the documentoLegal
	 */
	public DocumentoLegal getDocumentoLegal() {
		return documentoLegal;
	}

	/**
	 * @param documentoLegal the documentoLegal to set
	 */
	public void setDocumentoLegal(DocumentoLegal documentoLegal) {
		this.documentoLegal = documentoLegal;
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
	 * @return the chosenCodigoControleReajuste
	 */
	public Long getChosenCodigoControleReajuste() {
		return chosenCodigoControleReajuste;
	}

	/**
	 * @param chosenCodigoControleReajuste the chosenCodigoControleReajuste to set
	 */
	public void setChosenCodigoControleReajuste(
			Long chosenCodigoControleReajuste) {
		this.chosenCodigoControleReajuste = chosenCodigoControleReajuste;
	}

	/**
	 * @return the historyList
	 */
	public List<HistoricoControleReajusteRow> getHistoryList() {
		return historyList;
	}

	/**
	 * @param historyList the historyList to set
	 */
	public void setHistoryList(List<HistoricoControleReajusteRow> historyList) {
		this.historyList = historyList;
	}
	
	
}
