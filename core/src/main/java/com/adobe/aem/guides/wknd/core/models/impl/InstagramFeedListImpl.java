package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.InstagramFeedList;
import com.adobe.aem.guides.wknd.core.models.dto.instagram.InstagramPost;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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

    @OSGiService
    private InstagramMediaService igMediaService;

    @ChildResource @Via("resource") @Optional
    protected Resource igPosts;

    @Getter
    private boolean hasPosts = false;

    private List<InstagramPost> instagramPosts;


    @PostConstruct
    private void init() throws JsonProcessingException {
        LOGGER.info("init InstagramFeedListImpl");
        instagramPosts = new ArrayList<>();

        if(igPosts != null && igPosts.hasChildren()){
            Iterator<Resource> iterator = igPosts.getChildren().iterator();
            hasPosts = true;
            while(iterator.hasNext()){
                Resource igPostNode = iterator.next();
                ValueMap properties = igPostNode.adaptTo(ValueMap.class);
                assert properties != null;
                boolean isDisabled = BooleanUtils.toBooleanDefaultIfNull(properties.get("igMediaIsDisabled", Boolean.class),
                        Boolean.FALSE);
                if(!isDisabled){

                    final String igMediaID = properties.get("igMediaID", String.class);
                    InstagramPost instagramPost = igMediaService.getMediaById(igMediaID);
                    final String xfRelatedProductPath = properties.get("igProductRelatedXfPath", String.class);
                    if(!StringUtils.isBlank(xfRelatedProductPath)){
                        instagramPost.setXfRelatedProductPath(xfRelatedProductPath);
                    }

                    instagramPosts.add(instagramPost);
                }
            }
        }

        LOGGER.info("finish initialization");
    }

    @Override public List<InstagramPost> getPosts() {
        return instagramPosts;
    }

}


