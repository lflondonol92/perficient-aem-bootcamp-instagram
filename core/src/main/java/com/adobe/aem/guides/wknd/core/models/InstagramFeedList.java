package com.adobe.aem.guides.wknd.core.models;


import com.adobe.aem.guides.wknd.core.models.dto.instagram.v2.GraphqlV2;

import java.util.List;

public interface InstagramFeedList {

    List<GraphqlV2> getPosts();

}
