package com.ciandt.pms.control.jsf.interfaces.chargeback.impl;

import com.ciandt.pms.control.jsf.pojo.LoginChargebackPojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class XlsxChargebackWorksheet extends ChargebackWorksheet{

    /* Logger */
    private static Logger logger = LogManager.getLogger(XlsxChargebackWorksheet.class.getName());

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
            XSSFWorkbook workbook = new XSSFWorkbook (is);

            XSSFSheet sheet = workbook.getSheetAt(0);
            readFromWorksheet(sheet.iterator(), logins);

        }catch (Exception e){
            logger.error("Error trying to read logins from worksheet.", e);
        }

        return logins;
    }
}
