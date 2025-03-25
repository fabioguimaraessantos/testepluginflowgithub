package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.Constants;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IPessoaCidadeBaseService;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaCidadeBase;
import com.ciandt.pms.persistence.dao.ICidadeBaseDao;


/**
 *
 * A classe CidadeBaseService proporciona as funcionalidades de seeviço para a
 * entidade CidadeBase.
 *
 * @since 17/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class CidadeBaseService implements ICidadeBaseService {

    private static final String EXCEPTION_NOT_FOUND = "City Base not found.";

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private ICidadeBaseDao dao;

    /**
     * Instancia do Serviço da entidade IPessoaCidadeBase.
     */
    @Autowired
    private IPessoaCidadeBaseService pessoaCidadeBaseService;

    /**
     * Busca pelo id da entidade.
     *
     * @param entityId
     *            id da entidade
     *
     * @return retorna a entidade
     */
    public CidadeBase findCidadeBaseById(final Long entityId) {
        return dao.findById(entityId);
    }

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    public List<CidadeBase> findCidadeBaseAll() {
        return dao.findAll();
    }

    /**
     * Retorna todas as entidades ativas.
     *
     * @return retorna uma lista com todas as entidades.
     */
    public List<CidadeBase> findCidadeBaseAllActive() {
        return dao.findAllActive();
    }

    /**
     * Retorna entidade por sigla.
     *
     * @param sigla
     *            sigla.
     * @return entidade.
     */
    public CidadeBase findCidadeBaseBySigla(final String sigla) {
        return dao.findBySigla(sigla);
    }

    public CidadeBase findCidadeBaseByNome(final String nome) {
        return dao.findByNome(nome);
    }

    /**
     * Busca a {@link CidadeBase} atual de {@code pessoa}.
     *
     * @param pessoa
     * @return {@link CidadeBase}
     */
    public CidadeBase findCurrentCidadeBaseByPessoa(final Pessoa pessoa) {
        PessoaCidadeBase pessoaCidadeBase = pessoaCidadeBaseService.findPessCBByPessoaAndDate(pessoa, new Date());

        if (pessoaCidadeBase != null) {
            return pessoaCidadeBase.getCidadeBase();
        }

        return null;
    }

    /**
     * Realiza consulta na tabela cidade_base_filial
     *
     * @param cidadeBaseCode
     *            id da entidade ReceitaDealFiscal.
     *
     *
     * @return código da empresa vinculada a cidade base do colaborador
     */
    public Long findEmpresaByCidadeBase(final Long cidadeBaseCode){
        return dao.findEmpresaByCidadeBase(cidadeBaseCode);
    }

    public List<CidadeBase> findActiveByEmpresa(final Long codigoEmpresa) {
        return dao.findActiveByEmpresa(codigoEmpresa);
    }

    public List<CidadeBase> findActiveByPmsEmpresa(final Long codigoPmsEmpresa) {
        return dao.findActiveByPmsEmpresa(codigoPmsEmpresa);
    }

    public void createCidadeBase(final CidadeBase entity) throws IntegrityConstraintException {
        // verifica se j? existe Cidade Base cadastrada com a sigla digitado,
        // ele deve ser ?nico
        CidadeBase siglaCidadeBaseAux = this.findCidadeBaseBySigla(entity.getSiglaCidadeBase());
        if (siglaCidadeBaseAux != null) {
            throw new IntegrityConstraintException(
                    Constants.MSG_ERROR_ALREADY_EXISTS_CIDADE_BASE_ACRONYM);
        }

        // verifica se j? existe Cidade Base cadastrada com o nome digitado,
        // ele deve ser ?nico
        CidadeBase nomeCidadeBaseAux = this.findCidadeBaseByNome(entity.getNomeCidadeBase());
        if (nomeCidadeBaseAux != null) {
            throw new IntegrityConstraintException(
                    Constants.MSG_ERROR_ALREADY_EXISTS_CIDADE_BASE_NAME);
        }

        dao.create(entity);
    }

    public void removeCidadeBase(final CidadeBase entity) throws IntegrityConstraintException {

        if (this.findIfHasDependency(entity.getCodigoCidadeBase())) {
            throw new IntegrityConstraintException(
                    Constants.MSG_ERROR_CIDADE_BASE_HAS_DEPENDENCY);
        }

        dao.remove(entity);
    }

    public void updateCidadeBaseIndicadorAtivo(final CidadeBase entity) throws IntegrityConstraintException {
        CidadeBase cidadeBase = this.findCidadeBaseById(entity.getCodigoCidadeBase());

        if (cidadeBase == null) {
            throw new IntegrityConstraintException(
                    Constants.MSG_ERROR_NOT_EXISTS_CIDADE_BASE);
        } else if (findIfHasDependency(entity.getCodigoCidadeBase())) {
            throw new IntegrityConstraintException(
                    Constants.MSG_ERROR_CIDADE_BASE_HAS_DEPENDENCY);
        } else {
            cidadeBase.setIndicadorAtivo(entity.getIndicadorAtivo());
        }

        dao.update(cidadeBase);
    }

    public List<CidadeBase> findByFilter(final CidadeBase filter) throws BusinessException {
        List<CidadeBase> results = dao.findByFilter(filter);

        if (results == null || results.isEmpty()) {
            throw new BusinessException(EXCEPTION_NOT_FOUND);
        }

        return results;
    }

    public Boolean findIfHasDependency(final Long codeCidadeBase) {
        return dao.findIfHasDependency(codeCidadeBase);
    }

    public void updateCidadeBase(final CidadeBase entity) throws IntegrityConstraintException {
        List<CidadeBase> cidadeBaseList = new ArrayList<>();

        CidadeBase cidadeBaseBySigla = this.findCidadeBaseBySigla(entity.getSiglaCidadeBase());
        CidadeBase cidadeBaseByNome = this.findCidadeBaseByNome(entity.getNomeCidadeBase());

        if (cidadeBaseBySigla != null) cidadeBaseList.add(cidadeBaseBySigla);
        if (cidadeBaseByNome != null) cidadeBaseList.add(cidadeBaseByNome);

        for (CidadeBase cidadeBase : cidadeBaseList) {
            if (!cidadeBase.getCodigoCidadeBase().equals(entity.getCodigoCidadeBase())) {
                throw new IntegrityConstraintException(
                        Constants.MSG_ERROR_UPDATE_CIDADE_BASE);
            }
            // Verifica se est? sendo alterado o status da Cidade Base
            // e se o status anterior era INACTIVE.
            if (!cidadeBase.getIndicadorAtivo().equals(entity.getIndicadorAtivo()) &&
                    entity.getIndicadorAtivo().equals(Constants.INACTIVE)) {
                // Busca depend?ncias da Cidade Base existente. Se h? depend?ncias, n?o permite atualizar.
                if (this.findIfHasDependency(entity.getCodigoCidadeBase())) {
                    throw new IntegrityConstraintException(Constants.MSG_ERROR_CIDADE_BASE_HAS_DEPENDENCY);
                }
            }
        }

        dao.update(entity);
    }
}