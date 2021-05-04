package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import com.adobe.aem.guides.wknd.core.models.InstagramFeedList;
import com.adobe.aem.guides.wknd.core.models.dto.instagram.v2.GraphqlV2;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
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

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private InstagramMediaService igMediaService;

    @ChildResource @Via("resource") @Optional
    protected Resource igPosts;

    private boolean hasPosts = false;

    private List<GraphqlV2> instagramPosts;


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

                    GraphqlV2 graphql = gson.fromJson(jsonObject.toString(), GraphqlV2.class);
                    final String xfRelatedProductPath = properties.get("igProductRelatedXfPath", String.class);
                    if(!StringUtils.isBlank(xfRelatedProductPath)){
                        graphql.setXfRelatedProductPath(xfRelatedProductPath);
                    }

                    instagramPosts.add(graphql);
                }
            }
        }

        LOGGER.info("finish initialization");
    }

    @Override public List<GraphqlV2> getPosts() {
        return instagramPosts;
    }

    public boolean isHasPosts() {
        return hasPosts;
    }
}


