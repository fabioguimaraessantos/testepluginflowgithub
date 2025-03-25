package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IDreProcessoService;
import com.ciandt.pms.business.service.IProcessoService;
import com.ciandt.pms.enums.StatusDreProcesso;
import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.Processo;
import com.ciandt.pms.model.ProcessoDependencia;
import com.ciandt.pms.model.vo.ProcessoRow;
import com.ciandt.pms.persistence.dao.IProcessoDao;

/**
 * A classe ProcessoService proporciona as funcionalidades da camada de serviço
 * referente a entidade Processo.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Service
public class ProcessoService implements IProcessoService {

	/**
	 * Instancia do DAO da entidade.
	 */
	@Autowired
	private IProcessoDao dao;

	/**
	 * Instancia do serviço DreProcesso
	 */
	@Autowired
	private IDreProcessoService dreProcessoService;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createProcesso(Processo entity) {
		dao.create(entity);
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 */
	public void updateProcesso(Processo entity) {
		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 */
	public void removeProcesso(Processo entity) {
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
	public Processo findProcessoById(Long id) {
		return dao.findById(id);
	}

	/**
	 * Busca os processos por indicador
	 * 
	 * @param indicador
	 *            "A" para Ativo, "I" para Inativo
	 * 
	 */
	public List<Processo> findAllByIndicadorAtivo(final String indicador) {
		return dao.findAllByIndicadorAtivo(indicador);
	}

	/**
	 * Constroi um objeto do tipo {@link ProcessoRow} dado um {@link Processo} e
	 * uma data base.
	 * 
	 * @param processo
	 *            processo base
	 * @param baseDate
	 *            data base
	 * @return {@link ProcessoRow} para ser manipulado na tela
	 */
	private ProcessoRow buildProcessoRowByProcessoAndBaseDate(
			final Processo processo, final Date baseDate) {
		ProcessoRow row = new ProcessoRow();
		DreProcesso dreProcesso = null;

		row.setMonthYear(baseDate);
		row.setCodigoProcesso(processo.getCodigoProcesso());
		row.setNomeProcesso(processo.getNomeProcesso());

		// Busca o ultimo processo (na tabela DRE_PROCESSO) por código do
		// processo, data (MM/yyyy) e indicadorPorLogin
		dreProcesso = dreProcessoService.findLastByProcessoDataAndIndPorLogin(
				processo.getCodigoProcesso(), baseDate, Constants.NO);

		// Se já possuir DRE_PROCESSO, seta o status salvo no banco
		if (dreProcesso != null) {
			row.setStatusEnum(StatusDreProcesso.getByAbbreviation(dreProcesso
					.getIndicadorStatus()));

			// Se status for invalidado seta os dependentes
			if (StatusDreProcesso.INVALIDATE.equals(row.getStatusEnum())) {
				row.setProcessosDependentes(new ArrayList<ProcessoDependencia>(
						processo.getProcessoDependenciasForProcCdProcessoDependente()));
			}

			// Se status for In Progress, seta locked para true
			if (StatusDreProcesso.IN_PROGRESS.equals(row.getStatusEnum())) {
				row.setLocked(Boolean.valueOf(true));
			}

		} else {
			// Se não possuir DRE_PROCESSO, seta o status como LOCKED ou NOT
			// PERFORMED
			dreProcesso = new DreProcesso();
			dreProcesso.setProcesso(processo);
			DreMes dreMes = new DreMes();
			dreMes.setDataMes(baseDate);
			dreProcesso.setDreMes(dreMes);

			// Se o processo depender de outro processo para ser executado, seta
			// o processo como LOCKED
			if (processo.getProcessoDependenciasForProcCdProcessoDependente() != null
					&& processo
							.getProcessoDependenciasForProcCdProcessoDependente()
							.size() > 0) {

				row.setStatusEnum(StatusDreProcesso.LOCKED);
				row.setProcessosDependentes(new ArrayList<ProcessoDependencia>(
						processo.getProcessoDependenciasForProcCdProcessoDependente()));
			} else {
				// Se o processo NAO depender de outro processo para ser
				// executado, seta
				// o como processo como NOT_PERFORMED
				row.setStatusEnum(StatusDreProcesso.NOT_PERFORMED);
			}
		}
		row.setDreProcesso(dreProcesso);
		return row;
	}

	/**
	 * Busca os processos ativos e seus status por MM/yyyy para montar na tela.
	 * 
	 * @param monthYear
	 *            Date
	 * 
	 * @return {@link ProcessoRow}
	 */
	public List<ProcessoRow> findProcessoByDate(final Date monthYear) {

		List<ProcessoRow> processoRows = new ArrayList<ProcessoRow>();
		Map<Long, StatusDreProcesso> mapProcessos = new HashMap<Long, StatusDreProcesso>();
		String statusProcesso = null;
		String statusDependencia = null;
		boolean isAllPerformed = true;

		// Busca os processos ativos na tabela PROCESSO
		List<Processo> processos = dao
				.findAllByIndicadorAtivo(Constants.ACTIVE);

		for (Processo processo : processos) {
			// Cria um ProcessoRow com valores apropriados
			ProcessoRow row = this.buildProcessoRowByProcessoAndBaseDate(
					processo, monthYear);

			// Adiciona no map o status de cada processo para posterior
			// tratamento dos status dos processos que possuem dependentes
			mapProcessos.put(row.getCodigoProcesso(), row.getStatusEnum());

			// Adiciona na lista, os processos que serão mostrados na tela
			processoRows.add(row);
		}

		// Trata o status e a flag locked dos processos que possuem dependência
		for (ProcessoRow processoRow : processoRows) {

			if (processoRow.getProcessosDependentes() != null
					&& !processoRow.getProcessosDependentes().isEmpty()) {

				isAllPerformed = true;
				statusProcesso = mapProcessos.get(
						processoRow.getCodigoProcesso()).getAbbreviation();

				for (ProcessoDependencia processoDependencia : processoRow
						.getProcessosDependentes()) {

					statusDependencia = mapProcessos.get(
							Long.valueOf(processoDependencia
									.getProcessoByProcCdProcessoDependencia()))
							.getAbbreviation();

					// Se o processo tiver alguma dependencia com outro processo
					// que esteja com status diferente de Concluido ou status
					// igual a invalidate, entao seta a flag locked para true,
					// pois o processo não poderá ser executado
					if (!StatusDreProcesso.PERFORMED.getAbbreviation().equals(
							statusDependencia)
							|| StatusDreProcesso.INVALIDATE.getAbbreviation()
									.equals(statusDependencia)) {
						processoRow.setLocked(Boolean.valueOf(true));
						isAllPerformed = false;
						break;
					}
				}

				// Se todos os processos em que possui dependência tiver com
				// status Performed e não esteja com status Invalidate, então
				// muda o status de locked para not performed pois o processo
				// poderá ser executado
				if (isAllPerformed
						&& !StatusDreProcesso.INVALIDATE.getAbbreviation()
								.equals(statusProcesso)) {
					processoRow.setStatusEnum(StatusDreProcesso.NOT_PERFORMED);
				}

			}
		}
		return processoRows;
	}

	/**
	 * Invalida todos os processos dependentes de {@code processo}
	 * recursivamente.
	 * 
	 * <b>Warning:</b> Método recursivo.
	 * 
	 * @param dreMes
	 *            {@link DreMes}
	 * 
	 * @param processo
	 *            {@link Processo}
	 */
	@Override
	@Transactional
	public void invalidateDependingProcessoCascade(final DreMes dreMes,
			final Processo processo) {
		for (Processo p : processo.getDependentes()) {
			DreProcesso d = dreProcessoService
					.findLastByProcessoDataAndIndPorLogin(
							p.getCodigoProcesso(), dreMes.getDataMes(),
							Constants.NO);
			if (d != null) {
				d.setIndicadorStatus(StatusDreProcesso.INVALIDATE
						.getAbbreviation());
				dreProcessoService.updateDreProcesso(d);
				this.invalidateDependingProcessoCascade(dreMes, d.getProcesso());
			}
		}
	}
}
