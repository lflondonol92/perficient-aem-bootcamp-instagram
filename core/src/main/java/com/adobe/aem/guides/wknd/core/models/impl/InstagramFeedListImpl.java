package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.adobe.aem.guides.wknd.core.models.InstagramFeedList;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;


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

    @ScriptVariable
    private Page currentPage;

    @PostConstruct
    private void init() {
        LOGGER.info("init component");
        final String testUrl = "https://www.instagram.com/p/CNogcwMDand/";
        JsonObject jsonObject = igMediaService.getPostByURI(testUrl);
        LOGGER.info(jsonObject.toString());
        LOGGER.info("finish component");
    }

    @Override public List<Object> getPosts() {
        List<Object> emptyList = new ArrayList<>();
        emptyList.add("1");
        emptyList.add("2");
        emptyList.add("3");
        return emptyList;
    }
}


