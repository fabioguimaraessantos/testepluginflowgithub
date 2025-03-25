/**
 * Classe EtiquetaService - Implementação do serviço do Etiqueta
 */
package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoService;
import com.ciandt.pms.business.service.IEtiquetaAlocacaoService;
import com.ciandt.pms.business.service.IEtiquetaItemReceitaService;
import com.ciandt.pms.business.service.IEtiquetaService;
import com.ciandt.pms.business.service.IItemReceitaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Etiqueta;
import com.ciandt.pms.model.EtiquetaAlocacao;
import com.ciandt.pms.model.EtiquetaAlocacaoId;
import com.ciandt.pms.model.EtiquetaItemReceita;
import com.ciandt.pms.model.EtiquetaItemReceitaId;
import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.model.vo.AlocacaoRow;
import com.ciandt.pms.model.vo.ItemReceitaRow;
import com.ciandt.pms.persistence.dao.IEtiquetaDao;


/**
 * A classe EtiquetaService proporciona as funcionalidades de serviço para a
 * entidade Etiqueta.
 * 
 * @since 15/10/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class EtiquetaService implements IEtiquetaService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IEtiquetaDao etiquetaDao;

    /** Instancia do servico EtiquetaAlocacao. */
    @Autowired
    private IEtiquetaAlocacaoService etiquetaAlocacaoService;

    /** Instancia do servico EtiquetaItemReceita. */
    @Autowired
    private IEtiquetaItemReceitaService etiquetaItemReceitaService;

    /** Instancia do servico Alocacao. */
    @Autowired
    private IAlocacaoService alocacaoService;

    /** Instancia do servico ItemReceita. */
    @Autowired
    private IItemReceitaService itemReceitaService;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createEtiqueta(final Etiqueta entity) {
        // na criação, seta etiqueta como ativa
        entity.setIndicadorStatus(Etiqueta.ETIQUETA_ATIVO);
        
        etiquetaDao.create(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     * @throws IntegrityConstraintException
     *             - tratamento erro de referência de integridade, somente será
     *             removido se não existir Alocacao relacionada com esse
     *             Etiqueta
     */
    public void removeEtiqueta(final Etiqueta entity)
            throws IntegrityConstraintException {
        // verifica se existem Alocacao relacionadas com a Etiqueta corrente
        if (entity.getEtiquetaAlocacaos().size() > 0) {
            throw new IntegrityConstraintException(
                    Constants.ENTITY_NAME_ALOCACAO);
        }
        // verifica se existem ItemReceita relac com a Etiqueta corrente
        if (entity.getEtiquetaItemReceitas().size() > 0) {
            throw new IntegrityConstraintException(
                    Constants.ENTITY_NAME_ITEM_RECEITA);
        }

        etiquetaDao.remove(entity);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public Etiqueta findEtiquetaById(final Long entityId) {
        return etiquetaDao.findById(entityId);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param clienteId
     *            id do Cliente.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<Etiqueta> findEtiquetaByCliente(final Long clienteId) {
        return etiquetaDao.findByCliente(clienteId);
    }

    /**
     * Recupera o tamanho da String das Etiqueta (tags) do arquivo de
     * configurações.
     * 
     * @return tamanho da String das Etiqueta (tags) do arquivo de configurações
     */
    private int getTagsLength() {
        return Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_ETIQUETA_LENGTH));
    }

    // ==========================================
    // Alocacao
    // ==========================================

    /**
     * Gera e atribui os nomes das Etiqueta para cada Alocacao para serem
     * exibidos na tela.
     * 
     * @param alocacaoRow
     *            - linha da Alocacao corrente
     */
    public void generateEtiquetaNamesForAlocacaoRow(
            final AlocacaoRow alocacaoRow) {
        // recupera a Alocacao corrente
        Alocacao alocacao = alocacaoRow.getAlocacao();
        // recupera a lista de Etiqueta (tag) de cada Alocacao
        List<EtiquetaAlocacao> etiquetaAlocacaoList = alocacao
                .getEtiquetaAlocacaos();
        StringBuffer etiquetaNamesSB = new StringBuffer();
        // concatena as Etiqueta (tags) separados por virgula
        for (EtiquetaAlocacao etiquetaAlocacao : etiquetaAlocacaoList) {
            Etiqueta etiqueta = findEtiquetaById(etiquetaAlocacao.getId()
                    .getCodigoEtiqueta());
            etiquetaNamesSB.append(etiqueta.getNomeEtiqueta() + ", ");
        }
        // se existir pelo menos uma Etiqueta (tag) com pelo menos uma letra
        if (etiquetaNamesSB.length() > 1) {
            alocacaoRow.setEtiquetaNames(etiquetaNamesSB.toString().substring(
                    0, etiquetaNamesSB.length() - 2));
            String etiquetaNames = alocacaoRow.getEtiquetaNames();
            // pega o tamanho da String das Etiqueta (tags)
            int tagsLength = getTagsLength();
            // se a String das Etiqueta (tags) for maior do que o tamanho
            // configurado no properties, pega uma parte da String e concatena 3
            // pontinhos
            if (etiquetaNames.length() > tagsLength) {
                alocacaoRow.setEtiquetaNamesPart(etiquetaNames.substring(0,
                        tagsLength)
                        + "...");
            } else {
                alocacaoRow.setEtiquetaNamesPart(etiquetaNames);
            }
            // se não existir pelo menos uma Etiqueta (tag) limpa os campos
        } else {
            alocacaoRow.setEtiquetaNames("");
            alocacaoRow.setEtiquetaNamesPart("");
        }
    }

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
    public void filterEtiquetaForAlocacaoRowList(final Long codigoEtiqueta,
            final Long codigoMapaAlocacao,
            final List<AlocacaoRow> alocacaoRowList) {
        Map<Long, EtiquetaAlocacao> mapEtiquetaAlocacao = etiquetaAlocacaoService
                .findEtiqAlocByEtiquetaAndMapaAlocacao(codigoEtiqueta,
                        codigoMapaAlocacao);

        for (AlocacaoRow alocacaoRow : alocacaoRowList) {
            if (mapEtiquetaAlocacao.get(alocacaoRow.getAlocacao()
                    .getCodigoAlocacao()) == null) {
                alocacaoRow.setIsView(Boolean.valueOf(false));
            } else {
                alocacaoRow.setIsView(Boolean.valueOf(true));
            }
        }
    }
    
    /**
     * Filtra as alocações para exibir somenete as alocações sem etiqueta.
     * 
     * @param alocacaoRowList - lista de alocações (rows) 
     */
    public void filterWithoutEtiquetaForAlocacaoRowList(final List<AlocacaoRow> alocacaoRowList) {
        
        for (AlocacaoRow alocacaoRow : alocacaoRowList) {
            if (alocacaoRow.getAlocacao().getEtiquetaAlocacaos().isEmpty()) {
                alocacaoRow.setIsView(Boolean.TRUE);
            } else {
                alocacaoRow.setIsView(Boolean.FALSE);
            }
        }
    }

    /**
     * Aplica uma Etiqueta à Alocacao corrente.
     * 
     * @param alocacaoRowList
     *            - matriz de Alocacao X AlocacaoPeriodo
     * @param codigoEtiqueta
     *            - código da Etiqueta a ser aplicada
     */
    public void applyEtiquetaForAlocacaoRowList(
            final List<AlocacaoRow> alocacaoRowList, final Long codigoEtiqueta) {
        Etiqueta etiqueta = findEtiquetaById(codigoEtiqueta);
        if (etiqueta != null) {
            // para cada AlocacaoRow, verifica se está selecionada
            for (AlocacaoRow alocacaoRow : alocacaoRowList) {
                if (alocacaoRow.getIsSelected() && alocacaoRow.getIsView()) {
                    Alocacao alocacao = alocacaoRow.getAlocacao();
                    // verifica se a Alocacao na matrix tem código,
                    // ou seja, se já foi gravada no banco. Se não
                    // tem, dá mensagem de erro para essa linha
                    // e continua na próxima
                    if (alocacao.getCodigoAlocacao() == null) {
                        Messages.showError("applyEtiquetaForAlocacaoRowList",
                                Constants.MSG_ERROR_INVAL_ALLOCATION_TAG,
                                alocacao.getRecurso().getCodigoMnemonico());
                        continue;
                    }
                    // cria e popula um objeto EtiquetaAlocacao
                    EtiquetaAlocacao etiquetaAlocacao = new EtiquetaAlocacao();
                    etiquetaAlocacao.setAlocacao(alocacaoService
                            .findAlocacaoById(alocacao.getCodigoAlocacao()));
                    etiquetaAlocacao.setEtiqueta(etiqueta);

                    EtiquetaAlocacaoId id = new EtiquetaAlocacaoId();
                    id.setCodigoAlocacao(alocacao.getCodigoAlocacao());
                    id.setCodigoEtiqueta(etiqueta.getCodigoEtiqueta());
                    etiquetaAlocacao.setId(id);

                    // verifica se já existe essa associação na base
                    // de
                    // dados
                    EtiquetaAlocacao etiqAlocDB = etiquetaAlocacaoService
                            .findEtiquetaAlocacaoById(id);
                    if (etiqAlocDB == null) {
                        // se não existir, cria a associação
                        etiquetaAlocacaoService
                                .createEtiquetaAlocacao(etiquetaAlocacao);
                        // atualiza a lista de Etiqueta da Alocacao
                        // corrente
                        alocacao.getEtiquetaAlocacaos().add(etiquetaAlocacao);
                        // atualiza as Etiqueta na tela
                        generateEtiquetaNamesForAlocacaoRow(alocacaoRow);
                    }
                }
            }
        }
    }

    /**
     * Desaplica uma Etiqueta à Alocacao corrente.
     * 
     * @param alocacaoRowList
     *            - matriz de Alocacao X AlocacaoPeriodo
     * @param codigoEtiqueta
     *            - código da Etiqueta a ser aplicada
     */
    public void unapplyEtiquetaForAlocacaoRowList(
            final List<AlocacaoRow> alocacaoRowList, final Long codigoEtiqueta) {
        Etiqueta etiqueta = findEtiquetaById(codigoEtiqueta);
        if (etiqueta != null) {
            // para cada AlocacaoRow, verifica se está selecionada
            for (AlocacaoRow alocacaoRow : alocacaoRowList) {
                if (alocacaoRow.getIsSelected() && alocacaoRow.getIsView()) {
                    // cria e popula um objeto EtiquetaAlocacaoId
                    EtiquetaAlocacaoId id = new EtiquetaAlocacaoId();
                    Alocacao alocacao = alocacaoRow.getAlocacao();
                    id.setCodigoAlocacao(alocacao.getCodigoAlocacao());
                    id.setCodigoEtiqueta(etiqueta.getCodigoEtiqueta());
                    // busca o objeto EtiquetaAlocacao a ser
                    // removido
                    EtiquetaAlocacao etiquetaAlocacao = etiquetaAlocacaoService
                            .findEtiquetaAlocacaoById(id);
                    if (etiquetaAlocacao != null) {
                        // se existir, remove a associação
                        etiquetaAlocacaoService
                                .removeEtiquetaAlocacao(etiquetaAlocacao);
                        // atualiza a lista de Etiqueta da Alocacao
                        // corrente
                        Alocacao alocacaoDB = alocacaoService
                                .findAlocacaoById(alocacao.getCodigoAlocacao());
                        alocacao.setEtiquetaAlocacaos(alocacaoDB
                                .getEtiquetaAlocacaos());
                        // atualiza as Etiqueta na tela
                        generateEtiquetaNamesForAlocacaoRow(alocacaoRow);
                    }
                }
            }
        }
    }

    // ==========================================
    // ItemReceita
    // ==========================================

    /**
     * Gera e atribui os nomes das Etiqueta para cada ItemReceita para serem
     * exibidos na tela.
     * 
     * @param itemReceitaRow
     *            - linha da ItemReceita corrente
     */
    public void generateEtiquetaNamesForItemReceitaRow(
            final ItemReceitaRow itemReceitaRow) {
        // recupera o ItemReceita corrente
        ItemReceita itemReceita = itemReceitaRow.getItemReceita();
        
        // recupera a lista de Etiqueta (tag) de cada ItemReceita
        List<EtiquetaItemReceita> etiquetaItemReceitaList = 
            etiquetaItemReceitaService.findEtiqItemReceitaByItemReceita(itemReceita);
        
        StringBuffer etiquetaNamesSB = new StringBuffer();
        // concatena as Etiqueta (tags) separados por virgula
        for (EtiquetaItemReceita etiquetaItemReceita : etiquetaItemReceitaList) {
            Etiqueta etiqueta = findEtiquetaById(etiquetaItemReceita.getId()
                    .getCodigoEtiqueta());
            etiquetaNamesSB.append(etiqueta.getNomeEtiqueta() + ", ");
        }

        generateEtiquetaNamesForItemReceitaRow(itemReceitaRow, etiquetaNamesSB);
    }

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
    public void generateEtiquetaNamesForItemReceitaRow(
            final ItemReceitaRow itemReceitaRow,
            final List<EtiquetaAlocacao> etiquetaAlocacaoList) {
        StringBuffer etiquetaNamesSB = new StringBuffer();
        List<EtiquetaItemReceita> etiquetaItemReceitaList = new ArrayList<EtiquetaItemReceita>();
        // concatena as Etiqueta (tags) separados por virgula
        for (EtiquetaAlocacao etiquetaAlocacao : etiquetaAlocacaoList) {
            Etiqueta etiqueta = findEtiquetaById(etiquetaAlocacao.getId()
                    .getCodigoEtiqueta());
            etiquetaNamesSB.append(etiqueta.getNomeEtiqueta() + ", ");

            // cria a EtiquetaHrsFatDeal com base na EtiquetaAlocacao corrente
            EtiquetaItemReceita etiquetaItemReceita = new EtiquetaItemReceita();
            EtiquetaItemReceitaId id = new EtiquetaItemReceitaId();
            id.setCodigoEtiqueta(etiqueta.getCodigoEtiqueta());
            etiquetaItemReceita.setId(id);

            // atribui a EtiquetaItemReceita à lista
            etiquetaItemReceitaList.add(etiquetaItemReceita);
        }

        // atribui a lista ao ItemReceita corrente
        itemReceitaRow.setEtiquetaItemReceitaList(etiquetaItemReceitaList);

        generateEtiquetaNamesForItemReceitaRow(itemReceitaRow, etiquetaNamesSB);
    }

    /**
     * Gera e atribui os nomes das Etiqueta para cada ItemReceita para serem
     * exibidos na tela.
     * 
     * @param itemReceitaRow
     *            - linha da ItemReceita corrente
     * @param etiquetaNamesSB
     *            - nomes das Etiqueta do ItemReceita
     */
    private void generateEtiquetaNamesForItemReceitaRow(
            final ItemReceitaRow itemReceitaRow,
            final StringBuffer etiquetaNamesSB) {
        // se existir pelo menos uma Etiqueta (tag) com pelo menos uma letra
        if (etiquetaNamesSB.length() > 1) {
            itemReceitaRow.setEtiquetaNames(etiquetaNamesSB.toString()
                    .substring(0, etiquetaNamesSB.length() - 2));
            String etiquetaNames = itemReceitaRow.getEtiquetaNames();
            // pega o tamanho da String das Etiqueta (tags)
            int tagsLength = getTagsLength();
            // se a String das Etiqueta (tags) for maior do que o tamanho
            // configurado no properties, pega uma parte da String e concatena 3
            // pontinhos
            if (etiquetaNames.length() > tagsLength) {
                itemReceitaRow.setEtiquetaNamesPart(etiquetaNames.substring(0,
                        tagsLength)
                        + "...");
            } else {
                itemReceitaRow.setEtiquetaNamesPart(etiquetaNames);
            }
            // se não existir pelo menos uma Etiqueta (tag) limpa os campos
        } else {
            itemReceitaRow.setEtiquetaNames("");
            itemReceitaRow.setEtiquetaNamesPart("");
        }
    }

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
    public void filterEtiquetaForItemReceitaRowList(final Long codigoEtiqueta,
            final Long codigoReceita,
            final List<ItemReceitaRow> itemReceitaRowList) {
        Map<Long, EtiquetaItemReceita> mapEtiquetaItemReceita = etiquetaItemReceitaService
                .findEtiqItemReceitaByEtiquetaAndReceita(codigoEtiqueta,
                        codigoReceita);

        for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
            if (mapEtiquetaItemReceita.get(itemReceitaRow.getItemReceita()
                    .getCodigoItemReceita()) == null) {
                itemReceitaRow.setIsView(Boolean.valueOf(false));
            } else {
                itemReceitaRow.setIsView(Boolean.valueOf(true));
            }
        }
    }
    
    /**
     * Filtra os itens da receita para exibir somenete as alocações sem etiqueta.
     * 
     * @param itemReceitaRowList - lista de item receita (rows)
     */
    public void filterWithoutEtiquetaForItemReceitaRow(final List<ItemReceitaRow> itemReceitaRowList) {
        
        for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
            Long codItemReceita = itemReceitaRow.getItemReceita().getCodigoItemReceita();
            ItemReceita item = itemReceitaService.findItemReceitaById(codItemReceita);
            
            if (item.getEtiquetaItemReceitas().isEmpty()) {
                itemReceitaRow.setIsView(Boolean.TRUE);
            } else {
                itemReceitaRow.setIsView(Boolean.FALSE);
            }
        }
    }

    /**
     * Aplica uma Etiqueta à ItemReceita corrente.
     * 
     * @param itemReceitaRowList
     *            - lista de ItemReceita
     * @param codigoEtiqueta
     *            - código da Etiqueta a ser aplicada
     */
    public void applyEtiquetaForItemReceitaRowList(
            final List<ItemReceitaRow> itemReceitaRowList,
            final Long codigoEtiqueta) {
        Etiqueta etiqueta = findEtiquetaById(codigoEtiqueta);
        if (etiqueta != null) {
            // para cada itemReceitaRow, verifica se está selecionada
            for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
                if (itemReceitaRow.getIsSelected()
                        && itemReceitaRow.getIsView()) {
                    ItemReceita itemReceita = itemReceitaRow.getItemReceita();
                    // verifica se o ItemReceita tem código,
                    // ou seja, se já foi gravado no banco. Se não
                    // tem, dá mensagem de erro para essa linha
                    // e continua na próxima
                    if (itemReceita.getCodigoItemReceita() == null) {
                        Messages
                                .showError(
                                        "applyEtiquetaForItemReceitaRowList",
                                        Constants.MSG_ERROR_INVAL_ITEM_HRS_FAT_DEAL_TAG,
                                        itemReceita.getPessoa()
                                                .getCodigoLogin());
                        continue;
                    }
                    // cria e popula um objeto EtiquetaItemReceita
                    EtiquetaItemReceita etiquetaItemReceita = new EtiquetaItemReceita();
                    etiquetaItemReceita.setItemReceita(itemReceitaService
                            .findItemReceitaById(itemReceita
                                    .getCodigoItemReceita()));
                    etiquetaItemReceita.setEtiqueta(etiqueta);

                    EtiquetaItemReceitaId id = new EtiquetaItemReceitaId();
                    id.setCodigoItemReceita(itemReceita.getCodigoItemReceita());
                    id.setCodigoEtiqueta(etiqueta.getCodigoEtiqueta());
                    etiquetaItemReceita.setId(id);

                    // verifica se já existe essa associação na base
                    // de dados
                    EtiquetaItemReceita etiqItemReceitaDB = etiquetaItemReceitaService
                            .findEtiquetaItemReceitaById(id);
                    if (etiqItemReceitaDB == null) {
                        // se não existir, cria a associação
                        etiquetaItemReceitaService
                                .createEtiquetaItemReceita(etiquetaItemReceita);
                        // atualiza as Etiqueta na tela
                        generateEtiquetaNamesForItemReceitaRow(itemReceitaRow);
                    }
                }
            }
        }
    }

    /**
     * Desaplica uma Etiqueta à ItemReceita corrente.
     * 
     * @param itemReceitaRowList
     *            - lista de ItemReceita
     * @param codigoEtiqueta
     *            - código da Etiqueta a ser aplicada
     */
    public void unapplyEtiquetaForItemReceitaRowList(
            final List<ItemReceitaRow> itemReceitaRowList,
            final Long codigoEtiqueta) {
        Etiqueta etiqueta = findEtiquetaById(codigoEtiqueta);
        if (etiqueta != null) {
            // para cada ItemReceitaRow, verifica se está selecionada
            for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
                if (itemReceitaRow.getIsSelected()
                        && itemReceitaRow.getIsView()) {
                    // cria e popula um objeto EtiquetaItemReceitaId
                    EtiquetaItemReceitaId id = new EtiquetaItemReceitaId();
                    ItemReceita itemReceita = itemReceitaRow.getItemReceita();
                    id.setCodigoItemReceita(itemReceita.getCodigoItemReceita());
                    id.setCodigoEtiqueta(etiqueta.getCodigoEtiqueta());
                    // busca o objeto EtiquetaItemReceita a ser
                    // removido
                    EtiquetaItemReceita etiquetaItemReceita = etiquetaItemReceitaService
                            .findEtiquetaItemReceitaById(id);
                    if (etiquetaItemReceita != null) {
                        // se existir, remove a associação
                        etiquetaItemReceitaService
                                .removeEtiquetaItemReceita(etiquetaItemReceita);
                        // atualiza a lista de Etiqueta do ItemReceita
                        // corrente
                        ItemReceita itemReceitaDB = itemReceitaService
                                .findItemReceitaById(itemReceita
                                        .getCodigoItemReceita());
                        itemReceita.setEtiquetaItemReceitas(itemReceitaDB
                                .getEtiquetaItemReceitas());
                        // atualiza as Etiqueta na tela
                        generateEtiquetaNamesForItemReceitaRow(itemReceitaRow);
                    }
                }
            }
        }
    }

    /**
     * Aplica as Etiquetas vindas do MapaAlocacao aos ItemReceita.
     * 
     * @param itemReceitaRowList
     *            - lista de ItemReceitaRow
     */
    public void applyEtiquetaForItemReceitaRowList(
            final List<ItemReceitaRow> itemReceitaRowList) {
        // para cada itemReceitaRow
        for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
            // recupera o ItemReceita
            Long codigoItemReceita = itemReceitaRow.getItemReceita()
                    .getCodigoItemReceita();
            
            //verificação de segunrança
            if (codigoItemReceita != null) {
                ItemReceita itemReceita = itemReceitaService.
                    findItemReceitaById(codigoItemReceita);
            

                // recupera a lista de EtiquetaItemReceita
                List<EtiquetaItemReceita> etiquetaItemReceitaList = itemReceitaRow
                        .getEtiquetaItemReceitaList();
    
                // para cada EtiquetaItemReceita
                for (EtiquetaItemReceita etiquetaItemReceita : etiquetaItemReceitaList) {
                    // recupera o código da Etiqueta
                    Long codigoEtiqueta = etiquetaItemReceita.getId()
                            .getCodigoEtiqueta();
                    Etiqueta etiqueta = findEtiquetaById(codigoEtiqueta);
                    if (etiqueta != null) {
                        // cria e popula um novo objeto EtiquetaItemReceita
                        EtiquetaItemReceita etiquetaItemReceitaNw = new EtiquetaItemReceita();
                        // etiquetaItemReceitaNw.setItemReceita(itemReceita);
                        etiquetaItemReceitaNw.setEtiqueta(etiqueta);
    
                        EtiquetaItemReceitaId id = new EtiquetaItemReceitaId();
                        id.setCodigoItemReceita(itemReceita.getCodigoItemReceita());
                        id.setCodigoEtiqueta(etiqueta.getCodigoEtiqueta());
                        etiquetaItemReceitaNw.setId(id);
    
                        // verifica se já existe essa associação na base
                        // de dados
                        EtiquetaItemReceita etiqItemReceitaDB = etiquetaItemReceitaService
                                .findEtiquetaItemReceitaById(id);
                        if (etiqItemReceitaDB == null) {
                            // se não existir, cria a associação
                            etiquetaItemReceitaService
                                    .createEtiquetaItemReceita(etiquetaItemReceitaNw);
                        }
                    }
                }
            }
        }
    }

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */    
    public void updateEtiqueta(final Etiqueta entity) {        
        etiquetaDao.update(entity);  
    }

    /**
     * Busca etiquetas ativas por cliente.
     * 
     * @param codigoCliente
     *            id do Cliente.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<Etiqueta> findEtiquetaAtivaByCliente(final Long codigoCliente) {        
        return etiquetaDao.findAtivaByCliente(codigoCliente);
    }
    
    /**
     * Busca etiquetas que atendem ao criterio nome e cliente.
     * 
     * @param nomeEtiqueta
     *            nome da Etiqueta.
     * @param codigoCliente
     *            codigoCliente
     *             
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public Etiqueta findEtiquetaByNomeAndCliente(final String nomeEtiqueta,
            final Long codigoCliente) {
        List<Etiqueta> list = etiquetaDao.findByNomeAndCliente(nomeEtiqueta, codigoCliente);
        
        if (!list.isEmpty()) {
            return list.get(0);
        }
        
        return null;
    }
    
    /**
     * Adiciona etiqueta na tabela etiqueta relacionada ao cliente.
     * 
     * @param nomeEtiqueta nome da etiqueta a ser adicionada 
     * @param cliente cliente relacionado a etiqueta 
     *  
     * @return se sucesso, etiqueta adicionada. null, em caso de erro.
     */
    public Etiqueta addEtiqueta(final String nomeEtiqueta, final Cliente cliente) {
        Etiqueta etiquetaAdicionada = null;
        
        if (!StringUtils.isEmpty(nomeEtiqueta)) {            
            // busca etiqueta com mesmo nome e cliente
            Etiqueta etiquetaIgual = findEtiquetaByNomeAndCliente(nomeEtiqueta, cliente
                            .getCodigoCliente());           
            
            // se existir etiqueta com nome e codigo do cliente, seta seu status
            // para ativo
            if (etiquetaIgual != null) {
                etiquetaIgual.setIndicadorStatus(Etiqueta.ETIQUETA_ATIVO);
                updateEtiqueta(etiquetaIgual);
            
                etiquetaAdicionada = etiquetaIgual;
            } else {
                // cria etiqueta
                etiquetaAdicionada = new Etiqueta();
                etiquetaAdicionada.setNomeEtiqueta(nomeEtiqueta);
                etiquetaAdicionada.setCliente(cliente);                
                createEtiqueta(etiquetaAdicionada);
            }
        } 
        
        return etiquetaAdicionada;
    } 
    
    /**
     * Remove uma Etiqueta na tabela Etiqueta relacionado ao Cliente corrente.
     * 
     * @param nomeEtiqueta nome da etiqueta a ser removida
     * @param cliente cliente relacionado a etiqueta
     * 
     * @return true se sucesso na deleção; false, caso contrario. 
     */
    public boolean deleteEtiqueta(final String nomeEtiqueta, final Cliente cliente) {
        if (!StringUtils.isEmpty(nomeEtiqueta)) {             
            if (cliente != null) {
                // recupera a Etiqueta selecionada
                Etiqueta etiqueta = findEtiquetaByNomeAndCliente(nomeEtiqueta, 
                           cliente.getCodigoCliente());
            
                if (etiqueta != null) {                    
                        // seta seu status para inativo
                        etiqueta.setIndicadorStatus(Etiqueta.ETIQUETA_INATIVO);
                        updateEtiqueta(etiqueta);
                        
                        return true;
                }
            }            
            return false;
        }        
        return false;
    }
}