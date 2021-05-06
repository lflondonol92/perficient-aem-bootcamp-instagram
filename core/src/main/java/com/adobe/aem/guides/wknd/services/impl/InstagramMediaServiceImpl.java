package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.entity.InstagramResponse;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
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

import javax.imageio.ImageIO;
import javax.json.Json;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Inherited;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;


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


    @Override public JsonObject getPostByURI(String uri) {
        JsonObject jsonResponse = null;

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

            LOG.trace(jsonString);
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            jsonResponse = jsonElement.getAsJsonObject();

            httpClient.close();

        } catch (IOException e) {
            LOG.error("Unable to get the response from Instagram", e);
        }
        return jsonResponse;
    }

    /*protected JsonObject convertUrlToBase64(JsonObject jsonObject){
        JsonElement root = jsonObject.get("graphql");
        final String display_url = root.getAsJsonObject().get("shortcode_media")
                .getAsJsonObject().get("display_url").getAsString();
        String imageBase64 = getImageToBase64(display_url);
        if(StringUtils.isNotBlank(imageBase64)){
            root.getAsJsonObject().get("shortcode_media")
                    .getAsJsonObject().addProperty("display_url", imageBase64);
            jsonObject = root.getAsJsonObject();
        }

        return jsonObject;
    }*/

    @Override
    public String getImageToBase64(String src){
        final CloseableHttpClient httpClient = requestService.getConfiguredHttpClient();
        String stringBase64 = StringUtils.EMPTY;
        URI uri = null;
        try{
            uri = new URI(src);
        }catch (URISyntaxException e){
            LOG.error("Error un URI {}", e.getMessage());
        }
        StringBuilder igFeedUrl = new StringBuilder(src);

        HttpGet httpget = new HttpGet(igFeedUrl.toString());
        httpget.addHeader("method","GET");
        if(uri != null){
            httpget.addHeader("authority",uri.getHost());
            httpget.addHeader("path", uri.getPath());
        }
        httpget.addHeader("scheme","https");
        httpget.addHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpget.addHeader("user-agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:87.0) Gecko/20100101 Firefox/87.0");
        httpget.addHeader("accept-encoding","gzip, deflate, br");
        httpget.addHeader("Connection","keep-alive");
        httpget.addHeader("accept-language","en-US,en;q=0.9,fr;q=0.8");
        httpget.addHeader("cache-control","no-cache");
        httpget.addHeader("pragma","no-cache");

        httpget.addHeader("sec-fetch-dest","document");
        httpget.addHeader("sec-fetch-mode","navigate");
        httpget.addHeader("sec-fetch-site","none");
        httpget.addHeader("sec-fetch-user","?1");


        try {
            HttpResponse httpResponse = httpClient.execute(httpget);
            InstagramResponse response = new InstagramResponse(httpResponse);
            //
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(response.getResponseImage(), "jpg", baos);
            stringBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            httpClient.close();

        } catch (IOException e) {
            LOG.error("Unable to get the response from Instagram", e);
        }
        return stringBase64;
    }

}
