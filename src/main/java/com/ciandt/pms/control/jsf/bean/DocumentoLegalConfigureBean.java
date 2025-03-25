package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.richfaces.model.UploadItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.DocumentoLegalAnexoArquivo;
import com.ciandt.pms.model.FichaReajuste;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class DocumentoLegalConfigureBean implements Serializable {

	/** Defaul serial version UID. */
	private static final long serialVersionUID = 1L;

    /** Lista para o combobox com FichaReajuste. */
    private List<String> fichaReajustes = new ArrayList<String>();
    
    /** Lista para o combobox com FichaReajuste. */
    private Map<String, Long> fichaReajustesMap = new HashMap<String, Long>();
    
    /** Valor selecionado no combo de FichaReajuste. */
    private String fichaSelected = "";
    
    /** FichaReajuste. */
    private FichaReajuste fichaReajuste = new FichaReajuste();

	/** Lista de anexos. */
	private List<DocumentoLegalAnexoArquivo> filesList = new ArrayList<DocumentoLegalAnexoArquivo>();

	/** Item a ser uppado. */
	private UploadItem uploadItem;
	
	/** Comentario do arquivo a ser uppado*/
	private String textoComentarioAnexo;

	/** DocumentoLegalAnexoArquivo. */
	private DocumentoLegalAnexoArquivo documentoLegalAnexoArquivo = new DocumentoLegalAnexoArquivo();

	/** Lista de {@link ControleReajuste} do {@link DocumentoLegal}. */
	private List<ControleReajuste> controlesReajuste = new ArrayList<ControleReajuste>();

    /**
     * Reseta o bean.
     */
    public void reset() {
    	this.textoComentarioAnexo = null;
    	this.fichaReajuste = null;
    }

	/**
	 * @return the filesList
	 */
	public List<DocumentoLegalAnexoArquivo> getFilesList() {
		return filesList;
	}

	/**
	 * @param filesList
	 *            the filesList to set
	 */
	public void setFilesList(List<DocumentoLegalAnexoArquivo> filesList) {
		this.filesList = filesList;
	}

	/**
	 * @return the uploadItem
	 */
	public UploadItem getUploadItem() {
		return uploadItem;
	}

	/**
	 * @param uploadItem
	 *            the uploadItem to set
	 */
	public void setUploadItem(UploadItem uploadItem) {
		this.uploadItem = uploadItem;
	}
	
	/**
	 * @return the textoComentarioAnexo
	 */
	public String getTextoComentarioAnexo() {
		return textoComentarioAnexo;
	}

	/**
	 * @param textoComentarioAnexo the textoComentarioAnexo to set
	 */
	public void setTextoComentarioAnexo(String textoComentarioAnexo) {
		this.textoComentarioAnexo = textoComentarioAnexo;
	}

	/**
	 * @return the documentoLegalAnexoArquivo
	 */
	public DocumentoLegalAnexoArquivo getDocumentoLegalAnexoArquivo() {
		return documentoLegalAnexoArquivo;
	}

	/**
	 * @param documentoLegalAnexoArquivo
	 *            the documentoLegalAnexoArquivo to set
	 */
	public void setDocumentoLegalAnexoArquivo(
			DocumentoLegalAnexoArquivo documentoLegalAnexoArquivo) {
		this.documentoLegalAnexoArquivo = documentoLegalAnexoArquivo;
	}

	/**
	 * @return the fichaReajustes
	 */
	public List<String> getFichaReajustes() {
		return fichaReajustes;
	}

	/**
	 * @param fichaReajustes the fichaReajustes to set
	 */
	public void setFichaReajustes(List<String> fichaReajustes) {
		this.fichaReajustes = fichaReajustes;
	}

	/**
	 * @return the fichaReajustesMap
	 */
	public Map<String, Long> getFichaReajustesMap() {
		return fichaReajustesMap;
	}

	/**
	 * @param fichaReajustesMap the fichaReajustesMap to set
	 */
	public void setFichaReajustesMap(Map<String, Long> fichaReajustesMap) {
		this.fichaReajustesMap = fichaReajustesMap;
	}

	/**
	 * @return the fichaSelected
	 */
	public String getFichaSelected() {
		return fichaSelected;
	}

	/**
	 * @param fichaSelected the fichaSelected to set
	 */
	public void setFichaSelected(String fichaSelected) {
		this.fichaSelected = fichaSelected;
	}

	/**
	 * @return the fichaReajuste
	 */
	public FichaReajuste getFichaReajuste() {
		return fichaReajuste;
	}

	/**
	 * @param fichaReajuste the fichaReajuste to set
	 */
	public void setFichaReajuste(FichaReajuste fichaReajuste) {
		this.fichaReajuste = fichaReajuste;
	}

	/**
	 * @return the controlesReajuste
	 */
	public List<ControleReajuste> getControlesReajuste() {
		return controlesReajuste;
	}

	/**
	 * @param controlesReajuste the controlesReajuste to set
	 */
	public void setControlesReajuste(List<ControleReajuste> controlesReajuste) {
		this.controlesReajuste = controlesReajuste;
	}
}
