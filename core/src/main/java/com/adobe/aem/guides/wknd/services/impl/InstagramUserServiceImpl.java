package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.core.models.dto.instagram.InstagramUser;
import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.adobe.aem.guides.wknd.services.InstagramUserService;
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

@Component(service = InstagramUserService.class)
@Designate(ocd = InstagramUserServiceImpl.Configuration.class)

public class InstagramUserServiceImpl implements InstagramUserService {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramUserServiceImpl.class);

    protected static final String DEFAULT_IG_USER_FIELDS = "id,name,picture";
    protected static final String DEFAULT_IG_USER_ID = "10161708314639083";

    @Reference
    InstagramRequestService requestService;

    private final ObjectMapper mapper = new ObjectMapper();
    private String igUserFields;
    private String igUserId;

    @ObjectClassDefinition(name = "IG - User Service", description = "IG User Service Configuration")
    @Inherited
    protected @interface Configuration {

        @AttributeDefinition(name = "Instagram User Fields") String igUserFields() default DEFAULT_IG_USER_FIELDS;

        @AttributeDefinition(name = "Instagram User Fields") String igUserId() default DEFAULT_IG_USER_ID;

    }

    @Activate
    private void activate(Configuration config) {
        this.igUserFields = config.igUserFields();
        this.igUserId = config.igUserId();
    }

    @Override
    public InstagramUser getUserInfo() throws JsonProcessingException {
        LOG.info("igUserId: {}", igUserId);
        LOG.info("igUserFields: {}", igUserFields);
        String jsonResponse = requestService.callInstagramAPI(igUserId, igUserFields);
        LOG.info("JSON Response: {}", jsonResponse);
        return mapper.readValue(jsonResponse, InstagramUser.class);
    }
}
