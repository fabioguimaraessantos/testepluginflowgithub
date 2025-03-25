package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPessoaBillabilityService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Billability;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaBillability;
import com.ciandt.pms.model.PessoaBillabilityId;
import com.ciandt.pms.persistence.dao.IPessoaBillabilityDao;
import com.ciandt.pms.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * A classe PessoaBillabilityService proporciona as funcionalidades da camada de
 * serviço referente a entidade PessoaBillability.
 *
 */
@Service
public class PessoaBillabilityService implements IPessoaBillabilityService {

    /** Instancia do DAO do PessoaBillability. */
    @Autowired
    private IPessoaBillabilityDao dao;

    /**
     * Busca a entidade pelo id (codigoPessoa e codigoBillability).
     *
     * @param codigoPessoa
     *            - codigo da {@link Pessoa}
     * @param codigoBillability
     *            - codigo do {@link Billability}
     *
     * @return retorna a entidade
     */
    @Override
    public PessoaBillability findPessoaBillabilityById(final Long codigoPessoa,
                                                       final Long codigoBillability,
                                                       final Date dataInicio) {
        return dao.findById(codigoPessoa, codigoBillability, dataInicio);
    }

    /**
     * Remove a entity.
     *
     * @param pessoaBillability
     *            the {@link PessoaBillability}
     */
    @Override
    @Transactional
    public void removePessoaBillability(final PessoaBillability pessoaBillability) {

        PessoaBillability before = dao.findByPessoaAndDataFim(pessoaBillability.getPessoa().getCodigoPessoa(), DateUtil.addMonths(pessoaBillability.getDataInicio(),-1));

        if (before != null) {
            if (pessoaBillability.getDataFim() != null) {
                PessoaBillability next = dao.findByPessoaAndDataInicio(pessoaBillability.getPessoa().getCodigoPessoa(), DateUtil.addMonths(pessoaBillability.getDataFim(), 1));
                if (before != null) {
                    before.setDataFim(null);
                    dao.update(before);
                }
                if (next != null) {
                    dao.remove(next);
                }
            } else {
                before.setDataFim(null);
                dao.update(before);
            }
        }

        dao.remove(pessoaBillability);
    }

    /**
     * Add a entity.
     *
     * @param pessoaBillability
     *            the {@link PessoaBillability}
     */
    @Override
    @Transactional
    public void createPessoaBillability(final PessoaBillability pessoaBillability) {

        PessoaBillability before = dao.findByPessoaAndDateBetween(pessoaBillability.getPessoa().getCodigoPessoa(), pessoaBillability.getDataInicio());

        // Case 5
        if (before != null) {
            Calendar newEndDate = Calendar.getInstance();
            newEndDate.setTime(pessoaBillability.getDataInicio());
            newEndDate.add(Calendar.MONTH, -1);

            before.setDataFim(newEndDate.getTime());
            dao.update(before);
        }
        // Case 6
        PessoaBillability next = dao.findByPessoaAndAfterOrEqualDataInicio(pessoaBillability.getPessoa().getCodigoPessoa(), pessoaBillability.getDataInicio());
        if(next != null && next.hasSameBillabilityType(pessoaBillability.getBillability())) {
            dao.remove(next);
        }

        pessoaBillability.setId(new PessoaBillabilityId(pessoaBillability.getPessoa().getCodigoPessoa(),
                                                        pessoaBillability.getBillability().getCodigoBillability(),
                                                        pessoaBillability.getDataInicio()));
        pessoaBillability.setDataCriacao(new Date());
        dao.persist(pessoaBillability);
    }

    /**
     * Busca {@link PessoaBillability} pelo codigoPessoa.
     *
     * @param pessoa
     * @return Lista de {@link PessoaBillability}
     */
    @Override
    public List<PessoaBillability> findByPessoa(Pessoa pessoa) {
        return dao.findByPessoa(pessoa);
    }

    /**
     * Aplica as validações necessarias antes de criar um novo registro
     *
     * @param pessoaBillability
     * @return true se for valido criar um novo registro
     */
    public boolean isValidToCreate(PessoaBillability pessoaBillability) {

        PessoaBillability hasCreatedSamePeriod = dao.findByPessoaAndDataInicio(pessoaBillability.getPessoa().getCodigoPessoa(), pessoaBillability.getDataInicio());

        if(hasCreatedSamePeriod != null) {
            Messages.showError("validatePessoaBillabilty",
                    Constants.MSG_ERROR_PESSOA_BILLABILTY_PERIOD_EXISTS);
            return false;
        }
        PessoaBillability billabilityExists = dao.findByPessoaAndDateBetween(pessoaBillability.getPessoa().getCodigoPessoa(), pessoaBillability.getDataInicio());

        if(billabilityExists != null && billabilityExists.hasSameBillabilityType(pessoaBillability.getBillability())) {
            Messages.showError("validatePessoaBillabilty",
                    Constants.MSG_ERROR_PESSOA_BILLABILTY_EXISTS);
            return false;
        }

        PessoaBillability billabilityBetweenExists = dao.findByPessoaAndDateExists(pessoaBillability.getPessoa().getCodigoPessoa(), pessoaBillability.getDataInicio());

        if(billabilityBetweenExists != null) {
            Messages.showError("validatePessoaBillabilty",
                    Constants.MSG_ERROR_PESSOA_BILLABILTY_PERIOD_EXISTS);
            return false;
        }

        return true;
    }

    /**
     * Busca pela Pessoa e pela Data de vigencia.
     *
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     *
     * @return a entidade que atende aos criterios do filtro
     */
    public PessoaBillability findByPessoaAndDate(final Pessoa pessoa,
                                                 final Date dataMes) {
        return dao.findByPessoaAndDate(pessoa, dataMes);
    }

    @Override
    @Transactional
    public void addPessoaBillability(final PessoaBillability pessoaBillability) {

        pessoaBillability.setId(new PessoaBillabilityId(pessoaBillability.getPessoa().getCodigoPessoa(),
                pessoaBillability.getBillability().getCodigoBillability(),
                pessoaBillability.getDataInicio()));
        pessoaBillability.setDataCriacao(new Date());

        dao.persist(pessoaBillability);
    }

    @Override
    public List<PessoaBillability> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month) {
        return dao.findByPeopleCodeInAndMonth(peopleCodes, month);
    }
}