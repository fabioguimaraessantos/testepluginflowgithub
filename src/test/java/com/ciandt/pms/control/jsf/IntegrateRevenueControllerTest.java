package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.impl.ClienteService;
import com.ciandt.pms.business.service.impl.CompanyErpService;
import com.ciandt.pms.business.service.impl.ContratoPraticaService;
import com.ciandt.pms.business.service.impl.DealFiscalService;
import com.ciandt.pms.business.service.impl.EmpresaService;
import com.ciandt.pms.business.service.impl.FaturaService;
import com.ciandt.pms.business.service.impl.HistoricoPercentualIntercompService;
import com.ciandt.pms.business.service.impl.PessoaTipoContratoService;
import com.ciandt.pms.business.service.impl.ReceitaDealFiscalService;
import com.ciandt.pms.business.service.impl.ReceitaIntegracaoSemaforoService;
import com.ciandt.pms.business.service.impl.ReceitaLicencaService;
import com.ciandt.pms.business.service.impl.ReceitaPlantaoService;
import com.ciandt.pms.business.service.impl.ReceitaService;
import com.ciandt.pms.business.service.impl.TipoServicoService;
import com.ciandt.pms.business.service.impl.VwPmsIntegFaturaNacionalService;
import com.ciandt.pms.business.service.impl.VwPmsIntegReceitaNacionalService;
import com.ciandt.pms.control.jsf.bean.IntegrateRevenueBean;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.integration.queue.RevenueProducer;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.HistoricoPercentualIntercomp;
import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaTipoContrato;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaIntegracaoSemaforo;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.model.vo.ReceitaDealFiscalRow;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IntegrateRevenueControllerTest {

    @InjectMocks
    private IntegrateRevenueController controller;

    @Mock
    private IntegrateRevenueBean bean;

    @Mock
    private DealFiscalService dealFiscalService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private ReceitaPlantaoService receitaPlantaoService;

    @Mock
    private ReceitaService receitaService;

    @Mock
    private ReceitaIntegracaoSemaforoService receitaIntegracaoSemaforoService;

    @Mock
    private HistoricoPercentualIntercompService historicoPercentualIntercompService;

    @Mock
    private PessoaTipoContratoService pessoaTipoContratoService;

    @Mock
    private ReceitaDealFiscalService receitaDealFiscalService;

    @Mock
    private ReceitaDealFiscalService rdfService;

    @Mock
    private ContratoPraticaService contratoPraticaService;

    @Mock
    private TipoServicoService tipoServicoService;

    @Mock
    private ReceitaLicencaService receitaLicencaService;

    @Mock
    private VwPmsIntegReceitaNacionalService vwPmsIntegReceitaNacionalService;

    @Mock
    private VwPmsIntegFaturaNacionalService vwPmsIntegFaturaNacionalService;

    @Mock
    private MailSenderUtil mailSenderUtil;

    @Mock
    private FaturaService faturaService;

    @Mock
    private RevenueProducer revenueProducer;

    @Mock
    private CompanyErpService companyErpService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void integrate_givenOceaniaInternationalRevenueWithIntercompany_thenSuccess() throws BusinessException {

        //given
        controller.setBean(new IntegrateRevenueBean());

        Cliente cliente = new Cliente();
        cliente.setIndicadorTipo(Constants.CLIENTE_TYPE_INTERNATIONAL);
        cliente.setIndicadorAtivo("A");

        Empresa empresa = new Empresa();
        empresa.setCodigoEmpresa(Constants.CD_EMPRESA_OCEANIA);

        Empresa empresaIntercompany = new Empresa();
        empresaIntercompany.setCodigoEmpresa(Constants.CD_EMPRESA_AUS);

        DealFiscal dealFiscal = new DealFiscal();
        dealFiscal.setCodigoDealFiscal(0L);
        dealFiscal.setIndicadorEntrega("IN");
        dealFiscal.setIndicadorIntercompany(Boolean.TRUE);
        dealFiscal.setCliente(cliente);
        dealFiscal.setEmpresa(empresa);
        dealFiscal.setEmpresaIntercomp(empresaIntercompany);

        HistoricoPercentualIntercomp historicoPercentualIntercomp = new HistoricoPercentualIntercomp();
        historicoPercentualIntercomp.setDealFiscal(dealFiscal);
        historicoPercentualIntercomp.setEmpresaIntercomp(empresaIntercompany);
        historicoPercentualIntercomp.setDataFim(DateUtil.getDateLastDayOfMonth(new Date()));

        List<HistoricoPercentualIntercomp> historicoPercentualIntercompList = new ArrayList<>();
        historicoPercentualIntercompList.add(historicoPercentualIntercomp);

        Receita receita = new Receita();
        receita.setCodigoReceita(0L);

        Pessoa pessoa = new Pessoa();

        PessoaTipoContrato pessoaTipoContrato = new PessoaTipoContrato();
        pessoaTipoContrato.setPessoa(pessoa);
        pessoaTipoContrato.setEmpresa(empresaIntercompany);

        ItemReceita itemReceita = new ItemReceita();
        itemReceita.setValorTotalItem(new BigDecimal(1));
        itemReceita.setPessoa(pessoa);
        List<ItemReceita> itemReceitasList = new ArrayList<>();
        itemReceitasList.add(itemReceita);

        ReceitaMoeda receitaMoeda = new ReceitaMoeda();
        receitaMoeda.setReceita(receita);
        receitaMoeda.setItemReceitas(itemReceitasList);
        itemReceita.setReceitaMoeda(receitaMoeda);
        List<ReceitaMoeda> receitaMoedasList = new ArrayList<>();
        receitaMoedasList.add(receitaMoeda);

        receita.setReceitaMoedas(receitaMoedasList);

        ReceitaDealFiscal receitaDealFiscal = new ReceitaDealFiscal();
        receitaDealFiscal.setCodigoReceitaDfiscal(0L);
        receitaDealFiscal.setDealFiscal(dealFiscal);
        receitaDealFiscal.setIndicadorStatus(null);
        receitaDealFiscal.setReceitaMoeda(receitaMoeda);

        List<ReceitaDealFiscal> receitasDealFiscalList = new ArrayList<>();
        receitasDealFiscalList.add(receitaDealFiscal);

        ReceitaDealFiscalRow receitaDealFiscalRow = new ReceitaDealFiscalRow();
        receitaDealFiscalRow.setIsSelected(Boolean.TRUE);
        receitaDealFiscalRow.setTo(receitaDealFiscal);

        List<ReceitaDealFiscalRow> receitaDealFiscalRowList = new ArrayList<>();
        receitaDealFiscalRowList.add(receitaDealFiscalRow);
        controller.getBean().setReceitaDealFiscalRowList(receitaDealFiscalRowList);

        ReceitaIntegracaoSemaforo receitaIntegracaoSemaforo = new ReceitaIntegracaoSemaforo();
        receitaIntegracaoSemaforo.setReceitaDealFiscal(receitaDealFiscal);
        List<ReceitaIntegracaoSemaforo> receitaIntegracaoSemaforoList = new ArrayList<>();

        final Map<Long, String> companyErp = new HashMap<>();
        companyErp.put(empresa.getCodigoEmpresa(), "ORACLE");

        //with
        doNothing().when(receitaIntegracaoSemaforoService).createReceitaIntegracaoSemaforo(receitaIntegracaoSemaforo);
        doNothing().when(receitaDealFiscalService).updateReceitaDealFiscal(receitaDealFiscal);

        //when
        when(bean.getIsLicenca()).thenReturn(Boolean.FALSE);

        when(dealFiscalService.findDealFiscalById(receitaDealFiscal
                .getDealFiscal().getCodigoDealFiscal())).thenReturn(dealFiscal);

        when(receitaPlantaoService.findByReceitaDealFiscal(receitaDealFiscal)).thenReturn(null);

        when(receitaIntegracaoSemaforoService.findByReceitaDealFiscal(receitaDealFiscal.getCodigoReceitaDfiscal())).thenReturn(receitaIntegracaoSemaforoList);

        when(companyErpService.findAllActive()).thenReturn(companyErp);

        when(receitaService.isInternationalRevenueWithoutIntercompanyResource(companyErp, receita, dealFiscal)).thenReturn(Boolean.FALSE);
        when(receitaService.isInternationalRevenueWithIntercompany(companyErp, receita, dealFiscal)).thenReturn(Boolean.TRUE);

        when(receitaDealFiscalService.findReceitaDealById(receitaDealFiscal.getCodigoReceitaDfiscal())).thenReturn(receitaDealFiscal);

        controller.integrateAll();

        //then
        assertEquals(Constants.INTEGRACAO_STATUS_PENDENTE, receitaDealFiscal.getIndicadorStatus());
        assertEquals(receitaIntegracaoSemaforo.getReceitaDealFiscal(), receitaDealFiscal);
        assertEquals(Constants.NO, receitaDealFiscal.getIndicadorIntegradoQuickbooks());
        assertEquals(false, receitaDealFiscal.getIndicadorIntegradoQBO());

    }
}
