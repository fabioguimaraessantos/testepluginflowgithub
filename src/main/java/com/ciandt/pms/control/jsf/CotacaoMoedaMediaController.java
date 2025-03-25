/**
 * Classe CotacaoMoedaController - Classe da camada de apresentação da CotacaoMoeda
 */
package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICotacaoMoedaMediaService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.control.jsf.bean.CotacaoMoedaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.ExchangeRateException;
import com.ciandt.pms.model.CotacaoMoedaMedia;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.util.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * A classe CotacaoMoedaController proporciona as funcionalidades da camada de
 * apresentação para a entidade CotacaoMoeda.
 * 
 * @since 15/03/2016
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.EXCHANGE_RATES:VW" })
public class CotacaoMoedaMediaController {

    /*********** OUTCOMES **************************/

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_COTACAO_MOEDA_RESEARCH = "cotacaoMoeda_research";
    private static final String OUTCOME_COTACAO_MOEDA_COPY = "cotacaoMoeda_copy";
    private static final String OUTCOME_COTACAO_MOEDA_DELETE = "cotacaoMoeda_delete";

    /*********** SERVICES **************************/

    /** Instancia do servico. */
    @Autowired
    private ICotacaoMoedaMediaService cotacaoMoedaMediaService;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /*********** BEANS **************************/

    /** Instancia do bean. */
    @Autowired
    private CotacaoMoedaBean bean = null;

    /********************************************/
    
    /**
     * @return the bean
     */
    public CotacaoMoedaBean getBean() {
        return bean;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(final CotacaoMoedaBean bean) {
        this.bean = bean;
    }

    public int getPARAM_DIAS_VALIDACAO_PASSWORD() {
        return Constants.PARAM_DIAS_VALIDACAO_PASSWORD;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();

        // carrega as listas de entidades
        loadMoedaList(moedaService.findMoedaAll());

        this.setPeriodFilter();
        
        return OUTCOME_COTACAO_MOEDA_RESEARCH;
    }

    /**
     * Seta o range da datas do filtro. - Data inicial = (Data de hoje) - (1
     * mês) - Data final = (Data corrente) + (3 meses)
     */
    private void setPeriodFilter() {
        Date date = new Date();

        if (bean.getDataDiaBeg() == null) {
            bean.setDataDiaBeg(DateUtils.addMonths(date, -1));
        }
        if (bean.getDataDiaEnd() == null) {
            bean.setDataDiaEnd(DateUtils.addMonths(date, 2));
        }

    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        boolean isValidDate = false;

        Date dataDiaBeg = bean.getDataDiaBeg();
        Date dataDiaEnd = bean.getDataDiaEnd();
        if (dataDiaBeg != null && dataDiaEnd != null) {
            if (DateUtil.validateValidityDate(dataDiaBeg, dataDiaEnd, Boolean
                    .valueOf(false))) {
                isValidDate = true;
            }
        }

        if (isValidDate) {
            CotacaoMoedaMedia filter = bean.getFilter();

            Long codigoMoedaDe = bean.getMoedaDeMap().get(
                    filter.getMoedaDe().getNomeMoeda());
            if (codigoMoedaDe != null) {
                filter.getMoedaDe().setCodigoMoeda(codigoMoedaDe);
            } else {
                filter.setMoedaDe(null);
            }
            
            Long codigoMoedaPara = bean.getMoedaParaMap().get(
            		filter.getMoedaPara().getNomeMoeda());
            if (codigoMoedaPara != null) {
            	filter.getMoedaPara().setCodigoMoeda(codigoMoedaPara);
            } else {
            	filter.setMoedaPara(null);
            }

            // realiza a busca pela CotacaoMoeda
            bean.setResultList(cotacaoMoedaMediaService.findByFilter(
                    filter, dataDiaBeg, dataDiaEnd));

            // se nenhum resultado encontrado
            if (bean.getResultList().isEmpty()) {
                Messages.showWarning("findByFilter",
                        Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            }
        }
    }

    /**
     * Popula a lista de Moeda para combobox.
     * 
     * @param pMoedaList
     *            lista de Moeda.
     * 
     */
    private void loadMoedaList(final List<Moeda> pMoedaList) {

        Map<String, Long> moedaMap = new HashMap<String, Long>();
        List<String> moedaList = new ArrayList<String>();

        for (Moeda moeda : pMoedaList) {
            moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            moedaList.add(moeda.getNomeMoeda());
        }
        bean.setMoedaDeMap(moedaMap);
        bean.setMoedaParaMap(moedaMap);
        bean.setMoedaList(moedaList);
    }

    /**
     * Verifica se o valor do atributo Moeda é valido. Se o valor não é nulo e
     * existe no moedaMap, então o valor é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateMoedaDe(final FacesContext context,
            final UIComponent component, final Object value) {

        Long codigoMoeda = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            codigoMoeda = bean.getMoedaDeMap().get(strValue);
            if (codigoMoeda == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }
    
    /**
     * Verifica se o valor do atributo Moeda é valido. Se o valor não é nulo e
     * existe no moedaMap, então o valor é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateMoedaPara(final FacesContext context,
    		final UIComponent component, final Object value) {
    	
    	Long codigoMoeda = null;
    	String strValue = (String) value;
    	
    	if ((strValue != null) && (!"".equals(strValue))) {
    		codigoMoeda = bean.getMoedaParaMap().get(strValue);
    		if (codigoMoeda == null) {
    			String label = (String) component.getAttributes().get("label");
    			throw new ValidatorException(Messages.getMessageError(
    					Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
    		}
    	}
    }

    /**
     * Prepara a tela de cópia de cotações.
     *
     * @return pagina de destino
     */
    public String prepareCopy() {
        bean.reset();
        return OUTCOME_COTACAO_MOEDA_COPY;
    }

    /**
     * Prepara a tela de deletar cotações.
     *
     * @return pagina de destino
     */
    public String prepareDelete() {
        bean.reset();
        return OUTCOME_COTACAO_MOEDA_DELETE;
    }

    public void replace() {
        Date copyFromDate = bean.getCotacaoCopyFrom();
        Date copyUntilDate = bean.getCotacaoCopyUntil();
        String password = bean.getPassword();
        String ticket = bean.getTicket();

        if (copyFromDate == null || copyUntilDate == null ||
                !DateUtil.validateValidityDate(copyFromDate, copyUntilDate, Boolean.FALSE)) {
            return;
        }

        Date fiveDays = getLimitDateToValidatePassword();
        if (!copyFromDate.after(fiveDays) && !copyUntilDate.after(fiveDays)) {
            if (checkPassword(password)) return;
            if (checkTicket(ticket)) return;
            if (validateTicket(ticket)) return;
        } else {
            ticket = null;
            password = null;
        }

        try {
            cotacaoMoedaMediaService.replace(copyFromDate, copyUntilDate, password, ticket);
            Messages.showSucess("currencyCopy", Constants.MSG_EXCHANGE_COPY_SUCCESS);

        } catch (ExchangeRateException e) {
            Messages.showWarning("currencyCopy", e.getMessageBundleKey());
        } catch (Exception e) {
            Messages.showError("currencyCopy", Constants.GENERIC_ERROR);
        }
    }

    public void delete() {
        Date deleteFromDate = bean.getCotacaoDeleteFrom();
        Date deleteUntilDate = bean.getCotacaoDeleteUntil();
        String password = bean.getPassword();
        String ticket = bean.getTicket();

        if (deleteFromDate == null || deleteUntilDate == null) {
            return;
        }

        Date fiveDays = getLimitDateToValidatePassword();
        if (!deleteFromDate.after(fiveDays) && !deleteUntilDate.after(fiveDays)) {
            if (checkPassword(password)) return;
            if (checkTicket(ticket)) return;
            if (validateTicket(ticket)) return;
        } else {
            ticket = null;
            password = null;
        }

        try {
            cotacaoMoedaMediaService.delete(deleteFromDate, deleteUntilDate, password, ticket);
            Messages.showSucess("currencyCopy", Constants.MSG_EXCHANGE_DELETE_SUCCESS);
        } catch (Exception e) {
            Messages.showError("currencyCopy", Constants.GENERIC_ERROR);
        }
    }

    /**
     * Check if the ticket is empty.
     * @param ticket
     * @return
     */
    private static boolean checkTicket(String ticket) {
        if (ticket.isEmpty()) {
            Messages.showError("checkTicket", Constants.MSG_EXCHANGE_TICKET);
            return true;
        }
        return false;
    }

    /**
     * Check if the password is authorized.
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        if (!password.equals(getBundle(Constants.PWD_EXCHANGE_PASSWORD_1)) && !password.equals(getBundle(Constants.PWD_EXCHANGE_PASSWORD_2))) {
            Messages.showError("checkPassword", Constants.MSG_EXCHANGE_AUTH);
            return true;
        }
        return false;
    }

    private static String getBundle(String key) {
        return ResourceBundle.getBundle("resourceBundle").getString(key);
    }

    /**
     * Get the date five days ago.
     * @return
     */
    public static Date getLimitDateToValidatePassword() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -Constants.PARAM_DIAS_VALIDACAO_PASSWORD);
        Date fiveDays = calendar.getTime();
        return fiveDays;
    }

    /**
     * Validate the ticket format.
     * @param ticket
     * @return
     */
    private static boolean validateTicket(String ticket) {

        String pattern = "^[A-Za-z]{4}-\\d{4,5}$";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ticket);

        if (!matcher.matches()) {
            Messages.showError("validateTicket", Constants.MSG_EXCHANGE_TICKET);
            return true;
        }
        return false;
    }
}