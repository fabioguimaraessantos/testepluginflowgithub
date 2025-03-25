package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaTipoContrato;


/**
 * 
 * A classe IPessoaTipoContratoService proporciona a interface de acesso a
 * camada de serviço referente a entidade PessoaTipoContrato.
 * 
 * @since 27/07/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IPessoaTipoContratoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean createPessoaTipoContrato(final PessoaTipoContrato entity);

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @return true se update com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean updatePessoaTipoContrato(final PessoaTipoContrato entity);

    /**
     * Remove a entidade passada por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     * @return retorna true se sucesso senao false
     */
    @Transactional
    Boolean removePessoaTipoContrato(final PessoaTipoContrato entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    PessoaTipoContrato findPessoaTipoContratoById(final Long entityId);

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato.
     */
    PessoaTipoContrato findPessTCNext(final PessoaTipoContrato pessoaTC);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato anterior.
     */
    PessoaTipoContrato findPessTCPrevious(final PessoaTipoContrato pessoaTC);

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato anterior.
     */
    PessoaTipoContrato findPessTCByStartDate(final PessoaTipoContrato pessoaTC);

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
    PessoaTipoContrato findPessTCByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes);

    List<PessoaTipoContrato> findByPeopleCodesInAndMonth(final Set<Long> peopleCodes, final Date month);
}