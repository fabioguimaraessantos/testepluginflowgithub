package com.ciandt.pms.message.client;

import com.ciandt.pms.message.dto.CostCenterDTO;
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
public class CostCenterClient extends MegaClient {

    private Logger log = LoggerFactory.getLogger(CostCenterClient.class);

    private static final String URI_PROXY_COST_CENTERS = "cost-centers?accountType=SYNTHETIC&padCode=1";

    public  List<CostCenterDTO> get() {
        try {
            log.info("getCostCenterByProxy");
            JsonArray content = getData(URI_PROXY_COST_CENTERS).getAsJsonArray("content");

            Gson gson = new Gson();
            List<CostCenterDTO> costCenters = new ArrayList<>();
            if (!content.isJsonNull()) {
                for(JsonElement element : content)
                    costCenters.add(gson.fromJson(element, CostCenterDTO.class));
            }

            return costCenters;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
