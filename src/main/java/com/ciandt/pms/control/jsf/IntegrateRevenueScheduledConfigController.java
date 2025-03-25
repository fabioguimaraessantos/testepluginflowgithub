package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IIntegrateRevenueScheduledConfigService;
import com.ciandt.pms.control.jsf.bean.IntegrateRevenueScheduledConfigBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import java.util.Objects;

@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.INVOICING:ED", "BOF.INVOICING:VW", "BOF.INVOICING:CR", "BOF.INVOICING:DE"})
public class IntegrateRevenueScheduledConfigController {

    private static final Logger LOGGER = LogManager.getLogger(IntegrateRevenueScheduledConfigController.class);

    private static final String INTEGRACAO_RECEITA_AGENDADA_MANAGER = "integracaoReceitaAgendada_manager";

    @Autowired
    IntegrateRevenueScheduledConfigBean bean;

    @Autowired
    IIntegrateRevenueScheduledConfigService service;

    public void addDate() {
        bean.addIntegrationDate();
    }

    public void save() {
        try {
            bean.getParametro().setTextoParametro(removeBrackets(bean.getScheduledIntegrationDates().toString()));
            service.update(bean.getParametro());
            service.clearMegaMiddlewareCache();
            Messages.showSucess("IntegrateRevenueScheduledConfigController.save()", "_nls.msg.success.save");
        } catch (ConnectTimeoutException e) {
            LOGGER.error("Failed to connect to RabbitMQ and clear MegaMid cache.", e);
            Messages.showSucess("IntegrateRevenueScheduledConfigController.save()", "_nls.msg.success.save");
        } catch (Exception e) {
            LOGGER.error("Failed to save parameters.", e);
            Messages.showSucess("IntegrateRevenueScheduledConfigController.save()", "_nls.receita_agendamento_configuracao_integracao_failed_to_save");
        }
    }

    public void startManualIntegration() {
        try {
            service.startManualIntegration();
            Messages.showSucess("IntegrateRevenueScheduledConfigController.startManualIntegration", "_nls.receita_agendamento_configuracao_integracao_success");
        } catch (Exception e) {
            LOGGER.error("Failed to connect to RabbitMQ and start integration.", e);
            Messages.showError("IntegrateRevenueScheduledConfigController.startManualIntegration", "_nls.receita_agendamento_configuracao_integracao_error");
        }
    }

    public void delete() {
        for (int i = 0; i < bean.getScheduledIntegrationDates().size(); i++) {
            if (DateUtil.isSameDay(bean.getScheduledIntegrationDates().get(i), bean.getDateToDelete())) {
                bean.getScheduledIntegrationDates().remove(i);
            }
        }
    }

    public String prepareIntegrateScheduledConfig() {
        bean.reset();
        Parametro parametro = service.findParametro();

        if (Objects.isNull(parametro)) {
            Parametro param = new Parametro();
            param.setNomeParametro(Constants.MEGA_MIDDLEWARE_DATE_INTERCOMPANY_REVENUE_INTEGRATION);
            bean.setParametro(param);
        } else {
            bean.setParametro(service.findParametro());
            if (StringUtils.isNotEmpty(bean.getParametro().getTextoParametro())) {
                for (String date : bean.getParametro().getTextoParametro().split(",")) {
                    bean.addScheduledIntegrationDates(date);
                }
            }
        }

        bean.sortDates();
        return INTEGRACAO_RECEITA_AGENDADA_MANAGER;
    }

    public IntegrateRevenueScheduledConfigBean getBean() {
        return bean;
    }

    public void setBean(IntegrateRevenueScheduledConfigBean bean) {
        this.bean = bean;
    }

    private String removeBrackets(String dates) {
        return dates.replace("[", "").replace("]", "");
    }

}
