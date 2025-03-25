package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.HedgeMoedaMes;


/**
 * 
 * A classe IHedgeService proporciona a interface de acesso
 * a camada de serviço referente a entidade Hedge.
 *
 * @since 24/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IHedgeService {
    
    /**
     * Cria uma entidade.
     * 
     * @param entity que será criada.
     * 
     * @return true se for inserida com sucesso.
     */
    @Transactional
    Boolean createHedge(final HedgeMoedaMes entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity que será apagada.
     */
    @Transactional
    void removeHedge(final HedgeMoedaMes entity);
    
    /**
     * Realiza o update da entidade.
     * 
     * @param entity que será atualizada
     * 
     * @return retorna true se update realizado com sucesso,
     * caso contrario false.
     */
    @Transactional
    Boolean updateHedge(final HedgeMoedaMes entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    HedgeMoedaMes findHedgeById(final Long id);
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<HedgeMoedaMes> findHedgeByFilter(final HedgeMoedaMes filter);
}
