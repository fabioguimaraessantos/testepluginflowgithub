package com.ciandt.pms.persistence.dao.jpa;

import java.util.*;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IAlocacaoPeriodoDao;


/**
 * 
 * A classe AlocacaoPeriodoDao proporciona as funcionalidades de acesso ao banco
 * de dadso para a entidade AlocacaoPeriodo.
 * 
 * @since 18/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class AlocacaoPeriodoDao extends AbstractDao<Long, AlocacaoPeriodo>
        implements IAlocacaoPeriodoDao {

    /**
     * Contrutor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public AlocacaoPeriodoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, AlocacaoPeriodo.class);
    }

    /**
     * Busca a maior data referente ao mapa de alocacao.
     * 
     * @param mapa
     *            que se deseja obter a maior data de alocação
     * 
     * @return retorna a marior data
     */
    @SuppressWarnings("unchecked")
    public Date findMaxDateByMapa(final MapaAlocacao mapa) {

        List result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_MAX_DATE_BY_MAPA,
                new Object[] {mapa.getCodigoMapaAlocacao() });

        if (!result.isEmpty()) {
            return (Date) result.get(0);
        }

        return null;
    }

    /**
     * Busca a menor data referente ao mapa de alocacao.
     * 
     * @param mapa
     *            que se deseja obter a menor data de alocação
     * 
     * @return retorna a menor data
     */
    @SuppressWarnings("unchecked")
    public Date findMinDateByMapa(final MapaAlocacao mapa) {
        List result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_MIN_DATE_BY_MAPA,
                new Object[] {mapa.getCodigoMapaAlocacao() });

        if (!result.isEmpty()) {
            return (Date) result.get(0);
        }

        return null;
    }

    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param alocacao
     *            Alocacao utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna a AlocacaoPeriodo.
     */
    @SuppressWarnings("unchecked")
    public AlocacaoPeriodo findByAlocacaoAndDate(final Alocacao alocacao,
            final Date dataMes) {
        List result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_BY_ALOCACAO_AND_DATE,
                new Object[] {alocacao.getCodigoAlocacao(), dataMes });

        if (!result.isEmpty()) {
            return (AlocacaoPeriodo) result.get(0);
        }

        return null;
    }

    /**
     * Busca pelo Recurso e Data Mês.
     * 
     * @param recurso
     *            Recurso utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodo> findByRecursoAndDate(final Recurso recurso,
            final Date dataMes) {
        List<AlocacaoPeriodo> result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_BY_RECURSO_AND_DATE,
                new Object[] {recurso.getCodigoRecurso(), dataMes });

        return result;
    }

    /**
     * Busca pelo Recurso e Data Mês.
     *
     * @param recurso
     *            Recurso utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     *
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodo> findAlocacaoPeriodoGreaterZeroByContratoPraticaAndDateAndRecurso(final Long codigoContratoPratica, final Long codigoRecurso, final Date dataMes){
        List<AlocacaoPeriodo> result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_BY_CONTRATO_PRATICA_RECURSO_AND_DATE,
                new Object[] {codigoContratoPratica, codigoRecurso, dataMes });

        return result;
    }


    /**
     * Busca as alocações periodos, que não estão dentro do periodo passado por
     * aprametro.
     * 
     * @param mapa
     *            MapaAlocacao que se deseja deletar as AlocacaoPeriodo.
     * @param startDate
     *            Data referente ao inicio do periodo.
     * @param endDate
     *            Data referente ao fim do periodo.
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodo> findByMapaAndNotInRange(
            final MapaAlocacao mapa, final Date startDate, final Date endDate) {
        List<AlocacaoPeriodo> result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_BY_MAPA_AND_NOT_IN_RANGE,
                new Object[] {mapa.getCodigoMapaAlocacao(),
                        mapa.getCodigoMapaAlocacao(), startDate, endDate });

        return result;
    }

    /**
     * Busca as alocações periodos de uma alocacao.
     * 
     * @param alocacao
     *            - entidade do tipo Alocacao.
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodo> findByAlocacao(final Alocacao alocacao) {

        List<AlocacaoPeriodo> result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_BY_ALOCACAO,
                new Object[] {alocacao.getCodigoAlocacao() });

        return result;
    }

    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param recurso
     *            Recurso utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodo> findByMaxDateAndRecurso(final Recurso recurso,
            final Date dataMes) {
        List result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_BY_MAX_DATE_AND_RECURSO,
                new Object[] {recurso.getCodigoRecurso(),
                        recurso.getCodigoRecurso(), dataMes });

        return result;
    }

    
    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param alocacao
     *            Alucacao utilizado na busca.
     * @param startDate
     *            Data inicio do periodo.
     * @param endDate
     *            Data fim do periodo.
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodo> findByAlocacaoAndPeriod(
            final Alocacao alocacao, final Date startDate, final Date endDate) {
        
        List result = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodo.FIND_BY_ALOCACAO_AND_PERIOD,
                new Object[] {alocacao.getCodigoAlocacao(), startDate, endDate});

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
	public List<AlocacaoPeriodo> findByMapaAlocacaoAndRecurso(
			final MapaAlocacao mapaAlocacao, final Recurso recurso) {
    	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoMapaAlocacao", mapaAlocacao.getCodigoMapaAlocacao());
		params.put("codigoRecurso", recurso.getCodigoRecurso());

		List<AlocacaoPeriodo> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						AlocacaoPeriodo.FIND_BY_MAPA_ALOCACAO_AND_RECURSO,
						params);

		return results;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodo> findByPerfilVendidoAndClosingDate (
      final PerfilVendido perfilVendido, final Date closingMapDate) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoPerfilVendido", perfilVendido.getCodigoPerfilVendido());
        params.put("closingMapDate", closingMapDate);

        List<AlocacaoPeriodo> results = getJpaTemplate()
          .findByNamedQueryAndNamedParams(
            AlocacaoPeriodo.FIND_BY_PERFIL_VENDIDO_AND_CLOSING_DATE,
            params);

        return results;
    }

    @Override
    public List<AlocacaoPeriodo> findByResourceCodeInAndMonth(final Set<Long> resourceCodes, final Date month) {
        List<Long> codes = new ArrayList<Long>();
        List<AlocacaoPeriodo> allocationPeriods = new ArrayList<AlocacaoPeriodo>();

        for (Long code : resourceCodes) {
            codes.add(code);
            if (codes.size() >= 999) {
                allocationPeriods.addAll(this.executeFindByResourceCodeInAndMonth(codes, month));
                codes.clear();
            }
        }
        allocationPeriods.addAll(this.executeFindByResourceCodeInAndMonth(codes, month));

        return allocationPeriods;
    }

    private List<AlocacaoPeriodo> executeFindByResourceCodeInAndMonth(List<Long> resourceCodes, Date month) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("resourceCodes", resourceCodes);
        params.put("month", month);

        return getJpaTemplate().findByNamedQueryAndNamedParams(AlocacaoPeriodo.FIND_BY_RECURSO_IN_AND_DATE, params);
    }

}