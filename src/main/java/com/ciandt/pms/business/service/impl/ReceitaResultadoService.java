package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IReceitaResultadoService;
import com.ciandt.pms.model.ReceitaResultado;
import com.ciandt.pms.persistence.dao.IReceitaResultadoDao;

/**
 * 
 * A classe IReceitaResultadoService proporciona a interface de acesso para a
 * camada de serviço referente a entidade {@link ReceitaResultado}.
 * 
 * @since 12/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public class ReceitaResultadoService implements IReceitaResultadoService {

	/** Instancia do DAO. */
	@Autowired
	private IReceitaResultadoDao dao;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Override
	@Transactional
	public void createReceitaResultado(final ReceitaResultado entity) {
		dao.create(entity);
	}

	/**
	 * Atualiza a entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 * 
	 */
	@Override
	@Transactional
	public void updateReceitaResultado(final ReceitaResultado entity) {
		dao.update(entity);
	}

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 */
	@Override
	@Transactional
	public void removeReceitaResultado(final ReceitaResultado entity) {
		dao.remove(entity);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<ReceitaResultado> findReceitaResultadoAll() {
		return dao.findAll();
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public ReceitaResultado findReceitaResultadoById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna uma entidade.
	 * 
	 * @param entity
	 *            filtro {@link ReceitaResultado}
	 * 
	 * @return retorna uma entidade referente ao filtro passado.
	 */
	@Override
	public ReceitaResultado findReceitaResultadoByContratoAndMoedaAndDate(
			final ReceitaResultado entity) {
		return dao.findByContratoAndMoedaAndDate(entity);
	}

	/**
	 * Cria a foto da Receita a partir da lista recebida.
	 * 
	 * @param receitaResultadoList
	 *            recebe uma lista a ser persistida
	 */
	@Transactional
	public void snapshotReceitaResultado(
			final List<ReceitaResultado> receitaResultadoList) {
		for (ReceitaResultado rs : receitaResultadoList) {
			dao.create(rs);
		}
	}

	/**
	 * Mantem o registro da {@link ReceitaResultado} no banco porem, apaga o
	 * conteudo inserido pelo usuario referente a justificativa da receita. O
	 * registro volta a ficar como vindo da foto. Este caso e util quando uma
	 * receita é excluida e suas justificativas devem ser apagadas (mantendo a
	 * foto).
	 * 
	 * @param receitaResultado
	 *            registro a ser manipulado
	 */
	@Transactional
	public void removeShortTermRevenue(final ReceitaResultado receitaResultado) {
		receitaResultado.setTextoObservacao(null);
		receitaResultado.setValorReceitaRealizada(null);
		receitaResultado.setMotivoResultado(null);
		this.updateReceitaResultado(receitaResultado);
	}

}