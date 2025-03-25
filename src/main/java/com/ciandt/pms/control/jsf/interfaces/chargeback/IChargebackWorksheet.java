package com.ciandt.pms.control.jsf.interfaces.chargeback;

import com.ciandt.pms.control.jsf.pojo.LoginChargebackPojo;

import java.io.OutputStream;
import java.util.List;

public interface IChargebackWorksheet {

    /**
     *
     * @param data
     * @return
     */
    List<LoginChargebackPojo> readLoginsFromWorksheet(byte[] data);

    /**
     *
     * @param logins
     * @param output
     */
    void createErrorLoginsWorksheet(List<LoginChargebackPojo> logins, OutputStream output);
}