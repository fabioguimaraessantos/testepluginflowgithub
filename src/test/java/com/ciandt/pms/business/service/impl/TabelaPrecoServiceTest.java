package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow.PriceTableFlow;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PriceTableStatus;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.persistence.dao.ITabelaPrecoDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TabelaPrecoServiceTest {

    @InjectMocks
    public TabelaPrecoService service;

    @Mock
    private ITabelaPrecoDao tabelaPrecoDao;

    @Mock
    private IModuloService moduloService;

    @Mock
    private IParametroService parametroService;

    @Mock
    private IPriceTableEditorService priceTableEditorService;

    @Mock
    private IPriceTableApproverService priceTableApproverService;

    @Mock
    private IPessoaService pessoaService;

    @Mock
    private MailSenderUtil mailSender;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void cannotDeletePriceTable() {
    }

    @Test
    public void removeTabelaPreco_withDraftPriceTableAndClosingAfterTbBegin_shouldMarkIndicatorAndStatusAsDeleted() {

        // given
        Calendar priceTableBeginCalendar = DateUtil.getCalendar(1, 8, 2022);
        TabelaPreco toRemove = mockTabelaPreco();
        toRemove.setDataInicioVigencia(priceTableBeginCalendar.getTime());
        PriceTableStatus draftStatus = new PriceTableStatus();
        draftStatus.setCode(PriceTableFlow.STATUS_DRAFT);
        toRemove.setPriceTableStatus(draftStatus);

        PriceTableStatus deletedStatus = new PriceTableStatus();
        deletedStatus.setCode(PriceTableFlow.STATUS_DELETED);

        // when
        when(moduloService.dateAfterHistoryLock(toRemove.getDataInicioVigencia())).thenReturn(true);
        Boolean removed = service.removeTabelaPreco(toRemove);

        // then
        assertTrue(removed);
        assertEquals("S", toRemove.getIndicadorDeleteLogico());
        assertEquals(deletedStatus.getCode(), toRemove.getPriceTableStatus().getCode());
        assertEquals(deletedStatus.getCode(), toRemove.getPriceTableStatusFlowPms().getCode());
        verify(tabelaPrecoDao, times(0)).remove(toRemove);
    }

    @Test
    public void removeTabelaPreco_withDraftPriceTableAndClosingBeforeTbBegin_shouldMarkIndicatorAndStatusAsDeleted() {

        // given
        Calendar priceTableBeginCalendar = DateUtil.getCalendar(1, 8, 2022);
        TabelaPreco toRemove = mockTabelaPreco();
        toRemove.setDataInicioVigencia(priceTableBeginCalendar.getTime());
        PriceTableStatus draftStatus = new PriceTableStatus();
        draftStatus.setCode(PriceTableFlow.STATUS_DRAFT);
        toRemove.setPriceTableStatus(draftStatus);

        PriceTableStatus deletedStatus = new PriceTableStatus();
        deletedStatus.setCode(PriceTableFlow.STATUS_DELETED);

        // when
        when(moduloService.dateAfterHistoryLock(toRemove.getDataInicioVigencia())).thenReturn(false);
        Boolean removed = service.removeTabelaPreco(toRemove);

        // then
        assertTrue(removed);
        assertEquals("S", toRemove.getIndicadorDeleteLogico());
        assertEquals(deletedStatus.getCode(), toRemove.getPriceTableStatus().getCode());
        assertEquals(deletedStatus.getCode(), toRemove.getPriceTableStatusFlowPms().getCode());
        verify(tabelaPrecoDao, times(0)).remove(toRemove);
    }

    @Test
    public void canDeletePriceTable_withDateAfterLock_mustReturnTrue() {
        // given
        TabelaPreco tabelaPreco = mockTabelaPreco();

        // when
        when(moduloService.dateAfterHistoryLock(tabelaPreco.getDataInicioVigencia())).thenReturn(true);
        boolean canDelete = service.canDeletePriceTable(tabelaPreco);

        // then
        assertTrue(canDelete);
    }

    @Test
    public void canDeletePriceTable_withDateNotAfterLock_mustReturnFalse() {
        // given
        TabelaPreco tabelaPreco = mockTabelaPreco();

        // when
        when(moduloService.dateAfterHistoryLock(tabelaPreco.getDataInicioVigencia())).thenReturn(false);
        boolean canDelete = service.canDeletePriceTable(tabelaPreco);

        // then
        assertFalse(canDelete);
    }

    @Test
    public void markAsDeleted_withDraftStatus_mustDelete() {
        // given
        TabelaPreco pt = mockTabelaPreco();
        PriceTableStatus draft = new PriceTableStatus();
        draft.setCode(PriceTableFlow.STATUS_DRAFT);
        pt.setPriceTableStatus(draft);

        // when
        boolean deleted = service.markAsDeleted(pt);

        // then
        assertTrue(deleted);
    }

    private static TabelaPreco mockTabelaPreco() {
        TabelaPreco mock = new TabelaPreco();
        mock.setCodigoTabelaPreco(12L);
        mock.setDescricaoTabelaPreco("Price table one");
        mock.setRecalculaReceita(false);
        PriceTableStatus status = new PriceTableStatus();
        status.setCode(PriceTableFlow.STATUS_APPROVED);
        mock.setPriceTableStatus(status);
        Msa msa = new Msa();
        msa.setCodigoMsa(1L);
        msa.setNomeMsa("Msa one");
        mock.setMsa(msa);
        Moeda moeda = new Moeda();
        moeda.setCodigoMoeda(1L);
        moeda.setNomeMoeda("Real");
        mock.setMoeda(moeda);
        return mock;
    }

}