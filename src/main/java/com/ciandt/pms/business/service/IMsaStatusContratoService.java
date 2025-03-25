package com.ciandt.pms.business.service;

import com.ciandt.pms.model.MsaStatusContrato;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMsaStatusContratoService {

    List<MsaStatusContrato> findAllActive();

    MsaStatusContrato findById(Long statusCode);

    MsaStatusContrato findByName(String name);
}
