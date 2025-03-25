package com.ciandt.pms.persistence.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.Acelerador;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ComissaoAcelerador;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IComissaoAceleradorDao proporciona interface de acesso referente
 * a camada de persistencia referente a entidade ComissaoAcelerador.
 *
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IComissaoAceleradorDao 
    extends IAbstractDao<Long, ComissaoAcelerador> {

    /**
     * Busca pelo filtro.
     * 
     * @param startDate - data inicial
     * @param endDate - data final
     * @param c - cliente
     * @param cp - contrato pratica
     * @param a - acelerador
     * @param ae - AE da comissao
     * @param dn - DN aprovador da comissão
     * @param status - status da comissao.
     * 
     * @return retorna uma lista de ComissaoAcelerador
     */
    List<ComissaoAcelerador> findByFilter(
            final Date startDate, final Date endDate, final Cliente c, final ContratoPratica cp, 
            final Acelerador a, final Pessoa ae, final Pessoa dn, final String status);
    
    /**
     * Retorna o total acumulado por comissao.
     * 
     * @param comissaoAcelerador - Entidade do tipo ComissaoAcelerador
     * 
     * @return retorna o total Acumulado
     */
    BigDecimal getTotalAcumulado(final ComissaoAcelerador comissaoAcelerador);
    
}
