package com.ciandt.pms.control.jsf;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAliquotaService;
import com.ciandt.pms.business.service.IImpostoService;
import com.ciandt.pms.control.jsf.bean.AliquotaBean;
import com.ciandt.pms.control.jsf.bean.ContratoPraticaBean;
import com.ciandt.pms.control.jsf.bean.ImpostoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Aliquota;
import com.ciandt.pms.model.Imposto;


/**
 * Define o Controller da entidade.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_FINANCIAL" })
public class ImpostoController {

    /*********** SERVICES **************************/

    /** Instancia do serviço da entidade Imposto. */
    @Autowired
    private IImpostoService impostoService;

    /** Instancia do serviço da entidade Aliquota. */
    @Autowired
    private IAliquotaService aliquotaService;

    /*********** BEANS **************************/

    /** Instancia do bean. */
    @Autowired
    private ImpostoBean bean = null;

    /** Instancia do bean ContratoPratica. */
    @Autowired
    private ContratoPraticaBean contratoPraticaBean = null;

    /** Instancia do bean Aliquota. */
    @Autowired
    private AliquotaBean aliquotaBean = null;

    /*********** OUTCOMES **************************/

    /** outcome tela de busca da entidade. */
    private static final String OUTCOME_IMPOSTO_RESEARCH = "imposto_research";

    /***************************************************/

    /**
     * Pega o bean.
     * 
     * @return o bean ImpostoPratica
     */
    public ImpostoBean getBean() {
        return bean;
    }

    /**
     * Seta o bean.
     * 
     * @param iBean
     *            do ImpostoBeans
     */
    public void setBean(final ImpostoBean iBean) {
        this.bean = iBean;
    }

    /**
     * Pega o bean.
     * 
     * @return o bean ContratoPratica
     */
    public ContratoPraticaBean getContratoPraticaBean() {
        return contratoPraticaBean;
    }

    /**
     * Seta o bean.
     * 
     * @param contratoPraticaBean
     *            do ContratoPratica
     */
    public void setContratoPraticaBean(
            final ContratoPraticaBean contratoPraticaBean) {
        this.contratoPraticaBean = contratoPraticaBean;
    }

    /**
     * Pega o bean.
     * 
     * @return o bean Aliquota
     */
    public AliquotaBean getAliquotaBean() {
        return aliquotaBean;
    }

    /**
     * Seta o bean.
     * 
     * @param aliquotaBean
     *            do AliquotaBean
     */
    public void setAliquotaBean(final AliquotaBean aliquotaBean) {
        this.aliquotaBean = aliquotaBean;
    }

    /**
     * Prepara a tela para a TabelaPreco.
     * 
     * @return
     */
    public void prepareTabelaAliquota() {
        aliquotaBean.reset();

        // Seta a lista de aliquotas referente
        aliquotaBean.setResultList(aliquotaService.findAliquotaAll());
    }

    /**
     * Prepara a tela da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();

        this.loadImpostoList(impostoService.findImpostoAll());

        prepareTabelaAliquota();

        return OUTCOME_IMPOSTO_RESEARCH;
    }

    /**
     * Cria uma entidade Aliquota associado a um Imposto.
     */
    public void createAliquota() {
        Aliquota al = aliquotaBean.getTo();
        Date startDate = null;

        Long impostoId = bean.getImpostoMap().get(
                al.getImposto().getNomeImposto());

        Imposto imp = null;
        if (impostoId != null) {
            imp = impostoService.findImpostoById(impostoId);
        }

        // Se existir cliente, cria o contrato
        if (imp != null) {
            al.setImposto(imp);

            try {
                String startDateStr = aliquotaBean.getMesInicioVigencia() + "/"
                        + aliquotaBean.getAnoInicioVigencia();

                String[] dateFormat = {"MM/yyyy" };

                startDate = DateUtils.parseDate(startDateStr, dateFormat);
                al.setDataInicio(startDate);

                if (aliquotaService.createAliquota(al)) {
                    aliquotaBean.resetTo();
                }

                aliquotaBean.setResultList(aliquotaService.findAliquotaAll());

            } catch (ParseException e) {
                // provavelmente nunca ocorrera este erro,
                // pela interface com o usuário.
                e.printStackTrace();
                Messages.showError("createAliquota",
                        Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
            }
        } else {
            Messages.showError("create", Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_CLIENTE);
        }
    }

    /**
     * Popula a lista de praticas para combobox.
     * 
     * @param impostos
     *            lista de impostos.
     * 
     */
    private void loadImpostoList(final List<Imposto> impostos) {

        Map<String, Long> impostoMap = new HashMap<String, Long>();
        List<String> impostoList = new ArrayList<String>();

        for (Imposto imposto : impostos) {
            impostoMap
                    .put(imposto.getNomeImposto(), imposto.getCodigoImposto());
            impostoList.add(imposto.getNomeImposto());
        }

        bean.setImpostoMap(impostoMap);
        bean.setImpostoList(impostoList);
    }

    /**
     * Remove uma TabelaPreco.
     */
    public void removeAliquota() {
        Aliquota al = aliquotaService.findAliquotaById(aliquotaBean.getTo()
                .getCodigoAliquota());

        aliquotaService.removeAliquota(al);

        List<Aliquota> aliquotaList = aliquotaService.findAliquotaAll();

        aliquotaBean.setResultList(aliquotaList);
        aliquotaBean.resetTo();
        prepareTabelaAliquota();
    }

    /**
     * Verifica se o imposto é válido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateImposto(final FacesContext context,
            final UIComponent component, final Object value) {

        Long impostoId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get("label");

            impostoId = bean.getImpostoMap().get(strValue);

            if (impostoId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (impostoService.findImpostoById(impostoId) == null) {
                    throw new ValidatorException(
                            Messages
                                    .getMessageError(Constants.DEFAULT_MSG_ERROR_NOT_FOUND));
                }
            }
        }
    }

}
