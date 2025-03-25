package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ItemTabPerPadrao;
import com.ciandt.pms.model.TabelaPerfilPadrao;
import com.ciandt.pms.persistence.dao.IItemTabPerPadraoDao;


/**
 * 
 * A classe TabelaPrecoDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade ItemTabPerfilPadrao.
 * 
 * @since 21/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class ItemTabPerPadraoDao extends AbstractDao<Long, ItemTabPerPadrao>
        implements IItemTabPerPadraoDao {

    /**
     * Construtor.
     * @param factory facotry
     */
    @Autowired
    public ItemTabPerPadraoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ItemTabPerPadrao.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<ItemTabPerPadrao> findAll() {
        List<ItemTabPerPadrao> listResult =
                getJpaTemplate().findByNamedQuery(ItemTabPerPadrao.FIND_ALL);

        return listResult;
    }

    /**
     * Busca pelas entidades por tabelaPerfilPadrao.
     * @param entity tabela
     * @return lista
     */
    @SuppressWarnings("unchecked")
    public List<ItemTabPerPadrao> findByTabelaPerfilPadrao(
            final TabelaPerfilPadrao entity) {
        List<ItemTabPerPadrao> listResult =
                getJpaTemplate().findByNamedQuery(
                        ItemTabPerPadrao.FIND_BY_TABELA_PERFIL_PADRAO,
                        new Object[]{entity.getCodigoTabPerfilPadrao()});
        
        return listResult;
    }

}
