package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.GrupoCustoAreaOrcamentariaAud;
import com.ciandt.pms.persistence.dao.IGrupoCustoAreaOrcamentariaDao;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/08/2015
 */
@Repository
public class GrupoCustoAreaOrcamentariaDao extends AbstractDao<Long, GrupoCustoAreaOrcamentaria>
        implements IGrupoCustoAreaOrcamentariaDao {

    /**
     * Construtor padrão.
     *
     * @param factory Entidade do tipo Atividade
     */
    @Autowired
    public GrupoCustoAreaOrcamentariaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, GrupoCustoAreaOrcamentaria.class);
    }

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<GrupoCustoAreaOrcamentaria> findAll() {
        List<GrupoCustoAreaOrcamentaria> listResult = getJpaTemplate().findByNamedQuery(
                GrupoCustoAreaOrcamentaria.FIND_ALL);

        return listResult;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GrupoCustoAreaOrcamentaria> findByGrupoCustoId(
            Long codigoGrupoCusto) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoGrupoCusto", codigoGrupoCusto);

        List<GrupoCustoAreaOrcamentaria> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        GrupoCustoAreaOrcamentaria.FIND_BY_GRUPO_CUSTO_ID,
                        params);

        if (results.isEmpty()) {
            return null;
        }

        return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GrupoCustoAreaOrcamentaria findMaxDataInicioByCodigoGrupoCusto(
            Long codigoGrupoCusto) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoGrupoCusto", codigoGrupoCusto);

        List<GrupoCustoAreaOrcamentaria> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        GrupoCustoAreaOrcamentaria.FIND_MAX_DATA_INICIO_BY_CODIGO_GRUPO_CUSTO,
                        params);

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    @Override
    public List<GrupoCustoAreaOrcamentaria> findBySubAreaOrcamentariaAndVigencia(Long codigoAreaOrcamentaria, Date dataVigencia) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoAreaOrcamentaria", codigoAreaOrcamentaria);
        params.put("dataVigencia", dataVigencia);

        List<GrupoCustoAreaOrcamentaria> result = getJpaTemplate().findByNamedQueryAndNamedParams(
                GrupoCustoAreaOrcamentaria.FIND_BY_AREA_ORCAMENTARIA_AND_VIGENCIA, params
        );

        if (null != result && !result.isEmpty()) {
            for (GrupoCustoAreaOrcamentaria gcao : result) {
                Hibernate.initialize(gcao.getGrupoCusto());
            }

            return result;
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GrupoCustoAreaOrcamentariaAud> findHistoryByCodigo(
            Long codigoGrupoCusto) {
        return getJpaTemplate().findByNamedQuery(GrupoCustoAreaOrcamentariaAud.FIND_BY_CODIGO, codigoGrupoCusto);
    }

}
