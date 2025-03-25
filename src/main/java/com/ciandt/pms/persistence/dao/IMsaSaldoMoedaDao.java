package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaSaldoMoeda;

/**
 * 
 * A classe IMsaSaldoMoedaDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade MsaSaldoMoeda.
 * 
 * @since 06/10/2010
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
public interface IMsaSaldoMoedaDao extends IAbstractDao<Long, MsaSaldoMoeda> {

    /**
     * Busca todas as entidades.
     * 
     * @return retorna uma lista de MsaSaldoMoeda.
     */
    List<MsaSaldoMoeda> findAll();

    /**
     * Busca todas as entidades pelo Msa.
     * 
     * @return retorna uma lista de MsaSaldoMoeda do Msa passado por parâmetro.
     */
    List<MsaSaldoMoeda> findByMsa(final Msa msa);
 
    /**
     * Busca todas as entidades pelo Msa e ativos.
     * 
     * @return retorna uma lista de MsaSaldoMoeda do Msa passado por parâmetro.
     */
    List<MsaSaldoMoeda> findByMsaAndActive(final Msa msa);
}