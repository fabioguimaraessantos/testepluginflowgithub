package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.Moeda;


/**
 * 
 * A classe ICotacaoMoeda proporciona a interface para a camada de persistencia
 * para a entidade CotacaoMoeda.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface ICotacaoMoedaDao extends IAbstractDao<Long, CotacaoMoeda> {

    /**
     * Busca pela Moeda e Data Dia.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param dataDiaBeg
     *            Data inicio referente ao dia da cotação.
     * @param dataDiaEnd
     *            Data final referente ao dia da cotação.
     * 
     * @return retorna a CotacaoMoeda.
     */
    List<CotacaoMoeda> findByFilter(final CotacaoMoeda filter,
            final Date dataDiaBeg, final Date dataDiaEnd);
    
    /**
     * Busca a ultima cotação do mes de uma moeda.
     * 
     * @param moeda
     *            entidade do tipo Moeda.
     * @param dateMonth
     *            Data-mes referente a cotação.
     * 
     * @return retorna a ultimka cotação do mês, 
     * caso não exista nenhuma retorna null.
     */
    CotacaoMoeda findLastByMonth(
            final Moeda moeda, final Date dateMonth);
    
    /**
     * Busca a cotação de uma moeda em un determinado mes.
     * 
     * @param moeda
     *            entidade do tipo Moeda.
     * @param dateMonth
     *            Data-mes referente a cotação.
     * @param tipo 
     *          tipo da cotação (R-Realizada ou P-Planejada)
     * 
     * @return retorna a ultimka cotação do mês, 
     * caso não exista nenhuma retorna null.
     */
    CotacaoMoeda findByMoedaMesAndTipo(
            final Moeda moeda, final Date dateMonth, final String tipo);
    
    /**
     * Busca uma entidade cadastrada no ultimo dia do mes.
     * 
     * @param moeda
     *            - Moeda corrente.
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return a entidade cadastrada no ultimo dia do mes.
     */
    CotacaoMoeda findByMoedaAndLastDayMonth(final Moeda moeda,
            final Date dataMes);

}