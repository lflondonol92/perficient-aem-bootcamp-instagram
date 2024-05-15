package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.InstagramUserModel;
import com.adobe.aem.guides.wknd.core.models.dto.instagram.InstagramUser;
import com.adobe.aem.guides.wknd.services.InstagramUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {InstagramUserModel.class},
        resourceType = {InstagramFeedListImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class InstagramUserModelImpl implements InstagramUserModel {

    // Add a logger for any errors
    private static final Logger LOGGER = LoggerFactory.getLogger(InstagramUserModelImpl.class);

    @OSGiService
    private InstagramUserService instagramUserService;

    @Getter
    InstagramUser instagramUser;

    @PostConstruct
    private void init() throws JsonProcessingException {
        LOGGER.info("init InstagramFeedListImpl");
        instagramUser = instagramUserService.getUserInfo();
    }
}
