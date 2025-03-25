package com.ciandt.pms.util;

import org.springframework.security.util.EncryptionUtils;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * A classe RememberLoginUtil proporciona as funcionalidades de ... para ...
 * 
 * @since 02/06/2011
 * @author <a href="mailto:cmantovani@ciandt.com">Carlos Augusto Ribeiro
 *         Mantovani</a>
 * 
 */
public class RememberLoginUtil {

    /**
     * 
     */
    private static String chave = "YTpwpPa2kJ7WZbyTSBrajUlqtfRkoogS";

    /**
     * Cria os cookies para o remembeLogin.
     * 
     * @param user
     *            - login do usuario
     * @param pass
     *            - senha do usuario
     * @param rememberMe
     *            - checkbox de selecao
     * @param path
     *            - caminho do cookie
     * @param tempoVida
     *            - tempo de vida do cookie
     */
    public static void remember(final String user, final String pass,
            final String rememberMe, final String path, final Integer tempoVida) {
        // Set a cookie.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response =
                (HttpServletResponse) facesContext.getExternalContext()
                        .getResponse();
        Cookie userNameCookie = null;
        Cookie userPasswordCookie = null;
        Cookie rememberMeCookie = null;

        try {
            userNameCookie =
                    new Cookie("userName", EncryptionUtils.encrypt(chave, user));
            userPasswordCookie =
                    new Cookie("userPassword", EncryptionUtils.encrypt(chave,
                            pass));
            rememberMeCookie = new Cookie("rememberMe", rememberMe);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        userNameCookie.setPath(path);
        userPasswordCookie.setPath(path);
        rememberMeCookie.setPath(path);
        userNameCookie.setMaxAge(tempoVida);
        userPasswordCookie.setMaxAge(tempoVida);
        rememberMeCookie.setMaxAge(tempoVida * 2);

        response.addCookie(userNameCookie);
        response.addCookie(userPasswordCookie);
        response.addCookie(rememberMeCookie);

        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Marca como rememberMe e cria o cookie.
     * 
     * @param rememberMe
     *            - checkbox de selecao
     * @param path
     *            - caminho do cookie
     * @param tempoVida
     *            - tempo de vida do cookie
     */
    public static void setRememberMe(final String rememberMe,
            final String path, final Integer tempoVida) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response =
                (HttpServletResponse) facesContext.getExternalContext()
                        .getResponse();
        Cookie rememberMeCookie = null;
        rememberMeCookie = new Cookie("rememberMe", rememberMe);
        rememberMeCookie.setPath(path);
        rememberMeCookie.setMaxAge(tempoVida * 2);
        response.addCookie(rememberMeCookie);

        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Verifica se o usuario foi marcado como remember.
     * 
     * @return true - se foi marcado e, caso contrario, false
     */
    public static Boolean remembered() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request =
                (HttpServletRequest) facesContext.getExternalContext()
                        .getRequest();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userName")) {
                    return Boolean.valueOf(true);
                }
            }
        }
        return Boolean.valueOf(false);
    }

    /**
     * Retorna o nome de usuario gravado no cookie.
     * 
     * @return String - userName
     */
    public static String getUsername() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request =
                (HttpServletRequest) facesContext.getExternalContext()
                        .getRequest();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userName")) {
                    return EncryptionUtils.decrypt(chave, cookie.getValue());
                }
            }
        }
        return "";
    }

    /**
     * Retorna a senha de usuario gravada no cookie.
     * 
     * @return String - password
     */
    public static String getPassword() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request =
                (HttpServletRequest) facesContext.getExternalContext()
                        .getRequest();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userPassword")) {
                    return EncryptionUtils.decrypt(chave, cookie.getValue());
                }
            }
        }
        return "";
    }

    /**
     * Retorna a a selecao do checkbox salva no cookie.
     * 
     * @return Boolean - true se foi marcado e false, caso contrario
     */
    public static Boolean getRememberMe() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request =
                (HttpServletRequest) facesContext.getExternalContext()
                        .getRequest();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("rememberMe")) {
                    return Boolean.valueOf(cookie.getValue());
                }
            }
        }
        return Boolean.valueOf(false);
    }
}
