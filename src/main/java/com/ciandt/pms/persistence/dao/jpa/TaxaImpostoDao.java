package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.util.NumberUtil;
import org.apache.velocity.runtime.parser.node.MathUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.TaxaImposto;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.persistence.dao.ITaxaImpostoDao;


/**
 * 
 * A classe TaxaImpostoDao proporciona as funcionalidades de acesso ao banco de
 * dados para a entidade TaxaImposto.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:fmaximino@ciandt.com">Felipe Almeida Maximino</a>
 * 
 */
@Repository
public class TaxaImpostoDao extends AbstractDao<Long, TaxaImposto> implements
        ITaxaImpostoDao {

    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public TaxaImpostoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TaxaImposto.class);

       entityManager = factory.createEntityManager();
    }

    /**
     * Busca por todos os TaxaImposto.
     * 
     * @return uma lista com todos os TaxaImposto.
     */
    @SuppressWarnings("unchecked")
    public List<TaxaImposto> findAll() {

        List<TaxaImposto> listResult =
                getJpaTemplate().findByNamedQuery(TaxaImposto.FIND_ALL);

        return listResult;
    }

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
     * @return lista de entidades que atendem aos parametros
     */
    @SuppressWarnings("unchecked")
    public List<TaxaImposto> findByEmpresaAndTipoServicoAndDataInicio(
            final Long codigoEmpresa, final Long codigoTipoServico,
            final Date dataInicio) {

        List<TaxaImposto> listResult =
                getJpaTemplate().findByNamedQuery(
                        TaxaImposto.FIND_BY_EMPRESA_TIPOSERVICO_DATAINICIO,
                        new Object[]{codigoEmpresa, codigoTipoServico,
                                dataInicio});

        return listResult;
    }

    /**
     * Retorna a entidade cuja data de inicio é a mais proxima da data dada como
     * parametro.
     * 
     * @param taxa
     *            entidade do tipo TaxaImposto.
     * 
     * @return retorna uma entidade do tipo TaxaImposto
     */
    @SuppressWarnings("unchecked")
    public TaxaImposto findAnterior(final TaxaImposto taxa) {

        List<TaxaImposto> listResult =
                getJpaTemplate().findByNamedQuery(
                        TaxaImposto.FIND_BY_DATA_ANTERIOR,
                        new Object[]{taxa.getEmpresa().getCodigoEmpresa(),
                                taxa.getTipoServico().getCodigoTipoServico(),
                                taxa.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

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
    @SuppressWarnings("unchecked")
    public TaxaImposto findProximo(final TaxaImposto taxa) {

        List<TaxaImposto> listResult =
                getJpaTemplate().findByNamedQuery(
                        TaxaImposto.FIND_BY_DATA_POSTERIOR,
                        new Object[]{taxa.getEmpresa().getCodigoEmpresa(),
                                taxa.getTipoServico().getCodigoTipoServico(),
                                taxa.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @param wildCardDate
     *            - Date
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<TaxaImposto> findByFilter(final TaxaImposto filter,
            final Date wildCardDate) {
        return getJpaTemplate().findByNamedQuery(
                        TaxaImposto.FIND_BY_FILTER,
                        new Object[]{filter.getEmpresa().getCodigoEmpresa(),
                                filter.getTipoServico().getCodigoTipoServico(),
                                filter.getDataInicio(), filter.getDataFim(),
                                wildCardDate});
    }

    /**
     * Retorna a entidade cuja data de inicio é a maior (maxima).
     * 
     * @param taxa
     *            entidade do tipo TaxaImposto.
     * 
     * @return retorna uma entidade do tipo TaxaImposto
     */
    @SuppressWarnings("unchecked")
    public TaxaImposto findMaxStartDateByEmpTipServ(final TaxaImposto taxa) {
        System.out.print(taxa.getTipoServico());
        System.out.println(taxa.getEmpresa());

        List<TaxaImposto> listResult =
                getJpaTemplate().findByNamedQuery(
                        TaxaImposto.FIND_MAX_START_DATE_BY_EMP_TIP_SERV,
                        new Object[]{taxa.getEmpresa().getCodigoEmpresa(),
                                taxa.getTipoServico().getCodigoTipoServico()});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Retorna a entidade cuja data de inicio é a maior (maxima).
     *
     * @param taxa
     *            entidade do tipo TaxaImposto.
     *
     * @return retorna uma entidade do tipo TaxaImposto
     */
    @SuppressWarnings("unchecked")
    public TaxaImposto findTaxesByDealFiscalCodeAndMonth(final Long dealFiscalCode, final Date dateMonth){

        TaxaImposto taxaImposto = new TaxaImposto();

        try {
            Query query = entityManager.createNamedQuery(TaxaImposto.FIND_TAX_BY_CLOB_DEALFISCAL_MONTH);

            query.setParameter("dealFiscalCode", dealFiscalCode);
            query.setParameter("dtMonth", dateMonth);

            List listResult = query.getResultList();

            if (listResult != null) {
                taxaImposto.setValorTaxaFederal((BigDecimal)((Object[])listResult.get(0))[1]);
                taxaImposto.setValorTaxaMunicipal((BigDecimal)((Object[])listResult.get(0))[2]);
            }else{
                taxaImposto = null;
            }
        } catch (HibernateException e) {

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return taxaImposto;
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
    @SuppressWarnings("unchecked")
    public TaxaImposto findByEmpresaTipoServicoDate(final Empresa empresa,
            final TipoServico tipoServico, final Date dateMonth) {

        List<TaxaImposto> listResult =
                getJpaTemplate().findByNamedQuery(
                        TaxaImposto.FIND_BY_EMPRESA_TIPOSERVICO_DATE,
                        new Object[]{empresa.getCodigoEmpresa(),
                                tipoServico.getCodigoTipoServico(), dateMonth});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}