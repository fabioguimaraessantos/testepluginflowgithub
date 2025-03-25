package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Aliquota;
import com.ciandt.pms.model.Imposto;


/**
 * Define a interface do Service da entidade.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Service
public interface IAliquotaService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * @return retorna true se a data inicio da vigencia, é posterior a maior
     *         data existente no banco de dados. Caso contrario retorna false
     */
    @Transactional
    Boolean createAliquota(final Aliquota entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @return true se foi removido corretamente, false caso tenha acontecido
     *         algum erro
     * 
     */
    @Transactional
    Boolean removeAliquota(final Aliquota entity);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Aliquota> findAliquotaAll();

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    Aliquota findAliquotaById(final Long id);

    /**
     * Retorna todas as tabelas de preço associados a um ContratoPratica.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return retorna uma lista de Aliquota de um Imposto.
     */
    List<Aliquota> findAliquotaByImposto(final Imposto imposto);

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    Aliquota findMaxStartDateByImposto(final Imposto imposto);

    /**
     * Busca a entidade na data atual.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    Aliquota findByImpostoByCurrentDate(final Imposto imposto);

}
