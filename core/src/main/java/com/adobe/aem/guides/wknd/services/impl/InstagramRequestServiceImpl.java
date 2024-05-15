package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.entity.InstagramResponse;
import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.adobe.aem.guides.wknd.utils.InstagramConstants;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Inherited;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component(service = InstagramRequestService.class, immediate = true, property = {Constants.SERVICE_DESCRIPTION + "=Configuration for HTTP calls.",
        Constants.SERVICE_VENDOR + "=" + InstagramConstants.SERVICE_VENDOR, Constants.SERVICE_RANKING + ":Integer=1"})
@Designate(ocd = InstagramRequestServiceImpl.Configuration.class)
public class InstagramRequestServiceImpl implements InstagramRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramRequestServiceImpl.class);

    protected static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    protected static final int DEFAULT_SOCKET_TIMEOUT = 30000;
    protected static final int DEFAULT_MAX_CONNECTIONS = 200;
    protected static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 50;
    protected static final String DEFAULT_ACCESS_TOKEN = "EAAF1vW1sZA7EBOzbyWEnVt1vrZCwYSiARfg4U7nrtKHwTnnxvm7ZA8KxeOZAHCtZCX9ZArHIiVevvAS9R6qaKkKjxpKeZAENag0aioEjywqhF4QNJZBRkJZARLprHE0zNzIFlcZCjRKs3Tjft8OiBienFUMRusDT2JAX5Xit4xeDsnFaa73c0zkJwjR5VOwaPWKBNFIT1azwZDZD";
    protected static final String DEFAULT_IG_GRAPH_API_ENDPOINT = "https://graph.facebook.com/v19.0/";

    protected Integer maxConnections;
    protected Integer maxConnectionsPerHost;
    protected Integer connectionTimeout;
    protected Integer socketTimeout;
    protected String accessToken;
    protected String igGraphApiEndpoint;

    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;


    @ObjectClassDefinition(name = "IG - Connection Service", description = "Connection for all web service calls")
    @Inherited
    protected @interface Configuration {

        @AttributeDefinition(name = "Connection Timeout") int connectionTimeout() default DEFAULT_CONNECTION_TIMEOUT;

        @AttributeDefinition(name = "Socket Timeout") int socketTimeout() default DEFAULT_SOCKET_TIMEOUT;

        @AttributeDefinition(name = "Max Connections") int maxConnections() default DEFAULT_MAX_CONNECTIONS;

        @AttributeDefinition(name = "Max Connections per Host") int maxConnectionsPerHost() default DEFAULT_MAX_CONNECTIONS_PER_HOST;

        @AttributeDefinition(name = "Access Token") String accessToken() default DEFAULT_ACCESS_TOKEN;

        @AttributeDefinition(name = "Instagram Graph API V19.0") String igGraphEndpoint() default DEFAULT_IG_GRAPH_API_ENDPOINT;

    }

    @Activate
    protected void activate(Configuration config) {
        LOG.info("Activating IGRequestServiceImpl...");
        this.maxConnections = config.maxConnections();
        this.maxConnectionsPerHost = config.maxConnectionsPerHost();
        this.connectionTimeout = config.connectionTimeout();
        this.socketTimeout = config.socketTimeout();
        this.accessToken = config.accessToken();
        this.igGraphApiEndpoint = config.igGraphEndpoint();
        LOG.info("IGRequestServiceImpl was activated.");
    }

    private List<NameValuePair> getNameValuePairs(String fieldsValue) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", this.accessToken));
        nameValuePairs.add(new BasicNameValuePair("fields", fieldsValue));

        return nameValuePairs;
    }

    private CloseableHttpClient getConfiguredHttpClient() {
        HttpClientBuilder builder = httpClientBuilderFactory.newBuilder();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(connectionTimeout).build();
        builder.setDefaultRequestConfig(requestConfig);
        builder.setMaxConnTotal(maxConnections);
        builder.setMaxConnPerRoute(maxConnectionsPerHost);
        return builder.build();
    }

    public String callInstagramAPI(String id, String fields) {
        String jsonResponse = "";
        try (final CloseableHttpClient httpClient = getConfiguredHttpClient()) {
            URI uri = new URIBuilder(igGraphApiEndpoint + id)
                    .addParameters(getNameValuePairs(fields))
                    .build();
            HttpGet httpget = new HttpGet(uri);
            HttpResponse httpResponse = httpClient.execute(httpget);
            InstagramResponse response = new InstagramResponse(httpResponse);
            jsonResponse = response.getResponseBody();

            LOG.trace(jsonResponse);
        } catch (Exception e) {
            LOG.error("Error occurred in getMediaById {}", e.getMessage());
        }
        return jsonResponse;
    }

}
