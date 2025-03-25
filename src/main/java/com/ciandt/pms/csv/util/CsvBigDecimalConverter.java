package com.ciandt.pms.csv.util;

import org.jsefa.common.converter.SimpleTypeConverter;
import org.jsefa.common.converter.SimpleTypeConverterConfiguration;

import java.math.BigDecimal;

/**
 * 
 * A classe CsvBigDecimalConverter proporciona a 
 * funcionalidade de conversão de string para BigDecimal.
 *
 * @since 23/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class CsvBigDecimalConverter implements SimpleTypeConverter {


    /**
     * Converte BigDecimal para string.
     * 
     * @param arg0 - objeto a ser convertido para string.
     * 
     * @return retorna a string gerada.
     * 
     */
    @Override
    public String toString(final Object arg0) {
        return null;
    }

    /**
     * Converte uma string em um BigDecimal.
     * 
     * @param value - valor a ser convertido.
     * 
     * @return retorna o BigDecimal gerado.
     */
    @Override
    public BigDecimal fromString(final String value) {
       try {
       return new BigDecimal(value.replaceAll(",", "."));
       } catch (Exception e) {
    	   return null;
       }
    }

    /**
     * Retorna uma instancia da classe.
     * 
     * @param configuration - configuração.
     * 
     * @return retorna uma instancia da classe.
     */
    public static CsvBigDecimalConverter create(final
            SimpleTypeConverterConfiguration configuration) {
        return new CsvBigDecimalConverter();
    }
}
