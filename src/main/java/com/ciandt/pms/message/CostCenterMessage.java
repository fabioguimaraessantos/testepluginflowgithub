package com.ciandt.pms.message;

import com.ciandt.pms.message.client.CostCenterClient;
import com.ciandt.pms.message.dto.CostCenterDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CostCenterMessage {

    @Autowired
    private CostCenterClient client;

    /**
     * @return GrupoCustoDto
     */
    public List<CostCenterDTO> getForSelection() {
        try {
            return client.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
