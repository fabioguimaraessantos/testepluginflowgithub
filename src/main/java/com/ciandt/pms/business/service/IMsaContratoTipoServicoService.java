package com.ciandt.pms.business.service;

import com.ciandt.pms.model.MsaContratoTipoServico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IMsaContratoTipoServicoService {

    @Transactional
    List<MsaContratoTipoServico> findByMsaContratoCode(Long msaContratoCode);

    @Transactional
    void merge(List<MsaContratoTipoServico> msaContratoTipoServicos);

    @Transactional
    void create(MsaContratoTipoServico msaContratoTipoServico);


    @Transactional
    void removeByMsaContratoCode(Long codigo);
}
