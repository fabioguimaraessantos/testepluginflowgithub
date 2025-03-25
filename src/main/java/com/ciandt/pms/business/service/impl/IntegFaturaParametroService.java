/**
 * Classe IntegFaturaParametroService - Implementação do serviço da IntegFaturaParametro
 */
package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IIntegFaturaParametroService;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.model.IntegFaturaParametro;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.persistence.dao.IIntegFaturaParametroDao;


/**
 * A classe IntegFaturaParametroService proporciona as funcionalidades de
 * serviço para a entidade IntegFaturaParametro.
 * 
 * @since 22/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class IntegFaturaParametroService implements
        IIntegFaturaParametroService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IIntegFaturaParametroDao integFaturaParametroDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @throws IntegrityConstraintException
     *             - ao tentar criar, se já existir um registro com a mesma
     *             Empresa, o mesmo TipoServico, a mesma FonteReceita
     */
    public void createIntegFaturaParametro(final IntegFaturaParametro entity)
            throws IntegrityConstraintException {
        
        IntegFaturaParametro param = findIntFatByTpServicoFtReceitaAndEmpresa(
                entity.getTipoServico(), entity.getFonteReceita(), entity.getEmpresa());
        
        // verifica se já existe IntegFaturaParametro criado, se sim lança exceção
        if (param != null) {
            throw new IntegrityConstraintException(
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS);
        }

        integFaturaParametroDao.create(entity);
    }

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     * 
     * @throws IntegrityConstraintException
     *             - ao tentar atualizar, se já existir um registro com a mesma
     *             Empresa, o mesmo TipoServico, a mesma FonteReceita
     */
    public void updateIntegFaturaParametro(final IntegFaturaParametro entity)
            throws IntegrityConstraintException {

        IntegFaturaParametro param = findIntFatByTpServicoFtReceitaAndEmpresa(
                entity.getTipoServico(), entity.getFonteReceita(), entity.getEmpresa());
        // verifica se já existe
        if (param.getCodigoIntegFaturaParam().compareTo(
                entity.getCodigoIntegFaturaParam()) != 0) {
            throw new IntegrityConstraintException(
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS);
        }

        integFaturaParametroDao.update(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     * 
     */
    public void removeIntegFaturaParametro(final IntegFaturaParametro entity) {
        integFaturaParametroDao.remove(entity);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public IntegFaturaParametro findIntFatParamById(final Long entityId) {
        return integFaturaParametroDao.findById(entityId);
    }

    /**
     * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente
     * as entidades relacionadas a esta.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<IntegFaturaParametro> findIntFatParamByFilter(
            final IntegFaturaParametro filter) {
        return integFaturaParametroDao.findByFilter(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<IntegFaturaParametro> findIntFatParamAll() {
        return integFaturaParametroDao.findAll();
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param tipoServico
     *            entidade do do tipo TipoServico
     * @param fonteReceita
     *          entidade do tipo FonteReceita
     * @param empresa 
     *          entidade do tipo Empresa                     
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public IntegFaturaParametro findIntFatByTpServicoFtReceitaAndEmpresa(
            final TipoServico tipoServico, final FonteReceita fonteReceita, 
            final Empresa empresa) {
        
        return integFaturaParametroDao.findByTpServicoFtReceitaAndEmpresa(
                tipoServico, fonteReceita, empresa);
    }

}