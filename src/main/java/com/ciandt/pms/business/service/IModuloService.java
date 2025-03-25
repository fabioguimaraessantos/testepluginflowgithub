package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Modulo;


/**
 * 
 * A classe IModuloService proporciona a interface de acesso da camada de
 * servi�o referente a entidade Modulo.
 * 
 * @since 26/11/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IModuloService {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Modulo> findModuloaAll();

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que sera atualizada.
     * 
     */
    @Transactional
    void updateModulo(final Modulo entity);

    /**
     * Realiza o update da data de fechamento de um modulo.
     * 
     * @param entity
     *            - entidade do tipo Modulo.
     * @param newCloseDate
     *            - nova data de fechamento.
     */
    @Transactional
    void updateCloseDate(final Modulo entity, final Date newCloseDate);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    Modulo findModuloById(final Long id);

    /**
     * Pega a data de fechamento do MapaAlocacao.
     * 
     * @return retorna a Data de Fechamento do MapaAlocacao.
     */
    Date getClosingDateMapaAlocacao();

    /**
     * Pega a data de fechamento da Despesa.
     * 
     * @return retorna a Data de Fechamento da Despesa.
     */
    Date getClosingDateExpense();

    /**
     * Pega a data de fechamento da Revenue.
     * 
     * @return retorna a Data de Fechamento da Revenue.
     */
    Date getClosingDateRevenue();

    /**
     * Pega a data de fechamento do History Lock.
     * 
     * @return retorna a Data de Fechamento do History Lock.
     */
    Date getClosingDateHistoryLock();

    /**
     * Verifica se {@code date} � maior que o HistoryLock sem levar em considera��o o dia.
     *
     * @param date
     * @return true se {@code date} � maior que HistoryLock e false se {@code date} � menor que HistoryLock.
     */
    Boolean dateAfterHistoryLock(Date date);

    /**
     * Pega a data de fechamento do ChargeBack.
     * 
     * @return retorna a Data de Fechamento do ChargeBack.
     */
    Date getClosingDateChargeBack();

    /**
     * Valida��o do HistoryLock. Caso startDate (data de in�cio da vig�ncia)
     * seja maior do que a data History Lock, a vig�ncia � v�lida e pode ser
     * adicionada ou removida. Caso contr�rio, a vig�ncia n�o pode ser
     * adicionada nem removida.
     * 
     * @param startDate
     *            - data de in�cio da vig�ncia
     * @param showMsgError
     *            - mostra mensagem de erro caso startDate n�o seja v�lido
     * @param isCurrentMonthMsg
     *
     * @return true se startDate for maior do que HistoryLock. false caso
     *         contr�rio
     */
    Boolean verifyHistoryLock(final Date startDate, final Boolean showMsgError, Boolean isCurrentMonthMsg);

    /**
     * Pega a data de fechamento do M�dulo de horas extras e plant�o.
     * 
     * @return retorna a Data de Fechamento.
     */
    Date getClosingDateBancoHoras();

    /**
     * Retorna a data de fechamento do M�dulo de International Revenue.
     * 
     * @return Data de fechamento.
     */
    Date getClosingDateInternationalRevenue();

    /**
     * Retorna se a {@code date} � maior que closing date de receita nacional ou internacional.
     *
     * @param date
     * @param nationalityIndicator
     * 				- "NAC" for Nacional and "INT" for Interna
     * @return
     */
    Boolean isDateAfterRevenueClosingDate(Date date, String nationalityIndicator);
}