package com.ciandt.pms.message;

import com.ciandt.pms.message.client.ContractLobClient;
import com.ciandt.pms.message.dto.ContractLobDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContractLobMessage {

    @Autowired
    private ContractLobClient client;

    public List<ContractLobDTO> getForSelection() {
        try {
            return client.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
