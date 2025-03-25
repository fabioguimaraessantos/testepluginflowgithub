package com.ciandt.pms.model;

import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow.PriceTableFlow;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;

public class TabelaPrecoTest {

    @Spy
    @InjectMocks
    public TabelaPreco tabelaPreco;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void delete_withDraftStatus_mustDelete() {
        // given
        TabelaPreco pt = new TabelaPreco();
        PriceTableStatus draft = new PriceTableStatus();
        draft.setCode(PriceTableFlow.STATUS_DRAFT);
        pt.setPriceTableStatus(draft);

        // when
        pt.delete();

        // then
        assertEquals("S", pt.getIndicadorDeleteLogico());
        assertEquals(PriceTableFlow.STATUS_DELETED, pt.getPriceTableStatus().getCode());
    }
}