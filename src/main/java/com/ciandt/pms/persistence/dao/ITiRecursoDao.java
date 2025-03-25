package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TiRecurso;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * 
 * A classe ITiRecursoDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade TiRecurso.
 * 
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public interface ITiRecursoDao extends IAbstractDao<Long, TiRecurso> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<TiRecurso> findByFilter(final TiRecurso filter);

    /**
     * Retorna todas as entidades com o estado igual a ativo.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TiRecurso> findAllActive();

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param cp
     *            - ContratoPratica que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TiRecurso> findByContractAndPeriod(final ContratoPratica cp,
            final Date startDate, final Date endDate);

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param pessoa
     *            - Pessoa que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TiRecurso> findByPessoaAndPeriod(final Pessoa pessoa,
            final Date startDate, final Date endDate);

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param pessoa
     *            - Pessoa que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @param tipo
     *            - tipo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TiRecurso> findByPessoaAndPeriodAndType(final Pessoa pessoa,
            final Date startDate, final Date endDate, final String tipo);

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param type
     *            - Tipo de TiRecurso (CS-Contract Server, US-User Service)
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TiRecurso> findByType(final String type);

    TiRecurso findByNomeTiRecurso(String nomeTiRecurso);
}