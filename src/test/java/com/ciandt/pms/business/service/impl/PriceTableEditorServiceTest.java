package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PriceTableEditor;
import com.ciandt.pms.persistence.dao.jpa.PriceTableEditorDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class PriceTableEditorServiceTest {

    @InjectMocks
    PriceTableEditorService service;

    @Mock
    PessoaService pessoaService;

    @Mock
    PriceTableEditorDao dao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getEditorsEmailByMsaCode_withNonNullPerson_mustReturnAllEmails() {
        // given
        Long msaCode = 12L;

        Pessoa login1 = new Pessoa();
        login1.setCodigoPessoa(1L);
        login1.setCodigoLogin("login1");
        login1.setTextoEmail("login1@ciandt.com");

        Pessoa login2 = new Pessoa();
        login2.setCodigoPessoa(1L);
        login2.setCodigoLogin("login2");
        login2.setTextoEmail("login2@ciandt.com");

        PriceTableEditor editor1 = new PriceTableEditor();
        editor1.setLogin("login1");
        PriceTableEditor editor2 = new PriceTableEditor();
        editor2.setLogin("login2");

        // when
        when(service.findByMsaCode(msaCode)).thenReturn(Arrays.asList(editor1, editor2));
        when(pessoaService.findPessoaByLogin(login1.getCodigoLogin())).thenReturn(login1);
        when(pessoaService.findPessoaByLogin(login2.getCodigoLogin())).thenReturn(login2);
        List<String> emails = service.getEditorsEmailByMsaCode(msaCode);

        // then
        assertFalse(emails.isEmpty());
        assertEquals(2, emails.size());
    }

    @Test
    public void getEditorsEmailByMsaCode_withNullPerson_mustReturnOnlyExistent() {
        // given
        Long msaCode = 12L;

        Pessoa login1 = new Pessoa();
        login1.setCodigoPessoa(1L);
        login1.setCodigoLogin("login1");
        login1.setTextoEmail("login1@ciandt.com");

        PriceTableEditor editor1 = new PriceTableEditor();
        editor1.setLogin("login1");
        PriceTableEditor editor2 = new PriceTableEditor();
        editor2.setLogin("login2");

        // when
        when(service.findByMsaCode(msaCode)).thenReturn(Arrays.asList(editor1, editor2));
        when(pessoaService.findPessoaByLogin(login1.getCodigoLogin())).thenReturn(login1);
        when(pessoaService.findPessoaByLogin("inexistent")).thenReturn(null);
        List<String> emails = service.getEditorsEmailByMsaCode(msaCode);

        // then
        assertFalse(emails.isEmpty());
        assertEquals(1, emails.size());
    }
}