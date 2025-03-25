package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.persistence.dao.IMapaAlocacaoDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapaAlocacaoServiceTest {

    @Mock
    private IMapaAlocacaoDao mapaAlocacaoDao;

    @Mock
    private Properties systemProperties;

    @InjectMocks
    private MapaAlocacaoService mapaAlocacaoService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this); // Inicializa os mocks
    }

    @Test
    public void testUpdateMapaAlocacao_ValidEntity() {
        ContratoPratica contratoPratica = new ContratoPratica();
        contratoPratica.setNomeContratoPratica("tituloOriginal-Teste");
        Pratica pratica = new Pratica();
        pratica.setSiglaPratica("Teste");
        contratoPratica.setPratica(pratica);

        MapaAlocacao mapaAlocacao = new MapaAlocacao();
        mapaAlocacao.setTextoTitulo("tituloOriginal");
        mapaAlocacao.setContratoPratica(contratoPratica);

        when(systemProperties.get(Constants.MAPA_ALOCACAO_DEFAULT_ACRONYM_NAME)).thenReturn("Map-");
        mapaAlocacaoService.updateMapaAlocacao(mapaAlocacao);

        verify(mapaAlocacaoDao).update(mapaAlocacao);
        assertEquals("Map-tituloOriginal-Teste", mapaAlocacao.getTextoTitulo()); // Assert que o título não foi alterado
    }

    @Test
    public void testAtualizaSufixosMapaAlocacao_ValidContratoPratica() {
        ContratoPratica contratoPratica = new ContratoPratica();
        contratoPratica.setNomeContratoPratica("Contrato1-Teste");
        Pratica pratica = new Pratica();
        pratica.setSiglaPratica("Teste");
        contratoPratica.setPratica(pratica);


        MapaAlocacao mapaAlocacao1 = new MapaAlocacao();
        mapaAlocacao1.setTextoTitulo("tituloOriginal1");
        mapaAlocacao1.setContratoPratica(contratoPratica);

        when(systemProperties.get(Constants.MAPA_ALOCACAO_DEFAULT_ACRONYM_NAME)).thenReturn("Map-");
        when(mapaAlocacaoDao.findByFilter(any(MapaAlocacao.class)))
                .thenReturn(Arrays.asList(mapaAlocacao1));

        mapaAlocacaoService.atualizaSufixosMapaAlocacao(contratoPratica);

        verify(mapaAlocacaoDao, times(1)).update(any(MapaAlocacao.class));
        assertEquals("Map-Contrato1-Teste", mapaAlocacao1.getTextoTitulo());
    }

    @Test
    public void testAtualizaSufixosMapaAlocacao_NullContratoPratica() {
        mapaAlocacaoService.atualizaSufixosMapaAlocacao(null);
        verify(mapaAlocacaoDao, never()).findByFilter(any(MapaAlocacao.class));
    }
}