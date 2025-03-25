package com.ciandt.pms.control.jsf.interfaces.chargeback;

import com.ciandt.pms.control.jsf.interfaces.chargeback.impl.CsvChargebackWorksheet;
import com.ciandt.pms.control.jsf.interfaces.chargeback.impl.XlsChargebackWorksheet;
import com.ciandt.pms.control.jsf.interfaces.chargeback.impl.XlsxChargebackWorksheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChargebackWorksheetHandler {

    private final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
    private final String CSV_CONTENT_TYPE = "text/csv";

    @Autowired
    private XlsChargebackWorksheet xls;

    @Autowired
    private XlsxChargebackWorksheet xlsx;

    @Autowired
    private CsvChargebackWorksheet csv;

    /**
     *
     * @param contentType
     * @return
     */
    public IChargebackWorksheet findChargebackWorksheet(String contentType){

        if(contentType.equals(XLS_CONTENT_TYPE))
            return xls;

        if(contentType.equals(CSV_CONTENT_TYPE))
            return csv;

        return xlsx;
    }

    /**
     *
     * @param contentType
     * @return
     */
    public String findExtension(String contentType){

        if(contentType.equals(XLS_CONTENT_TYPE))
            return "XLS";

        if(contentType.equals(CSV_CONTENT_TYPE))
            return "CSV";

        return "XLSX";
    }
}
