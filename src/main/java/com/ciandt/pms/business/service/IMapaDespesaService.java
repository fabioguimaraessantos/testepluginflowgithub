package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.vo.MapaDespesaRow;


/**
 * 
 * A classe IMapaDespesaService proporciona a interface
 * de acesso a camada de serviço referente a entidade MapaDespesa.
 *
 * @since 28/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IMapaDespesaService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean createMapaDespesa(final MapaDespesa entity);

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @return true se update com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean updateMapaDespesa(final MapaDespesa entity);
    
    /**
     * Realiza o update da lista de despesas da entidade MapaDespesa.
     * 
     * @param entity - Entidade do tipo MapaDespesa
     * @param rowList - lista de despesas 
     * 
     * @return retorna true se update realizado com sucesso, caso contrario false.
     */
    @Transactional
    Boolean saveMapaDespesa(final MapaDespesa entity,
            final List<MapaDespesaRow> rowList);

    /**
     * Remove a entidade passada por parametro, exclusao na tela de abas,
     * verifica as Alocacao e remove os ItemTabelaPreco.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @return retorna true se sucesso senao false
     */
    @Transactional
    boolean removeMapaDespesa(final MapaDespesa entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    MapaDespesa findMapaDespesaById(final Long entityId);
    
    /**
     * Busca um mapa de despesa pelo contrato pratica.
     * 
     * @param cp - entidade do tipo ContratoPratica
     * 
     * @return retorna uma entidade do tipo MapaDespesa
     */
    MapaDespesa findMapaDespesaByContratoPratica(final ContratoPratica cp);
    
    /**
     * Prepara o mapa de despesa, para se criado.
     * 
     * @param cp - entidade do tipo ContratoPratica
     * @param startDate - Inicio da vigencia do mapa de despesas
     * @param endDate - Fom da vigencia do mapa de depesas
     * 
     * @return retorna uma entidade do tipo MapaDespesa
     */
    List<MapaDespesaRow> prepareCreate(final ContratoPratica cp, 
            final Date startDate, final Date endDate);
    
    /**
     * Prepara o MapaDespesa para o gerenciamento.
     * 
     * @param cp - entidade do tipo ContratoPratica
     * 
     * @return retorna a lista preparada para edição.
     */
    List<MapaDespesaRow> prepareManageMapaDespesa(final ContratoPratica cp);
    
    /**
     * Prepara o MapaDespesa para o gerenciamento.
     * 
     * @param mapDesp - entidade do tipo MapaDespesa
     * 
     * @return retorna a lista preparada para edição.
     */
    List<MapaDespesaRow> prepareManageMapaDespesa(final MapaDespesa mapDesp);
    
    /**
     * Pega a moeda do contratoPratica.
     * 
     * @param contratoPratica - entidade do tipo ContratoPratica
     * 
     * @return retorna uma moeda
     */
    Moeda getMoedaMapaDespesa(final ContratoPratica contratoPratica);
    
    /**
     * Busca uma lista de entidades pelo filtro. 
     * 
     * @param filter
     *            entidade populada com os valores do filtro e carrega.
     * @param cli
     *          entidade Cliente
     * @param msa 
     *          entidade Msa
     * @param natureza
     *          entidade NaturezaCentroLucro
     * @param centroLucro 
     *          entidade CentroLucro         
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<MapaDespesa> findMapaDespesaByFilter(final MapaDespesa filter,
            final Cliente cli, final Msa msa, 
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro);
    
    /**
     * Verifica se já exite mapa de despesa para um contrato pratica.
     * 
     * @param contratoPratica - entidade do tipo ContratoPratica
     * 
     * @return retorna true se existe, caso contrario false.
     */
    boolean existsMapaDespesa(final ContratoPratica contratoPratica);
    
    /**
     * Altera a vigencia do mapa de despesa.
     * 
     * @param mapaDesp - entidade do tipo MapaDespesa 
     * @param startDate - data inicio
     * @param endDate - data fim
     * 
     * @return retorna uma lista com a linhas do mapa de despesa
     */
    @Transactional
    List<MapaDespesaRow> mapaDespesachangePeriod(final MapaDespesa mapaDesp,
            final Date startDate, final Date endDate);
}
