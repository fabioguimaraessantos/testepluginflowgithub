package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.VwAlocContratoRecursoMes;
import com.ciandt.pms.persistence.dao.IVwAlocContratoRecursoMesDao;


/**
 * 
 * A classe VwAlocContratoRecursoMesDao proporciona as funcionalidades da
 * camada de persistencia referente a entidade VwAlocContratoRecursoMes.
 *
 * @since 14/09/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class VwAlocContratoRecursoMesDao 
    extends AbstractDao<Long, VwAlocContratoRecursoMes> implements IVwAlocContratoRecursoMesDao {

    /**
     * Construtor default.
     * 
     * @param factory - fabrica do entity manager.
     */
    @Autowired
    public VwAlocContratoRecursoMesDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, VwAlocContratoRecursoMes.class);
    }
    
    /**
     * Busca pelo mapa de pelo periodo inicio e fim.
     * 
     * @param mapaAlocacao - entidade MapaAlocacao
     * @param startDate - data inicio
     * @param endDate - data fim
     * 
     * @return retorna uma lista de VwAlocContratoRecursoMes
     */
    @SuppressWarnings("unchecked")
    public List<VwAlocContratoRecursoMes> findByMapaAndPeriod(
            final MapaAlocacao mapaAlocacao, final Date startDate, final Date endDate) {
        
        List<VwAlocContratoRecursoMes> listResult = getJpaTemplate()
            .findByNamedQuery(VwAlocContratoRecursoMes.FIND_BY_MAPA_AND_PERIOD,
                new Object[] {mapaAlocacao.getCodigoMapaAlocacao(), startDate, endDate});

        return listResult;
        
        
    }
}
