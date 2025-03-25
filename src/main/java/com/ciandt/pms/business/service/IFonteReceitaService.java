package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.FonteReceita;


/**
 * 
 * A classe IFonteReceitaService proporciona a interface
 * de acesso a camada de serviço, referente a entidade FonteReceita.
 * 
 *
 * @since 03/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IFonteReceitaService {

    /**
     * Busca por todos os ContratoPraticas.
     * 
     * @return uma lista com todos os ContratoPraticas.
     */
    List<FonteReceita> findFonteReceitaAll();
    
    /**
     * Busca a entidade pelo id.
     *
     * @param id - Id da entidade.
     * 
     * @return a entidade com o id correspondente, ou null caso não exista.
     */
    FonteReceita findFonteReceitaById(final Long id);
}
