package com.ciandt.pms.csv.util;

import com.ciandt.pms.business.service.IAtividadeService;
import com.ciandt.pms.model.Atividade;
import org.jsefa.common.converter.SimpleTypeConverter;
import org.jsefa.common.converter.SimpleTypeConverterConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.context.FacesContext;


/**
 * 
 * A classe CsvAtividadeConverter converte a sigla para o id da atividade.
 * 
 * @since 14/09/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class CsvAtividadeConverter implements SimpleTypeConverter {

    /** Instancia da atividadeService. */
    private static IAtividadeService atividadeService;

    /**
     * Transforma a sigla no id da atividade.
     * 
     * @param sigla
     *            - sigla da atividade.
     * 
     * @return retorna o id da atividade.
     */
    @Override
    public Long fromString(final String sigla) {

        Atividade atividade = atividadeService
                .findAtividadeBySigla((String) sigla);
        Long idAtividade = Long.valueOf(-1);

        if (atividade != null) {
            idAtividade = atividade.getCodigoAtividade();
        }

        return idAtividade;
    }

    /**
     * Transforma o objeto para string.
     * 
     * @param sigla
     *            - sigla da atividade.
     * 
     * @return retorna o id da atividade.
     */
    @Override
    public String toString(final Object sigla) {
        return null;
    }

    /**
     * retorna uma instancia da classe.
     * 
     * @param configuration
     *            - configuração.
     * 
     * @return retornaum um CsvDateConverter.
     */
    public static CsvAtividadeConverter create(
            final SimpleTypeConverterConfiguration configuration) {
        // pega o contexto da aplicação (Spring)
        ApplicationContext applicationContext = FacesContextUtils
                .getWebApplicationContext(FacesContext.getCurrentInstance());
        // pega o bean do serviço do mapa de alocação.
        atividadeService = (IAtividadeService) 
            applicationContext.getBean("atividadeService");

        return new CsvAtividadeConverter();
    }

}
