package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.business.service.ICustoTiRecursoService;
import com.ciandt.pms.model.CustoTiRecurso;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.persistence.dao.ICustoTiRecursoDao;


/**
 * 
 * A classe CustoTiRecursoService proporciona as funcionalidades da camada de
 * serviços referente a entidade CustoTiRecurso.
 * 
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class CustoTiRecursoService implements ICustoTiRecursoService {

    /** Instancia do DAO da entidade CustoTiRecurso. */
    @Autowired
    private ICustoTiRecursoDao dao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createCustoTiRec(final CustoTiRecurso entity) {

        CustoTiRecurso nextFr = dao.findNext(entity);
        CustoTiRecurso previousFr = dao.findPrevious(entity);

        if (nextFr != null) {
            entity.setDataFim(DateUtils.addMonths(nextFr.getDataInicio(), -1));
        }

        if (previousFr != null) {
            previousFr.setDataFim(DateUtils.addMonths(entity.getDataInicio(),
                    -1));

            dao.update(previousFr);
        }

        dao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateCustoTiRec(final CustoTiRecurso entity) {
        dao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    public void removeCustoTiRec(final CustoTiRecurso entity) {
        CustoTiRecurso fr =
                this.findCustoTiRecById(entity.getCodigoCustoTiRecurso());

        CustoTiRecurso nextFr = dao.findNext(fr);
        CustoTiRecurso previousFr = dao.findPrevious(fr);

        if (previousFr != null) {

            if (nextFr != null) {
                previousFr.setDataFim(DateUtils.addMonths(nextFr
                        .getDataInicio(), -1));
            } else {
                previousFr.setDataFim(null);
            }

            dao.update(previousFr);
        }

        dao.remove(fr);
    }

    /**
     * Busca os custoTiRecurso por tiRecurso.
     * 
     * @param tiRecurso
     *            - tiRecurso com a busca
     * @return lista com o resultado da consulta
     */
    public List<CustoTiRecurso> findCustoTiRecursoByTiRecurso(
            final TiRecurso tiRecurso) {
        return dao.findByTiRecurso(tiRecurso);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public CustoTiRecurso findCustoTiRecById(final Long id) {
        return dao.findById(id);
    }
}
