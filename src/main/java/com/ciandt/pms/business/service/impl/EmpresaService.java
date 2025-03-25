/**
 * Classe EmpresaService - Implementação do serviço do Empresa
 */
package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.persistence.dao.IEmpresaDao;


/**
 * A classe EmpresaService proporciona as funcionalidades de serviço para a
 * entidade Empresa.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class EmpresaService implements IEmpresaService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IEmpresaDao empresaDao;

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public Empresa findEmpresaById(final Long entityId) {
        return empresaDao.findById(entityId);
    }

    /**
     * Find by name
     *
     * @param name
     *
     * @return return the entity
     */
    public Empresa findEmpresaByName(final String name) {
        return empresaDao.findByName(name);
    }

    public Empresa findEmpresaByERP(final Long erpId) {
        return empresaDao.findEmpresaByERP(erpId);
    }


    /**
     * Busca uma lista de Empresa filiais.
     * 
     * @return lista de Empresa filiais.
     */
    public List<Empresa> findEmpresaAllSubsidiary() {
        return empresaDao.findAllSubsidiary();
    }
    
    /**
     * Busca uma lista de Empresa matriz.
     * 
     * @return lista de Empresa matriz.
     */
    public List<Empresa> findEmpresaAllParentCompany() {
        return empresaDao.findAllParentCompany();
    }
    
    /**
     * Busca uma de todas as Empresa .
     * 
     * @return lista de Empresas.
     */
    public List<Empresa> findEmpresaAll() {
        return empresaDao.findAll();
    }

    public List<Empresa> findAllActiveForCombobox() {
        return empresaDao.findAllActiveForCombobox();
    }
    

    /**
     * Insere uma entidade.
     * @param entity a ser inserida
     * 
     * @throws IntegrityConstraintException
     *             lança uma exceção caso esta empresa seja pai e tenha empresa
     *             pai atribuida ou caso a empresa seja pai de outras empresas e
     *             tente inativá-lo ou cadastre um mnenonico que já exista
     */
    public void createEmpresa(final Empresa entity)
            throws IntegrityConstraintException {
        // verifica se já existe Empresa cadastrada com o mnemonico digitado,
        // ele deve ser único
        Empresa empAux = this.findEmpresaByAcronym(entity.getCodigoMnemonico());
        if (empAux != null) {
            throw new IntegrityConstraintException(
                    Constants.MSG_ERROR_ALREADY_EXISTS_ACRONYM);
        }

        empresaDao.create(entity);
    }
    
    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * @throws IntegrityConstraintException
     *             lança uma exceção caso esta empresa seja pai e tenha empresa
     *             pai atribuida ou caso a empresa seja pai de outras empresas e
     *             tente inativá-lo ou cadastre um mnenonico que já exista
     */
    public void updateEmpresa(final Empresa entity)
            throws IntegrityConstraintException {
        // verifica se já existe Empresa cadastrada com o mnemonico digitado,
        // ele deve ser único
        String codigoMnemonico = entity.getCodigoMnemonico();
        Empresa empAux = this.findEmpresaByAcronym(codigoMnemonico);
        if (empAux != null && !empAux.getCodigoEmpresa().equals(entity.getCodigoEmpresa())) {
            throw new IntegrityConstraintException(
                    Constants.MSG_ERROR_ALREADY_EXISTS_ACRONYM);
        }
        
        // busca a entidade do banco por causa da sessão de conexão (hibernate)
        Empresa empresa = this.findEmpresaById(entity.getCodigoEmpresa());

        // usa a entidade do banco para verificar se existem filhos
        if (empresa.getEmpresas().size() > 0) {
            // verifica na entidade da tela se foi atribuído uma EmpresaPai
            if (entity.getEmpresa() != null) {
                // uma EmpresaPai não pode ter uma outra EmpresaPai
                throw new IntegrityConstraintException(Constants.MSG_ERROR_UPDATE_EMPRESA_PAI);
            }
        }

        // verifica se a entidade da tela está sendo inativada
        if (entity.getIndicadorAtivo().equals(Constants.INACTIVE)) {
            // usa a entidade do banco para verificar se existem filhos
            if (empresa.getEmpresas().size() > 0) {
                throw new IntegrityConstraintException(
                        Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_INACTIVE);
            }
        }

        empresaDao.update(entity);
    }
    
    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * @throws IntegrityConstraintException
     *             lança uma exception caso esta empresa seja pai de outras
     * 
     */
    public void removeEmpresa(final Empresa entity)
            throws IntegrityConstraintException {
        // verifica se existem EmpresaFilho relacionada
        if (entity.getEmpresas().size() > 0) {
            throw new IntegrityConstraintException(
                    Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE);
        }
        empresaDao.remove(entity);
    }
    
    /**
     * Busca uma lista de entidades pelo filtro - somente EmpresaFilho.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<Empresa> findEmpresaByFilter(final Empresa filter) {
        return empresaDao.findByFilter(filter);
    }
    
    /**
     * Busca uma Empresa pelo acronym (mnemonico).
     * 
     * @param acronym
     *            - código mnemonico
     * 
     * @return uma Empresa que possua o mnemonico passado por parâmetro
     */
    public Empresa findEmpresaByAcronym(final String acronym) {
        return empresaDao.findByAcronym(acronym);
    }

    @Override
    public boolean isUk(Empresa empresa) { return empresa.getCodigoEmpresa().equals(Constants.CD_EMPRESA_UK); }

    @Override
    public boolean isCinqInc(Empresa empresa) {
        return empresa.getCodigoEmpresa().equals(Constants.CD_EMPRESA_CINQ_INC);
    }

    @Override
    public boolean isCinqUs(Empresa empresa) {
        return empresa.getCodigoEmpresa().equals(Constants.CD_EMPRESA_CINQ_US);
    }

    @Override
    public boolean isDextraInc(Empresa empresa) {
        return empresa.getCodigoEmpresa().equals(Constants.CD_EMPRESA_DEXTRA_INC);
    }

    @Override
    public boolean isPortugal(Empresa empresa) {
        return empresa.getCodigoEmpresa().equals(Constants.CD_EMPRESA_PT);
    }

    @Override
    public boolean isColombia(Empresa empresa) { return empresa.getCodigoEmpresa().equals(Constants.CD_EMPRESA_COL); }

    @Override
    public boolean isOceania(Empresa empresa) {
        return empresa.getCodigoEmpresa().equals(Constants.CD_EMPRESA_OCEANIA);
    }

    @Override
    public List<Empresa> findWithAssociatedPosition() {
        return empresaDao.findWithAssociatedPosition();
    }

    public List<Empresa> findByIndicadorExibicaoMsaContrato(Boolean indicadorExibicaoMsaContrato) { return empresaDao.findByIndicadorExibicaoMsaContrato(indicadorExibicaoMsaContrato); }

    @Override
    public List<Empresa> findEmpresaByIdIn(List<Long> empresaCodes) {
        return empresaDao.findByIdIn(empresaCodes);
    }

    @Override
    public List<Empresa> findEmpresaByCodigoTaxpayerCompany(String codigoTaxpayerCompany) {
        return empresaDao.findByCodigoTaxpayerCompany(codigoTaxpayerCompany);
    }
}