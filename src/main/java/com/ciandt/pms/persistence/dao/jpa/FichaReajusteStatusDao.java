package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteStatus;
import com.ciandt.pms.persistence.dao.IFichaReajusteStatusDao;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Alisson Fernando da Silva</a>
 */
@Repository
public class FichaReajusteStatusDao extends AbstractDao<Long, FichaReajusteStatus>
        implements IFichaReajusteStatusDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public FichaReajusteStatusDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, FichaReajusteStatus.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<FichaReajusteStatus> findAll() {
        List<FichaReajusteStatus> listResult = getJpaTemplate().findByNamedQuery(
        		FichaReajusteStatus.FIND_ALL);

        return listResult;
    }

    /**
     * Busca todos os status de {@link FichaReajuste} ativos.
     *
     * @return Lista de {@link FichaReajusteStatus}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<FichaReajusteStatus> findAllActive() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("indicadorAtivo", Constants.ACTIVE);

		List<FichaReajusteStatus> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						FichaReajusteStatus.FIND_ALL_ACTIVE, params);

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult;
    }
    
    /**
     * Busca status de {@link FichaReajuste} com sigla igual a {@code siglaStatus}.
     *
     * @param siglaStatus
     * @return Lista de {@link FichaReajusteStatus}
     */
    @Override
    @SuppressWarnings("unchecked")
    public FichaReajusteStatus findBySiglaStatus(String siglaStatus) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("siglaFichaReajusteStatus", siglaStatus);
    	
		List<FichaReajusteStatus> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						FichaReajusteStatus.FIND_BY_SIGLA_FICHA_REAJUSTE_STATUS,
						params);
    	
    	if (listResult.isEmpty()) {
    		return null;
    	}
    	
    	return listResult.get(0);
    }
}