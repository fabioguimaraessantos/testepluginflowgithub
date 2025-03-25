package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ITabelaPrecoService;
import com.ciandt.pms.control.jsf.interfaces.priceTable.IPriceTableFlow;
import com.ciandt.pms.control.jsf.interfaces.priceTable.util.FlowUtil;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.model.PriceTableStatus;
import com.ciandt.pms.model.TabelaPreco;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PriceTableFlowTest {

    @InjectMocks
    public PriceTableFlow flow = new PriceTableFlow() {
        @Override
        public String outcome(boolean isApprover) {
            return isApprover ? Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED : Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW;
        }

        @Override
        protected PriceTableStatus status() {
            return new PriceTableStatus();
        }

        @Override
        protected String description() {
            return "Description";
        }

        @Override
        protected List<String> findAcronyms(PriceTableMemberType member) {
            return null;
        }
    };

    @Mock
    public ITabelaPrecoService priceTableService;

    @Mock
    public FlowUtil util;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create_withPriceTable_mustCallServiceAfterSetDefaults() {
        // given
        TabelaPreco pt = new TabelaPreco();
        pt.setCodigoTabelaPreco(1L);
        pt.setDescricaoTabelaPreco("Tabela Preço 1");

        // when
        when(priceTableService.createTabelaPreco(pt)).thenReturn(true);
        boolean created = flow.create(pt);

        // then
        assertTrue(created);
        assertEquals("N", pt.getIndicadorDeleteLogico());
        assertEquals(IPriceTableFlow.STATUS_DRAFT, pt.getPriceTableStatus().getCode());
        assertEquals(IPriceTableFlow.STATUS_DRAFT, pt.getPriceTableStatusFlowPms().getCode());
        verify(priceTableService).createTabelaPreco(pt);
    }

    @Test
    public void create_withNullPriceTable_mustNotCallServiceAndReturnFalse() {
        // given
        TabelaPreco pt = null;

        // when
        when(priceTableService.createTabelaPreco(pt)).thenReturn(true);

        // then
        assertFalse(flow.create(pt));
        verify(priceTableService, times(0)).createTabelaPreco(pt);
    }
}