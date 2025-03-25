package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DespesaMes;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.TipoDespesa;
import com.ciandt.pms.persistence.dao.IDespesaMesDao;


/**
 * 
 * A classe DespesaDao proporciona as funcionalidades 
 * da camada de persistencia referente a entidade Despesa.
 *
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class DespesaMesDao extends AbstractDao<Long, DespesaMes> implements IDespesaMesDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica da entidade
     * 
     */
    public DespesaMesDao(@Qualifier("entityManagerFactory") 
            final EntityManagerFactory factory) {
        super(factory, DespesaMes.class);
    }
    
    /**
     * Busca uma lista de entidades pelo contrato-pratica
     * e tipo de despesa.
     * 
     * @param cp entidade do tipo ContratoPratica.
     * 
     * @param tipo entidade do tipo TipoDespesa.
     * 
     * @return lista de entidades do tipo Despesa
     */
    @SuppressWarnings("unchecked")
    public List<DespesaMes> findByContratoPraticaAndTipo(
            final ContratoPratica cp, final TipoDespesa tipo) {

        List<DespesaMes> listResult = getJpaTemplate().findByNamedQuery(
                DespesaMes.FIND_BY_CONTRATO_PRATICA_AND_TIPO,
                new Object[] {cp.getCodigoContratoPratica(),
                       tipo.getCodigoTipoDespesa() });
        
        return listResult;
    }
    
    /**
     * Busca uma lista de entidades pelo contrato-pratica
     * e tipo de despesa.
     * 
     * @param mapDesp entidade do tipo MapaDespesa.
     * 
     * @param tipo entidade do tipo TipoDespesa.
     * 
     * @return lista de entidades do tipo Despesa
     */
    @SuppressWarnings("unchecked")
    public List<DespesaMes> findByMapaDespesaAndTipo(
            final MapaDespesa mapDesp, final TipoDespesa tipo) {

        List<DespesaMes> listResult = getJpaTemplate().findByNamedQuery(
                DespesaMes.FIND_BY_MAPA_DESPESA_AND_TIPO,
                new Object[] {mapDesp.getCodigoMapaDespesa(),
                       tipo.getCodigoTipoDespesa() });
        
        return listResult;
    }
    
    /**
     * Busca a maior data referente ao mapa de despesa.
     * 
     * @param mapa
     *            que se deseja obter a maior data da despesa
     * 
     * @return retorna a marior data
     */
    @SuppressWarnings("unchecked")
    public Date findMaxDateByMapa(final MapaDespesa mapa) {

        List result = getJpaTemplate().findByNamedQuery(
                DespesaMes.FIND_MAX_DATE_BY_MAPA,
                new Object[] {mapa.getCodigoMapaDespesa()});

        if (!result.isEmpty()) {
            return (Date) result.get(0);
        }

        return null;
    }

    /**
     * Busca a menor data referente ao mapa de despesa.
     * 
     * @param mapa
     *            que se deseja obter a menor data da despesa
     * 
     * @return retorna a menor data
     */
    @SuppressWarnings("unchecked")
    public Date findMinDateByMapa(final MapaDespesa mapa) {
        List result = getJpaTemplate().findByNamedQuery(
                DespesaMes.FIND_MIN_DATE_BY_MAPA,
                new Object[] {mapa.getCodigoMapaDespesa()});

        if (!result.isEmpty()) {
            return (Date) result.get(0);
        }

        return null;
    }

    /**
     * Busca uma despesa de debito pelo mapa, tipo e mes.
     * 
     * @param mapa que se deseja obter a menor data da despesa
     * @param tipoDesp entidade TipoDespesa
     * @param dataMes data do tipo Date.
     * 
     * @return retorna a menor data
     */
    @SuppressWarnings("unchecked")
    public DespesaMes findDebitoByMapaTipoAndDate(final MapaDespesa mapa, 
            final TipoDespesa tipoDesp, final Date dataMes) {
        
        List<DespesaMes> result = getJpaTemplate().findByNamedQuery(
                DespesaMes.FIND_DEBITO_BY_MAPA_TIPO_DATE,
                new Object[] {mapa.getCodigoMapaDespesa(), 
                        tipoDesp.getCodigoTipoDespesa(), dataMes});

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }
    
    /**
     * Busca as despesas, que não estão dentro 
     * do periodo passado por aprametro.
     * 
     * @param mapa
     *          entidade do tipo MapaDespesa.
     * @param startDate
     *          Data referente ao inicio do periodo.
     * @param endDate
     *          Data referente ao fim do periodo.
     *                
     * @return retorna uma lista de DespesaMes.
     */
    @SuppressWarnings("unchecked")
    public List<DespesaMes> findByMapaAndNotInRange(
            final MapaDespesa mapa, final Date startDate, final Date endDate) {
        
        List<DespesaMes> result = getJpaTemplate().findByNamedQuery(
                DespesaMes.FIND_BY_MAPA_AND_NOT_IN_RANGE,
                new Object[] {mapa.getCodigoMapaDespesa(), 
                        startDate, endDate});
        
        return result;
    }
    
    /**
     * Busca uma despesa de pelo mapa, tipo e mes.
     * 
     * @param mapa que se deseja obter a menor data da despesa
     * @param tipoDesp entidade TipoDespesa
     * @param dataMes data do tipo Date.
     * 
     * @return retorna a lista de DespesaMes
     */
    @SuppressWarnings("unchecked")
    public List<DespesaMes> findByMapaTipoAndDate(final MapaDespesa mapa, 
            final TipoDespesa tipoDesp, final Date dataMes) {
        
        List<DespesaMes> result = getJpaTemplate().findByNamedQuery(
                DespesaMes.FIND_BY_MAPA_TIPO_AND_DATE,
                new Object[] {mapa.getCodigoMapaDespesa(), 
                        tipoDesp.getCodigoTipoDespesa(), dataMes});
        
        return result;
    }
    
}
