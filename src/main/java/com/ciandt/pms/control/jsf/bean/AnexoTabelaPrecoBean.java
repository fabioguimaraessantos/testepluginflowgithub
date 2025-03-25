package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.AnexoTabelaPreco;


/**
 * 
 * Define o BackBean da entidade AnexoTabelaPreco.
 *
 * @since 27/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AnexoTabelaPrecoBean implements Serializable {
    
    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;
    
    /** To do backbean. */
    private AnexoTabelaPreco to = new AnexoTabelaPreco();
    
    /** Lista de AnexoTabelaPreco. */
    private List<AnexoTabelaPreco> listaAnexoTabelaPreco = new ArrayList<AnexoTabelaPreco>();
    
    /** Texto Comentario. */
    private String textoComentario;
    
    /** Flag para botao save upload. */
    private Boolean flagButtonSaveUpload = Boolean.valueOf(false);
    
    

    /**
     * @return the to
     */
    public AnexoTabelaPreco getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(final AnexoTabelaPreco to) {
        this.to = to;
    }

    /**
     * @return the listaAnexoTabelaPreco
     */
    public List<AnexoTabelaPreco> getListaAnexoTabelaPreco() {
        return listaAnexoTabelaPreco;
    }

    /**
     * @param listaAnexoTabelaPreco the listaAnexoTabelaPreco to set
     */
    public void setListaAnexoTabelaPreco(
            final List<AnexoTabelaPreco> listaAnexoTabelaPreco) {
        this.listaAnexoTabelaPreco = listaAnexoTabelaPreco;
    }

    /**
     * @return the textoComentario
     */
    public String getTextoComentario() {
        return textoComentario;
    }

    /**
     * @param textoComentario the textoComentario to set
     */
    public void setTextoComentario(final String textoComentario) {
        this.textoComentario = textoComentario;
    }
    
    /**
     * @return the flagButtonSaveUpload
     */
    public Boolean getFlagButtonSaveUpload() {
        return flagButtonSaveUpload;
    }

    /**
     * @param flagButtonSaveUpload the flagButtonSaveUpload to set
     */
    public void setFlagButtonSaveUpload(final Boolean flagButtonSaveUpload) {
        this.flagButtonSaveUpload = flagButtonSaveUpload;
    }
    

}
