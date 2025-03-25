package com.ciandt.pms.business.service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.impl.IntegrateLicenseService;
import com.ciandt.pms.business.service.impl.TiRecursoService;
import com.ciandt.pms.business.service.impl.VwPmsGrupoContaContabilService;
import com.ciandt.pms.integration.vo.licenses.IntegLicense;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.IntegrateLicense;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.VwPmsGrupoContaContabil;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.persistence.dao.jpa.IntegrateLicenseDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IntegrateLicenseServiceTest {

    @Mock
    private TiRecursoService tiRecursoService;

    @Mock
    private IntegrateLicenseDao dao;

    @Mock
    private IRateioLicencaSwService rateioService;

    @Mock
    private VwPmsGrupoContaContabilService grupoContaContabilService;

    @InjectMocks
    private IntegrateLicenseService service;


    @Test
    public void findIntegrateDetailsByTiRecurso_givenValidDetails_thenReturnDetails() {
        LicencaSwDetail detail = new LicencaSwDetail();
        detail.setNomeProjetoErp("Project");
        detail.setNomeGrupoCusto("Cost Center");
        List<LicencaSwDetail> details = Collections.singletonList(detail);

        TiRecurso tiRecurso = new TiRecurso();
        tiRecurso.setEmpresa(new Empresa());
        tiRecurso.getEmpresa().setCodigoEmpresa(Constants.CD_EMPRESA_INC);

        when(tiRecursoService.findTiRecursoById(anyLong())).thenReturn(tiRecurso);
        when(dao.findIntegratesByResourceAndDateAndProject(anyLong(), any(), anyString())).thenReturn(Collections.singletonList(new IntegrateLicense()));

        List<LicencaSwDetail> result = service.findIntegrateDetailsByTiRecurso(1L, new Date(), details);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getIntegrate());
    }

    @Test
    public void findIntegrateDetailsByTiRecurso_givenNullDetails_thenReturnEmptyList() {
        List<LicencaSwDetail> result = service.findIntegrateDetailsByTiRecurso(1L, new Date(), null);
        assertNull(result);
    }

    @Test
    public void findIntegrateDetailsByTiRecurso_givenEmptyDetails_thenReturnEmptyList() {
        List<LicencaSwDetail> result = service.findIntegrateDetailsByTiRecurso(1L, new Date(), Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void payload_givenValidDetails_thenReturnPayload() {
        TiRecurso tiRecurso = new TiRecurso();
        tiRecurso.setNomeTiRecurso("Resource Name");
        when(tiRecursoService.findTiRecursoById(anyLong())).thenReturn(tiRecurso);
        when(rateioService.findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(any(Date.class), anyLong())).thenReturn(this.getDetails());
        when(grupoContaContabilService.findByCodigoContratoPratica(anyLong())).thenReturn(this.getGrupoContaContabilService());
        IntegLicense result = service.payload(1L, new Date(), 1L);

        assertNotNull(result);
        assertEquals("Resource Name", result.getBatchName());
    }

    @Test
    public void payload_givenNullDetails_thenReturnEmptyPayload() {
        IntegLicense result = service.payload(1L, new Date(), 1L);

        assertNotNull(result);
        assertNull(result.getBatchName());
    }

    private VwPmsGrupoContaContabil getGrupoContaContabilService() {
        VwPmsGrupoContaContabil grupoContaContabil = new VwPmsGrupoContaContabil();
        grupoContaContabil.setCodigoGrupoCusto(1L);
        grupoContaContabil.setNomeGrupoCusto("Cost Center");
        return grupoContaContabil;
    }

    private List<LicencaSwDetail> getDetails() {
        LicencaSwDetail detail = new LicencaSwDetail();
        detail.setNomeProjetoErp("Project");
        detail.setNomeGrupoCusto("Cost Center");
        detail.setValor(BigDecimal.valueOf(100));
        detail.setCodigoGrupoCusto(1000L);
        detail.setCodigoProjetoErp(1000L);
        return Collections.singletonList(detail);
    }

}
