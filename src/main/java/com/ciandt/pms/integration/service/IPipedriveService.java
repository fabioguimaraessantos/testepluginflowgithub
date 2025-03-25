package com.ciandt.pms.integration.service;

import com.ciandt.pms.integration.vo.OrganizacaoPipedrive;
import com.ciandt.pms.model.VwPmsReceitaResultado;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @since 15/01/2018
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Service
public interface IPipedriveService {
    List<OrganizacaoPipedrive> findAllOrganizations();

}