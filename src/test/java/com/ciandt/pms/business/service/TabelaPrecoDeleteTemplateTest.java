package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.TabelaPreco;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TabelaPrecoDeleteTemplateTest {

    @Spy
    @InjectMocks
    public TabelaPrecoDeleteTemplate template = mockTemplate(true);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void delete_withValidEntity_mustDelete() throws BusinessException {
        // given
        TabelaPreco pt = new TabelaPreco();
        pt.setCodigoTabelaPreco(1L);
        pt.setDescricaoTabelaPreco("Price Table");
        pt.setRecalculaReceita(false);

        // when
        boolean deleted = template.delete(pt);

        // then
        assertTrue(deleted);
        verify(template).canDeletePriceTable(pt);
        verify(template).markAsDeleted(pt);
    }

    @Test(expected = BusinessException.class)
    public void delete_withInvalidEntity_mustThrowExeption() throws BusinessException {
        // given
        TabelaPreco pt = new TabelaPreco();
        pt.setCodigoTabelaPreco(1L);
        pt.setDescricaoTabelaPreco("Price Table");
        pt.setRecalculaReceita(false);

        // when
        template = mockTemplate(false);
        boolean deleted = template.delete(pt);

        // then
        assertFalse(deleted);
        verify(template).canDeletePriceTable(pt);
        verify(template, times(0)).markAsDeleted(pt);
    }

    public TabelaPrecoDeleteTemplate mockTemplate(final boolean value) {
        return new TabelaPrecoDeleteTemplate() {
            @Override
            protected boolean canDeletePriceTable(TabelaPreco entity) {
                return value;
            }

            @Override
            protected boolean markAsDeleted(TabelaPreco entity) {
                return value;
            }
        };
    }
}