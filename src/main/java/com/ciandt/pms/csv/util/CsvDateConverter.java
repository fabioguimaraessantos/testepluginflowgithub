package com.ciandt.pms.csv.util;

import org.jsefa.common.converter.SimpleTypeConverter;
import org.jsefa.common.converter.SimpleTypeConverterConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * A classe CsvDateConverter é uma classe utilitária referente
 * as funcionalidades de conversão de datas de arquivos CSV.
 *
 * @since 23/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class CsvDateConverter implements SimpleTypeConverter {
  
    /** Padrão de data MM/dd/yy. */
    private String mmddyy = 
        "(0[1-9]|1[012])/(0[1-9]|[12][0-9]|3[01])/([12][0-9]{3}|[0-9]{2})"; 
    
    /** Padrão de data dd/MMM/yy. */
    private String ddMMMyy = 
        "(0[1-9]|[12][0-9]|3[01])-(([A-Z]|[a-z]){3})-([12][0-9]{3}|[0-9]{2})";
    
    /**
     * Objeto para string.
     * 
     * @param arg0 - objeto a ser convertido.
     * 
     * @return retorna o objeto no formato de string.
     */
    @Override
    public String toString(final Object arg0) {
        return null;
    }

    /**
     * Pega uma string e tranforma em data.
     * 
     * @param strDate - string que contem uma data.
     * 
     * @return retorna um Date referente a strDate.
     */
    @Override
    public Date fromString(final String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date retDate = null;
        
        try {
            if (strDate.matches(mmddyy)) {
                sdf = new SimpleDateFormat("MM/dd/yy");
            } else if (strDate.matches(ddMMMyy)) {
                sdf = new SimpleDateFormat("dd-MMM-yy");
            } else {
                return null;    
            }

            retDate = sdf.parse(strDate);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return retDate;
    }

    /**
     * retorna uma instancia da classe.
     * 
     * @param configuration - configuração.
     * 
     * @return retornaum um CsvDateConverter.
     */
    public static CsvDateConverter create(final 
            SimpleTypeConverterConfiguration configuration) {
        return new CsvDateConverter();
    }
}
