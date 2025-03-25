package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IClientePipedriveService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.impl.PaymentConditionService;
import com.ciandt.pms.control.jsf.bean.ClienteBean;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.integration.vo.OrganizacaoPipedrive;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ClientePipedrive;
import com.ciandt.pms.model.vo.PaymentCondition;
import com.ciandt.pms.util.NumberUtil;
import org.apache.commons.lang.StringUtils;
import org.richfaces.component.html.HtmlInputText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Define o Controller da entidade.
 *
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.CLIENT_ENTITY:VW", "BOF.CLIENT_ENTITY:ED", "BOF.CLIENT_ENTITY:DE", "BOF.CLIENT_ENTITY:CR" })
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    /** outcome tela inclusao da entidade. */
    private static final String OUTCOME_CLIENTE_ADD = "cliente_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_CLIENTE_EDIT = "cliente_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_CLIENTE_DELETE = "cliente_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_CLIENTE_RESEARCH = "cliente_research";

    /**outcome tela pesquisa da entidade.*/
    private static final String OUTCOME_CLIENTE_VIEW = "cliente_view";

    /**
     * Instancia do servico.
     */
    @Autowired
    private IClienteService clienteService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private ClienteBean bean = null;

//    @Autowired
//    private PipedriveService pipedriveService;

    @Autowired
    private IClientePipedriveService clientePipedriveService;

    @Autowired
    private PaymentConditionService paymentConditionService;

    private HtmlInputText nomeCliente;

    public HtmlInputText getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(HtmlInputText nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    /**
     * @return the bean
     */
    public ClienteBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final ClienteBean bean) {
        this.bean = bean;
    }

    public Boolean atualizaCliente() {

        return true;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     *
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();
        this.loadClientePaiList(clienteService.findClienteAllClientePai());
//        this.loadOrganizacaoPipedrive();
        bean.setSuggestionsListInAtivo(Constants.ACTIVE_INACTIVE_VALUES);

        return OUTCOME_CLIENTE_RESEARCH;
    }


    public String prepareView() {
        findById(bean.getCurrentRowId());
        findOrganizationById();
        Cliente cliente = bean.getTo();

        if (cliente != null) {
            cliente = clienteService.findClienteById(cliente.getCodigoCliente());
            if (cliente.getCodigoAgenteMega() != null ) {
                this.findPaymentConditionByAgentCodeAndCompanyCode();
            }
            bean.setTo(cliente);
        }

        return OUTCOME_CLIENTE_VIEW;
    }


    /**
     * Prepara a tela de insercao da entidade.
     *
     * @return pagina de cria��o de cliente
     */
    public String prepareCreate() {
        bean.resetTo();
        loadClientePaiList(clienteService.findClienteAllClientePai());
        bean.setConditionsPayment(new ArrayList<SelectItem>());
//        this.loadOrganizacaoPipedrive();

        bean.setIsUpdate(Boolean.FALSE);
        bean.setHasRelationship(Boolean.FALSE);

        return OUTCOME_CLIENTE_ADD;
    }

    /**
     * Insere uma entidade.
     *
     * @return pagina de cria��o de cliente
     */
    public String create() {
        Cliente cliente = bean.getTo();
        if (!isValidCnpj(cliente)) {
            return null;
        }

        if (hasValidAgent()) {
            return null;
        }

        if (isInvalidCodigoOracleTaxpayerCustomer()) {
            Messages.showError("create",
                    Constants.DEFAULT_MSG_ERROR_HAS_INVALID_ORACLE_TAXPAYER);
            return null;
        }

        if (isExistingCodigoOracleTaxpayerCustomer(cliente, "create")) {
            return null;
        }

        cliente.setIndicadorAtivo(Constants.ACTIVE);

        Long clientePai = bean.getClientePaiMap().get(
                bean.getClientePaiSelected());
        if ((clientePai != null)) {
            cliente.setCliente(clienteService.findClienteById(clientePai));
        }

//        Long organizacaoPipedrive = bean.getOrganizacaoPipedriveMap().(bean.getOrganizacaoPipedriveSelected());

        clienteService.createCliente(cliente);
        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                Constants.ENTITY_NAME_CLIENTE);
        bean.resetTo();

        // atualiza o combo com o novo cliente criado.
        loadClientePaiList(clienteService.findClienteAllClientePai());

        return OUTCOME_CLIENTE_ADD;
    }

    /**
     * Prepara a tela de edicao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareUpdate() {
        loadClientePaiList(clienteService.findClienteAllClientePai());
        bean.setSuggestionsListInAtivo(Constants.ACTIVE_INACTIVE_VALUES);
        bean.setConditionsPayment(new ArrayList<SelectItem>());
        bean.setMegaAgentName("");
        findById(bean.getCurrentRowId());

        if (bean.getTo().getDealFiscals().isEmpty()) {
            bean.setHasRelationship(Boolean.FALSE);
        } else {
            bean.setHasRelationship(Boolean.TRUE);
        }

        if(bean.getTo().getCodigoAgenteMega() != null && bean.getClientePaiSelected() != null) {
            this.findPaymentConditionByAgentCodeAndCompanyCode();
        }

        bean.setIsUpdate(Boolean.TRUE);

        return OUTCOME_CLIENTE_EDIT;
    }

    /**
     * Executa um update da entidade.
     *
     * @return pagina de destino
     *
     */
    public String update() {
        Cliente cliente = bean.getTo();

        if (!isValidCnpj(cliente)) {
            return null;
        }

        if (hasValidAgent()) {
            return null;
        }

        if (isInvalidCodigoOracleTaxpayerCustomer()) {
            Messages.showError("update",
                    Constants.DEFAULT_MSG_ERROR_HAS_INVALID_ORACLE_TAXPAYER);
            return null;
        }

        if (isExistingCodigoOracleTaxpayerCustomer(cliente, "update")) {
            return null;
        }

        Long clientePai = bean.getClientePaiMap().get(
                bean.getClientePaiSelected());
        // se clientePai n�o for null, atribui o ClientePai e esse Cliente se
        // torna um ClienteFilho
        if ((clientePai != null)) {
            cliente.setCliente(clienteService.findClienteById(clientePai));
        } else {
            cliente.setCliente(null);
        }

        try {
            clienteService.updateCliente(cliente);

        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage(), new Object[] {
                    Constants.ENTITY_NAME_CLIENTE,
                    Constants.ENTITY_NAME_CLIENTE });

            return null;
        }

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_CLIENTE);

        bean.resetTo();
        findByFilter();

        // atualiza o combo com o novo cliente alterado
        loadClientePaiList(clienteService.findClienteAllClientePai());

        return OUTCOME_CLIENTE_RESEARCH;
    }

    private Boolean hasValidAgent(){
        if (!bean.getHasValidAgent() && bean.getTo().getCodigoAgenteMega() != null){
            Messages.showError("update",
                    Constants.DEFAULT_MSG_ERROR_HAS_INVALID_AGENT);

            return true;
        }
        return false;
    }

    private boolean isInvalidCodigoOracleTaxpayerCustomer() {
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = pattern.matcher(bean.getTo().getCodigoOracleTaxpayerCustomer());
        return matcher.find();
    }

    private boolean isExistingCodigoOracleTaxpayerCustomer(Cliente cliente, String methodName) {
        String taxpayerCode = cliente.getCodigoOracleTaxpayerCustomer();
        if (taxpayerCode != null && !taxpayerCode.isEmpty()) {
            List<Cliente> clienteByCodigoOracleTaxpayerCustomer = clienteService.findClienteByCodigoOracleTaxpayerCustomer(taxpayerCode);
            for (Cliente clienteFound : clienteByCodigoOracleTaxpayerCustomer) {
                log.info("Encontrado cliente com taxpayer '" + cliente.getCodigoOracleTaxpayerCustomer() + "': " + clienteFound.getNomeCliente());
            }
            if (!clienteByCodigoOracleTaxpayerCustomer.isEmpty() && !clienteByCodigoOracleTaxpayerCustomer.get(0).getCodigoCliente().equals(cliente.getCodigoCliente())) {
                Messages.showError(methodName,
                        Constants.DEFAULT_MSG_ERROR_HAS_EXISTING_ORACLE_TAXPAYER_CUSTOMER,
                        clienteByCodigoOracleTaxpayerCustomer.get(0).getNomeCliente());
                return true;
            }
        }
        return false;
    }

    /**
     * Prepara a tela de remocao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareRemove() {
        findById(bean.getCurrentRowId());
        return OUTCOME_CLIENTE_DELETE;
    }

    /**
     * Deleta uma entidade.
     *
     * @return pagina de destino
     *
     */
    public String remove() {

        Cliente cl = clienteService.findClienteById(bean.getTo()
                .getCodigoCliente());

        try {
            clienteService.removeCliente(cl);

        } catch (IntegrityConstraintException ice) {

            if (cl.getClientes().size() > 0) {
                Messages.showError("remove", ice.getMessage(), new Object[] {
                        Constants.ENTITY_NAME_CLIENTE,
                        Constants.ENTITY_NAME_CLIENTE });
            } else {
                Messages.showError("remove", ice.getMessage(), new Object[] {
                        Constants.ENTITY_NAME_CLIENTE,
                        Constants.ENTITY_NAME_MSA });
            }
            return null;
        } catch (DataIntegrityViolationException die) {
            Messages.showError("remove",
                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_CLIENTE);
        }

        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_CLIENTE);

        bean.resetTo();
        findByFilter();

        // atualiza o combo sem o cliente removido.
        loadClientePaiList(clienteService.findClienteAllClientePai());

        return OUTCOME_CLIENTE_RESEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     *
     * @param id
     *            da entidade.
     *
     */
    public void findById(final Long id) {
        bean.setTo(clienteService.findClienteById(id));
        Cliente to = bean.getTo();
        if (to != null) {
            if (to.getCliente() != null) {
                bean.setClientePaiSelected(to.getCliente().getNomeCliente());
            } else {
                bean.setClientePaiSelected("");
            }

            if (to.getCodigoOrganizacaoPipedrive() != null) {

                ClientePipedrive clientePipedrive = clientePipedriveService.findById(bean.getTo().getCodigoOrganizacaoPipedrive());
                if (null != clientePipedrive) {
                    OrganizacaoPipedrive organizacaoPipedrive = new OrganizacaoPipedrive(clientePipedrive.getCodigoClientePipedrive(), clientePipedrive.getNomeClientePipedrive());;
                    if (organizacaoPipedrive != null) {
                        bean.setOrganizationName(organizacaoPipedrive.getName());
                    } else {
                        bean.setOrganizationName("");
                    }
                } else {
                    bean.setOrganizationName("");
                }
            }
        } else {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     *
     */
    public void findByFilter() {
        Cliente filter = bean.getFilter();

        Long clientePai = bean.getClientePaiMap().get(
                bean.getClientePaiSelectedFilter());
        if ((clientePai != null)) {
            filter.setCliente(clienteService.findClienteById(clientePai));
        } else {
            filter.setCliente(null);
        }

        bean.setResultList(clienteService.findClienteFilhoByFilter(filter));
        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    /**
     * Popula a lista de clientes para combobox do cliente pai.
     *
     * @param clientes
     *            lista de clientes.
     *
     */
    private void loadClientePaiList(final List<Cliente> clientes) {

        Map<String, Long> clientePaiMap = new HashMap<String, Long>();
        List<String> clientePaiList = new ArrayList<String>();

        for (Cliente cliente : clientes) {
            clientePaiMap.put(cliente.getNomeCliente(), cliente
                    .getCodigoCliente());
            clientePaiList.add(cliente.getNomeCliente());
        }

        bean.setClientePaiMap(clientePaiMap);
        bean.setClientePaiList(clientePaiList);
    }

    /**
     * Verifica se o valor do atributo clientePai � valido. Se o valor � nulo,
     * indica que � um "Cliente Pai", ent�o o valor � valido. Se o valor n�o �
     * nulo e existe no clientePaiMap, indica que � um cliente filho, ent�o o
     * valor � valido.
     *
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateClientePai(final FacesContext context,
            final UIComponent component, final Object value) {

        Long clientePai = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            clientePai = bean.getClientePaiMap().get(strValue);
            if (clientePai == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * Busca todos os itens de uma TabelaPreco.
     *
     * @param e - evento de mudan�a.
     */
    public void setTipoRegistroFiscal(final ValueChangeEvent e) {
        bean.getTo().setTipoRegistroFiscal((String) e.getNewValue());
    }

    /**
     * Valida o CNPJ.
     *
     * @param cli
     *            - Cliente que se deseja validar o CNPJ.
     * @return true se o CNPJ for v�lido. false caso contr�rio
     */
    private boolean isValidCnpj(final Cliente cli) {
        if (!StringUtils.isEmpty(cli.getCodigoCnpj())) {
            //verifica se � do tipo CNPJ
            if ("C".equals(cli.getTipoRegistroFiscal())) {
                if (!NumberUtil.isValidCnpj(cli.getCodigoCnpj())) {
                    Messages.showError("isValidCnpj",
                            Constants.DEFAULT_MSG_ERROR_INVALID_CNPJ);
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean findOrganizationById() {

        ClientePipedrive clientePipedrive = null;

        if (null != bean.getTo().getCodigoOrganizacaoPipedrive()) {
            clientePipedrive = clientePipedriveService.findById(bean.getTo().getCodigoOrganizacaoPipedrive());
        } else if (clientePipedrive != null) {
            OrganizacaoPipedrive organizacaoPipedrive = new OrganizacaoPipedrive(clientePipedrive.getCodigoClientePipedrive(), clientePipedrive.getNomeClientePipedrive());
            bean.setOrganizationName(organizacaoPipedrive.getName());
        } else {
            if (bean.getTo().getCodigoOrganizacaoPipedrive() != null) {
                bean.setOrganizationName("No organization found with this code");
            } else {
                bean.setOrganizationName("");
            }
        }
        return true;
    }

    public void clientePaiListener(final ValueChangeEvent e) {
        String nomeClientPai = (String) e.getNewValue();

        if (nomeClientPai != null) {

            bean.setClientePaiSelected(nomeClientPai);
        }

        if(bean.getTo().getCodigoAgenteMega() != null) {
            this.findPaymentConditionByAgentCodeAndCompanyCode();
        }
    }


    public Boolean findPaymentConditionByAgentCodeAndCompanyCode() {

//        try{
            if(bean.getTo().getCodigoAgenteMega() == null) {
                bean.setMegaAgentName("");
                bean.setConditionsPayment(new ArrayList<SelectItem>());
            }
            if (bean.getClientePaiSelected() != null && !StringUtils.isEmpty(bean.getClientePaiSelected()) && bean.getTo().getCodigoAgenteMega() != null) {
                Long companyCode = clienteService.getCompanyCodeByClientName(bean.getClientePaiSelected());
                Collection<PaymentCondition> paymentConditions = paymentConditionService.findByClientAndCompany(bean.getTo().getCodigoAgenteMega(), companyCode);

                if (paymentConditions == null || paymentConditions.isEmpty()) {
                    bean.setMegaAgentName(BundleUtil.getBundle(Constants.MSG_PAYMENT_TERMS_NOT_FOUND));
                    bean.setHasValidAgent(false);
                    bean.setConditionsPayment(new ArrayList<SelectItem>());
                    return true;
                }

                List<SelectItem> conditionPaymentList = new ArrayList<SelectItem>();
                for (PaymentCondition paymentCondition : paymentConditions) {
                    conditionPaymentList.add(new SelectItem(paymentCondition.getName()));
                    bean.setMegaAgentName(paymentCondition.getAgentName());
                    bean.setHasValidAgent(true);
                }

                bean.setConditionsPayment(conditionPaymentList);
            }
//        } catch (IOException e) {
//            Messages.showError("paymentTerms",
//                    Constants.DEFAULT_MSG_ERROR_LOAD_PAYMENT_TERMS,
//                    Constants.ENTITY_NAME_CLIENTE);
//        }

        return true;
    }

    public boolean findOrganization() {
        String searchTerm = bean.getOrganizationSearchTerm();

            if (searchTerm == null) {
            bean.resetOrganizacaoPipedrives();

            return false;
        }

        List<ClientePipedrive> clientePipedrives = clientePipedriveService.findByIdOrName(searchTerm);
        List<OrganizacaoPipedrive> organizacaoPipedrives = new ArrayList<OrganizacaoPipedrive>();
        OrganizacaoPipedrive organizacaoPipedrive = null;
        for (ClientePipedrive clientePipedrive : clientePipedrives) {
            organizacaoPipedrive = new OrganizacaoPipedrive(clientePipedrive.getCodigoClientePipedrive(), clientePipedrive.getNomeClientePipedrive());
            organizacaoPipedrives.add(organizacaoPipedrive);
        }
        bean.setOrganizacaoPipedrives(organizacaoPipedrives);

        return true;
    }

    public void toggleOrganizationSearch() {
        bean.setShowOrganizationSearch(!bean.getShowOrganizationSearch());
    }

}
