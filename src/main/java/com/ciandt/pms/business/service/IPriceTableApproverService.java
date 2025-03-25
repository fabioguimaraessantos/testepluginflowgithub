package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PriceTableApprover;
import com.ciandt.pms.model.vo.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IPriceTableApproverService {

    List<PriceTableApprover> findByMsaCode(Long msaCode);

    PriceTableApprover findByLogin(String login);

    PriceTableApprover findByLoginAndMsaCode(String login, Long msaCode);

    @Transactional
    List<PriceTableApprover> update(Msa msa, List<String> logins);

    @Transactional
    void removePriceTableApprover(final PriceTableApprover entity);

    List<UserProfile> autoCompletePriceTable(String login);

    List<String> getApproversEmailByMsaCode(Long msaCode);
}
