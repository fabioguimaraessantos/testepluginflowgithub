package com.ciandt.pms.business.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaDelegacao;


/**
 * Define a interface do Service da entidade.
 * 
 * @author cmantovani
 * @since 07/07/2011
 */
@Service
public interface IPessoaDelegacaoService {

    /**
     * Cria uma nova entidade.
     * 
     * @param entity
     *            - entidade a ser criada
     * 
     * @return true se sucesso
     */
    @Transactional
    Boolean createPessoaDelegacao(final PessoaDelegacao entity);

    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            - entidade a ser atualizada
     */
    @Transactional
    void updatePessoaDelegacao(final PessoaDelegacao entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removePessoaDelegacao(final PessoaDelegacao entity);

    /**
     * Busca uma entidade pelo Id.
     * 
     * @param id
     *            - id da entidade a ser encontrada
     * @return entidade encontrada na busca
     */
    PessoaDelegacao findPessoaDelegacaoById(final Long id);

    /**
     * Busca os delegacoes pelo filtro no periodo.
     * 
     * @param pessoa
     *            - pessoa a ser pesquisada
     * @param data
     *            - data de pesquisa
     * @return pessoaDelegacao
     */
    PessoaDelegacao findPessoaDelegacaoByPessoaAndDate(final Pessoa pessoa,
            final Date data);
    
    /**
     * Busca as delegacoes nao expiradas.
     * 
     * @param pessoa
     *            - pessoa a ser pesquisada
     * @param data
     *            - data de pesquisa
     * @return pessoaDelegacao
     */
    PessoaDelegacao findPessoaDelegacaoByPessoaAndFinalDate(final Pessoa pessoa,
            final Date data);

}