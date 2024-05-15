package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.entity.InstagramResponse;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Inherited;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@Component(service = InstagramMediaService.class)
@Designate(ocd = InstagramMediaServiceImpl.Configuration.class)
public class InstagramMediaServiceImpl implements InstagramMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramMediaServiceImpl.class);

    protected static final String DEFAULT_ACCESS_TOKEN = "EAAF1vW1sZA7EBOzbyWEnVt1vrZCwYSiARfg4U7nrtKHwTnnxvm7ZA8KxeOZAHCtZCX9ZArHIiVevvAS9R6qaKkKjxpKeZAENag0aioEjywqhF4QNJZBRkJZARLprHE0zNzIFlcZCjRKs3Tjft8OiBienFUMRusDT2JAX5Xit4xeDsnFaa73c0zkJwjR5VOwaPWKBNFIT1azwZDZD";
    protected static final String DEFAULT_IG_GRAPH_API_ENDPOINT = "https://graph.facebook.com/v19.0/";
    protected static final String DEFAULT_IG_FIELDS = "caption,comments_count,ig_id,media_type,permalink,media_url,timestamp";


    @Reference
    InstagramRequestService requestService;

    private String igGraphAPIEndpoint;
    private String igFeedFields;
    private String igAccessToken;

    @ObjectClassDefinition(name = "IG - Media Service", description = "IG Media Service Configuration")
    @Inherited
    protected @interface Configuration {

        @AttributeDefinition(name = "Access Token") String accessToken() default DEFAULT_ACCESS_TOKEN;

        @AttributeDefinition(name = "Instagram Graph API V10.0") String igGraphEndpoint() default DEFAULT_IG_GRAPH_API_ENDPOINT;

        @AttributeDefinition(name = "Instagram Feed Fields") String igFeedFields() default DEFAULT_IG_FIELDS;

    }

    @Activate
    private void activate(Configuration config) {
        this.igGraphAPIEndpoint = config.igGraphEndpoint();
        this.igFeedFields = config.igFeedFields();
        this.igAccessToken = config.accessToken();

    }

    protected List<NameValuePair> getNameValuePairs(String fieldsValue) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", this.igAccessToken));
        nameValuePairs.add(new BasicNameValuePair("fields", fieldsValue));

        return nameValuePairs;
    }

    @Override
    public JsonObject getMediaById(String igMediaId) {
        JsonObject jsonResponse = null;
        try {
            final CloseableHttpClient httpClient = requestService.getConfiguredHttpClient();
            String igMediaUrl = igGraphAPIEndpoint + igMediaId;

            List<NameValuePair> nameValuePairs = this.getNameValuePairs(this.igFeedFields);

            URI uri = new URIBuilder(igMediaUrl)
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
