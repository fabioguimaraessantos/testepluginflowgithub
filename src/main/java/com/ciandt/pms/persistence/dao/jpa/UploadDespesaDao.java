package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.UploadDespesa;
import com.ciandt.pms.persistence.dao.IUploadDespesaDao;


/**
 * Define o DAO da entidade.
 * 
 * @author cmantovani
 * @since 01/07/2011
 */
@Repository
public class UploadDespesaDao extends AbstractDao<Long, UploadDespesa>
        implements IUploadDespesaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo UploadDespesa
     */
    @Autowired
    public UploadDespesaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, UploadDespesa.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<UploadDespesa> findAll() {
        List<UploadDespesa> listResult = getJpaTemplate().findByNamedQuery(
                UploadDespesa.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    @SuppressWarnings("unchecked")
    public List<UploadDespesa> findByDataMes(final Date dataMes) {

        List<UploadDespesa> listResult = getJpaTemplate().findByNamedQuery(
                UploadDespesa.FIND_BY_DATA_MES, new Object[] {dataMes });

        return listResult;
    }

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
    @SuppressWarnings("unchecked")
    public List<UploadDespesa> findByDataMesAndEmpresa(final Date dataMes,
            final Empresa empresa) {

        List<UploadDespesa> listResult = getJpaTemplate().findByNamedQuery(
                UploadDespesa.FIND_BY_DATA_MES_AND_EMPRESA, new Object[] {dataMes, empresa.getCodigoEmpresa() });

        return listResult;
    }

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
    @SuppressWarnings("unchecked")
    public List<UploadDespesa> findByFilter(final Date dataInicio,
            final Date dataFim, final String descricao,
            final UploadDespesa filter) {

        List<UploadDespesa> listResult = getJpaTemplate().findByNamedQuery(
                UploadDespesa.FIND_BY_FILTER,
                new Object[] {filter.getEmpresa().getCodigoEmpresa(),
                        filter.getEmpresa().getCodigoEmpresa(),
                        filter.getGrupoCusto().getCodigoGrupoCusto(),
                        filter.getGrupoCusto().getCodigoGrupoCusto(),
                        filter.getTipoDespesa().getCodigoTipoDespesa(),
                        filter.getTipoDespesa().getCodigoTipoDespesa(),
                        dataInicio, dataFim, descricao, descricao });

        return listResult;
    }
}