package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.vo.PerfilVendidoRow;


/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 31/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PerfilVendidoBean implements Serializable {

    /** Defaul serial version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private PerfilVendido to = new PerfilVendido();

    /** Lista de resultados. */
    private List<PerfilVendido> resultList = new ArrayList<PerfilVendido>();

    /** Flag que verifica se é um update. */
    private Boolean isUpdate = Boolean.FALSE;
    
    /** Lista de cidadeBase para combobox. */
    private List<String> listaCidadeBaseCombobox = new ArrayList<String>();
    
    /** Lista de vwhrsCargo para combobox. */
    private List<String> listaCargoCombobox = new ArrayList<String>();
    
    /** Lista de Pmg para combobox. */
    private List<String> listaPmgCombobox = new ArrayList<String>();
    
    /** Lista de moeda para combobox. */
    private List<String> listaMoeda = new ArrayList<String>();
        
    /** Lista de objetos para tabela da view. */
    private List<PerfilVendidoRow> listaPerfilVendidoRow = new ArrayList<PerfilVendidoRow>();
    
    /** Map para combo de pmg. */
    private Map<String, Long> mapPmg = new HashMap<String, Long>();

    /** Map para combo de cargo. */
    private Map<String, Long> mapCargo = new HashMap<String, Long>();

    /** Map para combo de base. */
    private Map<String, Long> mapBase = new HashMap<String, Long>();
    
    /** Lista de perfil vendio. */
    private List<PerfilVendido> listaPerfilVendido = new ArrayList<PerfilVendido>();
    
    /** Cargo vindo da view.. */
    private String cargo;
    
    /** Pmg vindo da view. */
    private String pmg;
    
    /** Cidade vinda da view. */
    private String cidade;
    
    /** Moeda vinda da view. */
    private String moeda;
    
    /** Map para combobox de moeda. */
    private Map<String, Long> mapMoeda = new HashMap<String, Long>();
    
    

    /**
     * Seta o TO .
     * 
     * @param pv
     *            entidade para setar
     */
    public void setTo(final PerfilVendido pv) {
        this.to = pv;
    }

    /**
     * Pega o TO.
     * 
     * @return retorna o TO
     */
    public PerfilVendido getTo() {
        return to;
    }

    /**
     * Pega a lista de resultados.
     * 
     * @param resultList
     *            lista de resultado da busca
     */
    public void setResultList(final List<PerfilVendido> resultList) {
        this.resultList = resultList;
    }

    /**
     * 
     * @return retorna a lista de resultados da busca
     */
    public List<PerfilVendido> getResultList() {
        return resultList;
    }

    /** Reseta o TO. */
    public void resetTo() {
        this.to = new PerfilVendido();
        this.to.setIndicadorAtivo("A");
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        this.resetTo();
        this.cargo = "";
        this.pmg = "";
        this.cidade = "";
        this.moeda = "";
        this.isUpdate = Boolean.FALSE;
        this.listaPerfilVendido = new ArrayList<PerfilVendido>();
    }

    /**
     * @param pIsUpdate
     *            the isUpdate to set
     */
    public void setIsUpdate(final Boolean pIsUpdate) {
        this.isUpdate = pIsUpdate;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @return the listaCidadeBaseCombobox
     */
    public List<String> getListaCidadeBaseCombobox() {
        return listaCidadeBaseCombobox;
    }

    /**
     * @param listaCidadeBaseCombobox the listaCidadeBaseCombobox to set
     */
    public void setListaCidadeBaseCombobox(final List<String> listaCidadeBaseCombobox) {
        this.listaCidadeBaseCombobox = listaCidadeBaseCombobox;
    }

    /**
     * @return the listaCargoCombobox
     */
    public List<String> getListaCargoCombobox() {
        return listaCargoCombobox;
    }

    /**
     * @param listaCargoCombobox the listaCargoCombobox to set
     */
    public void setListaCargoCombobox(final List<String> listaCargoCombobox) {
        this.listaCargoCombobox = listaCargoCombobox;
    }

    /**
     * @return the listaPmgCombobox
     */
    public List<String> getListaPmgCombobox() {
        return listaPmgCombobox;
    }

    /**
     * @param listaPmgCombobox the listaPmgCombobox to set
     */
    public void setListaPmgCombobox(final List<String> listaPmgCombobox) {
        this.listaPmgCombobox = listaPmgCombobox;
    }

    /**
     * @return the mapPmg
     */
    public Map<String, Long> getMapPmg() {
        return mapPmg;
    }

    /**
     * @param mapPmg the mapPmg to set
     */
    public void setMapPmg(final Map<String, Long> mapPmg) {
        this.mapPmg = mapPmg;
    }

    /**
     * @return the mapCargo
     */
    public Map<String, Long> getMapCargo() {
        return mapCargo;
    }

    /**
     * @param mapCargo the mapCargo to set
     */
    public void setMapCargo(final Map<String, Long> mapCargo) {
        this.mapCargo = mapCargo;
    }

    /**
     * @return the mapBase
     */
    public Map<String, Long> getMapBase() {
        return mapBase;
    }

    /**
     * @param mapBase the mapBase to set
     */
    public void setMapBase(final Map<String, Long> mapBase) {
        this.mapBase = mapBase;
    }

    /**
     * @return the cargo
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * @param cargo the cargo to set
     */
    public void setCargo(final String cargo) {
        this.cargo = cargo;
    }

    /**
     * @return the pmg
     */
    public String getPmg() {
        return pmg;
    }

    /**
     * @param pmg the pmg to set
     */
    public void setPmg(final String pmg) {
        this.pmg = pmg;
    }

    /**
     * @return the cidade
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(final String cidade) {
        this.cidade = cidade;
    }
    
    
    /**
     * get moeda.
     * @return moeda.
     */
    public String getMoeda() {
		return moeda;
	}

    /**
     * set moeda.
     * @param moeda moeda
     */
	public void setMoeda(final String moeda) {
		this.moeda = moeda;
	}

	/**
     * @return the listaPerfilVendidoRow
     */
    public List<PerfilVendidoRow> getListaPerfilVendidoRow() {
        return listaPerfilVendidoRow;
    }

    /**
     * @param listaPerfilVendidoRow the listaPerfilVendidoRow to set
     */
    public void setListaPerfilVendidoRow(
            final List<PerfilVendidoRow> listaPerfilVendidoRow) {
        this.listaPerfilVendidoRow = listaPerfilVendidoRow;
    }

    /**
     * @return the listaPerfilVendido
     */
    public List<PerfilVendido> getListaPerfilVendido() {
        return listaPerfilVendido;
    }

    /**
     * @param listaPerfilVendido the listaPerfilVendido to set
     */
    public void setListaPerfilVendido(final List<PerfilVendido> listaPerfilVendido) {
        this.listaPerfilVendido = listaPerfilVendido;
    }
    
    
    /**
     * currency list for combobox.
     * @return list
     */
    public List<String> getListaMoeda() {
		return listaMoeda;
	}

    /**
     * Currency list for combobox.
     * @param listaMoeda currecy list.
     */
	public void setListaMoeda(List<String> listaMoeda) {
		this.listaMoeda = listaMoeda;
	}

	/**
     * Map combobox currency
     * @return mapCurrecy
     */
	public Map<String, Long> getMapMoeda() {
		return mapMoeda;
	}

	/**
	 * Map combobox currency.
	 * @param mapMoeda map.
	 */
	public void setMapMoeda(final Map<String, Long> mapMoeda) {
		this.mapMoeda = mapMoeda;
	}

 

}
