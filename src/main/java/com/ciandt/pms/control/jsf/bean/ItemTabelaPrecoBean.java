package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;


/**
 * Define o BackingBean da entidade ItemTabelaPreco.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ItemTabelaPrecoBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private ItemTabelaPreco to = new ItemTabelaPreco();

    /** Lista de resultados. */
    private List<ItemTabelaPreco> resultList = new ArrayList<ItemTabelaPreco>();

    /** Flag que verifica se é um update. */
    private Boolean isUpdate = Boolean.FALSE;

    /** Lista para o combobox com as TabelaPreco. */
    private List<String> tabelaPrecoList = new ArrayList<String>();

    /** Lista para o combobox com as TabelaPreco. */
    private Map<String, Long> tabelaPrecoMap = new HashMap<String, Long>();

    /** Lista para o combobox com as PerfilVendido. */
    private List<String> perfilVendidoList = new ArrayList<String>();

    /** Lista para o combobox com as PerfilVendido. */
    private Map<String, Long> perfilVendidoMap = new HashMap<String, Long>();

    /** Valor do fte. */
    private BigDecimal fte = null;

    /** Indicador preco por FTE. */
    private Boolean isPricePerFTE = Boolean.FALSE;

    /** Lista de perfilVendido para combo. */
    private List<String> perfilVendidoComboList = new ArrayList<String>();

    /** Perfil venidido selecionado no combobox. */
    private String perfilVendidoSelected;

    /** `Preco do item em FTE. */
    private BigDecimal priceFTE = new BigDecimal(0);

    /** Lista para tabela da view. */
    private List<ItemTabelaPrecoRow> listaItemTabelaPrecoRow =
            new ArrayList<ItemTabelaPrecoRow>();

    /** String do nomeCargo para o modal de add ItemTabelaPreço. */
    private String nomeCargo;
    
    /** Boolean para checkbox de despesa embutida. */
    private Boolean isExpenseEmbedded = Boolean.FALSE;

    
    /** Valor de despesa embutida. */
    private BigDecimal vlrEmbedded;

	/**
     * @param to
     *            the to to set
     */
    public void setTo(final ItemTabelaPreco to) {
        this.to = to;
    }

    /**
     * @return the to
     */
    public ItemTabelaPreco getTo() {
        if (to == null) {
            to = new ItemTabelaPreco();
        }
        if (to.getPerfilVendido() == null) {
            to.setPerfilVendido(new PerfilVendido());
        }
        if (to.getTabelaPreco() == null) {
            to.setTabelaPreco(new TabelaPreco());
        }

        return to;
    }

    /**
     * @param isUpdate
     *            the isUpdate to set
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @param tabelaPrecoList
     *            the tabelaPrecoList to set
     */
    public void setTabelaPrecoList(final List<String> tabelaPrecoList) {
        this.tabelaPrecoList = tabelaPrecoList;
    }

    /**
     * @return the tabelaPrecoList
     */
    public List<String> getTabelaPrecoList() {
        return tabelaPrecoList;
    }

    /**
     * @param tabelaPrecoMap
     *            the tabelaPrecoMap to set
     */
    public void setTabelaPrecoMap(final Map<String, Long> tabelaPrecoMap) {
        this.tabelaPrecoMap = tabelaPrecoMap;
    }

    /**
     * @return the tabelaPrecoMap
     */
    public Map<String, Long> getTabelaPrecoMap() {
        return tabelaPrecoMap;
    }

    /**
     * @param perfilVendidoList
     *            the perfilVendidoList to set
     */
    public void setPerfilVendidoList(final List<String> perfilVendidoList) {
        this.perfilVendidoList = perfilVendidoList;
    }

    /**
     * @return the perfilVendidoList
     */
    public List<String> getPerfilVendidoList() {
        return perfilVendidoList;
    }

    /**
     * @param perfilVendidoMap
     *            the perfilVendidoMap to set
     */
    public void setPerfilVendidoMap(final Map<String, Long> perfilVendidoMap) {
        this.perfilVendidoMap = perfilVendidoMap;
    }

    /**
     * @return the perfilVendidoMap
     */
    public Map<String, Long> getPerfilVendidoMap() {
        return perfilVendidoMap;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<ItemTabelaPreco> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the resultList
     */
    public List<ItemTabelaPreco> getResultList() {
        return resultList;
    }

    /**
     * Popula a lista de PerfilVendido para combobox.
     * 
     * @param pefilsVendidos
     *            lista de PefilVendido.
     * 
     */
    public void loadPerfilVendidoList(final List<PerfilVendido> pefilsVendidos) {

        Map<String, Long> localPerfilVendidoMap = new HashMap<String, Long>();
        List<String> localPerfilVendidoList = new ArrayList<String>();

        for (PerfilVendido perfilVendido : pefilsVendidos) {
            localPerfilVendidoMap.put(perfilVendido.getNomePerfilVendido(),
                    perfilVendido.getCodigoPerfilVendido());
            localPerfilVendidoList.add(perfilVendido.getNomePerfilVendido());
        }

        this.setPerfilVendidoMap(localPerfilVendidoMap);
        this.setPerfilVendidoList(localPerfilVendidoList);
    }

    /**
     * Popula a lista de TabelaPreco para combobox.
     * 
     * @param tabelaPrecos
     *            lista de TabelaPreco.
     * 
     */
    public void loadTabelaPrecoList(final List<TabelaPreco> tabelaPrecos) {

        Map<String, Long> localTabelaPrecoMap = new HashMap<String, Long>();
        List<String> localTabelaPrecoList = new ArrayList<String>();

        for (TabelaPreco tabelaPreco : tabelaPrecos) {
            localTabelaPrecoMap.put(tabelaPreco.getDescricaoTabelaPreco(),
                    tabelaPreco.getCodigoTabelaPreco());
            localTabelaPrecoList.add(tabelaPreco.getDescricaoTabelaPreco());
        }

        this.setTabelaPrecoMap(localTabelaPrecoMap);
        this.setTabelaPrecoList(localTabelaPrecoList);
    }

    /**
     * Reseta o To do bean.
     */
    public void resetTo() {
        this.to = null;
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        this.resetTo();
        this.setResultList(new ArrayList<ItemTabelaPreco>());
        this.vlrEmbedded = null;
        this.isExpenseEmbedded = Boolean.FALSE;
        this.isUpdate = Boolean.FALSE;
    }

    /**
     * @param fte
     *            the fte to set
     */
    public void setFte(final BigDecimal fte) {
        this.fte = fte;
    }

    /**
     * @return the fte
     */
    public BigDecimal getFte() {
        return fte;
    }

    /**
     * @param isPricePerFTE
     *            the isPricePerFTE to set
     */
    public void setIsPricePerFTE(final Boolean isPricePerFTE) {
        this.isPricePerFTE = isPricePerFTE;
    }

    /**
     * @return the isPricePerFTE
     */
    public Boolean getIsPricePerFTE() {
        return isPricePerFTE;
    }

    /**
     * @return the perfilVendidoComboList
     */
    public List<String> getPerfilVendidoComboList() {
        return perfilVendidoComboList;
    }

    /**
     * @param perfilVendidoComboList
     *            the perfilVendidoComboList to set
     */
    public void setPerfilVendidoComboList(
            final List<String> perfilVendidoComboList) {
        this.perfilVendidoComboList = perfilVendidoComboList;
    }

    /**
     * @return the perfilVendidoSelected
     */
    public String getPerfilVendidoSelected() {
        return perfilVendidoSelected;
    }

    /**
     * @param perfilVendidoSelected
     *            the perfilVendidoSelected to set
     */
    public void setPerfilVendidoSelected(final String perfilVendidoSelected) {
        this.perfilVendidoSelected = perfilVendidoSelected;
    }

    /**
     * @return the priceFTE
     */
    public BigDecimal getPriceFTE() {
        return priceFTE;
    }

    /**
     * @param priceFTE
     *            the priceFTE to set
     */
    public void setPriceFTE(final BigDecimal priceFTE) {
        this.priceFTE = priceFTE;
    }

    /**
     * @return the listaItemTabelaPrecoRow
     */
    public List<ItemTabelaPrecoRow> getListaItemTabelaPrecoRow() {
        return listaItemTabelaPrecoRow;
    }

    /**
     * @param listaItemTabelaPrecoRow
     *            the listaItemTabelaPrecoRow to set
     */
    public void setListaItemTabelaPrecoRow(
            final List<ItemTabelaPrecoRow> listaItemTabelaPrecoRow) {
        this.listaItemTabelaPrecoRow = listaItemTabelaPrecoRow;
    }

    /**
     * @return the nomeCargo
     */
    public String getNomeCargo() {
        return nomeCargo;
    }

    /**
     * @param nomeCargo
     *            the nomeCargo to set
     */
    public void setNomeCargo(final String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

	/**
	 * @return the isExpenseEmbedded
	 */
	public Boolean getIsExpenseEmbedded() {
		return isExpenseEmbedded;
	}

	/**
	 * @param isExpenseEmbedded the isExpenseEmbedded to set
	 */
	public void setIsExpenseEmbedded(Boolean isExpenseEmbedded) {
		this.isExpenseEmbedded = isExpenseEmbedded;
	}
	
    /**
	 * @return the vlrEmbedded
	 */
	public BigDecimal getVlrEmbedded() {
		return vlrEmbedded;
	}

	/**
	 * @param vlrEmbedded the vlrEmbedded to set
	 */
	public void setVlrEmbedded(BigDecimal vlrEmbedded) {
		this.vlrEmbedded = vlrEmbedded;
	}


}