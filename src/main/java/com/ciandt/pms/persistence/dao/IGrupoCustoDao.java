package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoAud;


/**
 * 
 * A classe IGrupoCustoDao proporciona a interface de acesso 
 * a camada de persistencia referente a entidade GurpoCusto.
 *
 * @since 12/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IGrupoCustoDao extends IAbstractDao<Long, GrupoCusto> {
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<GrupoCusto> findByFilter(final GrupoCusto filter);
    

    List<GrupoCusto> findByAproverRescinded();

    /**
     * Busca e entidade pelo acronym.
     * 
     * @param siglaGrupoCusto
     *            sigla do GrupoCusto
     * 
     * @return retorna a entidade
     */
    GrupoCusto findByAcronym(final String siglaGrupoCusto);

    /**
     * Busca por todos os GrupoCusto ativos.
     * 
     * @return uma lista com todos os GrupoCusto ativos.
     */
    List<GrupoCusto> findAllActive();
	
	/**
	 * Busca por todos os GrupoCusto ativos, do tipo Prod_ Com_.
	 *
	 * @return uma lista com os GrupoCusto ativos do tipo Prod e Com.
	 */
	public List<GrupoCusto> findActiveTypeProdCom();

	/**
	 * Busca por todos os GrupoCusto.
	 *
	 * @return uma lista com todos os GrupoCusto.
	 */
	List<GrupoCusto> findAll();


	/**
	 * @param codigoGrupoCusto
	 * @return grupo de custo
	 */
	GrupoCusto findGrupoCustoByIdWithPeriodos(Long codigoGrupoCusto);

	/**
	 * @param sgEstruturaOrganizacional
	 * @return
	 */
	List<GrupoCusto> findGrupoCustoAllActiveAndEstrOrgan(
			String sgEstruturaOrganizacional);

    /**
	 * Busca por todos os GrupoCusto ativos.
	 * 
	 * @return uma lista com todos os GrupoCusto ativos mas s� o codigo e nome
	 *         est�o preenchidos.
	 */
	List<GrupoCusto> findAllActiveReturnCodigoAndNomeGrupoCusto();

	List<GrupoCustoAud> findHistoryByCodigo(Long codigoGrupoCusto);

    List<GrupoCusto> findByCostCenterHierarchy(final Long code);
}
