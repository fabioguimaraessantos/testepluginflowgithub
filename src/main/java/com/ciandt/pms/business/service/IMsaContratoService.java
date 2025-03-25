package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaContrato;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IMsaContratoService {

    @Transactional
    void update(MsaContrato entity);

    MsaContrato findById(Long codigoMsaContrato);

    @Transactional
    List<MsaContrato> findByMsaAndMsaStatusContratoDefaultPesquisa(Msa msa, Boolean indicadorDefaultPesquisa);

    List<MsaContrato> findByJiraCp(String jiraCp);

    List<MsaContrato> findByJiraLegal(String jiraLegal);

    List<MsaContrato> findMsaContratoByFilter(final MsaContrato filter);

    @Transactional
    MsaContrato save(MsaContrato msaContrato);

    @Transactional
    List<MsaContrato> findByUniqueKey(MsaContrato msaContrato, Long codeMsa);

    Boolean validateUniqueContract(MsaContrato msaContrato, Long codeMsa);
}
