package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Parametro;
import org.apache.http.conn.ConnectTimeoutException;

public interface IIntegrateRevenueScheduledConfigService {

    Parametro findParametro();

    void update(Parametro parametro);

    void startManualIntegration() throws ConnectTimeoutException;

    void clearMegaMiddlewareCache() throws ConnectTimeoutException;

}
