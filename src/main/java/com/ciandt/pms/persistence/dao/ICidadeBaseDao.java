package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.CidadeBase;


/**
 *
 * A classe ICidadeBase proporciona a interface para acesso ao DAO da entidade
 * CidadeBase.
 *
 * @since 17/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface ICidadeBaseDao extends IAbstractDao<Long, CidadeBase> {

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    List<CidadeBase> findAll();

    /**
     * Retorna todas as entidades ativas.
     *
     * @return retorna uma lista com todas as entidades.
     */
    List<CidadeBase> findAllActive();

    /**
     * Retorna entidade por sigla.
     *
     * @param sigla
     *            sigla.
     * @return entidade.
     */
    CidadeBase findBySigla(final String sigla);

    /**
     * Realiza consulta na tabela cidade_base_filial
     *
     * @param cidadeBaseCode
     *            id da entidade ReceitaDealFiscal.
     *
     *
     * @return código da empresa vinculada a cidade base do colaborador
     */
    Long findEmpresaByCidadeBase(final Long cidadeBaseCode);

    List<CidadeBase> findActiveByEmpresa(final Long codigoEmpresa);

    List<CidadeBase> findActiveByPmsEmpresa(final Long codigoPmsEmpresa);

    List<CidadeBase> findByIds(final List<Long> ids);

    CidadeBase findByNome(final String nome);

    List<CidadeBase> findByFilter(final CidadeBase filter);

    Boolean findIfHasDependency(final Long cidadeBaseCode);
}
