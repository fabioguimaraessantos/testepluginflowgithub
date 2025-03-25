package com.ciandt.pms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Properties;

/**
 * Created by mvidolin on 03/05/17.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class JobUtil {

    private Logger logger = LoggerFactory.getLogger(JobUtil.class);

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Returns a boolean from the give String propertie ("true"/"false").
     *
     * @param jobActivePropertie {@link String}
     * @return boolean
     */
    public boolean isJobActive(final String jobActivePropertie) {
        return Boolean.valueOf(systemProperties.getProperty(jobActivePropertie));
    }

}
