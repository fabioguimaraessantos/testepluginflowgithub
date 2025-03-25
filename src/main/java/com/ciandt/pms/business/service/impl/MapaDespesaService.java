/*
 * @(#) MapaDespesaService.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IDespesaMesService;
import com.ciandt.pms.business.service.IFatorUrMesService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IMapaDespesaService;
import com.ciandt.pms.business.service.ITipoDespesaService;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DespesaMes;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.TipoDespesa;
import com.ciandt.pms.model.vo.MapaDespesaRow;
import com.ciandt.pms.persistence.dao.IMapaDespesaDao;
import com.ciandt.pms.util.DateUtil;


/**
 * A classe MapaDespesaService proporciona as funcionalidades de ... para ...
 *
 * @since 28/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class MapaDespesaService implements IMapaDespesaService {

    /** Instancia DAO do tipo MapaDespesaDao. */
    @Autowired
    private IMapaDespesaDao mapDespDao;
    
    /** Instancia do Serviço do tipo DespesaMesService. */
    @Autowired
    private IDespesaMesService despesaMesService;
    
    /** Instancia do Serviço do tipo TipoDespesaService. */
    @Autowired
    private ITipoDespesaService tipoDespesaService;
    
    /** Instancia do Serviço do tipo TipoDespesaService. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;
    
    /** Instancia do Serviço do tipo FatorUrMes. */
    @Autowired
    private IFatorUrMesService fatorUrService;
    
    /** Instancia do Serviço do tipo MapaAlocacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;
    
    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    public Boolean createMapaDespesa(final MapaDespesa entity) {
        mapDespDao.create(entity);
        
        return true;
    }
    
    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public MapaDespesa findMapaDespesaById(final Long entityId) {
        return mapDespDao.findById(entityId);
    }

    /**
     * Remove a entidade passada por parametro, exclusao na tela de abas,
     * verifica as Alocacao e remove os ItemTabelaPreco.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @return retorna true se sucesso senao false
     */
    public boolean removeMapaDespesa(final MapaDespesa entity) {

        mapDespDao.remove(findMapaDespesaById(
                entity.getCodigoMapaDespesa()));
        
        return true;
    }

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @return true se update com sucesso, caso contrario retorna false
     */
    public Boolean updateMapaDespesa(final MapaDespesa entity) {
        mapDespDao.update(entity);
        
        return true;
    }
    
    /**
     * Realiza o update da lista de despesas da entidade MapaDespesa.
     * 
     * @param entity - Entidade do tipo MapaDespesa
     * @param rowList - lista de despesas 
     * 
     * @return retorna true se update realizado com sucesso, caso contrario false.
     */
    public Boolean saveMapaDespesa(final MapaDespesa entity,
            final List<MapaDespesaRow> rowList) {
        
        MapaDespesa mapaDesp = entity;
        
        if (entity.getCodigoMapaDespesa() != null) {
            mapaDesp = this.findMapaDespesaById(
                entity.getCodigoMapaDespesa());
        }
        
        List<DespesaMes> despesaMeses = mapaDesp.getDespesaMeses();
        despesaMeses.clear();
        despesaMeses.addAll(generateDespesaMesList(mapaDesp, rowList));
        
        if (entity.getCodigoMapaDespesa() != null) {
            return updateMapaDespesa(mapaDesp);
        } else {
            entity.setContratoPratica(
                    contratoPraticaService.findContratoPraticaById(
                    entity.getContratoPratica().getCodigoContratoPratica()));
            return createMapaDespesa(entity);
        }   
    }
    
    /**
     * Busca um mapa de despesa pelo contrato pratica.
     * 
     * @param cp - entidade do tipo ContratoPratica
     * 
     * @return retorna uma entidade do tipo MapaDespesa
     */
    public MapaDespesa findMapaDespesaByContratoPratica(final ContratoPratica cp) {
        return mapDespDao.findByContratoPratica(cp);
    }
    
    /**
     * Prepara o mapa de despesa, para se criado.
     * 
     * @param cp - entidade do tipo ContratoPratica
     * @param startDate - Inicio da vigencia do mapa de despesas
     * @param endDate - Fom da vigencia do mapa de depesas
     * 
     * @return retorna uma entidade do tipo MapaDespesa
     */
    public List<MapaDespesaRow> prepareCreate(final ContratoPratica cp, 
            final Date startDate, final Date endDate) {
        //pega o contrato pratica
        ContratoPratica contratoPratica = contratoPraticaService.
            findContratoPraticaById(cp.getCodigoContratoPratica());
        //cria a lista de datas referente a vigencia
        List<Date> dateList = DateUtil.getValidityDateList(startDate, endDate);
        
        MapaDespesa mapDesp = new MapaDespesa();
        mapDesp.setContratoPratica(contratoPratica);
        mapDesp.setNomeMapaDespesa(contratoPratica.getNomeContratoPratica());
        
        return prepareCreateDespesaMes(mapDesp, dateList);
    }
    
    /**
     * Prepara a lista de DepesaMes.
     * 
     * @param mapDesp - entidade do tipo MapaDespesa
     * @param dateList - Lista com a vigencia dos meses
     * 
     * @return retorna a lista de DespesaMes
     */
    private List<MapaDespesaRow> prepareCreateDespesaMes(
            final MapaDespesa mapDesp, final List<Date> dateList) {
        
        DespesaMes despMesCredito, despMesDebito;
        List<TipoDespesa> tipoDespList = tipoDespesaService.findTipoDespesaAllActive();
        List<DespesaMes> despMesCreditoList, despMesDebitoList;
        List<Double> valorReembolsoList;
        
        List<MapaDespesaRow> mapaDespesaRowList = new ArrayList<MapaDespesaRow>();
        MapaDespesaRow row;
        
        for (TipoDespesa tipoDespesa : tipoDespList) {
            
            despMesCreditoList = new ArrayList<DespesaMes>();
            despMesDebitoList = new ArrayList<DespesaMes>();
            valorReembolsoList = new ArrayList<Double>();
            
            String []units = getUnitsByTipoDespesa(tipoDespesa);
            
            for (Date date : dateList) {    
                // Cria a despesa (do tipo debito) 
                despMesDebito = new DespesaMes();
                despMesDebito.setDataMes(date);
                despMesDebito.setIndicadorDebitoCredito(
                        Constants.DESPESA_INDICADOR_DEBITO);
                despMesDebito.setIndicadorUnidade(units[0]);
                despMesDebito.setMapaDespesa(mapDesp);
                despMesDebito.setTipoDespesa(tipoDespesa);
                despMesDebito.setValorDespesa(BigDecimal.valueOf(0.0));
                //adiciona na lista a despesa de debito
                despMesDebitoList.add(despMesDebito);
                
                //Cria o reemboldo (despesa do tipo credito)
                despMesCredito = new DespesaMes();
                despMesCredito.setDataMes(date);
                despMesCredito.setIndicadorDebitoCredito(
                        Constants.DESPESA_INDICADOR_CREDITO);
                despMesCredito.setIndicadorUnidade(units[1]);
                despMesCredito.setMapaDespesa(mapDesp);
                despMesCredito.setTipoDespesa(tipoDespesa);
                despMesCredito.setValorDespesa(BigDecimal.valueOf(0.0));
                //adiciona na lista a despesa de credito
                despMesCreditoList.add(despMesCredito);
                
                //adiciona um novo elemento na lista com valor zero.
                valorReembolsoList.add(Double.valueOf(0.0));
            }
            
            row = new MapaDespesaRow();
            row.setDespesaCreditoList(despMesCreditoList);
            row.setDespesaDebitoList(despMesDebitoList);
            row.setTipoDespesa(tipoDespesa);
            row.setValorReembolsoList(valorReembolsoList);
            row.setUnidadeDespesa(units[0]);
            row.setUnidadeReembolso(units[1]);
            
            mapaDespesaRowList.add(row);
        }
        
        return mapaDespesaRowList;
    }
    
    /**
     * Gera um map com DespesaMes a partir de uma lista. 
     * 
     * @param despMesList lista com elementos DespesaMes.
     * 
     * @return retorna um mapa Map com DespesaMes
     */
    private Map<String, DespesaMes> generateDespesaMap(
            final List<DespesaMes> despMesList) {
        
        Map<String, DespesaMes> resultMap = new Hashtable<String, DespesaMes>();
        
        for (DespesaMes despesaMes : despMesList) {
            resultMap.put(getDespesaMesKey(despesaMes), despesaMes);
        }
        
        return resultMap;
    }
    
    /**
     * Gera a chave da entidade DespesaMes.
     * 
     * @param despesaMes entidade DespesaMes
     * 
     * @return retorna uma string representando a chave.
     */
    private String getDespesaMesKey(final DespesaMes despesaMes) {
        return "" + despesaMes.getDataMes().getTime()
        + despesaMes.getMapaDespesa().getCodigoMapaDespesa()
        + despesaMes.getTipoDespesa().getCodigoTipoDespesa()
        + despesaMes.getIndicadorDebitoCredito();
    }
    
    /**
     * Prepara a lista de DepesaMes.
     * 
     * @param mapDesp - entidade do tipo MapaDespesa
     * 
     * @return retorna a lista de DespesaMes
     */
    private List<MapaDespesaRow> prepareUpdateDespesaMes(final MapaDespesa mapDesp) {
        
        List<TipoDespesa> tipoDespList = tipoDespesaService.findTipoDespesaAllActive();
        List<DespesaMes> despMesCreditoList, despMesDebitoList;
        List<Double> valorReembolsoList;
        Double totalReembolso = null, totalDespesa = null;
        
        List<MapaDespesaRow> mapaDespesaRowList = new ArrayList<MapaDespesaRow>();
        MapaDespesaRow row;
        
        //realiza a iteração sobre todos os tipos de despesas
        for (TipoDespesa tipoDespesa : tipoDespList) {
            despMesCreditoList = new ArrayList<DespesaMes>();
            despMesDebitoList = new ArrayList<DespesaMes>();
            valorReembolsoList = new ArrayList<Double>(); 
            totalReembolso = Double.valueOf(0.0);
            totalDespesa = Double.valueOf(0.0);
            
            //pega a lista de despesas referente ao mapa e tipo de despesa
            List<DespesaMes> despMesList = despesaMesService.
                findDespesaByMapaDespesaAndTipo(mapDesp, tipoDespesa);
            //cria um mapa com as DespesaMes
            Map<String, DespesaMes> despesaMap = generateDespesaMap(despMesList);
            String unidadeDespesa = Constants.DESPESA_INDICADOR_UNIDADE_VALOR_ABSOLUTO;
            String unidadeReembolso = Constants.DESPESA_INDICADOR_UNIDADE_VALOR_ABSOLUTO;
            
            //cria a lista de meses(datas) do mapa de despesa
            List<Date> dateList = DateUtil.getValidityDateList(
                    despesaMesService.findDespesaMesMinDateByMapa(mapDesp),
                    despesaMesService.findDespesaMesMaxDateByMapa(mapDesp));
            
            DespesaMes despesaMes;
            //realiza a iteração sobre todos os meses
            for (Date monthDate : dateList) {
                String []units = getUnitsByTipoDespesa(tipoDespesa);
                
                despesaMes = new DespesaMes();
                despesaMes.setDataMes(monthDate);
                despesaMes.setMapaDespesa(mapDesp);
                despesaMes.setTipoDespesa(tipoDespesa);
                despesaMes.setValorDespesa(BigDecimal.valueOf(0.0));
                despesaMes.setIndicadorUnidade(units[0]);
                despesaMes.setIndicadorDebitoCredito(
                        Constants.DESPESA_INDICADOR_CREDITO);
                
                String despesaMesKey = getDespesaMesKey(despesaMes);
                //caso exista este tipo de despesa para o mes da iteração
                if (despesaMap.containsKey(despesaMesKey)) {
                    despesaMes = despesaMap.get(despesaMesKey);

                    //pega a despesa de debito referente a despesa de crédito
                    DespesaMes despMesDebito = despesaMesService.findDespesaMesDebitoByMapaTipoAndDate(
                            despesaMes.getMapaDespesa(), despesaMes.getTipoDespesa(), despesaMes.getDataMes());
                    
                    Double valorReembolso;
                    //se unidade for percentual
                    if (Constants.DESPESA_INDICADOR_UNIDADE_PERCENTUAL.equals(
                            despesaMes.getIndicadorUnidade())) {
                        
                        valorReembolso = despMesDebito.getValorDespesa().doubleValue()
                            * despesaMes.getValorDespesa().doubleValue();
                        
                    //se for valor absoluto    
                    } else {
                        valorReembolso = despesaMes.getValorDespesa().doubleValue();
                    }
                    
                    //seta os totais de Despesa de Debito
                    despMesDebitoList.add(despMesDebito);
                    totalDespesa += despMesDebito.getValorDespesa().doubleValue();
                    unidadeDespesa = despMesDebito.getIndicadorUnidade();
                    //seta os totais de Despesa de Reembolso (Crédito)
                    valorReembolsoList.add(valorReembolso);
                    totalReembolso += valorReembolso;
                    unidadeReembolso = despesaMes.getIndicadorUnidade();

                //caso não exista o tipo de despesa para o mes corrente da iteração.                        
                } else {
                    Double despValue = Double.valueOf(0.0);
                    
                    //seta os totais de despesa de Reembolso (Crédito)
                    valorReembolsoList.add(despValue);
                    totalReembolso += despValue;
                    unidadeReembolso = despesaMes.getIndicadorUnidade();
                    
                    //cria uma despesa de debito
                    DespesaMes despMesDebito = new DespesaMes();
                    despMesDebito.setIndicadorUnidade(units[1]);
                    despMesDebito.setDataMes(monthDate);
                    despMesDebito.setMapaDespesa(mapDesp);
                    despMesDebito.setTipoDespesa(tipoDespesa);
                    despMesDebito.setValorDespesa(BigDecimal.valueOf(0.0));
                    despMesDebito.setIndicadorDebitoCredito(
                            Constants.DESPESA_INDICADOR_DEBITO);
                    
                    //seta os totais de despesa de Debito
                    despMesDebitoList.add(despMesDebito);
                    unidadeDespesa = despMesDebito.getIndicadorUnidade(); 
                    totalDespesa += despValue;
                }
                
                despMesCreditoList.add(despesaMes);
                
            }
            
            //Cria a linha do mapa de despesas
            row = new MapaDespesaRow();
            row.setDespesaCreditoList(despMesCreditoList);
            row.setDespesaDebitoList(despMesDebitoList);
            row.setTipoDespesa(tipoDespesa);
            
            row.setValorReembolsoList(valorReembolsoList);
            row.setUnidadeDespesa(unidadeDespesa);
            row.setUnidadeReembolso(unidadeReembolso);
            
            row.setTotalDespesa(totalDespesa);
            row.setTotalReembolso(totalReembolso);
            
            mapaDespesaRowList.add(row);
            
        }
        
        return mapaDespesaRowList;
    }
    
    /**
     * Altera a vigencia do mapa de despesa.
     * 
     * @param mapaDesp - entidade do tipo MapaDespesa 
     * @param startDate - data inicio
     * @param endDate - data fim
     * 
     * @return retorna uma lista com a linhas do mapa de despesa
     */
    public List<MapaDespesaRow> mapaDespesachangePeriod(final MapaDespesa mapaDesp,
            final Date startDate, final Date endDate) {
        
        MapaDespesa md = this.findMapaDespesaById(
                mapaDesp.getCodigoMapaDespesa());
        
        List<DespesaMes> despMesList = despesaMesService.
            findDespesaMesByMapaAndNotInRange(md, startDate, endDate);
        
        for (DespesaMes despesaMes : despMesList) {
            despesaMesService.removeDespesa(despesaMes);
        }
        
        List<Date> dateList = DateUtil.getValidityDateList(startDate, endDate);
        List<TipoDespesa> tipoDespList = tipoDespesaService.findTipoDespesaAllActive();
        
        for (Date dateMonth : dateList) {
            
            for (TipoDespesa tipo  : tipoDespList) {
                String[] units = this.getUnitsByTipoDespesa(tipo);
                
                List<DespesaMes> despesaMesList = 
                    despesaMesService.findDespesaMesByMapaTipoAndDate(md, tipo, dateMonth);
                if (despesaMesList.isEmpty()) {
                    //cria a despesa
                    DespesaMes despMes = new DespesaMes();
                    despMes.setDataMes(dateMonth);
                    despMes.setIndicadorDebitoCredito(
                            Constants.DESPESA_INDICADOR_DEBITO);
                    despMes.setIndicadorUnidade(units[0]);
                    despMes.setMapaDespesa(md);
                    despMes.setTipoDespesa(tipo);
                    despMes.setValorDespesa(BigDecimal.valueOf(0.0));
                    
                    despesaMesService.createDespesa(despMes);
                    
                    //cria o reembolso
                    despMes = new DespesaMes();
                    despMes.setDataMes(dateMonth);
                    despMes.setIndicadorDebitoCredito(
                            Constants.DESPESA_INDICADOR_CREDITO);
                    despMes.setIndicadorUnidade(units[1]);
                    despMes.setMapaDespesa(md);
                    despMes.setTipoDespesa(tipo);
                    despMes.setValorDespesa(BigDecimal.valueOf(0.0));
                    
                    despesaMesService.createDespesa(despMes);
                }
            }
        }
        
        return prepareManageMapaDespesa(md);
        
    }
    
    /**
     * Gera a lista de despesa a partir de uma lista de MapaDespesaRow.
     * 
     * @param mapaDespesa - entidade do tipo MapaDespesa
     * 
     * @param rowList - lista de MapaDespesaRow.
     * 
     * @return retorna uma lista de DespesaMes.
     */
    private List<DespesaMes> generateDespesaMesList(
            final MapaDespesa mapaDespesa, final List<MapaDespesaRow> rowList) {
        
        List<DespesaMes> resultList = new ArrayList<DespesaMes>();
        DespesaMes despAux;
        
        for (MapaDespesaRow row : rowList) {
            List<DespesaMes> despesaCreditoList = row.getDespesaCreditoList();
            for (DespesaMes despesaMes : despesaCreditoList) {
                despAux = despesaMes;
                
                if (despAux.getValorDespesa() == null) {
                    despAux.setValorDespesa(BigDecimal.valueOf(0.0));
                }
                if (despesaMes.getCodigoDespesaMes() != null) {
                    despAux = despesaMesService.findDespesaById(
                            despesaMes.getCodigoDespesaMes());
                    
                    despAux.setValorDespesa(despesaMes.getValorDespesa());
                }
                despAux.setMapaDespesa(mapaDespesa);
                
                
                resultList.add(despAux);
            }
            
            List<DespesaMes> despesaDebito = row.getDespesaDebitoList();
            for (DespesaMes despesaMes : despesaDebito) {
                despAux = despesaMes;
                
                if (despesaMes.getCodigoDespesaMes() != null) {
                    despAux = despesaMesService.findDespesaById(
                            despesaMes.getCodigoDespesaMes());
                    
                    despAux.setValorDespesa(despesaMes.getValorDespesa());
                }
                despAux.setMapaDespesa(mapaDespesa);
                
                resultList.add(despAux);
            }
        }
        
        return resultList;
    }
    
    /**
     * Prepara a criação de uma mapa de custo a partir de
     * um mapa de alocação.
     * 
     * @param mapaAlocacao - entidade do tipo MapaAlocacao.
     * 
     * @return retorna uma lista com as despesas a serem criadas.
     */
    public List<MapaDespesaRow> prepareCreateDespesaMesFromMapa(
            final MapaAlocacao mapaAlocacao) {
        
        MapaAlocacao ma = mapaAlocacaoService.findMapaAlocacaoById(
                mapaAlocacao.getCodigoMapaAlocacao());
        
        Date startDate = fatorUrService.findFatorUrMesMinDateByMapa(mapaAlocacao);
        Date endDate = fatorUrService.findFatorUrMesMaxDateByMapa(mapaAlocacao);
        
        return prepareCreate(ma.getContratoPratica(), startDate, endDate);

    }
    
    /**
     * Prepara o MapaDespesa para o gerenciamento.
     * 
     * @param mapDesp - entidade do tipo MapaDespesa
     * 
     * @return retorna a lista preparada para edição.
     */
    public List<MapaDespesaRow> prepareManageMapaDespesa(final MapaDespesa mapDesp) {
        MapaDespesa md = findMapaDespesaById(
                mapDesp.getCodigoMapaDespesa());
        
        return prepareUpdateDespesaMes(md);
    }
    
    /**
     * Prepara o MapaDespesa para o gerenciamento.
     * 
     * @param cp - entidade do tipo ContratoPratica
     * 
     * @return retorna a lista preparada para edição.
     */
    public List<MapaDespesaRow> prepareManageMapaDespesa(final ContratoPratica cp) {
        
        MapaDespesa mapaDesp = findMapaDespesaByContratoPratica(cp);
        
        return prepareManageMapaDespesa(mapaDesp);
    }
    
    /**
     * Pega a moeda do contratoPratica.
     * 
     * @param contratoPratica - entidade do tipo ContratoPratica
     * 
     * @return retorna uma moeda
     */
    public Moeda getMoedaMapaDespesa(final ContratoPratica contratoPratica) {
        
        ContratoPratica cp = contratoPraticaService.findContratoPraticaById(
                contratoPratica.getCodigoContratoPratica());
        
        // TODO: na modelagem nova, considerar a multiplicidade da Moeda quando
		// for reimplementar a tela de MapaDespesa
        //return cp.getMoeda();
        return new Moeda();
    }
    
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
    public List<MapaDespesa> findMapaDespesaByFilter(final MapaDespesa filter,
            final Cliente cli, final Msa msa, 
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro) {
        
        return mapDespDao.findByFilter(filter, cli, msa, natureza, centroLucro);
    }
    
    /**
     * Verifica se já exite mapa de despesa para um contrato pratica.
     * 
     * @param contratoPratica - entidade do tipo ContratoPratica
     * 
     * @return retorna true se existe, caso contrario false.
     */
    public boolean existsMapaDespesa(
            final ContratoPratica contratoPratica) {
        
        MapaDespesa mapaDesp = 
            findMapaDespesaByContratoPratica(contratoPratica);
        
        return mapaDesp != null;
            
    }
    
    /**
     * Pega qual o tipo de unidade que sera inserido as despesas.
     * 
     * @param tipoDespesa - entidade do tipo TipoDespesa.
     * 
     * @return retorna um array de string com 2 posições.
     *   Posição 0 - unidade da despesa de credito
     *   Posição 1 - unidade da despesa de debito
     */
    private String[] getUnitsByTipoDespesa(final TipoDespesa tipoDespesa) {
        
        String split = systemProperties.getProperty(
                Constants.DESPESA_UNIDADE_TIPO_DESP 
                + tipoDespesa.getCodigoTipoDespesa());
        
        if (split != null) {
            return split.split(":");
        }
        
        return systemProperties.getProperty(
                Constants.DESPESA_UNIDADE_DEFAULT).split(":");
    }
}
