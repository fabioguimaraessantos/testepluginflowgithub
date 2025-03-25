package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaGrupoCusto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * 
 * A classe IPessoaGrupoCustoService proporciona a interface de acesso a camada
 * de serviço referente a entidade PessoaGrupoCusto.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IPessoaGrupoCustoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean createPessoaGrupoCusto(final PessoaGrupoCusto entity);

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @return true se update com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean updatePessoaGrupoCusto(final PessoaGrupoCusto entity);

    /**
     * Remove a entidade passada por parametro, exclusao na tela de abas,
     * verifica as Alocacao e remove os ItemTabelaPreco.
     * 
     * @param entity
     *            que será apagada.
     * @param ticketId código do ticket da solicitação de remoção
     * @return retorna true se sucesso senao false
     */
    @Transactional
    boolean removePessoaGrupoCusto(final PessoaGrupoCusto entity, final String ticketId);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    PessoaGrupoCusto findPessoaGrupoCustoById(final Long entityId);

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto.
     */
    PessoaGrupoCusto findNext(final PessoaGrupoCusto pessoaGc);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto anterior.
     */
    PessoaGrupoCusto findPrevious(final PessoaGrupoCusto pessoaGc);

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto anterior.
     */
    PessoaGrupoCusto findByStartDate(final PessoaGrupoCusto pessoaGc);

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
    PessoaGrupoCusto findPessGCByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes);

    List<PessoaGrupoCusto> findByPeopleCodeInAndDate(final Set<Long> peopleCodes, final Date month);

}