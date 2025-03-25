package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.util.DateUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.*;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntegrateRevenueScheduledConfigBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> scheduledIntegrationDates = new ArrayList<>();
    private Integer hour;
    private Integer minute;
    private Date date;
    private String dateToDelete;
    private Parametro parametro;

    public void reset() {
        this.scheduledIntegrationDates = new ArrayList<>();
        this.date = null;
        this.hour = null;
        this.minute = null;
        this.dateToDelete = null;
    }

    public void addIntegrationDate() {
        if (inputValid()) {
            String dateToAdd = buildDateString();
            if (!dateAlreadyInList(dateToAdd)) {
                this.scheduledIntegrationDates.add(dateToAdd);
            } else {
                Messages.showError("IntegrateRevenueScheduledConfigBean.addIntegrationDate()", "_nls.receita_agendamento_configuracao_integracao_duplicated_dates");
            }
            this.date = null;
            this.hour = null;
            this.minute = null;
            this.sortDates();
        } else {
            Messages.showError("IntegrateRevenueScheduledConfigBean.addIntegrationDate()", "_nls.receita_agendamento_configuracao_integracao_invalid_input");
        }
    }

    public boolean inputValid() {
        return Objects.nonNull(this.date) && Objects.nonNull(this.hour) && Objects.nonNull(this.minute);
    }

    public String buildDateString() {
        Calendar calendar = Calendar.getInstance(Locale.forLanguageTag("PT"));
        calendar.setTime(this.date);
        calendar.set(Calendar.HOUR, this.hour);
        calendar.set(Calendar.MINUTE, this.minute);
        return DateUtil.formatDate(new Date(calendar.getTimeInMillis()), Constants.DEFAULT_DATE_PATTERN_FULL, Constants.DEFAULT_CALENDAR_LOCALE);
    }

    public boolean dateAlreadyInList(String date) {
        for (int i = 0; i < this.scheduledIntegrationDates.size(); i++) {
            if (DateUtil.isSameDay(date, this.scheduledIntegrationDates.get(i))) {
                Messages.showError("IntegrateRevenueScheduledConfigBean.dateAlreadyInList()", "_nls.receita_agendamento_configuracao_integracao_date_already_in_list");
                return true;
            }
        }
        return false;
    }

    public void sortDates() {
        Collections.sort(this.scheduledIntegrationDates, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Date d1 = DateUtil.stringToDate(o1, Constants.DEFAULT_DATE_PATTERN_FULL, Constants.DEFAULT_CALENDAR_LOCALE);
                Date d2 = DateUtil.stringToDate(o2, Constants.DEFAULT_DATE_PATTERN_FULL, Constants.DEFAULT_CALENDAR_LOCALE);
                return d1.compareTo(d2);
            }
        });
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateToDelete() {
        return dateToDelete;
    }

    public void setDateToDelete(String dateToDelete) {
        this.dateToDelete = dateToDelete;
    }

    public List<String> getScheduledIntegrationDates() {
        return scheduledIntegrationDates;
    }

    public void setScheduledIntegrationDates(List<String> scheduledIntegrationDates) {
        this.scheduledIntegrationDates = scheduledIntegrationDates;
    }

    public void addScheduledIntegrationDates(String scheduledIntegrationDates) {
        this.scheduledIntegrationDates.add(scheduledIntegrationDates);
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }
}
