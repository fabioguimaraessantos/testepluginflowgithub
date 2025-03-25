package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.VwAlocacaoRecursoMes;

import java.util.Date;
import java.util.List;


/**
 * 
 * A classe IVwAlocacaoRecursoMesDao proporciona as funcionalidades interface
 * de acesso ai banco de dados referente a entidade VwAlocacaoRecursoMes.
 *
 * @since 08/10/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IVwAlocacaoRecursoMesDao extends IAbstractDao<Long, VwAlocacaoRecursoMes> {

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
    VwAlocacaoRecursoMes findByRecursoAndDataAlocacao(Long codigoRecurso, Date dataAlocacaoPeriodo);
  
    
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
    VwAlocacaoRecursoMes findByMnemonicoAndDataAlocacao(
            final String username, final Date dataAlocacaoPeriodo);

    List<VwAlocacaoRecursoMes> findByMnemonicoInAndDataAlocacao(final List<String> people, final Date month);
}
