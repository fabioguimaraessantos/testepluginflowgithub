package com.ciandt.pms.business.service;

import com.ciandt.pms.business.service.impl.IndustryTypeService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.IndustryType;
import com.ciandt.pms.persistence.dao.IIndustryTypeDao;
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
public class IndustryTypeServiceTest {

    @InjectMocks
    IndustryTypeService service;

    @Mock
    IIndustryTypeDao dao;

    @Test
    public void shouldCreate() throws BusinessException {

        //GIVEN industry to Persist
        IndustryType entity = create(null, "New One", null);

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

        //GIVEN industry to Persist
        IndustryType entity = create(null, "New One", null);

        //AND with mock
        IndustryType mock = create(1L, "New One", "Y");
        Mockito.when(dao.findByName(entity.getName().trim())).thenReturn(mock);

        //WHEN service is executed with entity.
        service.create(entity);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldUpdate() throws BusinessException {

        //GIVEN industry to Update
        IndustryType entity = create(1L, "Old One", "N");

        //AND with mock
        IndustryType mock = create(1L, "New One", "Y");
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
        IndustryType entity = create(1L, "Old One", "N");

        //AND with mocks
        IndustryType mock = create(2L, "Old One", "Y");
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
        IndustryType mock = create(1L, "Old One", "Y");
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
        IndustryType mock = create(1L, "New One", "Y");

        //AND service find by code
        Mockito.when(dao.findById(code)).thenReturn(mock);

        //WHEN service is executed with code.
        IndustryType result = service.findById(code);

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
        IndustryType filter = getFilter();

        //WITH existent entities
        List<IndustryType> entities = getEntities();

        //AND service find by filter
        Mockito.when(dao.find(Mockito.<IndustryType>any())).thenReturn(entities);

        //WHEN service is executed with filter.
        List<IndustryType> result = service.findByFilter(filter);

        //THEN result size should be 2
        assertEquals(2, result.size());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotFindListByFilterAndThrowBusinessException() throws BusinessException {

        //GIVEN filter
        IndustryType filter = getFilter();

        //AND service should thrown Business Exception
        Mockito.when(dao.find(Mockito.<IndustryType>any())).thenReturn(null);

        //WHEN service is executed with filter.
        service.findByFilter(filter);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldFindAllActives() throws BusinessException {

        //WITH existent entities
        List<IndustryType> entities = getEntities();

        //AND service find by filter
        Mockito.when(dao.find(Mockito.<IndustryType>any())).thenReturn(entities);

        //WHEN service is executed
        List<IndustryType> result = service.findAllActive();

        //THEN result size should be 2
        assertEquals(2, result.size());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotFindAllActivesAndThrowBusinessException() throws BusinessException {

        //GIVEN service executing with filter should thrown Business Exception
        Mockito.when(dao.find(Mockito.<IndustryType>any())).thenReturn(null);

        //WHEN service is executed.
        service.findAllActive();

        //EXPECTED BusinessException
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