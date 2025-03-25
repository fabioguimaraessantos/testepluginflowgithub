package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.control.jsf.bean.CidadeBaseBean;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.Moeda;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class CidadeBaseControllerTest {

    @InjectMocks
    private CidadeBaseController controller;

    @Mock
    private ICidadeBaseService cidadeBaseService;

    @Mock
    private IMoedaService moedaService;

    @Mock
    CidadeBaseBean bean;
    private CidadeBase cidadeBase;
    private Moeda moeda;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        moeda = new Moeda();
        moeda.setCodigoMoeda(1L);
        moeda.setNomeMoeda("Real");
        moeda.setSiglaMoeda("BRL");
        moeda.setCodigoErpIndFin(null);

        cidadeBase = new CidadeBase();
        cidadeBase.setCodigoCidadeBase(1L);
        cidadeBase.setNomeCidadeBase("Campinas");
        cidadeBase.setIndicadorAtivo(Constants.ACTIVE);
        cidadeBase.setSiglaCidadeBase("CPS");
        cidadeBase.setMoeda(moeda);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void prepareCreate_givenOutcome_whenPrepareCreate_mustReturnTheSameOutcome() {

        //GIVEN expected outcome
        String outcome = "cidadeBase_add";

        //WHEN prepareCreate is executed
        String result = controller.prepareCreate();

        //THEN must Return the same outcome
        Assert.assertEquals(outcome, result);
    }
    @Test
    public void create_givenValidCidadeBase_whenCreateCidadeBase_thenSuccess() throws IntegrityConstraintException {

        //GIVEN expected outcome
        String outcome = "cidadeBase_add";

        //WITH mocks
        doNothing().when(cidadeBaseService).createCidadeBase(cidadeBase);
        when(bean.getTo()).thenReturn(cidadeBase);
        when(moedaService.findMoedaById(moeda.getCodigoMoeda())).thenReturn(moeda);

        //WHEN create valid Cidade Base
        String result = controller.create();

        //THEN result string should be same as outcome
        Assert.assertEquals(outcome, result);
    }

    @Test
    public void create_givenInvalidValidCidadeBase_whenCreateCidadeBaseThrowError_thenThrowsIntegrityConstraintException()
            throws IntegrityConstraintException {

        // GIVEN mocks to generate error
        Mockito.doThrow(new IntegrityConstraintException())
                .when(cidadeBaseService).createCidadeBase(cidadeBase);
        when(bean.getTo()).thenReturn(cidadeBase);
        when(moedaService.findMoedaById(moeda.getCodigoMoeda())).thenReturn(moeda);

        // WHEN create invalid Cidade Base
        String result = controller.create();

        // THEN return must be null
        Assert.assertNull(result);
    }

    /**
     * METODO: prepareSearch()
     * REGRA: Retorna string para exibir tela de busca
     * CASE: Ao executar o método prepareSearch, deve retornar a string da tela de busca
     */
    @Test
    public void prepareSearch_givenValidOutcome_mustReturnSameOutcome() {
        //GIVEN return string
        String outcome = "cidadeBase_search";

        //WHEN controller is executed
        String result = controller.prepareSearch();

        //THEN result string should be same as outcome
        Assert.assertEquals(outcome, result);
    }

    @Test
    public void givenValidCidadeBase_whenUpdate_thenSuccessfullyOutcomeCidadeBaseSearchPage() throws IntegrityConstraintException {
        // GIVEN
        String expectedOutcome = "cidadeBase_search";

        BDDMockito.given(bean.getTo()).willReturn(cidadeBase);
        BDDMockito.given(moedaService.findMoedaById(isA(Long.class))).willReturn(moeda);
        BDDMockito.doNothing().when(cidadeBaseService).updateCidadeBase(isA(CidadeBase.class));
        BDDMockito.given(bean.getFilter()).willReturn(getFilter());

        // WHEN
        String actualOutcome = controller.update();

        // THEN
        Assert.assertEquals(expectedOutcome, actualOutcome);
    }

    @Test
    public void givenValidCidadeBase_whenCidadeBaseUpdateCancelled_thenRestoreFormAndFilterAndSuccessfullyOutcomeCidadeBaseSearchPage() {
        // GIVEN
        String expectedOutcome = "cidadeBase_search";

        BDDMockito.given(bean.getFilter()).willReturn(getFilter());
        BDDMockito.given(bean.getTo()).willReturn(cidadeBase);

        // WHEN
        String actualOutcome = controller.cancelUpdate();

        // THEN
        Assert.assertEquals(expectedOutcome, actualOutcome);
    }

    /**
     * METODO: findByFilter()
     * REGRA: Retorna a lista de entidades encontradas
     * CASE: Ao executar o método findByFilter, deve retornar uma lista com o mesmo número de entidades
     */
    @Test
    public void findByFilter_givenFilterAndEntityList_mustReturnListWithSameNumberOfEntities() throws BusinessException {

        //GIVEN filter
        CidadeBase filter = getFilter();
        List<CidadeBase> entities = getEntities();

        //WITH mocks
        Mockito.when(bean.getTo()).thenReturn(filter);
        Mockito.when(bean.getFilter()).thenReturn(filter);
        Mockito.when(cidadeBaseService.findByFilter(filter)).thenReturn(entities);
        Mockito.when(bean.getResultList()).thenReturn(entities);

        //WHEN controller is executed
        controller.findByFilter();

        //THEN size of result list in bean should be the same as entities.
        Assert.assertEquals(entities.size(), controller.getBean().getResultList().size());
    }

    /**
     * METODO: findByFilter()
     * REGRA: Retorna uma lista vazia
     * CASE: Ao executar o método findByFilter com informações de cidade base não cadastrada no banco, deve retornar
     * uma lista vazia
     */
    @Test
    public void findByFilter_givenFilterAndEmptyList_mustReturnEmptyList() throws BusinessException {

        //GIVEN filter
        CidadeBase filter = getFilter();

        //WITH mocks
        Mockito.when(bean.getTo()).thenReturn(filter);
        Mockito.when(cidadeBaseService.findByFilter(filter)).thenThrow(new BusinessException(""));

        //WHEN controller is executed
        controller.findByFilter();

        //THEN size of result list in bean should be empty.
        Assert.assertEquals(0, controller.getBean().getResultList().size());
    }

    @Test
    public void openDeleteModal_givenDeleteModalOpened_isDeleteModalOpenShouldBeTrue() {

        //GIVEN Process to delete a base city has started
        Mockito.when(controller.getBean().isDeleteModalOpen()).thenReturn(true);

        //WHEN Delete Modal opens
        controller.openDeleteModal();

        //THEN isDeleteModalOpen must be true
        Assert.assertTrue(controller.getBean().isDeleteModalOpen());
    }

    @Test
    public void closedeleteModal_givenDeleteModalClosed_isDeleteModalOpenShouldBeFalse() {

        //GIVEN Process to delete a base city is finished
        Mockito.when(controller.getBean().isDeleteModalOpen()).thenReturn(false);

        //WHEN Delete Modal closes
        controller.closeDeleteModal();

        //THEN isDeleteModalOpen must be False
        Assert.assertFalse(controller.getBean().isDeleteModalOpen());
    }

    @Test
    public void remove_givenCidadeBaseDeleted_withNoDependency_thenEntitieShouldBePassedToService() throws IntegrityConstraintException {

        //GIVEN expected outcome
        String outcome = "cidadeBase_search";

        //GIVEN Valid Delete
        doNothing().when(cidadeBaseService).removeCidadeBase(cidadeBase);
        Mockito.when(bean.getTo()).thenReturn(cidadeBase);
        Mockito.when(cidadeBaseService.findCidadeBaseById(cidadeBase.getCodigoCidadeBase())).thenReturn(cidadeBase);

        //WHEN remove
        String result = controller.remove();

        //THEN
        Assert.assertEquals(result, outcome);
    }

    @Test
    public void openInactivateModal_givenInactivateModalOpen_whenClickOnInactivateOrActivateButton_thenSetInactivateModalOpenToTrue() {
        //GIVEN Process to delete a base city has started
        Mockito.when(controller.getBean().isInactivateModalOpen()).thenReturn(true);

        // WHEN
        controller.openInactivateModal();

        // THEN
        Assert.assertTrue(controller.getBean().isInactivateModalOpen());
    }

    @Test
    public void confirmInactivateModal_givenInactivateModalOpen_whenClickOnConfirmButton_thenSetInactivateModalOpenToFalse() throws IntegrityConstraintException {
        // GIVEN
        Mockito.doNothing().when(cidadeBaseService).updateCidadeBaseIndicadorAtivo(cidadeBase);
        Mockito.when(bean.getTo()).thenReturn(cidadeBase);
        Mockito.when(bean.getFilter()).thenReturn(cidadeBase);
        Mockito.doNothing().when(bean).setTo(cidadeBase);


        // WHEN
        controller.confirmInactivateModal();

        // THEN
        Assert.assertFalse(controller.getBean().isInactivateModalOpen());
    }

    @Test
    public void confirmInactivateModal_givenInactivateModalOpen_whenClickOnCancelButton_thenSetInactivateModalOpenToFalse() {
        // WHEN
        controller.closeInactivateModal();

        // THEN
        Assert.assertEquals(false, controller.getBean().isInactivateModalOpen());
    }
    @Test
    public void remove_givenCidadeBaseDeleted_withDependency_thenShouldThrowException() throws IntegrityConstraintException {

        //GIVEN Valid Delete
        Mockito.doThrow(new IntegrityConstraintException()).when(cidadeBaseService).removeCidadeBase(cidadeBase);
        Mockito.when(bean.getTo()).thenReturn(cidadeBase);
        Mockito.when(cidadeBaseService.findCidadeBaseById(cidadeBase.getCodigoCidadeBase())).thenReturn(cidadeBase);

        //WHEN remove
        String result = controller.remove();

        //THEN
        Assert.assertNull(result);
    }

    private CidadeBase getFilter() {
        CidadeBase filter = new CidadeBase();
        filter.setNomeCidadeBase("Test");
        filter.setMoeda(new Moeda(1L, "Moeda Test", "MT"));
        return filter;
    }

    private List<CidadeBase> getEntities() {

        List<CidadeBase> entities = new ArrayList<CidadeBase>();
        entities.add(new CidadeBase(10L, "City Base Test", "A", "CBT", 1L, new Moeda(1L, "Moeda Test", "MT")));
        entities.add(new CidadeBase(20L, "City Base Test 2", "I", "CBT2", 11L, new Moeda(11L, "Moeda Test", "MT")));

        return entities;
    }
}