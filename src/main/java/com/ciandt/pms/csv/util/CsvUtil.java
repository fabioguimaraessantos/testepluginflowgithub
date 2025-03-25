package com.ciandt.pms.csv.util;

import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.PessoaBancoHoras;
import org.jsefa.DeserializationException;
import org.jsefa.Deserializer;
import org.jsefa.common.lowlevel.filter.HeaderAndFooterFilter;
import org.jsefa.csv.CsvDeserializer;
import org.jsefa.csv.CsvIOFactory;
import org.jsefa.csv.config.CsvConfiguration;
import org.jsefa.csv.lowlevel.config.QuoteMode;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A classe CSVUtil é uma classe utilitária que possui metodos para manapulação
 * de arquivos CSV.
 * 
 * @since 17/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public final class CsvUtil {

    /** Configuração default. */
    public static final CsvConfiguration DEFAULT_CONFIG;

    static {
        DEFAULT_CONFIG = new CsvConfiguration();
        // header of size 1, no footer, store the filtered lines
        DEFAULT_CONFIG.setLineFilter(new HeaderAndFooterFilter(1, false, true));
        DEFAULT_CONFIG.setDefaultQuoteMode(QuoteMode.ON_DEMAND);
        DEFAULT_CONFIG.setQuoteCharacter('"');
        DEFAULT_CONFIG.setFieldDelimiter(';');
    }

    /**
     * Construtor privado.
     */
    private CsvUtil() {
    }

    /**
     * Retorna o Deserializer.
     * 
     * @param <T>
     *            tipo generico
     * @param classe
     *            - Classe que se deseja popular
     * 
     * @return retorna o Deserializer
     */
    public static <T> CsvDeserializer getDeserializer(final Class<T> classe) {

        CsvDeserializer deserializer = getDeserializer(classe, DEFAULT_CONFIG);

        return deserializer;
    }

    /**
     * Retorna o Deserializer.
     * 
     * @param <T>
     *            tipo generico
     * @param classe
     *            - Classe que se deseja popular
     * @param config
     *            - configuração do arquivo CSV
     * 
     * @return retorna o Deserializer
     */
    public static <T> CsvDeserializer getDeserializer(final Class<T> classe,
            final CsvConfiguration config) {
        CsvDeserializer deserializer =
                CsvIOFactory.createFactory(config, classe).createDeserializer();

        return deserializer;
    }

    /**
     * Retorna os elementos passados pelo parametro csvSource.
     * 
     * @param <T>
     *            tipo generico
     * @param csvSource
     *            fonte de dados lido do CSV
     * @param classe
     *            - classe que se deseja popular.
     * @param config
     *            - configuração do arquivo CSV
     * 
     * @return retorna uma lista do tipo T generico.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getElementList(final String csvSource,
            final Class<T> classe, final CsvConfiguration config) {
        List<T> retList = new ArrayList<T>();

        StringReader sr = new StringReader(csvSource);

        CsvDeserializer deserializer = CsvUtil.getDeserializer(classe, config);

        deserializer.open(sr);

        while (deserializer.hasNext()) {
            Object next = null;
            try {
                next = deserializer.next();
                if (next == null) {
                    next = classe.newInstance();
                }

            } catch (DeserializationException e) {

                try {
                    next = classe.newInstance();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }

            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } finally {
                retList.add((T) next);
            }
        }

        return retList;
    }

    /**
     * Retorna os elementos passados pelo parametro csvSource.
     * 
     * @param <T>
     *            tipo generico
     * @param csvSource
     *            fonte de dados lido do CSV
     * @param classe
     *            - classe que se deseja popular.
     * 
     * @return retorna uma lista do tipo T generico.
     */
    public static <T> List<T> getElementList(final String csvSource,
            final Class<T> classe) {
        return getElementList(csvSource, classe, DEFAULT_CONFIG);
    }

    /**
     * Cria a configuração do arquivo CSV.
     * 
     * @param padraoArq
     *            padrão do arquivo.
     * 
     * @return retorna a configuração do CSV do tipo CsvConfiguration.
     */
    public static CsvConfiguration getCsvConfig(final PadraoArquivo padraoArq) {
        CsvConfiguration conf = new CsvConfiguration();
        conf.setLineFilter(new HeaderAndFooterFilter(1, false, true));
        conf.setDefaultQuoteMode(QuoteMode.ON_DEMAND);
        conf.setFieldDelimiter(padraoArq.getTextoDelimitadorColuna());
        conf.setQuoteCharacter(padraoArq.getTextoDelimitadorString());

        return conf;
    }

    /**
     * dfsdfsdfç.
     * 
     * @param args
     *            sdfsdf
     * @throws IOException
     *             ssdfsdf
     */
    public static void main(final String[] args) throws IOException {

        CsvConfiguration config = new CsvConfiguration();
        // header of size 1, no footer, store the filtered lines
        config.setLineFilter(new HeaderAndFooterFilter(1, false, true));
        config.setDefaultQuoteMode(QuoteMode.ON_DEMAND);
        config.setQuoteCharacter('"');
        config.setFieldDelimiter(';');

        System.out.println("Fim de linha: " + config.getLineBreak());

        Deserializer deserializer =
                CsvIOFactory.createFactory(config, PessoaBancoHoras.class)
                        .createDeserializer();

        deserializer.open(new FileReader(
                "C:\\Users\\ronaldon\\Desktop\\teste3.csv"));

        while (deserializer.hasNext()) {
            PessoaBancoHoras p = (PessoaBancoHoras) deserializer.next();
            // do something useful with it
            System.out.println(p.getLogin() + "|" + p.getDataMes() + "|"
                    + p.getNumeroHoras() + "|" + p.getValorFator());
        }
        deserializer.close(true);
    }

}
