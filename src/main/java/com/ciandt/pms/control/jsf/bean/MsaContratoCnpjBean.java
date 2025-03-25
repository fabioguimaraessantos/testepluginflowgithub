package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.model.MsaContratoCnpj;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MsaContratoCnpjBean implements Serializable {

    private MsaContratoCnpj to = new MsaContratoCnpj();

    public MsaContratoCnpj getTo() {
        return to;
    }

    public void setTo(MsaContratoCnpj to) {
        this.to = to;
    }

    public void reset () {
        this.resetTo();
    }

    public void resetTo () {
        this.to = new MsaContratoCnpj();
    }
}
