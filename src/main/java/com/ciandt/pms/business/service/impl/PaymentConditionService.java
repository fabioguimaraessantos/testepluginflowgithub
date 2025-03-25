package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPaymentConditionService;
import com.ciandt.pms.model.vo.PaymentCondition;
import com.ciandt.pms.util.PMSUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class PaymentConditionService implements IPaymentConditionService {

    @Autowired
    private Properties systemProperties;

    private Logger logger = LoggerFactory.getLogger(PaymentCondition.class);
    HttpClient client = HttpClientBuilder.create().build();
    Gson gson = new Gson();

    @Override
    public List<PaymentCondition> findByClientAndCompany(final Long agentCode, final Long companyCode) {
        if (agentCode == null){
            return null;
        }


        String revenueMSApi = PMSUtil.getURIGatewayAPI();
        System.out.println(revenueMSApi);
        String apiVersion = systemProperties.getProperty(Constants.URI_GATEWAY_API_VERSION);
        System.out.println(apiVersion);
        String authorizationToken = systemProperties.getProperty(Constants.URI_AUTHORIZATION_TOKEN);
        System.out.println(authorizationToken);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("agentCode", agentCode.toString());
        parameters.put("companyCode", companyCode.toString());

        String uri = revenueMSApi + apiVersion + "/payment-conditions?companyCode=" + companyCode.toString() + "&agentCode=" + agentCode.toString();
        logger.info("Vai chamar o mega-api");

        HttpGet request = new HttpGet(uri);
        request.setHeader("Authorization", authorizationToken);
        HttpResponse resp = null;
        List<PaymentCondition> paymentConditions = null;

        try {
            resp = client.execute(request);
            String result = EntityUtils.toString(resp.getEntity());
            logger.info("paymentCondition findByClientAndCompany" + result);
            paymentConditions = this.jsonArrayToEntityList(result);
        } catch (Exception e) {
            System.out.println("ERRO! ERRO! ERRO! ERRO! ERRO! ERRO! ERRO! ");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            e.printStackTrace();
            System.out.println("ERRO! ERRO! ERRO! ERRO! ERRO! ERRO! ERRO! ");
        }

        return paymentConditions;
    }

    private URI getURIForResource(String resource, Map<String, String> queryParameters) {
        String revenueMSApi = PMSUtil.getURIGatewayAPI();
        String apiVersion = systemProperties.getProperty(Constants.URI_GATEWAY_API_VERSION);

        URIBuilder builder = new URIBuilder().setScheme("https")

                .setHost(revenueMSApi + apiVersion)
                .setPath(resource);

        if (queryParameters != null) {
            for (Map.Entry<String, String> parameter : queryParameters.entrySet()) {
                builder.setParameter(parameter.getKey(), parameter.getValue());
            }
        }

        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return uri;
    }

    private List<PaymentCondition> jsonArrayToEntityList(String string) {
        System.out.println(string);
        Gson gson = new Gson();
        List<PaymentCondition> paymentConditions = new ArrayList<PaymentCondition>();
        JsonArray data = gson.fromJson(string, JsonArray.class).getAsJsonArray();
        if (!data.isJsonNull()) {
            for(JsonElement element : data){
                paymentConditions.add(gson.fromJson(element, PaymentCondition.class));
            }
        }

        return paymentConditions;
    }

}
