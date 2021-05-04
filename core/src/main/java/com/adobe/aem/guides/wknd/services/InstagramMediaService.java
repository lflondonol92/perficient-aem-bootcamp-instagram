package com.adobe.aem.guides.wknd.services;

import com.google.gson.JsonObject;

public interface InstagramMediaService {

    JsonObject getMediaById(String igMediaId);

    JsonObject getPostByURI(String uri);

}
