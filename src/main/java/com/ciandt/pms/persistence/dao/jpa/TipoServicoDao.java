package com.ciandt.pms.persistence.dao.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.persistence.dao.ITipoServicoDao;


/**
 * 
 * Define o DAO da entidade TipoServico.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class TipoServicoDao extends AbstractDao<Long, TipoServico> implements
        ITipoServicoDao {

    /**
     * Contrutor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public TipoServicoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TipoServico.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TipoServico> findAll() {
        List<TipoServico> listResult = getJpaTemplate().findByNamedQuery(
                TipoServico.FIND_ALL);

        return listResult;
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
    @SuppressWarnings("unchecked")
    public List<TipoServico> findByDeal(final DealFiscal deal) {
        List<TipoServico> listResult = getJpaTemplate().findByNamedQuery(
                TipoServico.FIND_BY_DEAL,
                new Object[] {deal.getCodigoDealFiscal() });

        return listResult;
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
    @SuppressWarnings("unchecked")
    public TipoServico findByIdAndDeal(final Long idTipoServico,
            final DealFiscal deal) {
        List<TipoServico> listResult = getJpaTemplate().findByNamedQuery(
                TipoServico.FIND_BY_CODIGO_AND_DEAL,
                new Object[] {deal.getCodigoDealFiscal(), idTipoServico });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
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
    @SuppressWarnings("unchecked")
    public TipoServico findByName(final String serviceTypeName) {

        List<TipoServico> listResult = getJpaTemplate().findByNamedQuery(
                TipoServico.FIND_BY_NAME, new Object[] {serviceTypeName });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Retorna todas as entidades relacionadas com a TaxaImposto.
     * 
     * @param codigoEmpresa
     *            - codigo da Empresa
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TipoServico> findAllRelatedTaxaImposto(final Long codigoEmpresa) {
        List<TipoServico> listResult = getJpaTemplate().findByNamedQuery(
                TipoServico.FIND_ALL_RELATED_TAXA_IMPOSTO,
                new Object[] {codigoEmpresa });

        return listResult;
    }

    /**
     * Retorna todas as entidades filtradas por codigoFonteReceita
     *
     * @param codigoFonteReceita - ID da tabela fonte_receita que é chave estrangeira na tabela tipo_serviço
     *
     * @return Retorna uma lista de tipos de serviço filtrado por fonte receita.
     */
    @SuppressWarnings("unchecked")
    public List<TipoServico> findByFonteReceita(final Long codigoFonteReceita) {
        List<TipoServico> listResult = getJpaTemplate().findByNamedQuery(TipoServico.FIND_BY_FONTE_RECEITA, new Object[] {codigoFonteReceita });

        return listResult;
    }

    @Override
    public List<TipoServico> findByIdIn(List<Long> tipoServicoCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tipoServicoCodes", tipoServicoCodes);

        List<TipoServico> result = getJpaTemplate().findByNamedQueryAndNamedParams(TipoServico.FIND_BY_ID_IN, params);

        if (null != result && !result.isEmpty()) {
            return result;
        }
        return new ArrayList<TipoServico>();
    }

    public List<TipoServico> findByIndicadorExibeMsaContrato(Boolean indicadorExibeMsaContrato) {
        List<TipoServico> listResult = getJpaTemplate().findByNamedQuery(TipoServico.FIND_BY_EXIBE_MSA_CONTRATO, new Object[] { indicadorExibeMsaContrato });

        return listResult;
    }

}