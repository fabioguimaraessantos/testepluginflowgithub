package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IMapaAlocacaoPessoaService;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaAlocacaoPessoa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IMapaAlocacaoPessoaDao;
import com.ciandt.pms.util.HashGeneratorUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;


/**
 * 
 * A classe MapaAlocacaoPessoaService proporciona as funcionalidades da camada
 * de serviço referente a entidade MapaAlocacaoPessoa.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class MapaAlocacaoPessoaService implements IMapaAlocacaoPessoaService {

    /** Instancia do dao da entidade. */
    @Autowired
    private IMapaAlocacaoPessoaDao mapaAlocacaoPessoaDao;

    /** Intancia de mailSender. */
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
     * Follow do MapaAlocacao.
     * 
     * @param mapaAlocacao
     *            - MapaAlocacao a ser seguido
     * @param pessoa
     *            - follower
     */
    public void followMapaAlocacao(final MapaAlocacao mapaAlocacao,
            final Pessoa pessoa) {
        MapaAlocacaoPessoa mapaAlocPessoa = new MapaAlocacaoPessoa();
        mapaAlocPessoa.setMapaAlocacao(mapaAlocacao);
        mapaAlocPessoa.setPessoa(pessoa);

        // time atual que vai concatenar com o login do usuário logado
        String currentTime = String.valueOf((new Date()).getTime());

        // gera o código hash MD5 para ser usado no unsubscribe para garantir
        // que nao haverá códigos hash iguais na base e garantir que somente o
        // próprio usuário pode fazer o UnFollow
        mapaAlocPessoa.setCodigoMD5(HashGeneratorUtil
                .generateAlphanumericHash(currentTime
                        + LoginUtil.getLoggedUsername()));

        this.createMapaAlocacaoPessoa(mapaAlocPessoa);
    }

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    private void createMapaAlocacaoPessoa(final MapaAlocacaoPessoa entity) {
        mapaAlocacaoPessoaDao.create(entity);
    }

    /**
     * Unfollow do MapaAlocacao.
     * 
     * @param mapaAlocacao
     *            - MapaAlocacao que não será mais seguido
     * @param pessoa
     *            - ex-follower
     */
    public void unfollowMapaAlocacao(final MapaAlocacao mapaAlocacao,
            final Pessoa pessoa) {
        MapaAlocacaoPessoa mapaAlocPessoa =
                this.findMapaAlocPessoaByMapaAndPessoa(mapaAlocacao, pessoa);

        this.removeMapaAlocacaoPessoa(mapaAlocPessoa);
    }

    /**
     * Unfollow uma lista de MapaAlocacao.
     * 
     * @param mapaAlocacao
     *            - {@link MapaAlocacao}
     * 
     * @param mapaAlocacaoPessoa
     *            - lista de {@link MapaAlocacaoPessoa}
     */
    public void unfollowListaMapaAlocacao(final MapaAlocacao mapaAlocacao,
            final List<MapaAlocacaoPessoa> mapaAlocacaoPessoa) {

        // remove a lista de MapaAlocacaoPessoa do mapa.
        List<MapaAlocacaoPessoa> maPessList =
                this.findMapaAlocPessoaByMapa(mapaAlocacao
                        .getCodigoMapaAlocacao());

        for (MapaAlocacaoPessoa maPess : maPessList) {
            MapaAlocacaoPessoa mapaAlocPessoa =
                    this.findMapaAlocPessoaByMapaAndPessoa(mapaAlocacao, maPess
                            .getPessoa());

            this.removeMapaAlocacaoPessoa(mapaAlocPessoa);
        }
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    private void removeMapaAlocacaoPessoa(final MapaAlocacaoPessoa entity) {
        mapaAlocacaoPessoaDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public MapaAlocacaoPessoa findMapaAlocacaoPessoaById(final Long id) {
        return mapaAlocacaoPessoaDao.findById(id);
    }

    /**
     * Busca o MapaAlocacaoPessoa segundo parametros MapaAlocacao e Pessoa.
     * 
     * @param mapaAlocacao
     *            - entidade MapaAlocacao
     * @param pessoa
     *            - entidade Pessoa
     * 
     * @return MapaAlocacaoPessoa
     */
    public MapaAlocacaoPessoa findMapaAlocPessoaByMapaAndPessoa(
            final MapaAlocacao mapaAlocacao, final Pessoa pessoa) {
        return mapaAlocacaoPessoaDao.findByMapaAlocAndPessoa(mapaAlocacao,
                pessoa);
    }

    /**
     * Busca uma lista de MapaAlocacaoPessoa pelo MapaAlocacao para saber quem
     * são os followers.
     * 
     * @param codigoMapaAlocacao
     *            - código do MapaAlocacao
     * 
     * @return uma lista de MapaAlocacaoPessoa
     */
    public List<MapaAlocacaoPessoa> findMapaAlocPessoaByMapa(
            final Long codigoMapaAlocacao) {
        return mapaAlocacaoPessoaDao.findByMapaAloc(codigoMapaAlocacao);
    }

    /**
     * Recupera um Map da relacao de MapaAlocacao que a Pessoa corrente está
     * seguindo.
     * 
     * @param pessoa
     *            follower
     * @return um Map com a relação de MapaAlocacao
     */
    public Map<Long, Long> getMapFollowByPessoa(final Pessoa pessoa) {
        Map<Long, Long> mapFollow = new HashMap<Long, Long>();

        List<MapaAlocacaoPessoa> mapaAlocPessoaList =
                pessoa.getMapaAlocacaoPessoas();
        for (MapaAlocacaoPessoa mapaAlocPessoa : mapaAlocPessoaList) {
            mapFollow.put(mapaAlocPessoa.getMapaAlocacao()
                    .getCodigoMapaAlocacao(), pessoa.getCodigoPessoa());
        }

        return mapFollow;
    }

    /**
     * Busca o MapaAlocacaoPessoa segundo parametros codigoMD5.
     * 
     * @param codigoMD5
     *            - chave encriptada para usar pelo Unsubscribe
     * 
     * @return MapaAlocacaoPessoa
     */
    public MapaAlocacaoPessoa findMapaAlocPessByCodigoMD5(final String codigoMD5) {
        return mapaAlocacaoPessoaDao.findByCodigoMD5(codigoMD5);
    }

}