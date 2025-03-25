package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.audit.listener.AuditoriaJpaListener;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.TaxaImpostoNotFoundException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IReceitaDao;
import com.ciandt.pms.persistence.dao.IReceitaDealFiscalDao;
import com.ciandt.pms.persistence.dao.IReceitaIntegracaoSemaforoDao;
import com.ciandt.pms.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class ReceitaIntegracaoSemaforoService implements IReceitaIntegracaoSemaforoService {

    @Autowired
    private IReceitaIntegracaoSemaforoDao dao;

    public void createReceitaIntegracaoSemaforo(final ReceitaIntegracaoSemaforo entity) {
        dao.create(entity);
    }

    public List<ReceitaIntegracaoSemaforo> findByReceitaDealFiscal(final Long codigoReceitaDealFiscal) {
        return dao.findByReceitaDealFiscal(codigoReceitaDealFiscal);
    }
}
