package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PriceTableEditor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPriceTableEditorDao extends IAbstractDao<Long, PriceTableEditor> {

    List<PriceTableEditor> findByMsaCode(Long msaCode);

    PriceTableEditor findByLogin(String login);

    PriceTableEditor findByLoginAndMsaCode(String login, Long msaCode);
}
