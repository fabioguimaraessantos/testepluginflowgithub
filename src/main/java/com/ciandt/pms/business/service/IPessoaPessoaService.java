package com.ciandt.pms.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaPessoa;


/**
 * 
 * A classe IPessoaPessoaService proporciona a interface de acesso a camada de
 * serviço referente a entidade PessoaPessoa.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IPessoaPessoaService {

    /**
     * Follow da Pessoa.
     * 
     * @param pessoa
     *            - Pessoa a ser seguida
     * @param pessoaFlwer
     *            - follower
     */
    @Transactional
    void followPessoa(final Pessoa pessoa, final Pessoa pessoaFlwer);

    /**
     * Unfollow da Pessoa.
     * 
     * @param pessoa
     *            - Pessoa que não será mais seguida
     * @param pessoaFlwer
     *            - ex-follower
     */
    @Transactional
    void unfollowPessoa(final Pessoa pessoa, final Pessoa pessoaFlwer);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    PessoaPessoa findPessoaPessoaById(final Long id);

    /**
     * Busca a PessoaPessoa segundo parametros Pessoa e PessoaFlwer.
     * 
     * @param pessoa
     *            - entidade Pessoa
     * @param pessoaFlwer
     *            - entidade Pessoa
     * 
     * @return PessoaPessoa
     */
    PessoaPessoa findPessPessByPessAndPessFlwer(final Pessoa pessoa,
            final Pessoa pessoaFlwer);

    /**
     * Busca uma lista de PessoaPessoa pela Pessoa para saber quem são os
     * followers.
     * 
     * @param codigoPessoa
     *            - código da Pessoa
     * 
     * @return uma lista de PessoaPessoa
     */
    List<PessoaPessoa> findPessPessByPessoa(final Long codigoPessoa);

    /**
     * Busca uma lista de PessoaPessoa pela Pessoa Follower para saber quais
     * Pessoas estão sendo seguidas.
     * 
     * @param codigoPessoaFlwer
     *            - código da Pessoa Follower
     * 
     * @return uma lista de PessoaPessoa
     */
    List<PessoaPessoa> findPessPessByPessoaFlwer(final Long codigoPessoaFlwer);

    /**
     * Recupera um Map da relacao de Pessoas que a Pessoa corrente está
     * seguindo.
     * 
     * @param pessoaFlwer
     *            follower
     * @return um Map com a relação de Pessoas
     */
    Map<Long, Long> getMapFollowByPessoaFlwer(final Pessoa pessoaFlwer);
    
    /**
     * Busca o PessoaPessoa segundo parametros codigoMD5.
     * 
     * @param codigoMD5
     *            - chave encriptada para usar pelo Unsubscribe
     * 
     * @return PessoaPessoa
     */
    PessoaPessoa findPessPessByCodigoMD5(final String codigoMD5);

}