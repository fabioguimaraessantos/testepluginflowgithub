package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMapaAlocacaoPessoaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaAlocacaoPessoa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.util.FacesUtil;


/**
 * Define o BackingBean da entidade - .
 * 
 * @since 06/05/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MapaAlocFollowUnSubscribeBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** Instancia do Serviço de MapaAlocacaoPessoa. */
    @Autowired
    private IMapaAlocacaoPessoaService mapaAlocPessService;

    /** Instancia do Serviço de Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /**
     * Inicializa o bean para fazer o UnSubscribe do Follow.
     * 
     * @return um String com o login da Pessoa que está deixando de ser seguido
     */
    public String getMapaAlocFollowUnSubscribe() {
        // obtem a chave md5 do registro a ser removido (unsubscribe)
        String encriptedKey = FacesUtil.getRequestParameter("encriptedKey");

        // obtem o tipo de Follow - MapaAlocacao ou Pessoa
        String flwType = FacesUtil.getRequestParameter("flwType");

        // busca o MapaAlocacaoPessoa ou PessoaPessoa pela Chave Encriptada e
        // faz o UnFollow dependendo do tipo
        if ((Constants.FOLLOW_TYPE_CLOB).equals(flwType)) {
            MapaAlocacaoPessoa mapaAlocPess = mapaAlocPessService
                    .findMapaAlocPessByCodigoMD5(encriptedKey);

            if (mapaAlocPess != null) {
                MapaAlocacao mapaAlocacao = mapaAlocPess.getMapaAlocacao();
                mapaAlocPessService.unfollowMapaAlocacao(mapaAlocacao,
                        mapaAlocPess.getPessoa());
                return mapaAlocacao.getContratoPratica()
                        .getNomeContratoPratica();
            }
        } else {
            Pessoa pessoaFlwer = pessoaService
                    .findPessByCodigoMD5(encriptedKey);

            if (pessoaFlwer != null) {
                pessoaFlwer.setIndicadorFollowOn(Constants.NO);
                pessoaFlwer.setCodigoMD5(null);

                pessoaService.updatePessoa(pessoaFlwer);

                return BundleUtil.getBundle(Constants.LABEL_ALL_YOU_MANAGE);
            }

            /*
             * PessoaPessoa pessPess = pessPessService
             * .findPessPessByCodigoMD5(encriptedKey);
             * 
             * if (pessPess != null) { Pessoa pessoa = pessPess.getPessoa();
             * pessPessService.unfollowPessoa(pessoa, pessPess
             * .getPessoaFlwer()); return pessoa.getNomePessoa(); }
             */
        }

        return "";
    }

}