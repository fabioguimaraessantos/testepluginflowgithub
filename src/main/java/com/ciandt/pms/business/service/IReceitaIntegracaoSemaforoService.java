package com.ciandt.pms.business.service;

import com.ciandt.pms.model.ReceitaIntegracaoSemaforo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * A classe IReceitaDealFiscalService proporciona a interface de acesso a camada
 * de serviço referente a entidade ReceitaDealFiscal.
 *
 * @since 04/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IReceitaIntegracaoSemaforoService {


    @Transactional
    void createReceitaIntegracaoSemaforo(final ReceitaIntegracaoSemaforo entity);

    List<ReceitaIntegracaoSemaforo> findByReceitaDealFiscal(final Long codigoReceitaDealFiscal);
}