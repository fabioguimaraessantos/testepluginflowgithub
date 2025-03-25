package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.persistence.dao.IContratoPraticaCentroLucroDao;


/**
 * 
 * A classe ContratoPraticaCentroLucroDao proporciona as funcionalidades de
 * acesso ao banco de dados referente a entidade ContratoPraticaCentroLucro.
 * 
 * @since 18/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class ContratoPraticaCentroLucroDao extends
        AbstractDao<Long, ContratoPraticaCentroLucro> implements
        IContratoPraticaCentroLucroDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            da entidade
     * 
     */
    public ContratoPraticaCentroLucroDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, ContratoPraticaCentroLucro.class);
    }

    /**
     * Busca pelo ContratoPratica.
     * 
     * @param cp
     *            - ContratoPratica utilizado na busca.
     * @return lista com todas as entidades ContratoPraticaCentroLucro
     *         relacionadas com o ContratoPratica
     */
    @SuppressWarnings("unchecked")
    public List<ContratoPraticaCentroLucro> findByContratoPratica(
            final ContratoPratica cp) {

        List<ContratoPraticaCentroLucro> listResult = getJpaTemplate()
                .findByNamedQuery(
                        ContratoPraticaCentroLucro.FIND_BY_CONTRATO_PRATICA,
                        new Object[] {cp.getCodigoContratoPratica() });

        return listResult;
    }

    /**
     * Busca pelo ContratoPratica e NaturezaCentroLucro opcionais (diferente de
     * obrigatorias).
     * 
     * @param cp
     *            - ContratoPratica utilizado na busca.
     * @return lista com todas as entidades ContratoPraticaCentroLucro
     *         relacionadas com o ContratoPratica
     */
    @SuppressWarnings("unchecked")
    public List<ContratoPraticaCentroLucro> findByContratoPraticaAndNaturezaOptional(
            final ContratoPratica cp) {

        List<ContratoPraticaCentroLucro> listResult = getJpaTemplate()
                .findByNamedQuery(
                        ContratoPraticaCentroLucro.FIND_BY_CONTRATO_PRATICA_AND_NATUREZA_OPTIONAL,
                        new Object[] {cp.getCodigoContratoPratica() });

        return listResult;
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param contratoPratica
     *            entidade populada com os valores do contrato pratica.
     * 
     * @param natureza
     *            utilizada na busca.
     * 
     * @return lista de entidades que atendem ao criterio do contrato pratica.
     */
    @SuppressWarnings("unchecked")
    public ContratoPraticaCentroLucro findMaxStartDateByContratoPraticaAndNatureza(
            final ContratoPratica contratoPratica,
            final NaturezaCentroLucro natureza) {

        List<ContratoPraticaCentroLucro> listResult = getJpaTemplate()
                .findByNamedQuery(
                        ContratoPraticaCentroLucro.FIND_MAX_START_DATE_BY_CONTRATO_PRATICA_AND_NATUREZA,
                        new Object[] {
                                contratoPratica.getCodigoContratoPratica(),
                                natureza.getCodigoNatureza(),
                                contratoPratica.getCodigoContratoPratica(),
                                natureza.getCodigoNatureza() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

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
    @SuppressWarnings("unchecked")
    public List<ContratoPraticaCentroLucro> findByContratoPraticaAndNatureza(
            final ContratoPratica cp, final NaturezaCentroLucro natureza) {

        List<ContratoPraticaCentroLucro> listResult = getJpaTemplate()
                .findByNamedQuery(
                        ContratoPraticaCentroLucro.FIND_BY_CONTRATO_PRATICA_AND_NATUREZA,
                        new Object[] {cp.getCodigoContratoPratica(),
                                natureza.getCodigoNatureza() });

        return listResult;
    }
    
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
    @SuppressWarnings("unchecked")
    public ContratoPraticaCentroLucro findByContPratAndNatAndDate(
            final ContratoPratica cp, final NaturezaCentroLucro natureza, final Date dataMes) {

        List<ContratoPraticaCentroLucro> listResult = getJpaTemplate()
                .findByNamedQuery(
                        ContratoPraticaCentroLucro.FIND_BY_CONT_PRAT_AND_NAT_AND_DATE,
                        new Object[] {cp.getCodigoContratoPratica(),
                                natureza.getCodigoNatureza(), dataMes, dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}