package com.adobe.aem.guides.wknd.core.models.dto.instagram;

public class Graphql {
    private String ig_id;
    private String caption;
    private String comments_count;
    private String id;
    private String media_type;
    private String media_url;
    private String permalink;
    private String xfRelatedProductPath;
    private String timestamp;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIg_id() {
        return ig_id;
    }

    public void setIg_id(String ig_id) {
        this.ig_id = ig_id;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getXfRelatedProductPath() {
        return xfRelatedProductPath;
    }

    public void setXfRelatedProductPath(String xfRelatedProductPath) {
        this.xfRelatedProductPath = xfRelatedProductPath;
    }

}
