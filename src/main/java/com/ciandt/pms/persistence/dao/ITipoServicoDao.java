/**
 * Interface da camada de DAO da entidade TipoServico
 */
package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.TipoServico;


/**
 * 
 * A classe ITipoServicoDao proporciona a interface de acesso para a camada DAO
 * da entidade TipoServico.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface ITipoServicoDao extends IAbstractDao<Long, TipoServico> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoServico> findAll();
    
    /**
     * Retorna todas as entidades relacionadas com o deal.
     * 
     * @param deal - entidade do tipo DealFiscal que 
     * se deseja saber os tipos serviços.
     * 
     * @return retorna uma lista de TipoServico.
     */
    List<TipoServico> findByDeal(final DealFiscal deal);

    /**
     * Retorna uma entidade TipoServico.
     * 
     * @param deal - entidade do tipo DealFiscal que 
     * se deseja saber os tipos serviços.
     * 
     * @param idTipoServico - Id da entidade TipoServico.
     * 
     * @return retorna uma uma entidade TipoServico se existir,
     * caso contrario retorna null.
     */
    TipoServico findByIdAndDeal(
            final Long idTipoServico, final DealFiscal deal);
    
    /**
     * Retorna uma entidade TipoServico.
     * 
     * @param serviceTypeName - nome da entidade TipoServiço
     * 
     * @return retorna uma uma entidade TipoServico se existir,
     * caso contrario retorna null.
     */
    TipoServico findByName(final String serviceTypeName);
    
    /**
     * Retorna todas as entidades relacionadas com a TaxaImposto.
     * 
     * @param codigoEmpresa - codigo da Empresa
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoServico> findAllRelatedTaxaImposto(final Long codigoEmpresa);

    /**
     *
     * @param codigoFonteReceita
     * @return Retorna uma lista de tipos de serviço filtrado por fonte receita.
     */
    List<TipoServico> findByFonteReceita(final Long codigoFonteReceita);

    List<TipoServico> findByIdIn(List<Long> tipoServicoCodes);

    List<TipoServico> findByIndicadorExibeMsaContrato(Boolean indicadorExibeMsaContrato);
}