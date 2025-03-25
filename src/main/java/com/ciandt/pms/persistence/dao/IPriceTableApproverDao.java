package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PriceTableApprover;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPriceTableApproverDao extends IAbstractDao<Long, PriceTableApprover> {

    List<PriceTableApprover> findByMsaCode(Long msaCode);

    PriceTableApprover findByLogin(String login);

    PriceTableApprover findByLoginAndMsaCode(String login, Long msaCode);
}
