package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.Recurso;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 26/01/2010
 */
@Service
public interface IPapelAlocacaoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createPapelAlocacao(final PapelAlocacao entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     */
    @Transactional
    void updatePapelAlocacao(final PapelAlocacao entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * @throws IntegrityConstraintException
     *             - tratamento erro de referência de integridade
     * 
     */
    @Transactional
    void removePapelAlocacao(final PapelAlocacao entity)
            throws IntegrityConstraintException;

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    PapelAlocacao findPapelAlocacaoById(final Long id);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<PapelAlocacao> findPapelAlocacaoByFilter(final PapelAlocacao filter);
    
    /**
     * Busca uma lista de entidades pelo filtro - Fetch.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<PapelAlocacao> findPapelAlocacaoByFilterFetch(final PapelAlocacao filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PapelAlocacao> findPapelAlocacaoAll();
    
    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PapelAlocacao> findPapelAlocacaoAllActive();
    
    /**
     * Recuprea a moeda corrente.
     * 
     * @param moeda
     *            - Moeda corrente
     * @return o pattern da Moeda.
     */
    String getCurrency(final Moeda moeda);
    
    /**
     * Retorna todas as entidades.
     * 
     * @param recurso - Recurso associado ao PapelAlocacao.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    PapelAlocacao findPapelAlocacaoByRecurso(final Recurso recurso);
    
    /**
     * Busca uma lista de entidades sem associacao com o TcePapelAlocacao.
     * 
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<PapelAlocacao> findPapelAlocAllNotTceAssociated(final Date dataMes);

}