package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.entity.InstagramResponse;
import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.adobe.aem.guides.wknd.services.InstagramUserService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component(service = InstagramUserService.class)
public class InstagramUserServiceImpl implements InstagramUserService {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramUserServiceImpl.class);

    protected static final String DEFAULT_ACCESS_TOKEN = "EAAF1vW1sZA7EBOzbyWEnVt1vrZCwYSiARfg4U7nrtKHwTnnxvm7ZA8KxeOZAHCtZCX9ZArHIiVevvAS9R6qaKkKjxpKeZAENag0aioEjywqhF4QNJZBRkJZARLprHE0zNzIFlcZCjRKs3Tjft8OiBienFUMRusDT2JAX5Xit4xeDsnFaa73c0zkJwjR5VOwaPWKBNFIT1azwZDZD";
    protected static final String DEFAULT_IG_GRAPH_API_ENDPOINT = "https://graph.facebook.com/v19.0/";
    protected static final String DEFAULT_IG_FIELDS = "id,name,picture";
    protected static final String DEFAULT_IG_USER_ID = "10161708314639083";
    @Reference
    InstagramRequestService requestService;

    protected List<NameValuePair> getNameValuePairs(String fieldsValue) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", DEFAULT_ACCESS_TOKEN));
        nameValuePairs.add(new BasicNameValuePair("fields", fieldsValue));

        return nameValuePairs;
    }

    @Override
    public JsonObject getUserInfo(String userId) {
        JsonObject jsonResponse = null;
        try {
            final CloseableHttpClient httpClient = requestService.getConfiguredHttpClient();
            String igUserUrl = DEFAULT_IG_GRAPH_API_ENDPOINT + DEFAULT_IG_USER_ID;

            List<NameValuePair> nameValuePairs = this.getNameValuePairs(DEFAULT_IG_FIELDS);

            URI uri = new URIBuilder(igUserUrl)
                    .addParameters(nameValuePairs)
                    .build();
            HttpGet httpget = new HttpGet(uri);
            HttpResponse httpResponse = httpClient.execute(httpget);
            InstagramResponse response = new InstagramResponse(httpResponse);
            String jsonString = response.getResponseBody();

            LOG.trace(jsonString);
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            jsonResponse = jsonElement.getAsJsonObject();
            httpClient.close();

        } catch (Exception e) {
            LOG.error("Error occurred in getMediaById {}", e.getMessage());
        }

        return jsonResponse;
    }
}
