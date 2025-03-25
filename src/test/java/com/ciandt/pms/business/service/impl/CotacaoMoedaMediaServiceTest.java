package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.exception.ExchangeRateException;
import com.ciandt.pms.model.CotacaoMoedaMedia;
import com.ciandt.pms.model.CotacaoMoedaMediaAud;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICotacaoMoedaMediaAudDao;
import com.ciandt.pms.persistence.dao.ICotacaoMoedaMediaDao;
import com.ciandt.pms.control.jsf.CotacaoMoedaMediaController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ciandt.pms.control.jsf.util.BundleUtil.getBundle;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CotacaoMoedaMediaServiceTest {

    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @InjectMocks
    private CotacaoMoedaMediaService service;

    @Mock
    private ICotacaoMoedaMediaDao dao;

    @Mock
    private IModuloService moduloService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private ICotacaoMoedaMediaAudDao iCotacaoMoedaMediaAudDao;

    @Test(expected = ExchangeRateException.class)
    public void copy_WithEqualDates() throws ExchangeRateException{

        //GIVEN DATES FROM and UNTIL
        Date from = getDate("16/09/2022");
        Date until = getDate("16/09/2022");
        String password = "password";
        String ticket = "ticket";


        //WHEN Copy method is Called
        service.replace(from, until, password, ticket);

        //THEN ExchangeRateException MUST BE THROWED
    }

    @Test(expected = ExchangeRateException.class)
    public void copy_WithoutExchanges() throws ExchangeRateException{

        //GIVEN DATES FROM and UNTIL
        Date from = getDate("16/09/2022");
        Date until = getDate("17/09/2022");
        String password = "password";
        String ticket = "ticket";

        //WITH
        Mockito.when(dao.findByDate(from)).thenReturn(Collections.emptyList());

        //WHEN Copy method is Called
        service.replace(from, until, password, ticket);

        //THEN ExchangeRateException MUST BE THROWED
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);

        // Mock UserDetails
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("testUser");

        // Configure SecurityContext to return the mocked Authentication
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    public void copy() throws ExchangeRateException{

        //GIVEN DATES FROM and UNTIL
        Date historyLockClosedAt = getDate("01/08/2022");
        Date from = getDate("16/09/2022");
        Date until = getDate("17/09/2022");
        String password = "password";
        String ticket = "ticket";

        //WITH
        Mockito.when(moduloService.getClosingDateHistoryLock()).thenReturn(historyLockClosedAt);
        Mockito.when(moduloService.dateAfterHistoryLock(until)).thenReturn(true);
        moduloService.dateAfterHistoryLock(until);
        Mockito.when(dao.findByDate(from)).thenReturn(getExchanges(from));

        //WHEN Copy method is Called
        service.replace(from, until, password, ticket);

        //THEN Method Executed Without Exception
        Assert.isTrue(Boolean.TRUE);
    }

    @Test(expected = ExchangeRateException.class)
    public void copy_withUntilEqualClosingDate_mustThrowExchangeRateException() throws ExchangeRateException {
        // given
        Date historyLockClosedAt = getDate("01/01/2023");

        Date from = getDate("30/01/2023");
        Date until = getDate("31/01/2023");
        String password = "password";
        String ticket = "ticket";


        // when
        Mockito.when(moduloService.getClosingDateHistoryLock()).thenReturn(historyLockClosedAt);
        service.replace(from, until, password, ticket);

        // then ExchangeRateException must be throwed
    }

    @Test(expected = ExchangeRateException.class)
    public void copy_withUntilBeforeClosingDate_mustThrowExchangeRateException() throws ExchangeRateException {
        // given
        Date historyLockClosedAt = getDate("01/01/2023");

        Date from = getDate("30/12/2022");
        Date until = getDate("31/12/2022");
        String password = "password";
        String ticket = "ticket";


        // when
        Mockito.when(moduloService.getClosingDateHistoryLock()).thenReturn(historyLockClosedAt);
        service.replace(from, until, password, ticket);

        // then ExchangeRateException must be throwed
    }

    @Test
    public void testDelete() {
        // GIVEN DATES FROM and UNTIL
        Date from = getDate("16/09/2022");
        Date until = getDate("17/09/2022");
        String password = "password";
        String ticket = "ticket";

        // WHEN delete method is Called
        service.delete(from, until, password, ticket);

        // THEN verify that deleteByDate was called for each date in the range
        verify(dao, times(1)).deleteByDate(from);
        verify(dao, times(1)).deleteByDate(until);

        // Verify that logAudit was called
        verify(iCotacaoMoedaMediaAudDao, times(1)).create(Mockito.any(CotacaoMoedaMediaAud.class));
    }

    /**
     * @param date - Date String to Convet
     * @return - Date converted
     */
    private Date getDate(String date){

        try{
            return format.parse(date);
        }catch (ParseException pe){
            return new Date();
        }
    }

    /**
     * @param date - Date from Quotation
     * @return List - CotacaoMoedaMedia
     */
    private List<CotacaoMoedaMedia> getExchanges(Date date){
        List<CotacaoMoedaMedia> exchanges = new ArrayList<>();
        exchanges.add(getCotacao(date));

        return exchanges;
    }

    /**
     * @param date - Date from Quotation
     * @return - Object Created
     */
    private CotacaoMoedaMedia getCotacao(Date date){
        CotacaoMoedaMedia exchange = new CotacaoMoedaMedia();
        exchange.setDataCotacao(date);
        exchange.setMoedaDe(getMoeda(Boolean.TRUE));
        exchange.setMoedaPara(getMoeda(Boolean.FALSE));
        exchange.setIndicadorNovaCotacao("Y");
        exchange.setValorMediaAno(new BigDecimal("5.124625"));
        exchange.setValorMediaMes(new BigDecimal("5.1854899"));
        exchange.setValorCotacaoVenda(new BigDecimal("5.2211006"));
        return exchange;
    }

    private Moeda getMoeda(Boolean isDolar){
        Moeda moeda = new Moeda();
        moeda.setCodigoMoeda(isDolar ? 2L : 1L);
        moeda.setNomeMoeda(isDolar ? "Dolar" : "Real");

        return moeda;
    }

    @Test
    public void testGetCurrentUser_withUserDetails() {
        String currentUser = service.getCurrentUser();
        assertEquals("testUser", currentUser);
    }

    @Test
    public void testGetCurrentUser_withStringPrincipal() {
        // Mock SecurityContext and Authentication
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);

        // Configure SecurityContext to return the mocked Authentication
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn("testPrincipal");

        String currentUser = service.getCurrentUser();
        assertEquals("testPrincipal", currentUser);
    }

    @Test
    public void testCheckPassword_validPassword1() {
        String validPassword1 = getBundle(Constants.PWD_EXCHANGE_PASSWORD_1);
        assertFalse(CotacaoMoedaMediaController.checkPassword(validPassword1));
    }

    @Test
    public void testCheckPassword_validPassword2() {
        String validPassword2 = getBundle(Constants.PWD_EXCHANGE_PASSWORD_2);
        assertFalse(CotacaoMoedaMediaController.checkPassword(validPassword2));
    }

    @Test
    public void testCheckPassword_invalidPassword() {
        String invalidPassword = "invalidPassword";
        assertTrue(CotacaoMoedaMediaController.checkPassword(invalidPassword));
    }

    @Test
    public void testGetFiveDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -Constants.PARAM_DIAS_VALIDACAO_PASSWORD);
        Date expectedDate = calendar.getTime();

        Date actualDate = CotacaoMoedaMediaController.getLimitDateToValidatePassword();

        assertEquals(expectedDate.getTime() / 1000, actualDate.getTime() / 1000);
    }
}
