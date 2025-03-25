package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.control.jsf.bean.ItemTabelaPrecoBean;
import com.ciandt.pms.control.jsf.bean.MsaBean;
import com.ciandt.pms.control.jsf.bean.TabelaPrecoBean;
import com.ciandt.pms.control.jsf.interfaces.priceTable.IPriceTableFlow;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.exception.PriceTableException;
import com.ciandt.pms.model.PriceTableStatus;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class OnApprovalFlowTest {

    @InjectMocks
    private OnApprovalFlow flow;

    @Mock
    private IModuloService moduloService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateBeforeStatusChange_withNewStatusApprovedAndAfterClosing_mustPass() throws PriceTableException {
        // given
        TabelaPreco pt = createPriceTable();
        PriceTableStatus approved = new PriceTableStatus();
        approved.setCode(IPriceTableFlow.STATUS_APPROVED);
        PriceTablePojo pojo = getPriceTablePojo();

        // when
        when(moduloService.dateAfterHistoryLock(pt.getDataInicioVigencia())).thenReturn(true);
        flow.validateBeforeStatusChange(approved, pt, pojo);
    }

    @Test
    public void validateBeforeStatusChange_withSameStatusAndAfterClosing_mustPass() throws PriceTableException {
        // given
        TabelaPreco pt = createPriceTable();
        PriceTableStatus approved = new PriceTableStatus();
        approved.setCode(IPriceTableFlow.STATUS_ON_APRROVAL);
        PriceTablePojo pojo = getPriceTablePojo();

        // when
        when(moduloService.dateAfterHistoryLock(pt.getDataInicioVigencia())).thenReturn(true);
        flow.validateBeforeStatusChange(approved, pt, pojo);
    }

    @Test
    public void validateBeforeStatusChange_withSameStatusAndNotAfterClosing_mustPass() throws PriceTableException {
        // given
        TabelaPreco pt = createPriceTable();
        PriceTableStatus approved = new PriceTableStatus();
        approved.setCode(IPriceTableFlow.STATUS_ON_APRROVAL);
        PriceTablePojo pojo = getPriceTablePojo();

        // when
        when(moduloService.dateAfterHistoryLock(pt.getDataInicioVigencia())).thenReturn(false);
        flow.validateBeforeStatusChange(approved, pt, pojo);
    }

    private static PriceTablePojo getPriceTablePojo() {
        TabelaPrecoBean tabelaPrecoBean = new TabelaPrecoBean();
        ItemTabelaPrecoBean itemTabelaPrecoBean = new ItemTabelaPrecoBean();
        MsaBean bean = new MsaBean();

        PriceTablePojo pojo = PriceTablePojo.builder()
                .tabelaPrecoBean(tabelaPrecoBean)
                .itemTabelaPrecoBean(itemTabelaPrecoBean)
                .msa(bean.getTo())
                .login(LoginUtil.getLoggedUsername())
                .build();
        return pojo;
    }

    private static TabelaPreco createPriceTable() {
        TabelaPreco pt = new TabelaPreco();
        pt.setCodigoTabelaPreco(1L);
        pt.setDescricaoTabelaPreco("Price Table");
        pt.setDataInicioVigencia(DateUtil.getCalendar(1, 7, 2022).getTime());
        return pt;
    }
}