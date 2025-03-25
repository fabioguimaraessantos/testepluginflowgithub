package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.HistoricoFatura;


/**
 * 
 * A classe IHistoricoFaturaService proporciona a interface de acesso para a
 * camada de serviço referente a entidade HistoricoFatura.
 * 
 * @since 10/05/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IHistoricoFaturaService {

	List<HistoricoFatura> findByCodigoFatura(Long codigoFatura);

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createHistoricoFatura(final HistoricoFatura entity);

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    @Transactional
    void removeHistoricoFatura(final HistoricoFatura entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    HistoricoFatura findHistoricoFaturaById(final Long entityId);

    /**
     * Cria um {@link HistoricoFatura} a partir de uma {@link Fatura}.
     *
     * @param fatura
     */
    void createHistoricoFatura(final Fatura fatura);
}