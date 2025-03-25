/**
 * Classe MapaAlocacaoFotoDao que implementa a camada de DAO da entidade MapaAlocacaoFoto 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.MapaAlocacaoFoto;
import com.ciandt.pms.persistence.dao.IMapaAlocacaoFotoDao;


/**
 * 
 * A classe MapaAlocacaoFotoDao proporciona as funcionalidades de acesso ao
 * banco de dados para a entidade MapaAlocacaoFoto.
 * 
 * @since 17/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class MapaAlocacaoFotoDao extends AbstractDao<Long, MapaAlocacaoFoto>
        implements IMapaAlocacaoFotoDao {

    /** Instancia do EntityManager do Hibernate. */
    private EntityManager entityManager;

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public MapaAlocacaoFotoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MapaAlocacaoFoto.class);

        entityManager = factory.createEntityManager();
    }

    /**
     * Atualiza o status dos registros as tabelas Foto.
     * 
     * @return código do resultado da execução da atualização
     */
    @SuppressWarnings("unchecked")
    public Integer updateStatusFotos() {
        Object result = null;
        try {
            Query query = entityManager.createNamedQuery(MapaAlocacaoFoto.UPDATE_STATUS_FOTOS);
            
            List listResult = query.getResultList();
            
            if (listResult != null) {
                result = ((BigDecimal) listResult.get(0)).intValue();
            }
            
            if (result == null) {
                result = Integer.valueOf(0);
            }
        } catch (HibernateException e) {
            result = Integer.valueOf(0);
            e.printStackTrace();
        } catch (Exception e) {
            result = Integer.valueOf(0);
            e.printStackTrace();
        }
        
        return Integer.valueOf(result.toString());
    }

}