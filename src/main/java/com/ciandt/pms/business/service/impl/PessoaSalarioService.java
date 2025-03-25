package com.ciandt.pms.business.service.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPessoaSalarioService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaSalario;
import com.ciandt.pms.persistence.dao.IPessoaSalarioDao;


/**
 * 
 * A classe PessoaSalarioService proporciona a funcionalidade da camada de
 * serviço referente a entidade PessoaSalario.
 * 
 * @since 02/08/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Deprecated
@Service
public class PessoaSalarioService implements IPessoaSalarioService {

    /** instancia do dao da entidade PessoaSalario. */
    @Autowired
    private IPessoaSalarioDao dao;

    /** instancia do servico da entidade Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** instancia do servico da entidade Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    public Boolean createPessoaSalario(final PessoaSalario entity) {

        // atualiza as entidades / referencias do hibernate
        entity.setMoeda(moedaService.findMoedaById(entity.getMoeda()
                .getCodigoMoeda()));
        entity.setPessoa(pessoaService.findPessoaById(entity.getPessoa()
                .getCodigoPessoa()));

        if (this.existsPessSal(entity)) {
            Messages.showWarning("createPessoaSalario",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_PESSOA_SALARIO);

            return Boolean.valueOf(false);
        }

        PessoaSalario next = this.findPessSalNext(entity);
        PessoaSalario previous = this.findPessSalPrevious(entity);

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
    public Boolean updatePessoaSalario(final PessoaSalario entity) {

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
    public Boolean removePessoaSalario(final PessoaSalario entity) {
        PessoaSalario ps = this
                .findPessSalById(entity.getCodigoPessoaSalario());

        PessoaSalario previous = this.findPessSalPrevious(ps);

        if (previous != null) {

            PessoaSalario next = this.findPessSalNext(ps);
            if (next != null) {
                previous.setDataFim(DateUtils.addMonths(next.getDataInicio(),
                        -1));
            } else {
                previous.setDataFim(null);
            }

            dao.update(previous);
        }

        dao.remove(ps);

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
    public PessoaSalario findPessSalById(final Long entityId) {
        return dao.findById(entityId);
    }

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario anterior.
     */
    public PessoaSalario findPessSalByStartDate(
            final PessoaSalario pessoaSalario) {
        return dao.findByStartDate(pessoaSalario);
    }

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario.
     */
    public PessoaSalario findPessSalNext(final PessoaSalario pessoaSalario) {
        return dao.findNext(pessoaSalario);
    }

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna a PessoaSalario anterior.
     */
    public PessoaSalario findPessSalPrevious(final PessoaSalario pessoaSalario) {
        return dao.findPrevious(pessoaSalario);
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
    public PessoaSalario findPessSalByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes) {
        return dao.findByPessoaAndDate(pessoa, dataMes);
    }

    /**
     * Verifica se existe PessoaSalario com a dataInicio passada por parametro.
     * 
     * @param ps
     *            - Entidade do tipo PessoaSalario
     * 
     * @return retorna true se existe, caso contrario false.
     */
    private Boolean existsPessSal(final PessoaSalario ps) {
        PessoaSalario pessoaSalario = this.findPessSalByStartDate(ps);

        if (pessoaSalario == null) {
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

}