package com.ciandt.pms.interceptor;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Response {

    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext facesContext = phaseEvent.getFacesContext();
        HttpServletRequest request = ((HttpServletRequest) facesContext.getExternalContext().getRequest());

        if (!request.getRequestURI().contains("protected")) {
            return;
        }

        HttpSession session = request.getSession();
        Enumeration sessionAttributesEnum = session.getAttributeNames();

        Map<String, Object> sessionAttributesMap = new HashMap<>();

        while (sessionAttributesEnum.hasMoreElements()) {
            String attributeName = (String) sessionAttributesEnum.nextElement();
            Object attribute = session.getAttribute(attributeName);

            sessionAttributesMap.put(attributeName, attribute);
        }

        session.invalidate();
        session = request.getSession(true);

        String[] attributeNames = sessionAttributesMap.keySet().toArray(new String[0]);

        for (String attributeName : attributeNames) {
            session.setAttribute(attributeName, sessionAttributesMap.get(attributeName));
        }
    }

    public void beforePhase(PhaseEvent phaseEvent) {
    }


    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
