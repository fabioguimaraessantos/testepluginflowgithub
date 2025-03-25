package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.util.LoginUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IBasePapelAlocacaoService;
import com.ciandt.pms.business.service.ICustoBasePapelAlocacaoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.model.CustoBasePapelAlocacao;
import com.ciandt.pms.persistence.dao.ICustoBasePapelAlocacaoDao;


/**
 * Define o Service da entidade.
 * 
 * @author cmantovani
 * @since 13/07/2011
 */
@Service
public class CustoBasePapelAlocacaoService implements
        ICustoBasePapelAlocacaoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IBasePapelAlocacaoService basePapelAlocacaoService;

    /** Instancia do DAO da entidade. */
    @Autowired
    private ICustoBasePapelAlocacaoDao custoBasePapelAlocacaoDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return retorna true se a data inicio da vigencia, é posterior a maior
     *         data existente no banco de dados. Caso contrario retorna false
     */
    public Boolean createCustoBasePapelAlocacao(
            final CustoBasePapelAlocacao entity) {

        // atualiza as entidades / referencias do hibernate
         entity.setBasePapelAlocacao(basePapelAlocacaoService
                .findBasePapelAlocacaoById(entity.getBasePapelAlocacao()
                        .getCodigoBasePapelAlocacao()));

        CustoBasePapelAlocacao existent = this.findCustoBasePapelAlocacaoByBasePapelAndDate(entity.getBasePapelAlocacao(), entity.getDataInicio());
        if (existent != null) {
            if (existent.getDataInicio().equals(entity.getDataInicio())
                    || existent.getDataFim() != null) {
                Messages.showWarning("createCustoBasePapelAlocacao",
                        Constants.MSG_ERROR_ADD_CUSTO_BASE_PAPEL_ALOCACAO_ALREADY_EXISTS);

                return Boolean.FALSE;
            } else {
                existent.setDataFim(DateUtils.addMonths(entity.getDataInicio(), -1));
                custoBasePapelAlocacaoDao.update(existent);
            }
        }

        CustoBasePapelAlocacao next = custoBasePapelAlocacaoDao.findNext(entity);
        if (next != null) {
            entity.setDataFim(DateUtils.addMonths(next.getDataInicio(), -1));
        }

        entity.setUpdateAuthor(LoginUtil.getLoggedUsername());
        entity.setLastUpdated(new Date());
        custoBasePapelAlocacaoDao.create(entity);

        return Boolean.TRUE;

    }

    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateCustoBasePapelAlocacao(final CustoBasePapelAlocacao entity) {
        entity.setUpdateAuthor(LoginUtil.getLoggedUsername());
        entity.setLastUpdated(new Date());
        custoBasePapelAlocacaoDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @return true se CustoBasePapelAlocacao foi removida corretamente, coso
     *         contrário retorna false
     */
    public Boolean removeCustoBasePapelAlocacao(
            final CustoBasePapelAlocacao entity) {
        CustoBasePapelAlocacao cbpa = custoBasePapelAlocacaoDao.findById(entity
                .getCodigoCustoBasePapelAloc());

        CustoBasePapelAlocacao previous = custoBasePapelAlocacaoDao
                .findPrevious(cbpa);

        if (previous != null) {

            CustoBasePapelAlocacao next = custoBasePapelAlocacaoDao
                    .findNext(cbpa);
            if (next != null) {
                previous.setDataFim(DateUtils.addMonths(next.getDataInicio(),
                        -1));
            } else {
                previous.setDataFim(null);
            }

            custoBasePapelAlocacaoDao.update(previous);
        }

        custoBasePapelAlocacaoDao.remove(cbpa);

        return Boolean.valueOf(true);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public CustoBasePapelAlocacao findCustoBasePapelAlocacaoById(final Long id) {
        return custoBasePapelAlocacaoDao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<CustoBasePapelAlocacao> findCustoBasePapelAlocacaoAll() {
        return custoBasePapelAlocacaoDao.findAll();
    }

    /**
     * Busca uma lista de entidades pelo PapelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do BasePapelAlocacao.
     */
    public List<CustoBasePapelAlocacao> findCustoBasePapelAlocacaoByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao) {
        return custoBasePapelAlocacaoDao
                .findByBasePapelAlocacao(basePapelAlocacao);
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    public CustoBasePapelAlocacao findMaxStartDateByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao) {
        return custoBasePapelAlocacaoDao
                .findMaxStartDateByBasePapelAlocacao(basePapelAlocacao);
    }

    /**
     * Busca a entidade no qual intervalo possua a pada passado por parametro,
     * referente ao PapelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * @param date
     *            Data que se deseja saber o valor do CustoBase do
     *            PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do BasePapelAlocacao.
     */
    public CustoBasePapelAlocacao findCustoBasePapelAlocacaoByBasePapelAndDate(
            final BasePapelAlocacao basePapelAlocacao, final Date date) {
        return custoBasePapelAlocacaoDao.findByBasePapelAndDate(
                basePapelAlocacao, date);
    }

    public CustoBasePapelAlocacao findCustoBasePapelAlocacaoByBasePapelAndCurrentDate(
            final BasePapelAlocacao basePapelAlocacao) {
        return custoBasePapelAlocacaoDao.findByBasePapelAndCurrentDate(
                basePapelAlocacao);
    }

    @Override
    public List<CustoBasePapelAlocacao> findCustoBasePapelAlocacaoByStartDateGreaterThan(
            final Long basePapelAlocacaoCode, final Date startDate) {
        return custoBasePapelAlocacaoDao.findByStartDateGreaterThan(basePapelAlocacaoCode, startDate);
    }


}