package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPraticaGrupoCusto;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/03/2015
 */
@Repository
public interface IContratoPraticaGrupoCustoDao extends IAbstractDao<Long, ContratoPraticaGrupoCusto> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<ContratoPraticaGrupoCusto> findAll();

    List<ContratoPraticaGrupoCusto> findByContratopraticaId(final Long codigoContratoPratica);

	ContratoPraticaGrupoCusto findLastByContratoPraticaId(final Long codigoContratoPratica);

	ContratoPraticaGrupoCusto findByContratoPraticaIdAndDataFimVigencia(final Long contratoPraticaId, final Date dataFimVigencia);
}