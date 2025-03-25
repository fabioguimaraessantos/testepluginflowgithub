package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IPriceTableEditorService;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PriceTableEditor;
import com.ciandt.pms.model.vo.UserProfile;
import com.ciandt.pms.persistence.dao.IPriceTableEditorDao;
import com.ciandt.pms.util.PMSUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
public class PriceTableEditorService implements IPriceTableEditorService {

    private Logger logger = LoggerFactory.getLogger(PriceTableEditorService.class);

    @Autowired
    private Properties systemProperties;
    @Autowired
    private IPriceTableEditorDao dao;

    @Autowired
    private IPessoaService pessoaService;

    @Override
    public List<PriceTableEditor> findByMsaCode(Long msaCode) {
        return dao.findByMsaCode(msaCode);
    }

    @Override
    public PriceTableEditor findByLogin(String login) {
        return dao.findByLogin(login);
    }
    @Override
    public PriceTableEditor findByLoginAndMsaCode(String login, Long msaCode) {
        return dao.findByLoginAndMsaCode(login, msaCode);
    }

    @Override
    @Transactional
    public void removePriceTableEditor(PriceTableEditor entity) {
        dao.remove(dao.merge(entity));
    }

    @Override
    @Transactional
    public List<PriceTableEditor> update(Msa msa, List<String> logins) {
        List<PriceTableEditor> entities = this.findByMsaCode(msa.getCodigoMsa());
        List<String> editorsLoginsExisted = new ArrayList<String>();
        List<PriceTableEditor> priceTableEditors = new ArrayList<PriceTableEditor>();


        for (PriceTableEditor priceTableEditor : entities) {
            editorsLoginsExisted.add(priceTableEditor.getLogin());
        }

        if (editorsLoginsExisted.isEmpty()) {
            priceTableEditors.addAll(createPriceTableEditor(msa, logins));
        } else {
            priceTableEditors.addAll(updatePriceTableEditor(msa, editorsLoginsExisted, logins));
        }

        return priceTableEditors;
    }

    private List<PriceTableEditor> updatePriceTableEditor(Msa msa, List<String> editorLoginsExisted, List<String> logins) {
        List<PriceTableEditor> priceTableEditorUpdated = new ArrayList<PriceTableEditor>();
        for (String login : logins) {
            if (!editorLoginsExisted.contains(login)) {
                List<String> loginListAux = new ArrayList<String>();
                loginListAux.add(login);
                priceTableEditorUpdated.addAll(createPriceTableEditor(msa, loginListAux));
            }
        }
        return priceTableEditorUpdated;
    }


    private List<PriceTableEditor> createPriceTableEditor(Msa msa, List<String> priceTableEditorlogins) {
        List<PriceTableEditor> entitiesUpdated = new ArrayList<PriceTableEditor>();
        for (String login : priceTableEditorlogins) {
            PriceTableEditor editorLogin = new PriceTableEditor();
            editorLogin.setLogin(login);
            editorLogin.setMsa(msa);
            dao.create(editorLogin);
            entitiesUpdated.add(editorLogin);
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

        for(int i = 0; i < data.size(); i++) {
            userProfiles.add(gson.fromJson(data.get(i), UserProfile.class));
        }

        return userProfiles;
    }

    @Override
    public List<String> getEditorsEmailByMsaCode(Long msaCode) {
        List<String> editorsEmail = new ArrayList<String>();

        List<PriceTableEditor> editors = findByMsaCode(msaCode);

        for (PriceTableEditor editor: editors) {
            Pessoa pessoa = pessoaService.findPessoaByLogin(editor.getLogin());
            if (pessoa != null) editorsEmail.add(pessoa.getTextoEmail());
        }

        return editorsEmail;
    }
}
