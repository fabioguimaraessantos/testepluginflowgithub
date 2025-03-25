package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CostCenterHierarchy;
import com.ciandt.pms.persistence.dao.ICostCenterHierarchyDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CostCenterHierarchyServiceTest {

    private static final String EXCEPTION_NOT_FOUND = "CostCenterHierarchy not found.";

    @InjectMocks
    private CostCenterHierarchyService costCenterHierarchyService;

    @Mock
    private ICostCenterHierarchyDao costCenterHierarchyDao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenCostCenterHierarchyCode_whenFindByCode_thenReturnCostCenterHierarchy() throws BusinessException {
        CostCenterHierarchy expectedCostCenterHierarchy = mockCostCenterHierarchy();
        when(costCenterHierarchyDao.findById(1L)).thenReturn(expectedCostCenterHierarchy);

        CostCenterHierarchy actualCostCenterHierarchy = costCenterHierarchyService.findByCode(1L);

        assertEquals(expectedCostCenterHierarchy, actualCostCenterHierarchy);
    }

    @Test
    public void givenInvalidCostCenterHierarchyCode_whenFindByCode_thenThrowBusinessException() {
        when(costCenterHierarchyDao.findById(1L)).thenReturn(null);

        try {
            costCenterHierarchyService.findByCode(1L);
        } catch (BusinessException e) {
            assertEquals(EXCEPTION_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    public void givenCostCenterHierarchyFilter_whenFindByFilter_thenReturnCostCenterHierarchyList() throws BusinessException {
        //GIVEN
        CostCenterHierarchy filter = mockCostCenterHierarchy();
        CostCenterHierarchy expected = mockCostCenterHierarchy();

        //WITH
        List<CostCenterHierarchy> expectedList = Collections.singletonList(expected);

        //WHEN
        when(costCenterHierarchyDao
                .findByFilter(Mockito.any(), Mockito.any()))
                .thenReturn(expectedList);

        //THEN
        List<CostCenterHierarchy> result = costCenterHierarchyService.findByFilter(filter);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void whenFindAllActive_thenReturnCostCenterHierarchyList() {
        when(costCenterHierarchyDao
                .findByFilter(null, Boolean.TRUE))
                .thenReturn(Arrays.asList(mockCostCenterHierarchy(), mockCostCenterHierarchy()));

        assertEquals(2, costCenterHierarchyService.findAllActive().size());
    }

    @Test
    public void givenInactiveCurrentCostCenterHierarchy_whenFindAllActiveIncludingCurrentIfInactive_thenReturnCostCenterHierarchyListWithCurrentInactive() {
        when(costCenterHierarchyDao
                .findByFilter(null, Boolean.TRUE))
                .thenReturn(Arrays.asList(mockCostCenterHierarchy(), mockCostCenterHierarchy()));
        CostCenterHierarchy currentCostCenterHierarchy = mockCostCenterHierarchy();
        currentCostCenterHierarchy.setInActive(Boolean.FALSE);

        List<CostCenterHierarchy> costCenterHierarchyList = costCenterHierarchyService.findAllActiveIncludingCurrentIfInactive(currentCostCenterHierarchy);
        assertEquals(3, costCenterHierarchyList.size());
    }

    @Test
    public void givenActiveCurrentCostCenterHierarchy_whenFindAllActiveIncludingCurrentIfInactive_thenReturnCostCenterHierarchyListWithoutDuplicatingCurrent() {
        when(costCenterHierarchyDao
                .findByFilter(null, Boolean.TRUE))
                .thenReturn(Arrays.asList(mockCostCenterHierarchy(), mockCostCenterHierarchy()));
        CostCenterHierarchy currentCostCenterHierarchy = mockCostCenterHierarchy();

        List<CostCenterHierarchy> costCenterHierarchyList = costCenterHierarchyService.findAllActiveIncludingCurrentIfInactive(currentCostCenterHierarchy);
        assertEquals(2, costCenterHierarchyList.size());
    }

    @Test
    public void givenNullCurrentCostCenterHierarchy_whenFindAllActiveIncludingCurrentIfInactive_thenReturnCostCenterHierarchyList() {
        when(costCenterHierarchyDao
                .findByFilter(null, Boolean.TRUE))
                .thenReturn(Arrays.asList(mockCostCenterHierarchy(), mockCostCenterHierarchy()));
        CostCenterHierarchy currentCostCenterHierarchy = null;

        List<CostCenterHierarchy> costCenterHierarchyList = costCenterHierarchyService.findAllActiveIncludingCurrentIfInactive(currentCostCenterHierarchy);
        assertEquals(2, costCenterHierarchyList.size());
    }

    @Test
    public void givenCostCenterHierarchyCode_whenRemove_thenRemoveCostCenterHierarchy() throws BusinessException {
        CostCenterHierarchy costCenterHierarchy = mockCostCenterHierarchy();
        when(costCenterHierarchyDao.findById(1L)).thenReturn(costCenterHierarchy);
        doNothing().when(costCenterHierarchyDao).remove(costCenterHierarchy);

        costCenterHierarchyService.remove(1L);

        verify(costCenterHierarchyDao).remove(costCenterHierarchy);
    }

    @Test
    public void givenInvalidCostCenterHierarchyCode_whenRemove_thenShouldThrowBusinessException() throws BusinessException {
        when(costCenterHierarchyDao.findById(1L)).thenReturn(null);

        try {
            costCenterHierarchyService.remove(1L);
        } catch (BusinessException e) {
            assertEquals(EXCEPTION_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    public void givenCostCenterHierarchy_whenCreate_thenSaveSuccessfully() throws BusinessException {
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();
        costCenterHierarchy.setName("Test create successfully");
        costCenterHierarchy.setOracleCode("TEST");

        when(costCenterHierarchyDao.findByFilter(costCenterHierarchy.getName(), null)).thenReturn(new ArrayList<>());

        costCenterHierarchyService.create(costCenterHierarchy);

        assertNotNull(costCenterHierarchy.getCreatedAt());
        assertNotNull(costCenterHierarchy.getUpdatedAt());
        assertEquals(Boolean.TRUE, costCenterHierarchy.getInActive());
    }

    @Test
    public void givenCostCenterHierarchyWhenAlreadyExistsCostCenterHierarchyWithSameName_whenCreate_thenThrowsBusinessException() {
        CostCenterHierarchy costCenterHierarchy = mockCostCenterHierarchy();

        when(costCenterHierarchyDao.findByFilter(costCenterHierarchy.getName(), null)).thenReturn(Arrays.asList(mockCostCenterHierarchy()));
        try {
            costCenterHierarchyService.create(costCenterHierarchy);
        } catch (BusinessException e) {
            assertEquals(Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS, e.getMessage());
        }
    }

    @Test
    public void givenCostCenterHierarchyWithInvalidOracleCode_whenCreate_thenThrowsBusinessException() throws BusinessException {
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();
        costCenterHierarchy.setName("Test create when invalid oracle code");
        costCenterHierarchy.setOracleCode("TEST@");

        when(costCenterHierarchyDao.findByFilter(costCenterHierarchy.getName(), null)).thenReturn(new ArrayList<>());

        try {
            costCenterHierarchyService.create(costCenterHierarchy);
        } catch (BusinessException e) {
            assertEquals(Constants.MSG_COST_CENTER_HIERARCHY_ORACLE_CODE_IS_INVALID, e.getMessage());
        }
    }

    @Test
    public void updateWithUniqueName_updatesSuccessfully() throws BusinessException {
        final CostCenterHierarchy costCenterHierarchySpy = spy(mockCostCenterHierarchy());
        when(costCenterHierarchyDao.findByNameOrOracleCode(costCenterHierarchySpy.getName(), costCenterHierarchySpy.getOracleCode()))
                .thenReturn(Collections.singletonList(costCenterHierarchySpy));

        final ArgumentCaptor<CostCenterHierarchy> costCenterHierarchyCaptor = ArgumentCaptor.forClass(CostCenterHierarchy.class);
        doNothing().when(costCenterHierarchyDao).update(costCenterHierarchyCaptor.capture());

        costCenterHierarchyService.update(costCenterHierarchySpy);

        verify(costCenterHierarchyDao, times(1))
                .findByNameOrOracleCode(costCenterHierarchySpy.getName(), costCenterHierarchySpy.getOracleCode());

        assertNotNull(costCenterHierarchyCaptor.getValue().getUpdatedAt());
        verify(costCenterHierarchySpy).setUpdatedAt(any(Date.class));

        verify(costCenterHierarchyDao).update(costCenterHierarchyCaptor.getValue());
    }

    @Test(expected = BusinessException.class)
    public void updateWithExistingName_throwsBusinessException() throws BusinessException {
        final CostCenterHierarchy costCenterHierarchy = mockCostCenterHierarchy();
        when(costCenterHierarchyDao.findByNameOrOracleCode(costCenterHierarchy.getName(), costCenterHierarchy.getOracleCode()))
                .thenReturn(Arrays.asList(costCenterHierarchy, new CostCenterHierarchy()));

        costCenterHierarchyService.update(costCenterHierarchy);

        verify(costCenterHierarchyDao, times(1))
                .findByNameOrOracleCode(costCenterHierarchy.getName(), costCenterHierarchy.getOracleCode());
        verify(costCenterHierarchyDao, never()).update(any(CostCenterHierarchy.class));
    }

    @Test(expected = BusinessException.class)
    public void updateWithNullCostCenterHierarchy_throwsBusinessException() throws BusinessException {
        costCenterHierarchyService.update(null);
        verify(costCenterHierarchyDao, never()).findByFilter(anyString(), anyBoolean());
        verify(costCenterHierarchyDao, never()).update(any(CostCenterHierarchy.class));
    }

    private CostCenterHierarchy mockCostCenterHierarchy() {
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();
        costCenterHierarchy.setCode(1L);
        costCenterHierarchy.setName("Test");
        costCenterHierarchy.setInActive(Boolean.TRUE);
        costCenterHierarchy.setCreatedAt(new Date());
        costCenterHierarchy.setUpdatedAt(new Date());
        costCenterHierarchy.setOracleCode("TEST");
        return costCenterHierarchy;
    }
}
