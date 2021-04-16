package com.adobe.aem.guides.wknd.entity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstagramResponse {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramResponse.class);

    public static final int SC_OK = 200;
    public static final int SC_CREATED = 201;
    public static final int SC_ACCEPTED = 202;

    private int status;
    private String responseBody;
    private HttpResponse response;

    public InstagramResponse(HttpResponse httpResponse) {
        LOG.debug("Creating IG Media object base on HttpResponse...");
        response = httpResponse;
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            status = httpResponse.getStatusLine().getStatusCode();
        }
        LOG.debug("IG Media object was created...");
    }

    public boolean success() {
        return status == SC_OK || status == SC_CREATED || status == SC_ACCEPTED;
    }

    public int getStatus() {
        return status;
    }

    public String getResponseBody() {
        if (StringUtils.isBlank(responseBody) && response.getEntity() != null) {
            try {
                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOG.error("response could not be parsed.", e);
            }
        }
        return responseBody;
    }

    public void setResponseBody(final String responseBody) {
        this.responseBody = responseBody;
    }

    public HttpResponse getResponse() {
        return response;
    }

}
