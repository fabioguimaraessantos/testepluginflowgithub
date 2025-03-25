package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.EstratificacaoUr;
import com.ciandt.pms.persistence.dao.IEstratificacaoUrDao;


/**
 * 
 * A classe EstratificacaoUr proporciona as funcionalidades de acesso ao banco
 * para referentes a entidade EstratificacaoUr.
 * 
 * @since 15/04/2011
 * @author cmantovani
 * 
 */
@Repository
public class EstratificacaoUrDao extends AbstractDao<Long, EstratificacaoUr>
        implements IEstratificacaoUrDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica do entity manager
     */
    @Autowired
    public EstratificacaoUrDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, EstratificacaoUr.class);
    }

    /**
     * Busca pelo id da receita.
     * 
     * @param idReceita
     *            id da receita
     * 
     * @return retorna uma lista de EstratificacaoUr
     */
    @SuppressWarnings("unchecked")
    public List<EstratificacaoUr> findByIdReceita(final Long idReceita) {

        List<EstratificacaoUr> listResult = getJpaTemplate().findByNamedQuery(
                EstratificacaoUr.FIND_BY_RECEITA, new Object[] {idReceita });

        return listResult;
    }

    /**
     * Busca pelo id do MapaAlocacao.
     * 
     * @param idMapaAlocacao
     *            - id do MapaAlocacao
     * @param data
     *            - data da estratificação
     * 
     * @return retorna uma lista de EstratificacaoUr
     */
    @SuppressWarnings("unchecked")
    public EstratificacaoUr findByIdMapaAlocacaoData(final Long idMapaAlocacao,
            final Date data) {

        List<EstratificacaoUr> listResult = getJpaTemplate().findByNamedQuery(
                EstratificacaoUr.FIND_BY_MAPA_ALOCACAO_DATA,
                new Object[] {idMapaAlocacao, data });
        if (!listResult.isEmpty()) {
            return listResult.get(0);
        } else {
            return null;
        }

    }

}