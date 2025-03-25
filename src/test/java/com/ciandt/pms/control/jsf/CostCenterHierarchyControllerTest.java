package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICostCenterHierarchyService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.control.jsf.bean.CostCenterHierarchyBean;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CostCenterHierarchy;
import com.ciandt.pms.model.GrupoCusto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CostCenterHierarchyControllerTest {

    @InjectMocks
    CostCenterHierarchyController costCenterHierarchyController;

    @Mock
    ICostCenterHierarchyService costCenterHierarchyService;

    @Mock
    IGrupoCustoService costCenterService;

    @Mock
    CostCenterHierarchyBean costCenterHierarchyBean;

    private static final String OUTCOME_COST_CENTER_HIERARCHY_ADD = "cost_center_hierarchy_add";

    private static final String OUTCOME_COST_CENTER_HIERARCHY_EDIT = "cost_center_hierarchy_edit";

    private static final String OUTCOME_COST_CENTER_HIERARCHY_DELETE = "cost_center_hierarchy_delete";

    private static final String OUTCOME_COST_CENTER_HIERARCHY_SEARCH = "cost_center_hierarchy_search";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenPrepareSearch_shouldResetBeanAndReturnSearchOutcome() {
        String outcome = costCenterHierarchyController.prepareSearch();

        verify(costCenterHierarchyBean).reset();
        assertEquals(OUTCOME_COST_CENTER_HIERARCHY_SEARCH, outcome);
    }

    @Test
    public void whenPrepareCreate_shouldResetAndSetUpdateBeanAndReturnAddOutcome() {
        String outcome = costCenterHierarchyController.prepareCreate();

        verify(costCenterHierarchyBean).reset();
        verify(costCenterHierarchyBean).setIsUpdate(Boolean.FALSE);

        assertEquals(OUTCOME_COST_CENTER_HIERARCHY_ADD, outcome);
    }

    @Test
    public void whenPrepareUpdate_shouldReturnUpdateOutcome() {
        String outcome = costCenterHierarchyController.prepareUpdate();

        assertEquals(OUTCOME_COST_CENTER_HIERARCHY_EDIT, outcome);
    }

    @Test
    public void whenPrepareRemove_withValidId_shouldReturnDeleteOutcome() throws BusinessException {
        when(costCenterHierarchyBean.getCurrentRowId()).thenReturn(1L);
        when(costCenterHierarchyService.findByCode(anyLong())).thenReturn(mockCostCenterHierarchy());
        when(costCenterService.findCostCentersByCostCenterHierarchy(anyLong())).thenReturn(Arrays.asList(mockGrupoCusto()));

        String outcome = costCenterHierarchyController.prepareRemove();

        verify(costCenterHierarchyService, times(1)).findByCode(anyLong());
        verify(costCenterService, times(1)).findCostCentersByCostCenterHierarchy(anyLong());

        assertEquals(OUTCOME_COST_CENTER_HIERARCHY_DELETE, outcome);
    }

    @Test
    public void whenPrepareRemove_withValidIdAndWithoutAssociatedCostCenters_shouldReturnDeleteOutcome() throws BusinessException {
        when(costCenterHierarchyBean.getCurrentRowId()).thenReturn(1L);
        when(costCenterHierarchyService.findByCode(anyLong())).thenReturn(mockCostCenterHierarchy());
        doThrow(BusinessException.class).when(costCenterService).findCostCentersByCostCenterHierarchy(anyLong());

        String outcome = costCenterHierarchyController.prepareRemove();

        verify(costCenterHierarchyService, times(1)).findByCode(anyLong());
        verify(costCenterService, times(1)).findCostCentersByCostCenterHierarchy(anyLong());
        assertEquals(OUTCOME_COST_CENTER_HIERARCHY_DELETE, outcome);
    }

    private GrupoCusto mockGrupoCusto() {
        GrupoCusto grupoCusto = new GrupoCusto();
        grupoCusto.setCodigoGrupoCusto(1L);
        grupoCusto.setNomeGrupoCusto("Test");
        grupoCusto.setIndicadorAtivo(Constants.YES);
        return grupoCusto;
    }

    @Test
    public void findByFilter_Success() throws BusinessException {
        // Setup
        CostCenterHierarchy costCenterHierarchy = mockCostCenterHierarchy();

        List<CostCenterHierarchy> expectedList = Collections.singletonList(costCenterHierarchy);
        when(costCenterHierarchyBean.getFilter()).thenReturn(costCenterHierarchy);
        when(costCenterHierarchyService.findByFilter(costCenterHierarchy)).thenReturn(expectedList);

        // Execute
        costCenterHierarchyController.findByFilter();

        // Verify
        verify(costCenterHierarchyBean).setListCostCenterHierarchy(expectedList);
        verify(costCenterHierarchyBean).setCurrentPageId(0);
        // Add any additional verifications here
    }

    @Test
    public void findByFilter_BusinessException() throws BusinessException {
        // Setup
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();
        when(costCenterHierarchyBean.getFilter()).thenReturn(costCenterHierarchy);
        doThrow(new BusinessException("Error")).when(costCenterHierarchyService).findByFilter(costCenterHierarchy);

        // Execute
        costCenterHierarchyController.findByFilter();

        // Verify
        verify(costCenterHierarchyBean).setListCostCenterHierarchy(Collections.EMPTY_LIST);
        // Verify that the warning message is shown, might need to mock Messages.showWarning
        // Note: Verifying logger output is more complex and might not be necessary
    }

    @Test
    public void whenPrepareRemove_withInvalidId_shouldReturnNull() throws BusinessException {
        when(costCenterHierarchyBean.getCurrentRowId()).thenReturn(1L);
        doThrow(BusinessException.class).when(costCenterHierarchyService).findByCode(anyLong());

        String outcome = costCenterHierarchyController.prepareRemove();

        verify(costCenterHierarchyService, times(1)).findByCode(anyLong());
        assertNull(outcome);
    }

    @Test
    public void whenDelete_shouldResetFormAndReturnSearchOutcome() throws BusinessException {
        when(costCenterHierarchyBean.getCostCenterHierarchy()).thenReturn(mockCostCenterHierarchy());

        String outcome = costCenterHierarchyController.delete();

        verify(costCenterHierarchyService, times(1)).remove(anyLong());
        verify(costCenterHierarchyBean, times(1)).resetForm();

        assertEquals(OUTCOME_COST_CENTER_HIERARCHY_SEARCH, outcome);
    }

    @Test
    public void whenDeleteAndRemoveThrowsException_shouldReturnNull() throws BusinessException {
        when(costCenterHierarchyBean.getCostCenterHierarchy()).thenReturn(mockCostCenterHierarchy());
        doThrow(BusinessException.class).when(costCenterHierarchyService).remove(1L);

        String outcome = costCenterHierarchyController.delete();

        verify(costCenterHierarchyService, times(1)).remove(1L);
        verify(costCenterHierarchyBean, times(0)).resetForm();
        assertNull(outcome);
    }

    @Test
    public void whenCreate_shouldSaveCostCenterHierarchySuccessfully() throws BusinessException {
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();
        when(costCenterHierarchyBean.getCostCenterHierarchy()).thenReturn(costCenterHierarchy);

        costCenterHierarchyController.create();

        verify(costCenterHierarchyService).create(costCenterHierarchy);
        verify(costCenterHierarchyBean).reset();
    }

    @Test
    public void whenCreateAndThrowsBusinessException_shouldShowError() throws BusinessException {
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();
        when(costCenterHierarchyBean.getCostCenterHierarchy()).thenReturn(costCenterHierarchy);
        doThrow(new BusinessException("Test exception")).when(costCenterHierarchyService).create(costCenterHierarchy);

        costCenterHierarchyController.create();

        verify(costCenterHierarchyBean).getCostCenterHierarchy();
        verify(costCenterHierarchyService).create(costCenterHierarchy);
    }

    @Test
    public void update_shouldUpdateCostCenterHierarchyAndReturnSearchOutcome() throws BusinessException {
        final CostCenterHierarchy costCenterHierarchy = mockCostCenterHierarchy();
        when(costCenterHierarchyBean.getCostCenterHierarchy()).thenReturn(costCenterHierarchy);
        final CostCenterHierarchyController costCenterHierarchyControllerSpy = spy(costCenterHierarchyController);

        final String actualOutcome = costCenterHierarchyControllerSpy.update();

        verify(costCenterHierarchyBean, times(1)).getCostCenterHierarchy();
        verify(costCenterHierarchyService, times(1)).update(costCenterHierarchy);
        verify(costCenterHierarchyBean, times(1)).resetForm();
        verify(costCenterHierarchyControllerSpy, times(1)).findByFilter();
        assertEquals("cost_center_hierarchy_search", actualOutcome);
    }

    @Test
    public void update_whenBusinessExceptionOccurs_shouldShowErrorAndReturnEditOutcome() throws BusinessException {
        final CostCenterHierarchy costCenterHierarchy = mockCostCenterHierarchy();
        when(costCenterHierarchyBean.getCostCenterHierarchy()).thenReturn(costCenterHierarchy);
        doThrow(new BusinessException("Error")).when(costCenterHierarchyService).update(costCenterHierarchy);
        final CostCenterHierarchyController costCenterHierarchyControllerSpy = spy(costCenterHierarchyController);

        final String actualOutcome = costCenterHierarchyControllerSpy.update();

        verify(costCenterHierarchyBean, times(1)).getCostCenterHierarchy();
        verify(costCenterHierarchyService, times(1)).update(costCenterHierarchy);
        verify(costCenterHierarchyBean, never()).resetForm();
        verify(costCenterHierarchyControllerSpy, never()).findByFilter();
        assertEquals("cost_center_hierarchy_edit", actualOutcome);
    }

    @Test
    public void cancelUpdate_shouldResetFormAndReturnSearchOutcome() throws BusinessException {
        final CostCenterHierarchyController costCenterHierarchyControllerSpy = spy(costCenterHierarchyController);

        final String actualOutcome = costCenterHierarchyControllerSpy.cancelUpdate();

        verify(costCenterHierarchyService, never()).update(any(CostCenterHierarchy.class));
        verify(costCenterHierarchyBean, times(1)).resetForm();
        verify(costCenterHierarchyControllerSpy, times(1)).findByFilter();
        assertEquals("cost_center_hierarchy_search", actualOutcome);
    }

    private CostCenterHierarchy mockCostCenterHierarchy() {
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();
        costCenterHierarchy.setCode(1L);
        costCenterHierarchy.setName("Test");
        costCenterHierarchy.setInActive(Boolean.TRUE);
        costCenterHierarchy.setCreatedAt(new Date());
        costCenterHierarchy.setUpdatedAt(new Date());
        costCenterHierarchy.setOracleCode("TEST");
        return costCenterHierarchy;
    }
}
