package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IContratoPraticaCentroLucroService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.enums.NaturezaCentroLucroSigla;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.persistence.dao.IContratoPraticaDao;
import com.ciandt.pms.persistence.dao.IMapaAlocacaoDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContratoPraticaServiceTest  {

    @Mock
    private IMapaAlocacaoDao mapaAlocacaoDao;

    @Mock
    private IMapaAlocacaoService mapaAlocacaoService;

    @Mock
    private IContratoPraticaCentroLucroService cpclService;

    @Mock
    private ContratoPraticaService contratoPraticaService;

    @Mock
    private INaturezaCentroLucroService naturezaCentroLucroService;

    @Mock
    private IContratoPraticaDao contratoPraticaDao;

    @InjectMocks
    private ContratoPraticaService contratoPraticaServiceMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAtualizaNomesContratoPratica_ValidPratica() {
        Pratica pratica = new Pratica();
        pratica.setCodigoPratica(1L);
        pratica.setSiglaPratica("CIANDT");

        ContratoPratica contratoPratica1 = new ContratoPratica();
        contratoPratica1.setMsa(new Msa(1L, "MSA 1"));
        contratoPratica1.setNomeContratoPratica("MSA 1-cit");
        contratoPratica1.setPratica(pratica);

        ContratoPratica contratoPratica2 = new ContratoPratica();
        contratoPratica2.setMsa(new Msa(2L, "MSA 2"));
        contratoPratica2.setNomeContratoPratica("MSA 2-cit");
        contratoPratica2.setPratica(pratica);

        when(contratoPraticaDao.findByPratica(pratica.getCodigoPratica()))
                .thenReturn(Arrays.asList(contratoPratica1, contratoPratica2));

        when(cpclService.findPresentByContratoPraticaAndNatureza(new ContratoPratica(), new NaturezaCentroLucro())).thenReturn(new ContratoPraticaCentroLucro());
        when(naturezaCentroLucroService.findBySigla(NaturezaCentroLucroSigla.LOB)).thenReturn(new NaturezaCentroLucro());
        when(contratoPraticaService.isSameNamePraticaAndCentroLucro(new ContratoPratica(),new NaturezaCentroLucro())).thenReturn(true);

        contratoPraticaServiceMock.atualizaContratoPratica(contratoPratica1);
        contratoPraticaServiceMock.atualizaContratoPratica(contratoPratica2);

        verify(mapaAlocacaoService, times(2)).atualizaSufixosMapaAlocacao(any(ContratoPratica.class));
        assertEquals("MSA 1-CIANDT", contratoPratica1.getNomeContratoPratica());
        assertEquals("MSA 2-CIANDT", contratoPratica2.getNomeContratoPratica());
    }

}