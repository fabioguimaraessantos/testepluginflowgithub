package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IHedgeService;
import com.ciandt.pms.model.HedgeMoedaMes;
import com.ciandt.pms.persistence.dao.IHedgeDao;


/**
 * 
 * A classe HedgeService proporciona as funcionalidades da camada
 * de serviço referente a entidade Hedge.
 *
 * @since 24/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class HedgeService implements IHedgeService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IHedgeDao dao;
    
    /**
     * Cria uma entidade.
     * 
     * @param entity que será criada.
     * 
     * @return true se for inserida com sucesso.
     */
    public Boolean createHedge(final HedgeMoedaMes entity) {
        HedgeMoedaMes hedge = dao.findUnique(
                entity.getDataMes(), entity.getMoeda());
        if (hedge != null) {
            return Boolean.FALSE;
        }
        
        dao.create(entity);
        return Boolean.TRUE;
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity que será apagada.
     */
    public void removeHedge(final HedgeMoedaMes entity) {
        dao.remove(dao.findById(
                entity.getCodigoHedgeMoedaMes()));
    }
    
    /**
     * Realiza o update da entidade.
     * 
     * @param entity que será atualizada
     * 
     * @return retorna true se update realizado com sucesso,
     * caso contrario false.
     */
    public Boolean updateHedge(final HedgeMoedaMes entity) {
        HedgeMoedaMes hedge = dao.findUnique(
                entity.getDataMes(), entity.getMoeda());
        if (hedge != null && hedge.getCodigoHedgeMoedaMes().compareTo(
                entity.getCodigoHedgeMoedaMes()) != 0) {
            return Boolean.FALSE;
        }
        
        dao.update(entity);
        
        return Boolean.TRUE;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public HedgeMoedaMes findHedgeById(final Long id) {

        return dao.findById(id);
    }
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<HedgeMoedaMes> findHedgeByFilter(final HedgeMoedaMes filter) {
        return dao.findByFilter(filter);
    }
}
