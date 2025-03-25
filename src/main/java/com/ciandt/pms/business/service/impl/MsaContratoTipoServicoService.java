package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaContratoTipoServicoService;
import com.ciandt.pms.model.MsaContratoTipoServico;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.persistence.dao.IMsaContratoTipoServicoDao;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MsaContratoTipoServicoService implements IMsaContratoTipoServicoService {

    @Autowired
    private IMsaContratoTipoServicoDao msaContratoTipoServicoDao;


    @Override
    public List<MsaContratoTipoServico> findByMsaContratoCode(Long msaContratoCode) {
        List<MsaContratoTipoServico> result = msaContratoTipoServicoDao.findByMsaContratoCode(msaContratoCode);

        for (MsaContratoTipoServico tipoServico : result) {
            Hibernate.initialize(tipoServico.getTipoServico());
        }

        return result;
    }

    @Override
    @Transactional
    public void merge(List<MsaContratoTipoServico> msaContratoTipoServicos) {
        msaContratoTipoServicoDao.merge(msaContratoTipoServicos);
    }


    @Override
    @Transactional
    public void create(MsaContratoTipoServico msaContratoTipoServicos) {
        msaContratoTipoServicoDao.create(msaContratoTipoServicos);
    }


    @Override
    @Transactional
    public void removeByMsaContratoCode(Long codigo) {
        List<MsaContratoTipoServico> tipoServicos = msaContratoTipoServicoDao.findByMsaContratoCode(codigo);

        for (MsaContratoTipoServico tipoServico : tipoServicos) {
            msaContratoTipoServicoDao.remove(tipoServico);
        }
    }
}
