package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.Comissao;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IComissaoDao proporciona a interface de acesso
 * para a camada de persistencia referente a entidade Comissao.
 *
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IComissaoDao extends IAbstractDao <Long, Comissao> {

    /**
     * Busca pelo filtro.
     * 
     * @param startDate - data inicial
     * @param endDate - data final
     * @param ae - AE da comissao
     * @param dn - DN aprovador da comissão
     * @param status - status da comissão
     * 
     * @return retorna uma lista de ComissaoAcelerador
     */
    List<Comissao> findByFilterDp(
            final Date startDate, final Date endDate,
            final Pessoa ae, final Pessoa dn, final String status);
}
