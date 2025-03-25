package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;


/**
 * 
 * A classe IMapaDespesaDao proporciona a interface de acesso
 * a camada de persistencia referente a entidade MapaDespesa.
 *
 * @since 28/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IMapaDespesaDao extends IAbstractDao<Long, MapaDespesa> {

    /**
     * Busca um mapa de despesa pelo contrato pratica. 
     * 
     * @param cp - Entidade do tipo ContratoPratica, utilizado na busca.
     * 
     * @return retorna uma entidade do tipo MapaDespesa
     */
    MapaDespesa findByContratoPratica(final ContratoPratica cp);
    
    /**
     * Busca uma lista de entidades pelo filtro. 
     * 
     * @param filter
     *            entidade populada com os valores do filtro e carrega.
     * @param cli
     *          entidade Cliente
     * @param msa 
     *          entidade Msa
     * @param natureza
     *          entidade NaturezaCentroLucro
     * @param centroLucro 
     *          entidade CentroLucro         
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<MapaDespesa> findByFilter(final MapaDespesa filter,
            final Cliente cli, final Msa msa, 
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro);
}
