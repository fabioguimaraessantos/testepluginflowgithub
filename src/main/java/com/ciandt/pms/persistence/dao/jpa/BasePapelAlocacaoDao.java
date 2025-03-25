package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.persistence.dao.IBasePapelAlocacaoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author cmantovani
 * @since 13/07/2011
 */
@Repository
public class BasePapelAlocacaoDao extends AbstractDao<Long, BasePapelAlocacao>
        implements IBasePapelAlocacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo NaturezaCentroLucro
     */
    @Autowired
    public BasePapelAlocacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, BasePapelAlocacao.class);
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
    public List<BasePapelAlocacao> findByFilter(final BasePapelAlocacao filter) {

        if (filter.getPapelAlocacao().getCodigoPapelAlocacao() == null) {
            filter.getPapelAlocacao().setCodigoPapelAlocacao(0L);
        }

        List<BasePapelAlocacao> listResult =
                getJpaTemplate().findByNamedQuery(
                        BasePapelAlocacao.FIND_BY_FILTER,
                        new Object[]{
                                filter.getPapelAlocacao()
                                        .getCodigoPapelAlocacao(),
                                filter.getPapelAlocacao()
                                        .getCodigoPapelAlocacao()});
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
    public List<BasePapelAlocacao> findByFilterFetch(
            final BasePapelAlocacao filter) {

        if (filter.getPapelAlocacao().getCodigoPapelAlocacao() == null) {
            filter.getPapelAlocacao().setCodigoPapelAlocacao(0L);
        }
        if (filter.getCodigoEmpresaERP() == null) {
            filter.setCodigoEmpresaERP(0L);
        }
        if (filter.getCidadeBase().getCodigoCidadeBase() == null) {
            filter.getCidadeBase().setCodigoCidadeBase(0L);
        }

        List<BasePapelAlocacao> listResult =
                getJpaTemplate().findByNamedQuery(
                        BasePapelAlocacao.FIND_BY_FILTER_FETCH,
                        new Object[]{
                                filter.getPapelAlocacao()
                                        .getNomePapelAlocacao(),
                                filter.getPapelAlocacao()
                                        .getNomePapelAlocacao(),
                                filter.getCodigoEmpresaERP(),
                                filter.getCodigoEmpresaERP(),
                                filter.getCidadeBase().getCodigoCidadeBase(),
                                filter.getCidadeBase().getCodigoCidadeBase()});
        return listResult;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<BasePapelAlocacao> findAll() {
        List<BasePapelAlocacao> listResult =
                getJpaTemplate().findByNamedQuery(BasePapelAlocacao.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma entidade pela entidade.
     * 
     * @param basePapelAlocacao
     *            - entidade populada .
     * 
     * @return entidade buscada.
     */
    @SuppressWarnings("unchecked")
    public BasePapelAlocacao findByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao) {
        List<BasePapelAlocacao> listResult =
                getJpaTemplate().findByNamedQuery(
                        BasePapelAlocacao.FIND_BY_BASE_PAPEL_ALOCACAO,
                        new Object[]{
                                basePapelAlocacao.getPapelAlocacao()
                                        .getCodigoPapelAlocacao(),
                                basePapelAlocacao.getCidadeBase()
                                        .getCodigoCidadeBase(),
                                basePapelAlocacao.getCodigoEmpresaERP()});

        if (!listResult.isEmpty()) {
            return listResult.get(0);
        }
        return null;
    }

}