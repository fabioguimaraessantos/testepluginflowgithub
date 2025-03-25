package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Aliquota;
import com.ciandt.pms.model.Imposto;


/**
 * 
 * A classe ITabelaPrecoDao proporciona a interfece de acesso para o banco de
 * dados referente a entidade TabelaPreco.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
public interface IAliquotaDao extends IAbstractDao<Long, Aliquota> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Aliquota> findAll();

    /**
     * Busca uma lista de entidades pelo imposto.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    List<Aliquota> findByImposto(final Imposto imposto);

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    Aliquota findMaxStartDateByImposto(final Imposto imposto);
    
    /**
     * Busca a entidade na data atual.
     * 
     * @param imposto
     *              entidade populada com os valores do imposto.
     *
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    Aliquota findByImpostoByCurrentDate(final Imposto imposto);

}
