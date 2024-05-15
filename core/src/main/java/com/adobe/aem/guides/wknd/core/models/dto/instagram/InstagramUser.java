package com.adobe.aem.guides.wknd.core.models.dto.instagram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class InstagramUser {
    String id;
    String name;
    String profilePictureUrl;

    @SuppressWarnings("unchecked")
    @JsonProperty("picture")
    private void unpackNested(Map<String,Object> picture) {
        this.profilePictureUrl = ((Map<String,String>)picture.get("data")).get("url");
    }
}
