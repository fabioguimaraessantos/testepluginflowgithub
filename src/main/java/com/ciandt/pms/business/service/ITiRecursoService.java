package com.ciandt.pms.business.service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CustoTiRecurso;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TiRecurso;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 
 * A classe ITiRecursoService proporciona a interface de acesso a camada de
 * serviço referente a entidade TiRecurso.
 * 
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface ITiRecursoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createTiRecurso(final TiRecurso entity);

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @param custoTiRecurso
     *            entidade do Tipo CustoTiRecurso a ser relacionada com entidade
     */
    @Transactional
    void createTiRecurso(final TiRecurso entity,
            final CustoTiRecurso custoTiRecurso);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateTiRecurso(final TiRecurso entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    @Transactional
    void removeTiRecurso(final TiRecurso entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    TiRecurso findTiRecursoById(final Long id);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<TiRecurso> findTiRecursoByFilter(final TiRecurso filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TiRecurso> findTiRecursoAllActive();

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
    List<TiRecurso> findTiRecursoByContractAndPeriod(final ContratoPratica cp,
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
    List<TiRecurso> findTiRecursoByPessoaAndPeriod(final Pessoa pessoa,
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
    List<TiRecurso> findTiRecursoByPessoaAndPeriodAndType(final Pessoa pessoa,
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
    List<TiRecurso> findTiRecursoByType(final String type);

    TiRecurso findByNomeTiRecurso(final String nomeTiRecurso);
}
