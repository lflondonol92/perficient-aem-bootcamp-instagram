package com.adobe.aem.guides.wknd.services;

import com.adobe.aem.guides.wknd.core.models.dto.instagram.InstagramPost;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface InstagramMediaService {

    InstagramPost getMediaById(String igMediaId) throws JsonProcessingException;

}
