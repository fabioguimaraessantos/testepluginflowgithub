package com.ciandt.pms.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaAlocacaoPessoa;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IMapaAlocacaoPessoaService proporciona a interface de acesso a
 * camada de serviço referente a entidade MapaAlocacaoPessoaService.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IMapaAlocacaoPessoaService {

    /**
     * Follow do MapaAlocacao.
     * 
     * @param mapaAlocacao
     *            - mapa a ser seguido
     * @param pessoa
     *            - follower
     */
    @Transactional
    void followMapaAlocacao(final MapaAlocacao mapaAlocacao, final Pessoa pessoa);

    /**
     * Unfollow do MapaAlocacao.
     * 
     * @param mapaAlocacao
     *            - MapaAlocacao que não será mais seguido
     * @param pessoa
     *            - ex-follower
     */
    @Transactional
    void unfollowMapaAlocacao(final MapaAlocacao mapaAlocacao,
            final Pessoa pessoa);

    /**
     * Unfollow uma lista de MapaAlocacao.
     * 
     * @param mapaAlocacao
     *            - {@link MapaAlocacao}
     * 
     * @param mapaAlocacaoPessoa
     *            - lista de {@link MapaAlocacaoPessoa}
     */
    @Transactional
    void unfollowListaMapaAlocacao(final MapaAlocacao mapaAlocacao,
            final List<MapaAlocacaoPessoa> mapaAlocacaoPessoa);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    MapaAlocacaoPessoa findMapaAlocacaoPessoaById(final Long id);

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
    MapaAlocacaoPessoa findMapaAlocPessoaByMapaAndPessoa(
            final MapaAlocacao mapaAlocacao, final Pessoa pessoa);

    /**
     * Busca uma lista de MapaAlocacaoPessoa pelo MapaAlocacao para saber quem
     * são os followers.
     * 
     * @param codigoMapaAlocacao
     *            - código do MapaAlocacao
     * 
     * @return uma lista de MapaAlocacaoPessoa
     */
    List<MapaAlocacaoPessoa> findMapaAlocPessoaByMapa(
            final Long codigoMapaAlocacao);

    /**
     * Recupera um Map da relacao de MapaAlocacao que a Pessoa corrente está
     * seguindo.
     * 
     * @param pessoa
     *            follower
     * @return um Map com a relação de MapaAlocacao
     */
    Map<Long, Long> getMapFollowByPessoa(final Pessoa pessoa);

    /**
     * Busca o MapaAlocacaoPessoa segundo parametros codigoMD5.
     * 
     * @param codigoMD5
     *            - chave encriptada para usar pelo Unsubscribe
     * 
     * @return MapaAlocacaoPessoa
     */
    MapaAlocacaoPessoa findMapaAlocPessByCodigoMD5(final String codigoMD5);

}