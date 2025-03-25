package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.AjusteReceitaForecast;

import java.util.Date;
import java.util.List;

/**
 * A classe IAjusteReceitaForecastDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade AjusteReceitaForecast.
 *
 * @since 17/06/2024
 * @author gbrunetto
 */
public interface IAjusteReceitaForecastDao extends IAbstractDao<Long, AjusteReceitaForecast> {

    /**
     * Metodo responsavel por buscar um ajuste de receita forecast por Contrato Pratica (C-LOB).
     *
     * @param codigoContratoPratica Codigo do C-LOB
     * @return AjusteReceitaForecast
     */
    List<AjusteReceitaForecast> findByContratoPratica(Long codigoContratoPratica);

    /**
     * Metodo responsavel por buscar ajustes de receita forecast.
     *
     * @param codigoContratoPratica Codigo do C-LOB
     * @param dataMesReceita Mes da receita
     * @return AjusteReceitaForecast
     */
    List<AjusteReceitaForecast> findByContratoPraticaAndDataMesReceita(Long codigoContratoPratica, Date dataMesReceita);
}