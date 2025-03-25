package com.ciandt.pms.business.service.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IItemTabPerPadraoService;
import com.ciandt.pms.business.service.ITabelaPerfilPadraoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ItemTabPerPadrao;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TabelaPerfilPadrao;
import com.ciandt.pms.persistence.dao.ITabelaPerfilPadraoDao;


/**
 * 
 * A classe TabelaPerfilPadraoService proporciona as funcionalidades da camada
 * de serviço referentes a entidade TabelaPerfilPadrao.
 * 
 * @since 19/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class TabelaPerfilPadraoService implements ITabelaPerfilPadraoService {

    /** Instancia do DAO da classe TabelaPerfilPadraoDao. */
    @Autowired
    private ITabelaPerfilPadraoDao dao;
    
    /** Instancia de servico itemTabPerPadrao. */
    @Autowired
    private IItemTabPerPadraoService itemTabPerPadraoService;

    /**
     * @param entity
     *            Entidade a ser persistida.
     * @return true se persistido.
     */
    public Boolean createTabelaPerfilPadrao(final TabelaPerfilPadrao entity) {

        TabelaPerfilPadrao tabelaSup = dao.findMaxStartDataByCurrency(entity.getMoeda());

        // verifica se a data de vigencia é a maior
        if (tabelaSup != null) {
            if (entity.getDataInicio().after(tabelaSup.getDataInicio())) {
                tabelaSup.setDataFim(DateUtils.addMonths(
                        entity.getDataInicio(), -1));
                dao.update(tabelaSup);
            } else {
                Messages.showError("createTabelaPerfilPadrao",
                        Constants.MSG_ERROR_ADD_TABELA_PRECO_PERIOD);
                return Boolean.FALSE;
            }
        }

        dao.create(entity);
        return Boolean.valueOf(true);
    }

    /**
     * Remove item da tabela de perfilPadrao.
     * 
     * @param entity
     *            entidade a ser removida
     * @return boolean
     */
    @Override
    @Transactional
    public Boolean removeTabelaPerfilPadrao(final TabelaPerfilPadrao entity) {
        TabelaPerfilPadrao tabelaPerfilPadrao = findMaxStartDataByCurrency(entity.getMoeda());

        TabelaPerfilPadrao tabelaDelete =
                findById(entity.getCodigoTabPerfilPadrao());

        if (tabelaPerfilPadrao.getCodigoTabPerfilPadrao().compareTo(
                entity.getCodigoTabPerfilPadrao()) == 0) {

            Iterator<ItemTabPerPadrao> it =
                    entity.getItemTabPerPadrao().iterator();
            
            while (it.hasNext()) {
                ItemTabPerPadrao item = it.next();
                itemTabPerPadraoService.removeItemTabPerPadrao(item);
            }

            dao.remove(tabelaDelete);

            tabelaPerfilPadrao = findMaxStartDataByCurrency(entity.getMoeda());

            if (tabelaPerfilPadrao != null) {
                tabelaPerfilPadrao.setDataFim(null);
                dao.update(tabelaPerfilPadrao);
            }

            return true;
        } else {
            Messages.showError("removeTabelaPreco",
                    Constants.MSG_ERROR_TABELA_PRECO_REMOVE);
            return false;

        }
    }
    
    /**
     * Atualiza entidade.
     * @param entity entidade
     * @return true
     */
    @Transactional
    public Boolean updateTabPerPadrao(final TabelaPerfilPadrao entity) {
        dao.update(entity);
        return Boolean.valueOf(true);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TabelaPerfilPadrao> findTabelaPerfilPadraoAll() {

        return dao.findAll();
    }

    /**
     * Retorna Tabela perfil padrao com maior data de incio.
     * 
     * @return tabela com maior data.
     */
    public TabelaPerfilPadrao findMaxStartDate() {
        return dao.findMaxStartDate();
    }

    /**
     * 
     * @param id
     *            id
     * @return entidade referente ao id.
     */
    public TabelaPerfilPadrao findById(final Long id) {
        return dao.findById(id);
    }
    
        /**
     * Busca maior data de inicio por moeda.
     * 
     * @param moeda
     *            moeda.
     * @return TabelaPerfilPadrao
     */
    public TabelaPerfilPadrao findMaxStartDataByCurrency(final Moeda moeda) {
        return dao.findMaxStartDataByCurrency(moeda);
    }

}
