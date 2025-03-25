package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.PaymentConditionDealFiscal;
import com.ciandt.pms.persistence.dao.IPaymentConditionDealFiscalDao;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentConditionDealFiscalDao extends AbstractDao<Long, PaymentConditionDealFiscal>
        implements IPaymentConditionDealFiscalDao {

    private EntityManager entityManager;

    @Autowired
    public PaymentConditionDealFiscalDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PaymentConditionDealFiscal.class);

        entityManager = factory.createEntityManager();
    }

    public PaymentConditionDealFiscal findActualPaymentCondition(final Long dealFiscalCode) {
        try {
            PaymentConditionDealFiscal result = null;

            Query query = entityManager.createNamedQuery(PaymentConditionDealFiscal.FIND_ACTUAL_PAYMENT_CONDITION);

            query.setParameter("dealFiscalCode", dealFiscalCode);

            List<PaymentConditionDealFiscal> listResult = query.getResultList();

            if (listResult != null && listResult.size() > 0) {
                result = listResult.get(0);
            }

            return result;

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public List<PaymentConditionDealFiscal> findByDealFiscal(final Long dealFiscalCode) {
        try {
            Query query = entityManager.createNamedQuery(PaymentConditionDealFiscal.FIND_BY_DEAL_FISCAL);

            query.setParameter("dealFiscalCode", dealFiscalCode);

           List<PaymentConditionDealFiscal> listResult = query.getResultList();


           return listResult;

        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
