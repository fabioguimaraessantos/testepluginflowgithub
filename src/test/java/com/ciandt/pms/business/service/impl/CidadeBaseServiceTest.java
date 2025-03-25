package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICidadeBaseDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class CidadeBaseServiceTest {

    @InjectMocks
    private CidadeBaseService service;

    @Mock
    private ICidadeBaseDao dao;

    private CidadeBase cidadeBase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Moeda moeda = new Moeda();
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
    public void createCidadeBase_givenValidCidadeBase_whenCreateCidadeBase_thenSuccess() throws IntegrityConstraintException {

        //GIVEN valid Cidade Base
        ArgumentCaptor<CidadeBase> valueCapture = ArgumentCaptor.forClass(CidadeBase.class);
        doNothing().when(dao).create(valueCapture.capture());
        when(service.findCidadeBaseBySigla(cidadeBase.getSiglaCidadeBase())).thenReturn(null);
        when(service.findCidadeBaseByNome(cidadeBase.getNomeCidadeBase())).thenReturn(null);

        //WHEN create Cidade Base
        service.createCidadeBase(cidadeBase);

        //THEN the same argument passed to service must be passed to dao
        Assert.assertEquals(cidadeBase, valueCapture.getValue());
    }
    @Test
    public void createCidadeBase_givenExistingSigla_whenCreateCidadeBase_thenThrowsIntegrityConstraintException() throws IntegrityConstraintException {

        //Rules Expected
        exceptionRule.expect(IntegrityConstraintException.class);
        exceptionRule.expectMessage(Constants.MSG_ERROR_ALREADY_EXISTS_CIDADE_BASE_ACRONYM);

        //GIVEN sigla already exinting in dao
        when(service.findCidadeBaseBySigla(cidadeBase.getSiglaCidadeBase())).thenReturn(cidadeBase);

        //WHEN create Cidade Base
        service.createCidadeBase(cidadeBase);

        //THEN must fail with the expected rule
        Assert.fail();
    }

    @Test
    public void createCidadeBase_givenExistingNome_whenCreateCidadeBase_thenThrowsIntegrityConstraintException() throws IntegrityConstraintException {

        //Rules Expected
        exceptionRule.expect(IntegrityConstraintException.class);
        exceptionRule.expectMessage(Constants.MSG_ERROR_ALREADY_EXISTS_CIDADE_BASE_NAME);

        //GIVEN nome already exinting in dao
        when(service.findCidadeBaseBySigla(cidadeBase.getSiglaCidadeBase())).thenReturn(null);
        when(service.findCidadeBaseByNome(cidadeBase.getNomeCidadeBase())).thenReturn(cidadeBase);

        //WHEN create Cidade Base
        service.createCidadeBase(cidadeBase);

        //THEN must fail with the expected rule
        Assert.fail();
    }

    @Test
    public void updateCidadeBase_givenIndicadorAtivoIsActive_whenToggleIndicadorAtivoToInactive_thenSuccess() throws IntegrityConstraintException {
        // GIVEN
        cidadeBase.setIndicadorAtivo(Constants.ACTIVE);
        when(service.findCidadeBaseById(cidadeBase.getCodigoCidadeBase())).thenReturn(cidadeBase);
        doNothing().when(dao).update(cidadeBase);
        cidadeBase.setIndicadorAtivo(Constants.INACTIVE);

        // WHEN
        service.updateCidadeBaseIndicadorAtivo(cidadeBase);

        // THEN
        Assert.assertTrue(true);
    }

    @Test
    public void updateCidadeBase_givenIndicadorAtivoIsInactive_whenToggleIndicadorAtivoToActive_thenSuccess() throws IntegrityConstraintException {
        // GIVEN
        cidadeBase.setIndicadorAtivo(Constants.INACTIVE);
        when(service.findCidadeBaseById(cidadeBase.getCodigoCidadeBase())).thenReturn(cidadeBase);
        doNothing().when(dao).update(cidadeBase);
        cidadeBase.setIndicadorAtivo(Constants.ACTIVE);

        // WHEN
        service.updateCidadeBaseIndicadorAtivo(cidadeBase);

        // THEN
        Assert.assertTrue(true);
    }

    @Test
    public void updateCidadeBase_givenIndicadorAtivoIsInactive_whenToggleIndicadorAtivoToActive_thenThrowsIntegrityConstraintException() throws IntegrityConstraintException {
        //Rules Expected
        exceptionRule.expect(IntegrityConstraintException.class);

        // GIVEN
        cidadeBase.setIndicadorAtivo(Constants.INACTIVE);
        when(service.findCidadeBaseById(cidadeBase.getCodigoCidadeBase())).thenReturn(null);
        doNothing().when(dao).update(cidadeBase);
        cidadeBase.setIndicadorAtivo(Constants.ACTIVE);

        // WHEN
        service.updateCidadeBaseIndicadorAtivo(cidadeBase);

        // THEN
        Assert.fail();
    }

    @Test
    public void givenValidCidadeBase_whenUpdateCidadeBase_thenSuccessfullyUpdateCidadeBase() throws IntegrityConstraintException {
        // GIVEN
        ArgumentCaptor<CidadeBase> valueCapture = ArgumentCaptor.forClass(CidadeBase.class);
        BDDMockito.given(dao.findByFilter(isA(CidadeBase.class))).willReturn(Arrays.asList(cidadeBase));
        BDDMockito.doNothing().when(dao).update(valueCapture.capture());

        // WHEN
        service.updateCidadeBase(cidadeBase);

        // THEN
        Assert.assertEquals(cidadeBase, valueCapture.getValue());
        BDDMockito.verify(dao, Mockito.times(1)).update(isA(CidadeBase.class));
    }

    @Test
    public void givenNotExistingCidadeBase_whenFindByFilterReturnEmptyCidadeBaseList_thenSuccessfullyCreateNewCidadeBase() throws IntegrityConstraintException {

        // GIVEN
        ArgumentCaptor<CidadeBase> valueCapture = ArgumentCaptor.forClass(CidadeBase.class);
        List<CidadeBase> cidadesBase = new ArrayList<>();
        BDDMockito.given(dao.findByFilter(isA(CidadeBase.class))).willReturn(cidadesBase);
        BDDMockito.doNothing().when(dao).update(valueCapture.capture());

        // WHEN
        service.updateCidadeBase(cidadeBase);

        // THEN
        Assert.assertEquals(cidadeBase, valueCapture.getValue());
        BDDMockito.verify(dao, Mockito.times(1)).update(isA(CidadeBase.class));
    }

    @Test
    public void givenValidCidadeBase_whenFindByFilterReturnMultipleCidadeBase_thenThrowIntegrityConstraintException() throws IntegrityConstraintException {
        exceptionRule.expect(IntegrityConstraintException.class);
        exceptionRule.expectMessage(Constants.MSG_ERROR_UPDATE_CIDADE_BASE);

        CidadeBase cidadeBase1 = new CidadeBase();
        cidadeBase1.setCodigoCidadeBase(2L);
        cidadeBase1.setNomeCidadeBase("teste");
        cidadeBase1.setIndicadorAtivo(Constants.ACTIVE);
        cidadeBase1.setSiglaCidadeBase("TEST");

        // GIVEN
        List<CidadeBase> cidadesBase = Arrays.asList(cidadeBase, cidadeBase, cidadeBase);
        BDDMockito.given(dao.findByFilter(isA(CidadeBase.class))).willReturn(cidadesBase);
        BDDMockito.given(dao.findBySigla(Mockito.any())).willReturn(cidadeBase1);
        BDDMockito.doNothing().when(dao).update(isA(CidadeBase.class));

        // WHEN
        service.updateCidadeBase(cidadeBase);
    }

    @Test
    public void givenCidadeBaseWithDifferentCode_whenComparingCodigoCidadeBase_thenThrowIntegrityConstraintException() throws IntegrityConstraintException {
        exceptionRule.expect(IntegrityConstraintException.class);
        exceptionRule.expectMessage(Constants.MSG_ERROR_UPDATE_CIDADE_BASE);

        // GIVEN
        CidadeBase filter = getFilter();
        filter.setCodigoCidadeBase(2L);
        List<CidadeBase> cidadesBase = Arrays.asList(filter);

        BDDMockito.given(dao.findByFilter(isA(CidadeBase.class))).willReturn(cidadesBase);
        BDDMockito.given(dao.findBySigla(Mockito.any())).willReturn(filter);
        BDDMockito.given(dao.findIfHasDependency(Mockito.any())).willReturn(false);
        BDDMockito.doNothing().when(dao).update(isA(CidadeBase.class));

        // WHEN
        service.updateCidadeBase(cidadeBase);
    }

    @Test
    public void findIfHasDependency_givenDependencyNotFound_whenFindIfHasDependency_thenReturnTrue() {

        //GIVEN dependency not found
        when(dao.findIfHasDependency(cidadeBase.getCodigoCidadeBase())).thenReturn(Boolean.TRUE);

        //WHEN search for dependency
        Boolean result = service.findIfHasDependency(cidadeBase.getCodigoCidadeBase());

        //THEN result must be true
        Assert.assertTrue(result);
    }

    @Test
    public void findIfHasDependency_givenDependencyFound_whenFindIfHasDependency_thenReturnFalse() {

        //GIVEN dependency found
        when(dao.findIfHasDependency(cidadeBase.getCodigoCidadeBase())).thenReturn(Boolean.FALSE);

        //WHEN search for dependency
        Boolean result = service.findIfHasDependency(cidadeBase.getCodigoCidadeBase());

        //THEN result must be false
        Assert.assertFalse(result);
    }

    /**
     * METODO: findByFilter()
     * REGRA: Retorna a lista de entidades encontradas
     * CASE: Ao executar o método findByFilter, deve retornar uma lista com o mesmo número de entidades
     */
    @Test
    public void findByFilter_givenValidFilter_shouldFindListOfEntities() throws BusinessException {

        //GIVEN filter
        CidadeBase filter = getFilter();

        //WITH existent entities
        List<CidadeBase> entities = getEntities();

        //AND service find by filter
        Mockito.when(dao.findByFilter(Mockito.<CidadeBase>any())).thenReturn(entities);

        //WHEN service is executed with filter.
        List<CidadeBase> result = service.findByFilter(filter);

        //THEN result size should be 2
        Assert.assertEquals(2, result.size());
    }

    /**
     * METODO: findByFilter()
     * REGRA: Retorna a lista de entidades encontradas
     * CASE: Ao executar o método findByFilter e não encontrar nada, deve retornar null e lançar uma BusinessException
     */
    @Test(expected = BusinessException.class)
    public void findByFilter_givenInvalidFilter_shouldReturnNullAndThrowException() throws BusinessException {

        //GIVEN filter
        CidadeBase filter = getFilter();

        //AND service should throw Business Exception
        Mockito.when(dao.findByFilter(Mockito.<CidadeBase>any())).thenReturn(null);

        //WHEN service is executed with filter.
        service.findByFilter(filter);

        //EXPECTED BusinessException and fail
        Assert.fail();
    }

    @Test
    public void removeCidadeBase_givenNoDependency_shouldExecuteWithoutFail() throws IntegrityConstraintException {

        //GIVEN valid Cidade Base without dependency
        ArgumentCaptor<CidadeBase> valueCapture = ArgumentCaptor.forClass(CidadeBase.class);
        doNothing().when(dao).remove(valueCapture.capture());
        when(service.findIfHasDependency(cidadeBase.getCodigoCidadeBase())).thenReturn(false);

        //WHEN remove Cidade Base
        service.removeCidadeBase(cidadeBase);

        //THEN the same argument passed to service must be passed to dao
        Assert.assertEquals(cidadeBase, valueCapture.getValue());

    }

    @Test
    public void removeCidadeBase_givenDependency_shouldThrowError() throws IntegrityConstraintException {

        //Rules Expected
        exceptionRule.expect(IntegrityConstraintException.class);
        exceptionRule.expectMessage(Constants.MSG_ERROR_CIDADE_BASE_HAS_DEPENDENCY);

        //GIVEN valid Cidade Base with dependency
        doNothing().when(dao).remove(cidadeBase);
        when(service.findIfHasDependency(cidadeBase.getCodigoCidadeBase())).thenReturn(true);

        //WHEN remove Cidade Base
        service.removeCidadeBase(cidadeBase);

        //THEN must fail with the expected rule
        Assert.fail();

    }


    private CidadeBase getFilter() {
        CidadeBase filter = new CidadeBase();
        filter.setNomeCidadeBase("Test");
        filter.setMoeda(getMoeda());
        return filter;
    }

    private CidadeBase createEntity(Long codigoCidadeBase, String nomeCidadeBase, String indicadorAtivo,
                                    String siglaCidadeBase, Long codigoEmpresaERP, Moeda moeda) {
        CidadeBase entity = new CidadeBase();

        if (codigoCidadeBase != null)
            entity.setCodigoCidadeBase(codigoCidadeBase);

        if (nomeCidadeBase != null)
            entity.setNomeCidadeBase(nomeCidadeBase);

        if (indicadorAtivo != null)
            entity.setIndicadorAtivo(indicadorAtivo);

        if (siglaCidadeBase != null)
            entity.setSiglaCidadeBase(siglaCidadeBase);

        if (codigoEmpresaERP != null)
            entity.setCodigoEmpresaERP(codigoEmpresaERP);

        if (moeda != null)
            entity.setMoeda(moeda);

        return entity;
    }

    private Moeda createMoeda(Long codigoMoeda, String nomeMoeda, String siglaMoeda) {
        Moeda entity = new Moeda();

        if (codigoMoeda != null)
            entity.setCodigoMoeda(codigoMoeda);

        if (nomeMoeda != null)
            entity.setNomeMoeda(nomeMoeda);

        if (siglaMoeda != null)
            entity.setSiglaMoeda(siglaMoeda);

        return entity;
    }

    private List<CidadeBase> getEntities() {

        List<CidadeBase> entities = new ArrayList<CidadeBase>();
        entities.add(createEntity(10L, "City Base Test", "A", "CBT", 1L, createMoeda(1L, "Moeda Test", "MT")));
        entities.add(createEntity(20L, "City Base Test 2", "I", "CBT2", 11L, createMoeda(11L, "Moeda Test", "MT")));

        return entities;
    }

    private Moeda getMoeda() {
        return createMoeda(1L, "Moeda Test", "MT");
    }
}
