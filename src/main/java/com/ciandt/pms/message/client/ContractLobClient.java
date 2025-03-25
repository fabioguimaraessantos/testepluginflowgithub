package com.ciandt.pms.message.client;

import com.ciandt.pms.message.dto.ContractLobDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ContractLobClient extends MegaClient{

    private Logger log = LoggerFactory.getLogger(ContractLobClient.class);

    private static final String URI_PROXY_PROJECTS = "cost-center/projects?accountType=SYNTHETIC&padCode=1";

    public List<ContractLobDTO> get() {
        try {
            log.info("getContractLobByProxy");
            JsonArray content = getData(URI_PROXY_PROJECTS).getAsJsonArray("content");

            Gson gson = new Gson();

            List<ContractLobDTO> projects = new ArrayList<>();
            if (!content.isJsonNull()) {
                for(JsonElement element : content)
                    projects.add(gson.fromJson(element, ContractLobDTO.class));
            }

            return projects;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
