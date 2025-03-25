package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaContratoCnpjService;
import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.model.MsaContratoCnpj;
import com.ciandt.pms.persistence.dao.IMsaContratoCnpjDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MsaContratoCnpjService implements IMsaContratoCnpjService {

    @Autowired
    private IMsaContratoCnpjDao dao;

    @Transactional
    public Boolean removeCnpj(final MsaContratoCnpj entity) {
        dao.remove(entity);
        return Boolean.TRUE;
    }

    @Transactional
    public MsaContratoCnpj findById(final Long id) {
        return dao.findById(id);
    }

    @Transactional
    public List<MsaContratoCnpj> findByMsaContrato(MsaContrato msaContrato) {
        return dao.findByMsaContrato(msaContrato);
    }

    @Transactional
    public boolean save(final MsaContratoCnpj msaContratoCnpjList) {
        dao.create(msaContratoCnpjList);
        return true;
    }

    @Transactional
    public void delete(MsaContratoCnpj msaContratoCnpj) {
        dao.remove(msaContratoCnpj);
    }
}
