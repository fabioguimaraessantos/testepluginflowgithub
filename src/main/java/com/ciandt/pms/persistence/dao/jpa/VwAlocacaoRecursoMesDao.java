package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.VwAlocacaoRecursoMes;
import com.ciandt.pms.persistence.dao.IVwAlocacaoRecursoMesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.*;


/**
 * 
 * A classe VwAlocacaoRecursoMesDao proporciona as funcionalidades de acesso 
 * ao banco de dados referente a entidade VwAlocacaoRecursoMes.
 *
 * @since 09/10/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class VwAlocacaoRecursoMesDao extends AbstractDao<Long, VwAlocacaoRecursoMes> 
    implements IVwAlocacaoRecursoMesDao {

    /**
     * Construtor padrão.
     * 
     * @param factory da entidade
     */
    @Autowired
    public VwAlocacaoRecursoMesDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, VwAlocacaoRecursoMes.class);
    }

    /**
     * Busca pelo recurso e pela data de alocação (mes\ano).
     * 
     * @param codigoRecurso - codigo do recurso que se deseja buscar
     * @param dataAlocacaoPeriodo - periodo que se deseja buscar
     * 
     * @return retorna o VwAlocacaoRecursoMes com o percentual de alocacao do mes,
     * caso não exista alocacao naquele mes retorna null 
     * 
     */
    @SuppressWarnings("unchecked")
    public VwAlocacaoRecursoMes findByRecursoAndDataAlocacao(
            final Long codigoRecurso, final Date dataAlocacaoPeriodo) {
        
        List<VwAlocacaoRecursoMes> listResult = getJpaTemplate()
            .findByNamedQuery(
                VwAlocacaoRecursoMes.FIND_BY_RECURSO_AND_DATA_ALOCACAO,
                new Object[] {codigoRecurso, dataAlocacaoPeriodo });
        
        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo login e pela data de alocação (mes\ano).
     * 
     * @param username - login do recurso que se deseja buscar
     * @param dataAlocacaoPeriodo - periodo que se deseja buscar
     * 
     * @return retorna o VwAlocacaoRecursoMes com o percentual de alocacao do mes,
     * caso não exista alocacao naquele mes retorna null 
     * 
     */
    @SuppressWarnings("unchecked")
    public VwAlocacaoRecursoMes findByMnemonicoAndDataAlocacao(
            final String username, final Date dataAlocacaoPeriodo) {
        
        List<VwAlocacaoRecursoMes> listResult = getJpaTemplate()
            .findByNamedQuery(
                VwAlocacaoRecursoMes.FIND_BY_MNEMONICO_AND_DATA_ALOCACAO,
                new Object[] {username, dataAlocacaoPeriodo });
        
        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }

    @Override
    public List<VwAlocacaoRecursoMes> findByMnemonicoInAndDataAlocacao(final List<String> people, final Date month) {
        Set<String> logins = new HashSet<String>();
        List<VwAlocacaoRecursoMes> result = new ArrayList<VwAlocacaoRecursoMes>();

        for (String person : people) {
            logins.add(person);
            if (logins.size() >= 999) {
                result.addAll(this.executeFindByMnemonicoInAndDataAlocacao(logins, month));
                logins.clear();
            }
        }
        result.addAll(this.executeFindByMnemonicoInAndDataAlocacao(logins, month));

        return result;
    }

    private List<VwAlocacaoRecursoMes> executeFindByMnemonicoInAndDataAlocacao(Set<String> people, Date month) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("people", people);
        params.put("month", month);

        return getJpaTemplate().findByNamedQueryAndNamedParams(
                VwAlocacaoRecursoMes.FIND_BY_MNEMONICO_IN_AND_DATA_ALOCACAO, params);
    }

}
