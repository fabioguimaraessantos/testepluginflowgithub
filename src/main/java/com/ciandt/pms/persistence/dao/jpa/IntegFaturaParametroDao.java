/**
 * Classe IntegFaturaParametroDao que implementa a camada de DAO da entidade IntegFaturaParametro 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.model.IntegFaturaParametro;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.persistence.dao.IIntegFaturaParametroDao;


/**
 * 
 * A classe IntegFaturaParametroDao proporciona as funcionalidades de acesso ao
 * banco de dados para a entidade IntegFaturaParametro.
 * 
 * @since 22/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class IntegFaturaParametroDao extends
        AbstractDao<Long, IntegFaturaParametro> implements
        IIntegFaturaParametroDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public IntegFaturaParametroDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, IntegFaturaParametro.class);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<IntegFaturaParametro> findByFilter(
            final IntegFaturaParametro filter) {
        Long codigoEmpresa = Long.valueOf(0);
        Empresa empresa = filter.getEmpresa();
        if (empresa != null && empresa.getCodigoEmpresa() != null) {
            codigoEmpresa = empresa.getCodigoEmpresa();
        }

        Long codigoTipoServico = Long.valueOf(0);
        TipoServico tipoServico = filter.getTipoServico();
        if (tipoServico != null && tipoServico.getCodigoTipoServico() != null) {
            codigoTipoServico = tipoServico.getCodigoTipoServico();
        }

        Long codigoFonteReceita = Long.valueOf(0);
        FonteReceita fonteReceita = filter.getFonteReceita();
        if (fonteReceita != null
                && fonteReceita.getCodigoFonteReceita() != null) {
            codigoFonteReceita = fonteReceita.getCodigoFonteReceita();
        }

        List<IntegFaturaParametro> listResult =
                getJpaTemplate().findByNamedQuery(
                        IntegFaturaParametro.FIND_BY_FILTER,
                        new Object[]{codigoEmpresa, codigoEmpresa,
                                codigoTipoServico, codigoTipoServico,
                                codigoFonteReceita, codigoFonteReceita});

        return listResult;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<IntegFaturaParametro> findAll() {
        List<IntegFaturaParametro> listResult =
                getJpaTemplate()
                        .findByNamedQuery(IntegFaturaParametro.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param tipoServico
     *            entidade do do tipo TipoServico
     * @param fonteReceita
     *            entidade do tipo FonteReceita
     * @param empresa
     *            entidade do tipo Empresa
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public IntegFaturaParametro findByTpServicoFtReceitaAndEmpresa(
            final TipoServico tipoServico, final FonteReceita fonteReceita,
            final Empresa empresa) {

        List<IntegFaturaParametro> listResult =
                getJpaTemplate()
                        .findByNamedQuery(
                                IntegFaturaParametro.FIND_BY_TP_SERVICO_FT_RECEITA_AND_EMPRESA,
                                new Object[]{
                                        tipoServico.getCodigoTipoServico(),
                                        fonteReceita.getCodigoFonteReceita(),
                                        empresa.getCodigoEmpresa()});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}