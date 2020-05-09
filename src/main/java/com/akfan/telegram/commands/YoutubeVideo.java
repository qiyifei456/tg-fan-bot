package com.akfan.telegram.commands;

/**
 * Sample Java code for youtube.channels.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/guides/code_samples#java
 */

import com.akfan.telegram.util.YoutubeUtil;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.common.collect.Lists;
import org.mortbay.util.SingletonList;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.List;

public class YoutubeVideo extends SendMessage implements ICommand, ICallbackable {
    public static String PATH = "content";
    public static String CALL_BACK_LATEST = PATH + "latest";
    public static String CALL_BACK_HOTTEST = PATH + "hottest";
    private YoutubeUtil youtubeUtil = new YoutubeUtil();
    private CallbackQuery optionalCallback;

    public YoutubeVideo(CallbackQuery callBack) {
        this.optionalCallback = callBack;
    }

    @Override
    public void handleCallback() {
        try {
            if (optionalCallback.getData().equals(CALL_BACK_LATEST)) {
                respondLatest();
            }

            if (optionalCallback.getData().equals(CALL_BACK_HOTTEST)) {
                respondHottest();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute() {
        try {
            if (optionalCallback != null) {
                handleCallback();
                return;
            }

            InlineKeyboardMarkup q = new InlineKeyboardMarkup();
            InlineKeyboardButton latest = new InlineKeyboardButton();
            InlineKeyboardButton hottest = new InlineKeyboardButton();

            latest.setText("最新");
            latest.setCallbackData(CALL_BACK_LATEST);

            hottest.setText("最热");
            hottest.setCallbackData(CALL_BACK_HOTTEST);

            List<InlineKeyboardButton> options = Lists.newArrayList(latest, hottest);
            q.setKeyboard(SingletonList.newSingletonList(options));

            super.setReplyMarkup(q);
            super.setText("要看什么？");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void respondLatest() throws IOException {
        YouTube youtubeService = youtubeUtil.getService();

        YouTube.PlaylistItems.List request = youtubeService.playlistItems()
                .list("snippet,contentDetails");
        PlaylistItemListResponse playlistItemListResponse = request
                .setKey(youtubeUtil.API_KEY)
                .setMaxResults(1L)
                .setPlaylistId(youtubeUtil.AK_CHANNEL_PLAYLIST)
                .execute();

        // latest
        PlaylistItem latest = playlistItemListResponse.getItems().get(0);
        String resourceId = latest.getContentDetails().getVideoId();
        String link = youtubeUtil.getYoutubeVideoURL(resourceId);

        super.setText(link);
        super.setReplyToMessageId(null); // unset message id
    }

    private void respondHottest() throws IOException {
        YouTube youtubeService = youtubeUtil.getService();

        YouTube.Search.List search = youtubeService.search().list("id,snippet");
        SearchListResponse searchListResponse =
                search.setKey(youtubeUtil.API_KEY)
                        .setType("video")
                        .setChannelId(youtubeUtil.AK_CHANNEL)
                        .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                        .setOrder("viewCount")
                        .setMaxResults(1L)
                        .execute();

        // most popular
        SearchResult hottest = searchListResponse.getItems().get(0);
        String videoId = hottest.getId().getVideoId();
        String link = youtubeUtil.getYoutubeVideoURL(videoId);

        super.setText(link);
    }

}