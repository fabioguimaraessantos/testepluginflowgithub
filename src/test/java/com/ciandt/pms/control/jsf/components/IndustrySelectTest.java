package com.ciandt.pms.control.jsf.components;

import com.ciandt.pms.control.jsf.components.impl.IndustrySelect;
import com.ciandt.pms.model.IndustryType;
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
public class IndustrySelectTest {

    IndustrySelect select;

    @Before
    public void initialize() {
        select = new IndustrySelect(getDtos());
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
        assertEquals("Y", select.entity().getInActive());
        assertEquals(10L, select.entity().getCode().longValue());
    }

    /**
     * @return List of Industry Type DTO
     */
    private List<IndustryType> getDtos() {

        List<IndustryType> dtos = new ArrayList<IndustryType>();
        dtos.add(createDto(10L, "Test", "Y"));
        dtos.add(createDto(11L, "Test 2", "N"));
        return dtos;
    }

    /**
     * @param code   Industry Type code
     * @param name   Industry Type name
     * @param status Industry Type status
     * @return Industry Type - New Entity
     */
    private IndustryType createDto(Long code, String name, String status) {
        IndustryType dto = new IndustryType();

        if (code != null)
            dto.setCode(code);

        if (name != null)
            dto.setName(name);

        if (status != null)
            dto.setInActive(status);

        dto.setCreatedAt(new Date());
        return dto;
    }
}
