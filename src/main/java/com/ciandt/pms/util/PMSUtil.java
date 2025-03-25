package com.ciandt.pms.util;

import com.ciandt.pms.Constants;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Classe utilitária do sistema.
 *
 * @since 18/11/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public final class PMSUtil {

    /**
     * Construtor privado.
     */
    private PMSUtil() { }
    
    /**
     * Retorna o ambiente. Se produção retornará 'production'
     * 
     * @return retorna uma string referente ao nome do ambiente.
     */
    public static String getEnvironmentName() {
        return System.getProperty(Constants.SERVER_ENVIRONMENT_KEY);
    }
    
    /**
     * Retorna true se o ambiente for de produção.
     * 
     * @return retorna true se produção, caso contrario false.
     */
    public static Boolean isProduction() {
        return Constants.SERVER_ENVIRONMENT_PROD.equals(getEnvironmentName());
    }

    /**
     * Retorna a URI do API Gateway
     *
     * @return retorna uma string referente ao uri do API Gateway.
     */
    public static String getURIGatewayAPI(){
        return System.getenv(Constants.MGMT_URI_GATEWAY_API);
    }

    /**
     * Recupera o pool de conexão com a base de Dados.
     * 
     * @return uma Connection
     * @throws NamingException
     *             - caso o nome jndi não esteja corretamente configurado
     * @throws SQLException
     *             - caso não consiga fazer conexão com a base de dados
     */
    public static Connection getDSPoolConnection() throws NamingException,
            SQLException {
        // Buscar contexto JNDI
        Context ic = new InitialContext();
        // Solicitar o objeto Data Source
        DataSource ds = (DataSource) ic.lookup(Constants.APPLICATION_JNDI_NAME);
        // Obter uma conexao
        Connection conn = (Connection) ds.getConnection();

        return conn;
    }

    public static String getSubstring(String string, int startPosition, int endPosition) {
    	return string.substring(Math.max(0, startPosition), Math.min(string.length(), endPosition));
    }
}