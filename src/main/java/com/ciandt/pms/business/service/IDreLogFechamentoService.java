package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.DreLogFechamento;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 07/06/2010
 */
@Service
public interface IDreLogFechamentoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     */
    @Transactional
    void createDreLogFechamento(final DreLogFechamento entity);
    
    /**
     * Altera uma entidade.
     * 
     * @param entity
     *            entidade a ser alterada.
     * 
     */
    @Transactional
    void updateDreLogFechamento(final DreLogFechamento entity);

    /**
     * Valida o fechamento da DRE.
     * 
     * @param dataMes
     *            - mes que será validado o fechamento da DRE.
     * @return true se validacao com sucesso, caso contrario retorna false
     */
    @Transactional
    DreLogFechamento validateDreLogFechamento(final Date dataMes);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<DreLogFechamento> findDreLogFechamentoAll();

    /**
     * Busca a entidade filtrando pelos parametros.
     * 
     * @param dataMes
     *            do CustoInfraBase.
     * 
     * @return lista de entidades que atendem ao criterio da CidadeBase e
     *         dataMes.
     */
    DreLogFechamento findDreLogFechByDataMes(final Date dataMes);
    
    /**
     * Realiza a consolidação da DRE.
     * 
     * @param dataMes
     *            mes do fechamento.
     * 
     * @return retorna o status da execução da consolidacao. Se retorno igual a
     *         zero erro, caso contrario sucesso.
     */
    @Transactional
    Boolean consolidateDre(final Date dataMes);

}