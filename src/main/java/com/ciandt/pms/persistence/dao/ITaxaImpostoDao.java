package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.TaxaImposto;
import com.ciandt.pms.model.TipoServico;


/**
 * 
 * A classe ITaxaImpostoDao proporciona a interface de acesso para a camada DAO
 * da entidade TaxaImposto.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:fmaximino@ciandt.com">Felipe Almeida Maximino</a>
 * 
 */
public interface ITaxaImpostoDao extends IAbstractDao<Long, TaxaImposto> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TaxaImposto> findAll();

    /**
     * Busca uma entidade pelo codigo da empresa, codigo do tipo de servico e
     * data inicio.
     * 
     * @param codigoEmpresa
     *            - Codigo da empresa
     * @param codigoTipoServico
     *            - Codigo do Tipo de Serviço
     * @param dataInicio
     *            - Data Inicio
     * 
     * @return lista de entidades que atendem aos parametros
     */
    List<TaxaImposto> findByEmpresaAndTipoServicoAndDataInicio(
            final Long codigoEmpresa, final Long codigoTipoServico,
            final Date dataInicio);

    /**
     * Retorna a entidade cuja data de inicio é a mais proxima da data dada como
     * parametro.
     * 
     * @param taxa
     *            entidade do tipo TaxaImposto.
     * 
     * @return retorna uma entidade do tipo TaxaImposto
     */
    TaxaImposto findAnterior(final TaxaImposto taxa);

    /**
     * Retorna a entidade cuja data de inicio é a mais proxima(anterior) da data
     * dada como parametro e que atende aos criterios de 'empresa' e 'tipo
     * serviço'.
     * 
     * @param taxa
     *            entidade do tipo TaxaImposto.
     * 
     * @return retorna uma entidade do tipo TaxaImposto
     */
    TaxaImposto findProximo(final TaxaImposto taxa);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @param wildCard
     *            - Date
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<TaxaImposto> findByFilter(final TaxaImposto filter, final Date wildCard);

    /**
     * Retorna a entidade cuja data de inicio é a maior (maxima).
     * 
     * @param taxa
     *            entidade do tipo TaxaImposto.
     * 
     * @return retorna uma entidade do tipo TaxaImposto
     */
    TaxaImposto findMaxStartDateByEmpTipServ(final TaxaImposto taxa);

    /**
     * Retorna a Taxa imposto referente a empresa, tipo servico e data passados
     * por parametro.
     * 
     * @param empresa
     *            - entidade do tipo Empresa.
     * @param tipoServico
     *            - entidade do tipo TipoServico
     * @param dateMonth
     *            - Data Mes referencia
     * 
     * @return retorna uma entidade do tipo TaxaImposto
     */
    TaxaImposto findByEmpresaTipoServicoDate(final Empresa empresa,
            final TipoServico tipoServico, final Date dateMonth);


    /**
     * Retorna a Taxa imposto referente a empresa, tipo servico e mês referente ao imposto
     * por parametro.
     *
     * @param contractLobCode
     *            - entidade do Contrato Pratica.
     * @param dealFiscalCode
     *            - entidade do Deal Fiscal
     * @param dateMonth
     *            - Data Mes referencia
     *
     * @return retorna uma entidade do tipo TaxaImposto
     */
    TaxaImposto findTaxesByDealFiscalCodeAndMonth(final Long dealFiscalCode, final Date dateMonth);
}