package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.EtiquetaItemReceita;
import com.ciandt.pms.model.EtiquetaItemReceitaId;
import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.persistence.dao.IEtiquetaItemReceitaDao;


/**
 * 
 * A classe EtiquetaItemReceitaDao proporciona as funcionalidades de acesso ao
 * banco de dados para a entidade EtiquetaItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class EtiquetaItemReceitaDao extends
        AbstractDao<EtiquetaItemReceitaId, EtiquetaItemReceita> implements
        IEtiquetaItemReceitaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public EtiquetaItemReceitaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, EtiquetaItemReceita.class);
    }

    /**
     * Busca um mapa de entidades pelo filtro.
     * 
     * @param codigoEtiqueta
     *            código da Etiqueta da busca.
     * @param codigoReceita
     *            código da Receita corrente.
     * 
     * @return mapa de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public Map<Long, EtiquetaItemReceita> findByEtiquetaAndReceita(
            final Long codigoEtiqueta, final Long codigoReceita) {
        if (codigoEtiqueta == null || codigoEtiqueta.longValue() <= 0
                || codigoReceita == null || codigoReceita <= 0) {
            return null;
        }

        List<EtiquetaItemReceita> listResult = getJpaTemplate()
                .findByNamedQuery(
                        EtiquetaItemReceita.FIND_BY_ETIQUETA_AND_RECEITA,
                        new Object[] {codigoEtiqueta, codigoReceita });

        Map<Long, EtiquetaItemReceita> mapResult = new HashMap<Long, EtiquetaItemReceita>();

        for (EtiquetaItemReceita etiquetaItemReceita : listResult) {
            mapResult.put(etiquetaItemReceita.getId().getCodigoItemReceita(),
                    etiquetaItemReceita);
        }

        return mapResult;
    }
    
    /**
     * Busca um mapa de entidades pelo filtro.
     * 
     * @param itemReceita
     *            ItemReceita da busca.
     * 
     * @return mapa de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<EtiquetaItemReceita> findByItemReceita(final ItemReceita itemReceita) {
        
        List<EtiquetaItemReceita> listResult = 
            getJpaTemplate().findByNamedQuery(
                        EtiquetaItemReceita.FIND_BY_ITEM_RECEITA,
                        new Object[] {itemReceita.getCodigoItemReceita()});
        
        return listResult;
        
    }

}