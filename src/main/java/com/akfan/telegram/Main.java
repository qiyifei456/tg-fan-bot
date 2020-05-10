package com.akfan.telegram;

import com.akfan.telegram.bot.FanBot;
import com.akfan.telegram.commands.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Bot process started");

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            FanBot bot = new FanBot();
            botsApi.registerBot(bot);
            CommandFactory commandFactory = new CommandFactory();
            SetMyCommands setMyCommands = new SetMyCommands(commandFactory.supportedCmd());
            bot.execute(setMyCommands);

            LOGGER.info("registered bot");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
