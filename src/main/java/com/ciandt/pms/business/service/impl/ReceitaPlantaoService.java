package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IReceitaDealFiscalService;
import com.ciandt.pms.business.service.IReceitaPlantaoService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaPlantao;
import com.ciandt.pms.persistence.dao.IReceitaPlantaoDao;
import com.ciandt.pms.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 * A classe ReceitaPlantaoService proporciona as funcionalidades da camada de
 * negócio referente a entidade ReceitaPlantao.
 * 
 * @since 14/07/2015
 * @author luizsj
 * 
 */
@Service
public class ReceitaPlantaoService implements IReceitaPlantaoService {

	/** Instancia do Dao da entidade ReceitaPlantao. */
	@Autowired
	private IReceitaPlantaoDao receitaPlantaoDao;

	/** Instancia do Servico. */
	@Autowired
	private IReceitaDealFiscalService receitaDealFiscalService;

	@Autowired
	private IReceitaService receitaService;

	/**
	 * Serviço de Modulo.
	 */
	@Autowired
	private IModuloService moduloService;

	@Override
	public ReceitaPlantao findById(Long id) {
		return receitaPlantaoDao.findById(id);
	}

	@Override
	public void remove(ReceitaPlantao entity) {
		receitaPlantaoDao.remove(entity);
	}

	@Override
	public void update(ReceitaPlantao entity) {
		receitaPlantaoDao.update(entity);
	}

	@Override
	public void create(ReceitaPlantao entity) {
		receitaPlantaoDao.create(entity);
	}

	@Override
	public List<ReceitaPlantao> findAll() {
		return receitaPlantaoDao.findAll();
	}

	@Override
	public ReceitaPlantao findByReceitaDealFiscal(
			ReceitaDealFiscal receitaDealFiscal) {
		return receitaPlantaoDao.findByReceitaDealFiscal(receitaDealFiscal.getCodigoReceitaDfiscal());
	}

	@Transactional
	public Boolean updateDutyHourRevenueFromMega(ReceitaPlantao receitaPlantao, String revenueStatus, BigDecimal orderID, String errorMessage) {
		if (revenueStatus.equals("A") && !receitaPlantao.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_INTEGRADO)) {
			ReceitaPlantao receita = this.findById(receitaPlantao.getCodigoReceitaPlantao());
			ReceitaDealFiscal receitaDealFiscal = receitaDealFiscalService.findReceitaDealById(receita.getReceitaDealFiscal().getCodigoReceitaDfiscal());
			receitaService.setStatusIntegratedRevenue(receitaDealFiscal, receitaDealFiscal.getCodigoErpPedido(), errorMessage);

			ReceitaPlantao rpUpdate = this.findById(receitaPlantao.getCodigoReceitaPlantao());
			rpUpdate.setIndicadorStatus(Constants.INTEGRACAO_STATUS_INTEGRADO);
			rpUpdate.setCodigoErpPedido(orderID);

			this.update(rpUpdate);
		}
		else if (revenueStatus.equals("E") && !receitaPlantao.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_INTEGRADO)){
			ReceitaPlantao receita = this.findById(receitaPlantao.getCodigoReceitaPlantao());
			ReceitaDealFiscal receitaDealFiscal = receitaDealFiscalService.findReceitaDealById(receita.getReceitaDealFiscal().getCodigoReceitaDfiscal());
			receitaService.setStatusErrorRevenue(receitaDealFiscal, receitaDealFiscal.getCodigoErpPedido(), errorMessage);
		}
		return true;
	}

	/**
	 * Validação do ClosingDateRevenue. Caso startDate (data de início da
	 * vigência) seja maior do que a data Revenue, a vigência é válida e pode
	 * ser adicionada ou removida. Caso contrário, a vigência não pode ser
	 * adicionada nem removida.
	 * 
	 * @param startDate
	 *            - data de início da vigência
	 * @param showMsgError
	 *            - mostra mensagem de erro caso startDate não seja válido
	 * 
	 * @return true se startDate for maior do que ClosingDateRevenue. false caso
	 *         contrário
	 */
	public Boolean verifyClosingDateRevenue(final Date startDate,
			final Boolean showMsgError) {
		Date closingRevenueDate = moduloService.getClosingDateRevenue();
		if (startDate.compareTo(closingRevenueDate) > 0) {
			return Boolean.valueOf(true);
		} else {
			if (showMsgError) {
				Messages.showError("verifyClosingDateRevenue",
						Constants.MSG_ERROR_AJUSTE_RECEITA_ADD_CLOS_DATE,
						DateUtil.formatDate(closingRevenueDate,
								Constants.DEFAULT_DATE_PATTERN_SIMPLE,
								Constants.DEFAULT_CALENDAR_LOCALE));
			}

			return Boolean.valueOf(false);
		}
	}

	/**
	 * Percorre todos os itens da lista e os cria ou atualiza.
	 * 
	 * @param receitaPlantaos
	 */
	@Override
	public void createOrUpdate(List<ReceitaPlantao> receitaPlantaos) {
		for (ReceitaPlantao rp : receitaPlantaos) {
			if (rp.getCodigoReceitaPlantao() != null) {

				this.update(rp);
			} else {

				this.create(rp);
			}
		}
	}
}
