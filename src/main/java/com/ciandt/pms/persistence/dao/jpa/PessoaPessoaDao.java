package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaPessoa;
import com.ciandt.pms.persistence.dao.IPessoaPessoaDao;


/**
 * 
 * A classe PessoaPessoaDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade PessoaPessoa.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class PessoaPessoaDao extends AbstractDao<Long, PessoaPessoa> implements
        IPessoaPessoaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            da entidade
     * 
     */
    public PessoaPessoaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, PessoaPessoa.class);
    }

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
    @SuppressWarnings("unchecked")
    public PessoaPessoa findByPessoaAndPessoaFlwer(final Pessoa pessoa,
            final Pessoa pessoaFlwer) {
        List<PessoaPessoa> listResult = getJpaTemplate().findByNamedQuery(
                PessoaPessoa.FIND_BY_PESSOA_AND_PESSOA_FLWER,
                new Object[] {pessoa.getCodigoPessoa(),
                        pessoaFlwer.getCodigoPessoa() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca uma lista de PessoaPessoa pela Pessoa para saber quem são os
     * followers.
     * 
     * @param codigoPessoa
     *            - código da Pessoa
     * 
     * @return uma lista de PessoaPessoa
     */
    @SuppressWarnings("unchecked")
    public List<PessoaPessoa> findByPessoa(final Long codigoPessoa) {
        List<PessoaPessoa> listResult = getJpaTemplate().findByNamedQuery(
                PessoaPessoa.FIND_BY_PESSOA, new Object[] {codigoPessoa });

        return listResult;
    }

    /**
     * Busca uma lista de PessoaPessoa pela Pessoa Follower para saber quais
     * Pessoas estão sendo seguidas.
     * 
     * @param codigoPessoaFlwer
     *            - código da Pessoa Follower
     * 
     * @return uma lista de PessoaPessoa
     */
    @SuppressWarnings("unchecked")
    public List<PessoaPessoa> findByPessoaFlwer(final Long codigoPessoaFlwer) {
        List<PessoaPessoa> listResult = getJpaTemplate().findByNamedQuery(
                PessoaPessoa.FIND_BY_PESSOA_FLWER,
                new Object[] {codigoPessoaFlwer });

        return listResult;
    }
    
    /**
     * Busca o PessoaPessoa segundo parametro codigoMD5.
     * 
     * @param codigoMD5
     *            - chave encriptada para usar pelo Unsubscribe
     * 
     * @return PessoaPessoa
     */
    @SuppressWarnings("unchecked")
    public PessoaPessoa findByCodigoMD5(final String codigoMD5) {
        List<PessoaPessoa> listResult = getJpaTemplate().findByNamedQuery(
                PessoaPessoa.FIND_BY_CODIGO_MD5, new Object[] {codigoMD5 });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}