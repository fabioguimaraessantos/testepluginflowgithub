package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IPessoaGrupoCustoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaGrupoCusto;
import com.ciandt.pms.model.PessoaGrupoCustoMG;
import com.ciandt.pms.persistence.dao.IPessoaGrupoCustoDao;
import com.ciandt.pms.persistence.dao.IPessoaGrupoCustoDeleteDao;
import com.ciandt.pms.persistence.dao.IPessoaGrupoCustoMGDao;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * 
 * A classe PessoaGrupoCustoService proporciona a funcionalidade da camada de
 * serviço referente a entidade PessoaGrupoCusto.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class PessoaGrupoCustoService implements IPessoaGrupoCustoService {

    private final Logger logger = LoggerFactory.getLogger(PessoaGrupoCustoService.class);

    /** instancia do dao da entidade PessoaGrupoCusto. */
    @Autowired
    private IPessoaGrupoCustoDao dao;

    @Autowired
    private IPessoaGrupoCustoMGDao daoMG;

    @Autowired
    private IPessoaGrupoCustoDeleteDao daoDelete;

    /** instancia do servico da entidade Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** instancia do servico da entidade GrupoCusto. */
    @Autowired
    private IGrupoCustoService grupoCustoService;

    @Autowired
    private IModuloService moduloService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    public Boolean createPessoaGrupoCusto(final PessoaGrupoCusto entity) {

        // atualiza as entidades / referencias do hibernate
        entity.setGrupoCusto(grupoCustoService.findGrupoCustoById(entity
                .getGrupoCusto().getCodigoGrupoCusto()));
        entity.setPessoa(pessoaService.findPessoaById(entity.getPessoa()
                .getCodigoPessoa()));

        if (this.exists(entity)) {
            Messages.showWarning("createPessoaGrupoCusto",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_GRUPO_CUSTO);

            return Boolean.FALSE;
        }

        PessoaGrupoCusto next = dao.findNext(entity);
        PessoaGrupoCusto previous = dao.findPrevious(entity);

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
    public Boolean updatePessoaGrupoCusto(final PessoaGrupoCusto entity) {

        dao.update(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Remove a entidade passada por parametro
     * @param entity que será apagada.
     * @param ticketId código da solicitação de remoção (zendesk)
     * @return retorna true se sucesso senao false
     */
    public boolean removePessoaGrupoCusto(final PessoaGrupoCusto entity, String ticketId) {
        logger.info("Finding association (MG) by PessoaGrupoCusto (VW) to remove.");
        PessoaGrupoCustoMG existent = daoMG.findByPessoaAndGrupoCustoAndStartDate(entity.getPessoa().getCodigoPessoa(),
                entity.getGrupoCusto().getCodigoGrupoCusto(), entity.getDataInicio());

        if (existent == null) {
            logger.warn("Not found PessoaGrupoCustoMG for {} in {} at {} to remove.",
                    entity.getPessoa().getCodigoLogin(), entity.getGrupoCusto().getNomeGrupoCusto(), entity.getDataInicio());
            return false;
        }

        Long removedCode = existent.getCodigoPessoaGrupoCusto();

        Date historyLock = moduloService.getClosingDateHistoryLock();
        if (!existent.getDataInicio().after(historyLock)) {
            logger.warn("Cannot remove association before history lock closing date {}. ", historyLock);
            return false;
        }

        PessoaGrupoCustoMG previous = daoMG.findPrevious(existent);
        if (previous != null) {
            logger.info("Found previous CC {} to adjust current final date {}.",
                    previous.getGrupoCusto().getNomeGrupoCusto(), previous.getDataFim());
            PessoaGrupoCustoMG next = daoMG.findNext(existent);
            if (next != null) {
                logger.info("Found next CC {} starting at {}.", next.getGrupoCusto().getNomeGrupoCusto(),
                        next.getDataInicio());
                previous.setDataFim(DateUtils.addMonths(next.getDataInicio(), -1));
            } else {
                previous.setDataFim(null);
            }
            daoMG.update(previous);
            logger.info("Previous final date updated to {}.", previous.getDataFim());
        }

        daoMG.remove(existent);
        this.createTicketAssociation(removedCode, ticketId);
        logger.info("Entity removed successfully.");
        return true;
    }

    /**
     * Cria registro na tabela de relação de associações excluídas.
     * @param removedId Código da associação removida
     * @param ticketId Código da solicitação de remoção (zendesk)
     */
    private void createTicketAssociation(Long removedId, String ticketId) {
        daoDelete.createTicketAssociation(removedId, ticketId);
        logger.info("Created ticket association with removed ID {} and ticket ID {}", removedId, ticketId);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public PessoaGrupoCusto findPessoaGrupoCustoById(final Long entityId) {
        return dao.findById(entityId);
    }

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto anterior.
     */
    public PessoaGrupoCusto findByStartDate(final PessoaGrupoCusto pessoaGc) {
        return dao.findByStartDate(pessoaGc);
    }

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto.
     */
    public PessoaGrupoCusto findNext(final PessoaGrupoCusto pessoaGc) {
        return dao.findNext(pessoaGc);
    }

    /**
     * Verifica se o fator reajuste já existe. Esta verificação é feita
     * comparando o tipo de contrato e a data de inicio.
     * 
     * @param pgc
     *            - Entidade do tipo FatorReajuste
     * 
     * @return retorna true se existe, caso contrario false.
     */
    private Boolean exists(final PessoaGrupoCusto pgc) {
        PessoaGrupoCusto fr = dao.findByStartDate(pgc);

        if (fr == null) {
            return false;
        }

        return true;
    }

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto anterior.
     */
    public PessoaGrupoCusto findPrevious(final PessoaGrupoCusto pessoaGc) {
        return dao.findPrevious(pessoaGc);
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
    public PessoaGrupoCusto findPessGCByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes) {
        return dao.findByPessoaAndDate(pessoa, dataMes);
    }

    @Override
    public List<PessoaGrupoCusto> findByPeopleCodeInAndDate(final Set<Long> peopleCodes, final Date month) {
        return dao.findByPeopleCodeInAndDate(peopleCodes, month);
    }

}