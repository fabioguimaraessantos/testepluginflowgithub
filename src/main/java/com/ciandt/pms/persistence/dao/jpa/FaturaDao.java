/**
 * Classe FaturaDao que implementa a camada de DAO da entidade Fatura 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.persistence.dao.IFaturaDao;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.Constants;


/**
 * 
 * A classe FaturaDao proporciona as funcionalidades de acesso ao banco de dados
 * para a entidade Fatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class FaturaDao extends AbstractDao<Long, Fatura> implements IFaturaDao {

    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public FaturaDao(@Qualifier("entityManagerFactory") 
            final EntityManagerFactory factory) {
        super(factory, Fatura.class);

        entityManager = factory.createEntityManager();
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param dataPrevisaoBeg
     *            dataPrevisao inicial.
     * @param dataPrevisaoEnd
     *            dataPrevisao final.
     * @param cli
     *            entidade cliente.
     * @param msa
     *            entidade msa.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Fatura> findByFilter(final Fatura filter, final Cliente cli,
            final Msa msa, final Date dataPrevisaoBeg,
            final Date dataPrevisaoEnd) {
        Long codigoCliente = Long.valueOf(0);
        if (cli != null) {
            codigoCliente = cli.getCodigoCliente();
        }

        Long codigoMsa = Long.valueOf(0);
        if (msa != null) {
            codigoMsa = msa.getCodigoMsa();
        }

        Long codigoDealFiscal = Long.valueOf(0);
        if (filter.getDealFiscal() != null) {
            codigoDealFiscal = filter.getDealFiscal().getCodigoDealFiscal();
        }

        Long codigoMoeda = Long.valueOf(0);
        if (filter.getMoeda() != null) {
            codigoMoeda = filter.getMoeda().getCodigoMoeda();
        }

        String numeroDoc = null;
        if (filter.getNumeroDoc() != null) {
            numeroDoc = filter.getNumeroDoc();
        }

        List<Fatura> listResult;
          listResult = getJpaTemplate().findByNamedQuery(
            Fatura.FIND_BY_FILTER,
            new Object[] {dataPrevisaoBeg, dataPrevisaoEnd, codigoCliente,
              codigoCliente, codigoDealFiscal, codigoDealFiscal,
              codigoMoeda, codigoMoeda, numeroDoc, numeroDoc,
              filter.getIndicadorStatus(),
              filter.getIndicadorStatus(), codigoMsa,
              codigoMsa, filter.getCodigoLoginAe(),
              filter.getCodigoLoginAe() });

        return listResult;
    }

    /**
     * Busca uma lista de entidades pendentes.
     * 
     * @param rdf
     *            entidade do tipo ReceitaDealFiscal.
     * 
     * @return lista de entidades pendentes.
     */
    @SuppressWarnings("unchecked")
    public List<Fatura> findInvoicePedingByDeal(final ReceitaDealFiscal rdf) {

        List<Fatura> listResult = getJpaTemplate().findByNamedQuery(
                Fatura.FIND_INVOICE_PENDING_BY_DEAL,
                new Object[] {rdf.getDealFiscal().getCodigoDealFiscal(),
                        rdf.getCodigoReceitaDfiscal() });

        return listResult;
    }

    /**
     * Realiza a integração das Fatura referente ao id passado por parametro.
     * Esta integração é realizada através da chamada de uma procedure.
     * 
     * @param codigoFatura
     *            codigo da entidade Fatura.
     * 
     * 
     * @return código do resultado da execução da integração
     */
    @SuppressWarnings("unchecked")
    public Integer integrateFatura(final Long codigoFatura) {
        Object result = null;
        try {
            Query query = entityManager
                    .createNamedQuery(Fatura.INTEGRATE_FATURA);

            query.setParameter("param1", codigoFatura);
            query.setParameter("param2", LoginUtil.getLoggedUsername());

            List listResult = query.getResultList();

            if (listResult != null) {
                result = listResult.get(0);
            }

            if (result == null) {
                result = Integer.valueOf(0);
            }
        } catch (HibernateException e) {
            result = Integer.valueOf(0);
            e.printStackTrace();
        } catch (Exception e) {
            result = Integer.valueOf(0);
            e.printStackTrace();
        }

        return Integer.valueOf(result.toString());
    }

    /**
     * Busca se o pedido foi cancelado no ERP.
     * 
     * @param fatura
     *            entidade Fatura.
     * 
     * @return retorna true se cancelado ou não existe, caso contrario false.
     *         Retorna null caso ocorra uma excetion ao executar a consulta.
     */
    @SuppressWarnings({ "deprecation" })
    public Boolean isErpOrderCanceled(final Fatura fatura) {
        EntityManager entityManager = null;
        try {

            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            Session session = (Session) entityManager.getDelegate();

            // realiza a consulta do peido no ERP
            String query = "SELECT p.ped_in_sequencia, p.ped_ch_situacao "
                    + " FROM pms20.ven_pedidovenda p "
                    + " WHERE p.ped_in_sequencia = ? ";

            PreparedStatement st = session.connection().prepareStatement(query);

            //st.setLong(1, fatura.getCodigoFatura());
            st.setLong(1, fatura.getCodigoErpPedido());

            ResultSet rs = st.executeQuery();
            // pega o resultado
            if (rs.next()) {
                String canceledStatus = systemProperties
                        .getProperty("erp.pedido.status.cancelado");

                String status = rs.getString(2);
                // verifica se o status é igual a cancelado
                if (canceledStatus.equals(status)) {
                    return true;
                }
                return false;
            } else {
                return true;
            }

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
        }

        return null;
    }

    /**
     * Busca uma lista de entidades nao fechadas, status igual a Open e Aproved.
     * 
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Fatura> findAllNotClosed(final Date dataMes) {

        List<Fatura> listResult = getJpaTemplate().findByNamedQuery(
                Fatura.FIND_ALL_NOT_CLOSED, new Object[] {dataMes });

        return listResult;
    }
    
    /**
     * Busca uma lista de entidades no mês passado por parametro.
     * 
     * @param dataMes - data do mes.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Fatura> findByMonthDate(final Date dataMes) {

        List<Fatura> listResult = getJpaTemplate().findByNamedQuery(
                Fatura.FIND_BY_MONTH_DATE, new Object[] {dataMes });

        return listResult;
    }
    
    
    /**
     * Busca todas as faturas atrasadas, ou seseja,
     * com a data de pagamento passada e no status OPEN.
     * 
     * @return lista de faturas atrasadas.
     */
    @SuppressWarnings("unchecked")
    public List<Fatura> findDelayed() {

        List<Fatura> listResult = getJpaTemplate().findByNamedQuery(Fatura.FIND_DELAYED);

        return listResult;
    }
    
    /**
     * Busca que estão com a data pagamento para ser submetidas,
     * nos próximos 7 dias. 
     * 
     * @return lista de faturas a ser submetidas.
     */
    @SuppressWarnings("unchecked")
    public List<Fatura> findToBeSubmitted() {

        List<Fatura> listResult = getJpaTemplate().findByNamedQuery(Fatura.FIND_TO_BE_SUBMITTED);

        return listResult;
    }

	@Override
	@SuppressWarnings("unchecked")
	public Fatura findFaturaApagadaById(Long id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("faturaApagadaId", id);

		List<Fatura> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						FaturaApagada.FIND_BY_ID, params);

		if (results == null || results.isEmpty()) {
			return null;
		}

		return results.get(0);
	}
}