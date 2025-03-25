package com.ciandt.pms.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * A classe HashGeneratorUtil é uma classe utilitaria
 * que possui os metodos para gerar codigo hash.
 *
 * @since 23/10/2010
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 *
 */
public final class HashGeneratorUtil {

    /** Algoritmo SHA-256 de geração do código hash. */
    public static final String SHA256_ALGORITHM = "SHA-256";
    
    /** Algoritmo SHA-1 de geração do código hash. */
    public static final String SHA1_ALGORITHM = "SHA-1";
    
    /** Algoritmo MD5 de geração do código hash. */
    public static final String MD5_ALGORITHM = "MD5";
    
    /** Algoritmo default de geração do código hash. */
    private static final String DEFAULT_ALGORITHM = SHA256_ALGORITHM;
    
    /**
     * construtor privado.
     */
    private HashGeneratorUtil() { }
    
    /**
     * Gera o código hash somente com letras e números.
     * 
     * @param source - Fonte de dados para gerar o hashcod
     * @param algorithn - algoritimo utilizado para gerar o código hash.
     * 
     * @return retorna o código hash em formato de string
     */
    public static String generateAlphanumericHash(
            final String source,  final String algorithn) {  
          
        BigInteger hash = new BigInteger(
                1, getHashcode(source, algorithn));
        
        return hash.toString(16);
    }  
    
    /**
     * Gera o código hash somente com letras e números.
     * Utilizando como algoritomo SHA-256.
     * 
     * @param source - Fonte de dados para gerar o hashcod
     * 
     * @return retorna o código hash em formato de string
     */
    public static String generateAlphanumericHash(final String source) {
        return generateAlphanumericHash(source, DEFAULT_ALGORITHM);
    }
    
    /**
     * Gera o código hash.
     * 
     * @param source - Fonte de dados para gerar o hashcod
     * 
     * @param algorithn - algoritimo utilizado para gerar o código hash.
     * 
     * @return retorna o código hash em formato de string
     */
    public static String generateHash(
            final String source, final String algorithn) {  
          
        String hash = null;
        try {
            hash = new String(getHashcode(source, algorithn), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return hash;
    }  
    
    /**
     * Gera o código hash. Utilizando como algoritomo SHA-256.
     *  
     * @param source - fonte dos dados
     * 
     * @return retorna o código hash no formato de string.
     */
    public static String generateHash(final String source) {
        return generateHash(source, DEFAULT_ALGORITHM);
    }
    
    /**
     * Gera o hashcode no formtato de bytes.
     * 
     * @param source - fonte de dados
     * @param algorithn - algoritimo de geração de hash
     * 
     * @return retorna o hash no formato de bytes
     */
    private static byte[] getHashcode(
            final String source, final String algorithn) {
        
        try {
            MessageDigest md = MessageDigest.getInstance(algorithn);
            
            return md.digest(source.getBytes());
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        
        return null;
    }
}
