package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.TcePapelAlocacao;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 26/01/2010
 */
@Service
public interface ITcePapelAlocacaoService {

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
    Boolean createTcePapelAlocacao(final TcePapelAlocacao entity);
    
    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateTcePapelAlocacao(final TcePapelAlocacao entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     *            
     * @return true se TcePapelAlocacao foi removida corretamente, coso contrário
     *         retorna false 
     */
    @Transactional
    Boolean removeTcePapelAlocacao(final TcePapelAlocacao entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    TcePapelAlocacao findTcePapelAlocacaoById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TcePapelAlocacao> findTcePapelAlocacaoAll();
    
    /**
     * Busca a entidade no qual intervalo possua a 
     * pada passado por parametro, referente ao PapelAlocacao.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * @param date 
     *            Data que se deseja saber o valor do TCE do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    TcePapelAlocacao findTcePapelAlocacaoByPapelAndDate(
            final PapelAlocacao papelAlocacao , final Date date);

}