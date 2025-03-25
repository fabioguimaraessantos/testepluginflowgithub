package com.ciandt.pms.control.jsf;

import com.ciandt.pms.business.service.ICidadeBaseFilialService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.control.jsf.bean.CidadeBaseFilialBean;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CidadeBaseFilial;
import com.ciandt.pms.model.CidadeBaseFilialId;
import com.ciandt.pms.model.Empresa;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CidadeBaseFilialControllerTest {

    @InjectMocks
    private CidadeBaseFilialController controller;

    @Mock
    private CidadeBaseFilialBean bean;

    @Mock
    private ICidadeBaseFilialService cidadeBaseFilialService;

    @Mock
    private ICidadeBaseService cidadeBaseService;

    @Mock
    private IEmpresaService empresaService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldPrepareSearch() {
        when(cidadeBaseService.findCidadeBaseAllActive()).thenReturn(getCidadeBaseListMock());

        final String expectedOutcome = "cidadeBaseFilial_search";
        final String actualOutcome = controller.prepareSearch();

        assertEquals(expectedOutcome, actualOutcome);
        verify(bean, times(1)).reset();
    }

    @Test
    public void shouldSearch() {
        when(bean.getFilter()).thenReturn(new CidadeBaseFilial());

        controller.search();

        verify(bean, times(1)).getFilter();
        verify(bean, times(1)).setResultList(anyListOf(CidadeBaseFilial.class));
        verify(bean, times(1)).setCurrentPageId(1L);
    }

    @Test
    public void shouldPrepareCreate() {
        when(empresaService.findEmpresaAll()).thenReturn(getEmpresaListMock());

        final String expectedOutcome = "cidadeBaseFilial_add";
        final String actualOutcome = controller.prepareCreate();

        assertEquals(expectedOutcome, actualOutcome);
    }

    @Test
    public void shouldCreate() {
        when(bean.getFilter()).thenReturn(new CidadeBaseFilial());

        final String expectedOutcome = "cidadeBaseFilial_add";
        final String actualOutcome = controller.create();

        assertEquals(expectedOutcome, actualOutcome);

        verify(cidadeBaseFilialService, times(1)).create(any(CidadeBaseFilial.class));
        verify(bean, times(1)).getFilter();
        verify(bean, times(1)).resetFilter();
    }

    @Test
    public void shouldNotCreateWhenIdAlreadyExists() {
        when(bean.getFilter()).thenReturn(new CidadeBaseFilial());
        doThrow(DataIntegrityViolationException.class).when(cidadeBaseFilialService).create(any(CidadeBaseFilial.class));

        final String actualOutcome = controller.create();

        assertNull(actualOutcome);

        verify(cidadeBaseFilialService, times(1)).create(any(CidadeBaseFilial.class));
        verify(bean, times(0)).resetTo();
    }

    @Test
    public void shouldPrepareUpdate() {
        when(bean.getTo()).thenReturn(new CidadeBaseFilial());
        when(bean.getFilter()).thenReturn(new CidadeBaseFilial());
        when(cidadeBaseFilialService.findById(any(CidadeBaseFilialId.class))).thenReturn(getCidadeBaseFilialMock());

        final String expectedOutcome = "cidadeBaseFilial_edit";
        final String actualOutcome = controller.prepareUpdate();

        assertEquals(expectedOutcome, actualOutcome);

        verify(bean, times(1)).getTo();
        verify(bean, times(1)).setUpdate(true);
        verifyZeroInteractions(cidadeBaseFilialService);
    }

    @Test
    public void shouldNotPrepareUpdateWhenCidadeBaseFilialNotExists() {
        when(bean.getCurrentRowId()).thenReturn(new CidadeBaseFilialId());
        when(cidadeBaseFilialService.findById(any(CidadeBaseFilialId.class))).thenReturn(null);

        final String actualOutcome = controller.prepareUpdate();

        assertNull(actualOutcome);

        verify(bean, times(1)).getTo();
        verifyNoMoreInteractions(bean);
        verifyZeroInteractions(cidadeBaseFilialService);
    }

    @Test
    public void shouldUpdate() {
        when(bean.isEditModalOpen()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(bean.getFilter()).thenReturn(new CidadeBaseFilial());

        final String expectedOutcome = "cidadeBaseFilial_search";
        final String actualOutcome = controller.update();

        assertEquals(expectedOutcome, actualOutcome);

        verify(bean, times(1)).getTo();
        verify(bean, times(1)).getCurrentRowId();
        verify(cidadeBaseFilialService, times(1)).findById(any(CidadeBaseFilialId.class));
        verify(cidadeBaseFilialService, times(1)).update(any(CidadeBaseFilial.class), any(CidadeBaseFilial.class));
        verify(bean, times(1)).setTo(any(CidadeBaseFilial.class));
        verify(bean, times(1)).setUpdate(false);
    }

    @Test
    public void shouldNotUpdateWhenIdAlreadyExists() {
        when(bean.getFilter()).thenReturn(new CidadeBaseFilial());
        when(bean.isEditModalOpen()).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(cidadeBaseFilialService)
                .update(any(CidadeBaseFilial.class), any(CidadeBaseFilial.class));

        final String actualOutcome = controller.update();

        assertNull(actualOutcome);

        verify(bean, times(1)).getFilter();
        verify(bean, times(1)).getCurrentRowId();
        verify(cidadeBaseFilialService, times(1)).findById(any(CidadeBaseFilialId.class));
        verify(cidadeBaseFilialService, times(1))
                .update(any(CidadeBaseFilial.class), any(CidadeBaseFilial.class));
    }

    @Test
    public void shouldCancelUpdate() {
        when(bean.isEditModalOpen()).thenReturn(true);

        final String expectedOutcome = "cidadeBaseFilial_search";
        final String actualOutcome = controller.cancelUpdate();

        assertEquals(expectedOutcome, actualOutcome);

        verify(bean, times(1)).resetFilter();
        verify(bean, times(1)).setUpdate(false);
    }

    @Test
    public void shouldRemove() {
        when(bean.isDeleteModalOpen()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(bean.getCurrentRowId()).thenReturn(new CidadeBaseFilialId());
        when(cidadeBaseFilialService.findById(any(CidadeBaseFilialId.class))).thenReturn(getCidadeBaseFilialMock());
        when(bean.getFilter()).thenReturn(new CidadeBaseFilial());

        final String expectedOutcome = "cidadeBaseFilial_search";
        final String actualOutcome = controller.remove();

        assertEquals(expectedOutcome, actualOutcome);

        verify(cidadeBaseFilialService, times(1)).findById(any(CidadeBaseFilialId.class));
        verify(cidadeBaseFilialService, times(1)).remove(any(CidadeBaseFilial.class));
        verify(bean, times(2)).isDeleteModalOpen();
        verify(bean, times(1)).setDeleteModalOpen(false);
        verify(bean, times(1)).resetTo();
    }

    @Test
    public void shouldNotRemoveWhenCidadeBaseFilialNotFound() {
        when(bean.getCurrentRowId()).thenReturn(new CidadeBaseFilialId());
        when(cidadeBaseFilialService.findById(any(CidadeBaseFilialId.class))).thenReturn(null);

        final String actualOutcome = controller.remove();

        assertNull(actualOutcome);
        assertFalse(bean.isDeleteModalOpen());

        verify(cidadeBaseFilialService, times(1)).findById(any(CidadeBaseFilialId.class));
        verify(cidadeBaseFilialService, times(0)).remove(any(CidadeBaseFilial.class));
    }

    private CidadeBaseFilial getCidadeBaseFilialMock() {
        final CidadeBaseFilial cidadeBaseFilial = new CidadeBaseFilial();
        cidadeBaseFilial.setId(new CidadeBaseFilialId(new CidadeBase(), new Empresa()));
        cidadeBaseFilial.setEmpresaFilial(new Empresa());
        cidadeBaseFilial.setEmpresaMatriz(new Empresa());
        return cidadeBaseFilial;
    }

    private List<CidadeBase> getCidadeBaseListMock() {
        final List<CidadeBase> cidadeBaseList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final CidadeBase c = new CidadeBase();
            c.setCodigoCidadeBase(Long.valueOf(i));
            c.setNomeCidadeBase("Testando");
            cidadeBaseList.add(c);
        }

        return cidadeBaseList;
    }

    private List<Empresa> getEmpresaListMock() {
        final List<Empresa> empresaList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final Empresa e = new Empresa();
            e.setCodigoEmpresa(Long.valueOf(i));
            e.setNomeEmpresa("Testando");
            empresaList.add(e);
        }

        return empresaList;
    }
}
