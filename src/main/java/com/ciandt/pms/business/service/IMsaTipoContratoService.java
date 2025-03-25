package com.ciandt.pms.business.service;

import com.ciandt.pms.model.MsaTipoContrato;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMsaTipoContratoService {

    List<MsaTipoContrato> findAllActive();

    MsaTipoContrato findById(Long typeCode);

    MsaTipoContrato findActiveByAcronimo(String acronimo);
}
