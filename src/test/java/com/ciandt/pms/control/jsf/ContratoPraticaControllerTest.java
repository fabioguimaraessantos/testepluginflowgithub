package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.ContratoPraticaBean;
import com.ciandt.pms.integration.queue.RecalculateMapProducer;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.TipoModeloNegocio;
import com.ciandt.pms.model.vo.combo.ComboBox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContratoPraticaControllerTest {

    @InjectMocks
    ContratoPraticaController controller;

    @Mock
    IContratoPraticaService contratoPraticaService;

    @Mock
    IDealFiscalService dealFiscalService;

    @Mock
    ITecnologiaContratoPraticaService tecnologiaContratoPraticaService;

    @Mock
    IModuloService moduloService;

    @Mock
    IAlocacaoService alocacaoService;

    @Mock
    IConvergenciaService convergenciaService;

    @Mock
    ITipoModeloNegocioService tipoModeloNegocioService;

    @Mock
    ContratoPraticaBean bean;

    @Mock
    RecalculateMapProducer recalculateMapProducer;

    @Test
    public void updateContratoPraticaDealFiscalUpdatesSuccessfully() {
        ContratoPratica contratoPratica = new ContratoPratica();
        contratoPratica.setMsa(new Msa());
        contratoPratica.setTipoModeloNegocio(new TipoModeloNegocio());
        DealFiscal dealFiscal = new DealFiscal();
        dealFiscal.setIndicadorStatus(Constants.ACTIVE);

        when(bean.getTo()).thenReturn(contratoPratica);
        when(bean.getSelectedDealFiscalPickList()).thenReturn(new String[]{"1"});
        when(dealFiscalService.findDealFiscalById(anyLong())).thenReturn(dealFiscal);
        when(contratoPraticaService.findContratoPraticaById(anyLong())).thenReturn(contratoPratica);
        when(tipoModeloNegocioService.findTipoModeloNegocioById(anyLong())).thenReturn(new TipoModeloNegocio());
        when(bean.getGerenteCombo()).thenReturn(new ComboBox<>());
        when(bean.getAprovadorCombo()).thenReturn(new ComboBox<>());
        when(bean.getGrupoCustoCombo()).thenReturn(new ComboBox<>());

        controller.saveConfigurationTab();

        verify(contratoPraticaService).updateCpraticaDfiscal(any(), any());
    }

    @Test
    public void updateContratoPraticaDealFiscalDoesNotUpdateWhenDealFiscalInactive() {
        when(bean.getTo()).thenReturn(new ContratoPratica());
        when(bean.getSelectedDealFiscalPickList()).thenReturn(new String[]{"1"});
        DealFiscal dealFiscal = new DealFiscal();
        dealFiscal.setIndicadorStatus("I");
        when(dealFiscalService.findDealFiscalById(anyLong())).thenReturn(dealFiscal);
        controller.saveConfigurationTab();
        verify(contratoPraticaService, never()).updateCpraticaDfiscal(any(), any());
    }
}