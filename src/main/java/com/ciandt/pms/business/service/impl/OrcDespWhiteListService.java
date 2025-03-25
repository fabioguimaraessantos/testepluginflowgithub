package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IOrcDespWhiteListService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcDespWhiteListDao;


/**
 * 
 * A classe OrcDespWhiteListService proporciona as funcionalidades de serviço
 * para OrcDespWhiteList.
 * 
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class OrcDespWhiteListService implements IOrcDespWhiteListService {

    /** Instancia de dao. */
    @Autowired
    private IOrcDespWhiteListDao dao;

    /**
     * Adiciona Pessoa na lista branca (OrcDespWhiteList).
     * 
     * @param entity
     *            OrcDespWhiteList
     * @return true
     */
    @Transactional
    public Boolean addPersonOrcDespWhiteList(final OrcDespWhiteList entity) {
        if (isNameExists(entity)) {
            Messages.showWarning("create", Constants.ALLOW_LIST_ALREADY_EXISTS);
            return Boolean.FALSE;
        } else {
            dao.create(entity);
            return Boolean.TRUE;
        }
    }

    /**
     * Deleta pessoa da lista WhitList.
     * 
     * @param entity
     *            pessoa.
     * @return true.
     */
    @Transactional
    public Boolean removePersonOrcDespWhiteLIst(final OrcDespWhiteList entity) {
        dao.remove(entity);
        return Boolean.TRUE;
    }

    /**
     * Busca por id.
     * 
     * @param id
     *            id
     * @return OrcDespWhiteList.
     */
    @Transactional
    public OrcDespWhiteList findById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca por OrcamentoDespesa.
     * 
     * @param orcamentoDespesa
     *            orcamentoDespesa
     * @return lista.
     */
    @Transactional
    public List<OrcDespWhiteList> findByOrcamentoDespesa(
            final OrcamentoDespesa orcamentoDespesa) {
        return dao.findByOrcamentoDespesa(orcamentoDespesa);
    }

    /**
     * Busca por OrcamentoDespesa e nome.
     * 
     * @param orcDesp
     *            orcamentoDespesa
     * @param name
     *            nome
     * @return lista.
     */
    @Transactional
    public List<OrcDespWhiteList> findByOrcDespAndName(
            final OrcamentoDespesa orcDesp, final String name) {
        return dao.findByOrcDespAndName(orcDesp, name);
    }

    /**
     * Verifica se nome a ser cadastrado ja existe na lista.
     * 
     * @param entity
     *            orcDespWhiteList.
     * @return boolean
     */
    private Boolean isNameExists(final OrcDespWhiteList entity) {
        // Busca lista
        List<OrcDespWhiteList> list =
                this.findByOrcDespAndName(entity.getOrcamentoDespesa(), entity
                        .getCodigoLogin());

        if (list.isEmpty()) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

}
