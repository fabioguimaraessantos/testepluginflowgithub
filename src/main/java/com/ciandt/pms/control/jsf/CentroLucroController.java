/**
 * CentroLucroController - Classe da camada de apresentação do CentroLucro
 */
package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.control.jsf.bean.CentroLucroBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.NaturezaCentroLucro;


/**
 * A classe CentroLucroController proporciona as funcionalidades da camada de
 * apresentação para as acoes referentes a entidade CentroLucro.
 * 
 * @since 04/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_FINANCIAL" })
public class CentroLucroController {

    /** outcome tela inclusao da entidade. */
    private static final String OUTCOME_CENTRO_LUCRO_ADD = "centroLucro_add";
    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_CENTRO_LUCRO_EDIT = "centroLucro_edit";
    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_CENTRO_LUCRO_DELETE = "centroLucro_delete";
    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_CENTRO_LUCRO_RESEARCH = "centroLucro_research";

    /**
     * Instancia do servico.
     */
    @Autowired
    private ICentroLucroService centroLucroService;

    /** Instancia do servico da NaturesaCentrolucro. */
    @Autowired
    private INaturezaCentroLucroService naturezaService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private CentroLucroBean bean = null;

    /**
     * @return the bean
     */
    public CentroLucroBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final CentroLucroBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();
        loadNaturezaCentroLucroList(naturezaService
                .findNaturezaCentroLucroAll());
        return OUTCOME_CENTRO_LUCRO_RESEARCH;
    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de criação
     */
    public String prepareCreate() {
        bean.resetTo();
        loadNaturezaCentroLucroList(naturezaService
                .findNaturezaCentroLucroAll());
        return OUTCOME_CENTRO_LUCRO_ADD;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void create() {
        CentroLucro cl = bean.getTo();
        Long naturezaId = bean.getNaturezaCentroLucroMap().get(
                cl.getNaturezaCentroLucro().getNomeNatureza());

        NaturezaCentroLucro ncl = null;
        if (naturezaId != null) {
            ncl = naturezaService.findNaturezaCentroLucroById(naturezaId);
        }

        // se existir a natureza cria o centro de lucro
        if (ncl != null) {
            cl.setNaturezaCentroLucro(ncl);
            centroLucroService.createCentroLucro(cl);

            Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_CENTRO_LUCRO);
            
            bean.resetTo();
            // se a natureza nao existir na base de dados
        } else {
            Messages.showError("create", Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_NATUREZA_CENTRO_LUCRO);
        }
    }

    /**
     * Prepara a tela de editar uma entidade.
     * 
     * @return retorna a pagina de update
     */
    public String prepareUpdate() {
        findById(bean.getTo().getCodigoCentroLucro());
        return OUTCOME_CENTRO_LUCRO_EDIT;
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        CentroLucro cl = bean.getTo();
        Long naturezaId = bean.getNaturezaCentroLucroMap().get(
                cl.getNaturezaCentroLucro().getNomeNatureza());

        // busca pelo centro de lucro
        NaturezaCentroLucro ncl = null;
        if (naturezaId != null) {
            ncl = naturezaService.findNaturezaCentroLucroById(naturezaId);
        }

        // se existir a natureza cria o centro de lucro
        if (ncl != null) {
            cl.setNaturezaCentroLucro(ncl);
            centroLucroService.updateCentroLucro(cl);
            Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_CENTRO_LUCRO);
            // se a natureza nao existir na base de dados
        } else {
            Messages.showError("update", Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_NATUREZA_CENTRO_LUCRO);
        }
        
        bean.resetTo();
        findByFilter();
        return OUTCOME_CENTRO_LUCRO_RESEARCH;
    }

    /**
     * Prepara a tela de remocao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareRemove() {
        findById(bean.getTo().getCodigoCentroLucro());
        return OUTCOME_CENTRO_LUCRO_DELETE;
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {
        try { 
        centroLucroService.removeCentroLucro(centroLucroService
                .findCentroLucroById(bean.getTo().getCodigoCentroLucro()));
        } catch (IntegrityConstraintException ice) {
            Messages.showError("remove", ice.getMessage(), new Object[] {
                Constants.ENTITY_NAME_CENTRO_LUCRO,
                Constants.ENTITY_NAME_CONTRATO_PRATICA });
        return null;
    }
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_CENTRO_LUCRO);
        bean.resetTo();
        findByFilter();
        return OUTCOME_CENTRO_LUCRO_RESEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        // pega ocentro de lucro pelo id
        bean.setTo(centroLucroService.findCentroLucroById(id));
        // verifica se o centro de lucro é nulo
        if (bean.getTo() == null || bean.getTo().getCodigoCentroLucro() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        CentroLucro filter = bean.getFilter();
        Long naturezaId = bean.getNaturezaCentroLucroMap().get(
                filter.getNaturezaCentroLucro().getNomeNatureza());

        // se a natureza existir
        if (naturezaId != null) {
            filter.getNaturezaCentroLucro().setCodigoNatureza(naturezaId);
            // se a natureza nao existir
        } else if (!""
                .equals(filter.getNaturezaCentroLucro().getNomeNatureza())) {
            filter.getNaturezaCentroLucro().setCodigoNatureza(-1L);
            // se nao selecionou nenhuma natureza
        } else {
            filter.getNaturezaCentroLucro().setCodigoNatureza(0L);
        }

        // realiza a busca pelo centro de lucro
        bean.setResultList(centroLucroService
                .findCentroLucroByFilterFetch(filter));
        // se nenhum resultado encontrado
        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    /**
     * Popula a lista de naturezas para combobox da natureza.
     * 
     * @param naturezas
     *            lista de naturezas.
     * 
     */
    private void loadNaturezaCentroLucroList(
            final List<NaturezaCentroLucro> naturezas) {

        Map<String, Long> naturezaCentroLucroMap = new HashMap<String, Long>();
        List<String> naturezaCentroLucroList = new ArrayList<String>();

        for (NaturezaCentroLucro natureza : naturezas) {
            naturezaCentroLucroMap.put(natureza.getNomeNatureza(), natureza
                    .getCodigoNatureza());
            naturezaCentroLucroList.add(natureza.getNomeNatureza());
        }

        bean.setNaturezaCentroLucroMap(naturezaCentroLucroMap);
        bean.setNaturezaCentroLucroList(naturezaCentroLucroList);
    }

    /**
     * Verifica se o valor do atributo natureza é valido. Se o valor não é nulo
     * e existe no naturezaCentroLucroMap, então o valor é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateNaturezaCentroLucro(final FacesContext context,
            final UIComponent component, final Object value) {

        Long naturezaCentroLucro = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            naturezaCentroLucro = bean.getNaturezaCentroLucroMap()
                    .get(strValue);
            if (naturezaCentroLucro == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

}