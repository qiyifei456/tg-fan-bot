package com.akfan.telegram.commands;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public class CommandFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandFactory.class);

    public SendMessage getSendMessage(Update update) {
        SendMessage sendMessage = null;
        CallbackQuery callBack = null;
        String cmd;
        Long chatId;
        int messageId;

        if (update.hasMessage() && update.getMessage().hasText()) {
            // normal command
            chatId = update.getMessage().getChatId();
            messageId = update.getMessage().getMessageId();
            sendMessage = instruction(update); // default
            cmd = update.getMessage().getText();
        } else {
            // handle call back
            callBack = update.getCallbackQuery();
            chatId = callBack.getMessage().getChatId();
            messageId = callBack.getMessage().getMessageId();
            cmd = update.getCallbackQuery().getData();
        }

        if (cmd.contains(Echo.PATH)) {
            LOGGER.info("Received echo request");
            sendMessage = new Echo(update);
            ((Echo) sendMessage).execute();

        } else if (cmd.contains(YoutubeAbout.PATH)) {
            LOGGER.info("Received about request");
            sendMessage = new YoutubeAbout();
            ((YoutubeAbout) sendMessage).execute();

        } else if (cmd.contains(YoutubeVideo.PATH)) {
            LOGGER.info("Received video request");
            sendMessage = new YoutubeVideo(callBack);
            ((YoutubeVideo) sendMessage).execute();

        }

        setBasics(sendMessage, chatId, messageId);

        return sendMessage;
    }

    // register command here
    public List<BotCommand> supportedCmd() {
        BotCommand echo = new BotCommand("/" + Echo.PATH, "打个招呼");
        BotCommand about = new BotCommand("/" + YoutubeAbout.PATH, "关于AK");
        BotCommand content = new BotCommand("/" + YoutubeVideo.PATH, "看看视频");

        return Lists.newArrayList(echo, about, content);
    }

    private SendMessage instruction(Update update) {
        SendMessage instruction = new SendMessage();
        setBasics(instruction, update.getMessage().getChatId(),
                update.getMessage().getMessageId());

        StringBuilder cmdBuilder = new StringBuilder();
        for (BotCommand cmd : supportedCmd()) {
            cmdBuilder.append(cmd.getCommand());
            cmdBuilder.append(" ");
            cmdBuilder.append(cmd.getDescription());
            cmdBuilder.append("\n");
        }

        instruction.setParseMode("HTML");
        instruction.setText(
                "<b>AK's Fan</b> Beta\n" +
                "<i>By anonymous fan</i>\n\n" +
                "<pre>我可以干什么？</pre>\n" +
                cmdBuilder.toString()
        );

        return instruction;
    }

    private void setBasics(SendMessage message, Long chatId, int replyId) {
        message.setChatId(chatId);
        message.setReplyToMessageId(replyId);
    }
}
