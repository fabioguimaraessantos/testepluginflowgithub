package com.ciandt.pms.control.servlet;

import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.stereotype.Component;
 
/**
 * 
 * A classe LoginRedirectFilter proporciona as funcionalidades de filtro
 * referente ao redirecionamento ap�s o login no sistema.
 *
 * @since 19/05/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Component
public class LoginRedirectFilter implements Filter {
    
    /** Valor da chave de redirecionamento. */
    private static final String LAST_URL_REDIRECT_KEY = "REDIRECT_INDEX_URL";
 
    /** Valor da chave do usu�rio logado no sistema. */
    private static final String USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";
    
    /** 
     * Rediriciona ap�s o login para a p�gina inicial do sistema.
     * 
     *  @param request - ServletRequest
     *  
     *  @param response - ServletResponse
     *  
     *  @param chain - FilterChain
     *  
     *  @throws IOException lan�a a exception de IO
     *  
     *  @throws ServletException lan�a a exception de Servlet
     */
    public void doFilter(final ServletRequest request, final ServletResponse response, 
            final FilterChain chain) throws IOException, ServletException {
        
        HttpSession session = ((HttpServletRequest) request).getSession();
        String redirectUrl = (String) session.getAttribute(LAST_URL_REDIRECT_KEY);
        HttpServletResponse resp = (HttpServletResponse) response;
        
        //verifica se o usuario est� logado e existe url para redirecionar
        if (this.isAuthenticated() && (redirectUrl != null) && !redirectUrl.isEmpty()) {
            // remove a url como atributo da sess�o
            session.removeAttribute(LAST_URL_REDIRECT_KEY);
            
            //pega o username do usuario
            String username = (String) session.getAttribute(USERNAME_KEY);
            // caso usuario igual a system
            if (username.equals("system")) {
                //redireciona para pagina de gerenciamento de sess�o
                resp.sendRedirect("/pms/admin/sessions.jsf");
            } else {
                //caso seja um usu�rio normal, vau pra o home da aplica��o
                resp.sendRedirect(redirectUrl);
            }

        } else {
        	try {
				chain.doFilter(request, response);
			} catch (ServletException e) {
				// trata o erro de sessao expirada (timeout) - javax.faces.application.ViewExpiredException
				if (e.getCause() instanceof javax.faces.application.ViewExpiredException) {
					resp.sendRedirect("/pms/pages/public/sessionTimeout.jsf");
				} else {
					e.printStackTrace();
				}
			}
        }
    }
 
    /**
     * Verifica se a autentica��o foi realizada com sucesso.
     * 
     * @return retorna true se autenticado com sucesso, caso contrario false.
     */
    private boolean isAuthenticated() {
        boolean result = false;
        SecurityContext context = SecurityContextHolder.getContext();
        
        if (context instanceof SecurityContext) {
            Authentication authentication = context.getAuthentication();
            
            if (authentication instanceof AnonymousAuthenticationToken) {
                
                result = false;
                
            } else if (authentication instanceof Authentication) {
                
                result = true;
                
            }
        }
        return result;
    }
 
    /** 
     * Medodo executado no momento que o filtro � inicializado. 
     * 
     * @param filterConfig - configura��es iniciais
     * 
     * @throws ServletException lan�a a exception de Servlet
     * 
    */
    public void init(final FilterConfig filterConfig) throws ServletException { }
    
    /**
     * Executa no momento em que o filtro � finalizado.
     */
    public void destroy() { }
}