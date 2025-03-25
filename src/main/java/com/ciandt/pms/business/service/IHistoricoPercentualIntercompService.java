package com.ciandt.pms.business.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.HistoricoPercentualIntercomp;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IHistoricoPercentualIntercompService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    HistoricoPercentualIntercomp findById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<HistoricoPercentualIntercomp> findAll();
	/**
	 * Retorna todas as entidades.
	 *
	 * @return retorna uma lista com todas as entidades.
	 */
	List<HistoricoPercentualIntercomp> findByDealFiscal(final Long dealFiscalCode);

	void delete(HistoricoPercentualIntercomp entity);

	void update(HistoricoPercentualIntercomp entity);

	void create(HistoricoPercentualIntercomp entity);

	void update(Set<HistoricoPercentualIntercomp> historicoPercentualIntercomps);

	void create(Set<HistoricoPercentualIntercomp> hpis);
}