package com.ciandt.pms.control.jsf;

import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.model.Empresa;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class LicensaSoftwareControllerTest {

    @Mock
    protected IEmpresaService empresaService;

    protected static final Long CIT_CODE = 1L;
    protected static final Long INC_CODE = 10L;

    /**
     *
     */
    protected void companyMocks(){
        List<Empresa> empresas = createListCompany();
        Mockito.when(empresaService.findEmpresaById(1L)).thenReturn(empresas.get(0));
        Mockito.when(empresaService.findEmpresaById(10L)).thenReturn(empresas.get(1));
    }

    /**
     * @return Collection of Companies.
     */
    protected List<Empresa> createListCompany(){

        List<Empresa> empresas = new ArrayList<>();
        empresas.add(createCompany(1L, "CIT Software S.A."));
        empresas.add(createCompany(10L, "CIT INC - EUA"));

        return empresas;
    }

    /**
     * @param id - Company ID
     * @param name - Company Name
     * @return - New Company
     */
    protected Empresa createCompany(Long id, String name){
        Empresa empresa = new Empresa();
        empresa.setCodigoEmpresa(id);
        empresa.setNomeEmpresa(name);
        return empresa;
    }

    @Test
    public void test() {
    }
}