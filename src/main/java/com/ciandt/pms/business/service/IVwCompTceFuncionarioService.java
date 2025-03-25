package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.VwCompTceFuncionario;


/**
 * 
 * A classe IVwCompTceFuncionarioService proporciona a interface de acesso a
 * camada de serviço referente a entidade VwCompTceFuncionario.
 * 
 * @since 10/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IVwCompTceFuncionarioService {

    /**
     * Retorna todas as entidades pela data mês.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return retorna uma lista com todas as entidades do mês.
     */
    List<VwCompTceFuncionario> findAllByDate(final Date dataMes);

}