package com.adobe.aem.guides.wknd.core.models;


import com.adobe.aem.guides.wknd.core.models.dto.instagram.Graphql;

import java.util.List;

public interface InstagramFeedList {

    List<Graphql> getPosts();

}
