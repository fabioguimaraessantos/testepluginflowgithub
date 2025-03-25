package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IBmDnService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.BmDn;
import com.ciandt.pms.persistence.dao.IBmDnDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * A classe BmDnService proporciona as funcionalidades de servico para a entidade BmDn.
 *
 * @since 01/07/2022
 * @author <a href="mailto:pricaldeira@ciandt.com">Priscilla Caldeira</a>
 *
 */
@Service
public class BmDnService implements IBmDnService {

    private static final String EXCEPTION_NOT_FOUND = "BmDns not found.";
    private static final String EXCEPTION_NOT_FOUND_BY_ID = "BmDn not found";

    private static final String IN_ATIVO_Y = "Y";
    private static final String IN_ATIVO_N = "N";

    @Autowired
    private IBmDnDAO dao;

    @Override
    public List<BmDn> findByFilter(BmDn filter) throws BusinessException {

        prepareFilter(filter);
        List<BmDn> bmdns = dao.find(filter);
        if (bmdns == null || bmdns.isEmpty())
            throw new BusinessException(EXCEPTION_NOT_FOUND);

        for (BmDn bmdn : bmdns)
            bmdn.setInActive(getInAtivoView(bmdn.getInActive()));

        return bmdns;
    }

    @Override
    public BmDn findById(Long id) throws BusinessException {

        BmDn bmdn = dao.findById(id);
        if (bmdn == null)
            throw new BusinessException(EXCEPTION_NOT_FOUND_BY_ID);

        bmdn.setInActive(getInAtivoView(bmdn.getInActive()));
        return bmdn;
    }

    @Override
    public void remove(Long code) throws BusinessException {

        BmDn bmdn = dao.findById(code);
        if (bmdn == null)
            throw new BusinessException(EXCEPTION_NOT_FOUND_BY_ID);

        bmdn.setInActive("N");
        dao.update(bmdn);
    }

    @Override
    public void update(BmDn bmdn) throws BusinessException {
        if (isAlreadyExists(bmdn.getCode(), bmdn.getName()))
            throw new BusinessException(Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS);

        BmDn entity = dao.findById(bmdn.getCode());
        if (entity == null)
            throw new BusinessException(EXCEPTION_NOT_FOUND_BY_ID);

        entity.setName(bmdn.getName());
        entity.setInActive(getInAtivoModel(bmdn.getInActive()));

        dao.update(entity);
    }

    @Override
    public void create(BmDn bmdn) throws BusinessException {

        if (isAlreadyExists(bmdn.getName()))
            throw new BusinessException(Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS);

        bmdn.setInActive(IN_ATIVO_Y);
        bmdn.setCreatedAt(new Date());

        dao.create(bmdn);
    }

    @Override
    public List<BmDn> findAllActive() throws BusinessException {

        BmDn filter = new BmDn();
        filter.setInActive(IN_ATIVO_Y);

        List<BmDn> bmdns = dao.find(filter);
        if (bmdns != null && !bmdns.isEmpty())
            return bmdns;

        throw new BusinessException(EXCEPTION_NOT_FOUND);
    }

    /**
     * Verify if BmDn already exists by Name
     *
     * @param name - Name to Find
     * @return Boolean - True if Exists.
     */
    private boolean isAlreadyExists(String name) {
        return isAlreadyExists(null, name);
    }

    /**
     * Verify if BmDn already exists by Code or Name
     *
     * @param code - Code to find
     * @param name - Name to Find
     * @return Boolean - True if Exists.
     */
    private boolean isAlreadyExists(Long code, String name) {
        BmDn entity = dao.findByName(name.trim());

        if (entity == null)
            return Boolean.FALSE;

        if (code == null)
            return Boolean.TRUE;

        if (!code.equals(entity.getCode()))
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    /**
     * @param inAtivo - Valor do Atributo Ativo
     * @return String - Valor do Atributo para o Modelo
     */
    private String getInAtivoModel(String inAtivo) {
        return inAtivo.equals(Constants.ACTIVE) ? IN_ATIVO_Y : IN_ATIVO_N;
    }

    /**
     * @param inAtivo - Valor do Atributo Ativo
     * @return String - Valor do Atributo para a View
     */
    private String getInAtivoView(String inAtivo) {
        return inAtivo.equals(IN_ATIVO_Y) ? Constants.ACTIVE : Constants.INACTIVE;
    }

    /**
     * @param filter - Prepara o filter para o Modelo
     */
    private void prepareFilter(BmDn filter) {
        if (filter.getInActive() != null && !filter.getInActive().isEmpty())
            filter.setInActive(getInAtivoModel(filter.getInActive()));
    }
}