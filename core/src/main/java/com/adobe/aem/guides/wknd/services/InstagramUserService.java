package com.adobe.aem.guides.wknd.services;

import com.google.gson.JsonObject;

public interface InstagramUserService {
    JsonObject getUserInfo(String userId);
}
