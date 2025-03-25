package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ciandt.pms.model.ChargebackPessoa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TiRecurso;


/**
 * 
 * A classe IChargebackPessDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade ChargebackPessoa.
 * 
 * @since 01/07/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IChargebackPessDao extends
        IAbstractDao<Long, ChargebackPessoa> {

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
     * Realiza uma busca por ChargebackPessoa referentes a um TiRecurso em um
     * determinado periodo, passados por parametro.
     * 
     * @param tiRecurso
     *            do tipo TiRecurso.
     * @param startDate
     *            data inicio do periodo.
     * @param endDate
     *            data fim do periodo.
     * 
     * @return lista de ChargebackPessoa que atendem ao criterio de filtro
     */
    List<ChargebackPessoa> findByTiRecursoAndPeriod(final TiRecurso tiRecurso,
            final Date startDate, final Date endDate);

    /**
     * Realiza uma busca por ChargebackPessoa referentes a uma Pessoa em um
     * determinado periodo, passados por parametro.
     * 
     * @param pessoa
     *            do tipo Pessoa.
     * @param startDate
     *            data inicio do periodo.
     * @param endDate
     *            data fim do periodo.
     * 
     * @return lista de ChargebackPessoa que atendem ao criterio de filtro
     */
    List<ChargebackPessoa> findByPessoaAndPeriod(final Pessoa pessoa,
            final Date startDate, final Date endDate);

    /**
     * Realiza uma busca por ChargebackPessoa por Pessoa, TiRecurso e Data.
     * Estes tres compoem uma cahva unica.
     * 
     * @param tiRecurso
     *            do tipo TiRecurso.
     * 
     * @param pessoa
     *            do tipo Pessoa.
     * 
     * @param monthDate
     *            data mes.
     * 
     * @return lista de ChargebackPessoa que atendem ao criterio de filtro
     */
    ChargebackPessoa findByUniqueKey(final TiRecurso tiRecurso,
            final Pessoa pessoa, final Date monthDate);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param multCodTiRecMap
     *            multiplos codigos de TiRecurso.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ChargebackPessoa> findByFilter(final ChargebackPessoa filter,
            final Map<Long, String> multCodTiRecMap);

    /**
     * Busca uma lista de entidades pelo filtro e com algum valor nulo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ChargebackPessoa> findByFilterMissBlank(final ChargebackPessoa filter);

    /**
     * Busca uma lista de entidades pelo filtro de mes.
     * 
     * @param dataMes
     *            - mes de filtro
     * @return lista de entidades
     */
    List<Long> findByMonth(final Date dataMes);

    ChargebackPessoa findByPessoaAndTiRecursoAndEndDateBefore(Pessoa pessoa, TiRecurso tiRecurso, Date dataMes);
}