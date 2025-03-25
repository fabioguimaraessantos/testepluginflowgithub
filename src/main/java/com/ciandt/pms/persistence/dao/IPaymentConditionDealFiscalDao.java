package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PaymentConditionDealFiscal;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPaymentConditionDealFiscalDao extends IAbstractDao<Long, PaymentConditionDealFiscal> {

    List<PaymentConditionDealFiscal> findByDealFiscal(Long dealFiscalCode);

    PaymentConditionDealFiscal findActualPaymentCondition(final Long dealFiscalCode);
}
