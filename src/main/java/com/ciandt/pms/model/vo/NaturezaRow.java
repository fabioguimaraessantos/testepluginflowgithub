package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.NaturezaCentroLucro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * A classe NaturezaRow representa a lista de naturezas 
 * na tela de criação do grupo de custo.
 *
 * @since 14/05/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class NaturezaRow implements Serializable {

    /** Default serial Version UID. */
    private static final long serialVersionUID = 1L;
    
    /** Entidade do tipo NaturezaCentroLucro. */
    private NaturezaCentroLucro natureza;
    
    /** Lista com os nomes do centro de lucro, utilizado no combo. */
    private List<String> centroLucroList = new ArrayList<String>(); 
    
    /** Map com os nomes do centro de lucro, utilizado no combo. */
    private Map<String, Long> centroLucroMap = new HashMap<String, Long>();

    /** Armazena o nome do centroLucro, selecionado no combo. */
    private String centroLucroName;
    
    /**
     * @param natureza the natureza to set
     */
    public void setNatureza(final NaturezaCentroLucro natureza) {
        this.natureza = natureza;
    }

    /**
     * @return the natureza
     */
    public NaturezaCentroLucro getNatureza() {
        return natureza;
    }

    /**
     * @return the centroLucroList
     */
    public List<String> getCentroLucroList() {
        return centroLucroList;
    }

    /**
     * @param centroLucroList the centroLucroList to set
     */
    public void setCentroLucroList(final List<String> centroLucroList) {
        this.centroLucroList = centroLucroList;
    }

    /**
     * @return the centroLucroMap
     */
    public Map<String, Long> getCentroLucroMap() {
        return centroLucroMap;
    }

    /**
     * @param centroLucroMap the centroLucroMap to set
     */
    public void setCentroLucroMap(final Map<String, Long> centroLucroMap) {
        this.centroLucroMap = centroLucroMap;
    }

    /**
     * @param centroLucroName the centroLucroName to set
     */
    public void setCentroLucroName(final String centroLucroName) {
        this.centroLucroName = centroLucroName;
    }

    /**
     * @return the centroLucroName
     */
    public String getCentroLucroName() {
        return centroLucroName;
    }
    
    
}
