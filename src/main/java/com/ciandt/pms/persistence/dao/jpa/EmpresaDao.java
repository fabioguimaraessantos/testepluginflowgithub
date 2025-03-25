/**
 * Classe EmpresaDao que implementa a camada de DAO da entidade Empresa 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.persistence.dao.IEmpresaDao;


/**
 * 
 * A classe EmpresaDao proporciona as funcionalidades de acesso ao banco de
 * dados para a entidade Empresa.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class EmpresaDao extends AbstractDao<Long, Empresa> implements IEmpresaDao {

    /**
     * Intancia do entity manager do hibernate.
     */
    private EntityManager entityManager;

    /**
     * Construtor padrão.
     *
     * @param factory do tipo da entidade
     */
    @Autowired
    public EmpresaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Empresa.class);

        entityManager = factory.createEntityManager();
    }

    /**
     * Busca uma lista de Empresa filiais.
     *
     * @return lista de Empresa filiais.
     */
    @SuppressWarnings("unchecked")
    public List<Empresa> findAllSubsidiary() {
        List<Empresa> listResult = getJpaTemplate().findByNamedQuery(
                Empresa.FIND_ALL_SUBSIDIARY);
        return listResult;
    }

    /**
     * Busca uma lista de Empresa matriz.
     *
     * @return lista de Empresa matriz
     */
    @SuppressWarnings("unchecked")
    public List<Empresa> findAllParentCompany() {
        List listResult = getJpaTemplate().findByNamedQuery(
                Empresa.FIND_ALL_PARENT_COMPANY);
        return listResult;
    }

    /**
     * Busca uma de todas as Empresa .
     *
     * @return lista de Empresas.
     */
    @SuppressWarnings("unchecked")
    public List<Empresa> findAll() {
        List<Empresa> listResult = getJpaTemplate().findByNamedQuery(
                Empresa.FIND_ALL);
        return listResult;
    }

    public List<Empresa> findAllActiveForCombobox() {

        List<Empresa> listResult = getJpaTemplate().findByNamedQuery(
                Empresa.FIND_ALL_ACTIVE_FOR_COMBOBOX);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro - somente EmpresaFilho.
     *
     * @param filter entidade populada com os valores do filtro.
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Empresa> findByFilter(final Empresa filter) {
        Long codigoEmpresaPai = Long.valueOf(0);
        if (filter.getEmpresa() != null) {
            codigoEmpresaPai = filter.getEmpresa().getCodigoEmpresa();
        }

        List<Empresa> listResult = getJpaTemplate().findByNamedQuery(
                Empresa.FIND_BY_FILTER,
                new Object[]{filter.getNomeEmpresa(), filter.getNomeEmpresa(),
                        filter.getIndicadorAtivo(), filter.getIndicadorAtivo(),
                        codigoEmpresaPai, codigoEmpresaPai,
                        filter.getNumeroCnpj(), filter.getNumeroCnpj(),
                        filter.getTextoPais(), filter.getTextoPais(),
                        filter.getTextoEstado(), filter.getTextoEstado(),
                        filter.getTextoCidade(), filter.getTextoCidade(),
                        filter.getCodigoMnemonico(), filter.getCodigoMnemonico()});
        return listResult;
    }

    /**
     * Busca uma Empresa pelo acronym (mnemonico).
     *
     * @param acronym - código mnemonico
     * @return uma Empresa que possua o mnemonico passado por parâmetro
     */
    @SuppressWarnings("unchecked")
    public Empresa findByAcronym(final String acronym) {
        List<Empresa> listResult = getJpaTemplate().findByNamedQuery(
                Empresa.FIND_BY_ACRONYM, new Object[]{acronym});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @Override
    public List<Empresa> findWithAssociatedPosition() {

        try {
            Query q = entityManager.createNamedQuery(Empresa.FIND_COMPANIES_WITH_ASSOCIATED_POSITION);

            List<Object[]> result = q.getResultList();

            List<Empresa> resultObject = new ArrayList<Empresa>();

            for (Object obj : result) {
                Empresa empresa = new Empresa();
                empresa.setCodigoEmpresa(((BigDecimal)((Object[])obj)[0]).longValue());
                empresa.setNomeEmpresa((String)((Object[])obj)[1]);
                empresa.setCodigoEmpresaERP(((BigDecimal)((Object[])obj)[2]).longValue());

                resultObject.add(empresa);
            }

            return resultObject;

        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Empresa> findByIndicadorExibicaoMsaContrato(Boolean indicadorExibicaoMsaContrato) {
        List<Empresa> listResult = getJpaTemplate().findByNamedQuery(
                Empresa.FIND_BY_INDICADOR_MSA_CONTRATO, new Object[]{indicadorExibicaoMsaContrato});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult;
    }

    @Override
    public List<Empresa> findByIdIn(List<Long> empresaCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empresaCodes", empresaCodes);

        List<Empresa> result = getJpaTemplate().findByNamedQueryAndNamedParams(Empresa.FIND_BY_ID_IN, params);

        if (null != result && !result.isEmpty()) {
            return result;
        }
        return new ArrayList<Empresa>();
    }

    @Override
    public Empresa findEmpresaByERP(Long erpId) {

        List<Empresa> listResult = getJpaTemplate().findByNamedQuery(Empresa.FIND_BY_ERP, new Object[]{new BigDecimal(erpId)});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
    /**
     * Find Company by name
     *
     * @param name - name of the company
     * @return a company that has the name passed by parameter
     */
    @Override
    public Empresa findByName(String name) {

        List<Empresa> listResult = getJpaTemplate().findByNamedQuery(Empresa.FIND_BY_NAME, new Object[]{name});
        if (listResult.isEmpty()) {
            return null;
        }
        return listResult.get(0);
    }

    @Override
    public List<Empresa> findByCodigoTaxpayerCompany(String codigoTaxpayerCompany) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoTaxpayerCompany", codigoTaxpayerCompany);
        return getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        Empresa.FIND_BY_CODIGO_TAXPAYER_COMPANY,
                        params);
    }
}