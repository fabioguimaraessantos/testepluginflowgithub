package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Acelerador;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Comissao;
import com.ciandt.pms.model.ComissaoAcelerador;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IComissaoAceleradorDao;


/**
 * 
 * A classe ComissaoAceleradorDao proporciona as funcionalidades de persistencia
 * referente a entidade ComissaoAcelerador.
 * 
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class ComissaoAceleradorDao extends
        AbstractDao<Long, ComissaoAcelerador> implements IComissaoAceleradorDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - Fabrica do entity manager
     * 
     * @param entityClass
     */
    @Autowired
    public ComissaoAceleradorDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ComissaoAcelerador.class);
    }

    /**
     * Busca pelo filtro.
     * 
     * @param startDate
     *            - data inicial
     * @param endDate
     *            - data final
     * @param c
     *            - cliente
     * @param cp
     *            - contrato pratica
     * @param a
     *            - acelerador
     * @param ae
     *            - AE da comissao
     * @param dn
     *            - DN aprovador da comissão
     * @param status
     *            - status da comissão.
     * 
     * @return retorna uma lista de ComissaoAcelerador
     */
    @SuppressWarnings("unchecked")
    public List<ComissaoAcelerador> findByFilter(final Date startDate,
            final Date endDate, final Cliente c, final ContratoPratica cp,
            final Acelerador a, final Pessoa ae, final Pessoa dn,
            final String status) {

        Long codigoContratoPratica = Long.valueOf(0);
        if (cp != null) {
            codigoContratoPratica = cp.getCodigoContratoPratica();
        }
        Long codigoCliente = Long.valueOf(0);
        if (c != null) {
            codigoCliente = c.getCodigoCliente();
        }
        Long codigoAcelerador = Long.valueOf(0);
        if (a != null) {
            codigoAcelerador = a.getCodigoAcelerador();
        }
        Long codigoAe = Long.valueOf(0);
        if (ae != null) {
            codigoAe = ae.getCodigoPessoa();
        }
        Long codigoDn = Long.valueOf(0);
        if (dn != null) {
            codigoDn = dn.getCodigoPessoa();
        }

        List<ComissaoAcelerador> listResult =
                getJpaTemplate().findByNamedQuery(
                        ComissaoAcelerador.FIND_BY_FILTER,
                        new Object[]{codigoAcelerador, codigoContratoPratica,
                                codigoCliente, startDate, endDate, codigoAe,
                                codigoDn, status});

        return listResult;
    }

    /**
     * Retorna o total acumulado por comissao.
     * 
     * @param comissaoAcelerador
     *            - Entidade do tipo ComissaoAcelerador
     * 
     * @return retorna o total Acumulado
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getTotalAcumulado(
            final ComissaoAcelerador comissaoAcelerador) {

        Comissao comissao = comissaoAcelerador.getComissao();

        List<BigDecimal> listResult =
                getJpaTemplate().findByNamedQuery(
                        ComissaoAcelerador.GET_TOTAL_ACUMULADO,
                        new Object[]{
                                comissao.getContratoPratica()
                                        .getCodigoContratoPratica(),
                                comissao.getPessoaAe().getCodigoPessoa(),
                                comissaoAcelerador.getAcelerador()
                                        .getCodigoAcelerador(),
                                comissao.getDataComissao()});

        if (listResult.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return listResult.get(0);
    }
}
