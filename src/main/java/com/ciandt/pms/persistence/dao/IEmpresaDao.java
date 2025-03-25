/**
 * Interface da camada de DAO da entidade Empresa
 */
package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Empresa;


/**
 * 
 * A classe IEmpresaDao proporciona a interface de acesso para a camada DAO da
 * entidade Empresa.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IEmpresaDao extends IAbstractDao<Long, Empresa> {

    /**
     * Busca uma lista de Empresa filiais.
     * 
     * @return lista de Empresa filiais.
     */
    List<Empresa> findAllSubsidiary();
    
    /**
     * Busca uma lista de Empresa matriz.
     * 
     * @return lista de Empresa matriz
     */
    List<Empresa> findAllParentCompany();
    
    /**
     * Busca uma de todas as Empresa .
     * 
     * @return lista de Empresas.
     */
    List<Empresa> findAll();

    List<Empresa> findAllActiveForCombobox();
    
    /**
     * Busca uma lista de entidades pelo filtro - somente EmpresaFilho.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Empresa> findByFilter(final Empresa filter);
    
    /**
     * Busca uma Empresa pelo acronym (mnemonico).
     * 
     * @param acronym
     *            - código mnemonico
     * 
     * @return uma Empresa que possua o mnemonico passado por parâmetro
     */
    Empresa findByAcronym(final String acronym);

    List<Empresa> findWithAssociatedPosition();

    List<Empresa> findByIndicadorExibicaoMsaContrato(Boolean indicadorExibicaoMsaContrato);

    List<Empresa> findByIdIn(List<Long> empresaCodes);

    Empresa findEmpresaByERP(Long erpId);

    Empresa findByName(String name);

    List<Empresa> findByCodigoTaxpayerCompany(String codigoTaxpayerCompany);
}