package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TabelaPerfilPadrao;
import com.ciandt.pms.persistence.dao.ITabelaPerfilPadraoDao;


/**
 * 
 * A classe TabelaPrecoDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade TabelaPerfilPadrao.
 * 
 * @since 19/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class TabelaPerfilPadraoDao extends
        AbstractDao<Long, TabelaPerfilPadrao> implements ITabelaPerfilPadraoDao {

    /**
     * Construtor.
     * 
     * @param factory
     *            factory
     */
    @Autowired
    public TabelaPerfilPadraoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TabelaPerfilPadrao.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TabelaPerfilPadrao> findAll() {
        List<TabelaPerfilPadrao> listResult =
                getJpaTemplate().findByNamedQuery(TabelaPerfilPadrao.FIND_ALL);

        return listResult;
    }

    /**
     * Retorna Tabela perfil padrao com maior data de incio.
     * @return tabela com maior data.
     */
    @SuppressWarnings("unchecked")
    public TabelaPerfilPadrao findMaxStartDate() {
        List<TabelaPerfilPadrao> listResult =
                getJpaTemplate().findByNamedQuery(
                        TabelaPerfilPadrao.FIND_MAX_START_DATE);
        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
    
        /**
     * Busca maior data de inicio por moeda.
     * @param moeda moeda.
     * @return TabelaPerfilPadrao
     */
    @SuppressWarnings("unchecked")
    public TabelaPerfilPadrao findMaxStartDataByCurrency(final Moeda moeda) {
        List<TabelaPerfilPadrao> listResult =
                getJpaTemplate().findByNamedQuery(
                        TabelaPerfilPadrao.FIND_MAX_START_DATE_BY_CURRENCY,
                        new Object[]{moeda.getCodigoMoeda(), moeda.getCodigoMoeda()});
        if (listResult == null || listResult.size() == 0) {
            return null;
        }
        
        return listResult.get(0);
    }

}
