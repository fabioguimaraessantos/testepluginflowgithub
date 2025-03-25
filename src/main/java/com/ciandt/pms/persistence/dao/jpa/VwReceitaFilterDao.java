package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.VwReceitaFilter;
import com.ciandt.pms.persistence.dao.IVwReceitaFilterDao;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe VwReceitaFilterDao proporciona as funcionalidades de acesso a view
 * do banco de dados referente a VwReceitaFilter.
 * 
 * @since 28/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class VwReceitaFilterDao extends AbstractDao<Long, VwReceitaFilter>
        implements IVwReceitaFilterDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public VwReceitaFilterDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, VwReceitaFilter.class);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param natureza
     *            entidade NaturezaCentroLucro.
     * @param cl
     *            - entidade CentroLucro.
     * @param p
     *            - entidade Pratica.
     * @param cli
     *            - entidade Cliente.
     * @param status
     *            - valor do Status.
     * @param dataMes
     *            - valor da data (mes/ano)
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<VwReceitaFilter> findByFilter(
            final NaturezaCentroLucro natureza, final CentroLucro cl,
            final Pratica p, final Cliente cli, final String status,
            final Date dataMes) {

        Long codNatureza = Long.valueOf(-1L), codCentroLucro = Long
                .valueOf(-1L), codPratica = Long.valueOf(-1L), codCliente = Long
                .valueOf(-1L);

        if (natureza != null && natureza.getCodigoNatureza() != null) {
            codNatureza = natureza.getCodigoNatureza();
        }
        if (cl != null && cl.getCodigoCentroLucro() != null) {
            codCentroLucro = cl.getCodigoCentroLucro();
        }
        if (p != null && p.getCodigoPratica() != null) {
            codPratica = p.getCodigoPratica();
        }
        if (cli != null && cli.getCodigoCliente() != null) {
            codCliente = cli.getCodigoCliente();
        }

        List<VwReceitaFilter> listResult = getJpaTemplate().findByNamedQuery(
                VwReceitaFilter.FIND_BY_FILTER,
                new Object[] {codNatureza, codNatureza, codCentroLucro,
                        codCentroLucro, codPratica, codPratica, codCliente,
                        codCliente, status, status, dataMes,
                        DateUtil.getDate("01", "1900") });

        return listResult;
    }

}