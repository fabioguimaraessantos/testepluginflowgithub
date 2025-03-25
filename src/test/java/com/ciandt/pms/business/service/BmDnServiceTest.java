package com.ciandt.pms.business.service;

import com.ciandt.pms.business.service.impl.BmDnService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.BmDn;
import com.ciandt.pms.persistence.dao.IBmDnDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class BmDnServiceTest {

    @InjectMocks
    BmDnService service;

    @Mock
    private IBmDnDAO dao;

    @Test
    public void shouldCreate() throws BusinessException {

        //GIVEN entity to Persist
        BmDn entity = create(null, "New One", null);

        //AND dao creates a new one
        Mockito.doNothing().when(dao).create(entity);
        Mockito.when(dao.findByName(entity.getName().trim())).thenReturn(null);

        //WHEN service is executed with Entity.
        service.create(entity);

        //THEN new entity should be inserted
        assertNotNull(entity.getCreatedAt());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotCreateAndThrowBusinessException() throws BusinessException {

        //GIVEN entity to Persist
        BmDn entity = create(null, "New One", null);

        //AND with mock
        BmDn mock = create(1L, "New One", "Y");
        Mockito.when(dao.findByName(entity.getName().trim())).thenReturn(mock);

        //WHEN service is executed with entity.
        service.create(entity);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldUpdate() throws BusinessException {

        //GIVEN entity to Update
        BmDn entity = create(1L, "Old One", "N");

        //AND with mock
        BmDn mock = create(1L, "New One", "Y");
        Mockito.when(dao.findByName(entity.getName().trim())).thenReturn(null);
        Mockito.when(dao.findById(entity.getCode())).thenReturn(mock);

        //AND dao updates entity
        Mockito.doNothing().when(dao).update(entity);

        //WHEN service is executed with entity.
        service.update(entity);

        //THEN entity should be updated
        assertEquals("N", mock.getInActive());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotUpdateAndThrowBusinessException() throws BusinessException {

        //GIVEN entity to Update
        BmDn entity = create(1L, "Old One", "N");

        //AND with mocks
        BmDn mock = create(2L, "Old One", "Y");
        Mockito.when(dao.findByName(entity.getName().trim())).thenReturn(mock);

        //WHEN service is executed with entity.
        service.update(entity);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldRemove() throws BusinessException {

        //GIVEN entity code
        Long code = 1L;

        //WITH mock
        BmDn mock = create(1L, "Old One", "Y");
        Mockito.when(dao.findById(code)).thenReturn(mock);

        //AND service remove entity by code
        Mockito.doNothing().when(dao).update(mock);

        //WHEN service is executed with entity.
        service.remove(code);

        //THEN entity should be removed
        assertEquals("N", mock.getInActive());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotRemoveAndThrowBusinessException() throws BusinessException {

        //GIVEN entity code
        Long code = 13L;

        //WITH mock
        Mockito.when(dao.findById(code)).thenReturn(null);

        //WHEN service is executed with code.
        service.remove(code);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldFindById() throws BusinessException {

        //GIVEN entity code
        Long code = 1L;

        //WITH existent entity
        BmDn mock = create(1L, "New One", "Y");

        //AND service find by code
        Mockito.when(dao.findById(code)).thenReturn(mock);

        //WHEN service is executed with code.
        BmDn result = service.findById(code);

        //THEN entity should be returned
        assertEquals(Long.valueOf(1L), result.getCode());
        assertEquals("New One", result.getName());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotFindByIdThrowBusinessException() throws BusinessException {

        //GIVEN entity code
        Long code = 15L;

        //AND service should thrown Business Exception
        Mockito.when(dao.findById(code)).thenReturn(null);

        //WHEN service is executed with code.
        service.findById(code);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldFindListByFilter() throws BusinessException {

        //GIVEN filter
        BmDn filter = getFilter();

        //WITH existent entities
        List<BmDn> entities = getEntities();

        //AND service find by filter
        Mockito.when(dao.find(Mockito.<BmDn>any())).thenReturn(entities);

        //WHEN service is executed with filter.
        List<BmDn> result = service.findByFilter(filter);

        //THEN result size should be 2
        assertEquals(2, result.size());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotFindListByFilterAndThrowBusinessException() throws BusinessException {

        //GIVEN filter
        BmDn filter = getFilter();

        //AND service should thrown Business Exception
        Mockito.when(dao.find(Mockito.<BmDn>any())).thenReturn(null);

        //WHEN service is executed with filter.
        service.findByFilter(filter);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldFindAllActives() throws BusinessException {

        //WITH existent entities
        List<BmDn> entities = getEntities();

        //AND service find by filter
        Mockito.when(dao.find(Mockito.<BmDn>any())).thenReturn(entities);

        //WHEN service is executed
        List<BmDn> result = service.findAllActive();

        //THEN result size should be 2
        assertEquals(2, result.size());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotFindAllActivesAndThrowBusinessException() throws BusinessException {

        //GIVEN service executing with filter should thrown Business Exception
        Mockito.when(dao.find(Mockito.<BmDn>any())).thenReturn(null);

        //WHEN service is executed.
        service.findAllActive();

        //EXPECTED BusinessException
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
