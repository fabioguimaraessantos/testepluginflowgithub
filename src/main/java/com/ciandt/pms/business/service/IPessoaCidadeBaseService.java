package com.ciandt.pms.business.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaCidadeBase;


/**
 * 
 * A classe IPessoaCidadeBaseService proporciona a interface de acesso a camada
 * de serviço referente a entidade PessoaCidadeBase.
 * 
 * @since 06/06/2011
 * @author cmantovani
 * 
 */
@Service
public interface IPessoaCidadeBaseService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean createPessoaCidadeBase(final PessoaCidadeBase entity);

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @return true se update com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean updatePessoaCidadeBase(final PessoaCidadeBase entity);

    /**
     * Remove a entidade passada por parametro, exclusao na tela de abas,
     * verifica as Alocacao e remove os ItemTabelaPreco.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @return retorna true se sucesso senao false
     */
    @Transactional
    boolean removePessoaCidadeBase(final PessoaCidadeBase entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    PessoaCidadeBase findPessoaCidadeBaseById(final Long entityId);

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o PessoaCidadeBase.
     */
    PessoaCidadeBase findNext(final PessoaCidadeBase pessoaCidadeBase);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o PessoaCidadeBase anterior.
     */
    PessoaCidadeBase findPrevious(final PessoaCidadeBase pessoaCidadeBase);

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o PessoaCidadeBase anterior.
     */
    PessoaCidadeBase findByStartDate(final PessoaCidadeBase pessoaCidadeBase);

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
    PessoaCidadeBase findPessCBByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes);

}