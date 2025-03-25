package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.VwDealHrsFatFilter;


/**
 * 
 * A classe IVwDealHrsFatFilterDao proporciona a interface referente a view 
 * do banco de dados VwDealHrsFatFilter.
 *
 * @since 22/10/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IVwDealHrsFatFilterDao extends IAbstractDao<Long, VwDealHrsFatFilter> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param natureza
     *            entidade NaturezaCentroLucro.
     * @param cl - entidade CentroLucro.
     * @param p - entidade Pratica.
     * @param cli - entidade Cliente.
     * @param status - valor do Status.
     * @param dataMes - valor da data (mes/ano)
     *      
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<VwDealHrsFatFilter> findByFilter(final NaturezaCentroLucro natureza, final CentroLucro cl, 
            final Pratica p, final Cliente cli, final String status, final Date dataMes);

}
