package com.adobe.aem.guides.wknd.core.models.dto.instagram;

import com.google.gson.Gson;

public class ShortCodeMedia extends Node {

    private EdgeChildren edge_media_to_caption;
    private EdgeChildren edge_sidecar_to_children;
    private Owner owner;
    private final Gson gson = new Gson();

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

    public String getEdgeMediaToCaption(){
        if(edge_media_to_caption != null){
            return gson.toJson(edge_media_to_caption);
        }
        return null;
    }

    public String getEdgeSidecarToChildren(){
        if(edge_sidecar_to_children != null){
            return gson.toJson(edge_sidecar_to_children);
        }
        return null;
    }

    public String getEdgeOwner(){
        if(owner !=null){
            return gson.toJson(owner);
        }
        return null;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
