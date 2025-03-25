package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IAlocacaoPeriodoService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IAlocacaoPeriodoDao;


/**
 * 
 * A classe AlocacaoPeriodoService proporciona as funcionalidades de serviço
 * para a entidade AlocacaoPeriodo.
 * 
 * @since 19/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class AlocacaoPeriodoService implements IAlocacaoPeriodoService {

    /** DAO da entidade PeriodoAlocacao. */
    @Autowired
    private IAlocacaoPeriodoDao alocacaoPeriodoDao;

    /** Serviço da entidade MapaAlocacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createAlocacaoPeriodo(final AlocacaoPeriodo entity) {
        if (BigDecimal.ZERO.compareTo(entity.getPercentualAlocacaoPeriodo()) != 0 
                &&  entity.getCodigoAlocacaoPeriodo() == null) {
            alocacaoPeriodoDao.create(entity);
        }
    }

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateAlocacaoPeriodo(final AlocacaoPeriodo entity) {
        if (entity.getCodigoAlocacaoPeriodo() != null) {
            AlocacaoPeriodo al = findAlocacaoPeriodoById(entity.getCodigoAlocacaoPeriodo());
            
            if (al.getCustoRealizados().isEmpty()) {   
                if (BigDecimal.ZERO.compareTo(entity.getPercentualAlocacaoPeriodo()) != 0) {
                    alocacaoPeriodoDao.update(entity);
                } else {
                    removeAlocacaoPeriodo(entity);    
                }
            }
        }
    }

    /**
     * Remove a entidade passado por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    public void removeAlocacaoPeriodo(final AlocacaoPeriodo entity) {
        if (entity.getCodigoAlocacaoPeriodo() != null) {
            AlocacaoPeriodo al = findAlocacaoPeriodoById(entity.getCodigoAlocacaoPeriodo());
            
            if (al.getCustoRealizados().isEmpty()) {
                if (al != null) {
                    alocacaoPeriodoDao.remove(al);
                }
                
                entity.setCodigoAlocacaoPeriodo(null);
            }
        }
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public AlocacaoPeriodo findAlocacaoPeriodoById(final Long id) {
        return alocacaoPeriodoDao.findById(id);
    }

    /**
     * Busca a maior data referente ao mapa de alocacao.
     * 
     * @param mapa
     *            que se deseja obter a maior data de alocação
     * 
     * @return retorna a marior data
     */
    public Date findMaxDateByMapa(final MapaAlocacao mapa) {
        return alocacaoPeriodoDao.findMaxDateByMapa(mapa);
    }

    /**
     * Busca a menor data referente ao mapa de alocacao.
     * 
     * @param mapa
     *            que se deseja obter a menor data de alocação
     * 
     * @return retorna a menor data
     */
    public Date findMinDateByMapa(final MapaAlocacao mapa) {
        return alocacaoPeriodoDao.findMinDateByMapa(mapa);
    }

    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param alocacao
     *            Alocacao utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna a AlocacaoPeriodo.
     */
    public AlocacaoPeriodo findAlocacaoPeriodoByAlocacaoAndDate(
            final Alocacao alocacao, final Date dataMes) {
        return alocacaoPeriodoDao.findByAlocacaoAndDate(alocacao, dataMes);
    }

    public Boolean findAlocacaoPeriodoGreaterZeroByContratoPraticaAndDateAndRecurso(final Long codigoContratoPratica, final Long codigoRecurso, final Date dataMes){

        return alocacaoPeriodoDao.findAlocacaoPeriodoGreaterZeroByContratoPraticaAndDateAndRecurso(codigoContratoPratica, codigoRecurso, dataMes).size() > 0;
    }

    /**
     * Busca pelo Recurso e Data Mês.
     * 
     * @param recurso
     *            Recurso utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    public List<AlocacaoPeriodo> findAlocacaoPeriodoByRecursoAndDate(
            final Recurso recurso, final Date dataMes) {
        return alocacaoPeriodoDao.findByRecursoAndDate(recurso, dataMes);
    }

    /**
     * Deleta as alocações periodos, que não estão dentro do periodo passado por
     * aprametro.
     * 
     * @param mapa
     *            MapaAlocacao que se deseja deletar as AlocacaoPeriodo.
     * 
     */
    public void deleteAlocacaoPeriodoByMapaAndNotInRange(final MapaAlocacao mapa) {

        MapaAlocacao m = mapaAlocacaoService.findMapaAlocacaoById(mapa
                .getCodigoMapaAlocacao());

        List<AlocacaoPeriodo> resultList = alocacaoPeriodoDao
                .findByMapaAndNotInRange(m, m.getDataInicio(), m.getDataFim());

        for (AlocacaoPeriodo ap : resultList) {
            this.removeAlocacaoPeriodo(ap);
        }
    }

    /**
     * Busca as alocações periodos de uma alocacao.
     * 
     * @param alocacao
     *            - entidade do tipo Alocacao.
     * 
     * @return retorna uma lista de AlocacaoPeriodo.
     */
    public List<AlocacaoPeriodo> findAlocacaoPeriodoByAlocacao(
            final Alocacao alocacao) {
        return alocacaoPeriodoDao.findByAlocacao(alocacao);
    }

    /**
     * Busca pela Alocacao e Data Mês.
     * 
     * @param recurso
     *            Recurso utilizado na busca.
     * @param dataMes
     *            Data referente ao mes da alocação.
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    public List<AlocacaoPeriodo> findAlocPerByMaxDateAndRecurso(
            final Recurso recurso, final Date dataMes) {
        return alocacaoPeriodoDao.findByMaxDateAndRecurso(recurso, dataMes);
    }
    
    /**
     * Busca pela Alocacao e periodo.
     * 
     * @param alocacao
     *            Alucacao utilizado na busca.
     * @param startDate
     *            Data inicio do periodo.
     * @param endDate
     *            Data fim do periodo.
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    public List<AlocacaoPeriodo> findAlocPerByAlocacaoAndPeriod(
            final Alocacao alocacao, final Date startDate, final Date endDate) {
        return alocacaoPeriodoDao.findByAlocacaoAndPeriod(alocacao, startDate, endDate);
    }

    /**
     * Busca pela Pessoa e MapaAlocacao
     * 
     * @param recurso
     *            Recurso utilizada na busca
     * @param mapaAlocacao
     *            Escopo da busca
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    @Override
    public List<AlocacaoPeriodo> findByRecursoAndMapaAlocacao(Recurso recurso,
    		MapaAlocacao mapaAlocacao) {
		return alocacaoPeriodoDao.findByMapaAlocacaoAndRecurso(mapaAlocacao,
				recurso);
    }

    /**
     * Busca pela Pessoa e MapaAlocacao
     * 
     * @param pessoa
     *            Pessoa utilizada na busca
     * @param mapaAlocacao
     *            Escopo da busca
     * 
     * @return retorna uma lista AlocacaoPeriodo.
     */
    @Override
	public List<AlocacaoPeriodo> findByPessoaAndMapaAlocacao(Pessoa pessoa,
			MapaAlocacao mapaAlocacao) {
		return alocacaoPeriodoDao.findByMapaAlocacaoAndRecurso(mapaAlocacao,
				pessoa.getRecurso());
	}

    @Override
    public List<AlocacaoPeriodo> findByPerfilVendidoAndClosingDate (
        final PerfilVendido perfilVendido, final Date closingMapDate) {
        return alocacaoPeriodoDao.findByPerfilVendidoAndClosingDate(perfilVendido, closingMapDate);
    }

    @Override
    public List<AlocacaoPeriodo> findByResourceCodeInAndMonth(final Set<Long> resourceCodes, final Date month) {
        return alocacaoPeriodoDao.findByResourceCodeInAndMonth(resourceCodes, month);
    }
}