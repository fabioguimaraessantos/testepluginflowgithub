package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IAjusteReceitaForecastService;
import com.ciandt.pms.model.AjusteReceitaForecast;
import com.ciandt.pms.persistence.dao.IAjusteReceitaForecastDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * A classe AjusteReceitaForecastService proporciona as funcionalidades da camada de
 * negócio referente a entidade AjusteReceitaForecast.
 *
 * @since 17/06/2024
 * @author gbrunetto
 *
 */
@Service
public class AjusteReceitaForecastService implements IAjusteReceitaForecastService {

    @Autowired
    private IAjusteReceitaForecastDao ajusteReceitaForecastDao;

    public List<AjusteReceitaForecast> findByContratoPratica(Long code) {
        return ajusteReceitaForecastDao.findByContratoPratica(code);
    }

    public List<AjusteReceitaForecast> findByContratoPraticaAndDataMesReceita(Long codigoContratoPratica, Date dataMesReceita) {
        return ajusteReceitaForecastDao.findByContratoPraticaAndDataMesReceita(codigoContratoPratica, dataMesReceita);
    }
}
