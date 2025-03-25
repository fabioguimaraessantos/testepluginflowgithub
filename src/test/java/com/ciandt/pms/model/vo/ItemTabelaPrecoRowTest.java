package com.ciandt.pms.model.vo;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class ItemTabelaPrecoRowTest {

    @InjectMocks
    private ItemTabelaPrecoRow row;

    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getBackgroundColor_withEditedItem_mustReturnWarningColor() {
        // given
        ItemTabelaPrecoRow row = new ItemTabelaPrecoRow();
        row.setEdited(true);

        // when
        String warningColor = row.getBackgroundColor();

        // then
        assertEquals("#F7E6B6", warningColor);
    }

    @Test
    public void getBackgroundColor_withEditedItem_mustReturnDefaultColor() {
        // given
        ItemTabelaPrecoRow row = new ItemTabelaPrecoRow();
        row.setEdited(false);

        // when
        String defaultColor = row.getBackgroundColor();

        // then
        assertEquals("#FFFFFF", defaultColor);
    }
}