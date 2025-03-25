package com.ciandt.pms.business.service.impl;

import java.util.List;

import com.ciandt.pms.model.MsaContratoFilial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ITipoServicoService;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.persistence.dao.ITipoServicoDao;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * A classe TipoServicoService proporciona as funcionalidades de serviço para a
 * entidade TipoServico.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class TipoServicoService implements ITipoServicoService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private ITipoServicoDao tipoServicoDao;

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public TipoServico findTipoServicoById(final Long entityId) {
        return tipoServicoDao.findById(entityId);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TipoServico> findTipoServicoAll() {
        return tipoServicoDao.findAll();
    }

    public List<TipoServico> findByIndicadorExibeMsaContrato(Boolean indicadorExibeMsaContrato) {
        return tipoServicoDao.findByIndicadorExibeMsaContrato(indicadorExibeMsaContrato);
    }

    /**
     * Retorna uma entidade TipoServico.
     * 
     * @param deal
     *            - entidade do tipo DealFiscal que se deseja saber os tipos
     *            serviços.
     * 
     * @param idTipoServico
     *            - Id da entidade TipoServico.
     * 
     * @return retorna uma uma entidade TipoServico se existir, caso contrario
     *         retorna null.
     */

    public TipoServico findTipoServicoByIdAndDeal(final Long idTipoServico,
            final DealFiscal deal) {
        return tipoServicoDao.findByIdAndDeal(idTipoServico, deal);
    }

    /**
     * Retorna todas as entidades relacionadas com o deal.
     * 
     * @param deal
     *            - entidade do tipo DealFiscal que se deseja saber os tipos
     *            serviços.
     * 
     * @return retorna uma lista de TipoServico.
     */
    public List<TipoServico> findTipoServicoByDeal(final DealFiscal deal) {
        return tipoServicoDao.findByDeal(deal);
    }

    /**
     * Retorna uma entidade TipoServico.
     * 
     * @param serviceTypeName
     *            - nome da entidade TipoServiço
     * 
     * @return retorna uma uma entidade TipoServico se existir, caso contrario
     *         retorna null.
     */
    public TipoServico findTipoServicoByName(final String serviceTypeName) {
        return tipoServicoDao.findByName(serviceTypeName);
    }

    /**
     * Retorna todas as entidades relacionadas com a TaxaImposto.
     * 
     * @param codigoEmpresa - codigo da Empresa
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TipoServico> findTipServAllRelatTaxaImp(final Long codigoEmpresa) {
        return tipoServicoDao.findAllRelatedTaxaImposto(codigoEmpresa);
    }

    public  List<TipoServico> findByFonteReceita(final Long codigoFonteReceita){
        return tipoServicoDao.findByFonteReceita(codigoFonteReceita);
    }

    @Override
    public List<TipoServico> findByIdIn(List<Long> tipoServicoCodes) {
        return tipoServicoDao.findByIdIn(tipoServicoCodes);
    }


    @Override
    public TipoServico findById(Long tipoServicoId) {
        return tipoServicoDao.findById(tipoServicoId);
    }

    @Override
    public void create(TipoServico tipoServicoId) {

    }

}