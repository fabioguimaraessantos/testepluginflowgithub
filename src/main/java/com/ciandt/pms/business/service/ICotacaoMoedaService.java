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
     *            Data inicio referente ao dia da cota��o.
     * @param dataDiaEnd
     *            Data final referente ao dia da cota��o.
     * 
     * @return retorna uma lista de CotacaoMoeda.
     */
    List<CotacaoMoeda> findCotacaoMoedaByFilter(final CotacaoMoeda filter,
            final Date dataDiaBeg, final Date dataDiaEnd);

    /**
     * Pega a cota��o do mes. � considerado a cota��o do m�s a �ltima
     * cota��o registrada no m�s.
     * 
     * @param moeda
     *            - Moeda que se deseja saber a cota��o
     * @param dateMonth
     *            data-mes que se deseja saber a cota��o.
     * 
     * @return retorna uma CotacaoMoeda do mes
     */
    CotacaoMoeda findCotacaoMoedaByMonth(final Moeda moeda, final Date dateMonth);

    /**
     * Busca a cota��o moeda da data passada por parametro.
     * 
     * @param moeda - moeda em quest�o
     * @param date - data em quest�o
     * 
     * @return retorna a cota��o.
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
     * Remove uma cota��o planejada.
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