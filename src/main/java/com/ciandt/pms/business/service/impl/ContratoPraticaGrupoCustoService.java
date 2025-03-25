package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaGrupoCustoService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IConvergenciaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaGrupoCusto;
import com.ciandt.pms.model.Convergencia;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.persistence.dao.IContratoPraticaGrupoCustoDao;

/**
 * A classe ControleReajusteService proporciona as funcionalidades de serviço
 * referente a entidade ControleReajuste.
 * 
 * @since 11/03/2015
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class ContratoPraticaGrupoCustoService extends AbstractGestaoReajusteService
		implements IContratoPraticaGrupoCustoService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IContratoPraticaGrupoCustoDao dao;

	/** Instancia do servico da entidade ConvergenciaService. */
	@Autowired
	private IConvergenciaService convergenciaService;
	
	/** Instancia do servico da entidade ContratoPraticaService. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;
	
	/** Instancia do servico da entidade GrupoCustoService. */
	@Autowired
	private IGrupoCustoService grupoCustoService;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public ContratoPraticaGrupoCusto findById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<ContratoPraticaGrupoCusto> findAll() {
		return dao.findAll();
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @param cpgc
	 * @throws MoreThanOneActiveEntityException 
	 */
	@Override
	@Transactional
	public Boolean create(final ContratoPraticaGrupoCusto cpgc) {

		ContratoPraticaGrupoCusto cpgcAntigo = this.findLastByContratoPraticaId(
				cpgc.getContratoPratica().getCodigoContratoPratica());

		if (cpgcAntigo != null) {
			if (cpgc.getDataInicioVigencia().after(cpgcAntigo.getDataInicioVigencia())) {

				cpgcAntigo.setDataFimVigencia(DateUtils.addMonths(
						cpgc.getDataInicioVigencia(), -1));
				this.update(cpgcAntigo);
			} else {
				Messages.showError("createCPGC", Constants.CPGC_VIGENCIA_INVALIDA);
				return Boolean.FALSE;
			}
		}
		// TODO: criar cpgc, atualizar antigo, atualizar convergencia
		dao.create(cpgc);
		Convergencia convergencia = convergenciaService.findByContratoPraticaId(cpgc.getContratoPratica().getCodigoContratoPratica());
		if (convergencia.getCodigoConvergencia() != null) {
			
			convergencia.setGrupoCusto(cpgc.getGrupoCusto());
			convergencia.setCodigoCentroCustoMega(cpgc.getGrupoCusto().getErpCodigoCentroCusto());
			convergenciaService.update(convergencia);
		}

		return Boolean.TRUE;
	}

	@Override
	@Transactional
	public Boolean create(final ContratoPratica contratoPratica, final GrupoCusto grupoCusto, final Date inicioVigencia) {
		ContratoPraticaGrupoCusto cpgc = new ContratoPraticaGrupoCusto(contratoPratica, grupoCusto, inicioVigencia);
		return this.create(cpgc);
	}

	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void update(final ContratoPraticaGrupoCusto entity) {
		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param cpgc
	 */
	@Override
	@Transactional
	public Boolean remove(final ContratoPraticaGrupoCusto cpgc) {
		ContratoPraticaGrupoCusto cpgcMaisRecente = this
				.findLastByContratoPraticaId(cpgc
						.getContratoPratica().getCodigoContratoPratica());

		if (cpgcMaisRecente.getContratoPratica().getCodigoContratoPratica().compareTo(cpgc.getContratoPratica().getCodigoContratoPratica()) == 0) {
			ContratoPratica cp = contratoPraticaService.findContratoPraticaById(cpgc.getContratoPratica().getCodigoContratoPratica());
			cp.getContratoPraticaGrupoCustos().remove(cpgc);
			
			GrupoCusto gc = grupoCustoService.findGrupoCustoById(cpgc.getGrupoCusto().getCodigoGrupoCusto());
			gc.getContratoPraticaGrupoCustos().remove(cpgc);

			dao.remove(cpgc);

			cpgcMaisRecente = this
					.findLastByContratoPraticaId(cp.getCodigoContratoPratica());

			if (cpgcMaisRecente != null) {
				cpgcMaisRecente.setDataFimVigencia(null);
				dao.update(cpgcMaisRecente);

				Convergencia convergencia = convergenciaService
						.findByContratoPraticaId(cpgcMaisRecente.getContratoPratica().getCodigoContratoPratica());
				if (convergencia.getCodigoConvergencia() != null) {

					convergencia.setGrupoCusto(cpgcMaisRecente.getGrupoCusto());
					convergencia.setCodigoCentroCustoMega(cpgcMaisRecente.getGrupoCusto().getErpCodigoCentroCusto());
					convergenciaService.update(convergencia);
				}
			}

			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public List<ContratoPraticaGrupoCusto> findByContratoPraticaId(final Long codigoContratoPratica) {
		return dao.findByContratopraticaId(codigoContratoPratica);
	}

	@Override
	public ContratoPraticaGrupoCusto findLastByContratoPraticaId(final Long codigoContratoPratica) {
		return dao.findLastByContratoPraticaId(codigoContratoPratica);
	}

	@Override
	public ContratoPraticaGrupoCusto findByContratoPraticaIdAndDataFimVigencia(final Long contratoPraticaId, final Date dataFimVigencia) {
		return dao.findByContratoPraticaIdAndDataFimVigencia(contratoPraticaId, dataFimVigencia);
	}
}