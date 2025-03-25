package com.ciandt.pms.control.jsf.bean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class Health implements Serializable {

	private static final String JNDI = "jdbc/pmsDS";

	public String check;

	public String getTimeDate() {
		Instant instant = Instant.now();
		ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
		Date date = Date.from(instant.atZone(zoneId).toInstant());
		return "Current time: " + new Date() + " - " + date;
	}


	public String getCheck() throws Exception {

		if (verificarConexao()) {
			return "OK";
		}
		redirecionarError();
		return null;
	}

	public void redirecionarError() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect("error-page.jsf");
		System.err.println("Problemas na conexão com o banco de dados. O Kubernetes irá reiniciar a aplicação.");
	}

	public boolean verificarConexao() {

		try {
			Context initialContext = new InitialContext();
			DataSource dataSource = (DataSource) initialContext.lookup(JNDI);
			try (Connection connection = dataSource.getConnection()) {
				return true;
			} catch (Exception e) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}