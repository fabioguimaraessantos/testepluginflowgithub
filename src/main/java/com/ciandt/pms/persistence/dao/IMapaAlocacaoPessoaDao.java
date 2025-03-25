package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaAlocacaoPessoa;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IMapaAlocacaoPessoaDao proporciona a interface de acesso ao banco de
 * dados referente a entidade MapaAlocacaoPessoa.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IMapaAlocacaoPessoaDao extends
        IAbstractDao<Long, MapaAlocacaoPessoa> {

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
    MapaAlocacaoPessoa findByMapaAlocAndPessoa(final MapaAlocacao mapaAlocacao,
            final Pessoa pessoa);

    /**
     * Busca uma lista de MapaAlocacaoPessoa pelo MapaAlocacao para saber quem
     * são os followers.
     * 
     * @param codigoMapaAlocacao
     *            - código do MapaAlocacao
     * 
     * @return uma lista de MapaAlocacaoPessoa
     */
    List<MapaAlocacaoPessoa> findByMapaAloc(final Long codigoMapaAlocacao);

    /**
     * Busca o MapaAlocacaoPessoa segundo parametros codigoMD5.
     * 
     * @param codigoMD5
     *            - chave encriptada para usar pelo Unsubscribe
     * 
     * @return MapaAlocacaoPessoa
     */
    MapaAlocacaoPessoa findByCodigoMD5(final String codigoMD5);

}