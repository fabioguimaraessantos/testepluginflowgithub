package org.ajax4jsf.webapp;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.config.WebXMLParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.richfaces.VersionBean;

/**
 * Classe do Richfaces alterada para corrigir problemas no Firefox 11.
 *
 * @since 04/04/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 *
 */
public class WebXml extends WebXMLParser implements Serializable {

    /** CONTEXT_ATTRIBUTE. */
    public static final String CONTEXT_ATTRIBUTE =
            "org/ajax4jsf/webapp/WebXml.getName()";
    /** serialVersionUID. */
    private static final long serialVersionUID = 0x828122b4566ea857L;
    /** _log. */
    static final Logger _log = LogManager.getLogger("org/ajax4jsf/webapp/WebXml");
    /** RESOURCE_URI_PREFIX. */
    public static final String RESOURCE_URI_PREFIX = "a4j";
    /** GLOBAL_RESOURCE_URI_PREFIX. */
    public static final String GLOBAL_RESOURCE_URI_PREFIX = "a4j/g";
    /** SESSION_RESOURCE_URI_PREFIX. */
    public static final String SESSION_RESOURCE_URI_PREFIX = "a4j/s";
    /** RESOURCE_URI_PREFIX_VERSIONED. */
    public static final String RESOURCE_URI_PREFIX_VERSIONED;
    /** GLOBAL_RESOURCE_URI_PREFIX_VERSIONED. */
    public static final String GLOBAL_RESOURCE_URI_PREFIX_VERSIONED;
    /** SESSION_RESOURCE_URI_PREFIX_VERSIONED. */
    public static final String SESSION_RESOURCE_URI_PREFIX_VERSIONED;
    /** RESOURCE_URI_PREFIX_PARAM. */
    public static final String RESOURCE_URI_PREFIX_PARAM =
            "org.ajax4jsf.RESOURCE_URI_PREFIX";
    /** GLOBAL_RESOURCE_URI_PREFIX_PARAM. */
    public static final String GLOBAL_RESOURCE_URI_PREFIX_PARAM =
            "org.ajax4jsf.GLOBAL_RESOURCE_URI_PREFIX";
    /** SESSION_RESOURCE_URI_PREFIX_PARAM. */
    public static final String SESSION_RESOURCE_URI_PREFIX_PARAM =
            "org.ajax4jsf.SESSION_RESOURCE_URI_PREFIX";
    /** _resourcePrefix. */
    String _resourcePrefix;
    /** _globalResourcePrefix. */
    String _globalResourcePrefix;
    /** _sessionResourcePrefix. */
    String _sessionResourcePrefix;
    /** _prefixMapping. */
    protected boolean _prefixMapping;

    /**
     * WebXml.
     */
    public WebXml() {
        _resourcePrefix = "a4j";
        _prefixMapping = false;
    }

    /**
     * getInstance.
     * @return WebXml
     */
    public static WebXml getInstance() {
        return getInstance(FacesContext.getCurrentInstance());
    }

    /**
     * getInstance.
     * @param context context
     * @return WebXml
     */
    public static WebXml getInstance(FacesContext context) {
        WebXml webXml =
                (WebXml) context.getExternalContext().getApplicationMap().get(
                        CONTEXT_ATTRIBUTE);
        return webXml;
    }

    /**
     * init.
     * @param context context
     * @param filterName filterName
     */
    public void init(ServletContext context, String filterName)
            throws ServletException {
        super.init(context, filterName);
        setupResourcePrefixes(context);
        context.setAttribute(CONTEXT_ATTRIBUTE, this);
    }

    /**
     * getFacesResourceURL.
     * @param context context
     * @param Url Url
     * @param isGlobal isGlobal
     * @return string
     */
    public String getFacesResourceURL(FacesContext context, String Url,
            boolean isGlobal) {
        StringBuffer buf = new StringBuffer();
        buf.append(
                isGlobal ? getGlobalResourcePrefix()
                        : getSessionResourcePrefix()).append(Url);
        int index;
        if (isPrefixMapping()) {
            buf.insert(0, getFacesFilterPrefix());
        } else if ((index = buf.indexOf("?")) >= 0) {
            buf.insert(index, getFacesFilterSuffix());
        } else {
            buf.append(getFacesFilterSuffix());
        }
        String resourceURL =
                context.getApplication().getViewHandler().getResourceURL(
                        context, buf.toString());
        return resourceURL;
    }

    /**
     * @deprecated Method getFacesResourceURL is deprecated
     */

    public String getFacesResourceURL(FacesContext context, String Url) {
        return getFacesResourceURL(context, Url, false);
    }

    /**
     * getFacesResourceKey.
     * @param resourcePath resourcePath
     * @return string
     */
    public String getFacesResourceKey(String resourcePath) {
        int jsesionidStart;
        if ((jsesionidStart = resourcePath.lastIndexOf(";jsessionid")) >= 0) {
            resourcePath = resourcePath.substring(0, jsesionidStart);
        }
        String resourcePrefix = getResourcePrefix();
        if (isPrefixMapping()) {
            String facesFilterPrefix = getFacesFilterPrefix();
            if (resourcePath.startsWith(facesFilterPrefix)) {
                String sessionResourcePrefix = getSessionResourcePrefix();
                if (resourcePath.startsWith(sessionResourcePrefix,
                        facesFilterPrefix.length())) {
                    return resourcePath.substring(facesFilterPrefix.length()
                            + sessionResourcePrefix.length());
                }
                String globalResourcePrefix = getGlobalResourcePrefix();
                if (!sessionResourcePrefix.equals(globalResourcePrefix)
                        && resourcePath.startsWith(globalResourcePrefix,
                                facesFilterPrefix.length())) {
                    return resourcePath.substring(facesFilterPrefix.length()
                            + globalResourcePrefix.length());
                }
                if (!globalResourcePrefix.equals(resourcePrefix)
                        && resourcePath.startsWith(resourcePrefix,
                                facesFilterPrefix.length())) {
                    return resourcePath.substring(facesFilterPrefix.length()
                            + resourcePrefix.length());
                }
            }
        } else {
            String sessionResourcePrefix = getSessionResourcePrefix();
            if (resourcePath.startsWith(sessionResourcePrefix)) {
                return resourcePath
                        .substring(sessionResourcePrefix.length(), resourcePath
                                .length()
                                - getFacesFilterSuffix().length());
            }
            String globalResourcePrefix = getGlobalResourcePrefix();
            if (!sessionResourcePrefix.equals(globalResourcePrefix)
                    && resourcePath.startsWith(globalResourcePrefix)) {
                return resourcePath
                        .substring(globalResourcePrefix.length(), resourcePath
                                .length()
                                - getFacesFilterSuffix().length());
            }
            if (!globalResourcePrefix.equals(resourcePrefix)
                    && resourcePath.startsWith(resourcePrefix)) {
                return resourcePath
                        .substring(resourcePrefix.length(), resourcePath
                                .length()
                                - getFacesFilterSuffix().length());
            }
        }
        return null;
    }

    /**
     * getFacesResourceKey.
     * @param request request
     * @return string
     */
    public String getFacesResourceKey(HttpServletRequest request) {
        String resourcePath =
                request.getRequestURI().substring(
                        request.getContextPath().length());
        String s = getFacesResourceKey(resourcePath);
        if (null != s) {
            try {
                return URLDecoder.decode(s, "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                // Doesn't happen.
            }
        }
        return null;
    }

    /**
     * isFacesRequest.
     * @param request request
     * @return string
     */
    public boolean isFacesRequest(HttpServletRequest request) {
        return true;
    }

    /**
     * @deprecated Method getResourcePrefix is deprecated
     */

    public String getResourcePrefix() {
        return _resourcePrefix;
    }

    /**
     * getGlobalResourcePrefix.
     * @return string
     */
    public String getGlobalResourcePrefix() {
        return _globalResourcePrefix;
    }

    /**
     * getSessionResourcePrefix.
     * @return string
     */
    public String getSessionResourcePrefix() {
        return _sessionResourcePrefix;
    }

    /**
     * isPrefixMapping.
     * @return boolean
     */
    public boolean isPrefixMapping() {
        return _prefixMapping;
    }

    /**
     * @deprecated Method setResourcePrefix is deprecated
     */

    void setResourcePrefix(String resourcePrefix) {
        _resourcePrefix = resourcePrefix;
    }

    /**
     * setGlobalResourcePrefix.
     * @param resourcePrefix resourcePrefix
     */
    void setGlobalResourcePrefix(String resourcePrefix) {
        _globalResourcePrefix = resourcePrefix;
    }

    /**
     * setSessionResourcePrefix.
     * @param resourcePrefix resourcePrefix
     */
    void setSessionResourcePrefix(String resourcePrefix) {
        _sessionResourcePrefix = resourcePrefix;
    }

    /**
     * setupResourcePrefixes.
     * @param context context
     */
    void setupResourcePrefixes(ServletContext context) {
        String globalResourcePrefix =
                context
                        .getInitParameter("org.ajax4jsf.GLOBAL_RESOURCE_URI_PREFIX");
        String sessionResourcePrefix =
                context
                        .getInitParameter("org.ajax4jsf.SESSION_RESOURCE_URI_PREFIX");
        String resourcePrefix =
                context.getInitParameter("org.ajax4jsf.RESOURCE_URI_PREFIX");
        if (null != resourcePrefix) {
            if (globalResourcePrefix == null) {
                globalResourcePrefix = resourcePrefix;
            }
            if (sessionResourcePrefix == null) {
                sessionResourcePrefix = resourcePrefix;
            }
        } else {
            resourcePrefix = RESOURCE_URI_PREFIX_VERSIONED;
        }
        if (globalResourcePrefix == null) {
            globalResourcePrefix = GLOBAL_RESOURCE_URI_PREFIX_VERSIONED;
        }
        if (sessionResourcePrefix == null) {
            sessionResourcePrefix = SESSION_RESOURCE_URI_PREFIX_VERSIONED;
        }
        if (null != getFacesFilterPrefix()) {
            _prefixMapping = true;
            if (getFacesFilterPrefix().endsWith("/")) {
                setGlobalResourcePrefix(globalResourcePrefix);
                setSessionResourcePrefix(sessionResourcePrefix);
                setResourcePrefix(resourcePrefix);
            } else {
                setGlobalResourcePrefix((new StringBuilder()).append("/")
                        .append(globalResourcePrefix).toString());
                setSessionResourcePrefix((new StringBuilder()).append("/")
                        .append(sessionResourcePrefix).toString());
                setResourcePrefix((new StringBuilder()).append("/").append(
                        resourcePrefix).toString());
            }
        } else if (null != getFacesFilterSuffix()) {
            _prefixMapping = false;
            setResourcePrefix((new StringBuilder()).append("/").append(
                    resourcePrefix).toString());
            setGlobalResourcePrefix((new StringBuilder()).append("/").append(
                    globalResourcePrefix).toString());
            setSessionResourcePrefix((new StringBuilder()).append("/").append(
                    sessionResourcePrefix).toString());
        }
    }

    static {
        VersionBean versionBean = new VersionBean();
        org.richfaces.VersionBean.Version version = versionBean.getVersion();
        String suffix =
                (new StringBuilder()).append("/").append(version.getMajor())
                        .append("_").append(version.getMinor()).append("_")
                        .append(version.getRevision()).toString();
        RESOURCE_URI_PREFIX_VERSIONED =
                (new StringBuilder()).append("a4j").append(suffix).toString();
        GLOBAL_RESOURCE_URI_PREFIX_VERSIONED =
                (new StringBuilder()).append("a4j/g").append(suffix).toString();
        SESSION_RESOURCE_URI_PREFIX_VERSIONED =
                (new StringBuilder()).append("a4j/s").append(suffix).toString();
    }
}
