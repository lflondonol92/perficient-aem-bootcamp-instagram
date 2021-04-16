package com.adobe.aem.guides.wknd.services.impl;

import com.adobe.aem.guides.wknd.services.InstagramRequestService;
import com.adobe.aem.guides.wknd.utils.InstagramConstants;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

@Component(service = InstagramRequestService.class, immediate = true, property = {Constants.SERVICE_DESCRIPTION + "=Configuration for HTTP calls.",
        Constants.SERVICE_VENDOR + "=" + InstagramConstants.SERVICE_VENDOR, Constants.SERVICE_RANKING + ":Integer=1"})
@Designate(ocd = InstagramRequestServiceImpl.Configuration.class)
public class InstagramRequestServiceImpl implements InstagramRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(InstagramRequestServiceImpl.class);

    protected static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    protected static final int DEFAULT_SOCKET_TIMEOUT = 30000;
    protected static final int DEFAULT_MAX_CONNECTIONS = 200;
    protected static final int DEFAULT_MAX_CONNECTIONS_PER_HOST = 50;

    protected Integer maxConnections;
    protected Integer maxConnectionsPerHost;
    protected Integer connectionTimeout;
    protected Integer socketTimeout;

    @Reference private HttpClientBuilderFactory httpClientBuilderFactory;


    @ObjectClassDefinition(name = "IG - Connection Service", description = "Connection for all web service calls")
    @Inherited
    protected @interface Configuration {

        @AttributeDefinition(name = "Connection Timeout") int connectionTimeout() default DEFAULT_CONNECTION_TIMEOUT;

        @AttributeDefinition(name = "Socket Timeout") int socketTimeout() default DEFAULT_SOCKET_TIMEOUT;

        @AttributeDefinition(name = "Max Connections") int maxConnections() default DEFAULT_MAX_CONNECTIONS;

        @AttributeDefinition(name = "Max Connections per Host") int maxConnectionsPerHost() default DEFAULT_MAX_CONNECTIONS_PER_HOST;

    }

    @Activate
    protected void activate(Configuration config) {
        LOG.info("Activating IGRequestServiceImpl...");
        this.maxConnections = config.maxConnections();
        this.maxConnectionsPerHost = config.maxConnectionsPerHost();
        this.connectionTimeout = config.connectionTimeout();
        this.socketTimeout = config.socketTimeout();
        LOG.info("IGRequestServiceImpl was activated.");
    }

    @Override public CloseableHttpClient getConfiguredHttpClient() {
        HttpClientBuilder builder = httpClientBuilderFactory.newBuilder();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(connectionTimeout).build();
        builder.setDefaultRequestConfig(requestConfig);
        builder.setMaxConnTotal(maxConnections);
        builder.setMaxConnPerRoute(maxConnectionsPerHost);
        return builder.build();
    }

}