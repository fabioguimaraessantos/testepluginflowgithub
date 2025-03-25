package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaContrato;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMsaContratoDao extends IAbstractDao<Long, MsaContrato> {

    List<MsaContrato> findByMsa(Msa msa);

    List<MsaContrato> findByMsaAndIndicadorDefaultPesuisa(Msa msa, Boolean indicadorDefaultPesquisa);

    List<MsaContrato> findByJiraCp(String jiraCp);

    List<MsaContrato> findByJiraLegal(String jiraLegal);

    List<MsaContrato> findByFilter(final MsaContrato filter);

    List<MsaContrato> findByUniqueKey(MsaContrato msaContrato, Long codeMsa);
}
