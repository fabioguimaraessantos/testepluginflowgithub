package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.CustoPessoaMes;
import com.ciandt.pms.model.Pessoa;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 30/08/2010
 */
@Service
public interface ICustoPessoaMesService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     */
    @Transactional
    void createCustoPessoaMes(final CustoPessoaMes entity);
    
    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    @Transactional
    void updateCustoPessoaMes(final CustoPessoaMes entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeCustoPessoaMes(final CustoPessoaMes entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CustoPessoaMes findCustoPessoaMesById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoPessoaMes> findCustoPessoaMesAll();

    /**
     * Busca uma lista de entidades pela Pessoa e dataMes.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<CustoPessoaMes> findCustPessMesByPessoaAndDataMes(final Pessoa pessoa,
            final Date dataMes);
    
    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<CustoPessoaMes> findCustPessMesByDataMes(final Date dataMes);
    
    /**
     * Faz a apropriacao do Plantao e das Horas Extras.
     * 
     * @param dataMes - mes da apropriacao
     * @return true se apropriou corretamente, caso contrario false
     */
    @Transactional
    @Deprecated
    Boolean registerPLAndHECosts(final Date dataMes);
    
    /**
     * FIXME: **** ATENÇÃO: **** 
     * 
	 * Faz a apropriacao do Plantao e das Horas Extras por Valor. Método criado
	 * somente para fechamento do antigo Closing DRE com a nova apropriacao de
	 * PL e HE por valor.
	 * 
	 * @param dataMes
	 *            - mes da apropriacao
	 * @return true se apropriou corretamente, caso contrario false
	 */
    @Transactional
    @Deprecated
    Boolean registerPLAndHEByValue(final Date dataMes);

}