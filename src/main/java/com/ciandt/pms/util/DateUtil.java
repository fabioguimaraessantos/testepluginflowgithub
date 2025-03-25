package com.ciandt.pms.util;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A classe DateUtil proporciona as funcionalidades de utilizades para Datas.
 *
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * @since 19/08/2009
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public final class DateUtil {

    /**
     * Construtor privado.
     */
    private DateUtil() {

    }

	private static final String DATE_NOT_NULL = "The 'date' can not be null.";

    /**
     * Pega o numero do mes da data passada por parametro.
     *
     * @param data que deseja seber o m�s
     * @return retorna o n�mero do mes
     */
    public static int getMonthNumber(final Date data) {
        Calendar calendar = new GregorianCalendar();

        calendar.setTime(data);

        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * Pega o numero do mes da data passada por parametro.
     *
     * @param data que deseja seber o m�s
     * @return retorna o n�mero do mes como uma string
     */
    public static String getMonthString(final Date data) {
        int mesNmbr = getMonthNumber(data);

        if (mesNmbr < 10) {
            return "0" + String.valueOf(mesNmbr);
        }

        return String.valueOf(mesNmbr);
    }

    /**
     * Pega o ano da data passada por parametro.
     *
     * @param data que deseja seber o m�s
     * @return retorna o ano como uma string
     */
    public static String getYearString(final Date data) {
        return String.valueOf(getYear(data));
    }

    /**
     * Pega o ano da data passada por parametro.
     *
     * @param data que deseja seber o ano
     * @return retorna o ano
     */
    public static int getYear(final Date data) {
        Calendar calendar = new GregorianCalendar();

		calendar.setTime(data);

		return calendar.get(Calendar.YEAR);
	}

	/**
	 * Pega o dia do mes da data passada por parametro.
	 * 
	 * @param data
	 *            que deseja seber o dia
	 * @return retorna o ano
	 */
	public static int getDayOfMonth(final Date data) {
		Calendar calendar = new GregorianCalendar();

        calendar.setTime(data);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Pega o primeiro dia do ano da data passada por parametro.
     *
     * @param date que deseja seber o primeiro dia do mes
     * @return retorna o ano
     */
    public static Date getDateFirstDayOfYear(final Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * Pega o primeiro dia do mes da data passada por parametro.
     *
     * @param date que deseja seber o primeiro dia do mes
     * @return retorna o ano
     */
    public static Date getDateFirstDayOfMonth(final Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    /**
     * Pega a data com �ltimo dia do mes da data passada por parametro.
     *
     * @param date que deseja seber o primeiro dia do mes
     * @return retorna o ano
     */
    public static Date getDateLastDayOfMonth(final Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

        return c.getTime();
    }

    public static String getLastWorkingDayOfMonth(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MONTH, 1);//Used for finding next month
        calendar.set(Calendar.DAY_OF_MONTH, 1);//Setting the Day of month as 1 for starting
        do {
            calendar.add(Calendar.DATE, -1); //In the first case decease day by 1 so get the this months last day
        } while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY); // Checking whether the last day is saturday or sunday then it will decrease by 1
        Date lastDayOfMonth = calendar.getTime();
        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(lastDayOfMonth);
    }

    public static Date getDateLastDayOfMonthMinusOne(final Date date) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);

        return c.getTime();
    }

    /**
     * Pega o ultimo dia do mes da data passada por parametro.
     *
     * @param date que deseja seber o ultimo dia do mes
     * @return retorna o ano
     */
    public static int getLastDayOfMonth(final Date date) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // retorna o �ltimo dia do mes da data passado por parametro
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Cria o periodo/lista de datas dado um data inicial e final da vigencia.
     *
     * @param validityMonthBeg - mes inicial da vigencia
     * @param validityYearBeg  - ano inicial da vigencia
     * @param validityMonthEnd - mes final da vigencia
     * @param validityYearEnd  - ano final da vigencia
     * @return lista de datas da vigencia
     */
    public static List<Date> getValidityDateList(final String validityMonthBeg,
                                                 final String validityYearBeg, final String validityMonthEnd,
                                                 final String validityYearEnd) {

        Date dateCurrent = getDate(validityMonthBeg, validityYearBeg);

        Date dateEnd = getDate(validityMonthEnd, validityYearEnd);

        return getValidityDateList(dateCurrent, dateEnd);
    }

    /**
     * Cria o periodo/lista de datas dado um data inicial e final da vigencia.
     *
     * @param startDate - data inicial da vigancia
     * @param endDate   - data final da vigencia
     * @return retorna uma lista com as data pertencentes a vigencia.
     */
    public static List<Date> getValidityDateList(final Date startDate,
                                                 final Date endDate) {

        List<Date> validityDateList = new ArrayList<Date>();

        Date auxDate = startDate;
        while (auxDate.compareTo(endDate) <= 0) {
            validityDateList.add(auxDate);
            auxDate = DateUtils.addMonths(auxDate, 1);
        }

        return validityDateList;
    }

    /**
     * Cria o periodo/lista de datas dado uma data inicial e quantidade de meses
     * do range.
     *
     * @param startDate   - data inicial da vigancia
     * @param rangeMonths - quantidade de meses do range
     * @return retorna uma lista com as data pertencentes a vigencia.
     */
    public static List<Date> getValidityDateList(final Date startDate,
                                                 final Integer rangeMonths) {

        List<Date> validityDateList = new ArrayList<Date>();

        Date auxDate = startDate;
        int i = 0;
        while (i++ < rangeMonths) {
            validityDateList.add(auxDate);
            auxDate = DateUtils.addMonths(auxDate, 1);
        }

        return validityDateList;
    }

    /**
     * Cria o periodo/lista de datas em String, no formato passado por
     * par�metro, dado uma data inicial e quantidade de meses do range.
     *
     * @param startDate   - data inicial da vigancia
     * @param rangeMonths - quantidade de meses do range
     * @param sdf         - formato da data
     * @return retorna uma lista com as data pertencentes a vigencia.
     */
    public static List<String> getValidityDateStringList(final Date startDate,
                                                         final Integer rangeMonths, final SimpleDateFormat sdf) {

        List<String> validityDateStringList = new ArrayList<String>();

        Date auxDate = startDate;
        int i = 0;
        while (i++ < rangeMonths) {
            validityDateStringList.add(sdf.format(auxDate));
            auxDate = DateUtils.addMonths(auxDate, 1);
        }

        return validityDateStringList;
    }

    /**
     * Carrega a lista de datas (String) com o range ClosingDate + 1 at� data
     * atual para ser exibido no combo na tela.
     *
     * @param startDate - data Inicial
     * @param sdf       - formato da data
     * @return a lista de datas (String)
     */
    public static List<String> getDateMonthListString(final Date startDate,
                                                      final SimpleDateFormat sdf) {
        Date nextMonthDate = DateUtil.getNextMonth(startDate);
        Date currentDate = DateUtil.getDate(new Date());

        // recupera a lista de datas da vigencia passada por parametro
        List<Date> dataMesListAux = DateUtil.getValidityDateList(nextMonthDate,
                currentDate);

        // formata as datas para ser exibido na tela
        List<String> dataMesList = new ArrayList<String>();
        for (Date date : dataMesListAux) {
            dataMesList.add(sdf.format(date));
        }

        return dataMesList;
    }

    /**
     * Converte a data para uma string.
     *
     * @param date - data a ser convertida
     * @param sdf  - formato da data
     * @return string contendo a data
     */
    public static String getStringDate(final Date date,
                                       final SimpleDateFormat sdf) {
        return sdf.format(date);
    }

    /**
     * Formata uma data String em Date.
     *
     * @param date - data a ser formatada
     * @param sdf  - formato da data
     * @return data formatada
     */
    public static Date getDateParse(final String date,
                                    final SimpleDateFormat sdf) {
        Date dateParse = null;
        try {
            dateParse = sdf.parse(date);
        } catch (ParseException e) {
            dateParse = null;
        }

        return dateParse;
    }

    /**
     * Retorna o proximo mes.
     *
     * @param date - data para calculo do mes
     * @return retorna a data de valida��o;
     */
    public static Date getNextMonth(final Date date) {
        return DateUtils.addMonths(date, 1);
    }

    /**
     * Cria uma data v�lida dado um m�s e ano de vig�ncia - 01/mm/aaaa 00:00:00.
     *
     * @param validityMonth - mes da vigencia. Valores v�lidos "1" � "12" (em string)
     * @param validityYear  - ano da vigencia
     * @return data v�lida
     */
    public static Date getDate(final String validityMonth,
                               final String validityYear) {
        if (StringUtils.isEmpty(validityMonth)
                || StringUtils.isEmpty(validityYear)) {
            return null;
        }

        return getDate(Integer.parseInt(validityMonth.trim()),
                Integer.parseInt(validityYear.trim()));
    }

    /**
     * Cria uma data v�lida dado um m�s e ano de vig�ncia - 01/mm/aaaa 00:00:00.
     *
     * @param validityMonth - mes da vigencia. Valores v�lidos 1 � 12
     * @param validityYear  - ano da vigencia
     * @return data v�lida
     */
    public static Date getDate(final int validityMonth, final int validityYear) {

        Calendar calendar = new GregorianCalendar();

        calendar.set(validityYear, validityMonth - 1, 1);
        Date validityDate = DateUtils.truncate(calendar.getTime(),
                Calendar.MONTH);

        return validityDate;
    }

    /**
     * Cria uma data truncada dado um m�s e ano de vig�ncia - 01/mm/aaaa
     * 00:00:00.
     *
     * @param randonDate - uma data aleatoria
     * @return data truncada
     */
    public static Date getDate(final Date randonDate) {

        Date validityDate = DateUtils.truncate(randonDate, Calendar.MONTH);

        return validityDate;
    }

    /**
     * Retorna uma data a partir da data atual subtraindo ou adicionando dias a mesma
     *
     * @param qtDays n�mero de dias a serem adicionados ou subtraidos
     * @return data
     */
    public static Date getDate(int qtDays) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, qtDays);
        return calendar.getTime();
    }

    /**
     * Cria uma data truncada dado um m�s e ano de vig�ncia - 01/mm/aaaa
     * 00:00:00, considerando range de 21/mm at� 20/mm+1:
     * <p>
     * Exemplo: 15/05/2011 - retorna 01/05/2011 25/05/2011 - retorna 01/06/2011.
     *
     * @param randonDate    - uma data aleatoria
     * @param initialDayChp - dia in�cio da vig�ncia
     * @return data truncada
     */
    public static Date getDateTruncChp(final Date randonDate,
                                       final Integer initialDayChp) {
        Integer dayOfMonth = Integer.valueOf(getDayOfMonth(randonDate));

        Date dateTrunc = DateUtils.truncate(randonDate, Calendar.MONTH);

        if (dayOfMonth.compareTo(initialDayChp) >= 0) {
            return DateUtils.addMonths(dateTrunc, 1);
        } else {
            return dateTrunc;
        }
    }

    /**
     * Realiza a valida��o da vigencia.
     *
     * @param validityMonthBeg    - mes inicial da vigencia
     * @param validityYearBeg     - ano inicial da vigencia
     * @param validityMonthEnd    - mes final da vigencia
     * @param validityYearEnd     - ano final da vigencia
     * @param fieldLabelBundleKey - bundle key do label corrente
     * @param isUpdate            - indicador modo de criacao/edicao
     * @param currentDate         - Data corrente.
     * @return retorna true se o per�odo for v�lido. Se a data corrente � menor
     * que a data inicial ou se a data inicial � maior que a data final,
     * o per�odo � inv�lido e retorna false
     */
    public static Boolean validateValidityDate(final String validityMonthBeg,
                                               final String validityYearBeg, final String validityMonthEnd,
                                               final String validityYearEnd, final String fieldLabelBundleKey,
                                               final boolean isUpdate, final Date currentDate) {

        Date dateCurrent = DateUtils.truncate(currentDate, Calendar.MONTH);

        Date dateBeg = getDate(validityMonthBeg, validityYearBeg);

        Date dateEnd = getDate(validityMonthEnd, validityYearEnd);

        // se for inser��o da entidade, verifica se data corrente � maior que
        // data inicial,
        // se sim d� mensagem de erro.
        // se for altera��o da entidade, essa valida��o n�o deve ser �
        // realizada.
        if (!isUpdate) {
            if (dateCurrent.compareTo(dateBeg) >= 0) {
                Messages.showError("validateValidityDate",
                        Constants.DEFAULT_MSG_ERROR_DATE_CURRENT_GT_DATE_BEG,
                        fieldLabelBundleKey);
                return Boolean.FALSE;
            }
        }

        // verifica se data inicial � maior que data final,
        // se sim d� mensagem de erro.
        if (dateBeg.after(dateEnd)) {
            Messages.showError("validateValidityDate",
                    Constants.DEFAULT_MSG_ERROR_DATE_BEG_GT_DATE_END,
                    fieldLabelBundleKey);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * Realiza a valida��o da vigencia / periodo.
     *
     * @param validityDateBeg   - data inicial da vigencia
     * @param validityDateEnd   - data final da vigencia
     * @param verifyCurrentDate - indicador para verificar se data inicial deve ser maior do
     *                          que data corrente
     * @return retorna true se o per�odo for v�lido. Se a data corrente � menor
     * que a data inicial ou se a data inicial � maior que a data final,
     * o per�odo � inv�lido e retorna false
     */
    public static Boolean validateValidityDate(final Date validityDateBeg,
                                               final Date validityDateEnd, final boolean verifyCurrentDate) {
        // se for para verificar data corrente, verifica se data corrente �
        // maior que data inicial, se sim d� mensagem de erro.
        if (verifyCurrentDate) {
            Date dateCurrent = DateUtils.truncate(new Date(), Calendar.MONTH);
            if (dateCurrent.after(validityDateBeg)) {
                Messages.showError("validateValidityDate",
                        Constants.DEFAULT_MSG_ERROR_DATE_CURRENT_GT_DATE_BEG);
                return Boolean.FALSE;
            }
        }

        // verifica se data inicial � maior que data final,
        // se sim d� mensagem de erro.
        if (validityDateBeg.after(validityDateEnd)) {
            Messages.showError("validateValidityDate",
                    Constants.DEFAULT_MSG_ERROR_DATE_BEG_GT_DATE_END);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * Verifica se a data 'verifyDate' esta entre o periodo das datas
     * 'startDate' e 'endDate'. (startDate >= verifyDate <= endDate).
     *
     * @param verifyDate - data a ser verificada
     * @param startDate  - data inicio
     * @param endDate    - data fim
     * @param field      - Campo utilizado para fazer o truncate. o Campo da classe
     *                   Calendar ou SEMI_MONTH.
     * @return - retorna true se a data estiver dentro do periodo, caso
     * contrario false.
     */
    public static Boolean isBetween(final Date verifyDate,
                                    final Date startDate, final Date endDate, final int field) {

        Date verifyDateTrunc = DateUtils.truncate(verifyDate, field);
        Date startDateTrunc = DateUtils.truncate(startDate, field);
        Date endDateTrunc = null;

        if (endDate != null) {
            endDateTrunc = DateUtils.truncate(endDate, field);

            if (verifyDateTrunc.compareTo(startDateTrunc) >= 0
                    && verifyDateTrunc.compareTo(endDateTrunc) <= 0) {
                return Boolean.valueOf(true);
            }
        } else {
            if (verifyDateTrunc.compareTo(startDateTrunc) >= 0) {
                return Boolean.valueOf(true);
            }
        }

        return Boolean.valueOf(false);
    }

    /**
     * Compara duas datas, se a primeira for depois da segunda, utilizando
     * truncate, determinado pelo paramtro 'field', retorna true.
     *
     * @param date1 - Data a ser comparada
     * @param date2 - Data a ser comparada
     * @param field - Campo utilizado para fazer o truncate. o Campo da classe
     *              Calendar ou SEMI_MONTH.
     * @return retorna true se primeira data for depois da segunda, caso
     * contrario false.
     */
    public static Boolean after(final Date date1, final Date date2,
                                final int field) {

        return DateUtils.truncate(date1, field).after(
                DateUtils.truncate(date2, field));

    }

    /**
     * Compara duas datas, se a primeira for depois da segunda, utilizando
     * truncate por mes.
     *
     * @param date1 - Data a ser comparada
     * @param date2 - Data a ser comparada
     * @return retorna true se primeira data for depois da segunda, caso
     * contrario false.
     */
    public static Boolean after(final Date date1, final Date date2) {

        return after(date1, date2, Calendar.MONTH);

    }

    /**
     * Compara duas datas, se a primeira for antes da segunda, utilizando
     * truncate, determinado pelo paramtro 'field', retorna true.
     *
     * @param date1 - Data a ser comparada
     * @param date2 - Data a ser comparada
     * @param field - Campo utilizado para fazer o truncate. o Campo da classe
     *              Calendar ou SEMI_MONTH.
     * @return retorna true se primeira data for antes da segunda, caso
     * contrario false.
     */
    public static Boolean before(final Date date1, final Date date2,
                                 final int field) {

        return DateUtils.truncate(date1, field).before(
                DateUtils.truncate(date2, field));

    }

    /**
     * Compara duas datas, se a primeira for antes da segunda, utilizando
     * truncate por mes, retorna true.
     *
     * @param date1 - Data a ser comparada
     * @param date2 - Data a ser comparada
     * @return retorna true se primeira data for antes da segunda, caso
     * contrario false.
     */
    public static Boolean before(final Date date1, final Date date2) {

        return before(date1, date2, Calendar.MONTH);

    }

    /**
     * Verifica se duas datas s�o iguais, utilizando o campo field como 'TRUNC'.
     *
     * @param date1 - Data 1
     * @param date2 - Data 2
     * @param field - Campo utilizado para fazer o truncate. o Campo da classe
     *              Calendar ou SEMI_MONTH.
     * @return retorna true se as duas datas forem iguais, caso contrario false.
     */
    public static boolean isSameDate(final Date date1, final Date date2,
                                     final int field) {
        return DateUtils.isSameDay(DateUtils.truncate(date1, field),
                DateUtils.truncate(date2, field));
    }

    public static boolean isSameDay(final Date date1, final Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isSameDay(String dateOne, String dateTwo) {
        return isSameDay(stringToDate(dateOne), stringToDate(dateTwo));
    }

    public static Date stringToDate(String date) {
        try {
            return new Date(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getPastYear(final Date data) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(data);
		return calendar.get(Calendar.YEAR ) - 1;
    }

    public static Date stringToDate(String date, String bundleKeyFormat, String bundleKeyLocale) {
        try {
            String format = BundleUtil.getBundle(bundleKeyFormat);
            Locale locale = new Locale(BundleUtil.getBundle(bundleKeyLocale));

            SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Retorna a maior data entre as duas datas passadas por par�metro.
     *
     * @param date1 - primeira data
     * @param date2 - segunda data
     * @return um Date - maior data
     */
    public static Date getMaxDate(final Date date1, final Date date2) {
        if (date1.compareTo(date2) >= 0) {
            return date1;
        } else {
            return date2;
        }
    }

    /**
     * Soma o numero de dias passado por paratro a data passada por parametro.
     *
     * @param numeroDias de dias, data a ser somada
     * @param data       data a ser acrescentado os dias
     * @return a data somada
     */
    public static Date sumDays(final Long numeroDias, final Date data) {
        Calendar calendar = new GregorianCalendar();

        calendar.setTime(data);

        calendar.add(Calendar.DAY_OF_MONTH, numeroDias.intValue());

        return calendar.getTime();
    }

    /**
     * Altera a data para a proxima segunda feira caso seja sabado ou domingo.
     *
     * @param data a ser modificada
     * @return a data alterada
     */
    public static Date nextWorkDay(final Date data) {
        Calendar calendar = new GregorianCalendar();

        calendar.setTime(data);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 2);
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar.getTime();
    }

    /**
     * Formata a data conforme formato e locale passado por par�metro.
     *
     * @param date            - data a ser formatada
     * @param bundleKeyFormat - bundle key do formato da data
     * @param bundleKeyLocale - bundle key do locale da data
     * @return um String com a data formatada
     */
    public static String formatDate(final Date date,
                                    final String bundleKeyFormat, final String bundleKeyLocale) {
        String format = BundleUtil.getBundle(bundleKeyFormat);
        Locale locale = new Locale(BundleUtil.getBundle(bundleKeyLocale));

        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(date);
    }

    /**
     * Converte uma data do tipo Date para uma do tipo XMLGregorianCalendar.
     *
     * @param date - data a ser conevrtida
     * @return data convertida
     */
    public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(
            final Date date) {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        XMLGregorianCalendar dataXML = null;
        try {
            dataXML = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            dataXML.setDay(gc.get(GregorianCalendar.DAY_OF_MONTH));
            dataXML.setMonth(gc.get(GregorianCalendar.MONTH) + 1);
            dataXML.setYear(gc.get(GregorianCalendar.YEAR));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataXML;

    }

    /**
     * Verifica se {@code date1} e {@code date2} s�o iguais.
     *
     * @param date1
     * @param date2
     * @return true se {@code date1} e {@code date2} s�o iguais e false se s�o diferentes.
     */
    public static Boolean equalDateMonthAndYear(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int y1 = calendar.get(Calendar.YEAR);
        int m1 = calendar.get(Calendar.MONTH);
        calendar.setTime(date2);
        int y2 = calendar.get(Calendar.YEAR);
        int m2 = calendar.get(Calendar.MONTH);

        return y1 < y2 || (y1 == y2 && m1 <= m2);
    }


    /**
     * Verifica se {@code date1} e {@code date2} s�o iguais.
     *
     * @param date1
     * @param date2
     * @return true se {@code date1} e {@code date2} s�o iguais e false se s�o diferentes.
     */
    public static Boolean equalDateMonthAndYearPrecision(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int y1 = calendar.get(Calendar.YEAR);
        int m1 = calendar.get(Calendar.MONTH);
        calendar.setTime(date2);
        int y2 = calendar.get(Calendar.YEAR);
        int m2 = calendar.get(Calendar.MONTH);

        return (y1 == y2 && m1 == m2) || Boolean.FALSE;
    }


    public static Calendar getCalendar(int day, int month, int year) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date;
    }

    public static Date addMonths(Date dataInicio, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataInicio);
        cal.add(Calendar.MONTH, i);

        return cal.getTime();
    }

    public static List<String> getMonthsList(){
		List<String> months = new ArrayList<String>();
		months.add("JANUARY");
		months.add("FEBRUARY");
		months.add("MARCH");
		months.add("APRIL");
		months.add("MAY");
		months.add("JUNE");
		months.add("JULY");
		months.add("AUGUST");
		months.add("SEPTEMBER");
		months.add("OCTOBER");
		months.add("NOVEMBER");
		months.add("DECEMBER");
		return months;
	}

	public static Date addDays(Date date, int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, i);
		return cal.getTime();
	}

    public static long getDifferenceInDays(Date date, Date toCompare) {
        long diffInMillies = Math.abs(toCompare.getTime() - date.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
