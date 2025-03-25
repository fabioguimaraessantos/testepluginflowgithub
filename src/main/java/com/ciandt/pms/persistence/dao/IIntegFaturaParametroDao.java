/**
 * Interface da camada de DAO da entidade IntegFaturaParametro
 */
package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.model.IntegFaturaParametro;
import com.ciandt.pms.model.TipoServico;


/**
 * 
 * A classe IIntegFaturaParametroDao proporciona a interface de acesso para a
 * camada DAO da entidade IntegFaturaParametro.
 * 
 * @since 22/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IIntegFaturaParametroDao extends
        IAbstractDao<Long, IntegFaturaParametro> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<IntegFaturaParametro> findByFilter(final IntegFaturaParametro filter);
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<IntegFaturaParametro> findAll();
    
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
    IntegFaturaParametro findByTpServicoFtReceitaAndEmpresa(
            final TipoServico tipoServico, final FonteReceita fonteReceita, final Empresa empresa);

}