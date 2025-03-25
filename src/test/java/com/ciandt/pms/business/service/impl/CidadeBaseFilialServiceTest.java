package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CidadeBaseFilial;
import com.ciandt.pms.model.CidadeBaseFilialId;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.persistence.dao.ICidadeBaseFilialDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CidadeBaseFilialServiceTest {

    @InjectMocks
    private CidadeBaseFilialService service;

    @Mock
    private ICidadeBaseFilialDao dao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindAll() {
        final List<CidadeBaseFilial> expectedResult = getCidadeBaseFilialListMock();
        when(dao.findAll()).thenReturn(expectedResult);

        final List<CidadeBaseFilial> actualResult = service.findAll();

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        assertEquals(5, actualResult.size());
        verify(dao, times(1)).findAll();
    }

    @Test
    public void shouldFindById() {
        final CidadeBaseFilial expectedResult = getCidadeBaseFilialMock();
        when(dao.findById(any(CidadeBaseFilialId.class))).thenReturn(expectedResult);

        final CidadeBaseFilial actualResult = service.findById(new CidadeBaseFilialId());

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(dao, times(1)).findById(any(CidadeBaseFilialId.class));
    }

    @Test
    public void shouldFindByFilter() throws BusinessException {
        final List<CidadeBaseFilial> expectedResult = getCidadeBaseFilialListMock();
        when(dao.findByFilter(any(CidadeBaseFilial.class))).thenReturn(expectedResult);

        final List<CidadeBaseFilial> actualResult = service.findByFilter(new CidadeBaseFilial());

        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        assertEquals(5, actualResult.size());
        verify(dao, times(1)).findByFilter(any(CidadeBaseFilial.class));
    }

    @Test
    public void shouldFindByFilterAndThrowBusinessExceptionWhenListIsEmpty() {
        when(dao.findByFilter(any(CidadeBaseFilial.class))).thenReturn(Collections.emptyList());

        try {
            service.findByFilter(new CidadeBaseFilial());
        } catch (final BusinessException e) {
            assertThat(e.getMessage(), is(Constants.MSG_NOT_FOUND_CIDADE_BASE_FILIAL));
            verify(dao, times(1)).findByFilter(any(CidadeBaseFilial.class));
        }
    }

    @Test
    public void shouldCreate() {
        final ArgumentCaptor<CidadeBaseFilial> valueCapture = ArgumentCaptor.forClass(CidadeBaseFilial.class);
        doNothing().when(dao).create(valueCapture.capture());

        final CidadeBaseFilial cidadeBaseFilial = new CidadeBaseFilial();
        service.create(cidadeBaseFilial);

        assertEquals(cidadeBaseFilial, valueCapture.getValue());
        verify(dao, times(1)).create(any(CidadeBaseFilial.class));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotCreateWhenIdAlreadyExists() {
        doThrow(DataIntegrityViolationException.class).when(dao).create(any(CidadeBaseFilial.class));
        service.create(new CidadeBaseFilial());
        verify(dao, times(1)).create(any(CidadeBaseFilial.class));
    }

    @Test
    public void shouldUpdate() {
        doNothing().when(dao).update(any(CidadeBaseFilial.class));
        service.update(new CidadeBaseFilial(), new CidadeBaseFilial());
        verify(dao, times(1)).update(any(CidadeBaseFilial.class));
    }

    @Test
    public void shouldRemove() {
        final ArgumentCaptor<CidadeBaseFilial> valueCapture = ArgumentCaptor.forClass(CidadeBaseFilial.class);
        doNothing().when(dao).remove(valueCapture.capture());

        final CidadeBaseFilial cidadeBaseFilial = new CidadeBaseFilial();
        service.remove(cidadeBaseFilial);

        assertEquals(cidadeBaseFilial, valueCapture.getValue());
        verify(dao, times(1)).remove(any(CidadeBaseFilial.class));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotRemoveWhenDaoThrowsError() {
        doThrow(DataIntegrityViolationException.class).when(dao).remove(any(CidadeBaseFilial.class));

        service.remove(new CidadeBaseFilial());

        fail();
    }


    private CidadeBaseFilial getCidadeBaseFilialMock() {
        final CidadeBaseFilial cidadeBaseFilial = new CidadeBaseFilial();
        cidadeBaseFilial.setId(new CidadeBaseFilialId(new CidadeBase(), new Empresa()));
        cidadeBaseFilial.setEmpresaFilial(new Empresa());
        cidadeBaseFilial.setEmpresaMatriz(new Empresa());
        return cidadeBaseFilial;
    }

    private List<CidadeBaseFilial> getCidadeBaseFilialListMock() {
        final List<CidadeBaseFilial> cidadeBaseFilialList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final CidadeBaseFilial c = new CidadeBaseFilial();
            c.setId(new CidadeBaseFilialId(new CidadeBase(), new Empresa()));
            c.setEmpresaFilial(new Empresa());
            c.setEmpresaMatriz(new Empresa());
            cidadeBaseFilialList.add(c);
        }

        return cidadeBaseFilialList;
    }
}
