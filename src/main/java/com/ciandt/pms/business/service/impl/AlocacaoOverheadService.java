package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IAlocacaoOverheadService;
import com.ciandt.pms.business.service.IAlocacaoPeriodoOhService;
import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.persistence.dao.IAlocacaoOverheadDao;
import com.ciandt.pms.util.DateUtil;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Service
public class AlocacaoOverheadService implements IAlocacaoOverheadService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IAlocacaoOverheadDao dao;

    /** Instancia do Servico da entidade - AlocacaoPeriodoOh. */
    @Autowired
    private IAlocacaoPeriodoOhService alocacaoPeriodoOhService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createAlocacaoOverhead(final AlocacaoOverhead entity) {
        entity.setAlocacaoPeriodoOhs(this.generateAlocPerOhList(entity));

        dao.create(entity);
    }

    /**
     * Gera a lista de AlocacaoPeriodoOh e seus percetuais através das datas
     * (dataInicio e dataFim) da AlocacaoOverhead passada por parametro.
     * 
     * @param entity
     *            - AlocacaoOverhead que deseja gerar a lista de
     *            AlocacaoPeriodoOh
     * 
     * @return lista de AlocacaoPeriodoOh
     */
    private List<AlocacaoPeriodoOh> generateAlocPerOhList(
            final AlocacaoOverhead entity) {
        // lista de AlocacaoPeriodoOh
        List<AlocacaoPeriodoOh> alocPerOhList = new ArrayList<AlocacaoPeriodoOh>();

        // dataInicio, dataFim e mes da dataFim
        Date startDate = entity.getDataInicio();
        Date endDate = entity.getDataFim();

        // dataInicio e dataFim padrao (01/mm/yyyy)
        Date defaultStartDate = DateUtil.getDate(startDate);
        Date defaultEndDate = DateUtil.getDate(endDate);

        // range, lista de datas entre a vigencia
        List<Date> dateList = DateUtil.getValidityDateList(defaultStartDate,
                defaultEndDate);
        // para cada data do range da vigencia, cria uma AlocacaoPeriodoOh para
        // a AlocacaoOverhead que está sendo criada
        for (Date validityDate : dateList) {
            AlocacaoPeriodoOh alocPerOh = new AlocacaoPeriodoOh();
            alocPerOh.setAlocacaoOverhead(entity);
            alocPerOh.setDataAlocPeriodoOh(validityDate);

            double lastDay = 0;
            // se mes da data fim é maior do que o mes da data corrente, quer
            // dizer que ainda nao é a ultima data que veio da tela. Portanto
            // considera o mes da data corrente do range da vigencia
            if (defaultEndDate.compareTo(validityDate) > 0) {
                lastDay = DateUtil.getLastDayOfMonth(validityDate);
                // caso contrario, quer dizer que o mes da dataFim é o mesmo mes
                // da data corrente da vigencia. Portanto considera a dataFim
                // para pegar o ultimo dia que foi selecionado na tela
            } else {
                lastDay = DateUtil.getDayOfMonth(endDate);
            }
            // primeiro dia (dataInicio)
            double firstDay = DateUtil.getDayOfMonth(startDate);
            // ultimo dia do mes da data corrente do range
            double lastDayOfMonth = DateUtil.getLastDayOfMonth(validityDate);
            // calcula o percentual para o mes corrente
            double percentAlocPeriodoOh = ((lastDay - firstDay) + 1)
                    / lastDayOfMonth;

            alocPerOh.setPercentualAlocPeriodoOh(BigDecimal
                    .valueOf(percentAlocPeriodoOh));
            alocPerOhList.add(alocPerOh);

            // adiociona mes + 1 na data de inicio para considerar o proximo mes
            // nos calculos
            startDate = DateUtil.getDate(DateUtils.addMonths(startDate, 1));
        }

        return alocPerOhList;
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateAlocacaoOverhead(final AlocacaoOverhead entity) {
        List<AlocacaoPeriodoOh> alocPerOhList = alocacaoPeriodoOhService
                .findAlocPerOhByAlocacaoOverhead(entity);
        for (AlocacaoPeriodoOh alocacaoPeriodoOh : alocPerOhList) {
            alocacaoPeriodoOhService.removeAlocacaoPeriodoOh(alocacaoPeriodoOh);
        }

        entity.setAlocacaoPeriodoOhs(this.generateAlocPerOhList(entity));

        dao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeAlocacaoOverhead(final AlocacaoOverhead entity) {
        dao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public AlocacaoOverhead findAlocacaoOverheadById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<AlocacaoOverhead> findAlocacaoOverheadByFilter(
            final AlocacaoOverhead filter) {
        return dao.findByFilter(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<AlocacaoOverhead> findAlocacaoOverheadAll() {
        return dao.findAll();
    }

}