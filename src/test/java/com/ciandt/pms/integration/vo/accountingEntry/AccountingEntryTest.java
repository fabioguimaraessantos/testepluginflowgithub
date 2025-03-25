package com.ciandt.pms.integration.vo.accountingEntry;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountingEntryTest {

    @Test
    public void getCode_withDate_mustCreateLotWithMonthOfDate() {
        AccountingEntry entry = new AccountingEntry();
        entry.setDate("01-01-2022");

        assertNotNull(entry.getCode());
        assertEquals(entry.getLotPrefix()+"01"+entry.getLotCode(), entry.getCode());
    }

    @Test
    public void getCode_withoutDate_mustCreateLotWithCurrentMonth() {
        AccountingEntry entry = new AccountingEntry();
        entry.setDate(null);
        String currentMonthStr = this.getCurrentMonth();

        assertNotNull(entry.getCode());
        assertEquals(entry.getLotPrefix()+currentMonthStr+entry.getLotCode(), entry.getCode());
    }

    private String getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int currentMonth = cal.get(Calendar.MONTH);
        return ++currentMonth < 10 ? "0" + currentMonth : String.valueOf(currentMonth);
    }
}