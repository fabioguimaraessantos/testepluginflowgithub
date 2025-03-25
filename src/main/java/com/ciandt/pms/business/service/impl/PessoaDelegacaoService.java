package com.ciandt.pms.business.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IPessoaDelegacaoService;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaDelegacao;
import com.ciandt.pms.persistence.dao.IPessoaDelegacaoDao;


/**
 * 
 * A classe PessoaCidadeBaseService proporciona a funcionalidade da camada de
 * serviço referente a entidade PessoaCidadeBase.
 * 
 * @since 07/07/2011
 * @author cmantovani
 * 
 */
@Service
public class PessoaDelegacaoService implements IPessoaDelegacaoService {

    /** instancia do dao da entidade PessoaCidadeBase. */
    @Autowired
    private IPessoaDelegacaoDao dao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    public Boolean createPessoaDelegacao(final PessoaDelegacao entity) {
        dao.create(entity);
        return Boolean.TRUE;
    }

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updatePessoaDelegacao(final PessoaDelegacao entity) {
        dao.update(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    public void removePessoaDelegacao(final PessoaDelegacao entity) {
        dao.remove(entity);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param id
     *            - id da entidade
     * 
     * @return retorna a entidade
     */
    public PessoaDelegacao findPessoaDelegacaoById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca os delegacoes pelo filtro no periodo.
     * 
     * @param pessoa
     *            - pessoa a ser pesquisada
     * @param data
     *            - data de pesquisa
     * @return pessoaDelegacao
     */
    public PessoaDelegacao findPessoaDelegacaoByPessoaAndDate(
            final Pessoa pessoa, final Date data) {
        return dao.findByPessoaAndDate(pessoa, data);
    }
    
    /**
     * Busca as delegacoes nao expiradas.
     * 
     * @param pessoa
     *            - pessoa a ser pesquisada
     * @param data
     *            - data de pesquisa
     * @return pessoaDelegacao
     */
    public PessoaDelegacao findPessoaDelegacaoByPessoaAndFinalDate(
            final Pessoa pessoa, final Date data) {
        return dao.findByPessoaAndFinalDate(pessoa, data);
    }

}