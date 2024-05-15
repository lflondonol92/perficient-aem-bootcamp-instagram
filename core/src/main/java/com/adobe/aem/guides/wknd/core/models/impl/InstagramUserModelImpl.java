package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.InstagramUserModel;
import com.adobe.aem.guides.wknd.core.models.dto.instagram.InstagramUser;
import com.adobe.aem.guides.wknd.services.InstagramUserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

public class InstagramUserModelImpl implements InstagramUserModel {

    // Add a logger for any errors
    private static final Logger LOGGER = LoggerFactory.getLogger(InstagramUserModelImpl.class);

    @OSGiService
    private InstagramUserService instagramUserService;

    @Getter
    InstagramUser instagramUser;

    @PostConstruct
    private void init() {
        LOGGER.info("init InstagramFeedListImpl");
        instagramUser = new InstagramUser();
        Gson gson = new GsonBuilder().create();
        JsonObject jsonObject = instagramUserService.getUserInfo("");
        instagramUser = gson.fromJson(jsonObject, InstagramUser.class);
    }
}
