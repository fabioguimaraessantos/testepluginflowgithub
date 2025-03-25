package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;


/**
 * 
 * A classe DateBean proporciona as funcionalidades de backbean referente ao
 * combo de datas.
 * 
 * @since 29/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class DateBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * @return lista de anos.
     */
    public List<String> getYearList() {

        int yearBegin =
                Integer
                        .parseInt(systemProperties
                                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range =
                Integer
                        .parseInt(systemProperties
                                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yearList.add("" + i);
        }

        return yearList;
    }

    /**
     * @return lista de meses.
     */
    public List<String> getMonthList() {
        return Constants.MONTH_DAY_LIST;
    }
}
