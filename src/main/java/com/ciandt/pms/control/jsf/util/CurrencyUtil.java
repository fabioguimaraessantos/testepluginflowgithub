package com.ciandt.pms.control.jsf.util;

import com.ciandt.pms.model.Moeda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyUtil {
    private CurrencyUtil() {
    }

    public static List<String> loadMoedaList(List<Moeda> currencyList) {
        List<String> list = new ArrayList<>();

        for (Moeda moeda : currencyList) {
            list.add(moeda.getNomeMoeda());
        }

        return list;
    }

    public static Map<String, Long> loadMoedaMap(List<Moeda> currencyList) {
        Map<String, Long> map = new HashMap<>();

        for (Moeda moeda : currencyList) {
            map.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
        }

        return map;
    }
}
