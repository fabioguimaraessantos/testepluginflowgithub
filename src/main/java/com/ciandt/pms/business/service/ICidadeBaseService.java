package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.Pessoa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * A classe ICidadeBaseService proporciona a interface para o servico referente
 * a entidade CidadeBase.
 *
 * @since 17/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface ICidadeBaseService {

    /**
     * Busca pelo id da entidade.
     *
     * @param entityId
     *            id da entidade
     *
     * @return retorna a entidade
     */
    CidadeBase findCidadeBaseById(final Long entityId);

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    List<CidadeBase> findCidadeBaseAll();

    /**
     * Retorna todas as entidades ativas.
     *
     * @return retorna uma lista com todas as entidades.
     */
    List<CidadeBase> findCidadeBaseAllActive();

    /**
     * Retorna entidade por sigla.
     *
     * @param sigla
     *            sigla.
     * @return entidade.
     */
    CidadeBase findCidadeBaseBySigla(final String sigla);

    /**
     * Busca a {@link CidadeBase} atual de {@code pessoa}.
     *
     * @param pessoa
     * @return {@link CidadeBase}
     */
    public CidadeBase findCurrentCidadeBaseByPessoa(final Pessoa pessoa);

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

    public List<CidadeBase> findActiveByEmpresa(final Long codigoEmpresa);

    public List<CidadeBase> findActiveByPmsEmpresa(final Long codigoPmsEmpresa);

    @Transactional
    void createCidadeBase(final CidadeBase entity) throws IntegrityConstraintException;

    @Transactional
    void removeCidadeBase(final CidadeBase entity) throws IntegrityConstraintException;

    @Transactional
    void updateCidadeBaseIndicadorAtivo(final CidadeBase entity) throws IntegrityConstraintException;

    public List<CidadeBase> findByFilter (final CidadeBase filter) throws BusinessException;

    @Transactional
    void updateCidadeBase (final CidadeBase entity) throws IntegrityConstraintException;

    public Boolean findIfHasDependency(final Long code);
}
