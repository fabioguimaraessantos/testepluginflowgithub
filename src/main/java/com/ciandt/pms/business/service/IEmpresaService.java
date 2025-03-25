package com.ciandt.pms.business.service;

import java.util.List;

import com.ciandt.pms.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Empresa;


/**
 * 
 * A classe IEmpresaService proporciona a interface de acesso para a camada de
 * serviço referente a entidade Empresa.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IEmpresaService {

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    Empresa findEmpresaById(final Long entityId);

    Empresa findEmpresaByName(final String name);

    Empresa findEmpresaByERP(final Long erpId);

    /**
     * Busca uma lista de Empresa filiais.
     * 
     * @return lista de Empresa filiais.
     */
    List<Empresa> findEmpresaAllSubsidiary();
    
    /**
     * Busca uma lista de Empresa matriz.
     * 
     * @return lista de Empresa matriz.
     */
    List<Empresa> findEmpresaAllParentCompany();

    
    /**
     * Busca uma de todas as Empresa .
     * 
     * @return lista de Empresas.
     */
    List<Empresa> findEmpresaAll();

    List<Empresa> findAllActiveForCombobox();
    
    /**
     * Insere uma entidade.
     * @param entity a ser inserida
     * 
     * @throws IntegrityConstraintException
     *             lança uma exceção caso esta empresa seja pai e tenha empresa
     *             pai atribuida ou caso a empresa seja pai de outras empresas e
     *             tente inativá-lo ou cadastre um mnenonico que já exista
     */
    @Transactional
    void createEmpresa(final Empresa entity) throws IntegrityConstraintException;
    
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
    @Transactional
    void updateEmpresa(final Empresa entity) throws IntegrityConstraintException;
    
    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * @throws IntegrityConstraintException
     *             lança uma exception caso esta empresa seja pai de outras
     * 
     */
    @Transactional
    void removeEmpresa(final Empresa entity) throws IntegrityConstraintException;
    
    /**
     * Busca uma lista de entidades pelo filtro - somente EmpresaFilho.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Empresa> findEmpresaByFilter(final Empresa filter);
    
    /**
     * Busca uma Empresa pelo acronym (mnemonico).
     * 
     * @param acronym
     *            - código mnemonico
     * 
     * @return uma Empresa que possua o mnemonico passado por parâmetro
     */
    Empresa findEmpresaByAcronym(final String acronym);

    boolean isUk(Empresa empresa);

    boolean isCinqInc(Empresa empresa);

    boolean isCinqUs(Empresa empresa);

    boolean isDextraInc(Empresa empresa);

    boolean isPortugal(Empresa empresa);

    boolean isColombia(Empresa empresa);

    boolean isOceania(Empresa empresa);

    List<Empresa> findWithAssociatedPosition();

    List<Empresa> findByIndicadorExibicaoMsaContrato(Boolean indicadorExibicaoMsaContrato);

    List<Empresa> findEmpresaByIdIn(List<Long> empresaCodes);

    List<Empresa> findEmpresaByCodigoTaxpayerCompany(String codigoTaxpayerCompany);
}