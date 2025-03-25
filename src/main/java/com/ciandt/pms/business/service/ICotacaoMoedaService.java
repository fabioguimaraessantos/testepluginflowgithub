package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.Moeda;


/**
 * 
 * A classe ICotacaoMoedaService proporciona a interface para acesso ao servicos
 * relacionados a entidade CotacaoMoeda.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface ICotacaoMoedaService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CotacaoMoeda findCotacaoMoedaById(final Long id);

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
     * @return retorna uma lista de CotacaoMoeda.
     */
    List<CotacaoMoeda> findCotacaoMoedaByFilter(final CotacaoMoeda filter,
            final Date dataDiaBeg, final Date dataDiaEnd);

    /**
     * Pega a cotação do mes. É considerado a cotação do mês a última
     * cotação registrada no mês.
     * 
     * @param moeda
     *            - Moeda que se deseja saber a cotação
     * @param dateMonth
     *            data-mes que se deseja saber a cotação.
     * 
     * @return retorna uma CotacaoMoeda do mes
     */
    CotacaoMoeda findCotacaoMoedaByMonth(final Moeda moeda, final Date dateMonth);

    /**
     * Busca a cotação moeda da data passada por parametro.
     * 
     * @param moeda - moeda em questão
     * @param date - data em questão
     * 
     * @return retorna a cotação.
     */
    CotacaoMoeda findCotacaoMoedaByMoedaMes(final Moeda moeda, final Date date);
    
    /**
     * Cria uma nova CotacaoMoeda.
     * 
     * @param cotacao
     *            entidade do tipo CotacaoMoeda.
     * 
     * @return retorna true se cirado com sucesso, caso contrario falso.
     */
    @Transactional
    Boolean createCotacaoMoedaForecast(final CotacaoMoeda cotacao);

    /**
     * Atualiza uma nova CotacaoMoeda.
     * 
     * @param cotacao
     *            entidade do tipo CotacaoMoeda.
     * 
     * @return retorna true se cirado com sucesso, caso contrario falso.
     */
    @Transactional
    Boolean updateCotacaoMoedaForecast(final CotacaoMoeda cotacao);

    /**
     * Remove uma cotação planejada.
     * 
     * @param cotacao
     *            entidade CotacaoMoeda a ser removida.
     * 
     * @return retorna true se removido com sucesso
     */
    @Transactional
    Boolean removeCotacaoMoedaForecast(final CotacaoMoeda cotacao);

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
    CotacaoMoeda findCotMoedaByMoedaAndLastDayMonth(final Moeda moeda,
            final Date dataMes);

}