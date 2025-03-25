package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.model.CustoBasePapelAlocacao;


/**
 * Define a interface do Service da entidade.
 * 
 * @author cmantovani
 * @since 13/07/2011
 */
@Service
public interface ICustoBasePapelAlocacaoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return retorna true se a data inicio da vigencia, é posterior a maior
     *         data existente no banco de dados. Caso contrario retorna false
     */
    @Transactional
    Boolean createCustoBasePapelAlocacao(final CustoBasePapelAlocacao entity);

    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateCustoBasePapelAlocacao(final CustoBasePapelAlocacao entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @return true se CustoBasePapelAlocacao foi removida corretamente, coso
     *         contrário retorna false
     */
    @Transactional
    Boolean removeCustoBasePapelAlocacao(final CustoBasePapelAlocacao entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CustoBasePapelAlocacao findCustoBasePapelAlocacaoById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoBasePapelAlocacao> findCustoBasePapelAlocacaoAll();

    /**
     * Busca uma lista de entidades pelo PapelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do BasePapelAlocacao.
     */
    List<CustoBasePapelAlocacao> findCustoBasePapelAlocacaoByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao);

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    CustoBasePapelAlocacao findMaxStartDateByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao);

    /**
     * Busca a entidade no qual intervalo possua a pada passado por parametro,
     * referente ao PapelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * @param date
     *            Data que se deseja saber o valor do CustoBase do
     *            PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do BasePapelAlocacao.
     */
    CustoBasePapelAlocacao findCustoBasePapelAlocacaoByBasePapelAndDate(
            final BasePapelAlocacao basePapelAlocacao, final Date date);

    CustoBasePapelAlocacao findCustoBasePapelAlocacaoByBasePapelAndCurrentDate(
            final BasePapelAlocacao basePapelAlocacao);

    List<CustoBasePapelAlocacao> findCustoBasePapelAlocacaoByStartDateGreaterThan(
            final Long basePapelAlocacaoCode, final Date startDate);
}