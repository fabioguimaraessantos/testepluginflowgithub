package com.ciandt.pms.business.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaSalario;


/**
 * 
 * A classe IPessoaSalarioService proporciona a interface de acesso a camada de
 * serviço referente a entidade PessoaSalario.
 * 
 * @since 02/08/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IPessoaSalarioService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean createPessoaSalario(final PessoaSalario entity);

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @return true se update com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean updatePessoaSalario(final PessoaSalario entity);

    /**
     * Remove a entidade passada por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     * @return retorna true se sucesso senao false
     */
    @Transactional
    Boolean removePessoaSalario(final PessoaSalario entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    PessoaSalario findPessSalById(final Long entityId);

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario.
     */
    PessoaSalario findPessSalNext(final PessoaSalario pessoaSalario);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario anterior.
     */
    PessoaSalario findPessSalPrevious(final PessoaSalario pessoaSalario);

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario anterior.
     */
    PessoaSalario findPessSalByStartDate(final PessoaSalario pessoaSalario);

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
    PessoaSalario findPessSalByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes);

}