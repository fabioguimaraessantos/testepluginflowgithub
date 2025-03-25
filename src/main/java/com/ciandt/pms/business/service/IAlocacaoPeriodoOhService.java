package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IAlocacaoPeriodoOhService proporciona a intefece de acesso
 * a camada de serviço referente a entidade AlocacaoPeriodoOh.
 *
 * @since 11/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IAlocacaoPeriodoOhService {
    
    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeAlocacaoPeriodoOh(final AlocacaoPeriodoOh entity);

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
    List<AlocacaoPeriodoOh> findAlocacaoPeriodoOhByPessoaAndMonthDate(
            final Pessoa pessoa, final Date monthDate);
    
    /**
     * Retorna todas as entidades relacionadas com a 
     * Pessoa e data mes.
     * 
     *  @param pessoa - pessoa que se deseja buscar o relacionamento.
     *  
     *  @param startMonthDate - data mes fim.
     * 
     *  @param endMonthDate - data mes inicio
     *  
     * @return retorna uma lista com todas as entidades.
     */
    List<AlocacaoPeriodoOh> findAlocacaoPeriodoOhByPessoaAndBetweenMonthDate(
            final Pessoa pessoa, final Date startMonthDate, final Date endMonthDate);
    
    /**
     * Retorna todas as entidades relacionadas com a AlocacaoOverhead passada
     * por parametro.
     * 
     * @param alocacaoOverhead
     *            - AlocacaoOverhead que se deseja buscar as AlocacaPeriodoOh.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<AlocacaoPeriodoOh> findAlocPerOhByAlocacaoOverhead(
            final AlocacaoOverhead alocacaoOverhead);

    List<AlocacaoPeriodoOh> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month);
}