package com.ciandt.pms.business.service;

import com.ciandt.pms.model.MsaTipoMontanteDespesa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMsaTipoMontanteDespesaService {

    List<MsaTipoMontanteDespesa> findAllActive();

    MsaTipoMontanteDespesa findById(Long amountExpenseTypeCode);

    MsaTipoMontanteDespesa findByName(String name);
}

