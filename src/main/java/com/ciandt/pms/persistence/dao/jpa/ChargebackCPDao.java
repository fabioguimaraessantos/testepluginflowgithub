package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ChargebackContratoPratica;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.persistence.dao.IChargebackCPDao;


/**
 * 
 * A classe ChargebackCPDao proporciona as funcionalidades de persistencia
 * referente a entidade ChargebackContratoPratica.
 *
 * @since 16/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class ChargebackCPDao extends
        AbstractDao<Long, ChargebackContratoPratica> implements
        IChargebackCPDao {

    /**
     * Construtor Padrão.
     * 
     * @param factory - Instancia da fabrica da entidade.
     */
    @Autowired
    public ChargebackCPDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, ChargebackContratoPratica.class);
    }

    /**
     * Realiza uma busca por ChargebackContratoPratica referentes 
     * a um recurso em um determinado periodo, passados por parametro.
     * 
     * @param recurso do tipo TiRecurso.
     * @param startDate data inicio do periodo.
     * @param endDate data fim do periodo.
     * 
     * @return lista de CentroLucro que atendem ao criterio de filtro
     */
    @SuppressWarnings("unchecked")
    public List<ChargebackContratoPratica> findByRecursoAndPeriod(
            final TiRecurso recurso, final Date startDate, final Date endDate) {
        
        List<ChargebackContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
                ChargebackContratoPratica.FIND_BY_TIRECURSO_AND_PERIOD, 
                new Object[] {recurso.getCodigoTiRecurso(), startDate, endDate});
        
        return listResult;
    }
    
    /**
     * Realiza uma busca por ChargebackContratoPratica referentes 
     * a um contrato Pratica em um determinado periodo, passados por parametro.
     * 
     * @param cp do tipo ContratoPratica.
     * @param startDate data inicio do periodo.
     * @param endDate data fim do periodo.
     * 
     * @return lista de CentroLucro que atendem ao criterio de filtro
     */
    @SuppressWarnings("unchecked")
    public List<ChargebackContratoPratica> findByContractAndPeriod(
            final ContratoPratica cp, final Date startDate, final Date endDate) {
        
        List<ChargebackContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
                ChargebackContratoPratica.FIND_BY_CONTRACT_AND_PERIOD, 
                new Object[] {cp.getCodigoContratoPratica(), startDate, endDate});
        
        return listResult;
    }
    
    /**
     * Realiza uma busca por ChargebackContratoPratica por
     * ContratoPratica, TiRecurso e Data. Estes tres compoem uma cahva unica.
     * 
     * @param recurso do tipo TiRecurso.
     * 
     * @param cp do tipo ContratoPratica.
     * 
     * @param monthDate data mes.
     * 
     * @return lista de CentroLucro que atendem ao criterio de filtro
     */
    @SuppressWarnings("unchecked")
    public ChargebackContratoPratica findByUniqueKey(final TiRecurso recurso,
            final ContratoPratica cp, final Date monthDate) {
        
        List<ChargebackContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
                ChargebackContratoPratica.FIND_BY_UNIQUE_KEY, 
                new Object[] {cp.getCodigoContratoPratica(), recurso.getCodigoTiRecurso(), monthDate});
        
        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
}
