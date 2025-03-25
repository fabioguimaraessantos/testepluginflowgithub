package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteStatus;
import com.ciandt.pms.persistence.dao.IControleReajusteStatusDao;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Alisson Fernando da Silva</a>
 */
@Repository
public class ControleReajusteStatusDao extends AbstractDao<Long, ControleReajusteStatus>
        implements IControleReajusteStatusDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public ControleReajusteStatusDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ControleReajusteStatus.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ControleReajusteStatus> findAll() {
        List<ControleReajusteStatus> listResult = getJpaTemplate().findByNamedQuery(
        		ControleReajusteStatus.FIND_ALL);

        return listResult;
    }

    /**
     * Retorna todos os status de {@link ControleReajuste} ativos.
     *
     * @return Lista de {@link ControleReajusteStatus}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ControleReajusteStatus> findAllActive() {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("indicadorAtivo", Constants.ACTIVE);
    	
		List<ControleReajusteStatus> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ControleReajusteStatus.FIND_ALL_ACTIVE, params);
    	
    	if (results.isEmpty()) {
    		return null;
    	}
    	
    	return results;
    }
    
    /**
     * Retorna o status de {@link ControleReajuste} em que possui a sigla igual a .
     *
     * @return Lista de {@link ControleReajusteStatus}
     */
    @Override
    @SuppressWarnings("unchecked")
    public ControleReajusteStatus findBySiglaControleReajusteStatus(String siglaStatus) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("siglaControleReajStatus", siglaStatus);
    	
		List<ControleReajusteStatus> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ControleReajusteStatus.FIND_BY_SIGLA_CONTROLE_REAJ_STATUS,
						params);
    	
    	if (results.isEmpty()) {
    		return null;
    	}
    	
    	return results.get(0);
    }
}