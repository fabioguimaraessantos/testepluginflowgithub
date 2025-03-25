package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.persistence.dao.IPapelAlocacaoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 26/01/2010
 */
@Repository
public class PapelAlocacaoDao extends AbstractDao<Long, PapelAlocacao>
        implements IPapelAlocacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo NaturezaCentroLucro
     */
    @Autowired
    public PapelAlocacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PapelAlocacao.class);
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
    public List<PapelAlocacao> findByFilter(final PapelAlocacao filter) {
        Long codigoRecurso = Long.valueOf(0);
        if (filter.getRecurso() != null) {
            codigoRecurso = filter.getRecurso().getCodigoRecurso();
        }

        List<PapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                PapelAlocacao.FIND_BY_FILTER,
                new Object[] {filter.getNomePapelAlocacao(),
                        filter.getNomePapelAlocacao(),
                        filter.getIndicadorAtivo(), filter.getIndicadorAtivo(),
                        codigoRecurso, codigoRecurso, });
        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro - Fetch.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<PapelAlocacao> findByFilterFetch(final PapelAlocacao filter) {
        Long codigoRecurso = Long.valueOf(0);
        if (filter.getRecurso() != null) {
            codigoRecurso = filter.getRecurso().getCodigoRecurso();
        }

        List<PapelAlocacao> listResult = getJpaTemplate()
                .findByNamedQuery(
                        PapelAlocacao.FIND_BY_FILTER_FETCH,
                        new Object[] {filter.getNomePapelAlocacao(),
                                filter.getNomePapelAlocacao(),
                                filter.getIndicadorAtivo(),
                                filter.getIndicadorAtivo(), codigoRecurso,
                                codigoRecurso});
        return listResult;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<PapelAlocacao> findAll() {
        List<PapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                PapelAlocacao.FIND_ALL);

        return listResult;
    }
    
    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<PapelAlocacao> findAllActive() {
        List<PapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                PapelAlocacao.FIND_ALL_ACTIVE);

        return listResult;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @param recurso
     *            - Recurso associado ao PapelAlocacao.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public PapelAlocacao findByRecurso(final Recurso recurso) {
        List<PapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                PapelAlocacao.FIND_BY_RECURSO,
                new Object[] {recurso.getCodigoRecurso() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca uma lista de entidades sem associacao com o TcePapelAlocacao.
     * 
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<PapelAlocacao> findAllNotTceAssociated(final Date dataMes) {

        List<PapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                PapelAlocacao.FIND_ALL_NOT_TCE_ASSOCIATED,
                new Object[] {dataMes, dataMes });

        return listResult;
    }

}