package com.ciandt.pms.business.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.business.service.IItemReceitaService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.persistence.dao.IItemReceitaDao;
import com.ciandt.pms.persistence.dao.IModuloDao;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ITaxaImpostoService;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.TaxaImposto;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.persistence.dao.ITaxaImpostoDao;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * A classe TaxaImpostoService proporciona as funcionalidades de serviço para a
 * entidade Taxa Imposto.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:fmaximino@ciandt.com">Felipe Almeida Maximino</a>
 * 
 */
@Service
public class TaxaImpostoService implements ITaxaImpostoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private ITaxaImpostoDao taxaImpostoDao;

    @Autowired
    private IItemReceitaService itemReceitaService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createTaxaImposto(final TaxaImposto entity) {
        setDataFimVigencia(entity);

        taxaImpostoDao.create(entity);
        taxaImpostoDao.flush();
    }

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateTaxaImposto(final TaxaImposto entity) {

        taxaImpostoDao.update(entity);
        taxaImpostoDao.flush();
    }

    /**
     * Atualiza a vigencia da entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * @param novaDataInicio
     *            Nova data inicio.
     */
    public void updateTaxaImpostoVigencia(final TaxaImposto entity,
            final Date novaDataInicio) {

        this.atualizaDataVigenciaOnRemove(entity);

        entity.setDataInicio(novaDataInicio);
        this.setDataFimVigencia(entity);

        taxaImpostoDao.update(entity);
        taxaImpostoDao.flush();
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TaxaImposto> findTaxaImpostoAll() {
        return taxaImpostoDao.findAll();
    }

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
    public List<TaxaImposto> findTaxaImpostoByEmpresaAndTipoServicoAndDataInicio(
            final Long codigoEmpresa, final Long codigoTipoServico,
            final Date dataInicio) {

        return taxaImpostoDao.findByEmpresaAndTipoServicoAndDataInicio(
                codigoEmpresa, codigoTipoServico, dataInicio);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<TaxaImposto> findTaxaImpostoByFilter(final TaxaImposto filter) {

        Date wildCard = null;
        try {
            wildCard = DateUtils.parseDate("01/1000",
                    new String[] {"MM/yyyy"});
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return taxaImpostoDao.findByFilter(filter, wildCard);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param entityId
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public TaxaImposto findTaxaImpostoById(final Long entityId) {
        return taxaImpostoDao.findById(entityId);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param taxaImposto
     *            que será apagada.
     * 
     * @return retorna true se removido com sucesso, caso contrario false.
     */
    public boolean removeTaxaImposto(final TaxaImposto taxaImposto) {
        atualizaDataVigenciaOnRemove(taxaImposto);

        taxaImpostoDao.remove(taxaImposto);
        taxaImpostoDao.flush();
        return true;
    }

    /**
     * determina data de fim da vigencia da entidade, baseado nas datas dos
     * outros taxaImpostos já existentes. se necessário, atualiza data de início
     * e fim dos outros taxaImpostos.
     * 
     * @param taIm
     *            - entidade do tipo TaxaImposto.
     * 
     */
    private void setDataFimVigencia(final TaxaImposto taIm) {

        TaxaImposto taimProximo = taxaImpostoDao.findProximo(taIm);

        TaxaImposto taimAnterior = taxaImpostoDao.findAnterior(taIm);

        if (taimProximo != null) {
            taIm.setDataFim(DateUtils
                    .addMonths(taimProximo.getDataInicio(), -1));
        } else {
            taIm.setDataFim(null);
        }

        if (taimAnterior != null) {
            taimAnterior.setDataFim(DateUtils.addMonths(taIm.getDataInicio(),
                    -1));

            taxaImpostoDao.update(taimAnterior);
            taxaImpostoDao.flush();
        }

    }

    /**
     * atualiza data de vigencia dos taxaImposto anterior quando da remoção do
     * taxaImposto passado por parametro .
     * 
     * @param taIm
     *            - entidade do tipo TaxaImposto.
     * 
     */
    private void atualizaDataVigenciaOnRemove(final TaxaImposto taIm) {

        TaxaImposto taimPosterior = taxaImpostoDao.findProximo(taIm);

        TaxaImposto taimAnterior = taxaImpostoDao.findAnterior(taIm);

        if (taimAnterior != null) {

            if (taimPosterior != null) {
                taimAnterior.setDataFim(DateUtils.addMonths(taimPosterior
                        .getDataInicio(), -1));
            } else {
                taimAnterior.setDataFim(null);
            }

            taxaImpostoDao.update(taimAnterior);
            taxaImpostoDao.flush();
        }
    }

    /**
     * Retorna a entidade cuja data de inicio é a maior (maxima).
     * 
     * @param taxa
     *            entidade do tipo TaxaImposto.
     * 
     * @return retorna uma entidade do tipo TaxaImposto
     */
    public TaxaImposto findTaxaMaxStartDateByEmpTipServ(final TaxaImposto taxa) {
        return taxaImpostoDao.findMaxStartDateByEmpTipServ(taxa);
    }

    public TaxaImposto findTaxesByDealFiscalCodeAndMonth( final Long dealFiscalCode, final Date dateMonth){
        return taxaImpostoDao.findTaxesByDealFiscalCodeAndMonth(dealFiscalCode, dateMonth);
    }

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
    public TaxaImposto findTaxaByEmpresaTipoServicoDate(final Empresa empresa,
            final TipoServico tipoServico, final Date dateMonth) {
        return taxaImpostoDao.findByEmpresaTipoServicoDate(empresa,
                tipoServico, dateMonth);
    }

    public Boolean updateTaxAndNetRevenueForecast(Date datames) {
        boolean updated = false;

        updated = itemReceitaService.updateAllMultiDealFiscalTaxAndNetRevenueAfterDate(datames);

        updated = itemReceitaService.updateAllTaxAndNetRevenueAfterDate(datames);

        return updated;
    }

}