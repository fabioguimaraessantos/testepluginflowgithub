package com.ciandt.pms.business.service;

import java.util.List;

import com.ciandt.pms.model.MsaContratoFilial;
import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.TipoServico;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * A classe ITipoServicoService proporciona a interface para o servico referente
 * a entidade TipoServico.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface ITipoServicoService {

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    TipoServico findTipoServicoById(final Long entityId);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoServico> findTipoServicoAll();
    
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
    TipoServico findTipoServicoByIdAndDeal(
            final Long idTipoServico, final DealFiscal deal);
    
    /**
     * Retorna todas as entidades relacionadas com o deal.
     * 
     * @param deal - entidade do tipo DealFiscal que 
     * se deseja saber os tipos serviços.
     * 
     * @return retorna uma lista de TipoServico.
     */
    List<TipoServico> findTipoServicoByDeal(final DealFiscal deal);
    
    /**
     * Retorna uma entidade TipoServico.
     * 
     * @param serviceTypeName - nome da entidade TipoServiço
     * 
     * @return retorna uma uma entidade TipoServico se existir,
     * caso contrario retorna null.
     */
    TipoServico findTipoServicoByName(final String serviceTypeName);
    
    /**
     * Retorna todas as entidades relacionadas com a TaxaImposto.
     * 
     * @param codigoEmpresa - codigo da Empresa
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoServico> findTipServAllRelatTaxaImp(final Long codigoEmpresa);

    /**
     * Retorna uma lista de tipos de serviço filtrado por fonte receita.
     * @param codigoFonteReceita
     * @return Retorna uma lista de tipos de serviço filtrado por fonte receita.
     */
    List<TipoServico> findByFonteReceita(final Long codigoFonteReceita);

    List<TipoServico> findByIdIn(List<Long> tipoServicoCodes);

    TipoServico findById(Long tipoServicoId);

    @Transactional
    void create(TipoServico tipoServicoId);

    List<TipoServico> findByIndicadorExibeMsaContrato(Boolean indicadorExibeMsaContrato);
}