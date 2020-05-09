package com.akfan.telegram.bot;

import com.akfan.telegram.commands.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FanBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(FanBot.class);
    private String username;
    private String token;

    public FanBot() {
        try {
            // load bot properties
            InputStream in = FanBot.class.getResourceAsStream("/bot.properties");
            Properties properties = new Properties();
            properties.load(in);

            this.username = properties.getProperty("bot.username");
            this.token = properties.getProperty("bot.token");

            if (username == null || token == null ||username.isEmpty() || token.isEmpty()) {
                throw new IllegalArgumentException("Missing required bot information");
            }

        } catch (IOException| IllegalArgumentException e) {
            LOGGER.error("Failed to initialize bot");
            System.exit(-1);
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        CommandFactory commandFactory = new CommandFactory();
        SendMessage message = commandFactory.getSendMessage(update);
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }
}
