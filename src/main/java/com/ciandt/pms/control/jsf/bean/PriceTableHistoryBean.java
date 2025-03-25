package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.control.jsf.components.ISelect;
import com.ciandt.pms.control.jsf.components.impl.TabelaPrecoSelect;
import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PriceTableHistoryBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Lista de resultados. */
    private List<PriceTableHistory> resultList;

    /** List para o combobox com Price Table. */
    private ISelect<TabelaPreco> priceTableSelect;

    /** Lista de items resultados. */
    private List<ItemTabelaPrecoRow> items;

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId;

    /** Selected History to show in Item Details. */
    private PriceTableHistory history;

    /** Price Table to show in Item Details. */
    private TabelaPreco priceTable;

    /**
     *
     */
    public void init(){
        resultList = new ArrayList<>();
        priceTableSelect = new TabelaPrecoSelect(Collections.emptyList());
        items = new ArrayList<>();
        currentPageId = Integer.valueOf(0);
    }

    /* Getters and Setters */
    public List<PriceTableHistory> getResultList() {
        return resultList;
    }
    public void setResultList(List<PriceTableHistory> resultList) {
        this.resultList = resultList;
    }
    public ISelect<TabelaPreco> getPriceTableSelect() {
        return priceTableSelect;
    }
    public void setPriceTableSelect(ISelect<TabelaPreco> priceTableSelect) {
        this.priceTableSelect = priceTableSelect;
    }
    public List<ItemTabelaPrecoRow> getItems() {
        return items;
    }
    public void setItems(List<ItemTabelaPrecoRow> items) {
        this.items = items;
    }
    public Integer getCurrentPageId() {
        return currentPageId;
    }
    public void setCurrentPageId(Integer currentPageId) {
        this.currentPageId = currentPageId;
    }
    public PriceTableHistory getHistory() {
        return history;
    }
    public void setHistory(PriceTableHistory history) {
        this.history = history;
    }
    public TabelaPreco getPriceTable() {
        return priceTable;
    }
    public void setPriceTable(TabelaPreco priceTable) {
        this.priceTable = priceTable;
    }
}