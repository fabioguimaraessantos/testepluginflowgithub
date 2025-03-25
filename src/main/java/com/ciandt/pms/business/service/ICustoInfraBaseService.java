package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CustoInfraBase;
import com.ciandt.pms.model.Moeda;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 31/05/2010
 */
@Service
public interface ICustoInfraBaseService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createCustoInfraBase(final CustoInfraBase entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     */
    @Transactional
    void updateCustoInfraBase(final CustoInfraBase entity);

    /**
     * Executa um update na lista de entidade passado por parametro.
     * 
     * @param entityList
     *            - lista de entidades que serao atualizadas.
     * 
     */
    @Transactional
    void updateCustoInfraBaseList(final List<CustoInfraBase> entityList);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    @Transactional
    void removeCustoInfraBase(final CustoInfraBase entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CustoInfraBase findCustoInfraBaseById(final Long id);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<CustoInfraBase> findCustoInfraBaseByFilter(final CustoInfraBase filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoInfraBase> findCustoInfraBaseAll();

    /**
     * Recuprea a moeda corrente.
     * 
     * @param moeda
     *            - Moeda corrente
     * @return o pattern da Moeda.
     */
    String getCurrency(final Moeda moeda);

    /**
     * Busca a entidade filtrando pelos parametros.
     * 
     * @param dataMes
     *            do CustoInfraBase.
     * @param cidadeBase
     *            entidade populada com os valores da CidadeBase.
     * 
     * @return lista de entidades que atendem ao criterio da CidadeBase e
     *         dataMes.
     */
    CustoInfraBase findCustoInfBaseByDateAndCidadeBase(final Date dataMes,
            final CidadeBase cidadeBase);

}