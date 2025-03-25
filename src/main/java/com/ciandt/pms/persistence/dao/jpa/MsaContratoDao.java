package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.persistence.dao.IMsaContratoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class MsaContratoDao extends AbstractDao<Long, MsaContrato> implements IMsaContratoDao {

    /**
     * Contrutor padrão.
     *
     * @param factory
     *            da entidade
     */
    @Autowired
    public MsaContratoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaContrato.class);
    }

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<MsaContrato> findByMsa(Msa msa) {
        List<MsaContrato> listResult = getJpaTemplate().findByNamedQuery(MsaContrato.FIND_BY_MSA,
                new Object[] { msa.getCodigoMsa() });

        return listResult;
    }

    public List<MsaContrato> findByMsaAndIndicadorDefaultPesuisa(Msa msa, Boolean indicadorDefaultPesquisa) {
        List<MsaContrato> listResult = getJpaTemplate().findByNamedQuery(MsaContrato.FIND_BY_MSA_AND_INDICADOR_DEFAULT_PESQUISA,
                new Object[] { msa.getCodigoMsa(), indicadorDefaultPesquisa });

        return listResult;
    }

    public List<MsaContrato> findByFilter(final MsaContrato filter) {

        Long idMoeda = filter.getMoeda().getCodigoMoeda();
        if (idMoeda == null) {
            idMoeda = 0L;
        }

        Long idStatus = filter.getStatus().getCodigo();
        if (idStatus == null) {
            idStatus = 0L;
        }

        List<MsaContrato> listResult = getJpaTemplate().findByNamedQuery(
                MsaContrato.FIND_BY_FILTER,
                new Object[] { filter.getJiraCp(), filter.getJiraCp(),
                        filter.getJiraLegal(), filter.getJiraLegal(),
                        idMoeda, idMoeda, idStatus, idStatus, filter.getMsa().getCodigoMsa() });
        return listResult;
    }

    @Override
    public List<MsaContrato> findByJiraCp(String jiraCp) {
        return getJpaTemplate().findByNamedQuery(MsaContrato.FIND_BY_JIRA_CP, new Object[] { jiraCp });
    }

    @Override
    public List<MsaContrato> findByJiraLegal(String jiraLegal) {
        return getJpaTemplate().findByNamedQuery(MsaContrato.FIND_BY_JIRA_LEGAL, new Object[] { jiraLegal });
    }

    public List<MsaContrato> findByUniqueKey(MsaContrato msaContrato, Long codeMsa) {
        List<MsaContrato> listResult = getJpaTemplate().findByNamedQuery(MsaContrato.FIND_BY_UNIQUE_KEY,
                msaContrato.getJiraLegal().isEmpty() ? null : msaContrato.getJiraLegal(),
                msaContrato.getJiraLegal().isEmpty() ? null : msaContrato.getJiraLegal(),
                msaContrato.getJiraCp().isEmpty() ? null : msaContrato.getJiraCp(),
                msaContrato.getJiraCp().isEmpty() ? null : msaContrato.getJiraCp(),
                msaContrato.getSow().isEmpty() ? null : msaContrato.getSow(),
                msaContrato.getSow().isEmpty() ? null : msaContrato.getSow(),
                msaContrato.getPo().isEmpty() ? null : msaContrato.getPo(),
                msaContrato.getPo().isEmpty() ? null : msaContrato.getPo(),
                msaContrato.getDataInicial() == null ? new Date() : msaContrato.getDataInicial(),
                msaContrato.getDataInicial() == null ? new Date() : msaContrato.getDataInicial(),
                Boolean.TRUE.equals(msaContrato.getDataIndeterminada()) ? new Date(0) : msaContrato.getDataFinal(),
                codeMsa);

        if (listResult.isEmpty()) {
            return new ArrayList<MsaContrato>();
        }

        return listResult;
    }

}
