package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.AnexoTabelaPreco;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.persistence.dao.IAnexoTabelaPrecoDao;


/**
 * 
 * A classe AnexoTabelaPrecoDao proporciona as funcionalidades de acesso ao
 * banco de dados da entidade anexo tabela preco.
 * 
 * @since 27/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class AnexoTabelaPrecoDao extends AbstractDao<Long, AnexoTabelaPreco>
        implements IAnexoTabelaPrecoDao {

    /**
     * Contrutor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public AnexoTabelaPrecoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, AnexoTabelaPreco.class);
    }

    /**
     * Busca pro entidades de anexoTabelaPreco por tabelaPreco.
     * 
     * @param tabelaPreco
     *            tabela.
     * @return Lista com entidades de anexo tabela preco.
     */
    @SuppressWarnings("unchecked")
    public List<AnexoTabelaPreco> findAnexoTabelaPrecoByTabelaPreco(
            final TabelaPreco tabelaPreco) {
        List<AnexoTabelaPreco> resultList =
                getJpaTemplate().findByNamedQuery(
                        AnexoTabelaPreco.FIND_BY_TABELA_PRECO,
                        new Object[]{tabelaPreco.getCodigoTabelaPreco()});
        return resultList;
    }

}
