package com.ciandt.pms.business.service.impl;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IBasePapelAlocacaoService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.IBasePapelAlocacaoDao;


/**
 * Define o Service da entidade.
 * 
 * @author cmantovani
 * @since 13/07/2011
 */
@Service
public class BasePapelAlocacaoService implements IBasePapelAlocacaoService {

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IBasePapelAlocacaoDao basePapelAlocacaoDao;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createBasePapelAlocacao(final BasePapelAlocacao entity) {
        basePapelAlocacaoDao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateBasePapelAlocacao(final BasePapelAlocacao entity) {
        basePapelAlocacaoDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeBasePapelAlocacao(final BasePapelAlocacao entity) {
        basePapelAlocacaoDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public BasePapelAlocacao findBasePapelAlocacaoById(final Long id) {
        BasePapelAlocacao basePapelAlocacao = basePapelAlocacaoDao.findById(id);

        return basePapelAlocacao;
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<BasePapelAlocacao> findBasePapelAlocacaoByFilter(
            final BasePapelAlocacao filter) {
        return basePapelAlocacaoDao.findByFilter(filter);
    }

    /**
     * Busca uma lista de entidades pelo filtro - Fetch.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<BasePapelAlocacao> findBasePapelAlocacaoByFilterFetch(
            final BasePapelAlocacao filter) {
        return basePapelAlocacaoDao.findByFilterFetch(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<BasePapelAlocacao> findBasePapelAlocacaoAll() {

        return basePapelAlocacaoDao.findAll();
    }

    /**
     * Recuprea a moeda corrente.
     * 
     * @param moeda
     *            - Moeda corrente
     * @return o pattern da Moeda.
     */
    public String getCurrency(final Moeda moeda) {
        if (moeda != null && moeda.getCodigoMoeda() != null
                && !StringUtils.isEmpty(moeda.getSiglaMoeda())) {
            // atribui o pattern dos valores conforme sigla da moeda
            return systemProperties
                    .getProperty(Constants.DEFAULT_PROPERTY_PATTERN_CURRENCY
                            + moeda.getSiglaMoeda().toLowerCase());
            // se a Moeda é null, força Moeda padrão por default no TCE do
            // BasePapelAlocacao
        } else {
            String defaultCurrencyCode = systemProperties
                    .getProperty(Constants.DEFAULT_PROPERTY_CURRENCY_CODE);

            if (!StringUtils.isEmpty(defaultCurrencyCode)) {
                Moeda defaultMoeda = moedaService.findMoedaById(Long
                        .valueOf(defaultCurrencyCode));

                if (defaultMoeda != null) {
                    return defaultMoeda.getSiglaMoeda();
                }
            }
        }

        return "";
    }

    /**
     * Busca uma entidade pela entidade.
     * 
     * @param basePapelAlocacao
     *            - entidade populada .
     * 
     * @return entidade buscada.
     */
    public BasePapelAlocacao findBasePapelAlocacaoByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao) {

        return basePapelAlocacaoDao.findByBasePapelAlocacao(basePapelAlocacao);
    }

}