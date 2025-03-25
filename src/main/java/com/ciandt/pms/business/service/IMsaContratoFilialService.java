package com.ciandt.pms.business.service;

import com.ciandt.pms.model.MsaContratoFilial;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IMsaContratoFilialService {

    @Transactional
    List<MsaContratoFilial> findByMsaContratoCode(Long msaContratoCode);

    @Transactional
    void save(List<MsaContratoFilial> msaContratoFiliais);

    @Transactional
    void create(MsaContratoFilial msaContratoFiliai);

    @Transactional
    void removeByMsaContratoCode(Long msaContratoCode);
}
