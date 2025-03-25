/**
 * Interface da camada de DAO da entidade Fatura
 */
package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.ReceitaDealFiscal;


/**
 * 
 * A classe IFaturaDao proporciona a interface de acesso para a camada DAO da
 * entidade Fatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IFaturaDao extends IAbstractDao<Long, Fatura> {

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
    List<Fatura> findByFilter(final Fatura filter, final Cliente cli, final Msa msa,
            final Date dataPrevisaoBeg, final Date dataPrevisaoEnd);

    /**
     * Busca uma lista de faturas pendentes.
     * 
     * @param rdf
     *            entidade do tipo FaturaDealFiscal.
     * 
     * @return lista de entidades pendentes.
     */
    List<Fatura> findInvoicePedingByDeal(final ReceitaDealFiscal rdf);
    
    /**
     * Realiza a integração das Fatura referente ao id passado por parametro.
     * Esta integração é realizada através da chamada de uma procedure.
     * 
     * @param codigoFatura
     *            codigo da entidade Fatura.
     * 
     * 
     * @return código do resultado da execução da integração
     */
    Integer integrateFatura(final Long codigoFatura);
    
    /**
     * Busca se o pedido foi cancelado no ERP.
     * 
     * @param fatura entidade Fatura.
     * 
     * 
     * @return retorna true se cancelado ou não existe, caso contrario false.
     * Retorna null caso ocorra uma excetion ao executar a consulta.
     */
    Boolean isErpOrderCanceled(final Fatura fatura);
    
    /**
     * Busca uma lista de entidades nao fechadas, status igual a Open e Aproved.
     * 
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Fatura> findAllNotClosed(final Date dataMes);
    
    /**
     * Busca que estão com a data pagamento para ser submetidas,
     * nos próximos 7 dias. 
     * 
     * @return lista de faturas a ser submetidas.
     */
    List<Fatura> findToBeSubmitted();
    
    /**
     * Busca todas as faturas atrasadas, ou seseja,
     * com a data de pagamento passada e no status OPEN.
     * 
     * @return lista de faturas atrasadas.
     */
    List<Fatura> findDelayed();
    
    /**
     * Busca uma lista de entidades no mês passado por parametro.
     * 
     * @param dataMes - data do mes.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Fatura> findByMonthDate(final Date dataMes);

	Fatura findFaturaApagadaById(Long id);
}