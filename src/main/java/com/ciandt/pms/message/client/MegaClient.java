package com.ciandt.pms.message.client;

import com.ciandt.pms.Constants;
import com.ciandt.pms.util.PMSUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MegaClient {

    private Logger log = LoggerFactory.getLogger(MegaClient.class);

    @Autowired
    protected Properties systemProperties;

    protected String getBaseUrl(){
        return PMSUtil.getURIGatewayAPI() + systemProperties.getProperty(Constants.URI_GATEWAY_API_VERSION);
    }

    protected JsonObject getData(String endpoint) throws IOException {

        HttpGet request = new HttpGet(getBaseUrl() + "/" + endpoint);

        request.setHeader("Authorization", systemProperties.getProperty(Constants.URI_AUTHORIZATION_TOKEN));

        HttpClient client = HttpClientBuilder.create().build();
        log.info("MegaClient-request: " + request);
        HttpResponse resp = client.execute(request);
        log.info("MegaClient-response Status: " + resp.getStatusLine());

        return new Gson().fromJson(EntityUtils.toString(resp.getEntity()), JsonObject.class).getAsJsonObject();
    }
}
