package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaGrupoCusto;
import com.ciandt.pms.model.PessoaGrupoCustoMG;
import com.ciandt.pms.persistence.dao.IPessoaGrupoCustoDeleteDao;
import com.ciandt.pms.persistence.dao.IPessoaGrupoCustoMGDao;
import com.ciandt.pms.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.Assert.*;

public class PessoaGrupoCustoServiceTest {

    @InjectMocks
    private PessoaGrupoCustoService service;

    @Mock
    private IModuloService moduloService;

    @Mock
    private IPessoaGrupoCustoMGDao dao;
    @Mock
    private IPessoaGrupoCustoDeleteDao daoDelete;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void removePessoaGrupoCusto_withNotFoundAssociation_mustReturnFalse() {
        // given
        PessoaGrupoCusto input = createInput();
        String ticketId = "123456";

        // when
        Mockito.when(dao.findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio())).thenReturn(null);
        boolean removed = service.removePessoaGrupoCusto(input, ticketId);

        // then
        Mockito.verify(dao).findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio());
        assertFalse(removed);
    }

    @Test
    public void removePessoaGrupoCusto_withAssociationBeforeClosingDate_mustReturnFalse() {
        // given
        PessoaGrupoCusto input = createInput();
        PessoaGrupoCustoMG existent = createExistent(input);
        String ticketId = "123456";

        // when
        Mockito.when(dao.findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio())).thenReturn(existent);
        Mockito.when(moduloService.getClosingDateHistoryLock())
                .thenReturn(DateUtil.addMonths(existent.getDataInicio(), 1));
        boolean removed = service.removePessoaGrupoCusto(input, ticketId);

        // then
        Mockito.verify(dao).findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio());
        Mockito.verify(moduloService).getClosingDateHistoryLock();
        assertFalse(removed);
    }

    @Test
    public void removePessoaGrupoCusto_withAssociationEqualClosingDate_mustReturnFalse() {
        // given
        PessoaGrupoCusto input = createInput();
        PessoaGrupoCustoMG existent = createExistent(input);
        String ticketId = "123456";

        // when
        Mockito.when(dao.findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio())).thenReturn(existent);
        Mockito.when(moduloService.getClosingDateHistoryLock())
                .thenReturn(existent.getDataInicio());
        boolean removed = service.removePessoaGrupoCusto(input, ticketId);

        // then
        Mockito.verify(dao).findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio());
        Mockito.verify(moduloService).getClosingDateHistoryLock();
        assertFalse(removed);
    }

    @Test
    public void removePessoaGrupoCusto_withAssociationAfterClosingDate_mustReturnTrue() {
        // given
        PessoaGrupoCusto input = createInput();
        PessoaGrupoCustoMG existent = createExistent(input);
        String ticketId = "123456";

        // when
        Mockito.when(dao.findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio()))
                .thenReturn(existent);
        Mockito.when(moduloService.getClosingDateHistoryLock())
                .thenReturn(DateUtil.addMonths(existent.getDataInicio(), -1));

        boolean removed = service.removePessoaGrupoCusto(input, ticketId);

        // then
        Mockito.verify(dao).findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio());
        Mockito.verify(moduloService).getClosingDateHistoryLock();
        assertTrue(removed);
    }

    @Test
    public void removePessoaGrupoCusto_withPreviousAndWithoutNext_mustUpdatePreviousFinalDateToNull() {
        // given
        PessoaGrupoCusto input = createInput();
        PessoaGrupoCustoMG existent = createExistent(input);
        String ticketId = "123456";

        PessoaGrupoCustoMG previous = new PessoaGrupoCustoMG();
        previous.setGrupoCusto(new GrupoCusto(2L, "CostCenter2"));
        previous.setDataInicio(DateUtil.addMonths(existent.getDataInicio(), -5));
        previous.setDataFim(DateUtil.addMonths(existent.getDataInicio(), -1));

        // when
        Mockito.when(dao.findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio())).thenReturn(existent);
        Mockito.when(moduloService.getClosingDateHistoryLock())
                .thenReturn(DateUtil.addMonths(existent.getDataInicio(), -1));
        Mockito.when(dao.findPrevious(existent)).thenReturn(previous);
        Mockito.when(dao.findNext(existent)).thenReturn(null);
        boolean removed = service.removePessoaGrupoCusto(input, ticketId);

        // then
        Mockito.verify(dao).findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio());
        Mockito.verify(moduloService).getClosingDateHistoryLock();
        Mockito.verify(dao).update(previous);
        assertNull(previous.getDataFim());
        assertTrue(removed);
    }

    @Test
    public void removePessoaGrupoCusto_withPreviousAndNext_mustUpdatPreviousFinalDate() {
        // given
        PessoaGrupoCusto input = createInput();
        PessoaGrupoCustoMG existent = createExistent(input);
        String ticketId = "123456";

        PessoaGrupoCustoMG previous = new PessoaGrupoCustoMG();
        previous.setGrupoCusto(new GrupoCusto(2L, "CostCenter2"));
        previous.setDataInicio(DateUtil.addMonths(existent.getDataInicio(), -5));
        previous.setDataFim(DateUtil.addMonths(existent.getDataInicio(), -1));

        PessoaGrupoCustoMG next = new PessoaGrupoCustoMG();
        next.setGrupoCusto(new GrupoCusto(3L, "CostCenter3"));
        next.setDataInicio(DateUtil.addMonths(existent.getDataInicio(), 5));

        // when
        Mockito.when(dao.findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio())).thenReturn(existent);
        Mockito.when(moduloService.getClosingDateHistoryLock())
                .thenReturn(DateUtil.addMonths(existent.getDataInicio(), -1));
        Mockito.when(dao.findPrevious(existent)).thenReturn(previous);
        Mockito.when(dao.findNext(existent)).thenReturn(next);
        boolean removed = service.removePessoaGrupoCusto(input, ticketId);

        // then
        Mockito.verify(dao).findByPessoaAndGrupoCustoAndStartDate(input.getPessoa().getCodigoPessoa(),
                input.getGrupoCusto().getCodigoGrupoCusto(), input.getDataInicio());
        Mockito.verify(moduloService).getClosingDateHistoryLock();
        Mockito.verify(dao).update(previous);
        assertEquals(DateUtil.addMonths(next.getDataInicio(), -1), previous.getDataFim());
        assertTrue(removed);
    }

    private PessoaGrupoCusto createInput() {
        PessoaGrupoCusto input = new PessoaGrupoCusto();
        input.setCodigoPessoaGrupoCusto(1L);
        input.setPessoa(new Pessoa(1L, "login"));
        input.setGrupoCusto(new GrupoCusto(1L, "Cost Center"));
        input.setDataInicio(DateUtil.getDateFirstDayOfMonth(new Date()));
        return input;
    }

    private PessoaGrupoCustoMG createExistent(PessoaGrupoCusto input) {
        PessoaGrupoCustoMG existent = new PessoaGrupoCustoMG();
        existent.setGrupoCusto(input.getGrupoCusto());
        existent.setPessoa(input.getPessoa());
        existent.setDataInicio(input.getDataInicio());
        return existent;
    }
}