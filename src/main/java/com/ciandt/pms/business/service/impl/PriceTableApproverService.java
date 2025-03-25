package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IPriceTableApproverService;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PriceTableApprover;
import com.ciandt.pms.model.vo.UserProfile;
import com.ciandt.pms.persistence.dao.IPriceTableApproverDao;
import com.ciandt.pms.util.PMSUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class PriceTableApproverService implements IPriceTableApproverService {

    private final Logger logger = LoggerFactory.getLogger(PriceTableApproverService.class);

    @Autowired
    private Properties systemProperties;
    @Autowired
    private IPriceTableApproverDao dao;

    @Autowired
    private IPessoaService pessoaService;

    @Override
    public List<PriceTableApprover> findByMsaCode(Long msaCode) {
        return dao.findByMsaCode(msaCode);
    }

    @Override
    public PriceTableApprover findByLogin(String login) {
        return dao.findByLogin(login);
    }

    @Override
    public PriceTableApprover findByLoginAndMsaCode(String login, Long msaCode) {
        return dao.findByLoginAndMsaCode(login, msaCode);
    }

    @Override
    @Transactional
    public void removePriceTableApprover(PriceTableApprover entity) {
        dao.remove(dao.merge(entity));
    }

    @Override
    @Transactional
    public List<PriceTableApprover> update(Msa msa, List<String> logins) {
        List<PriceTableApprover> entities = this.findByMsaCode(msa.getCodigoMsa());
        List<String> approversLoginsExisted = new ArrayList<String>();
        List<PriceTableApprover> priceTableApprovers = new ArrayList<PriceTableApprover>();


        for (PriceTableApprover priceTableApprover : entities) {
            approversLoginsExisted.add(priceTableApprover.getLogin());
        }

        if (approversLoginsExisted.isEmpty()) {
            priceTableApprovers.addAll(createPriceTableApprover(msa, logins));
        } else {
            priceTableApprovers.addAll(updatePriceTableApprover(msa, approversLoginsExisted, logins));
        }

        return priceTableApprovers;
    }

    private List<PriceTableApprover> updatePriceTableApprover(Msa msa, List<String> approverLoginsExisted, List<String> logins) {
        List<PriceTableApprover> priceTableApproverUpdated = new ArrayList<PriceTableApprover>();
        for (String login : logins) {
            if (!approverLoginsExisted.contains(login)) {
                List<String> loginListAux = new ArrayList<String>();
                loginListAux.add(login);
                priceTableApproverUpdated.addAll(createPriceTableApprover(msa, loginListAux));
            }
        }
        return priceTableApproverUpdated;
    }


    private List<PriceTableApprover> createPriceTableApprover(Msa msa, List<String> priceTableApproverlogins) {
        List<PriceTableApprover> entitiesUpdated = new ArrayList<PriceTableApprover>();
        for (String login : priceTableApproverlogins) {
            PriceTableApprover approverLogin = new PriceTableApprover();
            approverLogin.setLogin(login);
            approverLogin.setMsa(msa);
            dao.create(approverLogin);
            entitiesUpdated.add(approverLogin);
        }
        return entitiesUpdated;
    }

    @Override
    public List<UserProfile> autoCompletePriceTable(String login) {
        return buildAndDoRequestToGetLoginsWithPriceTableProfiles(login);
    }

    private List<UserProfile> buildAndDoRequestToGetLoginsWithPriceTableProfiles(String login) {
        String gatewayUrl = PMSUtil.getURIGatewayAPI();
        String apiVersion = systemProperties.getProperty(Constants.URI_GATEWAY_API_VERSION);
        String authorizationToken = systemProperties.getProperty(Constants.URI_AUTHORIZATION_TOKEN);
        String uri = gatewayUrl + apiVersion + "/users/price-table/" + login;

        HttpGet request = new HttpGet(uri);
        request.setHeader("Authorization", authorizationToken);
        request.setHeader("Content-Type", "application/json");

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse resp = client.execute(request);
            return jsonToEntity(EntityUtils.toString(resp.getEntity()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private List<UserProfile> jsonToEntity(String string) {
        List<UserProfile> userProfiles = new ArrayList<UserProfile>();
        Gson gson = new Gson();
        JsonArray data = gson.fromJson(string, JsonArray.class).getAsJsonArray();

        for (int i = 0; i < data.size(); i++) {
            userProfiles.add(gson.fromJson(data.get(i), UserProfile.class));
        }

        return userProfiles;
    }

    @Override
    public List<String> getApproversEmailByMsaCode(Long msaCode) {
        List<String> approversEmail = new ArrayList<String>();

        List<PriceTableApprover> approvers = findByMsaCode(msaCode);

        for (PriceTableApprover approver: approvers) {
            Pessoa pessoa = pessoaService.findPessoaByLogin(approver.getLogin());
            approversEmail.add(pessoa.getTextoEmail());
        }

        return approversEmail;
    }
}
