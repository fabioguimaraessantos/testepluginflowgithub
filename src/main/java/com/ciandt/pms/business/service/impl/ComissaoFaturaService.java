package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IComissaoFaturaService;
import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.persistence.dao.IComissaoFaturaDao;


/**
 * 
 * A classe ComissaoService proporciona as funcionalidades da camada de servico
 * referente as comissões.
 * 
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ComissaoFaturaService implements IComissaoFaturaService {

    /** Instancia do dao ComissaoFaturaDao. */
    @Autowired
    private IComissaoFaturaDao comissaoFaturaDao;

    /**
     * Cria o uma comissao de acelerador.
     * 
     * @param itemFatura
     *            - entidade ItemFatura
     * 
     * @return retorna true se criado com sucesso, caso contrario retorna false.
     */
    public List<ComissaoFatura> findByItemFatura(final ItemFatura itemFatura) {

        List<ComissaoFatura> comissoesFaturas = comissaoFaturaDao
                .findByItemFatura(itemFatura);

        return comissoesFaturas;

    }

    public List<ComissaoFatura> findByCodigoItemFatura(final Long codigoItemFatura) {
    	
		List<ComissaoFatura> comissoesFaturas = comissaoFaturaDao
				.findByCodigoItemFatura(codigoItemFatura);
    	
    	return comissoesFaturas;
    	
    }
}