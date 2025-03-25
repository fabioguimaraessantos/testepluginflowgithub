package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.integration.queue.RevenueProducer;
import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.jpa.ReceitaDao;
import com.ciandt.pms.persistence.dao.jpa.VwItemReceitaDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ReceitaServiceTest {

    @InjectMocks
    private ReceitaService service;

    @Mock
	private ReceitaDao receitaDao;

    @Mock
    private VwItemReceitaDao vwItemReceitaDao;

    @Mock
    private ItemReceitaService itemReceitaService;

    @Mock
    private ReceitaDealFiscalService receitaDealFiscalService;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private CidadeBaseService cidadeBaseService;

    @Mock
    private PerfilVendidoService perfilVendidoService;

    @Mock
    private ItemTabelaPrecoService itemTabelaPrecoService;

    @Mock
    private ContratoPraticaService contratoPraticaService;

    @Mock
    private ModuloService moduloService;

    @Mock
    private AlocacaoService alocacaoService;

    @Mock
    private AlocacaoPeriodoService alocacaoPeriodoService;

    @Mock
    private EtiquetaService etiquetaService;

    @Mock
    private ReceitaDealFiscalService rdfService;

    @Mock
    private FatorUrMesService fatorUrMesService;

    @Mock
    private MapaAlocacaoService mapaAlocacaoService;

    @Mock
    private MoedaService moedaService;

    @Mock
    private ReceitaMoedaService receitaMoedaService;

    @Mock
    private HistoricoReceitaService hiReService;

    @Mock
    private HistoricoPercentualIntercompService historicoPercentualIntercompService;

    @Mock
    private MsaService msaService;

    @Mock
    private CotacaoMoedaMediaService cotacaoMoedaMediaService;

    @Mock
    private Properties systemProperties;

    @Mock
    private ReceitaResultadoService receitaResultadoService;

    @Mock
    private AjusteReceitaService ajusteReceitaSerice;

    @Mock
    private ReceitaLicencaService receitaLicencaService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private DealFiscalService dealFiscalService;

    @Mock
    private CpraticaDfiscalService cpraticaDfiscalService;

    @Mock
    private ReceitaPlantaoService receitaPlantaoService;

    @Mock
    private PessoaCidadeBaseService pessoaCidadeBaseService;

    @Mock
    private DiasUteisCidadeBaseService diasUteisCidadeBaseService;

    @Mock
    private PessoaTipoContratoService pessoaTipoContratoService;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private RevenueProducer revenueProducer;

    @Mock
    private MailSenderUtil mailSenderUtil;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenOceaniaInternationalIntercompanyRevenue_whenChekingInternationalIntercompanyRevenue_thenSuccess() {

        //given
        Cliente cliente = new Cliente();
        cliente.setIndicadorTipo(Constants.CLIENTE_TYPE_INTERNATIONAL);
        cliente.setIndicadorAtivo("A");

        Empresa empresa = new Empresa();
        empresa.setCodigoEmpresa(Constants.CD_EMPRESA_OCEANIA);

        Empresa empresaIntercompany = new Empresa();
        empresaIntercompany.setCodigoEmpresa(Constants.CD_EMPRESA_AUS);
        Empresa empresaIntercompanyMatriz = new Empresa();
        empresaIntercompanyMatriz.setCodigoEmpresa(124L);
        empresaIntercompany.setEmpresa(empresaIntercompanyMatriz);

        DealFiscal dealFiscal = new DealFiscal();
        dealFiscal.setCodigoDealFiscal(0L);
        dealFiscal.setIndicadorEntrega("IN");
        dealFiscal.setIndicadorIntercompany(Boolean.TRUE);
        dealFiscal.setCliente(cliente);
        dealFiscal.setEmpresa(empresa);
        dealFiscal.setEmpresaIntercomp(empresaIntercompany);

        HistoricoPercentualIntercomp historicoPercentualIntercomp =  new HistoricoPercentualIntercomp();
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
        pessoaTipoContrato.setEmpresa(empresaIntercompanyMatriz);

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

        final Map<Long, String> companyErp = new HashMap<>();
        companyErp.put(empresa.getCodigoEmpresa(), "ORACLE");

        //when
        Mockito.when(receitaDealFiscalService
                .findReceitaDealByReceita(receita)).thenReturn(receitasDealFiscalList);
        Mockito.when(dealFiscalService.findDealFiscalById(receitaDealFiscal
                        .getDealFiscal().getCodigoDealFiscal())).thenReturn(dealFiscal);
        Mockito.when(clienteService.findClienteById(dealFiscal.getCliente()
                        .getCodigoCliente())).thenReturn(cliente);
        Mockito.when(empresaService.findEmpresaById(dealFiscal.getEmpresa()
                        .getCodigoEmpresa())).thenReturn(empresa);
        Mockito.when(receitaDao.findById(receita.getCodigoReceita())).thenReturn(receita);
        Mockito.when(historicoPercentualIntercompService.findByDealFiscal(dealFiscal.getCodigoDealFiscal())).thenReturn(historicoPercentualIntercompList);
        Mockito.when(pessoaTipoContratoService
                        .findPessTCByPessoaAndDate(itemReceita.getPessoa(), itemReceita
                                .getReceitaMoeda().getReceita().getDataMes())).thenReturn(pessoaTipoContrato);
        Mockito.when(empresaService
                        .findEmpresaById(pessoaTipoContrato.getEmpresa()
                                .getCodigoEmpresa())).thenReturn(pessoaTipoContrato.getEmpresa());

        //then
        boolean resultIsInternationalRevenue = service.isInternationalRevenue(companyErp, receita);
        boolean resultInternationalIntercompanyRevenue = service.isInternationalRevenueWithIntercompany(companyErp, receita, dealFiscal);

        Assert.assertEquals(Boolean.TRUE, resultIsInternationalRevenue);
        Assert.assertEquals(Boolean.TRUE, resultInternationalIntercompanyRevenue);
    }
}
