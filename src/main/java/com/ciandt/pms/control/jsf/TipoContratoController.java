/**
 * CentroLucroController - Classe da camada de apresentação do CentroLucro
 */
package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.ITipoContratoEncargoService;
import com.ciandt.pms.business.service.ITipoContratoService;
import com.ciandt.pms.control.jsf.bean.TipoContratoBean;
import com.ciandt.pms.control.jsf.bean.TipoContratoEncargoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TipoContrato;
import com.ciandt.pms.model.TipoContratoEncargo;
import com.ciandt.pms.util.DateUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.event.ValueChangeEvent;
import java.util.*;


/**
 * A classe TipoContratoController proporciona as funcionalidades da camada de
 * apresentação para as acoes referentes a entidade TipoContrato.
 * 
 * @since 27/05/2011
 * @author cmantovani
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"PER.CONTRACT_TYPE:VW", "PER.CONTRACT_TYPE:ED", "PER.CONTRACT_TYPE:CR", "PER.CONTRACT_TYPE:DE"})
public class TipoContratoController {

    /** outcome tela de gerenciamento inclusao da entidade. */
    private static final String OUTCOME_TIPO_CONTRATO_MANAGE = "tipoContrato_manage";

    /** Instancia do servico. */
    @Autowired
    private ITipoContratoService tipoContratoService;

    /** Instancia do servico. */
    @Autowired
    private ITipoContratoEncargoService tipoContratoEncargoService;

    /** Instancia do servico. */
    @Autowired
    private IModuloService moduloService;

    /** Instancia do servico. */
    @Autowired
    private IMoedaService moedaService;

    /** Instancia do bean. */
    @Autowired
    private TipoContratoBean bean = null;

    /** Instancia do bean. */
    @Autowired
    private TipoContratoEncargoBean beanTipoContratoEncargo = null;

    /**
     * @return the bean
     */
    public TipoContratoBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final TipoContratoBean bean) {
        this.bean = bean;
    }

    /**
     * @return the beanTipoContratoEncargo
     */
    public TipoContratoEncargoBean getBeanTipoContratoEncargo() {
        return beanTipoContratoEncargo;
    }

    /**
     * @param beanTipoContratoEncargo
     *            the beanTipoContratoEncargo to set
     */
    public void setBeanTipoContratoEncargo(
            final TipoContratoEncargoBean beanTipoContratoEncargo) {
        this.beanTipoContratoEncargo = beanTipoContratoEncargo;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareManage() {
        bean.reset();

        this.loadMoedaList(moedaService.findMoedaAll());

        bean.setResultList(tipoContratoService.findTipoContratoAll());

        beanTipoContratoEncargo.setStartMonth(DateUtil
                .getMonthString(new Date()));
        beanTipoContratoEncargo
                .setStartYear(DateUtil.getYearString(new Date()));

        // guarda a data do HistoryLock
        beanTipoContratoEncargo.setHistoryLockDate(moduloService
                .getClosingDateHistoryLock());

        return OUTCOME_TIPO_CONTRATO_MANAGE;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void createTipoContrato() {

        TipoContrato tipoContrato = bean.getTo();
        Long moedaId = bean.getMoedaMap().get(bean.getNomeMoedaSelected());

        Moeda moeda = null;
        if (moedaId != null) {
            moeda = moedaService.findMoedaById(moedaId);
        }

        // se existir a moeda cria o tipoContrato
        if (moeda != null) {
            tipoContrato.setMoeda(moeda);
            tipoContrato.setIndicadorAtivo("A");
            tipoContrato.setSiglaTipoContrato(tipoContrato
                    .getNomeTipoContrato());
            tipoContratoService.createTipoContrato(tipoContrato);

            Messages.showSucess("addTipoContrato",
                    Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_TIPO_CONTRATO);

            bean.getResultList().add(0, tipoContrato);

            bean.resetTo();
            bean.setNomeMoedaSelected("");
            // se a moeda nao existir na base de dados
        } else {
            Messages.showError("addTipoContrato",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_MOEDA);
        }
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void createTipoContratoEncargo() {

        TipoContratoEncargo tipoContratoEncargo = beanTipoContratoEncargo
                .getTo();

        Date startDate = DateUtil.getDate(beanTipoContratoEncargo
                .getStartMonth(), beanTipoContratoEncargo.getStartYear());

        // verifica o History Lock. Se startDate não for válido, dá mensagem de
        // erro
        if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
            return;
        }

        Long tipoContratoId = beanTipoContratoEncargo.getTipoContratoMap().get(
                beanTipoContratoEncargo.getNomeTipoContratoSelected());

        TipoContrato tipoContrato = null;
        if (tipoContratoId != null) {
            tipoContrato = tipoContratoService
                    .findTipoContratoById(tipoContratoId);
        }

        // se existir o tipoContrato cria o Encargo
        if (tipoContrato != null) {
            tipoContratoEncargo.setTipoContrato(tipoContrato);
            tipoContratoEncargo.setDataInicioVigencia(startDate);

            boolean success = tipoContratoEncargoService
                    .createTipoContratoEncargo(tipoContratoEncargo);

            if (success) {
                Messages.showSucess("createTipoContratoEncargo",
                        Constants.DEFAULT_MSG_SUCCESS_CREATE,
                        Constants.ENTITY_NAME_TIPO_CONTRATO_ENCARGO);

                beanTipoContratoEncargo.setResultList(tipoContrato
                        .getTipoContratoEncargos());

            }

            beanTipoContratoEncargo.resetTo();
            // se o tipoContrato nao existir na base de dados
        } else {
            Messages.showError("addTipoContratoEncargo",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_TIPO_CONTRATO);
        }
    }

    /**
     * Prepara a tela de adicao de Encargos.
     */
    public void prepareCreateWelfare() {
        beanTipoContratoEncargo.reset();
        List<TipoContrato> tipoContratoList = tipoContratoService
                .findTipoContratoAllActive();
        loadTipoContratoList(tipoContratoList);
    }

    /**
     * Prepara a tela de atualizacao de TipoContrato.
     */
    public void prepareUpdateTipoContrato() {
        if (bean.getTo().getMoeda() != null) {
            bean.setNomeMoedaSelected(bean.getTo().getMoeda().getNomeMoeda());
        }
    }

    /**
     * Atualiza uma entidade.
     * 
     */
    public void updateTipoContrato() {

        Long moedaId = bean.getMoedaMap().get(bean.getNomeMoedaSelected());

        Moeda moeda = null;
        if (moedaId != null) {
            moeda = moedaService.findMoedaById(moedaId);
        }

        // se existir a natureza cria o centro de lucro
        if (moeda != null) {
            bean.getTo().setMoeda(moeda);
            bean.getTo().setSiglaTipoContrato(
                    bean.getTo().getNomeTipoContrato());
            try {
                tipoContratoService.updateTipoContrato(bean.getTo());

                Messages.showSucess("updateTipoContrato",
                        Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                        Constants.ENTITY_NAME_TIPO_CONTRATO);

            } catch (IntegrityConstraintException ice) {
                Messages.showError("updateTipoContrato", ice.getMessage(),
                        new Object[] {Constants.ENTITY_NAME_TIPO_CONTRATO,
                                Constants.ENTITY_NAME_CONTRATO_PRATICA });
            }

            bean.resetTo();
            bean.setNomeMoedaSelected("");
            // se a natureza nao existir na base de dados
        } else {
            Messages.showError("updateTipoContrato",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_MOEDA);
        }
    }

    /**
     * Remove o Tipo Contrato.
     * 
     */
    public void removeTipoContrato() {
        try {
            tipoContratoService
                    .removeTipoContrato(tipoContratoService
                            .findTipoContratoById(bean.getTo()
                                    .getCodigoTipoContrato()));

            bean.getResultList().remove(bean.getTo());

            Messages.showSucess("removeTipoContrato",
                    Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                    Constants.ENTITY_NAME_TIPO_CONTRATO);

        } catch (IntegrityConstraintException ice) {
            Messages.showError("removeTipoContrato",
                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_TIPO_CONTRATO);
        } catch (ConstraintViolationException cve) {
            Messages.showError("removeTipoContrato",

            Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_TIPO_CONTRATO);
        } catch (DataIntegrityViolationException dive) {
            Messages.showError("removeTipoContrato",

            Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_TIPO_CONTRATO);
        }
        bean.resetTo();
    }

    /**
     * Remove o Tipo Contrato.
     * 
     */
    public void removeTipoContratoEncargo() {

        boolean success = tipoContratoEncargoService
                .removeTipoContratoEncargo(tipoContratoEncargoService
                        .findTipoContratoEncargoById(beanTipoContratoEncargo
                                .getTo().getCodigoTipoContratoEncargo()));

        if (success) {
            Long idTipoContrato = beanTipoContratoEncargo.getTipoContratoMap()
                    .get(beanTipoContratoEncargo.getNomeTipoContratoSelected());
            TipoContrato tipoContrato = tipoContratoService
                    .findTipoContratoById(idTipoContrato);
            beanTipoContratoEncargo.setResultList(tipoContrato
                    .getTipoContratoEncargos());

            Messages.showSucess("removeTipoContratoEncargo",
                    Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                    Constants.ENTITY_NAME_TIPO_CONTRATO_ENCARGO);
        }

        beanTipoContratoEncargo.resetTo();
    }

    /**
     * Cancela a remocao do Tipo Contrato.
     * 
     */
    public void cancelTipoContrato() {
        bean.resetTo();
        bean.setNomeMoedaSelected("");
    }

    /**
     * Cancela a remocao do Tipo Contrato.
     * 
     */
    public void cancelTipoContratoEncargo() {
        beanTipoContratoEncargo.resetTo();
    }

    /**
     * Carrega o nome da Moeda e a lista de TipoContratoEncargo.
     * 
     * @param e
     *            - evento de alteracao de valor
     */
    public void findTipoContratoEncargos(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        beanTipoContratoEncargo.setNomeTipoContratoSelected(value);

        Long idTipoContrato = beanTipoContratoEncargo.getTipoContratoMap().get(
                value);

        if (idTipoContrato != null) {
            TipoContrato tipoContrato = tipoContratoService
                    .findTipoContratoById(idTipoContrato);
            if (tipoContrato.getMoeda() != null) {
                beanTipoContratoEncargo.setNomeMoeda("("
                        + tipoContrato.getMoeda().getSiglaMoeda() + ")");
            } else {
                beanTipoContratoEncargo.setNomeMoeda("");
            }

            beanTipoContratoEncargo.setResultList(tipoContrato
                    .getTipoContratoEncargos());
        }
    }

    /**
     * Popula a lista de moedas para combobox da natureza.
     * 
     * @param moedas
     *            lista de moedas.
     * 
     */
    private void loadMoedaList(final List<Moeda> moedas) {

        Map<String, Long> moedaMap = new HashMap<String, Long>();
        List<String> moedaList = new ArrayList<String>();

        for (Moeda moeda : moedas) {
            moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            moedaList.add(moeda.getNomeMoeda());
        }

        bean.setMoedaMap(moedaMap);
        bean.setMoedaList(moedaList);
    }

    /**
     * Popula a lista de tipoContrato para combobox da natureza.
     * 
     * @param tipoContratos
     *            lista de tipoContrato.
     * 
     */
    private void loadTipoContratoList(final List<TipoContrato> tipoContratos) {

        Map<String, Long> tipoContratoMap = new HashMap<String, Long>();
        List<String> tipoContratoList = new ArrayList<String>();

        for (TipoContrato tipoContrato : tipoContratos) {
            tipoContratoMap.put(tipoContrato.getNomeTipoContrato(),
                    tipoContrato.getCodigoTipoContrato());
            tipoContratoList.add(tipoContrato.getNomeTipoContrato());
        }

        beanTipoContratoEncargo.setTipoContratoMap(tipoContratoMap);
        beanTipoContratoEncargo.setTipoContratoList(tipoContratoList);
    }

}