package com.ciandt.pms.persistence.dao;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoAud;
import com.ciandt.pms.model.Invoice;
import java.util.List;
/**
 *
 * A classe IInvoiceDao proporciona a interface de acesso
 * a camada de persistencia referente a entidade {@link Invoice}.
 *
 * */
public interface IInvoiceDao extends IAbstractDao<Long, Invoice> {

    /**
     * Busca uma lista de entidades pelo filtro.
     *
     * @param filter ,entidade populada com os valores do filtro.
     *
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Invoice> findByFilter(final Invoice filter);

    List<Invoice> findByCode();
    /**
     * Busca por todos os Invoice.
     *
     * @return uma lista com todos os Invoice.
     */
    List<Invoice> findAll();
    /**
     * @param codigoInvoice
     * @return invoice
     */
    Invoice findInvoiceByIdWithPeriodo(Long codigoInvoice);
    // implementar o que mais for necess�rio
}