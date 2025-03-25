package com.ciandt.pms.authority;

import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IUserProfileService;
import com.ciandt.pms.model.vo.UserProfile;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.ldap.LdapAuthoritiesPopulator;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * A classe PmsAuthoritiesPopulator proporciona as funcionalidades de
 * das autoriza��es(Perfis) do us�rio no sistema.
 *
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * @since 03/01/2011
 */
public class PMSAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    /**
     * Instancia do servi�o PessoaService.
     */
    private IPessoaService pessoaService;

    private IUserProfileService userProfileService;

    /**
     * Construtor padr�o.
     *
     * @param pessoaService - instancia de PessoaService.
     */
    public PMSAuthoritiesPopulator(final IPessoaService pessoaService,
                                   final IUserProfileService userProfileService) {
        this.pessoaService = pessoaService;
        this.userProfileService = userProfileService;
    }

    /**
     * Retorna a lista de perfis(Autoriza��es) do usu�rio.
     *
     * @param userData dados do usu�rio
     * @param username login do usu�rio.
     * @return retorna um array de GrantedAuthority do usu�rio logado.
     */
    @Override
    public GrantedAuthority[] getGrantedAuthorities(
            final DirContextOperations userData, final String username) {

        Set<GrantedAuthority> userAuthorities = new HashSet<GrantedAuthority>();

        try {
            UserProfile userProfile = userProfileService.findByUserAndProfile(username, "PMS");
            if (userProfile != null) {
                Set<String> profiles = userProfileService.convertRolesToPMS(userProfile);

                for (String profile : profiles) {
                    userAuthorities.add(new GrantedAuthorityImpl(profile));
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! ERROR!");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            System.out.println("ERROR! ERROR! ERROR! ERROR! ERROR! ERROR! ERROR!");
        }

        return userAuthorities.toArray(new GrantedAuthority[userAuthorities.size()]);
    }
}