package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.AjusteReceitaForecast;
import com.ciandt.pms.persistence.dao.IAjusteReceitaForecastDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AjusteReceitaForecastDao extends AbstractDao<Long, AjusteReceitaForecast>
        implements IAjusteReceitaForecastDao {

    /**
     * Construtor padr�o.
     *
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public AjusteReceitaForecastDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, AjusteReceitaForecast.class);
    }

    @Override
    public List<AjusteReceitaForecast> findByContratoPratica(Long codigoContratoPratica) {
        Map<String, Object> params = new HashMap<>();
        params.put("codigoContratoPratica", codigoContratoPratica);

        List<AjusteReceitaForecast> ajusteReceitaForecastList = getJpaTemplate().findByNamedQuery(
                AjusteReceitaForecast.FIND_BY_CONTRATO_PRATICA, params);
        return ajusteReceitaForecastList != null ? ajusteReceitaForecastList : new ArrayList<>();
    }

    @Override
    public List<AjusteReceitaForecast> findByContratoPraticaAndDataMesReceita(Long codigoContratoPratica, Date dataMesReceita) {

        Map<String, Object> params = new HashMap<>();
        params.put("codigoContratoPratica", codigoContratoPratica);
        params.put("dataMesReceita", dataMesReceita);

        List<AjusteReceitaForecast> ajusteReceitaForecastList = getJpaTemplate().findByNamedQueryAndNamedParams(
                AjusteReceitaForecast.FIND_BY_CONTRATO_PRATICA_AND_DATA_MES_RECEITA, params);
        return ajusteReceitaForecastList != null ? ajusteReceitaForecastList : new ArrayList<>();
    }
}
