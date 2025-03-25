package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaGrupoCusto;
import com.ciandt.pms.model.GrupoCusto;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/03/2015
 */
@Service
public interface IContratoPraticaGrupoCustoService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
	ContratoPraticaGrupoCusto findById(final Long id);

    List<ContratoPraticaGrupoCusto> findAll();

	@Transactional
	Boolean create(final ContratoPraticaGrupoCusto entity);

	@Transactional
	Boolean create(final ContratoPratica contratoPratica, final GrupoCusto grupoCusto, final Date inicioVigencia);

	@Transactional
	void update(final ContratoPraticaGrupoCusto entity);

	@Transactional
	Boolean remove(final ContratoPraticaGrupoCusto entity);

	List<ContratoPraticaGrupoCusto> findByContratoPraticaId(final Long codigoContratoPratica);

	ContratoPraticaGrupoCusto findLastByContratoPraticaId(final Long codigoContratoPratica);

	ContratoPraticaGrupoCusto findByContratoPraticaIdAndDataFimVigencia(final Long contratoPraticaId, final Date dataFimVigencia);
}