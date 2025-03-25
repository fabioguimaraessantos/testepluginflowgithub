package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ComposicaoTce;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IComposicaoTceDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade ComposicaoTce.
 * 
 * @since 07/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public interface IComposicaoTceDao extends IAbstractDao<Long, ComposicaoTce> {
    
    /**
     * Remove as entidades pela data e tipo.
     * 
     * @param dataMes
     *            - data da vigencia.
     * @param indicadorTipo
     *            - tipo MN ou SY.
     * 
     * @return true se a remoção ocorrou corretamente. False caso contrário.
     */
    Boolean removeByDateAndType(final Date dataMes, final String indicadorTipo);

    /**
     * Busca pela Pessoa e pela Data.
     * 
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     * 
     * @return a entidade que atende aos criterios do filtro
     */
    ComposicaoTce findByPessoaAndDate(final Pessoa pessoa, final Date dataMes);

    /**
     * Busca o último registro ComposicaoTce da Pessoa (maior data).
     * 
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     * 
     * @return a entidade que atende aos criterios do filtro
     */
    ComposicaoTce findByPessoaAndMaxDate(final Pessoa pessoa, final Date dataMes);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ComposicaoTce> findByFilter(final ComposicaoTce filter);

    /**
     * Busca uma lista de entidades pelo filtro e com algum valor nulo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ComposicaoTce> findByFilterMissBlank(final ComposicaoTce filter);

}