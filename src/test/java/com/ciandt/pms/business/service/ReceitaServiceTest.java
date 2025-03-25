package com.ciandt.pms.business.service;

import com.ciandt.pms.business.service.impl.ReceitaService;
import com.ciandt.pms.integration.vo.IncomingRevenue;
import com.ciandt.pms.model.VwPmsIntegReceitaNacional;
import com.ciandt.pms.persistence.dao.jpa.VwPmsIntegReceitaNacionalDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class ReceitaServiceTest {
    @InjectMocks
    ReceitaService receitaService;

    @Mock
    VwPmsIntegReceitaNacionalDao dao;

    @Test
    public void whenModelIsCreateWithServiceCodeInRangeThenAplInCodigoIs57() throws Exception {
        //Given
        VwPmsIntegReceitaNacional model = build(103L);
        //then
        IncomingRevenue incomingRevenue = receitaService.convertFromModel(model);
        assertEquals(Optional.ofNullable(incomingRevenue.getRevenueItem().getItemApplicationCode()), Optional.of(57L));
        assertEquals(Optional.ofNullable(incomingRevenue.getDocumentTypeCode()), Optional.of(159L));
    }

    @Test
    public void whenModelIsCreateWithoutServiceCodeInRangeThenAplInCodigoIs26() throws Exception {
        //Given
        VwPmsIntegReceitaNacional model = build(107L);
        //then
        IncomingRevenue incomingRevenue = receitaService.convertFromModel(model);
        assertEquals(Optional.ofNullable(incomingRevenue.getRevenueItem().getItemApplicationCode()), Optional.of(26L));
    }

    private VwPmsIntegReceitaNacional build(Long serviceCode) {
        VwPmsIntegReceitaNacional model = new VwPmsIntegReceitaNacional();

        model.setServiceCode(serviceCode);
        model.setRevenueCode(1L);
        model.setRevenueSource("PMS");
        model.setIsIntercompany("N");
        model.setOperationCode("TESTE");
        model.setBranchCompanyCode(1L);
        model.setIssueAt(new Date());
        model.setPaymentConditionName("5 DIAS");
        model.setTotalValue(BigDecimal.valueOf(1000.25));
        model.setInvoiceSituation("PENDING");
        model.setDocumentTypeCode(1L);
        model.setAuxCode("A");
        model.setCalcAgentCode("STC");
        model.setCalcAgentTypeCode("CTS");
        model.setItemOperationCode("25");
        model.setItemCode(1L);
        model.setItemQuantity(25L);
        model.setUnitValue(BigDecimal.valueOf(10.0));
        model.setItemApplicationCode(26L);
        model.setClassType("Vd Serv");
        model.setIdProjectCode(1L);
        model.setProjectReduceCode(15844L);
        model.setIdCostCenterCode(1L);
        model.setCostCenterReduceCode(1153L);
        model.setInstallmentOperationCode("I");
        model.setInstallmentNumber(1L);
        model.setInstallmentValue(BigDecimal.valueOf(675897.5610351562));

        return model;

    }
}
