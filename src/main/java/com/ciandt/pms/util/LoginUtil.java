package com.ciandt.pms.util;

import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.model.Pessoa;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;

import javax.faces.context.FacesContext;


/**
 * 
 * A classe LoginUtil proporciona as funcionalidades úteis referentes ao login.
 * 
 * @since 15/10/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public final class LoginUtil {

    /** Construtor privado. */
    private LoginUtil() {
    }

    /**
     * Pega o username do usuario logado no sistema.
     * 
     * @return username do usuario.
     */
    public static String getLoggedUsername() {
        FacesContext context = FacesContext.getCurrentInstance();
        // Essa condição é necessária para os testes unitarios não falharem.
        // Pois no testes unitarios não existe contexto.
        if (context != null) {
            // Pega o login do usuario logado no sistema
            return (String) context.getExternalContext().getSessionMap().get(
                    "SPRING_SECURITY_LAST_USERNAME");
        }

        return "";
    }

    /**
     * Retorna a entidade Pessoa referente ao usuario, logado no sistema.
     * 
     * @return retorna uma instancia de Pessoa.
     */
    public static Pessoa getLoggedUser() {
        String username = getLoggedUsername();

        IPessoaService pessoaService =
                (IPessoaService) SpringUtil.getBean("pessoaService");

        return pessoaService.findPessoaByLogin(username);
    }

    /**
     * Retorna um array contendo as authorities do usuario, logado no sistema.
     * 
     * @return retorna uma instancia de Pessoa.
     */
    public static GrantedAuthority[] getLoggedUserAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();
    }

    /**
     * Retorna um booleano indicando se o usuario corrente possui a authority
     * passada por parametro.
     * 
     * @param role
     *            String referente a Authority do usuario
     * 
     * @return retorna true se a role passada contiver nas Authorities do
     *         usuario corrente.
     */
    public static boolean isCurrentUserInRole(final String role) {
        for (GrantedAuthority userAuthority : LoginUtil
                .getLoggedUserAuthorities()) {
            if (userAuthority.getAuthority().equals(role)) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
    }
}
