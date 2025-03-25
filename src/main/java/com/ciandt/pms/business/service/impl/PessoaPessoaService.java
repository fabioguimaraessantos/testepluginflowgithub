package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IPessoaPessoaService;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaPessoa;
import com.ciandt.pms.persistence.dao.IPessoaPessoaDao;
import com.ciandt.pms.util.HashGeneratorUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;


/**
 * 
 * A classe PessoaPessoaService proporciona as funcionalidades da camada de
 * serviço referente a entidade PessoaPessoa.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class PessoaPessoaService implements IPessoaPessoaService {

    /** Instancia do dao da entidade. */
    @Autowired
    private IPessoaPessoaDao pessoaPessoaDao;

    /** Instancia de mailSender. */
    private MailSenderUtil mailSender;

    /**
     * @return the mailSender
     */
    public MailSenderUtil getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender
     *            the mailSender to set
     */
    public void setMailSender(final MailSenderUtil mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Follow da Pessoa.
     * 
     * @param pessoa
     *            - Pessoa a ser seguida
     * @param pessoaFlwer
     *            - follower
     */
    public void followPessoa(final Pessoa pessoa, final Pessoa pessoaFlwer) {
        PessoaPessoa pessoaPessoa = new PessoaPessoa();
        pessoaPessoa.setPessoa(pessoa);
        pessoaPessoa.setPessoaFlwer(pessoaFlwer);

        // time atual que vai concatenar com o login do usuário logado
        String currentTime = String.valueOf((new Date()).getTime());

        // gera o código hash MD5 para ser usado no unsubscribe para garantir
        // que nao haverá códigos hash iguais na base e garantir que somente o
        // próprio usuário pode fazer o UnFollow
        pessoaPessoa.setCodigoMD5(HashGeneratorUtil
                .generateAlphanumericHash(currentTime
                        + LoginUtil.getLoggedUsername()));

        this.createPessoaPessoa(pessoaPessoa);
    }

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    private void createPessoaPessoa(final PessoaPessoa entity) {
        pessoaPessoaDao.create(entity);
    }

    /**
     * Unfollow da Pessoa.
     * 
     * @param pessoa
     *            - Pessoa que não será mais seguida
     * @param pessoaFlwer
     *            - ex-follower
     */
    public void unfollowPessoa(final Pessoa pessoa, final Pessoa pessoaFlwer) {
        PessoaPessoa pessoaPessoa = this.findPessPessByPessAndPessFlwer(pessoa,
                pessoaFlwer);

        this.removePessoaPessoa(pessoaPessoa);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    private void removePessoaPessoa(final PessoaPessoa entity) {
        pessoaPessoaDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public PessoaPessoa findPessoaPessoaById(final Long id) {
        return pessoaPessoaDao.findById(id);
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
    public PessoaPessoa findPessPessByPessAndPessFlwer(final Pessoa pessoa,
            final Pessoa pessoaFlwer) {
        return pessoaPessoaDao.findByPessoaAndPessoaFlwer(pessoa, pessoaFlwer);
    }

    /**
     * Busca uma lista de PessoaPessoa pela Pessoa para saber quem são os
     * followers. Pessoa corrente está sendo seguida.
     * 
     * @param codigoPessoa
     *            - código da Pessoa
     * 
     * @return uma lista de PessoaPessoa
     */
    public List<PessoaPessoa> findPessPessByPessoa(final Long codigoPessoa) {
        return pessoaPessoaDao.findByPessoa(codigoPessoa);
    }

    /**
     * Busca uma lista de PessoaPessoa pela Pessoa Follower para saber quais
     * Pessoas estão sendo seguidas. Pessoa corrente é follower, está seguindo
     * 
     * @param codigoPessoaFlwer
     *            - código da Pessoa Follower
     * 
     * @return uma lista de PessoaPessoa
     */
    public List<PessoaPessoa> findPessPessByPessoaFlwer(
            final Long codigoPessoaFlwer) {
        return pessoaPessoaDao.findByPessoaFlwer(codigoPessoaFlwer);
    }

    /**
     * Recupera um Map da relacao de Pessoas que a Pessoa corrente está
     * seguindo.
     * 
     * @param pessoaFlwer
     *            follower
     * @return um Map com a relação de Pessoas
     */
    public Map<Long, Long> getMapFollowByPessoaFlwer(final Pessoa pessoaFlwer) {
        Map<Long, Long> mapFollow = new HashMap<Long, Long>();

        List<PessoaPessoa> pessPessList = this
                .findPessPessByPessoaFlwer(pessoaFlwer.getCodigoPessoa());
        for (PessoaPessoa pessoaPessoa : pessPessList) {
            mapFollow.put(pessoaPessoa.getPessoa().getCodigoPessoa(),
                    pessoaFlwer.getCodigoPessoa());
        }

        return mapFollow;
    }

    /**
     * Busca o PessoaPessoa segundo parametros codigoMD5.
     * 
     * @param codigoMD5
     *            - chave encriptada para usar pelo Unsubscribe
     * 
     * @return PessoaPessoa
     */
    public PessoaPessoa findPessPessByCodigoMD5(final String codigoMD5) {
        return pessoaPessoaDao.findByCodigoMD5(codigoMD5);
    }

}