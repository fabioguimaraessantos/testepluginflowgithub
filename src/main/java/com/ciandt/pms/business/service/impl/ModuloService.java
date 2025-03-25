package com.ciandt.pms.business.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.persistence.dao.IModuloDao;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe ModuloService proporciona as funcionalidades da camada de serviço
 * referente a entidade Modulo.
 * 
 * @since 26/11/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ModuloService implements IModuloService {

    /** Instancia do DAO da entidade TabelaPreco. */
    @Autowired
    private IModuloDao dao;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<Modulo> findModuloaAll() {
        return dao.findAll();
    }

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    public void updateModulo(final Modulo entity) {
        dao.update(entity);
    }

    /**
     * Realiza o update da data de fechamento de um modulo.
     * 
     * @param entity
     *            - entidade do tipo Modulo.
     * @param newCloseDate
     *            - nova data de fechamento.
     */
    public void updateCloseDate(final Modulo entity, final Date newCloseDate) {
        String username = "";
        FacesContext context = FacesContext.getCurrentInstance();
        // Essa condição é necessária para os testes unitarios não falharem.
        // Pois no testes unitarios não existe contexto.
        if (context != null) {
            // Pega o login do usuario logado no sistema
            username = (String) context.getExternalContext().getSessionMap()
                    .get("SPRING_SECURITY_LAST_USERNAME");
        }

        Modulo modulo = this.findModuloById(entity.getCodigoModulo());
        modulo.setDataFechamento(newCloseDate);
        modulo.setCodigoAutor(username);
        modulo.setDataAtualizacao(new Date());

        this.updateModulo(modulo);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public Modulo findModuloById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Pega a data de fechamento do módulo passado por parâmetro.
     * 
     * @param codigoModulo
     *            codigo do Modulo
     * 
     * @return retorna a Data de Fechamento do módulo passado por parâmetro.
     */
    private Date getClosingDateByModulo(final Long codigoModulo) {
        Modulo modulo = this.findModuloById(codigoModulo);

        return DateUtil.getDate(modulo.getDataFechamento());
    }

    /**
     * Pega a data de fechamento do MapaAlocacao.
     * 
     * @return retorna a Data de Fechamento do MapaAlocacao.
     */
    public Date getClosingDateMapaAlocacao() {
        return this.getClosingDateByModulo(new Long(systemProperties
                .getProperty(Constants.MODULO_ALOCATION_MAP_CODE)));
    }

    /**
     * Pega a data de fechamento da Despesa.
     * 
     * @return retorna a Data de Fechamento da Despesa.
     */
    public Date getClosingDateExpense() {
        return this.getClosingDateByModulo(new Long(systemProperties
                .getProperty(Constants.MODULO_EXPENSE_CODE)));
    }

    /**
     * Pega a data de fechamento da Revenue.
     * 
     * @return retorna a Data de Fechamento da Revenue.
     */
    public Date getClosingDateRevenue() {
        return this.getClosingDateByModulo(new Long(systemProperties
                .getProperty(Constants.MODULO_RECEITA_CODE)));
    }

    /**
     * Pega a data de fechamento do History Lock.
     * 
     * @return retorna a Data de Fechamento do History Lock.
     */
    public Date getClosingDateHistoryLock() {
        return this.getClosingDateByModulo(new Long(systemProperties
                .getProperty(Constants.MODULO_HISTORY_LOCK_CODE)));
    }

    /**
     * Verifica se {@code date} é maior que o HistoryLock sem levar em consideração o dia.
     *
     * @param date
     * @return true se {@code date} é maior que HistoryLock e false se {@code date} é menor que HistoryLock.
     */
    public Boolean dateAfterHistoryLock(Date date) {
    	Boolean dateBeforeHistoryLock = DateUtil.before(date, this.getClosingDateHistoryLock());
    	Boolean equalDates = DateUtil.equalDateMonthAndYear(date, this.getClosingDateHistoryLock());
    	if (equalDates) {
			return false;
		} else if (dateBeforeHistoryLock) {
			return false;
		}

    	return true;
    }

    /**
     * Pega a data de fechamento do ChargeBack.
     * 
     * @return retorna a Data de Fechamento do ChargeBack.
     */
    public Date getClosingDateChargeBack() {
        return this.getClosingDateByModulo(new Long(systemProperties
                .getProperty(Constants.MODULO_CHARGEBACK_CODE)));
    }

    /**
     * Validação do HistoryLock. Caso startDate (data de início da vigência)
     * seja maior do que a data History Lock, a vigência é válida e pode ser
     * adicionada ou removida. Caso contrário, a vigência não pode ser
     * adicionada nem removida.
     * 
     * @param startDate
     *            - data de início da vigência
     * @param showMsgError
     *            - mostra mensagem de erro caso startDate não seja válido
     * @param isCurrentMonthMsg
     *
     * @return true se startDate for maior do que HistoryLock. false caso
     *         contrário
     */
    public Boolean verifyHistoryLock(final Date startDate,
            final Boolean showMsgError, final Boolean isCurrentMonthMsg) {
        Date historyLockDate = this.getClosingDateHistoryLock();
        if (startDate.compareTo(historyLockDate) > 0) {
            return Boolean.valueOf(true);
        } else {
            if (showMsgError) {
                Messages.showError("verifyHistoryLock",
                        (isCurrentMonthMsg) ?
                                Constants.MSG_ERROR_MODULO_VERIFY_HISTORY_LOCK_CURRENT_MONTH :
                                Constants.MSG_ERROR_MODULO_VERIFY_HISTORY_LOCK,
                        DateUtil.formatDate(historyLockDate,
                                Constants.DEFAULT_DATE_PATTERN_SIMPLE,
                                Constants.DEFAULT_CALENDAR_LOCALE));
            }

            return Boolean.valueOf(false);
        }
    }

    /**
     * Pega a data de fechamento do Módulo de horas extras e plantão.
     * 
     * @return retorna a Data de Fechamento.
     */
    @Override
    public Date getClosingDateBancoHoras() {
        // pega a data de fechamento do modulo do MapaAlocacao
        Modulo modulo = this.findModuloById(new Long(systemProperties
                        .getProperty(Constants.MODULO_LEAVES_OVERTIME_CODE)));

        return DateUtils.truncate(
                modulo.getDataFechamento(), Calendar.MONTH);
    }
    
    /**
     * Retorna a data de fechamento do Módulo de International Revenue.
     * 
     * @return Data de fechamento.
     */
    public Date getClosingDateInternationalRevenue() {
    	return this.getClosingDateByModulo(new Long(systemProperties
    			.getProperty(Constants.MODULO_INT_REVENUE_CODE)));
    }

    /**
     * Retorna se a {@code date} é maior que closing date de receita nacional ou internacional.
     *
     * @param date
     * @param nationalityIndicator
     * 				- "NAC" for Nacional and "INT" for Interna
     * @return
     */
    public Boolean isDateAfterRevenueClosingDate(Date date, String nationalityIndicator) {

    	Date closingDate = null;
    	if (nationalityIndicator.equals(Constants.CLIENTE_TYPE_NATIONAL)) {
    		closingDate = DateUtils.truncate(this.getClosingDateRevenue(), Calendar.MONTH);
    	} else if (nationalityIndicator.equals(Constants.CLIENTE_TYPE_INTERNATIONAL)) {
    		closingDate = DateUtils.truncate(this.getClosingDateInternationalRevenue(), Calendar.MONTH);
		}

		if (date.after(closingDate)) {
			return true;
		}

		return false;
	}
}