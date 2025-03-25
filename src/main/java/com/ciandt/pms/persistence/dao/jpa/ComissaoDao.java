package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Comissao;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IComissaoDao;


/**
 * 
 * A classe ComissaoDao proporciona as funcionalidades de ... para ...
 *
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class ComissaoDao extends 
    AbstractDao<Long, Comissao> implements IComissaoDao {

    /**
     * Construtor padrão.
     *  
     * @param factory - fabrica do entity manager
     */
    @Autowired
    public ComissaoDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, Comissao.class);
    }

    /**
     * Busca pelo filtro.
     * 
     * @param startDate - data inicial
     * @param endDate - data final
     * @param ae - AE da comissao
     * @param dn - DN aprovador da comissão
     * @param status - status da comissão
     * 
     * @return retorna uma lista de ComissaoAcelerador
     */
    @SuppressWarnings("unchecked")
    public List<Comissao> findByFilterDp(
            final Date startDate, final Date endDate, 
            final Pessoa ae, final Pessoa dn, final String status) {
        
        Long codigoAe = Long.valueOf(0);
        if (ae != null) {
            codigoAe = ae.getCodigoPessoa();
        }
        Long codigoDn = Long.valueOf(0);
        if (dn != null) {
            codigoDn = ae.getCodigoPessoa();
        }
        
        List<Comissao> listResult = getJpaTemplate()
                .findByNamedQuery(Comissao.FIND_BY_FILTER_DP,
                        new Object[] {startDate, endDate, codigoAe, codigoDn, status});
        
        return listResult;
    }
   

}
