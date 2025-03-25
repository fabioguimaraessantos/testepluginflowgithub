package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Etiqueta;
import com.ciandt.pms.model.EtiquetaAlocacao;
import com.ciandt.pms.model.vo.AlocacaoRow;
import com.ciandt.pms.model.vo.ItemReceitaRow;


/**
 * 
 * A classe IEtiquetaService proporciona a interface de acesso para a camada de
 * serviço referente a entidade Etiqueta.
 * 
 * @since 15/10/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IEtiquetaService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createEtiqueta(final Etiqueta entity);

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     * @throws IntegrityConstraintException
     *             - tratamento erro de referência de integridade
     */
    @Transactional
    void removeEtiqueta(final Etiqueta entity)
            throws IntegrityConstraintException;

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    Etiqueta findEtiquetaById(final Long entityId);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param clienteId
     *            id do Cliente.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Etiqueta> findEtiquetaByCliente(final Long clienteId);

    /**
     * Gera e atribui os nomes das Etiqueta para cada Alocacao para serem
     * exibidos na tela.
     * 
     * @param alocacaoRow
     *            - linha da Alocacao corrente
     */
    void generateEtiquetaNamesForAlocacaoRow(final AlocacaoRow alocacaoRow);

    /**
     * Filtra as Etiqueta (tags) na matriz para exibir somente a Etiqueta
     * selecionada.
     * 
     * @param codigoEtiqueta
     *            - código da Etiqueta
     * @param codigoMapaAlocacao
     *            - código do MapaAlocacao
     * @param alocacaoRowList
     *            - matriz de Alocacao X AlocacaoPeriodo
     */
    void filterEtiquetaForAlocacaoRowList(final Long codigoEtiqueta,
            final Long codigoMapaAlocacao,
            final List<AlocacaoRow> alocacaoRowList);

    /**
     * Aplica uma Etiqueta à Alocacao corrente.
     * 
     * @param alocacaoRowList
     *            - matriz de Alocacao X AlocacaoPeriodo
     * @param codigoEtiqueta
     *            - código da Etiqueta a ser aplicada
     */
    void applyEtiquetaForAlocacaoRowList(
            final List<AlocacaoRow> alocacaoRowList, final Long codigoEtiqueta);

    /**
     * Desaplica uma Etiqueta à Alocacao corrente.
     * 
     * @param alocacaoRowList
     *            - matriz de Alocacao X AlocacaoPeriodo
     * @param codigoEtiqueta
     *            - código da Etiqueta a ser aplicada
     */
    void unapplyEtiquetaForAlocacaoRowList(
            final List<AlocacaoRow> alocacaoRowList, final Long codigoEtiqueta);

    /**
     * Gera e atribui os nomes das Etiqueta para cada ItemReceita para serem
     * exibidos na tela.
     * 
     * @param itemReceitaRow
     *            - linha da ItemReceita corrente
     */
    void generateEtiquetaNamesForItemReceitaRow(
            final ItemReceitaRow itemReceitaRow);

    /**
     * Gera e atribui os nomes das Etiqueta para cada ItemReceita para serem
     * exibidos na tela, dado uma lista de EtiquetaAlocacao (na criacao da
     * Receita com base no MapaAlocacao).
     * 
     * @param itemReceitaRow
     *            - linha da ItemReceita corrente
     * @param etiquetaAlocacaoList
     *            - lista de EtiquetaAlocacao que vem do MapaAlocacao
     */
    void generateEtiquetaNamesForItemReceitaRow(
            final ItemReceitaRow itemReceitaRow,
            final List<EtiquetaAlocacao> etiquetaAlocacaoList);

    /**
     * Filtra as Etiqueta (tags) na lista de ItemReceita para exibir somente a
     * Etiqueta selecionada.
     * 
     * @param codigoEtiqueta
     *            - código da Etiqueta
     * @param codigoReceita
     *            - código do Receita
     * @param itemReceitaRowList
     *            - lista de ItemReceitaRow
     */
    void filterEtiquetaForItemReceitaRowList(final Long codigoEtiqueta,
            final Long codigoReceita,
            final List<ItemReceitaRow> itemReceitaRowList);

    /**
     * Aplica uma Etiqueta à ItemReceita corrente.
     * 
     * @param itemReceitaRowList
     *            - lista de ItemReceita
     * @param codigoEtiqueta
     *            - código da Etiqueta a ser aplicada
     */
    @Transactional
    void applyEtiquetaForItemReceitaRowList(
            final List<ItemReceitaRow> itemReceitaRowList,
            final Long codigoEtiqueta);

    /**
     * Desaplica uma Etiqueta à ItemReceita corrente.
     * 
     * @param itemReceitaRowList
     *            - lista de ItemReceita
     * @param codigoEtiqueta
     *            - código da Etiqueta a ser aplicada
     */
    void unapplyEtiquetaForItemReceitaRowList(
            final List<ItemReceitaRow> itemReceitaRowList,
            final Long codigoEtiqueta);

    /**
     * Aplica as Etiquetas vindas do MapaAlocacao aos ItemReceita.
     * 
     * @param itemReceitaRowList
     *            - lista de ItemReceitaRow
     */
    @Transactional
    void applyEtiquetaForItemReceitaRowList(
            final List<ItemReceitaRow> itemReceitaRowList);

    /**
     * Filtra as alocações para exibir somenete as alocações sem etiqueta.
     * 
     * @param alocacaoRowList
     *            - lista de alocações (rows)
     */
    void filterWithoutEtiquetaForAlocacaoRowList(
            final List<AlocacaoRow> alocacaoRowList);

    /**
     * Filtra os itens da receita para exibir somenete as alocações sem
     * etiqueta.
     * 
     * @param itemReceitaRowList
     *            - lista de item receita (rows)
     */
    void filterWithoutEtiquetaForItemReceitaRow(
            final List<ItemReceitaRow> itemReceitaRowList);

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateEtiqueta(Etiqueta entity);

    /**
     * Busca etiquetas ativas por cliente.
     * 
     * @param codigoCliente
     *            id do Cliente.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Etiqueta> findEtiquetaAtivaByCliente(final Long codigoCliente);

    /**
     * Busca etiquetas que atendem ao criterio nome e cliente.
     * 
     * @param codigoCliente
     *            id do Cliente.
     * @param nomeEtiqueta
     *            nome da Etiqueta.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    Etiqueta findEtiquetaByNomeAndCliente(final String nomeEtiqueta,
            final Long codigoCliente);

    /**
     * Adiciona etiqueta na tabela etiqueta relacionada ao cliente.
     * 
     * @param nomeEtiqueta
     *            nome da etiqueta a ser adicionada
     * @param cliente
     *            cliente relacionado a etiqueta
     * 
     * @return se sucesso, etiqueta adicionada. null, em caso de erro.
     */
    @Transactional
    Etiqueta addEtiqueta(final String nomeEtiqueta, final Cliente cliente);

    /**
     * Remove uma Etiqueta na tabela Etiqueta relacionado ao Cliente corrente.
     * 
     * @param nomeEtiqueta
     *            nome da etiqueta a ser removida
     * @param cliente
     *            cliente relacionado a etiqueta
     * 
     * @return true se sucesso na deleção; false, caso contrario.
     */
    @Transactional
    boolean deleteEtiqueta(final String nomeEtiqueta, final Cliente cliente);
}