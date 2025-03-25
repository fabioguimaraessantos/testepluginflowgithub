package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICustoInfraBaseService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CustoInfraBase;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICustoInfraBaseDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Service
public class CustoInfraBaseService implements ICustoInfraBaseService {

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private ICustoInfraBaseDao dao;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createCustoInfraBase(final CustoInfraBase entity) {
        dao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateCustoInfraBase(final CustoInfraBase entity) {
        dao.update(entity);
    }

    /**
     * Executa um update na lista de entidade passado por parametro.
     * 
     * @param entityList
     *            - lista de entidades que serao atualizadas.
     * 
     */
    public void updateCustoInfraBaseList(final List<CustoInfraBase> entityList) {
        for (CustoInfraBase entity : entityList) {
            dao.update(entity);
        }
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeCustoInfraBase(final CustoInfraBase entity) {
        dao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public CustoInfraBase findCustoInfraBaseById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<CustoInfraBase> findCustoInfraBaseByFilter(
            final CustoInfraBase filter) {
        return dao.findByFilter(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<CustoInfraBase> findCustoInfraBaseAll() {

        return dao.findAll();
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
            // se a Moeda é null, força Moeda padrão
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
     * Busca a entidade filtrando pelos parametros.
     * 
     * @param dataMes
     *            do CustoInfraBase.
     * @param cidadeBase
     *            entidade populada com os valores da CidadeBase.
     * 
     * @return lista de entidades que atendem ao criterio da CidadeBase e
     *         dataMes.
     */
    public CustoInfraBase findCustoInfBaseByDateAndCidadeBase(
            final Date dataMes, final CidadeBase cidadeBase) {
        return dao.findByDateAndCidadeBase(dataMes, cidadeBase);
    }

}