package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.persistence.dao.jpa.GrupoCustoDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrupoCustoServiceTest {

    @InjectMocks
    private GrupoCustoService service;

    @Mock
    private GrupoCustoDao dao;

    @Test
    public void givenValidCostCenterHierarchyCode_whenFindCostCentersByCostCenterHierarchy_shouldReturnListOfCostCenter() throws BusinessException {
        Long code = 1L;
        GrupoCusto grupoCusto = new GrupoCusto();
        when(dao.findByCostCenterHierarchy(code)).thenReturn(Collections.singletonList(grupoCusto));

        List<GrupoCusto> result = service.findCostCentersByCostCenterHierarchy(code);

        assertFalse(result.isEmpty());
        assertEquals(grupoCusto, result.get(0));
    }

    @Test
    public void givenInvalidCostCenterHierarchyCode_whenFindCostCentersByCostCenterHierarchy_shouldThrowBusinessException() {
        Long code = 1L;
        when(dao.findByCostCenterHierarchy(code)).thenReturn(Collections.emptyList());
        try {
            service.findCostCentersByCostCenterHierarchy(code);
        } catch (BusinessException e) {
            assertEquals("Cost Centers not found.", e.getMessage());
        }
    }
}
