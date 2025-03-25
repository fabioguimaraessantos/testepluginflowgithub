package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IPessoaTipoContratoService;
import com.ciandt.pms.business.service.ITipoContratoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaTipoContrato;
import com.ciandt.pms.persistence.dao.IPessoaTipoContratoDao;


/**
 * 
 * A classe PessoaTipoContratoService proporciona a funcionalidade da camada de
 * serviço referente a entidade PessoaTipoContrato.
 * 
 * @since 27/07/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class PessoaTipoContratoService implements IPessoaTipoContratoService {

    /** instancia do dao da entidade PessoaTipoContrato. */
    @Autowired
    private IPessoaTipoContratoDao dao;

    /** instancia do servico da entidade Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** instancia do servico da entidade TipoContrato. */
    @Autowired
    private ITipoContratoService tipoContratoService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    public Boolean createPessoaTipoContrato(final PessoaTipoContrato entity) {

        // atualiza as entidades / referencias do hibernate
        entity.setTipoContrato(tipoContratoService.findTipoContratoById(entity
                .getTipoContrato().getCodigoTipoContrato()));
        entity.setPessoa(pessoaService.findPessoaById(entity.getPessoa()
                .getCodigoPessoa()));

        if (this.existsPessTC(entity)) {
            Messages.showWarning("createPessoaTipoContrato",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_TIPO_CONTRATO);

            return Boolean.valueOf(false);
        }

        PessoaTipoContrato next = this.findPessTCNext(entity);
        PessoaTipoContrato previous = this.findPessTCPrevious(entity);

        if (next != null) {
            entity.setDataFim(DateUtils.addMonths(next.getDataInicio(), -1));
        }

        if (previous != null) {
            previous
                    .setDataFim(DateUtils.addMonths(entity.getDataInicio(), -1));

            dao.update(previous);
        }

        entity.setDataCriacao(new Date());

        dao.create(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @return true se update com sucesso, caso contrario retorna false
     */
    public Boolean updatePessoaTipoContrato(final PessoaTipoContrato entity) {

        dao.update(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Remove a entidade passada por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     * @return retorna true se sucesso senao false
     */
    public Boolean removePessoaTipoContrato(final PessoaTipoContrato entity) {
        PessoaTipoContrato ptc = this.findPessoaTipoContratoById(entity
                .getCodigoPessoaTpContrato());

        PessoaTipoContrato previous = this.findPessTCPrevious(ptc);

        if (previous != null) {

            PessoaTipoContrato next = this.findPessTCNext(ptc);
            if (next != null) {
                previous.setDataFim(DateUtils.addMonths(next.getDataInicio(),
                        -1));
            } else {
                previous.setDataFim(null);
            }

            dao.update(previous);
        }

        dao.remove(ptc);

        return Boolean.valueOf(true);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public PessoaTipoContrato findPessoaTipoContratoById(final Long entityId) {
        return dao.findById(entityId);
    }

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato anterior.
     */
    public PessoaTipoContrato findPessTCByStartDate(
            final PessoaTipoContrato pessoaTC) {
        return dao.findByStartDate(pessoaTC);
    }

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato.
     */
    public PessoaTipoContrato findPessTCNext(final PessoaTipoContrato pessoaTC) {
        return dao.findNext(pessoaTC);
    }

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato anterior.
     */
    public PessoaTipoContrato findPessTCPrevious(
            final PessoaTipoContrato pessoaTC) {
        return dao.findPrevious(pessoaTC);
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
    public PessoaTipoContrato findPessTCByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes) {
        return dao.findByPessoaAndDate(pessoa, dataMes);
    }

    @Override
    public List<PessoaTipoContrato> findByPeopleCodesInAndMonth(final Set<Long> peopleCodes, final Date month) {
        return dao.findByPeopleCodeInAndMonth(peopleCodes, month);
    }

    /**
     * Verifica se existe PessoaTipoContrato com a dataInicio passada por
     * parametro.
     * 
     * @param ptc
     *            - Entidade do tipo PessoaTipoContrato
     * 
     * @return retorna true se existe, caso contrario false.
     */
    private Boolean existsPessTC(final PessoaTipoContrato ptc) {
        PessoaTipoContrato fr = this.findPessTCByStartDate(ptc);

        if (fr == null) {
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

}