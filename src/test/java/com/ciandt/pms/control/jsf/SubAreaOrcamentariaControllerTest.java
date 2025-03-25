package com.ciandt.pms.control.jsf;

import com.ciandt.pms.business.service.IAreaOrcamentariaService;
import com.ciandt.pms.business.service.IGrupoCustoAreaOrcamentariaService;
import com.ciandt.pms.business.service.IModeloAreaOrcamentariaService;
import com.ciandt.pms.business.service.IModeloService;
import com.ciandt.pms.control.jsf.bean.SubAreaOrcamentariaBean;
import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.Modelo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubAreaOrcamentariaControllerTest {


    @InjectMocks
    private SubAreaOrcamentariaController controller;
    @Mock
    private SubAreaOrcamentariaBean bean;
    @Mock
    private IAreaOrcamentariaService areaOrcamentariaService;
    @Mock
    private IGrupoCustoAreaOrcamentariaService grupoCustoAreaOrcamentariaService;
    @Mock
    private IModeloAreaOrcamentariaService modeloAreaOrcamentariaService;
    @Mock
    private IModeloService modeloService;


    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

    }
    @Test
    public void testPrepareCreate() {

        String outcome = controller.prepareCreate();

        assertEquals("subAreaOrcamentaria_add", outcome);
        verify(bean, times(1)).reset();
        verify(bean, times(1)).setIsUpdate(Boolean.FALSE);
    }

    @Test
    public void testPrepareResearch() {

        String outcome = controller.prepareResearch();

        assertEquals("subAreaOrcamentaria_research", outcome);
        verify(bean, times(1)).reset();
    }

    @Test
    public void testGetAreasOrcamentariasPais() {

        List<AreaOrcamentaria> areasOrcamentarias = new ArrayList<>();
        when(areaOrcamentariaService.findAllActiveAreaOrcamentariaPai()).thenReturn(areasOrcamentarias);


        controller.getAreasOrcamentariasPais();

        verify(areaOrcamentariaService, times(1)).findAllActiveAreaOrcamentariaPai();
        verify(bean, times(1)).setAreaOrcamentariaPais(areasOrcamentarias);
    }

    @Test
    public void testGetModelos() {

        List<Modelo> modelos = new ArrayList<>();
        when(modeloService.findAll()).thenReturn(modelos);

        controller.getModelos();

        verify(modeloService, times(1)).findAll();
        verify(bean, times(1)).setModelos(modelos);
    }

}