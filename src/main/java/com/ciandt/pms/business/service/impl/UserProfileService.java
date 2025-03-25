package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IUserProfileService;
import com.ciandt.pms.model.vo.*;
import com.ciandt.pms.util.PMSUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Service
public class UserProfileService implements IUserProfileService {

    @Autowired
    private Properties systemProperties;

    private Logger logger = LoggerFactory.getLogger(UserProfileService.class);
    HttpClient client = HttpClientBuilder.create().build();

    @Override
    public UserProfile findByUserAndProfile(String login, String applicationAcronym) {
        if (login != null && !login.isEmpty()) {

            System.out.println("LOGIN: " + login);

            System.out.println("Estou fazendo o login para: " + login);

            String gatewayUrl = PMSUtil.getURIGatewayAPI();
            String apiVersion = systemProperties.getProperty(Constants.URI_GATEWAY_API_VERSION);
            String authorizationToken = systemProperties.getProperty(Constants.URI_AUTHORIZATION_TOKEN);
            String parameters = "?search=application.acronym:" + applicationAcronym;

            String uri = gatewayUrl + apiVersion + "/users/" + login + "/profiles" + parameters;

            System.out.println("Vou chamar a URL : " + uri + " para o usu�rio: " + login);

            HttpGet request = new HttpGet(uri);
            request.setHeader("Authorization", authorizationToken);
            request.setHeader("Content-Type", "application/json");

            try {
                System.out.println("Indo chamar agora o proxy para: " + login);
                // logCurlCommand(uri, authorizationToken);
                HttpResponse resp = client.execute(request);
                String result = EntityUtils.toString(resp.getEntity());
                System.out.println("Chamei o proxy e deu resposta: " + login);
                return this.jsonToEntity(result);
            } catch (Exception e) {
                System.out.println("ERRO! ERRO! ERRO! ERRO! ERRO! ERRO! ERRO!");
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                e.printStackTrace();
                System.out.println("ERRO! ERRO! ERRO! ERRO! ERRO! ERRO! ERRO!");
            }
        }
        return null;
    }

    private void logCurlCommand(String uri, String authorizationToken) {
        String curlCommand = String.format("curl -X GET \"%s\" -H \"Authorization: %s\" -H \"Content-Type: application/json\"", uri, authorizationToken);
        System.out.println("Comando curl gerado: " + curlCommand);
    }

    private UserProfile jsonToEntity(String string) {
        Gson gson = new Gson();
        UserProfile userProfile = new UserProfile();
        JsonObject data = gson.fromJson(string, JsonObject.class).getAsJsonObject();
        if (!data.isJsonNull()) {
            userProfile = gson.fromJson(data, UserProfile.class);
        }
        return userProfile;
    }

    @Override
    public Set<String> convertRolesToPMS(UserProfile userProfile) {

        Set<String> pmsRoles = new HashSet<String>();

        try {
            for (Profile profile : userProfile.getProfiles()) {
                for (Application application : profile.getApplications()) {
                    for (Feature feature : application.getFeatures()) {
                        for (Operation operation : feature.getOperations()) {

                            if (!profile.getAcronym().isEmpty()
                                    && !feature.getAcronym().isEmpty()
                                    && !operation.getAcronym().isEmpty()) {

                                String role = feature.getAcronym() +
                                        ":" +
                                        operation.getAcronym();
                                pmsRoles.add(role);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            String errorMessage = "Error while converting roles to PMS: " + e.getMessage();
            logger.error(errorMessage);
            logger.error("Error details: ", e);
        }

        return pmsRoles;
    }
}
