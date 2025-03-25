/**
 * Interface da camada de DAO da entidade MapaAlocação
 */
package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Pratica;


/**
 * 
 * A classe IMapaAlocacaoDao proporciona a interface de acesso para a camada DAO
 * da entidade MapaAlocacao.
 * 
 * @since 12/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IMapaAlocacaoDao extends IAbstractDao<Long, MapaAlocacao> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<MapaAlocacao> findByFilter(final MapaAlocacao filter);

    /**
     * Busca uma lista de entidades pelo filtro. Também carrega asentidades
     * relacionadas.
     * 
     * @param filter
     *            entidade populada com os valores do filtro e carrega.
     * @param cli
     *            entidade Cliente
     * @param msa
     *            entidade Msa
     * @param natureza
     *            entidade NaturezaCentroLucro
     * @param centroLucro
     *            entidade CentroLucro
     * @param pessoaFlwer
     *            - follower
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<MapaAlocacao> findByFilterFetch(final MapaAlocacao filter,
            final Cliente cli, final Msa msa,
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro,
            final Pessoa pessoaFlwer);

    /**
     * Busca uma lista de entidades pelo filtro. Também carrega asentidades
     * relacionadas.
     * 
     * @param filter
     *            entidade populada com os valores do filtro e carrega.
     * @param cli
     *            entidade Cliente
     * @param monthDate
     *            data para filtro
     * @param natureza
     *            NaturezaCentroLucro
     * @param centroLucro
     *            CentroLucro
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<MapaAlocacao> findByFilter(final MapaAlocacao filter,
            final Cliente cli, final Date monthDate,
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro);

    /**
     * Busca uma lista de entidades pelo filtro. Também carrega asentidades
     * relacionadas.
     * 
     * @param monthDate
     *            data para filtro
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<MapaAlocacao> findByDate(final Date monthDate);

    /**
     * Busca uma lista de entidades pelo login(username) que possui o 'lock' do
     * mapa de alocação.
     * 
     * @param username
     *            login do usuário desejado.
     * 
     * @return lista de MapaAlocacao.
     */
    List<MapaAlocacao> findByLoginTrava(final String username);

    /**
     * Busca uma lista de entidades pelo por todos os mapas que estão com lock.
     * 
     * @return lista de MapaAlocacao.
     */
    List<MapaAlocacao> findAllLock();

    /**
     * Busca uma lista de entidades pelo por todos os mapas que possuem a versão
     * igual a 'Published'.
     * 
     * @return lista de MapaAlocacao.
     */
    List<MapaAlocacao> findAllPublished();

    /**
     * Busca o Mapa pelo contratoPratica que possuem a versão igual a
     * 'Published'.
     * 
     * @param cp
     *            - Entidade ContratoPratica.
     * 
     * @return retorna o MapaAlocacao.
     */
    MapaAlocacao findByContratoPratica(final ContratoPratica cp);

    /**
     * Busca uma lista de entidades pelo filtro. Também carrega asentidades
     * relacionadas.
     * 
     * @param cli
     *            entidade Cliente
     * @param natureza
     *            entidade NaturezaCentroLucro
     * @param centroLucro
     *            entidade CentroLucro
     * @param dataMes
     *            entidade do tipo Date, referente a data-mes
     * @param pratica
     *            do tipo Pratica.
     * 
     * @param statusReceita
     *            status da receita
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<MapaAlocacao> findByFilterWithoutRevenue(final Cliente cli,
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro,
            final Pratica pratica, final Date dataMes,
            final String statusReceita);

    /**
     * Busca uma lista de mapas que possuem a versão 'Published' no range de
     * meses: closingDate + 1 até infinito.
     * 
     * @param previousDate
     *            - data corrente (sysdate) menos 1 mes
     * 
     * @return lista de MapaAlocacao.
     */
    List<MapaAlocacao> findAllPBByRangeMonths(final Date previousDate);

}