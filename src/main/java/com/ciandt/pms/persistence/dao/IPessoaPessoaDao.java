package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaPessoa;


/**
 * 
 * A classe IPessoaPessoaDao proporciona a interface de acesso ao banco de dados
 * referente a entidade PessoaPessoa.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IPessoaPessoaDao extends IAbstractDao<Long, PessoaPessoa> {

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
    PessoaPessoa findByPessoaAndPessoaFlwer(final Pessoa pessoa,
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
    List<PessoaPessoa> findByPessoa(final Long codigoPessoa);
    
    /**
     * Busca uma lista de PessoaPessoa pela Pessoa Follower para saber quais
     * Pessoas estão sendo seguidas.
     * 
     * @param codigoPessoaFlwer
     *            - código da Pessoa Follower
     * 
     * @return uma lista de PessoaPessoa
     */
    List<PessoaPessoa> findByPessoaFlwer(final Long codigoPessoaFlwer);
    
    /**
     * Busca o PessoaPessoa segundo parametro codigoMD5.
     * 
     * @param codigoMD5
     *            - chave encriptada para usar pelo Unsubscribe
     * 
     * @return PessoaPessoa
     */
    PessoaPessoa findByCodigoMD5(final String codigoMD5);

}