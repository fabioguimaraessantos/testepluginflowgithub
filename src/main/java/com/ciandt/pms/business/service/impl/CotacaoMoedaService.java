package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICotacaoMoedaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICotacaoMoedaDao;


/**
 * 
 * A classe CotacaoMoedaService proporciona as funcionalidades de serviço para a
 * entidade CotacaoMoeda.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class CotacaoMoedaService implements ICotacaoMoedaService {

    /** DAO da entidade CotacaoMoeda. */
    @Autowired
    private ICotacaoMoedaDao cotacaoMoedaDao;

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public CotacaoMoeda findCotacaoMoedaById(final Long id) {
        return cotacaoMoedaDao.findById(id);
    }

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
    public List<CotacaoMoeda> findCotacaoMoedaByFilter(
            final CotacaoMoeda filter, final Date dataDiaBeg,
            final Date dataDiaEnd) {
        return cotacaoMoedaDao.findByFilter(filter, dataDiaBeg, dataDiaEnd);
    }

    /**
     * Busca a ultima cotação do mes de uma moeda.
     * 
     * @param moeda
     *            entidade do tipo Moeda.
     * @param dateMonth
     *            Data-mes referente a cotação.
     * 
     * @return retorna a ultimka cotação do mês, caso não exista nenhuma retorna
     *         null.
     */
    private CotacaoMoeda findLastByMonth(final Moeda moeda, final Date dateMonth) {

        return cotacaoMoedaDao.findLastByMonth(moeda, dateMonth);
    }

    /**
     * Pega a cotação do mes. É considerado a cotação do mês a última cotação
     * registrada no mês.
     * 
     * @param moeda
     *            - Moeda que se deseja saber a cotação
     * @param dateMonth
     *            data-mes que se deseja saber a cotação.
     * 
     * @return retorna uma CotacaoMoeda do mes
     */
    public CotacaoMoeda findCotacaoMoedaByMonth(final Moeda moeda,
            final Date dateMonth) {

        return findLastByMonth(moeda, dateMonth);
    }

    /**
     * Cria uma nova CotacaoMoeda.
     * 
     * @param cotacao
     *            entidade do tipo CotacaoMoeda.
     * 
     * @return retorna true se cirado com sucesso, caso contrario falso.
     */
    public Boolean createCotacaoMoedaForecast(final CotacaoMoeda cotacao) {
        cotacao.setIndicadorTipo(Constants.COTACAO_MOEDA_TIPO_PREVISTO);

        if (validateCreate(cotacao)) {
            cotacaoMoedaDao.create(cotacao);
        } else {
            return false;
        }

        return true;
    }

    /**
     * Atualiza uma nova CotacaoMoeda.
     * 
     * @param cotacao
     *            entidade do tipo CotacaoMoeda.
     * 
     * @return retorna true se cirado com sucesso, caso contrario falso.
     */
    public Boolean updateCotacaoMoedaForecast(final CotacaoMoeda cotacao) {
        cotacao.setIndicadorTipo(Constants.COTACAO_MOEDA_TIPO_PREVISTO);

        if (validateUpdate(cotacao)) {
            cotacaoMoedaDao.update(cotacao);
        } else {
            return false;
        }

        return true;
    }

    /**
     * Remove uma cotação planejada.
     * 
     * @param cotacao
     *            entidade CotacaoMoeda a ser removida.
     * 
     * @return retorna true se removido com sucesso
     */
    public Boolean removeCotacaoMoedaForecast(final CotacaoMoeda cotacao) {

        cotacaoMoedaDao.remove(findCotacaoMoedaById(cotacao
                .getCodigoCotacaoMoeda()));

        return true;
    }

    /**
     * Busca a cotação moeda da data passada por parametro.
     * 
     * @param moeda
     *            - moeda em questão
     * @param date
     *            - data em questão
     * 
     * @return retorna a cotação.
     */
    public CotacaoMoeda findCotacaoMoedaByMoedaMes(final Moeda moeda,
            final Date date) {
        return cotacaoMoedaDao.findByMoedaMesAndTipo(moeda, date,
                Constants.COTACAO_MOEDA_TIPO_REAL);
    }

    /**
     * Valida a criação de uma cotação.
     * 
     * @param cotacao
     *            entidade do tipo CotacaoMoeda
     * 
     * @return retorna true se validado com sucesso, caos contrario false
     */
    private Boolean validateCreate(final CotacaoMoeda cotacao) {
        CotacaoMoeda cm =
                cotacaoMoedaDao.findByMoedaMesAndTipo(cotacao.getMoeda(),
                        cotacao.getDataDia(), cotacao.getIndicadorTipo());

        if (cm != null) {
            Messages.showError("validateCreate",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_COTACAO_MOEDA);
            return false;
        }

        return true;

    }

    /**
     * Valida a criação de uma cotação.
     * 
     * @param cotacao
     *            entidade do tipo CotacaoMoeda
     * 
     * @return retorna true se validado com sucesso, caos contrario false
     */
    private Boolean validateUpdate(final CotacaoMoeda cotacao) {
        CotacaoMoeda cm =
                cotacaoMoedaDao.findByMoedaMesAndTipo(cotacao.getMoeda(),
                        cotacao.getDataDia(), cotacao.getIndicadorTipo());

        if (cm != null
                && cm.getCodigoCotacaoMoeda().compareTo(
                        cotacao.getCodigoCotacaoMoeda()) != 0) {

            Messages.showError("validateUpdate",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_COTACAO_MOEDA);
            return false;
        }

        return true;
    }

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
    public CotacaoMoeda findCotMoedaByMoedaAndLastDayMonth(final Moeda moeda,
            final Date dataMes) {
        return cotacaoMoedaDao.findByMoedaAndLastDayMonth(moeda, dataMes);
    }

    /**
     * @param cotacaoMoedaDao
     *            the cotacaoMoedaDao to set
     */
    public void setCotacaoMoedaDao(ICotacaoMoedaDao cotacaoMoedaDao) {
        this.cotacaoMoedaDao = cotacaoMoedaDao;
    }

}