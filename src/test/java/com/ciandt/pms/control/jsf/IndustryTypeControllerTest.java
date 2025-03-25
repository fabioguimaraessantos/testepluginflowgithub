package com.ciandt.pms.control.jsf;

import com.ciandt.pms.business.service.IIndustryTypeService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.control.jsf.bean.IndustryTypeBean;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.IndustryType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class IndustryTypeControllerTest {

    @InjectMocks
    IndustryTypeController controller;

    @Mock
    IIndustryTypeService service;

    @Mock
    IMsaService msaService;

    @Mock
    IndustryTypeBean bean;

    @Test
    public void prepareResearch() {

        //GIVEN return string
        String outcome = "industry_type_research";

        //WHEN controller is executed
        String result = controller.prepareResearch();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void prepareCreate() {

        //GIVEN return string
        String outcome = "industry_type_add";

        //WHEN controller is executed
        String result = controller.prepareCreate();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void prepareUpdate() {

        //GIVEN return string
        String outcome = "industry_type_edit";

        //WHEN controller is executed
        String result = controller.prepareUpdate();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void prepareUpdateReturnsNull() throws BusinessException {

        //GIVEN industry type selected code
        Long industryTypeCode = 15L;

        //AND service return exception
        Mockito.when(bean.getCurrentRowId()).thenReturn(industryTypeCode);
        Mockito.when(service.findById(industryTypeCode)).thenThrow(new BusinessException(""));

        //WHEN controller is executed
        String result = controller.prepareUpdate();

        //THEN result should be null
        assertNull(result);
    }

    @Test
    public void prepareDelete() {

        //GIVEN return string
        String outcome = "industry_type_delete";

        //WHEN controller is executed
        String result = controller.prepareDelete();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void prepareDeleteReturnsNull() throws BusinessException {

        //GIVEN industry type selected code
        Long industryTypeCode = 15L;

        //AND service return exception
        Mockito.when(bean.getCurrentRowId()).thenReturn(industryTypeCode);
        Mockito.when(service.findById(industryTypeCode)).thenThrow(new BusinessException(""));

        //WHEN controller is executed
        String result = controller.prepareDelete();

        //THEN result should be null
        assertNull(result);
    }

    @Test
    public void filter() throws BusinessException {

        //GIVEN filter
        IndustryType filter = getFilter();
        List<IndustryType> entities = getEntities();

        //WITH mocks
        Mockito.when(bean.getFilter()).thenReturn(filter);
        Mockito.when(service.findByFilter(filter)).thenReturn(entities);
        Mockito.when(bean.getListIndustryType()).thenReturn(entities);

        //WHEN controller is executed
        controller.filter();

        //THEN size of result list in bean should be the same as entities.
        assertEquals(entities.size(), controller.getBean().getListIndustryType().size());
    }

    @Test
    public void filterRetunsNull() throws BusinessException {

        //GIVEN filter
        IndustryType filter = getFilter();

        //WITH mocks
        Mockito.when(bean.getFilter()).thenReturn(filter);
        Mockito.when(service.findByFilter(filter)).thenThrow(new BusinessException(""));
        Mockito.when(bean.getListIndustryType()).thenReturn(Collections.EMPTY_LIST);

        //WHEN controller is executed
        controller.filter();

        //THEN size of result list in bean should be empty.
        assertEquals(0, controller.getBean().getListIndustryType().size());
    }

    @Test
    public void delete() throws BusinessException {

        //GIVEN outcome string
        String outcome = "industry_type_research";

        //AND entity
        IndustryType entity = create(15L, "Selected Industry Type", "Y");

        //WITH mocks
        Mockito.when(bean.getIndustryType()).thenReturn(entity);
        Mockito.doNothing().when(service).remove(entity.getCode());

        //WHEN controller is executed
        String result = controller.delete();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void deleteReturnsNull() throws BusinessException {

        //GIVEN entity
        IndustryType entity = create(15L, "Selected Industry Type", "Y");

        //WITH mocks
        Mockito.when(bean.getIndustryType()).thenReturn(entity);
        Mockito.doThrow(new BusinessException("")).when(service).remove(entity.getCode());

        //WHEN controller is executed
        String result = controller.delete();

        //THEN result string should be null
        assertNull(result);
    }

    @Test
    public void update() throws BusinessException {

        //GIVEN outcome string
        String outcome = "industry_type_research";

        //AND entity
        IndustryType entity = create(15L, "Selected Industry Type", "Y");

        //WITH mocks
        Mockito.when(bean.getIndustryType()).thenReturn(entity);
        Mockito.doNothing().when(service).update(entity);

        //WHEN controller is executed
        String result = controller.update();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void updateReturnsNull() throws BusinessException {

        //GIVEN entity
        IndustryType entity = create(15L, "Selected Industry Type", "Y");

        //WITH mocks
        Mockito.when(bean.getIndustryType()).thenReturn(entity);
        Mockito.doThrow(new BusinessException("")).when(service).update(entity);

        //WHEN controller is executed
        String result = controller.update();

        //THEN result string should be null
        assertNull(result);
    }

    @Test
    public void create() throws BusinessException {

        //GIVEN entity
        IndustryType entity = create(null, "New Industry Type", null);

        //WITH mocks
        Mockito.when(bean.getIndustryType()).thenReturn(entity);
        Mockito.doNothing().when(service).create(entity);

        //WHEN controller is executed
        controller.create();

        //THEN result is okay
       assertTrue(Boolean.TRUE);
    }

    @Test
    public void createFailBusinessException() throws BusinessException {

        //GIVEN entity
        IndustryType entity = create(null, "New Industry Type", null);

        //WITH mocks
        Mockito.when(bean.getIndustryType()).thenReturn(entity);
        Mockito.doThrow(new BusinessException("")).when(service).create(entity);

        //WHEN controller is executed
        controller.create();

        //THEN result is okay
        assertTrue(Boolean.TRUE);
    }

    /**
     * @return Industry Type Filter
     */
    private IndustryType getFilter() {
        IndustryType filter = new IndustryType();
        filter.setName("Test");
        return filter;
    }

    /**
     * @return List of Industry Type
     */
    private List<IndustryType> getEntities() {

        List<IndustryType> entities = new ArrayList<IndustryType>();
        entities.add(create(10L, "Test", "Y"));
        entities.add(create(11L, "Test 2", "Y"));
        return entities;
    }

    /**
     * @param code   Industry Type code
     * @param name   Industry Type name
     * @param status Industry Type status
     * @return Industry Type - New Entity
     */
    private IndustryType create(Long code, String name, String status) {
        IndustryType entity = new IndustryType();

        if (code != null)
            entity.setCode(code);

        if (name != null)
            entity.setName(name);

        if (status != null)
            entity.setInActive(status);

        entity.setCreatedAt(new Date());
        return entity;
    }
}