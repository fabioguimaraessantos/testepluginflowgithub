package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ITiRecursoService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CustoTiRecurso;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.persistence.dao.ITiRecursoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 
 * A classe TiRecursoService proporciona as funcionalidades de serviço referente
 * a entidade TiRecurso.
 * 
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class TiRecursoService implements ITiRecursoService {

    /** Instancia do DAO da entidade TiRecurso. */
    @Autowired
    private ITiRecursoDao dao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createTiRecurso(final TiRecurso entity) {
        // seta como ativo ao criar um recurso.
        entity.setIndicadorAtivo(Constants.ACTIVE);

        dao.create(entity);
    }

    /**
     * Insere uma entidade.
     * 
     * @param tiRecurso
     *            entidade a ser inserida.
     * 
     * @param custoTiRecurso
     *            entidade do Tipo CustoTiRecurso a ser inserida e relacionada
     *            com tiRecurso.
     */
    public void createTiRecurso(final TiRecurso tiRecurso,
            final CustoTiRecurso custoTiRecurso) {
        // seta a lista de CustoTiRecurso no TiRecurso
        List<CustoTiRecurso> custoTiRecursoSet =
                new ArrayList<CustoTiRecurso>();
        custoTiRecursoSet.add(custoTiRecurso);

        tiRecurso.setCustoTiRecursos(custoTiRecursoSet);

        this.createTiRecurso(tiRecurso);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateTiRecurso(final TiRecurso entity) {
        dao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    public void removeTiRecurso(final TiRecurso entity) {
        TiRecurso tiRec = findTiRecursoById(entity.getCodigoTiRecurso());

        dao.remove(tiRec);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public TiRecurso findTiRecursoById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<TiRecurso> findTiRecursoByFilter(final TiRecurso filter) {
        return dao.findByFilter(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TiRecurso> findTiRecursoAllActive() {
        return dao.findAllActive();
    }

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param cp
     *            - ContratoPratica que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TiRecurso> findTiRecursoByContractAndPeriod(
            final ContratoPratica cp, final Date startDate, final Date endDate) {
        return dao.findByContractAndPeriod(cp, startDate, endDate);
    }

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param pessoa
     *            - Pessoa que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TiRecurso> findTiRecursoByPessoaAndPeriod(final Pessoa pessoa,
            final Date startDate, final Date endDate) {
        return dao.findByPessoaAndPeriod(pessoa, startDate, endDate);
    }

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param pessoa
     *            - Pessoa que se deseja buscar os TiRecurso
     * 
     * @param startDate
     *            - Data inicio do periodo.
     * 
     * @param endDate
     *            - Data fim do periodo
     * 
     * @param tipo
     *            - tipo
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TiRecurso> findTiRecursoByPessoaAndPeriodAndType(
            final Pessoa pessoa, final Date startDate, final Date endDate,
            final String tipo) {
        return dao.findByPessoaAndPeriodAndType(pessoa, startDate, endDate,
                tipo);
    }

    /**
     * Retorna todos os TiRecurso relacionados com o chargeback e dentro de um
     * periodo.
     * 
     * @param type
     *            - Tipo de TiRecurso (CS-Contract Server, US-User Service)
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TiRecurso> findTiRecursoByType(final String type) {
        return dao.findByType(type);
    }

    public TiRecurso findByNomeTiRecurso(final String nomeTiRecurso) {
        return dao.findByNomeTiRecurso(nomeTiRecurso);
    }
}