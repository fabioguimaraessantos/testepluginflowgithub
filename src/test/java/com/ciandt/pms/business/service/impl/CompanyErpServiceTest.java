package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CompanyErp;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.persistence.dao.ICompanyErpDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class CompanyErpServiceTest {

    @InjectMocks
    private CompanyErpService companyErpService;

    @Mock
    private ICompanyErpDao companyErpDao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenCompanyCode_whenFindActiveByCompany_thenReturnCompanyErp() throws BusinessException {
        final CompanyErp expectedCompanyErp = mockCompanyErp();
        when(companyErpDao.findActiveByCompany(1L)).thenReturn(Arrays.asList(expectedCompanyErp));

        final CompanyErp actualCompanyErp = companyErpService.findActiveByCompany(1L);

        assertEquals(expectedCompanyErp, actualCompanyErp);
    }

    @Test
    public void givenCompanyCode_whenFindActiveByCompanyAndActiveCompanyErpDoesNotExists_thenReturnNull() throws BusinessException {
        when(companyErpDao.findActiveByCompany(1L)).thenReturn(Collections.emptyList());

        final CompanyErp actualCompanyErp = companyErpService.findActiveByCompany(1L);

        assertNull(actualCompanyErp);
    }

    @Test
    public void givenCompanyCode_whenFindActiveByCompanyAndReturnsMoreThanOneCompanyErp_thenThrowsBusinessException() {
        when(companyErpDao.findActiveByCompany(10L)).thenReturn(Arrays.asList(mockCompanyErp(), mockCompanyErp()));

        try {
            companyErpService.findActiveByCompany(10L);
            fail("Expected a BusinessException to be thrown");
        } catch (BusinessException e) {
            assertEquals(Constants.DEFAULT_MSG_ERROR_COMPANY_ERP_HAS_MORE_THAN_ONE_ACTIVE, e.getMessage());
        }
    }

    @Test
    public void whenFindAllActive_thenReturnCompanyErpMap() throws BusinessException {
        final CompanyErp expectedCompanyErp = mockCompanyErp();
        when(companyErpDao.findAllActive()).thenReturn(Collections.singletonList(expectedCompanyErp));

        final Map<Long, String> actualCompanyErp = companyErpService.findAllActive();

        assertNotNull(actualCompanyErp);
        assertTrue(actualCompanyErp.containsKey(expectedCompanyErp.getCompany().getCodigoEmpresa()));
        assertEquals(expectedCompanyErp.getErpName(), actualCompanyErp.get(expectedCompanyErp.getCompany().getCodigoEmpresa()));
    }

    @Test
    public void whenFindAllActiveAndActiveCompanyErpDoesNotExists_thenReturnEmptyMap() throws BusinessException {
        when(companyErpDao.findAllActive()).thenReturn(Collections.emptyList());

        final Map<Long, String> actualCompanyErp = companyErpService.findAllActive();

        assertNotNull(actualCompanyErp);
        assertTrue(actualCompanyErp.isEmpty());
    }

    private CompanyErp mockCompanyErp() {
        Empresa company = new Empresa();
        company.setCodigoEmpresa(1L);
        company.setNomeEmpresa("Company Test");

        CompanyErp companyErp = new CompanyErp();
        companyErp.setCompanyErpCode(1L);
        companyErp.setCompany(company);
        companyErp.setErpName("XERO");
        companyErp.setActive(true);
        companyErp.setCreatedAtDate(new Date());
        companyErp.setLogin("login");
        companyErp.setObservation("Test");
        return companyErp;
    }

}
