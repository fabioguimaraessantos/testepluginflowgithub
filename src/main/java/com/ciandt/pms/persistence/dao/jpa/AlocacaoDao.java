package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.persistence.dao.IAlocacaoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.*;


/**
 * 
 * A classe AlocacaoDao proporciona as funcionalidades de acesso ao banco para
 * referentes a entidade Alocacao.
 * 
 * @since 14/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class AlocacaoDao extends AbstractDao<Long, Alocacao> implements
        IAlocacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public AlocacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Alocacao.class);
    }

    /**
     * Busca por alocações de uma mapa.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @return uma lista de Alocações do Mapa.
     */
    @SuppressWarnings("unchecked")
    public List<Alocacao> findByMapaAlocacao(final MapaAlocacao mapa) {

        List<Alocacao> listResult =
                getJpaTemplate().findByNamedQuery(
                        Alocacao.FIND_BY_MAPA_ALOCACAO,
                        new Object[]{mapa.getCodigoMapaAlocacao()});

        return listResult;
    }

    /**
     * Busca por alocações de uma mapa dentro de um periodo.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @param startDate
     *            - Data inicial da vigencia
     * 
     * @param endDate
     *            - Data final da vigencia.
     * 
     * @return uma lista de Alocações do Mapa.
     */
    @SuppressWarnings("unchecked")
    public List<Alocacao> findByMapaAlocacaoAndPeriod(final MapaAlocacao mapa,
            final Date startDate, final Date endDate) {

        List<Alocacao> listResult =
                getJpaTemplate().findByNamedQuery(
                        Alocacao.FIND_BY_MAPA_ALOCACAO_AND_PERIOD,
                        new Object[]{mapa.getCodigoMapaAlocacao(), startDate,
                                endDate});

        return listResult;
    }

    /**
     * Busca a alocacao de uma mapa de acordo com o recurso e perfilvendido.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @param recurso
     *            - entidade do tipo Recurso.
     * 
     * @param perfil
     *            - entidade do tipo PerfilVendido.
     * 
     * @return uma Alocacao.
     */
    @SuppressWarnings("unchecked")
    public Alocacao findByMapaAlocacaoAndRecursoAndPerfil(
            final MapaAlocacao mapa, final Recurso recurso,
            final PerfilVendido perfil) {

        List<Alocacao> result =
                getJpaTemplate().findByNamedQuery(
                        Alocacao.FIND_BY_MAPA_ALOCACAO_AND_RECURSO_AND_PERFIL,
                        new Object[]{mapa.getCodigoMapaAlocacao(),
                                perfil.getCodigoPerfilVendido(),
                                recurso.getCodigoMnemonico()});

        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }

    public List<Alocacao> findVigentesByContratoPratica(Long contratoPraticaCode, Date closingDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contratoPraticaCode", contratoPraticaCode);
        params.put("closingDate", closingDate);
        List<Alocacao> allocations = getJpaTemplate().findByNamedQueryAndNamedParams(Alocacao.FIND_VIGENTES_BY_CONTRATO_PRATICA, params);
        return allocations != null ? allocations : new ArrayList<Alocacao>();
    }

}
