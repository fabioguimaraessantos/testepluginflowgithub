package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.VwReceitaFilter;


/**
 * 
 * A classe IVwReceitaFilterDao proporciona a interface referente a view do
 * banco de dados VwReceitaFilter.
 * 
 * @since 28/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IVwReceitaFilterDao extends
        IAbstractDao<Long, VwReceitaFilter> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param natureza
     *            entidade NaturezaCentroLucro.
     * @param cl
     *            - entidade CentroLucro.
     * @param p
     *            - entidade Pratica.
     * @param cli
     *            - entidade Cliente.
     * @param status
     *            - valor do Status.
     * @param dataMes
     *            - valor da data (mes/ano)
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<VwReceitaFilter> findByFilter(final NaturezaCentroLucro natureza,
            final CentroLucro cl, final Pratica p, final Cliente cli,
            final String status, final Date dataMes);

}
