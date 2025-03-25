package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.persistence.dao.ITiRecursoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * A classe TiRecursoDao proporciona as funcionalidades de persistencia
 * referente a entidade TiRecurso.
 * 
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class TiRecursoDao extends AbstractDao<Long, TiRecurso> implements
        ITiRecursoDao {

    /**
     * Contrutor padrão.
     * 
     * @param factory
     *            - Fabrica do do entity manager.
     */
    @Autowired
    public TiRecursoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TiRecurso.class);
    }

    /**
     * Busca todas as entidades ativas('A').
     * 
     * @return retorna uma lista de TiRecurso
     */
    @SuppressWarnings("unchecked")
    public List<TiRecurso> findAllActive() {
        List<TiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(TiRecurso.FIND_ALL_ACTIVE);

        return listResult;
    }

    /**
     * Realiza a busca pelos criterios do filtro.
     * 
     * @param filter
     *            - entidade do tipo TiRecurso que possui os criteiros do
     *            filtro.
     * 
     * @return retorna uma lista de TiRecurso.
     */
    @SuppressWarnings("unchecked")
    public List<TiRecurso> findByFilter(final TiRecurso filter) {

        Long companyCode = filter.getEmpresa() == null || filter.getEmpresa().getCodigoEmpresa() == null ? null : filter.getEmpresa().getCodigoEmpresa();

        List<TiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(
                        TiRecurso.FIND_BY_FILTER,
                        new Object[]{filter.getNomeTiRecurso(),
                                filter.getIndicadorTipoAlocacao(),
                                filter.getCodigoMnemonico(),
                                companyCode});

        return listResult;
    }

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param cp
     *            - ContratoPratica que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TiRecurso> findByContractAndPeriod(final ContratoPratica cp,
            final Date startDate, final Date endDate) {

        List<TiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(
                        TiRecurso.FIND_BY_CONTRACT_AND_PERIOD,
                        new Object[]{cp.getCodigoContratoPratica(), startDate,
                                endDate});

        return listResult;
    }

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param pessoa
     *            - Pessoa que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TiRecurso> findByPessoaAndPeriod(final Pessoa pessoa,
            final Date startDate, final Date endDate) {

        List<TiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(
                        TiRecurso.FIND_BY_PESSOA_AND_PERIOD,
                        new Object[]{pessoa.getCodigoPessoa(), startDate,
                                endDate});

        return listResult;
    }

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param pessoa
     *            - Pessoa que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @param tipo
     *            - tipo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TiRecurso> findByPessoaAndPeriodAndType(final Pessoa pessoa,
            final Date startDate, final Date endDate, final String tipo) {

        List<TiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(
                        TiRecurso.FIND_BY_PESSOA_AND_PERIOD_AND_TYPE,
                        new Object[]{pessoa.getCodigoPessoa(), startDate,
                                endDate, tipo});

        return listResult;
    }

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param type
     *            - Tipo de TiRecurso (CS-Contract Server, US-User Service)
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TiRecurso> findByType(final String type) {

        List<TiRecurso> listResult =
                getJpaTemplate().findByNamedQuery(TiRecurso.FIND_BY_TYPE,
                        new Object[]{type});

        return listResult;
    }

    public TiRecurso findByNomeTiRecurso(String nomeTiRecurso) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nomeTiRecurso", nomeTiRecurso);

        List<TiRecurso> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(TiRecurso.FIND_BY_NOME_TI_RECURSO, params);

        return results.get(0);
    }

}
