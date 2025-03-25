package com.ciandt.pms.business.service;

import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoPeriodo;
import com.ciandt.pms.model.GrupoCustoPeriodoAud;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * A classe IGrupoCustoPeriodoService proporciona a interface de acesso da camada de
 * servi�o referente a entidade.
 * 
 * @since 15/05/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IGrupoCustoPeriodoService {
    
    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que ser� apagada.
     * 
     * @return retorna true se a data inicio da vigencia, � posterior a maior
     *         data existente no banco de dados. Caso contrario retorna false
     */
    @Transactional
    Boolean createGrupoCustoPeriodo(final GrupoCustoPeriodo entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que ser� apagada.
     *            
     * @return true se foi removido com sucesso, false caso contrario            
     * 
     */
    @Transactional
    Boolean removeGrupoCustoPeriodo(final GrupoCustoPeriodo entity);

    /**
     * Busca e entidade pelo id.
     * 
     * @param id
     *            - id da entidade.
     * 
     * @return retorna a entidade com o id passado por parametro. Caso n�o
     *         exista retorna null.
     */
    GrupoCustoPeriodo findGrupoCustoPeriodoById(final Long id);

	/**
	 * Busca a entidade com maior data inicio da vigencia.
	 * 
	 * @param grupoCusto
	 *            entidade populada com os valores do GrupoCusto.
	 * 
	 * @return lista de entidades que atendem ao criterio do GrupoCusto.
	 */
	GrupoCustoPeriodo findMaxStartDateByGrupoCusto(final GrupoCusto grupoCusto);

    List<GrupoCustoPeriodoAud> findHistoryByCodigo(final Long codigoGrupoCusto);
}
