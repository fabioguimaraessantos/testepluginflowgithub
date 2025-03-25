package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.model.IntegFaturaParametro;
import com.ciandt.pms.model.TipoServico;


/**
 * 
 * A classe IFaturaService proporciona a interface de acesso para a camada de
 * serviço referente a entidade Fatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IIntegFaturaParametroService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @throws IntegrityConstraintException
     *             - ao tentar criar, se já existir um registro com a mesma
     *             Empresa, o mesmo TipoServico, a mesma FonteReceita
     */
    @Transactional
    void createIntegFaturaParametro(final IntegFaturaParametro entity)
            throws IntegrityConstraintException;

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     * 
     * @throws IntegrityConstraintException
     *             - ao tentar atualizar, se já existir um registro com a mesma
     *             Empresa, o mesmo TipoServico, a mesma FonteReceita
     */
    @Transactional
    void updateIntegFaturaParametro(final IntegFaturaParametro entity)
            throws IntegrityConstraintException;

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    @Transactional
    void removeIntegFaturaParametro(final IntegFaturaParametro entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    IntegFaturaParametro findIntFatParamById(final Long entityId);

    /**
     * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente
     * as entidades relacionadas a esta.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<IntegFaturaParametro> findIntFatParamByFilter(
            final IntegFaturaParametro filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<IntegFaturaParametro> findIntFatParamAll();
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param tipoServico
     *            entidade do do tipo TipoServico
     * @param fonteReceita
     *          entidade do tipo FonteReceita
     * @param empresa 
     *          entidade do tipo Empresa                     
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    IntegFaturaParametro findIntFatByTpServicoFtReceitaAndEmpresa(
            final TipoServico tipoServico, final FonteReceita fonteReceita, final Empresa empresa);

}