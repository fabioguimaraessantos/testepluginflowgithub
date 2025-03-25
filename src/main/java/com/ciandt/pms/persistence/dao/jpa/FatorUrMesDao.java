package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FatorUrMes;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.persistence.dao.IFatorUrMesDao;


/**
 * 
 * A classe FatorUrMesDao proporciona as funcionalidades da 
 * camada de persistencia referente a entidade FatorUrMes.
 *
 * @since 09/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class FatorUrMesDao extends 
    AbstractDao<Long, FatorUrMes> implements IFatorUrMesDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica da entidade.
     * 
     */
    @Autowired
    public FatorUrMesDao(@Qualifier("entityManagerFactory") 
            final EntityManagerFactory factory) {
        
        super(factory, FatorUrMes.class);
    }
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param mapa
     *            entidade Mapaalocacao.
     * @param dataMes
     *            entidade do tipo java.util.Date.           
     * 
     * @return lista de entidades que atendem ao criterio de busca.
     */
    @SuppressWarnings("unchecked")
    public FatorUrMes findByMapaAndDate(final MapaAlocacao mapa, final Date dataMes) {
        
        List<FatorUrMes> listResult = getJpaTemplate().findByNamedQuery(
                FatorUrMes.FIND_BY_MAPA_AND_DATE, new Object[] {
                        mapa.getCodigoMapaAlocacao(), dataMes});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
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
                FatorUrMes.FIND_MAX_DATE_BY_MAPA,
                new Object[] {mapa.getCodigoMapaAlocacao()});

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
                FatorUrMes.FIND_MIN_DATE_BY_MAPA,
                new Object[] {mapa.getCodigoMapaAlocacao()});

        if (!result.isEmpty()) {
            return (Date) result.get(0);
        }

        return null;
    }
    
    /**
     * Retorna os UR mes a partir da data passada por parametro.
     * 
     * @param mapa - que se deseja obter os fatores.
     * @param startDate - data inicial
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    @SuppressWarnings("unchecked")
    public List<FatorUrMes> findByMapaAndStartDate(
            final MapaAlocacao mapa, final Date startDate) {
        
        List<FatorUrMes> result = getJpaTemplate().findByNamedQuery(
                FatorUrMes.FIND_MAPA_AND_START_DATE,
                new Object[] {mapa.getCodigoMapaAlocacao(), startDate});
        
        return result;
    }
    
    /**
     * Busca todos os fatores de um mapa.
     * 
     * @param mapa
     *            que se deseja obter os fatores.
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    @SuppressWarnings("unchecked")
    public List<FatorUrMes> findByMapa(final MapaAlocacao mapa) {
        List<FatorUrMes> result = getJpaTemplate().findByNamedQuery(
                FatorUrMes.FIND_BY_MAPA,
                new Object[] {mapa.getCodigoMapaAlocacao()});

        return result;
    }
    
    /**
     * Busca todos os fatores de um mapa por periodo.
     * 
     * @param mapa - que se deseja obter os fatores.
     * @param startDate - data inicial
     * @param endDate - data final
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    @SuppressWarnings("unchecked")
    public List<FatorUrMes> findByMapaAndPeriod(final MapaAlocacao mapa, 
            final Date startDate, final Date endDate) {
        
        List<FatorUrMes> result = getJpaTemplate().findByNamedQuery(
                FatorUrMes.FIND_BY_MAPA_AND_PERIOD,
                new Object[] {mapa.getCodigoMapaAlocacao(), startDate, endDate});

        return result;
    }
    
    /**
     * Busca os FatorUrMes, que não estão dentro 
     * do periodo do mapa de alocação passado por aprametro.
     * 
     * @param mapa
     *          MapaAlocacao que se deseja deletar as AlocacaoPeriodo.
     *                
     * @return retorna uma lista de FatorUrMes.
     */
    @SuppressWarnings("unchecked")
    public List<FatorUrMes> findByMapaAndNotInRange(final MapaAlocacao mapa) {
        
        List<FatorUrMes> result = getJpaTemplate().findByNamedQuery(
                FatorUrMes.FIND_BY_MAPA_AND_NOT_IN_RANGE,
                new Object[] {mapa.getCodigoMapaAlocacao(), 
                        mapa.getDataInicio(), mapa.getDataFim()});

        return result;
    }

}
