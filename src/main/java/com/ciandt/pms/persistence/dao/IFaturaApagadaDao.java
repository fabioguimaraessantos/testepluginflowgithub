/**
 * Interface da camada de DAO da entidade Fatura
 */
package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.Msa;


/**
 * 
 * A classe IFaturaApagadaDao proporciona a interface de acesso para a camada DAO da
 * entidade FaturaApagada.
 * 
 * @since 30/10/2014
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
public interface IFaturaApagadaDao extends IAbstractDao<Long, FaturaApagada> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param dataPrevisaoBeg
     *            dataPrevisao inicial.
     * @param dataPrevisaoEnd
     *            dataPrevisao final.
     * @param cli
     *          entidade cliente.
     * @param msa
     *          entidade msa.                    
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<FaturaApagada> findByFilter(final Fatura filter, final Cliente cli, final Msa msa,
            final Date dataPrevisaoBeg, final Date dataPrevisaoEnd);
}