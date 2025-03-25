package com.ciandt.pms.control.jsf.components;

import com.ciandt.pms.control.jsf.components.impl.CostCenterHierarchySelect;
import com.ciandt.pms.model.CostCenterHierarchy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class CostCenterHierarchySelectTest {

    CostCenterHierarchySelect select;

    @Before
    public void initialize() {
        select = new CostCenterHierarchySelect(getEntities());
        select.setFilter("Test");
        select.setSelected("Test 2");
    }

    @Test
    public void mapTest() {
        assertEquals(2, select.map().size());
    }

    @Test
    public void mapGetTest() {
        assertEquals(11L, select.map().get("Test 2").longValue());
    }

    @Test
    public void mapGetNullTest() {
        assertNull(select.map().get("Test 3"));
    }

    @Test
    public void listTest() {
        assertEquals(2, select.list().size());
    }

    @Test
    public void listGetTest() {
        assertEquals("Test", select.list().get(0));
    }

    @Test
    public void filterTest() {
        assertEquals(10L, select.filter().longValue());
    }

    @Test
    public void filterEmptyTest() {
        select.setFilter("");
        assertEquals(0L, select.filter().longValue());
    }

    @Test
    public void valueTest() {
        assertEquals(11L, select.value().longValue());
    }

    @Test
    public void valueEmptyTest() {
        select.setSelected("");
        assertNull(select.value());
    }

    @Test
    public void selectTest() {
        select.select(11L);
        assertEquals("Test 2", select.entity().getName());
    }

    @Test
    public void selectNullTest() {
        select.select(13L);
        assertNull(select.entity());
    }

    @Test
    public void entityTest() {
        select.select(10L);
        assertEquals("Test", select.entity().getName());
        assertEquals(Boolean.TRUE, select.entity().getInActive());
        assertEquals(10L, select.entity().getCode().longValue());
    }

    private List<CostCenterHierarchy> getEntities() {

        List<CostCenterHierarchy> costCenterHierarchyList = new ArrayList<CostCenterHierarchy>();
        costCenterHierarchyList.add(create(10L, "Test", "123", Boolean.TRUE));
        costCenterHierarchyList.add(create(11L, "Test 2", "123",Boolean.FALSE));
        return costCenterHierarchyList;
    }

    private CostCenterHierarchy create(Long code, String name, String oracleCode, Boolean status) {
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();

        if (code != null)
            costCenterHierarchy.setCode(code);

        if (name != null)
            costCenterHierarchy.setName(name);

        if(oracleCode != null)
            costCenterHierarchy.setOracleCode(oracleCode);

        if (status != null)
            costCenterHierarchy.setInActive(status);

        costCenterHierarchy.setCreatedAt(new Date());
        costCenterHierarchy.setUpdatedAt(new Date());
        return costCenterHierarchy;
    }
}
