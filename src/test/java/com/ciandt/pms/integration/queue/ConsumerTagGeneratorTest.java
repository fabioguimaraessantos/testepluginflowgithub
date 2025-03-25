package com.ciandt.pms.integration.queue;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ConsumerTagGeneratorTest {


    @Test
    public void generateProductionTag_withValidUri_mustCreateHostTag() {
        // given
        String gatewayUri = "http://LNAWBRP004.ciandt.global:9000/";

        // when
        String tag = ConsumerTagGenerator.generateProductionTag(gatewayUri);

        // then
        assertNotNull(tag);
        assertTrue(tag.startsWith("LNAWBRP004.ciandt.global"));
    }

    @Test
    public void generateProductionTag_withInvalidUri_mustCreateProductionTag() {
        // given
        String gatewayUri = "htp:/LNAWBRP004.ciandt.global+++9000/";

        // when
        String tag = ConsumerTagGenerator.generateProductionTag(gatewayUri);

        // then
        assertNotNull(tag);
        assertTrue(tag.startsWith("production"));
    }

    @Test
    public void generateProductionTag_withNullUri_mustCreateProductionTag() {
        // given
        String gatewayUri = null;

        // when
        String tag = ConsumerTagGenerator.generateProductionTag(gatewayUri);

        // then
        assertNotNull(tag);
        assertTrue(tag.startsWith("production"));
    }

    @Test
    public void generateEnvironmentTag_withNullEnvironment_mustCreateLocalTag() {

        // when
        String tag = ConsumerTagGenerator.generateEnvironmentTag(null);

        // then
        assertNotNull(tag);
        assertTrue(tag.startsWith("local"));
    }

    @Test
    public void generateEnvironmentTag_withValidEnvironment_mustCreateEnvironmentTag() {
        // given
        String envName = "development";

        // when
        String tag = ConsumerTagGenerator.generateEnvironmentTag(envName);

        // then
        assertNotNull(tag);
        assertTrue(tag.startsWith(envName));
    }
}