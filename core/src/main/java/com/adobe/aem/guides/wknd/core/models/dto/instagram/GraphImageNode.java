package com.adobe.aem.guides.wknd.core.models.dto.instagram;

public class GraphImageNode {

    private Node node;
    private String display_url;
    private String accessibility_caption;
    private boolean is_video;

    public String getDisplay_url() {
        return display_url;
    }

    public void setDisplay_url(String display_url) {
        this.display_url = display_url;
    }

    public String getAccessibility_caption() {
        return accessibility_caption;
    }

    public void setAccessibility_caption(String accessibility_caption) {
        this.accessibility_caption = accessibility_caption;
    }

    public boolean isIs_video() {
        return is_video;
    }

    public void setIs_video(boolean is_video) {
        this.is_video = is_video;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
