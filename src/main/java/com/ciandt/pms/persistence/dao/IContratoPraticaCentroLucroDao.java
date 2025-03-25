package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.NaturezaCentroLucro;


/**
 * 
 * A classe IContratoPraticaCentroLucroDao proporciona a interface de acesso ao
 * banco de dados referente a entidade ContratoPraticaCentroLucro.
 * 
 * @since 18/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IContratoPraticaCentroLucroDao extends
        IAbstractDao<Long, ContratoPraticaCentroLucro> {

    /**
     * Busca pelo ContratoPratica.
     * 
     * @param cp
     *            - ContratoPratica utilizado na busca.
     * @return lista com todas as entidades ContratoPraticaCentroLucro
     *         relacionadas com o ContratoPratica
     */
    List<ContratoPraticaCentroLucro> findByContratoPratica(ContratoPratica cp);

    /**
     * Busca pelo ContratoPratica e NaturezaCentroLucro opcionais (diferente de
     * obrigatorias).
     * 
     * @param cp
     *            - ContratoPratica utilizado na busca.
     * @return lista com todas as entidades ContratoPraticaCentroLucro
     *         relacionadas com o ContratoPratica
     */
    List<ContratoPraticaCentroLucro> findByContratoPraticaAndNaturezaOptional(
            final ContratoPratica cp);

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param contratoPratica
     *            entidade populada com os valores do contrato pratica.
     * 
     * @param natureza
     *            utilizado na busca.
     * 
     * @return lista de entidades que atendem ao criterio do contrato pratica.
     */
    ContratoPraticaCentroLucro findMaxStartDateByContratoPraticaAndNatureza(
            final ContratoPratica contratoPratica,
            final NaturezaCentroLucro natureza);

    /**
     * Busca pelo ContratoPratica e pela Natureza.
     * 
     * @param cp
     *            - ContratoPratica utilizado na busca.
     * 
     * @param natureza
     *            - NaturezaCentroLucro utilizado na busca.
     * 
     * @return lista com todas as entidades ContratoPraticaCentroLucro
     *         relacionadas com o ContratoPratica e Natureza
     */
    List<ContratoPraticaCentroLucro> findByContratoPraticaAndNatureza(
            final ContratoPratica cp, final NaturezaCentroLucro natureza);

    /**
     * Busca pelo ContratoPratica e pela Natureza e pela Data de vigencia.
     * 
     * @param cp
     *            - ContratoPratica utilizado na busca.
     * 
     * @param natureza
     *            - NaturezaCentroLucro utilizado na busca.
     * 
     * @param dataMes
     *            - data da vigencia.
     * 
     * @return a entidade que atende aos criterios do filtro
     */
    ContratoPraticaCentroLucro findByContPratAndNatAndDate(
            final ContratoPratica cp, final NaturezaCentroLucro natureza,
            final Date dataMes);

}