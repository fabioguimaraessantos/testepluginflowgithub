package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IFatorReajusteService;
import com.ciandt.pms.model.FatorReajuste;
import com.ciandt.pms.model.TipoContrato;
import com.ciandt.pms.persistence.dao.IFatorReajusteDao;


/**
 * 
 * A classe FatorReajusteService proporciona as funcionalidades
 * da camada de negócio referente a entidade FatorReajuste.
 *
 * @since 25/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class FatorReajusteService implements IFatorReajusteService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IFatorReajusteDao dao;
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createFatorReajuste(final FatorReajuste entity) {
        
        FatorReajuste nextFr = dao.findNext(entity);
        FatorReajuste previousFr = dao.findPrevious(entity);
        
        if (nextFr != null) {
            entity.setDataFim(DateUtils.addMonths(
                    nextFr.getDataInicio(), -1));
        }
        
        if (previousFr != null) {
            previousFr.setDataFim(DateUtils.addMonths(
                    entity.getDataInicio(), -1));
            
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
    public void updateFatorReajuste(final FatorReajuste entity) {
        dao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeFatorReajuste(final FatorReajuste entity) {
        FatorReajuste fr = this.findFatorReajusteById(
                entity.getCodigoFatorReajuste());
        
        FatorReajuste nextFr = dao.findNext(fr);
        FatorReajuste previousFr = dao.findPrevious(fr);
        
        if (previousFr != null) {
            
            if (nextFr != null) {
                previousFr.setDataFim(
                        DateUtils.addMonths(nextFr.getDataInicio(), -1));
            } else {
                previousFr.setDataFim(null);
            }
            
            dao.update(previousFr);
        }
        
        dao.remove(fr);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public FatorReajuste findFatorReajusteById(final Long id) {
        return dao.findById(id);
    }
    
    /**
     * Busca por todos os FatorRajuste associados a 
     * um tipo de contrato.
     * 
     * @param tipo - entidade do tipo TipoContrato.
     * 
     * @return uma lista de FatorRajuste.
     */
    public List<FatorReajuste> findFatorReajusteByTipoContrato(
            final TipoContrato tipo) {
        return dao.findByTipoContrato(tipo);
    }
    
    /**
     * Verifica se o fator reajuste já existe. Esta verificação 
     * é feita comparando o tipo de contrato e a data de inicio. 
     * 
     * @param fatorReajuste - Entidade do tipo FatorReajuste
     * 
     * @return retorna true se existe, caso contrario false.
     */
    public Boolean exists(final FatorReajuste fatorReajuste) {
        FatorReajuste fr = dao.findByStartDate(fatorReajuste);
        
        if (fr == null) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Busca os reajuste entre periodos de data.
     * 
     * @param tipoContrato - TipoContrato
     * 
     * @param startDate - data inicial.
     * 
     * @param endDate - data fim.
     * 
     * @return retorna o FatorRajuste anterior.
     */
    public List<FatorReajuste> findFatorReajusteByPeriod(final TipoContrato tipoContrato, 
            final Date startDate, final Date endDate) {
        
        return dao.findByPeriod(tipoContrato, startDate, endDate);
    }
}
