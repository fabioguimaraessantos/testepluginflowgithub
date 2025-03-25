package com.ciandt.pms.control.jsf.interfaces.chargeback.impl;

import com.ciandt.pms.control.jsf.pojo.LoginChargebackPojo;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsefa.common.lowlevel.filter.HeaderAndFooterFilter;
import org.jsefa.csv.CsvDeserializer;
import org.jsefa.csv.CsvIOFactory;
import org.jsefa.csv.config.CsvConfiguration;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvChargebackWorksheet extends ChargebackWorksheet{

    /* Logger */
    private static Logger logger = LogManager.getLogger(CsvChargebackWorksheet.class.getName());

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
            Reader reader = new InputStreamReader(new ByteArrayInputStream(data));
            CsvConfiguration config = new CsvConfiguration();
            config.setLineFilter(new HeaderAndFooterFilter(0, false, true));

            CsvDeserializer deserializer = CsvIOFactory.createFactory(config, LoginChargebackPojo.class).createDeserializer();
            deserializer.open(reader);

            while (deserializer.hasNext()) {
                LoginChargebackPojo login = deserializer.next();

                if(login != null && !StringUtils.isEmpty(login.getLogin()))
                    logins.add(login.getLogin());
            }

            deserializer.close(true);
        }catch (Exception e){
            logger.error("Error trying to read logins from worksheet.", e);
        }

        return logins;
    }
}
