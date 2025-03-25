package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.VwChbackSoftware;


/**
 * 
 * A classe IVwChbackSoftwareService proporciona a interface de acesso a camada
 * de servi�o referente a entidade VwChbackSoftware.
 * 
 * @since 13/07/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IVwChbackSoftwareService {

    /**
     * Retorna todas as entidades pela data m�s.
     * 
     * @param dataMes
     *            - data m�s
     * 
     * @return retorna uma lista com todas as entidades do m�s.
     */
    List<VwChbackSoftware> findAllByDate(final Date dataMes);

}