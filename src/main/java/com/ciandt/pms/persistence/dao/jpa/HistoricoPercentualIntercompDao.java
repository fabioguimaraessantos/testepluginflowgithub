package com.ciandt.pms.persistence.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.HistoricoPercentualIntercomp;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IControleReajusteDao;
import com.ciandt.pms.persistence.dao.IHistoricoPercentualIntercompDao;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @since 11/12/2015
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Repository
public class HistoricoPercentualIntercompDao extends AbstractDao<Long, HistoricoPercentualIntercomp>
        implements IHistoricoPercentualIntercompDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public HistoricoPercentualIntercompDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, HistoricoPercentualIntercomp.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<HistoricoPercentualIntercomp> findAll() {
        List<HistoricoPercentualIntercomp> listResult = getJpaTemplate().findByNamedQuery(
        		HistoricoPercentualIntercomp.FIND_ALL);

        return listResult;
    }


    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<HistoricoPercentualIntercomp> findByDealFiscal(final Long dealFiscalCode) {
        List<HistoricoPercentualIntercomp> listResult = getJpaTemplate().findByNamedQuery(HistoricoPercentualIntercomp.FIND_BY_DEAL_FISCAL,new Object[]{dealFiscalCode});

        return listResult;
    }
}