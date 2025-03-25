package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IApropriacaoFaturaService;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IFaturaReceitaService;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.business.service.IItemFaturaService;
import com.ciandt.pms.business.service.IReceitaDealFiscalService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.vo.ApropriacaoFaturaFormFilter;
import com.ciandt.pms.model.vo.ApropriacaoFaturaRow;
import com.ciandt.pms.model.vo.FaturaReceitaRow;
import com.ciandt.pms.util.NumberUtil;

/**
 * 
 * A classe ApropriacaoFaturaService proporciona as funcionalidades relacionadas
 * a apropriacao da fatura com as receitas.
 * 
 * @since 19/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ApropriacaoFaturaService implements IApropriacaoFaturaService {

	/** instância do serviço ReceitaDealFiscal. */
	@Autowired
	private IReceitaDealFiscalService rdfService;

	/** instância do serviço CentroLucro. */
	@Autowired
	private ICentroLucroService centroLucroSenvice;

	/** instância do serviço ContratoPratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** instância do serviço Cliente. */
	@Autowired
	private IClienteService clienteService;

	/** instância do serviço FaturaReceita. */
	@Autowired
	private IFaturaReceitaService faturaReceitaService;

	/** instância do serviço Fatura. */
	@Autowired
	private IFaturaService faturaService;

	/** instância do serviço ItemFatura. */
	@Autowired
	private IItemFaturaService itemFaturaService;

	/** instância do serviço ReceitaService. */
	@Autowired
	private IReceitaService receitaService;

	/** instancai de dealfiscalservice. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/**
	 * Realiza a busca pelas entidades ReceitaDealFiscal.
	 * 
	 * @param formFilter
	 *            - form do filtro
	 * 
	 * @return - retorna uma lista de ApropriacaoFaturaRow, com o resultado da
	 *         busca
	 */
	public List<ApropriacaoFaturaRow> findApropriacaoFaturaByFilter(
			final ApropriacaoFaturaFormFilter formFilter) {

		ContratoPratica cp = null;
		if (formFilter.getCodigoContratoPratica() != null) {
			cp = contratoPraticaService.findContratoPraticaById(formFilter
					.getCodigoContratoPratica());
		}

		Cliente cli = null;
		if (formFilter.getCodigoCliente() != null) {
			cli = clienteService.findClienteById(formFilter.getCodigoCliente());
		}

		CentroLucro cl = null;
		if (formFilter.getCodigoCentroLucro() != null) {
			cl = centroLucroSenvice.findCentroLucroById(formFilter
					.getCodigoCentroLucro());
		}

		DealFiscal dealFiscal = null;
		if (formFilter.getCodigoDealFiscal() != null) {
			dealFiscal = dealFiscalService.findDealFiscalById(formFilter.getCodigoDealFiscal());
		}

		List<ReceitaDealFiscal> rdfList = new ArrayList<ReceitaDealFiscal>();
		List<Receita> receitaList = new ArrayList<Receita>();
		List<ReceitaDealFiscal> listaFinal = new ArrayList<ReceitaDealFiscal>();

		// Se centroLucro for selecionado, realiza subQuery
		if (cl != null) {
			receitaList = receitaService.findReceitaByCpclAndCentroLucro(cl);
			for (Receita receita : receitaList) {
				rdfList = rdfService.findReceitaDealByFilterSub(cp, cli,
						formFilter.getDataMesInicio(),
						formFilter.getDataMesFim(), receita, dealFiscal);
				// Adiciona a lista da busca anterior na lista final
				for (ReceitaDealFiscal rdf : rdfList) {
					listaFinal.add(rdf);
				}
			}
		} else {
			listaFinal = rdfService.findReceitaDealByFilterSub(cp, cli,
					formFilter.getDataMesInicio(), formFilter.getDataMesFim(),
					null, dealFiscal);
		}

		// resolve o problema de LazyInicialization dos Ajustes de Receita
		for (ReceitaDealFiscal rdfInitialize : listaFinal) {
			Hibernate.initialize(rdfInitialize.getAjusteReceitas());
			for (AjusteReceita ajusteInitialize : rdfInitialize
					.getAjusteReceitas()) {
				Hibernate.initialize(ajusteInitialize);

			}
		}

		ApropriacaoFaturaRow row = null;
		List<ApropriacaoFaturaRow> rowList = new ArrayList<ApropriacaoFaturaRow>();
		String status = formFilter.getIndicadorStatus();
		for (ReceitaDealFiscal rdf : listaFinal) {
			row = new ApropriacaoFaturaRow();

			row.setTotalReceita(rdfService.getTotalReceitaByDealAndReceita(
					rdf.getReceitaMoeda().getReceita(), rdf.getDealFiscal())
					.doubleValue());

			row.setTotalFatura(faturaReceitaService
					.getTotalFaturaByReceitaDeal(rdf).doubleValue());

			if (status != null) {
				if (row.getTotalFatura().compareTo(row.getTotalReceita()) != 0) {
					if (status
							.equals(Constants.APROPRIACAO_FATURA_STATUS_COMPLETO)) {
						continue;
					}
				} else if (status
						.equals(Constants.APROPRIACAO_FATURA_STATUS_PENDENTE)) {
					continue;
				}
			}

			ContratoPratica contratoPratica = rdf.getReceitaMoeda()
					.getReceita().getContratoPratica();

			row.setContratoPratica(contratoPratica);
			row.setCliente(contratoPratica.getMsa().getCliente());
			row.setDeal(rdf.getDealFiscal());
			row.setReceitaDealFiscal(rdf);
			row.setReceita(rdf.getReceitaMoeda().getReceita());

			row.setPatternCurrency(rdf.getReceitaMoeda().getMoeda()
					.getSiglaMoeda());

			rowList.add(row);
		}

		Collections.sort(rowList);

		return rowList;
	}

	/**
	 * Realiza a apropriação. Salva as associações entre receita e fatura.
	 * 
	 * @param fatRecList
	 *            - Lista com a faturas a serem associadas.
	 * 
	 * @return retorna true se salvo com sucesso.
	 */
	public Boolean save(final List<FaturaReceitaRow> fatRecList) {

		Boolean isValidate = validateFaturaReceitaList(fatRecList);

		if (isValidate) {
			for (FaturaReceitaRow faturaReceitaRow : fatRecList) {
				FaturaReceita fr = faturaReceitaRow.getFaturaReceita();

				if (fr.getCodigoFaturaReceita() != null) {
					if (fr.getValorReceitaAssociada() == null
							|| fr.getValorReceitaAssociada().compareTo(
									BigDecimal.valueOf(0.0)) == 0) {
						faturaReceitaService
								.removeFaturaReceita(faturaReceitaService
										.findFaturaReceitaById(fr
												.getCodigoFaturaReceita()));
						fr.setCodigoFaturaReceita(null);
					} else {
						faturaReceitaService.updateFaturaReceita(fr);
					}
				} else {
					if (fr.getValorReceitaAssociada().compareTo(
							BigDecimal.valueOf(0.0)) != 0) {
						faturaReceitaService.createFaturaReceita(fr);
					}
				}
			}
		}

		return isValidate;
	}

	/**
	 * Realiza a validação do valor associado da fatura com a receita.
	 * 
	 * @param fr
	 *            - FaturaReceita a ser associado.
	 * 
	 * @return retorna true se total do valor associado <= ao total da fatura.
	 */
	public Boolean validateAssociateValue(final FaturaReceita fr) {

		Boolean ret = Boolean.TRUE;
		Double totalAssociate = faturaReceitaService
				.getFaturaReceitaTotalByFatura(fr.getFatura()).doubleValue();

		Double totalInvoice = itemFaturaService.getItemFaturaTotalByFatura(
				fr.getFatura()).doubleValue();

		if (fr.getCodigoFaturaReceita() != null) {
			FaturaReceita fatRec = faturaReceitaService
					.findFaturaReceitaById(fr.getCodigoFaturaReceita());

			totalAssociate -= fatRec.getValorReceitaAssociada().doubleValue();
			totalAssociate += fr.getValorReceitaAssociada().doubleValue();
			if (NumberUtil.round(totalAssociate) > NumberUtil
					.round(totalInvoice)) {
				ret = Boolean.FALSE;
			}

		} else {
			totalAssociate += fr.getValorReceitaAssociada().doubleValue();

			if (NumberUtil.round(totalAssociate) > NumberUtil
					.round(totalInvoice)) {
				ret = Boolean.FALSE;
			}
		}

		return ret;
	}

	/**
	 * Realiza a validação da lista de fatura-receita.
	 * 
	 * @param fatRecList
	 *            - Lista de fatura-receita
	 * 
	 * @return retorna true se toda as lista for validada com sucesso.
	 */
	private Boolean validateFaturaReceitaList(
			final List<FaturaReceitaRow> fatRecList) {

		for (FaturaReceitaRow row : fatRecList) {

			if (!this.validateAssociateValue(row.getFaturaReceita())) {
				// mensagem de erro
				Messages.showError("validate",
						Constants.APROPRIACAO_FATURA_MSG_ERROR_ASSOCIATE_VALUE);

				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	/**
	 * Prepara a a lista de FaturaReceita que será exibido na tela de
	 * gerenciamento da apropriação.
	 * 
	 * @param rdf
	 *            - ReceitaDealFiscal utilizado para buscar a lista.
	 * 
	 * @return lista de FaturaReceita.
	 */
	public List<FaturaReceitaRow> prepareManage(final ReceitaDealFiscal rdf) {

		ReceitaDealFiscal receitaDF = rdfService.findReceitaDealById(rdf
				.getCodigoReceitaDfiscal());

		List<FaturaReceita> faturaReceitaList = faturaReceitaService
				.findFaturaReceitaByReceitaDealFiscal(rdf);

		List<Fatura> faturaList = faturaService
				.findInvoicePendingByDeal(receitaDF);

		FaturaReceita faturaReceita = null;
		for (Fatura fatura : faturaList) {
			faturaReceita = new FaturaReceita();

			faturaReceita.setFatura(fatura);
			faturaReceita.setReceitaDealFiscal(receitaDF);
			faturaReceita.setValorReceitaAssociada(BigDecimal.valueOf(0.0));

			faturaReceitaList.add(faturaReceita);
		}

		FaturaReceitaRow row = null;
		List<FaturaReceitaRow> resultList = new ArrayList<FaturaReceitaRow>();
		for (FaturaReceita fr : faturaReceitaList) {
			row = new FaturaReceitaRow();

			row.setFaturaReceita(fr);
			double totalAssociated = faturaReceitaService
					.getFaturaReceitaTotalByFatura(fr.getFatura())
					.doubleValue();
			double totalInvoice = itemFaturaService.getItemFaturaTotalByFatura(
					fr.getFatura()).doubleValue();

			row.setInvoiceBalance(totalInvoice - totalAssociated);

			row.setTotalValueInvoice(totalInvoice);

			resultList.add(row);

		}

		Collections.sort(resultList);

		return resultList;
	}

	/**
	 * Retorna uma lista com as Receitas(FaturaReceita) associados a fatura
	 * passada por parametro.
	 * 
	 * @param fatura
	 *            - entidade do tipo Fatura.
	 * 
	 * @return retorna uma lista de FaturaReceita
	 */
	public List<FaturaReceita> findRevenuesByInvoice(final Fatura fatura) {
		Fatura f = faturaService.findFaturaById(fatura.getCodigoFatura());

		return f.getFaturaReceitas();
	}

}
