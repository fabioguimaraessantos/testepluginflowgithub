package com.ciandt.pms.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 
 * A classe NumberUtil proporciona as funcionalidades de utilizades para
 * Numeros.
 * 
 * @since 17/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public final class NumberUtil {
    
    /** Expressão Reg CNPJ. */
    private static final String REG_EX_CNPJ = "\\d\\d\\.\\d\\d\\d\\.\\d\\d\\d\\/\\d\\d\\d\\d\\-\\d\\d";

    /**
     * Construtor privado.
     */
    private NumberUtil() {

    }

    /**
     * Valida CNPJ.
     * 
     * @param pCnpj
     *            - cnpj a ser validado
     * @return true se CNPJ for válido e false caso contrário
     */
    public static boolean isValidCnpj(final String pCnpj) {
        String cnpj = pCnpj.trim();
        if (!cnpj.substring(0, 1).equals("") && cnpj.length() == 18
                && cnpj.matches(REG_EX_CNPJ)) {
            try {
                cnpj = cnpj.replace('.', ' ');
                cnpj = cnpj.replace('/', ' ');
                cnpj = cnpj.replace('-', ' ');
                cnpj = cnpj.replaceAll(" ", "");
                int soma = 0, dig;
                String cnpjCalc = cnpj.substring(0, 12);

                if (cnpj.length() != 14) {
                    return false;
                }
                char[] chrCnpj = cnpj.toCharArray();
                /* Primeira parte */
                for (int i = 0; i < 4; i++) {
                    if (chrCnpj[i] - 48 >= 0 && chrCnpj[i] - 48 <= 9) {
                        soma += (chrCnpj[i] - 48) * (6 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chrCnpj[i + 4] - 48 >= 0 && chrCnpj[i + 4] - 48 <= 9) {
                        soma += (chrCnpj[i + 4] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpjCalc += (dig == 10 || dig == 11) ? "0" : Integer
                        .toString(dig);
                /* Segunda parte */
                soma = 0;
                for (int i = 0; i < 5; i++) {
                    if (chrCnpj[i] - 48 >= 0 && chrCnpj[i] - 48 <= 9) {
                        soma += (chrCnpj[i] - 48) * (7 - (i + 1));
                    }
                }
                for (int i = 0; i < 8; i++) {
                    if (chrCnpj[i + 5] - 48 >= 0 && chrCnpj[i + 5] - 48 <= 9) {
                        soma += (chrCnpj[i + 5] - 48) * (10 - (i + 1));
                    }
                }
                dig = 11 - (soma % 11);
                cnpjCalc += (dig == 10 || dig == 11) ? "0" : Integer
                        .toString(dig);
                return cnpj.equals(cnpjCalc);
            } catch (Exception e) {
                System.err.println("Erro !" + e);
                return false;
            }
        } else {
            return false;
        }
    }
    
    /**
     * Realiza o arredondamento default. 
     * Arredonda para 2 casas de precisão
     * e modo de arredondamento é ROUND_HALF_UP. 
     * 
     * @param value - valor a ser arredondado
     * 
     * @return retorna o valor arredondado.
     */
    public static double round(final double value) {
        return new BigDecimal(value).setScale(
                2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
    
    /**
     * Realiza o arredondamento default. 
     * Arredonda para 2 casas de precisão
     * e modo de arredondamento é ROUND_HALF_UP. 
     * 
     * @param value - valor a ser arredondado
     * 
     * @return retorna o valor arredondado.
     */
    public static BigDecimal round(final BigDecimal value) {
        return new BigDecimal(NumberUtil.round(value.doubleValue()));
    }
    
    /**
     * Faz o casting de long para int de forma segura.
     *
     * @param l
     * @return int.
     */
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    
    /**
     * Verifica se uma {@link String} é composta apenas de numeros inteiros.
     *
     * @param string
     * @return {@link Boolean}
     */
    public static Boolean isInteger(String string) {
        try { 
            Integer.parseInt(string); 
        } catch(NumberFormatException e) { 
            return false; 
        }

        return true;
    }

    public static boolean isValidPercentage(String percent) {
        try {
            double value = Double.parseDouble(percent);
            return value >= 0 && value <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}