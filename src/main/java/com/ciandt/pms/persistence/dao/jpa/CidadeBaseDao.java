package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.job.CompareMapaAlocSnapshotsJob;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.persistence.dao.ICidadeBaseDao;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Define o DAO da entidade.
 *
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class CidadeBaseDao extends AbstractDao<Long, CidadeBase> implements
        ICidadeBaseDao {

    /**
     * Contrutor padrão.
     *
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public CidadeBaseDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CidadeBase.class);
    }

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<CidadeBase> findAll() {
        List<CidadeBase> listResult =
                getJpaTemplate().findByNamedQuery(CidadeBase.FIND_ALL);

        return listResult;
    }

    /**
     * Retorna todas as entidades ativas.
     *
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<CidadeBase> findAllActive() {
        List<CidadeBase> listResult =
                getJpaTemplate().findByNamedQuery(CidadeBase.FIND_ALL_ACTIVE);

        return listResult;
    }

    /**
     * Retorna entidade por sigla.
     *
     * @param sigla
     *            sigla.
     * @return entidade.
     */
    @SuppressWarnings("unchecked")
    public CidadeBase findBySigla(final String sigla) {
        List<CidadeBase> listResult =
                getJpaTemplate().findByNamedQuery(CidadeBase.FIND_BY_SIGLA,
                        new Object[]{sigla});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Realiza consulta na tabela cidade_base_filial
     *
     * @param cidadeBaseCode
     *            id da entidade ReceitaDealFiscal.
     *
     *
     * @return código da empresa vinculada a cidade base do colaborador
     */
    @SuppressWarnings("unchecked")
    public Long findEmpresaByCidadeBase(final Long cidadeBaseCode) {

        Object result = null;
        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("param", cidadeBaseCode);
            List listResult = getJpaTemplate().findByNamedQueryAndNamedParams(CidadeBase.FIND_EMPRESA_BY_CIDADE_BASE, params);

            if (listResult != null) {
                result = listResult.get(0);
            }

            if (result == null) {
                result = Long.valueOf(0);
            }
        } catch (HibernateException e) {
            result = Long.valueOf(0);
            e.printStackTrace();
        } catch (Exception e) {
            result = Long.valueOf(0);
            e.printStackTrace();
        }

        return Long.valueOf(result.toString());
    }

    @SuppressWarnings("unchecked")
    public List<CidadeBase> findActiveByEmpresa(final Long codigoEmpresa) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyCode", codigoEmpresa);

        List<BigDecimal> listResult = getJpaTemplate().findByNamedQueryAndNamedParams(CidadeBase.FIND_ACTIVE_BY_EMPRESA, params);
        List<CidadeBase> cidadeBaseList = new ArrayList<CidadeBase>();

        if (listResult != null) {
            for (BigDecimal result : listResult) {
                cidadeBaseList.add(this.findById(result.longValue()));
            }
        }

        return cidadeBaseList;
    }

    @Override
    public List<CidadeBase> findByIds(final List<Long> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        return getJpaTemplate().findByNamedQueryAndNamedParams(CidadeBase.FIND_BY_IDS, params);
    }

    @Override
    public List<CidadeBase> findActiveByPmsEmpresa(Long codigoPmsEmpresa) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("companyCode", codigoPmsEmpresa);

        List<BigDecimal> listResult = getJpaTemplate().findByNamedQueryAndNamedParams(CidadeBase.FIND_ACTIVE_BY_PMS_EMPRESA, params);

        ArrayList<Long> ids = new ArrayList<Long>();
        for (BigDecimal id : listResult) {
            ids.add(id.longValue());
        }
        return this.findByIds(ids);
    }

    public CidadeBase findByNome(final String nome) {
        List<CidadeBase> listResult =
                getJpaTemplate().findByNamedQuery(CidadeBase.FIND_BY_NOME,
                        new Object[]{nome});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @Override
    public List<CidadeBase> findByFilter(final CidadeBase filter) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", filter.getNomeCidadeBase());
        params.put("siglaCidadeBase", filter.getSiglaCidadeBase());
        params.put("codigoEmpresaERP", filter.getCodigoEmpresaERP());
        params.put("codigoMoeda", filter.getMoeda() != null ? filter.getMoeda().getCodigoMoeda() : null);
        params.put("indicadorAtivo", filter.getIndicadorAtivo());

        return getJpaTemplate().findByNamedQueryAndNamedParams(
                CidadeBase.FIND_BY_FILTER, params);

    }

    @Override
    public Boolean findIfHasDependency(final Long cidadeBaseCode) {
        Map<String, Object> params = new HashMap<>();

        params.put("codigoCidadeBase", cidadeBaseCode);

        List<Long> listResult = getJpaTemplate().findByNamedQueryAndNamedParams(
                CidadeBase.FIND_IF_HAS_DEPENDENCY, params);

        return !listResult.isEmpty();
    }
}
