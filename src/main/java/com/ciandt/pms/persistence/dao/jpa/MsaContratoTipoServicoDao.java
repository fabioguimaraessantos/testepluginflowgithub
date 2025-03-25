package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.MsaContratoTipoServico;
import com.ciandt.pms.persistence.dao.IMsaContratoTipoServicoDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MsaContratoTipoServicoDao extends AbstractDao<Long, MsaContratoTipoServico> implements IMsaContratoTipoServicoDao {

    /**
     * Construtor padrão.
     *
     * @param factory     EntityManagerFactory
     */
    public MsaContratoTipoServicoDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaContratoTipoServico.class);
    }

    @Override
    public List<MsaContratoTipoServico> findByMsaContratoCode(Long msaContratoCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msaContratoCode", msaContratoCode);

        List<MsaContratoTipoServico> result = getJpaTemplate().findByNamedQueryAndNamedParams(MsaContratoTipoServico.FIND_BY_MSA_CONTRATO_CODE, params);

        if (null != result && !result.isEmpty()) {
            return result;
        }
        return new ArrayList<MsaContratoTipoServico>();
    }

    @Override
    @Transactional
    public void merge(List<MsaContratoTipoServico> msaContratoTipoServicos) {
        if (null != msaContratoTipoServicos && !msaContratoTipoServicos.isEmpty()) {
            for (MsaContratoTipoServico tipoServico : msaContratoTipoServicos) {
                this.merge(tipoServico);
                this.flush();
            }
        }
    }
}
