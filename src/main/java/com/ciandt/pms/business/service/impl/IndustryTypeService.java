package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IIndustryTypeService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.IndustryType;
import com.ciandt.pms.persistence.dao.IIndustryTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IndustryTypeService implements IIndustryTypeService {

    private static final String EXCEPTION_NOT_FOUND = "Industries types not found.";
    private static final String EXCEPTION_NOT_FOUND_BY_ID = "Industry type not found";

    private static final String IN_ATIVO_Y = "Y";
    private static final String IN_ATIVO_N = "N";

    @Autowired
    private IIndustryTypeDao dao;

    @Override
    public List<IndustryType> findByFilter(IndustryType filter) throws BusinessException {

        prepareFilter(filter);
        List<IndustryType> industries = dao.find(filter);
        if (industries == null || industries.isEmpty())
            throw new BusinessException(EXCEPTION_NOT_FOUND);

        for (IndustryType industryType : industries)
            industryType.setInActive(getInAtivoView(industryType.getInActive()));

        return industries;
    }

    @Override
    public IndustryType findById(Long id) throws BusinessException {

        IndustryType industryType = dao.findById(id);
        if (industryType == null)
            throw new BusinessException(EXCEPTION_NOT_FOUND_BY_ID);

        industryType.setInActive(getInAtivoView(industryType.getInActive()));
        return industryType;
    }

    @Override
    public void remove(Long code) throws BusinessException {

        IndustryType industryType = dao.findById(code);
        if (industryType == null)
            throw new BusinessException(EXCEPTION_NOT_FOUND_BY_ID);

        industryType.setInActive("N");
        dao.update(industryType);
    }

    @Override
    public void update(IndustryType industryType) throws BusinessException {
        if (isAlreadyExists(industryType.getCode(), industryType.getName()))
            throw new BusinessException(Constants.DEFAULT_MSG_INDUSTRY_TYPE_ALREADY_EXISTS);

        IndustryType entity = dao.findById(industryType.getCode());
        if (entity == null)
            throw new BusinessException(EXCEPTION_NOT_FOUND_BY_ID);

        entity.setName(industryType.getName());
        entity.setInActive(getInAtivoModel(industryType.getInActive()));

        dao.update(entity);
    }

    @Override
    public void create(IndustryType industryType) throws BusinessException {

        if (isAlreadyExists(industryType.getName()))
            throw new BusinessException(Constants.DEFAULT_MSG_INDUSTRY_TYPE_ALREADY_EXISTS);

        industryType.setInActive(IN_ATIVO_Y);
        industryType.setCreatedAt(new Date());

        dao.create(industryType);
    }

    @Override
    public List<IndustryType> findAllActive() throws BusinessException {

        IndustryType filter = new IndustryType();
        filter.setInActive(IN_ATIVO_Y);

        List<IndustryType> industries = dao.find(filter);
        if (industries != null && !industries.isEmpty())
            return industries;

        throw new BusinessException(EXCEPTION_NOT_FOUND);
    }

    /**
     * Verify if Industry Type already exists by Name
     *
     * @param name - Name to Find
     * @return Boolean - True if Exists.
     */
    private boolean isAlreadyExists(String name) {
        return isAlreadyExists(null, name);
    }

    /**
     * Verify if Industry Type already exists by Code or Name
     *
     * @param code - Code to find
     * @param name - Name to Find
     * @return Boolean - True if Exists.
     */
    private boolean isAlreadyExists(Long code, String name) {
        IndustryType entity = dao.findByName(name.trim());

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
    private void prepareFilter(IndustryType filter) {
        if (filter.getInActive() != null && !filter.getInActive().isEmpty())
            filter.setInActive(getInAtivoModel(filter.getInActive()));
    }
}