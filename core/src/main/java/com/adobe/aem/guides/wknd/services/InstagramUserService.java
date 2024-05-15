package com.adobe.aem.guides.wknd.services;

import com.adobe.aem.guides.wknd.core.models.dto.instagram.InstagramUser;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface InstagramUserService {
    InstagramUser getUserInfo() throws JsonProcessingException;
}
