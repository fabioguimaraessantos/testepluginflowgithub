package com.ciandt.pms.control.jsf;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.control.jsf.bean.CentroLucroBean;
import com.ciandt.pms.control.jsf.bean.NaturezaCentroLucroBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.NaturezaCentroLucro;


/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.PROFIT_CENTER_TYPE:CR", "BOF.PROFIT_CENTER_TYPE:VW", "BOF.PROFIT_CENTER_TYPE:ED",
"BOF.PROFIT_CENTER_TYPE:DE" })
public class NaturezaCentroLucroController {

    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_NATUREZA_CENTRO_LUCRO_ADD = "naturezaCentroLucro_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_NATUREZA_CENTRO_LUCRO_EDIT = "naturezaCentroLucro_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_NATUREZA_CENTRO_LUCRO_DELETE = "naturezaCentroLucro_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_NATUREZA_CENTRO_LUCRO_RESEARCH = "naturezaCentroLucro_research";

    /**
     * Instancia do servico.
     */
    @Autowired
    private INaturezaCentroLucroService naturezaCentroLucroService;

    /**
     * Instancia do servico.
     */
    @Autowired
    private ICentroLucroService centroLucroService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private NaturezaCentroLucroBean bean = null;

    /**
     * Instancia do bean.
     */
    @Autowired
    private CentroLucroBean beanCentroLucro = null;

    /**
     * @return the bean
     */
    public NaturezaCentroLucroBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final NaturezaCentroLucroBean bean) {
        this.bean = bean;
    }

    /**
     * @return the beanCentroLucro
     */
    public CentroLucroBean getBeanCentroLucro() {
        return beanCentroLucro;
    }

    /**
     * @param beanCentroLucro
     *            the beanCentroLucro to set
     */
    public void setBeanCentroLucro(final CentroLucroBean beanCentroLucro) {
        this.beanCentroLucro = beanCentroLucro;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();
        bean.setSuggestionsListInAtivo(Constants.ACTIVE_INACTIVE_VALUES);
        return OUTCOME_NATUREZA_CENTRO_LUCRO_RESEARCH;
    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareCreate() {
        bean.reset();
        bean.setIsUpdate(Boolean.FALSE);
        return OUTCOME_NATUREZA_CENTRO_LUCRO_ADD;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void addCentroLucro() {
        CentroLucro cl = beanCentroLucro.getTo();
        Long naturezaId = bean.getTo().getCodigoNatureza();

        NaturezaCentroLucro ncl = null;
        if (naturezaId != null) {
            ncl = naturezaCentroLucroService
                    .findNaturezaCentroLucroById(naturezaId);
        }

        // se existir a natureza cria o centro de lucro
        if (ncl != null) {
            cl.setNaturezaCentroLucro(ncl);
            centroLucroService.createCentroLucro(cl);

            Messages.showSucess("addCentroLucro",
                    Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_CENTRO_LUCRO);

            beanCentroLucro.getCentroLucroList().add(0, cl);

            beanCentroLucro.resetTo();
            // se a natureza nao existir na base de dados
        } else {
            Messages.showError("addCentroLucro",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_NATUREZA_CENTRO_LUCRO);
        }
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void create() {
        bean.getTo().setIndicadorAtivo(Constants.ACTIVE);
        naturezaCentroLucroService.createNaturezaCentroLucro(bean.getTo());
        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                Constants.ENTITY_NAME_NATUREZA_CENTRO_LUCRO);
        bean.resetTo();
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareUpdate() {
        findById(bean.getCurrentRowId());

        beanCentroLucro.setCentroLucroList(centroLucroService
                .findCentroLucroByNatureza(bean.getTo()));

        beanCentroLucro.setTo(new CentroLucro());

        bean.setIsUpdate(Boolean.TRUE);
        return OUTCOME_NATUREZA_CENTRO_LUCRO_EDIT;
    }

    /**
     * Prepara a tela de edicao do centro lucro.
     * 
     */
    public void prepareUpdateCentroLucro() {

        beanCentroLucro.setTo(centroLucroService
                .findCentroLucroById(beanCentroLucro.getTo()
                        .getCodigoCentroLucro()));

        beanCentroLucro.setIsUpdate(Boolean.TRUE);
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        try {
            naturezaCentroLucroService.updateNaturezaCentroLucro(bean.getTo());

        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage(), new Object[] {
                    Constants.ENTITY_NAME_NATUREZA_CENTRO_LUCRO,
                    Constants.ENTITY_NAME_CENTRO_LUCRO });

            return null;
        }

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_NATUREZA_CENTRO_LUCRO);
        bean.resetTo();
        findByFilter();
        return OUTCOME_NATUREZA_CENTRO_LUCRO_RESEARCH;
    }

    /**
     * Executa um update da entidade.
     * 
     */
    public void updateCentroLucro() {
        CentroLucro cl = centroLucroService.findCentroLucroById(beanCentroLucro
                .getTo().getCodigoCentroLucro());

        cl.setNomeCentroLucro(beanCentroLucro.getTo().getNomeCentroLucro());
        cl.setIndicadorAtivo(beanCentroLucro.getTo().getIndicadorAtivo());

        centroLucroService.updateCentroLucro(cl);

        beanCentroLucro.setCentroLucroList(centroLucroService
                .findCentroLucroByNatureza(bean.getTo()));

        Messages.showSucess("updateCentroLucro",
                Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_CENTRO_LUCRO);
        // se a natureza nao existir na base de dados

        beanCentroLucro.resetTo();

        beanCentroLucro.setIsUpdate(Boolean.valueOf(false));
    }

    /**
     * Prepara a tela de remocao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareRemove() {
        findById(bean.getCurrentRowId());
        return OUTCOME_NATUREZA_CENTRO_LUCRO_DELETE;
    }

    /**
     * Remove o centro lucro.
     * 
     */
    public void removeCentroLucro() {
        try {
            centroLucroService.removeCentroLucro(centroLucroService
                    .findCentroLucroById(beanCentroLucro.getTo()
                            .getCodigoCentroLucro()));

            beanCentroLucro.getCentroLucroList()
                    .remove(beanCentroLucro.getTo());

            Messages.showSucess("removeCentroLucro",
                    Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                    Constants.ENTITY_NAME_CENTRO_LUCRO);

        } catch (IntegrityConstraintException ice) {
            Messages.showError("removeCentroLucro", ice.getMessage(),
                    new Object[] {Constants.ENTITY_NAME_CENTRO_LUCRO,
                            Constants.ENTITY_NAME_CONTRATO_PRATICA });
        }
        beanCentroLucro.resetTo();
    }

    /**
     * Cancela a remocao do centro lucro.
     * 
     */
    public void cancelCentroLucro() {
        beanCentroLucro.resetTo();
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {
        try {
            naturezaCentroLucroService
                    .removeNaturezaCentroLucro(naturezaCentroLucroService
                            .findNaturezaCentroLucroById(bean.getTo()
                                    .getCodigoNatureza()));
        } catch (IntegrityConstraintException ice) {
            Messages.showError("remove", ice.getMessage(), new Object[] {
                    Constants.ENTITY_NAME_NATUREZA_CENTRO_LUCRO,
                    Constants.ENTITY_NAME_CENTRO_LUCRO });
            return null;
        }
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_NATUREZA_CENTRO_LUCRO);
        bean.resetTo();
        findByFilter();
        return OUTCOME_NATUREZA_CENTRO_LUCRO_RESEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        bean.setTo(naturezaCentroLucroService.findNaturezaCentroLucroById(id));
        if (bean.getTo() == null || bean.getTo().getCodigoNatureza() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        bean.setResultList(naturezaCentroLucroService
                .findNaturezaCentroLucroByFilter(bean.getFilter()));

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

}