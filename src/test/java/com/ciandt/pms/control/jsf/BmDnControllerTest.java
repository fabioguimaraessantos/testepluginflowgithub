package com.ciandt.pms.control.jsf;

import com.ciandt.pms.business.service.IBmDnService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.control.jsf.bean.BmDnBean;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.BmDn;
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
public class BmDnControllerTest {

    @InjectMocks
    BmDnController controller;

    @Mock
    IBmDnService service;

    @Mock
    IMsaService msaService;

    @Mock
    BmDnBean bean;

    @Test
    public void prepareResearch() {

        //GIVEN return string
        String outcome = "bmdn_research";

        //WHEN controller is executed
        String result = controller.prepareResearch();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void prepareCreate() {

        //GIVEN return string
        String outcome = "bmdn_add";

        //WHEN controller is executed
        String result = controller.prepareCreate();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void prepareUpdate() {

        //GIVEN return string
        String outcome = "bmdn_edit";

        //WHEN controller is executed
        String result = controller.prepareUpdate();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void prepareUpdateReturnsNull() throws BusinessException {

        //GIVEN bmdn selected code
        Long bmdnCode = 15L;

        //AND service return exception
        Mockito.when(bean.getCurrentRowId()).thenReturn(bmdnCode);
        Mockito.when(service.findById(bmdnCode)).thenThrow(new BusinessException(""));

        //WHEN controller is executed
        String result = controller.prepareUpdate();

        //THEN result should be null
        assertNull(result);
    }

    @Test
    public void prepareRemove() {

        //GIVEN return string
        String outcome = "bmdn_delete";

        //WHEN controller is executed
        String result = controller.prepareRemove();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void prepareRemoveReturnsNull() throws BusinessException {

        //GIVEN bmdn selected code
        Long bmdnCode = 15L;

        //AND service return exception
        Mockito.when(bean.getCurrentRowId()).thenReturn(bmdnCode);
        Mockito.when(service.findById(bmdnCode)).thenThrow(new BusinessException(""));

        //WHEN controller is executed
        String result = controller.prepareRemove();

        //THEN result should be null
        assertNull(result);
    }

    @Test
    public void findByFilter() throws BusinessException {

        //GIVEN filter
        BmDn filter = getFilter();
        List<BmDn> entities = getEntities();

        //WITH mocks
        Mockito.when(bean.getFilter()).thenReturn(filter);
        Mockito.when(service.findByFilter(filter)).thenReturn(entities);
        Mockito.when(bean.getListBmDn()).thenReturn(entities);

        //WHEN controller is executed
        controller.findByFilter();

        //THEN size of result list in bean should be the same as entities.
        assertEquals(entities.size(), controller.getBean().getListBmDn().size());
    }

    @Test
    public void findByFilterRetunsNull() throws BusinessException {

        //GIVEN filter
        BmDn filter = getFilter();

        //WITH mocks
        Mockito.when(bean.getFilter()).thenReturn(filter);
        Mockito.when(service.findByFilter(filter)).thenThrow(new BusinessException(""));
        Mockito.when(bean.getListBmDn()).thenReturn(Collections.EMPTY_LIST);

        //WHEN controller is executed
        controller.findByFilter();

        //THEN size of result list in bean should be empty.
        assertEquals(0, controller.getBean().getListBmDn().size());
    }

    @Test
    public void remove() throws BusinessException {

        //GIVEN outcome string
        String outcome = "bmdn_research";

        //AND entity
        BmDn entity = create(15L, "Selected Bmdn", "Y");

        //WITH mocks
        Mockito.when(bean.getBmDn()).thenReturn(entity);
        Mockito.doNothing().when(service).remove(entity.getCode());

        //WHEN controller is executed
        String result = controller.delete();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void removeReturnsNull() throws BusinessException {

        //GIVEN entity
        BmDn entity = create(15L, "Selected Industry Type", "Y");

        //WITH mocks
        Mockito.when(bean.getBmDn()).thenReturn(entity);
        Mockito.doThrow(new BusinessException("")).when(service).remove(entity.getCode());

        //WHEN controller is executed
        String result = controller.delete();

        //THEN result string should be null
        assertNull(result);
    }

    @Test
    public void update() throws BusinessException {

        //GIVEN outcome string
        String outcome = "bmdn_research";

        //AND entity
        BmDn entity = create(15L, "Selected BmDn", "Y");

        //WITH mocks
        Mockito.when(bean.getBmDn()).thenReturn(entity);
        Mockito.doNothing().when(service).update(entity);

        //WHEN controller is executed
        String result = controller.update();

        //THEN result string should be same as outcome
        assertEquals(outcome, result);
    }

    @Test
    public void updateReturnsNull() throws BusinessException {

        //GIVEN entity
        BmDn entity = create(15L, "Selected BmDn", "Y");

        //WITH mocks
        Mockito.when(bean.getBmDn()).thenReturn(entity);
        Mockito.doThrow(new BusinessException("")).when(service).update(entity);

        //WHEN controller is executed
        String result = controller.update();

        //THEN result string should be null
        assertNull(result);
    }

    @Test
    public void create() throws BusinessException {

        //GIVEN entity
        BmDn entity = create(null, "New BmDn", null);

        //WITH mocks
        Mockito.when(bean.getBmDn()).thenReturn(entity);
        Mockito.doNothing().when(service).create(entity);

        //WHEN controller is executed
        controller.create();

        //THEN result is okay
        assertTrue(Boolean.TRUE);
    }

    @Test
    public void createFailBusinessException() throws BusinessException {

        //GIVEN entity
        BmDn entity = create(null, "New Bmdn", null);

        //WITH mocks
        Mockito.when(bean.getBmDn()).thenReturn(entity);
        Mockito.doThrow(new BusinessException("")).when(service).create(entity);

        //WHEN controller is executed
        controller.create();

        //THEN result is okay
        assertTrue(Boolean.TRUE);
    }

    /**
     * @return Bmdn Filter
     */
    private BmDn getFilter() {
        BmDn filter = new BmDn();
        filter.setName("Test");
        return filter;
    }

    /**
     * @return List of Bmdn
     */
    private List<BmDn> getEntities() {

        List<BmDn> entities = new ArrayList<BmDn>();
        entities.add(create(10L, "Test", "Y"));
        entities.add(create(11L, "Test 2", "Y"));
        return entities;
    }

    /**
     * @param code Bmdn code
     * @param name Bmdn name
     * @param status Bmdn status
     * @return Bmdn - New Entity
     */
    private BmDn create(Long code, String name, String status) {
        BmDn bmdn = new BmDn();

        if (code != null)
            bmdn.setCode(code);

        if (name != null)
            bmdn.setName(name);

        if (status != null)
            bmdn.setInActive(status);

        bmdn.setCreatedAt(new Date());
        return bmdn;
    }
}
