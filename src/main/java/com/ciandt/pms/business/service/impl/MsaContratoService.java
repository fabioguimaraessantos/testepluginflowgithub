package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaContratoService;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.persistence.dao.IMsaContratoDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.RememberLoginUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MsaContratoService implements IMsaContratoService {

    @Autowired
    private IMsaContratoDao dao;

    @Transactional
    public void update(MsaContrato entity) {
        if (entity != null ) {
            dao.update(entity);
        }
    }

    public MsaContrato findById(Long codigoMsaContrato) {
        return dao.findById(codigoMsaContrato);
    }

    @Override
    @Transactional
    public List<MsaContrato> findByMsaAndMsaStatusContratoDefaultPesquisa(Msa msa, Boolean indicadorDefaultPesquisa) {
        List<MsaContrato> msaContratoList = new ArrayList<MsaContrato>();
        if (indicadorDefaultPesquisa) {
            msaContratoList = dao.findByMsaAndIndicadorDefaultPesuisa(msa, true);
        } else {
            msaContratoList = dao.findByMsa(msa);
        }

        for (MsaContrato contrato : msaContratoList) {
            Hibernate.initialize(contrato.getMoeda());
        }

        return msaContratoList;
    }

    @Transactional
    public List<MsaContrato> findByUniqueKey(MsaContrato msaContrato, Long codeMsa) {
        return dao.findByUniqueKey(msaContrato, codeMsa);
    }

    @Override
    public Boolean validateUniqueContract(MsaContrato msaContrato, Long codeMsa) {
        List<MsaContrato> contratos = this.findByUniqueKey(msaContrato, codeMsa);

        for (MsaContrato contrato : contratos) {
            if (msaContrato.getCodigo() != contrato.getCodigo() ) {
                return false;
            }
        }
        return true;
    }

    public List<MsaContrato> findMsaContratoByFilter(final MsaContrato filter) {
        return dao.findByFilter(filter);
    }

    @Override
    public List<MsaContrato> findByJiraCp(String jiraCp) {
        List<MsaContrato> resultList = dao.findByJiraCp(jiraCp);
        if (null != resultList && !resultList.isEmpty()) {
            return resultList;
        }
        return new ArrayList<MsaContrato>();
    }

    @Override
    public List<MsaContrato> findByJiraLegal(String jiraLegal) {
        List<MsaContrato> resultList = dao.findByJiraLegal(jiraLegal);
        if (null != resultList && !resultList.isEmpty()) {
            return resultList;
        }
        return new ArrayList<MsaContrato>();
    }

    @Override
    @Transactional
    public MsaContrato save(MsaContrato msaContrato) {
        msaContrato.setDeleteLogico(null == msaContrato.getDeleteLogico() ? Boolean.FALSE : msaContrato.getDeleteLogico());
        msaContrato.setDataAtualizacao(new Date());
        msaContrato.setLoginAtualizacao(LoginUtil.getLoggedUsername());

        return dao.merge(msaContrato);
    }
}
