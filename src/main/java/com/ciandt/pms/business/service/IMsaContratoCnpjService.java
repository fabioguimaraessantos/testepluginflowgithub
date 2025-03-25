package com.ciandt.pms.business.service;

import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.model.MsaContratoCnpj;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMsaContratoCnpjService {

    MsaContratoCnpj findById(final Long id);

    List<MsaContratoCnpj> findByMsaContrato(MsaContrato msaContrato);

    boolean save(final MsaContratoCnpj msaContratoCnpjList);

    void delete(final MsaContratoCnpj msaContratoCnpj);
}
