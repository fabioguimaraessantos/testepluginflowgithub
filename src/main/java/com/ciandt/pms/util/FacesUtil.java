package com.ciandt.pms.util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 
 * Utility class used to handle FacesContext related methods. Examples would be
 * getting parameters, adding error messages, etc.
 * 
 * @since 30/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public final class FacesUtil {

    /**
     * Construtor padrão.
     */
    private FacesUtil() {
    }

    /**
     * Pega o parametro da request corrente.
     * 
     * @param name
     *            nome do parametro
     * 
     * @return retorna o valor do parametro no formato de string
     */
    public static String getRequestParameter(final String name) {
        return (String) FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get(name);
    }

    /**
     * Retorna o url da aplicação no servidor.
     * 
     * @return retornauma string que representa o url
     */
    public static String getAppServerPath() {

        HttpServletRequest request = getCurrentRequest();

        return request.getScheme() + "://" + request.getLocalAddr() + ":"
                + request.getLocalPort() + request.getContextPath();
    }

    /**
     * Method to get a managed bean from the context This approach is not ideal,
     * as injection should be used instead But in some cases (ie: validators and
     * converters) there is no other way to get at a bean.
     * 
     * @param beanName
     *            to get
     * @return the bean
     */
    @SuppressWarnings("deprecation")
    public static Object getManagedBean(final String beanName) {
        FacesContext context = FacesContext.getCurrentInstance();

        return context.getApplication().createValueBinding(
                "#{" + beanName + "}").getValue(context);
    }

    /**
     * Method to return the value of an f:attribute tag, based on the passed
     * name.
     * 
     * @param event
     *            that has the f:attribute associated with it
     * @param name
     *            of the tag
     * @return value of the tag
     */
    public static Object getFAttribute(final ActionEvent event,
            final String name) {
        return (Object) event.getComponent().getAttributes().get(name);
    }

    /**
     * Method to get a parameter from the HTTP request for the passed key.
     * 
     * @param key
     *            of the parameter
     * @return the parameter, or null on error or if not found
     */
    public static String getURLParameter(final String key) {
        try {
            HttpServletRequest req = getCurrentRequest();
            return req.getParameter(key);
        } catch (Exception paramError) {
            paramError.printStackTrace();
        }

        return null;
    }

    /**
     * Method to safely get the current HTTP request.
     * 
     * @return request, or null on error
     */
    public static HttpServletRequest getCurrentRequest() {
        try {
            return (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();
        } catch (Exception requestError) {
            requestError.printStackTrace();
        }

        return null;
    }

    /**
     * Method to safely get the current HTTP response.
     * 
     * @return response, or null on error
     */
    public static HttpServletResponse getCurrentResponse() {
        try {
            return (HttpServletResponse) FacesContext.getCurrentInstance()
                    .getExternalContext().getResponse();
        } catch (Exception responseError) {
            responseError.printStackTrace();
        }

        return null;
    }

    /**
     * Method to get a context-param value from the web.xml, based on the passed
     * name.
     * 
     * @param parameter
     *            name to get
     * @return value of the parameter, or null if not found
     */
    public static String getContextParameter(final String parameter) {
        try {
            // Get the servlet context based on the faces context
            ServletContext sc = (ServletContext) FacesContext
                    .getCurrentInstance().getExternalContext().getContext();

            // Return the value read from the parameter
            return sc.getInitParameter(parameter);
        } catch (Exception failedParameter) {
            failedParameter.printStackTrace();
        }

        return null;
    }

    /**
     * Pega o endereço ip de quem está fazendo a requisição (ip do cliente).
     * 
     * @return retorna o IP em formato de string
     */
    public static String getRemoteIpAddress() {
        ExternalContext context = FacesContext.getCurrentInstance()
                .getExternalContext();

        return ((HttpServletRequest) context.getRequest()).getRemoteAddr();
    }

    /**
     * Pega o caminho real do arquivo.
     * 
     * @param file
     *            - nome do arquivo.
     * 
     * @return retorna o caminho real do arquivo
     */
    @SuppressWarnings("deprecation")
    public static String getRealPath(final String file) {
        return getCurrentRequest().getRealPath(file);
    }

    /**
     * @param components - Lista de components da árvore DOM
     * @param id - Id do componente a ser encontrado
     * @return Componente.
     */
    private static UIComponent getComponent(List<UIComponent> components, String id){
        UIComponent result = null;
        for (UIComponent component : components) {
            if(component.getChildren() != null && !component.getChildren().isEmpty())
                result = getComponent(component.getChildren(), id);

            if(result != null)
                return result;

            if(id.equals(component.getId()))
                return component;

        }

        return null;
    }

    /**
     * @param root - Componente Root do Contexto.
     * @param id - Id do componente a ser encontrado.
     * @return Componente.
     */
    public static UIComponent getComponentById(UIViewRoot root, String id){
        return getComponent(root.getChildren(), id);
    }
}