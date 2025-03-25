/**
 * @(#) LogAspect.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.util.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Aspecto responsável por efetuar o log da aplicação.
 */
public class LogAspect {

    /** Instância do _logger. */
    private static Logger logger = LogManager.getLogger(LogAspect.class);

    /**
     * Executa o log do método que está sendo executado.
     * 
     * @param pjp
     *            - ponto de juncao
     * @throws Throwable
     *             exception
     * @return Object
     */
    public Object profile(final ProceedingJoinPoint pjp) throws Throwable {

        String sign = null;
        long init = 0;

        if (logger.isDebugEnabled()) {

            init = System.currentTimeMillis();
            StringBuffer buff = new StringBuffer();

            buff.append(pjp.getTarget().getClass().getName()).append(".")
                    .append(pjp.getSignature().getName()).append("()");

            sign = buff.toString();

            logger.debug(sign + " - Inicio");

            buff.setLength(0);
            buff.append("ARGS=(");
            Object[] args = pjp.getArgs();
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    // buff.append((args[i] == null) ? "null" :
                    // args[i].toString()).append(", ");
                    String str = "null";
                    if (args[i] != null) {
                        str = args[i].toString();
                    }
                    buff.append(str).append(", ");
                }
                buff.setLength(buff.length() - 2);
            }
            buff.append(")");
            logger.debug(buff.toString());
        }

        try {

            return pjp.proceed();

        } catch (Exception ex) {

            throw ex;

        } catch (Throwable ex) {

            logger.error("ERROR:" + ex.getMessage(), ex);
            throw ex;

        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug(sign + " - Fim ("
                        + (System.currentTimeMillis() - init) + "ms)");
            }
        }
    }
}
