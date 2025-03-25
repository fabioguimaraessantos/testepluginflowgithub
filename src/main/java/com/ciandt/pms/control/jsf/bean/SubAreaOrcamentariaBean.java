package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.Modelo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SubAreaOrcamentariaBean implements Serializable {

    /** Flag indicando se e criacao ou edicao de uma Sub Budget Area */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Entidade que será persistida ou populada na tela */
    private AreaOrcamentaria to = new AreaOrcamentaria();

    /** Lista de Objetos AreaOrcamentaria */
    private List<AreaOrcamentaria> areaOrcamentariaPais = new ArrayList<AreaOrcamentaria>();

    /** Lista de nomes das Areas Orcamentarias Pais para exibir no ComboBox */
    private List<String> areasOrcamentariasPaisString = new ArrayList<String>();

    /** Nome da Area Orcamentaria Pai selecionada no ComboBox */
    private String selectedAreaOrcamentariaPai;

    private List<Modelo> modelos = new ArrayList<Modelo>();
    private Modelo selectedModelo;


    /** Objeto utilizado como filtro na consulta */
    private AreaOrcamentaria filter = new AreaOrcamentaria();

    private List<String> suggestionsListInAtivo = new ArrayList<String>();

    private List<AreaOrcamentaria> resultList = new ArrayList<>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);



    public AreaOrcamentaria getTo() {
        return this.to;
    }

    public void setTo(AreaOrcamentaria to) {
        this.to = to;
    }

    public List<AreaOrcamentaria> getAreaOrcamentariaPais() {
        return this.areaOrcamentariaPais;
    }

    public void setAreaOrcamentariaPais(List<AreaOrcamentaria> areaOrcamentariaPais) {
        this.areaOrcamentariaPais = areaOrcamentariaPais;
    }

    public List<Modelo> getModelos() {
        return this.modelos;
    }

    public void setModelos(List<Modelo> modelos) {
        this.modelos = modelos;
    }

    public Modelo getSelectedModelo() {
        return selectedModelo;
    }

    public void setSelectedModelo(Modelo selectedModelo) {
        this.selectedModelo = selectedModelo;
    }


    public Boolean getIsUpdate() {
        return this.isUpdate;
    }

    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public List<String> getAreasOrcamentariasPaisString() {
        return areasOrcamentariasPaisString;
    }

    public void setAreasOrcamentariasPaisString(List<String> areasOrcamentariasPaisString) {
        this.areasOrcamentariasPaisString = areasOrcamentariasPaisString;
    }

    public String getSelectedAreaOrcamentariaPai() {
        return selectedAreaOrcamentariaPai;
    }

    public void setSelectedAreaOrcamentariaPai(String selectedAreaOrcamentariaPai) {
        this.selectedAreaOrcamentariaPai = selectedAreaOrcamentariaPai;
    }

    public AreaOrcamentaria getFilter() {
        return filter;
    }

    public void setFilter(AreaOrcamentaria filter) {
        this.filter = filter;
    }

    public List<String> getSuggestionsListInAtivo() {
        return suggestionsListInAtivo;
    }

    public void setSuggestionsListInAtivo(List<String> suggestionsListInAtivo) {
        this.suggestionsListInAtivo = suggestionsListInAtivo;
    }

    public List<AreaOrcamentaria> getResultList() {
        return resultList;
    }

    public void setResultList(List<AreaOrcamentaria> resultList) {
        this.resultList = resultList;
    }

    public Integer getCurrentPageId() {
        return currentPageId;
    }

    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    public Long getCurrentRowId() {
        return currentRowId;
    }

    public void setCurrentRowId(final Long currentRowId) {
        this.currentRowId = currentRowId;
    }



    public void reset() {
        this.to = new AreaOrcamentaria();
        this.areaOrcamentariaPais = new ArrayList<AreaOrcamentaria>();
        this.areasOrcamentariasPaisString = new ArrayList<String>();
        this.setSelectedAreaOrcamentariaPai(new String());
        this.setModelos(new ArrayList<>());
        this.setSelectedModelo(new Modelo());
        this.resetSearch();
    }

    public void resetSearch() {
        this.resultList = new ArrayList<AreaOrcamentaria>();
        this.setFilter(new AreaOrcamentaria());
        this.setCurrentRowId(Long.valueOf(0));
        this.setCurrentPageId(Integer.valueOf(0));
    }
}
