package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaDelegacao;
import com.ciandt.pms.persistence.dao.IPessoaDelegacaoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author cmantovani
 * @since 07/07/2011
 */
@Repository
public class PessoaDelegacaoDao extends AbstractDao<Long, PessoaDelegacao>
        implements IPessoaDelegacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo PessoaDelegacao
     */
    @Autowired
    public PessoaDelegacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PessoaDelegacao.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<PessoaDelegacao> findAll() {
        List<PessoaDelegacao> listResult =
                getJpaTemplate().findByNamedQuery(PessoaDelegacao.FIND_ALL);

        return listResult;
    }

    /**
     * Busca as delegacoes pelo filtro no periodo.
     * 
     * @param pessoa
     *            - pessoa para consulta
     * @param data
     *            - data de pesquisa
     * @return pessoaDelegacao
     */
    @SuppressWarnings("unchecked")
    public PessoaDelegacao findByPessoaAndDate(final Pessoa pessoa,
            final Date data) {

        List<PessoaDelegacao> listResult =
                getJpaTemplate().findByNamedQuery(
                        PessoaDelegacao.FIND_BY_PESSOA_AND_DATE,
                        new Object[]{pessoa.getCodigoPessoa(), data, data});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca as delegacoes nao expiradas.
     * 
     * @param pessoa
     *            - pessoa a ser pesquisada
     * @param data
     *            - data de pesquisa
     * @return pessoaDelegacao
     */
    @SuppressWarnings("unchecked")
    public PessoaDelegacao findByPessoaAndFinalDate(final Pessoa pessoa,
            final Date data) {

        if (pessoa == null || pessoa.getCodigoPessoa() == null) {
            return null;
        }

        List<PessoaDelegacao> listResult =
                getJpaTemplate().findByNamedQuery(
                        PessoaDelegacao.FIND_BY_PESSOA_AND_FINAL_DATE,
                        new Object[]{pessoa.getCodigoPessoa(), data});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
}