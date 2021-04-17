package com.adobe.aem.guides.wknd.core.models.dto.instagram;

public class ShortCodeMedia extends Node {

    private EdgeChildren edge_media_to_caption;
    private EdgeChildren edge_sidecar_to_children;

    public ShortCodeMedia(){
    }

    public EdgeChildren getEdge_media_to_caption() {
        return edge_media_to_caption;
    }

    public void setEdge_media_to_caption(EdgeChildren edge_media_to_caption) {
        this.edge_media_to_caption = edge_media_to_caption;
    }

    public EdgeChildren getEdge_sidecar_to_children() {
        return edge_sidecar_to_children;
    }

    public void setEdge_sidecar_to_children(EdgeChildren edge_sidecar_to_children) {
        this.edge_sidecar_to_children = edge_sidecar_to_children;
    }
}
