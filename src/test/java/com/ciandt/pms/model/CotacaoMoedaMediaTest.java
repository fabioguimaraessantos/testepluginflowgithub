package com.ciandt.pms.model;

import com.ciandt.pms.util.DateUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CotacaoMoedaMediaTest {

    @Test
    public void copyToDate_withDifferentCurrencies_shouldCopyWithIndicatorFalse() {
        // given
        CotacaoMoedaMedia origin = mockOrigin();
        Date day2 = DateUtil.addDays(origin.getDataCotacao(), 1);

        // when
        CotacaoMoedaMedia copied = CotacaoMoedaMedia.copyToDate(day2, origin);

        // then
        assertNotNull(copied);
        assertEquals("N", copied.getIndicadorNovaCotacao());
        assertEquals(day2, copied.getDataCotacao());
        assertEquals(origin.getValorCotacaoVenda(), copied.getValorCotacaoVenda());
    }

    @Test
    public void copyToDate_withSameCurrencies_shouldCopyWithIndicatorTrue() {
        // given
        CotacaoMoedaMedia origin = mockOrigin();
        Date day2 = DateUtil.addDays(origin.getDataCotacao(), 1);

        // with same currency
        origin.setMoedaPara(origin.getMoedaDe());
        origin.setValorCotacaoVenda(BigDecimal.ONE);
        origin.setValorMediaMes(BigDecimal.ONE);
        origin.setValorMediaAno(BigDecimal.ONE);

        // when
        CotacaoMoedaMedia copied = CotacaoMoedaMedia.copyToDate(day2, origin);

        // then
        assertNotNull(copied);
        assertEquals("Y", copied.getIndicadorNovaCotacao());
        assertEquals(day2, copied.getDataCotacao());
        assertEquals(BigDecimal.ONE, copied.getValorCotacaoVenda());
    }

    private CotacaoMoedaMedia mockOrigin() {
        Moeda from = new Moeda();
        from.setCodigoMoeda(1L);
        from.setNomeMoeda("Bitcoin");
        from.setSiglaMoeda("BTC");
        Moeda to = new Moeda();
        to.setCodigoMoeda(2L);
        to.setNomeMoeda("Ethereum");
        to.setSiglaMoeda("ETH");
        CotacaoMoedaMedia origin = new CotacaoMoedaMedia(100D);
        origin.setCodigoCotacaoMoedaMedia(1L);
        origin.setMoedaDe(from);
        origin.setMoedaPara(to);
        origin.setValorMediaAno(BigDecimal.valueOf(120.0));
        origin.setValorMediaMes(BigDecimal.valueOf(121.1));
        origin.setDataCotacao(DateUtil.getDateFirstDayOfMonth(new Date()));
        origin.setIndicadorNovaCotacao("Y");
        return origin;
    }
}