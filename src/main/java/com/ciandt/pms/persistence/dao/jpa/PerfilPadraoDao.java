package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.PerfilPadrao;
import com.ciandt.pms.model.Pmg;
import com.ciandt.pms.persistence.dao.IPerfilPadraoDao;


/**
 * 
 * A classe TabelaPrecoDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade PerfilPadrao.
 * 
 * 
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class PerfilPadraoDao extends AbstractDao<Long, PerfilPadrao> implements
        IPerfilPadraoDao {

    /**
     * Construtor.
     * 
     * @param factory
     *            factory
     */
    @Autowired
    public PerfilPadraoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PerfilPadrao.class);

    }

    /**
     * 
     * @param cargo
     *            cargo
     * @param pmg
     *            pmg
     * @param base
     *            base
     * @return Perfil Padrao
     */
    @SuppressWarnings("unchecked")
    public List<PerfilPadrao> findByCargoPmgAndCidadeBase(final CargoPms cargo,
            final Pmg pmg, final CidadeBase base) {
        List<PerfilPadrao> listResult =
                getJpaTemplate()
                        .findByNamedQuery(
                                PerfilPadrao.FIND_BY_CARGO_PMG_AND_CIDADEBASE,
                                new Object[]{cargo.getCodigoCargoPms(),
                                        pmg.getCodigoPmg(),
                                        base.getCodigoCidadeBase()});
        return listResult;
    }

    /**
     * 
     * @param cargoPms cargoPms.
     * @return lista.
     */
    @SuppressWarnings("unchecked")
    public List<PerfilPadrao> findByCargoPms(final CargoPms cargoPms) {
        List<PerfilPadrao> listResult =
                getJpaTemplate().findByNamedQuery(
                        PerfilPadrao.FIND_BY_CARGO_PMS,
                        new Object[]{cargoPms.getCodigoCargoPms()});
        return listResult;
    }

}
