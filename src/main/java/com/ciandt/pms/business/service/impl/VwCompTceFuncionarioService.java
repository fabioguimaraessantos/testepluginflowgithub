package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwCompTceFuncionarioService;
import com.ciandt.pms.model.VwCompTceFuncionario;
import com.ciandt.pms.persistence.dao.IVwCompTceFuncionarioDao;


/**
 * 
 * A classe VwCompTceFuncionarioService proporciona as funcionalidades da camada
 * de serviço referente a entidade VwCompTceFuncionario.
 * 
 * @since 10/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class VwCompTceFuncionarioService implements
        IVwCompTceFuncionarioService {

    /** Instancia DAO da entidade VwCompTceFuncionario. */
    @Autowired
    private IVwCompTceFuncionarioDao compTceFuncionarioDao;

    /**
     * Retorna todas as entidades pela data mês.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return retorna uma lista com todas as entidades do mês.
     */
    public List<VwCompTceFuncionario> findAllByDate(final Date dataMes) {
        return compTceFuncionarioDao.findAllByDate(dataMes);
    }

}