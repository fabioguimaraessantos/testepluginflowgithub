package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MsaContratoTipoServico;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMsaContratoTipoServicoDao extends IAbstractDao<Long, MsaContratoTipoServico> {

    List<MsaContratoTipoServico> findByMsaContratoCode(Long msaContratoCode);

    void merge(List<MsaContratoTipoServico> msaContratoTipoServicos);
}
