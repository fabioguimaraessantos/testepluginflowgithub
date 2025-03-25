package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.FatorReajuste;
import com.ciandt.pms.model.TipoContrato;


/**
 * 
 * A classe IFatorReajusteService proporciona a interface de acesso
 * a camada de negócio referente a entidade FatorReajuste.
 * 
 *
 * @since 25/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IFatorReajusteService {
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createFatorReajuste(final FatorReajuste entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateFatorReajuste(final FatorReajuste entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeFatorReajuste(final FatorReajuste entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    FatorReajuste findFatorReajusteById(final Long id);
    
    /**
     * Busca por todos os FatorRajuste associados a 
     * um tipo de contrato.
     * 
     * @param tipo - entidade do tipo TipoContrato.
     * 
     * @return uma lista de FatorRajuste.
     */
    List<FatorReajuste> findFatorReajusteByTipoContrato(
            final TipoContrato tipo);
    
    /**
     * Verifica se o fator reajuste já existe. Esta verificação 
     * é feita comparando o tipo de contrato e a data de inicio. 
     * 
     * @param fatorReajuste - Entidade do tipo FatorReajuste
     * 
     * @return retorna true se existe, caso contrario false.
     */
    Boolean exists(final FatorReajuste fatorReajuste);
    
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
    List<FatorReajuste> findFatorReajusteByPeriod(final TipoContrato tipoContrato, 
            final Date startDate, final Date endDate);
}
