package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICompanyErpService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.business.service.IItemFaturaService;
import com.ciandt.pms.control.jsf.bean.FaturaBean;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CompanyErp;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.vo.FaturaRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.event.ValueChangeEvent;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FaturaControllerTest {

    @InjectMocks
    private FaturaController controller;

    @Mock
    private IFaturaService faturaService;

    @Mock
    private IItemFaturaService itemFaturaService;

    @Mock
    private IEmpresaService empresaService;

    @Mock
    private ICompanyErpService companyErpService;

    @Mock
    private FaturaBean faturaBean;

    @Test
    public void shouldDocEditableWhenStatusNotApprovedAndNotSubmitted() {

        //GIVEN Invoice with status OPEN
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_OPEN, Boolean.TRUE, null);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be False.
        assertFalse(isDocEditable);
    }

    @Test
    public void shouldDocEditableWhenStatusApprovedAndIntercompany() {

        //GIVEN Invoice with status Approved and Deal Fiscal is Intercompany
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_APPROVED, Boolean.TRUE, null);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be True.
        assertTrue(isDocEditable);
    }

    @Test
    public void shouldDocEditableWhenStatusSubmittedAndIntercompany() {

        //GIVEN Invoice with status Submitted and Deal Fiscal is Intercompany
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_SUBMITTED, Boolean.TRUE, null);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be True.
        assertTrue(isDocEditable);
    }

    @Test
    public void shouldDocEditableWhenStatusApprovedAndNotIntercompanyAndIsCompanyInc() {

        //GIVEN Invoice with status Approved and Deal Fiscal is NOT Intercompany and is a US company
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_APPROVED, Boolean.FALSE, Constants.CD_EMPRESA_INC);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be TRUE.
        assertTrue(isDocEditable);
    }

    @Test
    public void shouldDocEditableWhenStatusApprovedAndNotIntercompanyAndIsUkCompany() {

        //GIVEN Invoice with status Approved and Deal Fiscal is NOT Intercompany
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_APPROVED, Boolean.FALSE, Constants.CD_EMPRESA_UK);

        //AND is a UK company
        Mockito.when(empresaService.isUk(fatura.getDealFiscal().getEmpresa())).thenReturn(Boolean.TRUE);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be TRUE.
        assertTrue(isDocEditable);
    }

    @Test
    public void shouldDocEditableWhenStatusApprovedAndNotIntercompanyAndIsPTCompany() {

        //GIVEN Invoice with status Approved and Deal Fiscal is NOT Intercompany
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_APPROVED, Boolean.FALSE, Constants.CD_EMPRESA_PT);

        //AND is a PT company
        Mockito.when(empresaService.isPortugal(fatura.getDealFiscal().getEmpresa())).thenReturn(Boolean.TRUE);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be TRUE.
        assertTrue(isDocEditable);
    }

    @Test
    public void shouldDocEditableWhenStatusApprovedAndNotIntercompanyAndIsCinqUSCompany() {

        //GIVEN Invoice with status Approved and Deal Fiscal is NOT Intercompany
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_APPROVED, Boolean.FALSE, Constants.CD_EMPRESA_CINQ_US);

        //AND is a CINQUS company
        Mockito.when(empresaService.isCinqUs(fatura.getDealFiscal().getEmpresa())).thenReturn(Boolean.TRUE);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be TRUE.
        assertTrue(isDocEditable);
    }

    @Test
    public void shouldDocEditableWhenStatusApprovedAndNotIntercompanyAndIsDextraCompany() {

        //GIVEN Invoice with status Approved and Deal Fiscal is NOT Intercompany
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_APPROVED, Boolean.FALSE, Constants.CD_EMPRESA_DEXTRA_INC);

        //AND is a DEXTRA company
        Mockito.when(empresaService.isDextraInc(fatura.getDealFiscal().getEmpresa())).thenReturn(Boolean.TRUE);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be TRUE.
        assertTrue(isDocEditable);
    }

    @Test
    public void shouldDocEditableWhenStatusApprovedAndNotIntercompanyAndIsNotErpCompany() {

        //GIVEN Invoice with status Approved and Deal Fiscal is NOT Intercompany
        Fatura fatura = getFaturaByParams(Constants.FATURA_STATUS_APPROVED, Boolean.FALSE, Constants.CD_EMPRESA_AUS);

        //AND is a AUS company
        Mockito.when(empresaService.isUk(fatura.getDealFiscal().getEmpresa())).thenReturn(Boolean.FALSE);

        //WHEN The controller is executed with fatura.
        Boolean isDocEditable = controller.canDocBeEditable(fatura);

        //THEN The result must be FALSE.
        assertFalse(isDocEditable);
    }

    @Test
    public void shouldSuccesfullyExecuteOnFaturaRowSelectionChange() {
        final FaturaRow faturaRow = mockFaturaRow();
        final HtmlSelectOneRadio component = new HtmlSelectOneRadio();
        component.getAttributes().put("faturaRow", faturaRow);

        controller.onFaturaRowSelectionChange(new ValueChangeEvent(component, false, true));

        assertFalse(faturaBean.getIsIntegrateButtonDisabled());

        verify(faturaBean, times(1)).addToFaturaRowsSelected(1);
        verify(faturaBean, times(1)).getFaturaRowSelected();
        verify(faturaBean, times(1)).addToFaturaRowsSelectedToShowXeroLineIntegrationModal(1);
    }

    @Test
    public void shouldOpenXeroIntegrationSettingsModalWhenClickIntegrateButtonAndFaturaRowsSelectedToShowXeroLineIntegrationModalGreaterThanZero() throws Exception {
        when(faturaBean.getFaturaRowsSelectedToShowXeroLineIntegrationModal()).thenReturn(1);

        final FaturaController faturaControllerSpy = spy(controller);
        faturaControllerSpy.openXeroIntegrationSettingsModalOrIntegrateAll();

        verify(faturaBean, times(1)).getFaturaRowsSelectedToShowXeroLineIntegrationModal();
        verify(faturaBean, times(1)).setShowXeroLineIntegrationModal(true);
        verify(faturaControllerSpy, times(0)).integrateAll();
    }

    @Test
    public void shouldNotOpenXeroIntegrationSettingsModalAndIntegrateAllWhenClickIntegrateButtonAndFaturaRowsSelectedToShowXeroLineIntegrationModalEqualsZero() throws Exception {
        when(faturaBean.getFaturaRowsSelectedToShowXeroLineIntegrationModal()).thenReturn(0);

        final FaturaController faturaControllerSpy = spy(controller);
        faturaControllerSpy.openXeroIntegrationSettingsModalOrIntegrateAll();

        verify(faturaBean, times(1)).getFaturaRowsSelectedToShowXeroLineIntegrationModal();
        verify(faturaBean, times(1)).setShowXeroLineIntegrationModal(false);
        verify(faturaControllerSpy, times(1)).integrateAll();
    }

    @Test
    public void shouldSetCompanyErpWhenDealFiscalParentCompanyExists() throws BusinessException {
        final Fatura fatura = mock(Fatura.class);
        final DealFiscal dealFiscal = mock(DealFiscal.class);
        final Empresa empresa = mock(Empresa.class);
        final Empresa parentEmpresa = mock(Empresa.class);
        final CompanyErp companyErp = mock(CompanyErp.class);

        when(fatura.getDealFiscal()).thenReturn(dealFiscal);
        when(dealFiscal.getEmpresa()).thenReturn(empresa);
        when(empresa.getEmpresa()).thenReturn(parentEmpresa);
        when(parentEmpresa.getCodigoEmpresa()).thenReturn(1L);
        when(faturaService.findFaturaById(anyLong())).thenReturn(fatura);
        when(companyErpService.findActiveByCompany(1L)).thenReturn(companyErp);

        controller.findById(1L);

        verify(faturaBean).setCompanyErp(companyErp);
    }

    @Test
    public void shouldNotThrowExceptionWhenCompanyErpHasMoreThanOneConfigurationAndBusinessExceptionOccurs() throws BusinessException {
        final Fatura fatura = mock(Fatura.class);
        final DealFiscal dealFiscal = mock(DealFiscal.class);
        final Empresa empresa = mock(Empresa.class);
        final Empresa parentEmpresa = mock(Empresa.class);

        when(fatura.getDealFiscal()).thenReturn(dealFiscal);
        when(dealFiscal.getEmpresa()).thenReturn(empresa);
        when(empresa.getEmpresa()).thenReturn(parentEmpresa);
        when(parentEmpresa.getCodigoEmpresa()).thenReturn(1L);
        when(faturaService.findFaturaById(anyLong())).thenReturn(fatura);
        when(companyErpService.findActiveByCompany(1L)).
                thenThrow(new BusinessException("More than one active CompanyErp found for companyCode: 1"));

        controller.findById(1L);

        verify(faturaBean, never()).setCompanyErp(any());
    }

    /**
     * Método responsável por criar nova fatura de acordo
     * com as informações dos parâmetros.
     *
     * @param status
     * @param isIntercompany
     * @param company
     * @return Fatura
     */
    private Fatura getFaturaByParams(String status, Boolean isIntercompany, Long company) {
        Fatura fatura = new Fatura();
        fatura.setIndicadorStatus(status);

        DealFiscal dealFiscal = new DealFiscal();
        dealFiscal.setIndicadorIntercompany(isIntercompany);

        if (company != null) {
            Empresa empresa = new Empresa();
            empresa.setCodigoEmpresa(company);
            dealFiscal.setEmpresa(empresa);
        }

        fatura.setDealFiscal(dealFiscal);
        return fatura;
    }

    private FaturaRow mockFaturaRow() {
        final Empresa empresa = new Empresa();
        empresa.setCodigoEmpresa(Constants.CD_EMPRESA_OCEANIA);

        final DealFiscal dealFiscal = new DealFiscal();
        dealFiscal.setEmpresa(empresa);

        final FonteReceita fonteReceita = new FonteReceita();
        fonteReceita.setCodigoFonteReceita(Constants.CONTRACTOR_REVENUE_SOURCE);

        final ItemFatura itemFatura = new ItemFatura();
        itemFatura.setFonteReceita(fonteReceita);

        final Fatura fatura = new Fatura();
        fatura.setDealFiscal(dealFiscal);
        fatura.setItemFaturas(Collections.singletonList(itemFatura));

        final FaturaRow faturaRow = new FaturaRow();
        faturaRow.setFatura(fatura);

        return faturaRow;
    }
}
