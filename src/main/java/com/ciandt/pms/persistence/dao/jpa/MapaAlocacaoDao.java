/**
 * Classe MapaAlocacaoDao que implementa a camada de DAO da entidade MapaAlocacao 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.persistence.dao.IMapaAlocacaoDao;


/**
 * 
 * A classe MapaAlocacaoDao proporciona as funcionalidades de acesso ao banco de
 * dados para a entidade MapaAlocacao.
 * 
 * @since 12/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class MapaAlocacaoDao extends AbstractDao<Long, MapaAlocacao> implements
        IMapaAlocacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public MapaAlocacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MapaAlocacao.class);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findByFilter(final MapaAlocacao filter) {
        Long codigoContratoPratica = Long.valueOf(0);
        if (filter.getContratoPratica() != null) {
            codigoContratoPratica = filter.getContratoPratica()
                    .getCodigoContratoPratica();
        }

        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_BY_FILTER,
                new Object[] {filter.getTextoTitulo(),
                        filter.getTextoTitulo(), filter.getIndicadorVersao(),
                        filter.getIndicadorVersao(), codigoContratoPratica,
                        codigoContratoPratica });
        return listResult;
    }

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
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findByFilterFetch(final MapaAlocacao filter,
            final Cliente cli, final Msa msa,
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro,
            final Pessoa pessoaFlwer) {

        Long codigoContratoPratica = Long.valueOf(0);
        if (filter.getContratoPratica() != null) {
            codigoContratoPratica = filter.getContratoPratica()
                    .getCodigoContratoPratica();
        }

        Long idCliente = cli.getCodigoCliente();
        if (idCliente == null) {
            idCliente = Long.valueOf(0);
        }

        Long idContrato = msa.getCodigoMsa();
        if (idContrato == null) {
            idContrato = Long.valueOf(0);
        }

        Long idNatureza = natureza.getCodigoNatureza();
        if (idNatureza == null) {
            idNatureza = Long.valueOf(0);
        }

        Long idCentroLucro = centroLucro.getCodigoCentroLucro();
        if (idCentroLucro == null) {
            idCentroLucro = Long.valueOf(0);
        }

        List<MapaAlocacao> listResult = null;

        if (pessoaFlwer != null) {
            listResult = getJpaTemplate().findByNamedQuery(
                    MapaAlocacao.FIND_BY_FILTER_FETCH_FOLLOWING_ONLY,
                    new Object[] {filter.getTextoTitulo(),
                            filter.getTextoTitulo(),
                            filter.getIndicadorVersao(),
                            filter.getIndicadorVersao(), codigoContratoPratica,
                            codigoContratoPratica, idContrato, idContrato,
                            idCliente, idCliente, idCentroLucro, idCentroLucro,
                            idNatureza, idNatureza,
                            pessoaFlwer.getCodigoPessoa() });
        } else {
            listResult = getJpaTemplate().findByNamedQuery(
                    MapaAlocacao.FIND_BY_FILTER_FETCH,
                    new Object[] {filter.getTextoTitulo(),
                            filter.getTextoTitulo(),
                            filter.getIndicadorVersao(),
                            filter.getIndicadorVersao(), codigoContratoPratica,
                            codigoContratoPratica, idContrato, idContrato,
                            idCliente, idCliente, idCentroLucro, idCentroLucro,
                            idNatureza, idNatureza });
        }

        return listResult;
    }

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
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findByFilter(final MapaAlocacao filter,
            final Cliente cli, final Date monthDate,
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro) {

        Long codigoContratoPratica = Long.valueOf(0);
        if (filter.getContratoPratica() != null) {
            codigoContratoPratica = filter.getContratoPratica()
                    .getCodigoContratoPratica();
        }

        Long idCliente = cli.getCodigoCliente();
        if (idCliente == null) {
            idCliente = Long.valueOf(0);
        }

        Long idNatureza = natureza.getCodigoNatureza();
        if (idNatureza == null) {
            idNatureza = Long.valueOf(0);
        }

        Long idCentroLucro = centroLucro.getCodigoCentroLucro();
        if (idCentroLucro == null) {
            idCentroLucro = Long.valueOf(0);
        }

        String indicadorVersao = "PB";

        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_BY_FILTER_REVIEW,
                new Object[]{codigoContratoPratica,
                                codigoContratoPratica, idCliente, idCliente,
                                monthDate, indicadorVersao, idCentroLucro,
                                idCentroLucro, idNatureza, idNatureza,
                                monthDate});
        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro. Também carrega asentidades
     * relacionadas.
     * 
     * @param monthDate
     *            data para filtro
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findByDate(final Date monthDate) {

        String indicadorVersao = "PB";

        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_BY_DATE,
                new Object[] {monthDate, indicadorVersao });
        return listResult;
    }

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
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findByFilterWithoutRevenue(final Cliente cli,
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro,
            final Pratica pratica, final Date dataMes,
            final String statusReceita) {

        Long idCliente = Long.valueOf(0);
        if (cli != null) {
            idCliente = cli.getCodigoCliente();
        }

        Long idNatureza = Long.valueOf(0);
        if (natureza != null) {
            idNatureza = natureza.getCodigoNatureza();
        }

        Long idCentroLucro = Long.valueOf(0);
        if (centroLucro != null) {
            idCentroLucro = centroLucro.getCodigoCentroLucro();
        }

        Long idPratica = Long.valueOf(0);
        if (pratica != null) {
            idPratica = pratica.getCodigoPratica();
        }

        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_BY_FILTER_WITHOUT_REVENUE,
                new Object[] {idCliente, idCliente, idCentroLucro,
                        idCentroLucro, idNatureza, idNatureza, idPratica,
                        idPratica, dataMes, dataMes, dataMes });
        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo login(username) que possui o 'lock' do
     * mapa de alocação.
     * 
     * @param username
     *            login do usuário desejado.
     * 
     * @return lista de MapaAlocacao.
     */
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findByLoginTrava(final String username) {

        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_BY_LOGIN_TRAVA, new Object[] {username });

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo por todos os mapas que estão com lock.
     * 
     * @return lista de MapaAlocacao.
     */
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findAllLock() {
        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_ALL_LOCK);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo por todos os mapas que possuem a versão
     * igual a 'Published'.
     * 
     * @return lista de MapaAlocacao.
     */
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findAllPublished() {
        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_ALL_PUBLISHED);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo por todos os mapas que possuem a versão
     * igual a 'Published'.
     * 
     * @param cp
     *            - Entidade ContratoPratica.
     * 
     * @return lista de MapaAlocacao.
     */
    @SuppressWarnings("unchecked")
    public MapaAlocacao findByContratoPratica(final ContratoPratica cp) {
        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_BY_CONTRATO_PRATICA,
                new Object[] {cp.getCodigoContratoPratica() });

        if (listResult.isEmpty() || listResult == null) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca uma lista de mapas que possuem a versão 'Published' no range de
     * meses: closingDate + 1 até infinito.
     * 
     * @param startDate
     *            - data de início do range do follow
     * 
     * @return lista de MapaAlocacao.
     */
    @SuppressWarnings("unchecked")
    public List<MapaAlocacao> findAllPBByRangeMonths(final Date startDate) {
        List<MapaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                MapaAlocacao.FIND_ALL_PB_BY_RANGE_MONTHS,
                new Object[] {startDate});

        return listResult;
    }

}