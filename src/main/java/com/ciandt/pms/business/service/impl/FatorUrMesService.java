package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IFatorUrMesService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.model.FatorUrMes;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.persistence.dao.IFatorUrMesDao;


/**
 * 
 * A classe FatorUrMesService proporciona as funcionalidades
 * da camada de serviço referente a entidade FatorUrMes.
 *
 * @since 09/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class FatorUrMesService implements IFatorUrMesService {

    /** instancia do serviço FaturaReceita. */
    @Autowired
    private IFatorUrMesDao fatorUrMesDao;
    
    /** Instancia do servico MapaAlocacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;
    
    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public FatorUrMes findFatorUrMesById(final Long id) {
        return fatorUrMesDao.findById(id);
    }
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createFatorUrMes(final FatorUrMes entity) {
        fatorUrMesDao.create(entity);
    }
    
    /**
     * realiza o update de uma entidade.
     * 
     * @param entity
     *            entidade a realizar o update.
     */
    public void updateFatorUrMes(final FatorUrMes entity) {
        fatorUrMesDao.update(entity);
    }
    
    /**
     * remove uma entidade.
     * 
     * @param entity
     *            entidade a realizar a remoção.
     */
    public void removeFatorUrMes(final FatorUrMes entity) {
        fatorUrMesDao.remove(entity);
    }
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param mapa
     *            entidade Mapaalocacao.
     * @param dataMes
     *            entidade do tipo java.util.Date.           
     * 
     * @return lista de entidades que atendem ao criterio de busca.
     */
    public FatorUrMes findFatorUrMesByMapaAndDate(
            final MapaAlocacao mapa, final Date dataMes) {
        return fatorUrMesDao.findByMapaAndDate(mapa, dataMes);
    }
    
    /**
     * Busca a maior data referente ao mapa de alocacao.
     * 
     * @param mapa que se deseja obter a maior data de alocação
     * 
     * @return retorna a marior data
     */
    public Date findFatorUrMesMaxDateByMapa(final MapaAlocacao mapa) {
        return fatorUrMesDao.findMaxDateByMapa(mapa);
    }
    
    /**
     * Busca a menor data referente ao mapa de alocacao.
     *  
     * @param mapa que se deseja obter a menor data de alocação 
     *  
     * @return retorna a menor data
     */
    public Date findFatorUrMesMinDateByMapa(final MapaAlocacao mapa) {
        return fatorUrMesDao.findMinDateByMapa(mapa);
    }
    
    /**
     * Busca todos os fatores de um mapa.
     * 
     * @param mapa
     *            que se deseja obter os fatores.
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    public List<FatorUrMes> findByMapa(final MapaAlocacao mapa) {
        return fatorUrMesDao.findByMapa(mapa);
    }
    
    /**
     * Busca todos os fatores de um mapa por periodo.
     * 
     * @param mapa - que se deseja obter os fatores.
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    public List<FatorUrMes> findFatorUrMesByMapaAndPeriod(final MapaAlocacao mapa) {
        
        return fatorUrMesDao.findByMapaAndPeriod(
                mapa, mapa.getDataInicioJanela(), 
                mapaAlocacaoService.getMapaAlocacaoEndDateWindow(mapa));
    }
    
    /**
     * Retorna os UR mes a partir da data passada por parametro.
     * 
     * @param mapa - que se deseja obter os fatores.
     * @param startDate - data inicial
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    public List<FatorUrMes> findFatorUrByMapaAndStartDate(
            final MapaAlocacao mapa, final Date startDate) {
        return fatorUrMesDao.findByMapaAndStartDate(mapa, startDate);
    }
    
    /**
     * Deleta os FatoresUrMes, que não estão dentro 
     * do periodo do mapa de alocação.
     * 
     * @param mapa
     *          MapaAlocacao que se deseja deletar os FatorUrMes.              
     */
    public void deleteFatorUrByMapaAndNotInRange(final MapaAlocacao mapa) {
        
        MapaAlocacao m = mapaAlocacaoService.
            findMapaAlocacaoById(mapa.getCodigoMapaAlocacao());
        
        List<FatorUrMes> resultList = 
            fatorUrMesDao.findByMapaAndNotInRange(m);
        
        for (FatorUrMes fator : resultList) {
            this.removeFatorUrMes(fator);
        }
    }
    
}
