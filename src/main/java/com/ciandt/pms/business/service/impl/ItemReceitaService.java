package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.business.service.*;
import com.ciandt.pms.model.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.audit.listener.AuditoriaJpaListener;
import com.ciandt.pms.persistence.dao.IItemReceitaDao;

/**
 * 
 * A classe ItemReceitaService proporciona as funcionalidades de servi�o para a
 * entidade ItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class ItemReceitaService implements IItemReceitaService {

	/**
	 * Instancia do DAO da entidade.
	 */
	@Autowired
	private IItemReceitaDao itemReceitaDao;

	@Autowired
	private ITaxaImpostoService taxaImpostoService;

	@Autowired
	private ITipoServicoService tipoServicoService;

	@Autowired
	private IDealFiscalService dealFiscalService;

	@Autowired
	private ICidadeBaseService cidadeBaseService;

	@Autowired IPessoaCidadeBaseService pessoaCidadeBaseService;

	/** Intancia que realiza a auditoria (Log). */
	private AuditoriaJpaListener auditoria = new AuditoriaJpaListener();

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createItemReceita(final ItemReceita entity) {
		itemReceitaDao.create(entity);

		auditoria.postPersist(entity);
	}

	/**
	 * Atualiza a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que ser� atualizada.
	 * 
	 */
	public void updateItemReceita(final ItemReceita entity) {
		auditoria.preUpdate(entity);

		itemReceitaDao.update(entity);
	}

	/**
	 * Remove a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que ser� removida.
	 * 
	 */
	public void removeItemReceita(final ItemReceita entity) {
		auditoria.postRemove(entity);

		itemReceitaDao.remove(entity);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public ItemReceita findItemReceitaById(final Long id) {
		return itemReceitaDao.findById(id);
	}

	/**
	 * Busca os itens de uma receita.
	 * 
	 * @param receita
	 *            entidade do tipo Receita.
	 * 
	 * @return lista de itnes da receitas.
	 */
	public List<ItemReceita> findItemReceitaByReceita(final Receita receita) {
		return itemReceitaDao.findByReceita(receita);
	}

	/**
	 * Busca os itens de uma receitaMoeda.
	 * 
	 * @param receitaMoeda
	 *            - receitaMoeda.
	 * @return lista de itens de uma receita moeda.
	 */
	public List<ItemReceita> findItemReceitaByReceitaMoeda(
			final ReceitaMoeda receitaMoeda) {
		return itemReceitaDao.findByReceitaMoeda(receitaMoeda);
	}

	public List<ItemReceita> calculateTaxMultiDealFiscal(List<ReceitaDealFiscal> receitasDealFiscal, List<ItemReceita> itensReceita) {

		DealFiscal dealFiscalTax = null;
		for (ReceitaDealFiscal receitaDealFiscal : receitasDealFiscal) {
			//if (receitaDealFiscal.getValorReceita().doubleValue() > 0 || receitasDealFiscal.size() == 1) {
			//break;
			dealFiscalTax = receitaDealFiscal.getDealFiscal();

			dealFiscalTax = dealFiscalService.findDealFiscalById(dealFiscalTax.getCodigoDealFiscal());

			TaxaImposto taxa = new TaxaImposto();
			taxa.setEmpresa(dealFiscalTax.getEmpresa());
			taxa.setTipoServico(tipoServicoService.findTipoServicoByDeal(dealFiscalTax).get(0));
			TaxaImposto taxaImposto = taxaImpostoService.findTaxesByDealFiscalCodeAndMonth(dealFiscalTax.getCodigoDealFiscal(), receitaDealFiscal.getReceitaMoeda().getReceita().getDataMes());

			Double valorImposto = 0.0;
			if (taxaImposto != null && taxaImposto.getValorTaxaMunicipal() != null && taxaImposto.getValorTaxaFederal() != null) {
				valorImposto = taxaImposto.getValorTaxaMunicipal().doubleValue() + taxaImposto.getValorTaxaFederal().doubleValue();
			} else if (taxaImposto != null && taxaImposto.getValorTaxa() != null) {
				valorImposto = taxaImposto.getValorTaxa().doubleValue();
			}

			for (ItemReceita itemReceita : itensReceita) {

				PessoaCidadeBase pcb = pessoaCidadeBaseService.findPessCBByPessoaAndDate(itemReceita.getPessoa(),itemReceita.getReceitaMoeda().getReceita().getDataMes());

				Long empresaPessoa = cidadeBaseService.findEmpresaByCidadeBase(pcb.getCidadeBase().getCodigoCidadeBase());
				Long empresaDealFiscal = dealFiscalTax.getEmpresa().getCodigoEmpresa();
				if(empresaDealFiscal.equals(empresaPessoa)){
					Double itemTaxValue = valorImposto * itemReceita.getValorTotalItem().doubleValue() / 100;
					itemReceita.setValorImpostoItem(BigDecimal.valueOf(itemTaxValue));
					itemReceita.setValorLiquidoItem(BigDecimal.valueOf(itemReceita.getValorTotalItem().doubleValue() - itemTaxValue));

					itemReceita.setCodigoReceitaDfiscal(receitaDealFiscal.getCodigoReceitaDfiscal());
				}
			}
		}

		return itensReceita;
	}

	public List<ItemReceita> calculateTax(List<ReceitaDealFiscal> receitasDealFiscal, List<ItemReceita> itensReceita) {

		DealFiscal dealFiscalTax = null;
		ReceitaDealFiscal receitaDF = null;
		for (ReceitaDealFiscal receitaDealFiscal : receitasDealFiscal) {
			if (receitaDealFiscal.getValorReceita().doubleValue() > 0 || receitasDealFiscal.size() == 1) {
				dealFiscalTax = receitaDealFiscal.getDealFiscal();
				receitaDF = receitaDealFiscal;
				break;
			}
		}

		if (dealFiscalTax != null) {
			dealFiscalTax = dealFiscalService.findDealFiscalById(dealFiscalTax.getCodigoDealFiscal());

			TaxaImposto taxa = new TaxaImposto();
			taxa.setEmpresa(dealFiscalTax.getEmpresa());
			taxa.setTipoServico(tipoServicoService.findTipoServicoByDeal(dealFiscalTax).get(0));
			TaxaImposto taxaImposto = taxaImpostoService.findTaxesByDealFiscalCodeAndMonth(dealFiscalTax.getCodigoDealFiscal(), receitaDF.getReceitaMoeda().getReceita().getDataMes());

			Double valorImposto = 0.0;
			if (taxaImposto != null && taxaImposto.getValorTaxaMunicipal() != null && taxaImposto.getValorTaxaFederal() != null) {
				valorImposto = taxaImposto.getValorTaxaMunicipal().doubleValue() + taxaImposto.getValorTaxaFederal().doubleValue();
			} else if (taxaImposto != null && taxaImposto.getValorTaxa() != null) {
				valorImposto = taxaImposto.getValorTaxa().doubleValue();
			}

			for (ItemReceita itemReceita : itensReceita) {
				Double itemTaxValue = valorImposto * itemReceita.getValorTotalItem().doubleValue() / 100;
				itemReceita.setValorImpostoItem(BigDecimal.valueOf(itemTaxValue));
				itemReceita.setValorLiquidoItem(BigDecimal.valueOf(itemReceita.getValorTotalItem().doubleValue() - itemTaxValue));
			}
		} else {
			for (ItemReceita itemReceita : itensReceita) {

				itemReceita.setValorImpostoItem(BigDecimal.valueOf(0));
				itemReceita.setValorLiquidoItem(BigDecimal.valueOf(0));
			}
		}

		return itensReceita;
	}

	public Boolean updateAllMultiDealFiscalTaxAndNetRevenueAfterDate(final Date dataMes) {
		return itemReceitaDao.updateAllMultiDealFiscalTaxAndNetRevenueAfterDate(dataMes);
	}

	public Boolean updateAllTaxAndNetRevenueAfterDate(final Date dataMes) {
		return itemReceitaDao.updateAllTaxAndNetRevenueAfterDate(dataMes);
	}

}