package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ChargebackContratoPratica;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.vo.ChargebackRow;


/**
 * 
 * A classe IChargebackCPService proporciona a interface
 * de acesso a camada de serviço referente a entidade ChargebackContratoPratica.
 *
 * @since 16/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IChargebackCPService {
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional 
    void createChargeback(final ChargebackContratoPratica entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que sera atualizada.
     * 
     */
    @Transactional
    void updateChargeback(final ChargebackContratoPratica entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que sera apagada.
     * 
     */
    @Transactional
    void removeChargeback(final ChargebackContratoPratica entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    ChargebackContratoPratica findChargebackById(final Long id);
    
    /**
     * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente
     * o atributo 'naturezaCentroLucro'.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ChargebackContratoPratica> findChargebackByFilter(final ChargebackContratoPratica filter);
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<ChargebackContratoPratica> findChargebackAll();
    
    /**
     * Busca e prepara a o Mapa de Chargeback para o gerenciamento, 
     * referente a um recurso.
     *  
     * @param recurso - entidade do tipo ItRecurso.
     * @param startDate - data inicio do periodo
     * @param endDate - data fim do periodo
     * 
     * @return returna um lista de ChargebackRow referente ao mapa de Chargeback.
     */
    List<ChargebackRow> findAndPrepareManagePerResource(
            final TiRecurso recurso, final Date startDate, final Date endDate);
    
    /**
     * Busca e prepara a o Mapa de Chargeback para o gerenciamento, 
     * referente a um contrato-prática.
     *  
     * @param cp - entidade do tipo ContratoPratica.
     * @param startDate - data inicio do periodo
     * @param endDate - data fim do periodo
     * 
     * @return returna um lista de ChargebackRow referente ao mapa de Chargeback.
     */
    List<ChargebackRow> findAndPrepareManagePerContract(final ContratoPratica cp,
            final Date startDate, final Date endDate);
    
    /**
     * Realiza o update do 'Mapa' do chargeback.
     * 
     * @param chargebackRowList - Lista do Mapa do chargeback.
     *  
     * @return retorna true se update realizado com sucesso, caso contrario false.
     */
    @Transactional
    Boolean updateManageChargeback(final List<ChargebackRow> chargebackRowList);
    
    /**
     * Remove todas as linhas selecionadas.
     * 
     * @param rowList - Lista com as linas
     * 
     * @return retorna true se removido com sucesso,
     * caso contrario false. 
     */
    @Transactional
    Boolean removeAllSelectedRow(final List<ChargebackRow> rowList);
    
    /**
     * Cria a linha do Mapa.
     * 
     * @param chargeback - entidade do tipo ChargebackContratoPratica.
     * @param startDate - data inicio do peridod
     * @param endDate = data fim do periodo
     * 
     * @return a linha criada, entidade do tipo ChargebackRow. 
     */
    @Transactional
    ChargebackRow createRow(
            final ChargebackContratoPratica chargeback, 
            final Date startDate, final Date endDate);
    
    /**
     * Remove todas as linhas selecionadas.
     * 
     * @param numUnits - Numero de unidades 
     * @param rowList - Lista com as linas
     * 
     * @return retorna true se removido com sucesso,
     * caso contrario false. 
     */
    @Transactional
    Boolean updateAllNumUnits(final BigDecimal numUnits, 
            final List<ChargebackRow> rowList);
    
    /**
     * Pega a data inicio default para o filtro.
     *  
     * @return retorna a data inicio
     */
    Date getDefaultStartDate();
    
    /**
     * Pega a data fim default para o filtro.
     *  
     * @return retorna a data fim
     */
    Date getDefaultEndDate();
}
