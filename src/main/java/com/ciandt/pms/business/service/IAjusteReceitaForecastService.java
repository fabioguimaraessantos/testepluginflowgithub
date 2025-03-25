package com.ciandt.pms.business.service;

import com.ciandt.pms.model.AjusteReceitaForecast;
import com.ciandt.pms.model.AjusteReceitaForecastStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * A classe IAjusteReceitaForecastService proporciona a interface de
 * acesso a camada de serviço referente a entidade AjusteReceitaForecast.
 *
 * @author gbrunetto
 * @since 17/06/2024
 */
@Service
public interface IAjusteReceitaForecastService {

    List<AjusteReceitaForecast> findByContratoPratica(Long code);

    List<AjusteReceitaForecast> findByContratoPraticaAndDataMesReceita(Long codigoContratoPratica, Date dataMesReceita);
}
