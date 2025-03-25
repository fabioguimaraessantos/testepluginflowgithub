package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.RegistroAtividade;
import com.ciandt.pms.persistence.dao.IRegistroAtividadeDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 17/08/2010
 */
@Repository
public class RegistroAtividadeDao extends AbstractDao<Long, RegistroAtividade>
        implements IRegistroAtividadeDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo RegistroAtividade
     */
    @Autowired
    public RegistroAtividadeDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, RegistroAtividade.class);
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
    public List<RegistroAtividade> findByFilter(final RegistroAtividade filter) {

        Long codigoContratoPratica = 0L;
        if (filter.getContratoPratica() != null
                && filter.getContratoPratica().getCodigoContratoPratica() != null) {
            codigoContratoPratica =
                    filter.getContratoPratica().getCodigoContratoPratica();
        }

        Long codigoGrupoCusto = 0L;
        if (filter.getGrupoCusto() != null
                && filter.getGrupoCusto().getCodigoGrupoCusto() != null) {
            codigoGrupoCusto = filter.getGrupoCusto().getCodigoGrupoCusto();
        }

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        Long codigoAtividade = filter.getAtividade().getCodigoAtividade();
        Date dataMesChp = filter.getDataMesChp();

        List<RegistroAtividade> listResult =
                getJpaTemplate().findByNamedQuery(
                        RegistroAtividade.FIND_BY_FILTER,
                        new Object[]{codigoContratoPratica,
                                codigoContratoPratica, codigoGrupoCusto,
                                codigoGrupoCusto, codigoLogin, codigoLogin,
                                codigoAtividade, codigoAtividade, dataMesChp});

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return entidade que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public RegistroAtividade findUnique(final RegistroAtividade filter) {

        Long codigoContratoPratica = 0L;
        if (filter.getContratoPratica() != null
                && filter.getContratoPratica().getCodigoContratoPratica() != null) {
            codigoContratoPratica =
                    filter.getContratoPratica().getCodigoContratoPratica();
        }

        Long codigoGrupoCusto = 0L;
        if (filter.getGrupoCusto() != null
                && filter.getGrupoCusto().getCodigoGrupoCusto() != null) {
            codigoGrupoCusto = filter.getGrupoCusto().getCodigoGrupoCusto();
        }

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        Long codigoAtividade = filter.getAtividade().getCodigoAtividade();
        Date dataRegistroAtividade = filter.getDataRegistroAtividade();

        List<RegistroAtividade> listResult =
                getJpaTemplate().findByNamedQuery(
                        RegistroAtividade.FIND_UNIQUE,
                        new Object[]{codigoContratoPratica,
                                codigoContratoPratica, codigoGrupoCusto,
                                codigoGrupoCusto, codigoLogin, codigoAtividade,
                                dataRegistroAtividade});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<RegistroAtividade> findAll() {
        List<RegistroAtividade> listResult =
                getJpaTemplate().findByNamedQuery(RegistroAtividade.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pela dataMesChp.
     * 
     * @param dataMesChp
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    @SuppressWarnings("unchecked")
    public List<RegistroAtividade> findByDataMesChp(final Date dataMesChp) {

        List<RegistroAtividade> listResult =
                getJpaTemplate().findByNamedQuery(
                        RegistroAtividade.FIND_BY_DATA_MES_CHP,
                        new Object[]{dataMesChp});

        return listResult;
    }

}