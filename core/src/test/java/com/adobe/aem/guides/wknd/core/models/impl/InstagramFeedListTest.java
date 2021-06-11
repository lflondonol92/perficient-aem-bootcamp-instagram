package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.InstagramFeedList;
import com.adobe.aem.guides.wknd.services.InstagramMediaService;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class InstagramFeedListTest {

    private final AemContext ctx = new AemContext();

    @Mock
    private InstagramMediaService igMediaService;
    private String graphQL;

    @BeforeEach
    void setUp() throws Exception {

        ctx.load().json("/com/adobe/aem/guides/wknd/core/models/impl/InstagramFeedListTest.json", "/content");

        graphQL = readFileAsString(this.getClass()
                .getResource("/com/adobe/aem/guides/wknd/core/models/impl/instagram-post.json")
                .getFile());

        MockitoAnnotations.initMocks(this);
        Mockito.when(igMediaService.getPostByURI("https://www.instagram.com/p/CNaaHt1jb2w/")).thenReturn(createJsonObject());
        Mockito.when(igMediaService.getPostByURI("https://www.instagram.com/p/CNogcwMDand/")).thenReturn(createJsonObject());
        Mockito.when(igMediaService.getImageToBase64(Mockito.anyString())).thenReturn( "" );


        ctx.registerService(InstagramMediaService.class, igMediaService,
                org.osgi.framework.Constants.SERVICE_RANKING, Integer.MAX_VALUE);

    }

    @Test
    public void tesHasPosts() {
        final int expected = 2;

        ctx.currentResource("/content/one");
        InstagramFeedList instagram = ctx.request().adaptTo(InstagramFeedList.class);

        assertNotNull(instagram.getPosts());
        final int actual = instagram.getPosts().size();

        assertEquals(expected, actual);
    }

    private JsonObject createJsonObject(){
        JsonElement jsonElement =  JsonParser.parseString(graphQL);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject;
    }

    public static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

}
