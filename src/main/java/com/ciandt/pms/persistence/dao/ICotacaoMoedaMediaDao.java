package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.CotacaoMoedaMedia;
import com.ciandt.pms.model.Moeda;


/**
 * 
 * A classe ICotacaoMoedaMedia proporciona a interface para a camada de persistencia
 * para a entidade CotacaoMoedaMedia.
 * 
 * @since 15/03/2016
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
public interface ICotacaoMoedaMediaDao extends IAbstractDao<Long, CotacaoMoedaMedia> {

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
     * @return retorna a CotacaoMoedaMedia.
     */
    List<CotacaoMoedaMedia> findByFilter(final CotacaoMoedaMedia filter,
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
    CotacaoMoedaMedia findLastByMonth(
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
    CotacaoMoedaMedia findByMoedaMesAndTipo(
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
    CotacaoMoedaMedia findByMoedaAndLastDayMonth(final Moeda moeda,
            final Date dataMes);

    List<CotacaoMoedaMedia> findByDeAndParaAndPeriodAndNovaCotacao(final Date startDate,
			final Date endDate, final Moeda moedaDe, final Moeda moedaPara, final Boolean isNovaCotacao);

    Date findLast();

    Date findLastByMoedaDeAndMoedaPara(final Moeda moedaDe, final Moeda moedaPara);

    List<CotacaoMoedaMedia> findByDate(Date date);

    void create(List<CotacaoMoedaMedia> exchanges);

    void deleteByDate(Date date);
}