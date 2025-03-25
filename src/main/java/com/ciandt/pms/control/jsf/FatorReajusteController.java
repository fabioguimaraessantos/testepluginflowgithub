package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IFatorReajusteService;
import com.ciandt.pms.business.service.ITipoContratoService;
import com.ciandt.pms.control.jsf.bean.FatorReajusteBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.FatorReajuste;
import com.ciandt.pms.model.TipoContrato;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe FatorReajusteController proporciona as funcionalidades 
 * da camada de apresentação referente a entidade FatorReajuste.
 *
 * @since 25/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_FINANCIAL" })
public class FatorReajusteController {

    /** Instancia do bean. */
    @Autowired
    private FatorReajusteBean bean;
    
    /** Instancia do servico FatorReajusteService. */
    @Autowired
    private IFatorReajusteService fatorReajusteService;
    
    /** Instancia do servico TipoContratoService. */
    @Autowired
    private ITipoContratoService tipoContratoService;
    
    
    /*********** OUTCOMES **************************/

    /** outcome tela gerenciamento da entidade. */
    private static final String OUTCOME_FATOR_REAJUSTE_MANAGE = "fatorReajuste_manage";

    
    /**
     * Preapara a tela inicial.
     * 
     * @return retorna a pagina de destino.
     */
    public String prepareManage() {
        
        this.loadTipoContratoList(
                tipoContratoService.findTipoContratoAll());
        
        bean.reset();
        
        return OUTCOME_FATOR_REAJUSTE_MANAGE;
    }
    
    /**
     * Prepara a os dados para a tela de ciração.
     */
    public void prepareCreate() {
        TipoContrato tp = bean.getTo().getTipoContrato();
        
        bean.resetTo();
        
        bean.getTo().setTipoContrato(tp);
    }
    
    /**
     * Ação que cria uma entidade FatorReajuste.
     */
    public void create() {
        
        FatorReajuste fatorReajuste = bean.getTo();
        fatorReajuste.setCodigoFatorReajuste(null);
        
        fatorReajuste.setDataInicio(
                DateUtil.getDate(bean.getMonth(), bean.getYear()));
        
        Long codTipoContrato = bean.getTipoContratoMap().get(
                fatorReajuste.getTipoContrato().getNomeTipoContrato());
        
        TipoContrato tipoContrato = tipoContratoService.
            findTipoContratoById(codTipoContrato);
        
        fatorReajuste.setTipoContrato(tipoContrato);
        
        if (!fatorReajusteService.exists(fatorReajuste)) {
            fatorReajusteService.createFatorReajuste(fatorReajuste);
            
            bean.setResultList(fatorReajusteService.
                    findFatorReajusteByTipoContrato(tipoContrato));
            
            bean.resetTo();
            
            bean.getTo().setTipoContrato(tipoContrato);
            
            Messages.showSucess("create", 
                    Constants.DEFAULT_MSG_SUCCESS_SAVE, 
                    Constants.ENTITY_NAME_FATOR_REAJUSTE);
        } else {
            Messages.showWarning("create", 
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_FATOR_REAJUSTE);
        }
       
    }
    
    /**
     * Realiza a remoção de uma entidade FatorRejuste.
     */
    public void delete() {
        FatorReajuste fr = bean.getTo();
        
        fatorReajusteService.removeFatorReajuste(fr);
        
        bean.setResultList(fatorReajusteService.
                findFatorReajusteByTipoContrato(fr.getTipoContrato()));
        
        Messages.showSucess("delete", 
                Constants.DEFAULT_MSG_SUCCESS_REMOVE, 
                Constants.ENTITY_NAME_FATOR_REAJUSTE);
    }
    
    /**
     * Realiza o update de uma entidade FatorReajuste.
     */
    public void update() {
        FatorReajuste fr = bean.getResultList().get(bean.getSelectedRow());
        
        fatorReajusteService.updateFatorReajuste(fr);
    }
    
    /**
     * Carrega a lista de resultados.
     * 
     * @param e - evento de mudança
     */
    public void loadFatorReajusteResultList(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null && !"".equals(value)) {
            Long codTipoContrato = bean.getTipoContratoMap().get(value);
            TipoContrato tipoContrato = null;

            // se o tipo de contrsto existir
            if (codTipoContrato != null) {
                tipoContrato = tipoContratoService
                        .findTipoContratoById(codTipoContrato);
                bean.setResultList(fatorReajusteService.
                        findFatorReajusteByTipoContrato(tipoContrato));
            }
        }
    }
    
    /**
     * Popula a lista de clientes para combobox.
     * 
     * @param tipoContratos
     *            lista de clientes.
     * 
     */
    private void loadTipoContratoList(final List<TipoContrato> tipoContratos) {

        Map<String, Long> tipoContratoMap = new HashMap<String, Long>();
        List<String> tipocontratoList = new ArrayList<String>();

        for (TipoContrato tipocontrato : tipoContratos) {
            tipoContratoMap.put(tipocontrato.getNomeTipoContrato(), 
                    tipocontrato.getCodigoTipoContrato());
            tipocontratoList.add(tipocontrato.getNomeTipoContrato());
        }
        bean.setTipoContratoMap(tipoContratoMap);
        bean.setTipoContratoList(tipocontratoList);
    }
    
    /**
     * Verifica se o valor do atributo TipoContrato é valido. Se o valor não
     * é nulo e existe no contratoPraticaMap, então o valor é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateTipoContrato(final FacesContext context,
            final UIComponent component, final Object value) {

        Long codTipoContrato = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            codTipoContrato = bean.getTipoContratoMap().get(strValue);
            if (codTipoContrato == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * @return the bean
     */
    public FatorReajusteBean getBean() {
        return bean;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(final FatorReajusteBean bean) {
        this.bean = bean;
    }
}
