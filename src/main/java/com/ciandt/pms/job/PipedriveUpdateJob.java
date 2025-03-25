package com.ciandt.pms.job;

import com.ciandt.pms.business.service.impl.ClientePipedriveService;
import com.ciandt.pms.integration.vo.OrganizacaoPipedrive;
import com.ciandt.pms.model.ClientePipedrive;
import com.ciandt.pms.util.JobUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 
 * @author peter
 * 
 */
public class PipedriveUpdateJob {

    private Logger logger = LoggerFactory.getLogger(PipedriveUpdateJob.class);

	@Autowired
	private JobUtil jobUtil;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	@Autowired
	private ClientePipedriveService clientePipedriveService;

	public void sendMail() throws InterruptedException {

		logger.info("JOB", "Executando Job DocumentoLegalMailJob.sendMail - " + new Date());



		logger.info("JOB", "Fim Job DocumentoLegalMailJob.sendMail - " + new Date());
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet("https://bcgj7lp5a0.execute-api.sa-east-1.amazonaws.com/dev/pipedrive/organizations");
		HttpResponse resp = null;
		String responseAsString = null;

		try {
			resp = client.execute(request);
			responseAsString = EntityUtils.toString(resp.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gson gson = new Gson();
		JsonArray data = gson.fromJson(responseAsString, JsonArray.class).getAsJsonArray();
		List<ClientePipedrive> clientePipedrives = new ArrayList<ClientePipedrive>();
		if (!data.isJsonNull()) {
			for (JsonElement element : data) {
				JsonArray organizations = (JsonArray) element;
				for (JsonElement organization : organizations) {
					OrganizacaoPipedrive organizacaoPipedrive = gson.fromJson(organization, OrganizacaoPipedrive.class);
					clientePipedrives.add(new ClientePipedrive(organizacaoPipedrive.getId(), Normalizer.normalize(organizacaoPipedrive.getName(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")));
				}
			}
		}

		clientePipedriveService.create(clientePipedrives);
	}

}
