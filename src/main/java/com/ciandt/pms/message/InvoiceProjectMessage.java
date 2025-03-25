package com.ciandt.pms.message;

import com.ciandt.pms.message.client.InvoiceProjectClient;
import com.ciandt.pms.message.dto.InvoiceProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class InvoiceProjectMessage {
    @Autowired
    private InvoiceProjectClient client;
    /**
     * @return InvoiceProjectDTO
     */
    public List<InvoiceProjectDTO> getForSelection() {
        try {
            return client.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
