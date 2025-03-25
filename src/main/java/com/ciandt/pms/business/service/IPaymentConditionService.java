package com.ciandt.pms.business.service;

import com.ciandt.pms.model.vo.PaymentCondition;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface IPaymentConditionService {
    List<PaymentCondition> findByClientAndCompany(final Long agentCode, final Long companyCode) throws IOException;
}
