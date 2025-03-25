package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.TaxaImposto;
import com.ciandt.pms.model.TipoServico;


/**
 * 
 * A classe ITaxaImpostoService proporciona interface para os serviços
 * referentes a entidade TaxaImposto.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:fmaximino@ciandt.com">Felipe Almeida Maximino</a>
 * 
 */
@Service
public interface ITaxaImpostoService {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TaxaImposto> findTaxaImpostoAll();

    /**
     * Insere uma entidade.
     * 
     * @param taim
     *            entidade a ser inserida.
     */
    @Transactional
    void createTaxaImposto(final TaxaImposto taim);

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateTaxaImposto(final TaxaImposto entity);

    /**
     * Busca uma entidade pelo codigo da empresa, codigo do tipo de servico e
     * data inicio.
     * 
     * @param codigoEmpresa
     *            - Codigo da empresa
     * @param codigoTipoServico
     *            - Codigo Tipo do Serviço
     * @param dataInicio
     *            - Data inicio da vigencia
     * 
     * @return lista de entidades que atendem aos parametros
     */
    List<TaxaImposto> findTaxaImpostoByEmpresaAndTipoServicoAndDataInicio(
            final Long codigoEmpresa, final Long codigoTipoServico,
            final Date dataInicio);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<TaxaImposto> findTaxaImpostoByFilter(final TaxaImposto filter);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    TaxaImposto findTaxaImpostoById(final Long id);

    /**
     * Deleta uma entidade.
     * 
     * @param taxaImposto
     *            que será apagada.
     * 
     * @return retorna true se removido com sucesso, caso contrario false.
     */
    @Transactional
    boolean removeTaxaImposto(final TaxaImposto taxaImposto);

    /**
     * Atualiza a vigencia da entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * @param novaDataInicio
     *            Nova data inicio.
     */
    @Transactional
    void updateTaxaImpostoVigencia(final TaxaImposto entity,
            final Date novaDataInicio);

    /**
     * Retorna a entidade cuja data de inicio é a maior (maxima).
     * 
     * @param taxa
     *            entidade do tipo TaxaImposto.
     * 
     * @return retorna uma entidade do tipo TaxaImposto
     */
    TaxaImposto findTaxaMaxStartDateByEmpTipServ(final TaxaImposto taxa);

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
    TaxaImposto findTaxaByEmpresaTipoServicoDate(final Empresa empresa,
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

    @Transactional
    Boolean updateTaxAndNetRevenueForecast(Date datames);
}