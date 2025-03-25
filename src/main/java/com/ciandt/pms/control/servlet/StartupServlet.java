package com.ciandt.pms.control.servlet;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.model.MapaAlocacao;


/**
 * Servlet com a funcionalidade de executar ações no momento em que a aplicação
 * é inicilizada.
 * 
 * @since 19/11/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class StartupServlet extends HttpServlet {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Método executado ao inicializar o servlet.
     * 
     * @param config
     *            - instancia de ServletConfig, que contem as configurações da
     *            aplicação.
     * 
     * @throws ServletException
     *             exception a ser lançada.
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        // Remove todos os locks dos MapaAlocacao ao subir o servidor.
        this.removeAlocationMapLock(config.getServletContext());
    }

    /**
     * Remove todos os locks dos MapaAlocacao.
     * 
     * @param context
     *            - instancia de ServletContext que possui o contexto a
     *            aplicação.
     * 
     * @param context
     */
    private void removeAlocationMapLock(final ServletContext context) {
        // pega o contexto da aplicação (Spring)
        ApplicationContext applicationContext = WebApplicationContextUtils
                .getWebApplicationContext(context);
        // pega o bean do serviço do mapa de alocação.
        IMapaAlocacaoService mapaAlocacaoService = (IMapaAlocacaoService) applicationContext
                .getBean("mapaAlocacaoService");

        // remove o lock de todos os mapas
        List<MapaAlocacao> mapaAlocacaoList = mapaAlocacaoService
                .findMapaAlocacaoAllLock();
        for (MapaAlocacao mapaAlocacao : mapaAlocacaoList) {
            mapaAlocacao.setLoginTrava(null);
            mapaAlocacao.setDataTrava(null);
            mapaAlocacaoService.updateMapaAlocacao(mapaAlocacao);
        }
    }

}
