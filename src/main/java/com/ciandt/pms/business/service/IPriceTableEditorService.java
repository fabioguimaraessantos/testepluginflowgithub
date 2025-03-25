package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PriceTableEditor;
import com.ciandt.pms.model.vo.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IPriceTableEditorService {

    List<PriceTableEditor> findByMsaCode(Long msaCode);

    PriceTableEditor findByLogin(String login);

    PriceTableEditor findByLoginAndMsaCode(String login, Long msaCode);

    @Transactional
    List<PriceTableEditor> update(Msa msa, List<String> logins);

    @Transactional
    void removePriceTableEditor(final PriceTableEditor entity);

    List<UserProfile> autoCompletePriceTable(String login);

    List<String> getEditorsEmailByMsaCode(Long codigoMsa);
}
