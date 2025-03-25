package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.UploadDespesa;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author cmantovani
 * @since 01/07/2011
 */
@Repository
public interface IUploadDespesaDao extends IAbstractDao<Long, UploadDespesa> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<UploadDespesa> findAll();

    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<UploadDespesa> findByDataMes(final Date dataMes);

    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * @param empresa
     *            - empresa a ser consultada
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<UploadDespesa> findByDataMesAndEmpresa(final Date dataMes,
            final Empresa empresa);

    /**
     * Busca os Itens de Upload de Despesa pelo filtro no periodo.
     * 
     * @param dataInicio
     *            - data de Inicio
     * @param dataFim
     *            - data de Fim
     * @param descricao
     *            - texto de descricao
     * @param filter
     *            - entidade para pesquisa
     * @return lista de UploadDespesas
     */
    List<UploadDespesa> findByFilter(final Date dataInicio, final Date dataFim,
            final String descricao, final UploadDespesa filter);

}