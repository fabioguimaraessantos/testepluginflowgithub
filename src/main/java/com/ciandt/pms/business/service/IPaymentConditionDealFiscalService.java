package com.ciandt.pms.business.service;

import com.ciandt.pms.model.PaymentConditionDealFiscal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPaymentConditionDealFiscalService {


    List<PaymentConditionDealFiscal> findByDealFiscal(final Long dealFiscalCode);

    PaymentConditionDealFiscal findActualPaymentCondition(final Long dealFiscalCode);

    void saveByDealFiscal(final Long dealFiscalCode, String paymentConditionName);

    void create(PaymentConditionDealFiscal entity);

    void update(final PaymentConditionDealFiscal paymentConditionDealFiscal);

    PaymentConditionDealFiscal preparePaymentConditionDealFiscalToCreateDealFiscal(String paymentConditionName);
}
