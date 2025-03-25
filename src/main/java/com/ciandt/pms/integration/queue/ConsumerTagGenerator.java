package com.ciandt.pms.integration.queue;

import com.ciandt.pms.util.PMSUtil;
import org.apache.commons.lang.RandomStringUtils;

import java.net.URI;

public class ConsumerTagGenerator {

    public static final int RANDOM_STRING_COUNT = 16;
    public static final String SEPARATOR = "-";
    public static final String LOCAL_ENV_NAME = "local";
    public static final String PRODUCTION_ENV_NAME = "production";
    public static final boolean RANDOM_STRING_INCLUDE_LETTERS = true;
    public static final boolean RANDOM_STRING_INCLUDE_NUMBERS = true;

    private ConsumerTagGenerator() {}

    public static String generate() {
        return PMSUtil.isProduction() ?
                generateProductionTag(PMSUtil.getURIGatewayAPI()) :
                generateEnvironmentTag(PMSUtil.getEnvironmentName());
    }

    /**
     * Generates TAGs for rabbitMQ queue consumers and production environment
     * @param gatewayUri to extract hostname
     * @return unique generated tag with production hostname
     */
    protected static String generateProductionTag(String gatewayUri) {
        String randomString = generateRandomString();
        String hostname = getProductionHost(gatewayUri);
        return buildTag(hostname, randomString);
    }

    /**
     * Generates TAGs for rabbitMQ queue consumers and NOT production environment
     * @param environmentName e.g. development, qa, uat
     * @return unique generated tag
     */
    protected static String generateEnvironmentTag(String environmentName) {
        String randomString = generateRandomString();
        String environment = environmentName == null ? LOCAL_ENV_NAME : environmentName;
        return buildTag(environment, randomString);
    }

    /**
     * Extract production hostname
     * @param url gateway url
     * @return current instance hostname or 'production' default
     */
    private static String getProductionHost(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost() != null ? uri.getHost() : PRODUCTION_ENV_NAME;
        } catch (Exception e) {
            return PRODUCTION_ENV_NAME;
        }
    }

    /**
     * Generates random ID for tag suffix
     * @return random generated string with current time
     */
    private static String generateRandomString() {
        String randomString = RandomStringUtils
                .random(RANDOM_STRING_COUNT, RANDOM_STRING_INCLUDE_LETTERS, RANDOM_STRING_INCLUDE_NUMBERS)
                .concat(SEPARATOR)
                .concat(String.valueOf(System.currentTimeMillis()));
        return randomString;
    }

    private static String buildTag(String prefix, String suffix) {
        return prefix.concat(SEPARATOR).concat(suffix);
    }
}
