package com.ciandt.pms.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class HistoricoGrupoCusto<T> {
    private List<T> grupoCusto;

    public HistoricoGrupoCusto() {
        this.grupoCusto = new ArrayList<T>();
    }

    public void addDataSet(T dataset) {
        this.grupoCusto.add(dataset);
    }

}
