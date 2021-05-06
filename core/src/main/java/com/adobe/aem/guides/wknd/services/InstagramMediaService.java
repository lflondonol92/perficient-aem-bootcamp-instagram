package com.adobe.aem.guides.wknd.services;

import com.google.gson.JsonObject;

public interface InstagramMediaService {

    JsonObject getMediaById(String igMediaId);

    String getImageToBase64(String uri);

    JsonObject getPostByURI(String uri);

}
