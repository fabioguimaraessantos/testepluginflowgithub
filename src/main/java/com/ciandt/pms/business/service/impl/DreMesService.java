package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IDreMesService;
import com.ciandt.pms.business.service.IDreProcessoService;
import com.ciandt.pms.business.service.IProcessoService;
import com.ciandt.pms.enums.StatusDreProcesso;
import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.Processo;
import com.ciandt.pms.persistence.dao.IDreMesDao;

/**
 * A classe DreMesService proporciona as funcionalidades da camada de serviço
 * referente a entidade DreMes.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Service
public class DreMesService implements IDreMesService {

	/**
	 * Instancia do DAO da entidade.
	 */
	@Autowired
	private IDreMesDao dao;

	/**
	 * Instancia do servico DreProcessoService.
	 */
	@Autowired
	private IDreProcessoService dreProcessoService;

	/**
	 * Instancia do servico ProcessoService.
	 */
	@Autowired
	private IProcessoService processoService;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	public void createDreMes(DreMes entity) {
		dao.create(entity);
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 */
	@Transactional
	public void updateDreMes(DreMes entity) {
		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 */
	@Transactional
	public void removeDreMes(DreMes entity) {
		dao.remove(entity);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public DreMes findDreMesById(Long id) {
		return dao.findById(id);
	}

	/**
	 * Realiza a busca de um {@link DreMes} por uma data base.
	 * 
	 * @param dataMes
	 *            data base para busca da entidade.
	 * @return {@link DreMes}
	 */
	public DreMes findDreMesByDataMes(final Date dataMes) {
		return dao.findByDataMes(dataMes);
	}

	/**
	 * Verifica se todos os processos foram executados para um MM/yyyy.
	 * 
	 * @param dreMes
	 *            DreMes.
	 * @return {@link boolean}
	 */
	public boolean isDreMesCompleted(final DreMes dreMes) {
		// Busca todos os processos ativos na tabela PROCESSO
		List<Processo> processos = processoService
				.findAllByIndicadorAtivo(Constants.ACTIVE);

		for (Processo processo : processos) {

			// Busca o ultimo processo executado para um MM/yyyy
			DreProcesso lastDreProcesso = dreProcessoService
					.findLastByProcessoDataAndIndPorLogin(
							processo.getCodigoProcesso(), dreMes.getDataMes(),
							Constants.NO);

			if (lastDreProcesso == null
					|| !StatusDreProcesso.PERFORMED.getAbbreviation().equals(
							lastDreProcesso.getIndicadorStatus())) {
				return false;
			}
		}

		return true;
	}
}
