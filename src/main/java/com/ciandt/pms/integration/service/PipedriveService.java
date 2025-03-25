package com.ciandt.pms.integration.service;

import com.ciandt.pms.integration.vo.OrganizacaoPipedrive;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;


/**
 * 
 * A classe PipedriveService proporciona acesso a API do Pipedrive.
 *
 * @since 15/01/2018
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 *
 */
@Service
public class PipedriveService {

    private Logger logger = LoggerFactory.getLogger(PipedriveService.class);

    HttpClient client = HttpClients.custom()
            .setDefaultRequestConfig(
                    RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                            .build()
            )
            .build();
    Gson gson = new Gson();

    private List<OrganizacaoPipedrive> allOrganizations = null; //this.findAllOrganizations();

    /**
     * Prametros:
     *
     * host
     * token
     * NumPagesInTotal
     * ItemsPerPage (LImit)
     * OrganizationUri
     *
     * @return
     */

    public List<OrganizacaoPipedrive> findAllOrganizations() {

        if (allOrganizations != null) {
            return allOrganizations;
        }

        ExecutorService service = Executors.newCachedThreadPool();
        Set<Callable<List<OrganizacaoPipedrive>>> callables = new HashSet<Callable<List<OrganizacaoPipedrive>>>();
        Integer pipedriveApiNumberOrganizationPages = 10;

        for (int page = 0; page < pipedriveApiNumberOrganizationPages; page++) {
            callables.add(this.getOrganizationCallableByPage(page));
        }

        try {
            List<Future<List<OrganizacaoPipedrive>>> futures = service.invokeAll(callables);
            List<OrganizacaoPipedrive> organizacaoPipedrives = new ArrayList<OrganizacaoPipedrive>();
            for (Future<List<OrganizacaoPipedrive>> future : futures) {
                organizacaoPipedrives.addAll(future.get());
            }

            Collections.sort(organizacaoPipedrives, new Comparator<OrganizacaoPipedrive>() {
                public int compare(OrganizacaoPipedrive s1, OrganizacaoPipedrive s2) {
                    int idComparison = s1.getId().compareTo(s2.getId());
                    int nameComparison = s1.getName().compareTo(s2.getName());
                    return nameComparison == 0 ? idComparison : nameComparison;
                }
            });
            this.allOrganizations = organizacaoPipedrives;

            return organizacaoPipedrives;

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }

    public List<OrganizacaoPipedrive> findByName(String name) {
        List<OrganizacaoPipedrive> allOrganizacaoPipedrives = this.findAllOrganizations();

        List<OrganizacaoPipedrive> result = new ArrayList<OrganizacaoPipedrive>();
        for (OrganizacaoPipedrive organizacaoPipedrive : allOrganizacaoPipedrives) {
            if (organizacaoPipedrive.getName().equals(name)) {
                result.add(organizacaoPipedrive);
            }
        }

        return result;
    }

    public OrganizacaoPipedrive findById(Long id) {
        String organizationUri = "organizations/" + id;
        final URI uri = this.getURIForResource(organizationUri, null);

        HttpGet request = new HttpGet(uri);
        HttpResponse resp = null;
        try {
            resp = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        OrganizacaoPipedrive organizacaoPipedrive = null;
        try {
            String result = EntityUtils.toString(resp.getEntity());
            organizacaoPipedrive = this.jsonToEntity(result);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return organizacaoPipedrive;
    }

    public List<OrganizacaoPipedrive> findByIdOrName(String searchTerm) {
        List<OrganizacaoPipedrive> allOrganizacaoPipedrives = this.findAllOrganizations();
        List<Long> orgIds = new ArrayList<Long>();

        searchTerm = searchTerm.trim().toLowerCase();

        List<OrganizacaoPipedrive> result = new ArrayList<OrganizacaoPipedrive>();
        for (OrganizacaoPipedrive organizacaoPipedrive : allOrganizacaoPipedrives) {
            if (organizacaoPipedrive.getCombinedData().toLowerCase().contains(searchTerm)) {
                result.add(organizacaoPipedrive);
            }
        }

        Collections.sort(result, new Comparator<OrganizacaoPipedrive>() {
            @Override
            public int compare(OrganizacaoPipedrive lhs, OrganizacaoPipedrive rhs) {
                return lhs.getId() < rhs.getId() ? -1 : (lhs.getId() > rhs.getId()) ? 1 : 0;
            }
        });

        return result;
    }

    private Callable<List<OrganizacaoPipedrive>> getOrganizationCallableByPage(final Integer page) {
        Integer pipedriveApiPageSize = 500;

        String organizationUri = "organizations";

        Map<String, String> parameters = new HashMap<String, String>();
        Integer start = page * pipedriveApiPageSize;
        parameters.put("start", start.toString());
        parameters.put("limit", pipedriveApiPageSize.toString());

        final URI uri = this.getURIForResource(organizationUri, parameters);

        Callable<List<OrganizacaoPipedrive>> result = new Callable<List<OrganizacaoPipedrive>>()
        {
            @Override
            public List<OrganizacaoPipedrive> call() throws Exception
            {
                HttpGet request = new HttpGet(uri);
                HttpResponse resp = null;
                try {
                    resp = client.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }

                return PipedriveService.this.jsonArrayToEntityList(EntityUtils.toString(resp.getEntity()));
            }
        };

        return result;
    }

    private List<OrganizacaoPipedrive> jsonArrayToEntityList(String string) {
        Gson gson = new Gson();
        List<OrganizacaoPipedrive> organizacaoPipedrives = new ArrayList<OrganizacaoPipedrive>();
        JsonElement data = gson.fromJson(string, JsonObject.class).get("data");
        if (!data.isJsonNull()) {
            organizacaoPipedrives = Arrays.asList(gson.fromJson(data, OrganizacaoPipedrive[].class));
        }

        return organizacaoPipedrives;
    }

    private OrganizacaoPipedrive jsonToEntity(String string) {
        Gson gson = new Gson();
        List<OrganizacaoPipedrive> organizacaoPipedrives = new ArrayList<OrganizacaoPipedrive>();
        JsonElement data = gson.fromJson(string, JsonObject.class).get("data");
        if (data != null && !data.isJsonNull()) {
            organizacaoPipedrives = Arrays.asList(gson.fromJson(data, OrganizacaoPipedrive.class));
        }

        if (organizacaoPipedrives != null && organizacaoPipedrives.size() != 0) {
            return organizacaoPipedrives.get(0);
        }

        return null;
    }

    private URI getURIForResource(String resource, Map<String, String> queryParameters) {
        String pipedriveApi = "api.pipedrive.com/v1/";

        String pipedriveApiToken = "1994bfa46fa199b82ffe5762162622a97a04c258";

        URIBuilder builder = new URIBuilder().setScheme("https")
                .setHost(pipedriveApi)
                .setPath(resource)
                .setParameter("api_token", pipedriveApiToken);

        if (queryParameters != null) {
            for (Map.Entry<String, String> parameter : queryParameters.entrySet()) {
                builder.setParameter(parameter.getKey(), parameter.getValue());
            }
        }


        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return uri;
    }
}
