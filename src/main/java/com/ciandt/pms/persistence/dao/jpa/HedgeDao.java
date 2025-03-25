package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.HedgeMoedaMes;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.IHedgeDao;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe HedgeDao proporciona as funcionalidades de persistencia
 * referente a entidade HedgeMoedaMes.
 *
 * @since 24/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class HedgeDao extends AbstractDao<Long, HedgeMoedaMes> implements
        IHedgeDao {

    /**
     * Construtor Padrão.
     * 
     * @param factory - Fabrica do entity manager
     */
    @Autowired
    public HedgeDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, HedgeMoedaMes.class);
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
    public List<HedgeMoedaMes> findByFilter(final HedgeMoedaMes filter) {
        
        Long codigoMoeda = Long.valueOf(0L);
        if (filter.getMoeda() != null) {
            codigoMoeda = filter.getMoeda().getCodigoMoeda();
        }
        
        Date dateMonth = DateUtil.getDate("1", "1900"); 
        if (filter.getDataMes() != null) {
            dateMonth = filter.getDataMes();
        }
        
        List<HedgeMoedaMes> listResult = getJpaTemplate()
                .findByNamedQuery(HedgeMoedaMes.FIND_BY_FILTER,
                        new Object[] {dateMonth, codigoMoeda});
        
        return listResult;
    }
    
    /**
     * Busca a entidade pela chave unica.
     * 
     * @param monthDate
     *            entidade populada com os valores do filtro.
     *            
     * @param currency intancia da moeda           
     *            
     * 
     * @return entidade referente a chave unica
     */
    @SuppressWarnings("unchecked")
    public HedgeMoedaMes findUnique(
            final Date monthDate, final Moeda currency) {
       
        List<HedgeMoedaMes> listResult = getJpaTemplate()
                .findByNamedQuery(HedgeMoedaMes.FIND_UNIQUE,
                        new Object[] {monthDate, currency.getCodigoMoeda()});
        
        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    
}
