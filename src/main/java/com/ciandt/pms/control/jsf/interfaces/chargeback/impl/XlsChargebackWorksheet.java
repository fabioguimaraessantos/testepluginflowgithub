package com.ciandt.pms.control.jsf.interfaces.chargeback.impl;

import com.ciandt.pms.control.jsf.pojo.LoginChargebackPojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class XlsChargebackWorksheet extends ChargebackWorksheet{

    /* Logger */
    private static Logger logger = LogManager.getLogger(XlsChargebackWorksheet.class.getName());

    @Override
    public List<LoginChargebackPojo> readLoginsFromWorksheet(byte[] data) {
        return createListLoginsChargeback(getLoginsFromData(data));
    }

    /**
     *
     * @param data
     * @return
     */
    private List<String> getLoginsFromData(byte[] data){

        List<String> logins = new ArrayList<String>();

        try{
            InputStream is = new ByteArrayInputStream(data);
            HSSFWorkbook workbook = new HSSFWorkbook (is);

            HSSFSheet sheet = workbook.getSheetAt(0);
            readFromWorksheet(sheet.iterator(), logins);

        }catch (Exception e){
            logger.error("Error trying to read logins from worksheet.", e);
        }

        return logins;
    }
}
