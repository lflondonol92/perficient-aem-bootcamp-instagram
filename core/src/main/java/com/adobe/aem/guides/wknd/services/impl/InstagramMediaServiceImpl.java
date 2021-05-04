package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.entity.InstagramResponse;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.adobe.aem.guides.wknd.utils.InstagramConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
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

    protected static final String DEFAULT_ACCESS_TOKEN = "EAAC8ZCVnhe18BAHC30gFOLGwGOfqPEmfLwB8NGRZAMYb2WejoVwnKzGpGGdFd8NQAKcWGkI4KeLjEXQZAlYxEbuvcrtjxv0ZBGTppGlnVUP61UO6hwck5T4DDVK52XjZB8DmnjwZCCvfcT0BgSTDwLTnUOfeBCZAji9Kq7Pd2doZCdZBUZAKrQRv7h";
    protected static final String DEFAULT_IG_GRAPH_API_ENDPOINT = "https://graph.facebook.com/v10.0/";
    protected static final String DEFAULT_IG_FIELDS = "caption, comments_count,ig_id,is_comment_enabled,media_product_type,media_type,media_url,permalink,shortcode,thumbnail_url,children";
                                                      //caption,comments_count,ig_id,media_type,permalink,media_url,thumbnail_url,timestamp,children
    protected static final String[] DEFAULT_IG_MEDIA_IDS = new String[] {"17841447226224242"};


    @Reference InstagramRequestService requestService;

    private String igGraphAPIEndpoint;
    private String igFeedFields;
    private String igAccessToken;
    private String[] igMediaId;
    private List<JsonObject> igMediaCache;

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
        this.igMediaId = DEFAULT_IG_MEDIA_IDS;

        if(igMediaCache == null || (igMediaCache != null && igMediaCache.size()==0)){
            loadMediaIdCache();
        }

    }

    protected void loadMediaIdCache(){
        igMediaCache = new ArrayList<>();
        for(String mediaId : igMediaId){
            JsonObject jsonObject = getMediaById(mediaId);
            if(jsonObject != null){
                igMediaCache.add(jsonObject);
            }
        }

    }

    protected List getNameValuePairs(String fieldsValue){
        List nameValuePairs = new ArrayList();
        nameValuePairs.add(new BasicNameValuePair("access_token", this.igAccessToken));
        nameValuePairs.add(new BasicNameValuePair("fields", fieldsValue));

        return nameValuePairs;
    }

    @Override public JsonObject getMediaById(String igMediaId) {
        JsonObject jsonResponse = null;
        try{
            final CloseableHttpClient httpClient = requestService.getConfiguredHttpClient();
            StringBuilder igMediaUrl = new StringBuilder(igGraphAPIEndpoint)
                    .append(igMediaId).append("/media");

            List nameValuePairs = this.getNameValuePairs(this.igFeedFields);

            URI uri = new URIBuilder(igMediaUrl.toString())
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

        }catch(Exception e){
            LOG.error("Error occurred in getMediaById {}", e.getMessage());
        }

        return jsonResponse;
    }


    protected JsonObject getChildrenDetailsByMediaId(String igMediaId){
        JsonObject jsonResponse = null;
        try {

            final CloseableHttpClient httpClient = requestService.getConfiguredHttpClient();
            StringBuilder igMediaUrl = new StringBuilder(igGraphAPIEndpoint)
                    .append(igMediaId).append("/children");
            final String childrenFields = "id,media_type,media_url";
            List nameValuePairs = this.getNameValuePairs(childrenFields);

            URI uri = new URIBuilder(igMediaUrl.toString())
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

        }catch(Exception e){
            LOG.error("Error occurred in getMediaById {}", e.getMessage());
        }
        return jsonResponse;
    }

    @Override public JsonObject getPostByURI(String uri) {

        JsonObject jsonResponse = null;
        if(igMediaCache != null && igMediaCache.size()>0){
            for(JsonObject jsonObject : igMediaCache) {
                JsonArray jsonArray = jsonObject.getAsJsonArray("data");
                for(int i=0; i < jsonArray.size(); i++){
                    JsonObject igMediaItem = jsonArray.get(i).getAsJsonObject();
                    final JsonElement permalink = igMediaItem.get("permalink");
                    if(permalink != null && permalink.getAsString().contains(uri)) {
                        jsonResponse = igMediaItem;
                        if(hasChildren(jsonResponse)){
                            //here add the details for the children
                            final String id = igMediaItem.get("id").getAsString();
                            jsonResponse.add("children_details", this.getChildrenDetailsByMediaId(id));
                            return jsonResponse;
                        }
                    }
                }
            }
        }

        return jsonResponse;
    }

    protected boolean hasChildren(JsonObject igMediaItem){
        final JsonElement mediaType = igMediaItem.get("media_type");
        final JsonElement children = igMediaItem.get("children");
        boolean mediaTypeIsAlbum = mediaType != null &&
                mediaType.getAsString().equals(InstagramConstants.IG_MEDIA_TYPE_CAROUSEL_ALBUM);

        return (children != null && mediaTypeIsAlbum);
    }
}
