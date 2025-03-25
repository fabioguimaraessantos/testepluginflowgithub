package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteStatus;
import com.ciandt.pms.persistence.dao.IFichaReajusteDao;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Alisson Fernando da Silva</a>
 */
@Repository
public class FichaReajusteDao extends AbstractDao<Long, FichaReajuste>
        implements IFichaReajusteDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public FichaReajusteDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, FichaReajuste.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<FichaReajuste> findAll() {
        List<FichaReajuste> listResult = getJpaTemplate().findByNamedQuery(
        		FichaReajuste.FIND_ALL);

        return listResult;
    }
    
	/**
	 * Retorna Fichas de Reajuste com status igual a {@code status}.
	 * 
	 * @return list<FichaReajusteStatus>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<FichaReajuste> findByFichaReajusteStatus(
			FichaReajusteStatus status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fichaReajusteStatus", status);

		List<FichaReajuste> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(FichaReajuste.FIND_BY_STATUS,
						params);

		return results;
	}

    /**
     * Retorna uma {@link FichaReajuste} com seu nome igual a {@code nomeFichaReajuste}.
     *
     * @param nomeFichaReajuste
     * @return {@link FichaReajuste}
     */
    @Override
    @SuppressWarnings("unchecked")
    public FichaReajuste findByNomeFichaReajuste(String nomeFichaReajuste) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("nomeFichaReajuste", nomeFichaReajuste);
    	
		List<FichaReajuste> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						FichaReajuste.FIND_BY_NOME_FICHA_REAJUSTE, params);
    	
    	if (results.isEmpty()) {
    		return null;
    	}
    	
    	return results.get(0);
	}

    /**
     * Retorna Fichas de Reajuste que estão em {@code documentosLegais}.
     *
     * @param idsFichaReajuste
     * @return List<FichaReajuste>.
     */
    @Override
    @SuppressWarnings("unchecked")
	public List<FichaReajuste> findByDocumentosLegais(
			List<Long> idsFichaReajuste) {
		Map<String, List<Long>> params = new HashMap<String, List<Long>>();
		params.put("idsFichaReajuste", idsFichaReajuste);

		List<FichaReajuste> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						FichaReajuste.FIND_BY_DOCUMENTOS_LEGAIS, params);

		return results;
    }
}