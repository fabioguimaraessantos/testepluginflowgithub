package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.ItemFatura;


/**
 * 
 * A classe IComissaoFaturaService proporciona a interface de acesso para a camada de
 * serviço referente as comissões.
 * 
 * @since 07/04/2011
 * @author cmantovani
 * 
 */
@Service
public interface IComissaoFaturaService {

    /**
     * Cria o uma comissao de acelerador.
     * 
     * @param itemFatura
     *            - entidade ItemFatura.
     * 
     * @return retorna true se criado com sucesso, caso contrario retorna false.
     */
    @Transactional
    List<ComissaoFatura> findByItemFatura(final ItemFatura itemFatura);

    List<ComissaoFatura> findByCodigoItemFatura(final Long codigoItemFatura);
}