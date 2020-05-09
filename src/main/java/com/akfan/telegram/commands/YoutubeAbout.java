package com.akfan.telegram.commands;

/**
 * Sample Java code for youtube.channels.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/guides/code_samples#java
 */

import com.akfan.telegram.util.YoutubeUtil;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;


public class YoutubeAbout extends SendMessage implements ICommand {
    public static String PATH = "about";
    private YoutubeUtil youtubeUtil = new YoutubeUtil();

    @Override
    public void execute() {
        try {

            YouTube youtubeService = youtubeUtil.getService();
            YouTube.Channels.List request = youtubeService.channels()
                    .list("snippet,contentDetails,statistics");

            ChannelListResponse response =
                    request.setId(youtubeUtil.AK_CHANNEL)
                            .setKey(youtubeUtil.API_KEY)
                            .execute();
            Channel channel = response.getItems().get(0);

            super.setText(channel.getSnippet().getDescription());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}