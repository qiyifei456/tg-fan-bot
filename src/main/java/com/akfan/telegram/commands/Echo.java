package com.akfan.telegram.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Echo extends SendMessage implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Echo.class);

    public static String PATH = "echo";

    private Update update;

    public Echo(Update update) {
        this.update = update;
    }

    private String buildResponse(String rawText) {
        String[] slice = rawText.split(" ", 2);
        if (slice.length < 2) {
            return "叫我干嘛鸭？（使用方式：/echo [text])";
        }

        return slice[1];
    }

    @Override
    public void execute() {
        super.setText(buildResponse(update.getMessage().getText()));
    }
}
