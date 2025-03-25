/**
 * Classe FaturaService - Implementação do serviço da Fatura
 */
package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IComissaoFaturaService;
import com.ciandt.pms.business.service.IFaturaApagadaService;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.business.service.IHistoricoFaturaService;
import com.ciandt.pms.business.service.IItemFaturaApagadoService;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.ItemFaturaApagado;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.vo.FaturaRow;
import com.ciandt.pms.persistence.dao.IFaturaApagadaDao;

/**
 * A classe FaturaApagadaService proporciona as funcionalidades de serviço para a
 * entidade FaturaApagada.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Service
public class FaturaApagadaService implements IFaturaApagadaService {

	@Autowired
	private IFaturaApagadaDao dao;
	
	@Autowired
	private IFaturaService faturaService;

	/** Instancia do servico HistoricoFatura. */
	@Autowired
	private IHistoricoFaturaService hiFaService;

	@Autowired
	private IItemFaturaApagadoService itemFaturaApagadoService;

	@Autowired
	private IComissaoFaturaService comissaoFaturaService;

	@Override
	public void create(FaturaApagada faturaApagada) {
		dao.create(faturaApagada);
	}

	@Override
	public void update(FaturaApagada faturaApagada) {
		dao.update(faturaApagada);
	}

	@Override
	public FaturaApagada findById(Long faturaApagadaId) {
		return dao.findById(faturaApagadaId);
	}

	/**
	 * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente
	 * as entidades relacionadas a esta.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * @param dataPrevisaoBeg
	 *            dataPrevisao inicial.
	 * @param dataPrevisaoEnd
	 *            dataPrevisao final.
	 * @param cli
	 *            entidade cliente.
	 * @param msa
	 *            entidade msa.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<FaturaRow> findFaturaByFilter(final Fatura filter,
			final Cliente cli, final Msa msa, final Date dataPrevisaoBeg,
			final Date dataPrevisaoEnd) {

		List<FaturaApagada> faturasApagadas = new ArrayList<FaturaApagada>();

		if (filter.getCodigoFatura() == null) {
			faturasApagadas = dao.findByFilter(filter, cli, msa,
					dataPrevisaoBeg, dataPrevisaoEnd);
		} else {
			FaturaApagada faturaApagada = dao.findById(filter.getCodigoFatura());
			if (faturaApagada != null) {
				faturasApagadas.add(faturaApagada);
			}
		}

		List<FaturaRow> faturaRowList = new ArrayList<FaturaRow>();
		int rowCont = 0;

		for (FaturaApagada faturaApagada : faturasApagadas) {
			List<ItemFaturaApagado> itemFaturaList = faturaApagada.getItemFaturaApagados();
			double totalFatura = Double.valueOf(0);

			// Flag para identificar se a fatura esta inteiramente comissionada.
			// Se tudo comissioned = YES
			boolean commission = Boolean.valueOf(true);

			for (ItemFaturaApagado itemFaturaApagado : itemFaturaList) {
				totalFatura += itemFaturaApagado.getValorItem().doubleValue();
				itemFaturaApagado.setStatusItem(this
						.statusItemFatura(itemFaturaApagado));
				itemFaturaApagado.setStatusComissao(this
						.statusItemComissao(itemFaturaApagado));

				// se existir algum item que não for "Comisssioned" setta como
				// false. Se houver um item nao comssioned = NO
				if (!(this.statusItemComissao(itemFaturaApagado)
						.equals(Constants.COMISSAO_STATUS_COMISSIONED))) {
					commission = Boolean.valueOf(false);
				}
			}

			FaturaRow faturaRow = new FaturaRow(faturaApagada, rowCont++);
			faturaRow.setTotalFatura(BigDecimal.valueOf(totalFatura));
			faturaRow.setPatternCurrency(this.getCurrency(faturaApagada.getMoeda()));
			faturaRow.setStatusPagamento(this.status(faturaApagada));
			faturaRow.setIsCommission(commission);

			faturaRowList.add(faturaRow);
		}

		return faturaRowList;
	}

	/**
	 * Status de pagamento.
	 * 
	 * @param faturaApagada
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return string de status.
	 */
	private String status(final FaturaApagada faturaApagada) {
		List<ItemFaturaApagado> lista = itemFaturaApagadoService.findByFaturaApagada(faturaApagada);
		int cont = 0;

		if (lista == null) {
			return Constants.FATURA_STATUS_NOTPAID;
		}
		for (ItemFaturaApagado itemFaturaApagado : lista) {
			if (itemFaturaApagado.getDataPagamento() != null) {
				cont++;
			}
		}
		if (cont > 0 && cont < lista.size()) {
			return Constants.FATURA_STATUS_PARTLYPAG;
		} else if (cont == 0) {
			return Constants.FATURA_STATUS_NOTPAID;
		} else {
			return Constants.FATURA_STATUS_PAID;
		}
	}


	/**
	 * 
	 * @param item
	 *            item de itemFatura
	 * @return status do itemFatura
	 */
	private String statusItemFatura(final ItemFaturaApagado item) {
		String status = "";
		if (item.getDataPagamento() == null) {
			status = Constants.FATURA_STATUS_NOTPAID;
		}

		return status;
	}

	/**
	 * Retorna o status da comissao.
	 * 
	 * @param item
	 *            itemFatura
	 * @return status do item de fatura
	 */
	private String statusItemComissao(final ItemFaturaApagado item) {
		List<ComissaoFatura> comissaoFaturas = new ArrayList<ComissaoFatura>();
		comissaoFaturas = comissaoFaturaService.findByCodigoItemFatura(item.getCodigoItemFatura());
		
		if (comissaoFaturas != null
				&& comissaoFaturas.size() != 0) {
			return comissaoFaturas.get(0).getComissao()
					.getIndicadorEstadoAtual();
		} else {
			return Constants.COMISSAO_STATUS_OPEN;
		}
	}

	/**
	 * Retorna o status da comissao.
	 * 
	 * @param item
	 *            itemFatura
	 * @return status do item de fatura
	 */
	private String statusItemComissao(final ItemFatura item) {
		if (item.getComissaoFaturas() != null
				&& item.getComissaoFaturas().size() != 0) {
			return item.getComissaoFaturas().get(0).getComissao()
					.getIndicadorEstadoAtual();
		} else {
			return Constants.COMISSAO_STATUS_OPEN;
		}
	}

	/**
	 * Recuprea a moeda da Fatura corrente.
	 * 
	 * @param moeda
	 *            - Moeda da Fatura corrente
	 * @return o pattern da Moeda.
	 */
	private String getCurrency(final Moeda moeda) {
		if (moeda != null) {
			// atribui o pattern dos valores conforme sigla da moeda
			return moeda.getSiglaMoeda();
		}
		return "";
	}
}