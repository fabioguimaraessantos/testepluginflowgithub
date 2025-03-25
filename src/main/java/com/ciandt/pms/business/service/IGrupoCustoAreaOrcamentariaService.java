package com.ciandt.pms.business.service;

import com.ciandt.pms.model.GrupoCustoAreaOrcamentariaAud;
import com.ciandt.pms.persistence.dao.jpa.GrupoCustoAreaOrcamentaria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * Define a interface do Service da entidade.
 *
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IGrupoCustoAreaOrcamentariaService {

    /**
     * Busca uma entidade pelo id.
     *
     * @param id
     *            da entidade.
     *
     * @return entidade com o id passado por parametro.
     */
    GrupoCustoAreaOrcamentaria findById(final Long id);

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    List<GrupoCustoAreaOrcamentaria> findAll();

	/**
	 * Insere uma entidade.
	 *
	 * @param entity
	 * @return
	 */
    @Transactional
	Boolean create(final GrupoCustoAreaOrcamentaria entity);

	/**
	 * Atualiza uma entidade.
	 *
	 * @param entity
	 */
	@Transactional
	void update(final GrupoCustoAreaOrcamentaria entity);

	/**
	 * Deleta uma entidade.
	 *
	 * @param entity
	 * @return
	 */
	@Transactional
	Boolean remove(final GrupoCustoAreaOrcamentaria entity);

	List<GrupoCustoAreaOrcamentaria> findByGrupoCustoId(final Long codigoGrupoCusto);

	GrupoCustoAreaOrcamentaria findMaxDataInicioByCodigoGrupoCusto(
			Long codigoGrupoCusto);

	List<GrupoCustoAreaOrcamentaria> findBySubAreaOrcamentariaAndVigencia(Long codigoAreaOrcamentaria, Date dataVigencia);

	List<GrupoCustoAreaOrcamentariaAud> findHistoryByCodigo(Long codigoGrupoCusto);
}
