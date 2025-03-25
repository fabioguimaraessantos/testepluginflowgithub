package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Processo;

/**
 * A classe IProcessoDao proporciona a interface de acesso para a camada DAO da
 * entidade Processo.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
public interface IProcessoDao extends IAbstractDao<Long, Processo> {

	/**
	 * Busca os processos por indicador
	 * 
	 * @param indicador
	 *            "A" para Ativo, "I" para Inativo
	 * 
	 */
	public List<Processo> findAllByIndicadorAtivo(final String indicador);
}
