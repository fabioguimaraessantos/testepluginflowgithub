package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.VwPMSProjeto;
import com.ciandt.pms.persistence.dao.IVwPMSProjetoDao;

/**
 * A classe VwPMSProjetoDao proporciona as funcionalidades de acesso ao banco
 * de dados referente a entidade {@link VwPMSProjeto}.
 * 
 * @since 04/09/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 * 
 */
@Repository
public class VwPMSProjetoDao extends AbstractDao<Long, VwPMSProjeto> implements
		IVwPMSProjetoDao {

	/**
	 * Construtor.
	 * 
	 * @param factory
	 *            facotry
	 */
	@Autowired
	public VwPMSProjetoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, VwPMSProjeto.class);
	}

	/*
	 * @see
	 * com.ciandt.pms.persistence.dao.IVwPMSProjetoDao#findByProjetoEmpresa(
	 * java.lang.Long, com.ciandt.pms.model.Empresa)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public VwPMSProjeto findByProjetoEmpresa(Long codigoProjeto, Empresa empresa) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codigoProjeto", codigoProjeto);
		map.put("codigoEmpresa", empresa.getCodigoEmpresa());
		List<VwPMSProjeto> list = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						VwPMSProjeto.FIND_BY_PROJETO_EMPRESA, map);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}