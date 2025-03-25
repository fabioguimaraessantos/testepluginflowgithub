package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FatorReajuste;
import com.ciandt.pms.model.TipoContrato;
import com.ciandt.pms.persistence.dao.IFatorReajusteDao;


/**
 * 
 * A classe FatorReajusteDao proporciona as funcionalidades 
 * da camada de persistencia referente a entidade FatorReajuste.
 *
 * @since 25/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class FatorReajusteDao 
    extends AbstractDao<Long, FatorReajuste> implements IFatorReajusteDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public FatorReajusteDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, FatorReajuste.class);
    }
    
    /**
     * Busca por todos os FatorRajuste associados a 
     * um tipo de contrato.
     * 
     * @param tipo - entidade do tipo TipoContrato.
     * 
     * @return uma lista de FatorRajuste.
     */
    @SuppressWarnings("unchecked")
    public List<FatorReajuste> findByTipoContrato(final TipoContrato tipo) {

        List<FatorReajuste> listResult = getJpaTemplate().findByNamedQuery(
                FatorReajuste.FIND_BY_TIPO_CONTRATO,
                new Object[] {tipo.getCodigoTipoContrato()});

        return listResult;
    }
    
    /**
     * Busca pelo proximo fator de reajuste, referente a 
     * data de inicio.
     * 
     * @param fatorReajuste - entidade do tipo FatorReajuste.
     * 
     * @return retorna o proximo FatorRajuste.
     */
    @SuppressWarnings("unchecked")
    public FatorReajuste findNext(final FatorReajuste fatorReajuste) {

        Long codigoTipoContrato = fatorReajuste.
            getTipoContrato().getCodigoTipoContrato();
        
        List<FatorReajuste> listResult = getJpaTemplate().findByNamedQuery(
                FatorReajuste.FIND_NEXT,
                new Object[] {codigoTipoContrato, codigoTipoContrato,
                    fatorReajuste.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo fator de reajuste anterior, referente a 
     * data de inicio.
     * 
     * @param fatorReajuste - entidade do tipo FatorReajuste.
     * 
     * @return retorna o FatorRajuste anterior.
     */
    @SuppressWarnings("unchecked")
    public FatorReajuste findPrevious(final FatorReajuste fatorReajuste) {

        Long codigoTipoContrato = fatorReajuste.
            getTipoContrato().getCodigoTipoContrato();
        
        List<FatorReajuste> listResult = getJpaTemplate().findByNamedQuery(
                FatorReajuste.FIND_PREVIOUS,
                new Object[] {codigoTipoContrato, codigoTipoContrato,
                    fatorReajuste.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo fator de reajuste referente a 
     * data de inicio.
     * 
     * @param fatorReajuste - entidade do tipo FatorReajuste.
     * 
     * @return retorna o FatorRajuste anterior.
     */
    @SuppressWarnings("unchecked")
    public FatorReajuste findByStartDate(final FatorReajuste fatorReajuste) {

        List<FatorReajuste> listResult = getJpaTemplate().findByNamedQuery(
                FatorReajuste.FIND_BY_START_DATE,
                new Object[] {fatorReajuste.getDataInicio(),
                        fatorReajuste.getTipoContrato().getCodigoTipoContrato()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca os reajuste entre periodos de data.
     * 
     * @param tipoContrato - TipoContrato.
     * 
     * @param startDate - data inicial.
     * 
     * @param endDate - data fim.
     * 
     * @return retorna uma lista de FatorRajuste.
     */
    @SuppressWarnings("unchecked")
    public List<FatorReajuste> findByPeriod(final TipoContrato tipoContrato,
            final Date startDate, final Date endDate) {

        List<FatorReajuste> listResult = getJpaTemplate().findByNamedQuery(
                FatorReajuste.FIND_BY_PERIOD, new Object[] {
                        tipoContrato.getCodigoTipoContrato(),
                        startDate, endDate});
        
        return listResult;
    }

}
