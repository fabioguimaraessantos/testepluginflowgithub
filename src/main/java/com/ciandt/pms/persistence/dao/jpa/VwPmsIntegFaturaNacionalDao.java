package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.VwPmsIntegFaturaNacional;
import com.ciandt.pms.persistence.dao.IVwPmsIntegFaturaNacionalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class VwPmsIntegFaturaNacionalDao extends AbstractDao<Long, VwPmsIntegFaturaNacional> implements IVwPmsIntegFaturaNacionalDao {

    /**
     * Construtor.
     *
     * @param factory
     *            facotry
     */
    @Autowired
    public VwPmsIntegFaturaNacionalDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, VwPmsIntegFaturaNacional.class);
    }

    public List<VwPmsIntegFaturaNacional> findAll(){
        List<VwPmsIntegFaturaNacional> listResult = getJpaTemplate().findByNamedQuery(
                VwPmsIntegFaturaNacional.FIND_ALL);

        return listResult;
    }

    public List<VwPmsIntegFaturaNacional> findByInvoiceCode(final Long invoiceCode) {

        List<VwPmsIntegFaturaNacional> listResult = getJpaTemplate().findByNamedQuery(
                VwPmsIntegFaturaNacional.FIND_BY_INVOICE_CODE,
                new Object[] { invoiceCode });

        return listResult;
    }
}
