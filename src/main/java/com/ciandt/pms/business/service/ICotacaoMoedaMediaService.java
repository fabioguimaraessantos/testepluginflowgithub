package com.ciandt.pms.business.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.exception.ExchangeRateException;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Service;

import com.ciandt.pms.model.CotacaoMoedaMedia;
import com.ciandt.pms.model.Moeda;


/**
 * 
 * A classe ICotacaoMoedaMediaService proporciona a interface para acesso ao servicos
 * relacionados a entidade CotacaoMoedaMedia.
 * 
 * @since 15/03/2016
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Service
public interface ICotacaoMoedaMediaService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CotacaoMoedaMedia findCotacaoMoedaMediaById(final Long id);

    /**
     * Busca pela Moeda e Periodo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param dataDiaBeg
     *            Data inicio referente ao dia da cotação.
     * @param dataDiaEnd
     *            Data final referente ao dia da cotação.
     * 
     * @return retorna uma lista de CotacaoMoedaMedia.
     */
    List<CotacaoMoedaMedia> findByFilter(final CotacaoMoedaMedia filter,
            final Date dataDiaBeg, final Date dataDiaEnd);

    /**
     * Pega a cotação do mes. É considerado a cotação do mês a última
     * cotação registrada no mês.
     * 
     * @param startDate
     *            - Moeda que se deseja saber a cotação
     * @param moedaDe
     *            data-mes que se deseja saber a cotação.
     * 
     * @return retorna uma CotacaoMoedaMedia do mes
     */
    List<CotacaoMoedaMedia> findByDeAndParaAndPeriod(final Date startDate,
			final Date endDate, final Moeda moedaDe, final Moeda moedaPara);

    /**
     * Busca a cotação moeda da data passada por parametro.
     * 
     * @param moeda - moeda em questão
     * @param date - data em questão
     * 
     * @return retorna a cotação.
     */
    CotacaoMoedaMedia findCotacaoMoedaMediaByMoedaMes(final Moeda moeda, final Date date);

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
    CotacaoMoedaMedia findCotMoedaByMoedaAndLastDayMonth(final Moeda moeda,
            final Date dataMes);

	Date findLast();

	Date findLastByMoedaDeAndMoedaPara(final Moeda moedaDe, final Moeda moedaPara);

	void update(CotacaoMoedaMedia cotacaoMoedaMedia);

    Double getMeanByCurrencyAndPeriod(Date startDate, Date endDate, Moeda moedaDe, Moeda moedaPara);

	void syncCotacaoMoedaMediaByDate(Moeda moedaDe, Moeda moedaPara, Date date,
			HttpClient client) throws IOException, ParseException;

    List<CotacaoMoedaMedia> findByDate(Date copyFromDate);

    void replace(Date from, Date until, String login, String ticket) throws ExchangeRateException;

    void deleteByDate(Date date);

    void delete(Date fromDate, Date untilDate, String login, String ticket);
}