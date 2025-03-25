package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteStatus;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Repository
public interface IFichaReajusteDao extends IAbstractDao<Long, FichaReajuste> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<FichaReajuste> findAll();

    /**
     * Retorna Fichas de Reajuste com status igual a {@code status}.
     * 
     * @return list<FichaReajusteStatus>
     */
    List<FichaReajuste> findByFichaReajusteStatus(
			FichaReajusteStatus status);

    /**
     * Retorna uma {@link FichaReajuste} com seu nome igual a {@code nomeFichaReajuste}.
     *
     * @param nomeFichaReajuste
     * @return {@link FichaReajuste}
     */
    FichaReajuste findByNomeFichaReajuste(String nomeFichaReajuste);

    /**
     * Retorna Fichas de Reajuste que estão em {@code documentosLegais}.
     *
     * @param documentosLegais
     * @return List<FichaReajuste>.
     */
	List<FichaReajuste> findByDocumentosLegais(
			List<Long> idsFichaReajuste);
}