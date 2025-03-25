package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FichaReajusteIndice;
import com.ciandt.pms.persistence.dao.IFichaReajusteIndiceDao;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Alisson Fernando da Silva</a>
 */
@Repository
public class FichaReajusteIndiceDao extends AbstractDao<Long, FichaReajusteIndice>
        implements IFichaReajusteIndiceDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public FichaReajusteIndiceDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, FichaReajusteIndice.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<FichaReajusteIndice> findAll() {
        List<FichaReajusteIndice> listResult = getJpaTemplate().findByNamedQuery(
        		FichaReajusteIndice.FIND_ALL);

        return listResult;
    }

	/**
	 * Retorna o indice com nome igual a {@code nomeIndice}
	 *
	 * @param nome
	 * @return {@link FichaReajusteIndice}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public FichaReajusteIndice findByNome(String nomeIndice) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nomeFichaReajusteIndice", nomeIndice);

		List<FichaReajusteIndice> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						FichaReajusteIndice.FIND_BY_NOME_FICHA_REAJUSTE_INDICE,
						params);

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
	}
}