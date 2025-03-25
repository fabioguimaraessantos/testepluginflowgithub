package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.TipoContratoEncargo;
import com.ciandt.pms.persistence.dao.ITipoContratoEncargoDao;


/**
 * 
 * A classe TipoContratoEncargoDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade TipoContratoEncargo.
 * 
 * @since 01/06/2011
 * @author cmantovani
 * 
 */
@Repository
public class TipoContratoEncargoDao extends
        AbstractDao<Long, TipoContratoEncargo> implements
        ITipoContratoEncargoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public TipoContratoEncargoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TipoContratoEncargo.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TipoContratoEncargo> findAll() {
        List<TipoContratoEncargo> listResult = getJpaTemplate()
                .findByNamedQuery(TipoContratoEncargo.FIND_ALL);

        return listResult;
    }

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param tipoContratoEncargo
     *            - entidade do tipo TipoContratoEncargo.
     * 
     * @return retorna o proximo TipoContratoEncargo.
     */
    @SuppressWarnings("unchecked")
    public TipoContratoEncargo findNext(
            final TipoContratoEncargo tipoContratoEncargo) {

        Long codigo = tipoContratoEncargo.getTipoContrato()
                .getCodigoTipoContrato();

        List<TipoContratoEncargo> listResult = getJpaTemplate()
                .findByNamedQuery(
                        TipoContratoEncargo.FIND_NEXT,
                        new Object[] {codigo, codigo,
                                tipoContratoEncargo.getDataInicioVigencia() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param tipoContratoEncargo
     *            - entidade do tipo TipoContratoEncargo.
     * 
     * @return retorna o TipoContratoEncargo anterior.
     */
    @SuppressWarnings("unchecked")
    public TipoContratoEncargo findPrevious(
            final TipoContratoEncargo tipoContratoEncargo) {

        Long codigo = tipoContratoEncargo.getTipoContrato()
                .getCodigoTipoContrato();

        List<TipoContratoEncargo> listResult = getJpaTemplate()
                .findByNamedQuery(
                        TipoContratoEncargo.FIND_PREVIOUS,
                        new Object[] {codigo, codigo,
                                tipoContratoEncargo.getDataInicioVigencia() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca pelo tipoContratoEncargo referente a data de inicio.
     * 
     * @param tipoContratoEncargo
     *            - entidade do tipo TipoContratoEncargo.
     * 
     * @return retorna o TipoContratoEncargo.
     */
    @SuppressWarnings("unchecked")
    public TipoContratoEncargo findByStartDate(
            final TipoContratoEncargo tipoContratoEncargo) {

        List<TipoContratoEncargo> listResult = getJpaTemplate()
                .findByNamedQuery(
                        TipoContratoEncargo.FIND_BY_START_DATE,
                        new Object[] {
                                tipoContratoEncargo.getDataInicioVigencia(),
                                tipoContratoEncargo.getTipoContrato()
                                        .getCodigoTipoContrato() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}