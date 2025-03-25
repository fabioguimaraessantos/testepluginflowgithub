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
import com.ciandt.pms.model.Aliquota;
import com.ciandt.pms.model.Imposto;


/**
 * Define o BackingBean da entidade TabelaPreco.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AliquotaBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** to do backingBean. */
    private Aliquota to = new Aliquota();

    /** Lista de resultados. */
    private List<Aliquota> resultList = new ArrayList<Aliquota>();

    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;

    /** Lista dos possiveis valores de anos. */
    private List<String> yearList = new ArrayList<String>();

    /** Propriedade do mes de inicio da vigencia. */
    private String mesInicioVigencia;

    /** Propriedade do ano de inicio da vigencia. */
    private String anoInicioVigencia;

    /**
     * @return the to
     */
    public Aliquota getTo() {
        // Verifica se o Imposto é nulo, se verdade instancia um novo.
        if (to != null && to.getImposto() == null) {
            to.setImposto(new Imposto());
        }
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final Aliquota to) {
        this.to = to;
    }

    /**
     * @return the resultList
     */
    public List<Aliquota> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<Aliquota> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the monthList
     */
    public List<String> getMonthList() {
        return monthList;
    }

    /**
     * @return the yearList
     */
    public List<String> getYearList() {

        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> localYearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            localYearList.add("" + i);
        }

        this.yearList = localYearList;

        return this.yearList;
    }

    /**
     * @param yearList
     *            the yearList to set
     */
    public void setYearList(final List<String> yearList) {
        this.yearList = yearList;
    }

    /**
     * @return the mesInicioVigencia
     */
    public String getMesInicioVigencia() {
        return mesInicioVigencia;
    }

    /**
     * @param mesInicioVigencia
     *            the mesInicioVigencia to set
     */
    public void setMesInicioVigencia(final String mesInicioVigencia) {
        this.mesInicioVigencia = mesInicioVigencia;
    }

    /**
     * @return the anoInicioVigencia
     */
    public String getAnoInicioVigencia() {
        return anoInicioVigencia;
    }

    /**
     * @param anoInicioVigencia
     *            the anoInicioVigencia to set
     */
    public void setAnoInicioVigencia(final String anoInicioVigencia) {
        this.anoInicioVigencia = anoInicioVigencia;
    }

    /**
     * Reste o to do bean.
     */
    public void resetTo() {
        this.mesInicioVigencia = "";
        this.anoInicioVigencia = "";
        this.to = new Aliquota();
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        this.resetTo();
    }

}
