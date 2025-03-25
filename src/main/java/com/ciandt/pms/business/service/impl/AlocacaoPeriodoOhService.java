package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IAlocacaoPeriodoOhService;
import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IAlocacaoPeriodoOhDao;


/**
 * 
 * A classe AlocacaoPeriodoOhService proporciona as funcionalidades da camada
 * de serviço referente a entidade AlocacaoPeriodoOh.
 *
 * @since 11/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class AlocacaoPeriodoOhService implements IAlocacaoPeriodoOhService {

    /** Dao da entidade AlocacaoPeriodoOh. */
    @Autowired
    private IAlocacaoPeriodoOhDao dao;
    
    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeAlocacaoPeriodoOh(final AlocacaoPeriodoOh entity) {
        dao.remove(entity);
    }
    
    /**
     * Retorna todas as entidades relacionadas com a 
     * Pessoa e data mes.
     * 
     *  @param pessoa - pessoa que se deseja buscar o relacionamento.
     *  
     *  @param monthDate - data mes que se deseja buscar.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<AlocacaoPeriodoOh> findAlocacaoPeriodoOhByPessoaAndMonthDate(
            final Pessoa pessoa, final Date monthDate) {
    
        return dao.findByPessoaAndMonthDate(pessoa, monthDate);
    }
    
    /**
     * Retorna todas as entidades relacionadas com a 
     * Pessoa e o periodo data mes.
     * 
     *  @param pessoa - pessoa que se deseja buscar o relacionamento.
     *  
     *  @param startMonthDate - data mes fim.
     * 
     *  @param endMonthDate - data mes inicio
     *  
     * @return retorna uma lista com todas as entidades.
     */
    public List<AlocacaoPeriodoOh> findAlocacaoPeriodoOhByPessoaAndBetweenMonthDate(
            final Pessoa pessoa, final Date startMonthDate, final Date endMonthDate) {
        
        return dao.findByPessoaAndBetweenMonthDate(pessoa, startMonthDate, endMonthDate);
    }
    
    /**
     * Retorna todas as entidades relacionadas com a AlocacaoOverhead passada
     * por parametro.
     * 
     * @param alocacaoOverhead
     *            - AlocacaoOverhead que se deseja buscar as AlocacaPeriodoOh.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<AlocacaoPeriodoOh> findAlocPerOhByAlocacaoOverhead(
            final AlocacaoOverhead alocacaoOverhead) {
       return dao.findByAlocacaoOverhead(alocacaoOverhead); 
    }

    @Override
    public List<AlocacaoPeriodoOh> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month) {
        return dao.findByPeopleCodeInAndMonth(peopleCodes, month);
    }
}