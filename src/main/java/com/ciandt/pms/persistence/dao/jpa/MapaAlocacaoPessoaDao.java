package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaAlocacaoPessoa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IMapaAlocacaoPessoaDao;


/**
 * 
 * A classe MapaAlocacaoPessoaDao proporciona as funcionalidades de acesso ao
 * banco de dados referente a entidade MapaAlocacaoPessoa.
 * 
 * @since 18/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class MapaAlocacaoPessoaDao extends
        AbstractDao<Long, MapaAlocacaoPessoa> implements IMapaAlocacaoPessoaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            da entidade
     * 
     */
    public MapaAlocacaoPessoaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, MapaAlocacaoPessoa.class);
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
    @SuppressWarnings("unchecked")
    public MapaAlocacaoPessoa findByMapaAlocAndPessoa(
            final MapaAlocacao mapaAlocacao, final Pessoa pessoa) {
        List<MapaAlocacaoPessoa> listResult = getJpaTemplate()
                .findByNamedQuery(
                        MapaAlocacaoPessoa.FIND_BY_MAPA_ALOC_AND_PESSOA,
                        new Object[] {mapaAlocacao.getCodigoMapaAlocacao(),
                                pessoa.getCodigoPessoa() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
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
    @SuppressWarnings("unchecked")
    public List<MapaAlocacaoPessoa> findByMapaAloc(final Long codigoMapaAlocacao) {
        List<MapaAlocacaoPessoa> listResult = getJpaTemplate()
                .findByNamedQuery(
                        MapaAlocacaoPessoa.FIND_BY_MAPA_ALOC,
                        new Object[] {codigoMapaAlocacao});

        return listResult;
    }
    
    /**
     * Busca o MapaAlocacaoPessoa segundo parametros codigoMD5.
     * 
     * @param codigoMD5
     *            - chave encriptada para usar pelo Unsubscribe
     * 
     * @return MapaAlocacaoPessoa
     */
    @SuppressWarnings("unchecked")
    public MapaAlocacaoPessoa findByCodigoMD5(final String codigoMD5) {
        List<MapaAlocacaoPessoa> listResult = getJpaTemplate()
                .findByNamedQuery(MapaAlocacaoPessoa.FIND_BY_CODIGO_MD5,
                        new Object[] {codigoMD5 });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
    
}