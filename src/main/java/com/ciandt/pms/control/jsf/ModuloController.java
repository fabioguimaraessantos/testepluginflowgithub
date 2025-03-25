package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.control.jsf.bean.ModuloBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.vo.ModuloRow;
import com.ciandt.pms.util.DateUtil;


/**
 * A classe ModuloController proporciona as funcionalidades da camada de
 * apresentação para as acoes referentes a entidade CentroLucro.
 * 
 * @since 04/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.CLOSING_DATE:VW", "BOF.CLOSING_DATE:ED" })
public class ModuloController {

    /** Instancia do servico. */
    @Autowired
    private IModuloService moduloService;
    
    /** Instancia do bean. */
    @Autowired
    private ModuloBean bean = null;
    
    /*********** OUTCOMES **************************/

    /** outcome tela manage do Modulo. */
    private static final String OUTCOME_MODULO_MANAGE = "modulo_manage";
    
    /***********************************************/
    
    /**
     * Prepara a tela de gerenciamento.
     * 
     * @return pagina de geranciamento.
     */
    public String prepareUpdate() {
        this.prepareModuloRowList(moduloService.findModuloaAll());
        return OUTCOME_MODULO_MANAGE;
    }
    
    /**
     * Realiza o update da entidade Modulo.
     */
    public void update() {
        ModuloRow row = bean.getRow();
        
        Date date = DateUtil.getDate(row.getMonth(), row.getYear());
        
        moduloService.updateCloseDate(row.getModulo(), date);

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_MODULO);
        
        this.prepareUpdate();
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(final ModuloBean bean) {
        this.bean = bean;
    }

    /**
     * @return the bean
     */
    public ModuloBean getBean() {
        return bean;
    }
    
    /**
     * Prepara a lista com as linhas a serem exibidas na pagina.
     * 
     * @param moduloList - Lista de Modulos.
     */
    private void prepareModuloRowList(final List<Modulo> moduloList) {
        List<ModuloRow> moduloRowList = new ArrayList<ModuloRow>();
        ModuloRow row = null;
        
        for (Modulo modulo : moduloList) {
            row = new ModuloRow();
            Date dataFechamento = modulo.getDataFechamento();
            row.setMonth("" + DateUtil.getMonthNumber(dataFechamento));
            row.setYear("" + DateUtil.getYear(dataFechamento));
            row.setModulo(modulo);
            moduloRowList.add(row);
        }
        
        bean.setModuloRowList(moduloRowList);
    }
}
