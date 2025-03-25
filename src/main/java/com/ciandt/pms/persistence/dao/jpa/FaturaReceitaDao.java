package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.persistence.dao.IFaturaReceitaDao;


/**
 * 
 * A classe FaturaReceitaDao proporciona as funcionalidades de persistencia
 * referente a entidade FaturaReceita.
 *
 * @since 19/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class FaturaReceitaDao extends 
        AbstractDao<Long, FaturaReceita> implements IFaturaReceitaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public FaturaReceitaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, FaturaReceita.class);
    }
    
    /**
     * Pega o total da receita associada com a fatura,
     * referente a ReceitaDealFiscal pasado por parametro.
     * 
     * @param rdf - ReceitaDealFiscal em questão
     * 
     * @return retorna um double com o valor total associado da fatura
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getTotalFaturaByReceitaDeal(
            final ReceitaDealFiscal rdf) {
        
        List total = getJpaTemplate().findByNamedQuery(
                FaturaReceita.GET_TOTAL_FATURA_BY_RECEITA_DEAL,
                new Object[] {rdf.getCodigoReceitaDfiscal()});

        //verifica se a lista esta vazia
        //a segunda coição é necessária pois a lista 
        //pode conter com um unico elemento nulo
        if (total.isEmpty() || total.get(0) == null) {
            return BigDecimal.valueOf(0);
        }
        
        return (BigDecimal) total.get(0);
    }
    
    /**
     * Pega o total associado pela fatura.
     * 
     * @param fatura - Fatura em questão
     * 
     * @return retorna um double com o valor total associado da fatura
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getTotalByFatura(
            final Fatura fatura) {
        
        List total = getJpaTemplate().findByNamedQuery(
                FaturaReceita.GET_TOTAL_BY_FATURA,
                new Object[] {fatura.getCodigoFatura()});

        //verifica se a lista esta vazia
        //a segunda coição é necessária pois a lista
        //pode conter com um unico elemento nulo
        if (total.isEmpty() || total.get(0) == null) {
            return BigDecimal.valueOf(0);
        }
        
        return (BigDecimal) total.get(0);
    }
    
    /**
     * Pega o total da receita associada com a fatura,
     * referente a ReceitaDealFiscal pasado por parametro.
     * 
     * @param rdf - ReceitaDealFiscal en questão
     * 
     * @return retorna um double com o valor total associado da fatura
     */
    @SuppressWarnings("unchecked")
    public List<FaturaReceita> findByReceitaDealFiscal(
            final ReceitaDealFiscal rdf) {
        
        List<FaturaReceita> list = getJpaTemplate().findByNamedQuery(
                FaturaReceita.FIND_BY_RECEITA_DEAL_FISCAL,
                new Object[] {rdf.getCodigoReceitaDfiscal()});

        return list;
    }
    
    /**
     * Pega o saldo restande de uma fatura.
     * 
     * @param fatura - Fatura em questão
     * 
     * @return retorna um double com o valor do saldo.
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getSaldoByFatura(final Fatura fatura) {
        
        List total = getJpaTemplate().findByNamedQuery(
                FaturaReceita.GET_SALDO_BY_FATURA,
                new Object[] {fatura.getCodigoFatura()});

        if (total.isEmpty()) {
            return BigDecimal.valueOf(0);
        }
        
        return (BigDecimal) total.get(0);
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IFaturaReceitaDao#getTotalFaturaByDealFiscal
	 * (com.ciandt.pms.model.DealFiscal, java.util.Date)
	 */
    @SuppressWarnings("unchecked")
	public BigDecimal getTotalFaturaByDealFiscal(final DealFiscal dealFiscal,
			final Date dataFim) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoDealFiscal", dealFiscal.getCodigoDealFiscal());
		params.put("dataFim", dataFim);
		
		List<FaturaReceita> faturaReceitaList = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						FaturaReceita.GET_TOTAL_FATURA_BY_DEAL_FISCAL, params);
		
		BigDecimal total = new BigDecimal(0d);
		
		if (faturaReceitaList != null) {
			for (FaturaReceita fr : faturaReceitaList) {
				
				if (fr.getValorReceitaAssociada() != null) {
					total = total.add(fr.getValorReceitaAssociada());
				}
			}
		}

		return total;
	}

}
