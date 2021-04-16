package com.adobe.aem.guides.wknd.services;

import com.google.gson.JsonObject;

public interface InstagramMediaService {

    JsonObject getMediaById(String igMediaId);

    JsonObject getFeedById(String id);

    JsonObject getPostByURI(String uri);

}
