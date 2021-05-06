package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import com.adobe.aem.guides.wknd.core.models.InstagramFeedList;
import com.adobe.aem.guides.wknd.core.models.dto.instagram.DisplayResource;
import com.adobe.aem.guides.wknd.core.models.dto.instagram.GraphImageNode;
import com.adobe.aem.guides.wknd.core.models.dto.instagram.Graphql;
import com.adobe.aem.guides.wknd.core.models.dto.instagram.Node;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {InstagramFeedList.class},
        resourceType = {InstagramFeedListImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class InstagramFeedListImpl implements InstagramFeedList {

    protected static final String RESOURCE_TYPE = "wknd/components/instagram-feed-list";

    // Add a logger for any errors
    private static final Logger LOGGER = LoggerFactory.getLogger(InstagramFeedListImpl.class);
    private static final String IMG_BASE64_PREFIX = "data:image/jpg;base64,";

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private InstagramMediaService igMediaService;

    @ChildResource @Via("resource") @Optional
    protected Resource igPosts;

    private boolean hasPosts = false;

    private List<Graphql> instagramPosts;


    @PostConstruct
    private void init() {
        LOGGER.info("init InstagramFeedListImpl");
        instagramPosts = new ArrayList<>();
        Gson gson = new GsonBuilder().create();

        if(igPosts != null && igPosts.hasChildren()){
            Iterator<Resource> iterator = igPosts.getChildren().iterator();
            hasPosts = true;
            while(iterator.hasNext()){
                Resource igPostNode = iterator.next();
                ValueMap properties = igPostNode.adaptTo(ValueMap.class);
                Boolean isDisabled = BooleanUtils.toBooleanDefaultIfNull(properties.get("igMediaIsDisabled", Boolean.class),
                        Boolean.FALSE);
                if(!isDisabled){
                    final String igMediaUrl = properties.get("igMediaUrl", String.class);
                    JsonObject jsonObject = igMediaService.getPostByURI(igMediaUrl);
                    Graphql graphql = gson.fromJson(jsonObject.get("graphql").toString(), Graphql.class);
                    final String xfRelatedProductPath = properties.get("igProductRelatedXfPath", String.class);
                    if(!StringUtils.isBlank(xfRelatedProductPath)){
                        graphql.setXfRelatedProductPath(xfRelatedProductPath);
                    }
                    updateGraphToBaseIGURI(graphql);
                    instagramPosts.add(graphql);
                }
            }
        }

        LOGGER.info("finish initialization");
    }

    @Override public List<Graphql> getPosts() {
        return instagramPosts;
    }

    public boolean isHasPosts() {
        return hasPosts;
    }

    protected void updateGraphToBaseIGURI(Graphql graphql){
        if(graphql != null){
            String base64Src;
            DisplayResource[] resources = graphql.getShortcode_media().getDisplay_resources();
            //Set all the resources images
            for(DisplayResource dr: resources){
                String src = dr.getSrc();
                base64Src = igMediaService.getImageToBase64(src);
                dr.setSrc(new String(IMG_BASE64_PREFIX + base64Src));
            }
            graphql.getShortcode_media().setDisplay_resources(resources);

            //profile pic_url
            final String profilePicUrl = graphql.getShortcode_media().getOwner().getProfile_pic_url();
            base64Src = igMediaService.getImageToBase64(profilePicUrl);
            graphql.getShortcode_media().getOwner().setProfile_pic_url(new String(IMG_BASE64_PREFIX + base64Src));

            //edges
            if(graphql.getShortcode_media().getEdge_sidecar_to_children() !=null &&
                    graphql.getShortcode_media().getEdge_sidecar_to_children().getEdges()!= null){
                GraphImageNode[] edges = graphql.getShortcode_media().getEdge_sidecar_to_children().getEdges();
                for(int i=0; i< edges.length; i++){
                    Node node = edges[i].getNode();
                    final String displayUrl = node.getDisplay_url();
                    base64Src = igMediaService.getImageToBase64(displayUrl);
                    node.setDisplay_url(new String(IMG_BASE64_PREFIX + base64Src));
                }
            }

        }
    }

}


