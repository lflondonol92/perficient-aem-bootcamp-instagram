package com.adobe.aem.guides.wknd.core.models.dto.instagram;

public class Node {

    private String __typename;
    private String id;
    private String text;
    private String shortcode;
    private Dimensions dimensions;
    private String display_url;
    private boolean is_video;
    private DisplayResource[] display_resources;

    public Node(){
    }

    public String get__typename() {
        return __typename;
    }

    public void set__typename(String __typename) {
        this.__typename = __typename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

    public boolean isIs_video() {
        return is_video;
    }

    public void setIs_video(boolean is_video) {
        this.is_video = is_video;
    }

    public DisplayResource[] getDisplay_resources() {
        return display_resources;
    }

    public void setDisplay_resources(DisplayResource[] display_resources) {
        this.display_resources = display_resources;
    }
}
