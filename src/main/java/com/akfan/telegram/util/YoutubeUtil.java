package com.akfan.telegram.util;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class YoutubeUtil {
    public String AK_CHANNEL;
    public String AK_CHANNEL_PLAYLIST;
    public String API_KEY;
    String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    String APPLICATION_NAME;
    String PROPERTIES_FILENAME = "/youtube.properties";
    JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public YoutubeUtil() {
        try {
            InputStream in = YoutubeUtil.class.getResourceAsStream(PROPERTIES_FILENAME);
            Properties properties = new Properties();
            properties.load(in);

            API_KEY = properties.getProperty("youtube.apikey");
            AK_CHANNEL = properties.getProperty("youtube.channelId");

            StringBuilder AKCHANNEL = new StringBuilder(AK_CHANNEL);
            AKCHANNEL.setCharAt(1, 'U');
            AK_CHANNEL_PLAYLIST = AKCHANNEL.toString();

            APPLICATION_NAME = properties.getProperty("youtube.applicationName");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getYoutubeVideoURL(String videoId) {
        return YOUTUBE_URL + videoId;
    }

    public YouTube getService() {
        return new YouTube.Builder(new NetHttpTransport(), JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
            }
        }).setApplicationName(APPLICATION_NAME).build();
    }
}
