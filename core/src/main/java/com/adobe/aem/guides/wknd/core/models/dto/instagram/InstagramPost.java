package com.adobe.aem.guides.wknd.core.models.dto.instagram;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstagramPost {
    private String ig_id;
    private String caption;
    private String comments_count;
    private String id;
    private String media_type;
    private String media_url;
    private String permalink;
    private String xfRelatedProductPath;
    private String timestamp;
}
