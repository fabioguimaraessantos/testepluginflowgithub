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
 * Servlet com a funcionalidade de executar a��es no momento em que a aplica��o
 * � inicilizada.
 * 
 * @since 19/11/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class StartupServlet extends HttpServlet {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * M�todo executado ao inicializar o servlet.
     * 
     * @param config
     *            - instancia de ServletConfig, que contem as configura��es da
     *            aplica��o.
     * 
     * @throws ServletException
     *             exception a ser lan�ada.
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
     *            aplica��o.
     * 
     * @param context
     */
    private void removeAlocationMapLock(final ServletContext context) {
        // pega o contexto da aplica��o (Spring)
        ApplicationContext applicationContext = WebApplicationContextUtils
                .getWebApplicationContext(context);
        // pega o bean do servi�o do mapa de aloca��o.
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
