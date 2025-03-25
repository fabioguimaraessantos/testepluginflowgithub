package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IDespesaMesService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DespesaMes;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.TipoDespesa;
import com.ciandt.pms.persistence.dao.IDespesaMesDao;


/**
 * 
 * A classe DespesaService proporciona as funcionalidades da camada 
 * de serviço referente a entidade Despesa.
 *
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class DespesaMesService implements IDespesaMesService {

    /** Instancia da entidade DespesaDao. */
    @Autowired
    private IDespesaMesDao despDao; 
    
    /**
     * Insere uma entidade.
     * 
     * @param entity entidade a ser inserida.
     */
    public void createDespesa(final DespesaMes entity) {
        despDao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity que será atualizada.
     * 
     */
    public void updateDespesa(final DespesaMes entity) {
        despDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity que será apagada.
     * 
     */
    public void removeDespesa(final DespesaMes entity) {
        despDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public DespesaMes findDespesaById(final Long id) {
        return despDao.findById(id);
    }
    
    /**
     * Busca uma lista de entidades pelo contrato-pratica
     * e tipo de despesa.
     * 
     * @param cp entidade do tipo ContratoPratica.
     * 
     * @param tipo entidade do tipo TipoDespesa.
     * 
     * @return lista de entidades do tipo Despesa
     */
    public List<DespesaMes> findDespesaByContratoPraticaAndTipo(
            final ContratoPratica cp, final TipoDespesa tipo) {
        return despDao.findByContratoPraticaAndTipo(cp, tipo);
    }
    
    /**
     * Busca uma lista de entidades pelo contrato-pratica
     * e tipo de despesa.
     * 
     * @param mapDesp entidade do tipo MapaDespesa.
     * 
     * @param tipo entidade do tipo TipoDespesa.
     * 
     * @return lista de entidades do tipo Despesa
     */
    public List<DespesaMes> findDespesaByMapaDespesaAndTipo(
            final MapaDespesa mapDesp, final TipoDespesa tipo) {
        return despDao.findByMapaDespesaAndTipo(mapDesp, tipo);
    }

    /**
     * Busca a maior data referente ao mapa de despesa.
     * 
     * @param mapa
     *            que se deseja obter a maior data da despesa
     * 
     * @return retorna a marior data
     */
    public Date findDespesaMesMaxDateByMapa(final MapaDespesa mapa) {
        return despDao.findMaxDateByMapa(mapa);
    }

    /**
     * Busca a menor data referente ao mapa de despesa.
     * 
     * @param mapa
     *            que se deseja obter a menor data da despesa
     * 
     * @return retorna a menor data
     */
    public Date findDespesaMesMinDateByMapa(final MapaDespesa mapa) {
        return despDao.findMinDateByMapa(mapa);
    }
    
    /**
     * Busca uma despesa de debito pelo mapa, tipo e mes.
     * 
     * @param mapa que se deseja obter a menor data da despesa
     * @param tipoDesp entidade TipoDespesa
     * @param dataMes data do tipo Date.
     * 
     * @return retorna a menor data
     */
    public DespesaMes findDespesaMesDebitoByMapaTipoAndDate(final MapaDespesa mapa, 
            final TipoDespesa tipoDesp, final Date dataMes) {
        return despDao.findDebitoByMapaTipoAndDate(mapa, tipoDesp, dataMes);
    }
    
    /**
     * Busca as despesas, que não estão dentro 
     * do periodo passado por aprametro.
     * 
     * @param mapa
     *          entidade do tipo MapaDespesa.
     * @param startDate
     *          Data referente ao inicio do periodo.
     * @param endDate
     *          Data referente ao fim do periodo.
     *                
     * @return retorna uma lista de DespesaMes.
     */
    public List<DespesaMes> findDespesaMesByMapaAndNotInRange(
            final MapaDespesa mapa, final Date startDate, final Date endDate) {
       return despDao.findByMapaAndNotInRange(mapa, startDate, endDate); 
    }
    
    /**
     * Busca uma despesa de pelo mapa, tipo e mes.
     * 
     * @param mapa que se deseja obter a menor data da despesa
     * @param tipoDesp entidade TipoDespesa
     * @param dataMes data do tipo Date.
     * 
     * @return retorna a lista de DespesaMes
     */
    public List<DespesaMes> findDespesaMesByMapaTipoAndDate(final MapaDespesa mapa, 
            final TipoDespesa tipoDesp, final Date dataMes) {
        return despDao.findByMapaTipoAndDate(mapa, tipoDesp, dataMes);
    }

}
