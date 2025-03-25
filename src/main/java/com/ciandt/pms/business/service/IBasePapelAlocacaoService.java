package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.model.Moeda;


/**
 * Define a interface do Service da entidade.
 * 
 * @author cmantovani
 * @since 13/07/2011
 */
@Service
public interface IBasePapelAlocacaoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createBasePapelAlocacao(final BasePapelAlocacao entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     */
    @Transactional
    void updateBasePapelAlocacao(final BasePapelAlocacao entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeBasePapelAlocacao(final BasePapelAlocacao entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    BasePapelAlocacao findBasePapelAlocacaoById(final Long id);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<BasePapelAlocacao> findBasePapelAlocacaoByFilter(
            final BasePapelAlocacao filter);

    /**
     * Busca uma lista de entidades pelo filtro - Fetch.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<BasePapelAlocacao> findBasePapelAlocacaoByFilterFetch(
            final BasePapelAlocacao filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<BasePapelAlocacao> findBasePapelAlocacaoAll();

    /**
     * Busca uma entidade pela entidade.
     * 
     * @param basePapelAlocacao
     *            - entidade populada .
     * 
     * @return entidade buscada.
     */
    BasePapelAlocacao findBasePapelAlocacaoByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao);

    /**
     * Recupera a moeda corrente.
     * 
     * @param moeda
     *            - Moeda corrente
     * @return o pattern da Moeda.
     */
    String getCurrency(final Moeda moeda);

}