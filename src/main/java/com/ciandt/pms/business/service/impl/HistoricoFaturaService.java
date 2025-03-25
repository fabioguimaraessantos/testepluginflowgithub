/**
 * Classe HistoricoFaturaService - Implementação do serviço da HistoricoFatura
 */
package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IHistoricoFaturaService;
import com.ciandt.pms.business.service.IItemFaturaService;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.HistoricoFatura;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.persistence.dao.IHistoricoFaturaDao;
import com.ciandt.pms.util.LoginUtil;


/**
 * A classe HistoricoFaturaService proporciona as funcionalidades de serviço
 * para a entidade HistoricoFatura.
 * 
 * @since 10/05/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class HistoricoFaturaService implements IHistoricoFaturaService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IHistoricoFaturaDao hiFaDao;

    /** Instancia do Serviço da entidade ItemFaturaService. */
    @Autowired
    private IItemFaturaService itemFaturaService;

    public List<HistoricoFatura> findByCodigoFatura(Long codigoFatura) {
    	return hiFaDao.findByCodigoFatura(codigoFatura);
    }

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createHistoricoFatura(final HistoricoFatura entity) {
        hiFaDao.create(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    public void removeHistoricoFatura(final HistoricoFatura entity) {
        hiFaDao.remove(entity);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public HistoricoFatura findHistoricoFaturaById(final Long entityId) {
        return hiFaDao.findById(entityId);
    }
    /**
     * Cria um {@link HistoricoFatura} a partir de uma {@link Fatura}.
     *
     * @param fatura
     */
    @Transactional
    public void createHistoricoFatura(final Fatura fatura) {

    	HistoricoFatura historicoFatura = new HistoricoFatura();
    	historicoFatura.setCodigoAutor(LoginUtil.getLoggedUsername());
    	historicoFatura.setDataAlteracao(new Date());
    	historicoFatura.setDataPrevisao(fatura.getDataPrevisao());
    	historicoFatura.setFatura(fatura);
//    	historicoFatura.setIndicadorStatusAnterior(indicadorStatusAnterior);
    	historicoFatura.setIndicadorStatusAtual(fatura.getIndicadorStatus());
    	historicoFatura.setMoeda(fatura.getMoeda());

    	List<ItemFatura> itensFatura = itemFaturaService.findItemFaturaByFatura(fatura);
    	BigDecimal valorFatura = BigDecimal.ZERO;

    	for (ItemFatura itemFatura : itensFatura) {
			valorFatura = valorFatura.add(itemFatura.getValorItem());
		}
    	
    	historicoFatura.setValorFatura(valorFatura);

    	this.createHistoricoFatura(historicoFatura);
    }
}