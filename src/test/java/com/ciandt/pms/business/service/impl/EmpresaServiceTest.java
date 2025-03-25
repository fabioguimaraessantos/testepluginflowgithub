package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.IEmpresaDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class EmpresaServiceTest {

    @InjectMocks
    private EmpresaService service;

    @Mock
    private IEmpresaDao dao;

    private Empresa empresa;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Moeda moeda = new Moeda();
        moeda.setCodigoMoeda(1L);
        moeda.setNomeMoeda("Real");
        moeda.setSiglaMoeda("BRL");
        moeda.setCodigoErpIndFin(null);

        empresa = new Empresa();
        empresa.setCodigoEmpresa(1L);
        empresa.setEmpresa(null);
        empresa.setNomeEmpresa("CIT Software S.A.");
        empresa.setNumeroCnpj("00.609.634/0001-46");
        empresa.setIndicadorAtivo(Constants.ACTIVE);
        empresa.setCodigoMnemonico("CIT");
        empresa.setTextoCidade("Campinas");
        empresa.setTextoEstado("SP");
        empresa.setTextoPais("Brazil");
        empresa.setMoeda(moeda);
        empresa.setIndicadorPlanoContabilPorMoeda(Boolean.FALSE);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void givenValidEmpresa_whenCreateEmpresa_thenSuccess() throws IntegrityConstraintException {
        doNothing().when(dao).create(empresa);
        when(service.findEmpresaByAcronym(empresa.getCodigoMnemonico())).thenReturn(null);

        service.createEmpresa(empresa);

        Assert.assertTrue(Boolean.TRUE);
    }

    @Test(expected = IntegrityConstraintException.class)
    public void givenExistingEmpresa_whenCreateEmpresa_thenThrowsIntegrityConstraintException() throws IntegrityConstraintException {
        when(service.findEmpresaByAcronym(empresa.getCodigoMnemonico())).thenReturn(empresa);

        service.createEmpresa(empresa);

        Assert.assertTrue(Boolean.TRUE);
    }

    @Test
    public void givenValidEmpresa_whenUpdateEmpresa_thenSuccess() throws IntegrityConstraintException {
        doNothing().when(dao).update(empresa);
        when(service.findEmpresaByAcronym(empresa.getCodigoMnemonico())).thenReturn(empresa);
        when(service.findEmpresaById(empresa.getCodigoEmpresa())).thenReturn(empresa);

        service.updateEmpresa(empresa);

        Assert.assertTrue(Boolean.TRUE);
    }

    @Test
    public void givenEmpresaPai_whenUpdateEmpresa_thenSuccess() throws IntegrityConstraintException {
        Set<Empresa> empresas = new HashSet<>();
        empresas.add(new Empresa());
        empresa.setEmpresas(empresas);

        doNothing().when(dao).update(empresa);
        when(service.findEmpresaByAcronym(empresa.getCodigoMnemonico())).thenReturn(empresa);
        when(service.findEmpresaById(empresa.getCodigoEmpresa())).thenReturn(empresa);

        service.updateEmpresa(empresa);

        Assert.assertTrue(Boolean.TRUE);
    }

    @Test
    public void givenEmpresaPaiWithAnotherEmpresaPai_whenUpdateEmpresa_thenThrowsIntegrityConstraintException() throws IntegrityConstraintException {
        exceptionRule.expect(IntegrityConstraintException.class);
        exceptionRule.expectMessage(Constants.MSG_ERROR_UPDATE_EMPRESA_PAI);

        Set<Empresa> empresas = new HashSet<>();
        empresas.add(new Empresa());
        empresa.setEmpresas(empresas);
        empresa.setEmpresa(new Empresa());

        doNothing().when(dao).update(empresa);
        when(service.findEmpresaByAcronym(empresa.getCodigoMnemonico())).thenReturn(empresa);
        when(service.findEmpresaById(empresa.getCodigoEmpresa())).thenReturn(empresa);

        service.updateEmpresa(empresa);
    }

    @Test
    public void givenEmpresaPaiWithIndicadorAtivoInactive_whenUpdateEmpresa_thenThrowsIntegrityConstraintException() throws IntegrityConstraintException {
        exceptionRule.expect(IntegrityConstraintException.class);
        exceptionRule.expectMessage(Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_INACTIVE);

        empresa.setIndicadorAtivo(Constants.INACTIVE);
        Set<Empresa> empresas = new HashSet<>();
        empresas.add(new Empresa());
        empresa.setEmpresas(empresas);

        doNothing().when(dao).update(empresa);
        when(service.findEmpresaByAcronym(empresa.getCodigoMnemonico())).thenReturn(empresa);
        when(service.findEmpresaById(empresa.getCodigoEmpresa())).thenReturn(empresa);

        service.updateEmpresa(empresa);
    }

    @Test
    public void givenNotExistingEmpresa_whenUpdateEmpresa_thenThrowsIntegrityConstraintException() throws IntegrityConstraintException {
        exceptionRule.expect(IntegrityConstraintException.class);
        exceptionRule.expectMessage(Constants.MSG_ERROR_ALREADY_EXISTS_ACRONYM);

        Empresa newEmpresa = new Empresa();
        newEmpresa.setCodigoEmpresa(2L);

        when(service.findEmpresaByAcronym(empresa.getCodigoMnemonico())).thenReturn(newEmpresa);

        service.updateEmpresa(empresa);
    }

    @Test
    public void givenEmpresaOceania_WhenCheckingEmpresaOceania_thenSuccess() {
        //given
        Empresa empresa = new Empresa();
        empresa.setCodigoEmpresa(Constants.CD_EMPRESA_OCEANIA);

        //when

        //then
        boolean result = service.isOceania(empresa);
        Assert.assertEquals(Boolean.TRUE, result);
    }
}
