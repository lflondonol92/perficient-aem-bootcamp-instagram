package com.adobe.aem.guides.wknd.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

public class InstagramResponse {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramResponse.class);

    public static final int SC_OK = 200;
    public static final int SC_CREATED = 201;
    public static final int SC_ACCEPTED = 202;

    private int status;
    private String responseBody;
    private HttpResponse response;
    private byte[] responseBodyBytesArr;
    private String contentType;

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

    public BufferedImage getResponseImage(){
        BufferedImage img = null;
        try{
            if (response.getEntity() != null) {
                img = ImageIO.read(response.getEntity().getContent());
            }
        }catch (Exception e){
            LOG.error("Error in getting image response {}", e.getMessage());
        }
        return img;
    }

    public byte[] getResponseBodyAsByteArr(){
        if (StringUtils.isBlank(responseBody) && response.getEntity() != null) {
            try {
                responseBodyBytesArr = IOUtils.toByteArray(response.getEntity().getContent());
                contentType = response.getEntity().getContentType().getValue();
            } catch (IOException e) {
                LOG.error("response could not be parsed.", e);
            }
        }
        return responseBodyBytesArr;
    }

    public void setResponseBody(final String responseBody) {
        this.responseBody = responseBody;
    }

    public HttpResponse getResponse() {
        return response;
    }

}
