package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.core.models.dto.instagram.InstagramPost;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;
import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Inherited;


@Component(service = InstagramMediaService.class)
@Designate(ocd = InstagramMediaServiceImpl.Configuration.class)
public class InstagramMediaServiceImpl implements InstagramMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramMediaServiceImpl.class);

    protected static final String DEFAULT_IG_FIELDS = "caption,comments_count,ig_id,media_type,permalink,media_url,timestamp";


    @Reference
    InstagramRequestService requestService;

    private final ObjectMapper mapper = new ObjectMapper();
    private String igFeedFields;

    @ObjectClassDefinition(name = "IG - Media Service", description = "IG Media Service Configuration")
    @Inherited
    protected @interface Configuration {

        @AttributeDefinition(name = "Instagram Feed Fields") String igFeedFields() default DEFAULT_IG_FIELDS;

    }

    @Activate
    private void activate(Configuration config) {
        this.igFeedFields = config.igFeedFields();
    }


    @Override
    public InstagramPost getMediaById(String igMediaId) throws JsonProcessingException {
        LOG.info("igMediaId = {}", igMediaId);
        String jsonResponse = requestService.callInstagramAPI(igMediaId, this.igFeedFields);
        LOG.info("JSON Response: {}", jsonResponse);
        return mapper.readValue(jsonResponse, InstagramPost.class);
    }
}
