package com.adobe.aem.guides.wknd.core.models.dto.instagram;

public class Graphql {

    ShortCodeMedia shortcode_media;
    private String xfRelatedProductPath;

    public Graphql(){
    }

    public ShortCodeMedia getShortcode_media() {
        return shortcode_media;
    }

    public void setShortcode_media(ShortCodeMedia shortcode_media) {
        this.shortcode_media = shortcode_media;
    }

    public String getXfRelatedProductPath() {
        return xfRelatedProductPath;
    }

    public void setXfRelatedProductPath(String xfRelatedProductPath) {
        this.xfRelatedProductPath = xfRelatedProductPath;
    }
}
