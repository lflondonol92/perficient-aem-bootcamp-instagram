package com.adobe.aem.guides.wknd.core.models.dto.instagram.v2;

public class GraphqlV2 extends Node {

    private String ig_id;
    private String comments_count;
    private String media_product_type;
    private String permalink;
    private Children children_details;
    private String xfRelatedProductPath;

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

    public String getMedia_product_type() {
        return media_product_type;
    }

    public void setMedia_product_type(String media_product_type) {
        this.media_product_type = media_product_type;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public Children getChildren_details() {
        return children_details;
    }

    public void setChildren_details(Children children_details) {
        this.children_details = children_details;
    }

    public String getXfRelatedProductPath() {
        return xfRelatedProductPath;
    }

    public void setXfRelatedProductPath(String xfRelatedProductPath) {
        this.xfRelatedProductPath = xfRelatedProductPath;
    }

}
