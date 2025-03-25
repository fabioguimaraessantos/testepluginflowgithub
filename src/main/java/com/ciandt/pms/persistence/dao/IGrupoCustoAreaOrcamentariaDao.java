package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.GrupoCustoAreaOrcamentariaAud;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.persistence.dao.jpa.GrupoCustoAreaOrcamentaria;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/08/2015
 */
@Repository
public interface IGrupoCustoAreaOrcamentariaDao extends
		IAbstractDao<Long, GrupoCustoAreaOrcamentaria> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<GrupoCustoAreaOrcamentaria> findAll();

    List<GrupoCustoAreaOrcamentaria> findByGrupoCustoId(Long codigoGrupoCusto);

	GrupoCustoAreaOrcamentaria findMaxDataInicioByCodigoGrupoCusto(
			Long codigoGrupoCusto);

	List<GrupoCustoAreaOrcamentaria> findBySubAreaOrcamentariaAndVigencia(Long codigoAreaOrcamentaria, Date dataVigencia);

	List<GrupoCustoAreaOrcamentariaAud> findHistoryByCodigo(Long codigoGrupoCusto);
}
