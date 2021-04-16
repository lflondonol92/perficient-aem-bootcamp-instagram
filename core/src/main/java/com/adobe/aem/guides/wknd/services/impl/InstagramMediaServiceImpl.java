package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.entity.InstagramResponse;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Inherited;


@Component(service = InstagramMediaService.class)
@Designate(ocd = InstagramMediaServiceImpl.Configuration.class)
public class InstagramMediaServiceImpl implements InstagramMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramMediaServiceImpl.class);

    protected static final String DEFAULT_ACCESS_TOKEN = "";
    protected static final String DEFAULT_IG_GRAPH_API_ENDPOINT = "https://graph.facebook.com/v10.0/";
    protected static final String DEFAULT_IG_FIELDS = "caption,comments_count,ig_id,is_comment_enabled,media_product_type,media_type,media_url,permalink,shortcode,thumbnail_url,children";

    @Reference InstagramRequestService requestService;
    //@Reference ResourceResolverFactory resourceResolverFactory;

    private String url;
    private String igFeedFields;
    private String accessToken;

    @ObjectClassDefinition(name = "IG - Media Service", description = "IG Media Service Configuration")
    @Inherited
    protected @interface Configuration {

        @AttributeDefinition(name = "Access Token") String accessToken() default DEFAULT_ACCESS_TOKEN;

        @AttributeDefinition(name = "Instagram Graph API V10.0") String igGraphEndpoint() default DEFAULT_IG_GRAPH_API_ENDPOINT;

        @AttributeDefinition(name = "Instagram Feed Fields") String igFeedFields() default DEFAULT_IG_FIELDS;

    }

    @Activate
    private void activate(Configuration config) {
        this.url = config.igGraphEndpoint();
        this.igFeedFields = config.igFeedFields();
        this.accessToken = config.accessToken();
    }


    @Override public JsonObject getMediaById(String igMediaId) {
        JsonObject jsonResponse = null;
        final CloseableHttpClient httpClient = requestService.getConfiguredHttpClient();
        StringBuilder igUrl = new StringBuilder(DEFAULT_IG_GRAPH_API_ENDPOINT);
        return jsonResponse;
    }

    @Override public JsonObject getFeedById(String id) {
        return null;
    }

    @Override public JsonObject getPostByURI(String uri) {
        JsonObject jsonResponse = null;
        //JsonReader jsonReader = null;

        final CloseableHttpClient httpClient = requestService.getConfiguredHttpClient();
        StringBuilder igFeedUrl = new StringBuilder(uri);
        igFeedUrl.append("?__a=1");
        HttpGet httpget = new HttpGet(igFeedUrl.toString());
        httpget.addHeader("Host","www.instagram.com");
        httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpget.addHeader("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:87.0) Gecko/20100101 Firefox/87.0");
        httpget.addHeader("Accept-Encoding","gzip, deflate, br");
        httpget.addHeader("Connection","keep-alive");


        try {
            HttpResponse httpResponse = httpClient.execute(httpget);
            InstagramResponse response = new InstagramResponse(httpResponse);
            String jsonString = response.getResponseBody();
            //Check to make sure JSON is well formed. PIM HTML starts with [
            if (!StringUtils.startsWith(jsonString, "{")) {
                //remove the [] from the response to make it flat JSON
                //String noArray = StringUtils.substring(StringUtils.substringBeforeLast(jsonString, "]"), 1);
                //add surrounding {} to make it will formed JSON
            }
            LOG.trace(jsonString);
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            jsonResponse = jsonElement.getAsJsonObject();
            httpClient.close();

        } catch (IOException e) {
            LOG.error("Unable to get the response from Salsify", e);
        } finally {
            /*if (jsonReader != null) {
                jsonReader.close();
            }*/
        }
        return jsonResponse;
    }
}
