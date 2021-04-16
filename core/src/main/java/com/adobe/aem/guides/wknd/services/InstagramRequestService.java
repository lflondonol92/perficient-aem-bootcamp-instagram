package com.adobe.aem.guides.wknd.services;

import org.apache.http.impl.client.CloseableHttpClient;

public interface InstagramRequestService {

    CloseableHttpClient getConfiguredHttpClient();

}