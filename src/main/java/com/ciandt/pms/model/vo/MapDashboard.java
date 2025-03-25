package com.ciandt.pms.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A classe MapDashboard representa o dashboard do mapa de alocação.
 * 
 * @since 19/10/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie do Nascimento</a>
 * 
 */
public class MapDashboard implements Serializable {

    /** Default serial Version UID. */
    private static final long serialVersionUID = 1L;

    // Financials - MapaAlocacao
    /** Lista de MapDashboardRow. */
    private List<MapDashboardRow> mapDashboardRowList = new ArrayList<MapDashboardRow>();

    /** Lista de total FTE por Mes. */
    private List<Double> totalFteMesList = new ArrayList<Double>();

    /** Valor total da lista 'totalFteMesList'. */
    private Double totalFteRow;

    /** Lista da Média Ponderada de UR por mes. */
    private List<Double> medPonUrMesList = new ArrayList<Double>();

    /** Media da lista 'medPonUrMesList'. */
    private Double medPonUrRow;

    /** Total de receita por mês. */
    private List<Double> totalReceitaMesList = new ArrayList<Double>();

    /** Total da lista 'totalReceitaMesList'. */
    private Double totalReceitaRow;

    /** Total receita\FTE por mes. */
    private List<Double> totalReceitaMesPerFteList = new ArrayList<Double>();

    /** Total da lista 'totalReceitaPerFteRow'. */
    private Double totalReceitaPerFteRow;
    
    /** Construtor padrão. */
    public MapDashboard() {
    }

    /**
     * Construtor.
     * 
     * @param totalDashboardMatrix
     *            - matriz com os valores do dashboard do MapaAlocacao.
     * 
     */
    public MapDashboard(final Object[][] totalDashboardMatrix) {
        // Financials - MapaAlocacao
        this.totalFteRow = new Double(0);
        this.medPonUrRow = new Double(0);
        this.totalReceitaPerFteRow = new Double(0);
        this.totalReceitaRow = new Double(0);

        Object[] mapDashboardRowArray = totalDashboardMatrix[0];
        for (Object object : mapDashboardRowArray) {
            this.mapDashboardRowList.add((MapDashboardRow) object);
        }

        Object[] totalFteMesArray = totalDashboardMatrix[1];
        this.totalFteRow = Double.valueOf(0.0);
        for (Object object : totalFteMesArray) {
            Double value = (Double) object;
            this.totalFteMesList.add(value);
            this.totalFteRow += value;
        }
        
        Object[] medPonUrMesArray = totalDashboardMatrix[2];
        this.medPonUrRow = Double.valueOf(0.0);
        for (Object object : medPonUrMesArray) {
            Double value = (Double) object;
            this.medPonUrMesList.add(value);
            this.medPonUrRow += value;
        }
        this.medPonUrRow = this.medPonUrRow / medPonUrMesList.size();

        Object[] totalReceitaMesArray = totalDashboardMatrix[3];
        this.totalReceitaRow = Double.valueOf(0.0);
        for (Object object : totalReceitaMesArray) {
            Double value = (Double) object;
            this.totalReceitaMesList.add(value);
            this.totalReceitaRow += value;
        }
        
        Object[] totalReceitaMesPerFteArray = totalDashboardMatrix[4];
        this.totalReceitaPerFteRow = Double.valueOf(0.0);
        for (Object object : totalReceitaMesPerFteArray) {
            Double value = (Double) object;
            this.totalReceitaMesPerFteList.add(value);
            this.totalReceitaPerFteRow += value;
        }
    }

    /**
     * @return the mapDashboardRowList
     */
    public List<MapDashboardRow> getMapDashboardRowList() {
        return mapDashboardRowList;
    }

    /**
     * @param mapDashboardRowList
     *            the mapDashboardRowList to set
     */
    public void setMapDashboardRowList(
            final List<MapDashboardRow> mapDashboardRowList) {
        this.mapDashboardRowList = mapDashboardRowList;
    }

    /**
     * @return the totalFteMesList
     */
    public List<Double> getTotalFteMesList() {
        return totalFteMesList;
    }

    /**
     * @param totalFteMesList
     *            the totalFteMesList to set
     */
    public void setTotalFteMesList(final List<Double> totalFteMesList) {
        this.totalFteMesList = totalFteMesList;
    }

    /**
     * @return the medPonUrMesList
     */
    public List<Double> getMedPonUrMesList() {
        return medPonUrMesList;
    }

    /**
     * @param medPonUrMesList
     *            the medPonUrMesList to set
     */
    public void setMedPonUrMesList(final List<Double> medPonUrMesList) {
        this.medPonUrMesList = medPonUrMesList;
    }

    /**
     * @return the totalReceitaMesList
     */
    public List<Double> getTotalReceitaMesList() {
        return totalReceitaMesList;
    }

    /**
     * @param totalReceitaMesList
     *            the totalReceitaMesList to set
     */
    public void setTotalReceitaMesList(final List<Double> totalReceitaMesList) {
        this.totalReceitaMesList = totalReceitaMesList;
    }

    /**
     * @param totalReceitaMesPerFteList
     *            the totalReceitaMesPerFteList to set
     */
    public void setTotalReceitaMesPerFteList(
            final List<Double> totalReceitaMesPerFteList) {
        this.totalReceitaMesPerFteList = totalReceitaMesPerFteList;
    }

    /**
     * @return the totalReceitaMesPerFteList
     */
    public List<Double> getTotalReceitaMesPerFteList() {
        return totalReceitaMesPerFteList;
    }

    /**
     * @return the totalFteRow
     */
    public Double getTotalFteRow() {
        return totalFteRow;
    }

    /**
     * @param totalFteRow
     *            the totalFteRow to set
     */
    public void setTotalFteRow(final Double totalFteRow) {
        this.totalFteRow = totalFteRow;
    }

    /**
     * @return the medPonUrRow
     */
    public Double getMedPonUrRow() {
        return medPonUrRow;
    }

    /**
     * @param medPonUrRow
     *            the medPonUrRow to set
     */
    public void setMedPonUrRow(final Double medPonUrRow) {
        this.medPonUrRow = medPonUrRow;
    }

    /**
     * @return the totalReceitaRow
     */
    public Double getTotalReceitaRow() {
        return totalReceitaRow;
    }

    /**
     * @param totalReceitaRow
     *            the totalReceitaRow to set
     */
    public void setTotalReceitaRow(final Double totalReceitaRow) {
        this.totalReceitaRow = totalReceitaRow;
    }

    /**
     * @return the totalReceitaPerFteRow
     */
    public Double getTotalReceitaPerFteRow() {
        return totalReceitaPerFteRow;
    }

    /**
     * @param totalReceitaPerFteRow
     *            the totalReceitaPerFteRow to set
     */
    public void setTotalReceitaPerFteRow(final Double totalReceitaPerFteRow) {
        this.totalReceitaPerFteRow = totalReceitaPerFteRow;
    }

}
