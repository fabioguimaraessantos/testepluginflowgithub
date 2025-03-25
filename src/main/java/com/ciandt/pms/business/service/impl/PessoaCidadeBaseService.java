package com.ciandt.pms.business.service.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IPessoaCidadeBaseService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaCidadeBase;
import com.ciandt.pms.persistence.dao.IPessoaCidadeBaseDao;


/**
 *
 * A classe PessoaCidadeBaseService proporciona a funcionalidade da camada de
 * serviço referente a entidade PessoaCidadeBase.
 *
 * @since 06/06/2011
 * @author cmantovani
 *
 */
@Service
public class PessoaCidadeBaseService implements IPessoaCidadeBaseService {

    /** instancia do dao da entidade PessoaCidadeBase. */
    @Autowired
    private IPessoaCidadeBaseDao dao;

    /** instancia do servico da entidade Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** instancia do servico da entidade CidadeBase. */
    @Autowired
    private ICidadeBaseService cidadeBaseService;

    /**
     * Insere uma entidade.
     *
     * @param entity
     *            entidade a ser inserida.
     *
     * @return true se criado com sucesso, caso contrario retorna false
     */
    public Boolean createPessoaCidadeBase(final PessoaCidadeBase entity) {

        // atualiza as entidades / referencias do hibernate
        entity.setCidadeBase(cidadeBaseService.findCidadeBaseById(entity
                .getCidadeBase().getCodigoCidadeBase()));
        entity.setPessoa(pessoaService.findPessoaById(entity.getPessoa()
                .getCodigoPessoa()));

        if (this.exists(entity)) {
            Messages.showWarning("createPessoaCidadeBase",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_PESSOA_CIDADE_BASE);

            return Boolean.FALSE;
        }

        PessoaCidadeBase next = dao.findNext(entity);
        PessoaCidadeBase previous = dao.findPrevious(entity);

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

        /*
         * if (entity.getDataFim() == null) { Pessoa pessoa =
         * entity.getPessoa(); pessoa.setCidadeBase(entity.getCidadeBase());
         * pessoaService.updatePessoa(pessoa); }
         */

        return Boolean.TRUE;
    }

    /**
     * Executa um update na entidade passada por parametro.
     *
     * @param entity
     *            que será atualizada.
     *
     * @return true se update com sucesso, caso contrario retorna false
     */
    public Boolean updatePessoaCidadeBase(final PessoaCidadeBase entity) {

        dao.update(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Remove a entidade passada por parametro, exclusao na tela de abas,
     * verifica as Alocacao e remove os ItemTabelaPreco.
     *
     * @param entity
     *            que será apagada.
     *
     * @return retorna true se sucesso senao false
     */
    public boolean removePessoaCidadeBase(final PessoaCidadeBase entity) {
        PessoaCidadeBase pcb = dao.findById(entity.getCodigoPessoaCidadeBase());

        PessoaCidadeBase previous = dao.findPrevious(pcb);

        if (previous != null) {

            PessoaCidadeBase next = dao.findNext(pcb);
            if (next != null) {
                previous.setDataFim(DateUtils.addMonths(next.getDataInicio(),
                        -1));
            } else {
                previous.setDataFim(null);
            }

            dao.update(previous);
        }

        dao.remove(pcb);

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
    public PessoaCidadeBase findPessoaCidadeBaseById(final Long entityId) {
        return dao.findById(entityId);
    }

    /**
     * Busca pelo data de inicio e pessoa.
     *
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     *
     * @return retorna o PessoaCidadeBase anterior.
     */
    public PessoaCidadeBase findByStartDate(
            final PessoaCidadeBase pessoaCidadeBase) {
        return dao.findByStartDate(pessoaCidadeBase);
    }

    /**
     * Busca pelo proximo, referente a data de inicio.
     *
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     *
     * @return retorna o PessoaCidadeBase.
     */
    public PessoaCidadeBase findNext(final PessoaCidadeBase pessoaCidadeBase) {
        return dao.findNext(pessoaCidadeBase);
    }

    /**
     * Verifica se o fator reajuste já existe. Esta verificação é feita
     * comparando o tipo de contrato e a data de inicio.
     *
     * @param pcb
     *            - Entidade do tipo PessoaCidadeBase
     *
     * @return retorna true se existe, caso contrario false.
     */
    private Boolean exists(final PessoaCidadeBase pcb) {
        PessoaCidadeBase fr = dao.findByStartDate(pcb);

        if (fr == null) {
            return false;
        }

        return true;
    }

    /**
     * Busca pelo anterior, referente a data de inicio.
     *
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     *
     * @return retorna o PessoaCidadeBase anterior.
     */
    public PessoaCidadeBase findPrevious(final PessoaCidadeBase pessoaCidadeBase) {
        return dao.findPrevious(pessoaCidadeBase);
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
    public PessoaCidadeBase findPessCBByPessoaAndDate(final Pessoa pessoa,
                                                      final Date dataMes) {
        return dao.findByPessoaAndDate(pessoa, dataMes);
    }

}