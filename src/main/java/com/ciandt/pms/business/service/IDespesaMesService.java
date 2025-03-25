package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DespesaMes;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.TipoDespesa;


/**
 * 
 * A classe IDespesaService proporciona a interface de acesso a 
 * camada de serviço referente a entidade Despesa.
 *
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IDespesaMesService {

    /**
     * Insere uma entidade.
     * 
     * @param entity entidade a ser inserida.
     */
    void createDespesa(final DespesaMes entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity que será atualizada.
     * 
     */
    void updateDespesa(final DespesaMes entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    void removeDespesa(final DespesaMes entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    DespesaMes findDespesaById(final Long id);
    
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
    List<DespesaMes> findDespesaByContratoPraticaAndTipo(
            final ContratoPratica cp, final TipoDespesa tipo);
    
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
    List<DespesaMes> findDespesaByMapaDespesaAndTipo(
            final MapaDespesa mapDesp, final TipoDespesa tipo);
    
    /**
     * Busca a maior data referente ao mapa de despesa.
     * 
     * @param mapa
     *            que se deseja obter a maior data da despesa
     * 
     * @return retorna a marior data
     */
    Date findDespesaMesMaxDateByMapa(final MapaDespesa mapa);

    /**
     * Busca a menor data referente ao mapa de despesa.
     * 
     * @param mapa
     *            que se deseja obter a menor data da despesa
     * 
     * @return retorna a menor data
     */
    Date findDespesaMesMinDateByMapa(final MapaDespesa mapa);
    
    /**
     * Busca uma despesa de debito pelo mapa, tipo e mes.
     * 
     * @param mapa que se deseja obter a menor data da despesa
     * @param tipoDesp entidade TipoDespesa
     * @param dataMes data do tipo Date.
     * 
     * @return retorna a menor data
     */
    DespesaMes findDespesaMesDebitoByMapaTipoAndDate(final MapaDespesa mapa, 
            final TipoDespesa tipoDesp, final Date dataMes);
    
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
    List<DespesaMes> findDespesaMesByMapaAndNotInRange(
            final MapaDespesa mapa, final Date startDate, final Date endDate);
    
    /**
     * Busca uma despesa de pelo mapa, tipo e mes.
     * 
     * @param mapa que se deseja obter a menor data da despesa
     * @param tipoDesp entidade TipoDespesa
     * @param dataMes data do tipo Date.
     * 
     * @return retorna a lista de DespesaMes
     */
    List<DespesaMes> findDespesaMesByMapaTipoAndDate(final MapaDespesa mapa, 
            final TipoDespesa tipoDesp, final Date dataMes);
}
