package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Imposto;
import com.ciandt.pms.persistence.dao.IImpostoDao;


/**
 * 
 * A classe ContratoPraticaDao proporciona as funcionalidades de acesso ao banco
 * de dados para a entidade ContratoPratica.
 * 
 * @since 08/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Repository
public class ImpostoDao extends AbstractDao<Long, Imposto> implements
        IImpostoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public ImpostoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Imposto.class);
    }

    /**
     * Busca por todos os ContratoPraticas.
     * 
     * @return uma lista com todos os ContratoPraticas.
     */
    @SuppressWarnings("unchecked")
    public List<Imposto> findAll() {

        List<Imposto> listResult = getJpaTemplate().findByNamedQuery(
                Imposto.FIND_ALL);

        return listResult;
    }

}
